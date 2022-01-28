package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.PlatformTemporaryCostOrderDTO;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface PlatformTemporaryCostOrderMapper extends BaseMapper<PlatformTemporaryCostOrderModel> {

    //批量新增
    Integer batchSave(List<PlatformTemporaryCostOrderModel> list) throws SQLException;


    /**
     * 分页查询
     * @param dto
     * @return
     */
    PageDataModel<PlatformTemporaryCostOrderDTO> listDTOByPage(PlatformTemporaryCostOrderDTO dto);

    /**
     * 根据id查看
     * @param id
     * @return
     */
    PlatformTemporaryCostOrderDTO searchByDTOId(Long id);

    List<Map<String,Object>> listForMapItem(PlatformTemporaryCostOrderDTO dto);

    Integer getPlatFormTemporaryCount(PlatformTemporaryCostOrderDTO dto);

    /**
     * 根据外部订单号集合和商家id批量查询暂估费用单
     * @param externalCodes 外部订单号集合
     * @param merchantId 商家id
     * @return
     */
    List<PlatformTemporaryCostOrderModel> listByExternalCodes(@Param("externalCodes") List<String> externalCodes, @Param("merchantId") Long merchantId) throws SQLException;

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

    /**
     * 获取orderCode 去重
     * @param costOrderDTO
     * @return
     */
    List<PlatformTemporaryCostOrderDTO> listDistinctOrderByDTO(PlatformTemporaryCostOrderDTO costOrderDTO);

    Long statisticsDistinctByDTO(PlatformTemporaryCostOrderDTO costOrderDTO);
    /**
     * 根据orderCode单号删除 
     * @param orderCodeList
     * @return
     * @throws SQLException
     */
    int deleteByOrderCode(List<String>list)throws SQLException;
}
