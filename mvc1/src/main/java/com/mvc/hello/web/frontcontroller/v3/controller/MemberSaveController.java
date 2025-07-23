package com.mvc.hello.web.frontcontroller.v3.controller;

import java.util.Map;

import com.mvc.hello.domain.member.Member;
import com.mvc.hello.domain.member.MemberRepository;
import com.mvc.hello.web.frontcontroller.ModelView;
import com.mvc.hello.web.frontcontroller.v3.ControllerV3;

public class MemberSaveController implements ControllerV3 {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  public ModelView process(Map<String, String> paramMap) {

    String username = paramMap.get("username");
    int age = Integer.parseInt(paramMap.get("age"));

    Member member = new Member(username, age);
    System.out.println("member = " + member);

    memberRepository.save(member);

    String viewPath = "save-result";
    ModelView mv = new ModelView(viewPath);
    mv.getModel().put("member", member);
    return mv;
  }
}
