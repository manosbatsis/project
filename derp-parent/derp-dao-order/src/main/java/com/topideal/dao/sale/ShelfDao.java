package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.LongNode;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.entity.vo.sale.ShelfModel;
import org.apache.ibatis.annotations.Param;


public interface ShelfDao extends BaseDao<ShelfModel> {

    /**
     * 根据上架单号查询上架表体信息
     */
    List<Map<String, Object>> getShelfItemInfo(String code) throws SQLException;

    ShelfDTO searchShelfModelById(Long id);

    Integer getOrderCount(ShelfDTO shelfDTO);

    List<Map<String,Object>> getExportList(ShelfDTO shelfDTO);

    ShelfDTO searchByPage1(ShelfDTO shelfDTO);

    /**
     * 根据上架单号统计上架表体信息
     */
    List<Map<String, Object>> getShelfItemByCodes(List<String> codeList) throws SQLException;
    
    List<ShelfDTO> listDTO(ShelfDTO dto) throws SQLException;

    /**
     * 查询销售订单为购销-整批结算、购销-实销实结的上架单
     */
    List<ShelfDTO> listToBTempDTO(ShelfDTO dto);

    /**
     * 批量更新上架单勾稽状态
     */
    Integer batchUpdateStatus(String isGenerate, List<String> shelfCodes);

    /**
     * 根据指定公司、月份、客户查询
     */
    List<ShelfModel> getReceiveShelfList(Long merchantId, List<Long> customerIds, String month);
}
