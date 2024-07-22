package com.hiremesh.interviewbot.common;

import java.util.LinkedHashMap;
import java.util.Map;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;

public class InterviewBotLogAppender extends AppenderBase<ILoggingEvent> {

	@Override
	protected void append(ILoggingEvent event) {
		long oldestTs = System.currentTimeMillis() - 30000;

		Map<Long, Object> eventEntry = new LinkedHashMap<Long, Object>();
		Map<String, Object> eventDetails = new LinkedHashMap<String, Object>();
		eventDetails.put("timeStamp", event.getTimeStamp());
		eventDetails.put("level", event.getLevel());
		eventDetails.put("threadName", event.getThreadName());
		eventDetails.put("loggerName", event.getLoggerName());
		eventDetails.put("formattedMessage", event.getFormattedMessage());
		eventDetails.put("message", event.getMessage());
		IThrowableProxy throwbleProxy = event.getThrowableProxy();
		StringBuffer sbuf = new StringBuffer();
		if (throwbleProxy != null) {
			sbuf.append(ThrowableProxyUtil.asString(throwbleProxy)).append(CoreConstants.LINE_SEPARATOR);
			eventDetails.put("stackTrace", sbuf.toString());
		}
	}

}