package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.order.CrawlerBillDao;
import com.topideal.dao.order.CrawlerInventoryDao;
import com.topideal.dao.reporting.YunjiDailySalesReportDao;
import com.topideal.entity.vo.reporting.YunjiDailySalesReportModel;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.report.service.reporting.YunjiDailySalesReportService;

/**
 * 新云集报表 serviceImpl
 * @author 杨创
 *
 */
@Service
public class YunjiDailySalesReportServiceImpl implements YunjiDailySalesReportService {

	@Autowired
	private YunjiDailySalesReportDao yunjiDailySalesReportDao;
	// 爬虫账单
	@Autowired
	CrawlerBillDao crawlerBillDao;
	//商品
	@Autowired
	MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	CrawlerInventoryDao crawlerInventoryDao;
	
	@Override
	public YunjiDailySalesReportModel listDailySalesReportByPage(YunjiDailySalesReportModel model) throws SQLException {
		YunjiDailySalesReportModel yunJiModel = yunjiDailySalesReportDao.getListByPage(model);

		return yunJiModel;
	}
	
	/**
	 * 云集采销日报导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<YunjiDailySalesReportModel> listDailySalesReportExport(YunjiDailySalesReportModel model) throws SQLException {
		List<YunjiDailySalesReportModel> listModel = yunjiDailySalesReportDao.listDailySalesReportExport(model);
		return listModel;
	}

	@Override
	public int getDailySalesReportCount(YunjiDailySalesReportModel model) throws SQLException {
		return yunjiDailySalesReportDao.getDailySalesReportCount(model);
	}
	
    
	


}
