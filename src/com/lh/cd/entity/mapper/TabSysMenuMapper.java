package com.lh.cd.entity.mapper;

import com.lh.cd.entity.model.TabSysMenu;
import com.lh.cd.entity.model.TabSysMenuExample;
import com.lh.cd.pub.db.SqlMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TabSysMenuMapper extends SqlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    int countByExample(TabSysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    int deleteByExample(TabSysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer menuId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    int insert(TabSysMenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    int insertSelective(TabSysMenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    List<TabSysMenu> selectByExample(TabSysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    TabSysMenu selectByPrimaryKey(Integer menuId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") TabSysMenu record, @Param("example") TabSysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") TabSysMenu record, @Param("example") TabSysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TabSysMenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TabSysMenu record);
}