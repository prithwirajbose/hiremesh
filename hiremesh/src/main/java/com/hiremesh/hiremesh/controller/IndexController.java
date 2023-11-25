package com.hiremesh.hiremesh.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    @GetMapping("/api")
    public ResponseEntity<Object> api() {
        return null;
    }

}
