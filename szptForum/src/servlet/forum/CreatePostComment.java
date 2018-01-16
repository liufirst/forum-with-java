package servlet.forum;

import java.io.IOException;

/*
 * ���ڴ��������۵�servlet
 */

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ado.DataBaseADO;

@WebServlet("/CreatePostComment.bin")
public class CreatePostComment extends HttpServlet{
	String postid;
	String editorValue;
	String author = "";
	String PostTitle;
	String plateid;
	
	// ������ݵĺϷ���
	public boolean vail() {
		if(editorValue==null || editorValue.equals("") || author==null || author.equals("")) {
			return false;
		}
		if(postid.equals("")) {
			System.out.println("postitle.equals('')"+PostTitle.equals(""));
			if(PostTitle.equals("") || plateid.equals("")) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected void doPost(HttpServletRequest requests, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			requests.setCharacterEncoding("utf-8");
			DataBaseADO ado = DataBaseADO.getAdo();
			postid = requests.getParameter("postid");
			editorValue = requests.getParameter("editorValue");
			// �����������
			Cookie[] cookies = requests.getCookies();
			for(Cookie cookie:cookies) {
				if(cookie.getName().equals("username")) {
					author = cookie.getValue();
				}
			}
			
			//�ж��Ǵ������ӻ��Ƕ�һ�����ӷ������ۣ����postidΪ�վ��Ǵ������ӣ������Ϊ�գ����Ƕ�һ�����ӷ�������
			if(postid.equals("")) {
				//������ӱ���
				PostTitle = requests.getParameter("posttitle");
				// ��ð��id
				plateid = requests.getParameter("plateid");
				
				System.out.println("posttitle:"+PostTitle);
				System.out.println("plateid:"+plateid);
				System.out.println("author:"+author);
				// ������ݺϷ��ԣ�������Ϸ���ô�ض��򵽷��������Ǹ�ҳ��
				if(!vail()) {
					response.sendRedirect("CreatePost.jsp?plateid="+plateid);
					return;
				}
				
				// �����ݿ��в���һ������
				ado.InsertPost(plateid, author, PostTitle);
				// �����ݿ����һ����������
				int id = ado.getIDPostLast(Integer.parseInt(plateid));
				ado.InsertPostComment(id, author, editorValue);
				
				// �ض�������ҳ��
				response.sendRedirect("postweb.jsp?id="+id);
			}else {
				// ������ݺϷ��ԣ�������Ϸ���ô�ض��򵽷��������Ǹ�ҳ��
				if(!vail()) {
					response.sendRedirect("CreatePost.jsp?postid="+postid);
					return;
				}
				// �����ӷ�������
				ado.InsertPostComment(Integer.parseInt(postid), author, editorValue);
				// �ض�������ҳ��
				response.sendRedirect("postweb.jsp?id="+postid);				
			}
	}
	
}
