package com.topideal.mapper.common;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface FileTempMapper extends BaseMapper<FileTempModel> {

	List<FileTempDTO> getPageList(FileTempDTO dto);

	int delByFileId(@Param("fileId") Long fileId) throws SQLException;

	List<FileTempDTO> listFileTempInfo(FileTempDTO dto) throws SQLException;

	Integer coutPageList(FileTempDTO dto);
}
