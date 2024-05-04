package hello.jdbc.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TransactionTest {
    @Autowired
    TxService txService;

    @Test
    void checkedException_false() throws Exception {
        txService.checkedException(false);
    }

    @Test
    void uncheckedException_false() {
        txService.uncheckedException(false);
    }

    // Check 예외 발생 시 해당 Transaction Commit
    @Test
    void checkedException_true() throws Exception {
        Assertions.assertThatThrownBy(() -> {
            txService.checkedException(true);
        }).isInstanceOf(Exception.class);
    }

    // Uncheck 예외 발생 시 해당 Transaction Rollback
    @Test
    void uncheckedException_true() {
        Assertions.assertThatThrownBy(() -> {
            txService.uncheckedException(true);
        }).isInstanceOf(RuntimeException.class);
    }


    @TestConfiguration
    static class TransactionTestConfig {
        @Bean
        TxService txService() {
            return new TxService();
        }
    }

    @Slf4j
    @Transactional
    static class TxService {
        public void checkedException(boolean isException) throws Exception {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx is {}", isActive);
            if (isException) {
                log.error("Exception");
                throw new Exception();
            }
        }

        public void uncheckedException(boolean isException) {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx is {}", isActive);
            if (isException) {
                log.error("RuntimeException");
                throw new RuntimeException();
            }
        }
    }

}
