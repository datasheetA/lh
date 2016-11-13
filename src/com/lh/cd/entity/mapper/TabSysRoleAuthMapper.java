package com.lh.cd.entity.mapper;

import com.lh.cd.entity.model.TabSysRoleAuth;
import com.lh.cd.entity.model.TabSysRoleAuthExample;
import com.lh.cd.pub.db.SqlMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TabSysRoleAuthMapper extends SqlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    int countByExample(TabSysRoleAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    int deleteByExample(TabSysRoleAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    int insert(TabSysRoleAuth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    int insertSelective(TabSysRoleAuth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    List<TabSysRoleAuth> selectByExample(TabSysRoleAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    TabSysRoleAuth selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") TabSysRoleAuth record, @Param("example") TabSysRoleAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") TabSysRoleAuth record, @Param("example") TabSysRoleAuthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TabSysRoleAuth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_role_auth
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TabSysRoleAuth record);
}