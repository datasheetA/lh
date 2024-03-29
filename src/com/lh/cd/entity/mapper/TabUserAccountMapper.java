package com.lh.cd.entity.mapper;

import com.lh.cd.entity.model.TabUserAccount;
import com.lh.cd.entity.model.TabUserAccountExample;
import com.lh.cd.pub.db.SqlMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TabUserAccountMapper extends SqlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    int countByExample(TabUserAccountExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    int deleteByExample(TabUserAccountExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    int insert(TabUserAccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    int insertSelective(TabUserAccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    List<TabUserAccount> selectByExample(TabUserAccountExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    TabUserAccount selectByPrimaryKey(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") TabUserAccount record, @Param("example") TabUserAccountExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") TabUserAccount record, @Param("example") TabUserAccountExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TabUserAccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_user_account
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TabUserAccount record);
}