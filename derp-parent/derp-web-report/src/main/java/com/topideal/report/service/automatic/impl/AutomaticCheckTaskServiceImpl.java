package com.topideal.report.service.automatic.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.automatic.AutomaticCheckTaskDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.entity.dto.AutomaticCheckTaskDTO;
import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.report.service.automatic.AutomaticCheckTaskService;


/**
 * POP核对任务
 * @author wy
 */
@Service
public class AutomaticCheckTaskServiceImpl  implements AutomaticCheckTaskService{
	@Autowired
	private AutomaticCheckTaskDao automaticCheckTaskDao;
	// 商家店铺Mongo
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao ;
	@Autowired
	private DepotInfoDao depotInfoDao ;
	
	 
	@Override
	public AutomaticCheckTaskDTO listAutomaticCheckTaskByPage(AutomaticCheckTaskDTO dto) throws SQLException {
		return automaticCheckTaskDao.queryDTOListByPage(dto);
	}

	@Override
	public AutomaticCheckTaskDTO searchDetail(Long id) throws SQLException {
		AutomaticCheckTaskDTO dto = new AutomaticCheckTaskDTO();
		dto.setId(id);
		return automaticCheckTaskDao.searchDTOById(id);
	}

	@Override
	public List<MerchantShopRelMongo> getMerchantShopRelList(Long merchantId, String storePlatformCode,Integer outDepotId)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", "1") ;
		params.put("merchantId", merchantId) ;
		if(StringUtils.isNotBlank(storePlatformCode)){
			params.put("storePlatformCode", storePlatformCode) ;
		}
		if(outDepotId!=null){
			params.put("depotId", outDepotId) ;
		}
		List<MerchantShopRelMongo> shopList = merchantShopRelMongoDao.findAll(params);	//mongo查询参数集合
		return shopList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String modifyCheckResult(String ids,String checkResult,String remark,Long userId, String userName) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		for (int i = 0; i < idList.size(); i++) {
			Long id =idList.get(i);
			AutomaticCheckTaskModel model = new AutomaticCheckTaskModel();
			model.setId(id);
			model.setIsMark("1");	//是否标记过（0-否，1-是）
			model.setRemark(remark);
			model.setCheckResult(checkResult);
			model.setModifyDate(TimeUtils.getNow());
			model.setModifier(userId);
			model.setModifierName(userName);
			int num = automaticCheckTaskDao.modify(model);
			if(num<=0){
				return "标记失败";
			}
		}
		return "标记成功";
	}

	@Override
	public List<DepotInfoModel> getDepotByDataSource(String dataSource, String depotCode, User user) {
		
		Map<String , Object> queryMap = new HashMap<String , Object>() ;
		queryMap.put("merchantId", user.getMerchantId()) ;
		queryMap.put("dataSource", dataSource) ;
		queryMap.put("depotCode", depotCode) ;
		
		return depotInfoDao.getDepotByDataSource(queryMap);
	}

	@Override
	public String saveCheckResult(AutomaticCheckTaskDTO dto, MultipartFile file) throws Exception {
		
		
		String taskCode = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_HDRW) ;
		
		//保存文件
		String basePath = ApolloUtilDB.reportBasepath;//excel文件存放目录
		basePath += "/SJXY/" + taskCode ;
		
		//创建目录
	   	File outFil = new File(basePath);
	   	if(!outFil.exists()) {
	   		outFil.mkdirs() ;
	   	}
	   	
	   	
	   	String fileName = basePath + "/" + file.getOriginalFilename() ;
	   	
	   	//获取文件输入流
	   	byte buffer[] = new byte[1024];
	   	InputStream ins = file.getInputStream();
	   	int len = 0 ;
	   	
	   	//获取输出流
	   	FileOutputStream fos = new FileOutputStream(fileName) ;
	   	
	   	while((len = ins.read(buffer)) > 0) {
	   		fos.write(buffer, 0, len);
	   	}
	   	
	   	fos.close();
	   	ins.close();
		
		if(dto.getOutDepotId() != null) {
			DepotInfoModel depot = depotInfoDao.searchById(dto.getOutDepotId());
			dto.setOutDepotCode(depot.getDepotCode());
			dto.setOutDepotName(depot.getName());
		}
		
		AutomaticCheckTaskModel automaticCheckTaskModel = new AutomaticCheckTaskModel() ;
		
		if(dto.getCheckStartDate() != null) {
			automaticCheckTaskModel.setCheckStartDate(Timestamp.valueOf(dto.getCheckStartDate()));
		}
		
		if(dto.getCheckEndDate() != null) {
			automaticCheckTaskModel.setCheckEndDate(Timestamp.valueOf(dto.getCheckEndDate()));
		}
		
		automaticCheckTaskModel.setCheckResult(dto.getCheckResult());
		automaticCheckTaskModel.setCreateName(dto.getCreateName());
		automaticCheckTaskModel.setCreater(dto.getCreater());
		automaticCheckTaskModel.setDataSource(dto.getDataSource());
		automaticCheckTaskModel.setIsMark(dto.getIsMark());
		automaticCheckTaskModel.setMerchantId(dto.getMerchantId());
		automaticCheckTaskModel.setMerchantName(dto.getMerchantName());
		automaticCheckTaskModel.setOutDepotId(dto.getOutDepotId());
		automaticCheckTaskModel.setOutDepotCode(dto.getOutDepotCode());
		automaticCheckTaskModel.setOutDepotName(dto.getOutDepotName());
		automaticCheckTaskModel.setRemark(dto.getRemark());
		automaticCheckTaskModel.setShopCode(dto.getShopCode());
		automaticCheckTaskModel.setStorePlatformCode(dto.getStorePlatformCode());
		automaticCheckTaskModel.setState(DERP_REPORT.AUTOMATICCHECKTASK_STATE_1);
		automaticCheckTaskModel.setTaskCode(taskCode);
		automaticCheckTaskModel.setTaskType(dto.getTaskType());
		automaticCheckTaskModel.setSourcePath(fileName);
		automaticCheckTaskModel.setCreateDate(TimeUtils.getNow());
		automaticCheckTaskModel.setCheckResult(DERP_REPORT.AUTOMATICCHECKTASK_CHECKRESULT_2);// 未标记过
		automaticCheckTaskModel.setIsMark("0");	// 是否标记过（0-否，1-是)
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", "1") ;
		params.put("merchantId", dto.getMerchantId()) ;
		params.put("shopCode", dto.getShopCode()) ;
		MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(params);
		if(shop!=null){
			automaticCheckTaskModel.setShopName(shop.getShopName());
		}
		
		automaticCheckTaskDao.save(automaticCheckTaskModel) ;
		return automaticCheckTaskModel.getTaskCode();
	}

	@Override
	public List<AutomaticCheckTaskModel> getListByModel(AutomaticCheckTaskModel paramModel) throws SQLException {
		return automaticCheckTaskDao.list(paramModel);
	}

	@Override
	public Integer modify(AutomaticCheckTaskModel model) throws SQLException {
		return automaticCheckTaskDao.modify(model);
	}
	/**
	 * 删除
	 * @param ids
	 * @return
	 * @throws RuntimeException 
	 */
	@Override
	public boolean delAutomaticCheckTask(List ids) throws Exception {
		List<AutomaticCheckTaskModel> automaticCheckTaskList = new ArrayList<AutomaticCheckTaskModel>();
		for(Object id:ids){
			//获取自动检验任务信息
			AutomaticCheckTaskModel automaticCheckTaskModel = automaticCheckTaskDao.searchById(Long.parseLong(id.toString()));
			if(automaticCheckTaskModel==null){
				throw new RuntimeException("删除失败，没找到对应的任务");
			}
			automaticCheckTaskList.add(automaticCheckTaskModel);
		}
		int num = automaticCheckTaskDao.delete(ids);
		boolean deleteFlag=false;
		for (AutomaticCheckTaskModel automaticCheckTaskModel:automaticCheckTaskList) {
			String storePath =automaticCheckTaskModel.getStorePath();
			if(storePath==null){
				storePath =automaticCheckTaskModel.getSourcePath();
			}
			storePath = storePath.substring(0, storePath.lastIndexOf("/"));
			File file = new File(storePath);// 读取
			deleteFlag = deleteFile(file);
		}
		if(num<=0 || !deleteFlag){
            throw new RuntimeException("删除失败");
        }
        return  true;
	}

	private boolean deleteFile(File file) {
		boolean deleteFlag = false;
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete();
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFlag = this.deleteFile(files[i]);
				}
			}
			deleteFlag = file.delete();
		}
		return deleteFlag;
	}
}
	
