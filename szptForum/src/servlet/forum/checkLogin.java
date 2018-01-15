package servlet.forum;

import java.io.IOException;

/*
 *	��������¼��servlet������˺ţ����룬��֤�룬�Ƿ���ȷ����ȷ���ض�����ҳ��ͬʱ����cookies
 *	���û���cookies={
 *		isLogin:true;
 *		username:�˺�;
 *		eamil:����;
 *	} 
 *  �������ȷ���ض���ص�¼ҳ��
 * 
 */

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ado.DataBaseADO;

@WebServlet("/checkLogin.bin")
public class checkLogin extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String seccodeverify = request.getParameter("seccodeverify");
		// ��ѯ���ݿ⣬�鿴�Ƿ��û�����������ȷ
		DataBaseADO ado = DataBaseADO.getAdo();
		if(!ado.checkUserAndPassword(username, password)) {
			response.getWriter().println("<script>alert('�û������������');</script>");
			System.out.println("�û������������");
			response.sendRedirect("Login.jsp");
			return;
		}
		// �����֤���Ƿ���д��ȷ
		HttpSession session = request.getSession();
		String true_graphcode = (String) session.getAttribute("graphhashcode");
		//��������֤�붼���Сд��ĸ��Ȼ��Ƚ�
		true_graphcode = true_graphcode.toLowerCase();
		seccodeverify = seccodeverify.toLowerCase();
		if(!true_graphcode.equals(seccodeverify)) {
			response.getWriter().println("<script>alert('��֤�����');</script>");
			System.out.println("��֤�����");
			response.sendRedirect("Login.jsp");
			return;			
		}
		
		// �������ּ�飬��֤����û��������붼��ȷ���ض�����ҳ,ͬʱΪ�û�����cookie
		Cookie cookie = new Cookie("username", username);
		Cookie cookie2 = new Cookie("isLogin", "true");
		response.addCookie(cookie);
		response.addCookie(cookie2);
		response.sendRedirect("index.jsp");
	}
	
}
