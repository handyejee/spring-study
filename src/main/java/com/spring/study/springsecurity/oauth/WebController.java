package com.spring.study.springsecurity.oauth;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class WebController {

  @GetMapping("/login")
  public String loginPage() {
    return "login";  // templates/login.html로 연결
  }

  @GetMapping("/login-success")
  public String loginSuccess(@RequestParam String accessToken,
      @RequestParam String refreshToken,
      HttpSession session) {

    // 토큰을 세션에 저장
    session.setAttribute("accessToken", accessToken);
    session.setAttribute("refreshToken", refreshToken);

    return "login-success";
  }

  @GetMapping("/login-failed")
  public String loginFailed(Model model) {
    model.addAttribute("error", "로그인 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
    return "login-failed";  // templates/login-failed.html로 연결
  }

  @GetMapping("/")
  public String home() {
    return "home";  // home.html 템플릿을 렌더링
  }
}
