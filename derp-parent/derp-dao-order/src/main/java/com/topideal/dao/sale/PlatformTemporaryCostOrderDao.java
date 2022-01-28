package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.PlatformTemporaryCostOrderDTO;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface PlatformTemporaryCostOrderDao extends BaseDao<PlatformTemporaryCostOrderModel> {

    /**
     * 批量新增
     * @param list
     * @return
     */
    Integer batchSave(List<PlatformTemporaryCostOrderModel> list) throws SQLException;


    /**
     * 分页查询
     * @param dto
     * @return
     */
    PlatformTemporaryCostOrderDTO listDTOByPage(PlatformTemporaryCostOrderDTO dto);

    /**
     * 根据id查看
     * @param id
     * @return
     */
    PlatformTemporaryCostOrderDTO  searchByDTOId(Long id);

    /**
     * 导出分页查询
     * @param dto
     * @return
     */
    List<Map<String,Object>> listForMapItem(PlatformTemporaryCostOrderDTO dto);

    /**
     * 获取记录数
     * @param dto
     * @return
     */
    Integer getPlatFormTemporaryCount(PlatformTemporaryCostOrderDTO dto);


    /**
     * 根据外部订单号集合和商家id批量查询暂估费用单
     * @param externalCodes 外部订单号集合
     * @param merchantId 商家id
     * @return
     */
    List<PlatformTemporaryCostOrderModel> listByExternalCodes(List<String> externalCodes, Long merchantId) throws SQLException;

    /**
     * 以“月份+事业部+客户+店铺id+运营类型+平台”的维度统计数量
     * @param dto
     * @return
     */
    List<Map<String, Object>> countOrderByDTO(PlatformTemporaryCostOrderDTO dto) throws SQLException;

    /**
     * 分页查询平台暂估费用单
     * @param dto
     * @return
     */
    List<PlatformTemporaryCostOrderDTO> listTempOrderDTO(PlatformTemporaryCostOrderDTO dto) throws SQLException;

    List<PlatformTemporaryCostOrderDTO> listDistinctOrderByDTO(PlatformTemporaryCostOrderDTO costOrderDTO);

    Long statisticsDistinctByDTO(PlatformTemporaryCostOrderDTO costOrderDTO);
   /**
    * 根据orderCode单号删除 
    * @param orderCodeList
    * @return
    * @throws SQLException
    */
   int deleteByOrderCode(List<String>orderCodeList)throws SQLException;
}
