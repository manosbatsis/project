package com.topideal.service.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.main.NoticeDTO;
import com.topideal.entity.vo.main.NoticeModel;
import com.topideal.entity.vo.main.NoticeUserRelModel;

public interface NoticeService {

	Map<String , Object> uploadFile(MultipartFile file) throws IOException;

	void saveOrModity(NoticeModel model) throws Exception;

	NoticeDTO listNotice(NoticeDTO dto) throws Exception;

	NoticeModel searchById(Long id) throws SQLException;

	NoticeDTO searchDTOById(Long id) throws SQLException;

	void modifyStatus(NoticeModel model) throws Exception;

	List<NoticeDTO> getNoticeByUser(User user, String pageNo) throws SQLException;

	Integer getUnReadAmountByUser(User user);

	void changeReadStatus(NoticeUserRelModel relModel) throws SQLException;

	Integer getNoticeAccountByUser(User user);

	Map<String, Object> getAroundNotice(Long noticeId) throws SQLException;

}
