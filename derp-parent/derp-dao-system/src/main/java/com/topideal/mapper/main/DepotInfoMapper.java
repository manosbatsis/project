package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.DepotInfoDTO;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/**
 * 仓库信息表 Mapper
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface DepotInfoMapper extends BaseMapper<DepotInfoModel> {

	/**
	 * 查询仓库下拉列表
	 * */
	List<SelectBean> getSelectBean(DepotInfoModel model) throws SQLException;
	
	/**
	 * 查询代理商家下拉列表
	 * */
	List<MerchantInfoModel> getSelectPoxy() throws SQLException;
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<DepotInfoDTO> getListByPage(DepotInfoDTO model) throws SQLException;
	
	/**
	 * 查询仓库下拉列表，是否同关区
	 * */
	List<SelectBean> getSelectBeanByArea(DepotInfoDTO dto) throws SQLException;
	/**
	 * admin账号查询仓库下拉列表
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBeanForAdmin(DepotInfoModel model) throws SQLException;
	/**
	 * 根据页面传入商家id获取此商家下仓库的下拉框
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBeanByMerchantRel(DepotMerchantRelDTO dto) throws SQLException;

	DepotInfoDTO searchDTOById(@Param("id")Long id);
	/**
	 * 获取商家下仓库的下拉框
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<DepotInfoDTO> getSelectBeanByDTO(DepotInfoDTO dto) throws SQLException;

	/**
	 * 根据页面传入商家id、事业部id、仓库类别、是否代客管理仓库、是否是代销仓获取此商家事业部下仓库的下拉框
	 * */
	List<SelectBean> getSelectBeanByMerchantBuRel(MerchantDepotBuRelDTO dto) throws SQLException;
	
	/**
	 * 修改非必填的字段
	 * @param depotInfoModel
	 * @return
	 */
	int updateNULL(DepotInfoModel depotInfoModel);

    List<DepotInfoModel> listByLike(DepotInfoModel depotInfoModel);
}

