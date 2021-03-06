package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /*
     * 주문
     * */
    @Transactional
    public long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // order 가 CascadeType.ALL 이기 때문에 한 번 만 save 해주면 됨

        return order.getId();
    }

    /*
     * 주문 취소
     * */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();
    }

    /*
     * 검색
     *
     * 이렇게 위임만 하는 경우 controller 에서 repository 를 불러서 사용 하는 것도 고려.
     * 이런거에 너무 얽메이지 말고 선택 하면 됨
     * */
    public List<Order> findOrders(OrderSearch orderSearch) {

        return orderRepository.findAllByCriteria(orderSearch);
    }

}
