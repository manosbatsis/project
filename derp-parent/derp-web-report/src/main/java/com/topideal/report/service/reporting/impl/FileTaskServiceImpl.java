package com.topideal.report.service.reporting.impl;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.FileTaskDTO;
import com.topideal.mongo.dao.FileTaskMongoDao;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.tools.BaseUtils;

import net.sf.json.JSONObject;

@Service
public class FileTaskServiceImpl implements FileTaskService {

    @Autowired
    private FileTaskMongoDao fileTaskMongoDao;

    @Override
    public FileTaskDTO listFileTask(FileTaskDTO dto) throws Exception {

        FileTaskMongo mongo = new FileTaskMongo();
        BeanUtils.copyProperties(dto, mongo);
        Map<String, Object> params = copyFields(mongo);

        PageMongo<FileTaskMongo> list = fileTaskMongoDao.findAllByPage(params, dto.getBegin(), dto.getPageSize());

        FileTaskDTO fileTaskDTO = new FileTaskDTO();

        List<FileTaskMongo> mongos = list.getList();
        List<FileTaskDTO> dtos = new ArrayList<>();
        for (FileTaskMongo taskMongo : mongos) {
            FileTaskDTO taskDTO = new FileTaskDTO();
            BeanUtils.copyProperties(taskMongo, taskDTO);
            dtos.add(taskDTO);
        }
        fileTaskDTO.setList(dtos);
        fileTaskDTO.setTotal((int) list.getTotal());
        fileTaskDTO.setBegin(list.getBegin());
        fileTaskDTO.setPageSize(list.getPageSize());
        fileTaskDTO.setPageNo((list.getBegin() - 1) * list.getPageSize());
        fileTaskDTO.setEnd(list.getBegin() * list.getPageSize());
        return fileTaskDTO;
    }

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

        Map<String, Object> eqParams = new HashMap<String, Object>();
        eqParams.put("merchantId", mongo.getMerchantId());
        eqParams.put("state", mongo.getState());
        eqParams.put("ownMonth", mongo.getOwnMonth());
        params.put(BaseUtils.Operator.eq, eqParams);

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
            if(value instanceof String && StringUtils.isNotBlank(String.valueOf(value))) {
            	 params.put(fieldName, value);
            }else if (!(value instanceof String) && value != null) {
                params.put(fieldName, value);
            }
        }

        return params;
    }

}
