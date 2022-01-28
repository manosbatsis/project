package com.topideal.order.service.attachment;

import com.topideal.common.system.auth.User;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.tools.PageMongo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AttachmentService {

	/**
	 * 上传附件
	 */
	Map<String,Object> uploadFile(MultipartFile file, String code, User user, Boolean saveMongoFlag) throws Exception;

	/**
	 * 查询附件列表信息
	 */
	PageMongo<AttachmentInfoMongo> listAttachment(String code);

	/**
	 * 逻辑删除附件信息
	 */
	void delAttachment(String[] codeArr) throws Exception;

	/**
	 * 根据文件编码查询数据
	 */
	List<AttachmentInfoMongo> listByAttachmentCodes(String attachmentCodes, String orderCode) throws Exception;

}
