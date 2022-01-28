package com.topideal.service.common;


import com.topideal.webapi.common.dto.TemplateExplainDTO;

import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/10/29 11:58
 * @Description: 模板说明Service
 */
public interface TemplateExplainService {

    /**
     * list by DTO
     * @param dto
     * @return
     */
    List<TemplateExplainDTO> listByDTO(TemplateExplainDTO dto) throws Exception;
}
