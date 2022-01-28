package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.JdStocksaleInfoDao;
import com.topideal.dao.sale.PlatformMerchandiseInfoDao;
import com.topideal.entity.vo.sale.JdStocksaleInfoModel;
import com.topideal.entity.vo.sale.PlatformMerchandiseInfoModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.CrawlerJDGoodsAndStocksaleService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CrawlerJDGoodsAndStocksaleServiceImpl implements CrawlerJDGoodsAndStocksaleService {
	private static final Logger logger = LoggerFactory.getLogger(CrawlerJDGoodsAndStocksaleServiceImpl.class);

	@Autowired
	MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	ReptileConfigMongoDao reptileConfigMongoDao;
	@Autowired
	PlatformMerchandiseInfoDao platformMerchandiseInfoDao;
	@Autowired
	JdStocksaleInfoDao jdStocksaleInfoDao;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201149800,model=DERP_LOG_POINT.POINT_13201149800_Label)
	public void synsJDGoodsAndStocksale(String json,String keys,String topics,String tags) throws Exception {
		
		JSONObject jsonData = JSONObject.fromObject(json);
		String loginName = (String)jsonData.get("loginName");

		/**1.查询京东爬虫账号*/
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("platformType", DERP.CRAWLER_TYPE_3);//1-云集  2-唯品 3-京东 4-天猫
		if(StringUtils.isNotBlank(loginName)){
			params.put("loginName", loginName);
		}
		List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
		if(reptileConfigList==null||reptileConfigList.size()<=0){
			logger.info("爬虫配置账号数量为0，结束同步");
		}
		//获取链接
		Class.forName(ApolloUtilDB.jdbforName);
		Connection conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);
		// 关闭事务自动提交
		conn.setAutoCommit(false);
        try{
			/**循环账号同步数据*/
			for(ReptileConfigMongo reptileConfigMongo : reptileConfigList){
				//同步商品
				saveJDGoods(jsonData,reptileConfigMongo,conn);
				//同步每日销量
				savesJDtocksale(jsonData,reptileConfigMongo,conn);
			}
			conn.commit();
		}catch (Exception e){
			conn.rollback();
        	e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(conn!=null){
				conn.close();
				conn = null;
			}
		}

	}
    /**同步京东商品
	 * */
	private void saveJDGoods(JSONObject jsonData,ReptileConfigMongo reptileConfigMongo,Connection conn) throws Exception {
		//有指定时间则按指定时间同步，无则同步昨天的数据
		String startTime = (String)jsonData.get("startTime");
		String endTime = (String)jsonData.get("endTime");
		if(StringUtils.isBlank(startTime)||StringUtils.isBlank(endTime)){
			startTime = TimeUtils.getYesterday()+" 00:00:00";
			endTime = TimeUtils.getYesterday()+" 23:59:59";
		}
		long startIndex = 0;
		long pageSize = 2000;
		Statement pst = null;
		ResultSet rs = null;
		Statement countPst = null;
		ResultSet countRs = null;
		try {
			//统计总行数
			String countSql = "select count(*) as countNum from jd_goods_info " +
					"where userCode='"+reptileConfigMongo.getLoginName()+
					"' and ((createTime>='"+startTime+"' and createTime<='"+endTime+"') or (updateTime>='"+startTime+"' and updateTime<='"+endTime+"'))";
			countPst = conn.createStatement();
			countRs = countPst.executeQuery(countSql);
			long countNum = 0;//总行数
			if(countRs.next()) countNum = countRs.getLong("countNum");

			while(countNum>0&&startIndex<countNum){//-------------start-001
				String sql = "select wareId,name,pid,brand,unit,upc,platform,userCode from jd_goods_info " +
						"where userCode='"+reptileConfigMongo.getLoginName()+
						"' and ((createTime>='"+startTime+"' and createTime<='"+endTime+"') or (updateTime>='"+startTime+"' and updateTime<='"+endTime+"'))"+
						" order by createTime limit "+startIndex+","+pageSize+";";
				System.out.println("sql="+sql);
				pst = conn.createStatement();
				rs = pst.executeQuery(sql);
				while(rs.next()) {//-----------start-002
					PlatformMerchandiseInfoModel jdGoodsInfoModel = new PlatformMerchandiseInfoModel();
					jdGoodsInfoModel.setWareId(rs.getString("wareId"));
					jdGoodsInfoModel.setName(rs.getString("name"));
					jdGoodsInfoModel.setPid(rs.getString("pid"));
					jdGoodsInfoModel.setBrand(rs.getString("brand"));
					jdGoodsInfoModel.setUnit(rs.getString("unit"));
					jdGoodsInfoModel.setUpc(rs.getString("upc"));
					jdGoodsInfoModel.setPlatform(rs.getString("platform"));
					jdGoodsInfoModel.setUserCode(rs.getString("userCode"));
					jdGoodsInfoModel.setMerchantId(reptileConfigMongo.getMerchantId());
					jdGoodsInfoModel.setMerchantName(reptileConfigMongo.getMerchantName());
					jdGoodsInfoModel.setCrawlerType(DERP.CRAWLER_TYPE_3);//爬虫平台类型-京东
					jdGoodsInfoModel.setModifyDate(TimeUtils.getNow());
					//查询商品是否已存在
					PlatformMerchandiseInfoModel jdGoodsInfoTemp = new PlatformMerchandiseInfoModel();
					jdGoodsInfoTemp.setWareId(jdGoodsInfoModel.getWareId());
					jdGoodsInfoTemp.setUserCode(jdGoodsInfoModel.getUserCode());
					jdGoodsInfoTemp = platformMerchandiseInfoDao.searchByModel(jdGoodsInfoTemp);
					if(jdGoodsInfoTemp!=null){
						jdGoodsInfoModel.setId(jdGoodsInfoTemp.getId());
						platformMerchandiseInfoDao.modify(jdGoodsInfoModel);
					}else{
						platformMerchandiseInfoDao.save(jdGoodsInfoModel);
					}
				}//-----------end-002
				startIndex +=pageSize;//下一页
				rs.close();
				pst.close();
			}//-------------end-001
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
				rs = null;
			}
			if(pst!=null) {
				pst.close();
				pst = null;
			}
			if(countRs!=null) {
				countRs.close();
				countRs = null;
			}
			if(countPst!=null) {
				countPst.close();
				countPst = null;
			}

		}
	}
	/**同步京东每日销量-库存
	 * */
	private void savesJDtocksale(JSONObject jsonData,ReptileConfigMongo reptileConfigMongo,Connection conn) throws Exception {
		long pageSize = 2000;//每次拿2000直到获取完截止
		Statement pst = null;
		ResultSet rs = null;
		Statement updatePst = null;
		try {
			while(true){//-------------start-001
				StringBuffer ids = new StringBuffer();//存储id用于更新爬虫库存接收状态

				String sql = "select id,wareId,name,warehouse,volume,stock,orderNum,businessDate,platform,userCode  from jd_stocksale_info" +
						" where hasReceived='0' and userCode='"+reptileConfigMongo.getLoginName()+"'"+
						" order by id limit "+pageSize+";";
				System.out.println("sql="+sql);
				pst = conn.createStatement();
				rs = pst.executeQuery(sql);
				while(rs.next()) {//-----------start-002
					ids.append(rs.getInt("id")+",");
					JdStocksaleInfoModel  jdStocksaleInfo = new JdStocksaleInfoModel();
					jdStocksaleInfo.setWareId(rs.getString("wareId"));
					jdStocksaleInfo.setName(rs.getString("name"));
					jdStocksaleInfo.setWarehouse(rs.getString("warehouse"));
					jdStocksaleInfo.setVolume(rs.getInt("volume"));
					jdStocksaleInfo.setStock(rs.getInt("stock"));
					jdStocksaleInfo.setOrderNum(rs.getInt("orderNum"));
					jdStocksaleInfo.setBusinessDate(rs.getDate("businessDate"));
					jdStocksaleInfo.setPlatform(rs.getString("platform"));
					jdStocksaleInfo.setUserCode(rs.getString("userCode"));
					jdStocksaleInfo.setOldId(rs.getInt("id"));
					jdStocksaleInfo.setMerchantId(reptileConfigMongo.getMerchantId());
					jdStocksaleInfo.setMerchantName(reptileConfigMongo.getMerchantName());
					jdStocksaleInfo.setCustomerId(reptileConfigMongo.getCustomerId());
					jdStocksaleInfo.setCustomerName(reptileConfigMongo.getCustomerName());
					jdStocksaleInfo.setModifyDate(TimeUtils.getNow());

					//查询id是否已存在，存在则跳过
					JdStocksaleInfoModel jdStocksaleTemp = new JdStocksaleInfoModel();
					jdStocksaleTemp.setOldId(jdStocksaleInfo.getOldId());
					jdStocksaleTemp = jdStocksaleInfoDao.searchByModel(jdStocksaleTemp);
					if(jdStocksaleTemp!=null) continue;

					jdStocksaleInfoDao.save(jdStocksaleInfo);
				}//-----------end-002

				if(StringUtils.isBlank(ids)) break;
				//更新爬虫库状态为已接收
				ids = ids.deleteCharAt(ids.length()-1);
				String updateSql = " update jd_stocksale_info set hasReceived='1' where id in("+ids.toString()+")";
				System.out.println("updateSql="+updateSql);
				updatePst = conn.createStatement();
				updatePst.execute(updateSql);
				updatePst.close();
				updatePst = null;

				rs.close();
				pst.close();
				rs = null;
				pst = null;
			}//-------------end-001
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
				rs = null;
			}
			if(pst!=null) {
				pst.close();
				pst = null;
			}
			if(updatePst!=null){
				updatePst.close();
				updatePst = null;
			}
		}
	}
}
