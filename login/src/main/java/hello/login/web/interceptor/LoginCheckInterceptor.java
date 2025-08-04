package hello.login.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
  public static final String[] excludePathPatterns = { "/", "/members/add", "/login", "/logout", "/css/**", "/*.ico",
      "/error" };

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    String requestURI = req.getRequestURI();
    log.info("로그인 인증 인터셉터 로직 url={}", requestURI);
    log.info("인증 URL 접근");
    HttpSession session = req.getSession();
    if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
      log.info("인증 정보 없음");
      res.sendRedirect("/login?redirectUrl=" + requestURI);
      return false;
    }

    return true; // false 진행 X
  }
}
