package com.topideal.report.service.reporting.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.BuBusinessAddSaleNoshelfDetailsDao;
import com.topideal.dao.reporting.BuBusinessAddTransferNoshelfDetailsDao;
import com.topideal.dao.reporting.BuBusinessDecreaseSaleNoshelfDao;
import com.topideal.dao.reporting.BuBusinessInBadDetailsDao;
import com.topideal.dao.reporting.BuBusinessLocationAdjustmentDetailsDao;
import com.topideal.dao.reporting.BuBusinessOutBadDetailsDao;
import com.topideal.dao.reporting.BuBusinessTransferNoshelfDetailsDao;
import com.topideal.dao.reporting.BuInventorySummaryDao;
import com.topideal.dao.reporting.BuInventorySummaryItemDao;
import com.topideal.dao.reporting.BuLocationSummaryDao;
import com.topideal.dao.reporting.BuPurchaseNotshelfInfoDao;
import com.topideal.dao.reporting.BuSaleNotshelfInfoDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.entity.dto.BuInventorySummaryDTO;
import com.topideal.entity.dto.BuInventorySummaryItemDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.reporting.BuInventorySummaryService;
import com.topideal.report.tools.DownloadExcelUtil;

import net.sf.json.JSONObject;

@Service
public class BuInventorySummaryServiceImpl implements BuInventorySummaryService{
   
	/*@Autowired
	public InventorySummaryDao inventorySummaryDao;*/
	@Autowired
	public BuInventorySummaryDao buInventorySummaryDao;
	
	/*@Autowired
	public InventorySummaryItemDao inventorySummaryItemDao;*/
    @Autowired
    private BuInventorySummaryItemDao buInventorySummaryItemDao;// 事业部业务经销存表体
    @Autowired
    private BuBusinessInBadDetailsDao buBusinessInBadDetailsDao;// 事业部来货残次
    @Autowired
    private BuBusinessOutBadDetailsDao buBusinessOutBadDetailsDao;// 事业部出库残次
    @Autowired
    private BuBusinessTransferNoshelfDetailsDao buBusinessTransferNoshelfDetailsDao;// (事业部业务经分销)调拨在途
	@Autowired
    private BuBusinessAddSaleNoshelfDetailsDao buBusinessAddSaleNoshelfDetailsDao;//(事业部业务经销存)累计销售在途明细表
	@Autowired
	private BuSaleNotshelfInfoDao buSaleNotshelfInfoDao;//(事业部业务经销存)销售在途
	@Autowired
	private BuPurchaseNotshelfInfoDao buPurchaseNotshelfInfoDao;//(事业部业务经销存)采购在途
    @Autowired
    private BuBusinessDecreaseSaleNoshelfDao buBusinessDecreaseSaleNoshelfDao; //(事业部业务经分销)本期减少销售在途明细表 
    @Autowired
    private BuBusinessAddTransferNoshelfDetailsDao buBusinessAddTransferNoshelfDetailsDao;// (事业部业务经分销)累计调拨在途
    @Autowired
    private BuBusinessLocationAdjustmentDetailsDao buBusinessLocationAdjustmentDetailsDao;// (事业部业务经分销)库位类型调整明细    
    @Autowired
    private BuLocationSummaryDao buLocationSummaryDao;// (事业部业务经分销)库位类型调整明细 
    @Autowired
    private BusinessUnitDao businessUnitDao;// 事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;	
	
	/**
	 * 列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public BuInventorySummaryDTO listByPage(User user,BuInventorySummaryDTO dto) throws Exception {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return buInventorySummaryDao.getListByPage(dto);
	}


    /**
     * 生成商家、仓库、月份的excel文件报表
     */
    public String createExcel(FileTaskMongo task, String basePath) throws Exception {
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramJsonMap = jsonData;
        Integer merchantId = (Integer) paramJsonMap.get("merchant_id");
        String depotIds = (String) paramJsonMap.get("depot_id");
        String month = (String) paramJsonMap.get("own_month");
        formatter(TimeUtils.getNow(), "yyyy-MM-dd");
        Integer buIdInt = (Integer) paramJsonMap.get("bu_id");
        String parm = depotIds;
        Long buId = null;
		    if (buIdInt!=null) {
		    	buId = Long.valueOf(buIdInt.toString());
		    	parm=parm+"buId"+buId;
			}
		    List<Long> buList=null;
		    String userId="";
		    if (task.getUserId()!=null) {
	        	userId="/"+task.getUserId();
	        	buList = userBuRelMongoDao.getBuListByUser(task.getUserId());
			}
		    basePath = basePath+"/"+task.getTaskType()+"/"+merchantId+"/"+month+"/"+parm+userId;
		    System.out.println("商家Id="+merchantId+",仓库Id="+depotIds+",事业部Id="+buId+",月份="+month+"---生成excel文件开始----------");

		    List<Long> depotListIds=new ArrayList<Long>();
		    String[] Ids = {};
		    if (!StringUtils.isEmpty(depotIds))  Ids = depotIds.split(",");
			for(String id:Ids){
				if(!StringUtils.isEmpty(id)) depotListIds.add(Long.valueOf(id));
			}
				
			Map<String, Object> paramMap =new HashedMap<String, Object>();	
			paramMap.put("buList", buList);
			paramMap.put("merchantId", merchantId.longValue());
			paramMap.put("depotListIds", depotListIds);
			paramMap.put("ownMonth", month);
			paramMap.put("buId", buId);

    	    //查询商家、仓库、月份汇总报表
        	List<Map<String,Object>> summaryList = buInventorySummaryDao.listBuDepotMonth(paramMap);
        	int startIndex = 0;
        	int pageSize = 10000;//每页10000
        	int sheetPageLenth = 30;//每个sheet30页
        	int maxSize=300000;//每个文件存放最大记录数
        	Map<String, Object> itemMap = new HashMap<String, Object>();
        	itemMap.put("merchantId", merchantId.longValue());
        	itemMap.put("depotListIds", depotListIds);
        	itemMap.put("ownMonth", month);
        	itemMap.put("pageSize", pageSize);
        	itemMap.put("buId", buId);
        	itemMap.put("buList", buList);
        	//查询商家、仓库、月份入库明细
        	List<Map<String,Object>> inItemList = new ArrayList<Map<String,Object>>();
        	for(int i=0;i<sheetPageLenth;i++){//30页等于30万
        		itemMap.put("startIndex", startIndex);
        		List<Map<String,Object>> tempList = buInventorySummaryItemDao.listBuInListForMap(itemMap);
        		if(tempList==null||tempList.size()<=0) break;
        		inItemList.addAll(tempList);
        		startIndex += pageSize;
        		System.out.println("0-30万入startIndex="+startIndex);
        	}
        	 //查询商家、仓库、月份出库明细
        	List<Map<String,Object>> outItemList = new ArrayList<Map<String,Object>>();
        	startIndex = 0;
        	for(int i=0;i<sheetPageLenth;i++){//30页等于30万
        		itemMap.put("startIndex", startIndex);
        		List<Map<String,Object>> tempList = buInventorySummaryItemDao.listBuOutListForMap(itemMap);
        		if(tempList==null||tempList.size()<=0) break;
        		outItemList.addAll(tempList);
        		startIndex += pageSize;
        		System.out.println("0-30万出startIndex="+startIndex);
        	}
        	
        	
        	
        	//查询商家、仓库、月份损益明细
		    List<Map<String,Object>> profitLossList = buInventorySummaryItemDao.listBuProfitLossListForMap(itemMap);
		  /*  //查询商家、仓库、月份效期预警明细表
			List<Map<String,Object>> vaildList = inventoryValidDetailsDao.listInventoryVaildListForMap(itemMap);*/


        	//基础信息
        	String sheetName1 = "事业部进销存汇总";
        	String[] columns1={"仓库","事业部","商品名称","品牌","母品牌","货号","条码","8位码","理货单位","本月期初库存","本月正常品期初库存","本月残次品期初库存","本月入库","本月出库","来货残次","出库残次","来货短缺","好品损益","坏品损益","损益",
        			           "本月期末库存","本月正常品期末库存","本月残次品期末库存","正常品已过期量","仓库现存量","销售未确认","采购未上架","本期调拨在途"};
        	String[] keys1={"depot_name","bu_name","goods_name","brand_name","superior_parent_brand","goods_no","barcode","factory_no","unit","month_begin_num","month_begin_normal_num","month_begin_damaged_num","month_instorage_num","month_outstorage_num","month_in_bad_num","month_out_bad_num","in_damaged_num","profitloss_good_num","profitloss_damaged_num",
        			"month_profitloss_num","month_end_num","month_end_normal_num","month_end_damaged_num","month_normal_expire_num","available_num","month_sale_unconfirmed_num","month_purchase_notshelf_num","month_transfer_notshelf_num"};
        	
        	String sheetName1A = "库位进销存汇总表";
        	String[] columns1A={"公司","事业部","仓库名称","报表月份","母品牌","标准品牌","条形码","商品名称",
        			"库位类型","本月期初数量","本月入库数量","本月出库数量","本月损益","本月调整量","本月期末数量",
        			"本月好品期末量","本月坏品期末量","累计采购在途","累计调拨在途","理货单位"};
        	String[] keys1A={"merchant_name","bu_name","depot_name","month","superior_parent_brand","brand_name","barcode","goods_name",
        			"stock_location_type_name","month_begin_num","month_instorage_num","month_outstorage_num","month_profitloss_num","month_adjustment_num","month_end_num",
        			"month_end_normal_num","month_end_damaged_num","add_purchase_notshelf_num","add_transfer_notshelf_num","unit"};
 	
        	List<Map<String, Object>> locationSummaryListForMap = buLocationSummaryDao.getLocationSummaryListForMap(itemMap);
        	for (Map<String, Object> map : locationSummaryListForMap) {
        		String unit = (String) map.get("unit");
        		String unitLabel = DERP.getLabelByKey(DERP.inventory_unitList,unit);
        		map.put("unit", unitLabel);
			}																		

        	
        	
        	
        	
        	//入库详情
        	String sheetName2 = "入库详情";
        	String[] columns2={"事业部","来源单号","业务单号","单据类型","入库仓库","入库时间","合同号","PO号","供应商","母品牌","商品货号","条码","标准条码","商品名称","入库数量","理货单位","备注","库位类型"};
        	String[] keys2={"bu_name","code","business_no","thing_name","depot_name","divergence_date","contract_no","po_no","customer_name","superior_parent_brand","goods_no","barcode","commbarcode","goods_name","num","unit","remark","stock_location_type_name"};
        	
        	//出库详情
        	String sheetName3 = "出库详情";
        	String[] columns3 = {"事业部","来源单号","业务单号","平台单号","平台名称","店铺名称","单据类型","出库仓库","出库时间","合同号","PO号","客户","入库仓库","母品牌","商品货号","条码","标准条码","商品名称","出库数量","理货单位","备注","库位类型"};
        	String[] keys3 = {"bu_name","code","business_no","ex_order_id","store_platform_name","shop_name","thing_name","depot_name","divergence_date","contract_no","po_no","customer_name","in_depot_name","superior_parent_brand","goods_no","barcode","commbarcode","goods_name","num","unit","remark","stock_location_type_name"};
        	

        	String sheetName3A = "来货残次";
        	String[] columns3A = {"母品牌","商品货号","条码","商品名称","单据类型","残次数量","入库仓库","事业部","入库时间","来源单号","业务单号","标准条码","理货单位","库位类型"};        	       	
        	String[] keys3A = {"superior_parent_brand","goods_no","barcode","goods_name","order_type","num","in_depot_name","bu_name","deliver_date","order_code","in_order_code","commbarcode","tallying_unit","stock_location_type_name"};
        	
        	List<Map<String, Object>> listInBadDetailsList = buBusinessInBadDetailsDao.listInBadDetailsMap(paramMap);
        	String sheetName3B = "出库残次";
        	String[] columns3B = {"母品牌","商品货号","条码","商品名称","单据类型","残次数量","出库仓库","事业部","出库时间","来源单号","业务单号","标准条码","理货单位","库位类型"};
        	String[] keys3B = {"superior_parent_brand","goods_no","barcode","goods_name","order_type","num","out_depot_name","bu_name","deliver_date","order_code","out_order_code","commbarcode","tallying_unit","stock_location_type_name"};
        	List<Map<String, Object>> listOutBadDetailsList = buBusinessOutBadDetailsDao.listOutBadDetailsMap(paramMap);
        	
        	
        	//采购未上架
        	String sheetName5 = "采购未上架";
        	String[] columns5 = {"事业部","采购订单号","入库仓库","PO号","录单日期","发票日期","订单状态","未上架数量","母品牌","商品名称","条码","标准条码","理货单位","库位类型"};
        	String[] keys5 = {"bu_name","code","depot_name","po_no","order_create_date","draw_invoice_date","status","notshelf_num","superior_parent_brand","goods_name","barcode","commbarcode","unit","stock_location_type_name"};
        	//采购未上架
        	List<Map<String, Object>> purchaseList = buPurchaseNotshelfInfoDao.listBuForMap(itemMap);
        	
        	// 调拨在途
        	String sheetName5A = "调拨在途";
        	String[] columns5A = {"事业部","调拨订单号","调拨出单号","调出仓库","调入仓库","出库时间","归属月份","在途数量","商品名称","母品牌","商品货号","条码","标准条码","PO号","理货单位","库位类型"};
        	String[] keys5A = {"bu_name","order_code","out_order_code","out_depot_name","in_depot_name","deliver_date","month","num","goods_name","superior_parent_brand","goods_no","barcode","commbarcode","po_no","tallying_unit","stock_location_type_name"};
        	List<Map<String, Object>> listTransferNoshelfDetailsList = buBusinessAddTransferNoshelfDetailsDao.listBuTransferNoshelfDetailsMap(paramMap);

        	
        	//损益明细表
			String sheetName6 = "损益明细表";
			String[] columns6 = {"事业部","调整单号", "业务类别", "来源单据号","母品牌","商品货号", "商品名称", "商品条码","标准条码","调整类型", "调整数量", "调整时间", "调整仓库", "批次号", "是否坏品", "生产日期", "失效日期","库位类型"};
			String[] keys6 = {"bu_name","code", "thing_name", "business_no", "superior_parent_brand","goods_no", "goods_name", "barcode", "commbarcode","operate_type", "num", "divergence_date", "depot_name", "batch_no", "is_worn", "production_date", "overdue_date","stock_location_type_name"};

			//(业务经分销)本期减少销售在途明细表
			String sheetName8 = "本期减少销售在途明细";
			String[] columns8 = {"事业部","条码","标准条码","商品名称","母品牌","商品货号","销售类型","销售订单号","出库单号","出库仓库","PO号","订单归属月份","发货时间","上架时间","本期上架量","本期残次量","本期少货量","理货单位"};
			String[] keys8 = {"bu_name","barcode","commbarcode","goods_name","superior_parent_brand","goods_no","order_type","order_code","out_order_code","depot_name","po_no","order_own_month","deliver_date","shelf_date","shelf_num","damaged_num","lack_num","tallying_unit"};				
			List<Map<String, Object>> decreaseSaleNoshelfList=buBusinessDecreaseSaleNoshelfDao.listBuDecreaseSaleNoshelfMap(paramMap);
			
			//销售未确认
        	String sheetName4 = "销售未确认";
        	String[] columns4 = {"事业部","出库单号","出库仓库","PO号","销售订单号","客户名称","发货时间","未确认数量","母品牌","商品名称","条码","标准条码","理货单位","销售类型"};
        	String[] keys4 = {"bu_name","out_order_code","depot_name","po_no","order_code","customer_name","deliver_date","num","superior_parent_brand","goods_name","barcode","commbarcode","tallying_unit","order_type"};
        	//销售未确认
        	List<Map<String, Object>> saleList = buBusinessAddSaleNoshelfDetailsDao.listBuSaleNoshelfDetailsMap(itemMap);
			// (业务经销存)累计销售在途明细表
			String sheetName9 = "累计销售在途明细";													
			String[] columns9 = {"事业部","条码","标准条码","商品名称","母品牌","商品货号","销售类型","销售订单号","客户名称","出库单号","出库仓库","PO号","订单归属月份","发货时间","累计在途量","理货单位"};
			String[] keys9 = {"bu_name","barcode","commbarcode","goods_name","superior_parent_brand","goods_no","order_type","order_code","customer_name","out_order_code","depot_name","po_no","order_own_month","deliver_date","num","tallying_unit"};			
			List<Map<String, Object>> addSaleNoshelfDetailsList = buBusinessAddSaleNoshelfDetailsDao.listBuAddSaleNoshelfDetailsMap(paramMap);

			
			// 累计调拨在途
        	String sheetName10 = "累计调拨在途";
        	String[] columns10 = {"事业部","调拨订单号","调拨出单号","调出仓库","调入仓库","出库时间","归属月份","在途数量","商品名称","母品牌","商品货号","条码","标准条码","PO号","理货单位","库位类型"};
        	String[] keys10 = {"bu_name","order_code","out_order_code","out_depot_name","in_depot_name","deliver_date","month","num","goods_name","superior_parent_brand","goods_no","barcode","commbarcode","po_no","tallying_unit","stock_location_type_name"};
        	List<Map<String, Object>> listAddTransferNoshelfDetailsList = buBusinessAddTransferNoshelfDetailsDao.listBuAddTransferNoshelfDetailsMap(paramMap);

			
        	//采购未上架
        	String sheetName11 = "累计采购未上架";
        	String[] columns11 = {"事业部","采购订单号","入库仓库","PO号","录单日期","发票日期","完结日期","归属日期","订单状态","未上架数量","母品牌","商品名称","条码","标准条码","理货单位","库位类型"};
        	String[] keys11 = {"bu_name","code","depot_name","po_no","order_create_date","draw_invoice_date","end_date","attribution_date","status","notshelf_num","superior_parent_brand","goods_name","barcode","commbarcode","unit","stock_location_type_name"};
        	List<Map<String, Object>> purchaseAddList = buPurchaseNotshelfInfoDao.getPurchaseAddForMap(itemMap);
        	
        	//
        	String sheetName12 = "库位类型调整明细";
        	String[] columns12 = {"事业部","库位调整单号","调整仓库","调整单据类型","客户名称","平台","订单号","母品牌",
        			"标准品牌","条码","商品名称","调整数量","库存类型","库位类型","理货单位"};
        	String[] keys12 = {"bu_name","order_code","depot_name","order_type","customer_name","store_platform_name","external_code","superior_parent_brand",
        			"brand_name","barcode","goods_name","num","inventory_type","stock_location_type_name","unit"};
        	List<Map<String, Object>> locationAdjustmentList = buBusinessLocationAdjustmentDetailsDao.getLocationAdjustmentDetailsForMap(itemMap);
        	for (Map<String, Object> map : locationAdjustmentList) {
        		String orderType = (String) map.get("order_type");
        		String orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.locationAdjustmentOrder_orderTypeList,orderType);
        		map.put("order_type", orderTypeLabel);
        		String inventoryType = (String) map.get("inventory_type");
        		String inventoryTypeLabel = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList,inventoryType);
        		map.put("inventory_type", inventoryTypeLabel);
        		String unit = (String) map.get("unit");
        		String unitLabel = DERP_INVENTORY.getLabelByKey(DERP.inventory_unitList,unit);
        		map.put("unit", unitLabel);
        		}	
        	
        	
        	
			//生成表格
        	SXSSFWorkbook wb = DownloadExcelUtil.createBuExcelSXSS5(sheetName1, columns1, keys1,summaryList,
        			sheetName1A, columns1A, keys1A,locationSummaryListForMap,       			
        			sheetName2, columns2, keys2, inItemList,
        			sheetName3, columns3, keys3, outItemList,
        			sheetName3A, columns3A, keys3A,listInBadDetailsList,
        			sheetName3B, columns3B, keys3B,listOutBadDetailsList,
        			sheetName4, columns4, keys4, saleList,
        			sheetName5, columns5, keys5, purchaseList,
        			sheetName5A, columns5A, keys5A, listTransferNoshelfDetailsList,
					sheetName6, columns6, keys6, profitLossList,
					//sheetName7, columns7, keys7, vaildList,
					sheetName8, columns8, keys8, decreaseSaleNoshelfList,
					sheetName9, columns9, keys9, addSaleNoshelfDetailsList,
					sheetName10, columns10, keys10, listAddTransferNoshelfDetailsList,
					sheetName11, columns11, keys11,purchaseAddList,
					sheetName12, columns12, keys12,locationAdjustmentList
					);
        	 System.out.println("开始写第一个文件");
        	//删除目录下的子文件
        	DownloadExcelUtil.deleteFile(basePath);
        	//创建目录
        	new File(basePath).mkdirs();
        	String remarks = task.getRemark();
        	String remark="";
        	List<String> remarkList = Arrays.asList(remarks.split(","));
        	if (remarkList.size()>5) {
        		for (int i = 0; i < remarkList.size(); i++) {
        			if (i<5) {
        				remark=remark+remarkList.get(i)+",";
					}
				}
        		remark=remark+"...等";
			}else {
				remark=remarks;
			}
        	
        	//写出文件
        	String fileName =remark +".xlsx";
        	FileOutputStream fileOut=new FileOutputStream(basePath+"/"+fileName);
        	wb.write(fileOut);
            fileOut.close();
            wb=null;
            //inItemList=null;
            //outItemList=null;
            System.out.println("第一个文件创建结束");
           
             //统计明细数量超过30万则另外生成过文件存储
        	Integer inCount = buInventorySummaryItemDao.listBuInListCount(itemMap);
        	if(inCount.intValue()>maxSize){
        		int startIndex2 = maxSize;
        		int k2=2;//第几个文件
        		while(startIndex2<inCount.intValue()){
        			createExcelInItem2(itemMap,k2,month,basePath,startIndex2,pageSize,sheetName2,columns2,keys2,sheetPageLenth);
        			k2++;
        			startIndex2 = startIndex2+maxSize;
        		}
        	}
        	Integer outCount = buInventorySummaryItemDao.listBuOutListCount(itemMap);
        	if(outCount.intValue()>maxSize){
        		int startIndex3 = maxSize;
        		int k3=2;
        		while(startIndex3<outCount.intValue()){
        			createExcelOutItem2(itemMap,k3,month,basePath,startIndex3,pageSize,sheetName3,columns3,keys3,sheetPageLenth);
        			k3++;
        			startIndex3 = startIndex3+maxSize;
        		}
        	}
            System.out.println("商家Id="+merchantId+",仓库Id="+depotIds+",月份="+month+"---生成事业部excel文件结束----------");
            return basePath;
           
	}
	private void formatter(Timestamp now, String string) {
		// TODO Auto-generated method stub
		
	}

	public void createExcelInItem2(Map<String, Object> itemMap,int k,String month,String path,
			Integer startIndex,Integer pageSize,String sheetName2,String[] columns2,String[] keys2,
			int sheetPageLenth){
		try {
	    	//查询商家、仓库、月份入库明细
	    	List<Map<String,Object>> inItemList2 = new ArrayList<Map<String,Object>>();
			itemMap.put("startIndex", startIndex);
			for(int i=0;i<sheetPageLenth;i++){
				List<Map<String,Object>> tempList = buInventorySummaryItemDao.listBuInListForMap(itemMap);
				if(tempList==null||tempList.size()<=0) break;
				inItemList2.addAll(tempList);
				startIndex += pageSize;
				itemMap.put("startIndex", startIndex);
				System.out.println("30万+入startIndex="+startIndex);
			}
	    	 System.out.println("开始写第"+k+"个文件");
	    	//生成第二个文件
	    	SXSSFWorkbook wb2 = ExcelUtilXlsx.createSXSSExcel(sheetName2, columns2, keys2, inItemList2);
	    	//写出文件
	    	String fileName2 = "("+k+")入库明细"+month+".xlsx";
	    	FileOutputStream fileOut2=new FileOutputStream(path+"/"+fileName2);
	    	wb2.write(fileOut2);
	    	fileOut2.close();
	    	wb2=null;
	        inItemList2=null;
	    	System.out.println("第"+k+"个文件创建结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void createExcelOutItem2(Map<String, Object> itemMap,int k,String month,String path,
			Integer startIndex,Integer pageSize,String sheetName3,String[] columns3,String[] keys3,
			int sheetPageLenth){
		try {
	    	//查询商家、仓库、月份入库明细
	    	List<Map<String,Object>> outItemList2 = new ArrayList<Map<String,Object>>();
			itemMap.put("startIndex", startIndex);
			for(int i=0;i<sheetPageLenth;i++){
				List<Map<String,Object>> tempList = buInventorySummaryItemDao.listBuOutListForMap(itemMap);
				if(tempList==null||tempList.size()<=0) break;
				outItemList2.addAll(tempList);
				startIndex += pageSize;
				itemMap.put("startIndex", startIndex);
				System.out.println("30万+出startIndex="+startIndex);
			}
	    	 System.out.println("开始写第"+k+"事业部个文件");
	    	//生成第二个文件
	    	SXSSFWorkbook wb2 = ExcelUtilXlsx.createSXSSExcel(sheetName3, columns3, keys3, outItemList2);
	    	//写出文件
	    	String fileName2 = "("+k+")出库明细"+month+".xlsx";
	    	FileOutputStream fileOut2=new FileOutputStream(path+"/"+fileName2);
	    	wb2.write(fileOut2);
	    	fileOut2.close();
	    	wb2=null;
	    	outItemList2=null;
	    	System.out.println("第"+k+"个事业部文件创建结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<BuInventorySummaryItemDTO> listBuItem(BuInventorySummaryItemDTO model) throws Exception {
		return buInventorySummaryItemDao.getBuList(model);
	}

	
	@Override
	public BusinessUnitModel getBusinessUnit(Long buId) throws Exception {
		BusinessUnitModel buModel = businessUnitDao.searchById(buId);
		return buModel;
	}
	
}
