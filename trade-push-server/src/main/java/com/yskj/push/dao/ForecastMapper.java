package com.yskj.push.dao;

import com.yskj.push.domain.Forecast;

public interface ForecastMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_forecast
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_forecast
     *
     * @mbggenerated
     */
    int insert(Forecast record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_forecast
     *
     * @mbggenerated
     */
    int insertSelective(Forecast record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_forecast
     *
     * @mbggenerated
     */
    Forecast selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_forecast
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Forecast record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_forecast
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Forecast record);
}