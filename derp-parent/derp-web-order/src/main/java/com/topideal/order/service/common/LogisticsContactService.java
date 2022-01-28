package com.topideal.order.service.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.common.LogisticsContactTemplateDTO;
import com.topideal.entity.dto.common.LogisticsContactTemplateLinkDTO;
import com.topideal.entity.vo.common.LogisticsContactTemplateLinkModel;
import com.topideal.entity.vo.common.LogisticsContactTemplateModel;

public interface LogisticsContactService {

	LogisticsContactTemplateDTO saveOrUpdateLogisTemplate(LogisticsContactTemplateModel saveModel) throws SQLException;

	LogisticsContactTemplateDTO getDetailsTemplate(Long id) throws SQLException;

	void delTemplate(Long id) throws SQLException;

	List<LogisticsContactTemplateDTO> listTemplate(String type) throws SQLException;

	LogisticsContactTemplateLinkDTO getDetailsTemplateLink(Long id) throws SQLException;

	LogisticsContactTemplateDTO saveOrUpdateLogisTemplateLink(LogisticsContactTemplateLinkModel saveModel) throws SQLException;

	void delTemplateLink(Long id) throws SQLException;

	List<LogisticsContactTemplateLinkDTO> listTemplateLink() throws SQLException;

}
