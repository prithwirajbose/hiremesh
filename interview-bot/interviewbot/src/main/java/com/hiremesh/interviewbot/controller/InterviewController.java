package com.hiremesh.interviewbot.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class InterviewController extends BaseController {

	@RequestMapping(value = "/api/audio", method = RequestMethod.GET)
	public String getAudio(HttpServletRequest request, HttpServletResponse response) {
		try {
			String translateUrl = "https://translate.google.com/translate_tts?ie=UTF-8&total=1&idx=0&client=tw-ob&prev=input&ttsspeed=1";
			URL url = new URL(translateUrl + "&q=" + "Hello World".replace(" ", "%20") + "&tl=" + "en");
			URLConnection urlConn = url.openConnection();
			urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			InputStream audioSrc = urlConn.getInputStream();
			response.setContentType("audio/mpeg");
			IOUtils.copy(audioSrc, response.getOutputStream());
		} catch (Exception ex) {
			try {
				response.getWriter().write("filed to load audio");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

}
