package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.common.FileTempModel;

import java.sql.SQLException;
import java.util.List;


public interface FileTempDao extends BaseDao<FileTempModel>{

	List<FileTempDTO> getPageList(FileTempDTO dto);
		
	int delByFileId(Long fileId) throws SQLException;

	List<FileTempDTO> listFileTempInfo(FileTempDTO dto) throws SQLException;

	Integer coutPageList(FileTempDTO dto);







}
