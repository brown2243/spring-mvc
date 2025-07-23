package com.mvc.hello.web.frontcontroller.v1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mvc.hello.web.frontcontroller.v1.controller.MemberFormController;
import com.mvc.hello.web.frontcontroller.v1.controller.MemberListController;
import com.mvc.hello.web.frontcontroller.v1.controller.MemberSaveController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

  private Map<String, ControllerV1> controllerMap = new HashMap<>();

  public FrontControllerServletV1() {
    controllerMap.put("/front-controller/v1/members/new-form", new MemberFormController());
    controllerMap.put("/front-controller/v1/members/save", new MemberSaveController());
    controllerMap.put("/front-controller/v1/members", new MemberListController());
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    System.out.println("FrontControllerServletV1.service()");
    String requestURI = req.getRequestURI();
    ControllerV1 controllerV1 = controllerMap.get(requestURI);
    if (controllerV1 == null) {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    controllerV1.process(req, resp);
  }
}