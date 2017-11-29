package com.whu.MyConnecitonPool;

import com.whu.JDBCUtils.JDBCUtils;
import com.whu.MyConnection.MyConnection;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Java中自定义连接池，需要实现DataSource接口
 */

/*
* 在MyConnection类中，我们包装了connection,下面连接池中存放的都应该是包装后的connection.
* */
public class MyConnectionPool implements DataSource {
    /*
    这个地方list的类型参数设置为Connection，方便运用多态；
     */
    private static List<Connection> pool = new LinkedList<>();

    /*使用静态代码块，类一加载进来，就往连接池里放五个Connection。
    * 考虑到连接的频繁释放操作，我们用LinkedList作为容器；
    * */
    static {
        if (pool == null) {
            pool = new LinkedList<>();
        } else {
            for (int i = 0; i < 5; i++) {
                /**
                 * 现在池子里放的就是包装后的connection;
                 */
                Connection con = JDBCUtils.getConnection();
                MyConnection myConnection = new MyConnection(con);
                pool.add(myConnection);
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        //如果池子被销毁了，重新创建;
        if (pool == null) {
            pool = new LinkedList<>();
        }
        //如果池子里的连接被取完了，在来五个;
        if (pool.size() == 0) {
            for (int i = 0; i < 5; i++) {
                Connection con = JDBCUtils.getConnection();
                MyConnection myConnection = new MyConnection(con);
                pool.add(myConnection);
            }
        }
        return pool.remove(0);
    }

    /*下面要实现连接池的归还操作*/

    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            pool.add(connection);
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
