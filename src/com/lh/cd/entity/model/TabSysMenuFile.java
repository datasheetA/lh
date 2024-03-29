package com.lh.cd.entity.model;

import java.io.Serializable;
import java.util.Date;

public class TabSysMenuFile implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tab_sys_menu_file.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tab_sys_menu_file.menu_id
     *
     * @mbggenerated
     */
    private Integer menuId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tab_sys_menu_file.file_url
     *
     * @mbggenerated
     */
    private String fileUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tab_sys_menu_file.upd_time
     *
     * @mbggenerated
     */
    private Date updTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tab_sys_menu_file
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tab_sys_menu_file.id
     *
     * @return the value of tab_sys_menu_file.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tab_sys_menu_file.id
     *
     * @param id the value for tab_sys_menu_file.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tab_sys_menu_file.menu_id
     *
     * @return the value of tab_sys_menu_file.menu_id
     *
     * @mbggenerated
     */
    public Integer getMenuId() {
        return menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tab_sys_menu_file.menu_id
     *
     * @param menuId the value for tab_sys_menu_file.menu_id
     *
     * @mbggenerated
     */
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tab_sys_menu_file.file_url
     *
     * @return the value of tab_sys_menu_file.file_url
     *
     * @mbggenerated
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tab_sys_menu_file.file_url
     *
     * @param fileUrl the value for tab_sys_menu_file.file_url
     *
     * @mbggenerated
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tab_sys_menu_file.upd_time
     *
     * @return the value of tab_sys_menu_file.upd_time
     *
     * @mbggenerated
     */
    public Date getUpdTime() {
        return updTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tab_sys_menu_file.upd_time
     *
     * @param updTime the value for tab_sys_menu_file.upd_time
     *
     * @mbggenerated
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu_file
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TabSysMenuFile other = (TabSysMenuFile) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMenuId() == null ? other.getMenuId() == null : this.getMenuId().equals(other.getMenuId()))
            && (this.getFileUrl() == null ? other.getFileUrl() == null : this.getFileUrl().equals(other.getFileUrl()))
            && (this.getUpdTime() == null ? other.getUpdTime() == null : this.getUpdTime().equals(other.getUpdTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tab_sys_menu_file
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMenuId() == null) ? 0 : getMenuId().hashCode());
        result = prime * result + ((getFileUrl() == null) ? 0 : getFileUrl().hashCode());
        result = prime * result + ((getUpdTime() == null) ? 0 : getUpdTime().hashCode());
        return result;
    }
}