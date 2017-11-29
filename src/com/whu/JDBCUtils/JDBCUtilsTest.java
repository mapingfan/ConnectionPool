package com.whu.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCUtilsTest {
    @org.junit.Test
    public void getConnection() throws Exception {
        Connection con = JDBCUtils.getConnection();
        String sql = "SELECT * FROM web09.product";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1) + rs.getString(2));
        }
        JDBCUtils.closeConnection(con);
    }

}