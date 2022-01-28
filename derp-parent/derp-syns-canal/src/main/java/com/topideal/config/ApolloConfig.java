package com.topideal.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
/**实时获取apollo最新配置，无需重启
 */

public class ApolloConfig {

    //库存库名
    public static String synInventoryDatabase = "syn.inventory.database";
    //库存需要同步的表
    public static String synInventoryTable = "syn.inventory.table";
    //库存需要同步的大数据表
    public static String synInventoryBigTable = "syn.inventorybig.table";
    //库存需要同步当天的表
    public static String synInventoryIndependentTable = "syn.inventory.independent.table";

    //业务库名
    public static String synOrderDatabase = "syn.order.database";
    //业务库需要同步的表
    public static String synOrderTable = "syn.order.table";
    //业务库需要同步的表大数据表
    public static String synOrderBigTable = "syn.orderbig.table";

    //仓储库名
    public static String synStorageDatabase = "syn.storage.database";
    //仓储需要同步的表
    public static String synStorageTable = "syn.storage.table";

    //主服务库名
    public static String synSystemDatabase = "syn.system.database";
    //主服务需要同步的表
    public static String synSystemTable = "syn.system.table";


    public static String getPropertyByName(String key){
        Config config = ConfigService.getConfig("synchronization-table");
        String value = config.getProperty(key, "");
        return value;
    }

}
