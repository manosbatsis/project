package com.topideal.service.main;


import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.dto.main.ProductInfoDTO;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 货品
 * @author zhanghx
 */
public interface ProductInfoService {
	
	/**
	 * 分页
	 * @return
	 * @throws SQLException
	 */
	MerchandiseInfoDTO getListByPage(MerchandiseInfoDTO model) throws SQLException;
	
	/**
	 * 根据id获取商品详情
	 * @param id
	 * @return
	 */
	ProductInfoDTO searchDetail(Long id) throws SQLException;

	/**
	 * 修改品类
	 * @param model
	 * @return
	 */
	boolean modifyProduct(ProductInfoModel model) throws SQLException;
	
	/**
	 * 批量修改产品类别
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	boolean updateBatch(List<Long> list, String productTypeId) throws Exception;
	
}
