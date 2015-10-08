<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Contacts</title>
</head>
<body>

    <table border="1" align="center">
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/handler?action=pages&recPerPage=5&page=1">5</a>
                <a href="${pageContext.request.contextPath}/handler?action=pages&recPerPage=10&page=1">10</a>
                <a href="${pageContext.request.contextPath}/handler?action=pages&recPerPage=15&page=1">15</a>
            </td>
        </tr>
        <tr>
            <th><a href="${pageContext.request.contextPath}/handler?action=sort&recPerPage=${recPerPage}&sortParam=Name">NAME</a></th>
            <th><a href="${pageContext.request.contextPath}/handler?action=sort&recPerPage=${recPerPage}&sortParam=Surname">SURNAME</a></th>
            <th><a href="${pageContext.request.contextPath}/handler?action=sort&recPerPage=${recPerPage}&sortParam=Login">LOGIN</a></th>
            <th><a href="${pageContext.request.contextPath}/handler?action=sort&recPerPage=${recPerPage}&sortParam=Email">E-MAIL</a></th>
            <th><a href="${pageContext.request.contextPath}/handler?action=sort&recPerPage=${recPerPage}&sortParam=PhoneNumber">PHONE</a></th>
        </tr>

        <c:forEach var="contact" items="${contacts}">
            <tr>
                <td>${contact.name}</td>
                <td>${contact.surname}</td>
                <td>${contact.login}</td>
                <td>${contact.email}</td>
                <td>${contact.phoneNumber}</td>
            </tr>
        </c:forEach>
    </table>

    <table border="1" align="center">
        <tr>
            <c:if test="${currentPage != 1}">
                <td><a href="${pageContext.request.contextPath}/handler?action=pages&recPerPage=${recPerPage}&page=${currentPage - 1}">Previous</a></td>
            </c:if>
        </tr>
        <tr>
            <c:forEach begin="1" end="${numberOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="${pageContext.request.contextPath}/handler?action=pages&recPerPage=${recPerPage}&page=${i}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
        <tr>
            <c:if test="${currentPage lt sessionScope.numberOfPages}">
                <td><a href="${pageContext.request.contextPath}/handler?action=pages&recPerPage=${recPerPage}&page=${currentPage + 1}">Next</a></td>
            </c:if>
        </tr>
    </table>
    <p align="center"><a href="index.jsp">На главную</a></p>
</body>
</html>
