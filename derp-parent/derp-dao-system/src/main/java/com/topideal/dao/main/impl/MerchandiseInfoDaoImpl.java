package com.topideal.dao.main.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.mapper.main.MerchandiseInfoMapper;
import com.topideal.mapper.main.ProductInfoMapper;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.ProductInfoMongoDao;

import net.sf.json.JSONObject;

/**
 * 商品信息impl
 * @author lchenxing
 */
@Repository
public class MerchandiseInfoDaoImpl implements MerchandiseInfoDao {

    @Autowired
    private MerchandiseInfoMapper mapper;  //商品信息
    @Autowired
    private ProductInfoMapper productInfoMapper;//产品信息

	@Autowired
	private MerchandiseInfoMogoDao mongo;  //商品信息 mongo
	@Autowired
	private ProductInfoMongoDao productInfoMongo;//产品信息 mongo
	
	
    /**
     * 新增
     * @param model
     */
    @Override
    public Long save(MerchandiseInfoModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	MerchandiseInfoModel merchandise = new MerchandiseInfoModel();
    		merchandise.setId(model.getId());
    		merchandise = mapper.get(merchandise);
        	//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(merchandise);
			jsonObject.put("merchandiseId",model.getId());
			// 移除字段
			jsonObject.remove("id");
			jsonObject.remove("productName");
			jsonObject.remove("brandName");
			jsonObject.remove("countyName");
			jsonObject.remove("unitName");
			jsonObject.remove("goodsClassifyName");
			jsonObject.remove("productImg01");
			jsonObject.remove("color");
			jsonObject.remove("size");
			jsonObject.remove("productRemark");
			jsonObject.remove("depotId");
			jsonObject.remove("merchantIds");
			jsonObject.remove("supplyMinPrice");
			jsonObject.remove("cloudMerchantId");
			jsonObject.remove("createrName");
			jsonObject.remove("modifierName");
			jsonObject.remove("supplierId");
			jsonObject.remove("createDate");
			jsonObject.remove("modifyDate");			
			mongo.insertJson(jsonObject.toString());		
        }
        return model.getId();
    }
    
    /**
     * 批量删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
		//先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseId",(Long)ids.get(i));
			mongo.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
    /**
     * 修改
     * @param model
     */
    @Override
    public int modify(MerchandiseInfoModel model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
    	int num = mapper.update(model);
    	if(num > 0){
    		MerchandiseInfoModel merchandise = new MerchandiseInfoModel();
    		merchandise.setId(model.getId());
    		merchandise = mapper.get(merchandise);
    		
    		//修改mongodb 商品信息
    		Map<String, Object> params = new HashMap<String,Object>();
    		params.put("merchandiseId",model.getId());
			JSONObject jsonObject=JSONObject.fromObject(merchandise);
			jsonObject.put("merchandiseId",model.getId());
			
			// 移除字段
			jsonObject.remove("id");
			jsonObject.remove("productName");
			jsonObject.remove("brandName");
			jsonObject.remove("countyName");
			jsonObject.remove("unitName");
			jsonObject.remove("goodsClassifyName");
			jsonObject.remove("productImg01");
			jsonObject.remove("color");
			jsonObject.remove("size");
			jsonObject.remove("productRemark");
			jsonObject.remove("depotId");
			jsonObject.remove("merchantIds");
			jsonObject.remove("supplyMinPrice");
			jsonObject.remove("cloudMerchantId");
			jsonObject.remove("createrName");
			jsonObject.remove("modifierName");
			jsonObject.remove("supplierId");
			jsonObject.remove("createDate");
			jsonObject.remove("modifyDate");			
        	mongo.update(params,jsonObject);
    	}
        return num;
    }

    /**
     * 分页查询
     * @param model
     */
    @Override
    public MerchandiseInfoModel  searchByPage(MerchandiseInfoModel  model) throws SQLException{
        PageDataModel<MerchandiseInfoModel> pageModel=mapper.listByPage(model);
        return (MerchandiseInfoModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchandiseInfoModel  searchById(Long id)throws SQLException {
        MerchandiseInfoModel  model=new MerchandiseInfoModel ();
        model.setId(id);
        return mapper.getDetails(model);
    }
    
    /**
     * 根据商品实体类查询商品
     * @param model
     * */
	@Override
	public MerchandiseInfoModel searchByModel(MerchandiseInfoModel model) throws SQLException{
		return mapper.get(model);
	}

	/**
	 * 保存商品和产品
	 * @param merchandiseInfoModel 产品
	 * @param productInfoModel
	 * */
	@Override
	public void saveGoods(ProductInfoModel productInfoModel,MerchandiseInfoModel merchandiseInfoModel) throws Exception {
		//根据条形码查询产品（如果产品不存在就新增产品和商品  ）（如果产品存在就看商品存在不存在 如果存在就修改，如果不存在就新增）
		
		ProductInfoModel p= new ProductInfoModel();
		p.setBarcode(productInfoModel.getBarcode());
		p = productInfoMapper.get(p);
		
		MerchandiseInfoModel m= new MerchandiseInfoModel();
		m.setUniques(merchandiseInfoModel.getUniques());
		m = mapper.get(m);
		if (null==p) {//新增
			productInfoMapper.insert(productInfoModel);
		}else {//修改
			productInfoModel.setId(p.getId());
			merchandiseInfoModel.setModifyDate(TimeUtils.getNow());
			productInfoMapper.update(productInfoModel);
		}
		if (null==m) {//新增商品
			mapper.insert(merchandiseInfoModel);
		}else {// 修改商品
			merchandiseInfoModel.setModifyDate(TimeUtils.getNow());
			merchandiseInfoModel.setId(m.getId());
			mapper.update(merchandiseInfoModel);
		}
	}

	/**
	 *列表查询 分页 
	 * @param model
	 */
	@Override
	public List<MerchandiseInfoModel> list(MerchandiseInfoModel model) throws SQLException {
		return mapper.list(model);
	} 
	
	/**
	 * 条件查询商品下拉列表
	 * @param merchantId
	 * @param depotId
	 * */
	@Override
	public List<SelectBean> getSelectBean(Long merchantId, Long depotId) throws SQLException{
		return mapper.getSelectBean(merchantId, depotId);
	}

	@Override
	public boolean insertMerchandisInfo(MerchandiseInfoModel merchandiseInfoModel) throws SQLException {
		//商品编码
		MerchandiseInfoModel mModel=new MerchandiseInfoModel();
		mModel.setUniques(merchandiseInfoModel.getUniques());
		mModel=mapper.get(mModel);
		if(mModel==null){
			mapper.insert(merchandiseInfoModel);
		}else{
			merchandiseInfoModel.setId(mModel.getId());
			merchandiseInfoModel.setModifyDate(TimeUtils.getNow());
			mapper.update(merchandiseInfoModel);
		}
		return true;
	}

	/**
	 * 根据ids获取商品列表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<MerchandiseInfoModel> getListByIds(List ids) throws SQLException {
		return mapper.getListByIds(ids);
	}

	@Override
	public MerchandiseInfoModel getListforMerchantIdByPage(MerchandiseInfoModel model) {
		PageDataModel<MerchandiseInfoModel> pageModel=mapper.getListforMerchantIdByPage(model);
        return (MerchandiseInfoModel ) pageModel.getModel();
	}

	/**
	 * 采购订单取当前商家的商品以及选择的供应商商品
	 * @param model
	 * @return
	 */
	@Override
	public MerchandiseInfoModel getListToPurchaseByPage(MerchandiseInfoModel model) throws SQLException{
		PageDataModel<MerchandiseInfoModel> pageModel=mapper.getListToPurchaseByPage(model);
        return (MerchandiseInfoModel ) pageModel.getModel();
	}

	/**
	 * 新增商品根据商家id获取备案商品
	 * @return
	 */
	@Override
	public MerchandiseInfoModel getListForAddByPage(MerchandiseInfoModel model) throws SQLException {
		PageDataModel<MerchandiseInfoModel> pageModel=mapper.getListForAddByPage(model);
        return (MerchandiseInfoModel ) pageModel.getModel();
	}

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public MerchandiseInfoDTO getListByPage(MerchandiseInfoDTO dto) throws SQLException {
		List<MerchandiseInfoDTO> goodsList=mapper.getList(dto);
		int total=mapper.getListCount(dto);
		dto.setList(goodsList);
		dto.setTotal(total);
        return dto;
	}

	/**
	 * 公共的商品分页弹窗
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public MerchandiseInfoModel getPopupListByPage(MerchandiseInfoModel model) throws SQLException {
		PageDataModel<MerchandiseInfoModel> pageModel=mapper.getPopupListByPage(model);
        return (MerchandiseInfoModel ) pageModel.getModel();
	}

	@Override
	public MerchandiseInfoDTO getPopupListDTOByPage(MerchandiseInfoDTO dto) throws SQLException {
		PageDataModel<MerchandiseInfoDTO> pageModel=mapper.getPopupListDTOByPage(dto);
		return (MerchandiseInfoDTO ) pageModel.getModel();
	}

	@Override
	public MerchandiseInfoModel getSaleListByPage(MerchandiseInfoModel model) throws SQLException {
		PageDataModel<MerchandiseInfoModel> pageModel=mapper.getSaleListByPage(model);
        return (MerchandiseInfoModel ) pageModel.getModel();
	}

	/**
	 * 根据ids获取客户销售价格列表
	 */
	@Override
	public List<MerchandiseInfoModel> getSaleListByIds(List ids) throws SQLException {
		return mapper.getSaleListByIds(ids);
	}
	/**
	 * 结算价格-选择商品
	 * 获取商家下所有商品（条码相同只取一条记录）
	 * @param model
	 * @return
	 */
	@Override
	public MerchandiseInfoModel getListForSettlmentByPage(MerchandiseInfoModel model) throws SQLException {
		PageDataModel<MerchandiseInfoModel> pageModel=mapper.getListForSettlmentByPage(model);
		return (MerchandiseInfoModel ) pageModel.getModel();
	}

/*	
	public List<MerchandiseInfoModel> getListByBarcode(Long merchantId, String barcode) {
		return mapper.getListByMerchantIdAndBarcode(merchantId, barcode);
	}*/

	@Override
	public List<MerchandiseInfoModel> getRelListByBarcode(Long merchantId, String barcode) {
		return mapper.getListByRelMerchantIdAndBarcode(merchantId, barcode);
	}

	@Override
	public MerchandiseInfoDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}

	@Override
	public List<MerchandiseInfoModel> getRelListByCommbarcode(List<Long> merchantIds, String commbarcode) throws SQLException {
		return mapper.getRelListByCommbarcode(merchantIds, commbarcode);
	}

	@Override
	public List<MerchandiseInfoDTO> exportList(MerchandiseInfoDTO dto) throws SQLException {
		return mapper.exportMerchandiseInfoMap(dto);
	}


	@Override
	public List<MerchandiseInfoModel> getListByModel(MerchandiseInfoModel model) throws SQLException {
		return mapper.getListByModel(model);
	}

	/**
	 * 根据条码查询商品标准条码不为空的商品
	 */
	@Override
	public List<MerchandiseInfoModel> getByMerchantBarcode(MerchandiseInfoModel model) throws SQLException {
		return mapper.getByMerchantBarcode(model);
	}

	/**
	 * 判断库存明细是否有该商品有返回true 没有返回false
	 */
	@Override
	public boolean getInventoryDetials(Long merchantId, Long goodsId) throws Exception {
		
		Class.forName(ApolloUtilDB.jdbforName);
		String dburl = ApolloUtilDB.crInventoryUrl;
		String dbUserName = ApolloUtilDB.crInventoryUserName;
		String dbPassword = ApolloUtilDB.crInventoryPassword;
		Connection conn = DriverManager.getConnection(dburl, dbUserName, dbPassword);
		if (conn == null) {
			throw new RuntimeException("链接库存失败");
		}
		
		Statement pst = null;
		ResultSet rs = null;
		Integer surplusNum = 0;
		Integer count=null;
		try {
			String queryStr = "SELECT COUNT(1) as count  from i_inventory_details where merchant_id="+ merchantId + " AND goods_id=" + goodsId;
			pst = conn.createStatement();
			rs = pst.executeQuery(queryStr);
			System.out.println("查询库存加减明细是否使用过:" + queryStr);
			while (rs.next()) {
				count = rs.getInt("count");
			}			
			if (rs != null)rs.close();
			if (pst != null)pst.close();
			conn.close();			
		} catch (Exception e) {
			if (rs != null)rs.close();
			if (pst != null)pst.close();
			conn.close();			
			throw new RuntimeException("链接库存查询失败");
		}
		
		if (count>0) {// 库存加减明细已经存在
			return true;
		}else {
			return false;//库存加减明细不存在
		}

	}
	/**
	 *获取所有非卓普信/嘉宝 状态是启用的商品
	 */
	@Override
	public List<MerchandiseInfoModel> getCopyGoodsList(Map<String, Object> paramMap) throws SQLException {
		return mapper.getCopyGoodsList(paramMap);
	}

	/**
	 * 获取嘉宝和卓普信 外仓唯一码
	 */
	@Override
	public List<Map<String, Object>> getZPXAndJBCopyGoodsList() throws SQLException {
		return mapper.getZPXAndJBCopyGoodsList();
	}

	/**
	 * 获取推送卓普信推送商品档案
	 */
	@Override
	public List<Map<String, Object>> getSendSapienceGoodsList(Map<String, Object> map) throws SQLException {
		return mapper.getSendSapienceGoodsList(map);
	}

	@Override
	public List<MerchandiseInfoModel> getByDepotAndBarcode(Long depotId, List<String> barcodes, Long merchantId) throws SQLException {
		return mapper.getByDepotAndBarcode(depotId, barcodes, merchantId);
	}

}

