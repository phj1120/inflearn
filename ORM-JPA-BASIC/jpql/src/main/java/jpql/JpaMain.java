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
            Team team1 = new Team();
            team1.setName("mjc");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("4this");
            em.persist(team2);

            for (int i = 0; i <= 4; i++) {
                Member member = new Member();
                member.setUsername("phj" + i);
                member.setAge(i);
                member.changeTeam(team1);
                em.persist(member);
            }

            for (int i = 5; i < 10; i++) {
                Member member = new Member();
                member.setUsername("phj" + i);
                member.setAge(i);
                member.changeTeam(team2);
                em.persist(member);
            }

            em.flush();
            em.clear();

            String query;
            System.out.println("=====================================================================");
            query = "select m from Member m where m.age > (select avg(m2.age) from Member m2)";
            System.out.println(query);
            List<Member> resultList1 = em.createQuery(query, Member.class)
                    .getResultList();
            for (Member m : resultList1) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            System.out.println("=====================================================================");

            System.out.println("=====================================================================");
            query = "select m from Member m where exists (select t from m.team t where t.name='mjc')";
            System.out.println(query);
            List<Member> resultList2 = em.createQuery(query, Member.class)
                    .getResultList();
            for (Member m : resultList2) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            System.out.println("=====================================================================");

            System.out.println("=====================================================================");
            query = "select m from Member m where m.team = any (select t from Team t)";
            System.out.println(query);
            List<Member> resultList3 = em.createQuery(query, Member.class)
                    .getResultList();
            for (Member m : resultList3) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            System.out.println("=====================================================================");


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
