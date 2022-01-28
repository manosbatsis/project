package com.topideal.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_LOG;
import com.topideal.dao.LogStreamApiDao;
import com.topideal.entity.vo.LogStreamApiModel;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.APILogService;
import com.topideal.tools.CollectionEnum;
import com.topideal.tools.ParseDaterangepicker;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/8/6.
 */
@Service
public class APILogServiceImpl implements APILogService {

	@Autowired
	private JSONMongoDao jsonMongoDao;

	@Autowired
	private LogStreamApiDao logStreamApiDao;
	
	@Override
	public PageMongo searchLogByPage(PageMongo pageMongo, String keyword, String state, String point, String derpCode,
			String endDateStr, String id, String selectScope) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(derpCode)) {
			params.put("derpCode", derpCode);
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}
		if (StringUtils.isNotBlank(endDateStr)) {
			params.put("receiveData", ParseDaterangepicker.parse(endDateStr));
		}
		if (StringUtils.isNotBlank(id)) {
			params.put("id", id);
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "receiveData");
		if(selectScope != null && selectScope.equals(DERP_LOG.LOG_SELECT_SCOPE_2)){
			return jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.API_MONITOR_HISTORY.getCollectionName());
		}
		return jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.API_MONITOR.getCollectionName());
	}

	@Override
	public boolean resetSend(String id, String collectionName) throws Exception {
		return false;
	}
	
	/**
	 * 批量推送
	 * @param ids
	 * @param collectionName
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean resetSend(List<String> ids, String collectionName) throws Exception {
		for (String id : ids) {
			//通过logs_is查找mongo中信息
			LogStreamApiModel model1 = new LogStreamApiModel();
			model1.setId(Long.parseLong(id));
			LogStreamApiModel model = logStreamApiDao.searchByModel(model1);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", model.getLogId());
			JSONObject jsonObject = jsonMongoDao.findOne(params, collectionName);
			if (jsonObject==null) {
				collectionName = CollectionEnum.API_MONITOR_HISTORY.getCollectionName();
				jsonObject = jsonMongoDao.findOne(params, collectionName);
			}
			
			String url = jsonObject.getString("url");
			//url = url.replace("http://10.10.102.30:86", "http://127.0.0.1:8080");
			
			PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type","application/json;charset=GBK");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            String string = jsonObject.getString("requestMessage");
            out.print(jsonObject.getString("requestMessage"));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
                JSONObject jsonObject2 = new JSONObject();
                JSONObject fromObject = jsonObject2.fromObject(result);
                if ("2".equals(fromObject.getString("status"))) {
					throw new Exception(); 
				}
            }
            if(out!=null){
                out.close();
            }
            if(in!=null){
                in.close();
            }
			
			Thread.currentThread().sleep(100);
		}
		return true;
	}
}
