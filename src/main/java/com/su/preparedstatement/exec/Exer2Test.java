package com.su.preparedstatement.exec;

import com.su.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class Exer2Test {
    @Test
    public void insertTest(){
        String sql = "insert into examstudent(FlowID,Type,IDCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?,?)";
        int insert = insertDB(sql, 7, 6, 123456, 7891011, "张三", "广州", "100");
        if (insert == 1){
            System.out.println("插入成功");
        }else {
            System.out.println("插入失败");
        }
    }

    public int insertDB(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }

    // 根据身份证号或准考证号查询学生成绩
    @Test
    public void searchStudent(){
        String sql = "select flowID,studentName from examstudent where IDCard=?";
        List<Student> list = getInstance(Student.class, sql, "342824195263214584");
        list.forEach(System.out::println);
    }

    public <T> List<T> getInstance(Class<T> clazz, String sql, Object... args){
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

            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取列数
            int columnCount = rsmd.getColumnCount();
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columnValue = rs.getObject(i + 1);
                    // 获取列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = t.getClass().getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }
}
