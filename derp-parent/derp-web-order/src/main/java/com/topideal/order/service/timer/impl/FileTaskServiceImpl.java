package com.topideal.order.service.timer.impl;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mongo.dao.FileTaskMongoDao;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.order.service.timer.FileTaskService;
import com.topideal.tools.BaseUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileTaskServiceImpl implements FileTaskService {

    @Autowired
    private FileTaskMongoDao fileTaskMongoDao;

    @Override
    public List<FileTaskMongo> getListByParamTask(FileTaskMongo mongo) throws Exception {
        Map<String, Object> params = copyFields(mongo);

        List<FileTaskMongo> list = fileTaskMongoDao.findAll(params);

        return list;
    }

    @Override
    public void save(FileTaskMongo mongo) throws Exception {
        fileTaskMongoDao.insert(mongo);
    }

    @Override
    public void modify(FileTaskMongo mongo) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", mongo.getCode());

        String json = JSONObject.fromObject(mongo).toString();
        JSONObject jsonObject = JSONObject.fromObject(json);
        jsonObject.put("modifyDate", TimeUtils.formatFullTime());

        fileTaskMongoDao.update(params, jsonObject);
    }

    @Override
    public FileTaskMongo searchByCode(String code) throws SQLException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        return fileTaskMongoDao.findOne(params);
    }

    @Override
    public List<FileTaskMongo> getListByParam(FileTaskMongo mongo) throws SQLException {
        Map<BaseUtils.Operator, Map<String, Object>> params = new HashMap<>();

        Map<String, Object> merParams = new HashMap<String, Object>();
        merParams.put("merchantId", mongo.getMerchantId());
        params.put(BaseUtils.Operator.eq, merParams);

        Map<String, Object> stateParams = new HashMap<String, Object>();
        stateParams.put("state", mongo.getMerchantId());
        params.put(BaseUtils.Operator.eq, stateParams);

        Map<String, Object> ownMonthParams = new HashMap<String, Object>();
        ownMonthParams.put("ownMonth", mongo.getMerchantId());
        params.put(BaseUtils.Operator.eq, ownMonthParams);

        List<String> taskTypeList = new ArrayList<>();
        Map<String, Object> typeParams = new HashMap<String, Object>();

        if (DERP_REPORT.FILETASK_TASKTYPE_SXCW.equals(mongo.getTaskType())) {
            taskTypeList.add(DERP_REPORT.FILETASK_TASKTYPE_SXCW);
            taskTypeList.add(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);
            typeParams.put("taskType", taskTypeList);
            params.put(BaseUtils.Operator.in, typeParams);
        }

        if (DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW.equals(mongo.getTaskType())) {
            taskTypeList.add(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW);
            taskTypeList.add(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);
            typeParams.put("taskType", taskTypeList);
            params.put(BaseUtils.Operator.in, typeParams);
        }

        if ("SXYW".equals(mongo.getTaskType())) {
            taskTypeList.add("SXYW");
            taskTypeList.add(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);
            typeParams.put("taskType", taskTypeList);
            params.put(BaseUtils.Operator.in, typeParams);
        }

        if ("SXSYBYW".equals(mongo.getTaskType())) {
            taskTypeList.add("SXSYBYW");
            taskTypeList.add(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);
            typeParams.put("taskType", taskTypeList);
            params.put(BaseUtils.Operator.in, typeParams);
        }

        if ("SXZHD".equals(mongo.getTaskType())) {
            taskTypeList.add("SXSYBCW");
            taskTypeList.add("SXCW");
            taskTypeList.add("SXYW");
            taskTypeList.add("SXSYBYW");
            taskTypeList.add(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);
            typeParams.put("taskType", taskTypeList);
            params.put(BaseUtils.Operator.in, typeParams);
        }

        return fileTaskMongoDao.findAllInParams(params);
    }

    private Map<String, Object> copyFields(FileTaskMongo fileTaskMongo) throws Exception {
        Map<String, Object> params = new HashMap<>();

        Class classType = fileTaskMongo.getClass();

        //获取对象所有属性
        Field fields[] = classType.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            String fieldName = field.getName();

            //打开私有访问
            field.setAccessible(true);

            Object value = field.get(fileTaskMongo);

            if (value != null) {
                params.put(fieldName, value);
            }
        }

        return params;
    }


}
