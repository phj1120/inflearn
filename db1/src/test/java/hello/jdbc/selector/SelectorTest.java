package hello.jdbc.selector;

import hello.jdbc.selector.vo.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SelectorTest {
    @Autowired
    private OrderService orderService;

    @Test
    void BO() {
        orderService.order(new OrderRequest("BO", "GENERAL"));
    }

    @Test
    void FO() {
        orderService.order(new OrderRequest("FO", "GENERAL"));
    }

}
