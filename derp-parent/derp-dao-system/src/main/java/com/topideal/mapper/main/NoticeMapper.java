package com.topideal.mapper.main;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.NoticeDTO;
import com.topideal.entity.vo.main.NoticeModel;
import com.topideal.entity.vo.main.NoticeUserRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface NoticeMapper extends BaseMapper<NoticeModel> {

	PageDataModel<NoticeDTO> getListByPage(NoticeDTO dto);

	NoticeDTO searchDTOById(@Param("id")Long id);

	List<NoticeDTO> getNoticeByUser(Map<String, Object> params);

	Integer getUnReadAmountByUser(@Param("userId")Long userId);

	Integer getNoticeAccountByUser(@Param("userId")Long userId);

	NoticeModel getBeforeNotice(@Param("noticeId")Long noticeId);
	
	NoticeModel getAfterNotice(@Param("noticeId")Long noticeId);
	

}
