package hello.jdbc.transaction;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
@SpringBootTest
class TransactionPropagationTest {

    public final TxRepository txRepository;
    public final TxInnerService txInnerService;
    public final TxOuterService txOuterService;

    @Autowired
    public TransactionPropagationTest(TxRepository txRepository, TxInnerService txInnerService, TxOuterService txOuterService) {
        this.txRepository = txRepository;
        this.txInnerService = txInnerService;
        this.txOuterService = txOuterService;
    }

    @TestConfiguration
    static class config {
        @Bean
        DataSource dataSource() {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setUsername(USERNAME);
            hikariConfig.setJdbcUrl(URL);
            hikariConfig.setPassword(PASSWORD);

            return new HikariDataSource(hikariConfig);
        }

        @Bean
        TxRepository txRepository() {
            return new TxRepository(dataSource());
        }

        @Bean
        TxInnerService txInnerService() {
            return new TxInnerService(txRepository());
        }

        @Bean
        TxOuterService txOuterService() {
            return new TxOuterService(txRepository(), txInnerService());
        }
    }

    @Test
    @DisplayName("REQUIRED: 부모 트랜잭션에 맡김")
    void dependsOnParentTransactions() {
        // inner: 새 트랜잭션 생성
        // Creating new transaction with name [hello.jdbc.transaction.TxInnerService.inner_Required]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
        log.info("parent: Nx / Child: Tx");
        txOuterService.dependsOnParentTransactions_OuterNxInnerTx();

        log.info("------------------------------------------------------");

        // inner: 기존 트랜잭션 참여
        // Participating in existing transaction
        log.info("parent: Tx / Child: Tx");
        txOuterService.dependsOnParentTransactions_OuterTxInnerTx();
    }

    @Test
    @DisplayName("REQUIRES_NEW:  부모 트랜잭션에 상관없이 새 트랜잭션 생성")
    void inDependsOnParentTransactions() {
        // inner: 새 트랜잭션 생성
        // Suspending current transaction, creating new transaction with name [hello.jdbc.transaction.TxInnerService.inner_RequiresNew]
        printLog("parent: Tx / Child: Tx");
        txOuterService.inDependsOnParentTransactions_OuterTxInnerTx(false, false, false);

        printLog("outer 실패 -> x");
        // 에러가 발생하면 그 뒤의 코드는 실행 되지 않음
        Assertions.assertThatThrownBy(() -> {
            txOuterService.inDependsOnParentTransactions_OuterTxInnerTx(true, false, false);
        }).isInstanceOf(NoSuchElementException.class);

        printLog("outer 성공 -> inner 실패 -> outer 성공");
        // outer 에서 inner 예외 처리를 하면 inner 가 rollback 되어도 outer 이어서 실행 할 수 있고,
        // outer 에서 inner 예외 처리를 하지 않으면 inner 의 예외가 outer 로 전파 되기 때문에 outer 또한 rollback 된다.
        txOuterService.inDependsOnParentTransactions_OuterTxInnerTx(false, true, false);

        printLog("outer 성공 -> inner 성공 -> outer 실패");
        // REQUIRES_NEW 로 선언된 inner 트랜잭션의 경우 outer 트랜잭션과 상관 없이 독립적으로 사용 된다.
        Assertions.assertThatThrownBy(() -> {
            txOuterService.inDependsOnParentTransactions_OuterTxInnerTx(false, false, true);
        }).isInstanceOf(NoSuchElementException.class);
    }

    private static void printLog(String msg) {
        log.info("\n\n--- {} ---", msg);
    }
}
