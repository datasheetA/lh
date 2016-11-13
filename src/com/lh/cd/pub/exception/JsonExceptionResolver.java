package com.lh.cd.pub.exception;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.JsonErrorHandler;
import org.springframework.web.servlet.view.json.JsonExceptionHandler;
import org.springframework.web.servlet.view.json.exception.StackTraceExceptionHandler;

public class JsonExceptionResolver implements HandlerExceptionResolver {
	private static Logger logger = Logger.getLogger(JsonExceptionResolver.class);
	private static ModelAndView defaultModel = new ModelAndView();

	private List<JsonExceptionHandler> exceptionHandler = new ArrayList<JsonExceptionHandler>();
	private List<JsonErrorHandler> errorHandler = new ArrayList<JsonErrorHandler>();
	
	public List<JsonExceptionHandler> getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(List<JsonExceptionHandler> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public List<JsonErrorHandler> getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(List<JsonErrorHandler> errorHandler) {
		this.errorHandler = errorHandler;
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		Map model = new HashMap();
		try {
			if(ex instanceof AppException){
				AppException aex = (AppException)ex;
				model.put("errorCode", aex.getErrorCode());
			}
			else if(ex instanceof SysException){
				SysException sex = (SysException)ex;
				model.put("errorCode", sex.getErrorCode());
			}

			triggerExceptionHandler(ex, model, request, response);
			triggerErrorHandler(model, request, response);
			if ((exceptionHandler == null || exceptionHandler.size() == 0) && (errorHandler == null || errorHandler.size() == 0)){
				throw new IllegalArgumentException("No JsonExceptionHandler or JsonErrorHandler registered!");
			}
			Writer writer = response.getWriter();
			JSON json = JSONSerializer.toJSON(model);
			json.write(writer);
			logger.debug(json.toString());
			writer.flush();
		}
		catch (Exception e) {
			StackTraceExceptionHandler errHandler = new StackTraceExceptionHandler();
			try {
				errHandler.triggerException(e, model, request, response);
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return defaultModel;
	}

	@SuppressWarnings("unchecked")
	protected void triggerErrorHandler(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (errorHandler == null || errorHandler.size() == 0) return;

		for (JsonErrorHandler error : errorHandler) {
			error.triggerError(model, null, null, request, response);
		}
	}

	@SuppressWarnings("unchecked")
	protected void triggerExceptionHandler(Exception ex, Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (exceptionHandler == null || exceptionHandler.size() == 0) return;

		for (JsonExceptionHandler exception : exceptionHandler) {
			exception.triggerException(ex, model, request, response);
		}
	}
}
