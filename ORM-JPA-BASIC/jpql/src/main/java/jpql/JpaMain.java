package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        System.out.println("=====================================================================");
        try {
            Team teamA = new Team();
            teamA.setName("TEAM_A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("TEAM_B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            System.out.println("=====================================================================");
            String query = "select m from Member m";
            System.out.println(query);
            List<Member> resultList1 = em.createQuery(query, Member.class).getResultList();

            for (Member member : resultList1) {
                System.out.println("member Name = " + member.getUsername() + " team Name = " + member.getTeam().getName());
            }

            em.flush();
            em.clear();

            System.out.println("=====================================================================");
            query = "select m from Member m inner join m.team";
            System.out.println(query);
            List<Member> resultList2 = em.createQuery(query, Member.class).getResultList();

            for (Member member : resultList2) {
                System.out.println("member Name = " + member.getUsername() + " team Name = " + member.getTeam().getName());
            }

            em.flush();
            em.clear();

            System.out.println("=====================================================================");
            query = "select m from Member m join fetch m.team";
            System.out.println(query);
            List<Member> resultList3 = em.createQuery(query, Member.class).getResultList();

            for (Member member : resultList3) {
                System.out.println("member Name = " + member.getUsername() + " team Name = " + member.getTeam().getName());
            }

            em.flush();
            em.clear();

            System.out.println("중복 제거 =====================================================================");
            query = "select distinct t from Team t join fetch t.members";
            System.out.println(query);
            List<Team> resultList4 = em.createQuery(query, Team.class).getResultList();

            System.out.println("select count : " + resultList4.size());
            for (Team team : resultList4) {
                System.out.println("team Name = " + team.getName());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
                System.out.println();
            }

            em.flush();
            em.clear();

            System.out.println("페치 조인과 일반 조인의 차이=====================================================================");
            query = "select m from Member m join m.team";
            System.out.println(query);
            List<Member> resultList5 = em.createQuery(query, Member.class).getResultList();

            System.out.println("select count : " + resultList5.size());

            for (Member member : resultList5) {
                System.out.println("member = " + member + " team = " + member.getTeam());
            }

            em.flush();
            em.clear();

            System.out.println("=====================================================================");
            query = "select m from Member m join fetch m.team";
            System.out.println(query);
            List<Member> resultList6 = em.createQuery(query, Member.class).getResultList();

            System.out.println("select count : " + resultList6.size());

            for (Member member : resultList6) {
                System.out.println("member = " + member + " team = " + member.getTeam());
            }

            tx.commit();
        } catch (Exception e) {
            System.out.println("[Error Message] : " + e);
            tx.rollback();
        } finally {
            System.out.println("=====================================================================");
            em.close();
        }
        emf.close();
    }
}
