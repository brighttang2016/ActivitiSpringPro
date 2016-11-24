<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/include-base-styles.jsp" %>
<title>任务列表</title>
</head>
<body>
<div>
<table width="100%" class="table table-bordered table-hover table-condensed">
		<thead>
			 <tr>  
	            <th>任务id</th>  
	            <th>任务name</th>  
	            <th>流程实例id</th>  
	            <th>责任人</th>  
	            <th>流程定义标识</th>  
	            <th>流程实例标识</th>  
	            <th>操作</th>  
	        </tr>  
		</thead>
		<tbody>
			<c:forEach items="${taskList}" var="task">
				<tr>
					<td>${task.id }</td>  
		            <td>${task.name}</td>  
		            <td>${task.processInstanceId }</td>  
		            <td>${task.assignee }</td>  
		            <td>${task.processDefinitionId }</td>  
		            <td>${task.executionId }</td> 
		            <td><a target="_blank" href='${ctx}/read_taskimage.ctrl?pdid=${task.processDefinitionId }&executionId=${task.executionId}'>查看</a></td> 
		            <td>  
						<a href="<c:url value='/flow/showImage' />/${task.processDefinitionId}/${task.processInstanceId}">查看流程图</a>  
		                <a href="<c:url value='/flow/toCheck' />/${task.id }">审批</a>  
		            </td> 
		            <td><a href=""/></td> 
					<%-- <td><a target="_blank" href='${ctx }/read_resource.ctrl?pdid=${pd.id }&resourceName=${pd.resourceName }'>${pd.resourceName }</a></td>
					<td><a target="_blank" href='${ctx }/read_resource.ctrl?pdid=${pd.id }&resourceName=${pd.diagramResourceName }'>${pd.diagramResourceName }</a></td>
					<td><a target="_blank" href='${ctx }/chapter5/delete-deployment?deploymentId=${pd.deploymentId }'>删除</a></td> --%>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

</body>
</html>