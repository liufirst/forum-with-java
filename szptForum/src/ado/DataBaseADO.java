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
			// 判断是否有values （?,?,...?）等字眼，没有就给sql语句加上
			if(sql.indexOf("values")==-1) {
				// 自动把问号加上
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
	
	//预查询
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
	
	
	//查询用户名是否已经存在
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
	/*用户部分	  								  */
	/*========================================*/
	//根据用户名和password查询，查看是否有这个用户，如果没有，那么返回false
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
	/*帖子部分	  								  */
	/*========================================*/
	// 根据帖子id找到该帖子下所有回复信息，以一个对象数组（对象类型为PostComment）的形式返回
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
	// 根据帖子id和回复的楼层，得到这一层楼所有的评论（简称楼中楼）
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
	//根据页面page参数，获得page*10-page*10+10位置的数据,plateid:帖子所属板块id
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
	//根据帖子id，获得这个帖子的回复数量,也相当于楼层吧
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
	/*板块部分	  								  */
	/*========================================*/
	//找到所有大板块部分,返回一个列表
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
	//根据大板块的id，找到该大板块下的所有小版块
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
	// 计算一个板块的所有帖子数量
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
	//得到一个板块的最新发表的一个帖子
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
