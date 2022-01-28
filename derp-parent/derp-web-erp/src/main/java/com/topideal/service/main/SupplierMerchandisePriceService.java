package com.topideal.service.main;


import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.entity.dto.main.SupplierMerchandisePriceDTO;
import com.topideal.entity.vo.main.SupplierMerchandisePriceModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 供应商商品价格service实现类
 */
public interface SupplierMerchandisePriceService {
	/**
	 * 供应商商品价格列表（分页）
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	SupplierMerchandisePriceDTO listSMPrice(SupplierMerchandisePriceDTO dto) throws SQLException;

	/**
	 * 删除供应商商品价格
	 * 
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	boolean delSMPrice(List ids) throws SQLException;

	/**
	 * 导入供应商商品价格
	 * 
	 * @param data
	 * @param user
	 * @return
	 */
	Map importSMPrice(Map<Integer, List<List<Object>>> data, User user);


	/**
	 * 根据条件获取信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<SupplierMerchandisePriceDTO> getSMPriceList(SupplierMerchandisePriceDTO dto) throws SQLException;
	/**
	 * 审核
	 * @param list
	 * @param user
	 * @throws Exception
	 */
	void auditSMPrice(List<Long> list , User user, String auditType) throws Exception;

	/**
	 * 采购管理启用时，查询采购价格
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	com.alibaba.fastjson.JSONObject getSMPriceByPurchaseOrder(SupplierMerchandisePriceModel model) throws SQLException;

	/**
	 * 统计各个状态下的采购价格数量
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SupplierMerchandisePriceDTO statisticsStateNum(SupplierMerchandisePriceDTO dto);

	/**
	 * 提交
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	void submitSMPrice(List<Long> ids , User user) throws Exception;

	/**
	 * 编辑保存
	 * @param dto
	 * @param user
	 * @throws Exception
	 */
	void modifySMPrice(SupplierMerchandisePriceDTO dto , User user) throws Exception;

	/**
	 * 根据id查看详情
	 * @param id
	 * @return
	 */
	SupplierMerchandisePriceDTO searchDTOById(Long id);

	/**
	 * 申请作废
	 * @param user
	 * @param ids
	 * @param remark
	 * @return
	 */
	void submitInvalid(User user,String ids,String remark) throws Exception;

	/**
	 * 作废审核
	 * @param user
	 * @param ids
	 * @param auditResult
	 * @return
	 */
	void invalidAudit(User user,String ids,String auditResult) throws Exception;

	/**
	 * 新增
	 * @param dto
	 */
	ResponseBean addSMPrice(User user, SupplierMerchandisePriceDTO dto) throws Exception;

	/**
	 * 获取编码
	 * @param id
	 * @return
	 */
    String preGetCode(Long id) throws Exception;
}
