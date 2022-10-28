package com.su.preparedstatement.crud;

import com.su.bean.Customer;
import org.junit.Test;
import com.su.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对于Customers表的查询
 */
public class CustomerForQuery {
    @Test
    public void testQueryForCustomers(){
        String sql = "select name,birth,email from customers where name=?";
        Customer customer = queryForCustomers(sql, "哪吒");
        System.out.println(customer);
    }

    public Customer queryForCustomers(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                Customer customer = new Customer();
                // 处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    // 获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);

                    // 给customer对象指定的columnName属性赋值为对应的columnValue，通过反射实现
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true); // 若是私有属性则设置为可以访问
                    field.set(customer, columnValue);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBCUtils.closeResource(conn, ps, rs);
        return null;
    }


    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select * from customers where name = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, "哪吒");
            // 执行并返回结果
            resultSet = ps.executeQuery();

            // 处理结果集
            if (resultSet.next()){
                // 判断结果集的下一条是否有数据，若有则返回true并指针下移
                // 获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);
    //            Object[] data = new Object{id, name, email, birth};
                // 封装成对象
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn, ps, resultSet);
        }

    }
}
