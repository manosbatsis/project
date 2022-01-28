package com.topideal.service.main.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.NoticeDao;
import com.topideal.dao.main.NoticeUserRelDao;
import com.topideal.dao.user.UserInfoDao;
import com.topideal.entity.dto.main.NoticeDTO;
import com.topideal.entity.vo.main.NoticeModel;
import com.topideal.entity.vo.main.NoticeUserRelModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.service.main.NoticeService;

import net.sf.json.JSONObject;

@Service
public class NoticeServiceImpl implements NoticeService{
	
	@Autowired 
	private NoticeDao noticeDao ;
	
	@Autowired
	private UserInfoDao userInfoDao ;
	
	@Autowired
	private NoticeUserRelDao noticeUserRelDao ;

	@Override
	public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
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
        
        if(jsonObj.getInt("code")!= 200){
        	map.put("code" , jsonObj.getInt("code")) ;
        	map.put("msg", jsonObj.getString("msg"));
        	
        	return map ;
        }
        
        //返回文件服务器存储URL
        String url = jsonObj.getString("url") ;
        
        map.put("state", "SUCCESS");
        map.put("url", url);
        map.put("original", "");
        map.put("type", ext);
        map.put("size", file.getSize());
        map.put("title", fileName);
        
        return map ;
	}

	@Override
	public void saveOrModity(NoticeModel model) throws Exception {
		
		if(model.getId() == null) {
			model.setStatus(DERP_SYS.NOTICE_STATUS_001);
			model.setCreateDate(TimeUtils.getNow());
			noticeDao.save(model) ;
		}else {
			model.setModifyDate(TimeUtils.getNow());
			noticeDao.modify(model) ;
		}
	}

	@Override
	public NoticeDTO listNotice(NoticeDTO dto) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		
		if(StringUtils.isNotBlank(dto.getPublishStartDateStr())) {
			String publishStartDateStr = dto.getPublishStartDateStr() ;
			publishStartDateStr += " 00:00:00" ;
			
			Timestamp publishStartDate = new Timestamp(sdf.parse(publishStartDateStr).getTime()) ;
			
			dto.setPublishStartDate(publishStartDate);
		}
		
		if(StringUtils.isNotBlank(dto.getPublishEndDateStr())) {
			String publishEndDateStr = dto.getPublishEndDateStr() ;
			publishEndDateStr += " 23:59:59" ;
			
			Timestamp publishEndDate = new Timestamp(sdf.parse(publishEndDateStr).getTime()) ;
			
			dto.setPublishEndDate(publishEndDate);
		}
		
		return noticeDao.getListByPage(dto);
	}

	@Override
	public NoticeModel searchById(Long id) throws SQLException {
		return noticeDao.searchById(id);
	}

	@Override
	public NoticeDTO searchDTOById(Long id) throws SQLException {
		return noticeDao.searchDTOById(id);
	}

	@Override
	public void modifyStatus(NoticeModel model) throws Exception {
		
		/*
		 *	查询状态是否未发布 
		 */
		Long id = model.getId();
		NoticeModel tempModel = noticeDao.searchById(id);
		
		if(tempModel != null) {
			
			if(!DERP_SYS.NOTICE_STATUS_001.equals(tempModel.getStatus())) {
				throw new RuntimeException("修改失败，该公告不为待发布状态") ;
			}
			// 非删除状态才有发布日期
			if (!DERP_SYS.NOTICE_STATUS_006.equals(model.getStatus())) {
				model.setPublishDate(TimeUtils.getNow());
			}
			model.setModifyDate(TimeUtils.getNow());
			int rows = noticeDao.modify(model);
			
			/**
			 *  如果修改为已发布，推送到每个用户 
			 */
			if(rows > 0 && DERP_SYS.NOTICE_STATUS_002.equals(model.getStatus())) {
				List<UserInfoModel> userList = userInfoDao.list(new UserInfoModel());
			
				
				for (UserInfoModel userInfoModel : userList) {
					NoticeUserRelModel noticeUserRelModel = new NoticeUserRelModel();
					noticeUserRelModel.setUserId(userInfoModel.getId());
					noticeUserRelModel.setNoticeId(model.getId());
					noticeUserRelModel.setStatus(DERP_SYS.NOTICE_USER_REL_STATUS_0);
					noticeUserRelModel.setCreateDate(TimeUtils.getNow());
					
					noticeUserRelDao.save(noticeUserRelModel);
					
				}
			}
			
		}else {
			throw new RuntimeException("该公告不存在") ;
		}
	}

	@Override
	public List<NoticeDTO> getNoticeByUser(User user, String pageNoStr) throws SQLException{
		
		Long userId = user.getId();
		Integer pageNo = Integer.valueOf(pageNoStr);
		Integer pageSize = 10 ;
		
		Map<String , Object> params = new HashMap<String , Object>() ;
		params.put("userId", userId) ;
		params.put("begin", pageSize * (pageNo - 1)) ;
		params.put("pageSize", pageSize) ;
		
		return noticeDao.getNoticeByUser(params);
	}

	@Override
	public Integer getUnReadAmountByUser(User user) {
		Long userId = user.getId();
		return noticeDao.getUnReadAmountByUser(userId);
	}

	@Override
	public void changeReadStatus(NoticeUserRelModel relModel) throws SQLException {
		NoticeUserRelModel tempRelModel = noticeUserRelDao.searchByModel(relModel);
		
		NoticeUserRelModel updateModel = new NoticeUserRelModel() ;
		updateModel.setId(tempRelModel.getId());
		updateModel.setStatus(DERP_SYS.NOTICE_USER_REL_STATUS_1);
		updateModel.setModifyDate(TimeUtils.getNow());
		
		noticeUserRelDao.modify(updateModel) ;
	}

	@Override
	public Integer getNoticeAccountByUser(User user) {
		return noticeDao.getNoticeAccountByUser(user.getId());
	}

	@Override
	public Map<String, Object> getAroundNotice(Long noticeId) throws SQLException {
		
		Map<String,Object> map = new HashMap<String,Object>() ;
		
		NoticeModel beforeModel = noticeDao.getBeforeNotice(noticeId) ;
		NoticeModel afterModel = noticeDao.getAfterNotice(noticeId) ;
		
		if(beforeModel != null) {
			map.put("before", beforeModel) ;
		}
		
		if(afterModel != null) {
			map.put("after", afterModel) ;
		}
		
		return map;
	}

}
