package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleShelfDao;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.entity.vo.sale.SaleShelfModel;
import com.topideal.mapper.sale.SaleShelfMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 销售上架信息表 daoImpl
 * @author lian_
 *
 */
@Repository
public class SaleShelfDaoImpl implements SaleShelfDao {

    @Autowired
    private SaleShelfMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleShelfModel> list(SaleShelfModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleShelfModel model) throws SQLException {
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
    public int modify(SaleShelfModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleShelfModel  searchByPage(SaleShelfModel  model) throws SQLException{
        PageDataModel<SaleShelfModel> pageModel=mapper.listByPage(model);
        return (SaleShelfModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleShelfModel  searchById(Long id)throws SQLException {
        SaleShelfModel  model=new SaleShelfModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleShelfModel searchByModel(SaleShelfModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public Timestamp getMaxShelfDate(Long orderId, List<String> poNos) {
		return mapper.getMaxShelfDate(orderId, poNos);
	}
	@Override
	public SaleShelfModel getShelfRecord(SaleShelfModel saleShelfModel) {
		return mapper.getShelfRecord(saleShelfModel);
	}

	@Override
	public Map<String, Object> getShelfInfo(Map<String, Object> map) {
		return mapper.getShelfInfo(map);
	}
	@Override
	public Integer getStayShelNum(SaleShelfModel saleShelfModel) {
		return mapper.getStayShelNum(saleShelfModel);
	}
	/**2.0唯品爬虫:查询本商家、仓库、po号、指定货号中最新上架的货号
	 * */
	@Override
	public SaleShelfModel getNewShelfGoods(Map<String, Object> map){
		return mapper.getNewShelfGoods(map);
	}

	@Override
	public List<Map<String, Object>> listByPoNoAndOrderId(Long orderId) {
		return mapper.listByPoNoAndOrderId(orderId);
	}

	@Override
	public List<SaleShelfModel> listShelfByPoNos(Long orderId, List<String> poNos) {
		return mapper.listShelfByPoNos(orderId, poNos);
	}

	@Override
	public List<SaleShelfDTO> getSaleShelfModelById(Long id) {
		return mapper.getSaleShelfModelById(id);
	}

    @Override
    public List<SaleShelfModel> getShelfDTOByCodeAndBarcode(ShelfDTO shelfDTO) {
		return mapper.getShelfDTOByCodeAndBarcode(shelfDTO);
    }

	@Override
	public List<SaleShelfModel> getSaleShelfModelByGoodsId(Long goodsId) {
		return mapper.getSaleShelfModelByGoodsId(goodsId);
	}

    @Override
    public SaleShelfDTO getTotalShelf(SaleShelfDTO saleShelfDTO) {
		return mapper.getTotalShelf(saleShelfDTO);
    }

    @Override
    public List<SaleShelfDTO> searchDetailByOrderId(SaleShelfModel saleShelfModel) {
        return mapper.searchDetailByOrderId(saleShelfModel);
    }
	@Override
	public List<SaleShelfModel> listOrderByDate(SaleShelfModel saleShelfModel) throws SQLException {
		return mapper.listOrderByDate(saleShelfModel);
	}

	@Override
	public List<SaleShelfModel> getShelfInfoByCode(Map<String, Object> map) {
		return mapper.getShelfInfoByCode(map);
	}

	@Override
	public List<Map<String, Object>> getItemByPoNoAndOrderId(Map<String, Object>  queryMap){
		return mapper.getItemByPoNoAndOrderId(queryMap);
	}
	@Override
	public Map<String, Object> getSaleShelfByPoNoAndGoodsNoAndBarcode(Map<String, Object> queryMap) {
		return mapper.getSaleShelfByPoNoAndGoodsNoAndBarcode(queryMap);
	}
	@Override
	public List<SaleShelfDTO> listDTO(SaleShelfDTO dto) {
		return mapper.listDTO(dto);
	}
	@Override
	public int delItemsByShelfIds(List<Long> shelfIdList) throws SQLException {
		return mapper.delItemsByShelfIds(shelfIdList);
	}
	@Override
	public SaleShelfDTO listDTOByPage(SaleShelfDTO dto){
		PageDataModel<SaleShelfDTO> pageModel=mapper.listDTOByPage(dto);
		return (SaleShelfDTO) pageModel.getModel();
	}

}
