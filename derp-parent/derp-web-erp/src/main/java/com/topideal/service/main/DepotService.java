package com.topideal.service.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.DepotInfoDTO;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.BatchValidationInfoModel;
import com.topideal.entity.vo.main.DepotCustomsRelModel;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import net.sf.json.JSONArray;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
/**
 * 仓库管理service实现类
 */
public interface DepotService {
	
	/**
	 * 仓库列表（分页）
	 * @param model 
	 * @return
	 */
	DepotInfoDTO listDepot(DepotInfoDTO dto) throws SQLException;
	
	/**
	 * 新增仓库
	 * @param model
	 * @param isOutDependIns 
	 * @param isInDependOuts 
	 * @param productRestrictions 
	 * @param isInvertoryFallingPrices 
	 * @param isInOutInstructions 
	 * @return
	 */
	boolean saveDepot(User user, DepotInfoModel model, JSONArray jSONArray) throws Exception;
	
	/**
	 * 根据仓库id删除仓库(支持批量)
	 * @param ids
	 * @return
	 */
	boolean delDepot(List<Long> ids) throws SQLException;
	
	/**
	 * 修改仓库信息
	 * @param model
	 * @param isOutDependIns 
	 * @param isInDependOuts 
	 * @param productRestrictions 
	 * @param isInvertoryFallingPrices 
	 * @param isInOutInstructions 
	 * @param
	 * @return
	 */
	boolean modifyDepot(User user,DepotInfoModel model,JSONArray jSONArray) throws Exception;
	
	/**
	 * 根据id获取仓库信息详情
	 * @param id
	 * @return
	 */
	DepotInfoDTO searchDetail(Long id) throws SQLException;
	
	/**
	 * 导入仓库信息
	 * @param data 解析excel的仓库信息数据
	 * @return  Map importDepot(Map<Integer, List<List<Object>>> data,User user);
	 * @throws SQLException 
	 */
	Map importDepot(User user, Map<Integer, List<List<Object>>> data, Long userId);
	
	/**
	 * 查询商品下拉列表
	 * @param
	 * */
	public List<SelectBean> getSelectBean(DepotInfoModel depotInfoModel) throws SQLException;
	
	/**
	 * 查询代理商家下拉列表
	 * @param
	 * */
	public List<MerchantInfoModel> getSelectPoxy() throws SQLException;
	
	/**
	 * 查询仓库下拉列表，是否同关区
	 * */
	List<SelectBean> getSelectBeanByArea(DepotInfoDTO dto) throws SQLException;
	
	/**
	 * 9011弹框列表查询
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<BatchValidationInfoModel> getListById(Long id) throws SQLException;

	boolean audit(User user, DepotInfoModel model) throws SQLException;
	/**
	 * admin账号查询仓库下拉列表
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBeanForAdmin(DepotInfoModel model) throws SQLException;
	
	
	/**
	 * 根据页面传入商家id获取此商家下仓库的下拉框
	 * @param
	 * */
	public List<SelectBean> getSelectBeanByMerchantRel(DepotMerchantRelDTO dto) throws SQLException;

	/**
	 * 获取商家下仓库的下拉框
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	public List<DepotInfoDTO> getSelectBeanByDTO(DepotInfoDTO dto) throws SQLException;
	
	/**
	 * 获取仓库关区关系表
	 * @param model
	 * @throws SQLException
	 */
	List<DepotCustomsRelModel> getDepotCustomsRel(DepotCustomsRelModel model)throws SQLException;

	/**
	 * 根据页面传入商家id、事业部id、仓库类别、是否代客管理仓库、是否是代销仓获取此商家事业部下仓库的下拉框
	 * */
	List<SelectBean> getSelectBeanByMerchantBuRel(MerchantDepotBuRelDTO dto) throws SQLException;
}
