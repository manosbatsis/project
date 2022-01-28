package com.topideal.common.tools;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.topideal.common.tools.ApolloUtil.updateOrderAttribute;

/**
 * 从apollo注入配置管理类-配置
 * 
 */
@Component
public class ApolloUtilDB {
	
	private static final Logger logger = LoggerFactory.getLogger(ApolloUtilDB.class);
	
	/**jdbc驱动*/
	public static String jdbforName="com.mysql.jdbc.Driver";
	
	/**爬虫================================================*/
    /**爬虫库url*/
	public static String crawlerUrl;
	/**爬虫库username*/
	public static String crawlerUserName;
	/**爬虫库Password*/
	public static String crawlerPassword;
	
	/**库存库url*/
	public static String crInventoryUrl;
	/**库存库username*/
	public static String crInventoryUserName;
	/**库存库Password*/
	public static String crInventoryPassword;
	

	/**数据同步================================================*/
	/**报表库url*/
	public static String reportUrl;
	/**报表库username*/
	public static String reportUserName;
	/**报表库Password*/
	public static String reportPassword;
	/**报表库Database*/
	public static String reportDatabase;
	
	
	 /**库存库url*/
	public static String inventoryUrl;
	/**库存库username*/
	public static String inventoryUserName;
	/**库存库Password*/
	public static String inventoryPassword;
	/**库存库Database*/
	public static String inventoryDatabase;
	
	 /**业务库url*/
	public static String orderUrl;
	/**业务库username*/
	public static String orderUserName;
	/**业务库Password*/
	public static String orderPassword;
	/**业务库Database*/
	public static String orderDatabase;
	
	 /**仓储库url*/
	public static String storageUrl;
	/**仓储库username*/
	public static String storageUserName;
	/**仓储库Password*/
	public static String storagePassword;
	/**仓储库Database*/
	public static String storageDatabase;
	
	 /**主服务库url*/
	public static String systemUrl;
	/**主服务库username*/
	public static String systemUserName;
	/**主服务库Password*/
	public static String systemPassword;
	/**主服务库Database*/
	public static String systemDatabase;
	
	/**库存库需要同步的表*/
	public static String synInventoryTable;
	/**库存库需要同步的大数据表*/
	public static String synInventoryBigTable;
	/**库存库需要单独同步的表*/
	public static String synInventoryIndependentTable;
	/**业务库需要同步的表*/
	public static String synOrderTable;
	/**业务库需要同步的大数据表*/
	public static String synOrderBigTable;
	/**仓储库需要同步的表*/
	public static String synStorageTable;
	/**主服务库需要同步的表*/
	public static String synSystemTable;
	
	/**报表库web存放生成excel目录*/
	public static String reportBasepath;

	/**业务发票文件存放目录*/
	public static String orderBasepath;
	
	/**#主服务商品图片压缩包存放目录*/
	public static String systemBasepath;

	 /**采购计划报表url*/
	public static String scmUrl;
	/**采购计划报表username*/
	public static String scmUserName;
	/**采购计划报表Password*/
	public static String scmPassword;

	/**canal实时同步组件配置*/
	/**canal服务端ip*/
	public static String canalHost;
	/**canal监听指定库表*/
	public static String canalSubscribe;
	/**
	 * 只需要配置set方法
	 * */
	/**爬虫功能================================================*/
	@Value("${jdbccrawler.url:}")
	public void setCrawlerUrl(String crawlerUrl) {
		ApolloUtilDB.crawlerUrl = crawlerUrl;
	}
	@Value("${jdbccrawler.username:}")
	public void setCrawlerUserName(String crawlerUserName) {
		ApolloUtilDB.crawlerUserName = crawlerUserName;
	}
	@Value("${jdbccrawler.password:}")
	public void setCrawlerPassword(String crawlerPassword) {
		ApolloUtilDB.crawlerPassword = crawlerPassword;
	}
	@Value("${crinventory.url:}")
	public void setCrInventoryUrl(String crInventoryUrl) {
		ApolloUtilDB.crInventoryUrl = crInventoryUrl;
	}
	@Value("${crinventory.username:}")
	public void setCrInventoryUserName(String crInventoryUserName) {
		ApolloUtilDB.crInventoryUserName = crInventoryUserName;
	}
	@Value("${crinventory.password:}")
	public void setCrInventoryPassword(String crInventoryPassword) {
		ApolloUtilDB.crInventoryPassword = crInventoryPassword;
	}
	
	/**数据同步================================================*/
	@Value("${synreport.url:}")
	public void setReportUrl(String reportUrl) {
		ApolloUtilDB.reportUrl = reportUrl;
	}
	@Value("${synreport.username:}")
	public void setReportUserName(String reportUserName) {
		ApolloUtilDB.reportUserName = reportUserName;
	}
	@Value("${synreport.password:}")
	public void setReportPassword(String reportPassword) {
		ApolloUtilDB.reportPassword = reportPassword;
	}
	@Value("${synreport.database:}")
	public void setReportDatabase(String reportDatabase) {
		ApolloUtilDB.reportDatabase = reportDatabase;
	}
	@Value("${syninventory.url:}")
	public void setInventoryUrl(String inventoryUrl) {
		ApolloUtilDB.inventoryUrl = inventoryUrl;
	}
	@Value("${syninventory.username:}")
	public void setInventoryUserName(String inventoryUserName) {
		ApolloUtilDB.inventoryUserName = inventoryUserName;
	}
	@Value("${syninventory.password:}")
	public void setInventoryPassword(String inventoryPassword) {
		ApolloUtilDB.inventoryPassword = inventoryPassword;
	}
	@Value("${syninventory.database:}")
	public void setInventoryDatabase(String inventoryDatabase) {
		ApolloUtilDB.inventoryDatabase = inventoryDatabase;
	}
	
	@Value("${synorder.url:}")
	public void setOrderUrl(String orderUrl) {
		ApolloUtilDB.orderUrl = orderUrl;
	}
	@Value("${synorder.username:}")
	public void setOrderUserName(String orderUserName) {
		ApolloUtilDB.orderUserName = orderUserName;
	}
	@Value("${synorder.password:}")
	public void setOrderPassword(String orderPassword) {
		ApolloUtilDB.orderPassword = orderPassword;
	}
	@Value("${synorder.database:}")
	public void setOrderDatabase(String orderDatabase) {
		ApolloUtilDB.orderDatabase = orderDatabase;
	}
	
	@Value("${synstorage.url:}")
	public void setStorageUrl(String storageUrl) {
		ApolloUtilDB.storageUrl = storageUrl;
	}
	@Value("${synstorage.username:}")
	public void setStorageUserName(String storageUserName) {
		ApolloUtilDB.storageUserName = storageUserName;
	}
	@Value("${synstorage.password:}")
	public void setStoragePassword(String storagePassword) {
		ApolloUtilDB.storagePassword = storagePassword;
	}
	@Value("${synstorage.database:}")
	public void setStorageDatabase(String storageDatabase) {
		ApolloUtilDB.storageDatabase = storageDatabase;
	}
	
	@Value("${synsystem.url:}")
	public void setSystemUrl(String systemUrl) {
		ApolloUtilDB.systemUrl = systemUrl;
	}
	@Value("${synsystem.username:}")
	public void setSystemUserName(String systemUserName) {
		ApolloUtilDB.systemUserName = systemUserName;
	}
	@Value("${synsystem.password:}")
	public void setSystemPassword(String systemPassword) {
		ApolloUtilDB.systemPassword = systemPassword;
	}
	@Value("${synsystem.database:}")
	public void setSystemDatabase(String systemDatabase) {
		ApolloUtilDB.systemDatabase = systemDatabase;
	}

	@Value("${syninventory_table:}")
	public void setSynInventoryTable(String synInventoryTable) {
		ApolloUtilDB.synInventoryTable = synInventoryTable;
	}
	@Value("${syninventory_big_table:}")
	public void setSynInventoryBigTable(String synInventoryBigTable) {
		ApolloUtilDB.synInventoryBigTable = synInventoryBigTable;
	}
	@Value("${syninventory_independent_table:}")
	public void setSynInventoryIndependentTable(String synInventoryIndependentTable) {
		ApolloUtilDB.synInventoryIndependentTable = synInventoryIndependentTable;
	}
	@Value("${synorder_table:}")
	public  void setSynOrderTable(String synOrderTable) {
		ApolloUtilDB.synOrderTable = synOrderTable;
	}
	@Value("${synorder_big_table:}")
	public void setSynOrderBigTable(String synOrderBigTable) {
		ApolloUtilDB.synOrderBigTable = synOrderBigTable;
	}
	@Value("${synstorage_table:}")
	public void setSynStorageTable(String synStorageTable) {
		ApolloUtilDB.synStorageTable = synStorageTable;
	}
	@Value("${synsystem_table:}")
	public void setSynSystemTable(String synSystemTable) {
		ApolloUtilDB.synSystemTable = synSystemTable;
	}
	@Value("${report.basepath:}")
	public void setReportBasepath(String reportBasepath) {
		ApolloUtilDB.reportBasepath = reportBasepath;
	}
	@Value("${order.basepath:}")
	public void setOrderBasepath(String orderBasepath) {
		ApolloUtilDB.orderBasepath = orderBasepath;
	}
	@Value("${system.basepath:}")
	public void setSystemBasepath(String systemBasepath) {
		ApolloUtilDB.systemBasepath = systemBasepath;
	}

	@Value("${jdbcscm.url:}")
	public void setScmUrl(String scmUrl) {
		ApolloUtilDB.scmUrl = scmUrl;
	}
	@Value("${jdbcscm.username:}")
	public void setScmUserName(String scmUserName) {
		ApolloUtilDB.scmUserName = scmUserName;
	}
	@Value("${jdbcscm.password:}")
	public void setScmPassword(String scmPassword) {
		ApolloUtilDB.scmPassword = scmPassword;
	}

	@Value("${syncanal.host:}")
	public void setCanalHost(String canalHost) {
		ApolloUtilDB.canalHost = canalHost;
	}
	@Value("${syncanal.subscribe:}")
	public void setCanalSubscribe(String canalSubscribe) {
		ApolloUtilDB.canalSubscribe = canalSubscribe;
	}
	/**xxxx================================================*/
	/**
	 * 初始化后打印加载配置
	 * */
	@PostConstruct
	public static void loadLog(){
		logger.info("【Apollo配置加载DB】");
		logger.info("爬虫===============");
		logger.info("crawlerUrl="+crawlerUrl);
		logger.info("crawlerUserName="+crawlerUserName);
		logger.info("crawlerPassword="+(StringUtils.isNotBlank(crawlerPassword)?"***":""));
		logger.info("crInventoryUrl="+crInventoryUrl);
		logger.info("crInventoryUserName="+crInventoryUserName);
		logger.info("crInventoryPassword="+(StringUtils.isNotBlank(crInventoryPassword)?"***":""));
		logger.info("数据同步===============");
		logger.info("reportUrl="+reportUrl);
		logger.info("reportUserName="+reportUserName);
		logger.info("reportPassword="+(StringUtils.isNotBlank(reportPassword)?"***":""));
		logger.info("inventoryUrl="+inventoryUrl);
		logger.info("inventoryUserName="+inventoryUserName);
		logger.info("inventoryPassword="+(StringUtils.isNotBlank(inventoryPassword)?"***":""));
		logger.info("orderUrl="+orderUrl);
		logger.info("orderUserName="+orderUserName);
		logger.info("orderPassword="+(StringUtils.isNotBlank(orderPassword)?"***":""));
		logger.info("storageUrl="+storageUrl);
		logger.info("storageUserName="+storageUserName);
		logger.info("storagePassword="+(StringUtils.isNotBlank(storagePassword)?"***":""));
		logger.info("systemUrl="+systemUrl);
		logger.info("systemUserName="+systemUserName);
		logger.info("systemPassword="+(StringUtils.isNotBlank(systemPassword)?"***":""));

		logger.info("synInventoryTable="+synInventoryTable);
		logger.info("synInventoryBigTable="+synInventoryBigTable);
		logger.info("synInventoryIndependentTable="+synInventoryIndependentTable);
		logger.info("synOrderTable="+synOrderTable);
		logger.info("synOrderBigTable="+synOrderBigTable);
		logger.info("synStorageTable="+synStorageTable);
		logger.info("synSystemTable="+synSystemTable);
		logger.info("reportBasepath="+reportBasepath);
		logger.info("orderBasepath="+orderBasepath);
		
		logger.info("采购计划报表===============");
		logger.info("scmUrl="+scmUrl);
		logger.info("scmUserName="+scmUserName);
		logger.info("scmPassword="+(StringUtils.isNotBlank(scmPassword)?"***":""));
	}

	/**xxxx================================================*/

}
