package com.topideal.order.service.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.purchase.DeclareOrderDTO;
import com.topideal.entity.dto.purchase.DeclareOrderItemDTO;
import com.topideal.entity.dto.purchase.FirstCustomsInfoDTO;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.entity.vo.purchase.DeclareOrderItemModel;
import com.topideal.entity.vo.purchase.DeclareOrderModel;
import com.topideal.entity.vo.purchase.FirstCustomsInfoModel;
import com.topideal.order.webapi.purchase.form.DeclareDetailsAddForm;
import com.topideal.order.webapi.purchase.form.DeclareOrderDeliveryForm;
import com.topideal.order.webapi.sale.form.MerchandiseForm;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 预申报单 service
 *
 * @author lian_
 *
 */
public interface DeclareOrderService {

	/**
	 * 导出表体
	 * @param
	 * @param
	 * @return
	 * @throws SQLException
	 */
	List<DeclareOrderItemDTO> getDetailsByExport(Long id) throws SQLException;

	/**
	 * 导出表头
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<DeclareOrderDTO> getDeclareOrderByExport(DeclareOrderDTO dto, User user) throws SQLException;

	/**
	 * 预申报单（分页）
	 *
	 * @param
	 * @return
	 */
	DeclareOrderDTO listDeclare(DeclareOrderDTO dto,User user) throws SQLException;

	/**
	 * 根据id获取预申报单详情
	 *
	 * @param id
	 * @return
	 */
	DeclareOrderModel searchDetail(Long id) throws SQLException;

	/**
	 * 修改
	 *
	 * @param model
	 * @param logisticsAttorneyModel
	 * @param purchaseCode
	 * @return
	 * @throws Exception
	 */
	void modifyDeclare(DeclareOrderModel model, LogisticsAttorneyModel logisticsAttorneyModel, User user, String purchaseCode,List<PackingDetailsModel> packingList) throws Exception;

	/**
	 * 批量提交预申报单
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	void submitDeclareOrder(Long id, User user) throws Exception;

	/**
	 * 批量取消预申报单
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	void delDeclare(List<Long> list, User user) throws SQLException,RuntimeException;

	/**
	 * 获取预申报单商品列表
	 * @param id
	 * @param type 1-查看详情 2-复制
	 * @return
	 * @throws SQLException
	 */
	List<DeclareOrderItemModel> getItem(Long id,String type) throws SQLException;

	/**
	 * 一线清关资料导出
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	FirstCustomsInfoDTO getCustomsDeclare(Long id) throws SQLException;
	/**
	 * 一线清关资料导出
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> exportCustomsDeclares(Long id, List<Long> fileTempIds, User user) throws SQLException, Exception;

	/**
	 * 编辑一线清关资料
	 * @param model
	 * @return
	 */
	boolean modifyCustomsDeclare(FirstCustomsInfoModel model, String[] goodsIds, String[] palletNos, String[] cartons, String name) throws SQLException, RuntimeException;

	/**
	 * 保存一线清关资料
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws
	 */
	FirstCustomsInfoDTO saveCustomsDeclare(Long id) throws SQLException, RuntimeException;

	/**
	 * 查看详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	DeclareOrderDTO searchDTODetail(Long id) throws Exception;

	/**
	 * 复制预申报单
	 * @param id
	 * @return
	 */
	DeclareOrderDTO declareOrderByCopyId(Long id) throws Exception;

	/**
	 * 导出商品
	 * @param
	 * @return
	 */
	List<Map<String, Object>> getGoodsList(String itemList) throws Exception;

	/**
	 * 导入商品
	 * @param
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> importGoods(List<Map<String, String>> data, String orderId,String type,User user) throws SQLException;

	/**
	 * 导入装箱明细
	 * @param data
	 * @param user
	 * @param declareOrderId
	 * @return
	 */
    Map<String, Object> importPackingDetails(List<Map<String, String>> data, User user, List<DeclareOrderItemDTO> itemList) throws SQLException;

    /**
     * 更新物流轨迹时间
     * @param user
     * @throws SQLException
     */
	void updateTrajectory(DeclareDetailsAddForm form, User user) throws SQLException;

	/**
	 * 预申报打托入库
	 * @param form
	 * @param user
	 * @throws SQLException
	 * @throws Exception
	 */
	void saveDeclareDelivery(DeclareOrderDeliveryForm form, User user) throws Exception;

	/**
	 * 统计类型数量
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getDeclareTypeNum(DeclareOrderDTO dto, User user);


	/**
	 * 常规修改预申报
	 * @param declareOrderModel
	 */
	void modifyDeclareOrder(DeclareOrderModel declareOrderModel) throws SQLException;


	/**
	 * 编辑物流委托书
	 * @param model
	 * @param user
	 * @throws SQLException
	 */
	void modifyLogisticsAttorney(LogisticsAttorneyModel model, User user) throws SQLException;


	/**
	 * 复制前检查
	 * @param user
	 * @param id
	 * @return
	 */
	void getDeclareDetailCheckList(User user,Long id) throws SQLException;

	/**
	 * //检查相同SKU是否存在多条
	 * @param declareOrderId
	 * @throws Exception
	 */
	void checkRepeatGoods(Long declareOrderId) throws Exception;

	/**
	 * 选择商品弹窗
	 * @param form
	 * @return
	 * @throws Exception
	 */
	DeclareOrderItemDTO getItemPopupListByPage(MerchandiseForm form) throws Exception;

	/**
	 * 获取装箱明细
	 * @param id
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	List<PackingDetailsModel> getPackingDetail(Long id) throws SQLException;
}
