package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV2Test {

    @Autowired
    ExternalService externalService;

    @Autowired
    InternalService internalService;

    @Test
    void printProxy() {
        log.info("externalService = class = {}, isAop = {}", AopUtils.isAopProxy(externalService), externalService.getClass());
        log.info("internalService = class = {}, isAop = {}", AopUtils.isAopProxy(internalService), internalService.getClass());
    }

    @Test
    void callInternal() {
        internalService.internal();
    }

    @Test
    void callExternal() {
        externalService.external();
    }

    @TestConfiguration
    static class InternalCallTestConfig {
        @Bean
        InternalService internalService() {
            return new InternalService();
        }

        @Bean
        ExternalService callService() {
            return new ExternalService(internalService());
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class ExternalService {
        private final InternalService internalService;

        public void external() {
            log.info("call external");
            printTxInfo();
            internalService.internal();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);
        }
    }

    @Slf4j
    static class InternalService {
        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);
        }
    }
}
