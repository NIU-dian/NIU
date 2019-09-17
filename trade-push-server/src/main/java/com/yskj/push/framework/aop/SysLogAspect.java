package com.yskj.push.framework.aop;

import com.alibaba.fastjson.JSON;


import com.yskj.push.framework.annotation.SysLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * 系统日志，切面处理类
 * 
 * @author kai.tang
 * @date 2017年5月3日 上午11:07:35
 */
@Aspect
@Component
public class SysLogAspect {

	private static String split = "||";

	private Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
	
	@Pointcut("@annotation(com.yskj.push.framework.annotation.SysLog)")
	public void logPointCut() { 
		
	}
	
	@Before("logPointCut()")
	public void beforeLog(JoinPoint joinPoint) {
		StringBuilder sb = getLog(joinPoint);
		if (sb == null) return;
		logger.info(sb.toString());
	}


	@AfterThrowing(throwing = "ex", pointcut = "logPointCut()")
	public void afterThrowingLog(Throwable ex) {
		logger.error("afterThrowing :{}", ex);
	}

	/**
	 * 获取注解信息
	 * @param joinPoint
	 * @return
     */
	private StringBuilder getLog(JoinPoint joinPoint) {
		StringBuilder sb = new StringBuilder();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog syslog = method.getAnnotation(SysLog.class);
		if( syslog == null){
			logger.warn("not found annotation SysLog !!!");
			return null;
		}
		sb.append(syslog.value()).append(split);
		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sb.append(className).
				append(".").
				append(methodName).
				append("()").
				append(split);

		//请求的参数
		Object[] args = joinPoint.getArgs();
		String params = JSON.toJSONString(args[0]);
		sb.append(params);
		return sb;
	}


}
