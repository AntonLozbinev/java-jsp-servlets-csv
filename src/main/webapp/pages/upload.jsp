<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<html>
<head>
    <title>Upload</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/handler" method="post" enctype="multipart/form-data">
        <table align="center">
            <tr>
                <td align="center">
                    <p>Выберите CSV файл для загрузких данных в базу</p>
                    <input type="file" name="userFile" pattern="\w+\.*\w*\.csv" required>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <p>Выберите разделитель</p>
                    <select name="separator" required>
                        <option selected value=",">,</option>
                        <option value=";">;</option>
                        <option value="|">|</option>
                    </select>
                    <input type="hidden" name="action" value="upload">
                </td>
            </tr>
            <tr>
                  <td align="center">
                      <input type="submit" value="Upload">
                  </td>
            </tr>
            <tr>
                <td align="center">
                    <a href="../index.jsp">На главную</a>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
