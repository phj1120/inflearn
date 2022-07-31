package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            List<Member> result1 = em.createQuery("select m from Member m").getResultList();
            System.out.println("select m from Member m = " + result1);

            List<Team> result2 = em.createQuery("select m.team from Member m", Team.class).getResultList();
            System.out.println("select m.team from Member m = " + result2);

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
