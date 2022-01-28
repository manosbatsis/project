package com.topideal.common.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/***库存库
 *  表字段状态/类型常量文件
 * */
public class DERP_INVENTORY {
	/**期初库存i_bu_inventory_item---------------------------------*/
	/**效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上,7过期*/
	public static final String BUINVENTORY_EFFECTIVEINTERVAL_1= "1";
	public static final String BUINVENTORY_EFFECTIVEINTERVAL_2= "2";
	public static final String BUINVENTORY_EFFECTIVEINTERVAL_3= "3";
	public static final String BUINVENTORY_EFFECTIVEINTERVAL_4= "4";
	public static final String BUINVENTORY_EFFECTIVEINTERVAL_5= "5";
	public static final String BUINVENTORY_EFFECTIVEINTERVAL_6= "6";
	public static final String BUINVENTORY_EFFECTIVEINTERVAL_7= "7";
	public static ArrayList<DerpBasic> initInventory_effectiveIntervalList = new ArrayList<DerpBasic>();
	
	/**期初库存i_init_inventory---------------------------------*/
	/**库存类型 0 好品  1 坏品*/
	public static final String INITINVENTORY_TYPE_0 = "0";
	public static final String INITINVENTORY_TYPE_1 = "1";
	public static ArrayList<DerpBasic> initInventory_typeList = new ArrayList<DerpBasic>();

	/**状态 1-未确认  2-已确认*/
	public static final String INITINVENTORY_STATE_1 = "1";
	public static final String INITINVENTORY_STATE_2 = "2";
	public static ArrayList<DerpBasic> initInventory_stateList= new ArrayList<DerpBasic>();

	/**库存状态  E0: 可用  E1:冻结*/
	public static final String INITINVENTORY_INVENTORYSTATUS_E0 = "E0";
	public static final String INITINVENTORY_INVENTORYSTATUS_E1 = "E1";
	public static ArrayList<DerpBasic> initInventory_inventoryStatusList= new ArrayList<DerpBasic>();

	/**数据来源：1-录入/导入  2-OP 3-OFC*/
	public static final String INITINVENTORY_SOURCE_1 = "1";
	public static final String INITINVENTORY_SOURCE_2 = "2";
	public static final String INITINVENTORY_SOURCE_3 = "3";
	public static ArrayList<DerpBasic> initInventory_sourceList= new ArrayList<DerpBasic>();
	
	/**批次表i_inventory_batch-------------------------*/
	/**库存类型  0-正常品  1-残次品*/
	public static final String INVENTORY_TYPE_0 = "0";
	public static final String INVENTORY_TYPE_1 = "1";
	public static ArrayList<DerpBasic> inventoryTypeList = new ArrayList<DerpBasic>();

	/**冻结解冻表i_inventory_freeze_details-------------------------*/
	/**库存冻结操作类型  0-冻结 1-解冻*/
	public static final String INVENTORYFREEZE_OPERATETYPE_0 = "0";
	public static final String INVENTORYFREEZE_OPERATETYPE_1 = "1";
	public static ArrayList<DerpBasic> inventoryFreeze_operateTypeList= new ArrayList<DerpBasic>();

	/**收发明细i_inventory_details------------------------------*/
	/**库存加减操作类型  0-调减 1-调增 2-其他*/
	public static final String INVENTORY_OPERATETYPE_0 = "0";
	public static final String INVENTORY_OPERATETYPE_1 = "1";
	public static final String INVENTORY_OPERATETYPE_2 = "2";
	public static ArrayList<DerpBasic> inventory_operateTypeList= new ArrayList<DerpBasic>();
	
	/**库存单据来源  001-采购入库 002-销售订单 003-销售出库 004-销售退货订单 005-销售退货入库
	 006-销售退货出库 007-电商订单 008-电商订单出库 009-调拨订单 0010-调拨入库
	 0011-调拨出库 0012-库存调整单 0013-盘点结果单 0014-类型调整单 0015-电商订单退货 0016-账单出入库
	 0017-上架入库 0018-采购退货出库  0019-移库单 0020-库位调整单 0021-领料单*/
	public static final String INVENTORY_SOURCE_001 = "001";
	public static final String INVENTORY_SOURCE_002 = "002";
	public static final String INVENTORY_SOURCE_003 = "003";
	public static final String INVENTORY_SOURCE_004 = "004";
	public static final String INVENTORY_SOURCE_005 = "005";
	public static final String INVENTORY_SOURCE_006 = "006";
	public static final String INVENTORY_SOURCE_007 = "007";
	public static final String INVENTORY_SOURCE_008 = "008";
	public static final String INVENTORY_SOURCE_009 = "009";
	public static final String INVENTORY_SOURCE_0010 = "0010";
	public static final String INVENTORY_SOURCE_0011 = "0011";
	public static final String INVENTORY_SOURCE_0012 = "0012";
	public static final String INVENTORY_SOURCE_0013 = "0013";
	public static final String INVENTORY_SOURCE_0014 = "0014";
	public static final String INVENTORY_SOURCE_0015 = "0015";
	public static final String INVENTORY_SOURCE_0016 = "0016";
	public static final String INVENTORY_SOURCE_0017 = "0017";
	public static final String INVENTORY_SOURCE_0018 = "0018";
	public static final String INVENTORY_SOURCE_0019 = "0019";
	public static final String INVENTORY_SOURCE_0020 = "0020";
	public static final String INVENTORY_SOURCE_0021 = "0021";
	public static ArrayList<DerpBasic> inventory_sourceList = new ArrayList<DerpBasic>();

	/**事物类型 001-采购入库 002-销售出库 003-销售退货入库 004-销售退货出库 005-电商订单出库 006-调拨入库
	 * 007-调拨出库 008-盘盈入 009-盘亏出 0010-销毁 0011-其他出 0012-其他入 0013-月结盘盈 
	 * 0014-月结盘亏 0015-好/坏品入 0016-好/坏品出 0017-效期调整入 0018-效期调整出 0019-货号变更入
	 * 0020-货号变更出 0021-大货理货出 0022-大货理货入 0023-唯品红冲入 0024-采购执行入 0025-采购执行出
	 * 0026-国检抽样出 0027-自然过期出 0028-自然过期入 0029-唯品盘点入 0030-唯品盘点出 0031-唯品报废出
	 * 0032-电商订单退货入  0033-客退入库  0034-账单销售出库  0035-上架入库 0036-采购退货出库
	 * 0037-移库单出  0038-移库单入 0039-库位调整入  0040-库位调整出 ,0041 领料出库
	 * */
	public static final String INVENTORY_THINGSTATUS_001 = "001";
	public static final String INVENTORY_THINGSTATUS_002 = "002";
	public static final String INVENTORY_THINGSTATUS_003 = "003";
	public static final String INVENTORY_THINGSTATUS_004 = "004";
	public static final String INVENTORY_THINGSTATUS_005 = "005";
	public static final String INVENTORY_THINGSTATUS_006 = "006";
	public static final String INVENTORY_THINGSTATUS_007 = "007";
	public static final String INVENTORY_THINGSTATUS_008 = "008";
	public static final String INVENTORY_THINGSTATUS_009 = "009";
	public static final String INVENTORY_THINGSTATUS_0010 = "0010";
	public static final String INVENTORY_THINGSTATUS_0011 = "0011";
	public static final String INVENTORY_THINGSTATUS_0012 = "0012";
	public static final String INVENTORY_THINGSTATUS_0013 = "0013";
	public static final String INVENTORY_THINGSTATUS_0014 = "0014";
	public static final String INVENTORY_THINGSTATUS_0015 = "0015";
	public static final String INVENTORY_THINGSTATUS_0016 = "0016";
	public static final String INVENTORY_THINGSTATUS_0017 = "0017";
	public static final String INVENTORY_THINGSTATUS_0018 = "0018";
	public static final String INVENTORY_THINGSTATUS_0019 = "0019";
	public static final String INVENTORY_THINGSTATUS_0020 = "0020";
	public static final String INVENTORY_THINGSTATUS_0021 = "0021";
	public static final String INVENTORY_THINGSTATUS_0022 = "0022";
	public static final String INVENTORY_THINGSTATUS_0023 = "0023";
	public static final String INVENTORY_THINGSTATUS_0024 = "0024";
	public static final String INVENTORY_THINGSTATUS_0025 = "0025";
	public static final String INVENTORY_THINGSTATUS_0026 = "0026";
	public static final String INVENTORY_THINGSTATUS_0027 = "0027";
	public static final String INVENTORY_THINGSTATUS_0028 = "0028";
	public static final String INVENTORY_THINGSTATUS_0029 = "0029";
	public static final String INVENTORY_THINGSTATUS_0030 = "0030";
	public static final String INVENTORY_THINGSTATUS_0031 = "0031";
	public static final String INVENTORY_THINGSTATUS_0032 = "0032";
	public static final String INVENTORY_THINGSTATUS_0033 = "0033";
	public static final String INVENTORY_THINGSTATUS_0034 = "0034";
	public static final String INVENTORY_THINGSTATUS_0035 = "0035";
	public static final String INVENTORY_THINGSTATUS_0036 = "0036";
	public static final String INVENTORY_THINGSTATUS_0037 = "0037";
	public static final String INVENTORY_THINGSTATUS_0038 = "0038";
	public static final String INVENTORY_THINGSTATUS_0039 = "0039";
	public static final String INVENTORY_THINGSTATUS_0040 = "0040";
	public static final String INVENTORY_THINGSTATUS_0041 = "0041";
	public static ArrayList<DerpBasic> inventory_thingStatusList = new ArrayList<DerpBasic>();
	
	

	/**库存单据类型 001-采购入库 002-销售订单 003-销售出库 004-销售退货订单 005-销售退货入库 006-销售退货出库
	 * 007-电商订单 008-电商订单出库 009-调拨订单 0010-调拨入库 0011-调拨出库 0012-月结损益 0013-销毁 
	 * 0014-其他出入 0015-盘点结果单 0016-好坏互转 0017-货号变更 0018-效期调整 0019-大货理货 
	 * 0020-唯品红冲 0021-采购执行 0022-国检抽样 0023-自然过期 0024-唯品盘点 0025-唯品报废 0026-电商订单退
	 * 0027-客退入库 0028-账单销售出库 0029-上架入库 0030-采购退货出库  0031-移库单 0032-库位调整单 0033-领料单 
	 * */
	public static final String INVENTORY_SOURCETYPE_001 = "001";
	public static final String INVENTORY_SOURCETYPE_002 = "002";
	public static final String INVENTORY_SOURCETYPE_003 = "003";
	public static final String INVENTORY_SOURCETYPE_004 = "004";
	public static final String INVENTORY_SOURCETYPE_005 = "005";
	public static final String INVENTORY_SOURCETYPE_006 = "006";
	public static final String INVENTORY_SOURCETYPE_007 = "007";
	public static final String INVENTORY_SOURCETYPE_008 = "008";
	public static final String INVENTORY_SOURCETYPE_009 = "009";
	public static final String INVENTORY_SOURCETYPE_0010 = "0010";
	public static final String INVENTORY_SOURCETYPE_0011 = "0011";
	public static final String INVENTORY_SOURCETYPE_0012 = "0012";
	public static final String INVENTORY_SOURCETYPE_0013 = "0013";
	public static final String INVENTORY_SOURCETYPE_0014 = "0014";
	public static final String INVENTORY_SOURCETYPE_0015 = "0015";
	public static final String INVENTORY_SOURCETYPE_0016 = "0016";
	public static final String INVENTORY_SOURCETYPE_0017 = "0017";
	public static final String INVENTORY_SOURCETYPE_0018 = "0018";
	public static final String INVENTORY_SOURCETYPE_0019 = "0019";
	public static final String INVENTORY_SOURCETYPE_0020 = "0020";
	public static final String INVENTORY_SOURCETYPE_0021 = "0021";
	public static final String INVENTORY_SOURCETYPE_0022 = "0022";
	public static final String INVENTORY_SOURCETYPE_0023 = "0023";
	public static final String INVENTORY_SOURCETYPE_0024 = "0024";
	public static final String INVENTORY_SOURCETYPE_0025 = "0025";
	public static final String INVENTORY_SOURCETYPE_0026 = "0026";
	public static final String INVENTORY_SOURCETYPE_0027 = "0027";
	public static final String INVENTORY_SOURCETYPE_0028 = "0028";
	public static final String INVENTORY_SOURCETYPE_0029 = "0029";
	public static final String INVENTORY_SOURCETYPE_0030 = "0030";
	public static final String INVENTORY_SOURCETYPE_0031 = "0031";
	public static final String INVENTORY_SOURCETYPE_0032 = "0032";
	public static final String INVENTORY_SOURCETYPE_0033 = "0033";
	public static ArrayList<DerpBasic> inventory_sourceTypeList = new ArrayList<DerpBasic>();
	
	/**实时库存快照表i_inventory_real_time_snapshot---------------------------------*/
	/**库存状态  0-可用，1-冻结*/
	public static final String REALTIMESNAPSHOT_STORESTYPE_0 = "0";
	public static final String REALTIMESNAPSHOT_STORESTYPE_1 = "1";
	public static ArrayList<DerpBasic> realTimeSnapshot_storesTypeList = new ArrayList<DerpBasic>();

	/**快照来源 OP-OP接口 OFC-OFC接口 GSS-GSS接口 GSS_CN-GSS菜鸟接口 YWMS-众邦云 ALI 阿里菜鸟接口*/
	public static final String REALTIMESNAPSHOT_SNAPSHOTSOURCE_OP = "OP";
	public static final String REALTIMESNAPSHOT_SNAPSHOTSOURCE_OFC = "OFC";
	public static final String REALTIMESNAPSHOT_SNAPSHOTSOURCE_GSS = "GSS";
	public static final String REALTIMESNAPSHOT_SNAPSHOTSOURCE_GSS_CN = "GSS_CN";
	public static final String REALTIMESNAPSHOT_SNAPSHOTSOURCE_YWMS = "YWMS";
	public static final String REALTIMESNAPSHOT_SNAPSHOTSOURCE_ALI = "ALI";
	public static ArrayList<DerpBasic> realTimeSnapshot_snapshotSourceList = new ArrayList<DerpBasic>();

	/**月结账单i_monthly_account---------------------------------*/
	/**状态：1-未结转 2-已结转*/
	public static final String MONTHLYACCOUNT_STATE_1 = "1";
	public static final String MONTHLYACCOUNT_STATE_2 = "2";
	public static ArrayList<DerpBasic> monthlyAccount_stateList = new ArrayList<DerpBasic>();
	
static{
	
	/**期初库存i_bu_inventory_item---------------------------------*/
	/**效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上,7过期*/
	initInventory_effectiveIntervalList.add(new DerpBasic(DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_1,"0＜X≤1/10"));
	initInventory_effectiveIntervalList.add(new DerpBasic(DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_2,"1/10＜X≤1/5"));
	initInventory_effectiveIntervalList.add(new DerpBasic(DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_3,"1/5＜X≤1/3"));
	initInventory_effectiveIntervalList.add(new DerpBasic(DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_4,"1/3＜X≤1/2"));
	initInventory_effectiveIntervalList.add(new DerpBasic(DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_5,"1/2＜X≤2/3"));
	initInventory_effectiveIntervalList.add(new DerpBasic(DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_6,"2/3以上"));
	initInventory_effectiveIntervalList.add(new DerpBasic(DERP_INVENTORY.BUINVENTORY_EFFECTIVEINTERVAL_7,"过期"));

	
		/**期初库存i_init_inventory---------------------------------*/
		/**库存类型 0-好品  1-坏品*/
		initInventory_typeList.add(new DerpBasic(DERP_INVENTORY.INITINVENTORY_TYPE_0,"好品"));
		initInventory_typeList.add(new DerpBasic(DERP_INVENTORY.INITINVENTORY_TYPE_1,"坏品"));
	
		/**状态 1-未确认  2-已确认*/
		initInventory_stateList.add(new DerpBasic(DERP_INVENTORY.INITINVENTORY_STATE_1,"未确认"));
		initInventory_stateList.add(new DerpBasic(DERP_INVENTORY.INITINVENTORY_STATE_2,"已确认"));
	
		/**库存状态  E0: 可用  E1:冻结*/
		initInventory_inventoryStatusList.add(new DerpBasic(DERP_INVENTORY.INITINVENTORY_INVENTORYSTATUS_E0,"可用"));
		initInventory_inventoryStatusList.add(new DerpBasic(DERP_INVENTORY.INITINVENTORY_INVENTORYSTATUS_E1,"冻结"));
	
		/**数据来源：1-录入/导入  2-OP 3-OFC*/
		initInventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INITINVENTORY_SOURCE_1,"录入/导入"));
		initInventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INITINVENTORY_SOURCE_2,"OP"));
		initInventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INITINVENTORY_SOURCE_3,"OFC"));
		
		/**批次表i_inventory_batch-------------------------*/
		/**库存类型  0-正常品  1-残次品*/
		inventoryTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_TYPE_0, "正常品"));
		inventoryTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_TYPE_1, "残次品"));
		
		/**冻结解冻表i_inventory_freeze_details-------------------------*/
		/**库存冻结操作类型  0-冻结 1-解冻*/
		/**库存冻结操作类型  0-冻结 1-解冻*/
		inventoryFreeze_operateTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_0,"冻结"));
		inventoryFreeze_operateTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1,"解冻"));
		
		/**收发明细i_inventory_details------------------------------*/
		/**库存加减操作类型  0-调减 1-调增 2-其他*/
		inventory_operateTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_OPERATETYPE_0,"调减"));
		inventory_operateTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_OPERATETYPE_1,"调增"));
		inventory_operateTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_OPERATETYPE_2,"其他"));
		
		/**库存单据来源  001-采购入库 002-销售订单 003-销售出库 004-销售退货订单 005-销售退货入库
		 006-销售退货出库 007-电商订单 008-电商订单出库 009-调拨订单 0010-调拨入库
		 0011-调拨出库 0012-库存调整单 0013-盘点结果单 0014-类型调整单 0015-电商订单退货 0016-账单出入库 0017-上架入库
		 0018-采购退货出库 0019-移库单 0020-库位调整单 0021-领料单*/
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_001,"采购入库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_002,"销售订单"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_003,"销售出库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_004,"销售退货订单"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_005,"销售退货入库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_006,"销售退货出库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_007,"电商订单"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_008,"电商订单出库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_009,"调拨订单"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0010,"调拨入库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0011,"调拨出库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0012,"库存调整单"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0013,"盘点结果单"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0014,"类型调整单"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0015,"电商订单退货"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0016,"账单出入库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0017,"上架入库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0018,"采购退货出库"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0019,"移库单"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0020,"库位调整单"));
		inventory_sourceList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCE_0021,"领料单"));

	
		/**库存单据类型 001-采购入库 002-销售订单 003-销售出库 004-销售退货订单 005-销售退货入库 006-销售退货出库
		 * 007-电商订单 008-电商订单出库 009-调拨订单 0010-调拨入库 0011-调拨出库 0012-月结损益 0013-销毁
		 * 0014-其他出入 0015-盘点结果单 0016-好坏互转 0017-货号变更 0018-效期调整 0019-大货理货 
		 * 0020-唯品红冲 0021-采购执行 0022-国检抽样 0023-自然过期 0024-唯品盘点 0025-唯品报废 0026-电商订单退
		 *  0027-客退入库 0028-账单销售出库 0029-上架入库 0030 采购退货出库 0031-移库单  0032-库位调整  0033-领料单
		 * */
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_001,"采购入库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_002,"销售订单"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_003,"销售出库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_004,"销售退货订单"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_005,"销售退货入库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_006,"销售退货出库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_007,"电商订单"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_008,"电商订单出库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_009,"调拨订单"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0010,"调拨入库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011,"调拨出库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0012,"月结损益"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0013,"销毁"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0014,"其他出入"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0015,"盘点结果单"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0016,"好坏互转"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0017,"货号变更"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0018,"效期调整"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0019,"大货理货"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0020,"唯品红冲"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0021,"采购执行"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0022,"国检抽样"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0023,"自然过期"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0024,"唯品盘点"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0025,"唯品报废"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0026,"电商订单退"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0027,"客退入库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0028,"账单销售出库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0029,"上架入库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0030,"采购退货出库"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0031,"移库单出"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0032,"库位调整"));
		inventory_sourceTypeList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_SOURCETYPE_0033,"领料单"));

	
		
		/**事物类型 001-采购入库 002-销售出库 003-销售退货入库 004-销售退货出库 005-电商订单出库 006-调拨入库
		 * 007-调拨出库 008-盘盈入 009-盘亏出 0010-销毁 0011-其他出 0012-其他入 0013-月结盘盈 
		 * 0014-月结盘亏 0015-好/坏品入 0016-好/坏品出 0017-效期调整入 0018-效期调整出 0019-货号变更入
		 * 0020-货号变更出 0021-大货理货出 0022-大货理货入 0023-唯品红冲入 0024-采购执行入 0025-采购执行出
		 * 0026-国检抽样出 0027-自然过期出 0028-自然过期入 0029-唯品盘点入 0030-唯品盘点出 0031-唯品报废出
		 * 0032- 电商订单退货入   0033-客退入库  0034-账单销售出库  0035-上架入库
		 * 0037-移库单出  0038-移库单入 0039-库位调整入  0040-库位调整出
		 * */
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_001,"采购入库"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_002,"销售出库"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_003,"销售退货入库"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_004,"销售退货出库"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_005,"电商订单出库"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_006,"调拨入库"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_007,"调拨出库"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_008,"盘盈入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_009,"盘亏出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0010,"销毁"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0011,"其他出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0012,"其他入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0013,"月结盘盈"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0014,"月结盘亏"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0015,"好/坏品入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0016,"好/坏品出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0017,"效期调整入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0018,"效期调整出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0019,"货号变更入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0020,"货号变更出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0021,"大货理货出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0022,"大货理货入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0023,"唯品红冲入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0024,"采购执行入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0025,"采购执行出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0026,"国检抽样出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0027,"自然过期出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0028,"自然过期入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0029,"唯品盘点入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0030,"唯品盘点出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0031,"唯品报废出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0032,"电商订单退货入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0033,"客退入库"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0034,"账单销售出库 "));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0035,"上架入库"));		
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0036,"采购退货出库"));	
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0037,"移库单出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0038,"移库单入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0039,"库位调整入"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0040,"库位调整出"));
		inventory_thingStatusList.add(new DerpBasic(DERP_INVENTORY.INVENTORY_THINGSTATUS_0041,"领料单出库"));


		/**实时库存快照表i_inventory_real_time_snapshot---------------------------------*/
		/**库存状态  0-可用，1-冻结*/
		realTimeSnapshot_storesTypeList.add(new DerpBasic(DERP_INVENTORY.REALTIMESNAPSHOT_STORESTYPE_0,"可用"));
		realTimeSnapshot_storesTypeList.add(new DerpBasic(DERP_INVENTORY.REALTIMESNAPSHOT_STORESTYPE_1,"冻结"));
	
		/**快照来源 OP-OP接口 OFC-OFC接口 GSS-GSS接口 YWMS-众邦云 ALI_CN 阿里菜鸟接口*/
		realTimeSnapshot_snapshotSourceList.add(new DerpBasic(DERP_INVENTORY.REALTIMESNAPSHOT_SNAPSHOTSOURCE_OP,"OP接口"));
		realTimeSnapshot_snapshotSourceList.add(new DerpBasic(DERP_INVENTORY.REALTIMESNAPSHOT_SNAPSHOTSOURCE_OFC,"OFC接口"));
		realTimeSnapshot_snapshotSourceList.add(new DerpBasic(DERP_INVENTORY.REALTIMESNAPSHOT_SNAPSHOTSOURCE_GSS,"GSS接口"));
		realTimeSnapshot_snapshotSourceList.add(new DerpBasic(DERP_INVENTORY.REALTIMESNAPSHOT_SNAPSHOTSOURCE_YWMS,"众邦云接口"));

		/**月结账单i_monthly_account---------------------------------*/
		/**状态：1-未结转 2-已结转*/
		monthlyAccount_stateList.add(new DerpBasic(DERP_INVENTORY.MONTHLYACCOUNT_STATE_1,"未结转"));
		monthlyAccount_stateList.add(new DerpBasic(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2,"已结转"));
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
			   Field[] fields = DERP_INVENTORY.class.getDeclaredFields();
		       for(Field field:fields){
		          if(field.getName().equals(listName)){
		        	 list = (ArrayList<DerpBasic>) field.get(new DERP_INVENTORY());
		          }
		       }
		   }catch(Exception e){}
		   return list;
	}
}
