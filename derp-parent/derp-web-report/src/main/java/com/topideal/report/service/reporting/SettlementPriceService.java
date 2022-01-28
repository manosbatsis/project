package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.SettlementPriceDTO;
import com.topideal.entity.dto.SettlementPriceExamineDTO;
import com.topideal.entity.dto.SettlementPriceRecordDTO;
import com.topideal.entity.vo.reporting.SettlementPriceModel;
import com.topideal.entity.vo.reporting.SettlementPriceRecordModel;
import com.topideal.entity.dto.SettlementPriceItemDTO;

/**
 * 结算价格 service
 */
public interface SettlementPriceService {
	/**
	 * 根据条件获取成本单价列表以及修改记录的信息
	 * @param model
	 * @return
	 */
	 List<SettlementPriceDTO> listSettlementPrice(User user,SettlementPriceDTO dto) throws SQLException;

	/**
	 * 结算价格列表
	 * @param model 
	 * @return
	 */
	SettlementPriceModel listPrice(SettlementPriceModel model) throws SQLException;
	/**
	 * 新增
	 * @param merchantName 
	 * @param merchantId 
	 * @param user 
	 * @throws SQLException
	 */
	boolean saveSettlementPrice(String json, Long merchantId, String merchantName, User user) throws Exception;
	
	/**
	 * 编辑
	 * @param json
	 * @param user 
	 * @return
	 * @throws Exception
	 */
	boolean modifySettlementPrice(String json, Long merchantId, String merchantName, User user) throws Exception;
	/**
	 *  导入结算价格信息--有问题，获取不到单位名称、原产国名称
	 * @return
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	Map importPrice(Map<Integer, List<List<Object>>> data, User user) throws SQLException, ParseException;
	
	/**
	 * 删除
	 * 
	 * @param ids
	 * @return
	 */
	boolean delPrice(List<Long> ids) throws SQLException;
	
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	SettlementPriceModel searchDetail(Long id) throws SQLException;
	
	/**
	 * 列表查询
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SettlementPriceModel> list(SettlementPriceModel model) throws SQLException;
	/**
	 * 获取已关账的最大日期
	 * @param barcode
	 * @param merchantId
	 * @return
	 * @throws Exception 
	 */
	String getMaxMonthByParam(Long merchantId) throws Exception;
	/**
	 * 获取变更记录分页数据
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	SettlementPriceRecordModel listRecordPrice(SettlementPriceRecordModel model)throws SQLException;
	
	
	/**
	 * 根据商品ID查看组合商品的详情
	 * @param goodId
	 * @return
	 * @throws SQLException
	 */
	//List<MerchandiseInfoModel> getAllGroupMerchandiseByGroupId(Long goodsId) throws SQLException;

	/**
	 * 提交审核
	 * @param list
	 * @return 
	 * @throws Exception 
	 */
	boolean saveAudit(List list, User user) throws Exception;

	/**
	 * 获取审核分页列表
	 * @param model
	 * @return
	 */
	SettlementPriceExamineDTO listExamineList(SettlementPriceExamineDTO model, User user);

	/**
	 * 审核
	 * @param list
	 * @param status
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	boolean examine(List list, String status, User user) throws Exception;

	/**
	 * 分页DTO
	 * @param dto
	 * @return
	 */
	SettlementPriceDTO listPriceDTO(User user,SettlementPriceDTO dto) throws SQLException;

	/**
	 * 历史记录分页
	 * 
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	SettlementPriceRecordDTO listRecordPriceDTO(SettlementPriceRecordDTO dto) throws SQLException;


	/**
	 * 新增API接口
	 * @param itemList
	 * @param merchantId
	 * @param merchantName
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> saveSettlementPrice(Long buId, List<SettlementPriceItemDTO> itemList, Long merchantId, String merchantName, User user) throws Exception;


	/**
	 * 编辑API接口
	 * @param user
	 * @return
	 * @throws Exception
	 */
	boolean modifySettlementPrice(List<SettlementPriceItemDTO> itemList, User user, Long buId, Long id) throws Exception;
}
