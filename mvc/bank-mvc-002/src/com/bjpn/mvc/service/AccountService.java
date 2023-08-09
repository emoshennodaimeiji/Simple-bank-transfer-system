package com.bjpn.mvc.service;

import com.bjpn.mvc.exceptions.AppException;
import com.bjpn.mvc.exceptions.MoneyNotEnoughException;

public interface AccountService {
    void transfer(String fromActno,String toActno,Double money)throws MoneyNotEnoughException, AppException;
}
