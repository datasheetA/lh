package com.lh.cd.sysadmin.frame.mapper;

import java.util.List;
import java.util.Map;

import com.lh.cd.pub.db.SqlMapper;

public interface SysadminMapper extends SqlMapper {
	
	int selectUserListByPageCount(Map<String, Object> paraMap);
	List<?> selectUserListByPage(Map<String, Object> paraMap);

	
	
	List<?> selectSelectedUserByRoleId(Map<String, Object> paraMap);
	
	List<?> selectUsableUserByRoleId(Map<String, Object> paraMap);
	
	List<?> selectRoleTreeByUserId(Map<String, Object> paraMap);
	
	List<?> selectMenuTreeByRoleId(Map<String, Object> paraMap);
	
	List<?> selectGrantUserAuthByUserId(Map<String, Object> paraMap);
	
	List<?> selectRealUserAuthByUserId(Map<String, Object> paraMap);
}
