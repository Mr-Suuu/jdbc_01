package com.su;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    @Test
    public void testConnection1() throws SQLException {
        // 获取Driver的实现类对象
                Driver driver = new com.mysql.jdbc.Driver();

        String url = "jdbc:mysql://localhost:3306/db1";
        // 将用户名和密码封装
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");
        Connection conn = driver.connect(url, info);

        System.out.println(conn);

    }

    @Test
    public void testConnection2() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        // 获取Driver的实现类对象，使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        // 提供要链接的数据库
        String url = "jdbc:mysql://localhost:3306/db1";
        // 将用户名和密码封装
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");
        // 获取连接
        Connection conn = driver.connect(url, info);

        System.out.println(conn);
    }

    // 将数据库连接需要的4个基本信息生命在配置文件中，通过读取配置文件的方式，获取连接
    @Test
    public void getConnection() throws Exception {
        // 1.读取配置文件中的4个基本信息
        // 注意这里写类名
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        // 2.加载驱动
        Class.forName(driverClass);

        // 3.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }
}
