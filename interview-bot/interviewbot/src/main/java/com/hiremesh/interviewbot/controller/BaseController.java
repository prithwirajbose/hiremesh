package com.hiremesh.interviewbot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiremesh.interviewbot.common.AppRequestContext;
import com.hiremesh.interviewbot.common.Utils;

public class BaseController {

	@Autowired
	protected AppRequestContext ctx;

	@Autowired
	protected Environment env;

	@Autowired
	protected Utils utils;
	
	protected ObjectMapper mapper = new ObjectMapper();
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
