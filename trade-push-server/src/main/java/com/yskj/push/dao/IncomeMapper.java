package com.yskj.push.dao;

import com.yskj.push.domain.Income;

public interface IncomeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_income
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_income
     *
     * @mbggenerated
     */
    int insert(Income record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_income
     *
     * @mbggenerated
     */
    int insertSelective(Income record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_income
     *
     * @mbggenerated
     */
    Income selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_income
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Income record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_income
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Income record);
}