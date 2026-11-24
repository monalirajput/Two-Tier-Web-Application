<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html><html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Student Management | AWS 2-Tier</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css"></head>
<body><main class="container">
<header><div><p class="eyebrow">AWS · 2-Tier Architecture</p><h1>Student Management</h1><p class="sub">Java · Apache Tomcat · Amazon RDS MySQL/MariaDB</p></div><span class="tag">Portfolio Project</span></header>
<c:if test="${not empty error}"><div class="alert"><c:out value="${error}"/></div></c:if>
<c:if test="${param.success eq '1'}"><div class="success">Student record updated successfully.</div></c:if>
<section class="panel"><h2>Add a student</h2>
<form method="post" action="${pageContext.request.contextPath}/students" class="form-grid">
<label>Full name<input name="name" maxlength="100" required placeholder="e.g. Aditi Sharma"></label>
<label>Address<input name="address" maxlength="100" required placeholder="City, State"></label>
<label>Age<input name="age" type="number" min="1" max="120" required></label>
<label>Qualification<input name="qualification" maxlength="20" required placeholder="B.E. / B.Sc."></label>
<label>Percentage / CGPA<input name="percentage" maxlength="10" required></label>
<label>Year passed<input name="yearPassed" maxlength="10" required></label>
<div class="full"><button type="submit">Save student</button></div>
</form></section>
<section class="panel"><div class="section-heading"><h2>Student records</h2><form method="get" action="${pageContext.request.contextPath}/students" class="search"><input name="q" value="<c:out value='${query}'/>" placeholder="Search name or qualification"><button type="submit">Search</button></form></div>
<div class="table-wrap"><table><thead><tr><th>ID</th><th>Name</th><th>Address</th><th>Age</th><th>Qualification</th><th>Percentage</th><th>Year</th><th>Action</th></tr></thead><tbody>
<c:forEach var="s" items="${students}"><tr><td><c:out value="${s.id}"/></td><td><c:out value="${s.name}"/></td><td><c:out value="${s.address}"/></td><td><c:out value="${s.age}"/></td><td><c:out value="${s.qualification}"/></td><td><c:out value="${s.percentage}"/></td><td><c:out value="${s.yearPassed}"/></td><td><form method="post" action="${pageContext.request.contextPath}/students" onsubmit="return confirm('Delete this record?')"><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<c:out value='${s.id}'/>"><button class="danger" type="submit">Delete</button></form></td></tr></c:forEach>
<c:if test="${empty students}"><tr><td colspan="8" class="empty">No records found yet. Add a student above.</td></tr></c:if>
</tbody></table></div></section>
<footer>Student Management Web Application · Built for AWS EC2 + RDS</footer>
</main></body></html>
