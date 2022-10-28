package com.su.preparedstatement.crud;

import org.junit.Test;
import com.su.util.JDBCUtils;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


/**
 * 使用PreparedStatement来替代Statement，实现对数据表的增删改查操作
 */
public class PreparedStatementUpdateTest {

    @Test
    public void testUpdate(){
        String sql = "delete from customers where name = ?";
        update(sql, "小明");

    }

    // 通用的增删改操作
    public void update(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 获取链接
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);

            // 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn, ps);
        }

    }


    // 修改
    @Test
    public void testUpdate1() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 1.获取数据库的连接
            conn = JDBCUtils.getConnection();

            // 2.预编译SQL语句，返回PreparedStatement实例
            String sql = "update customers set email = ? where name = ?";
            ps = conn.prepareStatement(sql);
            // 3.填充占位符
            ps.setObject(1, "test");
            ps.setObject(2, "小明");
            // 4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 5.资源的关闭
        JDBCUtils.closeResource(conn, ps);
    }

    @Test
    public void testInsert() {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            Class.forName(driverClass);

            conn = DriverManager.getConnection(url, user, password);

            String sql = "insert into customers(name,email)values(?,?)";//?:占位符
            ps = conn.prepareStatement(sql);

            ps.setString(1, "哪吒");
            ps.setString(2, "nezha@gmail.com");

            ps.execute();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
