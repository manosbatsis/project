package com.topideal.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.WlbItem;
import com.taobao.api.request.WlbItemQueryRequest;
import com.taobao.api.request.WlbWmsSkuGetRequest;
import com.taobao.api.response.WlbItemQueryResponse;
import com.taobao.api.response.WlbWmsSkuGetResponse;
import com.topideal.api.sync.sy01.SyncGoodsRequest;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.SynsCLGoodsInfoService;
import com.topideal.service.UpdateCommbarcodeBrandService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 维护标准条码中的标准品牌信息——定时器
 * @author 
 */
@Component
public class UpdateCommbarcodeBrandMQ extends ConsumerMQAbstract {

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(UpdateCommbarcodeBrandMQ.class);

	@Autowired
	private UpdateCommbarcodeBrandService updateCommbarcodeBrandService;

	public UpdateCommbarcodeBrandMQ() {
		super.setTags(MQErpEnum.UPDATE_COMMBARCODE_BRAND.getTags());
		super.setTopics(MQErpEnum.UPDATE_COMMBARCODE_BRAND.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============维护标准条码中的标准品牌信息开始json: --==========="+json);
		try {
			updateCommbarcodeBrandService.UpdateCommbarcodeBrand(json, keys, topics, tags);
			LOGGER.info("=============维护标准条码中的标准品牌信息结束: --===========");
		} catch (Exception e) {
			LOGGER.error("维护标准条码中的标准品牌信息异常:{}", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
