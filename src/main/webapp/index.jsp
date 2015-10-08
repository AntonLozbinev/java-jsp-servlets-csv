<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Main</title>
    <style>
        #div {
            margin: 0 auto;
            text-align: center;
        }
    </style>
</head>
<body>
    <div id="div">
        <p>Вы хотетите загрузить данные в базу</p>
        <button><a href="pages/upload.jsp">Upload</a></button>

        <p>Вы хотетите просмотреть данные в базе</p>
        <button><a href="${pageContext.request.contextPath}/handler?action=pages&page=1">Overview</a></button>
    </div>
</body>
</html>
