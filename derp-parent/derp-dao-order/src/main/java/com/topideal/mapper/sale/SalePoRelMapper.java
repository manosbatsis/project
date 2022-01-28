package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.SalePoRelModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface SalePoRelMapper extends BaseMapper<SalePoRelModel> {

    int delbyPoNoAndOrderId(SalePoRelModel model);

    int getCountByOrderId(@Param("orderId") Long orderId);

    List<SalePoRelModel> getAllByOrderId (@Param("orderId") Long orderId,@Param("merchantId")Long merchantId);

    List<SalePoRelModel> getAllByNoDelete(Map<String,Object> paramMap);
}
