package study.querydsl.repository.member;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;

import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

/**
 * MemberRepository > JpaRepository, MemberRepositoryCustom
 * MemberRepository 에서 MemberRepositoryImpl 에 구현된 소스가 실행 됨.
 * 구현체를 Bean 에 등록하지도 않았는데 어떻게?
 *  => 해당 기능을 사용자 정의 리포지토리라 하고, Spring Data Jpa 가 인식해서 스프링 Bean 으로 등록.
 *  Repository 인터페이스 이름 + Impl (ex. MemberRepositoryImpl)
 *  사용자 정의 인터페이스명 + Impl (ex. MemberRepositoryCustomImpl) // Spring Data Jpa 2.x 이후 부터
 *  postfix 바꾸고 싶으면, repositoryImplementationPostfix 값 변경.
 * */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageLoe(condition.getAgeLoe()),
                        ageGoe(condition.getAgeGoe())
                )
                .fetch();
    }

    private Predicate usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    private Predicate teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private Predicate ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

}
