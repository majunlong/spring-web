<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="mvc" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>EMPLOYEE</title>
</head>
<body>
	<mvc:form action="${pageContext.request.contextPath }/restful/employee" method="post" id="employee" modelAttribute="employee" enctype="multipart/form-data">
		<div class="table">
			<c:if test="${ empty requestScope.employee.id }">
				<div>
					<label class="save">头像</label>
					<input class="save" name="portrait" type="file">
					<span class="save"><mvc:errors  path="portrait"/>&nbsp;</span>
				</div>
			</c:if>
			<div>
				<label class="save">姓名</label>
				<mvc:input cssClass="save" path="name"/>
				<span class="save"><mvc:errors  path="name"/>&nbsp;</span>
			</div>
			<div>
				<label class="save">性别</label>
				<mvc:select cssClass="save" path="gender" items="${requestScope.genders }"/>
				<span class="save"><mvc:errors  path="gender"/>&nbsp;</span>
			</div>
			<c:if test="${ empty requestScope.employee.id }">
				<div>
					<label class="save">生日</label>
					<mvc:input cssClass="save" path="birthday" type="date"/>
					<span class="save"><mvc:errors  path="birthday"/>&nbsp;</span>
				</div>
			</c:if>
			<c:if test="${!empty requestScope.employee.id }">
				<mvc:hidden path="id"/>
				<input type="hidden" name="_method" value="put">
			</c:if>
			<div>
				<label class="save">邮箱</label>
				<mvc:input cssClass="save" path="email"/>
				<span class="save"><mvc:errors  path="email"/>&nbsp;</span>
			</div>
			<div>
				<label class="save">工资</label>
				<mvc:input cssClass="save" path="salary"/>
				<span class="save"><mvc:errors  path="salary"/>&nbsp;</span>
			</div>
			<div>
				<label class="save">部门</label>
				<mvc:select cssClass="save" path="department.id" items="${requestScope.departments }" itemLabel="name" itemValue="id"/>
				<span class="save"><mvc:errors  path="department.id"/>&nbsp;</span>
			</div>
			<hr style="margin:8px 2px 8px 2px;border:0;height:1px;background:#000;">
			<div>
				<a class="save" href="${pageContext.request.contextPath }/restful/employees">返回</a>
				<c:if test="${ empty requestScope.employee.id }">
					<a class="save"  href="//" onclick="employee.submit();return false;">保存</a>
				</c:if>
				<c:if test="${!empty requestScope.employee.id }">
					<a class="save"  href="//" onclick="employee.enctype='application/x-www-form-urlencoded';employee.submit();return false;">更新</a>
				</c:if>
			</div>
		</div>
	</mvc:form>
</body>
</html>
