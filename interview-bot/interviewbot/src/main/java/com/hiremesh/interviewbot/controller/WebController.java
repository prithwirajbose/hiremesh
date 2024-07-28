package com.hiremesh.interviewbot.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class WebController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/app/{pageName}")
    public String appPage(@PathVariable String pageName, HttpServletRequest request,
			HttpServletResponse response, final Model model) {
    	if(StringUtils.isBlank(pageName)) {
    		pageName = "index";
    	}
    	logger.info("Web page request " + request.getRequestURI(), ctx);
        return pageName;
    }

    @RequestMapping(value={"/","/app", "/app/"}, method=RequestMethod.GET)
    public String appHome(HttpServletRequest request,
			HttpServletResponse response, final Model model) {
    	logger.info("Web page request " + request.getRequestURI(), ctx);
        return "index";
    }

}
