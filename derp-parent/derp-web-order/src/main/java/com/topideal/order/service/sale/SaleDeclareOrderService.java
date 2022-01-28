package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleDeclareOrderDTO;
import com.topideal.entity.dto.sale.SaleDeclareOrderItemDTO;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;
import com.topideal.order.webapi.sale.form.MerchandiseForm;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface SaleDeclareOrderService {

	/**
	 * 获取列表 分页
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	SaleDeclareOrderDTO listDTOByPage(SaleDeclareOrderDTO dto, User user) throws Exception;
	/**
	 * 销售单转预申报单 封装数据展示
	 * @param saleIds
	 * @return
	 * @throws Exception
	 */
	SaleDeclareOrderDTO saleOrderToSaleDeclare(String saleIds) throws Exception;
	/**
	 * 统计类型数量
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getDeclareTypeNum(SaleDeclareOrderDTO dto, User user);
	/**
	 * 获取详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	SaleDeclareOrderDTO searchDetail(Long id) throws Exception;
	/**
	 * 获取表体详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<SaleDeclareOrderItemDTO> searchItemsByOrderId(Long id) throws Exception;
	/**
	 * 保存
	 * @param dto
	 * @throws Exception
	 */
	void saveSaleDeclareOrder(SaleDeclareOrderDTO dto, User user, LogisticsAttorneyModel logisticsAttorneyModel) throws Exception;
	/**
	 * 下推出库指令
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	void modifyPushOutOrder(Long id, User user) throws Exception;
	/**
	 * 更新时间轨迹信息
	 * @param dto
	 * @throws Exception
	 */
	void updateTimeTrace(Long id, String orderTime, User user, String operate) throws Exception;
	/**
	 * 打托出库
	 * @param id
	 * @return
	 * @throws Exception
	 */
	void saveSaleDeclareOut(SaleDeclareOrderDTO dto, User user, String deliverDateStr) throws Exception ;
	/**
	 * 删除
	 * @param dto
	 * @throws Exception
	 */
	void delSaleDeclareOrder(String ids) throws Exception;
	/**
	 * 获取所有数据
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<SaleDeclareOrderDTO> listSaleDeclareOrder(SaleDeclareOrderDTO dto, User user) throws Exception;

	/**
	 * 导出清关
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> exportCustomsDeclares(Long id, List<Long> fileTempIds, String type, User user) throws Exception;
	/**
	 * 选择商品弹窗
	 * @param form
	 * @return
	 * @throws Exception
	 */
	SaleDeclareOrderItemDTO getSalePopupListByPage(MerchandiseForm form) throws Exception;
	/**
	 * 导出物流委托书
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> exportLogisticsDelegation(Long id)  throws Exception;

	/**
	 * 导出打托资料
	 * @param id
	 * @return
	 * @throws Exception
	 */
	SXSSFWorkbook exportPallteizeData(String sheetName, String[] columns, Long id, User user) throws Exception;
	/**
	 * 修改物流委托书
	 * @param dto
	 * @throws Exception
	 */
	void modifyLogisticsAttorney(LogisticsAttorneyModel logisticsAttorneyModel , User user) throws Exception;
	/**
	 * 复制
	 * @param id
	 * @return
	 * @throws Exception
	 */
	SaleDeclareOrderDTO copySaleDeclare(Long id) throws Exception;
}
