package hello.login.web.login;

import org.springframework.stereotype.Service;

import hello.login.web.member.Member;
import hello.login.web.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

  private final MemberRepository memberRepository;

  public Member login(String loginId, String password) {
    return memberRepository.findByLoginId(loginId)
        .filter(member -> member.getPassword().equals(password))
        .orElse(null);
  }
}
