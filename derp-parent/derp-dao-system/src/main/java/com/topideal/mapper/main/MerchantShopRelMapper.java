package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/**
 * 商家店铺关联表 mapper
 * @author lian_     
 *                        
 */
@MyBatisRepository
public interface MerchantShopRelMapper extends BaseMapper<MerchantShopRelModel> {
	
	/**
	  * 分页
	  * @param model
	  * @return
	  * @throws SQLException
	  */
	 PageDataModel<MerchantShopRelDTO> getListByPage(MerchantShopRelDTO dto) throws SQLException;

	 /**
	  * 查询商家下拉列表
	  * @param
	  * @return
	  * @throws SQLException
	  */
	 List<MerchantInfoModel> getSelectMerchant() throws SQLException;
	 
	 /**
	  * 获取详情
	  */
	 MerchantShopRelDTO getDetails(@Param("id")Long id) throws SQLException;

	/**
	 * 查询店铺下拉列表
	 */
	List<MerchantShopRelModel> getSelectMerchantShopRel(MerchantShopRelModel model) throws SQLException;
	
	/**检查店铺编码是否已存在*/
	List<MerchantShopRelModel> getcheckShopCode(MerchantShopRelModel model);

	/**根据查询条件查询导出信息*/
	List<MerchantShopRelDTO> getExportList(MerchantShopRelDTO dto);

	/**
	 * 更新非必填字段
	 * @param model
	 * @return
	 */
    int updateWithNull(MerchantShopRelModel model);
}
