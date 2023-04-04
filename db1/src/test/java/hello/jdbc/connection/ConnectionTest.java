package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    private DataSource dataSource;

    @BeforeEach
    public void before() {
        // DriverManagerDataSource
//        dataSource = getDriverManagerDataSource();

        // HikariDataSource
        dataSource = getHikariDataSource();
    }

    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }


    // DataSource: 커넥션을 획득하는 방법을 추상화
    // 설정과 사용을 분리 함
    @Test
    void driverManagerDataSource() throws SQLException {
        // 설정
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 사용
        Connection con0 = dataSource.getConnection();
        Connection con1 = dataSource.getConnection();

        log.info("connection={}, class={}", con0, con0.getClass());
        log.info("connection={}, class={}", con1, con1.getClass());
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        // 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        // pool 에 connection 추가하는 건 리소스를 많이 잡아 먹기 때문에 다른 Thread 에서 돔
        // connection adder Thread 가 실행 완료 되기전에
        // main Thread 가 끝나 로그가 찍히지 않을 수 있기 때문에
        // 1초 대기
        Thread.sleep(1000);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con0 = dataSource.getConnection();
        Connection con1 = dataSource.getConnection();

        log.info("connection={}, class={}", con0, con0.getClass());
        log.info("connection={}, class={}", con1, con1.getClass());
    }

    @Test
    public void overPoolSize() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setConnectionTimeout(5000L);

        Connection con0 = dataSource.getConnection();
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        Connection con3 = dataSource.getConnection();
        Connection con4 = dataSource.getConnection();
        Connection con5 = dataSource.getConnection();
        Connection con6 = dataSource.getConnection();
        Connection con7 = dataSource.getConnection();
        Connection con8 = dataSource.getConnection();
        Connection con9 = dataSource.getConnection();
//        Connection con10 = dataSource.getConnection(); // 예외 발생

        // Connection 종료 후
        JdbcUtils.closeConnection(con0);
        JdbcUtils.closeConnection(con1);
        JdbcUtils.closeConnection(con2);
        JdbcUtils.closeConnection(con3);
        JdbcUtils.closeConnection(con4);
        JdbcUtils.closeConnection(con5);
        JdbcUtils.closeConnection(con6);
        JdbcUtils.closeConnection(con7);
        JdbcUtils.closeConnection(con8);
        JdbcUtils.closeConnection(con9);

        // Connection 가져오기
        Connection con10 = dataSource.getConnection();
        Connection con11 = dataSource.getConnection();
        Connection con12 = dataSource.getConnection();
        Connection con13 = dataSource.getConnection();
        Connection con14 = dataSource.getConnection();
    }

    /**
     * 각 커넥션 마다 close 할 경우
     * connection: HikariProxyConnection@157168588 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@319689067 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@238564722 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     */
    @Test
    void hikariConnectionClose() throws InterruptedException {
        DataSource dataSource = getHikariDataSource();

        Thread.sleep(1000);

        Connection con1 = null;
        try {
            con1 = dataSource.getConnection();
            bizLogic(con1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(con1);
        }

        Connection con2 = null;
        try {
            con2 = dataSource.getConnection();
            bizLogic(con2);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(con2);
        }

        Connection con3 = null;
        try {
            con3 = dataSource.getConnection();
            bizLogic(con3);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(con3);
        }

        Thread.sleep(1000);
    }

    /**
     * 각 커넥션을 close 하지 않을 경우
     * connection: HikariProxyConnection@100929741 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@20111564 wrapping conn1: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@2065718717 wrapping conn2: url=jdbc:h2:tcp://localhost/~/test user=SA
     */
    @Test
    void hikariConnectionNoClose() throws SQLException, InterruptedException {
        DataSource dataSource = getHikariDataSource();
        Thread.sleep(1000);

        Connection con1 = dataSource.getConnection();
        bizLogic(con1);

        Connection con2 = dataSource.getConnection();
        bizLogic(con2);

        Connection con3 = dataSource.getConnection();
        bizLogic(con3);

        Thread.sleep(1000);
    }


    /**
     * 한 커넥션으로 여러 쿼리 수행(Transaction 을 위해)
     * connection: HikariProxyConnection@1154821602 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@1154821602 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     * connection: HikariProxyConnection@1154821602 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
     */
    @Test
    void hikariConnectionOne() throws InterruptedException {
        DataSource dataSource = getHikariDataSource();

        Connection con = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            bizLogic(con);
            bizLogic(con);
            bizLogic(con);

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(con);
        }
        Thread.sleep(1000);
    }

    DataSource getHikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setConnectionTimeout(5000L);

        return dataSource;
    }

    DataSource getDriverManagerDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        return dataSource;
    }

    void bizLogic(Connection con) throws SQLException {
        log.info("connection: {}", con);
        String sql = "select * from member";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.executeQuery();
        JdbcUtils.closeStatement(pstmt);
    }
}
