package com.hiremesh.interviewbot.common;

import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiremesh.interviewbot.controller.WebController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class Utils {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment env;

	private ObjectMapper mapper = new ObjectMapper();

	public boolean propertyExistsAndNotNull(Object bean, String property) {
		try {
			return (PropertyUtils.getProperty(bean, property) != null);
		} catch (Exception ex) {
			return false;
		}
	}

	public Object getAnyOfAvailableProperties(Object bean, String... properties) {
		Object val = null;
		if (properties != null && properties.length > 0) {
			for (String property : properties) {
				try {
					if (this.propertyExistsAndNotNull(bean, property)) {
						val = PropertyUtils.getProperty(bean, property);
						break;
					}
				} catch (Exception ex) {
					continue;
				}
			}
		}
		return val;
	}

	public Object getProperty(Object bean, String property) {
		return getProperty(bean, property, null);
	}

	public Object getProperty(Object bean, String property, Object defaultVal) {
		Object res = null;
		if (bean != null && StringUtils.isNotBlank(property)) {
			try {
				res = PropertyUtils.getProperty(bean, property);
			} catch (Exception ex) {
				// silently ignore
			}
		}
		return (res != null) ? res : defaultVal;
	}

	public Object setProperty(Object bean, String propertyName, Object propertyVal) {
		if (bean != null && propertyName != null) {
			try {
				PropertyUtils.setProperty(bean, propertyName, propertyVal);
			} catch (Exception ex) {
				// silently ignore
			}
		}
		return bean;
	}

	public List getPropertiesListFromObjectList(List objectList, String... properties) {
		List res = null;
		if (properties != null && properties.length > 0 && objectList != null && objectList.size() > 0) {
			res = new ArrayList();
			for (Object obj : objectList) {
				if (properties.length == 1) {
					try {
						res.add(PropertyUtils.getProperty(obj, properties[0]));
					} catch (Exception ex) {
						res.add(null);
						continue;
					}
				} else {
					Map val = null;
					for (String property : properties) {
						try {
							if (val == null) {
								val = new LinkedHashMap<String, Object>();
							}
							val.put(property, PropertyUtils.getProperty(obj, property));
						} catch (Exception ex) {
							continue;
						}
					}
					res.add(val);
				}
			}
		}
		return res;
	}

	public List<LinkedHashMap<String, Object>> removeKeysFromListOfMaps(List<LinkedHashMap<String, Object>> data,
			String... keys) {
		List<LinkedHashMap<String, Object>> newData = null;
		if (data != null && data.size() > 0 && keys != null && keys.length > 0) {
			newData = new ArrayList<LinkedHashMap<String, Object>>();
			for (int i = 0; i < data.size(); i++) {
				LinkedHashMap<String, Object> cloneObj = (LinkedHashMap<String, Object>) data.get(i).clone();
				for (String key : keys) {
					cloneObj.remove(key);
				}
				newData.add(cloneObj);
			}
		}
		return newData;
	}

	public void setErrorStatusToRequestContext(AppRequestContext ctx, int status) {
		this.setErrorStatusToRequestContext(ctx, status, null);
	}

	public void setErrorStatusToRequestContext(AppRequestContext ctx, int status, String errorDescription) {
		ctx.getHttpRequest().setAttribute("HTTP_ERROR_CODE", status > 0 ? status : 500);
		ctx.getHttpRequest().setAttribute("HTTP_ERROR_DESC",
				errorDescription == null ? (status > 0 ? "HTTP Error" : "Unknown")
						: errorDescription);
		ctx.getHttpRequest().setAttribute("REQUEST_ID", ctx.getRequestId());
		ctx.getHttpResponse().setStatus(status);
	}

	public String getFullURLFromRequest(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURI().toString());
		String queryString = request.getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	public Object getInputFieldValueFromRowRecord(LinkedHashMap<String, Object> record, String fieldLabel,
			LinkedHashMap<String, String> inputColumnMapping) {
		if (record != null && inputColumnMapping != null) {
			return this.getProperty(record, inputColumnMapping.get(fieldLabel), this.getProperty(record, fieldLabel));
		}
		return null;
	}

	public LinkedHashMap<String, Object> flattenMap(Object data, String rootPrefix, int arrayMaxLength) {
		LinkedHashMap<String, Object> flatMap = new LinkedHashMap<String, Object>();
		if (data instanceof Map && ((Map) data).size() > 0) {
			Map<String, Object> dataMap = (Map<String, Object>) data;
			for (String key : dataMap.keySet()) {
				flatMap.putAll(flattenMap(dataMap.get(key), rootPrefix + "." + key, arrayMaxLength));
			}
		} else if (data instanceof List && ((List) data).size() > 0) {
			List dataList = (List) data;
			if (arrayMaxLength >= 0 && dataList != null && dataList.size() > arrayMaxLength) {
				dataList.subList(arrayMaxLength, dataList.size()).clear();
			}

			if (isPrimitiveWrapperClass(dataList.toArray())) {
				flatMap.put(rootPrefix, String.join("; ", dataList));
			} else {
				for (int i = 0; i < dataList.size(); i++) {
					if (rootPrefix != null && rootPrefix.contains(".") && !rootPrefix.startsWith("(")
							&& !rootPrefix.endsWith(")")) {
						String[] prefixArr = rootPrefix.split("\\.");
						prefixArr[prefixArr.length - 1] = "(" + prefixArr[prefixArr.length - 1] + ")";
						rootPrefix = String.join(".", prefixArr);
					} else if (rootPrefix != null && !rootPrefix.startsWith("(") && !rootPrefix.endsWith(")")) {
						rootPrefix = "(" + rootPrefix + ")";
					}

					flatMap.putAll(flattenMap(dataList.get(i), rootPrefix + "[" + i + "]", arrayMaxLength));
					if (i == 2)
						break;
				}
			}
		} else {
			flatMap.put(rootPrefix, data instanceof Map || data instanceof List ? "" : data);
		}
		return flatMap;
	}

	public boolean isPrimitiveWrapperClass(Object[] objects) {
		for (Object o : objects) {
			if (o != null
					&& (o instanceof String || o instanceof Boolean || o instanceof Number || o instanceof Character)) {
				return true;
			}
		}
		return false;
	}

	public boolean isNonStringPrimitiveValue(Object o) {
		if (o != null) {
			try {
				Long.parseLong(String.valueOf(o));
				return true;
			} catch (Exception ex) {
				try {
					Integer.parseInt(String.valueOf(o));
					return true;
				} catch (Exception ex2) {
					try {
						Double.parseDouble(String.valueOf(o));
						return true;
					} catch (Exception ex3) {
						try {
							Float.parseFloat(String.valueOf(o));
							return true;
						} catch (Exception ex4) {
							try {
								Boolean.parseBoolean(String.valueOf(o).toLowerCase());
								return true;
							} catch (Exception ex5) {

							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isEmptyCollection(Collection c) {
		if (c == null || c.size() <= 0) {
			return true;
		} else {
			Iterator it = c.iterator();
			while (it.hasNext()) {
				if (it.next() != null) {
					return false;
				}
			}
			return true;
		}
	}

	public String getTimeStringFromMilliseconds(long l) {
		String timeString = "";
		if (l > 86400000L) {
			timeString += l / 86400000L + "d ";
		}
		l = l % 86400000L;
		if (l > 3600000L) {
			timeString += l / 3600000L + "h ";
		}
		l = l % 3600000L;
		if (l > 60000L) {
			timeString += l / 60000L + "m ";
		}
		l = l % 60000L;
		if (l > 1000L) {
			timeString += l / 1000L + "s ";
		}
		return timeString.trim();
	}

	public Map<String, Object> beanToMap(Object bean) {
		if (bean != null) {
			PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean.getClass());
			Map<String, Object> map = new HashMap<String, Object>();
			if (descriptors != null && descriptors.length > 0) {
				for (PropertyDescriptor descriptor : descriptors) {
					this.setProperty(map, descriptor.getName(), this.getProperty(bean, descriptor.getName()));
				}
			}
			return map;
		} else {
			return null;
		}
	}

	public boolean copyFile(String srcFilePath, String destFilePath) throws IOException {
		return copyFile(new File(srcFilePath), new File(destFilePath));
	}

	public boolean copyFile(File srcFile, File destFile) throws IOException {
		if (destFile != null && destFile.exists()) {
			destFile.delete();
		}
		destFile.createNewFile();

		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(srcFile));
			out = new BufferedOutputStream(new FileOutputStream(destFile));
			byte[] buffer = new byte[1024];
			int lengthRead;
			while ((lengthRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, lengthRead);
				out.flush();
			}
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception ex) {

				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {

				}
			}
		}
		return true;
	}

	public void writeContentsToFile(File file, String content, boolean overwrite) throws IOException {
		if (overwrite && file != null && file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
			fw.write(content);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (Exception ex) {

				}
			}
		}
	}

	public String convertObjectToJsonString(Object obj) throws Exception {
		DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("	", DefaultIndenter.SYS_LF);
		DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
		printer.indentObjectsWith(indenter);
		printer.indentArraysWith(indenter);
		return mapper.writer(printer).writeValueAsString(obj);
	}

	public boolean isValidPassword(String pass, String userName, String emailId) {
		return pass != null && emailId != null && userName != null && pass.length() >= 8 && pass.matches(".*[A-Z]+.*")
				&& pass.matches(".*[a-z]+.*") && pass.matches(".*[0-9]+.*")
				&& (pass.contains("~") || pass.contains("!") || pass.contains("@") || pass.contains("#")
						|| pass.contains("$") || pass.contains("%") || pass.contains("^") || pass.contains("&")
						|| pass.contains("*") || pass.contains(".") || pass.contains(",") || pass.contains("-")
						|| pass.contains("_") || pass.contains("+"))
				&& !pass.toLowerCase().contains(userName.toLowerCase())
				&& !pass.toLowerCase().contains(emailId.toLowerCase())
				&& !userName.toLowerCase().contains(pass.toLowerCase())
				&& !emailId.toLowerCase().contains(pass.toLowerCase());
	}

	public static String escapeSqlString(String x, boolean escapeDoubleQuotes) {
		StringBuilder sBuilder = new StringBuilder(x.length() * 11 / 10);

		int stringLength = x.length();

		for (int i = 0; i < stringLength; ++i) {
			char c = x.charAt(i);

			switch (c) {
			case 0: /* Must be escaped for 'mysql' */
				sBuilder.append('\\');
				sBuilder.append('0');

				break;

			case '\n': /* Must be escaped for logs */
				sBuilder.append('\\');
				sBuilder.append('n');

				break;

			case '\r':
				sBuilder.append('\\');
				sBuilder.append('r');

				break;

			case '\\':
				sBuilder.append('\\');
				sBuilder.append('\\');

				break;

			case '\'':
				sBuilder.append('\\');
				sBuilder.append('\'');

				break;

			case '"': /* Better safe than sorry */
				if (escapeDoubleQuotes) {
					sBuilder.append('\\');
				}

				sBuilder.append('"');

				break;

			case '\032': /* This gives problems on Win32 */
				sBuilder.append('\\');
				sBuilder.append('Z');

				break;

			case '\u00a5':
			case '\u20a9':
				// escape characters interpreted as backslash by mysql
				// fall through

			default:
				sBuilder.append(c);
			}
		}

		return sBuilder.toString();
	}

	public Map<String, String> getAllApplicationUrls() {
		Map<String, String> allUrls = new LinkedHashMap<String, String>();
		List<Method> methods = Arrays.asList(WebController.class.getDeclaredMethods());
		for (Method method : methods) {
			for (Annotation annotation : method.getDeclaredAnnotations()) {
				Class<? extends Annotation> type = annotation.annotationType();
				if (type.equals(GetMapping.class) || type.equals(PostMapping.class)
						|| type.equals(RequestMapping.class)) {
					try {
						String[] urlPatterns = (String[]) type.getMethod("value").invoke(annotation, new Object[0]);
						if (urlPatterns != null && urlPatterns.length > 0) {
							allUrls.put(method.getName(), env.getProperty("app.url") + urlPatterns[0]);
						}
					} catch (Exception e) {
						logger.info("Failed to retrieve URL from UIController", e);
					}
				}
			}
		}
		return allUrls;
	}

	public String generateRandomPassword() {
		int length = 12;
		String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
		String specialCharacters = "~!@#$%^&*.,-_+";
		String numbers = "1234567890";
		String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
		Random random = new Random();
		char[] password = new char[length];

		password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
		password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
		password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
		password[3] = numbers.charAt(random.nextInt(numbers.length()));

		for (int i = 4; i < length; i++) {
			password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
		}
		return new String(password);
	}

	public void httpRedirect(String toUrl, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> attributes) {

		try {
			Enumeration<String> oldAttrs = request.getAttributeNames();
			while (oldAttrs.hasMoreElements()) {
				request.removeAttribute(oldAttrs.nextElement());
			}
			if (attributes != null && attributes.size() > 0) {
				for (String attrKey : attributes.keySet()) {
					request.setAttribute(attrKey, attributes.get(attrKey));
					;
				}
			}
			request.getRequestDispatcher(toUrl).forward(request, response);
		} catch (Exception e1) {
			try {
				response.sendError(500);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
