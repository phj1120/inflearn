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
            member1.setUsername("member1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            em.persist(member3);

            member1.addFriend(member2);
            member1.addFriend(member3);

            em.flush();
            em.clear();

            Member findMember1 = em.find(Member.class, member1.getId());
            List<Member> requesterMember1 = findMember1.getRequesterMember();


            Member findMember2 = em.find(Member.class, member2.getId());
            List<Member> targetMember2 = findMember2.getTargetMember();


            Member findMember3 = em.find(Member.class, member3.getId());
            List<Member> targetMember3 = findMember3.getTargetMember();

            System.out.println("MEMBER1가 친구 추가한 Member");
            for (Member member : requesterMember1) {
                System.out.println("member = " + member);
            }

            System.out.println("MEMBER2를 친구 추가한 Member");
            for (Member member : targetMember2) {
                System.out.println("member = " + member);
            }

            System.out.println("MEMBER3를 친구 추가한 Member");
            for (Member member : targetMember3) {
                System.out.println("member = " + member);
            }
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
