package com.spring.mvc.basic.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RequestBodyStringController {
  @PostMapping("/request-body-string-v1")
  public void requestBodyString1(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
      throws IOException {
    ServletInputStream inputStream = httpServletRequest.getInputStream();
    String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    log.info("body = {}", body);
    httpServletResponse.getWriter().write("ok");
  }

  @PostMapping("/request-body-string-v2")
  public void requestBodyString2(InputStream inputStream, Writer writer)
      throws IOException {
    String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    log.info("body = {}", body);
    writer.write("ok");
  }

  @PostMapping("/request-body-string-v3")
  public HttpEntity<String> requestBodyString3(HttpEntity<String> entity)
      throws IOException {
    String body = entity.getBody();
    log.info("body = {}", body);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/request-body-string-v4")
  @ResponseBody
  public String requestBodyString4(@RequestBody String body)
      throws IOException {
    log.info("body = {}", body);
    return HttpStatus.OK.getReasonPhrase();
  }
}
