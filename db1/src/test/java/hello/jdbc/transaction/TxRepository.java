package hello.jdbc.transaction;

import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TxRepository {

    private final DataSource dataSource;

    public void tx() {
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("TxRepository.tx - connection: {}", con);
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    public void create(String memberId, int money) {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DataSourceUtils.getConnection(dataSource);

            String sql = "insert into member values(?, ?)";

            stmt = con.prepareStatement(sql);
            stmt.setString(1, memberId);
            stmt.setInt(2, money);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    public Member read(String memberId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DataSourceUtils.getConnection(dataSource);

            String sql = "select * from member where member_id = ?";

            stmt = con.prepareStatement(sql);
            stmt.setString(1, memberId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }
            throw new NoSuchElementException("Member is not exist");
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    public void txException(boolean isExcept) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DataSourceUtils.getConnection(dataSource);
            log.info("connection: {}", con);
            String sql = "select * from member where member_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "member1");
            stmt.executeQuery();
            if (isExcept) {
                throw new NoSuchElementException();
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }
}
