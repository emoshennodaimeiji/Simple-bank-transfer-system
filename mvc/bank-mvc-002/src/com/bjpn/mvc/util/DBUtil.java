package com.bjpn.mvc.util;

import java.security.PublicKey;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * jdbc工具类
 */
public class DBUtil {
    private static ResourceBundle bundle = ResourceBundle.getBundle("resources/jdbc");
    private static String driver = bundle.getString("driver");
    private static String url = bundle.getString("url");
    private static String user = bundle.getString("user");
    private static String password = bundle.getString("password");

    private DBUtil(){/*工具类不需要创建对象，故将构造方法私有化*/}

    //类加载时执行，注册驱动
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ThreadLocal<Connection> local = new ThreadLocal<>();//相当于一个全局的map集合<thread,connection>
    /**
     * 这里没有使用数据库连接池，每一次调方法都是一个新的连接对象
     *
     * 改造后的方法使得同一线程下使用的都是同一个连接对象
     * 注意：因为线程池原理，线程对象是被重复使用的，所以关闭时要加上local.remove()以把集合中的connection对象移除
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        /*Connection connection = DriverManager.getConnection(url, user, password);
        return connection;*/
        Connection conn = local.get();
        if(conn==null){
            conn = DriverManager.getConnection(url, user, password);
            local.set(conn);
        }
        return conn;
    }

    /**
     * 关闭资源
     * @param rs
     * @param stat
     * @param conn
     */
    public static void close(ResultSet rs, Statement stat,Connection conn){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stat!=null){
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
                local.remove();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(Statement stat,Connection conn){
        if(stat!=null){
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
                local.remove();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
