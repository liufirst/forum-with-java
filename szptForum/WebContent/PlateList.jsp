<%@page import="ado.DataBaseADO"%>
<%
	/*
		用于展示板块内容
	*/
%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="java.util.*,bean.forum.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="./CSS/style_11_common.css">
	<link rel="stylesheet" id="css_widthauto" type="text/css" href="./CSS/style_11_widthauto.css">
	<link rel="stylesheet" type="text/css" href="./CSS/temp_style.css">
	<link rel="stylesheet" type="text/css" href="./CSS/style_11_forum_index.css">
	<script src="JS/jquery-3.2.1.min.js"></script>
</head>
<%
	// 获得大板块列表
	DataBaseADO ado = DataBaseADO.getAdo();
	List<BigPlates>BigPlateslist = ado.getAllBigPlates();
	pageContext.setAttribute("BigPlateslist", BigPlateslist);
%>
<div class="fl bm">
	<div class="bm bmw  flg cl">
		<!-- 显示所有大板块 -->
		<c:forEach items="${BigPlateslist}" var="bigplates">
			<!-- 大板块标题 -->
			<div class="bm_h cl">
				<h2>
					<a href="#" style="">${bigplates.getName()}</a>
				</h2>
			</div>		

			<%
				//通过大板块的id，拿到这个大板块下的所有小版块
				// 得到当前大板块id
				int id = ((BigPlates)pageContext.getAttribute("bigplates")).getId();
				//获得大板块里面各个板块的列表
				List<Plates>PlatesList = ado.getAllPlates(id);
				pageContext.setAttribute("PlatesList", PlatesList);
			%>
			<!-- 此处显示小版块内容 -->
			<div id="category_70" class="bm_c" style="">
				<table cellspacing="0" cellpadding="0" class="fl_tb">
					<c:forEach items="${PlatesList}" var="plates">
						<tr>
							<td class="fl_g" width="49.9%">
								<div class="fl_icn_g" style="width: 64px;">
									<a href="#"><img
										src="#" align="left"
										alt=""></a>
								</div>
								<dl style="margin-left: 64px;">
									<dt>
										<a href="#">${plates.getName()}</a>
									</dt>
									<dd>
										<em>主题: 496</em>, <em>帖数: <span title="37175">3万</span></em>
									</dd>
									<dd>
										<a
											href="#"
											class="xi2">【短十】数码物语-第三章 [宠物 ...</a> <cite><span
											title="2018-1-13 00:03">昨天&nbsp;00:03</span> <a
											href="#">寡人</a></cite>
									</dd>
								</dl>
							</td>
						</tr>
					</c:forEach>					
				</table>
			</div>
		</c:forEach>
	</div>
</div>