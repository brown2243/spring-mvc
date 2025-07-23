package com.mvc.hello.web.frontcontroller.v5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mvc.hello.web.frontcontroller.ModelView;
import com.mvc.hello.web.frontcontroller.MyHandlerAdapter;
import com.mvc.hello.web.frontcontroller.MyView;
import com.mvc.hello.web.frontcontroller.v3.controller.MemberFormController;
import com.mvc.hello.web.frontcontroller.v3.controller.MemberListController;
import com.mvc.hello.web.frontcontroller.v3.controller.MemberSaveController;
import com.mvc.hello.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.mvc.hello.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.mvc.hello.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import com.mvc.hello.web.frontcontroller.v5.adaptor.ControllerV3HandlerAdapter;
import com.mvc.hello.web.frontcontroller.v5.adaptor.ControllerV4HandlerAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
  private final Map<String, Object> handlerMappingMap = new HashMap<>();
  private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

  public FrontControllerServletV5() {
    initHandlerMappingMap();
    initHandlerAdapters();
  }

  private void initHandlerMappingMap() {
    handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormController());
    handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveController());
    handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListController());

    handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
    handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
    handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
  }

  private void initHandlerAdapters() {
    handlerAdapters.add(new ControllerV3HandlerAdapter());
    handlerAdapters.add(new ControllerV4HandlerAdapter());
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Object handler = getHandler(request);
    if (handler == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    MyHandlerAdapter adapter = getHandlerAdapter(handler);
    ModelView mv = adapter.handle(request, response, handler);
    MyView view = viewResolver(mv.getViewName());
    view.render(mv.getModel(), request, response);
  }

  private Object getHandler(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    return handlerMappingMap.get(requestURI);
  }

  private MyHandlerAdapter getHandlerAdapter(Object handler) {
    for (MyHandlerAdapter adapter : handlerAdapters) {
      if (adapter.supports(handler)) {
        return adapter;
      }
    }
    throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
  }

  private MyView viewResolver(String viewName) {
    return new MyView("/WEB-INF/views/" + viewName + ".jsp");
  }
}