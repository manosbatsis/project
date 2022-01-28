package com.topideal.order.service.common;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.OperateLogDTO;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/***
 * 公共业务service
 * @author gy
 *
 */
public interface CommonBusinessService {

	/**
	 * 采购入库自动勾稽
	 * @param purchaseWarehouseCode
	 * @throws Exception
	 */
//	void saveAutoPurchaseAnalysis(String purchaseWarehouseCode) throws Exception ;

	/**
	 * 采购汇率回填
	 * @param purchaseOrder
	 * @param date
	 * @throws SQLException
	 */
	void saveRate(PurchaseOrderModel purchaseOrder, Timestamp date) throws SQLException;

	/**
	 * 采购修改勾稽状态
	 * @param purchaseOrder
	 * @param date
	 * @throws SQLException
	 */
	void modifyCorrelationstatus(PurchaseWarehouseModel purchaseWarehouseModel) throws SQLException;

	/**
	 * 保存日志
	 * @param user 用户信息
	 * @param module 模块
	 * @param code 关联编码
	 * @param operateAction 操作项
	 * @param result 结果
	 * @param remark 备注
	 * @throws SQLException
	 */
	void saveLog(User user, String module, String code, String operateAction,
			String result, String remark) throws SQLException;

	/**
	 * 获取日志列表
	 * @param queryModel
	 * @return
	 * @throws SQLException
	 */
	List<OperateLogDTO> getOperateLogList(OperateLogModel queryModel) throws SQLException;
}
