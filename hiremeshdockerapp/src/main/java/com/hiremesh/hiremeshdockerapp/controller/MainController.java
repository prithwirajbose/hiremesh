package com.hiremesh.hiremeshdockerapp.controller;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.hiremesh.hiremeshdockerapp.bean.ResponseBean;

@RestController
@RequestMapping("/")
public class MainController {
	
	@Autowired
	private Environment env;
	

    @GetMapping("/listFiles")
    public ResponseBean listFiles(@RequestParam(name="path", required=false, defaultValue="/") String path) {
        if(StringUtils.isBlank(env.getProperty("APP_PATH"))) {
        	throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Application config missing");
        }
        path = StringUtils.isNotBlank(path) ? path : "/";
        File appDir = new File(env.getProperty("APP_PATH").trim() + path);
        if(appDir.exists() && appDir.isDirectory()) {
        	File[] fileList = appDir.listFiles(new FilenameFilter() {
        		String allowedFileExtns[] = new String[] {".java",".xml",".properties"};
				public boolean accept(File dir, String name) {
					File file = new File(dir+"/"+name);
					return (file!=null && file.exists() && file.isDirectory()) || isValidExtn(name);
				}
				
				private boolean isValidExtn(String name) {
					for(String allowedFileExtn : allowedFileExtns) {
						if(name!=null && name.endsWith(allowedFileExtn)) {
							return true;
						}
					}
					return false;
				}
        		
        	});
        	return new ResponseBean(fileList);
        }
        else {
        	throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Application not found");
        }
    }

}