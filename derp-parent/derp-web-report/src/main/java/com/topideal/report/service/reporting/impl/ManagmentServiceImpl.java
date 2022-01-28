package com.topideal.report.service.reporting.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.NumberUtil;
import com.topideal.dao.inventory.BuInventoryDao;
import com.topideal.dao.inventory.BuInventoryItemDao;
import com.topideal.dao.reporting.*;
import com.topideal.dao.system.*;
import com.topideal.entity.dto.*;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.CustomerMerchantRelModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.DepartmentInfoMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.dao.UserMerchantRelMongoDao;
import com.topideal.mongo.entity.DepartmentInfoMongo;
import com.topideal.mongo.entity.UserBuRelMongo;
import com.topideal.report.service.reporting.ManagmentService;
import com.topideal.report.service.reporting.RetailAdminService;
import com.topideal.report.webapi.form.ManagmentReportForm;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ManagmentServiceImpl implements ManagmentService {

    private static final String UNIT_STRING_WAN = "万";
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

    @Autowired
    private RetailAdminService retailAdminService;
    @Autowired
    private UserMerchantRelMongoDao userMerchantRelMongoDao;
    @Autowired
    private CustomerMerchantRelDao customerMerchantRelDao;
    @Autowired
    private MerchantDepotBuRelDao merchantDepotBuRelDao;
    @Autowired
    private DepotInfoDao depotInfoDao;
    @Autowired
    private DepartmentInfoMongoDao departmentInfoMongoDao;
    @Autowired
    private BuInventoryItemDao buInventoryItemDao;
    @Autowired
    private BuInventoryDao inventoryDao;

    /**
     * 根据传入的月份，得到前三个月
     * @param form
     */
    private void convertBeforeMonth(ManagmentReportForm form) {
        // 根据传入的month， 获取传入月份前两个月
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        if(StringUtils.isNotBlank(form.getMonth())) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(simpleDateFormat.parse(form.getMonth()));
                cal.add(Calendar.MONTH, -2);
                Date beforeDate = cal.getTime();

                Calendar calMiddle = Calendar.getInstance();
                calMiddle.setTime(simpleDateFormat.parse(form.getMonth()));
                calMiddle.add(Calendar.MONTH, -1);
                Date middleDate = calMiddle.getTime();

                form.setBeforeMonth(simpleDateFormat.format(beforeDate));
                form.setMiddleMonth(simpleDateFormat.format(middleDate));
                form.setTargetMonth(form.getMonth());
            }catch (Exception e) {
                throw new RuntimeException("时间转换失败");
            }
        }
    }

    /**
     * 校验事业部与公司是否在规定的范围内
     * @param paramMap
     */
    private void checkBussinessAndBuByUser(User user, ManagmentReportForm form, Map<String, Object> paramMap){
        try {
            List<BusinessUnitModel> buList = retailAdminService.getBuList(user);
            Set<Long> buIdSet = buList.stream().map(e -> e.getId()).collect(Collectors.toSet());
            if(form.getBuIds() == null || form.getBuIds().isEmpty()) {
                if(buIdSet != null && buIdSet.size() > 0){
                    paramMap.put("buIds",buIdSet);
                }
            }else {
                form.setBuIds(form.getBuIds().stream().filter(e -> buIdSet.contains(e)).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            throw new RuntimeException("获取事业部失败", e);
        }
        try {
            List<MerchantInfoModel> merchantInfoModelList = getMerchantList(user);
            Set<Long> merchantIdList = merchantInfoModelList.stream().filter(entity -> entity != null)
                    .map(entity -> {
                        return entity.getId();
                    }).collect(Collectors.toSet());

            if(form.getMerchantIds() == null || form.getMerchantIds().isEmpty()) {
                if(merchantIdList != null && merchantIdList.size() > 0){
                    paramMap.put("merchantIds", merchantIdList);
                }
            }else {
                form.setMerchantIds(form.getMerchantIds().stream().filter(e -> merchantIdList.contains(e)).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取公司失败", e);
        }

        try{
            List<Long> departmentIds = retailAdminService.getDepartmentIds(user);
            if(form.getDepartmentIds() == null || form.getDepartmentIds().isEmpty()) {
                if(departmentIds != null && departmentIds.size() > 0){
                    paramMap.put("departmentIds", departmentIds);
                }
            }else {
                form.setDepartmentIds(form.getDepartmentIds().stream().filter(e -> departmentIds.contains(e)).collect(Collectors.toList()));
            }
        }catch (Exception e) {
            throw new RuntimeException("获取部门失败", e);
        }
    }

    @Override
    public List getDepartmentSalesAmountStatistic(ManagmentReportForm form, User user) {
        if(form == null) {
            return null;
        }
        BigDecimal unitBigDecimal = new BigDecimal(10000);
        // 获取前两个月的月份
        convertBeforeMonth(form);

        Map<String, Object> paramMap = object2Map(form);
        // 校验公司+事业部+部门
        checkBussinessAndBuByUser(user, form, paramMap);

        List<ManageDepartmentSaleDataDTO> resultList = saleDataDao.getDepartmentSalesAmountStatistic(paramMap);

        if(resultList == null || resultList.isEmpty()) {
            return null;
        }

        // 最终要返回的结果集
        List<ManageDepartmentSaleDataDTO> result = new ArrayList<>();

        // 生成总合计
        // 1. 第一条记录， 总金额DTO
        ManageDepartmentSaleDataDTO sumDTO = new ManageDepartmentSaleDataDTO();
        sumDTO.setMonthAmount1(BigDecimal.ZERO);
        sumDTO.setMonthAmount2(BigDecimal.ZERO);
        sumDTO.setMonthAmount3(BigDecimal.ZERO);
        resultList.stream().forEach(entity -> {
            entity.setMonthAmount1(NumberUtil.convertNumber(entity.getMonthAmount1(), unitBigDecimal, 2));
            entity.setMonthAmount2(NumberUtil.convertNumber(entity.getMonthAmount2(), unitBigDecimal, 2));
            entity.setMonthAmount3(NumberUtil.convertNumber(entity.getMonthAmount3(), unitBigDecimal, 2));
            sumDTO.setMonthAmount1(sumDTO.getMonthAmount1().add(entity.getMonthAmount1() == null ? BigDecimal.ZERO : entity.getMonthAmount1()));
            sumDTO.setMonthAmount2(sumDTO.getMonthAmount2().add(entity.getMonthAmount2() == null ? BigDecimal.ZERO : entity.getMonthAmount2()));
            sumDTO.setMonthAmount3(sumDTO.getMonthAmount3().add(entity.getMonthAmount3() == null ? BigDecimal.ZERO : entity.getMonthAmount3()));
        });
        sumDTO.setDepartmentSumFlag(true);
        sumDTO.setDepartmentSumTitle("合计");
        result.add(sumDTO);

        // 根据部门进行分组
        Map<Long, List<ManageDepartmentSaleDataDTO>> collect = resultList.stream().collect(
                Collectors.groupingBy(ManageDepartmentSaleDataDTO::getDepartmentId)
        );

        // 生成部门合计和部门数据
        collect.forEach((departId, dtoList) -> {
            ManageDepartmentSaleDataDTO dto = new ManageDepartmentSaleDataDTO();
            dto.setMonthAmount1(dtoList.stream().map(ManageDepartmentSaleDataDTO::getMonthAmount1).reduce(BigDecimal.ZERO, BigDecimal::add));
            dto.setMonthAmount2(dtoList.stream().map(ManageDepartmentSaleDataDTO::getMonthAmount2).reduce(BigDecimal.ZERO, BigDecimal::add));
            dto.setMonthAmount3(dtoList.stream().map(ManageDepartmentSaleDataDTO::getMonthAmount3).reduce(BigDecimal.ZERO, BigDecimal::add));
            dto.setDepartmentSumTitle(dtoList.get(0).getDepartmentName() + " 合计");
            dto.setDepartmentSumFlag(true);
            result.add(dto);
            result.addAll(dtoList);
        });

        // 生成序号
        genNoByList(result, ManageDepartmentSaleDataDTO.class);

        return result;
    }

    private List<ManageDepartmentSaleAchieveDTO> convertAchieveAndTargetList(Map<String, Object> queryMap, String type) {
        BigDecimal percentage = new BigDecimal(100);
        // 月和年销售目标
        List<ManageDepartmentSaleAchieveDTO> targetStatistic = saleAmountTargetDao.getDepartmentSalesTargetStatistic(queryMap);
        // 获取月和年达成
        List<ManageDepartmentSaleAchieveDTO> achieveStatistic = saleDataDao.getDepartmentSalesAchieveStatistic(queryMap);

        Map<String, ManageDepartmentSaleAchieveDTO> achieveMap = new HashMap<>();
        Map<String, ManageDepartmentSaleAchieveDTO> targetMap = new HashMap<>();

        achieveStatistic.stream().forEach(achieve -> {
            String key = null;
            if("A".equals(type)) {
                key = achieve.getDepartmentId() + "_" + achieve.getParentBrandId() + "_" + achieve.getMonth();
            }
            if("B".equals(type)) {
                key = achieve.getDepartmentId() + "_" + achieve.getMonth();
            }
            achieveMap.put(key, achieve);
        });

        targetStatistic.stream().forEach(target -> {
            String key = null;
            if("A".equals(type)) {
                key = target.getDepartmentId() + "_" + target.getParentBrandId() + "_" + target.getMonth();
            }
            if("B".equals(type)) {
                key = target.getDepartmentId() + "_" + target.getMonth();
            }
            targetMap.put(key, target);
        });

        List<ManageDepartmentSaleAchieveDTO> remainAchieveList = new ArrayList<>();
        achieveMap.entrySet().stream().forEach(entity -> {
            String achieveKey = entity.getKey();
            if(!targetMap.containsKey(achieveKey)) {
                remainAchieveList.add(entity.getValue());
            }
        });
        remainAchieveList.stream().forEach(entity -> {
            entity.setMonthTargetAmount(BigDecimal.ZERO);
            entity.setYearTargetAmount(BigDecimal.ZERO);
        });

        List<ManageDepartmentSaleAchieveDTO> remainTargetList = new ArrayList<>();
        targetMap.entrySet().stream().forEach(entity -> {
            String targetKey = entity.getKey();
            if(!achieveMap.containsKey(targetKey)) {
                remainTargetList.add(entity.getValue());
            }
        });
        remainTargetList.stream().forEach(entity -> {
            entity.setMonthAchieveAmount(BigDecimal.ZERO);
            entity.setYearAchieveAmount(BigDecimal.ZERO);

            if(entity.getMonthTargetAmount() != null && entity.getMonthTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
                entity.setMonthCompletionPercentage((entity.getMonthAchieveAmount() == null ? BigDecimal.ZERO : entity.getMonthAchieveAmount()).divide(entity.getMonthTargetAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(percentage));
            }
            if(entity.getYearTargetAmount() != null && entity.getYearTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
                entity.setYearCompletionPercentage((entity.getYearAchieveAmount() == null ? BigDecimal.ZERO : entity.getYearAchieveAmount()).divide(entity.getYearTargetAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(percentage));
            }
        });

        // 合并目标和达成<事业部+母品牌+月份>
        List<ManageDepartmentSaleAchieveDTO> detailList = achieveStatistic.stream().flatMap(achieve -> targetStatistic.stream()
                .filter(target -> {
                    if("A".equals(type)) {
                        String key1 = achieve.getDepartmentId() + "_" + achieve.getParentBrandId() + "_" + achieve.getMonth();
                        String key2 = target.getDepartmentId() + "_" + target.getParentBrandId() + "_" + target.getMonth();
                        return StringUtils.equals(key1, key2);
                    }else if("B".equals(type)) {
                        String key1 = achieve.getDepartmentId() + "_" + achieve.getMonth();
                        String key2 = target.getDepartmentId() + "_" + target.getMonth();
                        return StringUtils.equals(key1, key2);
                    }else {
                        return false;
                    }
                }).map(target -> {
                    target.setMonthAchieveAmount(achieve.getMonthAchieveAmount());
                    target.setYearAchieveAmount(achieve.getYearAchieveAmount());
                    if(target.getMonthTargetAmount() != null && target.getMonthTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
                        target.setMonthCompletionPercentage((achieve.getMonthAchieveAmount() == null ? BigDecimal.ZERO : achieve.getMonthAchieveAmount()).divide(target.getMonthTargetAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(percentage));
                    }else {
                        target.setMonthCompletionPercentage(null);
                    }
                    if(target.getYearTargetAmount() != null && target.getYearTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
                        target.setYearCompletionPercentage((achieve.getYearAchieveAmount() == null ? BigDecimal.ZERO : achieve.getYearAchieveAmount()).divide(target.getYearTargetAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(percentage));
                    }else {
                        target.setYearCompletionPercentage(null);
                    }
                    return target;
                })
        ).collect(Collectors.toList());
        detailList.addAll(remainTargetList);
        detailList.addAll(remainAchieveList);
        return detailList;
    }

    @Override
    public List<ManageDepartmentSaleAchieveDTO> getDepartmentSalesAchieveStatistic(ManagmentReportForm form, User user) {
        if(form == null) {
            return null;
        }
        BigDecimal unitBigDecimal = new BigDecimal(10000);
        BigDecimal percentage = new BigDecimal(100);
        Map<String, Object> queryMap = object2Map(form);
        // 校验公司+事业部+部门
        checkBussinessAndBuByUser(user, form, queryMap);

        if(StringUtils.isNotBlank(form.getYear())) {
            return getDepartmentSalesAchieveStatisticByYear(form);
        }

        // 最终的结果集
        List<ManageDepartmentSaleAchieveDTO> result = new ArrayList<>();

        queryMap.put("groupByDepartmentFlag", true);
        queryMap.put("groupByParentBrandFlag", true);
        queryMap.put("groupByMonthFlag", false);

        // 1. 获取达成和目标的年以及月集合
        List<ManageDepartmentSaleAchieveDTO> detailList = convertAchieveAndTargetList(queryMap, "A");

        // 2. 第一条记录， 生成合计总金额DTO
        ManageDepartmentSaleAchieveDTO sumDTO = new ManageDepartmentSaleAchieveDTO();
        sumDTO.setMonthTargetAmount(BigDecimal.ZERO);
        sumDTO.setMonthAchieveAmount(BigDecimal.ZERO);
        sumDTO.setMonthCompletionPercentage(BigDecimal.ZERO);
        sumDTO.setYearTargetAmount(BigDecimal.ZERO);
        sumDTO.setYearAchieveAmount(BigDecimal.ZERO);
        sumDTO.setYearCompletionPercentage(BigDecimal.ZERO);
        detailList.stream().forEach(entity -> {
            if(entity.getMonthAchieveAmount() == null) {
                entity.setMonthAchieveAmount(BigDecimal.ZERO);
            }
            if(entity.getMonthTargetAmount() == null) {
                entity.setMonthTargetAmount(BigDecimal.ZERO);
            }
            if(entity.getYearAchieveAmount() == null) {
                entity.setYearAchieveAmount(BigDecimal.ZERO);
            }
            if(entity.getYearTargetAmount() == null) {
                entity.setYearTargetAmount(BigDecimal.ZERO);
            }
            entity.setMonthAchieveAmount(NumberUtil.convertNumber(entity.getMonthAchieveAmount(), unitBigDecimal, 2));
            entity.setMonthTargetAmount(NumberUtil.convertNumber(entity.getMonthTargetAmount(), unitBigDecimal, 2));
            entity.setYearAchieveAmount(NumberUtil.convertNumber(entity.getYearAchieveAmount(), unitBigDecimal, 2));
            entity.setYearTargetAmount(NumberUtil.convertNumber(entity.getYearTargetAmount(), unitBigDecimal, 2));

            sumDTO.setMonthAchieveAmount(sumDTO.getMonthAchieveAmount().add(entity.getMonthAchieveAmount()));
            sumDTO.setMonthTargetAmount(sumDTO.getMonthTargetAmount().add(entity.getMonthTargetAmount()));
            sumDTO.setYearAchieveAmount(sumDTO.getYearAchieveAmount().add(entity.getYearAchieveAmount()));
            sumDTO.setYearTargetAmount(sumDTO.getYearTargetAmount().add(entity.getYearTargetAmount()));
        });
        if(sumDTO.getMonthTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
            sumDTO.setMonthCompletionPercentage(null);
        }else {
            sumDTO.setMonthCompletionPercentage(sumDTO.getMonthAchieveAmount().divide(sumDTO.getMonthTargetAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(percentage));
        }
        if(sumDTO.getYearTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
            sumDTO.setYearCompletionPercentage(null);
        }else {
            sumDTO.setYearCompletionPercentage(sumDTO.getYearAchieveAmount().divide(sumDTO.getYearTargetAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(percentage));
        }

        sumDTO.setDepartmentSumFlag(true);
        sumDTO.setDepartmentSumTitle("合计");
        result.add(sumDTO);

        // 3.根据部门分组 key = departmentId
        Map<Long, List<ManageDepartmentSaleAchieveDTO>> collect = detailList.stream().collect(Collectors.groupingBy(ManageDepartmentSaleAchieveDTO::getDepartmentId));
        // 4.生成部门合计
        collect.forEach((departId, dtoList) -> {
            ManageDepartmentSaleAchieveDTO dto = new ManageDepartmentSaleAchieveDTO();
            dto.setMonthAchieveAmount(dtoList.stream().map(ManageDepartmentSaleAchieveDTO::getMonthAchieveAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            dto.setMonthTargetAmount(dtoList.stream().map(ManageDepartmentSaleAchieveDTO::getMonthTargetAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            if(dto.getMonthTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
                dto.setMonthCompletionPercentage(null);
            }else {
                dto.setMonthCompletionPercentage(dto.getMonthAchieveAmount().divide(dto.getMonthTargetAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(percentage));
            }
            dto.setYearAchieveAmount(dtoList.stream().map(ManageDepartmentSaleAchieveDTO::getYearAchieveAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            dto.setYearTargetAmount(dtoList.stream().map(ManageDepartmentSaleAchieveDTO::getYearTargetAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            if(dto.getYearTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
                dto.setYearCompletionPercentage(null);
            }else {
                dto.setYearCompletionPercentage(dto.getYearAchieveAmount().divide(dto.getYearTargetAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(percentage));
            }
            dto.setDepartmentSumFlag(true);
            dto.setDepartmentSumTitle(dtoList.get(0).getDepartmentName() + " 合计");

            result.add(dto);
            result.addAll(dtoList);
        });

        // 生成序号
        genNoByList(result, ManageDepartmentSaleAchieveDTO.class);

        // 组合数据，生成前端需要的数据结构
        result.stream().forEach(entity -> {
            SalesAchieveDTO dto = new SalesAchieveDTO();
            dto.setMonth(entity.getMonth());
            dto.setMonthAchieveAmount(entity.getMonthAchieveAmount());
            dto.setMonthTargetAmount(entity.getMonthTargetAmount());
            dto.setMonthCompletionPercentage(entity.getMonthCompletionPercentage());
            entity.setMonthAchieveList(Arrays.asList(dto));
        });

        return result;
    }

    private List<ManageDepartmentSaleAchieveDTO> getDepartmentSalesAchieveStatisticByYear(ManagmentReportForm form) {
        if(form == null) {
            return null;
        }

        BigDecimal unitBigDecimal = new BigDecimal(10000);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        int numOfMonth = 0;
        try {
            Date date = sdf.parse(form.getYear());
            c.setTime(date);
            numOfMonth = c.getMaximum(Calendar.MONTH) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<ManageDepartmentSaleAchieveDTO> result = new ArrayList<>();
        // 月和年销售目标
        Map<String, Object> queryMap = object2Map(form);
        queryMap.put("groupByDepartmentFlag", true);
        queryMap.put("groupByParentBrandFlag", true);
        queryMap.put("groupByMonthFlag", true);
        // 3.1 获取<部门+母品牌+月份> 目标+达成
        List<ManageDepartmentSaleAchieveDTO> detailList = convertAchieveAndTargetList(queryMap, "A");
        detailList.stream().forEach(entity -> {
            // 个->万
            entity.setYearAchieveAmount(NumberUtil.convertNumber(entity.getYearAchieveAmount(), unitBigDecimal, 2));
            entity.setYearTargetAmount(NumberUtil.convertNumber(entity.getYearTargetAmount(), unitBigDecimal, 2));
            entity.setMonthAchieveAmount(NumberUtil.convertNumber(entity.getMonthAchieveAmount(), unitBigDecimal, 2));
            entity.setMonthTargetAmount(NumberUtil.convertNumber(entity.getMonthTargetAmount(), unitBigDecimal, 2));
        });

        // 2.1 获取<部门+月份> 目标+达成
        queryMap.put("groupByParentBrandFlag", false);
        List<ManageDepartmentSaleAchieveDTO> sumDepartmentList = convertAchieveAndTargetList(queryMap, "B");
        sumDepartmentList.stream().forEach(entity -> {
            // 个->万
            entity.setYearAchieveAmount(NumberUtil.convertNumber(entity.getYearAchieveAmount(), unitBigDecimal, 2));
            entity.setYearTargetAmount(NumberUtil.convertNumber(entity.getYearTargetAmount(), unitBigDecimal, 2));
            entity.setMonthAchieveAmount(NumberUtil.convertNumber(entity.getMonthAchieveAmount(), unitBigDecimal, 2));
            entity.setMonthTargetAmount(NumberUtil.convertNumber(entity.getMonthTargetAmount(), unitBigDecimal, 2));
        });

        // 1.1 获取<月份> 目标+达成
        queryMap.put("groupByDepartmentFlag", false);
        List<ManageDepartmentSaleAchieveDTO> sumList = convertAchieveAndTargetList(queryMap, "A");

        // 1.2 生成总合计
        List<SalesAchieveDTO> salesAchieveDTOs = new ArrayList<>();
        for (int i = 0; i<numOfMonth; i++) {
            salesAchieveDTOs.add(new SalesAchieveDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        sumList.stream().forEach(entity -> {
            try {
                Date date = sdf2.parse(entity.getMonth());
                c.setTime(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            // 个->万
            entity.setYearAchieveAmount(NumberUtil.convertNumber(entity.getYearAchieveAmount(), unitBigDecimal, 2));
            entity.setYearTargetAmount(NumberUtil.convertNumber(entity.getYearTargetAmount(), unitBigDecimal, 2));
            entity.setMonthAchieveAmount(NumberUtil.convertNumber(entity.getMonthAchieveAmount(), unitBigDecimal, 2));
            entity.setMonthTargetAmount(NumberUtil.convertNumber(entity.getMonthTargetAmount(), unitBigDecimal, 2));

            SalesAchieveDTO salesAchieveDTO = salesAchieveDTOs.get(c.get(Calendar.MONTH));
            salesAchieveDTO.setMonthAchieveAmount(entity.getMonthAchieveAmount());
            salesAchieveDTO.setMonthTargetAmount(entity.getMonthTargetAmount());
            salesAchieveDTO.setMonthCompletionPercentage(entity.getMonthCompletionPercentage());
            salesAchieveDTO.setMonth(entity.getMonth());
        });
        ManageDepartmentSaleAchieveDTO sum = new ManageDepartmentSaleAchieveDTO();
        sum.setDepartmentSumFlag(true);
        sum.setDepartmentSumTitle("合计");
        sum.setMonthAchieveList(salesAchieveDTOs);
        result.add(sum);

        // 2.2 <部门> 合并月份数据
        Map<String, List<ManageDepartmentSaleAchieveDTO>> departmentCollect =
                sumDepartmentList.stream().collect(Collectors.groupingBy(v -> v.getDepartmentId().toString()));
//        Map<String, List<SalesAchieveDTO>> departmentChildMap = new HashMap<>();
        Map<String, ManageDepartmentSaleAchieveDTO> departmentMap = new HashMap<>();

        int finalNumOfMonth = numOfMonth;
        departmentCollect.entrySet().forEach(entity -> {
            List<SalesAchieveDTO> childList = new ArrayList<>();
            for (int i = 0; i< finalNumOfMonth; i++) {
                childList.add(new SalesAchieveDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            }
            List<ManageDepartmentSaleAchieveDTO> value = entity.getValue();
            value.forEach(e -> {
                try {
                    Date date = sdf2.parse(e.getMonth());
                    c.setTime(date);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                SalesAchieveDTO childDto = childList.get(c.get(Calendar.MONTH));
                BeanUtils.copyProperties(e, childDto);
            });
//            departmentChildMap.put(entity.getKey(), childList);

            ManageDepartmentSaleAchieveDTO manageDepartmentSaleAchieveDTO = entity.getValue().get(0);
            manageDepartmentSaleAchieveDTO.setDepartmentSumFlag(true);
            manageDepartmentSaleAchieveDTO.setDepartmentSumTitle(manageDepartmentSaleAchieveDTO.getDepartmentName() + "合计");
            manageDepartmentSaleAchieveDTO.setMonthAchieveList(childList);
            departmentMap.put(entity.getKey(), manageDepartmentSaleAchieveDTO);
        });

        // 3.1 <部门+母品牌> 合并月份
        Map<String, List<ManageDepartmentSaleAchieveDTO>> departmentAndBrandCollect =
                detailList.stream().collect(Collectors.groupingBy(v -> v.getDepartmentId() + "_" + v.getParentBrandId()));
        Map<String, List<SalesAchieveDTO>> departmentAndBrandChildMap = new HashMap<>();
        Map<String, ManageDepartmentSaleAchieveDTO> departmentAndBrandMap = new HashMap<>();

        departmentAndBrandCollect.entrySet().forEach(entity -> {
            List<SalesAchieveDTO> childList = new ArrayList<>();
            for (int i = 0; i< finalNumOfMonth; i++) {
                childList.add(new SalesAchieveDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            }

            List<ManageDepartmentSaleAchieveDTO> value = entity.getValue();
            value.forEach(e -> {
                try {
                    Date date = sdf2.parse(e.getMonth());
                    c.setTime(date);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                SalesAchieveDTO childDto = childList.get(c.get(Calendar.MONTH));
                BeanUtils.copyProperties(e, childDto);
            });
            departmentAndBrandChildMap.put(entity.getKey(), childList);

            ManageDepartmentSaleAchieveDTO manageDepartmentSaleAchieveDTO = entity.getValue().get(0);
            manageDepartmentSaleAchieveDTO.setMonthAchieveList(childList);
            departmentAndBrandMap.put(entity.getKey(), manageDepartmentSaleAchieveDTO);
        });

        // 把部门合计和具体数据按照前端的顺序进行放入
        List<ManageDepartmentSaleAchieveDTO> collect = departmentAndBrandMap.values().stream().collect(Collectors.toList());
        Map<Long, List<ManageDepartmentSaleAchieveDTO>> collect1 = collect.stream().collect(Collectors.groupingBy(ManageDepartmentSaleAchieveDTO::getDepartmentId));
        collect1.entrySet().forEach(entity -> {
            String key = entity.getKey().toString();
            if(departmentMap.containsKey(key)) {
                result.add(departmentMap.get(key));
                departmentMap.remove(key);
            }
            result.addAll(entity.getValue());
        });

        // 生成序号
        genNoByList(result, ManageDepartmentSaleAchieveDTO.class);

        return result;
    }

    /**
     * 给list中的数据生成序号
     * @param list
     * @param clazz
     */
    private void genNoByList(List list, Class clazz) {
        // 生成序号
        AtomicLong num = new AtomicLong();
        list.stream().forEach(entity -> {
            Boolean getDepartmentSumFlag = null;
            try {
                getDepartmentSumFlag = (Boolean) clazz.getMethod("getDepartmentSumFlag").invoke(entity);
                if(getDepartmentSumFlag == null || getDepartmentSumFlag == false) {
                    Method[] methods = clazz.getMethods();
                    clazz.getMethod("setNo", Long.class).invoke(entity, num.incrementAndGet());
                }
            } catch (Exception e) {
                throw new RuntimeException("生成序号失败", e);
            }
        });
    }

    @Override
    public List<MerchantInfoModel> getMerchantList(User user) throws Exception {
        List<MerchantInfoModel> merchantList = new ArrayList<MerchantInfoModel>();

        MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194100022");//宝信
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP31194300027");//广旺贸易
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
        merchantInfoModel.setCode("ERP23112900030");//轩盛
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP13111400051");//香港元森泰
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP26145800046");//易速达
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setCode("ERP26143500022");//卓烨贸易
        merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
        merchantList.add(merchantInfoModel);

        merchantList = merchantList.stream().filter(entity -> entity != null).collect(Collectors.toList());
        List<Long> merchantIdListByUser = userMerchantRelMongoDao.getMerchantListByUser(user.getId());
        merchantList = merchantList.stream().filter(e ->
                merchantIdListByUser.contains(e.getId())).collect(Collectors.toList());
        return merchantList;
    }

    @Override
    public List<BusinessUnitModel> getBuList(User user, List<Long> departmentIds) {
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

        Map<String, Object> map = new HashMap<>();
        List<String> buCodeList = new ArrayList<>();
        for (String reqBuCode : reqBuCodes) {
            if (buCodes.contains(reqBuCode)) {
                buCodeList.add(reqBuCode);
            }
        }
        map.put("codeList", buCodeList);
        if(departmentIds != null && !departmentIds.isEmpty()) {
            map.put("departmentIdList", departmentIds);
        }

        List<BusinessUnitModel> modelList = businessUnitDao.getListByMap(map).stream().collect(Collectors.toList());
        return modelList;
    }

    @Override
    public List<SelectBean> getDepartmentSelectList(ManagmentReportForm form, User user) {
        List<SelectBean> result = new ArrayList<SelectBean>();
        List<BusinessUnitModel> buList = getBuList(user, null);

        ArrayList<BusinessUnitModel> collect = buList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BusinessUnitModel::getDepartmentId))), ArrayList::new));

        Set<Long> departmentIdSet = collect.stream().map(entity -> {
            return entity.getDepartmentId();
        }).collect(Collectors.toSet());

        List<DepartmentInfoMongo> departmentInfoMongoList = departmentInfoMongoDao.findAllByIn("departmentInfoId", new ArrayList(departmentIdSet));
        List<DepartmentInfoMongo> collect1 = departmentInfoMongoList.stream().sorted(Comparator.comparing(DepartmentInfoMongo::getCode)).collect(Collectors.toList());

        collect1.stream().forEach(entity -> {
            SelectBean selectBean = new SelectBean();
            selectBean.setLabel(entity.getName());
            selectBean.setValue(entity.getDepartmentInfoId().toString());
            result.add(selectBean);
        });

        return result;
    }

    @Override
    public List<SelectBean> getCustomerSelectList(User user) {
        List<CustomerMerchantRelModel> merchantList = new ArrayList<>();
        List<CustomerMerchantRelModel> result = new ArrayList<>();
        List<SelectBean> resultList = new ArrayList<>();
        try {
            merchantList = getMerchantList(user).stream().map(entity -> {
                CustomerMerchantRelModel customerMerchantRelModel = new CustomerMerchantRelModel();
                customerMerchantRelModel.setMerchantId(entity.getId());
                return customerMerchantRelModel;
            }).collect(Collectors.toList());

            for (CustomerMerchantRelModel model : merchantList) {
                result.addAll(customerMerchantRelDao.list(model));
            }
        } catch (Exception e) {
            throw new RuntimeException("获取公司列表失败", e);
        }

        ArrayList<CustomerMerchantRelModel> collect = result.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CustomerMerchantRelModel::getCustomerId))), ArrayList::new));
        collect.stream().forEach(entity -> {
            SelectBean selectBean = new SelectBean();
            selectBean.setLabel(entity.getName());
            selectBean.setValue(entity.getCustomerId().toString());
            resultList.add(selectBean);
        });
        return resultList;
    }

    @Override
    public List<SelectBean> getDepotSelectList(User user) {
        Map<String, Object> map = new HashMap<>();
        List<SelectBean> resultList = new ArrayList<>();
        try {
            List<MerchantInfoModel> merchantList = getMerchantList(user);
            List<BusinessUnitModel> buList = getBuList(user, null);
            List<Long> merchantIds = merchantList.stream().map(entity -> {return entity.getId();}).collect(Collectors.toList());
            List<Long> buIds = buList.stream().map(entity -> {return entity.getId();}).collect(Collectors.toList());

            map.put("buIds", buIds);
            map.put("merchantIds", merchantIds);
        } catch (Exception e) {
            throw new RuntimeException("获取仓库失败", e);
        }

        List<Map<String, Object>> buMerchAndDepotBuList = merchantDepotBuRelDao.getListByMap(map);
        Set<Long> depotIdSet = buMerchAndDepotBuList.stream().map(entity -> {
            Long depotId = (Long) entity.get("depot_id");
            return depotId;
        }).collect(Collectors.toSet());
        map.clear();
        map.put("depotIds", depotIdSet);
        List<DepotInfoModel> collect = depotInfoDao.listByMap(map);

        collect.stream().forEach(entity -> {
            SelectBean selectBean = new SelectBean();
            selectBean.setLabel(entity.getName());
            selectBean.setValue(entity.getId().toString());
            resultList.add(selectBean);
        });
        return resultList;
    }

    @Override
    public List<ManageDepartmentInventoryDTO> getDepartmentInventoryStatistic(ManagmentReportForm form, User user) {
        if(form == null) {
            return null;
        }

        // 若没有传时间， 默认当前日期的前一天
        if(StringUtils.isBlank(form.getMonth())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            c.add(Calendar.DATE, -1);
            date = c.getTime();
            form.setMonth(sdf.format(date));
        }
        BigDecimal unitBigDecimal = new BigDecimal(10000);
        DecimalFormat decimalFormat = new DecimalFormat("0.00%");

        String month = form.getMonth();
        form.setMonth(month.substring(0, month.lastIndexOf("-")));

        BigDecimal percentage = new BigDecimal(100);
        Map<String, Object> paramMap = object2Map(form);

        // 校验公司+事业部+部门
        checkBussinessAndBuByUser(user, form, paramMap);

        List<ManageDepartmentInventoryDTO> inventoryStatistic = inventoryDao.getDepartmentInventoryStatistic(paramMap);
        if(inventoryStatistic == null || inventoryStatistic.isEmpty()) {
            return null;
        }

        // 最终要返回的结果集
        List<ManageDepartmentInventoryDTO> result = new ArrayList<>();

        // 生成总合计
        // 1. 第一条记录， 总金额DTO
        ManageDepartmentInventoryDTO sumDTO = new ManageDepartmentInventoryDTO();
        sumDTO.setNum(0L);
        sumDTO.setInventoryAmount(BigDecimal.ZERO);
        inventoryStatistic.stream().forEach(entity -> {
            entity.setInventoryAmount(NumberUtil.convertNumber(entity.getInventoryAmount(), unitBigDecimal, 2));
            sumDTO.setNum(sumDTO.getNum() + entity.getNum());
            sumDTO.setInventoryAmount(sumDTO.getInventoryAmount().add(entity.getInventoryAmount()));
            entity.setEffectiveIntervalLabel(DERP_REPORT.getLabelByKey(DERP_REPORT.fallingPrice_effectiveIntervalList, entity.getEffectiveInterval()));
        });
        sumDTO.setDepartmentSumFlag(true);
        sumDTO.setDepartmentSumTitle("合计");
        result.add(sumDTO);

        // 根据部门进行分组
        Map<Long, List<ManageDepartmentInventoryDTO>> collect = inventoryStatistic.stream().collect(
                Collectors.groupingBy(ManageDepartmentInventoryDTO::getDepartmentId)
        );

        // 生成部门合计和部门数据
        collect.forEach((departId, dtoList) -> {
            ManageDepartmentInventoryDTO dto = new ManageDepartmentInventoryDTO();
            dto.setNum(dtoList.stream().mapToLong(ManageDepartmentInventoryDTO::getNum).sum());
            dto.setInventoryAmount(dtoList.stream().map(ManageDepartmentInventoryDTO::getInventoryAmount).reduce(BigDecimal.ZERO, BigDecimal::add));if(sumDTO.getInventoryAmount().compareTo(BigDecimal.ZERO) == 0) {
                dto.setAmountRate(BigDecimal.ZERO);
            }else {
                dto.setAmountRate(dto.getInventoryAmount().divide(sumDTO.getInventoryAmount(), 4, BigDecimal.ROUND_HALF_UP));
            }
            dto.setAmountRateLabel(decimalFormat.format(dto.getAmountRate()));
            dto.setDepartmentSumTitle(dtoList.get(0).getDepartmentName() + " 合计");
            dto.setDepartmentSumFlag(true);
            result.add(dto);

            dtoList.stream().forEach(entity -> {
                if(dto.getInventoryAmount().compareTo(BigDecimal.ZERO) == 0) {
                    entity.setAmountRate(BigDecimal.ZERO);
                }else{
                    entity.setAmountRate(entity.getInventoryAmount().divide(dto.getInventoryAmount(), 4, BigDecimal.ROUND_HALF_UP));
                }
                entity.setAmountRateLabel(decimalFormat.format(entity.getAmountRate()));
            });
            result.addAll(dtoList);
        });

        // 生成序号
        genNoByList(result, ManageDepartmentInventoryDTO.class);

        return result;
    }

    @Override
    public Map<String, Object> exportDepartmentInventoryStatistic(ManagmentReportForm form, User user) {
        List<String> title = new ArrayList<>();
        List<String> keyList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        title.add("序号");
        keyList.add("No");

        title.add("部门");
        keyList.add("departmentName");
        if(form.getGroupByParentBrandFlag() != null && form.getGroupByParentBrandFlag()) {
            title.add("母品牌");
            keyList.add("parentBrandName");
        }
        if(form.getGroupByBrandParentFlag() != null && form.getGroupByBrandParentFlag()) {
            title.add("子品牌");
            keyList.add("brandParent");
        }
        if(form.getGroupByBuIdFlag() != null && form.getGroupByBuIdFlag()) {
            title.add("项目组");
            keyList.add("buName");
        }
        if(form.getGroupByMerchantFlag() != null && form.getGroupByMerchantFlag()) {
            title.add("公司");
            keyList.add("merchantName");
        }
        if(form.getGroupByDepotFlag() != null && form.getGroupByDepotFlag()) {
            title.add("仓库");
            keyList.add("depotName");
        }
        if(form.getGroupByEffectiveInterval() != null && form.getGroupByEffectiveInterval()) {
            title.add("效期区间");
            keyList.add("effectiveIntervalLabel");
        }

        title.add("在库库存数量（件)");
        keyList.add("num");

        title.add("在库库存金额（万)");
        keyList.add("inventoryAmount");

        title.add("金额占比");
        keyList.add("amountRateLabel");

        map.put("columns", title.toArray(new String[0]));
        map.put("keys", keyList.toArray(new String[0]));
        return map;
    }

    @Override
    public Map<String, Object> exportDepartmentInventoryCleanStatistic(ManagmentReportForm form, User user) {
        List<String> title = new ArrayList<>();
        List<String> keyList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        title.add("序号");
        keyList.add("No");

        title.add("部门");
        keyList.add("departmentName");
        if(form.getGroupByParentBrandFlag() != null && form.getGroupByParentBrandFlag()) {
            title.add("母品牌");
            keyList.add("parentBrandName");
        }
        if(form.getGroupByBrandParentFlag() != null && form.getGroupByBrandParentFlag()) {
            title.add("子品牌");
            keyList.add("brandParent");
        }
        if(form.getGroupByBuIdFlag() != null && form.getGroupByBuIdFlag()) {
            title.add("项目组");
            keyList.add("buName");
        }
        if(form.getGroupByMerchantFlag() != null && form.getGroupByMerchantFlag()) {
            title.add("公司");
            keyList.add("merchantName");
        }
        if(form.getGroupByDepotFlag() != null && form.getGroupByDepotFlag()) {
            title.add("仓库");
            keyList.add("depotName");
        }

        title.add("在库库存金额(万)");
        keyList.add("inventoryAmount");

        title.add("在库库存数量(件)");
        keyList.add("inventoryNum");

        title.add("90内日均销量");
        keyList.add("avgSaleNumOfNinety");

        title.add("库存清空天数");
        keyList.add("inventoryCleanDayLabel");

        map.put("columns", title.toArray(new String[0]));
        map.put("keys", keyList.toArray(new String[0]));
        return map;
    }

    @Override
    public List<ManageDepartmentInventoryCleanDTO> getDepartmentInventoryCleanStatistic(ManagmentReportForm form, User user) {
        if(form == null) {
            return null;
        }

        BigDecimal unitBigDecimal = new BigDecimal(10000);
        // 若没有传时间， 默认当前日期的前一天
        if(StringUtils.isBlank(form.getMonth())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            c.add(Calendar.DATE, -1);
            date = c.getTime();
            form.setMonth(sdf.format(date));
        }
        String month = form.getMonth();
        form.setMonth(month.substring(0, month.lastIndexOf("-")));

        Map<String, Object> paramMap = object2Map(form);

        // 校验公司+事业部+部门
        checkBussinessAndBuByUser(user, form, paramMap);
        List<ManageDepartmentInventoryCleanDTO> list = inventoryAnalysisDao.getDepartmentInventoryCleanStatistic(paramMap);
        if(list == null || list.isEmpty()) {
            return null;
        }

        // 最终要返回的结果集
        List<ManageDepartmentInventoryCleanDTO> result = new ArrayList<>();

        // 生成总合计
        // 1. 第一条记录， 总金额DTO
        ManageDepartmentInventoryCleanDTO sumDTO = new ManageDepartmentInventoryCleanDTO();
        sumDTO.setInventoryNum(0);
        sumDTO.setInventoryAmount(BigDecimal.ZERO);
        sumDTO.setAvgSaleNumOfNinety(0D);
        sumDTO.setInventoryCleanDay(0D);
        sumDTO.setSaleNum(0);
        list.stream().forEach(entity -> {
            entity.setInventoryAmount(NumberUtil.convertNumber(entity.getInventoryAmount(), unitBigDecimal, 2));
            sumDTO.setInventoryNum(sumDTO.getInventoryNum() + entity.getInventoryNum());
            sumDTO.setInventoryAmount(sumDTO.getInventoryAmount().add(entity.getInventoryAmount()));
            sumDTO.setSaleNum(sumDTO.getSaleNum() + entity.getSaleNum());
        });
        if(sumDTO.getSaleNum() == null || sumDTO.getSaleNum() == 0) {
            sumDTO.setAvgSaleNumOfNinety(0D);
        }else {
            sumDTO.setAvgSaleNumOfNinety((double)sumDTO.getSaleNum() / list.get(0).getSaleDays());
        }
        if(sumDTO.getAvgSaleNumOfNinety() == null || sumDTO.getAvgSaleNumOfNinety().compareTo(0D) == 0) {
            sumDTO.setInventoryCleanDay(null);
        }else {
            sumDTO.setInventoryCleanDay((double)sumDTO.getInventoryNum() / sumDTO.getAvgSaleNumOfNinety());
        }
        sumDTO.setDepartmentSumFlag(true);
        sumDTO.setDepartmentSumTitle("合计");
        result.add(sumDTO);

        // 根据部门进行分组
        Map<Long, List<ManageDepartmentInventoryCleanDTO>> collect = list.stream().collect(
                Collectors.groupingBy(ManageDepartmentInventoryCleanDTO::getDepartmentId)
        );

        // 生成部门合计和部门数据
        collect.forEach((departId, dtoList) -> {
            dtoList.stream().forEach(entity -> {
                if(entity.getSaleDays() == null || entity.getSaleDays().compareTo(0) == 0) {
                    entity.setAvgSaleNumOfNinety(null);
                }else {
                    entity.setAvgSaleNumOfNinety((double)entity.getSaleNum() / entity.getSaleDays());
                }
                if(entity.getAvgSaleNumOfNinety() == null || entity.getAvgSaleNumOfNinety().compareTo(0D) <= 0) {
                    entity.setInventoryCleanDay(null);
                }else {
                    entity.setInventoryCleanDay(entity.getInventoryNum() / entity.getAvgSaleNumOfNinety());
                }
            });

            ManageDepartmentInventoryCleanDTO dto = new ManageDepartmentInventoryCleanDTO();
            dto.setInventoryNum(dtoList.stream().mapToInt(ManageDepartmentInventoryCleanDTO::getInventoryNum).sum());
            dto.setInventoryAmount(dtoList.stream().map(ManageDepartmentInventoryCleanDTO::getInventoryAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            dto.setSaleNum(dtoList.stream().mapToInt(ManageDepartmentInventoryCleanDTO::getSaleNum).sum());
            if(dtoList != null && dtoList.size() > 0) {
                dto.setAvgSaleNumOfNinety(dtoList.stream().filter(e -> {return e.getAvgSaleNumOfNinety() != null;}).mapToDouble(ManageDepartmentInventoryCleanDTO::getAvgSaleNumOfNinety).sum());
//                dto.setInventoryCleanDay(dtoList.stream().filter(e -> {return e.getInventoryCleanDay() != null;}).mapToDouble(ManageDepartmentInventoryCleanDTO::getInventoryCleanDay).sum() / dtoList.size());
            }else {
                dto.setAvgSaleNumOfNinety(null);
//                dto.setInventoryCleanDay(null);
            }
            if(dto.getAvgSaleNumOfNinety() == null || dto.getAvgSaleNumOfNinety().compareTo(0D) <= 0) {
                dto.setInventoryCleanDay(null);
            }else {
                dto.setInventoryCleanDay(dto.getInventoryNum() / dto.getAvgSaleNumOfNinety());
            }
            dto.setDepartmentSumTitle(dtoList.get(0).getDepartmentName() + " 合计");
            dto.setDepartmentSumFlag(true);
            result.add(dto);
            result.addAll(dtoList);
        });

        // 生成序号
        genNoByList(result, ManageDepartmentInventoryCleanDTO.class);

        result.stream().forEach(entity -> {
            if(entity.getAvgSaleNumOfNinety() != null) {
                BigDecimal avgSaleNumOfNinety = new BigDecimal(entity.getAvgSaleNumOfNinety());
                entity.setAvgSaleNumOfNinety(avgSaleNumOfNinety.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }

            if(entity.getInventoryCleanDay() != null) {
                BigDecimal inventoryCleanDay = new BigDecimal(entity.getInventoryCleanDay());
                entity.setInventoryCleanDay(inventoryCleanDay.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                entity.setInventoryCleanDayLabel(entity.getInventoryCleanDay().toString());
            }else {
                entity.setInventoryCleanDayLabel("-");
            }
        });

        return result;
    }

    private Map<String, Object> object2Map(Object object){
        JSONObject jsonObject = (JSONObject) JSON.toJSON(object);
        Set<Map.Entry<String,Object>> entrySet = jsonObject.entrySet();
        Map<String, Object> map=new HashMap<String,Object>();
        for (Map.Entry<String, Object> entry : entrySet) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

}
