<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/bootstrap.jsp" %>
<%@ include file="/common/navbar.jsp" %>

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
            <form method="POST" action="${pageContext.request.contextPath}/user/loginAjax">
                <!-- Email input -->
                <div class="form-outline mb-4">
                    <label class="form-label" path="email">信箱</label>
                    <input type="email" class="form-control form-control-lg" path="email"
                           placeholder="輸入信箱" id="email"/>
                </div>
                <div id="error-message-mail" class="text-danger mb-3" style="display: none;"></div>
                <!-- Password input -->
                <div class="form-outline mb-3">
                    <label class="form-label" path="password">密碼</label>
                    <input type="password" class="form-control form-control-lg"
                           placeholder="輸入密碼" path="password" id="password"/>
                </div>

                <!-- Error message area -->
                <div id="error-message-password" class="text-danger mb-3" style="display: none;"></div>

                <div class="text-center text-lg-start mt-4 pt-2">
                    <input type="button" class="btn btn-primary btn-lg" value="登入" id="login"/>
                    <p class="small fw-bold mt-2 pt-1 mb-0">Don't have an account? <a
                            href="<%=request.getContextPath()%>/user/register"
                            class="link-danger">現在註冊</a></p>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="switch">
    <button class="btn btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/user/login'">進入
        LoginForm 頁面
    </button>
    <button class="btn btn-secondary"
            onclick="window.location.href='${pageContext.request.contextPath}/user/loginAjax'">進入 LoginAjax 頁面
    </button>
</div>
<%@ include file="/common/footer.jsp" %>

<script type="module">
    import {loginAPI} from '<%=request.getContextPath()%>/js/userAPI.js';

    document.getElementById("login").addEventListener("click", async (e) => {
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const res = await loginAPI({email: email, password: password});
        const errorMessagepasswordDiv = document.getElementById("error-message-password");
        const errorMessagemailDiv = document.getElementById("error-message-mail");

        errorMessagepasswordDiv.style.display = "none";
        errorMessagemailDiv.style.display = "none";

        if (!res.data.responseCode) { //如果登入成功，會回傳整個頁面，就不是json
            Swal.fire({
                title: "登入成功",
                text: "已成功登入，可以進入遊樂場",
                icon: "success"
            }).then(() => {
                document.write(res.data);
            });
        } else if ((res.data.responseCode === 3002)) {
            errorMessagepasswordDiv.textContent = "信箱或密碼錯誤";
            errorMessagepasswordDiv.style.display = "block";
        } else if ((res.data.responseCode === 3001)) {
            const errorMsg = res.data.errorMsg;
            const mailErrorMsg = errorMsg.email;
            const passwordErrorMsg = errorMsg.password
            if (mailErrorMsg !== undefined) {
                errorMessagemailDiv.textContent = mailErrorMsg
                errorMessagemailDiv.style.display = "block";
            }else {
                errorMessagemailDiv.textContent = "";
                errorMessagemailDiv.style.display = "none";
            }
            if (passwordErrorMsg !== undefined) {
                errorMessagepasswordDiv.textContent = passwordErrorMsg;
                errorMessagepasswordDiv.style.display = "block";
            }else {
                errorMessagepasswordDiv.textContent = "";
                errorMessagepasswordDiv.style.display = "none";
            }

        }
    });
</script>
</body>
</html>
