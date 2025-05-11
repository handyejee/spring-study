package com.spring.study.springsecurity.oauth;

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
      Model model) {

    log.info("로그인 성공 처리: accessToken={}, refreshToken={}",
        accessToken.substring(0, Math.min(10, accessToken.length())) + "...",
        refreshToken.substring(0, Math.min(10, refreshToken.length())) + "...");

    model.addAttribute("accessToken", accessToken);
    model.addAttribute("refreshToken", refreshToken);

    log.info("모델에 추가된 속성: {}", model.asMap().keySet());

    return "login-success";  // templates/login-success.html로 연결
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
