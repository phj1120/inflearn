package hello.advanced.app.trace.logtrace;

import hello.advanced.app.trace.TraceStatus;

public class LogTraceService {

    private LogTrace trace = new FieldLogTrace();

    public void logByDepth(int depth) {
        // ---- outer
        TraceStatus outerStatus = null;
        try {
            outerStatus = trace.begin("test");
            if (outerStatus.getTraceId().getLevel() < depth) {
                logByDepth(depth);
            }
            trace.end(outerStatus);
        } catch (Exception e) {
            trace.exception(outerStatus, e);
        }
        // ---- outer 종료
    }

    public void logThreeDept() {
        // ---- outer
        TraceStatus outerStatus = null;
        try {
            outerStatus = trace.begin("test.outer");

            // ---- mid 시작
            TraceStatus midStatus = null;
            try {
                midStatus = trace.begin("test.inner");

                // ---- inner 시작
                TraceStatus innerStatus = null;
                try {
                    innerStatus = trace.begin("test.inner");
                    trace.end(innerStatus);
                } catch (Exception e) {
                    trace.exception(innerStatus, e);
                }
                // ---- inner 종료


                trace.end(midStatus);
            } catch (Exception e) {
                trace.exception(midStatus, e);
            }
            // ---- mid 종료

            trace.end(outerStatus);
        } catch (Exception e) {
            trace.exception(outerStatus, e);
        }
        // ---- outer 종료
    }

}
