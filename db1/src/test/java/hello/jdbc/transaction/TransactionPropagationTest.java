package hello.jdbc.transaction;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

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
        // child: 새 트랜잭션 생성
        // Creating new transaction with name [hello.jdbc.transaction.TxInnerService.inner_Required]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
        log.info("parent: Nx / Child: Tx");
        txOuterService.dependsOnParentTransactions_parentNxChildTx();

        log.info("------------------------------------------------------");

        // child: 기존 트랜잭션 참여
        // Participating in existing transaction
        log.info("parent: Tx / Child: Tx");
        txOuterService.dependsOnParentTransactions_parentTxChildTx();
    }

    @Test
    @DisplayName("REQUIRES_NEW:  부모 트랜잭션에 상관없이 새 트랜잭션 생성")
    void inDependsOnParentTransactions() {
        // child: 새 트랜잭션 생성
        // Suspending current transaction, creating new transaction with name [hello.jdbc.transaction.TxInnerService.inner_RequiresNew]
        log.info("parent: Tx / Child: Tx");
        txOuterService.inDependsOnParentTransactions_parentTxChildTx();
    }
}
