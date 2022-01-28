package com.topideal.order.service.bill;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.ReceiveBillVerificationDTO;
import com.topideal.entity.vo.bill.ReceivePaymentNotesModel;

/**
 * @Description: 应收账单service
 * @Author: 杨创
 * @Date: 2020-07-27 15:01
 **/
public interface ReceiveBillVerificationService {

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
	ReceiveBillVerificationDTO listReceiveBillVerificationByPage(ReceiveBillVerificationDTO dto, User user) throws SQLException;
	
	/**
	 * 查询回款备注
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<ReceivePaymentNotesModel> getNotesList(ReceivePaymentNotesModel model)throws SQLException;
	
	/**
	 * 保存备注
	 * @param user
	 * @param receiveCode
	 * @param notes
	 * @return
	 * @throws SQLException
	 */
	boolean saveNotes(User user,String receiveCode,String notes)throws SQLException;
	
	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>>  exportBillVerification(ReceiveBillVerificationDTO dto, User user)throws SQLException;


}
