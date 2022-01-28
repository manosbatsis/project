package com.topideal.service.main.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandParentDao;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.entity.dto.main.CommbarcodeDTO;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.vo.base.BrandParentModel;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.service.main.CommbarcodeService;

@Service
public class CommbarcodeServiceImpl implements CommbarcodeService{

	@Autowired
	private CommbarcodeDao commbarcodeDao ;
	@Autowired
	private BrandParentDao brandParentDao ;

	@SuppressWarnings("unchecked")
	@Override
	public CommbarcodeDTO listCommbarcodes(CommbarcodeDTO model) {
		
		CommbarcodeDTO commbarcodeModel = new CommbarcodeDTO() ;
		
		List<CommbarcodeDTO> commbarcodeModelList = commbarcodeDao.listCommbarcodes(model) ; 
		
		Integer total  = commbarcodeDao.listCommbarcodesCount(model) ;
		commbarcodeModel.setTotal(total);
		commbarcodeModel.setList(commbarcodeModelList);
		
		return commbarcodeModel;
	}

	@Override
	public CommbarcodeModel detail(CommbarcodeModel model) throws SQLException {
		return commbarcodeDao.searchByModel(model);
	}

	@Override
	public int modify(CommbarcodeModel model) throws SQLException {
		
		if(model.getCommBrandParentId() != null ) {
			model.setMaintainStatus(DERP_SYS.COMMBARCODE_MAINTAINSTATUS_1);
		}
		model.setModifyDate(TimeUtils.getNow());
		
		return commbarcodeDao.modify(model);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map importCommbarcodes(Map<Integer, List<List<Object>>> data, User user) throws SQLException {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		Integer success = 0;
		Integer pass = 0;
		Integer failure = 0;
		List<List<Object>> objects = data.get(0);
		
		for (int j = 1; j < objects.size(); j++) {
			Map<String, String> map = this.toMap(data.get(0).get(0).toArray(), objects.get(j).toArray());
		
			String commbarcode = map.get("标准条码") ;
			if(checkIsNullOrNot(j, commbarcode, "标准条码不能为空", resultList)) {
				failure += 1; 
				continue ;
			}
			commbarcode = commbarcode.trim() ;
			
			String brandParentName = map.get("标准品牌") ;
			if(checkIsNullOrNot(j, brandParentName, "标准品牌不能为空", resultList) ) {
				failure += 1; 
				continue ;
			}
			brandParentName = brandParentName.trim();
			
			//判断标准品牌存在
			BrandParentModel brandParent = new BrandParentModel();
			brandParent.setName(brandParentName);
			brandParent = brandParentDao.searchByModel(brandParent);
			if(brandParent == null){
				setErrorMsg(j, "标准品牌不存在", resultList );
				failure += 1; 
				continue ;
			}
			
			CommbarcodeModel commbarcodeModel = new CommbarcodeModel();
			commbarcodeModel.setCommbarcode(commbarcode);
			commbarcodeModel = commbarcodeDao.searchByModel(commbarcodeModel) ;
			
			boolean flag = false ;
			
			if(commbarcodeModel == null) {
				setErrorMsg(j, "标准条码不存在", resultList );
				failure += 1; 
				continue ;
			}else {
				commbarcodeModel.setCommbarcode(commbarcode);
				commbarcodeModel.setMaintainStatus("1");
				commbarcodeModel.setCommBrandParentId(brandParent.getId());
				commbarcodeModel.setCommBrandParentName(brandParent.getName());
				commbarcodeModel.setModifyDate(TimeUtils.getNow());
				
				int rows = commbarcodeDao.modify(commbarcodeModel);
				
				flag = rows > 0 ? true : false ;
			}
			
			if(flag) {
				success  ++ ;
			}else {
				failure ++ ;
			}
			
		}
		
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}
	
	/**
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index , String content , 
			String msg ,List<ImportErrorMessage> resultList ) {
		
		if(StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);
			
			return true ;
			
		}else {
			return false ;
		}
		
	}
	
	/**
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}
	
	/**
	 * 把两个数组组成一个map
	 * 
	 * @param keys
	 *            键数组
	 * @param values
	 *            值数组
	 * @return 键值对应的map
	 */
	private Map<String, String> toMap(Object[] keys, Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				map.put((String) keys[i], values[i].toString());
			}
			return map;
		}
		return null;
	}

	/**
	 * 获取为维护标准条码
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public List<CommbarcodeModel> getUnMaintainList() throws SQLException {
		CommbarcodeModel queryModel = new CommbarcodeModel() ;
		queryModel.setMaintainStatus(DERP_SYS.COMMBARCODE_MAINTAINSTATUS_0);
		
		return commbarcodeDao.list(queryModel);
	}

	@Override
	public CommbarcodeModel searchByModel(CommbarcodeModel commbarcodeModel) throws SQLException {
		return commbarcodeDao.searchByModel(commbarcodeModel);
	}

	@Override
	public List<CommbarcodeModel> list(CommbarcodeModel commbarcodeModel) throws SQLException {
		return commbarcodeDao.list(commbarcodeModel);
	}

	@Override
	public List<CommbarcodeDTO> getExportList(String ids) {
		
		String[] split = ids.split(",");
		List<String> idList = Arrays.asList(split) ;
		
		if(StringUtils.isBlank(ids)) {
			idList = null ;
		}
		
		return commbarcodeDao.getExportList(idList);
	}

	@Override
	public CommbarcodeDTO searchByDTO(CommbarcodeDTO dto) {
		return commbarcodeDao.searchByDTO(dto);
	}
	
	@Override
	public List<CommbarcodeDTO> getExportCommbarcodes(String ids) {
		
		String[] split = ids.split(",");
		List<String> idList = Arrays.asList(split) ;
		
		if(StringUtils.isBlank(ids)) {
			idList = null ;
		}
		
		return commbarcodeDao.getExportCommbarcodes(idList);
	}
}
