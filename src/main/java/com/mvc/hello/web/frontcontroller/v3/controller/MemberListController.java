package com.mvc.hello.web.frontcontroller.v3.controller;

import java.util.List;
import java.util.Map;

import com.mvc.hello.domain.member.Member;
import com.mvc.hello.domain.member.MemberRepository;
import com.mvc.hello.web.frontcontroller.ModelView;
import com.mvc.hello.web.frontcontroller.v3.ControllerV3;

public class MemberListController implements ControllerV3 {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  public ModelView process(Map<String, String> paramMap) {
    System.out.println("MvcMemberListServlet.service");
    List<Member> members = memberRepository.findAll();

    String viewPath = "members";
    ModelView mv = new ModelView(viewPath);
    mv.getModel().put("members", members);

    return mv;
  }
}
