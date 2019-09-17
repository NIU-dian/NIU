package com.yskj.push.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kai.tang
 * @version 1.0
 * @date 2017/1/3 19:26
 */
public class DateUtil {


    public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

    public static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 获取默认的日期格式
     * @param date 当前时间
     * @return
     */
    public static String getDefaultFormatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
        return sdf.format(date);
    }


    /**
     * 返回自定义的日期格式
     * @param date  当前时间
     * @param format 日期格式
     * @return
     */
    public static String getFormatDate(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * 将字符串日期转换为Date日期
     * @param date  日期
     * @param format  日期格式
     * @return
     */
    public static Date parseDate(String date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            logger.error("parseDate error:{}",e);
        }
        return null;
    }


}
