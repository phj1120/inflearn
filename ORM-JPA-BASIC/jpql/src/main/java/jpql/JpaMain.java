package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("mjc");
            em.persist(team);

            Member member = new Member();
            member.setUsername("phj");
            member.setAge(25);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();


            TypedQuery<Member> typeQuery = em.createQuery("select m from Member m", Member.class);

            Query query = em.createQuery("select m.username, m.age from Member m");

            List<Member> resultList = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            for (Member m : resultList) {
                System.out.println("member = " + m);
            }

            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "phj")
                    .getSingleResult();
            System.out.println("singleResult.getUsername() = " + singleResult.getUsername());

            tx.commit();
        } catch (Exception e) {
            System.out.println("[Error Message] : " + e);
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
