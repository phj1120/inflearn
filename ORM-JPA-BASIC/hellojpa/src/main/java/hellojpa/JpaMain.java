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
            Team team = new Team();
            team.setName("mjc");
            em.persist(team);

            Locker locker = new Locker();
            locker.setName("A");
            em.persist(locker);

            Member member = new Member();
            member.setUsername("phj");
            member.setTeam(team);
            member.deploymentLocker(locker);
            em.persist(member);

            Movie movie = new Movie();
            movie.setName("탑건");
            movie.setActor("톰쿠르즈");
            movie.setDirector("phj");
            movie.setPrice(12000);
            em.persist(movie);
            System.out.println("movie = " + movie);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("findMovie = " + findMovie);

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
