package com.topideal.service.base.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CustomsAreaConfigDao;
import com.topideal.dao.main.DepotCustomsRelDao;
import com.topideal.dao.main.MerchandiseCustomsRelDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.vo.main.CustomsAreaConfigModel;
import com.topideal.entity.vo.main.DepotCustomsRelModel;
import com.topideal.entity.vo.main.MerchandiseCustomsRelModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.service.base.CustomsAreaConfigService;

@Service
public class CustomsAreaConfigServiceImpl implements CustomsAreaConfigService {

    @Autowired
    CustomsAreaConfigDao customsAreaConfigDao;
    @Autowired
    DepotCustomsRelDao depotCustomsRelDao;
    @Autowired
    MerchandiseInfoDao merchandiseInfoDao;
    @Autowired
    MerchandiseCustomsRelDao merchandiseCustomsRelDao;

    

    @Override
    public CustomsAreaConfigDTO getListByPage(CustomsAreaConfigDTO model) throws SQLException {
        return customsAreaConfigDao.listByPage(model);
    }

    @Override
    public Map<String, Object> saveCustomsArea(CustomsAreaConfigModel model) throws Exception {
        Map<String, Object> resultMap =new HashMap<String, Object>();
        //判断参数是否为空
        if(StringUtils.isEmpty(model.getCode())){
            resultMap.put("code", "01");
            resultMap.put("message", "关区代码不能为空");
            return resultMap;
        }
        if(StringUtils.isEmpty(model.getName())){
            resultMap.put("code", "01");
            resultMap.put("message", "平台关区不能为空");
            return resultMap;
        }
        //判断平台关区是否相同
        CustomsAreaConfigModel modelSearch=new CustomsAreaConfigModel();
        modelSearch.setName(model.getName());
        modelSearch.setStatus("1");
        modelSearch= customsAreaConfigDao.searchByModel(modelSearch);
        if(modelSearch!=null){
            resultMap.put("code", "01");
            resultMap.put("message", "平台关区已存在");
            return resultMap;
        }
        model.setStatus("1");
        long id=customsAreaConfigDao.save(model);
        if(id>0){
            resultMap.put("code", "00");
            resultMap.put("message", "保存成功！");
        }
        return resultMap;
    }

    @Override
    public List<CustomsAreaConfigModel> listForExport(CustomsAreaConfigModel model) throws Exception {
        List<CustomsAreaConfigModel> list= customsAreaConfigDao.list(model);
        return list;
    }

    @Override
    public Map importCustomsArea(Map<Integer, List<List<Object>>> data, User user) throws SQLException {
        List<Map<String,String>> msgList = new ArrayList<Map<String,String>>();
        Integer success = 0;//成功数量
        Integer pass = 0;//跳过数量
        Integer failure = 0;//错误数量
        List<List<Object>> objects = data.get(0);
        //存储关区
        List<CustomsAreaConfigModel> customsAreaConfigList= new ArrayList<>();
        //存储是否包含相同平台关区
        Map<String, String> customsContionMap=new HashMap<>();

        for(int j = 1; j < objects.size(); j++){
            Map<String,String> msg = new HashMap<String,String>();
            Map<String, String> map = this.toMap(data.get(0).get(0).toArray(), objects.get(j).toArray());
            //判断关区平台是否为空
            String  customsCode= map.get("关区代码") ;
            if(StringUtils.isBlank(customsCode)) {
                msg.put("row", String.valueOf(j+1));
                msg.put("message", "关区代码不能为空");
                msgList.add(msg);
                failure += 1;
                continue ;
            }
            customsCode=customsCode.trim();
            //判断平台关区是否为空
            String platformCustoms = map.get("平台关区") ;
            if(StringUtils.isBlank(platformCustoms)) {
                msg.put("row", String.valueOf(j+1));
                msg.put("message", "平台关区不能为空");
                msgList.add(msg);
                failure += 1;
                continue ;
            }
            platformCustoms = platformCustoms.trim();
            //判断列表中是否存在相同的平台关区
            if (customsContionMap.containsKey(platformCustoms)) {
                msg.put("row", String.valueOf(j+1));
                msg.put("message", "列表中存在相同的平台关区："+platformCustoms);
                msgList.add(msg);
                failure += 1;
                continue ;
            }else {
                customsContionMap.put(platformCustoms, platformCustoms);
            }
            //判断平台关区是否重名
            CustomsAreaConfigModel areaConfig=new CustomsAreaConfigModel();
            areaConfig.setName(platformCustoms);
            areaConfig.setStatus("1");
            areaConfig=customsAreaConfigDao.searchByModel(areaConfig);
            if(areaConfig!=null){
                msg.put("row", String.valueOf(j+1));
                msg.put("message", "平台关区名称已存在："+platformCustoms);
                msgList.add(msg);
                failure += 1;
                continue ;
            }
            //创建关区列表
            areaConfig=new CustomsAreaConfigModel();
            areaConfig.setName(platformCustoms);
            areaConfig.setCode(customsCode);
            areaConfig.setCreater(user.getId());
            areaConfig.setCreateName(user.getName());
            customsAreaConfigList.add(areaConfig);
        }
        //循环新增关区平台列表
        if(failure==0){
            for (CustomsAreaConfigModel customsAreaConfigModel : customsAreaConfigList) {
                customsAreaConfigModel.setStatus("1");
                Long customsAreaId = customsAreaConfigDao.save(customsAreaConfigModel);
                if(customsAreaId != null) {
                    success += 1 ;
                }else {
                    failure += 1 ;
                }
            }
        }

        Map map = new HashMap();
        map.put("success", success);
        map.put("pass", pass);
        map.put("failure", failure);
        map.put("message", msgList);
        return map;
    }

    @Override
    public Map<String, Object> delReptile(String ids) throws SQLException {
        Map<String, Object> resultMap =new HashMap<String, Object>();
        //校验id是否正确
        boolean isRight = StrUtils.validateIds(ids);
        if(!isRight){
            //输入信息不完整
            resultMap.put("code","01");
            resultMap.put("message","数据不能为空");
            return resultMap;//数据为空
        }
        List list = StrUtils.parseIds(ids);

        for(int i=0;i<list.size();i++){
            //查询是否被仓库信息表关联
            DepotCustomsRelModel depotCustomsRelModel=new DepotCustomsRelModel();
            depotCustomsRelModel.setCustomsAreaId((Long)list.get(i));
            List<DepotCustomsRelModel> listDepot=depotCustomsRelDao.list(depotCustomsRelModel);
            if(listDepot.size() > 0){
                resultMap.put("code","01");
                resultMap.put("message",listDepot.get(0).getCustomsAreaName()+"该平台关区已关联仓库，不能删除");
                return resultMap;
            }
            //查询是否被商品信息表关联
            MerchandiseInfoModel merchandiseInfoModel=new MerchandiseInfoModel();
            merchandiseInfoModel.setCustomsAreaId((Long)list.get(i));
            List<MerchandiseInfoModel> listMerchand=merchandiseInfoDao.list(merchandiseInfoModel);
            if(listMerchand.size() > 0) {
                CustomsAreaConfigModel  model = customsAreaConfigDao.searchById((Long)list.get(i));
                resultMap.put("code","01");
                resultMap.put("message", model.getName() + "该平台关区已关联商品，不能删除");
                return resultMap;
            }
        }

        //修改关区配置状态
        for (int i=0;i<list.size();i++){
            CustomsAreaConfigModel model=new CustomsAreaConfigModel();
            model.setId((Long)list.get(i));
            model.setStatus("006");
            model.setModifyDate(TimeUtils.getNow());
            customsAreaConfigDao.modify(model);
        }
        resultMap.put("code","00");
        resultMap.put("message", "删除成功！");
        return resultMap;
    }


    /**
     * 把两个数组组成一个map
     * @param keys 键数组
     * @param values 值数组
     * @return 键值对应的map
     */
    private Map<String, String> toMap(Object[] keys, Object[] values) {
        if (keys != null && values != null && keys.length == values.length) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            for (int i = 0; i < keys.length; i++) {
                map.put((String) keys[i], values[i].toString());
            }
            return map;
        }
        return null;
    }

    /**
     * 判断输入字段是否为空
     * @param index
     * @param content
     * @param msg
     * @param resultList
     * @return
     */
    private boolean checkIsNullOrNot(int index , String content ,
                                     String msg ,List<ImportErrorMessage> resultList ) {
        if(StringUtils.isBlank(content)) {
            ImportErrorMessage message = new ImportErrorMessage();
            message.setRows(index + 1);
            message.setMessage(msg);
            resultList.add(message);
            return true ;
        }else {
            return false ;
        }
    }

    /**
     * 错误时，设置错误内容
     * @param index
     * @param msg
     * @param resultList
     */
    private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
        ImportErrorMessage message = new ImportErrorMessage();
        message.setRows(index + 1);
        message.setMessage(msg);
        resultList.add(message);
    }
    
    /**
     * 获取下拉列表
     */
    public List<SelectBean> getSelectBean(DepotCustomsRelModel model) throws Exception{
    	if(model.getDepotId() == null) {
    		return null;
    	}
    	return depotCustomsRelDao.getSelectBean(model);
    }
    
    @Override
	public List<SelectBean> getCustomsSelectBean() throws Exception {
		return customsAreaConfigDao.getCustomsSelectBean();
	}

	@Override
	public List<MerchandiseCustomsRelModel> getmerchandiseCustomsRelList(MerchandiseCustomsRelModel model) throws Exception {
		List<MerchandiseCustomsRelModel> list = merchandiseCustomsRelDao.list(model);
		return list;
	}
}
