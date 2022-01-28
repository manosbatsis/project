package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.entity.dto.bill.ReceiveAgingReportDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryCostBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.ExchangeRateMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.ExchangeRateMongo;
import com.topideal.service.timer.ReceiveAgingReportService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReceiveAgingReportServiceImpl implements ReceiveAgingReportService {

    private static final Logger LOG = Logger.getLogger(ReceiveAgingReportServiceImpl.class) ;

    @Autowired
    private TobTemporaryReceiveBillDao tobTemporaryReceiveBillDao;
    @Autowired
    private TobTempVerifyRelDao tobTempVerifyRelDao;
    @Autowired
    private ExchangeRateMongoDao exchangeRateMongoDao;
    @Autowired
    private AccountingReminderItemDao accountingReminderItemDao;
    @Autowired
    private ReceiveAgingReportDao receiveAgingReportDao;
    @Autowired
    private ReceiveBillVerificationDao receiveBillVerificationDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private TocTemporaryReceiveBillDao tocTemporaryReceiveBillDao;
    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private TocSettlementReceiveBillVerifyItemDao tocSettlementReceiveBillVerifyItemDao;
    @Autowired
    private ReceiveAgingReportItemDao receiveAgingReportItemDao;
    @Autowired
    private TocTemporaryCostBillDao tocTemporaryCostBillDao;


    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201160009,model=DERP_LOG_POINT.POINT_13201160009_Label)
    public void saveReceiveAgingReport(String json, String keys, String topics, String tags) throws Exception {
        LOG.info("-----------------生成应收账龄报告开始：-----------------"+json);

        JSONObject jsonData = JSONObject.fromObject(json);

        Map<String,Object> searchQueryMap=new HashMap<>();
        String channelType="";
        Map<String,Integer> accountMap=new HashMap<>();//查询账期
        Map<String,String> costAreadyTob=new HashMap<>();
        Map<String,String> costAreadyToC=new HashMap<>();

        List<Map<String, Object>> verficatMap=new ArrayList<>();
        List<Map<String, Object>> settlementMap=new ArrayList<>();
        List<Map<String, Object>> oneGiveMap=new ArrayList<>();

        if(jsonData.get("currency")!=null && !"".equals(jsonData.get("currency"))){
            searchQueryMap.put("currency",jsonData.get("currency"));
        }
        if(jsonData.get("merchantId")!= null && !"".equals(jsonData.get("merchantId"))){
            searchQueryMap.put("merchantId",jsonData.get("merchantId"));
        }
        if(jsonData.get("customerId")!= null && !"".equals(jsonData.get("customerId"))){
            searchQueryMap.put("customerId",jsonData.get("customerId"));
        }
        if(jsonData.get("buId")!= null && !"".equals(jsonData.get("buId"))){
            searchQueryMap.put("buId",jsonData.get("buId"));
        }
        if(jsonData.get("channelType")!=null && !"".equals(jsonData.get("channelType"))){ //tob/toc类型
            channelType=(String)jsonData.get("channelType");
            searchQueryMap.put("channelType",channelType);
        }

        List<TobTemporaryReceiveBillDTO> listTobTemporaryDTOList=new ArrayList();
        List<TocTemporaryReceiveBillDTO> listToCTemporaryDTOList=new ArrayList<>();//存储Toc暂时应收 POP
        List<TocTemporaryReceiveBillDTO> listTocOneDTOList=new ArrayList<>();//存储Toc暂时应收 一件代发

        /*TOC 一件代发*/
        if(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_1.equals(channelType) || StringUtils.isBlank(channelType)){
            listTobTemporaryDTOList=tobTemporaryReceiveBillDao.listBySearchQuery(searchQueryMap);
            verficatMap=receiveBillVerificationDao.getSummary(searchQueryMap);

            searchQueryMap.put("shopTypeCode",DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002);

            //币种为CNY，或刷新的维度不包括币种，则查询toc暂估
            if(jsonData.get("currency")!=null && !"".equals(jsonData.get("currency"))){
                if(!DERP.CURRENCYCODE_CNY.equals(jsonData.get("currency").toString())){
                    listToCTemporaryDTOList=new ArrayList<>();
                }else{
                    listToCTemporaryDTOList=tocTemporaryReceiveBillDao.listBySearchQuery(searchQueryMap);
                }
            }else{
                listTocOneDTOList=tocTemporaryReceiveBillDao.listBySearchQuery(searchQueryMap);
            }
            oneGiveMap=tocSettlementReceiveBillDao.getSummarySettlement(searchQueryMap);
        }

        /*TOC 暂估、应收单 POP*/
        if(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_2.equals(channelType) || StringUtils.isBlank(channelType)){
            searchQueryMap.put("shopTypeCode",DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001);
            //币种为CNY，或刷新的维度不包括币种，则查询toc暂估
            if(jsonData.get("currency")!=null && !"".equals(jsonData.get("currency"))){
                if(!DERP.CURRENCYCODE_CNY.equals(jsonData.get("currency").toString())){
                    listToCTemporaryDTOList=new ArrayList<>();
                }else{
                    listToCTemporaryDTOList=tocTemporaryReceiveBillDao.listBySearchQuery(searchQueryMap);
                }
            }else{
                listToCTemporaryDTOList=tocTemporaryReceiveBillDao.listBySearchQuery(searchQueryMap);
            }
            settlementMap=tocSettlementReceiveBillDao.getSummarySettlement(searchQueryMap);
        }

        //封装toc暂估的POP数据
        List<Map<String,Object>> toCForTemporaryListMap=new ArrayList<>();
        for(TocTemporaryReceiveBillDTO oneToc:listTocOneDTOList){
            Map map=new HashMap<>();
            map.put("merchantId",oneToc.getMerchantId());
            map.put("merchantName",oneToc.getMerchantName());
            map.put("buId",oneToc.getBuId());
            map.put("buName",oneToc.getBuName());
            map.put("customerName",oneToc.getCustomerName());
            map.put("customerId",oneToc.getCustomerId());
            map.put("currency",DERP.CURRENCYCODE_CNY);
            toCForTemporaryListMap.add(map);
        }

        //先删除
        if(StringUtils.isBlank(channelType)){
            searchQueryMap.put("channelType","");
        }
        //查询表头id以及删除表体
        List<ReceiveAgingReportDTO> listIds=receiveAgingReportDao.listBySearchQuery(searchQueryMap);
        List<Long> receiveIds = listIds.stream().map(ReceiveAgingReportDTO::getId).collect(Collectors.toList());
        if(receiveIds.size()>0){
            receiveAgingReportItemDao.deleteReceiveAgingReportItem(receiveIds);
        }
        //删除表头
        receiveAgingReportDao.deleteByReceiveAging(searchQueryMap);


        Map<String,String> mapKeyToB=new HashMap<>();
        Map<String,String> mapKeyToC=new HashMap<>();

        /*tob暂估核销*/
        for(TobTemporaryReceiveBillDTO tobTemp:listTobTemporaryDTOList){
            ReceiveAgingReportModel receiveAgingReportModel=new ReceiveAgingReportModel();

            String str = tobTemp.getMerchantId() + "_" + tobTemp.getBuId() + "_" + tobTemp.getCustomerId() + "_" + tobTemp.getCurrency();
            mapKeyToB.put(str,str);
            costAreadyTob.put(str,str);


            //根据“商家+事业部+客户”查询账期提醒配置表，汇总该“事业部+客户”的所有计划节点时效天数；
            String accountStr=tobTemp.getMerchantId()+"_"+tobTemp.getBuId()+"_"+tobTemp.getCustomerId();
            accountMap=getAccountDay(accountMap,accountStr,receiveAgingReportModel,tobTemp.getBuId(),tobTemp.getCustomerId(),tobTemp.getMerchantId());

            Map<String, Object> queryMap =new HashMap<>();
            queryMap.put("merchantId",tobTemp.getMerchantId());
            queryMap.put("customerId",tobTemp.getCustomerId());
            queryMap.put("buId",tobTemp.getBuId());
            queryMap.put("currency",tobTemp.getCurrency());
            queryMap.put("day",receiveAgingReportModel.getAccountDay());
            queryMap.put("shopTypeCode",DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002);
            //汇总Tob暂估核销（收入）
            Map<String,BigDecimal> listMapTob=tobTempVerifyRelDao.getByTobItemId(queryMap);
            //汇总收款核销跟踪
            Map<String,BigDecimal> listMapVerification=receiveBillVerificationDao.getByUncollectedAmount(queryMap);
            //获取Toc暂估应收 （收入和费用）
            Map<String, BigDecimal> tocRecevieMap =new HashMap<>();
            Map<String, BigDecimal> tocRecevieCostMap =new HashMap<>();
            if(DERP.CURRENCYCODE_CNY.equals(tobTemp.getCurrency())) {
                tocRecevieMap = tocTemporaryReceiveBillDao.getByToCReceive(queryMap);
                List<TocTemporaryCostBillDTO> listCost=tocTemporaryCostBillDao.getTocTemporaryCostList(queryMap);
                BigDecimal amount=new BigDecimal(0);
                if(listCost.size()>0){
                    amount=listCost.get(0).getAmount();
                }
                tocRecevieCostMap.put("amount",amount);
            }
            //获取ToC 应收账单
            Map<String,Object> toSettlementMap=tocSettlementReceiveBillDao.getRecevieByBillStatus(queryMap);
            //根据toc的账单id查询已核销的金额
            Map<String,BigDecimal> mapWritten=getAmount(toSettlementMap);
            //获取tob暂估核销（费用）
            Map<String,BigDecimal> listMapTobCost=tobTemporaryReceiveBillDao.getTocTemprayCostBillDTO(queryMap);
            //应收费用（原币）= ToB暂估核销（费用）+ ToC 暂估应收 （费用）
            tocRecevieCostMap.forEach((key, value) -> listMapTobCost.merge(key,value, BigDecimal::add));
            receiveAgingReportModel.setCostOriginalAmount(listMapTobCost.get("amount"));

            //应收收入（原币）
            BigDecimal allOriginalAmount=sumAllOriginalAmount(listMapTob,listMapVerification,tocRecevieMap,mapWritten);
            receiveAgingReportModel.setOriginalAmount(allOriginalAmount);

            //应收账面余额（原币）=应收收入（原币）- 应收费用（原币）
            BigDecimal receiveIncome=receiveAgingReportModel.getOriginalAmount().subtract(receiveAgingReportModel.getCostOriginalAmount());
            receiveAgingReportModel.setReceiveIncomeOriginalAmount(receiveIncome);

            //map1合并到map2中,key相同value相加
            Map<String, BigDecimal> finalTocRecevieMap = tocRecevieMap;
            listMapTob.forEach((key, value) -> finalTocRecevieMap.merge(key,value, BigDecimal::add));
            finalTocRecevieMap.forEach((key,value) -> mapWritten.merge(key,value, BigDecimal::add));
            mapWritten.forEach((key,value) -> listMapVerification.merge(key,value, BigDecimal::add));

            //获取汇率，汇率日期以及计算应收账面余额（RMB）
            if(DERP.CURRENCYCODE_CNY.equals(tobTemp.getCurrency())){
                receiveAgingReportModel.setRate(1d);
                receiveAgingReportModel.setEffectiveDate(new Date());
                receiveAgingReportModel.setRmbAmount(allOriginalAmount);
                receiveAgingReportModel.setCostRmbAmount(receiveAgingReportModel.getCostOriginalAmount());//应收费用（本币）
                receiveAgingReportModel.setReceiveCostOriginalAmount(receiveAgingReportModel.getReceiveIncomeOriginalAmount()); //应收账面余额（本币）=应收账面余额（原币）*折算汇率值
            }else{
                receiveExchangeRate(tobTemp.getCurrency(),allOriginalAmount,receiveAgingReportModel);
            }
            receiveAgingReportModel.setThirtyAmount(listMapVerification.get("thirtyAmount"));
            receiveAgingReportModel.setSixtyAmount( listMapVerification.get("sixtyAmount"));
            receiveAgingReportModel.setNinetyAmount(listMapVerification.get("ninetyAmount"));
            receiveAgingReportModel.setOnetwentyAmount(listMapVerification.get("onetwentyAmount"));
            receiveAgingReportModel.setTwentyAboveAmount(listMapVerification.get("twentyAboveAmount"));
            receiveAgingReportModel.setAccountAmount( listMapVerification.get("dayAmount"));

            //逾期金额=应收账面余额（原币）-账期内金额，带上币种符号
            BigDecimal overdueAmount=allOriginalAmount.subtract(receiveAgingReportModel.getAccountAmount());
            receiveAgingReportModel.setOverdueAmount(overdueAmount);
            receiveAgingReportModel.setCurrency(tobTemp.getCurrency());
            receiveAgingReportModel.setMerchantId(tobTemp.getMerchantId());
            receiveAgingReportModel.setMerchantName(tobTemp.getMerchantName());
            receiveAgingReportModel.setBuId(tobTemp.getBuId());
            receiveAgingReportModel.setBuName(tobTemp.getBuName());
            receiveAgingReportModel.setRmbCurrency(DERP.CURRENCYCODE_CNY);
            receiveAgingReportModel.setCreateDate(TimeUtils.getNow());
            receiveAgingReportModel.setChannelType(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_1);
            receiveAgingReportModel.setCustomerId(tobTemp.getCustomerId());
            receiveAgingReportModel.setCustomerName(tobTemp.getCustomerName());
            Map<String, Object> customerParam = new HashMap<>();
            customerParam.put("customerid", tobTemp.getCustomerId());
            CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParam);
            if(customer!=null){
                receiveAgingReportModel.setSimpleName(customer.getSimpleName());
            }
            Long id=receiveAgingReportDao.save(receiveAgingReportModel);

            //生成表体
            saveTobReceiveAgingItem("3",id,tobTemp.getMerchantId(),tobTemp.getCustomerId(),tobTemp.getBuId(),tobTemp.getCurrency());
            //生成表体
            saveToCReceiveAgingItem("3",id,tobTemp.getMerchantId(),tobTemp.getCustomerId(),tobTemp.getBuId(),tobTemp.getCurrency(),DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002);

            LOG.info("-----------------TOB暂估核销，生成应收账龄报告结束-----------------");
        }

        if(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_1.equals(channelType) || StringUtils.isBlank(channelType)){
            /* 生成toc暂估（一件代发）*/
            mapKeyToB=saveTobReceiveAging(accountMap,toCForTemporaryListMap,mapKeyToB,"1",costAreadyTob);
            LOG.info("-----------------toc暂估（一件代发），生成应收账龄报告结束-----------------");

            /* 生成收款核销跟踪表*/
            mapKeyToB=saveTobReceiveAging(accountMap,verficatMap,mapKeyToB,"",costAreadyTob);
            LOG.info("-----------------收款核销跟踪，生成应收账龄报告结束-----------------");

            /*生成toc结算单（一件代发）*/
            mapKeyToB=saveTobReceiveAging(accountMap,oneGiveMap,mapKeyToB,"",costAreadyTob);
            LOG.info("-----------------生成toc结算单（一件代发），生成应收账龄报告结束-----------------");

        }


        /*生成Toc暂估*/
        for(TocTemporaryReceiveBillDTO tocTemp:listToCTemporaryDTOList){
            ReceiveAgingReportModel receiveAgingReportModel=new ReceiveAgingReportModel();
            tocTemp.setCurrency(DERP.CURRENCYCODE_CNY);

            String str = tocTemp.getMerchantId() + "_" + tocTemp.getBuId() + "_" + tocTemp.getCustomerId() + "_" + tocTemp.getCurrency();
            mapKeyToC.put(str,str);
            costAreadyToC.put(str,str);


            //根据“事业部+客户”查询账期提醒配置表，汇总该“事业部+客户”的所有计划节点时效天数；
            String accountStr=tocTemp.getMerchantId()+"_"+tocTemp.getBuId()+"_"+tocTemp.getCustomerId();
            accountMap=getAccountDay(accountMap,accountStr,receiveAgingReportModel,tocTemp.getBuId(),tocTemp.getCustomerId(),tocTemp.getMerchantId());

            Map<String, Object> queryMap =new HashMap<>();
            queryMap.put("merchantId",tocTemp.getMerchantId());
            queryMap.put("customerId",tocTemp.getCustomerId());
            queryMap.put("shopTypeCode",DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001);
            queryMap.put("buId",tocTemp.getBuId());
            queryMap.put("currency",DERP.CURRENCYCODE_CNY);
            queryMap.put("day",receiveAgingReportModel.getAccountDay());

            //获取Toc暂估应收
            Map<String,BigDecimal> tocRecevieMap=tocTemporaryReceiveBillDao.getByToCReceive(queryMap);
            //获取ToC 应收账单
            Map<String,Object> toSettlementMap=tocSettlementReceiveBillDao.getRecevieByBillStatus(queryMap);
            //根据toc的账单id查询已核销的金额
            Map<String,BigDecimal> mapWritten=getAmount(toSettlementMap);

            //获取ToC 暂估费用,应收费用（原币）
            List<TocTemporaryCostBillDTO> tocReceiveCostMap=tocTemporaryCostBillDao.getTocTemporaryCostList(queryMap);
            BigDecimal tempAmount=new BigDecimal(0);
            if(tocReceiveCostMap.size() > 0){
                tempAmount=tocReceiveCostMap.get(0).getAmount();
            }
            receiveAgingReportModel.setCostOriginalAmount(tempAmount);

            //应收收入(原币）
            BigDecimal originalAmountTob=getAllAmountVerify(tocRecevieMap);
            BigDecimal originalAmountTobVerify=getAllAmountVerify(mapWritten);
            BigDecimal allOriginalAmount=originalAmountTob.add(originalAmountTobVerify);
            receiveAgingReportModel.setOriginalAmount(allOriginalAmount);

            //应收账面余额（原币）=应收收入（原币）- 应收费用（原币）
            BigDecimal incomeAmount=receiveAgingReportModel.getOriginalAmount().subtract(receiveAgingReportModel.getCostOriginalAmount());
            receiveAgingReportModel.setReceiveIncomeOriginalAmount(incomeAmount);

            //map1合并到map2中,key相同value相加
            tocRecevieMap.forEach((key,value) -> mapWritten.merge(key,value, BigDecimal::add));

            //获取汇率，汇率日期以及计算应收账面余额（RMB）
            //receiveExchangeRate(dto.getCurrency(),allOriginalAmount,receiveAgingReportModel);
            receiveAgingReportModel.setRate(1d);
            receiveAgingReportModel.setEffectiveDate(new Date());
            //应收收入（本币）=应收收入（原币）*折算汇率值
            BigDecimal rmbAmount=allOriginalAmount.multiply(new BigDecimal(1));
            receiveAgingReportModel.setRmbAmount(rmbAmount);
            //应收费用（本币）=应收费用（原币）*折算汇率值
            BigDecimal costRmbAmount=receiveAgingReportModel.getCostOriginalAmount().multiply(new BigDecimal(1));
            receiveAgingReportModel.setCostRmbAmount(costRmbAmount);
            //应收账面余额（本币）=  应收账面余额（原币）*折算汇率值
            receiveAgingReportModel.setReceiveCostOriginalAmount(receiveAgingReportModel.getReceiveIncomeOriginalAmount());


            receiveAgingReportModel.setThirtyAmount(mapWritten.get("thirtyAmount"));
            receiveAgingReportModel.setSixtyAmount(mapWritten.get("sixtyAmount"));
            receiveAgingReportModel.setNinetyAmount(mapWritten.get("ninetyAmount"));
            receiveAgingReportModel.setOnetwentyAmount(mapWritten.get("onetwentyAmount"));
            receiveAgingReportModel.setTwentyAboveAmount(mapWritten.get("twentyAboveAmount"));
            receiveAgingReportModel.setAccountAmount(mapWritten.get("dayAmount"));
            //逾期金额=应收账面余额（原币）-账期内金额，带上币种符号
            BigDecimal overdueAmount=allOriginalAmount.subtract(receiveAgingReportModel.getAccountAmount());
            receiveAgingReportModel.setOverdueAmount(overdueAmount);
            receiveAgingReportModel.setCurrency(DERP.CURRENCYCODE_CNY);
            receiveAgingReportModel.setMerchantId(tocTemp.getMerchantId());
            receiveAgingReportModel.setMerchantName(tocTemp.getMerchantName());
            receiveAgingReportModel.setCustomerId(tocTemp.getCustomerId());
            receiveAgingReportModel.setCustomerName(tocTemp.getCustomerName());
            receiveAgingReportModel.setBuId(tocTemp.getBuId());
            receiveAgingReportModel.setBuName(tocTemp.getBuName());
            receiveAgingReportModel.setRmbCurrency(DERP.CURRENCYCODE_CNY);
            receiveAgingReportModel.setCreateDate(TimeUtils.getNow());
            receiveAgingReportModel.setChannelType(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_2);
            Map<String, Object> customerParam = new HashMap<>();
            customerParam.put("customerid", tocTemp.getCustomerId());
            CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParam);
            if(customer!=null){
                receiveAgingReportModel.setSimpleName(customer.getSimpleName());
            }
            Long id=receiveAgingReportDao.save(receiveAgingReportModel);

            //生成表体
            saveToCReceiveAgingItem("3",id,tocTemp.getMerchantId(),tocTemp.getCustomerId(),tocTemp.getBuId(),DERP.CURRENCYCODE_CNY,DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001);

            LOG.info("-----------------TOC暂估应收，生成应收账龄报告结束-----------------");
        }

        /* 生成toc结算单*/
        saveToCReceiveAging(accountMap,settlementMap,mapKeyToC);


        //查询toc暂估费用，汇总费用的未核销金额
        List<TocTemporaryCostBillDTO> listCostDTO=tocTemporaryCostBillDao.getTocTemporaryCostList(new HashMap<>());

        for(TocTemporaryCostBillDTO dtoCost:listCostDTO){
            dtoCost.setCurrency(DERP.CURRENCYCODE_CNY);

            String str = dtoCost.getMerchantId() + "_" + dtoCost.getBuId() + "_" + dtoCost.getCustomerId() + "_" + dtoCost.getCurrency();

            if(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(dtoCost.getShopTypeCode())){
                if(costAreadyToC.containsKey(str)){
                    continue;
                }
            }

            if(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002.equals(dtoCost.getShopTypeCode())){
                if(costAreadyTob.containsKey(str)){
                    continue;
                }
            }
            BigDecimal dayAmount=new BigDecimal(0);

            ReceiveAgingReportModel receiveAgingReportModel=new ReceiveAgingReportModel();
            Map<String, Object> queryMap =new HashMap<>();
            queryMap.put("merchantId",dtoCost.getMerchantId());
            queryMap.put("customerId",dtoCost.getCustomerId());
            queryMap.put("shopTypeCode",dtoCost.getShopTypeCode());
            queryMap.put("buId",dtoCost.getBuId());
            queryMap.put("currency",DERP.CURRENCYCODE_CNY);
            queryMap.put("day",0);

            //应收收入（原币）
            receiveAgingReportModel.setOriginalAmount(dayAmount);
            //应收费用（原币）获取ToC 暂估费用,
            Map<String,BigDecimal> tocReceiveCostMap=new HashMap<>();
            List<TocTemporaryCostBillDTO> listCost=tocTemporaryCostBillDao.getTocTemporaryCostList(queryMap);
            BigDecimal costAmount=new BigDecimal(0);
            if(listCost.size()>0){
                costAmount=listCost.get(0).getAmount();
            }
            tocReceiveCostMap.put("amount",costAmount);
            receiveAgingReportModel.setCostOriginalAmount(tocReceiveCostMap.get("amount"));
            //应收账面余额（原币）
            BigDecimal incomentAmount=dayAmount.subtract(receiveAgingReportModel.getCostOriginalAmount());
            receiveAgingReportModel.setReceiveIncomeOriginalAmount(incomentAmount);

            //获取汇率，汇率日期以及计算应收账面余额（RMB）
            //receiveExchangeRate(dto.getCurrency(),allOriginalAmount,receiveAgingReportModel);
            receiveAgingReportModel.setRate(1d);
            receiveAgingReportModel.setEffectiveDate(new Date());
            //应收收入（本币）=应收收入（原币）*折算汇率值
            receiveAgingReportModel.setRmbAmount(dayAmount.multiply(new BigDecimal(1)));
            //应收费用（本币）=应收费用（原币）*折算汇率值
            BigDecimal costRmbAmount=receiveAgingReportModel.getCostOriginalAmount().multiply(new BigDecimal(1));
            receiveAgingReportModel.setCostRmbAmount(costRmbAmount);
            //应收账面余额（本币）=  应收账面余额（原币）*折算汇率值
            receiveAgingReportModel.setReceiveCostOriginalAmount(receiveAgingReportModel.getReceiveIncomeOriginalAmount());

            receiveAgingReportModel.setThirtyAmount(dayAmount);
            receiveAgingReportModel.setSixtyAmount(dayAmount);
            receiveAgingReportModel.setNinetyAmount(dayAmount);
            receiveAgingReportModel.setOnetwentyAmount(dayAmount);
            receiveAgingReportModel.setTwentyAboveAmount(dayAmount);
            receiveAgingReportModel.setAccountAmount(dayAmount);
            receiveAgingReportModel.setOverdueAmount(dayAmount);
            receiveAgingReportModel.setCurrency(DERP.CURRENCYCODE_CNY);
            receiveAgingReportModel.setMerchantId(dtoCost.getMerchantId());
            receiveAgingReportModel.setMerchantName(dtoCost.getMerchantName());
            receiveAgingReportModel.setCustomerId(dtoCost.getCustomerId());
            receiveAgingReportModel.setCustomerName(dtoCost.getCustomerName());
            receiveAgingReportModel.setBuId(dtoCost.getBuId());
            receiveAgingReportModel.setBuName(dtoCost.getBuName());
            receiveAgingReportModel.setRmbCurrency(DERP.CURRENCYCODE_CNY);
            receiveAgingReportModel.setCreateDate(TimeUtils.getNow());
            receiveAgingReportModel.setChannelType(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_2);
            Map<String, Object> customerParam = new HashMap<>();
            customerParam.put("customerid", dtoCost.getCustomerId());
            CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParam);
            if(customer!=null){
                receiveAgingReportModel.setSimpleName(customer.getSimpleName());
            }
            Long id=receiveAgingReportDao.save(receiveAgingReportModel);


        }



        LOG.info("-----------------生成应收账龄报告结束-----------------");

    }


    /**
     * @param accountMap 账期内天数
     * @param verficatMap 表头
     * @param mapKey 已经生成的维度
     * @param type 是否生成费用
     * @return
     * @throws SQLException
     */
    public Map<String,String> saveTobReceiveAging(Map<String,Integer> accountMap,List<Map<String, Object>> verficatMap,Map<String,String> mapKey,String type,Map<String,String> costAreadyTob) throws SQLException {
        for(int i=0;i<verficatMap.size();i++) {
            ReceiveAgingReportModel receiveAgingReportModel = new ReceiveAgingReportModel();

            Map map = verficatMap.get(i);
            Long buId = (Long) map.get("buId");
            Long customerId = (Long) map.get("customerId");
            Long merchantId = (Long) map.get("merchantId");
            String currency = (String) map.get("currency");
            String channleType = "";

            String str = merchantId + "_" + buId + "_" + customerId + "_" + currency;
            if(mapKey.containsKey(str)){
                continue;
            }
            mapKey.put(str,str);
            if("1".equals(type)){
                costAreadyTob.put(str,str);
            }

            //根据“商家+事业部+客户”查询账期提醒配置表，汇总该“事业部+客户”的所有计划节点时效天数；
            String accountStr=merchantId+"_"+buId+"_"+customerId;
            accountMap=getAccountDay(accountMap,accountStr,receiveAgingReportModel,buId,customerId,merchantId);

            Map<String, Object> queryMap =new HashMap<>();
            queryMap.put("merchantId",merchantId);
            queryMap.put("customerId",customerId);
            queryMap.put("buId",buId);
            queryMap.put("currency",currency);
            queryMap.put("day",receiveAgingReportModel.getAccountDay());
            queryMap.put("shopTypeCode",DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002);
            //汇总Tob暂估核销
            Map<String,BigDecimal> listMapTob=tobTempVerifyRelDao.getByTobItemId(queryMap);
            //汇总收款核销跟踪
            Map<String,BigDecimal> listMapVerification=receiveBillVerificationDao.getByUncollectedAmount(queryMap);
            //获取Toc暂估应收
            Map<String, BigDecimal> tocRecevieMap =new HashMap<>();
            Map<String, BigDecimal> tocRecevieCostMap =new HashMap<>();
            if(DERP.CURRENCYCODE_CNY.equals(currency)){
                tocRecevieMap = tocTemporaryReceiveBillDao.getByToCReceive(queryMap);
                if("1".equals(type)){
                    List<TocTemporaryCostBillDTO> listCost=tocTemporaryCostBillDao.getTocTemporaryCostList(queryMap);

                    tocRecevieCostMap.put("amount",listCost.size()>0?listCost.get(0).getAmount():new BigDecimal(0));
                }
            }
            //获取ToC 应收账单
            Map<String,Object> toSettlementMap=tocSettlementReceiveBillDao.getRecevieByBillStatus(queryMap);
            //根据toc的账单id查询已核销的金额
            Map<String,BigDecimal> mapWritten=getAmount(toSettlementMap);

            //应收费用（原币）
            BigDecimal costOrginalAmount=new BigDecimal(0);
            if("1".equals(type)){
                //获取tob暂估核销（费用）
                Map<String,BigDecimal> listMapTobCost=tobTemporaryReceiveBillDao.getTocTemprayCostBillDTO(queryMap);
                //应收费用（原币）= ToB暂估核销（费用）+ ToC 暂估应收 （费用）
                tocRecevieCostMap.forEach((key, value) -> listMapTobCost.merge(key,value, BigDecimal::add));
                costOrginalAmount=listMapTobCost.get("amount");
            }
            receiveAgingReportModel.setCostOriginalAmount(costOrginalAmount);

            //应收收入（原币）
            BigDecimal allOriginalAmount=sumAllOriginalAmount(listMapTob,listMapVerification,tocRecevieMap,mapWritten);
            receiveAgingReportModel.setOriginalAmount(allOriginalAmount);

            //应收账面余额（原币）=应收收入（原币）- 应收费用（原币）
            BigDecimal incomeAmount=receiveAgingReportModel.getOriginalAmount().subtract(receiveAgingReportModel.getCostOriginalAmount());
            receiveAgingReportModel.setReceiveIncomeOriginalAmount(incomeAmount);


            //map1合并到map2中,key相同value相加
            Map<String, BigDecimal> finalTocRecevieMap = tocRecevieMap;
            listMapTob.forEach((key, value) -> finalTocRecevieMap.merge(key,value, BigDecimal::add));
            finalTocRecevieMap.forEach((key,value) -> mapWritten.merge(key,value, BigDecimal::add));
            mapWritten.forEach((key,value) -> listMapVerification.merge(key,value, BigDecimal::add));

            //获取汇率，汇率日期以及计算应收账面余额（RMB）
            if(DERP.CURRENCYCODE_CNY.equals(currency)){
                receiveAgingReportModel.setRate(1d);
                receiveAgingReportModel.setEffectiveDate(new Date());
                receiveAgingReportModel.setRmbAmount(allOriginalAmount);
                receiveAgingReportModel.setCostRmbAmount(receiveAgingReportModel.getCostOriginalAmount()==null?new BigDecimal(0):receiveAgingReportModel.getReceiveCostOriginalAmount());//应收费用（本币）
                receiveAgingReportModel.setReceiveCostOriginalAmount(receiveAgingReportModel.getReceiveIncomeOriginalAmount()); //应收账面余额（本币）=应收账面余额（原币）*折算汇率值
            }else{
                receiveExchangeRate(currency,allOriginalAmount,receiveAgingReportModel);
            }
            receiveAgingReportModel.setThirtyAmount(listMapVerification.get("thirtyAmount"));
            receiveAgingReportModel.setSixtyAmount( listMapVerification.get("sixtyAmount"));
            receiveAgingReportModel.setNinetyAmount(listMapVerification.get("ninetyAmount"));
            receiveAgingReportModel.setOnetwentyAmount(listMapVerification.get("onetwentyAmount"));
            receiveAgingReportModel.setTwentyAboveAmount(listMapVerification.get("twentyAboveAmount"));
            receiveAgingReportModel.setAccountAmount( listMapVerification.get("dayAmount"));

            //逾期金额=应收账面余额（原币）-账期内金额，带上币种符号
            BigDecimal overdueAmount=allOriginalAmount.subtract(receiveAgingReportModel.getAccountAmount());
            receiveAgingReportModel.setOverdueAmount(overdueAmount);
            receiveAgingReportModel.setCurrency(currency);
            receiveAgingReportModel.setMerchantId(merchantId);

            receiveAgingReportModel.setMerchantName((String)map.get("merchantName"));
            receiveAgingReportModel.setBuId(buId);
            receiveAgingReportModel.setBuName((String)map.get("buName"));
            receiveAgingReportModel.setRmbCurrency(DERP.CURRENCYCODE_CNY);
            receiveAgingReportModel.setCreateDate(TimeUtils.getNow());
            receiveAgingReportModel.setChannelType(channleType);
            receiveAgingReportModel.setCustomerId(customerId);
            receiveAgingReportModel.setChannelType(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_1);
            receiveAgingReportModel.setCustomerName((String)map.get("customerName"));
            Map<String, Object> customerParam = new HashMap<>();
            customerParam.put("customerid", customerId);
            CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParam);
            if(customer!=null){
                receiveAgingReportModel.setSimpleName(customer.getSimpleName());
            }
            Long id=receiveAgingReportDao.save(receiveAgingReportModel);

            //生成表体
            saveTobReceiveAgingItem("3",id,merchantId,customerId,buId,currency);
            //生成表体
            saveToCReceiveAgingItem("3",id,merchantId,customerId,buId,currency,DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002);

            LOG.info("-----------------TOB暂估核销，生成应收账龄报告结束-----------------");
        }
        return mapKey;
    }


    public Map<String,String> saveToCReceiveAging(Map<String,Integer> accountMap,List<Map<String, Object>> verficatMap,Map<String,String> mapKey) throws SQLException {
        for(int i=0;i<verficatMap.size();i++) {
            ReceiveAgingReportModel receiveAgingReportModel = new ReceiveAgingReportModel();

            Map map = verficatMap.get(i);
            Long buId = (Long) map.get("buId");
            Long customerId = (Long) map.get("customerId");
            Long merchantId = (Long) map.get("merchantId");
            String currency = (String) map.get("currency");

            String str = merchantId + "_" + buId + "_" + customerId + "_" + currency;
            if (mapKey.containsKey(str)) {
                continue;
            }
            mapKey.put(str, str);

            //receiveAgingReportModel.setCurrency(DERP.CURRENCYCODE_CNY);

            //根据“事业部+客户”查询账期提醒配置表，汇总该“事业部+客户”的所有计划节点时效天数；
            String accountStr=merchantId+"_"+buId+"_"+customerId;
            accountMap=getAccountDay(accountMap,accountStr,receiveAgingReportModel,buId,customerId,merchantId);

            Map<String, Object> queryMap =new HashMap<>();
            queryMap.put("merchantId",merchantId);
            queryMap.put("customerId",customerId);
            queryMap.put("shopTypeCode",DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001);
            queryMap.put("buId",buId);
            queryMap.put("currency",currency);
            queryMap.put("day",receiveAgingReportModel.getAccountDay());

            //获取Toc暂估应收
            Map<String, BigDecimal> tocRecevieMap =new HashMap<>();
            if(DERP.CURRENCYCODE_CNY.equals(currency)) {
                tocRecevieMap = tocTemporaryReceiveBillDao.getByToCReceive(queryMap);
            }
            //获取ToC 应收账单
            Map<String,Object> toSettlementMap=tocSettlementReceiveBillDao.getRecevieByBillStatus(queryMap);
            //根据toc的账单id查询已核销的金额
            Map<String,BigDecimal> mapWritten=getAmount(toSettlementMap);

            BigDecimal originalAmountTob=getAllAmountVerify(tocRecevieMap);
            BigDecimal originalAmountTobVerify=getAllAmountVerify(mapWritten);
            //应收账目余额（原币）
            BigDecimal allOriginalAmount=originalAmountTob.add(originalAmountTobVerify);
            receiveAgingReportModel.setOriginalAmount(allOriginalAmount);

            //map1合并到map2中,key相同value相加
            tocRecevieMap.forEach((key,value) -> mapWritten.merge(key,value, BigDecimal::add));

            //获取汇率，汇率日期以及计算应收账面余额（RMB）
            //receiveExchangeRate(dto.getCurrency(),allOriginalAmount,receiveAgingReportModel);
            receiveAgingReportModel.setRate(1d);
            receiveAgingReportModel.setEffectiveDate(new Date());
            BigDecimal rmbAmount=allOriginalAmount.multiply(new BigDecimal(1));
            receiveAgingReportModel.setRmbAmount(rmbAmount);

            receiveAgingReportModel.setThirtyAmount(mapWritten.get("thirtyAmount"));
            receiveAgingReportModel.setSixtyAmount(mapWritten.get("sixtyAmount"));
            receiveAgingReportModel.setNinetyAmount(mapWritten.get("ninetyAmount"));
            receiveAgingReportModel.setOnetwentyAmount(mapWritten.get("onetwentyAmount"));
            receiveAgingReportModel.setTwentyAboveAmount(mapWritten.get("twentyAboveAmount"));
            receiveAgingReportModel.setAccountAmount(mapWritten.get("dayAmount"));
            //逾期金额=应收账面余额（原币）-账期内金额，带上币种符号
            BigDecimal overdueAmount=allOriginalAmount.subtract(receiveAgingReportModel.getAccountAmount());
            receiveAgingReportModel.setOverdueAmount(overdueAmount);
            receiveAgingReportModel.setCurrency(currency);
            receiveAgingReportModel.setMerchantId(merchantId);
            receiveAgingReportModel.setMerchantName((String)map.get("merchantName"));
            receiveAgingReportModel.setBuId(buId);
            receiveAgingReportModel.setBuName((String)map.get("buName"));
            receiveAgingReportModel.setCustomerId(customerId);
            receiveAgingReportModel.setCustomerName((String)map.get("customerName"));
            receiveAgingReportModel.setRmbCurrency(DERP.CURRENCYCODE_CNY);
            receiveAgingReportModel.setCreateDate(TimeUtils.getNow());
            receiveAgingReportModel.setChannelType(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_2);
            Map<String, Object> customerParam = new HashMap<>();
            customerParam.put("customerid", customerId);
            CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParam);
            if(customer!=null){
                receiveAgingReportModel.setSimpleName(customer.getSimpleName());
            }
            Long id=receiveAgingReportDao.save(receiveAgingReportModel);

            //生成表体
            saveToCReceiveAgingItem("3",id,merchantId,customerId,buId,currency,DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001);

            LOG.info("-----------------toc结算单，生成应收账龄报告结束-----------------");
        }
        return mapKey;
    }


    /**
     * 应收账目余额（原币）
     */
    private BigDecimal sumAllOriginalAmount(Map<String,BigDecimal> map, Map<String,BigDecimal> map2, Map<String,BigDecimal> map3, Map<String,BigDecimal> map4){
        //计算应收账目金额（原币）（0~30天、30~60天、60~90天、90~120天、120天以上）这几个区间的总金额
        BigDecimal originalAmountTob=getAllAmountVerify(map);
        BigDecimal originalAmountTobVerify=getAllAmountVerify(map2);
        BigDecimal originalAmountToCVerify=getAllAmountVerify(map3);
        BigDecimal originalAmountToC=getAllAmountVerify(map4);

        BigDecimal allAmount=originalAmountTob.add(originalAmountTobVerify).add(originalAmountToCVerify).add(originalAmountToC);
        return allAmount;
    }


    /**
     * tob账单表体
     */
    private void saveTobReceiveAgingItem(String type,Long id,Long merchantId,Long customerId,Long buId,String currency) throws SQLException {
        Map<String,Object> map=new HashMap<>();
        map.put("merchantId",merchantId);
        map.put("customerId",customerId);
        map.put("buId",buId);
        map.put("currency",currency);

        /*ToB暂估核销*/
        if("1".equals(type)||"3".equals(type)){
            List<Map<String,Object>> tobListMap=tobTemporaryReceiveBillDao.getItemBySearch(map);
                for (Map<String,Object> tobMap : tobListMap) {
                    if (tobMap!=null) {
                        String shelfCode = (String) tobMap.get("shelfCode");
                        String poNo = (String) tobMap.get("poNo");
                        String month = (String) tobMap.get("month");
                        BigDecimal amount = (BigDecimal) tobMap.get("amount");
                        ReceiveAgingReportItemModel receiveAgingReportItemModel = new ReceiveAgingReportItemModel();
                        receiveAgingReportItemModel.setAgingReportId(id);
                        receiveAgingReportItemModel.setCode(shelfCode);
                        receiveAgingReportItemModel.setPoNo(poNo);
                        receiveAgingReportItemModel.setMonth(month);
                        receiveAgingReportItemModel.setWrittenOffAmount(amount);
                        receiveAgingReportItemModel.setOrderType(DERP_ORDER.RECEIVEAGING_ORDERTYPE_TYPE_1);
                        receiveAgingReportItemModel.setCreateDate(TimeUtils.getNow());
                        receiveAgingReportItemDao.save(receiveAgingReportItemModel);
                    }
                }
            }


        /*收款核销跟踪*/
        if("2".equals(type)||"3".equals(type)) {
            List<Map<String, Object>> verifiListMap = receiveBillVerificationDao.getItemBySearch(map);
            for (Map<String, Object> tobMap : verifiListMap) {
                if (tobMap!=null) {
                    String shelfCode = (String) tobMap.get("code");
                    String month = (String) tobMap.get("month");
                    BigDecimal amount = (BigDecimal) tobMap.get("amount");
                    ReceiveAgingReportItemModel receiveAgingReportItemModel = new ReceiveAgingReportItemModel();
                    receiveAgingReportItemModel.setAgingReportId(id);
                    receiveAgingReportItemModel.setCode(shelfCode);
                    receiveAgingReportItemModel.setMonth(month);
                    receiveAgingReportItemModel.setWrittenOffAmount(amount);
                    receiveAgingReportItemModel.setOrderType(DERP_ORDER.RECEIVEAGING_ORDERTYPE_TYPE_2);
                    receiveAgingReportItemModel.setCreateDate(TimeUtils.getNow());
                    receiveAgingReportItemDao.save(receiveAgingReportItemModel);
                }
            }
        }
    }


    /**
     * toc账单表体
     */
    private void saveToCReceiveAgingItem(String type,Long id,Long merchantId,Long customerId,Long buId,String currency,String shopTypeCode) throws SQLException {
        Map<String,Object> map=new HashMap<>();
        map.put("merchantId",merchantId);
        map.put("customerId",customerId);
        map.put("buId",buId);
        map.put("currency",currency);
        map.put("shopTypeCode",shopTypeCode);

        /*ToC 暂估应收*/
        if("1".equals(type)||"3".equals(type)) {
            if(DERP.CURRENCYCODE_CNY.equals(currency)) {
                List<Map<String, Object>> tocListMap = tocTemporaryReceiveBillDao.getItemSearchList(map);
                for (Map<String, Object> tobMap : tocListMap) {
                    if (tobMap!=null) {
                        String month = (String) tobMap.get("month");
                        BigDecimal amount = (BigDecimal) tobMap.get("amount");
                        ReceiveAgingReportItemModel receiveAgingReportItemModel = new ReceiveAgingReportItemModel();
                        receiveAgingReportItemModel.setAgingReportId(id);
                        receiveAgingReportItemModel.setMonth(month);
                        receiveAgingReportItemModel.setWrittenOffAmount(amount);
                        receiveAgingReportItemModel.setOrderType(DERP_ORDER.RECEIVEAGING_ORDERTYPE_TYPE_3);
                        receiveAgingReportItemModel.setCreateDate(TimeUtils.getNow());
                        receiveAgingReportItemDao.save(receiveAgingReportItemModel);
                    }
                }
            }
        }

        /*ToC 应收账单*/
        if("2".equals(type)||"3".equals(type)) {
            List<Map<String, Object>> tcSettlementMap = tocSettlementReceiveBillDao.getItemBySearch(map);
            for (Map<String, Object> tobMap : tcSettlementMap) {
                if(tobMap!=null) {
                    String month = (String) tobMap.get("month");
                    BigDecimal amount = (BigDecimal) tobMap.get("amount");
                    ReceiveAgingReportItemModel receiveAgingReportItemModel = new ReceiveAgingReportItemModel();
                    receiveAgingReportItemModel.setAgingReportId(id);
                    receiveAgingReportItemModel.setMonth(month);
                    receiveAgingReportItemModel.setWrittenOffAmount(amount);
                    receiveAgingReportItemModel.setOrderType(DERP_ORDER.RECEIVEAGING_ORDERTYPE_TYPE_4);
                    receiveAgingReportItemModel.setCreateDate(TimeUtils.getNow());
                    receiveAgingReportItemDao.save(receiveAgingReportItemModel);
                }
            }
        }
    }

    /**
     * toc应收账单，根据账单id查询已核销金额计算待核销金额
     * @param toSettlementMap
     * @return
     */
    private Map<String,BigDecimal> getAmount(Map<String,Object> toSettlementMap){
        Map<String,BigDecimal> mapWritten=new HashMap<>();
        BigDecimal thirtyAmount=(BigDecimal)toSettlementMap.get("thirtyAmount");
        BigDecimal sixtyAmount=(BigDecimal)toSettlementMap.get("sixtyAmount");
        BigDecimal ninetyAmount=(BigDecimal)toSettlementMap.get("ninetyAmount");
        BigDecimal onetwentyAmount=(BigDecimal)toSettlementMap.get("onetwentyAmount");
        BigDecimal twentyAboveAmount=(BigDecimal)toSettlementMap.get("twentyAboveAmount");
        BigDecimal daysAmount=(BigDecimal)toSettlementMap.get("dayAmount");

        if(toSettlementMap.get("thirtyIds")!=null){
            String idStr=(String)toSettlementMap.get("thirtyIds");
            List ids=Arrays.asList(idStr.split(","));
            BigDecimal price=tocSettlementReceiveBillVerifyItemDao.getAllPriceByBillIds(ids);
            thirtyAmount=thirtyAmount.subtract(price);
        }
        mapWritten.put("thirtyAmount",thirtyAmount);

        if(toSettlementMap.get("sixtyIds")!=null){
            String idStr=(String)toSettlementMap.get("sixtyIds");
            List ids=Arrays.asList(idStr.split(","));
            BigDecimal price=tocSettlementReceiveBillVerifyItemDao.getAllPriceByBillIds(ids);
            sixtyAmount=sixtyAmount.subtract(price);
        }
        mapWritten.put("sixtyAmount",sixtyAmount);

        if(toSettlementMap.get("ninetyIds")!=null){
            String idStr=(String)toSettlementMap.get("ninetyIds");
            List ids=Arrays.asList(idStr.split(","));
            BigDecimal price=tocSettlementReceiveBillVerifyItemDao.getAllPriceByBillIds(ids);
            ninetyAmount=ninetyAmount.subtract(price);
        }
        mapWritten.put("ninetyAmount",ninetyAmount);


        if(toSettlementMap.get("twentyIds")!=null){
            String idStr=(String)toSettlementMap.get("twentyIds");
            List ids=Arrays.asList(idStr.split(","));
            BigDecimal price=tocSettlementReceiveBillVerifyItemDao.getAllPriceByBillIds(ids);
            onetwentyAmount=onetwentyAmount.subtract(price);
        }
        mapWritten.put("onetwentyAmount",onetwentyAmount);

        if(toSettlementMap.get("aboveIds")!=null){
            String idStr=(String)toSettlementMap.get("aboveIds");
            List ids=Arrays.asList(idStr.split(","));
            BigDecimal price=tocSettlementReceiveBillVerifyItemDao.getAllPriceByBillIds(ids);
            twentyAboveAmount=twentyAboveAmount.subtract(price);
        }
        mapWritten.put("twentyAboveAmount",twentyAboveAmount);

        if(toSettlementMap.get("dayIds")!=null){
            String idStr=(String)toSettlementMap.get("dayIds");
            List ids=Arrays.asList(idStr.split(","));
            BigDecimal price=tocSettlementReceiveBillVerifyItemDao.getAllPriceByBillIds(ids);
            daysAmount=daysAmount.subtract(price);
        }
        mapWritten.put("dayAmount",daysAmount);

        return mapWritten;
    }


    /**
     * 计算map中所有的值，过滤账期内金额
     * @param map
     */
    private BigDecimal getAllAmountVerify(Map<String,BigDecimal> map){
        BigDecimal allDayAmount=new BigDecimal(0);
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            if("dayAmount".equals(entry.getKey())){//过滤
                continue;
            }
            allDayAmount=allDayAmount.add(entry.getValue());
        }
        return allDayAmount;
    }


    /**
     * 根据原币兑人命币获取汇率及汇率日期，计算应收账面余额（RMB）
     * @param curreny
     * @param allOriginalAmount
     * @param receiveAgingReportModel
     */
    private void receiveExchangeRate(String curreny,BigDecimal allOriginalAmount,ReceiveAgingReportModel receiveAgingReportModel){
        Map<String, Object> queryRateMap = new HashMap<String, Object>();
        queryRateMap.put("origCurrencyCode",curreny);
        queryRateMap.put("tgtCurrencyCode", DERP.CURRENCYCODE_CNY);
        queryRateMap.put("effectiveDate", TimeUtils.format(new Date(), "yyyy-MM-dd"));
        queryRateMap.put("status", "1") ;
        ExchangeRateMongo exchangeRateMongo=exchangeRateMongoDao.findLastRateByParams(queryRateMap);
        // List<ExchangeRateMongo> rateMongoList = exchangeRateMongoDao.findAll(queryRateMap);
        //查询最近的一个汇率配置的折算汇率值和汇率日期
            /*rateMongoList = rateMongoList.stream().sorted(Comparator.comparing(ExchangeRateMongo::getEffectiveDate).reversed())
                    .collect(Collectors.toList());
            ExchangeRateMongo exchangeRateMongo = rateMongoList.get(0);*/
        if(exchangeRateMongo != null){
            receiveAgingReportModel.setRate(exchangeRateMongo.getRate());//汇率
            receiveAgingReportModel.setEffectiveDate(TimeUtils.strToSqlDate(exchangeRateMongo.getEffectiveDate()));//汇率日期
            //应收账面余额（RMB）=  应收账面余额（原币）*折算汇率值
            Double rate = exchangeRateMongo.getRate();
            BigDecimal rateBd = new BigDecimal(rate);//汇率
            BigDecimal rmbAmount = allOriginalAmount.multiply(rateBd);
            //应收收入（本币）
            receiveAgingReportModel.setRmbAmount(rmbAmount);
            //应收费用（本币）=应收费用（原币）*折算汇率值
            receiveAgingReportModel.setCostRmbAmount(receiveAgingReportModel.getCostOriginalAmount()==null?new BigDecimal(0):receiveAgingReportModel.getCostOriginalAmount().multiply(rateBd));
            //应收账面余额（本币）=应收账面余额（原币）*折算汇率值
            receiveAgingReportModel.setReceiveCostOriginalAmount(receiveAgingReportModel.getReceiveIncomeOriginalAmount().multiply(rateBd));
        }
    }


    /**
     * 根据事业部+客户的维度获取账期内天数
     * @param accountMap
     * @param accountStr
     * @param receiveAgingReportModel
     * @param buId
     * @param customerId
     * @return
     */
    private Map<String,Integer> getAccountDay(Map<String,Integer> accountMap,String accountStr,ReceiveAgingReportModel receiveAgingReportModel,Long buId,Long customerId,Long merchantId){
        if(accountMap.containsKey(accountStr)){
            receiveAgingReportModel.setAccountDay(accountMap.get(accountStr));
        }else{
            Integer accountDay=accountingReminderItemDao.getByBuIdCustomerId(buId,customerId,merchantId);
            receiveAgingReportModel.setAccountDay(accountDay==null?0:accountDay);
            accountMap.put(accountStr,accountDay==null?0:accountDay);
        }
        return accountMap;
    }

}
