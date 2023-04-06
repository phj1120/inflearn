package hello.jdbc.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TxInnerService {
    private final TxRepository txRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiredInner(boolean isInnerExcept) throws SQLException {
        log.info("[  requiredInner.start]");
        txRepository.txException(isInnerExcept);
        log.info("[  requiredInner.end]");
    }
}
