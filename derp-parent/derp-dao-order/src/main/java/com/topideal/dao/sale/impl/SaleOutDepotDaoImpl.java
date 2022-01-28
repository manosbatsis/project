package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleOutDepotDao;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
import com.topideal.entity.vo.sale.SaleOutDepotModel;
import com.topideal.mapper.sale.SaleOutDepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *销售出库 impl
 * @author lchenxing
 */
@Repository
public class SaleOutDepotDaoImpl implements SaleOutDepotDao {

    @Autowired
    private SaleOutDepotMapper mapper;  //销售出库表体

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleOutDepotModel> list(SaleOutDepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 */
    @Override
    public Long save(SaleOutDepotModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }

	/**
     * 删除
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }

	/**
     * 修改
     */
    @Override
    public int modify(SaleOutDepotModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }

	/**
     * 分页查询
     */
    @Override
    public SaleOutDepotModel  searchByPage(SaleOutDepotModel  model) throws SQLException{
        PageDataModel<SaleOutDepotModel> pageModel=mapper.listByPage(model);
        return (SaleOutDepotModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     */
    @Override
    public SaleOutDepotModel  searchById(Long id)throws SQLException {
        SaleOutDepotModel  model=new SaleOutDepotModel ();
        model.setId(id);
        return mapper.get(model);
    }

     /**
     * 根据商家实体类查询商品
     * */
	@Override
	public SaleOutDepotModel searchByModel(SaleOutDepotModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据条件查询（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public SaleOutDepotModel queryListByPage(SaleOutDepotModel model) throws SQLException {
		PageDataModel<SaleOutDepotModel> pageModel=mapper.queryListByPage(model);
        return (SaleOutDepotModel ) pageModel.getModel();
	}
	/**
	 * 获取销售订单的商品的累计出库数量
	 * @param saleOrderId
	 * @param goodsId
	 * @return
	 */
	@Override
	public Integer getTotalNumByOrderGoods(Map<String, Object> paramMap) {
		return mapper.getTotalNumByOrderGoods(paramMap);
	}
	/**
	 * 根据条件获取销售出库清单
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SaleOutDepotModel> queryList(SaleOutDepotModel model) throws SQLException {
		return mapper.queryList(model);
	}
	/**
	 * 根据条件获取出库明细（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public SaleOutDepotModel queryDetailsListByPage(SaleOutDepotModel model) throws SQLException {
		PageDataModel<SaleOutDepotModel> pageModel= mapper.queryDetailsListByPage(model);
		return (SaleOutDepotModel ) pageModel.getModel();
	}
	/**
	 * 根据条件获取出库明细
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SaleOutDepotModel> queryDetailsList(SaleOutDepotModel model) throws SQLException {
		return mapper.queryDetailsList(model);
	}
	@Override
	public Integer getOutDepotCount(Map<String, Object> map) {
		return mapper.getOutDepotCount(map);
	}
	@Override
	public int getImportOrderCountByIds(List<Long> ids) throws Exception {
		return mapper.getImportOrderCountByIds(ids) ;
	}
	@Override
	public Integer getTransferNum(Map<String, Object> map) {
		return mapper.getTransferNum(map);
	}
	@Override
	public SaleOutDepotDTO searchDtoById(Long id) throws SQLException {
		SaleOutDepotDTO  dto=new SaleOutDepotDTO ();
		dto.setId(id);
		 return mapper.searchDtoById(dto);
	}
	/**
	 * 根据条件查询（分页）
	 * @throws SQLException
	 */
	@Override
	public SaleOutDepotDTO queryDTOListByPage(SaleOutDepotDTO dto) throws SQLException {
		PageDataModel<SaleOutDepotDTO> pageModel=mapper.queryDTOListByPage(dto);
        return (SaleOutDepotDTO ) pageModel.getModel();
	}
	/**
	 * 根据条件获取销售出库清单
	 * @throws SQLException
	 */
	@Override
	public List<SaleOutDepotDTO> queryDTOList(SaleOutDepotDTO dto) throws SQLException {
		return mapper.queryDTOList(dto);
	}
	/**
	 * 根据条件获取出库明细（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public SaleOutDepotDTO queryDTODetailsListByPage(SaleOutDepotDTO dto) throws SQLException {
		PageDataModel<SaleOutDepotDTO> pageModel= mapper.queryDTODetailsListByPage(dto);
		return (SaleOutDepotDTO ) pageModel.getModel();
	}
	/**
	 * 根据条件获取出库明细
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SaleOutDepotDTO> queryDTODetailsList(SaleOutDepotDTO dto) throws SQLException {
		return mapper.queryDTODetailsList(dto);
	}
	@Override
	public List<SaleOutDepotModel> listSaleOutNoShelfRecord(Map<String, Object> map) throws SQLException {
		return mapper.listSaleOutNoShelfRecord(map);
	}
	@Override
	public int modifyBySaleOrderId(SaleOutDepotModel saleOutDepotModel) {
		return mapper.modifyBySaleOrderId(saleOutDepotModel);
	}

	@Override
	public List<SaleOutDepotItemDTO> getSaleOutDepotCount(Map<String,Object> map) {
		return mapper.getSaleOutDepotCount(map);
	}
	/**根据销售单号统计出库单商品未上架量*/
	@Override
	public List<Map<String,Object>> getGoodsNotShelNumList(Map<String,Object> map){
		return mapper.getGoodsNotShelNumList(map);
	}
	/**根据销售单号查询销售数量-出库量 按商品分组统计未出库量*/
	public List<Map<String,Object>> getGoodsNotOutNumList(Map<String,Object> map){
		return mapper.getGoodsNotOutNumList(map);
	}

	@Override
	public List<SaleOutDepotDTO> listSaleOutDepot(SaleOutDepotDTO dto) throws SQLException {
		return mapper.listSaleOutDepot(dto);
	}
}
