package com.topideal.service.main.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.base.OperateSysLogDao;
import com.topideal.dao.main.*;
import com.topideal.entity.dto.main.CommbarcodeDTO;
import com.topideal.entity.dto.main.FixedCostPriceDTO;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.vo.base.OperateSysLogModel;
import com.topideal.entity.vo.main.FixedCostPriceModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.service.main.FixedCostPriceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/16 21:06
 * @Description: 固定成本价盘
 */
@Service
public class FixedCostPriceServiceImpl implements FixedCostPriceService {

    private static final Logger LOG = Logger.getLogger(FixedCostPriceServiceImpl.class);

    @Autowired
    private FixedCostPriceDao fixedCostPriceDao;
    @Autowired
    private MerchantBuRelDao merchantBuRelDao;
    @Autowired
    private MerchandiseInfoDao merchandiseInfoDao;
    @Autowired
    private CommbarcodeDao commbarcodeDao;
    @Autowired
    private OperateSysLogDao operateSysLogDao;
    @Autowired
    private MerchantInfoDao merchantInfoDao;
    @Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;

    @Override
    public FixedCostPriceDTO listByPage(User user, FixedCostPriceDTO dto) throws Exception{
        return fixedCostPriceDao.listDTOByPage(dto);
    }

    @Override
    public void delCostPrice(String ids) throws Exception{
        FixedCostPriceDTO dto = new FixedCostPriceDTO();
        dto.setIds(ids);
        List<FixedCostPriceDTO> fixedCostPriceDTOS = fixedCostPriceDao.listByDTO(dto);

        fixedCostPriceDTOS.stream().forEach(entity -> {
            if(!StringUtils.equals(DERP_SYS.FIXED_COST_PRICE_STATUS_0, entity.getStatus())) {
                throw new DerpException("已审核数据不允许删除！");
            }
        });
        List<Long> idList = StrUtils.parseIds(ids);
        int delete = fixedCostPriceDao.delete(idList);
        if(delete != idList.size()) {
            throw new DerpException("删除失败！");
        }
    }

    @Override
    public void auditCostPrice(String ids, User user) throws Exception{
        FixedCostPriceDTO dto = new FixedCostPriceDTO();
        dto.setIds(ids);
        dto.setStatus(DERP_SYS.FIXED_COST_PRICE_STATUS_1);
        List<FixedCostPriceDTO> fixedCostPriceDTOS = fixedCostPriceDao.listByDTO(dto);

        fixedCostPriceDTOS.stream().forEach(entity -> {
            if(!StringUtils.equals(DERP_SYS.FIXED_COST_PRICE_STATUS_0, entity.getStatus())) {
                throw new DerpException("已审核数据不允许审核！");
            }

            FixedCostPriceModel model = new FixedCostPriceModel();
            model.setStatus(DERP_SYS.FIXED_COST_PRICE_STATUS_1);
            model.setAuditDate(TimeUtils.getNow());
            model.setAuditer(user.getId());
            model.setAuditName(user.getName());
            model.setId(entity.getId());
            try {
                int update = fixedCostPriceDao.modify(model);
            } catch (SQLException throwables) {
                throw new DerpException("更新审批状态失败！");
            }
        });

        Set<Long> idSet = fixedCostPriceDTOS.stream().map(FixedCostPriceDTO::getId).collect(Collectors.toSet());
        // 记录操作日志
        for (Long id : idSet) {
            OperateSysLogModel saveModel = new OperateSysLogModel() ;
            saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_8);
            saveModel.setCreateDate(TimeUtils.getNow());
            saveModel.setOperateDate(TimeUtils.getNow());
            saveModel.setOperateId(user.getId());
            saveModel.setOperater(user.getName());
            saveModel.setOperateRemark("");
            saveModel.setOperateResult("");
            saveModel.setOperateAction(DERP_SYS.getLabelByKey(DERP_SYS.fixedCostPrice_statusList, dto.getStatus()));
            saveModel.setRelId(id);
            operateSysLogDao.save(saveModel);
        }

    }

    @Override
    public int countByDTO(FixedCostPriceDTO dto) {
        if(dto == null) {
            return 0;
        }
        return fixedCostPriceDao.countByDTO(dto);
    }

    @Override
    public List<ExportExcelSheet> exportCostPrice(User user, FixedCostPriceDTO dto) {
        String sheetFirstName = "固定成本价";
        String[] firstColumns = {"公司主体", "事业部名称", "标准品牌", "条形码", "商品名称", "固定成本价", "币种", "生效年月"};

        String[] firstKeys = {"merchantName", "buName", "brandParentName", "barcode", "goodsName", "fixedCostLabel", "currency", "effectiveDate"};


        int pageSize = 5000; //每页5000
        int exportCount = fixedCostPriceDao.countByDTO(dto);

        List<FixedCostPriceDTO> list = new ArrayList<>();
        for (int i = 0 ; i < exportCount; ) {
            int pageSub = (i + pageSize) < exportCount ? (i + pageSize) : exportCount;
            dto.setBegin(i);
            dto.setPageSize(pageSize);
            list.addAll(fixedCostPriceDao.listForExport(dto));
            i = pageSub;
        }

        //生成表格
        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();

        ExportExcelSheet firstSheet = ExcelUtilXlsx.createSheet(sheetFirstName, firstColumns, firstKeys, list);
        sheets.add(firstSheet);
        list = null;
        return sheets;
    }

    @Override
    public Map importCostPrice(Map<Integer, List<List<Object>>> data, User user) throws Exception{
        int success = 0;
        int pass = 0;
        int failure = 0;
        List<ImportMessage> msgList = new ArrayList<ImportMessage>();
        Set<FixedCostPriceModel> insertList = new HashSet<>();

        // 检查某个移入事业部+条形码+生效年月是否重复
        Set<String> checkSet = new HashSet<>();
        Long merchantId = user.getMerchantId();
        for (int i = 0; i < 1; i++) {//表头
            List<List<Object>> objects = data.get(i);
            for (int j = 1; j < objects.size(); j++) {
                try{
                    ImportMessage msg = new ImportMessage();
                    Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
                    String buCode = map.get("事业部编码");
                    if(StringUtils.isBlank(buCode)){
                        msg.setRows(j+1);
                        msg.setMessage("事业部编码不能为空");
                        msgList.add(msg);
                        failure+=1;
                        continue;
                    }
                    // 获取该事业部信息
                    MerchantBuRelDTO merchantBuRelDTO = new MerchantBuRelDTO();
                    merchantBuRelDTO.setMerchantId(merchantId);
                    merchantBuRelDTO.setBuCode(buCode);
                    merchantBuRelDTO.setStatus(DERP_SYS.MERCHANT_BU_REL_STATUS_1);
                    MerchantBuRelDTO merchantBuRel = merchantBuRelDao.getMerchantBuRel(merchantBuRelDTO);
                    if(merchantBuRel == null){
                        msg.setRows(j+1);
                        msg.setMessage("事业部编码为："+buCode+",未查到该公司下事业部信息");
                        msgList.add(msg);
                        failure+=1;
                        continue;
                    }

                    String barcode = map.get("条形码");
                    if(StringUtils.isBlank(barcode)){
                        msg.setRows(j+1);
                        msg.setMessage("商品条形码不能为空");
                        msgList.add(msg);
                        failure+=1;
                        continue;
                    }
                    //获取商品信息
                    MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
                    merchandiseInfoModel.setMerchantId(merchantId);
                    merchandiseInfoModel.setBarcode(barcode);
                    merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
                    List<MerchandiseInfoModel> merchandiseInfoModels = merchandiseInfoDao.list(merchandiseInfoModel);
                    if(merchandiseInfoModels == null || merchandiseInfoModels.isEmpty()){
                        msg.setRows(j+1);
                        msg.setMessage("商品条形码:" + barcode+"，未查到当前公司主体下已启用的商品条码");
                        msgList.add(msg);
                        failure+=1;
                        continue;
                    }
                    MerchandiseInfoModel merchandiseInfo = merchandiseInfoModels.get(0);

                    String fixedCost = map.get("固定成本价");
                    if(fixedCost == null || "".equals(fixedCost)){
                        msg.setRows(j+1);
                        msg.setMessage("固定成本价不能为空");
                        msgList.add(msg);
                        failure+=1;
                        continue;
                    }

                    String currency = map.get("币种");
                    if(StringUtils.isBlank(currency)){
                        msg.setRows(j+1);
                        msg.setMessage("币种不能为空");
                        msgList.add(msg);
                        failure+=1;
                        continue;
                    }else{
//                        String currencyLabel = DERP_ORDER.getLabelByKey(DERP.currencyCodeList, currency);
                        MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(user.getMerchantId());
                        if(!StringUtils.equals(merchantInfoModel.getAccountCurrency(), currency)){
                            msg.setRows(j+1);
                            msg.setMessage("币种必须为公司档案管理维护的核算币种");
                            msgList.add(msg);
                            failure+=1;
                            continue;
                        }
                    }

                    String effectiveDate = map.get("生效年月");
                    if(StringUtils.isBlank(effectiveDate)){
                        msg.setRows(j+1);
                        msg.setMessage("生效年月不能为空");
                        msgList.add(msg);
                        failure+=1;
                        continue;
                    }else{
                        if(!TimeUtils.isValidDate(effectiveDate, TimeUtils.YYYY_MM)){
                            msg.setRows(j+1);
                            msg.setMessage("生效年月输入有误");
                            msgList.add(msg);
                            failure+=1;
                            continue;
                        }

                        FinanceCloseAccountsMongo financeCloseAccountsMongo = new FinanceCloseAccountsMongo();
                        financeCloseAccountsMongo.setBuId(merchantBuRel.getBuId());
                        financeCloseAccountsMongo.setMerchantId(merchantBuRel.getMerchantId());
                        String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(financeCloseAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
//                        if(StringUtils.isBlank(maxdate)) {
//                            msg.setRows(j+1);
//                            msg.setMessage("未找到关账记录");
//                            msgList.add(msg);
//                            failure+=1;
//                            continue;
//                        }

                        if(StringUtils.isNotBlank(maxdate) && effectiveDate.compareTo(maxdate) <= 0) {
                            msg.setRows(j+1);
                            msg.setMessage("生效月份不能小于等于关账月份");
                            msgList.add(msg);
                            failure+=1;
                            continue;
                        }
                    }

                    // 相同“事业部+条形码+生效年月”维度下仅能有一条记录，
                    // 且校验导入的数据与数据库中相同维度下仅能有一条数据；
                    String isKey = buCode + "_" + barcode + "_" + effectiveDate;
                    if(!checkSet.contains(isKey)){
                        checkSet.add(isKey);
                    }else{
                        msg.setRows(j+1);
                        msg.setMessage("事业部:"+buCode+",商品条形码:"+barcode+",生效日期:"+effectiveDate+"有多条数据,请合并后导入");
                        msgList.add(msg);
                        failure+=1;
                        continue;
                    }
                    // 校验以“事业部+条形码+生效年月”为维度查询配置表是否已存在配置
                    FixedCostPriceModel fixedCostPriceModel = new FixedCostPriceModel();
                    fixedCostPriceModel.setMerchantId(merchantId);
                    fixedCostPriceModel.setBuCode(buCode);
                    fixedCostPriceModel.setBarcode(barcode);
                    fixedCostPriceModel.setEffectiveDate(effectiveDate);
                    List<FixedCostPriceModel> fixedCostPriceModels = fixedCostPriceDao.list(fixedCostPriceModel);
                    if(fixedCostPriceModels != null && !fixedCostPriceModels.isEmpty()){
                        msg.setRows(j+1);
                        msg.setMessage("事业部编码:"+buCode+",商品条形码:"+barcode+",生效日期:"+effectiveDate + "已存在");
                        msgList.add(msg);
                        failure+=1;
                        continue;
                    }
                    // 注入数据
                    FixedCostPriceModel saveModel = new FixedCostPriceModel();
                    saveModel.setMerchantId(merchantId);
                    saveModel.setMerchantName(user.getMerchantName());
                    saveModel.setBuId(merchantBuRel.getBuId());
                    saveModel.setBuCode(merchantBuRel.getBuCode());
                    saveModel.setBuName(merchantBuRel.getBuName());
                    saveModel.setStatus(DERP_SYS.FIXED_COST_PRICE_STATUS_0);// 待审核
                    saveModel.setGoodsId(merchandiseInfo.getId());
                    saveModel.setGoodsName(merchandiseInfo.getName());
                    saveModel.setBarcode(barcode);
                    saveModel.setFixedCost(new BigDecimal(fixedCost).setScale(8, BigDecimal.ROUND_HALF_UP));
                    saveModel.setCurrency(currency);
                    saveModel.setEffectiveDate(effectiveDate);

                    // 查询标准品牌信息
                    CommbarcodeDTO commbarcodeDTO = new CommbarcodeDTO();
                    commbarcodeDTO.setCommbarcode(merchandiseInfo.getCommbarcode());
                    commbarcodeDTO = commbarcodeDao.searchByDTO(commbarcodeDTO);
                    if(commbarcodeDTO != null){
                        saveModel.setBrandParentId(commbarcodeDTO.getCommBrandParentId());
                        saveModel.setBrandParentName(commbarcodeDTO.getCommBrandParentName());
                    }

                    insertList.add(saveModel);
                }catch (Exception e) {
                    e.printStackTrace();
                    failure+=1;
                    continue;
                }
            }
        }

        // 没有失败信息，保存数据
        if(failure==0 && insertList.size() > 0){
            int num = fixedCostPriceDao.batchSave(new ArrayList<>(insertList));
            success = num;
        }

        List<OperateSysLogModel> logList = insertList.stream().map(entity -> {
            OperateSysLogModel saveModel = new OperateSysLogModel();
            saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_8);
            saveModel.setCreateDate(TimeUtils.getNow());
            saveModel.setOperateDate(TimeUtils.getNow());
            saveModel.setOperateId(user.getId());
            saveModel.setOperater(user.getName());
            saveModel.setOperateRemark("");
            saveModel.setOperateResult("");
            saveModel.setOperateAction("新增");
            saveModel.setRelId(entity.getId());
            return saveModel;
        }).collect(Collectors.toList());

        //批量插入日志
        if(logList != null && !logList.isEmpty()) {
            operateSysLogDao.batchSave(logList);
        }

        Map map =new HashMap();
        map.put("success",success);
        map.put("pass",pass);
        map.put("failure",failure);
        map.put("msgList",msgList);
        return map;
    }

    /**
     * 把两个数组组成一个map
     * @param keys   键数组
     * @param values 值数组
     * @return 键值对应的map
     */
    private Map<String, String> toMap(Object[] keys, Object[] values) {
        if (keys != null && values != null) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            for (int i = 0; i < keys.length; i++) {
                map.put((String) keys[i], values[i].toString());
            }
            return map;
        }
        return null;
    }
}
