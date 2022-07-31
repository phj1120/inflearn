package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for (int i = 1; i <= 100; i++) {
                Member member = new Member();
                member.setUsername("phj" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> resultList1 = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(10)
                    .setMaxResults(10)
                    .getResultList();

            for (Member m : resultList1) {
                System.out.println("m.getUsername() = " + m.getUsername());
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
