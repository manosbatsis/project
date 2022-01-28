package com.topideal.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_LOG;
import com.topideal.dao.LogStreamApiDao;
import com.topideal.dao.LogStreamHistoryApiDao;
import com.topideal.dao.LogStreamHistoryInventoryDao;
import com.topideal.dao.LogStreamHistoryOrderDao;
import com.topideal.dao.LogStreamInventoryDao;
import com.topideal.dao.LogStreamOrderDao;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamApiModel;
import com.topideal.entity.vo.LogStreamInventoryModel;
import com.topideal.entity.vo.LogStreamMqModel;
import com.topideal.entity.vo.LogStreamOrderModel;
import com.topideal.service.StreamLogService;
import com.topideal.tools.ParseDaterangepicker;

/**
 * mq日志流水
 * @author zhanghx
 */
@Service
public class StreamLogServiceImpl implements StreamLogService {

	@Autowired
	private LogStreamApiDao logStreamApiDao;
	
	@Autowired
	private LogStreamHistoryApiDao logStreamHistoryApiDao;
	
	
	@Autowired
	private LogStreamOrderDao logStreamOrderDao;
	
	@Autowired
	private LogStreamHistoryOrderDao logStreamHistoryOrderDao;
	
	
	@Autowired
	private LogStreamInventoryDao logStreamInventoryDao;
	
	@Autowired
	private LogStreamHistoryInventoryDao logStreamHistoryInventoryDao;
	
	/**
	 * 分页
	 * @param model
	 * @return
	 */
	@Override
	public LogStreamMqDTO listByPage(LogStreamMqDTO dto) throws SQLException {
		if (StringUtils.isNotBlank(dto.getEndDateStr())) {
			List<String> list = ParseDaterangepicker.parseToString(dto.getEndDateStr());
			if(list!=null && list.size() == 2){
				dto.setConsumeStartDate(list.get(0));//开始
				dto.setConsumeEndDate(list.get(1));//结束
			}
		}
		
		LogStreamMqDTO returnDto = new LogStreamMqDTO();
		if(dto.getSelectScope().equals(DERP_LOG.LOG_SELECT_SCOPE_2)){
			if(DERP_LOG.LOG_MODELCODE_1.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(dto.getModelCode())){
				returnDto = logStreamHistoryOrderDao.getListByPage(dto);
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(dto.getModelCode())){
				returnDto = logStreamHistoryInventoryDao.getListByPage(dto);
			}else if("5".equals(dto.getModelCode())){
				returnDto = logStreamHistoryApiDao.getListByPage(dto);
			}
		}else{
			if(DERP_LOG.LOG_MODELCODE_1.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(dto.getModelCode())){
				returnDto = logStreamOrderDao.getListByPage(dto);
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(dto.getModelCode())){
				returnDto = logStreamInventoryDao.getListByPage(dto);
			}else if("5".equals(dto.getModelCode())){
				returnDto = logStreamApiDao.getListByPage(dto);
			}
		}
		return returnDto;
	}

	/**
	 * 批量关闭
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	@Override
	public boolean closeBatch(List<Long> list,String modelCode) throws SQLException{
		for (Long id : list) {
			if(DERP_LOG.LOG_MODELCODE_1.equals(modelCode) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(modelCode) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(modelCode)){
				LogStreamOrderModel model = logStreamOrderDao.searchById(id);
				if(model.getStatus().equals(DERP_LOG.LOG_STATUS_0)){
					LogStreamOrderModel logModel = new LogStreamOrderModel();
					logModel.setId(model.getId());
					logModel.setIsClose(DERP_LOG.LOG_STREAM_STATUS_1);
					logStreamOrderDao.modify(logModel);
				}
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(modelCode)){
				LogStreamInventoryModel model = logStreamInventoryDao.searchById(id);
				if(model.getStatus().equals(DERP_LOG.LOG_STATUS_0)){
					LogStreamInventoryModel logModel = new LogStreamInventoryModel();
					logModel.setId(model.getId());
					logModel.setIsClose(DERP_LOG.LOG_STREAM_STATUS_1);
					logStreamInventoryDao.modify(logModel);
				}
			}else if("5".equals(modelCode)){
				LogStreamApiModel model = logStreamApiDao.searchById(id);
				if(model.getStatus().equals(DERP_LOG.LOG_STATUS_0)){
					LogStreamApiModel logModel = new LogStreamApiModel();
					logModel.setId(model.getId());
					logModel.setIsClose(DERP_LOG.LOG_STREAM_STATUS_1);
					logStreamApiDao.modify(logModel);
				}
			}
		}
		return true;
	}

}
