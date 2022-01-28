
package com.topideal.common.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/***仓储库
 *  表字段状态/类型常量文件
 * */
public class DERP_STORAGE {
   
	/**库存调整s_adjustment_inventory---------------------------------*/
	/**状态 020-待调整 019-已调整 006-已删除 022-处理中*/
	public static final String ADJUSTMENTINVENTORY_STATUS_020 = "020";
	public static final String ADJUSTMENTINVENTORY_STATUS_019 = "019";
	public static final String ADJUSTMENTINVENTORY_STATUS_006 = "006";
	public static final String ADJUSTMENTINVENTORY_STATUS_022 = "022";
	public static ArrayList<DerpBasic> adjustmentInventory_statusList = new ArrayList<DerpBasic>();

	/**业务类别 1-销毁 2-月结损益 3-其他出入 4-唯品红冲 5-国检抽样 6-唯品报废*/
	public static final String ADJUSTMENTINVENTORY_MODEL_1 = "1";
	public static final String ADJUSTMENTINVENTORY_MODEL_2 = "2";
	public static final String ADJUSTMENTINVENTORY_MODEL_3 = "3";
	public static final String ADJUSTMENTINVENTORY_MODEL_4 = "4";
	public static final String ADJUSTMENTINVENTORY_MODEL_5 = "5";
	public static final String ADJUSTMENTINVENTORY_MODEL_6 = "6";
	public static ArrayList<DerpBasic> adjustmentInventory_modelList = new ArrayList<DerpBasic>();

	/**单据来源 01:接口回传 02:手工录入*/
	public static final String ADJUSTMENTINVENTORY_SOURCE_01 = "01";
	public static final String ADJUSTMENTINVENTORY_SOURCE_02 = "02";
	public static ArrayList<DerpBasic> adjustmentInventory_sourceList = new ArrayList<DerpBasic>();


	/**类型调整s_adjustment_type---------------------------------*/
	/**状态 020-待调整 019-已调整 006-已删除 022-处理中*/
	public static final String ADJUSTMENTTYPE_STATUS_020 = "020";
	public static final String ADJUSTMENTTYPE_STATUS_019 = "019";
	public static final String ADJUSTMENTTYPE_STATUS_006 = "006";
	public static final String ADJUSTMENTTYPE_STATUS_022 = "022";
	public static ArrayList<DerpBasic> adjustmentType_statusList = new ArrayList<DerpBasic>();

	/**业务类别 1-好坏品互转（批次无变更）  2-货号变更  3-效期调整（批次无变更） 4-大货理货 5-自然过期*/
	public static final String ADJUSTMENTTYPE_MODEL_1 = "1";
	public static final String ADJUSTMENTTYPE_MODEL_2 = "2";
	public static final String ADJUSTMENTTYPE_MODEL_3 = "3";
	public static final String ADJUSTMENTTYPE_MODEL_4 = "4";
	public static final String ADJUSTMENTTYPE_MODEL_5 = "5";
	public static ArrayList<DerpBasic> adjustmentType_modelList = new ArrayList<DerpBasic>();
	
	/**单据来源 01:接口回传 02:手工录入*/
	public static final String ADJUSTMENTTYPE_SOURCE_01 = "01";
	public static final String ADJUSTMENTTYPE_SOURCE_02 = "02";
	public static ArrayList<DerpBasic> adjustmentType_sourceList = new ArrayList<DerpBasic>();

	/**盘点指令表s_takes_stock---------------------------------*/
	/**服务类型 10-个性盘点 20-自主盘点入*/
	public static final String TAKESSTOCK_SERVERTYPE_10 = "10";
	public static final String TAKESSTOCK_SERVERTYPE_20 = "20";
	public static ArrayList<DerpBasic> takesStock_serverTypeList = new ArrayList<DerpBasic>();

	/**业务场景 10-客服盘点申请 20-仓库自行盘点 30-前端盘点申请*/
	public static final String TAKESSTOCK_MODEL_10= "10";
	public static final String TAKESSTOCK_MODEL_20= "20";
	public static final String TAKESSTOCK_MODEL_30= "30";
	public static ArrayList<DerpBasic> takesStock_modelList = new ArrayList<DerpBasic>();

	/**状态 006-已删除 014-已提交 013-待提交*/
	public static final String TAKESSTOCK_STATUS_006 = "006";
	public static final String TAKESSTOCK_STATUS_014 = "014";
	public static final String TAKESSTOCK_STATUS_013 = "013";
	public static ArrayList<DerpBasic> takesStock_statusList = new ArrayList<DerpBasic>();

	/**盘点结果详情表s_takes_stock_result_item---------------------------------*/
	/**调整类型: 1-盘盈 2-盘亏*/
	public static final String TAKESSTOCKRESULTITEM_TYPE_1 = "1";
	public static final String TAKESSTOCKRESULTITEM_TYPE_2 = "2";
	public static ArrayList<DerpBasic> takesStockResultItem_typeList = new ArrayList<DerpBasic>();

	/**原因描述	01：盘点差异；02：可保存报废；03：不可保存报废；04：二手售卖；05：调拨差异；06：收货错误；07：仓库错发；08：赔付平台已审批*/
	public static final String TAKESSTOCKRESULTITEM_REASONCODE_01 = "01";
	public static final String TAKESSTOCKRESULTITEM_REASONCODE_02 = "02";
	public static final String TAKESSTOCKRESULTITEM_REASONCODE_03 = "03";
	public static final String TAKESSTOCKRESULTITEM_REASONCODE_04 = "04";
	public static final String TAKESSTOCKRESULTITEM_REASONCODE_05 = "05";
	public static final String TAKESSTOCKRESULTITEM_REASONCODE_06 = "06";
	public static final String TAKESSTOCKRESULTITEM_REASONCODE_07 = "07";
	public static final String TAKESSTOCKRESULTITEM_REASONCODE_08 = "08";
	public static ArrayList<DerpBasic> takesStockResultItem_reasonCodeList = new ArrayList<DerpBasic>();

	/**盘点结果表s_takes_stock_results---------------------------------*/
	/**服务类型 10-个性盘点 20-自主盘点 30-唯品盘点*/
	public static final String TAKESSTOCKRESULT_SERVERTYPE_10 = "10";
	public static final String TAKESSTOCKRESULT_SERVERTYPE_20 = "20";
	public static final String TAKESSTOCKRESULT_SERVERTYPE_30 = "30";
	public static ArrayList<DerpBasic> takesStockResult_serverTypeList = new ArrayList<DerpBasic>();

	/**业务场景 10-客服盘点申请 20-仓库自行盘点 30-前端盘点申请 40-爬虫账单*/
	public static final String TAKESSTOCKRESULT_MODEL_10 = "10";
	public static final String TAKESSTOCKRESULT_MODEL_20 = "20";
	public static final String TAKESSTOCKRESULT_MODEL_30 = "30";
	public static final String TAKESSTOCKRESULT_MODEL_40 = "40";
	public static ArrayList<DerpBasic> takesStockResult_modelList = new ArrayList<DerpBasic>();

	/**状态 024-待确认  025-已确认 021-已作废  022-入库中 010-已入库 006-已删除*/
	public static final String TAKESSTOCKRESULT_STATUS_024 = "024";
	public static final String TAKESSTOCKRESULT_STATUS_025 = "025";
	public static final String TAKESSTOCKRESULT_STATUS_010 = "010";
	public static final String TAKESSTOCKRESULT_STATUS_021 = "021";
	public static final String TAKESSTOCKRESULT_STATUS_022 = "022";
	public static final String TAKESSTOCKRESULT_STATUS_006 = "006";
	public static ArrayList<DerpBasic> takesStockResult_statusList = new ArrayList<DerpBasic>();

	/**确认状态 009-待确认 022-确认中 010-已确认 004-已驳回*/
	public static final String TAKESSTOCKRESULT_CONFIRMSTATUS_009 = "009";
	public static final String TAKESSTOCKRESULT_CONFIRMSTATUS_010 = "010";
	public static final String TAKESSTOCKRESULT_CONFIRMSTATUS_022 = "022";
	public static final String TAKESSTOCKRESULT_CONFIRMSTATUS_004 = "004";
	public static ArrayList<DerpBasic> takesStockResult_confirmStatusList = new ArrayList<DerpBasic>();

	/**来源类型 1-op盘点结果 2-ofc盘点结果 3-唯品盘点结果 4-手工导入*/
	public static final String TAKESSTOCKRESULT_SOURCETYPE_1 = "1";
	public static final String TAKESSTOCKRESULT_SOURCETYPE_2 = "2";
	public static final String TAKESSTOCKRESULT_SOURCETYPE_3 = "3";
	public static final String TAKESSTOCKRESULT_SOURCETYPE_4 = "4";
	public static ArrayList<DerpBasic> takesStockResult_sourceTypeList = new ArrayList<DerpBasic>();

	/**库存调整类型  0-调减 1-调增 2-其他*/
	public static final String ADJUSTMENT_TYPE_0 = "0";
	public static final String ADJUSTMENT_TYPE_1 = "1";
	public static final String ADJUSTMENT_TYPE_2 = "2";
	public static ArrayList<DerpBasic> adjustment_typeList = new ArrayList<DerpBasic>();
	public static ArrayList<DerpBasic> adjustmentType_typeDHLHList = new ArrayList<DerpBasic>();

	/**仓储审核类型  10-驳回 20-确认*/
	public static final String STORAGE_CONFIRMTYPE_10 = "10";
	public static final String STORAGE_CONFIRMTYPE_20 = "20";
	public static ArrayList<DerpBasic> storage_confirmTypeList = new ArrayList<DerpBasic>();

	/**是否坏品 0-否 1-是*/
	public static final String TAKESSTOCKRESULT_ISDAMAGE_0 = "0";
	public static final String TAKESSTOCKRESULT_ISDAMAGE_1 = "1";
	public static ArrayList<DerpBasic> takesStockResult_isDamageList = new ArrayList<DerpBasic>();

static{
	
	/**状态 020-待调整 019-已调整 022-处理中*/
	adjustmentInventory_statusList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_020,"待调整"));
	adjustmentInventory_statusList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_019,"已调整"));
	adjustmentInventory_statusList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_022,"处理中"));

	/**业务类别 1-销毁 2-月结损益 3-其他出入 4-唯品红冲 5-国检抽样 6-唯品报废*/
	adjustmentInventory_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_1,"销毁"));
	adjustmentInventory_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_2,"月结损益"));
	adjustmentInventory_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_3,"其他出入"));
	adjustmentInventory_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_4,"唯品红冲"));
	adjustmentInventory_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_5,"国检抽样"));
	adjustmentInventory_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_6,"唯品报废"));

	/**单据来源 01:接口回传 02:手工录入*/
	adjustmentInventory_sourceList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_SOURCE_01,"接口回传"));
	adjustmentInventory_sourceList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTINVENTORY_SOURCE_02,"手工录入"));

	/**类型调整s_adjustment_type---------------------------------*/
	/**状态 020-待调整 019-已调整  022-处理中*/
	adjustmentType_statusList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_020,"待调整"));
	adjustmentType_statusList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_019,"已调整"));
	adjustmentType_statusList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_022,"处理中"));

	/**业务类别 1-好坏品互转（批次无变更）  2-货号变更  3-效期调整（批次无变更） 4-大货理货 5-自然过期*/
	adjustmentType_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_1,"好坏品互转（批次无变更）"));
	adjustmentType_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_2,"货号变更"));
	adjustmentType_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_3,"效期调整（批次无变更）"));
	adjustmentType_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_4,"大货理货"));
	adjustmentType_modelList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_5,"自然过期"));
	
	/**单据来源 01:接口回传 02:手工录入*/
	adjustmentType_sourceList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_01,"接口回传"));
	adjustmentType_sourceList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_02,"手工录入"));

	/**盘点指令表s_takes_stock---------------------------------*/
	/**服务类型 10-个性盘点 20-自主盘点入*/
	takesStock_serverTypeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCK_SERVERTYPE_10,"个性盘点"));
	takesStock_serverTypeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCK_SERVERTYPE_20,"自主盘点入"));

    /**业务场景 10-客服盘点申请 20-仓库自行盘点 30-前端盘点申请*/
	takesStock_modelList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCK_MODEL_10,"客服盘点申请"));
	takesStock_modelList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCK_MODEL_20,"仓库自行盘点"));
	takesStock_modelList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCK_MODEL_30,"前端盘点申请"));

	/**状态 006-已删除 014-已提交 013-待提交*/

	takesStock_statusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCK_STATUS_014,"已提交"));
	takesStock_statusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCK_STATUS_013,"待提交"));

	/**盘点结果详情表s_takes_stock_result_item---------------------------------*/
	/**调整类型: 1-盘盈 2-盘亏*/
	takesStockResultItem_typeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1,"盘盈"));
	takesStockResultItem_typeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_2,"盘亏"));

	/**原因描述	01：盘点差异；02：可保存报废；03：不可保存报废；04：二手售卖；05：调拨差异；06：收货错误；07：仓库错发；08：赔付平台已审批*/
	takesStockResultItem_reasonCodeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_REASONCODE_01,"盘点差异"));
	takesStockResultItem_reasonCodeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_REASONCODE_02,"可保存报废"));
	takesStockResultItem_reasonCodeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_REASONCODE_03,"不可保存报废"));
	takesStockResultItem_reasonCodeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_REASONCODE_04,"二手售卖"));
	takesStockResultItem_reasonCodeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_REASONCODE_05,"调拨差异"));
	takesStockResultItem_reasonCodeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_REASONCODE_06,"收货错误"));
	takesStockResultItem_reasonCodeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_REASONCODE_07,"仓库错发"));
	takesStockResultItem_reasonCodeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULTITEM_REASONCODE_08,"赔付平台已审批"));

	/**盘点结果表s_takes_stock_results---------------------------------*/
	/**服务类型 10-个性盘点 20-自主盘点 30-唯品盘点*/
	takesStockResult_serverTypeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_SERVERTYPE_10,"个性盘点"));
	takesStockResult_serverTypeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_SERVERTYPE_20,"自主盘点"));
	takesStockResult_serverTypeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_SERVERTYPE_30,"唯品盘点"));

	/**业务场景 10-客服盘点申请 20-仓库自行盘点 30-前端盘点申请 40-爬虫账单*/
	takesStockResult_modelList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_MODEL_10,"客服盘点申请"));
	takesStockResult_modelList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_MODEL_20,"仓库自行盘点"));
	takesStockResult_modelList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_MODEL_30,"前端盘点申请"));
	takesStockResult_modelList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_MODEL_40,"爬虫账单"));

	/**状态 024-待确认  025-已确认 021-已作废  022-入库中 010-已入库 006-已删除*/
	takesStockResult_statusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_024,"待确认"));
	takesStockResult_statusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_025,"已确认"));
	takesStockResult_statusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_021,"已作废"));
	takesStockResult_statusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022,"入库中"));
	takesStockResult_statusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010,"已入库"));

	/**确认状态 009-待确认 022-确认中 010-已确认 004-已驳回*/
	takesStockResult_confirmStatusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_CONFIRMSTATUS_009,"待确认"));
	takesStockResult_confirmStatusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_CONFIRMSTATUS_010,"确认中"));
	takesStockResult_confirmStatusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_CONFIRMSTATUS_022,"已确认"));
	takesStockResult_confirmStatusList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_CONFIRMSTATUS_004,"已驳回"));

	/**来源类型 1-op盘点结果 2-ofc盘点结果 3-唯品盘点结果  4-手工导入*/
	takesStockResult_sourceTypeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_1,"op盘点结果"));
	takesStockResult_sourceTypeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_2,"ofc盘点结果"));
	takesStockResult_sourceTypeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_3,"唯品盘点结果"));
	takesStockResult_sourceTypeList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_4,"手工导入"));

	/**库存调整类型  0-调减 1-调增 2-其他*/
	adjustment_typeList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENT_TYPE_0,"调减"));
	adjustment_typeList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENT_TYPE_1,"调增"));
	adjustment_typeList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENT_TYPE_2,"其他"));
	adjustmentType_typeDHLHList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENT_TYPE_0, "调整前"));
	adjustmentType_typeDHLHList.add(new DerpBasic(DERP_STORAGE.ADJUSTMENT_TYPE_1, "调整后"));


	/**仓储审核类型  10-驳回 20-确认*/
	storage_confirmTypeList.add(new DerpBasic(DERP_STORAGE.STORAGE_CONFIRMTYPE_10, "驳回"));
	storage_confirmTypeList.add(new DerpBasic(DERP_STORAGE.STORAGE_CONFIRMTYPE_20, "确认"));

	/**是否坏品 0-否 1-是*/
	takesStockResult_isDamageList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_ISDAMAGE_0, "否"));
	takesStockResult_isDamageList.add(new DerpBasic(DERP_STORAGE.TAKESSTOCKRESULT_ISDAMAGE_1, "是"));

  }
	/**根据key获取中文
	 * @param list 集合
	 * @param key 常量值
	 * */
	public static String getLabelByKey(List<DerpBasic> list,Object key){
		   for (DerpBasic item : list) {
				if(item.getKey().equals(key)){
					return item.getValue();
				}
			}
			return ""; 
	}
	/**获取常量集合
	 * @param listName 集合名称
	 */
	public static ArrayList<DerpBasic> getConstantListByName(String listName){
		   ArrayList<DerpBasic> list = null;
		   try{
			   Field[] fields = DERP_STORAGE.class.getDeclaredFields();
		       for(Field field:fields){
		          if(field.getName().equals(listName)){
		        	 list = (ArrayList<DerpBasic>) field.get(new DERP_STORAGE());
		          }
		       }
		   }catch(Exception e){}
		   return list;
	}
}
