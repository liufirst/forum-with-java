package ado;

import java.sql.*;
import java.util.*;

import bean.forum.BigPlates;
import bean.forum.Plates;
import bean.forum.Post;
import bean.forum.PostComment;
import bean.forum.PostCommentMiddleComment;

public class DataBaseADO {
	private final static DataBaseADO ADO = new DataBaseADO();
	private Connection conn = null;
	private Statement state = null;
	
	public Connection getConn() {
		return conn;
	}

	private PreparedStatement prestate = null;
	private DataBaseADO() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://127.0.0.1:3306/szpt_forum";
		String user = "root";
		String password = "09043330";
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,password);
			state = conn.createStatement();
		}catch(Exception e) {e.printStackTrace();}
	}
	
	public synchronized ResultSet queryselect(String sql) {
		ResultSet rs = null;
		try {
			if(sql.indexOf("select")!=-1) {
				rs = state.executeQuery(sql);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return rs;
	}
	
	public synchronized boolean preinsert_auto(String sql,Object... list) {
		if(sql.indexOf("insert")!=-1||sql.indexOf("update")!=-1||sql.indexOf("delete")!=-1) {
			// �ж��Ƿ���values ��?,?,...?�������ۣ�û�о͸�sql������
			if(sql.indexOf("values")==-1) {
				// �Զ����ʺż���
				sql += "values(";
				for(int i=0;i<list.length;i++) {
					if(i==0)
						sql += "?";
					else
						sql += ",?";
				}
				sql += ")";
			}
			try {
				prestate = conn.prepareStatement(sql);
				for(int i=0;i<list.length;i++) {
					prestate.setString(i+1, list[i].toString());
				}
				return prestate.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}else {
			return false;
		}
	}

	public synchronized boolean preupdate(String sql,Object... list) {
		if(sql.indexOf("insert")!=-1||sql.indexOf("update")!=-1||sql.indexOf("delete")!=-1) {
			try {
				prestate = conn.prepareStatement(sql);
				for(int i=0;i<list.length;i++) {
					prestate.setString(i+1, list[i].toString());
				}
				return prestate.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}else {
			return false;
		}
	}
	
	//Ԥ��ѯ
	public synchronized ResultSet prequery_auto(String sql,Object...list) {
		if(sql.indexOf("select")!=-1) {
			try {
				prestate = conn.prepareStatement(sql);
				for(int i=0;i<list.length;i++) {
					prestate.setString(i+1, list[i].toString());
				}
				return prestate.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}else {
			return null;
		}		
	}
	
	
	//��ѯ�û����Ƿ��Ѿ�����
	public boolean checkuser(String username) {
		String sql = "select * from user where name='"+username+"'";
		ResultSet rs = this.queryselect(sql);
		try {
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkemail(String email) {
		String sql = "select * from user where email='"+email+"'";
		ResultSet rs = this.queryselect(sql);
		try {
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static DataBaseADO getAdo() {
		return ADO;
	} 
	
	
	/*========================================*/
	/*�û�����	  								  */
	/*========================================*/
	//�����û�����password��ѯ���鿴�Ƿ�������û������û�У���ô����false
	public boolean checkUserAndPassword(String username,String password) {
		String sql = "select * from user where name=? and password=?";
		ResultSet rs = this.prequery_auto(sql, username,password);
		try {
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	/*========================================*/
	/*���Ӳ���	  								  */
	/*========================================*/
	// ��������id�ҵ������������лظ���Ϣ����һ���������飨��������ΪPostComment������ʽ����
	public List<PostComment>getallpostcomment(int id){
		List<PostComment>list = new ArrayList<>();
		String sql = "select * from post_comment where postid=?";
		ResultSet rs = this.prequery_auto(sql, id);
		try {
			while(rs.next()) {
				PostComment postcomment = new PostComment();
				postcomment.setPostid(id);
				postcomment.setUsername(rs.getString("username"));
				postcomment.setTime(rs.getTimestamp("time"));
				postcomment.setContent(rs.getString("content"));
				postcomment.setAgreecount(rs.getInt("agreecount"));
				postcomment.setAgainstcount(rs.getInt("againstcount"));
				list.add(postcomment);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	// ��������id�ͻظ���¥�㣬�õ���һ��¥���е����ۣ����¥��¥��
	public List<PostCommentMiddleComment>getAllpostcomment_middle_comment(int postid,int height){
		List<PostCommentMiddleComment>list = new ArrayList<>();
		String sql = "select * from posts_comment_middle_comment where postid=? and height=?";
		ResultSet rs = this.prequery_auto(sql, postid,height);
		try {
			while(rs.next()) {
				PostCommentMiddleComment pcmc = new PostCommentMiddleComment();
				pcmc.setContent(rs.getString("content"));
				pcmc.setHeight(height);
				pcmc.setPostid(postid);
				pcmc.setTime(rs.getTimestamp("time"));
				pcmc.setUsername(rs.getString("username"));
				list.add(pcmc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.size()==0?null:list;
	}
	//����ҳ��page���������page*10-page*10+10λ�õ�����,plateid:�����������id
	public List<Post>GetPostWithPage(int page,int plateid){
		List<Post>list = new ArrayList<Post>();
		String sql = "select * from posts where plateid=? limit ?,?";
		try {
			PreparedStatement prestmt = this.conn.prepareStatement(sql);
			prestmt.setInt(1, plateid);
			prestmt.setInt(2, page*10);
			prestmt.setInt(3, 10);
			ResultSet rs = prestmt.executeQuery();
		
			while(rs.next()) {
				Post post = new Post();
				post.setCreatetime(rs.getTimestamp("createtime"));
				post.setCreatorname(rs.getString("creatorname"));
				post.setId(rs.getInt("id"));
				post.setTitle(rs.getString("title"));
				post.setReplycount(rs.getInt("replycount"));
				post.setViewcount(rs.getInt("viewcount"));
				list.add(post);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.size()==0?null:list;
	}
	//��������id�����������ӵĻظ�����,Ҳ�൱��¥���
	public long getPostReplyCount(int id) {
		String sql = "select * from posts where id=?";
		ResultSet rs = this.prequery_auto(sql, id);
		try {
			rs.last();
			return rs.getRow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	/*========================================*/
	/*��鲿��	  								  */
	/*========================================*/
	//�ҵ����д��鲿��,����һ���б�
	public List<BigPlates>getAllBigPlates(){
		List<BigPlates>list = new ArrayList<>();
		String sql = "select * from bigplates";
		ResultSet rs = this.queryselect(sql);
		try {
			while(rs.next()) {
				BigPlates bp = new BigPlates();
				bp.setId(rs.getInt("id"));
				bp.setName(rs.getString("name"));
				list.add(bp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	//���ݴ����id���ҵ��ô����µ�����С���
	public List<Plates>getAllPlates(int BigPlatesId){
		List<Plates>list = new ArrayList<>();
		String sql = "select * from plates where bigplates=?";
		ResultSet rs = this.prequery_auto(sql, BigPlatesId);
		try {
			while(rs.next()) {
				Plates plates = new Plates();
				plates.setId(rs.getInt("id"));
				plates.setName(rs.getString("name"));
				plates.setBigplatesid(rs.getInt("bigplates"));
				list.add(plates);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	// ����һ������������������
	public long getPostCount(int plateid) {
		String sql = "select * from posts where plateid=?";
		ResultSet rs = this.prequery_auto(sql, plateid);
		long count = 0;
		try {
			rs.last();
			return rs.getRow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	//�õ�һ���������·����һ������
	public Post getLastlyPost(int plateid) {
		String sql = "select * from posts where plateid=?";
		ResultSet rs = this.prequery_auto(sql, plateid);
		try {
			if(rs.next()) {
				Post post = new Post();
				post.setId(rs.getInt("id"));
				post.setTitle(rs.getString("title"));
				post.setCreatorname(rs.getString("creatorname"));
				post.setPlateid(rs.getInt("plateid"));
				post.setReplycount(rs.getInt("replycount"));
				post.setCreatetime(rs.getTimestamp("createtime"));
				post.setViewcount(rs.getInt("viewcount"));
				return post;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
