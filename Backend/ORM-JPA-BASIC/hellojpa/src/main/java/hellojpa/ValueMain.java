package hellojpa;

public class ValueMain {

    public static void main(String[] args) {
        int a = 10;
        int b = 10;
        System.out.println("a==b = " + (a == b));

        Address address1 = new Address("CITY", "STREET", "ZIPCODE");
        Address address2 = new Address("CITY", "STREET", "ZIPCODE");

        System.out.println("address1 == address2 = " + (address1 == address2));
        System.out.println("address1.equals(address2) = " + address1.equals(address2));
    }
}
