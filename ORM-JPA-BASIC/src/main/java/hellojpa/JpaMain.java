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
            Member member1 = new Member();
            member1.setUsername("phj");

            Member member2 = new Member();
            member2.setUsername("phj");

            Member member3 = new Member();
            member3.setUsername("phj");

            System.out.println("==== Before persist ====");
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            System.out.println("==== After persist ====");

            System.out.println("==== Before commit ====");
            tx.commit();
            System.out.println("==== After commit ====");

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
