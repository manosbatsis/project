package com.topideal.report.service.reporting;

import com.topideal.entity.dto.FileTaskDTO;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.tools.PageMongo;

import java.sql.SQLException;
import java.util.List;

public interface FileTaskService {

    /*public List<FileTaskModel> getListByParam(FileTaskModel model) throws SQLException;
     *//**
     * 查询类型是生成excle的任务
     * @param model
     * @return
     * @throws SQLException
     *//*
	public List<FileTaskModel> getListByParamTaks(FileTaskModel model) throws SQLException;
	
	public void modify(FileTaskModel model) throws SQLException;
	
	public FileTaskDTO getListByPage(FileTaskDTO model) throws Exception ;
	
	public FileTaskModel searchById(Long id) throws SQLException;
	
	public Long save(FileTaskModel model) throws SQLException;*/

    /**
     * 查询文件任务列表信息
     */
   FileTaskDTO listFileTask(FileTaskDTO dto) throws Exception;

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
