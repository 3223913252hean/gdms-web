package com.oracle.gdms.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oracle.gdms.entity.UserMode;
import com.oracle.gdms.service.UserService;
import com.oracle.gdms.service.impl.AreaServiceImpl;
import com.oracle.gdms.service.impl.UserServiceImpl;
import com.oracle.gdms.util.GDMSUtil;

@WebServlet("/admin/user/login.php")
public class UserLoginAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		//所有提交过来的参数，接收以后封装成一个UserMode实体
		String account = request.getParameter("name");
		String password = request.getParameter("password");
		String code = request.getParameter("code");
		
		//
		HttpSession session =request.getSession();
		String yzm=(String)session.getAttribute("code");//从会话中获取正确的验证码
		//用过的验证码无效,销毁
		session.removeAttribute("code");
		
		if (yzm == null || !yzm.equalsIgnoreCase(code)) {
			request.setAttribute("login_msg", "验证码无效");
			request.getRequestDispatcher("../../index.jsp").forward(request, response);
			return; 
		}
		
		
//		System.out.println("account="+account);
//		System.out.println("password="+password);
		
		UserMode user = new UserMode();
		user.setAccount(account);
		try {
			user.setLogpwd(GDMSUtil.getMD5(password.getBytes()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//传给业务逻辑层的对象
		UserService userservice = new UserServiceImpl();
		UserMode loginUser = userservice.login(user);
		if(loginUser!=null) {
			//用户登录成功吧用户对象存进会话中
			session.setAttribute("loginUser", loginUser);
			response.sendRedirect("../main.jsp");  //重定向面页到admin/main.jsp
		}else {
			request.setAttribute("login_msg", "用户名或密码无效");
			request.getRequestDispatcher("../../index.jsp").forward(request, response);//执行转发
		}
		
	}
	

}
