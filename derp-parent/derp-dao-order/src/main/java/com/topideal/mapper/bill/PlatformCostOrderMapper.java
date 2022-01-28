package com.topideal.mapper.bill;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.PlatformCostOrderDTO;
import com.topideal.entity.vo.bill.PlatformCostOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PlatformCostOrderMapper extends BaseMapper<PlatformCostOrderModel> {

    void batchUpdateOrderStatus(@Param("model") PlatformCostOrderModel model, @Param("ids") List<Long> ids) throws Exception;
	// 分页查询
    PageDataModel<PlatformCostOrderDTO> listLatformCostOrderByPage(PlatformCostOrderDTO dto) throws SQLException;
    //获取详情
	PlatformCostOrderDTO getDetails(PlatformCostOrderDTO dto)throws SQLException;

}
