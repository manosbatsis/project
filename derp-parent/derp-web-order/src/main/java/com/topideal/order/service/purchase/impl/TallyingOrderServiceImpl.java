package com.topideal.order.service.purchase.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.TallyingOrderDao;
import com.topideal.entity.dto.purchase.TallyingOrderDTO;
import com.topideal.entity.dto.sale.PendingConfirmTallyingOrderVo;
import com.topideal.entity.vo.purchase.TallyingOrderItemModel;
import com.topideal.entity.vo.purchase.TallyingOrderModel;
import com.topideal.json.pushapi.epass.e02.TallyConfirmRootJson;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.purchase.TallyingOrderService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 理货单service实现类
 */
@Service
public class TallyingOrderServiceImpl implements TallyingOrderService {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory
			.getLogger(TallyingOrderServiceImpl.class);

	// 确认理货dao
	@Autowired
	private TallyingOrderDao tallyingOrderDao;
	// mq
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TallyingOrderDTO listTallyingOrderPage(TallyingOrderDTO dto, User user)
			throws SQLException {
		
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				
				return dto ;
			}
			
			dto.setBuIds(buIds);
		}
		
		return tallyingOrderDao.getDTOListByPage(dto);
	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public TallyingOrderDTO searchDetail(Long id) throws SQLException {
		TallyingOrderDTO dto = new TallyingOrderDTO();
		dto.setId(id);
		return tallyingOrderDao.getDetails(dto);
	}

	/**
	 * 确认/驳回 理货单
	 * @param ids
	 * @param state
	 * @param userInfoModel
	 * */
	@Override
	public String modifyOrderState(String ids, String state, Long userId, String name, String topidealCode) throws SQLException {
		int flag = 0;
		String str = "";
		String[] tall_ids = ids.split(",");
		for (int i = 0; i < tall_ids.length; i++) {
			//根据id获取理货单信息
			TallyingOrderModel model = tallyingOrderDao.searchById(Long.parseLong(tall_ids[i]));
			if(!model.getState().equals(DERP_ORDER.TALLYINGORDER_STATE_009)){
				flag = 1;
				break;
			}
		}
		if(flag == 1){
			return "状态必须为待确认";
		}
		for (int i = 0; i < tall_ids.length; i++) {
			//根据id获取理货单信息
			TallyingOrderModel model = tallyingOrderDao.searchById(Long.parseLong(tall_ids[i]));
			model.setState(state);//设置状态
			TallyConfirmRootJson request = new TallyConfirmRootJson();
			request.setTopideal_code(topidealCode);
			String now = TimeUtils.formatFullTime();
	    	request.setAsn_no(model.getCode()); //理货单号
	    	String status = "";
	    	//确认
	    	if(DERP_ORDER.TALLYINGORDER_STATE_010.equals(model.getState())){
	    		status = "1";
	    	}else{
	    		status = "2";//驳回
	    	}
	    	request.setDirective(status);// 指令值 1:确认 2:驳回
	    	request.setDate(now);//确认时间
			JSONObject jsonObject = JSONObject.fromObject(request);
			jsonObject.put("method",EpassAPIMethodEnum.EPASS_E02_METHOD.getMethod());
			try {
				rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(), MQPushApiEnum.PUSH_EPASS.getTags());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//推送成功的理货单更改状态
		for (int i = 0; i < tall_ids.length; i++) {
			TallyingOrderModel model = new TallyingOrderModel();
			model.setId(Long.parseLong(tall_ids[i]));
			model.setState(state);
			if(DERP_ORDER.TALLYINGORDER_STATE_010.equals(model.getState())){
				model.setConfirmDate(TimeUtils.getNow());
			}
			tallyingOrderDao.modify(model);
		}
		return str;
	}
	
	/**
	 * 调拨确认/驳回 理货单
	 * @param ids 理货单id 多个时用,号分隔
	 * @param state 操作指令 010:确认 004:驳回
	 * @param userInfoModel
	 * */
	@Override
	public String updateTallyConfirmTransfer(String ids, String state,Long userId,String name,String topidealCode) throws SQLException {
		StringBuffer failCode = new StringBuffer();//失败理货单号
	
		String[] tallIds = ids.split(",");
		for(int i = 0; i<tallIds.length;i++){
			//根据id获取理货单信息
			TallyingOrderModel tallyOrder = tallyingOrderDao.searchById(Long.parseLong(tallIds[i]));
			if(tallyOrder==null){
				continue;
			}
			//检查状态
			if(tallyOrder.getState().equals(DERP_ORDER.TALLYINGORDER_STATE_010)
					||tallyOrder.getState().equals(DERP_ORDER.TALLYINGORDER_STATE_004)){
				continue;
			}
			tallyOrder.setState(state);
			tallyOrder.setConfirmDate(TimeUtils.getNow());
			tallyOrder.setConfirmUser(userId);
			tallyOrder.setConfirmUserName(name);
			
			if(state.equals(DERP_ORDER.TALLYINGORDER_STATE_004)){//驳回
				state = "2";
			}else if(state.equals(DERP_ORDER.TALLYINGORDER_STATE_010)){//确认
				state = "1";
			}
			TallyConfirmRootJson rootJson = new TallyConfirmRootJson();
			String now = TimeUtils.formatFullTime();
			rootJson.setAsn_no(tallyOrder.getCode()); //理货单号
			rootJson.setDirective(state);// 指令值 1:确认 2:驳回
			rootJson.setDate(now);//确认时间
	    	JSONObject jsonObject = JSONObject.fromObject(rootJson);
			jsonObject.put("method",EpassAPIMethodEnum.EPASS_E02_METHOD.getMethod());
			jsonObject.put("topideal_code",topidealCode);
			//推送跨境宝消息
			SendResult result = null;
			try {
				result = rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(), MQPushApiEnum.PUSH_EPASS.getTags());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(result == null || "".equals(result)){
				failCode.append(tallyOrder.getCode()+":响应结果为空\n");
				continue;
			}
			if(!result.getSendStatus().name().equals("SEND_OK")){//SEND_OK-成功
				failCode.append(tallyOrder.getCode()+":发送消息失败\n");
				continue;
			}
			//更新状态
			tallyingOrderDao.modify(tallyOrder);
		}
		
		return failCode.toString();
	}

	/**
	 * 采购模块的理货单列表（分页）
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TallyingOrderDTO listPageByPurchase(TallyingOrderDTO dto, User user) throws SQLException {
		
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				
				return dto ;
			}
			
			dto.setBuIds(buIds);
		}
		
		return tallyingOrderDao.getListByPage(dto);
	}

	@Override
	public List<PendingConfirmTallyingOrderVo> getPendingConfirmOrders(Map<String, Object> map) throws SQLException {
		return tallyingOrderDao.getPendingConfirmOrders(map);
	}

	@Override
	public Integer countPendingConfirmOrders(Map<String, Object> map) throws SQLException {
		return tallyingOrderDao.countPendingConfirmOrders(map);
	}

	/**
	 *  获取导出列表
	 */
	@Override
	public List<TallyingOrderDTO> getDetailsByExport(TallyingOrderDTO dto, User user) throws SQLException {
		
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				return new ArrayList<TallyingOrderDTO>();
			}
			
			dto.setBuIds(buIds);
		}
		
		List<TallyingOrderDTO> list = tallyingOrderDao.getListDetails(dto) ;
		
		for (TallyingOrderDTO tallyingOrderDTO : list) {
			String state = tallyingOrderDTO.getState();
			tallyingOrderDTO.setState(DERP_ORDER.getLabelByKey(DERP_ORDER.tallyingOrder_stateList,state));
			List<TallyingOrderItemModel> itemList = tallyingOrderDTO.getItemList();
			
			for (TallyingOrderItemModel item : itemList) {
				
				String tallyingUnit = item.getTallyingUnit();
				if(tallyingUnit == null) {
					tallyingUnit = DERP.UNIT_02 ;
				}
				
				item.setTallyingUnit(DERP.getLabelByKey(DERP.unitList, tallyingUnit));
			}
		}
			
		return list ;
	}

	@Override
	public TallyingOrderDTO searchDTODetail(Long id) {
		TallyingOrderDTO dto = new TallyingOrderDTO();
		dto.setId(id);
		return tallyingOrderDao.getDTODetails(dto);
	}


}
