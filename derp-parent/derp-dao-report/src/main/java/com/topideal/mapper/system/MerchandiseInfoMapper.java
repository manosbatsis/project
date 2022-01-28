package com.topideal.mapper.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.system.MerchandiseInfoModel;

/**
 * 商品 mapper
 * @author zhanghx
 */
@MyBatisRepository
public interface MerchandiseInfoMapper extends BaseMapper<MerchandiseInfoModel> {
	
	/**
	 * 获取商家的所有商品条码并去重
	 */
	public List<Map<String, Object>> getMerchantAllBarcode(Map<String, Object> paramMap);
	/**
	 * 获取商家的所有商品
	 */
	public List<MerchandiseInfoModel> getListByMerchantIds(Map<String, Object> paramMap);
	

	
	/**
	 * 查询商家所有货品条码、货号
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getMerchantAllInfo(Map<String, Object> paramMap) throws SQLException;
	/**
	 * 结算价格-导入（条码相同只取一条记录）
	 * @param model
	 * @return
	 */
	MerchandiseInfoModel getListForSettlment(@Param("merchantId") Long merchantId, @Param("barcode") String barcode);
	/**
	 * 查询商家所有商品
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getAllMerchandiseByMerchantId(Map<String, Object> paramMap) throws SQLException;
	
	/**
	 * 根据商家+标准条码 获取随机一条商品信息(生成加权用)
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getGoodsByMerchantIdWeighted(Map<String, Object> paramMap)throws SQLException;
	
	/**
	 * 查看组合商品
	 * @param goodsId
	 * @return
	 * @throws SQLException
	 */
	   
	//List<MerchandiseInfoModel> getAllGroupMerchandiseByGroupId(@Param("goodsId")Long goodsId) throws SQLException;

	/**
	 * 根据商品id查询商品信息包含关联产品信息
	 */
	Map<String, Object> getMerchandiseInfo(@Param("goodsId") Long goodsId);

    /**查询昨天新增和修改的条码
	 * */
	List<String> getBarcodeListDSTP(Map<String,Object> map);
    /**根据条码查询商品
	 * */
	List<Map<String,Object>> getDetailList(Map<String,Object> map);
}
