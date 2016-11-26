<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/include-base-styles.jsp" %>
<title>已发布流程</title>
</head>
<body>
<div>
<table width="100%" class="table table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th>流程定义ID</th>
				<th>部署ID</th>
				<th>流程定义名称</th>
				<th>流程定义KEY</th>
				<th>版本号</th>
				<th>XML资源名称</th>
				<th>图片资源名称</th>
				<th width="80">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${processDefinitionList }" var="pd">
				<tr>
					<td>${pd.id }</td>
					<td>${pd.deploymentId }</td>
					<td>${pd.name }</td>
					<td>${pd.key }</td>
					<td>${pd.version }</td>
					<td><a target="_blank" href='${ctx }/read_resource.ctrl?pdid=${pd.id }&resourceName=${pd.resourceName }'>${pd.resourceName }</a></td>
					<td><a target="_blank" href='${ctx }/read_resource.ctrl?pdid=${pd.id }&resourceName=${pd.diagramResourceName }'>${pd.diagramResourceName }</a></td>
					<td><a target="_blank" href='${ctx }/chapter5/delete-deployment?deploymentId=${pd.deploymentId }'>删除</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

</body>
</html>