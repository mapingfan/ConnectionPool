package com.whu.MyConnecitonPool;

import com.whu.JDBCUtils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class MyConnectionPoolTest {
    @Test
    public void getConnection() throws Exception {
        for (int i = 0; i < 10; i++) {
            MyConnectionPool connectionPool = new MyConnectionPool();
            Connection con = connectionPool.getConnection();
            String sql = "SELECT * FROM web09.product";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1) + rs.getString(2));
            }
            /**这个地方存在严重问题，如果用户使用了con.close()方法，那么连接就真的被关闭了。
             * 而不是回到连接池重；所以我们要增强close的方法。
             * 这个地方的close方法并不是我们实现的，而是源码提供的，去改源码肯定不是好的做法；
             * 比较好的做法是我们也实现Connection接口，作为Connection的子类，重写close方法；
             * 这个地方需要包装下connection，增强功能；
             * 装饰器模式用户增强功能；
             * 接口C,实现接口的C的类A。现在类A的某个方法不好用，我们打算增强下，如何？
             * 让B也实现接口C；此处考虑下，如果不把A类的对象当做B类的成员，我们就需要把C的属性字段也拷贝
             * 一份到B中，在实现一下A类的功能 ；
             * 下面看下代码实现，更好理解；
             * */
            JDBCUtils.closeConnection(con);
            System.out.println("-----------------");
        }

    }

    /*测试包装后的连接*/

    @Test
    public void testConnectionPool() {
        MyConnectionPool myConnectionPool = new MyConnectionPool();
        Connection connection = null;
        /**
         * 注意这个地方存在坑,因为我们重新实现了Connection，那么此时调用
         * connection.prepareStatement(sql)，调用的其实是MyConnection类中的方法，而这个方法实现如下
         *  @Override
            public PreparedStatement prepareStatement(String sql) throws SQLException {
                return null;
            }
         *  所以，一定要注意也要去重写这个方法，不要返回null；
         */
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = myConnectionPool.getConnection();
            String sql = "SELECT * FROM web09.product";
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1) + rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBCUtils.closeResultSet(rs);
        JDBCUtils.closePreparedStatement(pstmt);
        try {
            connection.close(); //观察控制台，看看此处调用的是不是调用增强方法。
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
