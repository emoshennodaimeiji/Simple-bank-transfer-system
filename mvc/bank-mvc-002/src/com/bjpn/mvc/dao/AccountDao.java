package com.bjpn.mvc.dao;

import com.bjpn.mvc.pojo.Account;

import java.util.List;

public interface AccountDao {
    int insert(Account act);
    int deleteById(String id);
    int update(Account act);
    Account selectByActno(String actno);
    List<Account> selectAll();
}
