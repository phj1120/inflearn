package hello.jdbc.transaction;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
public class TxRepository {

    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public TxRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator();
    }

    public void txException(boolean isExcept) {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql = "select * from member where member_id = ?";

        try {
            con = DataSourceUtils.getConnection(dataSource);
            log.info("[method]: TxRepository.txException, [connection]: {}", con);
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "member1");
            stmt.executeQuery();
            if (isExcept) {
                throw new NoSuchElementException();
            }
        } catch (SQLException e) {
            throw exTranslator.translate("txExcpetion", sql, e);
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }


    public void create(String memberId, int money) {
        String sql = "insert into member values(?, ?)";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DataSourceUtils.getConnection(dataSource);
            log.info("[method]: TxRepository.create, [connection]: {}", con);

            stmt = con.prepareStatement(sql);
            stmt.setString(1, memberId);
            stmt.setInt(2, money);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw exTranslator.translate("create", sql, e);
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    public Member read(String memberId) {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DataSourceUtils.getConnection(dataSource);
            log.info("[method]: TxRepository.read, [connection]: {}", con);

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
            throw exTranslator.translate("read", sql, e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    public void update(String memberId, int money) {
        String sql = "update member set money = ? where member_id = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DataSourceUtils.getConnection(dataSource);
            log.info("[method]: TxRepository.update, [connection]: {}", con);

            stmt = con.prepareStatement(sql);
            stmt.setInt(1, money);
            stmt.setString(2, memberId);
            throw new NoSuchElementException("Member is not exist");
        } catch (SQLException e) {
            throw exTranslator.translate("read", sql, e);
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DataSourceUtils.getConnection(dataSource);
            log.info("[method]: TxRepository.delete, [connection]: {}", con);

            stmt = con.prepareStatement(sql);
            stmt.setString(1, memberId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("delete", sql, e);
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }
}
