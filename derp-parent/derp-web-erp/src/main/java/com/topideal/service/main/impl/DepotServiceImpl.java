package com.topideal.service.main.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.*;
import com.topideal.entity.dto.main.DepotInfoDTO;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.*;
import com.topideal.service.base.OperateSysLogService;
import com.topideal.service.main.DepotService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

/**
 * 仓库管理service实现类
 */
@Service
public class DepotServiceImpl implements DepotService {
	
	// 仓库信息dao
	@Autowired
	private DepotInfoDao depotDao;
	//批次效期强校验明细
	@Autowired
	private BatchValidationInfoDao batchValidationInfoDao;

	//仓库附表
	@Autowired
	private DepotScheduleDao depotScheduleDao;
	@Autowired
	private DepotCustomsRelDao depotCustomsRelDao;//仓库关区关系表
	@Autowired
	private CustomsAreaConfigDao customsAreaConfigDao;//关区表

	@Autowired
	private OperateSysLogService operateSysLogService;

	
	
	/**
	 * 根据id获取仓库信息详情
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public DepotInfoDTO searchDetail(Long id) throws SQLException {
		return depotDao.searchDTOById(id);
	}

	/**
	 * 仓库信息列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public DepotInfoDTO listDepot(DepotInfoDTO model) throws SQLException {
		return depotDao.getListByPage(model);
	}

	/**
	 * 根据仓库信息id删除仓库信息(支持批量)
	 * 
	 * @param ids
	 * @return
	 */
	@Override
	public boolean delDepot(List ids) throws SQLException {
//		int num = depotDao.delete(ids);
		int num = 0 ;
		for (Object object : ids) {
			Long depotId = (Long) object;
			
			//逻辑删除
			DepotInfoModel tempModel = new DepotInfoModel() ;
			tempModel.setId(depotId);
			tempModel.setStatus(DERP.DEL_CODE_006 );
			
			int rows = depotDao.modify(tempModel);
			
			//删除仓库附表信息
			if (rows > 0) {
				DepotScheduleModel depotScheduleModel = new DepotScheduleModel();
				depotScheduleModel.setDepotId(depotId);
				List<DepotScheduleModel> dsList = depotScheduleDao.list(depotScheduleModel);
				List<Long> dsIds = new ArrayList<Long>();
				for (DepotScheduleModel m : dsList) {
					dsIds.add(m.getId());
				}
				if (dsIds != null && dsIds.size()>0) {
					depotScheduleDao.delete(dsIds);
				}
				
				num += rows ;
			}
		}
		if (num >= 1) {
			return true;
		}
		return false;
	}

	/**
	 * 新增仓库信息
	 * 
	 * @param model
	 * @param
	 * @return
	 * @throws
	 */
	@Override
	public boolean saveDepot(User user, DepotInfoModel model, JSONArray jSONArray) throws Exception{
		
		
		// 判断仓库自编码是否存在
		DepotInfoModel queryModel = new DepotInfoModel();
		queryModel.setDepotCode(model.getDepotCode());
		queryModel = depotDao.searchByModel(queryModel);
		if (queryModel != null && !DERP.DEL_CODE_006.equals(queryModel.getStatus())) {
			throw new RuntimeException("该仓库自编码已存在");
		}
		// 判断仓库名称是否存在
		queryModel = new DepotInfoModel();
		queryModel.setName(model.getName());// 仓库名称
		queryModel = depotDao.searchByModel(queryModel);
		if (queryModel != null && !DERP.DEL_CODE_006.equals(queryModel.getStatus())) {
			throw new RuntimeException("该仓库名称已存在");
		}
		if(model.getIsTopBooks()==null || "".equals(model.getIsTopBooks())){
			model.setIsTopBooks(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);
		}
		
		/*if (DERP_SYS.DEPOTINFO_TYPE_A.equals(model.getType()) 
				&& DERP_SYS.DEPOTINFO_ISTOPBOOKS_1.equals(model.getIsTopBooks()) 
				&& model.getMerchantId() == null) {
			throw new RuntimeException("保税仓且为代销仓必须关联商家key！");
		}*/
	
		Long id = depotDao.save(model);
		
		if(model.getBatchValidation() != null){
			BatchValidationInfoModel batchModel = new BatchValidationInfoModel();
				batchModel.setBatchValidation(model.getBatchValidation());
				batchModel.setDepotId(model.getId());//获取仓库id
				batchModel.setDepotName(model.getName());//获取仓库名称
				batchModel.setCreateDate(TimeUtils.getNow());//获取创建时间
				batchModel.setEffectiveTime(TimeUtils.nextMonthFirstZeroPointDate());//获取生效时间，取当前时间的下月日期
			batchValidationInfoDao.save(batchModel);
		}
		
		if (id != null) {
			String address = model.getAddress();
			//存仓库附表信息
			if (StringUtils.isNotBlank(address)) {
				String[] addressList = address.split(",");
				for (String adr : addressList) {
					DepotScheduleModel depotScheduleModel = new DepotScheduleModel();
					depotScheduleModel.setDepotId(model.getId());
					depotScheduleModel.setDepotName(model.getName());
					depotScheduleModel.setAddress(adr);
					depotScheduleModel.setCreateDate(TimeUtils.getNow());
					depotScheduleDao.save(depotScheduleModel);
				}
			}
		}
		// 新增仓库关区关系表
		for (Object object : jSONArray) {
			JSONObject jSONObject=(JSONObject) object;
			String customsAreaConfigId = jSONObject.getString("customsAreaConfigId");
			String onlineRegisterNo = jSONObject.getString("onlineRegisterNo");
			String recCompanyName = jSONObject.getString("recCompanyName");
			String recCompanyEnname = jSONObject.getString("recCompanyEnname");
			String customerAddress = jSONObject.getString("customerAddress");
			
			CustomsAreaConfigModel customsAreaConfig = customsAreaConfigDao.searchById(Long.valueOf(customsAreaConfigId));
			if (customsAreaConfig==null) {customsAreaConfig=new CustomsAreaConfigModel();}
			DepotCustomsRelModel depotCustomsRel=new DepotCustomsRelModel();
			depotCustomsRel.setDepotId(model.getId());
			depotCustomsRel.setDepotName(model.getName());
			depotCustomsRel.setCustomsAreaId(customsAreaConfig.getId());
			depotCustomsRel.setCustomsAreaCode(customsAreaConfig.getCode());
			depotCustomsRel.setCustomsAreaName(customsAreaConfig.getName());
			depotCustomsRel.setRecCompanyName(recCompanyName);
			depotCustomsRel.setRecCompanyEnname(recCompanyEnname);
			depotCustomsRel.setAddress(customerAddress);
			depotCustomsRel.setOnlineRegisterNo(onlineRegisterNo);
			depotCustomsRelDao.save(depotCustomsRel);

		}

		// 记录操作日志
		operateSysLogService.saveLog(user, DERP_SYS.OREARTE_SYS_LOG_6, id, "新增仓库信息", null, null);

		return true;
	}

	/**
	 * 修改仓库信息
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public boolean modifyDepot(User user, DepotInfoModel model,JSONArray jSONArray) throws Exception {
		DepotInfoModel depotInfoModel= new DepotInfoModel();
//		depotInfoModel.setName(model.getName());//仓库名称
		depotInfoModel.setId(model.getId());
		depotInfoModel = depotDao.searchByModel(depotInfoModel);
		if (depotInfoModel!=null
				&& !DERP.DEL_CODE_006.equals(depotInfoModel.getStatus())
				&& depotInfoModel.getId().intValue()!=model.getId().intValue()) {
			throw new RuntimeException("仓库名称已存在");
		}
		
		/*if (DERP_SYS.DEPOTINFO_TYPE_A.equals(model.getType()) 
				&& DERP_SYS.DEPOTINFO_ISTOPBOOKS_1.equals(model.getIsTopBooks()) 
				&& model.getMerchantId() == null) {
			throw new RuntimeException("保税仓且为代销仓必须关联商家key！");
		}*/
		if(!depotInfoModel.getBatchValidation().equals(model.getBatchValidation())){
			BatchValidationInfoModel batchModel = new BatchValidationInfoModel();
			batchModel.setDepotId(model.getId());
			batchModel.setDepotName(model.getName());
			batchModel.setModifyDate(TimeUtils.getNow());
			batchModel.setBatchValidation(model.getBatchValidation());
			batchModel.setCreateDate(TimeUtils.getNow());//获取创建时间
			batchModel.setEffectiveTime(TimeUtils.nextMonthFirstZeroPointDate());
			batchValidationInfoDao.save(batchModel);
		}	
		// 修改非必填字段
		int updateNULL = depotDao.updateNULL(model);
		int num = depotDao.modify(model);
		
	    if(num >= 1){
	    	
	    	Long depotId = model.getId();
			//删除仓库附表信息
			if (depotId != null) {
				DepotScheduleModel depotScheduleModel = new DepotScheduleModel();
				depotScheduleModel.setDepotId(depotId);
				List<DepotScheduleModel> dsList = depotScheduleDao.list(depotScheduleModel);
				List<Long> dsIds = new ArrayList<Long>();
				for (DepotScheduleModel m : dsList) {
					dsIds.add(m.getId());
				}
				if (dsIds != null && dsIds.size()>0) {
					depotScheduleDao.delete(dsIds);
				}
			}
			String address = model.getAddress();
			//存仓库附表信息
			if (StringUtils.isNotBlank(address)) {
				String[] addressList = address.split(",");
				for (String adr : addressList) {
					DepotScheduleModel depotScheduleModel = new DepotScheduleModel();
					depotScheduleModel.setDepotId(model.getId());
					depotScheduleModel.setDepotName(model.getName());
					depotScheduleModel.setAddress(adr);
					depotScheduleModel.setCreateDate(TimeUtils.getNow());
					depotScheduleDao.save(depotScheduleModel);
				}
			}
			
			//删除商家仓库关系表数据
			//11
			Long id = model.getId();
			DepotCustomsRelModel relQuery=new DepotCustomsRelModel();
			relQuery.setDepotId(id);
			List<DepotCustomsRelModel> relList = depotCustomsRelDao.list(relQuery);
			List<Long>delIds=new ArrayList<>();
			if (relList!=null&&relList.size()>0) {
				for (DepotCustomsRelModel relmodel : relList) {
					delIds.add(relmodel.getId());
				}				
			}
			if (delIds.size()>0) {
				depotCustomsRelDao.delete(delIds);
			}

			// 新增仓库关区关系表
			for (Object object : jSONArray) {
				JSONObject jSONObject=(JSONObject) object;
				String customsAreaConfigId = jSONObject.getString("customsAreaConfigId");
				String onlineRegisterNo = jSONObject.getString("onlineRegisterNo");
				String recCompanyName = jSONObject.getString("recCompanyName");
				String recCompanyEnname = jSONObject.getString("recCompanyEnname");
				String customerAddress = jSONObject.getString("customerAddress");
				
				CustomsAreaConfigModel customsAreaConfig = customsAreaConfigDao.searchById(Long.valueOf(customsAreaConfigId));
				if (customsAreaConfig==null) {customsAreaConfig=new CustomsAreaConfigModel();}
				DepotCustomsRelModel depotCustomsRel=new DepotCustomsRelModel();
				depotCustomsRel.setDepotId(model.getId());
				depotCustomsRel.setDepotName(model.getName());
				depotCustomsRel.setCustomsAreaId(customsAreaConfig.getId());
				depotCustomsRel.setCustomsAreaCode(customsAreaConfig.getCode());
				depotCustomsRel.setCustomsAreaName(customsAreaConfig.getName());
				depotCustomsRel.setRecCompanyName(recCompanyName);
				depotCustomsRel.setRecCompanyEnname(recCompanyEnname);
				depotCustomsRel.setAddress(customerAddress);
				depotCustomsRel.setOnlineRegisterNo(onlineRegisterNo);
				depotCustomsRelDao.save(depotCustomsRel);

			}
			
    	}

		// 记录操作日志
		operateSysLogService.saveLog(user, DERP_SYS.OREARTE_SYS_LOG_6, model.getId(), "修改仓库信息", null, null);

		return true;
	}

	/**
	 * 导入仓库信息
	 */
	@Override
	public Map importDepot(User user, Map<Integer, List<List<Object>>> data, Long userId) {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int failure = 0;
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
				try {
					// 必填字段的校验
					String code = map.get("仓库编号");
					if (code == null || "".equals(code)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("仓库编号不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String name = map.get("仓库名称");
					if (name == null || "".equals(name)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("仓库名称不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					// 判断仓库编码是否存在
					DepotInfoModel depotInfoModel = new DepotInfoModel();
					depotInfoModel.setCode(code);
					depotInfoModel = depotDao.searchByModel(depotInfoModel);
					if (depotInfoModel != null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j);
						message.setMessage("仓库已存在");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String type = map.get("仓库类型");
					String adminName = map.get("仓管员");
					String customsNo = map.get("申报地海关编号");
					String inspectNo = map.get("申报地国检编号");
					String address = map.get("仓库详细地址");
					DepotInfoModel model = new DepotInfoModel();
					model.setAddress(address);
					model.setAdminName(adminName);
					model.setCode(code);
					model.setCreateDate(TimeUtils.getNow());
					model.setCreater(userId);
					model.setCustomsNo(customsNo);
					model.setInspectNo(inspectNo);
					model.setName(adminName);
					model.setType(type);
					Long id = depotDao.save(model);
					if (id != null) {
						success += 1;
						// 记录操作日志
						operateSysLogService.saveLog(user, DERP_SYS.OREARTE_SYS_LOG_6, id, "导入新增仓库", null, null);
					} else {
						failure += 1;
					}


				} catch (SQLException e) {
					e.printStackTrace();
					failure += 1;
				}
			}
		}
		Map map = new HashMap();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
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
	 * 查询商品下拉列表
	 * @param depotInfoModel
	 */
	@Override
	public List<SelectBean> getSelectBean(DepotInfoModel depotInfoModel) throws SQLException {
		return depotDao.getSelectBean(depotInfoModel);
	}
	
	/**
	 * 查询代理商家下拉列表
	 * 
	 * @param
	 */
	@Override
	public List<MerchantInfoModel> getSelectPoxy() throws SQLException {
		return depotDao.getSelectPoxy();
	}

	/**
	 * 查询仓库下拉列表，是否同关区
	 * */
	@Override
	public List<SelectBean> getSelectBeanByArea(DepotInfoDTO dto) throws SQLException {
		return depotDao.getSelectBeanByArea(dto);
	}

	/**
	 * 9011弹框列表查询
	 */
	@Override
	public List<BatchValidationInfoModel> getListById(Long id) throws SQLException {		
		BatchValidationInfoModel model = new BatchValidationInfoModel();
		model.setDepotId(id);
		return batchValidationInfoDao.getListById(model);
	}
	@Override
	public boolean audit(User user, DepotInfoModel model) throws SQLException {
		int num = depotDao.modify(model);
		String labelByKey = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_statusList, model.getStatus());
		if (num >= 1) {
			// 记录操作日志
			operateSysLogService.saveLog(user, DERP_SYS.OREARTE_SYS_LOG_6, model.getId(), "修改状态为" + labelByKey, null, null);
			return true;
		}
		return false;
	}
	/**
	 * admin账号查询仓库下拉列表
	 */
	@Override
	public List<SelectBean> getSelectBeanForAdmin(DepotInfoModel model) throws SQLException {
		return depotDao.getSelectBeanForAdmin(model);
	}
	
	/**
	 * 根据页面传入商家id获取此商家下仓库的下拉框
	 */
	@Override
	public List<SelectBean> getSelectBeanByMerchantRel(DepotMerchantRelDTO dto) throws SQLException {
		return depotDao.getSelectBeanByMerchantRel(dto);
	}
	
	/**
	 * 获取商家下仓库的下拉框
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<DepotInfoDTO> getSelectBeanByDTO(DepotInfoDTO dto) throws SQLException {		
		return depotDao.getSelectBeanByDTO(dto);
	}

	/**
	 * 获取仓库关区关系信息
	 */
	@Override
	public List<DepotCustomsRelModel> getDepotCustomsRel(DepotCustomsRelModel model) throws SQLException {
		return depotCustomsRelDao.list(model);
	}

	@Override
	public List<SelectBean> getSelectBeanByMerchantBuRel(MerchantDepotBuRelDTO dto) throws SQLException {
		return depotDao.getSelectBeanByMerchantBuRel(dto);
	}
}
