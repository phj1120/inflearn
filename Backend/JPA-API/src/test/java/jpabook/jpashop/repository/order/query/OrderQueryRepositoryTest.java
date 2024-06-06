package jpabook.jpashop.repository.order.query;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OrderQueryRepositoryTest {
    @Autowired
    OrderQueryRepository orderQueryRepository;

    @Test
    void test() {
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findOrderQueryDtos();

        for (OrderQueryDto orderQueryDto : orderQueryDtos) {
            System.out.println(orderQueryDto);
        }
    }

}