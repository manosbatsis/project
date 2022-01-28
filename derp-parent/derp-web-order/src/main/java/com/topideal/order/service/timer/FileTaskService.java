package com.topideal.order.service.timer;

import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.tools.PageMongo;

import java.sql.SQLException;
import java.util.List;

public interface FileTaskService {

    /**
     * 查询类型是生成excle的任务
     *
     * @param mongo
     * @return
     * @throws SQLException
     */
    List<FileTaskMongo> getListByParamTask(FileTaskMongo mongo) throws Exception;

    void save(FileTaskMongo mongo) throws Exception;

    void modify(FileTaskMongo mongo) throws Exception;

    FileTaskMongo searchByCode(String code) throws SQLException;

    List<FileTaskMongo> getListByParam(FileTaskMongo mongo) throws SQLException;

}
