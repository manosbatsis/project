package com.topideal.mapper.main;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.CustomerMainDTO;
import com.topideal.entity.vo.main.CustomerMainModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CustomerMainMapper extends BaseMapper<CustomerMainModel> {

	PageDataModel<CustomerMainDTO> getListByPage(CustomerMainDTO dto);

	CustomerMainDTO searchDTOById(@Param("id")Long id);



}
