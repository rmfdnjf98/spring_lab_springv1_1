<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/views/header.jsp" %>

<div class="container mt-3">
  <table class="table table-hover">
    <thead>
      <tr>
        <th>번호</th>
        <th>제목</th>
        <th>내용</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach var="board" items="${models}">
      <tr onclick="location.href='/boards/${board.id}'" style="cursor: pointer;">
        <td>${board.id}</td>
        <td>${board.title}</td>
        <td>${board.content}</td>
      </tr>
    </c:forEach>   
    </tbody>
  </table>
</div>
</body>
</html>