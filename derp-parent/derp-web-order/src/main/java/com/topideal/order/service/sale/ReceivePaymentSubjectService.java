package com.topideal.order.service.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;


/**
 * 收支费项 service
 * */
public interface ReceivePaymentSubjectService {

	/**
	 * 列表（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
    ReceivePaymentSubjectModel listReceivePaymentSubjectByPage(ReceivePaymentSubjectModel model)throws SQLException;
	/**
	 * 新增
	 * @param name
	 * @param code
	 * @param subCode
	 * @param subName
	 * @return
	 * @throws Exception
	 */
	String saveReceivePaymentSubject(String name,String code, String subCode, String subName,User user)throws Exception;

	/**
	 * 修改状态
	 * @param id
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	boolean enableNcPay(Long id, String type) throws SQLException;

	/**
	 * 根据id获取对象用于回显
	 * @param id
	 * @return
	 * @throws SQLException
	 */
    ReceivePaymentSubjectModel getById(Long id) throws SQLException ;

	/**
	 * 修改
	 * @param id
	 * @param name
	 * @param code
	 * @param subCode
	 * @param subName
	 * @return
	 * @throws SQLException
	 */
    String  modifyReceivePaymentSubject(String id, String name, String code, String subCode, String subName)  throws SQLException ;


	/**
	 * NC收支费项及科目关系
	 * @return
	 * @throws SQLException
	 */
	public List<ReceivePaymentSubjectModel> getReceivePaymentSubjectList() throws SQLException;
}
