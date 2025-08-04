package hello.exception.api.resolver;

import java.io.IOException;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

  @Override
  @Nullable
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
      @Nullable Object handler, Exception ex) {
    if (ex instanceof IllegalArgumentException) {
      log.info("IllegalArgumentException to 400");
      try {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        return new ModelAndView();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

}