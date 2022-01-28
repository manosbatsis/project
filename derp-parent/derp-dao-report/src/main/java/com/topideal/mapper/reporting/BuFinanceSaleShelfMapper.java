package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceSaleShelfModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceSaleShelfMapper extends BaseMapper<BuFinanceSaleShelfModel> {

	/**
	 *  清除事业部商家 仓库 月份 (财务经销存)销售已上架明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceSaleShelf(Map<String, Object> map) throws SQLException;
	/**
	 * 批量新增(事业部财务经销存)销售已上架明细表
	 * @param list
	 * @return
	 */
	public int batchBuInsertFinanceSaleShelf(List<BuFinanceSaleShelfModel> list);
	/**
	 * 导出(事业部财务经分销)销售上架 分页 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> exportBuFinanceSaleShelfMapList(Map<String, Object> map)throws SQLException;
	
	/**
	 * 导出(事业部财务经分销)销售上架 (退款数据) 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> exportBuFinanceSaleShelfTKMapList(Map<String, Object> map)throws SQLException;
	
	
	
	/**
	 * 导出(事业部财务经分销)销售未上架  (用于分页查询)统计总量
	 * @param map
	 * @return
	 */
	int getExportBuFinanceSaleShelfListCount(Map<String, Object> map)throws SQLException;

	/**
	 * 销售达成率按维度汇总统计销售已上架明细
	 * @param queryMap
	 * @return
	 */
	List<BuFinanceSaleShelfModel> getSaleDataCountList(Map<String, Object> queryMap);
	
	/**
	 * 导出销售渠道汇总
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> exportSaleChannelSummary(Map<String, Object> map)throws SQLException;
	/**
	 * 获取总账导出 销售上架 渠道类型为To B且运营类型为空 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getAllAccountShelfToB(Map<String, Object> map)throws SQLException;
	/**
	 * 获取总账导出 销售上架 渠道类型为To B且运营类型为一件代发
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getAllAccountShelfToB002(Map<String, Object> map)throws SQLException;
	/**
	 * 获取总账导出 销售上架 渠道类型为To C
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getAllAccountShelfToC(Map<String, Object> map)throws SQLException;
	
	/**
	 * 事业部财务经销存暂估应收PDF导出To B
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getTempEstimatePdfToB(Map<String, Object> map)throws SQLException;
	/**
	 * 事业部财务经销存暂估应收PDF导出To C
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getTempEstimatePdfToC(Map<String, Object> map)throws SQLException;
	
    /**
	 * 财务进销存入账汇总表 (库存)
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getInventorySummaryExpotr(Map<String, Object> paramMap);
	/**
	 * 获取财务出库成本差异
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getOutCostDifferenceExport(Map<String, Object> map)throws SQLException;
	
}
