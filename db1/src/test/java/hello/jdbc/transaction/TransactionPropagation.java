package hello.jdbc.transaction;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@Slf4j
@SpringBootTest
class TransactionPropagation {

    @Autowired
    public TxInnerService txInnerService;

    @Autowired
    public TxOuterService txOuterService;

    /**
     * 부모 트랜잭션이 없다면 새 트랜잭션 생성
     */
    @Test
    void propagationRequiredInner() throws SQLException {
        txInnerService.requiredInner(false);
    }

    @Test
    void propagationRequiredOuter() throws SQLException {
        txOuterService.requiredOuter(false, true);
    }


}
