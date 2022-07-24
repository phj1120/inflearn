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

            // Member 에서 친구 관계를 수정하고 싶었으나. 그러면 Member 객체 수정시 Friend 테이블 수정 되는 현상 발생
            // 유지보수가 어려워 지므로 객체 지향적으로 손해를 보더라도 외래키가 있는 곳에서 수정되도록 하라는 영한님 말씀을 듣고 이렇게 했음
            Friendship friendship1to2 = new Friendship(member1, member2);
            Friendship friendship2to1 = new Friendship(member2, member1);

            Friendship friendship1to3 = new Friendship(member1, member3);
            Friendship friendship3to1 = new Friendship(member3, member1);

            em.persist(friendship1to2);
            em.persist(friendship2to1);
            em.persist(friendship1to3);
            em.persist(friendship3to1);

            em.flush();
            em.clear();

            Member findMember1 = em.find(Member.class, member1.getId());
            List<Member> member1Friends = findMember1.selectFriends();
            System.out.println("MEMBER1's FRIENDS");
            for (Member member1Friend : member1Friends) {
                System.out.println("member1Friend = " + member1Friend);
            }

            Member findMember2 = em.find(Member.class, member2.getId());
            List<Member> member2Friends = findMember2.selectFriends();
            System.out.println("MEMBER2's FRIENDS");
            for (Member member2Friend : member2Friends) {
                System.out.println("member2Friend = " + member2Friend);
            }

            Member findMember3 = em.find(Member.class, member3.getId());
            List<Member> member3Friends = findMember3.selectFriends();
            System.out.println("MEMBER3's FRIENDS");
            for (Member member3Friend : member3Friends) {
                System.out.println("member3Friend = " + member3Friend);
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
