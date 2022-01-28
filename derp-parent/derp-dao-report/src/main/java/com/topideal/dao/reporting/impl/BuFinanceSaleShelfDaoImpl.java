package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceSaleShelfDao;
import com.topideal.entity.vo.reporting.BuFinanceSaleShelfModel;
import com.topideal.mapper.reporting.BuFinanceSaleShelfMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceSaleShelfDaoImpl implements BuFinanceSaleShelfDao {

    @Autowired
    private BuFinanceSaleShelfMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceSaleShelfModel> list(BuFinanceSaleShelfModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceSaleShelfModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(BuFinanceSaleShelfModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceSaleShelfModel  searchByPage(BuFinanceSaleShelfModel  model) throws SQLException{
        PageDataModel<BuFinanceSaleShelfModel> pageModel=mapper.listByPage(model);
        return (BuFinanceSaleShelfModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceSaleShelfModel  searchById(Long id)throws SQLException {
        BuFinanceSaleShelfModel  model=new BuFinanceSaleShelfModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceSaleShelfModel searchByModel(BuFinanceSaleShelfModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)销售已上架明细表
	 */
	@Override
	public int delBuFinanceSaleShelf(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceSaleShelf(map);
	}
	
	/**
	 *  批量新增(事业部财务经销存)销售已上架明细表
	 */
	@Override
	public int batchBuInsertFinanceSaleShelf(List<BuFinanceSaleShelfModel> list) {
		return mapper.batchBuInsertFinanceSaleShelf(list);
	}
	
	/**
	 * 导出(事业部财务经分销)销售上架 分页 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> exportBuFinanceSaleShelfMapList(Map<String, Object> map) throws SQLException {
		List<Map<String, Object>> exportBuFinanceSaleShelfMapList = mapper.exportBuFinanceSaleShelfMapList(map);
		for (Map<String, Object> exportMap : exportBuFinanceSaleShelfMapList) {
			String orderType = (String) exportMap.get("order_type");
			String tallyingUnit = (String) exportMap.get("tallying_unit");
			String outDepotCurrency = (String) exportMap.get("out_depot_currency");
			String saleCurrency = (String) exportMap.get("sale_currency");
			exportMap.put("order_type", DERP_REPORT.getLabelByKey(DERP_REPORT.financeSaleShelf_orderTypeList, orderType));
			exportMap.put("tallying_unit", DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit));
			exportMap.put("out_depot_currency", DERP.getLabelByKey(DERP.currencyCodeList, outDepotCurrency));
			exportMap.put("sale_currency", DERP.getLabelByKey(DERP.currencyCodeList, saleCurrency));

		}
		return exportBuFinanceSaleShelfMapList;
	}
	
	/**
	 * 导出(事业部财务经分销)销售上架 (退款数据)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> exportBuFinanceSaleShelfTKMapList(Map<String, Object> map) throws SQLException {
		List<Map<String, Object>> exportBuFinanceSaleShelfMapList = mapper.exportBuFinanceSaleShelfTKMapList(map);
		for (Map<String, Object> exportMap : exportBuFinanceSaleShelfMapList) {
			String orderType = (String) exportMap.get("order_type");
			String tallyingUnit = (String) exportMap.get("tallying_unit");
			String outDepotCurrency = (String) exportMap.get("out_depot_currency");
			String saleCurrency = (String) exportMap.get("sale_currency");
			exportMap.put("order_type", DERP_REPORT.getLabelByKey(DERP_REPORT.financeSaleShelf_orderTypeList, orderType));
			exportMap.put("tallying_unit", DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit));
			exportMap.put("out_depot_currency", DERP.getLabelByKey(DERP.currencyCodeList, outDepotCurrency));
			exportMap.put("sale_currency", DERP.getLabelByKey(DERP.currencyCodeList, saleCurrency));

		}
		return exportBuFinanceSaleShelfMapList;
	}
	/**
	 * 导出(事业部财务经分销)销售未上架  (用于分页查询)统计总量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int getExportBuFinanceSaleShelfListCount(Map<String, Object> map) throws SQLException {
		return mapper.getExportBuFinanceSaleShelfListCount(map);
	}

	/**
	 * 销售达成率按维度汇总统计销售已上架明细
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<BuFinanceSaleShelfModel> getSaleDataCountList(Map<String, Object> queryMap) {
		return mapper.getSaleDataCountList(queryMap) ;
	}
	@Override
	public List<Map<String, Object>> exportSaleChannelSummary(Map<String, Object> map) throws SQLException {
		List<Map<String, Object>> exportSaleChannelSummary = mapper.exportSaleChannelSummary(map);
		for (Map<String, Object> exportMap : exportSaleChannelSummary) {
			String orderType = (String) exportMap.get("order_type");
			if (!StringUtils.isEmpty(orderType)) {
				orderType=DERP_REPORT.getLabelByKey(DERP_REPORT.financeSaleShelf_orderTypeList, orderType);
				exportMap.put("order_type", orderType);
			}
			
			
		}
		return exportSaleChannelSummary;
	}
	/**
	 * 获取总账导出 销售上架 渠道类型为To B且运营类型为空 
	 */
	@Override
	public List<Map<String, Object>> getAllAccountShelfToB(Map<String, Object> map) throws SQLException {
		return mapper.getAllAccountShelfToB(map);
	}
	/**
	 * 获取总账导出 销售上架 渠道类型为To B且运营类型为一件代发
	 */
	@Override
	public List<Map<String, Object>> getAllAccountShelfToB002(Map<String, Object> map) throws SQLException {
		return mapper.getAllAccountShelfToB002(map);
	}
	/**
	 * 获取总账导出 销售上架 渠道类型为To C
	 */
	@Override
	public List<Map<String, Object>> getAllAccountShelfToC(Map<String, Object> map) throws SQLException {
		return mapper.getAllAccountShelfToC(map);
	}
	/**
	 * 事业部财务经销存暂估应收PDF导出To B
	 */
	@Override
	public List<Map<String, Object>> getTempEstimatePdfToB(Map<String, Object> map) throws SQLException {
		return mapper.getTempEstimatePdfToB(map);
	}
	/**
	 * 事业部财务经销存暂估应收PDF导出To C
	 */
	@Override
	public List<Map<String, Object>> getTempEstimatePdfToC(Map<String, Object> map) throws SQLException {
		return mapper.getTempEstimatePdfToC(map);
	}
	
	/**
	 * 财务进销存入账汇总表 (库存)
	 */
	@Override
	public List<Map<String, Object>> getInventorySummaryExpotr(Map<String, Object> paramMap) {
		return mapper.getInventorySummaryExpotr(paramMap);
	}
	/**
	 * 获取财务出库成本差异
	 */
	@Override
	public List<Map<String, Object>> getOutCostDifferenceExport(Map<String, Object> map) throws SQLException {
		return mapper.getOutCostDifferenceExport(map);
	}

}