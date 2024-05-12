package study.querydsl.entity;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 10, teamB);
        Member member4 = new Member("member4", 20, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();
        log.info("영속성 컨텍스트, 캐시 초기화 해 이후론 실제로 쿼리 나감.");

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }

    @DisplayName("Fetch 를 걸면 실제 값이 필요할때 조회해온다. ? Debug 를 단계별로 확인해야 쿼리가 날라가는데 이유는 아직 모르겠음.")
    @Test
    void jpaStudy2() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        em.persist(member1);

        em.flush();
        em.clear();

        String sql = "select m from Member m where m.id = " + member1.getId();
        Member findMember1 = em.createQuery(sql, Member.class).getResultList().stream().findFirst().get();
        log.info("------------findMember1: {}", findMember1);
        Team findMember1Team = findMember1.getTeam();
        log.info("------------findMember1Team: {}", findMember1Team);
        Long findMember1TeamId = findMember1Team.getId();
        log.info("------------findMember1TeamId: {}", findMember1TeamId);
    }

    @DisplayName("영속성 컨테스트를 비울 때, 변경 감지를 통해 값을 Update 한다.")
    @Test
    void jpaStudy3() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        em.persist(member1);

        member1.changeTeam(teamB);
        em.flush();
        em.clear();

        String sql = "select m from Member m where m.id = " + member1.getId();
        Member findMember1 = em.createQuery(sql, Member.class).getResultList().stream().findFirst().get();
        Assertions.assertThat(findMember1.getId()).isEqualTo(member1.getId());
        Assertions.assertThat(findMember1.getTeam().getId()).isEqualTo(teamB.getId());
    }

    @DisplayName("Mapped By 로 되어 있는 값은 바뀌어도 실제 데이터에 영향이 가지 않음.")
    @Test
    void jpaStudy4() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        em.persist(member1);

        teamA.getMembers().clear();
        em.flush();
        em.clear();

        String sql = "select m from Member m where m.id = " + member1.getId();
        Member findMember1 = em.createQuery(sql, Member.class).getResultList().stream().findFirst().get();
        Assertions.assertThat(findMember1.getId()).isEqualTo(member1.getId());
        Assertions.assertThat(findMember1.getTeam().getId()).isEqualTo(teamA.getId());
    }

}