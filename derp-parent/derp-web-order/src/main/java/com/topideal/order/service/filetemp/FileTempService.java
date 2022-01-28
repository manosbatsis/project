package com.topideal.order.service.filetemp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.common.FileTempCustomerRelModel;
import com.topideal.entity.vo.common.FileTempModel;
import org.springframework.web.multipart.MultipartFile;

public interface FileTempService {

	FileTempDTO searchById(Long id) throws SQLException;

	FileTempDTO listFiletemp(FileTempDTO dto) throws SQLException;

	void saveOrModity(FileTempModel model) throws Exception;

	List<FileTempCustomerRelModel> listCustomerRel(Long fileId) throws SQLException;

	/**
	 * 保存
	 */
	Map<String, String> saveCustomerRel(FileTempDTO dto) throws Exception;

	List<FileTempDTO> listFileTempInfo(FileTempDTO dto, String customerIds) throws SQLException;
	/**
	 * 获取模板下拉
	 * @return
	 * @throws SQLException
	 */
	List<FileTempModel> getFileTemp(FileTempModel model) throws SQLException;

	/**
	 * 生成发票html
	 * @param user
	 */
	String generateHtml(FileTempDTO fileTemp, User user) throws Exception;

	/**
	 * 上传附件
	 */
	Map<String,Object> uploadFile(MultipartFile file, String code, User user, String type) throws Exception;

	/**
	 * 逻辑删除
	 */
	void delAttachment(String code, String type) throws Exception;
}
