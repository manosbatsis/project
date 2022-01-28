package com.topideal.service.api.common.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.tools.ApolloUtil;
import com.topideal.dao.common.UpdateGoodsInfoDao;
import com.topideal.service.api.common.UpdateGoodsInfoService;

import net.sf.json.JSONObject;
/**
 * 修改商品信息
 */
@Service
public class UpdateGoodsInfoServiceImpl implements UpdateGoodsInfoService{
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateGoodsInfoServiceImpl.class);
	
	@Autowired
    private UpdateGoodsInfoDao updateGoodsInfoDao;
	
	@Override
//	@SystemServiceLog(point="",model="order修改商品信息",keyword="goodsId")
	public boolean updateGoodsInfo(String json,String keys,String topics,String tags) throws Exception {
		LOGGER.info("==============执行storage修改商品信息==================");
		//解析json
		JSONObject jsonObj= JSONObject.fromObject(json);
		//获取配置文件的配置，格式    表名:set字段1=jsonKey1,set字段2=jsonKey2'where字段1=jsonKey3;表名:set字段1=jsonKey1,set字段2=jsonKey2'where字段1=jsonKey3
		String str = ApolloUtil.updateStorageAttribute;
		//组装成sql
		String[] tables=str.split(";");//表名:set字段1=jsonKey1,set字段2=jsonKey2'where字段1=jsonKey3
		for(String table:tables){
			String[] tableArr = table.split(":");
			if(tableArr.length != 2){
				continue;
			}
			String tableName = tableArr[0];//表名
			String updateSQL =" update "+tableName+" set ";
			String tableParam = tableArr[1];//set字段1=jsonKey1,set字段2=jsonKey2'where字段1=jsonKey3
			String[] param = tableParam.split("'");
			if(param.length <= 0){
				continue;
			}
			String sets = param[0];//set字段1=jsonKey1,set字段2=jsonKey2
			String wheres = "";//where字段1=jsonKey3
			if(tableArr.length >= 2){
				wheres = param[1];
			}
			String[] setArr = sets.split(",");
			String whereOfSet = "";
			for(String set:setArr){//set字段1=jsonKey1  set字段2=jsonKey2
				String[] setParam = set.split("=");
				if(setParam.length!=2){
					continue;
				}
				if(jsonObj.get(setParam[1]) == null || StringUtils.isBlank(jsonObj.getString(setParam[1]))){
					continue;
				}
				if(jsonObj.get(setParam[1]) instanceof String){
					updateSQL += setParam[0]+"='"+jsonObj.get(setParam[1])+"',";
					whereOfSet += setParam[0]+"!='"+jsonObj.get(setParam[1])+"' or ";
				}else{
					updateSQL += setParam[0]+"="+jsonObj.get(setParam[1])+",";
					whereOfSet += setParam[0]+"="+jsonObj.get(setParam[1])+" or ";
				}
			}
			if(setArr.length>0){
				updateSQL = updateSQL.substring(0,updateSQL.length()-1);
			}
			if(whereOfSet.length()>0){
				whereOfSet = "("+whereOfSet.substring(0,whereOfSet.length()-4)+")";
			}
			String[] whereArr = wheres.split(",");
			if((whereArr != null && whereArr.length>0) || StringUtils.isNotBlank(whereOfSet)){
				updateSQL += " where ";
			}
			for(String where:whereArr){
				String[] whereParam = where.split("=");
				if(whereParam.length!=2){
					continue;
				}
				if(jsonObj.get(whereParam[1]) == null || StringUtils.isBlank(jsonObj.getString(whereParam[1]))){
					continue;
				}
				if(jsonObj.get(whereParam[1]) instanceof String){
					updateSQL += whereParam[0]+"='"+jsonObj.get(whereParam[1])+"' and ";
				}else{
					updateSQL += whereParam[0]+"="+jsonObj.get(whereParam[1])+" and ";
				}
				
			}
			if(whereArr.length>0){
				updateSQL = updateSQL.substring(0,updateSQL.length()-5);
				if(StringUtils.isNotBlank(whereOfSet)){
					updateSQL += " and " + whereOfSet;
				}
			}
			//执行更新sql
			updateGoodsInfoDao.updateGoodsInfo(updateSQL);
		}
		LOGGER.info("==============storage修改商品信息结束==================");
		return true;
	}
}
