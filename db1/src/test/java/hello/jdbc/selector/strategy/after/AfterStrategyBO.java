package hello.jdbc.selector.strategy.after;

import org.springframework.stereotype.Component;

@Component
public class AfterStrategyBO implements AfterStrategy{
    @Override
    public void call() {
        System.out.println("call.AfterStrategyBO");
    }
}
