package com.topideal.order.service.sale.impl;

import com.topideal.common.system.auth.User;
import com.topideal.dao.sale.SaleOutDepotDao;
import com.topideal.dao.sale.SaleShelfIdepotDao;
import com.topideal.dao.sale.SaleShelfIdepotItemDao;
import com.topideal.entity.dto.sale.SaleShelfIdepotDTO;
import com.topideal.entity.dto.sale.SaleShelfIdepotItemDTO;
import com.topideal.entity.vo.sale.SaleOutDepotModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.sale.SaleShelfIdepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SaleShelfIdepotServiceImpl implements SaleShelfIdepotService{

	@Autowired
	SaleShelfIdepotDao saleShelfIdepotDao ;

	@Autowired
	SaleShelfIdepotItemDao saleShelfIdepotItemDao ;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;

	@Override
	public SaleShelfIdepotDTO listSaleShelfIdepot(SaleShelfIdepotDTO dto, User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return saleShelfIdepotDao.getListByPage(dto);
	}

	@Override
	public SaleShelfIdepotDTO searchDetail(Long id) throws SQLException{
		SaleShelfIdepotDTO dto = saleShelfIdepotDao.searchDTOById(id);
		if(dto.getSaleOutDepotId() != null) {
			SaleOutDepotModel outModel = saleOutDepotDao.searchById(dto.getSaleOutDepotId());
			dto.setSaleDeclareOrderCode(outModel.getSaleDeclareOrderCode());
		}
		return dto;
	}

	@Override
	public List<SaleShelfIdepotItemDTO> listItemBySaleShelfIdepotId(Long id) throws SQLException {

		SaleShelfIdepotItemDTO itemDTO = new SaleShelfIdepotItemDTO() ;
		itemDTO.setSshelfIdepotId(id);

		return saleShelfIdepotItemDao.listDTO(itemDTO);
	}

	@Override
	public Integer getOrderCount(SaleShelfIdepotDTO dto, User user) throws SQLException{
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				return 0;
			}
			dto.setBuList(buList);
		}
		return saleShelfIdepotDao.getOrderCount(dto);
	}

	@Override
	public List<Map<String, Object>> getExportList(SaleShelfIdepotDTO dto, User user) {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return new ArrayList<>();
			}
			dto.setBuList(buIds);
		}
		return saleShelfIdepotDao.getExportList(dto);
	}

}
