package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("phj");
            member.setHomeAddress(new Address("homeCity", "STREET", "ZIPCODE"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new Address("OLD1", "STREET", "ZIPCODE"));
            member.getAddressHistory().add(new Address("OLD2", "STREET", "ZIPCODE"));

            System.out.println("==========================");
            em.persist(member);

            em.flush();

            System.out.println("==========================");

            System.out.println("================== 조회 ==================");
            Member findMember = em.find(Member.class, member.getId());

            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) {
                System.out.println("address = " + address);
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }

            System.out.println("================== 수정 ==================");

            // homeCity -> newCity
            Address homeAddress = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", homeAddress.getStreet(), homeAddress.getZipcode()));

            // 치킨 -> 한식
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            // OLD1 -> newCity
            findMember.getAddressHistory().remove(new Address("OLD1", "STREET", "ZIPCODE"));
            findMember.getAddressHistory().add(new Address("newCity", "STREET", "ZIPCODE"));

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
