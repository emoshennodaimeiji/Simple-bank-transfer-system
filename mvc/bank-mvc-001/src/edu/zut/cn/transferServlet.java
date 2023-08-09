package edu.zut.cn;

import com.bjpn.AppException;
import com.bjpn.MoneyNotEnoughException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * 一个类包含了：
 *  1>数据的接收
 *  2>核心的业务处理
 *  3>数据库表中数据的CRUD
 *  4>页面的数据展示
 *
 *  缺点：
 *  1>代码复用性太差
 *  2>耦合度太高，代码拓展能力差
 *  3>处理数据库数据的语句与业务逻辑语句混杂在一起，很容易发生错误
 */

@WebServlet("/transfer")
public class transferServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String fromAct = request.getParameter("fromAct");
        String toAct = request.getParameter("toAct");
        Double money = Double.parseDouble(request.getParameter("money"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/poetry?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding =utf-8&useSSL=false","root","20050306Zzh");
            String sql = "select balance from t_act where actno=?";
            stat = conn.prepareStatement(sql);
            stat.setNString(1,fromAct);
            rs = stat.executeQuery();
            if(rs.next()){
                double balance = rs.getDouble("balance");
                if(balance<money){
                    throw new MoneyNotEnoughException("抱歉，您的余额不足");
                }else {
                    conn.setAutoCommit(false);//关闭jdbc自动提交（为事务的开始）

                    String sql2 = "update t_act set balance = balance - ? where actno = ?";
                    stat2 = conn.prepareStatement(sql2);
                    stat2.setDouble(1,money);
                    stat2.setNString(2,fromAct);
                    int count = stat2.executeUpdate();

                    String sql3 = "update t_act set balance = balance + ? where actno = ?";
                    stat3 = conn.prepareStatement(sql3);
                    stat3.setDouble(1,money);
                    stat3.setNString(2,toAct);
                    count = count + stat3.executeUpdate();

                    if(count!=2){
                        throw new AppException("app错误，请联系管理员");
                    }

                    conn.commit();//手动提交事务
                    out.print("转账成功");

                }
            }
        } catch (Exception e) {
            try {
                if(conn != null){
                    conn.rollback();//回滚事务，是否执行需判断
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            out.print(e.getMessage());
        }finally {
            if(stat3!=null){
                try {
                    stat3.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stat2!=null){
                try {
                    stat2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        /*try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        ResultSet rs = null;
        try {
            String url = "jdbc:mysql://localhost:3306/poetry?" +
                    "serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding =utf-8&useSSL=false";
            String user = "root";
            String password = "20050306Zzh";
            conn = DriverManager.getConnection(url,user,password);
            String sql = "select balance from t_act where actno=?";
            stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();
            stat.setNString(1,fromAct);
            if(rs.next()){
                double balance = rs.getDouble("balance");
                if(balance<money){
                    throw new MoneyNotEnoughException("抱歉，您的余额不足");
                }else {
                    String sql2 = "update t_act set balance = balance - ? where actno = ?";
                    stat2 = conn.prepareStatement(sql2);
                    stat2.setDouble(1,money);
                    stat2.setNString(2,fromAct);
                    int count = stat2.executeUpdate();

                    String sql3 = "update t_act set balance = balance + ? where actno = ?";
                    stat3 = conn.prepareStatement(sql3);
                    stat3.setDouble(1,money);
                    stat3.setNString(2,toAct);
                    count = count + stat3.executeUpdate();

                    if(count!=2){
                        throw new AppException("app错误，请联系管理员");
                    }
                }
            }
        } catch (Exception e) {
            out.print(e.getMessage());
        }finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stat3!=null){
                try {
                    stat3.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stat2!=null){
                try {
                    stat2.close();
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
}
