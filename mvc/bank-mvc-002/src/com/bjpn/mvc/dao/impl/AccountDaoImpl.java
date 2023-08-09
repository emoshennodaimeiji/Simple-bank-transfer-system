package com.bjpn.mvc.dao.impl;

import com.bjpn.mvc.dao.AccountDao;
import com.bjpn.mvc.pojo.Account;
import com.bjpn.mvc.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 什么是DAO
 *  data access object 数据访问对象
 * DAO属于javaEE设计模式之一
 * AccountDao只负责数据库的CRUD，没有任何业务逻辑
 */
public class AccountDaoImpl implements AccountDao {
    public int insert(Account act){
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            conn = DBUtil.getConnection();
            String sql = "insert into t_act(actno,balance) values(?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,act.getActno());
            ps.setDouble(2,act.getBalance());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(ps,null);
        }
        return count;
    }

    public int deleteById(String id){
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            conn = DBUtil.getConnection();
            String sql = "delete from t_act where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,id);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(ps,null);
        }
        return count;
    }

    public int update(Account act){
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            conn = DBUtil.getConnection();
            String sql = "update t_act set actno = ?,balance = ? where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,act.getActno());
            ps.setDouble(2,act.getBalance());
            ps.setLong(3,act.getId());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(ps,null);
        }
        return count;
    }

    public Account selectByActno(String actno){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Account act = new Account();
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_act where actno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,actno);
            rs = ps.executeQuery();
            if(rs.next()){
                Long id = rs.getLong("id");
                String actno2 = rs.getString("actno");
                Double balance = rs.getDouble("balance");
                act = new Account(id,actno2,balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(rs,ps,null);
        }
        return act;
    }

    public List<Account> selectAll(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Account> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_act";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Long id = rs.getLong("id");
                String actno2 = rs.getString("actno");
                Double balance = rs.getDouble("balance");
                Account act = new Account(id,actno2,balance);
                list.add(act);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(rs,ps,null);
        }
        return list;
    }
}
