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
            query = "select " +
                    "case when m.age <= 10 then '학생요금' " +
                    "     when m.age >= 60 then '경로요금' " +
                    "     else '일반요금' " +
                    "end " +
                    "from Member m";
            List<String> resultList1 = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList1) {
                System.out.println("s = " + s);
            }
            System.out.println("=====================================================================");

            query = "select coalesce(m.username, '이름 없는 회원') as username " +
                    "from Member m";
            List<String> resultList2 = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList2) {
                System.out.println("s = " + s);
            }
            System.out.println("=====================================================================");

            query = "select nullif(m.username, :username) " +
                    "from Member m";
            List<String> resultList3 = em.createQuery(query, String.class)
                    .setParameter("username", "phj0")
                    .getResultList();

            for (String s : resultList3) {
                System.out.println("s = " + s);
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
