<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/header.jsp" %>


<div class="container p-5">

    <!-- 수정삭제버튼 -->
    <div class="d-flex justify-content-end">
        <a href="/boards/${model.id}/update-form" class="btn me-1" style="background-color: #999AA0; color: white;">
            수정
        </a>
        <form action="/boards/${model.id}/delete" method="post">
            <button class="btn btn-outline-secondary">삭제</button>
        </form>
    </div>

    <!-- 게시글내용 -->
    <div>
        <h2><b>${model.title}</b></h2>
        <hr />
        <div class="d-flex justify-content-end">
            작성자 : 익명
        </div>
        <div class="m-4 p-2">
            ${model.content}
        </div>
    </div>

</div>

</body>
</html>
