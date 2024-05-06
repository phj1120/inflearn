package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
@Slf4j
public class QueryDslBasicTest {

    // 멀티 쓰레드 환경에서도 동시성 문제 없이 사용 가능하도록 설계 됨.
    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    void init() {
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void startJPQL() {
        // 1. member1 찾기
        String qlString = "select m from Member m " +
                "where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void startQueryDsl() {
        // 아래와 같이 Q domain 을 사용할 수 있지만, static 으로 선언해 사용하는 것 권장
        // QMember m = new QMember("m"); // jpql 의 alias 사용, 같은 테이블을 조인해야할 경우 선언해서 사용할 것.
        // QMember m = QMember.member;
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /*
     * and 인 경우 그냥 여러개 넘겨주면 됨
     * */
    @Test
    void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"),
                        (member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void resultFetchTest() {
//        List<Member> fetch = queryFactory
//                .selectFrom(member)
//                .fetch();

//        Member fetchOne = queryFactory
//                .selectFrom(member)
//                .fetchOne();

//        Member fetchFirst = queryFactory
//                .selectFrom(member)
//                .fetchFirst();

        QueryResults<Member> memberQueryResults = queryFactory
                .selectFrom(member)
                .fetchResults();

        queryFactory.selectFrom(member)
                .fetchCount();
    }

    @Test
    void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> results = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = results.get(0);
        Member member6 = results.get(1);
        Member memberNull = results.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    void paging() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result).hasSize(2);
    }

    @Test
    void aggregation() {
        // 실무에서는 DTO 로 조회
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.min(),
                        member.age.max()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
    }

    @Test
    void group() {
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.id)
                .fetch();
        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    // TeamA 에 소속된 모든 회원
    @Test
    void join() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    // 팀 이름과 회원 이름이 같은 회원 조회
    @Test
    void thetaJoin() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result = queryFactory.select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA 인 팀만 조인, 회원은 모두 조회
     * SQL: select *
     * from member m
     * left join team t on m.team_id = t.team_id and t.name = 'teamA'
     * JPQL: select m, t from member m left join m.team t on t.name = 'teamA'
     */
    @Test
    void join_on_filtering() {
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();
        // + inner join 인 이면, on 절에 거는거나 where 절에 거는거나 동일함.
        //   where 절이 더 익숙하니 where 절 사용하기.
        for (Tuple tuple : result) {
            log.info("tuple: {}", tuple);
        }
    }


    /**
     * 연관관계 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     * <p>
     * SQL: select *
     * from member m
     * left join team t on m.username = t.name
     */
    @Test
    void join_on_no_relation() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            log.info("tuple: {}", tuple);
        }
    }

    /**
     * JPQL 로 연관관계가 있는 대상을 가지고 조인한다는 개념이 익숙하지 않음
     * inner join 을 주로 써서, 어떤 테이블을 기준 테이블으로 잡아야 하는지 익숙하지 않음.
     **/
    @Test
    void join_use_on_has_relation() {
        log.info("------- 기준 테이블: Member / left Join -------");
        List<Tuple> referenceMemberAndJustLeftJoin = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .fetch();

        log.info("------- 기준 테이블: Member / left Join On -------");
        List<Tuple> referenceMemberAndLeftJoinOn = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.team.id.eq(team.id))
                .fetch();

        log.info("------- 기준 테이블: Team / left Join -------");
        List<Tuple> referenceTeamAndJustLeftJoin = queryFactory
                .select(member, team)
                .from(team)
                .leftJoin(team.members, member)
                .fetch();

        log.info("------- 기준 테이블: Team / left Join On -------");
        List<Tuple> referenceTeamAndLeftJoinOn = queryFactory
                .select(member, team)
                .from(team)
                .leftJoin(member).on(member.team.id.eq(team.id))
                .fetch();

        for (int i = 0; i < referenceMemberAndJustLeftJoin.size(); i++) {
            log.info("{} - 기준 테이블: Member / left Join    : {}", i, referenceMemberAndJustLeftJoin.get(i));
            log.info("{} - 기준 테이블: Member / left Join On : {}", i, referenceMemberAndLeftJoinOn.get(i));
            log.info("{} - 기준 테이블: Team   / left Join    : {}", i, referenceTeamAndJustLeftJoin.get(i));
            log.info("{} - 기준 테이블: Team   / left Join On : {}", i, referenceTeamAndLeftJoinOn.get(i));
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    void fetchJoinNo() {
        // fetch 조인 테스트시 영속성 컨텍스트 비우고 진행
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치조인 미적용").isFalse();
    }

    @Test
    void fetchJoinUse() {
        // fetch 조인 테스트시 영속성 컨텍스트 비우고 진행
        em.flush();
        em.clear();

        // fetch join 관련 해서는 jpa 기본편, 활용편 2편 참고
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치조인 적용").isTrue();
    }

    /**
     * 나이가 가장 많은 회원 조회
     */
    @Test
    void subQueryMax() {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    /**
     * 나이가 평균 이상인 회원
     */
    @Test
    void subQueryGoe() {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }


    @Test
    void subQueryIn() {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    @Test
    void selectSubQuery() {
        QMember memberSub = new QMember("memberSub");
        List<Tuple> result = queryFactory
                .select(
                        member.username,
                        select(memberSub.age.avg())
                                .from(memberSub)
                )
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            log.info("tuple: {}", tuple);
        }
    }

    @Test
    void basicCase() {
        List<String> fetch = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("열살")
                        .otherwise("기타")
                ).from(member)
                .fetch();

        for (String str : fetch) {
            log.info("str: {}", str);
        }
    }

    @Test
    void complexCase() {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        for (String s : result) {
            log.info("s: {}", s);
        }
    }

    @Test
    void constant() {
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            log.info("tuple: {}", tuple);
        }
    }

    @Test
    void concat() {
        // username_age
        String result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(result).isEqualTo("member1_10");
    }

    @Test
    void simpleProjection() {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            log.info("username: {}", username);
            log.info("age: {}", age);
        }
    }

    /**
     * 순수 JPA 에서 DTO 조회
     * */
    @Test
    void findDtoByJPQL() {
        List<MemberDto> result = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            log.info("memberDto: {}", memberDto);
        }
    }

    // getter setter 필요
    @Test
    void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            log.info("memberDto: {}", memberDto);
        }
    }

    // getter setter 필요 없음
    @Test
    void findDtoByFields() {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            log.info("memberDto: {}", memberDto);
        }
    }

    @Test
    void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            log.info("memberDto: {}", memberDto);
        }
    }

    /**
     * dto 의 필드와 가져온 이름이 다를때, as 로 이름 지정
     * */
    @Test
    void findUserDtoByFields() {
        QMember memberSub = new QMember("memberSub");

        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(member.age, "age"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(memberSub.age.max())
                                        .from(memberSub), "age")
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            log.info("userDto: {}", userDto);
        }
    }


    /**
     * findDtoByConstructor 와 비슷하지만,
     * 생성자보다 값을 많이 넣어도, 컴파일 시점에서 오류를 잡을 수 있음.
     * + 안전은 한데,
     * */
    @Test
    void findDtoByQueryProjection() {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            log.info("memberDto: {}", memberDto);
        }
    }

    @Test
    void dynamicQueryByBooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result).hasSize(1);
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }
        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test
    void dynamicQueryByWhereParam() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result).hasSize(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCond), ageCondEq(ageCond))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
        if (usernameCond == null) {
            // queryDsl 에서 where 절에 넘어간게 null 이면 무시.
            return null;
        }
        return member.username.eq(usernameCond);
    }

    private BooleanExpression ageCondEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    /**
     * 공통적으로 사용되는 조건을 메서드로 빼두고 공통으로 사용할 수 있음.(서비스 가능 여부, 사용 가능한 쿠폰)
     * 가독성을 높일 수 있음
     * */
    @Test
    void dynamicQueryByWhereParamComposable() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(memberCondEq(usernameParam, ageParam))
                .fetch();

        List<MemberDto> memberDtos = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .where(memberCondEq(usernameParam, ageParam))
                .fetch();

        List<UserDto> userDtos = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        member.age))
                .from(member)
                .where(memberCondEq(usernameParam, ageParam))
                .fetch();

        assertThat(members).extracting("username").containsExactly(usernameParam);
        assertThat(members).extracting("age").containsExactly(ageParam);
        assertThat(memberDtos).extracting("username").containsExactly(usernameParam);
        assertThat(memberDtos).extracting("age").containsExactly(ageParam);
        assertThat(userDtos).extracting("name").containsExactly(usernameParam);
        assertThat(userDtos).extracting("age").containsExactly(ageParam);
    }

    private BooleanExpression memberCondEq(String usernameCond, Integer ageCond) {
        return member.id.eq(member.id) // Null 체크 필요하다길래 1=1 처럼 항상 참인거 일단 넣어둠.
                .and(usernameEq(usernameCond))
                .and(ageCondEq(ageCond));
    }
}

