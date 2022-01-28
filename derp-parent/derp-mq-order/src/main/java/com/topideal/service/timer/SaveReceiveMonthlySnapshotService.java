package com.topideal.service.timer;

public interface SaveReceiveMonthlySnapshotService {

    /**
     * 生成应收月结快照
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @return
     * @throws Exception
     */
    boolean SaveReceiveMonthlySnapshot(String json,String keys,String topics,String tags) throws Exception;

}
