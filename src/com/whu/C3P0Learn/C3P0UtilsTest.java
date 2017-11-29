package com.whu.C3P0Learn;

import com.whu.JDBCUtils.JDBCUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class C3P0UtilsTest {

    @Test
    public void getConnection() throws Exception {
        Connection con = C3P0Utils.getConnection();
        String sql = "SELECT * FROM web09.product";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + resultSet.getString(2));
        }
        C3P0Utils.close(con, pstmt, resultSet);
    }

    @Test
    public void testDBUtils() {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        /*测试中增删改*/
        String sql_insert = "Insert into product values(?,?,?,?)";
        String sql_delete = "delete from product where pid = ?";
        String sql_update = "update product set pname = ? where pid = ?";
        try {
//            qr.update(sql_delete, "p001");
            qr.update(sql_update, "大马士革", "p002");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDBUtils2() {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        /*测试中查找*/
        String sql_find = "select count(*) from product";
        try {
            ScalarHandler sh = new ScalarHandler(1);
            Object query = qr.query(sql_find, sh);
            System.out.println(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDBUtils3() {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        /*测试中查找*/
        String sql_find = "select * from product";
        try {
            Product product = qr.query(sql_find, new BeanHandler<Product>(Product.class));
            System.out.println(product.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDBUtils4() {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        /*测试中查找*/
        String sql_find = "select * from product";
        try {
            List<Product> productList = qr.query(sql_find, new BeanListHandler<>(Product.class));
            for (Product product : productList) {
                System.out.println(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}