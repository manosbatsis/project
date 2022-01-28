package com.topideal.service.pushback.sale.impl;

import java.util.HashMap;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.dao.sale.ShelfDao;
import com.topideal.entity.vo.sale.ShelfModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.SaleReturnIdepotDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.dao.sale.SaleShelfIdepotDao;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;
import com.topideal.entity.vo.sale.SaleShelfIdepotModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.XSShelfInDepotPushBackService;
import com.topideal.service.pushback.sale.XSTConsumerInStoragePushBackService;

import net.sf.json.JSONObject;
/**
 * 销售上架入库库存加减成功回推
 * @author wenyan
 *
 */
@Service
public class XSShelfInDepotPushBackServiceImpl implements XSShelfInDepotPushBackService{
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(XSShelfInDepotPushBackServiceImpl.class);
	@Autowired
	private SaleShelfIdepotDao saleShelfIdepotDao;// 销售上架入库
	@Autowired
	private ShelfDao shelfDao;   // 销售商家
	
	// 销售退入库
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201156300,model=DERP_LOG_POINT.POINT_13201156300_Label,keyword="code")
	public boolean updateXSShelfInDepotPushPushBack(String json,String keys,String topics,String tags) throws Exception {
		// 将字符串转成json 
		Thread.sleep(50);
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");	// 上架入库单号
		// 上架入库单
		SaleShelfIdepotModel saleShelfIdepotModel = new SaleShelfIdepotModel();
		saleShelfIdepotModel.setCode(code);
		saleShelfIdepotModel = saleShelfIdepotDao.searchByModel(saleShelfIdepotModel);
		if (saleShelfIdepotModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售上架入库单,上架入库单号:code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售上架入库单,上架入库单号:code" + code);
		}	
		// 修改状态
		SaleShelfIdepotModel model = new  SaleShelfIdepotModel();
		model.setState(DERP_ORDER.SALESHELFIDEPOT_STATUS_012); // 012-已入仓
		model.setId(saleShelfIdepotModel.getId());
		saleShelfIdepotDao.modify(model);

		ShelfModel shelfModel = new ShelfModel();
		shelfModel.setState(DERP_ORDER.SHELF_STATUS_2);   // 2-已入库
		shelfModel.setId(saleShelfIdepotModel.getSaleShelfId());
		shelfDao.modify(shelfModel);
		return true;
	}
}
