package hello.advanced.app.trace.logtrace;

import hello.advanced.app.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class ThreadLogTraceTest {

    private LogTrace trace = new ThreadLocalLogTrace();

    @Test
    void begin_end_level2() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_end_level3() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        TraceStatus status3 = trace.begin("hello3");
        trace.end(status3);
        trace.end(status2);
        trace.end(status1);
    }

}