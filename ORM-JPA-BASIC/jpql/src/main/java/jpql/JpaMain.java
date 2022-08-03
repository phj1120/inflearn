package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        System.out.println("=====================================================================");
        try {
            Team team1 = new Team();
            team1.setName("mjc");
            em.persist(team1);

            Member member1 = new Member();
            member1.setUsername("phj");
            member1.setAge(25);
            member1.changeTeam(team1);
            member1.setType(MemberType.ADMIN);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("park");
            member2.setAge(25);
            member2.changeTeam(team1);
            member2.setType(MemberType.ADMIN);
            em.persist(member2);

            em.flush();
            em.clear();

            String query;
            System.out.println("상태 필드 =====================================================================");
            query = "select m.username from Member m";
            System.out.println(query);
            List<String> result1 = em.createQuery(query, String.class)
                    .getResultList();
            for (String username : result1) {
                System.out.println("username = " + username);
            }

            System.out.println("단일 값 연관 필드 =====================================================================");
            query = "select m.team from Member m";
            System.out.println(query);
            List<Team> result2 = em.createQuery(query, Team.class)
                    .getResultList();
            for (Team team : result2) {
                System.out.println("Team = " + team);
            }

            System.out.println();
            query = "select t from Member m join m.team t";
            System.out.println(query);
            List<Team> result3 = em.createQuery(query, Team.class)
                    .getResultList();
            for (Team team : result3) {
                System.out.println("Team = " + team);
            }

            System.out.println();
            query = "select m from Member m join m.team t";
            System.out.println(query);
            List<Member> result4 = em.createQuery(query, Member.class).getResultList();
            for (Member member : result4) {
                System.out.println("member = " + member);
            }

            System.out.println("컬렉션 값 연관 필드 =====================================================================");
            query = "select t.members from Team t";
            System.out.println(query);
            List<Collection> result5 = em.createQuery(query, Collection.class).getResultList();
            for (Object o : result5) {
                System.out.println("o = " + o);
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
