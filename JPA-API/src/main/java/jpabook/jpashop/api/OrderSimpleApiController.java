package jpabook.jpashop.api;

import jdk.dynalink.linker.LinkerServices;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    //    @JsonIgnore 를 적절히 이용해 무한 루프를 없앨 수 있으나
//    연관관계가 있는 엔티티의 경우 지연로딩으로 프록시가 존재함
//    jackson 은 프록시를 어떻게 json 으로 변경할지 모른다.
//    Hibernate5Module 로 해결할 수 있으나
//    그냥 엔티티를 외부로 노출하지 않으면 되기에 패스!
//    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return orders;
    }

    @GetMapping("/api/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        return orderRepository.findAllByCriteria(new OrderSearch()).stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
