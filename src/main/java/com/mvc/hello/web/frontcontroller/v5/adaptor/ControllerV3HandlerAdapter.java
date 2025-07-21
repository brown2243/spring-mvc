package com.mvc.hello.web.frontcontroller.v5.adaptor;

import java.util.HashMap;
import java.util.Map;

import com.mvc.hello.web.frontcontroller.ModelView;
import com.mvc.hello.web.frontcontroller.MyHandlerAdapter;
import com.mvc.hello.web.frontcontroller.v3.ControllerV3;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
  @Override
  public boolean supports(Object handler) {
    return (handler instanceof ControllerV3);
  }

  @Override
  public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    ControllerV3 controller = (ControllerV3) handler;
    Map<String, String> paramMap = createParamMap(request);
    ModelView mv = controller.process(paramMap);
    return mv;
  }

  private Map<String, String> createParamMap(HttpServletRequest request) {
    Map<String, String> paramMap = new HashMap<>();
    request.getParameterNames().asIterator()
        .forEachRemaining(paramName -> paramMap.put(paramName,
            request.getParameter(paramName)));
    return paramMap;
  }
}
