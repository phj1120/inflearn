package hello.jdbc.selector;

import hello.jdbc.selector.strategy.after.AfterStrategy;
import hello.jdbc.selector.strategy.data.DataStrategy;
import hello.jdbc.selector.strategy.OrderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderContext {

    public void order(OrderStrategy orderStrategy) {
        AfterStrategy afterStrategy = orderStrategy.getAfterStrategy();
        afterStrategy.call();

        DataStrategy dataStrategy = orderStrategy.getDataStrategy();
        dataStrategy.call();
    }
}
