package com.topideal.service.impl;

import com.topideal.api.wx.WXUtils;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mq.tools.ConncetionUtil;
import com.topideal.service.SynCheckService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

 /**
 *
 * 主服务、业务库、仓储库、库存库>报表库 数据巡检->报表库
 */
@Service
public class SynCheckServiceImpl implements SynCheckService {

	private static final Logger logger = LoggerFactory.getLogger(SynCheckServiceImpl.class);


	/**
	 * 数据巡检->报表库
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_20600000001, model = DERP_LOG_POINT.POINT_20600000001_Label)
	public String synCheck(String json, String keys, String topics, String tags) throws Exception {
        String errortable = "";

		/**主服务库*/
		List<String> tableList = new ArrayList<>();
		tableList.addAll(Arrays.asList(ApolloUtilDB.synSystemTable.split(",")));
		errortable = check(ApolloUtilDB.systemUrl, ApolloUtilDB.systemUserName, ApolloUtilDB.systemPassword, ApolloUtilDB.systemDatabase, tableList);
		if(StringUtils.isNotBlank(errortable)){
			return errortable;
		}
		/**业务库*/
		tableList = new ArrayList<>();
		tableList.addAll(Arrays.asList(ApolloUtilDB.synOrderTable.split(",")));
		tableList.addAll(Arrays.asList(ApolloUtilDB.synOrderBigTable.split(",")));
		errortable = check(ApolloUtilDB.orderUrl, ApolloUtilDB.orderUserName, ApolloUtilDB.orderPassword, ApolloUtilDB.orderDatabase, tableList);
		if(StringUtils.isNotBlank(errortable)){
			return errortable;
		}
		/**库存库*/
		tableList = new ArrayList<>();
		tableList.addAll(Arrays.asList(ApolloUtilDB.synInventoryBigTable.split(",")));
		tableList.addAll(Arrays.asList(ApolloUtilDB.synInventoryIndependentTable.split(",")));
		tableList.addAll(Arrays.asList(ApolloUtilDB.synInventoryTable.split(",")));
		errortable = check(ApolloUtilDB.inventoryUrl, ApolloUtilDB.inventoryUserName, ApolloUtilDB.inventoryPassword, ApolloUtilDB.inventoryDatabase, tableList);
		if(StringUtils.isNotBlank(errortable)){
			return errortable;
		}
		/**仓储库存*/
		tableList = new ArrayList<>();
		tableList.addAll(Arrays.asList(ApolloUtilDB.synStorageTable.split(",")));
		errortable = check(ApolloUtilDB.storageUrl, ApolloUtilDB.storageUserName, ApolloUtilDB.storagePassword, ApolloUtilDB.storageDatabase, tableList);
		if(StringUtils.isNotBlank(errortable)){
			return errortable;
		}
		return errortable;
	}


	private String check(String dbUrl, String dbUserName, String dbPassword, String dbName, List<String> tableList) throws Exception {
		Connection sourceConn = null;
		Connection reportConnection = null;
		Statement sourceStatement = null;
		Statement reportStatement = null;
		ResultSet sourceRS = null;
		ResultSet reportRS = null;
		try{
			if(tableList==null||tableList.size()<=0) return "";
			// 获取来源库数据库连接
			sourceConn = ConncetionUtil.getJDBCConnection(dbUrl, dbUserName, dbPassword, dbName);
			if (sourceConn == null) {
				throw new Exception("jdbc连接失败"+dbName);
			}
			//报表库存连接
			reportConnection = ConncetionUtil.getJDBCConnection(ApolloUtilDB.reportUrl,ApolloUtilDB.reportUserName, ApolloUtilDB.reportPassword,ApolloUtilDB.reportDatabase);
			if (reportConnection == null) {
				throw new Exception("jdbc连接失败,报表库");
			}
			sourceStatement = sourceConn.createStatement();
			reportStatement = reportConnection.createStatement();
			String maxTime = TimeUtils.getNowBeforeMinTime(10);//10分钟前的时间
			String errorMsg = "";
			for(String tableName : tableList){
				String sql = "select count(*) as num from "+tableName+" WHERE create_date<'"+maxTime+"'";
				sourceRS = sourceStatement.executeQuery(sql);
				reportRS = reportStatement.executeQuery(sql);
				sourceRS.next();
				reportRS.next();
				int sourceNum = sourceRS.getInt("num");
				int reportNum = reportRS.getInt("num");
				logger.info(sql);
				logger.info("sourceNum="+sourceNum+" reportNum="+reportNum);
				if(sourceNum!=reportNum){
					errorMsg += tableName+",";
				}

				closeConn(null,null,sourceRS);
				closeConn(null,null,reportRS);
			}

			return errorMsg;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally {
			closeConn(sourceConn,sourceStatement,sourceRS);
			closeConn(reportConnection,reportStatement,reportRS);
		}
	}

	private void closeConn(Connection conn,Statement statement,ResultSet rs){
     try{
			if(conn !=null){
				conn.close();
				conn = null;
			}
			if(statement !=null){
				 statement.close();
				 statement = null;
			}
			if(rs !=null){
				rs.close();
				rs = null;
			}
	 }catch (Exception e){}

	}

}
