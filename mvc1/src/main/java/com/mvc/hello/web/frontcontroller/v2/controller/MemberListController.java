package com.mvc.hello.web.frontcontroller.v2.controller;

import java.io.IOException;
import java.util.List;

import com.mvc.hello.domain.member.Member;
import com.mvc.hello.domain.member.MemberRepository;
import com.mvc.hello.web.frontcontroller.MyView;
import com.mvc.hello.web.frontcontroller.v2.ControllerV2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberListController implements ControllerV2 {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("MvcMemberListServlet.service");
    List<Member> members = memberRepository.findAll();
    request.setAttribute("members", members);

    String viewPath = "/WEB-INF/views/members.jsp";
    return new MyView(viewPath);
  }
}
