package com.mvc.hello.web.frontcontroller.v3;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.DispatcherServlet;

import com.mvc.hello.web.frontcontroller.ModelView;
import com.mvc.hello.web.frontcontroller.MyView;
import com.mvc.hello.web.frontcontroller.v3.controller.MemberFormController;
import com.mvc.hello.web.frontcontroller.v3.controller.MemberListController;
import com.mvc.hello.web.frontcontroller.v3.controller.MemberSaveController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

  private Map<String, ControllerV3> controllerMap = new HashMap<>();

  public FrontControllerServletV3() {
    controllerMap.put("/front-controller/v3/members/new-form", new MemberFormController());
    controllerMap.put("/front-controller/v3/members/save", new MemberSaveController());
    controllerMap.put("/front-controller/v3/members", new MemberListController());
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    System.out.println("FrontControllerServletV3.service()");
    String requestURI = req.getRequestURI();
    ControllerV3 controller = controllerMap.get(requestURI);
    if (controller == null) {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    Map<String, String> paramMap = new HashMap<>();
    req.getParameterNames().asIterator().forEachRemaining(name -> {
      System.out.println(name + ", " + req.getParameter(name));
      paramMap.put(name, req.getParameter(name));
    });

    ModelView mv = controller.process(paramMap);

    String prefix = "/WEB-INF/views/";
    String suffix = ".jsp";
    MyView viewResolver = new MyView(prefix + mv.getViewName() + suffix);
    viewResolver.render(mv.getModel(), req, resp);
    // req.getRequestDispatcher(prefix + process.getViewName()).forward(req, resp);
  }
}