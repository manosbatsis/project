package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.entity.vo.sale.ShelfModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface ShelfMapper extends BaseMapper<ShelfModel> {


    ShelfDTO searchShelfModelById(Long id);

    Integer getOrderCount(ShelfDTO shelfDTO);
    /**
     * 根据上架单号查询上架表体信息
     */
    List<Map<String, Object>> getShelfItemInfo(@Param("code") String code) throws SQLException;

    /**
     * 根据上架单号统计上架表体信息
     */
    List<Map<String, Object>> getShelfItemByCodes(@Param("codeList") List<String> codeList) throws SQLException;

    List<Map<String,Object>> getExportList(ShelfDTO shelfDTO);

    PageDataModel<ShelfDTO> listByPage1(ShelfDTO shelfDTO);
    
    List<ShelfDTO> listDTO(ShelfDTO dto);

    /**
     * 查询销售订单为购销-整批结算、购销-实销实结的上架单
     */
    List<ShelfDTO> listToBTempDTO(ShelfDTO dto);

    /**
     * 批量更新上架单勾稽状态
     */
    Integer batchUpdateStatus(@Param("isGenerate") String isGenerate, @Param("shelfCodes") List<String> shelfCodes);

    /**
     * 根据指定公司、月份、客户查询
     */
    List<ShelfModel> getReceiveShelfList(@Param("merchantId") Long merchantId, @Param("customerIds") List<Long> customerIds,
                                         @Param("month") String month);
}
