package hello.jdbc.selector.strategy;

import hello.jdbc.selector.strategy.after.AfterStrategy;
import hello.jdbc.selector.strategy.data.DataStrategy;

public interface OrderStrategy {

    DataStrategy getDataStrategy();

    AfterStrategy getAfterStrategy();
}
