package com.topideal.order.service.purchase.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.purchase.DeclareOrderItemDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.dao.purchase.PurchaseWarehouseItemDao;
import com.topideal.dao.purchase.TallyingOrderItemDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.dao.sale.SaleOutDepotItemDao;
import com.topideal.dao.sale.SaleReturnIdepotItemDao;
import com.topideal.dao.sale.SaleReturnOdepotItemDao;
import com.topideal.dao.sale.SaleReturnOrderItemDao;
import com.topideal.dao.transfer.TransferInDepotItemDao;
import com.topideal.dao.transfer.TransferOrderItemDao;
import com.topideal.dao.transfer.TransferOutDepotItemDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.order.service.purchase.CheckGoodsIsUseService;

/**
 * 校验商品是否被使用
 * @author zhanghx
 */
@Service
public class CheckGoodsIsUseServiceImpl implements CheckGoodsIsUseService {

	// 商品
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	
	// 采购订单
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	// 预申报单
	@Autowired
	private DeclareOrderItemDao declareOrderItemDao;
	// 理货单
	@Autowired
	private TallyingOrderItemDao tallyingOrderItemDao;
	// 入库单
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
	// 电商订单
	@Autowired
	private OrderItemDao orderItemDao;
	// 销售订单
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	// 销售出库订单
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 销售退订单
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;
	// 销售退出库订单
	@Autowired
	private SaleReturnOdepotItemDao saleReturnOdepotItemDao;
	// 销售退入库订单
	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;
	// 调拨订单
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;
	// 调拨出
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;
	// 调拨入
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao;

	/**
	 * 校验商品是否被使用
	 * 
	 * @return
	 */
	@Override
	public boolean checkGoodsIsUse(List<Long> ids) throws SQLException, RuntimeException {
		int flag = 0;
		Long id = null;
		for (Long goodsId : ids) {
			// 采购订单
			Integer purchaseOrderItemCount = purchaseOrderItemDao.checkGoodsIsUse(goodsId);
			if (purchaseOrderItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			// 预申报单
			Integer declareOrderItemCount = declareOrderItemDao.checkGoodsIsUse(goodsId);
			if (declareOrderItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			// 理货单
			Integer tallyingOrderItemCount = tallyingOrderItemDao.checkGoodsIsUse(goodsId);
			if (tallyingOrderItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			// 入库单
			Integer purchaseWarehouseItemCount = purchaseWarehouseItemDao.checkGoodsIsUse(goodsId);
			if (purchaseWarehouseItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			// 电商订单
			Integer orderItemCount = orderItemDao.checkGoodsIsUse(goodsId);
			if (orderItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			// 销售订单
			Integer saleOrderItemCount = saleOrderItemDao.checkGoodsIsUse(goodsId);
			if (saleOrderItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			//销售出库订单
			Integer saleOutDepotItemCount = saleOutDepotItemDao.checkGoodsIsUse(goodsId);
			if (saleOutDepotItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			//销售退订单
			Integer saleReturnOrderItemCount = saleReturnOrderItemDao.checkGoodsIsUse(goodsId);
			if (saleReturnOrderItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			//销售退出库订单
			Integer saleReturnOdepotItemCount = saleReturnOdepotItemDao.checkGoodsIsUse(goodsId);
			if (saleReturnOdepotItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			//销售退入库订单
			Integer saleReturnIdepotItemCount = saleReturnIdepotItemDao.checkGoodsIsUse(goodsId);
			if (saleReturnIdepotItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			//调拨订单
			Integer transferOrderItemCount = transferOrderItemDao.checkGoodsIsUse(goodsId);
			if (transferOrderItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			//调拨出
			Integer transferOutDepotItemCount = transferOutDepotItemDao.checkGoodsIsUse(goodsId);
			if (transferOutDepotItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			//调拨入
			Integer transferInDepotItemCount = transferInDepotItemDao.checkGoodsIsUse(goodsId);
			if (transferInDepotItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
		}
		if (flag == 1) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", id);
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);
			String goodsNo = "";
			if(mogo!=null){
				goodsNo = mogo.getGoodsNo();
			}
			throw new RuntimeException(goodsNo+"商品已被使用，不能删除");
		}
		return true;
	}

}
