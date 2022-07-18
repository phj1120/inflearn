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
            member.changeTeam(team);
            em.persist(member);

            System.out.println("------------");
            List<Member> members = team.getMembers();
            System.out.println("members = " + members);
            System.out.println(member);
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
