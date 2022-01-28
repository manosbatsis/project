package com.topideal.dao.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import org.apache.commons.math3.distribution.LogNormalDistribution;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 商品信息 Dao
 * @author lchenxing
 */
public interface MerchandiseInfoDao extends BaseDao<MerchandiseInfoModel>{
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MerchandiseInfoDTO getListByPage(MerchandiseInfoDTO dto) throws SQLException;
	/**
	 * 导出商品信息
	 * @param model
	 * @return
	 */
	List<MerchandiseInfoDTO> exportList(MerchandiseInfoDTO dto) throws SQLException;
	
	/**
	 * 采购订单取当前商家的商品以及选择的供应商商品
	 * @param model
	 * @return
	 */
	MerchandiseInfoModel getListToPurchaseByPage(MerchandiseInfoModel model) throws SQLException;
	
	/**
	 * 保存商品和产品
	 * @param ProductInfoDTO
	 * @param MerchandiseInfoDTO
	 * @return
	 * */
	public void saveGoods(ProductInfoModel productInfoModel,MerchandiseInfoModel merchandiseInfoModel) throws Exception;
	
	/**
	 * 条件查询商品下拉列表
	 * @param Long
	 * */
	public List<SelectBean> getSelectBean(Long merchantId, Long depotId) throws SQLException;

	/**
	 * 新增商品信息
	 * 1、商品唯一标实不存在，新增
	 * 2、商品唯一标实存在，修改
	 * @param merchandiseInfoModel
	 * @return
	 * @throws SQLException
     */
	boolean insertMerchandisInfo(MerchandiseInfoModel merchandiseInfoModel)throws SQLException;
	
	/**
	 * 根据ids获取商品列表
	 * @return
	 * @throws SQLException
	 */
	List<MerchandiseInfoModel> getListByIds(List ids) throws SQLException;
	
	/**
	 * 根据ids获取客户销售价格列表
	 * @return
	 * @throws SQLException
	 */
	List<MerchandiseInfoModel> getSaleListByIds(List ids) throws SQLException;

	/**
	 * 获取商家所有的商品
	 * @param model
	 * @return
	 */
	MerchandiseInfoModel getListforMerchantIdByPage(MerchandiseInfoModel model);
	
	/**
	 * 新增商品根据商家id获取备案商品
	 * @return
	 */
	MerchandiseInfoModel getListForAddByPage(MerchandiseInfoModel model) throws SQLException;
	
	/**
	 * 公共的商品分页弹窗
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MerchandiseInfoModel getPopupListByPage(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 公共的商品分页弹窗
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	MerchandiseInfoDTO getPopupListDTOByPage(MerchandiseInfoDTO dto) throws SQLException;

	/**
	 * 新增客户销售价格弹框列表
	 * @return
	 */
	MerchandiseInfoModel getSaleListByPage(MerchandiseInfoModel model) throws SQLException;
	/**
	 * 结算价格-选择商品
	 * 获取商家下所有商品（条码相同只取一条记录）
	 * @param model
	 * @return
	 */
	MerchandiseInfoModel getListForSettlmentByPage(MerchandiseInfoModel model)throws SQLException;


	/**
	 * 通过商家商品条码找到对应代理商家的商品货号
	 * @Param
	 * @return
	 */
	/*List<MerchandiseInfoModel> getListByBarcode(Long merchantId, String barcode);*/

	/**
	 * 通过代理商家商品条码找到对应商家的商品货号
	 * @Param
	 * @return
	 */
	List<MerchandiseInfoModel> getRelListByBarcode(Long merchantId, String barcode);

	MerchandiseInfoDTO searchDTOById(Long id);

	/**
	 * 通过商品标准条码找到对应代理商家的商品货号
	 * @Param
	 * @return
	 */
	List<MerchandiseInfoModel> getRelListByCommbarcode(List<Long> merchantIds, String commbarcode) throws SQLException;

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
	 * 判断库存是否有该商品数据
	 * @param id
	 * @param id2
	 * @param goodsbarcode
	 * @param commonBarcode
	 * @return
	 */
	boolean getInventoryDetials(Long merchantId, Long goodsId)throws Exception;
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
	List<MerchandiseInfoModel> getByDepotAndBarcode(Long depotId, List<String> barcodes, Long merchantId) throws SQLException;
}