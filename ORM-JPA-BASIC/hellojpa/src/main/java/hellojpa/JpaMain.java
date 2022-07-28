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
            Address address1 = new Address("CITY", "STREET", "ZIPCODE");
            Member member1 = new Member("PHJ", new Period(), address1);
            em.persist(member1);

            em.find(Member.class, member1.getId());
            System.out.println("member1.getAddress().getCity() = " + member1.getAddress().getCity());

            Address copyAddress = new Address("NEWCITY", address1.getStreet(), address1.getZipcode());
            member1.setAddress(copyAddress);

            em.flush();
            em.clear();

            em.find(Member.class, member1.getId());
            System.out.println("member1.getAddress().getCity() = " + member1.getAddress().getCity());

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
