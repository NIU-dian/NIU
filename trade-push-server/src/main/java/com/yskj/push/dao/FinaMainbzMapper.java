package com.yskj.push.dao;

import com.yskj.push.domain.FinaMainbz;

public interface FinaMainbzMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fina_mainbz
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fina_mainbz
     *
     * @mbggenerated
     */
    int insert(FinaMainbz record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fina_mainbz
     *
     * @mbggenerated
     */
    int insertSelective(FinaMainbz record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fina_mainbz
     *
     * @mbggenerated
     */
    FinaMainbz selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fina_mainbz
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(FinaMainbz record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fina_mainbz
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(FinaMainbz record);
}