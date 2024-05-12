package study.querydsl.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Test
    void basicTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result = memberJpaRepository.findAll();
        assertThat(result).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsername(member.getUsername());
        assertThat(result2).containsExactly(member);
    }

    @Test
    void basicQuerydslTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result = memberJpaRepository.findAll_QueryDsl();
        assertThat(result).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsername_Querydsl(member.getUsername());
        assertThat(result2).containsExactly(member);
    }

    @Test
    void searchCondition() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamJpaRepository.save(teamA);
        teamJpaRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);

        MemberSearchCondition searchCond = new MemberSearchCondition();
        searchCond.setAgeGoe(35);
        searchCond.setAgeLoe(40);
        searchCond.setTeamName("teamB");

        List<MemberTeamDto> memberTeamDtosByBuilder = memberJpaRepository.searchByBuilder(searchCond);
        assertThat(memberTeamDtosByBuilder).extracting("username").containsExactly("member4");

        List<MemberTeamDto> memberTeamDtosByWhere = memberJpaRepository.searchByWhere(searchCond);
        assertThat(memberTeamDtosByWhere).extracting("username").containsExactly("member4");
    }
}