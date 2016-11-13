package com.lh.cd.sysadmin.menu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lh.cd.entity.mapper.TabSysMenuFileMapper;
import com.lh.cd.entity.mapper.TabSysMenuMapper;
import com.lh.cd.entity.model.TabSysMenu;
import com.lh.cd.entity.model.TabSysMenuExample;
import com.lh.cd.entity.model.TabSysMenuFile;
import com.lh.cd.entity.model.TabSysMenuFileExample;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class MenuService {

	private static Logger logger = Logger.getLogger(MenuService.class);

	@Autowired
	private TabSysMenuMapper tabSysMenuMapper;

	@Autowired
	private TabSysMenuFileMapper tabSysMenuFileMapper;

	public List<?> getMenuListByParentId(int parentId) {
		logger.debug("parentId="+parentId);
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<TabSysMenu> allList = getAllMenuList();
		List<TabSysMenu> menuList = getSubMenuList(parentId);

		for (TabSysMenu bean : menuList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", bean.getMenuId());
			map.put("text", bean.getMenuName());
			map.put("indexUrl", bean.getIndexUrl());
			map.put("leaf", isLeaf(bean.getMenuId(), allList));
			returnList.add(map);
		}
		return returnList;
	}

	private List<TabSysMenu> getSubMenuList(int parentId) {
		TabSysMenuExample example = new TabSysMenuExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		example.setOrderByClause("site_no, menu_name");
		return tabSysMenuMapper.selectByExample(example);
	}

	private List<TabSysMenu> getAllMenuList() {
		TabSysMenuExample example = new TabSysMenuExample();
		example.createCriteria();
		example.setOrderByClause("site_no, menu_name");
		return tabSysMenuMapper.selectByExample(example);
	}

	public TabSysMenu getMenuInfo(int menuId) {
		return tabSysMenuMapper.selectByPrimaryKey(menuId);
	}

	public List<TabSysMenuFile> getMenuFileUrl(int menuId) {
		TabSysMenuFileExample example = new TabSysMenuFileExample();
		example.createCriteria().andMenuIdEqualTo(menuId);
		return tabSysMenuFileMapper.selectByExample(example);
	}

	public String getFullMenuName(int menuId) {
		List<TabSysMenu> list = getAllMenuList();
		return getFullMenuName(list, menuId);
	}

	public String getFullMenuName(List<TabSysMenu> list, int menuId) {
		TabSysMenu menu = getMenuById(list, menuId);
		if (menu.getParentId() == 0) {
			return menu.getMenuName();
		}
		else {
			return getFullMenuName(list, menu.getParentId()) + "/" + menu.getMenuName();
		}
	}

	public TabSysMenu getMenuById(List<TabSysMenu> list, int menuId) {
		for (TabSysMenu menu : list) {
			if (menu.getMenuId() == menuId) {
				return menu;
			}
		}
		return null;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void insertMenu(TabSysMenu record, String otherUrl) throws Exception {
		tabSysMenuMapper.insertSelective(record);
		saveMenuFileUrl(record.getMenuId(), otherUrl);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateMenu(TabSysMenu record, String otherUrl) {
		tabSysMenuMapper.updateByPrimaryKeySelective(record);
		saveMenuFileUrl(record.getMenuId(), otherUrl);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteMenu(int menuId) {
		TabSysMenuExample example = new TabSysMenuExample();
		example.createCriteria().andParentIdEqualTo(menuId);
		List<TabSysMenu> list = tabSysMenuMapper.selectByExample(example);
		for (TabSysMenu menu : list) {
			deleteMenu(menu.getMenuId());
		}
		deleteOneMenu(menuId);
	}

	public void deleteOneMenu(int menuId) {
		tabSysMenuMapper.deleteByPrimaryKey(menuId);

		TabSysMenuFileExample example = new TabSysMenuFileExample();
		example.createCriteria().andMenuIdEqualTo(menuId);
		tabSysMenuFileMapper.deleteByExample(example);
	}

	public void saveMenuFileUrl(int menuId, String otherUrl) {
		TabSysMenuFileExample example = new TabSysMenuFileExample();
		example.createCriteria().andMenuIdEqualTo(menuId);
		tabSysMenuFileMapper.deleteByExample(example);

		String[] arrUrls = otherUrl.split("\r\n");
		for (int i = 0; i < arrUrls.length; i++) {
			if (arrUrls[i] != null && !arrUrls[i].trim().equals("")) {
				TabSysMenuFile record = new TabSysMenuFile();
				record.setMenuId(menuId);
				record.setFileUrl(arrUrls[i].trim());
				record.setUpdTime(new Date());
				tabSysMenuFileMapper.insertSelective(record);
			}
		}
	}

	public boolean existSameMenuName(String menuName, int parentId, int menuId) {
		TabSysMenuExample example = new TabSysMenuExample();
		example.createCriteria().andMenuNameEqualTo(menuName).andParentIdEqualTo(parentId).andMenuIdNotEqualTo(menuId);
		return tabSysMenuMapper.selectByExample(example).size() > 0;
	}

	private boolean isLeaf(int menuId, List<TabSysMenu> allList) {
		for (TabSysMenu bean : allList) {
			if (bean.getParentId() == menuId) {
				return false;
			}
		}
		return true;
	}
}
