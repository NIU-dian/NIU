package com.yskj.push.framework.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * 
 * @author kai.tang
 * @date 2017年5月3日 上午10:19:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
