package com.whu.JDBCUtils;

import com.whu.MyConnecitonPool.MyConnectionPool;

import java.sql.*;
import java.util.ResourceBundle;

public class JDBCUtils {
    /*这个类采用静态方法返回数据库的连接*/
    private static Connection con = null;
    private static String url = null;
    private static String user = null;
    private static String password = null;
    /*静态代码段进行初始化*/
    static {
        ResourceBundle rb = ResourceBundle.getBundle("db"); //切记此处带后缀properties；
        url = rb.getString("url");
        user = rb.getString("user");
        password = rb.getString("password");
        try {

            Class.forName("com.mysql.jdbc.Driver");
            if (con == null) {
                con = DriverManager.getConnection(url, user, password);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return con;
    }

    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            /*现在池子里放的是包装好的连接，不需要调用这个方法*/
           /* MyConnectionPool.releaseConnection(connection);*/
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
