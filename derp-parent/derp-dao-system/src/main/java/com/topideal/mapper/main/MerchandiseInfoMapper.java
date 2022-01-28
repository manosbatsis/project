package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.mapper.BaseMapper;

@MyBatisRepository
public interface MerchandiseInfoMapper extends BaseMapper<MerchandiseInfoModel> {

	/**
	 * 条件查询下拉列表
	 * 
	 * @param Long
	 */
	List<SelectBean> getSelectBean(@Param("id") Long id, @Param("depotId") Long depotId) throws SQLException;
	/**
	 * 导出商品信息
	 * @param id
	 * @return
	 */
	List<MerchandiseInfoDTO> exportMerchandiseInfoMap(MerchandiseInfoDTO dto) throws SQLException;
	/**
	 * 获取详情
	 */
	MerchandiseInfoModel getDetails(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 根据ids获取商品列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	List<MerchandiseInfoModel> getListByIds(List ids) throws SQLException;

	/**
	 * 根据条件获取商家所有的商品
	 * 
	 * @param model
	 * @param merchantId
	 * @return
	 */
	PageDataModel<MerchandiseInfoModel> getListforMerchantIdByPage(MerchandiseInfoModel model);
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<MerchandiseInfoDTO> getList(MerchandiseInfoDTO dto) throws SQLException;
	/**
	 * 获取分页总量
	 * @param dto
	 * @return
	 */
	int getListCount(MerchandiseInfoDTO dto);
	
	/**
	 * 采购订单取当前商家的商品
	 * @param model
	 * @return
	 */
	PageDataModel<MerchandiseInfoModel> getListToPurchaseByPage(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 新增商品根据商家id获取备案商品
	 * @return
	 */
	PageDataModel<MerchandiseInfoModel> getListForAddByPage(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 公共的商品分页弹窗
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<MerchandiseInfoModel> getPopupListByPage(MerchandiseInfoModel model) throws SQLException;

	PageDataModel<MerchandiseInfoDTO> getPopupListDTOByPage(MerchandiseInfoDTO model) throws SQLException;

	/**
	 * 新增客户销售价格弹框列表
	 * @return
	 */
	PageDataModel<MerchandiseInfoModel> getSaleListByPage(MerchandiseInfoModel model) throws SQLException;
	
	/**
	 * 根据ids获取客户销售价格列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	List<MerchandiseInfoModel> getSaleListByIds(List ids) throws SQLException;
	/**
	 * 结算价格-选择商品
	 * 获取商家下所有商品（条码相同只取一条记录）
	 * @param model
	 * @return
	 */
	PageDataModel<MerchandiseInfoModel> getListForSettlmentByPage(MerchandiseInfoModel model)throws SQLException;


	/**
	 * 通过商家商品条码找到对应代理商家的商品
	 * @Param
	 * @return
	 */
	/*List<MerchandiseInfoModel> getListByMerchantIdAndBarcode(@Param("merchantId") Long merchantId, @Param("barcode") String barcode);*/

	/**
	 * 通过代理商家商品条码找到对应商家的商品
	 * @Param
	 * @return
	 */
	List<MerchandiseInfoModel> getListByRelMerchantIdAndBarcode(@Param("merchantId") Long merchantId, @Param("barcode") String barcode);

	MerchandiseInfoDTO searchDTOById(@Param("id")Long id);

	/**
	 * 通过商品标准条码找到对应代理商家的商品货号
	 * @Param
	 * @return
	 */
	List<MerchandiseInfoModel> getRelListByCommbarcode(@Param("merchantIds") List<Long> merchantIds,@Param("commbarcode") String commbarcode) throws SQLException;


	/**
	 * 查询商品信息
	 */
	List<MerchandiseInfoModel> getListByModel(MerchandiseInfoModel model) throws SQLException;
	
	/**
	 * 根据条码查询商品标准条码不为空的商品
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<MerchandiseInfoModel> getByMerchantBarcode(MerchandiseInfoModel model) throws SQLException;
	/**
	 * 获取所有非卓普信/嘉宝 状态是启用的商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<MerchandiseInfoModel> getCopyGoodsList(Map<String, Object> paramMap) throws SQLException;
	/**
	 * 获取嘉宝和卓普信 外仓唯一码
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>>getZPXAndJBCopyGoodsList() throws SQLException;
	
	/**
	 * 获取推送卓普信推送商品档案
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>>getSendSapienceGoodsList(Map<String, Object> map) throws SQLException;

	/**
	 * 根据仓库+条码查询商品信息
	 * @param depotId
	 * @param barcodes
	 * @return
	 * @throws SQLException
	 */
	List<MerchandiseInfoModel> getByDepotAndBarcode(@Param("depotId") Long depotId, @Param("barcodes") List<String> barcodes, @Param("merchantId") Long merchantId) throws SQLException;
}
