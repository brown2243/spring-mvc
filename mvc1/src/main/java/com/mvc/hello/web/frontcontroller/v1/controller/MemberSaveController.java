package com.mvc.hello.web.frontcontroller.v1.controller;

import java.io.IOException;

import com.mvc.hello.domain.member.Member;
import com.mvc.hello.domain.member.MemberRepository;
import com.mvc.hello.web.frontcontroller.v1.ControllerV1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberSaveController implements ControllerV1 {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(username, age);
    System.out.println("member = " + member);
    memberRepository.save(member);

    request.setAttribute("member", member);
    String viewPath = "/WEB-INF/views/save-result.jsp";
    RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
    dispatcher.forward(request, response);
  }
}
