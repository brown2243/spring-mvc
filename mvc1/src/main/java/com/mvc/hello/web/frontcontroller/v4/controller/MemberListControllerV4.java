package com.mvc.hello.web.frontcontroller.v4.controller;

import java.util.List;
import java.util.Map;

import com.mvc.hello.domain.member.Member;
import com.mvc.hello.domain.member.MemberRepository;
import com.mvc.hello.web.frontcontroller.v4.ControllerV4;

public class MemberListControllerV4 implements ControllerV4 {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  public String process(Map<String, String> paramMap, Map<String, Object> model) {
    System.out.println("MvcMemberListServlet.service");
    List<Member> members = memberRepository.findAll();

    model.put("members", members);

    String viewPath = "members";
    return viewPath;
  }
}
