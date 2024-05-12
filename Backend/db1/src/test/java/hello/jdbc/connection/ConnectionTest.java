package hello.jdbc.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    private DataSource dataSource;

    @BeforeEach
    public void before() {
        // DriverManager
//        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // HikariCP
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(URL);
        hikariConfig.setUsername(USERNAME);
        hikariConfig.setPassword(PASSWORD);
        dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * DriverManager: 설정, 사용을 한 번에
     */
    @DisplayName("DriverManager 사용")
    @Test
    void useDriverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }

    /**
     * DataSource: 커넥션을 획득하는 방법을 추상화, 설정과 사용을 분리
     */
    @DisplayName("DriverManagerDataSource 사용")
    @Test
    void useDriverManagerDataSource() throws SQLException {
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        Connection con0 = dataSource.getConnection();
        Connection con1 = dataSource.getConnection();

        log.info("connection={}, class={}", con0, con0.getClass());
        log.info("connection={}, class={}", con1, con1.getClass());
    }

    @DisplayName("커넥션풀 크기를 초과할 경우")
    @Test
    public void overPoolSize() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(5);
        dataSource.setConnectionTimeout(5000L);

        Connection con0 = dataSource.getConnection();
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        Connection con3 = dataSource.getConnection();
        Connection con4 = dataSource.getConnection();

        // MaximumPoolSize 보다 많이 생성하면 SQLTransientConnectionException 발생
        Assertions.assertThatThrownBy(() -> {
            Connection con5 = dataSource.getConnection();
        }).isInstanceOf(SQLTransientConnectionException.class);

        // 기존의 Connection 종료 후 추가로 생성 가능
        JdbcUtils.closeConnection(con4);
        Connection con5 = dataSource.getConnection();
    }

    /**
     * connection: HikariProxyConnection@157168588 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@319689067 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@238564722 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     */
    @DisplayName("커넥션 사용 후 종료")
    @Test
    void CloseConnectionAfterUse() throws InterruptedException {
        Thread.sleep(1000);

        for (int i = 0; i < 3; i++) {
            Connection con = null;
            try {
                con = dataSource.getConnection();
                bizLogic(con);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JdbcUtils.closeConnection(con);
            }
        }

        Thread.sleep(1000);
    }

    /**
     * connection: HikariProxyConnection@100929741 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@20111564 wrapping conn1: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@2065718717 wrapping conn2: url=jdbc:h2:tcp://localhost/~/test user=SA
     */
    @DisplayName("커넥션 사용 후 종료 하지 않을 경우")
    @Test
    void NotCloseConnectionAfterUse() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            try {
                Connection con = dataSource.getConnection();
                bizLogic(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Thread.sleep(1000);
    }


    /**
     * connection: HikariProxyConnection@1154821602 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@1154821602 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@1154821602 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     */
    @DisplayName("하나의 커넥션 사용 (Transaction 을 위해)")
    @Test
    void useOneConnection() throws InterruptedException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            for (int i = 0; i < 3; i++) {
                bizLogic(con);
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(con);
        }
        Thread.sleep(1000);
    }

    private void bizLogic(Connection con) throws SQLException {
        log.info("connection: {}", con);
        String sql = "select * from member";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.executeQuery();
        JdbcUtils.closeStatement(pstmt);
    }
}
