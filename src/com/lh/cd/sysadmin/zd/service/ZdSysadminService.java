package com.lh.cd.sysadmin.zd.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lh.cd.entity.mapper.TabfDistrictMapper;
import com.lh.cd.entity.model.TabfDistrict;
import com.lh.cd.entity.model.TabfDistrictExample;
import com.lh.cd.pub.Constants;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ZdSysadminService {

	private static Logger logger = Logger.getLogger(ZdSysadminService.class);

	@Autowired
	private TabfDistrictMapper tabfDistrictMapper;

	/**
	 * 查询性别
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<?> getSexList() throws Exception {
		Constants consts = new Constants();
		return consts.getList("ZD_SEX");
	}

	/**
	 * 查询证件类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<?> getCertTypeList() throws Exception {
		Constants consts = new Constants();
		return consts.getList("ZD_CERTTYPE");
	}

	/**
	 * 查询教育
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<?> getEducationList() throws Exception {
		Constants consts = new Constants();
		return consts.getList("ZD_EDUCATION");
	}

	/**
	 * 查询地区
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TabfDistrict> getDistrictByParentIdList(int parentId) throws Exception {
		logger.debug("parentId="+parentId);
		TabfDistrictExample example = new TabfDistrictExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		example.setOrderByClause("order_no asc");
		return tabfDistrictMapper.selectByExample(example);
	}


	// /**
	// * 查询地区是否是叶节点
	// *
	// * @return
	// * @throws Exception
	// */
	// public boolean districtIsLeaf(int districtId) throws Exception {
	// TabfDistrictExample example = new TabfDistrictExample();
	// example.createCriteria().andParentIdEqualTo(districtId);
	// return (tabfDistrictMapper.selectByExample(example).size() == 0);
	// }
}
