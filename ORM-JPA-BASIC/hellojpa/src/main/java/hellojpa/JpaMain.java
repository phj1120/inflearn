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
            Member member1 = new Member();
            member1.setUsername("phj");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("phj");
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("phj");
            em.persist(member3);

            member2.setFriend(member1);
            member3.setFriend(member1);

            em.flush();
            em.clear();

            Member findMember1 = em.find(Member.class, member1.getId());
            List<Member> member1Friends = findMember1.getFriends();
            System.out.println("member1Friends = " + member1Friends);

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
