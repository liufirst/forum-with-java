import ado.DataBaseADO;
import bean.forum.PostComment;

// ��idΪx�����Ӳ�����������
public class Test6 {
	public static void main(String[] args) {
		int id = 12;
		for(int i=1;i<=10;i++) {
			DataBaseADO ado = DataBaseADO.getAdo();
			String sql = "insert into post_comment(postid,username,content,agreecount,againstcount) values(?,?,?,?,?)";
			ado.preinsert_auto(sql, id,"sjm","�������̫˧�˰ɣ�����������"+i,100,0);
		}
	}
}
