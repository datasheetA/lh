package com.lh.cd.entity.mapper;

import com.lh.cd.entity.model.TabUserInfo;
import com.lh.cd.entity.model.TabUserInfoExample;
import com.lh.cd.pub.db.SqlMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TabUserInfoMapper extends SqlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int countByExample(TabUserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int deleteByExample(TabUserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int insert(TabUserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int insertSelective(TabUserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    List<TabUserInfo> selectByExampleWithBLOBs(TabUserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    List<TabUserInfo> selectByExample(TabUserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    TabUserInfo selectByPrimaryKey(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") TabUserInfo record, @Param("example") TabUserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") TabUserInfo record, @Param("example") TabUserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") TabUserInfo record, @Param("example") TabUserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TabUserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(TabUserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TabUserInfo record);
}