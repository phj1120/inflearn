package jpabook.jpashop.api;

import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6() {
        List<OrderFlatDto> flat = orderQueryRepository.findAllByDto_flat();

        // OrderQueryDto 형식에 맞춰 반환하고 싶으면,
        // 애플리케이션에서 지지고 복고 하면 되긴 함.

        return flat;
    }
}

