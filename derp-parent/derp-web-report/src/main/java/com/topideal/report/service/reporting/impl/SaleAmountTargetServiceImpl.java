package com.topideal.report.service.reporting.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.topideal.common.constant.DERP;
import com.topideal.dao.common.OperateReportLogDao;
import com.topideal.dao.system.BrandSuperiorDao;
import com.topideal.entity.vo.common.OperateReportLogModel;
import com.topideal.entity.vo.system.BrandSuperiorModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.SaleAmountTargetDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.entity.dto.ImportErrorMessage;
import com.topideal.entity.dto.SaleAmountTargetDTO;
import com.topideal.entity.vo.reporting.SaleAmountTargetModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.SaleAmountTargetService;

@Service
public class SaleAmountTargetServiceImpl implements SaleAmountTargetService {

	@Autowired
	private SaleAmountTargetDao saleAmountTargetDao ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
	private BusinessUnitDao businessUnitDao ;
	@Autowired
	private BrandSuperiorDao brandSuperiorDao ;
	@Autowired
	private OperateReportLogDao operateReportLogDao;

	/**
	 * 销售额列表展示 分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	public SaleAmountTargetDTO listSaleAmountTarget(SaleAmountTargetDTO dto,User user) throws SQLException {
		if(dto.getBuIds() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}

			dto.setBuIds(buIds);
		}
		
		SaleAmountTargetDTO list = saleAmountTargetDao.getAmountListByPage(dto);

		return list;
	}

	/**
	 * 获取销售额列表总数
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Integer getOrderCount(SaleAmountTargetDTO dto,User user) throws SQLException{
		Integer count = 0;
		if(dto.getBuIds() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return count;
			}
			dto.setBuIds(buIds);
		}
		count = saleAmountTargetDao.getOrderCount(dto);
		return count;
	}

	/**
	 * 导出列表
	 * @param dto
	 * @return
	 */
	@Override
	public List<SaleAmountTargetDTO> exportAmountList(SaleAmountTargetDTO dto,User user) {
		List<SaleAmountTargetDTO> list = null;
		if(dto.getBuIds() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return list;
			}
			dto.setBuIds(buIds);
		}
		list = saleAmountTargetDao.getList(dto);
		return list;
	}

	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map importAmountTarget(List<Map<String, String>> data, User user) throws SQLException {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		//校验通过标识
		boolean flag = true ;
		//添加列表
		List<SaleAmountTargetModel> newAmountTargetList = new ArrayList<>();
		//需要删除的id列表
		List<Long> delIdList = new ArrayList<>();
		//校验导入是否重复
		List<String> checkExistList = new ArrayList<String>();
		//解析
		for(int i = 1 ; i <= data.size() ; i++) {
			Map<String, String> map = data.get(i - 1);

			String buCode = map.get("事业部编码");
			if(checkIsNullOrNot(i, buCode, "事业部不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			BusinessUnitModel buModel = new BusinessUnitModel() ;
			buModel.setCode(buCode);
			buModel = businessUnitDao.searchByModel(buModel) ;
			if(buModel == null) {
				setErrorMsg(i, "事业部不存在", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}

			boolean isRelate = userBuRelMongoDao.isUserRelateBu(user.getId(), buModel.getId());
			if(!isRelate) {
				setErrorMsg(i, "用户无权限操作该事业部", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			buCode = buCode.trim() ;
			String month = map.get("计划月份");
			if(checkIsNullOrNot(i, month, "计划月份不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			month = month.trim() ;
			if(!isMonth(month)) {
				setErrorMsg(i, "计划月份格式有误，要求yyyy-mm", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}

			String brandSuperior = map.get("母品牌");
			if(checkIsNullOrNot(i, brandSuperior, "母品牌不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			brandSuperior = brandSuperior.trim() ;
			BrandSuperiorModel brandSuperiorModel= new BrandSuperiorModel();
			brandSuperiorModel.setName(brandSuperior);
			brandSuperiorModel = brandSuperiorDao.searchByModel(brandSuperiorModel);
			if(brandSuperiorModel == null){
				setErrorMsg(i, "母品牌不存在", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			String checkStr = buCode +"_" + month +"_" + brandSuperior;
			if(checkExistList.contains(checkStr)) {
				setErrorMsg(i, "导入事业部:"+buCode+"+月份:"+month+"+母品牌:"+brandSuperior+"已重复", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			checkExistList.add(checkStr);

			String currency = map.get("币种");
			if(checkIsNullOrNot(i, currency, "币种不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			currency = currency.trim() ;
			if(!DERP.CURRENCYCODE_HKD.equals(currency)){
				setErrorMsg(i, "币种只能是港币HKD", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}

			String totalPlanAmount = map.get("计划金额");
			if(StringUtils.isNotBlank(totalPlanAmount)){
				int indexOf = totalPlanAmount.indexOf(".");
				// 如果是小数
				if (indexOf != -1) {
					int count = totalPlanAmount.length() - 1 - indexOf;
					if (count > 2 ) {
						setErrorMsg(i, "计划金额只能为2位小数", resultList);
						failure += 1;
						flag &= false ;
						continue;
					}
				}
				if (Double.valueOf(totalPlanAmount) <= 0) {
					setErrorMsg(i, "计划金额只能为正数", resultList);
					failure += 1;
					flag &= false ;
					continue;
				}
			}

			totalPlanAmount = StringUtils.isBlank(totalPlanAmount) ? "0" : totalPlanAmount;
			if(totalPlanAmount.equals("0")) {
				setErrorMsg(i, "计划金额不能为空或为0", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}

			if(!isInteger(totalPlanAmount) ) {
				setErrorMsg(i, "计划金额非数字类型", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}

			//校验事业部+月份+母品牌 是否存在数据,存在则删除
			SaleAmountTargetModel model = new SaleAmountTargetModel();
			model.setBuId(buModel.getId());
			model.setMonth(month);
			model.setParentBrandId(brandSuperiorModel.getId());
			List<SaleAmountTargetModel> existList = saleAmountTargetDao.list(model);
			if(existList.size() > 0) {
				List<Long> delIds = existList.stream().map(SaleAmountTargetModel::getId).collect(Collectors.toList());
				delIdList.addAll(delIds);
			}
			//新增
			SaleAmountTargetModel newModel = new SaleAmountTargetModel();
			newModel.setBuId(buModel.getId());
			newModel.setBuName(buModel.getName());
			newModel.setMonth(month);
			newModel.setCurrency(currency);
			newModel.setDepartmentId(buModel.getDepartmentId());
			newModel.setDepartmentName(buModel.getDepartmentName());
			newModel.setParentBrandId(brandSuperiorModel.getId());
			newModel.setParentBrandName(brandSuperiorModel.getName());
			newModel.setTotalPlanAmount(new BigDecimal(totalPlanAmount));
			newModel.setCreatorId(user.getId());
			newModel.setCreator(user.getName());
			newModel.setCreateDate(TimeUtils.getNow());
			newModel.setModifyDate(TimeUtils.getNow());

			newAmountTargetList.add(newModel);

		}
		if(flag){
			if(delIdList != null && delIdList.size() > 0){
				saleAmountTargetDao.delete(delIdList);
			}
			for (SaleAmountTargetModel  model: newAmountTargetList){
				saleAmountTargetDao.save(model);
				// 报表操作日志
				String action = "";
				String remark = "";
				OperateReportLogModel tempLog =new OperateReportLogModel();
				String relCode = model.getBuId() +"-" +model.getMonth() +"-"+ model.getParentBrandId();
				tempLog.setRelCode(relCode);
				tempLog.setModule("2");
				List<OperateReportLogModel> operateList = operateReportLogDao.list(tempLog);
				if(operateList != null && operateList.size() > 0){
					action = "修改";
					remark = "修改月度销售额目标";
				}else{
					action = "新增";
					remark = "新增月度销售额目标";
				}
				OperateReportLogModel log =new OperateReportLogModel();
				log.setRelCode(relCode);
				log.setModule("2");
				log.setOperateId(user.getId());
				log.setOperater(user.getName());
				log.setOperateAction(action);
				log.setOperateDate(TimeUtils.getNow());
				log.setOperateResult("成功");
				log.setOperateRemark(remark);
				log.setCreateDate(TimeUtils.getNow());

				operateReportLogDao.save(log);
				success++;
			}

		}
		Map<String, Object> map = new HashMap<String, Object>() ;
		map.put("success", success);
		map.put("failure", failure);
		map.put("pass", pass);
		map.put("message", resultList);
		return map;
	}

	/**
	 * 删除
	 * @param paramsArr
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Integer delAmountTarget(String[] paramsArr) throws SQLException {
		int rows = 0;
		if (paramsArr != null && paramsArr.length > 0){
			List ids = Arrays.asList(paramsArr);
			rows = saleAmountTargetDao.delete(ids);
		}
		return rows;
	}


	/**
	 * 错误时，设置错误内容
	 * 
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index, String msg, List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}
	
	/**
	 * 判断输入字段是否为空
	 * 
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index, String content, String msg, List<ImportErrorMessage> resultList) {

		if (StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true;

		} else {
			return false;
		}

	}
	
	/**
	 * 数字判断 可为空
	 * @param str
	 * @return
	 */
	private boolean isInteger(String str) {
		boolean isNum = false;
		if(StringUtils.isBlank(str)){
			isNum = true;
		}else {
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");//判断整数
			Pattern pattern1 = Pattern.compile("(\\d+\\.\\d+)");//判断小数

			if (pattern.matcher(str).matches() || pattern1.matcher(str).matches()) {
				isNum = true;
			}
		}
        return isNum;
	}
	
	private boolean isMonth(String str) {
		Pattern pattern = Pattern.compile("^[\\d]{4}-[\\d]{2}$"); 
		return pattern.matcher(str).matches();
	}


}
