package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.EmailConfigDTO;
import com.topideal.entity.vo.main.EmailConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface EmailConfigMapper extends BaseMapper<EmailConfigModel> {

	PageDataModel<EmailConfigDTO> getListByPage(EmailConfigDTO dto);

	EmailConfigDTO searchDTOById(Long id);



}
