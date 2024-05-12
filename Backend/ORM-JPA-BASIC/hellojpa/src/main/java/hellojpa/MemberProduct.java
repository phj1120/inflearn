package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberProduct {

    @Id
    @GeneratedValue
    @Column(name = "ORDERS_ID")
    private String id; // PK 는 의미 없는 값을 하는 것을 추천.

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private Long orderAmount;
    private LocalDateTime orderDate;
}
