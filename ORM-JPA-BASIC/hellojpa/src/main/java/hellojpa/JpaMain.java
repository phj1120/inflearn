package hellojpa;

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

            System.out.println("------------");
            Team findTeam = em.find(Team.class, team.getId());
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findTeam = " + findTeam);
            System.out.println("findMember = " + findMember);
            System.out.println("------------");

            tx.commit();
        } catch (Exception e) {
            System.out.println("e = " + e);
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
