package hello.jdbc.selector.strategy.after;

import org.springframework.stereotype.Component;

@Component
public class AfterStrategyFO implements AfterStrategy{
    @Override
    public void call() {
        System.out.println("call.AfterStrategyFO");
    }
}
