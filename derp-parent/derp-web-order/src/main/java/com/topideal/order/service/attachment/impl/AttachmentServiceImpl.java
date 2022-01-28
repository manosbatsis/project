package com.topideal.order.service.attachment.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.auth.User;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.AttachmentInfoMongoDao;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.order.service.attachment.AttachmentService;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class AttachmentServiceImpl implements AttachmentService{
	private static final Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);
	@Autowired
	private AttachmentInfoMongoDao attachmentInfoMongoDao ;

	/**
	 * 上传附件
	 */
	@Override
	public Map<String, Object> uploadFile(MultipartFile file, String code, User user, Boolean saveMongoFlag) throws Exception {
		
		Map<String ,Object> map = new HashMap<String, Object>() ;
		
		String fileName = file.getOriginalFilename();
        byte[] fileBytes = file.getBytes();
        Long fileSize = file.getSize();
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName",fileName);
        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
        jsonObject.put("fileExt",ext);
        jsonObject.put("fileSize",String.valueOf(fileSize));
        String resultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);
        
        JSONObject jsonObj = JSONObject.fromObject(resultJson);
    	logger.info("蓝精灵返回结果:"+jsonObj);
        if(jsonObj.getInt("code")!= 200){
        	map.put("code" , jsonObj.getInt("code")) ;
        	map.put("msg", jsonObj.getString("msg"));
        	
        	return map ;
        }
        
        //返回文件服务器存储URL
        String url = jsonObj.getString("url") ;
        
        //附件信息写入MongoDB
        AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
        attachmentInfoMongo.setAttachmentName(fileName); 		//附件名
        attachmentInfoMongo.setAttachmentSize(fileSize); 		//附件大小
        attachmentInfoMongo.setAttachmentType(ext);		       	//附件类型
        attachmentInfoMongo.setCreator(user.getId());			//上传者
        attachmentInfoMongo.setCreatorName(user.getName());
        attachmentInfoMongo.setCreateDate(new Date());
        attachmentInfoMongo.setRelationCode(code);              //关联订单编码
        attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
        attachmentInfoMongo.setAttachmentUrl(url);              //设置Url
        attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
        
        String module = "" ;
        //设置所属模块
        for(SourceStatusEnum sourceStatusEnum : SourceStatusEnum.values()){
  	      if(code.contains(sourceStatusEnum.name())){
  	        module = sourceStatusEnum.getKey() ;
  	        break ;
  	      }
  	    }
        
        if(StringUtils.isBlank(module)) {
			module = SourceStatusEnum.XSTHRK.getValue() ;
		}
        
        attachmentInfoMongo.setModule(module);

        if(saveMongoFlag == null){saveMongoFlag = true;}
        if(saveMongoFlag != null && saveMongoFlag.equals(true)) {
			attachmentInfoMongoDao.insert(attachmentInfoMongo);
		}

        map.put("code", 200) ;
        map.put("attachmentInfo", attachmentInfoMongo) ;
        
		return map;
	}

	/**
	 * 查询附件列表信息
	 */
	@Override
	public PageMongo<AttachmentInfoMongo> listAttachment(String code) {
		
		PageMongo<AttachmentInfoMongo> page = new PageMongo<>() ;

		List<AttachmentInfoMongo> list = new ArrayList<>();
		for(String codes:code.split(",")){
			Map<String,Object> params = new HashMap<String,Object>() ;
			params.put("relationCode", codes) ;
			params.put("status", DERP_ORDER.ATTACHMENT_STATUS_001) ;

			List<AttachmentInfoMongo> newList=attachmentInfoMongoDao.findAllOrderByDate(params);
			list.addAll(newList);
		}

		page.setList(list);
		page.setTotal(list.size());
		
		return page ;
	}
	
	/**
	 * 逻辑删除附件信息
	 */
	@Override
	public void delAttachment(String[] codes) throws Exception{
		
		Map<String,Object> params = new HashMap<String,Object>() ;
		Map<String,Object> data = new HashMap<String,Object>() ;
		for (String attachmentCode : codes) {
			params.clear();
			params.put("attachmentCode", attachmentCode) ;
			
			data.clear();
			data.put("status", DERP_ORDER.ATTACHMENT_STATUS_006) ;
			data.put("modifyDate", new Date()) ;
			
			attachmentInfoMongoDao.update(params, data);
		}
		
	}

    @Override
    public List<AttachmentInfoMongo> listByAttachmentCodes(String attachmentCodes, String orderCode) {
		List<AttachmentInfoMongo> attachmentInfoMongoList = new ArrayList<>();
		for (String attachmentCode : attachmentCodes.split(",")) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("attachmentCode", attachmentCode);
			params.put("relationCode", orderCode);
			params.put("status", DERP_ORDER.ATTACHMENT_STATUS_001);

			AttachmentInfoMongo attachmentInfoMongo = attachmentInfoMongoDao.findOne(params);
			attachmentInfoMongoList.add(attachmentInfoMongo);
		}

		return attachmentInfoMongoList;
    }

}
