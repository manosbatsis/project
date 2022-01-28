package com.topideal.mapper.main;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.main.CommbarcodeDTO;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CommbarcodeMapper extends BaseMapper<CommbarcodeModel> {

	/**
	 * 查询分页信息
	 * @param model
	 * @return
	 */
	List<CommbarcodeDTO> listCommbarcodes(CommbarcodeDTO model);

	/**
	 * 查询分页信息总数
	 * @param model
	 * @return
	 */
	Integer listCommbarcodesCount(CommbarcodeDTO model);

	/**
	 * 查询导出列表
	 * @param ids
	 * @return
	 */
	List<CommbarcodeDTO> getExportList(List<String> ids);

	/**
	 * 根据标准条码模糊搜索
	 * @param commbarcode
	 * @return
	 */
	List<CommbarcodeModel> getVagueList(@Param("commbarcode")String commbarcode);

	CommbarcodeDTO searchByDTO(CommbarcodeDTO dto);

	/**
	 * 批量更新标准条码信息
	 * @param commbarcodeModels
	 * @return
	 */
	void batchUpdateCommbarcode(@Param("commbarcodeModels") List<CommbarcodeModel> commbarcodeModels);
	/**
	 * 导出列表
	 * @param ids
	 * @return
	 */
	List<CommbarcodeDTO> getExportCommbarcodes(List<String> ids);

}
