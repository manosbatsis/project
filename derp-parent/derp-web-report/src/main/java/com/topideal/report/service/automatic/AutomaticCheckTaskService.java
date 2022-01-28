package com.topideal.report.service.automatic;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.AutomaticCheckTaskDTO;
import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.mongo.entity.MerchantShopRelMongo;

/**
 * POP核对任务   service
 * @author wy
 */
public interface AutomaticCheckTaskService {
	
	/**
	 * 列表（分页）
	 * @param dto 
	 * @return
	 */
	AutomaticCheckTaskDTO listAutomaticCheckTaskByPage(AutomaticCheckTaskDTO dto)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	AutomaticCheckTaskDTO searchDetail(Long id) throws SQLException;
	/**
	 * 根据商家/平台名称查询对应平台关联的店铺名称
	 * @param merchantId
	 * @param storePlatformCode
	 * @return
	 * @throws Exception
	 */
	List<MerchantShopRelMongo> getMerchantShopRelList(Long merchantId, String storePlatformCode, Integer outDepotId) throws Exception;
	
	/**
	 * 标识核对结果
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	String modifyCheckResult(String ids, String checkResult, String remark, Long userId, String userName) throws Exception;
	
	/**
	 * 根据数据源获取对应的仓库
	 * @param dataSource
	 * @param depotCode 
	 * @param user 
	 * @return
	 */
	List<DepotInfoModel> getDepotByDataSource(String dataSource, String depotCode, User user);
	
	String saveCheckResult(AutomaticCheckTaskDTO dto, MultipartFile file) throws SQLException, Exception;
	/**
	 * 根据model查询列表
	 * @param paramModel
	 * @return
	 * @throws SQLException 
	 */
	List<AutomaticCheckTaskModel> getListByModel(AutomaticCheckTaskModel paramModel) throws SQLException;
	
	/**
	 * 
	 * @param task
	 * @return
	 * @throws SQLException 
	 */
	Integer modify(AutomaticCheckTaskModel model) throws SQLException;
	/**
	 * 删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	boolean delAutomaticCheckTask(List ids)throws Exception;
}
