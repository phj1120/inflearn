package hello.jdbc.selector;

import hello.jdbc.selector.strategy.OrderBoGeneralStrategy;
import hello.jdbc.selector.strategy.OrderFoGeneralStrategy;
import hello.jdbc.selector.strategy.OrderStrategy;
import hello.jdbc.selector.vo.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStrategySelector {

    private final ApplicationContext ac;

    public OrderStrategy get(OrderRequest orderRequest) {
        String orderType = orderRequest.getOrderType();
        String systemType = orderRequest.getSystemType();

        if (orderType.equals("GENERAL") && systemType.equals("BO")) {
            OrderBoGeneralStrategy orderStrategy = ac.getBean(OrderBoGeneralStrategy.class);

            return orderStrategy;
        }

        if (orderType.equals("GENERAL") && systemType.equals("FO")) {
            OrderFoGeneralStrategy orderStrategy = ac.getBean(OrderFoGeneralStrategy.class);

            return orderStrategy;
        }

        throw new RuntimeException();
    }
}
