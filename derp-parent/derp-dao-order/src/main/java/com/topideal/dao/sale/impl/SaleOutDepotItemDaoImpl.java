package com.topideal.dao.sale.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleOutDepotItemDao;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
import com.topideal.entity.vo.sale.SaleOutDepotItemModel;
import com.topideal.mapper.sale.SaleOutDepotItemMapper;

/**
 * 销售出库表体 impl
 * 
 * @author lchenxing
 */
@Repository
public class SaleOutDepotItemDaoImpl implements SaleOutDepotItemDao {

	@Autowired
	private SaleOutDepotItemMapper mapper; // 销售出库表体

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<SaleOutDepotItemModel> list(SaleOutDepotItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param SaleOutDepotItemModel
	 */
	@Override
	public Long save(SaleOutDepotItemModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param List
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param List
	 */
	@Override
	public int modify(SaleOutDepotItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param SaleOutDepotItemModel
	 */
	@Override
	public SaleOutDepotItemModel searchByPage(SaleOutDepotItemModel model) throws SQLException {
		PageDataModel<SaleOutDepotItemModel> pageModel = mapper.listByPage(model);
		return (SaleOutDepotItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public SaleOutDepotItemModel searchById(Long id) throws SQLException {
		SaleOutDepotItemModel model = new SaleOutDepotItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public SaleOutDepotItemModel searchByModel(SaleOutDepotItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	@Override
	public Integer checkGoodsIsUse(Long id) {
		return mapper.checkGoodsIsUse(id);
	}

	@Override
	public List<SaleOutDepotItemDTO> listItemDTO(SaleOutDepotItemDTO dto) throws SQLException {
		return mapper.listItemDTO(dto);
	}

	@Override
	public List<SaleOutDepotItemDTO> listItemByOverdueDate(SaleOutDepotItemDTO dto) throws SQLException {
		return mapper.listItemByOverdueDate(dto);
	}
	/**生成上架入库-查询出库单商品剩余可分配余量按失效日期升序*/
	@Override
	public List<Map<String,Object>> listCountSurplusNum(Map<String,Object> map){
		List<Map<String,Object>> outDepotItemList = mapper.listCountSurplusNum(map);
		if(outDepotItemList!=null&&outDepotItemList.size()>0){
			for(Map<String,Object> outDepotItem : outDepotItemList){
				BigDecimal outnum = (BigDecimal)outDepotItem.get("outnum");//可分配出库余量
				outDepotItem.put("outnum",outnum.intValue());
			}
		}
		return outDepotItemList;
	}
	/**
	 * 分组查询销售出库单商品id
	 */
	@Override
	public List<Map<String, Object>> getItemGroupByOutId(Long saleOutDepotId) throws SQLException {		
		return mapper.getItemGroupByOutId(saleOutDepotId);
	}

}
