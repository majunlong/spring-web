<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="mvc" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>EMPLOYEES</title>
<script type="text/javascript">
	require(["view/restful/employees"]);
</script>
</head>
<body>
	<input type="hidden" id="initPageNumber" value="${requestScope.employee.pageNumber}">
	<input type="hidden" id="initPageSize" value="${requestScope.employee.pageSize}">
	<input type="hidden" id="initPages" value="${requestScope.employee.pages}">
	<mvc:form action="${pageContext.request.contextPath }/restful/employees" method="post" id="employee" modelAttribute="employee">
		<input type="hidden" name="_method" value="PATCH">
		<table>
			<thead>
				<tr>
					<td colspan="9">
						<mvc:input cssClass="list" path="name" placeholder="姓名"/><mvc:select
						cssClass="list" path="gender" items="${requestScope.genders }"/><mvc:select
						cssClass="list" path="department.id" items="${requestScope.departments }" itemLabel="name" itemValue="id"/><button
						class="list">查询</button><button
						class="list" onclick="window.location.href='${pageContext.request.contextPath }/restful/employee';return false;">添加</button>
					</td>
				</tr>
				<tr>
					<th width="11%">序号</th>
					<th width="11%">头像</th>
					<th width="11%">姓名</th>
					<th width="11%">性别</th>
					<th width="11%">生日</th>
					<th width="11%">邮箱</th>
					<th width="11%">工资</th>
					<th width="11%">部门</th>
					<th width="12%">操作</th>
				</tr>
				<tr>
					<td colspan="9"></td>
				</tr>
			</thead>
			<tbody style="color: #444;">
				<c:forEach var="employee" varStatus="i" items="${requestScope.employees }">
					<tr>
						<td>${i.index + 1 }</td>
						<td>
							<a href="${pageContext.request.contextPath }/restful/employee/portrait/${employee.id }">
								<img src="${pageContext.request.contextPath }/restful/employee/portrait/${employee.id }" width="50px" height="50px">
							</a>
						</td>
						<td>${employee.name }</td>
						<td>${employee.gender.value }</td>
						<td><fmt:formatDate value="${employee.birthday }" pattern="yyyy-MM-dd"/></td>
						<td>${employee.email }</td>
						<td><fmt:formatNumber value="${employee.salary }" pattern="￥#,###.00"/></td>
						<td>${employee.department.name }</td>
						<td>
							<a href="${pageContext.request.contextPath }/restful/employee/${employee.id }">修改</a>
							<a href="${pageContext.request.contextPath }/restful/employee/${employee.id }" class="delete">删除</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td id="pagingLocation" colspan="9"></td>
				</tr>
			</tfoot>
		</table>
	</mvc:form>
</body>
</html>
