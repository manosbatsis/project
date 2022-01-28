package com.topideal.service.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.dto.main.UpdateDepotOrderDTO;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.webapi.form.UpdateDepotOrderForm;

/**
 * 商品管理service实现类
 */
public interface MerchandiseService {
	/**
	 * 商品关联仓库导入
	 */
	Map importMerchandiseDepotRel(Map<Integer, List<List<Object>>> data, User user) throws Exception;
	/**
	 * 商品关联仓库保存
	 * @throws SQLException
	 */
	boolean saveMerchandiseDepotRel(User user,Long goodsId,String depotIds) throws SQLException;
	/**
	 * 商品列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	MerchandiseInfoDTO listMerchandise(MerchandiseInfoDTO model) throws SQLException;
	
    /**
     *  导出商品信息
     * @param id
     * @return
     * @throws Exception
     */
    List<MerchandiseInfoDTO> exportList(MerchandiseInfoDTO dto) throws SQLException;

	/**
	 * 新增商品
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean saveMerchandise(User user,MerchandiseInfoModel model,String customsAreaId) throws Exception;

	/**
	 * 根据商品id删除商品(支持批量)
	 *
	 * @param ids
	 * @return
	 */
	boolean delMerchandise(List<Long> ids) throws Exception;

	/**
	 * 录入修改商品
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean modifyMerchandise(User user,MerchandiseInfoModel model,String customsAreaId) throws Exception;

	/**
	 * 主数据修改商品
	 *
	 * @param model
	 * @param groupIds
	 * @param groupNums
	 * @param groupPrices
	 * @return
	 * @throws SQLException
	 */
	boolean modifyMerchandiseByMain(MerchandiseInfoModel model, String groupIds, String groupNums, String groupPrices)
			throws Exception;

	/**
	 * 根据id获取商品详情
	 *
	 * @param id
	 * @return
	 */
	MerchandiseInfoDTO searchDetail(Long id) throws SQLException;

	/**
	 * 导入商品信息
	 *
	 * @param data
	 *            解析excel的商品数据
	 * @return
	 * @throws SQLException
	 */
	Map importMerchandise(Map<Integer, List<List<Object>>> data, User user) throws Exception;
	
	/**
	 * 导入商品信息临时
	 *
	 * @param data
	 *            解析excel的商品数据
	 * @return
	 * @throws SQLException
	 */
	Map importMerchandiseLinshi(Map<Integer, List<List<Object>>> data, User user) throws Exception;
	
	

	/**
	 * 条件查询商品下拉列表
	 *
	 * @param Long
	 */
	public List<SelectBean> getSelectBean(Long merchantId, Long depotId) throws SQLException;

	/**
	 * 采购订单获取商品列表
	 *
	 * @param model
	 *            商品信息
	 * @param type
	 *            类型 1采购 2调拨
	 */
	MerchandiseInfoModel getList(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 根据仓库类型获取商品列表
	 */
	public MerchandiseInfoModel getListForDepotType(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 根据商品id获取商品列表
	 */
	List<MerchandiseInfoDTO> getListByIds(List ids) throws SQLException;

	/**
	 * 根据商家ID获取商品列表
	 */
	MerchandiseInfoModel getListByMerchantId(MerchandiseInfoModel model, Long outDepotId) throws SQLException;

	/**
	 * 根据商家id获取备案商品列表
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MerchandiseInfoModel getListByMerchant(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 客户销售价格弹框列表
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MerchandiseInfoModel getSaleListByPage(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 根据商品id获取客户销售价格列表
	 */
	List<MerchandiseInfoModel> getSaleListByIds(List ids) throws SQLException;


	/**
	 * 公共的商品弹窗
	 * @return
	 */
	MerchandiseInfoModel getPopupList(MerchandiseInfoModel model) throws SQLException;

	public List<MerchandiseInfoModel> getMerchandiseList(MerchandiseInfoModel model) throws SQLException;

	/**
	 * 上传图片
	 * @param fileName   文件名称
	 * @param fileBytes  文件字节数组
	 * @param fileSize   文件大小
	 * @param ext        文件后缀
	 * @param userId     用户id
	 * @param userName   用户名称
	 * @param id         供应商Id
	 * @param type       用于标识上传图片的字段 1:商品图片
	 * @return
	 * @throws SQLException
	 */
	String uploadFile(String fileName, byte[] fileBytes, Long fileSize, String ext, Long userId, String userName, Long id, String type) throws SQLException;

	/**
	 * 导入上传文件
	 * @param barcodeMap
	 * @param merchantId
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> uploadImportImage(Map<String, MerchandiseInfoModel> barcodeMap, Long merchantId)throws SQLException;;
	
	
	//通过id获取图片
	public MerchandiseInfoModel searchMerchandise(Long id) throws SQLException;
	
	/**
	 * 结算价格-选择商品
	 * 获取商家下所有商品（条码相同只取一条记录）
	 * @param model
	 * @return
	 */
	MerchandiseInfoModel getListForSettlmentByPage(MerchandiseInfoModel model)throws SQLException;
	
	/**
	 * 禁用启用
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean auditMerchandies(MerchandiseInfoModel model,User user) throws SQLException;


	/**
	 * 根据ids，单据类型，入仓仓库ID/出仓仓库ID 查询对应商品信息
	 * @Param
	 * @return
	 */
	List<Map<String, Object>> getListByIdsAndOutInDepot(List<String> ids, String type, Long depotId, Long merchantId) throws SQLException;

	/**
	 * 不同单据公共的商品弹窗
	 * @param model 商品信息
	 * @return
	 */
	MerchandiseInfoDTO getOrderPopupList(MerchandiseInfoDTO dto) throws SQLException;

	/**
	 * 不同单据公共的商品弹窗
	 * @param dto 商品信息
	 * @return
	 */
	MerchandiseInfoDTO getOrderPopupDtoList(MerchandiseInfoDTO dto,String productRestriction,User user) throws Exception;

	/**
	 * 不同单据切仓商品修改
	 * @param form
	 * @param user
	 * @return
	 * @throws Exception
	 */
	List<UpdateDepotOrderDTO> getOrderPopupDtoList(UpdateDepotOrderForm form, User user) throws Exception;

	/**
	 * 根据ids，单据类型，入仓仓库ID/出仓仓库ID 查询对应商品信息
	 * @Param
	 * @return
	 */
	List<Map<String, Object>> getListByIdsAndDepot(List<String> ids, String type, Long depotId, Long merchantId, String unNeedIds, User user) throws SQLException;

	/**
	 * 不同单据切仓商品修改返回商品集合
	 * @param form
	 * @param user
	 * @return
	 * @throws Exception
	 */
	List<UpdateDepotOrderDTO> getOrderPopupList(UpdateDepotOrderForm form, User user) throws Exception;

}
