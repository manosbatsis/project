package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.dto.main.ProductInfoDTO;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.mapper.BaseMapper;

/**
 * 产品表 Mapper
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface ProductInfoMapper extends BaseMapper<ProductInfoModel> {

	/**
	 * 分页
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<MerchandiseInfoDTO> getListByPage(MerchandiseInfoDTO model) throws SQLException;

	/**
	 * 获取详情
	 */
	ProductInfoDTO getDetails(ProductInfoDTO dto) throws SQLException;
	
	/**
	 * 批量修改产品类别
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	boolean updateBatch(List<ProductInfoModel> list) throws SQLException;
}

