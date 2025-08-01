package hello.login.session;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class SessionManager {
  private static String SESSION_COOKIE_NAME = "mySessionId";
  private Map<String, Object> store = new ConcurrentHashMap<>();

  public void createSession(Object value, HttpServletResponse res) {
    String sessionId = UUID.randomUUID().toString();
    store.put(sessionId, value);
    res.addCookie(new Cookie(SESSION_COOKIE_NAME, sessionId));
  }

  public Object getSession(HttpServletRequest req) {
    return findCookie(req)
        .map(cookie -> store.get(cookie.getValue()))
        .orElse(null);
  }

  public void expire(HttpServletRequest req) {
    findCookie(req).ifPresent(cookie -> {
      store.remove(cookie.getValue());
      cookie.setMaxAge(0);
    });
  }

  private Optional<Cookie> findCookie(HttpServletRequest req) {
    Cookie[] cookies = req.getCookies();
    if (cookies == null) {
      return null;
    }
    return Arrays.stream(cookies)
        .filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
        .findAny();
  }
}
