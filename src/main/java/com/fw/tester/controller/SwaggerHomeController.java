package com.fw.tester.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class SwaggerHomeController {

  /**
   * Index Page.
   *
   * @return ModelAndView
   */
  @RequestMapping(value = "/")
  public ModelAndView index() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setView(new RedirectView("swagger-ui.html"));
    return modelAndView;
  }
}