package com.mvc.hello.web.frontcontroller.v3;

import java.util.Map;

import com.mvc.hello.web.frontcontroller.ModelView;

public interface ControllerV3 {
  ModelView process(Map<String, String> paramMap);
}