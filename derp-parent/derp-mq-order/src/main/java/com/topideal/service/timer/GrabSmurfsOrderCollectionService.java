package com.topideal.service.timer;

import java.sql.SQLException;

import net.sf.json.JSONObject;


/**
 * 抓取蓝精灵已经发货和已完成订单
 * @author 杨创
 *2019/03/11
 */
public interface GrabSmurfsOrderCollectionService {
	/**
	 * 分页抓取蓝精灵已经发货和已完成订单
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	//public boolean getGrabSmurfsOrderCollection (String json,String keys,String topics,String tags) throws Exception;	
	/**
	 * 订单抓取数据过滤
	 * @param result
	 * @return
	 * @throws Exception
	 */
	//public void datafilterGrabSmurfsOrderCollection(String result) throws Exception;
	
	/**
	 * 向数据库插入蓝精灵发货数据数据
	 * @param listError 
	 * @param listOk
	 * @param listExternalCode
	 * @return
	 * @throws Exception
	 */
	//public void insertGrabSmurfsOrderCollection(List <JSONObject> listError,List <JSONObject> listOk,List <String> listExternalCode, List<JSONObject> listNull)throws Exception;
	
	/**
	 * 单个插入
	 * @param orderObject
	 * @param merchantInfoMongo
	 * @param depotInfoMongo
	 * @param s
	 * @param isEnforce 是否强制抓单 1-是 0-否
	 * @throws Exception
	 */
	public void insertGrabSmurfsOrderCollection(String json,String keys,String topics,String tags)throws Exception;

	/**
	 * @param jsonError
	 * @throws Exception
	 */
	public void grabSmurfsOrderCollectionError(JSONObject jsonError,JSONObject json,String orderNo);
	
	/**
	 * 单个抓取日志
	 * @param jsonError
	 * @param json
	 * @param orderNo
	 */
	public void getOneSmurfsOrderCollectionError(JSONObject jsonError,JSONObject json,String orderNo);

	
	/**
	 * 查询订单是否存在
	 * @param orderNo 订单单号
	 * @return
	 */
	public boolean isExistOrderNo(String orderNo) throws SQLException;
}
