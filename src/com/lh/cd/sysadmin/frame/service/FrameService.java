package com.lh.cd.sysadmin.frame.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lh.cd.entity.mapper.TabSysMenuMapper;
import com.lh.cd.entity.model.TabSysMenu;
import com.lh.cd.entity.model.TabSysMenuExample;
import com.lh.cd.sysadmin.frame.mapper.SysadminMapper;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class FrameService {

	private static Logger logger = Logger.getLogger(FrameService.class);

	@Autowired
	private TabSysMenuMapper tabSysMenuMapper;
	
	@Autowired
	private SysadminMapper userMapper;

//	public List<?> getMenuListByParentId(int parentId) {
//		logger.debug("parentId="+parentId);
//		
//		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
//		List<TabSysMenu> allList = getAllMenuList();
//		List<TabSysMenu> menuList = getSubMenuList(parentId);
//
//		for(TabSysMenu bean:menuList){
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("id", bean.getMenuId());
//			map.put("text", bean.getMenuName());
//			map.put("indexUrl", bean.getIndexUrl());
//			map.put("leaf", isLeaf(bean.getMenuId(), allList));
//			returnList.add(map);
//		}
//		return returnList;
//	}
//
//	private List<TabSysMenu> getSubMenuList(int parentId) {
//		TabSysMenuExample example = new TabSysMenuExample();
//		example.createCriteria().andParentIdEqualTo(parentId).andStatusEqualTo(1);
//		example.setOrderByClause("site_no, menu_name");
//		return tabSysMenuMapper.selectByExample(example);
//	}

	public List<TabSysMenu> getAllMenuList() {
		TabSysMenuExample example = new TabSysMenuExample();
		example.createCriteria();
		example.setOrderByClause("site_no, menu_name");
		return tabSysMenuMapper.selectByExample(example);
	}
	
	public boolean isLeaf(int menuId, List<TabSysMenu> allList){
		for(TabSysMenu bean:allList){
			if(bean.getParentId() == menuId){
				return false;
			}
		}
		return true;
	}

	public List<?> getRealUserAuthByUserId(Map<String, Object> paraMap) {
		return userMapper.selectRealUserAuthByUserId(paraMap);
	}
	
	public static void main(String[] args) {
		logger.debug("main");
	}
}
