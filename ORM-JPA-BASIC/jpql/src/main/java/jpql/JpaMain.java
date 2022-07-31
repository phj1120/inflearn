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
            Team team = new Team();
            team.setName("mjc");
            em.persist(team);

            Member member = new Member();
            member.setUsername("phj");
            member.setAge(25);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();


            TypedQuery<Member> typeQuery = em.createQuery("select m from Member m", Member.class);

            Query query = em.createQuery("select m.username, m.age from Member m");

            List<Member> resultList = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            for (Member m : resultList) {
                System.out.println("member = " + m);
            }

            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "phj")
                    .getSingleResult();
            System.out.println("singleResult.getUsername() = " + singleResult.getUsername());

            em.createQuery("select m from Member m", Member.class);
            em.createQuery("select m.team from Member m", Team.class);
            em.createQuery("select m.team from Member m join m.team t", Team.class);

            em.createQuery("select o.address from Order o", Address.class);

            List<Query> resultList1 = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();
            Object o = resultList1.get(0);
            Object[] result1 = (Object[]) o;
            System.out.println("username = " + result1[0]);
            System.out.println("age = " + result1[1]);

            List<Object[]> resultList2 = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();
            Object[] result2 = resultList2.get(0);
            System.out.println("username = " + result2[0]);
            System.out.println("age = " + result2[1]);

            List<MemberDTO> resultList3 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m")
                    .getResultList();
            MemberDTO memberDTO = resultList3.get(0);
            System.out.println("username = " + memberDTO.getUsername());
            System.out.println("age = " + memberDTO.getAge());

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
