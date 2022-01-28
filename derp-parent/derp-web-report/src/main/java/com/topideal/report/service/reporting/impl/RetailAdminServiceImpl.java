package com.topideal.report.service.reporting.impl;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.dao.reporting.*;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.dao.system.ExchangeRateDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.entity.dto.SaleDataDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.UserBuRelMongo;
import com.topideal.report.service.reporting.RetailAdminService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RetailAdminServiceImpl implements RetailAdminService {

    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao ;
    @Autowired
    private BusinessUnitDao businessUnitDao ;
    @Autowired
    private SaleAmountTargetDao saleAmountTargetDao;
    @Autowired
    private SaleDataDao saleDataDao;
    @Autowired
    private InWarehouseDetailsDao inWarehouseDetailsDao;
    @Autowired
    private ExchangeRateDao rateDao;
    @Autowired
    private InventoryAnalysisDao inventoryAnalysisDao;
    @Autowired
    private BuFinanceInventorySummaryDao buFinanceInventorySummaryDao;
    @Autowired
    private MerchantInfoDao merchantInfoDao;
    @Autowired
    private InventoryFallingPriceReservesDao inventoryFallingPriceReservesDao;

    @Override
    public List<BusinessUnitModel> getBuList(User user) throws SQLException {
        List<BusinessUnitModel> buList = new ArrayList<>();

        Map<String,Object> param = new HashedMap();
        param.put("userId", user.getId());
        List<UserBuRelMongo> userBuRelList = userBuRelMongoDao.findAll(param);
        List<String> buCodes = new ArrayList<String>();
        for(UserBuRelMongo mongo: userBuRelList) {
        	buCodes.add(mongo.getBuCode());
        }
        List<String> reqBuCodes = new ArrayList<String>();

        reqBuCodes.add("E071100");//宝洁品牌部
        reqBuCodes.add("E070200");//欧莱雅品牌部
        reqBuCodes.add("E080600");//母婴美赞臣组
        reqBuCodes.add("E081300");//母婴雀巢客户部
        reqBuCodes.add("E080602");//母婴美赞臣分销组
        reqBuCodes.add("E080700");//母婴新发展业务组
        reqBuCodes.add("E060000");//保健品事业部E
        reqBuCodes.add("F020814");//采销财务组
        reqBuCodes.add("E060300");//保健品事业部小米有品组
        reqBuCodes.add("I020000");//采销事业部I

        for (String reqBuCode : reqBuCodes) {
            if (buCodes.contains(reqBuCode)) {
                BusinessUnitModel model = new BusinessUnitModel();
                model.setCode(reqBuCode);
                model = businessUnitDao.searchByModel(model);

                if(model != null){
                    buList.add(model);
                }
            }
        }
        return buList;
    }
    //获取仓库信息
    @Override
    public List<Map<String,Object>> getDepotList(String month) throws Exception{
        boolean flag = true;
        List<Long> merchantList = new ArrayList<Long>();
        merchantList = getMerchantIds();

        String[] str = month.split("~");
        String startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
        String endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("merchantIds",merchantList);
        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        return inventoryFallingPriceReservesDao.getDepotInUnsellableWarning(queryMap);
    }
    //获取品牌信息
    @Override
    public List<Map<String,Object>> getBrandList(String month) throws Exception{
        boolean flag = true;
        List<Long> merchantList = new ArrayList<Long>();
        merchantList = getMerchantIds();
        String[] str = month.split("~");

        String startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
        String endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("merchantIds",merchantList);
        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        return inventoryFallingPriceReservesDao.getBrandInDepotExpireWarning(queryMap);
    }
    //事业部销量达成
    @Override
    public List<Map<String, Object>> getTargetAndAchieve(Long departmentId,String month, User user) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>() ;
        boolean flag = true;
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("month",month);
        //部门为空，获取当前用户关联事业部对应部门集合
        if(departmentId != null){
            List<Long> buIds = getBuIdByDepartmentId(departmentId, user);
            if(buIds != null && buIds.size() > 0){
            	queryMap.put("buIds",buIds);
            }else{
                flag= false;
            }
        }else{
        	List<Long> buIds = getBuIds(user);
            if(buIds != null && buIds.size() > 0){
            	queryMap.put("buIds",buIds);
            }else{
                flag= false;
            }
        }
        if(flag) {
        	List<String> brandIdsAndBrandNames = new ArrayList<String>();
            //月度和年度销售额目标
            List<Map<String, Object>> target = saleAmountTargetDao.getMonthAndYearTargetAmount(queryMap);
            //月度和年度销售额达成
            List<Map<String, Object>> achieve = saleDataDao.getMonthYearAchieveAmount(queryMap);

            Map<Long,Map<String,Object>> targetMap = new HashMap<>();
            Map<Long,Map<String,Object>> achieveMap = new HashMap<>();

            if(target != null && target.size() > 0){
                for(Map<String, Object> tMap : target){
                    Long parentBrandId = (Long) tMap.get("parentBrandId");
                    String parentBrandName = (String) tMap.get("parentBrandName");
                    targetMap.put(parentBrandId, tMap);
                    //记录品牌id
                    String key = parentBrandId+"_"+ parentBrandName;
                    BigDecimal monthTargetAmount = new BigDecimal((Double)tMap.get("monthTargetAmount"));
                    if(!brandIdsAndBrandNames.contains(key) && monthTargetAmount.compareTo(BigDecimal.ZERO) > 0) {
                    	brandIdsAndBrandNames.add(key);
                    }
                }

            }
            if(achieve != null && achieve.size() > 0){//
                for(Map<String, Object> aMap : achieve){
                    Long parentBrandId = (Long) aMap.get("parentBrandId");
                    String parentBrandName = (String) aMap.get("parentBrandName");
                    achieveMap.put(parentBrandId, aMap);
                    //记录品牌id
                    String key = parentBrandId+"_"+ parentBrandName;
                    if(!brandIdsAndBrandNames.contains(key)) {
                    	brandIdsAndBrandNames.add(key);
                    }
                }
            }
            for(String brandIdAndBrandName : brandIdsAndBrandNames){
            	Long parentBrandId = null;
            	if(StringUtils.isNumeric(brandIdAndBrandName.split("_")[0])) {
            		parentBrandId = Long.valueOf(brandIdAndBrandName.split("_")[0]);
            	}

                Map<String, Object> resultMap = new HashMap<String, Object>();
                Map<String, Object> tMap = targetMap.get(parentBrandId);
                Map<String, Object> aMap = achieveMap.get(parentBrandId);

                String parentBrandName = (String) brandIdAndBrandName.split("_")[1];
                BigDecimal monthAchieveAmount = BigDecimal.ZERO;
                BigDecimal yearAchieveAmount = BigDecimal.ZERO;
                BigDecimal monthTargetAmount = BigDecimal.ZERO;
                BigDecimal yearTargetAmount = BigDecimal.ZERO;
                if(tMap != null){
                    if(tMap.get("monthTargetAmount") != null){
                        monthTargetAmount = new BigDecimal((Double)tMap.get("monthTargetAmount"));
                        monthTargetAmount = monthTargetAmount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                    if(tMap.get("yearTargetAmount") != null){
                        yearTargetAmount = new BigDecimal((Double)tMap.get("yearTargetAmount"));
                        yearTargetAmount = yearTargetAmount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                }
                if(aMap != null){
                    if(aMap.get("monthAchieveAmount") != null){
                        monthAchieveAmount = (BigDecimal)aMap.get("monthAchieveAmount");
                        monthAchieveAmount = monthAchieveAmount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                    if(aMap.get("yearAchieveAmount") != null){
                        yearAchieveAmount = (BigDecimal)aMap.get("yearAchieveAmount");
                        yearAchieveAmount = yearAchieveAmount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                }
                resultMap.put("parentBrandId",parentBrandId);
                resultMap.put("parentBrandName",parentBrandName);
                resultMap.put("monthTargetAmount",monthTargetAmount);
                resultMap.put("yearTargetAmount",yearTargetAmount);
                resultMap.put("monthAchieveAmount",monthAchieveAmount);
                resultMap.put("yearAchieveAmount",yearAchieveAmount);
                resultList.add(resultMap);
            }
        }
        return resultList;
    }
    //品牌销量TOP8
    @Override
    public List<SaleDataDTO> getBrandSaleTop(SaleDataDTO dto, User user) throws SQLException {
        boolean flag = true;
        List<SaleDataDTO> list = null;
        //事业部为空，查询事业群数据
        if(dto.getBuId() == null){
        	List<Long> buIds = getBuIds(user);
            if(buIds != null && buIds.size() > 0){
            	dto.setBuIds(buIds);
            }else{
                flag= false;
            }
        }

        if(flag){
            String startDate= "";
            String endDate = "";
            if(StringUtils.isNotBlank(dto.getMonth())){
                String[] str = dto.getMonth().split("~");
                startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
                endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
            }else{
                SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                endDate = sdf.format(c.getTime());

                c.add(Calendar.DATE,60);
                startDate = sdf.format(c.getTime());
            }
            list = saleDataDao.getBrandSaleTop(dto,startDate,endDate);
            for(SaleDataDTO saleDto : list) {
                saleDto.setCnyAmount(saleDto.getCnyAmount().multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if(list !=null && list.size() > 7) {
            	List<String> names = new ArrayList<String>();
            	for(SaleDataDTO d : list) {
            		names.add(d.getBrandParent());
            	}
            	SaleDataDTO other = saleDataDao.getBrandSaleOther(dto, startDate, endDate, names);
            	if(other.getCnyAmount() !=null){
                    other.setCnyAmount(other.getCnyAmount().multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP));
                    list.add(other);
                }
            }
        }

        return list;
    }
    //客户销量TOP8
    @Override
    public List<SaleDataDTO> getCusSaleTop(SaleDataDTO dto, User user) throws SQLException {
        boolean flag = true;
        List<SaleDataDTO> list = null;
        //事业部为空，查询事业群数据
        if(dto.getBuId() == null){
        	List<Long> buIds = getBuIds(user);
            if(buIds != null && buIds.size() > 0){
                dto.setBuIds(buIds);
            }else{
                flag= false;
            }
        }

        if(flag){
            String startDate= "";
            String endDate = "";
            if(dto.getMonth() != null){
                String[] str = dto.getMonth().split("~");
                startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
                endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
            }else{
                SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                endDate = sdf.format(c.getTime());

                c.add(Calendar.DATE,60);
                startDate = sdf.format(c.getTime());
            }
            list = saleDataDao.getCusSaleTop(dto,startDate,endDate);
            for(SaleDataDTO saleDto : list) {
                saleDto.setCnyAmount(saleDto.getCnyAmount().multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if(list !=null && list.size() > 7) {
            	List<String> ids = new ArrayList<String>();
            	for(SaleDataDTO d : list) {
            		if(d.getCustomerId() !=null)
            			ids.add(d.getCustomerId().toString());
            	}
            	SaleDataDTO other = saleDataDao.getCusSaleOther(dto, startDate, endDate, ids);
            	if(other.getCnyAmount() != null){
                    other.setCnyAmount(other.getCnyAmount().multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP));
                    list.add(other);
                }
            }
        }
        return list;
    }
    //商品在库天数分析
    @Override
    public List<Map<String, Object>> getInWarehouseData(Long buId, String month, User user) throws SQLException {
        List<Long> rbuIds = getBuIds(user);
        Map<String , Object > queryMap = new HashMap<String , Object >() ;
        queryMap.put("month", month) ;
        queryMap.put("buId", buId) ;
        queryMap.put("buIds", rbuIds) ;

        List<Map<String, Object>> list = inWarehouseDetailsDao.inWarehouseDaysRangeData(queryMap) ;

        //表格显示列表
        List<Map<String, Object>> returnlist = new ArrayList<Map<String,Object>> (8) ;

        for(int i = 0 ; i < 8 ; i++) {
            returnlist.add(null) ;
        }

        for (Map<String, Object> map : list) {
            BigDecimal totalAmount = map.get("totalAmount") == null ? BigDecimal.ZERO : (BigDecimal)map.get("totalAmount") ;
            String currency = (String) map.get("currency");
            BigDecimal avgRate = BigDecimal.ONE;
            ExchangeRateModel exchangeRateModel = rateDao.getLatestRate(month,currency, DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY);
            if (exchangeRateModel != null && exchangeRateModel.getAvgRate() != null) {
                avgRate = new BigDecimal(exchangeRateModel.getAvgRate().toString());
            }
            map.put("totalAmount" , totalAmount.multiply(avgRate).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP)) ;
        }

        //计算库存金额占比
        for (Object mapOb : list) {
            Map<String, Object> resultMap = (Map<String, Object>) mapOb ;
            NumberFormat percent = NumberFormat.getPercentInstance();
            percent.setMaximumFractionDigits(2);

            switch (String.valueOf(resultMap.get("inWarehouseRange"))) {
                case "0~30天":
                    returnlist.set(0, resultMap);
                    break;

                case "30~60天":
                    returnlist.set(1, resultMap);
                    break;

                case "60~90天":
                    returnlist.set(2, resultMap);
                    break;

                case "90~120天":
                    returnlist.set(3, resultMap);
                    break;

                case "120~150天":
                    returnlist.set(4, resultMap);
                    break;

                case "150~180天":
                    returnlist.set(5, resultMap);
                    break;

                case "180天以上":
                    returnlist.set(6, resultMap);
                    break;

                case "未匹配":
                    returnlist.set(7, resultMap);
                    break;
            }
        }

        Iterator<Map<String, Object>> iterator = returnlist.iterator();
        while(iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            if(map == null) {
                iterator.remove();
            }
        }
        return returnlist;
    }
    //库存分析
    @Override
    public List<Map<String, Object>> getInventoryAnalysisData(Long buId, String month, String type, User user) throws SQLException {
        List<Long> rbuIds = getBuIds(user);
        Map<String , Object > queryMap = new HashMap<String , Object >() ;
        queryMap.put("month", month) ;
        queryMap.put("buId", buId) ;
        queryMap.put("buIds", rbuIds) ;
        queryMap.put("sourceType", type);

        List<Map<String, Object>> inventoryAnalysisData = inventoryAnalysisDao.getInventoryAnalysisData(queryMap);
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        Map<String, Object> otherMap = new HashMap<>();
        otherMap.put("name", "其他");
        BigDecimal otherSurplusNum = new BigDecimal("0");
        BigDecimal otherSaleNum = new BigDecimal("0");
        BigDecimal otherAmount = new BigDecimal("0");
        Integer otherSaleDays = 0;

        for (int i = 0; i < inventoryAnalysisData.size(); i++) {
            if (i<5) {
                //销售汇总数量
                BigDecimal saleNum = (BigDecimal) inventoryAnalysisData.get(i).get("saleNum");
                Integer saleDays = (Integer) inventoryAnalysisData.get(i).get("saleDays");
                //日均销量
                BigDecimal dailySaleNum = saleNum.divide(new BigDecimal(saleDays), 1, BigDecimal.ROUND_HALF_UP);
                BigDecimal surplusNum = (BigDecimal) inventoryAnalysisData.get(i).get("surplusNum");
                //预计清空天数公式=该维度下的库存量/该维度下的日均销量，四舍五入保留2位小数
                BigDecimal clearDays = new BigDecimal("0");
                if (dailySaleNum.compareTo(new BigDecimal("0")) != 0) {
                    clearDays  = surplusNum.divide(dailySaleNum, 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    clearDays = new BigDecimal(120);
                }
                Map<String, Object> inventoryAnalysisMap = inventoryAnalysisData.get(i);
                inventoryAnalysisMap.put("clearDays", clearDays);
                BigDecimal amount = (BigDecimal) inventoryAnalysisData.get(i).get("amount");
                inventoryAnalysisMap.put("amount", amount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP));
                inventoryAnalysisMap.put("surplusNum", surplusNum.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP));
                resultMapList.add(inventoryAnalysisMap);
            } else {
                BigDecimal surplusNum = (BigDecimal) inventoryAnalysisData.get(i).get("surplusNum");
                BigDecimal saleNum = (BigDecimal) inventoryAnalysisData.get(i).get("saleNum");
                BigDecimal amount = (BigDecimal) inventoryAnalysisData.get(i).get("amount");
                otherSaleDays = (Integer) inventoryAnalysisData.get(i).get("saleDays");
                otherSurplusNum = surplusNum.add(otherSurplusNum);
                otherSaleNum = saleNum.add(otherSaleNum);
                otherAmount = otherAmount.add(amount);
            }
        }
        if (inventoryAnalysisData.size() > 5) {
            //预计清空天数公式=该维度下的库存量/该维度下的日均销量，四舍五入保留2位小数
            //日均销量
            BigDecimal dailySaleNum = otherSaleNum.divide(new BigDecimal(otherSaleDays), 1, BigDecimal.ROUND_HALF_UP);
            BigDecimal clearDays = new BigDecimal("0");
            if (dailySaleNum.compareTo(new BigDecimal("0")) != 0) {
                clearDays  = otherSurplusNum.divide(dailySaleNum, 2, BigDecimal.ROUND_HALF_UP);
            } else {
                clearDays = new BigDecimal(120);
            }
            otherMap.put("amount", otherAmount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP));
            otherMap.put("surplusNum", otherSurplusNum.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP));
            otherMap.put("clearDays", clearDays);
            resultMapList.add(otherMap);
        }
        return resultMapList;
    }
    //年度进销存分析
    @Override
    public Map<String, Object> getYearFinanceInventoryAnalysis(Long buId,String month, String isParentBrand,String brandIds,User user) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>() ;
        boolean flag = true;
        SaleDataDTO saleDataDTO = new SaleDataDTO();
        BuFinanceInventorySummaryDTO buFinanceInventorySummaryDTO = new BuFinanceInventorySummaryDTO();
        //事业部为空，查询事业群数据
        if(buId == null){
            List<Long> buIds = getBuIds(user);
            if(buIds != null && buIds.size() > 0){
                saleDataDTO.setBuIds(buIds);
                saleDataDTO.setMonth(month);
                buFinanceInventorySummaryDTO.setBuList(buIds);
                buFinanceInventorySummaryDTO.setMonth(month);
            }else{
                flag= false;
            }
        }else{
            saleDataDTO.setBuId(buId);
            saleDataDTO.setMonth(month);
            buFinanceInventorySummaryDTO.setBuId(buId);
            buFinanceInventorySummaryDTO.setMonth(month);
        }
        if(flag) {
            List<Long> brandIdList =StrUtils.parseIds(brandIds);
            //年度采购结算金额、库存金额
            List<Map<String, Object>> amountList = buFinanceInventorySummaryDao.getAmountYear(buFinanceInventorySummaryDTO ,isParentBrand,brandIdList);
            //年度销售金额
            List<Map<String, Object>> saleAmountList = saleDataDao.getSaleAmountYear(saleDataDTO,isParentBrand,brandIdList);
            //12个月
            List<Map<String, Object>> returnYearList = new ArrayList<Map<String, Object>>();
            for(int i = 1 ;i<13; i++){
                Map<String, Object> returnMap = new HashMap<String, Object>();
               BigDecimal purchaseEndAmount = BigDecimal.ZERO ;
                BigDecimal endAmount =  BigDecimal.ZERO ;
                BigDecimal cnyAmount =  BigDecimal.ZERO;

                for(Map<String, Object> amountMap: amountList){
                    Integer purchaseMonth = (Integer)amountMap.get("month");
                    if(purchaseMonth.equals(i)){
                        purchaseEndAmount = amountMap.get("purchaseEndAmount")== null ? BigDecimal.ZERO : new BigDecimal((Double) amountMap.get("purchaseEndAmount"));
                        endAmount =  amountMap.get("endAmount")== null ? BigDecimal.ZERO : new BigDecimal((Double) amountMap.get("endAmount"));
                        break;
                    }
                }
                for(Map<String, Object> saleAmountMap: saleAmountList){
                    Integer saleAmountMonth = (Integer)saleAmountMap.get("month");
                    if(saleAmountMonth.equals(i)){
                        cnyAmount =  (BigDecimal)saleAmountMap.get("cnyAmount");
                        break;
                    }

                }
                returnMap.put("month", i);
                returnMap.put("purchaseEndAmount",purchaseEndAmount.multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));
                returnMap.put("inventoryEndAmount",endAmount.multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));
                returnMap.put("saleAmount",cnyAmount.multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));

                returnYearList.add(returnMap);
            }

            //品牌采购结算金额TOP8
            List<Map<String, Object>> list = buFinanceInventorySummaryDao.getBrandPurchaseAmountYear(buFinanceInventorySummaryDTO,isParentBrand,brandIdList);
            //表格显示列表
            List<Map<String, Object>> brandPurchaseList = new ArrayList<Map<String,Object>> () ;

            List<Map<String, Object>> brandsMap = buFinanceInventorySummaryDao.getBrandList(buFinanceInventorySummaryDTO,isParentBrand);//获取进销存品牌

            BigDecimal otherAmount = BigDecimal.ZERO;
            if(list !=null) {
                for(int i=0; i < list.size(); i++){
                    Double amountD = list.get(i).get("purchaseEndAmount") == null ? 0:(Double) list.get(i).get("purchaseEndAmount");
                    if(i < 8){
                        BigDecimal purchaseEndAmount = new BigDecimal(amountD);
                        list.get(i).put("purchaseEndAmount",purchaseEndAmount.multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));
                        brandPurchaseList.add(list.get(i));
                    }else{
                        BigDecimal amount =  new BigDecimal(amountD);
                        otherAmount = otherAmount.add(amount);
                    }
                }
                if(list.size() > 7){
                    Map<String,Object> otherMap = new HashMap<String,Object>();
                    otherMap.put("brandName", "其他");
                    otherMap.put("purchaseEndAmount", otherAmount.multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));
                    brandPurchaseList.add(otherMap);
                }
            }
            map.put("yearAmount", returnYearList);
            map.put("brandPurchase", brandPurchaseList);
            map.put("brandsMap", brandsMap);
        }
        return map;
    }
    //品牌在库天数
    @Override
    public List<Map<String, Object>> getBrandInWarehouse(Long buId, String month, User user, String inWarehouseRange ,String isParentBrand) throws Exception {
        boolean flag = true;
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        Map<String , Object > queryMap = new HashMap<String , Object >() ;
       //事业部为空，查询事业群数据
        if(buId == null){
            List<Long> buIds = getBuIds(user);
            if(buIds != null && buIds.size() > 0){
                queryMap.put("buIds", buIds) ;
            }else{
                flag= false;
            }
        }

        if(flag){
            queryMap.put("month", month) ;
            queryMap.put("buId", buId) ;
            queryMap.put("inWarehouseRange", inWarehouseRange);
            queryMap.put("isParentBrand", isParentBrand);
            returnList = inWarehouseDetailsDao.getBrandInWarehouse(queryMap);
            if(returnList !=null && returnList.size() > 7) {
                List<Long> brandIds = new ArrayList<Long>();
                for (Map<String, Object> brandMap : returnList) {
                    brandIds.add((Long) brandMap.get("brandId"));
                }
                queryMap.put("brandIds", brandIds);
                Map<String, Object> other = inWarehouseDetailsDao.getBrandInWarehouseOther(queryMap);
                returnList.add(other);
            }
        }
        return returnList;
    }
    //仓库滞销预警、仓库效期预警 获取公司
    @Override
    public List<MerchantInfoModel> getMerchantList() throws Exception {
        List<MerchantInfoModel> merchantList = new ArrayList<MerchantInfoModel>();

        MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194100022");//宝信
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194100049");//健太
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194200007");//润佰贸易
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194300027");//广旺贸易
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP26143500022");//卓烨贸易
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP13111400051");//香港元森泰
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        return merchantList;
    }
    //仓库滞销预警
    @Override
    public Map<String, Object> getDepotUnsellableWarning(String merchantIds, String month, String depotIds, User user) throws Exception {
        boolean flag = true;
        List<Long> merchantList = new ArrayList<Long>();
        if(StringUtils.isNotBlank(merchantIds)){
            merchantList = StrUtils.parseIds(merchantIds);
        }else{
            merchantList = getMerchantIds();
        }
        String[] str = month.split("~");
        String startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
        String endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("merchantIds",merchantList);
        queryMap.put("depotList", StrUtils.parseIds(depotIds));
        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        if(!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())){
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if(!buList.isEmpty()) {
                queryMap.put("buList",buList);
            }
        }
        List<Map<String, Object>> list = inventoryFallingPriceReservesDao.getDepotUnsellableWarning(queryMap);
        Map<String, BigDecimal> amountMap = new HashMap<String, BigDecimal>();
        Set<String> brandSet = new HashSet<String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for(Map<String, Object> map : list){
            Long brandId = (Long) map.get("brandId");
            String brandName = (String) map.get("brandName");
            String inWarehouseInterval = (String) map.get("inWarehouseInterval");
            BigDecimal totalAmount = map.get("totalAmount")== null ? BigDecimal.ZERO : new BigDecimal((Double)map.get("totalAmount")) ;

            totalAmount = totalAmount.multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP);
            amountMap.put(brandId+"_"+inWarehouseInterval,totalAmount);
            brandSet.add(brandId+"_"+brandName);
        }
        for(String brand : brandSet){
            Long brandId = Long.valueOf(brand.split("_")[0]);
            String brandName =brand.split("_")[1];

            //在仓天数区间划分为：1: 0~30天;2: 30天~60天;3: 60天~90天; 4: 90天~120天;5: 120天以上'
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("brandId", brandId);
            map.put("0~30天",amountMap.get(brandId+"_1") == null ? BigDecimal.ZERO : amountMap.get(brandId+"_1"));
            map.put("30天~60天",amountMap.get(brandId+"_2") == null ? BigDecimal.ZERO : amountMap.get(brandId+"_2"));
            map.put("60天~90天",amountMap.get(brandId+"_3") == null ? BigDecimal.ZERO : amountMap.get(brandId+"_3"));
            map.put("90天~120天",amountMap.get(brandId+"_4") == null ? BigDecimal.ZERO : amountMap.get(brandId+"_4"));
            map.put("120天以上",amountMap.get(brandId+"_5") == null ? BigDecimal.ZERO : amountMap.get(brandId+"_5"));

            resultMap.put(brandName,map);
        }
        return resultMap;
    }
    //仓库效期预警
    @Override
    public Map<String, Object> getDepotExpireWarning(String merchantIds, String month, String brandParentIds, User user) throws Exception {
        List<Long> merchantList = new ArrayList<Long>();
        if(StringUtils.isNotBlank(merchantIds)){
            merchantList = StrUtils.parseIds(merchantIds);
        }else{
            merchantList = getMerchantIds();
        }
        String[] str = month.split("~");
        String startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
        String endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("merchantIds",merchantList);
        queryMap.put("brandParentList", StrUtils.parseIds(brandParentIds));
        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        if(!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())){
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if(!buList.isEmpty()) {
                queryMap.put("buList",buList);
            }
        }
        List<Map<String, Object>> list = inventoryFallingPriceReservesDao.getDepotExpireWarning(queryMap);
        Map<String, BigDecimal> amountMap = new HashMap<String, BigDecimal>();
        Set<String> DepotSet = new HashSet<String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for(Map<String, Object> map : list){
            Long depotId = (Long) map.get("depotId");
            String depotName = (String) map.get("depotName");
            String effectiveInterval = (String) map.get("effectiveInterval");
            BigDecimal totalAmount =  map.get("totalAmount")== null ? BigDecimal.ZERO :new BigDecimal((Double)map.get("totalAmount")) ;

            String effectiveIntervalStr = "";
            if(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_1.equals(effectiveInterval) ||DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_7.equals(effectiveInterval)){
                effectiveIntervalStr = "1";
            }else if(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_2.equals(effectiveInterval)){
                effectiveIntervalStr = "2";
            }else if(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_3.equals(effectiveInterval)){
                effectiveIntervalStr = "3";
            }else if(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_4.equals(effectiveInterval)){
                effectiveIntervalStr = "4";
            }else if(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_5.equals(effectiveInterval) || DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_6.equals(effectiveInterval)){
                effectiveIntervalStr = "5";
            }
            String key = depotId + "_" + effectiveIntervalStr;
            if(amountMap.containsKey(key)){
                totalAmount = amountMap.get(key).add(totalAmount);
            }
            amountMap.put(key,totalAmount);
            DepotSet.add(depotId+"_"+depotName);
        }
        for(String depot:DepotSet){
            Long depotId = Long.valueOf(depot.split("_")[0]);
            String depotName =depot.split("_")[1];

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("depotId", depotId);
            map.put("1/10以下及过期",amountMap.get(depotId+"_1") == null ? BigDecimal.ZERO : amountMap.get(depotId+"_1").multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));
            map.put("1/10-1/5",amountMap.get(depotId+"_2") == null ? BigDecimal.ZERO : amountMap.get(depotId+"_2").multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));
            map.put("1/5-1/3",amountMap.get(depotId+"_3") == null ? BigDecimal.ZERO : amountMap.get(depotId+"_3").multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));
            map.put("1/3-1/2",amountMap.get(depotId+"_4") == null ? BigDecimal.ZERO : amountMap.get(depotId+"_4").multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));
            map.put("1/2以上",amountMap.get(depotId+"_5") == null ? BigDecimal.ZERO : amountMap.get(depotId+"_5").multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));

            resultMap.put(depotName,map);
        }
        return resultMap;
    }
    //获取仓库滞销预警各品牌金额明细
    @Override
    public List<Map<String, Object>> getDepotUnsellableDetail(String merchantIds, String month, Long brandParentId,String inWarehouseInterval, String depotIds, User user) throws Exception {
        List<Long> merchantList = new ArrayList<Long>();
        if(StringUtils.isNotBlank(merchantIds)){
            merchantList = StrUtils.parseIds(merchantIds);
        }else{
            merchantList = getMerchantIds();
        }
        String[] str = month.split("~");
        String startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
        String endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("merchantIds",merchantList);
        queryMap.put("brandParentId", brandParentId);
        queryMap.put("inWarehouseInterval", inWarehouseInterval);
        queryMap.put("depotList", StrUtils.parseIds(depotIds));
        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        if(!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())){
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if(!buList.isEmpty()) {
                queryMap.put("buList",buList);
            }
        }
        List<Map<String, Object>> resultList = inventoryFallingPriceReservesDao.getDepotUnsellableDetail(queryMap);
        for(Map<String, Object> map : resultList){
            BigDecimal totalAmount = map.get("totalAmount") == null ? BigDecimal.ZERO : new BigDecimal((Double) map.get("totalAmount"));
            map.put("totalAmount",totalAmount.multiply(new BigDecimal("0.0001")).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        return resultList;
    }
    //事业部销售达成导出
    @Override
    public List<Map<String,Object>> getTargetAndAchieveExportList(Long departmentId,String month, User user) throws Exception{
    	List<Map<String, Object>> resultList = new ArrayList<>() ;
        boolean flag = true;
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("month",month);
        //部门为空，获取当前用户关联事业部对应部门集合
        if(departmentId != null){
            List<Long> buIds = getBuIdByDepartmentId(departmentId, user);
            if(buIds != null && buIds.size() > 0){
            	queryMap.put("buIds",buIds);
            }else{
                flag= false;
            }
        }else{
        	List<Long> buIds = getBuIds(user);
            if(buIds != null && buIds.size() > 0){
            	queryMap.put("buIds",buIds);
            }else{
                flag= false;
            }
        }
        if(flag) {
            List<String> departmentsAndBusAndBrands = new ArrayList<String>();
            //获取月度达成和年度达成导出列表
            List<Map<String, Object>> achieveList = saleDataDao.getAchieveExportList(queryMap);
            //获取月度达成和年度目标导出列表
            List<Map<String, Object>> targetList = saleAmountTargetDao.getTargetExportList(queryMap);
            Map<String,Map<String,Object>> targetMap = new HashMap<>();
            Map<String,Map<String,Object>> achieveMap = new HashMap<>();

            if(targetList != null){
                for(Map<String, Object> tMap : targetList){
                    Long parentBrandId = (Long) tMap.get("parentBrandId");
                    String parentBrandName = (String) tMap.get("parentBrandName");
                    Long departId = (Long) tMap.get("departmentId");
                    String departmentName = (String) tMap.get("departmentName");
                    Long buId = (Long) tMap.get("buId");
                    String buName = (String) tMap.get("buName");
                    targetMap.put(departId+"_"+buId +"_"+parentBrandId, tMap);
                    //记录品牌id
                    BigDecimal monthTargetAmount = new BigDecimal((Double)tMap.get("monthTargetAmount"));
                    String key = departId+"_"+departmentName+"_"+buId+"_"+ buName+"_"+parentBrandId+"_"+ parentBrandName;
                    if(!departmentsAndBusAndBrands.contains(key) && monthTargetAmount.compareTo(BigDecimal.ZERO) > 0) {
                    	departmentsAndBusAndBrands.add(key);
                    }
                }

            }
            if(achieveList != null){
                for(Map<String, Object> aMap : achieveList){
                    Long parentBrandId = (Long) aMap.get("parentBrandId");
                    String parentBrandName = (String) aMap.get("parentBrandName");
                    Long departId = (Long) aMap.get("departmentId");
                    String departmentName = (String) aMap.get("departmentName");
                    Long buId = (Long) aMap.get("buId");
                    String buName = (String) aMap.get("buName");
                    achieveMap.put(departId+"_"+buId +"_"+parentBrandId, aMap);
                    String key = departId+"_"+departmentName+"_"+buId+"_"+ buName+"_"+parentBrandId+"_"+ parentBrandName;
                    if(!departmentsAndBusAndBrands.contains(key)) {
                    	departmentsAndBusAndBrands.add(key);
                    }
                }
            }
            for(String  departmentAndBuAndBrand: departmentsAndBusAndBrands){
            	Long departId = Long.valueOf(departmentAndBuAndBrand.split("_")[0]);
            	String departmentName = (String) departmentAndBuAndBrand.split("_")[1];
            	Long buId = Long.valueOf(departmentAndBuAndBrand.split("_")[2]);
            	String buName = (String) departmentAndBuAndBrand.split("_")[3];
            	Long parentBrandId = null;
            	if(StringUtils.isNumeric(departmentAndBuAndBrand.split("_")[4])) {
            		parentBrandId = Long.valueOf(departmentAndBuAndBrand.split("_")[4]);
            	}
            	String parentBrandName = (String) departmentAndBuAndBrand.split("_")[5];

            	String key = departId+"_"+buId +"_"+parentBrandId;
                Map<String, Object> tMap = targetMap.get(key);
                Map<String, Object> aMap = achieveMap.get(key);

                BigDecimal monthAchieveAmount = BigDecimal.ZERO;
                BigDecimal yearAchieveAmount = BigDecimal.ZERO;
                BigDecimal monthTargetAmount = BigDecimal.ZERO;
                BigDecimal yearTargetAmount = BigDecimal.ZERO;
                if(tMap != null){
                    if(tMap.get("monthTargetAmount") != null){
                        monthTargetAmount = new BigDecimal((Double)tMap.get("monthTargetAmount"));
//                        monthTargetAmount = monthTargetAmount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                    if(tMap.get("yearTargetAmount") != null){
                        yearTargetAmount = new BigDecimal((Double)tMap.get("yearTargetAmount"));
//                        yearTargetAmount = yearTargetAmount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                }
                if(aMap != null){
                    if(aMap.get("monthAchieveAmount") != null){
                        monthAchieveAmount = (BigDecimal)aMap.get("monthAchieveAmount");
//                        monthAchieveAmount = monthAchieveAmount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                    if(aMap.get("yearAchieveAmount") != null){
                        yearAchieveAmount = (BigDecimal)aMap.get("yearAchieveAmount");
//                        yearAchieveAmount = yearAchieveAmount.multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                }
                BigDecimal monthRate = BigDecimal.ZERO;
                if(monthTargetAmount.compareTo(BigDecimal.ZERO) > 0) {
                	monthRate = monthAchieveAmount.divide(monthTargetAmount, 8, BigDecimal.ROUND_HALF_UP);
                }
                monthRate = monthRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal yearRate = BigDecimal.ZERO;
                if(yearTargetAmount.compareTo(BigDecimal.ZERO) > 0) {
                	yearRate = yearAchieveAmount.divide(yearTargetAmount, 8, BigDecimal.ROUND_HALF_UP);
                }
                yearRate = yearRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

                Map<String, Object> map = new HashMap<String, Object>() ;
                map.put("departmentName",departmentName);
                map.put("buName",buName);
                map.put("month",month);
                map.put("parentBrandName", parentBrandName);
                map.put("currency","港币");
                map.put("monthAchieveAmount",monthAchieveAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                map.put("monthTargetAmount",monthTargetAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                map.put("monthRate", monthRate.compareTo(BigDecimal.ZERO) > 0 ? monthRate.toString()+"%" : "-");
                map.put("yearAchieveAmount",yearAchieveAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                map.put("yearTargetAmount",yearTargetAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                map.put("yearRate",yearRate.compareTo(BigDecimal.ZERO) > 0 ? yearRate.toString()+"%" : "-");

                resultList.add(map);
            }

        }
        return resultList;
    }
    //年度进销存分析导出
    @Override
    public List<BuFinanceInventorySummaryDTO> getYearFinanceInventoryAnalysisExportList(Long buId,String month, String isParentBrand,String brandIds,User user){
        List<BuFinanceInventorySummaryDTO> list = new ArrayList<BuFinanceInventorySummaryDTO>();
        BuFinanceInventorySummaryDTO buFinanceInventorySummaryDTO = new BuFinanceInventorySummaryDTO();
        boolean flag = true;
        //事业部为空，查询事业群数据
        List<Long> buIds = null;
        if(buId == null){
            buIds = getBuIds(user);
            if(buIds != null && buIds.size() > 0){
               buFinanceInventorySummaryDTO.setBuList(buIds);
                buFinanceInventorySummaryDTO.setMonth(month);
            }else{
                flag= false;
            }
        }else{
            buFinanceInventorySummaryDTO.setBuId(buId);
            buFinanceInventorySummaryDTO.setMonth(month);
        }
        if(flag) {
            List<Long> brandIdList = StrUtils.parseIds(brandIds);
            //获取销售金额
//            Map<String,Object> paramMap =  new HashMap<String, Object>() ;
//            paramMap.put("buId",buId);
//            paramMap.put("buList",buIds);
//            paramMap.put("month",month);
//            paramMap.put("brandIdList",brandIdList);
//            List<Map<String,Object>> saleAmountList = saleDataDao.getSaleCnyAmountList(paramMap);
//            Map<String,Object> saleAmountMap = new HashMap<String,Object>();
//            for(Map<String,Object> saleMap : saleAmountList){
//                String saleMonth = (String) saleMap.get("month");
//                Long saleBuId = (Long) saleMap.get("buId");
//                Long saleBrandId = (Long) saleMap.get("brandId");
//                BigDecimal saleCnyAmount = (BigDecimal) saleMap.get("cnyAmount");
//
//                String key = saleBuId + "_" + saleBrandId + "_" + saleMonth;
//                saleAmountMap.put(key,saleCnyAmount);
//            }
            //获取采购金额、库存金额、销售金额
            list = buFinanceInventorySummaryDao.getYearFinanceInventoryAnalysisExportList(buFinanceInventorySummaryDTO , isParentBrand,brandIdList);
            for(BuFinanceInventorySummaryDTO dto : list){
                BigDecimal purchaseEndAmount = dto.getPurchaseEndAmount() == null ? BigDecimal.ZERO : dto.getPurchaseEndAmount();
                BigDecimal endAmount = dto.getEndAmount() == null ? BigDecimal.ZERO : dto.getEndAmount();
                BigDecimal saleEndAmount = dto.getSaleEndAmount() == null ? BigDecimal.ZERO : dto.getSaleEndAmount();

//                String saleKey = dto.getBuId() + "_" + dto.getBrandId() + "_" + dto.getMonth();

                dto.setPurchaseEndAmount(purchaseEndAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                dto.setSaleEndAmount(saleEndAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                dto.setEndAmount(endAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                dto.setCurrency("人民币");
            }
        }
        return list;
    }
    //客户销量导出
    @Override
    public List<Map<String, Object>> getCustomersExportList(SaleDataDTO dto, User user){
        boolean flag = true;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //事业部为空，查询事业群数据
        if(dto.getBuId() == null){
            List<Long> buIds = getBuIds(user);
            if(buIds != null && buIds.size() > 0){
                dto.setBuIds(buIds);
            }else{
                flag= false;
            }
        }
        if(flag){
            String startDate= "";
            String endDate = "";
            if(StringUtils.isNotBlank(dto.getMonth())){
                String[] str = dto.getMonth().split("~");
                startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
                endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
            }else{
                SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                endDate = sdf.format(c.getTime());

                c.add(Calendar.DATE,60);
                startDate = sdf.format(c.getTime());
            }
            resultList = saleDataDao.getCustomerExportList(dto,startDate,endDate);
            for(Map<String, Object> map : resultList){
                //金额转为万元
                BigDecimal cynAmount = map.get("cynAmount") == null ? BigDecimal.ZERO : (BigDecimal) map.get("cynAmount");
                map.put("cynAmount", cynAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }
        return resultList;
    }
    //品牌销量导出
    @Override
    public List<Map<String, Object>> getBrandExportList(SaleDataDTO dto, User user){
        boolean flag = true;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //事业部为空，查询事业群数据
        if(dto.getBuId() == null){
            List<Long> buIds = getBuIds(user);
            if(buIds != null && buIds.size() > 0){
                dto.setBuIds(buIds);
            }else{
                flag= false;
            }
        }
        if(flag){
            String startDate= "";
            String endDate = "";
            if(StringUtils.isNotBlank(dto.getMonth())){
                String[] str = dto.getMonth().split("~");
                startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
                endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
            }else{
                SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                endDate = sdf.format(c.getTime());

                c.add(Calendar.DATE,60);
                startDate = sdf.format(c.getTime());
            }
            resultList = saleDataDao.getBrandExportList(dto,startDate,endDate);
            for(Map<String, Object> map : resultList){
                //金额转为万元
                BigDecimal cynAmount = map.get("cynAmount") == null ? BigDecimal.ZERO : (BigDecimal) map.get("cynAmount");
                map.put("cynAmount", cynAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                map.put("currency","人民币");
            }

        }
        return resultList;
    }
    //库存量分析导出
    @Override
    public List<Map<String, Object>>  getInventoryAnalysisExportList(Long buId, String month, String type, User user) {
        List<Long> rbuIds = getBuIds(user);
        Map<String , Object > queryMap = new HashMap<String , Object >() ;
        queryMap.put("month", month) ;
        queryMap.put("buId", buId) ;
        queryMap.put("buIds", rbuIds) ;
        queryMap.put("sourceType", type);

        List<Map<String, Object>> inventoryAnalysisData = inventoryAnalysisDao.getInventoryAnalysisExportList(queryMap);
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        for (int i = 0; i < inventoryAnalysisData.size(); i++) {
            //销售汇总数量
            Integer saleNum = (Integer) inventoryAnalysisData.get(i).get("saleNum");
            Integer saleDays = (Integer) inventoryAnalysisData.get(i).get("saleDays");
            //日均销量
            BigDecimal dailySaleNum = new BigDecimal(saleNum).divide(new BigDecimal(saleDays), 1, BigDecimal.ROUND_HALF_UP);
            Integer surplusNum = (Integer) inventoryAnalysisData.get(i).get("surplusNum");
            //预计清空天数公式=该维度下的库存量/该维度下的日均销量，四舍五入保留2位小数
            BigDecimal clearDays = new BigDecimal("0");
            if (dailySaleNum.compareTo(new BigDecimal("0")) != 0) {
                clearDays  = new BigDecimal(surplusNum).divide(dailySaleNum, 2, BigDecimal.ROUND_HALF_UP);
            } else {
                clearDays = new BigDecimal(120);
            }
            Map<String, Object> inventoryAnalysisMap = inventoryAnalysisData.get(i);
            inventoryAnalysisMap.put("clearDays", clearDays);//预计清空天数
            inventoryAnalysisMap.put("dailySaleNum", dailySaleNum);//日均销量

            //金额转为万元
            BigDecimal totalAmount = inventoryAnalysisData.get(i).get("amount") == null ? BigDecimal.ZERO : (BigDecimal) inventoryAnalysisData.get(i).get("amount");
            inventoryAnalysisMap.put("amount" , totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP)) ;

            inventoryAnalysisMap.put("currency","人民币");
            resultMapList.add(inventoryAnalysisMap);
        }
        return resultMapList;
    }
    //商品在库天数导出
    @Override
    public List<Map<String, Object>> getInWarehouseDaysExportList(Long buId, String month, User user) throws Exception {
        List<Long> rbuIds = getBuIds(user);
        Map<String , Object > queryMap = new HashMap<String , Object >() ;
        queryMap.put("month", month) ;
        queryMap.put("buId", buId) ;
        queryMap.put("buIds", rbuIds) ;

        List<Map<String, Object>> returnlist = inWarehouseDetailsDao.getInWarehouseDaysExportList(queryMap) ;

        for (Map<String, Object> map : returnlist) {
            String currency = (String) map.get("currency");
            BigDecimal avgRate = BigDecimal.ONE;
            ExchangeRateModel exchangeRateModel = rateDao.getLatestRate(month, currency, DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY);
            if (exchangeRateModel != null && exchangeRateModel.getAvgRate() != null) {
                avgRate = new BigDecimal(exchangeRateModel.getAvgRate().toString());
            }

            BigDecimal totalAmount = map.get("totalAmount") == null ? new BigDecimal(0.0) : (BigDecimal) map.get("totalAmount");
            map.put("totalAmount" , totalAmount.multiply(avgRate).setScale(2, BigDecimal.ROUND_HALF_UP)) ;

            BigDecimal weightedAmount = map.get("weightedAmount") == null ? new BigDecimal(0.0) : (BigDecimal) map.get("weightedAmount");
            map.put("weightedAmount" , weightedAmount.multiply(avgRate).setScale(2, BigDecimal.ROUND_HALF_UP)) ;

            Integer totalNum = map.get("totalNum") == null ? 0 : (Integer) map.get("totalNum");
            BigDecimal weightedPrice = BigDecimal.ZERO.equals(totalNum)? weightedAmount : weightedAmount.divide(new BigDecimal(totalNum) ,2,BigDecimal.ROUND_HALF_UP);
            map.put("weightedPrice" , weightedPrice) ;
        }
        return returnlist;
    }
    //仓库滞销预警导出
    @Override
    public List<Map<String, Object>> getDepotUnsellableWarningExportList(String merchantIds, String month, User user,String depotIds) throws Exception{
        boolean flag = true;
        List<Long> merchantList = new ArrayList<Long>();
        if(StringUtils.isNotBlank(merchantIds)){
            merchantList = StrUtils.parseIds(merchantIds);
        }else{
            merchantList = getMerchantIds();
        }
        String[] str = month.split("~");
        String startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
        String endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("merchantIds",merchantList);
        queryMap.put("depotList",StrUtils.parseIds(depotIds));
        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        if(!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())){
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if(!buList.isEmpty()) {
                queryMap.put("buList",buList);
            }
        }
        List<Map<String,Object>> returnList = inventoryFallingPriceReservesDao.getDepotUnsellableWarningExportList(queryMap) ;
        for (Map<String,Object> map : returnList ) {
             //库存类型
            String inverntoryType = (String) map.get("inverntoryType");
            String inverntoryTypelLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.fallingPrice_initinventoryTypeList,inverntoryType);
            map.put("inverntoryType",inverntoryTypelLabel);
            //在仓天数区间划分为：1: 0~30天;2: 30天~60天;3: 60天~90天; 4: 90天~120天;5: 120天以上'
            String inWarehouseInterval = (String) map.get("inWarehouseInterval");
            String inWarehouseIntervalStr = "";
            if("1".equals(inWarehouseInterval)){
                inWarehouseIntervalStr = "0~30天";
            }else if("2".equals(inWarehouseInterval)){
                inWarehouseIntervalStr = "30天~60天";
            }else if("3".equals(inWarehouseInterval)){
                inWarehouseIntervalStr = "60天~90天";
            }else if("4".equals(inWarehouseInterval)){
                inWarehouseIntervalStr = "90天~120天";
            }else if("5".equals(inWarehouseInterval)) {
                inWarehouseIntervalStr = "120天以上";
            }
            map.put("inWarehouseInterval",inWarehouseIntervalStr);

            String reportMonth = (String) map.get("reportMonth");
            BigDecimal avgRate = BigDecimal.ONE;
            ExchangeRateModel exchangeRateModel = rateDao.getLatestRate(reportMonth, DERP_SYS.EXCHANGERATE_CURRENCYCODE_HKD, DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY);
            if (exchangeRateModel != null && exchangeRateModel.getAvgRate() != null) {
                avgRate = new BigDecimal(exchangeRateModel.getAvgRate().toString());
            }
            BigDecimal totalAmount = map.get("totalAmount") == null ? new BigDecimal(0.0) : (BigDecimal) map.get("totalAmount");
            map.put("totalAmountCNY" , totalAmount.multiply(avgRate).setScale(2, BigDecimal.ROUND_HALF_UP)) ;

        }
        return returnList;
    }
    //仓库效期预警导出
    @Override
    public List<Map<String,Object>> getDepotExpireWarningExportList(String merchantIds, String month, User user, String brandParentIds) throws Exception {
        boolean flag = true;
        List<Long> merchantList = new ArrayList<Long>();
        if(StringUtils.isNotBlank(merchantIds)){
            merchantList = StrUtils.parseIds(merchantIds);
        }else{
            merchantList = getMerchantIds();
        }
        String[] str = month.split("~");
        String startDate =StringUtils.isBlank(str[0].trim()) ? "" : str[0].trim() ;
        String endDate = StringUtils.isBlank(str[1].trim()) ? "" : str[1].trim();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("merchantIds",merchantList);
        queryMap.put("brandParentList",StrUtils.parseIds(brandParentIds));
        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        if(!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())){
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if(!buList.isEmpty()) {
                queryMap.put("buList",buList);
            }
        }
        List<Map<String,Object>> returnList = inventoryFallingPriceReservesDao.getDepotExpireWarningExportList(queryMap) ;
        for (Map<String,Object> map : returnList ) {
            //剩余效期占比转换
            BigDecimal surplusProportion = (BigDecimal) map.get("surplusProportion");
            if(surplusProportion != null){
                surplusProportion = surplusProportion.multiply(new BigDecimal(100)).setScale(2);
            }else{
                surplusProportion = BigDecimal.ZERO;
            }
            map.put("surplusProportion",surplusProportion);

            //库存类型
            String inverntoryType = (String) map.get("inverntoryType");
            String inverntoryTypelLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.fallingPrice_initinventoryTypeList,inverntoryType);
            map.put("inverntoryType",inverntoryTypelLabel);
            //效期区间
            String effectiveInterval = (String) map.get("effectiveInterval");
            String effectiveIntervalLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.fallingPrice_effectiveIntervalLabelList,effectiveInterval);
            map.put("effectiveInterval",effectiveIntervalLabel);
            //剩余效期占比(财务逻辑）
            String financialSurplusProportion = (String) map.get("financialSurplusProportion");
            String financialSurplusProportionLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.fallingPrice_financialSurplusproportionList,financialSurplusProportion);
            map.put("financialSurplusProportion",financialSurplusProportionLabel);

            String reportMonth = (String) map.get("reportMonth");
            BigDecimal avgRate = BigDecimal.ONE;
            ExchangeRateModel exchangeRateModel = rateDao.getLatestRate(reportMonth, DERP_SYS.EXCHANGERATE_CURRENCYCODE_HKD, DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY);
            if (exchangeRateModel != null && exchangeRateModel.getAvgRate() != null) {
                avgRate = new BigDecimal(exchangeRateModel.getAvgRate().toString());
            }
            BigDecimal totalAmount = map.get("totalAmount") == null ? new BigDecimal(0.0) : (BigDecimal) map.get("totalAmount");
            map.put("totalAmountCNY" , totalAmount.multiply(avgRate).setScale(2, BigDecimal.ROUND_HALF_UP)) ;
        }
        return returnList;
    }

    /**
     * 获取事业部id
     * @return
     */
    private List<Long> getBuIds(User user) {
    	List<Long> buIds = new ArrayList<Long>();
    	Map<String,Object> param = new HashedMap();
        param.put("userId", user.getId());
        List<UserBuRelMongo> userBuRelList = userBuRelMongoDao.findAll(param);

        List<String> reqBuCodes = new ArrayList<String>();

        reqBuCodes.add("E071100");//宝洁品牌部
        reqBuCodes.add("E070200");//欧莱雅品牌部
        reqBuCodes.add("E080600");//母婴美赞臣组
        reqBuCodes.add("E081300");//母婴雀巢客户部
        reqBuCodes.add("E080602");//母婴美赞臣分销组
        reqBuCodes.add("E080700");//母婴新发展业务组
        reqBuCodes.add("E060000");//保健品事业部E
        reqBuCodes.add("F020814");//采销财务组
        reqBuCodes.add("E060300");//保健品事业部小米有品组
        reqBuCodes.add("I020000");//采销事业部I

        for(String codes :reqBuCodes) {
            for(UserBuRelMongo mongo: userBuRelList) {
         	if(codes.equals(mongo.getBuCode())) {
         		buIds.add(mongo.getBuId());
         	}
         }
       }
    	return buIds;
    }
    /**
     * 获取公司id
     * @return
     */
    private List<Long> getMerchantIds() throws Exception {
        List<Long> merchantList = new ArrayList<Long>();
        MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194100022");//宝信
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel.getId());

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194100049");//健太
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel.getId());

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194200007");//润佰贸易
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel.getId());

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194300027");//广旺贸易
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel.getId());

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP26143500022");//卓烨贸易
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel.getId());

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP13111400051");//香港元森泰
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel.getId());
        return merchantList;
    }
    /**
     * 获取部门id
     * @return
     */
    @Override
    public List<Long> getDepartmentIds(User user) throws Exception{
        List<Long> departmentIds = new ArrayList<Long>();
        Map<String,Object> param = new HashedMap();
        param.put("userId", user.getId());
        List<UserBuRelMongo> userBuRelList = userBuRelMongoDao.findAll(param);

        List<String> reqBuCodes = new ArrayList<String>();

        reqBuCodes.add("E071100");//宝洁品牌部
        reqBuCodes.add("E070200");//欧莱雅品牌部
        reqBuCodes.add("E080600");//母婴美赞臣组
        reqBuCodes.add("E081300");//母婴雀巢客户部
        reqBuCodes.add("E080602");//母婴美赞臣分销组
        reqBuCodes.add("E080700");//母婴新发展业务组
        reqBuCodes.add("E060000");//保健品事业部E
        reqBuCodes.add("F020814");//采销财务组
        reqBuCodes.add("E060300");//保健品事业部小米有品组
        reqBuCodes.add("I020000");//采销事业部I

        for(String codes :reqBuCodes) {
            for(UserBuRelMongo mongo: userBuRelList) {
                if(codes.equals(mongo.getBuCode())) {
                    BusinessUnitModel model = new BusinessUnitModel();
                    model.setCode(codes);
                    model = businessUnitDao.searchByModel(model);

                    if(model.getDepartmentId() != null && !departmentIds.contains(model.getDepartmentId())){
                        departmentIds.add(model.getDepartmentId());
                    }
                }
            }
        }
        return departmentIds;
    }

    private List<Long> getBuIdByDepartmentId(Long departmentId, User user) throws Exception{
    	 List<Long> result = new ArrayList<Long>();
         Map<String,Object> param = new HashedMap();
         param.put("userId", user.getId());
         List<UserBuRelMongo> userBuRelList = userBuRelMongoDao.findAll(param);

         BusinessUnitModel model = new BusinessUnitModel();
         model.setDepartmentId(departmentId);
         List<BusinessUnitModel> buList = businessUnitDao.list(model);

         List<Long> userBuIds =userBuRelList.stream().map(UserBuRelMongo :: getBuId).collect(Collectors.toList());
         List<Long> buIdsBydepartmentId = buList.stream().map(BusinessUnitModel :: getId).collect(Collectors.toList());

         result = userBuIds.stream().filter( buId -> buIdsBydepartmentId.contains(buId)).collect(Collectors.toList());


         return result;
    }

}
