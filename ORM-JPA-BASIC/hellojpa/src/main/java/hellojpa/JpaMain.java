package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team1 = new Team();
            team1.setName("mjc1");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("mjc2");
            em.persist(team2);

            Member member = new Member();
            member.setUsername("phj");
            member.setTeam(team1);
            em.persist(member);

            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();

            System.out.println("findTeam.getName() = " + findTeam.getName());

            findMember.setTeam(team2);

            findMember = em.find(Member.class, member.getId());
            findTeam = findMember.getTeam();

            System.out.println("findTeam.getName() = " + findTeam.getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
