package com.topideal.service;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.List;

public interface CanalService {

    /**
     * 处理canal server解析binlog获得的实体类信息
     */
    public void synsparseBinlogEntry(List<CanalEntry.Entry> entrys) throws Exception;
}
