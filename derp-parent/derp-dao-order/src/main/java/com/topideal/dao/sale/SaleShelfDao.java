package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.entity.vo.sale.SaleShelfModel;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 销售上架信息表 dao
 * @author lian_
 *
 */
public interface SaleShelfDao extends BaseDao<SaleShelfModel>{

		/**
		 * 查询出每条销售订单的最大上架时间
		 * @param orderId
		 * @return
		 */
		Timestamp getMaxShelfDate(Long orderId, List<String> poNos);


		/**
		 * 根据PO单号+商品货号+订单类型查询是否有上架记录
		 * @param saleShelfModel
		 * @return
		 */
		SaleShelfModel getShelfRecord(SaleShelfModel saleShelfModel);



		/**
		 * 有关新版唯品-按商家id、仓库id、货号统计上架信息
		 * @param map
		 * @return
		 */
		Map<String, Object> getShelfInfo(Map<String, Object> map);
		/**
		 * 订单号+相同条形码在该销售订单上架表内总的待上架总数量
		 * @param saleShelfModel
		 * @return
		 */
		Integer getStayShelNum(SaleShelfModel saleShelfModel);

		/**2.0唯品爬虫:查询本商家、仓库、po号、指定货号中最新上架的货号
		 * */
		SaleShelfModel getNewShelfGoods(Map<String, Object> map);

		/**
		 * 根据销售订单id统计各po单号各商品已上架的总量
		 */
		List<Map<String, Object>> listByPoNoAndOrderId(Long orderId);

		/**
		 * 根据销售订单id和po号集合查询销售订单对应的上架信息
		 * @param orderId
		 * @return
		 */
		List<SaleShelfModel> listShelfByPoNos(Long orderId, List<String> poNos);

	/**
	 * 根据id获取SaleShelfModel对象
	 * @param id
	 * @return
	 */
	List<SaleShelfDTO> getSaleShelfModelById(Long id);

	/**
	 * 根据销售订单编号和条码查询待上架总数量
	 * @param shelfDTO
	 * @return
	 */
	List<SaleShelfModel> getShelfDTOByCodeAndBarcode(ShelfDTO shelfDTO);

	/**
	 * 根据商品id查询销售上架对象集合
	 * @param goodsId
	 * @return
	 */
	List<SaleShelfModel> getSaleShelfModelByGoodsId(Long goodsId);

	/**
	 * 根据销售订单编号和条码获取已上架总数量
	 * @param saleShelfDTO
	 * @return
	 */
    SaleShelfDTO getTotalShelf(SaleShelfDTO saleShelfDTO);

	/**
	 * 通过销售订单id查询上架明细表
	 * @param saleShelfModel
	 * @return
	 */
	List<SaleShelfDTO> searchDetailByOrderId(SaleShelfModel saleShelfModel);

	  /**
          *  根据上架日期查询 列表
     * @param model
     * @return
     * @throws SQLException
     */
    List<SaleShelfModel> listOrderByDate(SaleShelfModel saleShelfModel)throws SQLException;

	/**
	 *  根据上架单号、po号、商品货号查询上架单明细
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<SaleShelfModel> getShelfInfoByCode(Map<String, Object> map);
	/**
	 * 根据 po 和orderId 统计各po的商品明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getItemByPoNoAndOrderId(Map<String, Object>  queryMap);
	/**
	 * 根据po 货号 条码 获取上架商品数据、单价、币种、客户信息
	 * @param queryMap
	 * @return
	 */
	Map<String, Object> getSaleShelfByPoNoAndGoodsNoAndBarcode(Map<String, Object>  queryMap);
	/**
	 * 获取所有数据
	 * @param shelfDTO
	 * @return
	 */
	List<SaleShelfDTO> listDTO(SaleShelfDTO dto);
	/**
	 * 根据上架单号删除数据
	 * @param shelfIdList
	 * @return
	 * @throws SQLException
	 */
	int delItemsByShelfIds(List<Long> shelfIdList) throws SQLException;
	/**
	 * 获取分页数据
	 * @param dto
	 * @return
	 */
	SaleShelfDTO listDTOByPage(SaleShelfDTO dto);

}
