package com.topideal.mapper.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.InventoryRealTimeSnapshotModel;
import com.topideal.mapper.BaseMapper;

/**
 * 实时库存快照表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface InventoryRealTimeSnapshotMapper extends BaseMapper<InventoryRealTimeSnapshotModel> {

    /**
     * 检查商家指定日期是否存在快照
     * */
    public Integer countSnapshotByDate(Map<String, Object> map);

    
    /**
     * 删除三个月前且不包含1号的数据 
     */
    public void delData();


	
    /**
     *  存货跌价获取实时库存快照
     * @param queryMap
     * @return
     */
    public List<Map<String, Object>> getInventoryFallingPriceReserves(Map<String, Object> queryMap);
    
    /**
     * 月结自校验根据条码统计
     * @param inventoryParams
     * @return
     */
	public List<Map<String, Object>> getMonthlyAutoVeriListGroupByBarcode(Map<String, Object> inventoryParams);
}

