package com.mvc.hello.web.frontcontroller.v4;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mvc.hello.web.frontcontroller.MyView;
import com.mvc.hello.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.mvc.hello.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.mvc.hello.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

  private Map<String, ControllerV4> controllerMap = new HashMap<>();

  public FrontControllerServletV4() {
    controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
    controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
    controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    System.out.println("FrontControllerServletV4.service()");
    String requestURI = req.getRequestURI();
    ControllerV4 controller = controllerMap.get(requestURI);
    if (controller == null) {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    Map<String, String> paramMap = new HashMap<>();
    Map<String, Object> model = new HashMap<>();
    req.getParameterNames().asIterator().forEachRemaining(name -> {
      paramMap.put(name, req.getParameter(name));
    });

    String viewName = controller.process(paramMap, model);

    String prefix = "/WEB-INF/views/";
    String suffix = ".jsp";
    MyView viewResolver = new MyView(prefix + viewName + suffix);
    viewResolver.render(model, req, resp);
  }
}