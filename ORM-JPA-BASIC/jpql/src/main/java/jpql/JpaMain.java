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
                member.setUsername("phj" + i);
                member.setAge(i);
                member.changeTeam(team2);
                member.setType(MemberType.USER);
                em.persist(member);
            }

            em.flush();
            em.clear();

            String query;
            System.out.println("=====================================================================");
            query = "select m.username, 'HELLO' from Member m " +
                    "where m.type = :userType";
            System.out.println(query);
            List<Object[]> resultList1 = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : resultList1) {
                System.out.println("object[0] = " + objects[0]);
                System.out.println("object[1] = " + objects[1]);
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
