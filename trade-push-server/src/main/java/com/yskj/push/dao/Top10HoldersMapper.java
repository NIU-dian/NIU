package com.yskj.push.dao;

import com.yskj.push.domain.Top10Holders;

public interface Top10HoldersMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_top10_holders
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_top10_holders
     *
     * @mbggenerated
     */
    int insert(Top10Holders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_top10_holders
     *
     * @mbggenerated
     */
    int insertSelective(Top10Holders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_top10_holders
     *
     * @mbggenerated
     */
    Top10Holders selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_top10_holders
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Top10Holders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_top10_holders
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Top10Holders record);
}