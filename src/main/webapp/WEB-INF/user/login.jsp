<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/bootstrap.jsp" %>
<%@ include file="/common/navbar.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>精誠娛樂城</title>
</head>
<body class="d-flex flex-column min-vh-100">

<div class="container-fluid h-custom">
    <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col-md-9 col-lg-6 col-xl-5">
            <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.webp"
                 class="img-fluid" alt="Sample image">
        </div>
        <div class="col-md-8 col-lg-6 col-xl-4 offset-xl-1">
            <form method="POST" action="${pageContext.request.contextPath}/user/login">
                <!-- Email input -->
                <div class="form-outline mb-4">
                    <label class="form-label" for="email">信箱</label>
                    <input type="email" class="form-control form-control-lg" id="email" name="email"
                           placeholder="輸入信箱" value="${param.email}"/>
                    <c:if test="${not empty errorMsg.email}">
                        <small class="text-danger">${errorMsg.email}</small>
                    </c:if>
                </div>

                <!-- Password input -->
                <div class="form-outline mb-3">
                    <label class="form-label" for="password">密碼</label>
                    <input type="password" class="form-control form-control-lg" id="password" name="password"
                           placeholder="輸入密碼"/>
                    <c:if test="${not empty errorMsg.password}">
                        <small class="text-danger">${errorMsg.password}</small>
                    </c:if>
                </div>

                <div class="text-center text-lg-start mt-4 pt-2">
                    <button type="submit" class="btn btn-primary btn-lg">登入</button>
                    <p class="small fw-bold mt-2 pt-1 mb-0">Don't have an account?
                        <a href="<%=request.getContextPath()%>/user/register" class="link-danger">現在註冊</a>
                    </p>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="switch position-absolute top-0 end-0 m-3">
    <!-- 切換到 Login 頁面 -->
    <button class="btn btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/user/login'">進入
        LoginForm 頁面
    </button>
    <!-- 切換到 LoginAjax 頁面 -->
    <button class="btn btn-secondary"
            onclick="window.location.href='${pageContext.request.contextPath}/user/loginAjax'">進入 LoginAjax 頁面
    </button>
</div>
<%
    // 清除 session 中的 errorMsg
    request.getSession().removeAttribute("errorMsg");
%>
<%@ include file="/common/footer.jsp" %>


</body>
</html>
