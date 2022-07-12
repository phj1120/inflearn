package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;

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

            // 데이터 중심 설계
            // 객체 설계를 관계형 DB 에 맞춤
            // 객체 그래프 탐색(.)이 불가능
            Order order = em.find(Order.class, 1L);
            Long memberId = order.getMemberId();
            Member member = em.find(Member.class, memberId);

//            객체 지향적으로 사용한다면?
//            Order order = em.find(Order.class, 1L);
//            Member member = order.getMember();


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
