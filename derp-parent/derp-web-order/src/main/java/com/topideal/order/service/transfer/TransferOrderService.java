package com.topideal.order.service.transfer;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOrderFrom;
import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import com.topideal.entity.dto.transfer.TransferOutInBean;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;
import com.topideal.order.webapi.transfer.enums.ActionTypeEnum;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 调拨订单 service
 * @author yucaifu
 * */
public interface TransferOrderService {


	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 */
	TransferOrderDTO listTransferOrderPage(TransferOrderDTO dto, User user) throws SQLException;
	/**根据id查询调拨订单
	 * @param id
	 * @return
	 */
	public TransferOrderDTO searchTransferOrderById(Long id) throws SQLException;

	public Map<String, String> delTransferOrder(Long userId,String userName,List<Long> ids) throws SQLException;

	/**
	 * 新增或编辑调拨单（带审核）
	 * @param user
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveOrUpdateTransferOrder(User user, TransferOrderFrom model, ActionTypeEnum actionTypeEnum) throws Exception;

	/**
	 * 调入调出流水列表（分页）
	 */
	public TransferOutInBean  listTransferOutInPage(TransferOutInBean model, User user) throws SQLException;

	/**
	 * 保存导入调拨单
	 * @param data 数据
	 * @param user 当前登录用户
	 * **/
	public Map<String, Object> saveImportTransfer(Long userId,String userName,Long merchantId,String merchantName,String topidealCode,
			String relMerchantIds,List<List<Map<String, String>>> data) throws Exception;

	/**修改lbxno
	 * @throws SQLException
	 *
	 * */
	public Map<String, Object> updateLbxNo(Long userId,String userName,Long transOrderId,String newLbxNo) throws Exception;
	/**
     * 根据调拨单id统计调出商品数量
     * */
    public List<Map<String, Object>> getItemSumByIds(List<Long> Ids);

	/**
	 * 暂存调拨订单，保存时只校验仓库、商品是否有值，这两个字段用户有输入值即可保存不做其他任何校验
	 */
	public Map<String, Object> saveTempTransferOrder(Long userId,String userName,Long merchantId,String MerchantName,String topidealCode,TransferOrderFrom model) throws Exception;

	/**
	 * 根据条件统计导出调拨订单数量
	 */
	public Long countForExport(TransferOrderDTO dto, User user);

	/**
	 * 根据条件查询导出调拨订单
	 */
	public List<Map<String, Object>> listForExport(TransferOrderDTO dto, User user);

	/**
	 * 根据条件查询导出调拨订单表体
	 */
	public List<Map<String, Object>> listForExportItem(TransferOrderDTO dto, User user);

	/**
	 * 根据订单类型和调拨订单id查询对应出入库单是否存在
	 */
	public Map<String, String> isExistOrder(TransferOrderDTO dto) throws SQLException;

	/**
	 * 校验入库时间
	 */
	Map<String, String> validInDepotDate(Long id, String transferInDate) throws Exception;
	/**
	 * 校验出库时间
	 */
	Map<String, String> validOutDepotDate(Long id, String transferOutDate) throws Exception;

	/**
	 * 商品导入
	 * @param data
	 * @param inDepotId
	 * @param user
	 * @return
	 */
	Map importTransferGoods(List<List<Map<String, String>>> data,String outDepotId, String inDepotId, User user );

	/**
	 * 商品导出
	 * @param json
	 * @return
	 */
	List<TransferOrderItemDTO> exportListItem(String json);

	/**
	 * 一线清关资料调入仓库多模板导出
	 * @param id
	 * @param inFileTempIds
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> exportInDepotCustomsDeclares(Long id, List<Long> inFileTempIds) throws Exception;

	/**
	 * 一线清关资料调出仓库多模板导出
	 * @param id
	 * @param outFileTempIds
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> exportOutDepotCustomsDeclares(Long id, List<Long> outFileTempIds) throws Exception;

	/**
	 * 装箱明细导入
	 * @param data
	 * @param orderId
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	Map importPackingDetails(List<List<Map<String, String>>> data, User user,List<TransferOrderItemModel> itemList) throws Exception;

	/**
	 * 获取装箱明细
	 * @param id
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	List<PackingDetailsModel> getPackingDetail(Long id) throws SQLException;
}
