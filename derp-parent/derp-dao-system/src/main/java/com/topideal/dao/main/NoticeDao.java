package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.NoticeDTO;
import com.topideal.entity.vo.main.NoticeModel;
import com.topideal.entity.vo.main.NoticeUserRelModel;


public interface NoticeDao extends BaseDao<NoticeModel> {

	NoticeDTO getListByPage(NoticeDTO dto) throws SQLException;

	NoticeDTO searchDTOById(Long id) throws SQLException;

	List<NoticeDTO> getNoticeByUser(Map<String, Object> params) throws SQLException;

	Integer getUnReadAmountByUser(Long userId);

	Integer getNoticeAccountByUser(Long userId);

	NoticeModel getBeforeNotice(Long noticeId) throws SQLException;

	NoticeModel getAfterNotice(Long noticeId) throws SQLException;
		










}
