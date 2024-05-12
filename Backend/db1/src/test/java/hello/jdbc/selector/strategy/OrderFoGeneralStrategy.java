package hello.jdbc.selector.strategy;

import hello.jdbc.selector.strategy.after.AfterStrategy;
import hello.jdbc.selector.strategy.after.AfterStrategyFO;
import hello.jdbc.selector.strategy.data.DataStrategy;
import hello.jdbc.selector.strategy.data.DataStrategyFO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFoGeneralStrategy implements OrderStrategy {
    private final ApplicationContext ac;

    @Override
    public DataStrategy getDataStrategy() {
        DataStrategyFO dataStrategy = ac.getBean(DataStrategyFO.class);

        return dataStrategy;
    }

    @Override
    public AfterStrategy getAfterStrategy() {
        AfterStrategyFO afterStrategy = ac.getBean(AfterStrategyFO.class);

        return afterStrategy;
    }
}
