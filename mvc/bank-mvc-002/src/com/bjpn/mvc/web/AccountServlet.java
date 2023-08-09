package com.bjpn.mvc.web;

import com.bjpn.mvc.exceptions.MoneyNotEnoughException;
import com.bjpn.mvc.service.AccountService;
import com.bjpn.mvc.service.impl.AccountServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * AccountServlet是一个司令官，它负责调度其他组件
 */
@WebServlet("/servlet")
public class AccountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String fromAct = request.getParameter("fromAct");
        String toAct = request.getParameter("toAct");
        Double money = Double.parseDouble(request.getParameter("money"));
        AccountService accountService = new AccountServiceImpl();
        try {
            accountService.transfer(fromAct,toAct,money);
            out.print("转账成功");
        } catch (MoneyNotEnoughException e) {
            response.sendRedirect(request.getContextPath()+"/moneynotenough.jsp");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()+"/error.jsp");
        }
    }
}
