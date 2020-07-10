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

@WebServlet("/admin/user/reg.php")
public class UserRegAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		//所有提交过来的参数，接收以后封装成一个UserMode实体
		String account = request.getParameter("name");
		String password = request.getParameter("password");
		String realname = request.getParameter("realname");
		String slogo = request.getParameter("slogo");
		String idnumber = request.getParameter("idnumber");
		String place = request.getParameter("prov");
		String city = request.getParameter("city");
		
		int pid = Integer.parseInt(place); //省份的id
		String pname = new AreaServiceImpl().findNameById(pid).getName();//查询省份名称
		int cid = Integer.parseInt(city); //城市的id
		String cname = new AreaServiceImpl().findNameById(cid).getName();//查询城市名称
		
		place =pname+"-"+cname;
		
		String code = request.getParameter("code");
		
		//
		HttpSession session =request.getSession();
		String yzm=(String)session.getAttribute("code");
		//用过的验证码无效,销毁
		session.removeAttribute("code");
		
		if (yzm == null || !yzm.equalsIgnoreCase(code)) {
			request.setAttribute("err_msg", "验证码无效");
			System.out.println("-----------------");
			request.getRequestDispatcher("../../index.jsp").forward(request, response);
			return; 
		}
		
		
//		System.out.println("account="+account);
//		System.out.println("password="+password);
//		System.out.println("realname="+realname);
//		System.out.println("slogo="+slogo);
//		System.out.println("idnumber="+idnumber);
//		System.out.println("place="+place);
//		System.out.println("code="+code);
		
		UserMode user = new UserMode();
		user.setAccount(account);
		try {
			user.setLogpwd(GDMSUtil.getMD5(password.getBytes()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		user.setIdnumber(idnumber);
		user.setPlace(place);
		user.setPhoto(slogo);
		user.setRealname(realname);
		
		//取得性别
		char c =idnumber.charAt(idnumber.length()-2);
		int i = Integer.parseInt(String.valueOf(c));
		String sex =i % 2 == 0 ? "女" :"男";
		
		user.setRowflag("oraU" + GDMSUtil.generic(20));
		user.setRoleid(3);
		user.setSex(sex);
		
		//取得生日
		String s =idnumber.substring(6,14); //取子串,包含6，不包含14
		//把字符串类型的日期转成Timestamp类型
		SimpleDateFormat sdf = new SimpleDateFormat();
		try {
			int yy=Integer.parseInt(s.substring(0,4));//取得年份
			int mm=Integer.parseInt(s.substring(4,6));//取得月份
			int dd=Integer.parseInt(s.substring(6));//取得天数
			Date d= new Date(yy-1900,mm-1,dd);
			Timestamp birthday =new Timestamp(d.getTime());
			user.setBirthday(birthday);
			
			Timestamp regtime = GDMSUtil.now();
			user.setRegtime(regtime);
			user.setLastlogin(regtime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setStatus(false);
		
		//传给业务逻辑层的对象
		UserService userservice = new UserServiceImpl();
		int count = userservice.add(user);
		
		if (count >0) {
			request.setAttribute("msg", "注册成功，请登录");
		}else {
			request.setAttribute("err_msg", "Sorry,注册失败，请稍后重试");
		}
		request.getRequestDispatcher("../../index.jsp").forward(request, response);//执行转发
	}
	

}
