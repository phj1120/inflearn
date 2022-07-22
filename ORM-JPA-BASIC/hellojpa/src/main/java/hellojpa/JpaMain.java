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
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("phj");
            em.persist(member2);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember.getClass() = " + findMember.getClass());
            System.out.println("findMember = " + findMember);
            System.out.println("findMember.getClass() = " + findMember.getClass());

            Member refMember = em.getReference(Member.class, member2.getId());
            System.out.println("refMember.getClass() = " + refMember.getClass());
            System.out.println("refMember = " + refMember);
            System.out.println("refMember.getClass() = " + refMember.getClass());

            System.out.println("findMember " + (findMember instanceof Member));
            System.out.println("refMember " + (refMember instanceof Member));

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
