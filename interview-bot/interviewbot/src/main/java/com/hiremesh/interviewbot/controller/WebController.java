package com.hiremesh.interviewbot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class WebController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/api")
    public ResponseEntity<Object> api() {
        return null;
    }

    @GetMapping("/app/{pageName}")
    public String app(@PathVariable String pageName, HttpServletRequest request,
			HttpServletResponse response, final Model model) {
    	logger.info("Web page request /app/" + pageName, ctx);
        return pageName;
    }

}
