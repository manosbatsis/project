package com.topideal.mapper;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.MonthlyAccountItemDTO;
import com.topideal.entity.vo.MonthlyAccountItemModel;

/**
 * 月结账单商品明细 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface MonthlyAccountItemMapper extends BaseMapper<MonthlyAccountItemModel> {

    /**
     *    根据库存扣减接口的商家仓库商品信息查询对应的月结详情信息
     * @param monthlyAccountItemModel
     * @return
     * @throws Exception
     */
	MonthlyAccountItemModel getMonthlyAccountItemListByMonth(MonthlyAccountItemModel monthlyAccountItemModel )  throws SQLException;

	/**
	 * 按时间排序
	 * @param monthlyAccountItemModel
	 * @return
	 * @throws SQLException
	 */
	List<MonthlyAccountItemModel>listOrbyOverdueDate(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException;

	
	/**
	 * 扣减商品库存量
	 * @param monthlyAccountItemModel
	 * @return
	 * @throws SQLException
	 */
	int updateLowerMonthlyGoodsNum(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException;
	
	
	/**
	 * 增加商品库存量
	 * @param monthlyAccountItemModel
	 * @return
	 * @throws SQLException
	 */
	int updateAddMonthlyGoodsNum(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException;
	
	/**
	 * 更新实时库存量和商品条形码
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int  updateAvailableNumOrBarcode(Map<String,Object> map) throws SQLException;
	
	
    
    /**
     * 更具当前时间查询月结详情
     * @param monthlyAccountItemModel
     * @return
     * @throws SQLException
     */
    List<MonthlyAccountItemModel> getMonthlyAccountItemByCreateDate (Map<String,Object> map) throws SQLException;
    
    /**
     * 查询唯一一条
     * @param monthlyAccountItemModel
     * @return
     * @throws SQLException
     */
    MonthlyAccountItemModel  selectMonthlyAccountItemModelMapByOne(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException;
    /**
     * 更新月结商品明细的库存余量
     * @param monthlyAccountItemModel
     * @return
     * @throws SQLException
     */
   int  updateAddMonthlySurplusNum(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException;
   
   /**
    * 按批次分组统计月结详情数据
    * @param monthlyAccountItemModel
    * @return
    * @throws SQLException
    */
   List<MonthlyAccountItemDTO> getMonthlyAccountItemModelMapList (MonthlyAccountItemDTO monthlyAccountItemModel) throws SQLException;
   
   
   
   /**
    * 更新期末库存数量
    * @param monthlyAccountItemModel
    * @return
    * @throws SQLException
    */
   int  updateMonthlyAccountItemModelSettlementNum(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException;

   MonthlyAccountItemModel getOne(MonthlyAccountItemModel monthlyAccountItemModel);
   /**
    * 获取批次有差异的记录
    * @param model
    * @return
    */
   List<Map<String, Object>> getDifferenceList(MonthlyAccountItemModel model);
   /**
    * 根据条件获取需要调增的记录
    * @param merchantId
    * @param depotId
    * @param goodsId
    * @param batchNo
    * @param bv 
    * @return
    */
   List<MonthlyAccountItemModel> getAddItemByParam(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId,
		   @Param("goodsId")Long goodsId, @Param("batchNo")String batchNo, @Param("unit")String unit,@Param("type")String type, @Param("bv")String bv);
   /**
    * 根据条件获取需要调减的记录
    * @param merchantId
    * @param depotId
    * @param goodsId
    * @param batchNo
    * @param bv 
    * @return
    */
   List<MonthlyAccountItemModel> getSubItemByParam(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId,
		   @Param("goodsId")Long goodsId, @Param("batchNo")String batchNo, @Param("unit")String unit,@Param("type")String type, @Param("bv")String bv);
   /**
    * 根据表头ID获取表体数据（结余数量大于0）
    * @param monthlyAccountId
    * @return
    */
   List<MonthlyAccountItemModel> getItemListById(@Param("monthlyAccountId")Long monthlyAccountId);
   /**
    * 按商品分组统计月结详情数据
    * @param monthlyAccountItemModel
    * @return
    * @throws SQLException
    */
   List<MonthlyAccountItemDTO> getItemListGroupGoods(MonthlyAccountItemDTO monthlyAccountItemModel) throws SQLException;
   /**
    * 按商品统计有差异的记录
    * @param model
    * @return
    */
   List<Map<String, Object>> getDifferenceGroupGoods(MonthlyAccountItemModel model);

   /**
    * 根据条件获取月结表体数据
    * @param merchantId 商家id
    * @param depotId 仓库id
    * @param goodsId 商品id
    * @param batchNo 批次
    * @param productionDate 生产日期
    * @param overdueDate 失效日期
    * @param unit 理货单位
    * @param stockType 库存类型（好、坏品）
    * @param monthlyAccountId 月结表头id
    * @return
    */
   MonthlyAccountItemModel getItemByInventoryRealTimeSnapshot(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId, @Param("goodsId")Long goodsId, @Param("batchNo")String batchNo,
		   @Param("productionDate")Date productionDate, @Param("overdueDate")Date overdueDate, @Param("unit")String unit, @Param("stockType")String stockType, @Param("monthlyAccountId")Long monthlyAccountId);
   /**
    * 根据月结id删除明细
    * @param monthlyAccountId
    */
   int delItemByMonthlyAccountId(@Param("monthlyAccountId")Long monthlyAccountId);
   /**
    * 导出月结明细-强校验
    * @param model
    * @return
    */
   List<Map<String, Object>> exportMonthlyAccountItemModelMapList(MonthlyAccountItemModel monthlyAccountItemModel);
   /**
    * 导出月结明细-弱校验
    * @param model
    * @return
    */
   List<Map<String, Object>> exportItemListGroupGoods(MonthlyAccountItemModel monthlyAccountItemModel);
   /**
	 * 校验月结现存量和 库存余量是否相等
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<MonthlyAccountItemModel>  checkMonthlySurplusNum(MonthlyAccountItemModel model) throws SQLException;
	   /**
	    * 获取月结数据大于0的数据
	    * @param model
	    * @return
	    */
	   List<MonthlyAccountItemModel> getMonthlyAccountItem(MonthlyAccountItemModel model);
   
}

