package hello.jdbc.selector.strategy;

import hello.jdbc.selector.strategy.after.AfterStrategy;
import hello.jdbc.selector.strategy.after.AfterStrategyBO;
import hello.jdbc.selector.strategy.data.DataStrategy;
import hello.jdbc.selector.strategy.data.DataStrategyBO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderBoGeneralStrategy implements OrderStrategy {
    private final ApplicationContext ac;

    @Override
    public DataStrategy getDataStrategy() {
        DataStrategyBO dataStrategy = ac.getBean(DataStrategyBO.class);

        return dataStrategy;
    }

    @Override
    public AfterStrategy getAfterStrategy() {
        AfterStrategyBO afterStrategy = ac.getBean(AfterStrategyBO.class);

        return afterStrategy;
    }
}
