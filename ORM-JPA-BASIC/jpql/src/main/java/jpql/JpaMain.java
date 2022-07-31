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
            Team team = new Team();
            team.setName("mjc2");
            em.persist(team);

            Member member = new Member();
            member.setUsername("phj");
            member.setAge(25);
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            String query;
            System.out.println("=====================================================================");
            query = "select m from Member m join m.team t";
            System.out.println(query);
            List<Member> resultList1 = em.createQuery(query, Member.class)
                    .getResultList();
            for (Member m : resultList1) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            System.out.println("=====================================================================");


            query = "select m from Member m left join m.team t";
            System.out.println(query);
            List<Member> resultList2 = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member m : resultList2) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            System.out.println("=====================================================================");


            query = "select m from Member m left join m.team t on t.name = :teamName";
            System.out.println(query);
            List<Member> resultList3 = em.createQuery(query, Member.class)
                    .setParameter("teamName", "mjc")
                    .getResultList();

            for (Member m : resultList3) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            System.out.println("=====================================================================");


            query = "select count(m) from Member m join m.team t on t.name = :teamName";
            System.out.println(query);
            Long innerJoinResult = em.createQuery(query, Long.class)
                    .setParameter("teamName", "mjc")
                    .getSingleResult();
            System.out.println("innerJoinResult = " + innerJoinResult);

            query = "select count(m) from Member m left join m.team t on t.name = :teamName";
            System.out.println(query);
            Long outerJoinResult = em.createQuery(query, Long.class)
                    .setParameter("teamName", "mjc")
                    .getSingleResult();
            System.out.println("outerJoinResult = " + outerJoinResult);

            System.out.println("=====================================================================");

            query = "select count(m) from Member m, Team t where m.username = t.name";
            System.out.println(query);
            Long singleResult = em.createQuery(query, Long.class).getSingleResult();
            System.out.println(singleResult);
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
