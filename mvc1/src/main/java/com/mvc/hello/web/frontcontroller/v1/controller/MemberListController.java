package com.mvc.hello.web.frontcontroller.v1.controller;

import java.io.IOException;
import java.util.List;

import com.mvc.hello.domain.member.Member;
import com.mvc.hello.domain.member.MemberRepository;
import com.mvc.hello.web.frontcontroller.v1.ControllerV1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberListController implements ControllerV1 {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("MvcMemberListServlet.service");
    List<Member> members = memberRepository.findAll();
    request.setAttribute("members", members);
    String viewPath = "/WEB-INF/views/members.jsp";
    RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
    dispatcher.forward(request, response);
  }
}
