package com.bjpn.mvc.service.impl;

import com.bjpn.mvc.dao.AccountDao;
import com.bjpn.mvc.dao.impl.AccountDaoImpl;
import com.bjpn.mvc.pojo.Account;
import com.bjpn.mvc.exceptions.AppException;
import com.bjpn.mvc.service.AccountService;
import com.bjpn.mvc.util.DBUtil;
import com.bjpn.mvc.exceptions.MoneyNotEnoughException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 业务类，专门处理业务
 */
public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao = new AccountDaoImpl();//获取数据库连接，定义到这里是因为所有业务方法都需要连接

    public void transfer(String fromActno,String toActno,Double money)
            throws MoneyNotEnoughException, AppException {
        try(Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);//关闭jdbc自动提交（为事务的开始）

            /**
             * 拿到转出账户的对象并判断余额是否足够
             */
            Account fromAct = accountDao.selectByActno(fromActno);
            if(fromAct.getBalance()<money){
                throw new MoneyNotEnoughException("抱歉，您的余额不足");
            }
            /**
             * 余额足够，拿到转入账户对象并执行操作
             */
            Account toAct = accountDao.selectByActno(toActno);
            fromAct.setBalance(fromAct.getBalance()-money);
            toAct.setBalance(toAct.getBalance()+money);
            int count = accountDao.update(fromAct);

            /*String t = null;
            t.toString();//模拟转账失败*/

            count += accountDao.update(toAct);
            if(count!=2){
                throw new AppException("app错误，转账失败，请联系管理员");
            }

            conn.commit();//手动提交事务
        } catch (SQLException e) {
            throw new AppException("app错误，转账失败，请联系管理员");
        }
    }
}
