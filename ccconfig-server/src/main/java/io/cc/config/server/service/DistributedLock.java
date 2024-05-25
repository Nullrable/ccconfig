package io.cc.config.server.service;

import jakarta.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author nhsoft.lsd
 */
@Component
public class DistributedLock {

    private static final Logger log = LoggerFactory.getLogger(DistributedLock.class);
    @Resource
    private DataSource dataSource;

    private Map<String, Connection> connections = new ConcurrentHashMap<>();

    public void unlock(String lockKey) {
        try {
            Connection conn = connections.get(lockKey);
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {}
    }


    public void lock(String lockKey, int lockTimeout) throws SQLException {

        Connection conn = connections.get(lockKey);
        if (conn == null) {
            conn = dataSource.getConnection();
            connections.put(lockKey, conn);
        }

        conn.setAutoCommit(false);

        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        Statement stmt = conn.createStatement();

        stmt.execute("set innodb_lock_wait_timeout = " + lockTimeout);

        stmt.execute("SELECT * FROM locks where lock_key = " + "'" + lockKey + "'" + " for update");
    }

    public boolean tryLock(String lockKey, int lockTimeout) {
        try {
            lock(lockKey, lockTimeout);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
