package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.dao.purchase.PurchaseWarehouseItemDao;
import com.topideal.dao.sale.SaleOrderDao;
import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.dao.sale.SaleOutDepotDao;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.BusinessUnitMongoDao;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.DepartmentInfoMongoDao;
import com.topideal.mongo.entity.BusinessUnitMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.DepartmentInfoMongo;
import com.topideal.service.timer.InternalAmountQuantityVarianceService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.smurfs.tools.gateway.client.GatewayHttpClient;
import priv.smurfs.tools.gateway.common.ApiResponse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 内部交易金额数量差异提醒定时任务
 * @author huangrenya
 **/
@Service
public class InternalAmountQuantityVarianceServiceImpl implements InternalAmountQuantityVarianceService {

    private static final Logger LOGGER = Logger.getLogger(InternalAmountQuantityVarianceServiceImpl.class);

    @Autowired
    private DepartmentInfoMongoDao departmentInfoMongoDao;
    @Autowired
    private BusinessUnitMongoDao businessUnitMongoDao ;
    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private SaleOrderDao saleOrderDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
//    @Autowired
//    private PurchaseAnalysisDao purchaseAnalysisDao;
    @Autowired
    private SaleOutDepotDao saleOutDepotDao;
    @Autowired
    private PurchaseOrderItemDao purchaseOrderItemDao;
    @Autowired
    private SaleOrderItemDao saleOrderItemDao;
    @Autowired
    private PurchaseWarehouseItemDao purchaseWarehouseItemDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_20100000001,model=DERP_LOG_POINT.POINT_20100000001_Label)
    public void getInternalAmountQuantityOrder(String json, String keys, String topics, String tags) throws Exception {

        //查询指定的部门（宝信事业部E070000、保健品事业部E060000、母婴事业部E080000）
        List<String> departmentCode=new ArrayList<>();
        departmentCode.add("E070000");
        departmentCode.add("E060000");
        departmentCode.add("E080000");
        //根据部门id获取事业部
        List<BusinessUnitMongo> businessUnitList=getDepartmentByBuId(departmentCode);
        //获取事业部id
        List<Long> businessUnitId=businessUnitList.stream().map(BusinessUnitMongo::getBusinessUnitId).collect(Collectors.toList());

        departmentCode.clear();
        departmentCode.add("E070000");
        List<BusinessUnitMongo> businessDiferentUnitList=getDepartmentByBuId(departmentCode);
        Map<Long,BusinessUnitMongo> baoBusiness=businessDiferentUnitList.stream().collect(Collectors.toMap(BusinessUnitMongo::getBusinessUnitId, s -> s,(k1,k2)->k1));

        departmentCode.clear();
        departmentCode.add("E060000");
        businessDiferentUnitList=getDepartmentByBuId(departmentCode);
        Map<Long,BusinessUnitMongo> baoJianBusiness=businessDiferentUnitList.stream().collect(Collectors.toMap(BusinessUnitMongo::getBusinessUnitId, s -> s,(k1,k2)->k1));

        departmentCode.clear();
        departmentCode.add("E080000");
        businessDiferentUnitList=getDepartmentByBuId(departmentCode);
        Map<Long,BusinessUnitMongo> muYingBusiness=businessDiferentUnitList.stream().collect(Collectors.toMap(BusinessUnitMongo::getBusinessUnitId, s -> s,(k1,k2)->k1));


        Map<String, Object> customerMap = new HashMap<>() ;
        customerMap.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_2) ;
        customerMap.put("type", DERP_SYS.CUSTOMERINFO_TYPE_1) ;
        customerMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;
        List<CustomerInfoMogo> supplierList = customerInfoMongoDao.findAll(customerMap);
        //获取供应商的内部客户为是的供应商id
        List<Long> supplierId=supplierList.stream().map(CustomerInfoMogo::getCustomerid).collect(Collectors.toList());
        //根据供应商封装数据
        Map<Long, CustomerInfoMogo> supplierInnerId = supplierList.stream().collect(Collectors.toMap(CustomerInfoMogo::getCustomerid, s -> s,(k1,k2)->k1));;

        customerMap.clear();
        customerMap.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_1) ;
        customerMap.put("type", DERP_SYS.CUSTOMERINFO_TYPE_1) ;
        customerMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;
        List<CustomerInfoMogo> customerList = customerInfoMongoDao.findAll(customerMap);
        //获取客户的内部公司为是的客户id
        List<Long> customerId=customerList.stream().map(CustomerInfoMogo::getCustomerid).collect(Collectors.toList());
        //根据客户封装数据
        Map<Long,CustomerInfoMogo> customerInnerId=customerList.stream().collect(Collectors.toMap(CustomerInfoMogo::getCustomerid,s -> s,(k1,k2)->k1));


        //查询采购订单创建日期在当前日期前60天内，以及供应商为内部公司
        Map<String,Object> queryMap=new HashMap<>();
        queryMap.put("buIds",businessUnitId);
        queryMap.put("supplierIds",supplierId);
        queryMap.put("type","1");
        List<PurchaseOrderModel> purchaseOrderList=purchaseOrderDao.getPurchaseOrderByParams(queryMap);


        JSONArray jsonArrayBaoJian=new JSONArray();//存储宝信部门发送邮件的数据格式
        JSONArray jsonArrayBaoXin=new JSONArray();//存储保健部门发送邮件的数据格式
        JSONArray jsonArrayMuYing=new JSONArray();//存储母婴部门发送邮件的数据格式

        List<PurchaseOrderModel> newPurchaseOrder=new ArrayList<>();//存储已经过滤掉销售单不存在的po单据
        Map<String,SaleOrderModel> salePoAlready=new HashMap<>();


        /*
         *   采购订单的查询逻辑
         */

        //1、先过滤掉采购单的PO在对应的销售公司是否存在
        for(PurchaseOrderModel orderModel:purchaseOrderList){
            CustomerInfoMogo supplierInner=supplierInnerId.get(orderModel.getSupplierId());

            //过滤掉销售单po不相同
            Map<String,Object> querySale=new HashMap<>();
            querySale.put("poNo",orderModel.getPoNo());
            querySale.put("merchantId",supplierInner.getInnerMerchantId());
            List<SaleOrderModel> saleOrder=saleOrderDao.getSaleOrderByParams(querySale);

            if(saleOrder==null || saleOrder.size() == 0){
                JSONArray array=new JSONArray();
                JSONObject jsonObject= new JSONObject();
                jsonObject.put("msg",orderModel.getMerchantName()+"公司存在采购单"+supplierInner.getInnerMerchantName()+"不存在销售单PO号："+orderModel.getPoNo());
                array.add(jsonObject);
                JSONObject newObject=encapsulationData(orderModel,null,array);
                getJsonArrayByBuId(orderModel.getBuId(),newObject,jsonArrayBaoXin,jsonArrayBaoJian,jsonArrayMuYing,baoBusiness,baoJianBusiness,muYingBusiness);
                continue;
            }
            if(saleOrder.size() > 1){
                JSONArray array=new JSONArray();
                JSONObject jsonObject= new JSONObject();
                jsonObject.put("msg",orderModel.getMerchantName()+"公司存在采购单"+supplierInner.getInnerMerchantName()+"存在多个销售单PO号："+orderModel.getPoNo());
                array.add(jsonObject);
                JSONObject newObject=encapsulationData(orderModel,null,array);
                getJsonArrayByBuId(orderModel.getBuId(),newObject,jsonArrayBaoXin,jsonArrayBaoJian,jsonArrayMuYing,baoBusiness,baoJianBusiness,muYingBusiness);
                continue;
            }
            SaleOrderModel salePoModel=saleOrder.get(0);
            newPurchaseOrder.add(orderModel);
            salePoAlready.put(orderModel.getCode(),salePoModel);
        }

        //2、如果采购单的PO在对应的销售公司存在；则采购单PO对应销售单PO匹配；
        for(PurchaseOrderModel orderModel:newPurchaseOrder){

            SaleOrderModel saleOrderModel=salePoAlready.get(orderModel.getCode());

            JSONArray array=new JSONArray();
            JSONObject jsonObject= new JSONObject();

            //2.1 采购单的PO检查采购单的币种与对应销售单的币种是否一致，若不一致，记录错误，提示：“币种不一致”
            if(!orderModel.getCurrency().equals(saleOrderModel.getCurrency())){
                jsonObject.put("msg","币种不一致");
                array.add(jsonObject);
            }

            //2.2 检查销售单的内部公司是否为采购单的公司主体
            CustomerInfoMogo customerInner=customerInnerId.get(saleOrderModel.getCustomerId());
            if(!customerInner.getInnerMerchantId().equals(orderModel.getMerchantId())){
                jsonObject= new JSONObject();
                jsonObject.put("msg","客户不一致");
                array.add(jsonObject);
            }

            //2.3 采购单的状态与对应销售单的状态是否一致
            //场景1：当采购订单状态为已审核、若销售订单不为已审核，记录错误，提示：“XX公司采购单已审核对应销售订单不为已审核”；
            if(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(orderModel.getStatus())){
                if(!DERP_ORDER.SALEORDER_STATE_003.equals(saleOrderModel.getState())){
                    jsonObject= new JSONObject();
                    jsonObject.put("msg",orderModel.getMerchantName()+"公司采购单已审核对应销售订单不为已审核");
                    array.add(jsonObject);
                }
            }

            //场景2：若采购订单状态为已入库，检查采购单累计的入库数量对应销售单累计的出库数量是否一致，若不一致，记录错误，提示：“XX公司采购单入库数量对应XX公司销售订单出库数量不一致”；
            if(DERP_ORDER.PURCHASEORDER_STATUS_007.equals(orderModel.getStatus())){
                //查询入库数量
//                PurchaseAnalysisModel queryPurchaseAnalysis = new PurchaseAnalysisModel() ;
//                queryPurchaseAnalysis.setOrderId(orderModel.getId());
//                List<PurchaseAnalysisModel> tempAnalysisList = purchaseAnalysisDao.list(queryPurchaseAnalysis);
//                //获取已入库的数量
//                Integer warehouseNum = tempAnalysisList.stream().filter(tempAnalysis -> tempAnalysis.getWarehouseId() != null).mapToInt(PurchaseAnalysisModel::getWarehouseNum).sum();
                //根据采购id获取已入库数量
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("purchaseOrderId", orderModel.getId());
                paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
                List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
                Integer warehouseNum = 0;
                if(numList != null && numList.size() > 0){
                    BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//已入库数量
                    warehouseNum = queryNum.intValue();
                }
                //获取销售出库数量
                SaleOutDepotDTO saleOutDTO = new SaleOutDepotDTO();
                saleOutDTO.setSaleOrderCode(saleOrderModel.getCode());
                List<SaleOutDepotDTO> saleOutDetailList = saleOutDepotDao.queryDTODetailsList(saleOutDTO);
                Integer transferNum = saleOutDetailList.stream().filter(s -> s.getTransferNum() != null).mapToInt(SaleOutDepotDTO::getTransferNum).sum();
                Integer wornNum = saleOutDetailList.stream().filter(s -> s.getWornNum() != null).mapToInt(SaleOutDepotDTO::getWornNum).sum();
                Integer totalOutNum = transferNum + wornNum;//累计销售出库数量

                if(warehouseNum.intValue()!=totalOutNum.intValue()){
                    jsonObject= new JSONObject();
                    jsonObject.put("msg",orderModel.getMerchantName()+"公司采购单入库数量对应"+saleOrderModel.getMerchantName()+"公司销售订单出库数量不一致");
                    array.add(jsonObject);
                }
            }

            //2.4 按采购单的PO+SKU（若存在多个货号则逐一匹配），检查对应销售订单的SKU价格是否一致，若不一致，记录错误，XXX货号采购价：2.1，销售价：2.7“单价不一致”；
            PurchaseOrderItemModel purchaseItemModel=new PurchaseOrderItemModel();
            purchaseItemModel.setPurchaseOrderId(orderModel.getId());
            List<PurchaseOrderItemModel> purchaseItemList=purchaseOrderItemDao.list(purchaseItemModel);

            SaleOrderItemModel saleOrderItemModel=new SaleOrderItemModel();
            saleOrderItemModel.setOrderId(saleOrderModel.getId());
            List<SaleOrderItemModel> saleItemList=saleOrderItemDao.list(saleOrderItemModel);
            //获取销售单所有商品
            Set<String> barcodeSet = saleItemList.stream().map(e -> e.getBarcode()).collect(Collectors.toSet());
            //根据商品条码进行分组
            Map<String, List<SaleOrderItemModel>> saleOrderItemMap = saleItemList.stream().collect(Collectors.groupingBy(SaleOrderItemModel::getBarcode));

            for(PurchaseOrderItemModel purchaseItem:purchaseItemList){
                Boolean goodFlag=barcodeSet.contains(purchaseItem.getBarcode());
                if(!goodFlag){
                    jsonObject= new JSONObject();
                    jsonObject.put("msg","销售订单找不到"+purchaseItem.getBarcode()+"条形码");
                    array.add(jsonObject);
                    continue;
                }
                List<SaleOrderItemModel> saleItem=saleOrderItemMap.get(purchaseItem.getBarcode());
                if(saleItem.size() > 1){
                    jsonObject= new JSONObject();
                    jsonObject.put("msg","销售订单存在多个条码:"+purchaseItem.getBarcode());
                    array.add(jsonObject);
                    continue;
                }
                SaleOrderItemModel saleGoods=saleItem.get(0);

                if(purchaseItem.getNum().intValue()!=saleGoods.getNum().intValue()){
                    jsonObject= new JSONObject();
                    jsonObject.put("msg",purchaseItem.getBarcode()+"条码采购数量："+purchaseItem.getNum()+",销售数量："+saleGoods.getNum()+"；商品数量不一致");
                    array.add(jsonObject);
                }

                BigDecimal purchasePrice=purchaseItem.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal salePrice=saleGoods.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
                if(!(purchasePrice.compareTo(salePrice)==0)){
                    jsonObject= new JSONObject();
                    jsonObject.put("msg",purchaseItem.getBarcode()+"条码采购价："+purchasePrice.toPlainString()+",销售价："+salePrice.toPlainString()+"；单价不一致");
                    array.add(jsonObject);
                }
            }


            if(array.size()>0){
                JSONObject newObject=encapsulationData(orderModel,saleOrderModel,array);
                getJsonArrayByBuId(orderModel.getBuId(),newObject,jsonArrayBaoXin,jsonArrayBaoJian,jsonArrayMuYing,baoBusiness,baoJianBusiness,muYingBusiness);
            }
        }



        /*
        * 、销售订单的查询逻辑：
            1、销售订单客户为内部公司，判断这个PO在对应的采购公司是否存在，如果不存在，记录错误，提示：“XX公司存在销售单XX不存在采购单PO号：0000”
        * */

        //查询销售订单创建日期在当前日期前60天内，以及客户为内部公司
        queryMap.remove("supplierIds");
        queryMap.put("customerIds",customerId);
        queryMap.put("type","1");
        List<SaleOrderModel> saleOrderList=saleOrderDao.getSaleOrderByParams(queryMap);

        for(SaleOrderModel saleOrderModel:saleOrderList){
            CustomerInfoMogo customerInner=customerInnerId.get(saleOrderModel.getCustomerId());
            //过滤掉采购单po不相同的
            Map purchaseMap=new HashMap();
            purchaseMap.put("poNo",saleOrderModel.getPoNo());
            purchaseMap.put("merchantId",customerInner.getInnerMerchantId());
            List<PurchaseOrderModel> purchaseOrder=purchaseOrderDao.getPurchaseOrderByParams(purchaseMap);

            if(purchaseOrder==null || purchaseOrder.size() == 0){
                JSONArray array=new JSONArray();
                JSONObject jsonObject= new JSONObject();
                jsonObject.put("msg",saleOrderModel.getMerchantName()+"公司存在销售单"+customerInner.getInnerMerchantName()+"不存在采购单PO号："+saleOrderModel.getPoNo());
                array.add(jsonObject);
                JSONObject newObject=encapsulationData(null,saleOrderModel,array);
                getJsonArrayByBuId(saleOrderModel.getBuId(),newObject,jsonArrayBaoXin,jsonArrayBaoJian,jsonArrayMuYing,baoBusiness,baoJianBusiness,muYingBusiness);
                continue;
            }
        }


        LOGGER.info("-----------------发送邮件提醒开始----------------------");

        if(jsonArrayBaoXin!=null && jsonArrayBaoXin.size()>0) {
            getSendEmailByDepartment(jsonArrayBaoXin, ApolloUtil.baoxinEmail);
            LOGGER.info("-----------------宝信事业部门发送邮件提醒结束----------------------");
        }

        if(jsonArrayBaoJian!=null && jsonArrayBaoJian.size()>0) {
            getSendEmailByDepartment(jsonArrayBaoJian, ApolloUtil.baojianEmail);
            LOGGER.info("-----------------保健品事业部门发送邮件提醒结束----------------------");
        }

        if(jsonArrayMuYing!=null && jsonArrayMuYing.size()>0) {
            getSendEmailByDepartment(jsonArrayMuYing, ApolloUtil.muyingEmail);
            LOGGER.info("-----------------母婴事业部门发送邮件提醒结束----------------------");
        }

    }

    private void getSendEmailByDepartment(JSONArray jsonArray,String email){
        ReminderEmailUserDTO emailDto = new ReminderEmailUserDTO();
        emailDto.setAttArray(jsonArray);
        JSONObject jsonData = JSONObject.fromObject(emailDto);

        LOGGER.info("参数："+jsonData.toString());

        JSONObject jsonObject = new JSONObject();// 推送内容
        jsonObject.put("paramJson", jsonData);
        jsonObject.put("mailCode", SmurfsAPICodeEnum.EMAIL_W035.getCode());
        jsonObject.put("recipients", email);

        // 调用外部接口发送邮件
        String resultMsg = SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_EMAIL);
//        String resultMsg = sendSc1(jsonObject, SmurfsAPIEnum.SMURFS_EMAIL);
        LOGGER.info("蓝精灵返回结果："+resultMsg);
        LOGGER.info("-----------------发送邮件提醒结束----------------------");
    }


    @SuppressWarnings("static-access")
    public String sendSc1(JSONObject json, SmurfsAPIEnum apiEnum){
        //GatewayHttpClient.newInstance(host, appKey, secret)
        try {
            GatewayHttpClient gatewayHttpClient =
                    GatewayHttpClient.newInstance("gateway.smurfs.topideal.mobi", "10002", "D8478AA375024F218902E72B04D33F58");
            //发送请求
            ApiResponse response =gatewayHttpClient.send(apiEnum.getApiCode(),apiEnum.getV(),json.toString());
            String resultJson = response.getResultJson();
            LOGGER.info("蓝精灵返回结果："+resultJson);
            return resultJson;

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("蓝精灵返回异常:"+e.getMessage());
            LOGGER.error("蓝精灵返回异常:"+e.getMessage());
            return null;
        }
    }


    /**
     * 封装数据
     * @param purchaseOrderModel
     * @return
     */
    private JSONObject encapsulationData(PurchaseOrderModel purchaseOrderModel,SaleOrderModel saleOrderModel,JSONArray jsonArray){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("purchaseMerchantName",purchaseOrderModel!=null?toNullStr(purchaseOrderModel.getMerchantName()):"");
        jsonObject.put("saleMerchantName",saleOrderModel!=null?toNullStr(saleOrderModel.getMerchantName()):"");
        jsonObject.put("purchaseBuName", purchaseOrderModel!=null?toNullStr(purchaseOrderModel.getBuName()):"");
        jsonObject.put("saleBuName", saleOrderModel!=null?toNullStr(saleOrderModel.getBuName()):"");
        jsonObject.put("supplier", purchaseOrderModel!=null?toNullStr(purchaseOrderModel.getSupplierName()):"");
        jsonObject.put("customer", saleOrderModel!=null?toNullStr(saleOrderModel.getCustomerName()):"");
        jsonObject.put("purchaseName", purchaseOrderModel!=null?toNullStr(purchaseOrderModel.getCreateName()):"");
        jsonObject.put("saleName", saleOrderModel!=null?toNullStr(saleOrderModel.getCreateName()):"");
        jsonObject.put("purchaseCode",purchaseOrderModel!=null?toNullStr(purchaseOrderModel.getCode()):"");
        jsonObject.put("saleCode",saleOrderModel!=null?toNullStr(saleOrderModel.getCode()):"");
        jsonObject.put("poNo",purchaseOrderModel!=null?toNullStr(purchaseOrderModel.getPoNo()):saleOrderModel!=null?saleOrderModel.getPoNo():"");
        jsonObject.put("explain",jsonArray);
        return jsonObject;
    }


    /**
     * 字符串转换为空
     * @param str
     * @return
     */
    private String toNullStr(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str;
    }


    /**
     * 根据宝信部门、保健部门、母婴部门获取部门下的所有事业部
     * @param departmentCode
     * @return
     */
    private List<BusinessUnitMongo> getDepartmentByBuId(List departmentCode){
        List<DepartmentInfoMongo> departmentInfoMongoList=departmentInfoMongoDao.findAllByIn("code",departmentCode);
        //获取部门id
        List<Long> departmentId=departmentInfoMongoList.stream().map(DepartmentInfoMongo::getDepartmentInfoId).collect(Collectors.toList());
        //根据部门id获取事业部
        List<BusinessUnitMongo> businessUnitList=businessUnitMongoDao.findAllByIn("departmentId",departmentId);
        return businessUnitList;
    }


    /**
     * 根据部门分组进行封装数据
     */
    private void getJsonArrayByBuId(Long buId,JSONObject jsonObject,JSONArray jsonArrayBaoXin,JSONArray jsonArrayBaoJian,JSONArray jsonArrayMuYing,
                                    Map<Long,BusinessUnitMongo> baoXinBusiness,Map<Long,BusinessUnitMongo> baoJianBusiness,Map<Long,BusinessUnitMongo> muYingBusiness){

        //宝信部门
        if(baoXinBusiness.containsKey(buId)){
            jsonArrayBaoXin.add(jsonObject);
        }
        //保健部门
        if(baoJianBusiness.containsKey(buId)){
            jsonArrayBaoJian.add(jsonObject);
        }
        //母婴部门
        if(muYingBusiness.containsKey(buId)){
            jsonArrayMuYing.add(jsonObject);
        }
    }

}
