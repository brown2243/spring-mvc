package com.mvc.hello.web.frontcontroller.v2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mvc.hello.web.frontcontroller.MyView;
import com.mvc.hello.web.frontcontroller.v2.controller.MemberFormController;
import com.mvc.hello.web.frontcontroller.v2.controller.MemberListController;
import com.mvc.hello.web.frontcontroller.v2.controller.MemberSaveController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {

  private Map<String, ControllerV2> controllerMap = new HashMap<>();

  public FrontControllerServletV2() {
    controllerMap.put("/front-controller/v2/members/new-form", new MemberFormController());
    controllerMap.put("/front-controller/v2/members/save", new MemberSaveController());
    controllerMap.put("/front-controller/v2/members", new MemberListController());
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    System.out.println("FrontControllerServletV2.service()");
    String requestURI = req.getRequestURI();
    ControllerV2 controller = controllerMap.get(requestURI);
    if (controller == null) {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    MyView process = controller.process(req, resp);
    process.render(req, resp);
  }
}