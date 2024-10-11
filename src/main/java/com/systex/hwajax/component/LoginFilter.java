package com.systex.hwajax.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.hwajax.dto.HttpResponseData;
import com.systex.hwajax.dto.user.UserDTO;
import com.systex.hwajax.model.user.LoginForm;
import com.systex.hwajax.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

import static com.systex.hwajax.constant.ResponseCodeConstant.EMAIL_REGEX;
import static com.systex.hwajax.constant.ResponseCodeConstant.LOGIN_DATA_ERROR;

@Component
@WebFilter(urlPatterns = {"/*"})
public class LoginFilter implements Filter {

    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession httpSession = request.getSession();
        String path = request.getRequestURI();
        String requestMethod = request.getMethod();
        //如果路徑是登入或註冊則放行
        if (path.endsWith("loginAjax")) {

            ObjectMapper mapper = new ObjectMapper();
            if (requestMethod.equals("POST")) {
                //處理JSON
                InputStream inputStream = request.getInputStream();
                LoginForm loginForm = mapper.readValue(inputStream, LoginForm.class);
                inputStream.close();
                String email = loginForm.getEmail();
                String password = loginForm.getPassword();
                HashMap<String, String> errorMsg = new HashMap<>();
                //驗證信箱值
                if (null == email || email.trim().isEmpty()) {
                    errorMsg.put("email", "請輸入信箱");
                } else {
                    //若非空或空白才來驗證正則
                    if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
                        errorMsg.put("email", "請輸入正確的信箱格式");
                    }
                }
                if (null == password || password.trim().isEmpty()) {
                    errorMsg.put("password", "請輸入密碼");
                }
                //有基本的錯誤訊息就回去
                if (!errorMsg.isEmpty()) {
                    HttpResponseData errorResponseData = new HttpResponseData<>(LOGIN_DATA_ERROR,null,errorMsg);
                    String jsonResponse = mapper.writeValueAsString(errorResponseData);
                    response.setContentType("application/json");
                    response.getWriter().write(jsonResponse);
                    return;
                }
                //能通過基本的驗證才進入service
                HttpResponseData loginResultData = userService.login(loginForm);
                if (loginResultData.getResponseCode() == 200) {// 登入成功
                    UserDTO userDTO = (UserDTO) loginResultData.getResponseData();
                    httpSession.setAttribute("user",userDTO);
                    response.sendRedirect(request.getContextPath() + "/index");
                } else { // 登入失敗
                    String jsonResponse = mapper.writeValueAsString(loginResultData);
                    response.setContentType("application/json");
                    response.getWriter().write(jsonResponse);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        } else if (path.endsWith("login")) {
            if (requestMethod.equals("POST")) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                HashMap<String, String> errorMsg = new HashMap<>();
                //驗證信箱值
                if (null == email || email.trim().isEmpty()) {
                    errorMsg.put("email", "請輸入信箱");
                } else {
                    //若非空或空白才來驗證正則
                    if (!Pattern.matches(EMAIL_REGEX,email)) {
                        errorMsg.put("email", "請輸入正確的信箱格式");
                    }
                }
                if (null == password || password.trim().isEmpty()) {
                    errorMsg.put("password", "請輸入密碼");
                }
                //有基本的錯誤訊息就回去
                if (!errorMsg.isEmpty()) {
                    httpSession.setAttribute("errorMsg", errorMsg);
                    response.sendRedirect(request.getContextPath() + "/user/login");
                }
                UserDTO userDTO = userService.loginForm(new LoginForm(email, password), errorMsg);
                //有service中的錯誤就回去
                if (!errorMsg.isEmpty()) {
                    httpSession.setAttribute("errorMsg", errorMsg);
                    response.sendRedirect(request.getContextPath() + "/user/login");
                } else {//沒有有service中的錯誤就回去
                    httpSession.setAttribute("user", userDTO);
                    response.sendRedirect(request.getContextPath() + "/index");
                }
            } else {
                filterChain.doFilter(request, response);
            }
        } else if (path.endsWith("register")) { //若為註冊請求則放行
            filterChain.doFilter(request, response);
        } else if (isStaticResource(path)) { //若為靜態資源則放行
            filterChain.doFilter(request, response);
        } else { //為需要登入的請求
            UserDTO user = (UserDTO) httpSession.getAttribute("user");
            if (user == null) {
                // 如果沒有登入，重導到登錄頁
                response.sendRedirect(request.getContextPath() + "/user/loginAjax");
            } else {
                // 已登入
                filterChain.doFilter(request, response);
            }

        }
    }

    private boolean isStaticResource(String path) {
        return (path.endsWith(".js") || path.endsWith("login.jsp") || path.endsWith("register.jsp") || path.endsWith("loginAjax.jsp") || path.endsWith("/h2-console"));
    }

}
