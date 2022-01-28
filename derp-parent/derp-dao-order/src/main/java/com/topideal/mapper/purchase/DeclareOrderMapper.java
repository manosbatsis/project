package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.DeclareOrderDTO;
import com.topideal.entity.vo.purchase.DeclareOrderItemModel;
import com.topideal.entity.vo.purchase.DeclareOrderModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.json.pushapi.epass.e01.PurchaseOrderRootJson;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;



/**
 * 预申报单
 *
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface DeclareOrderMapper extends BaseMapper<DeclareOrderModel> {


	/**
	 * 导出表头
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<DeclareOrderDTO> getDeclareOrderByExport(DeclareOrderDTO dto) throws SQLException;


	/**
	 * 获取详情
	 */
	DeclareOrderModel getDetails(DeclareOrderModel model) throws SQLException;

	/**
	 * 条件过滤查询，会查询出表体、商品
	 *
	 * @return
	 */
	List<DeclareOrderItemModel> getAll(@Param("id") Long id) throws SQLException;

	/**
	 * 根据表头id查询商品
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<DeclareOrderItemModel> getItem(@Param("id") Long id) throws SQLException;

	/**
	 * 根据表头id删除表体
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int delById(@Param("id") Long id) throws SQLException;

	/**
	 * 将采购实体类映射成Vo实体类
	 *
	 * @param id
	 * @return
	 */
	PurchaseOrderRootJson getVo(Long id);

	/**
	 * 根据预申报单id获取采购订单集合
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderModel> getPurchaseListById(@Param("id") Long id) throws SQLException;

	/**
	 * 分页
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<DeclareOrderDTO> getListByPage(DeclareOrderDTO dto) throws SQLException;

	DeclareOrderDTO searchDTOById(Long id);

	/**
	 * 获取预申报单各类型数量
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getDeclareTypeNum(DeclareOrderDTO dto);

	/**
	 * 根据条件获取数据
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<DeclareOrderDTO> listDTO(DeclareOrderDTO dto) ;


}
