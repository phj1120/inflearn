package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {

    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> {
            service.callThrow();
        }).isInstanceOf(MyUncheckedException.class);
    }


    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String msg) {
            super(msg);
        }
    }

    /**
     * Unchecked 예외는
     * 예외를 잡지 않으면 밖으로 던진다.
     */
    static class Service {
        Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지 않아도, 상위로 넘어감
         */
        public void callThrow() {
            repository.call();
        }
    }

    /**
     * Uncheck 예외도 throw 로 던져도 됨
     * 장점: 컴파일러가 잡지 않는다.
     * 단점: 컴파일러가 잡지 않는다.
     */
    static class Repository {
        public void call() {
            throw new MyUncheckedException("ex");
        }

    }


}
