package hello.advanced.app.trace.logtrace;

import org.junit.jupiter.api.Test;

public class LogTraceTest {
    LogTraceService logTraceService = new LogTraceService();

    @Test
    void test() {
        logTraceService.logByDepth(4);
    }
}
