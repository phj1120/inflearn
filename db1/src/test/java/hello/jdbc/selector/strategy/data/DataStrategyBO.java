package hello.jdbc.selector.strategy.data;

import org.springframework.stereotype.Component;

@Component
public class DataStrategyBO implements DataStrategy {
    @Override
    public void call() {
        System.out.println("call.DataStrategyBO");
    }
}
