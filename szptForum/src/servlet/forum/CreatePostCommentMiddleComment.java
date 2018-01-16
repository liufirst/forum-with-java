package servlet.forum;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ado.DataBaseADO;

/*
 * ���ڴ���¥��¥��servlet
 */
@WebServlet("/CreatePostCommentMiddleComment.bin")
public class CreatePostCommentMiddleComment extends HttpServlet{

	// �ж��Ƿ�Ϸ�
	public boolean vail(String postid,String height,String editorvalue) {
		if(postid==null || postid.equals("") || height==null || height.equals("") || editorvalue==null || editorvalue.equals("")) {
			return false;
		}
		return true;
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		DataBaseADO ado = DataBaseADO.getAdo();
		String postid = request.getParameter("postid");
		String height = request.getParameter("height");
		String content = request.getParameter("editorValue");
		
		String username = "";
		
		// �����������
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie:cookies) {
			if(cookie.getName().equals("username")) {
				username = cookie.getValue();
			}
		}
		if(vail(postid,height,content)) {
			ado.InsertPostCommentMiddleComment(Integer.parseInt(postid), Integer.parseInt(height), username, content);
			response.sendRedirect("postweb.jsp?id="+postid);
		}else {
			// ���Ϸ��ض�����ҳ
			response.sendRedirect("index.jsp");
		}
	}
	
}
