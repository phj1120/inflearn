package hello.jdbc.selector;

import hello.jdbc.selector.strategy.OrderStrategy;
import hello.jdbc.selector.vo.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderContext orderContext;
    private final OrderStrategySelector orderStrategySelector;

    public void order(OrderRequest orderRequest) {
        OrderStrategy orderStrategy = orderStrategySelector.get(orderRequest);
        orderContext.order(orderStrategy);
    }
}
