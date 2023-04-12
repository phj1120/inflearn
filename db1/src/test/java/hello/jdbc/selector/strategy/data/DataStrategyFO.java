package hello.jdbc.selector.strategy.data;

import org.springframework.stereotype.Component;

@Component
public class DataStrategyFO implements DataStrategy {
    @Override
    public void call() {
        System.out.println("call.DataStrategyFO");
    }
}
