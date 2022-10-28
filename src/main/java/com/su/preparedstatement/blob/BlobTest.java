package com.su.preparedstatement.blob;

import com.su.bean.Customer;
import org.junit.Test;
import com.su.util.JDBCUtils;
import sun.plugin.cache.CacheUpdateHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;

public class BlobTest {
    @Test
    public void testInsert() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, "zhangsan");
        ps.setObject(2, "zhangsan@qq.com");
        ps.setObject(3, "1992-09-08");
        FileInputStream is = new FileInputStream(new File("C:\\Users\\SuuuJ\\Desktop\\学习\\homework2.png"));
        ps.setBlob(4, is);
        ps.execute();
        JDBCUtils.closeResource(conn, ps);
    }

    @Test
    public void testQuery() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth,photo from customers where id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, 5);
        ResultSet rs = ps.executeQuery();
        InputStream is = null;
        FileOutputStream fos = null;
        if (rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            Date birth = rs.getDate("birth");
            Customer customer = new Customer(id, name, email, birth);
            System.out.println(customer);

            // 将Blob类型的字段下载下来，以文件的形式保存在本地
            Blob photo = rs.getBlob("photo");
            is = photo.getBinaryStream();
            fos = new FileOutputStream("./test.jps");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }
        }
        JDBCUtils.closeResource(conn, ps, rs);
        is.close();
        fos.close();
    }
}
