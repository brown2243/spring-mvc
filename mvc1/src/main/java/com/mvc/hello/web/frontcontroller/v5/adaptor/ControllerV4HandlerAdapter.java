package com.mvc.hello.web.frontcontroller.v5.adaptor;

import java.util.HashMap;
import java.util.Map;

import com.mvc.hello.web.frontcontroller.ModelView;
import com.mvc.hello.web.frontcontroller.MyHandlerAdapter;
import com.mvc.hello.web.frontcontroller.MyView;
import com.mvc.hello.web.frontcontroller.v4.ControllerV4;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
  @Override
  public boolean supports(Object handler) {
    return (handler instanceof ControllerV4);
  }

  @Override
  public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    ControllerV4 controller = (ControllerV4) handler;
    Map<String, String> paramMap = createParamMap(request);
    Map<String, Object> model = new HashMap<>();
    String viewName = controller.process(paramMap, model);
    ModelView mv = new ModelView(viewName);
    mv.setModel(model);

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
