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
                member.setType(MemberType.ADMIN);
                em.persist(member);
            }

            for (int i = 5; i < 10; i++) {
                Member member = new Member();
                member.setAge(i);
                member.changeTeam(team2);
                member.setType(MemberType.USER);
                em.persist(member);
            }

            em.flush();
            em.clear();

            String query;
            System.out.println("=====================================================================");
            query = "select 'a' || 'b' from Member m";
            query = "select concat('a', 'b') from Member m";
            query = "select substring(m.username, 0, 3) from Member m";

            List<String> resultList1 = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList1) {
                System.out.println("s = " + s);
            }

            System.out.println("=====================================================================");
            query = "select function('group_concat', m.username) from Member m";
            query = "select group_concat(m.username) from Member m";

            List<String> resultList2 = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList2) {
                System.out.println("s = " + s);
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
