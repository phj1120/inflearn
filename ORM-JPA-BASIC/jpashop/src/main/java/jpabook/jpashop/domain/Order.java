package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;


// DB 테이블 안까고 객체 보고 바로 사용할 수 있도록,
// 관례가 있다면 적지 않고, 표시할 수 있는 부분은 적는 편
// Spring boot 와 같이 쓰면 orderDate 가 order_date 으로.
@Entity
@Table(name = "orders") // order by 예약어 때문에

public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId; // DB 는 MEMBER_ID, member_id 애매한건 직접 매핑
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
