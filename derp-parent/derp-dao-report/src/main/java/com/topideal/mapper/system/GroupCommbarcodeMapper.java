package com.topideal.mapper.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.system.GroupCommbarcodeModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface GroupCommbarcodeMapper extends BaseMapper<GroupCommbarcodeModel> {


	/**
	 * 查询所有标准条码所对应的组码
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getCommbarcodeAndGroupCommbarcode() throws SQLException;

	/**
	 * 查询标准条码所对应的组码关联的标准条码
	 * @return
	 * @throws SQLException
	 */
	List<String> getCommbarcodeByCommbarcode(@Param("commbarcode") String commbarcode) throws SQLException;
	
	/**
	 * 根据组码获取所有的标准条码
	 */
	List<String> getCommbarcodeBygroupCommbar(@Param("groupCommbarcode") String groupCommbarcode) throws SQLException;
}
