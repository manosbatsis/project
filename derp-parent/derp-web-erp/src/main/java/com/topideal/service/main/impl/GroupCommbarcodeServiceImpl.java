package com.topideal.service.main.impl;

import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.dao.main.GroupCommbarcodeDao;
import com.topideal.dao.main.GroupCommbarcodeItemDao;
import com.topideal.entity.dto.main.GroupCommbarcodeDTO;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.entity.vo.main.GroupCommbarcodeItemModel;
import com.topideal.entity.vo.main.GroupCommbarcodeModel;
import com.topideal.service.main.GroupCommbarcodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 组码管理ServiceImpl
 * @author gy
 *
 */
@Service
public class GroupCommbarcodeServiceImpl implements GroupCommbarcodeService{
	
	/* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupCommbarcodeServiceImpl.class);

	@Autowired
	private GroupCommbarcodeDao groupCommbarcodeDao ;
	
	@Autowired
	private GroupCommbarcodeItemDao groupCommbarcodeItemDao ;
	
	@Autowired
	private CommbarcodeDao commbarcodeDao ;
	
	// mq
	@Autowired
	private RMQProducer rocketMQProducer;

	/**
	 * 获取组码分页列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GroupCommbarcodeDTO listgroupCommbarcodesPage(GroupCommbarcodeDTO model) {
		
		List<GroupCommbarcodeDTO> pageList = groupCommbarcodeDao.listgroupCommbarcodesPage(model);
		List<GroupCommbarcodeDTO> totalList = groupCommbarcodeDao.listgroupCommbarcodes(model);
		
		model.setList(pageList);
		model.setTotal(totalList.size());
		
		return model;
	}

	/**
	 * 获取标准条码
	 */
	@Override
	public List<CommbarcodeModel> getCommbarcode(String commbarcode) {
		return commbarcodeDao.getVagueList(commbarcode);
	}

	/**
	 * 保存
	 */
	@Override
	public boolean save(GroupCommbarcodeDTO model) throws SQLException {
		
		String commbarcode = model.getCommbarcode();
		
		List<String> commbarcodeList = new ArrayList<String> () ;
		if(StringUtils.isNotBlank(commbarcode)) {
			String[] comms = commbarcode.split(",");
			commbarcodeList = Arrays.asList(comms);
			
			judgeCommbarcodeExist(commbarcodeList);
		}
		
		GroupCommbarcodeModel groupCommbarcodeModel = new GroupCommbarcodeModel() ;
		groupCommbarcodeModel.setGroupCommbarcode(model.getGroupCommbarcode());
		groupCommbarcodeModel.setGroupName(model.getGroupName());
		groupCommbarcodeModel.setCreateDate(TimeUtils.getNow());
		groupCommbarcodeModel.setModifyDate(TimeUtils.getNow());
		
		Long id = groupCommbarcodeDao.save(groupCommbarcodeModel);
		
		if(id == null ) {
			return false ;
		}else {
			for (String commbarcodeTemp : commbarcodeList) {
				GroupCommbarcodeItemModel tempModel = new GroupCommbarcodeItemModel() ;
				tempModel.setCommbarcode(commbarcodeTemp);
				tempModel.setGroupCommbarcodeId(id);
				
				groupCommbarcodeItemDao.save(tempModel) ;
			}
			
			return true ;
		}
	}
	
	@Override
	public boolean modify(GroupCommbarcodeDTO model) throws SQLException {
		String commbarcode = model.getCommbarcode();
		
		List<String> commbarcodeList = new ArrayList<String> () ;
		if(StringUtils.isNotBlank(commbarcode)) {
			String[] comms = commbarcode.split(",");
			commbarcodeList = Arrays.asList(comms);
			
			for (String temp : commbarcodeList) {
				
				GroupCommbarcodeItemModel modelTemp = new GroupCommbarcodeItemModel() ;
				modelTemp.setCommbarcode(temp);
				modelTemp = groupCommbarcodeItemDao.searchByModel(modelTemp) ;
				
				if(modelTemp != null && 
						modelTemp.getGroupCommbarcodeId() != model.getId()) {
					throw new RuntimeException(commbarcode + "已存在于组码") ;
				}
			}
		}
		
		GroupCommbarcodeModel groupCommbarcodeModel = new GroupCommbarcodeModel() ;
		groupCommbarcodeModel.setId(model.getId());
		groupCommbarcodeModel.setGroupCommbarcode(model.getGroupCommbarcode());
		groupCommbarcodeModel.setGroupName(model.getGroupName());
		groupCommbarcodeModel.setModifyDate(TimeUtils.getNow());
		int rows = groupCommbarcodeDao.modify(groupCommbarcodeModel);
		
		if(rows > 0) {
			//先删除再新增
			GroupCommbarcodeItemModel groupCommbarcodeItemModel = new GroupCommbarcodeItemModel() ;
			groupCommbarcodeItemModel.setGroupCommbarcodeId(groupCommbarcodeModel.getId());
		
			List<GroupCommbarcodeItemModel> list = groupCommbarcodeItemDao.list(groupCommbarcodeItemModel);
		
			List<Long> ids = new ArrayList<Long>() ;
			for (GroupCommbarcodeItemModel temp : list) {
				ids.add(temp.getId()) ;
			}
			if (ids.size()>0) {
				groupCommbarcodeItemDao.delete(ids) ;
			}
			
			
			for (String commbarcodeTemp : commbarcodeList) {
				GroupCommbarcodeItemModel tempModel = new GroupCommbarcodeItemModel() ;
				tempModel.setCommbarcode(commbarcodeTemp);
				tempModel.setGroupCommbarcodeId(model.getId());
				
				groupCommbarcodeItemDao.save(tempModel) ;
			}
			
			return true ;
		}else {
			return false; 
		}
	}
	
	/**
	 * 判断标准条码是否被引用
	 * @throws SQLException 
	 */
	private void judgeCommbarcodeExist(List<String> commbarcodeList) throws SQLException {
		for (String commbarcode : commbarcodeList) {
			
			GroupCommbarcodeItemModel model = new GroupCommbarcodeItemModel() ;
			model.setCommbarcode(commbarcode);
			model = groupCommbarcodeItemDao.searchByModel(model) ;
			
			if(model != null) {
				throw new RuntimeException(commbarcode + "已存在于组码") ;
			}
		}
	}

	@Override
	public boolean delete(String id) throws Exception {
		
		String[] ids = id.split(",") ;
		
		List<Long> idList = new ArrayList<Long>() ;
		for (String temp : ids) {
			idList.add(Long.valueOf(temp)) ;
		}
		
		groupCommbarcodeDao.delete(idList) ;
		int rows = groupCommbarcodeItemDao.deleteByCommbarcodeId(idList) ;
		
		if(rows > 0) {
			return true ;
		}
		
		return false;
	}

	@Override
	public List<GroupCommbarcodeDTO> getDetailList(GroupCommbarcodeDTO model) throws SQLException {
		
		List<GroupCommbarcodeDTO> returnList = new ArrayList<GroupCommbarcodeDTO>() ;
		
		GroupCommbarcodeModel groupCommbarcode = groupCommbarcodeDao.searchById(model.getId());
		
		GroupCommbarcodeItemModel itemModel = new GroupCommbarcodeItemModel() ;
		itemModel.setGroupCommbarcodeId(model.getId());
		List<GroupCommbarcodeItemModel> list = groupCommbarcodeItemDao.list(itemModel);
		
		GroupCommbarcodeDTO commbarcodeDTO ;
		for (GroupCommbarcodeItemModel temp : list) {
			commbarcodeDTO = new GroupCommbarcodeDTO() ;
			
			commbarcodeDTO.setCommbarcode(temp.getCommbarcode());
			commbarcodeDTO.setGroupCommbarcode(groupCommbarcode.getGroupCommbarcode());
			
			CommbarcodeModel commbarcodeModel = new CommbarcodeModel() ;
			commbarcodeModel.setCommbarcode(temp.getCommbarcode());
			commbarcodeModel = commbarcodeDao.searchByModel(commbarcodeModel) ;
			
			if(commbarcodeModel != null) {
				commbarcodeDTO.setGroupName(commbarcodeModel.getCommGoodsName());
			}
			
			returnList.add(commbarcodeDTO) ;
		}
		
		
		return returnList;
	}

}
