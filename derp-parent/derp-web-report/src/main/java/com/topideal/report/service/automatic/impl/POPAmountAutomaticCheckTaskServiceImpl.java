package com.topideal.report.service.automatic.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excelReader.ExcelReader;
import com.topideal.dao.order.OrderDao;
import com.topideal.dao.order.OrderItemDao;
import com.topideal.entity.dto.POPAmountAutomaticDTO;
import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;
import com.topideal.entity.vo.order.OrderItemModel;
import com.topideal.entity.vo.order.OrderModel;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.report.service.automatic.POPAmountAutomaticCheckTaskService;
import com.topideal.report.tools.DownloadExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class POPAmountAutomaticCheckTaskServiceImpl implements POPAmountAutomaticCheckTaskService{

	@Autowired
	private OrderDao orderDao ;
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	
	@Override
	public String createExcel(AutomaticCheckTaskModel model, String basePath) throws Exception {

		// 获取源文件
		List<Map<String, String>> sheetList = ExcelUtilXlsx.parseSheetOne(model.getSourcePath());
		String fileName = null;
		
		String mainSheetName = null;
		SXSSFWorkbook wb = new SXSSFWorkbook();
		
		List<POPAmountAutomaticDTO> amountDtoList = new ArrayList<POPAmountAutomaticDTO>();

		Set<String> externalCodeSet = new HashSet<>();

		for (int j = 1 ; j <= sheetList.size() ; j ++) {
			
			Map<String, String> map = sheetList.get(j - 1);
			POPAmountAutomaticDTO dto = excelMapExchangeDto(map);
			
			String externalCode = dto.getExternalCode();
			if(StringUtils.isBlank(externalCode)) {
				setExceptionMsg(dto, amountDtoList, "是", "外部订单号不能为空");
				continue;
			}
			
			BigDecimal payment = dto.getPayment();
			if(payment == null) {
				setExceptionMsg(dto, amountDtoList, "是", "实付总金额不能为空");
				continue;
			}

			BigDecimal taxFee = dto.getTaxFee();
			if(taxFee == null) {
				setExceptionMsg(dto, amountDtoList, "是", "税费不能为空");
				continue;
			}

			BigDecimal wayFrtFee = dto.getWayFrtFee();
			if(wayFrtFee == null) {
				setExceptionMsg(dto, amountDtoList, "是", "运费不能为空");
				continue;
			}

			BigDecimal discount = dto.getDiscount();
			if(discount == null) {
				setExceptionMsg(dto, amountDtoList, "是", "优惠减免金额不能为空");
				continue;
			}

			if (externalCodeSet.contains(externalCode)) {
				setExceptionMsg(dto, amountDtoList, "是", "导入数据存在相同订单号");
				continue;
			}

			externalCodeSet.add(externalCode);

			OrderModel orderModelParam = new OrderModel();
			orderModelParam.setExternalCode(dto.getExternalCode());
			orderModelParam.setMerchantId(model.getMerchantId());
			List<OrderModel> orderList = orderDao.list(orderModelParam);
			
			// 过滤已删除的订单
			orderList = orderList.stream().filter(order -> !DERP.DEL_CODE_006.equals(order.getStatus()))
					.collect(Collectors.toList()) ;

			/** 系统找不到该外部交易单号时，“是否异常”标识为“是”、差异原因提示：系统查无此外部交易单号；*/
			if (orderList.isEmpty()) {
				setExceptionMsg(dto, amountDtoList, "是", "系统查无此外部交易单号");
				continue;
				
			}
			
			if (orderList.size() > 1) {
				setExceptionMsg(dto, amountDtoList, "是", "系统根据该外部交易单号找到了多条电商订单");
				continue;
			}
			
			OrderModel orderModel = orderList.get(0);

			//核对导入的订单实付金额是否与原订单实付金额一致
			boolean isClose = false;
			if(orderModel.getPayment().compareTo(payment) != 0) {

				if (!orderModel.getStatus().equals(DERP_ORDER.ORDER_STATUS_4)) {
					setExceptionMsg(dto, amountDtoList, "是", "订单未发货!");
					continue;
				}

				OrderItemModel orderItemModel = new OrderItemModel();
				orderItemModel.setOrderId(orderModel.getId());

				List<OrderItemModel> itemList = orderItemDao.list(orderItemModel);
				//2.发货时间不为空则检查月结、关账时间。
				FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
				closeAccountsMongo.setMerchantId(orderModel.getMerchantId());
				closeAccountsMongo.setDepotId(orderModel.getDepotId());
				for (OrderItemModel itemModel : itemList) {
					closeAccountsMongo.setBuId(itemModel.getBuId());
					String maxMonth = "";
					maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
					if(orderModel.getDeliverDate()!=null && StringUtils.isNotBlank(maxMonth)) {
						//获取最大关账月份下一个月1日时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxMonth + "-01 00:00:00"));
						String maxCloseNextMonthDate = nextMonth + "-01 00:00:00";
						// 关账下个月日期大于 入库日期
						if (orderModel.getDeliverDate().getTime() <Timestamp.valueOf(maxCloseNextMonthDate).getTime()) {
							isClose = true;
							break;
						}
					}
				}

				if (isClose) {
					setExceptionMsg(dto, amountDtoList, "是", "与原订单金额有差异，但已关账状态下已发货的订单数据不能调整!");
					continue;
				}

				setExceptionMsg(dto, amountDtoList, "是", "与原订单金额不一致!");
				continue;

			}
			
			setExceptionMsg(dto, amountDtoList, "否", null);
			
		}

		mainSheetName = "电商订单金额自校验核对结果表";
		String[] mainKey = { "externalCode", "payment","taxFee","wayFrtFee","discount", "isException", "excetionResult"};
		String[] mainColumns = { "外部订单号", "实付总金额", "税费", "运费", "优惠减免金额", "是否异常", "异常原因" };
		wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, mainColumns, mainKey, amountDtoList);
		// 写出文件
		fileName = basePath + "/SJXY/" + model.getTaskCode() + "/电商订单金额自校验核对结果表 .xlsx";

		basePath = basePath += "/SJXY/" + model.getTaskCode();
		// 删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		// 创建目录
		new File(basePath).mkdirs();
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		fileOut.close();

		return fileName;
	}
	
	private POPAmountAutomaticDTO excelMapExchangeDto(Map<String, String> map) {
		
		POPAmountAutomaticDTO dto = new POPAmountAutomaticDTO() ;
		
		String externalCode = map.get("外部订单号") ;
		
		dto.setExternalCode(externalCode);
		
		String paymentStr = map.get("实付总金额");
		if(StringUtils.isNotBlank(paymentStr)) {
			BigDecimal payment = new BigDecimal(paymentStr) ;
			
			dto.setPayment(payment);
		}

		String taxFeeStr = map.get("税费");
		if(StringUtils.isNotBlank(taxFeeStr)) {
			BigDecimal taxFee = new BigDecimal(taxFeeStr) ;

			dto.setTaxFee(taxFee);
		}

		String wayFrtFeeStr = map.get("运费");
		if(StringUtils.isNotBlank(wayFrtFeeStr)) {
			BigDecimal wayFrtFee = new BigDecimal(wayFrtFeeStr) ;

			dto.setWayFrtFee(wayFrtFee);
		}

		String discountStr = map.get("优惠减免金额");
		if(StringUtils.isNotBlank(discountStr)) {
			BigDecimal discount = new BigDecimal(discountStr) ;

			dto.setDiscount(discount);
		}
		
		return dto ;
	}
	
	/**
	 * 设置错误信息
	 * @param dto
	 * @param dtoList
	 * @param isException
	 * @param exceptionMsg
	 */
	private void setExceptionMsg(POPAmountAutomaticDTO dto, List<POPAmountAutomaticDTO> dtoList, String isException, String exceptionMsg) {
		
		dto.setIsException(isException);
		dto.setExcetionResult(exceptionMsg);
		
		dtoList.add(dto);
	}


}
