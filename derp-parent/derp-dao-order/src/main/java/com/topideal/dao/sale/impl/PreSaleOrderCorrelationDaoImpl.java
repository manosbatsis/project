package com.topideal.dao.sale.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.PreSaleOrderCorrelationDao;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.entity.vo.sale.PreSaleOrderCorrelationModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.entity.vo.sale.SaleOutDepotModel;
import com.topideal.mapper.sale.PreSaleOrderCorrelationMapper;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PreSaleOrderCorrelationDaoImpl implements PreSaleOrderCorrelationDao {

    @Autowired
    private PreSaleOrderCorrelationMapper mapper;
    @Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品信息

	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PreSaleOrderCorrelationModel> list(PreSaleOrderCorrelationModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PreSaleOrderCorrelationModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(PreSaleOrderCorrelationModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PreSaleOrderCorrelationModel  searchByPage(PreSaleOrderCorrelationModel  model) throws SQLException{
        PageDataModel<PreSaleOrderCorrelationModel> pageModel=mapper.listByPage(model);
        return (PreSaleOrderCorrelationModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PreSaleOrderCorrelationModel  searchById(Long id)throws SQLException {
        PreSaleOrderCorrelationModel  model=new PreSaleOrderCorrelationModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PreSaleOrderCorrelationModel searchByModel(PreSaleOrderCorrelationModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public PreSaleOrderCorrelationDTO listPreSaleOrderCorrelationByPage(PreSaleOrderCorrelationDTO dto) throws SQLException {
        PageDataModel<PreSaleOrderCorrelationDTO> pageModel = mapper.listPreSaleOrderCorrelationByPage(dto);
        return (PreSaleOrderCorrelationDTO ) pageModel.getModel();
    }

    @Override
    public int getTotal(PreSaleOrderCorrelationDTO dto) throws SQLException {
        return mapper.getTotal(dto);
    }

    @Override
    public PreSaleOrderCorrelationDTO detail(PreSaleOrderCorrelationDTO dto) throws SQLException {
        return mapper.detail(dto);
    }

    @Override
    public List<PreSaleOrderCorrelationDTO> detailByCodeAndGoodsNo(PreSaleOrderCorrelationDTO dto) throws SQLException {
        return mapper.detailByCodeAndGoodsNo(dto);
    }

    @Override
    public List<Map<String, Object>> detailForExport(PreSaleOrderCorrelationDTO dto) throws SQLException {
        return mapper.detailForExport(dto);
    }

    @Override
    public List<Map<String, Object>> listForExport(PreSaleOrderCorrelationDTO dto) throws SQLException {
        return mapper.listForExport(dto);
    }
    @Override
    public Integer getPreNumByPreSaleId(List<String> preSaleOrderCodes, Long goodsId) {
        return mapper.getPreNumByPreSaleId(preSaleOrderCodes,goodsId);
    }

    @Override
    public Integer getSaleNumByPreSaleId(List<String> preSaleOrderCodes, Long goodsId) {
        return mapper.getSaleNumByPreSaleId(preSaleOrderCodes,goodsId);
    }
    
    /**
     * 预售勾稽明细表出库数据
     */
	@Override
	public List<PreSaleOrderCorrelationModel> getPreSaleOrderCorList(SaleOutDepotModel saleOutDepotModel,SaleOrderModel saleOrderModel,List<Map<String, Object>> itemMapList)
			throws Exception {

		List<PreSaleOrderCorrelationModel> preSaleList=new ArrayList<PreSaleOrderCorrelationModel>();
		for (Map<String, Object> itemMap: itemMapList) {
			PreSaleOrderCorrelationModel preSaleModel=new PreSaleOrderCorrelationModel();
			Long goodsId = (Long) itemMap.get("goods_id");
			BigDecimal num = (BigDecimal) itemMap.get("num");
			preSaleModel.setSaleOrderCode(saleOrderModel.getCode());
			preSaleModel.setGoodsId(goodsId);
			List<PreSaleOrderCorrelationModel> preList = mapper.list(preSaleModel);
			if (preList==null||preList.size()==0) {
				throw new RuntimeException("订单号:"+saleOrderModel.getCode()+"商品id:"+goodsId+"没有找到预售(销售)勾稽数据");
			}	
			Map<String, Object> merchandiseInfoMap = new HashMap<>();
			merchandiseInfoMap.put("merchandiseId", goodsId);//商品id
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 查询商品信息

			if (preList.size()==1) {// 1条预售单
				PreSaleOrderCorrelationModel serchModel = preList.get(0);
				// 预售勾稽
				PreSaleOrderCorrelationModel preSale=new PreSaleOrderCorrelationModel();
				preSale.setMerchantId(saleOrderModel.getMerchantId());
				preSale.setMerchantName(saleOrderModel.getMerchantName());
				preSale.setPreSaleOrderId(serchModel.getPreSaleOrderId());;
				preSale.setPreSaleOrderCode(serchModel.getPreSaleOrderCode());
				preSale.setSaleOrderCode(saleOrderModel.getCode());
				preSale.setSaleOutDepotCode(saleOutDepotModel.getCode());
				preSale.setGoodsId(goodsId);
				preSale.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
				preSale.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
				preSale.setGoodsName(merchandiseInfoMogo.getName());
				preSale.setBarcode(merchandiseInfoMogo.getBarcode());
				preSale.setCommbarcode(merchandiseInfoMogo.getCommbarcode());				
				preSale.setOutNum(num.intValue());
				preSale.setOutDepotDate(saleOutDepotModel.getDeliverDate());		
				preSaleList.add(preSale);

			}else {// 多个条预售单 
				int intNum = num.intValue();//出库单的量
				for (int i = 0; i < preList.size(); i++) {
					PreSaleOrderCorrelationModel serchModel = preList.get(i);
					PreSaleOrderCorrelationModel preSale=new PreSaleOrderCorrelationModel();
					preSale.setMerchantId(saleOrderModel.getMerchantId());
					preSale.setMerchantName(saleOrderModel.getMerchantName());
					preSale.setPreSaleOrderId(serchModel.getPreSaleOrderId());;
					preSale.setPreSaleOrderCode(serchModel.getPreSaleOrderCode());
					preSale.setSaleOrderCode(saleOrderModel.getCode());
					preSale.setSaleOutDepotCode(saleOutDepotModel.getCode());
					preSale.setGoodsId(goodsId);
					preSale.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
					preSale.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
					preSale.setGoodsName(merchandiseInfoMogo.getName());
					preSale.setBarcode(merchandiseInfoMogo.getBarcode());
					preSale.setCommbarcode(merchandiseInfoMogo.getCommbarcode());				
					preSale.setOutDepotDate(saleOutDepotModel.getDeliverDate());		
	
					Integer saleNum = serchModel.getSaleNum();// 销售订单的量
					
					int comperNum=intNum-saleNum;
					if (comperNum>0) {//						
						if (i==preList.size()-1) {
							preSale.setOutNum(intNum);
						}else {
							preSale.setOutNum(saleNum);
						}							
						intNum=intNum-saleNum;
						preSaleList.add(preSale);
					}else {
						preSale.setOutNum(intNum);
						preSaleList.add(preSale);
						break;
					}						
				}
			}
		}
	
		return preSaleList;
	}
}