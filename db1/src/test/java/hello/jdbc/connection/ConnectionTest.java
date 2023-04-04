package hello.jdbc.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

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

        Connection con10 = dataSource.getConnection();
        Connection con11 = dataSource.getConnection();
        Connection con12 = dataSource.getConnection();
        Connection con13 = dataSource.getConnection();
        Connection con14 = dataSource.getConnection();
        Connection con15 = dataSource.getConnection();
        Connection con16 = dataSource.getConnection();
        Connection con17 = dataSource.getConnection();
        Connection con18 = dataSource.getConnection();
        Connection con19 = dataSource.getConnection();
    }

    void hikariReturnConnection(DataSource dataSource) throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("Connection: {} class: {}", con, con.getClass());
        String Sql = "select * from member";
        PreparedStatement pstmt = con.prepareStatement(Sql);
        ResultSet rs = pstmt.executeQuery();


        List<Member> memberList = new ArrayList<>();
        while (rs.next()) {
            String memberId = rs.getString("member_id");
            int money = rs.getInt("money");
            memberList.add(new Member(memberId, money));
        }
        log.info(memberList.toString());

        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeConnection(con);
    }

    @Test
    void hikariReturnConnectionRepeat() throws SQLException, InterruptedException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setConnectionTimeout(5000L);

        Thread.sleep(1000);

        hikariReturnConnection(dataSource);
        hikariReturnConnection(dataSource);
        hikariReturnConnection(dataSource);
        hikariReturnConnection(dataSource);

        Thread.sleep(1000);
    }

    @Test
    void dataSourceReturnConnectionRepeat() throws SQLException, InterruptedException {
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        hikariReturnConnection(dataSource);
        hikariReturnConnection(dataSource);
        hikariReturnConnection(dataSource);
        hikariReturnConnection(dataSource);

        Thread.sleep(1000);
    }
}
