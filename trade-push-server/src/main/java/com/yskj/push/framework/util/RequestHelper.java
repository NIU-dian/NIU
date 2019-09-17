package com.yskj.push.framework.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Created by mingwang.jiang on 2017/2/22.
 */
public class RequestHelper {

    public static final String CALLBACK = "callback";

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == servletRequestAttributes) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == servletRequestAttributes) {
            return null;
        }
        return servletRequestAttributes.getResponse();
    }

    public static boolean isJsonpRequest() {
        return isJsonpRequest(getRequest());
    }

    public static boolean isJsonpRequest(HttpServletRequest request) {
        Enumeration<?> paramsEnum = request.getParameterNames();
        boolean flg = false;
        while (paramsEnum.hasMoreElements()) {
            String paramsName = (String) paramsEnum.nextElement();
            if (StringUtils.endsWithIgnoreCase(CALLBACK, paramsName)) {
                flg = true;
                break;
            }
        }
        return flg;
    }

    public static String getIp() {
        return getIp(getRequest());
    }

    public static String getIp(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getIp12(String ip) {
        if (null == ip || "".equals(ip)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String[] items = ip.split("\\.");
        for (String item : items) {
            if (item.length() < 3) {
                item = StringUtils.repeat("0", 3 - item.length()) + item;
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 获得请求路径的URI
     *
     * @param request
     * @return
     */
    public static String getRequestUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        String context = request.getContextPath();
        if (url.startsWith(context + "/")) {
            url = url.replaceFirst(context, "");
        }
        return url;
    }

}
