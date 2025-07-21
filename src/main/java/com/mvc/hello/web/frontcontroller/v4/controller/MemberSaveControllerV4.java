package com.mvc.hello.web.frontcontroller.v4.controller;

import java.util.Map;

import com.mvc.hello.domain.member.Member;
import com.mvc.hello.domain.member.MemberRepository;
import com.mvc.hello.web.frontcontroller.v4.ControllerV4;

public class MemberSaveControllerV4 implements ControllerV4 {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  public String process(Map<String, String> paramMap, Map<String, Object> model) {

    String username = paramMap.get("username");
    int age = Integer.parseInt(paramMap.get("age"));

    Member member = new Member(username, age);
    System.out.println("member = " + member);
    memberRepository.save(member);

    model.put("member", member);

    String viewPath = "save-result";
    return viewPath;
  }
}
