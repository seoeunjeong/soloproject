package soloproject.seomoim.security.FormLogin;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//    /*Todo 로그인 실패 예외처리 아이디 실패인지 비밀번호 실패인지알려줘야할까 */
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        response.sendRedirect("/login-form?error");
//    }
//}
