package hello.login.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.PatternMatchUtils;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckFilter implements Filter {
  private static final String[] whiteList = { "", "/", "/members/add", "/login", "/logout", "/css/*" };

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String requestURI = req.getRequestURI();

    try {
      log.info("로그인 인증 필터 로직 url={}", requestURI);
      if (isLoginCheckPath(requestURI)) {
        log.info("인증 URL 접근");
        HttpSession session = req.getSession();
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
          log.info("인증 정보 없음");
          res.sendRedirect("/login?redirectUrl=" + requestURI);
          return;
        }
      }
      chain.doFilter(request, response);
    } catch (Exception e) {
      throw e;
    }
  }

  private boolean isLoginCheckPath(String path) {
    return !PatternMatchUtils.simpleMatch(whiteList, path);
  }
}
