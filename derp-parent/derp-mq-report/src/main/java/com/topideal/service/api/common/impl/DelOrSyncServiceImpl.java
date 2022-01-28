package com.topideal.service.api.common.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.enums.MQReportEnum;
import com.topideal.dao.common.UpdateGoodsInfoDao;
import com.topideal.service.DelMonthlyAccountItemService;
import com.topideal.service.DelReportDataService;
import com.topideal.service.SysDataService;
import com.topideal.service.api.common.DelOrSyncService;
/**
 * 修改商品信息
 */
@Service
public class DelOrSyncServiceImpl implements DelOrSyncService{
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(DelOrSyncServiceImpl.class);
	

	@Autowired
	private SysDataService sysDataService;//同步数据
	@Autowired
	private DelReportDataService delReportDataService;// 删除报表库数据
	@Autowired
	private DelMonthlyAccountItemService delMonthlyAccountItemService;// 删除月结明细

	/**
	 * 删除/同步数据 分发方法
	 */
	@Override
	public boolean getDelOrSync(String json, String keys, String topics, String tags) throws Exception {
		//锁住代码块
		synchronized (this) {
			// 同步数据方法
			if (MQReportEnum.SYS_DATA.getTags().equals(tags)) {
				LOGGER.info("==============执行report同步数据 分发方法开始==================");
				sysDataService.synsData(json, keys, topics, tags);
				LOGGER.info("==============执行report同步数据 分发方法结束==================");
				Thread.sleep(4000);
			}else if (MQReportEnum.DEL_REPORT_DATAS.getTags().equals(tags)) {// 删除报表库数据方法
				LOGGER.info("==============执行report删除数据 分发方法开始==================");
				delReportDataService.delReportData(json, keys, topics, tags);
				LOGGER.info("==============执行report删除数据 分发方法结束==================");
			}else if (MQReportEnum.DEL_MONTHLY_ACCOUNT_ITEM.getTags().equals(tags)) {// 删月结表体方法
				LOGGER.info("==============执行report删除月结明细数据 分发方法开始==================");
				//delMonthlyAccountItemService.delMonthlyAccountItemData(json, keys, topics, tags);
				LOGGER.info("==============执行report删除月结明细数据 分发方法结束==================");
			}

		}
		return true;
	}
}
