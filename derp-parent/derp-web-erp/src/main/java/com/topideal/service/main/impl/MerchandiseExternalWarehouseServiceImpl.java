package com.topideal.service.main.impl;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.ActionTypeEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.OperateSysLogDao;
import com.topideal.dao.main.*;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.dto.main.MerchandiseExternalWarehouseDTO;
import com.topideal.entity.vo.base.OperateSysLogModel;
import com.topideal.entity.vo.main.*;
import com.topideal.service.main.MerchandiseExternalWarehouseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MerchandiseExternalWarehouseServiceImpl implements MerchandiseExternalWarehouseService {

    @Autowired
    private MerchandiseExternalWarehouseDao merchandiseExternalWarehouseDao;
    @Autowired
    private OperateSysLogDao operateSysLogDao;
    @Autowired
    private MerchantInfoDao merchantInfoDao;
    @Autowired
    private CustomsAreaConfigDao customsAreaConfigDao;
    @Autowired
    private  MerchandiseCatDao merchandiseCatDao;

    @Autowired
    private MerchandiseInfoDao merchandiseInfoDao;

    @Override
    public MerchandiseExternalWarehouseDTO getListByPage(MerchandiseExternalWarehouseDTO dto, User user) throws SQLException {
        return merchandiseExternalWarehouseDao.getListByPage(dto);
    }

    @Override
    public Map deleteById(User user, List<Long> ids) throws SQLException {
        Map map=new HashMap();
        try{
           merchandiseExternalWarehouseDao.delete(ids);
            //插入日志
            for(Long id:ids){
                OperateSysLogModel model=new OperateSysLogModel();
                model.setRelId(id);
                model.setOperateId(user.getId());
                model.setOperater(user.getName());
                model.setOperateAction("删除");
                model.setModule(DERP_SYS.OREARTE_SYS_LOG_1);
                model.setModifyDate(TimeUtils.getNow());
                model.setOperateDate(TimeUtils.getNow());
                operateSysLogDao.save(model);
            }
            map.put("code","00");
            map.put("message","删除成功!");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map importMerchandiseWarehouse(Map<Integer, List<List<Object>>> data, User user) throws Exception {
        List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
        // 通过商家id获取商家信息
        MerchantInfoModel merchant = merchantInfoDao.searchById(user.getMerchantId());
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }
        //判断是新增还是修改
        boolean flag=true;
        //成功的数量
        int success = 0;
        // 按照平台商品货号+平台备案关区校验列表导入是否重复
        Map<String, String> goodsNoCheckMap=new HashMap<>();
        // 商家货号所对应的商品
        Map<String, MerchandiseExternalWarehouseModel> merchandiseInfoMap=new HashMap<>();

        // 数据
        for (int i = 0; i < 1; i++) {
            List<List<Object>> objects = data.get(i);
            for (int j = 1; j < objects.size(); j++) {
                Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
                    String goodsNo=map.get("平台商品货号");
                    String customsArea=map.get("平台备案关区");
                    String barcode = map.get("商品条形码");
                    String brandName = map.get("商品名称");
                    String grossWeightStr=map.get("商品毛重");
                    String netWeightStr=map.get("商品净重");
                    String countyCode=map.get("原产国");
                    String countyArea=map.get("原产地区");
                    String hsCode=map.get("HS编码");
                    String spec=map.get("规格型号");
                    String shelfLifeDaysStr=map.get("保质天数");
                    String filingPriceStr=map.get("备案单价");
                    String unitCode = map.get("计量单位");
                    String customsGoodsRecordNo= map.get("海关商品备案号");
                    String component=map.get("商品成分");
                    String englishGoodsName=map.get("商品英文名称");
                    String productBrand=map.get("商品品牌");
                    String productTypeName=map.get("二级品类");
                    String productTypeName0=map.get("一级品类");
                    String skuCode=map.get("SKU编码");
                    String materialAccount=map.get("账册备案料号");
                    String saleGoodNames=map.get("售卖商品名称（中文）");
                    String factoryNo=map.get("工厂编码");
                    String jinerxiang=map.get("金二项号");
                    String declareFactor=map.get("申报要素");
                    String lengthStr=map.get("长");
                    String widthStr=map.get("宽");
                    String heightStr=map.get("高");
                    String volumeStr=map.get("体积");
                    String boxToUnitStr=map.get("箱规");
                    String torrToUnitStr=map.get("托规");
                    String packType=map.get("包装方式");
                    String manufacturer=map.get("生产企业名称");
                    String manufacturerAddr=map.get("生产厂家地址");
                    String describe=map.get("商品描述");
                    String unitNameOne=map.get("第一法定单位");
                    String unitNameTwo=map.get("第二法定单位");
                    String brandType=map.get("品牌类型");
                    String emsCode=map.get("EMG编码");
                    String derpGoodsNo = map.get("经分销货号");

                    //校验必填项
                    if(StringUtils.isBlank(goodsNo)){
                        getImportErrorMessage(resultList,j,"平台商品货号不能为空");
                        continue;
                    }
                    if(StringUtils.isBlank(barcode)){
                        getImportErrorMessage(resultList,j,"商品条形码不能为空");
                        continue;
                    }
                    if(StringUtils.isBlank(brandName)){
                        getImportErrorMessage(resultList,j,"商品名称不能为空");
                        continue;
                    }
                    if(StringUtils.isBlank(customsArea)){
                        getImportErrorMessage(resultList,j,"平台备案关区不能为空");
                        continue;
                    }

                    //校验平台商品货号+平台备案关区的唯一性
                    String checkStr=customsArea.trim()+"_"+goodsNo.trim();
                    if(goodsNoCheckMap.containsKey(checkStr)){
                        getImportErrorMessage(resultList,j,"列表中平台商品货号+平台备案关区不能重复,货号:"+goodsNo+",平台备案关区:"+customsArea);
                        continue;
                    }

                    //检验填写的“平台备案关区”是否为关区代码，若不是则提示：”平台备案关区不存在“
                    CustomsAreaConfigModel customsAreaConfigModel = new CustomsAreaConfigModel();
                    customsAreaConfigModel.setName(customsArea);
                    customsAreaConfigModel.setStatus(DERP_SYS.CUSTOMS_AREA_STATUS);
                    CustomsAreaConfigModel model = customsAreaConfigDao.searchByModel(customsAreaConfigModel);
                    if(model == null) {
                        getImportErrorMessage(resultList,j,"平台备案关区不存在");
                        continue;
                    }

                    goodsNoCheckMap.put(checkStr,checkStr);

                    Pattern pattern = Pattern.compile("[0-9]{1,}");
                    Matcher matcher = pattern.matcher((CharSequence)customsArea);
                    boolean result=matcher.matches();
                    if (result) {
                        getImportErrorMessage(resultList,j,"平台备案关区不能为数字，必须为文本");
                        continue;
                    }

                    String reg = "^[0-9]+(.[0-9]+)?$";

                    if (!StringUtils.isEmpty(lengthStr) && !lengthStr.matches(reg)) {
                        getImportErrorMessage(resultList,j,"商品长度应为数字");
                        continue;
                    }

                    if (!StringUtils.isEmpty(widthStr) && !widthStr.matches(reg)) {
                        getImportErrorMessage(resultList,j,"商品宽度应为数字");
                        continue;
                    }

                    if (!StringUtils.isEmpty(heightStr) && !heightStr.matches(reg)) {
                        getImportErrorMessage(resultList,j,"商品高度应为数字");
                        continue;
                    }

                    if(!StringUtils.isEmpty(grossWeightStr) && !grossWeightStr.matches(reg)) {
                        getImportErrorMessage(resultList,j,"商品毛重应为数字");
                        continue;
                    }

                    if(!StringUtils.isEmpty(netWeightStr) && !netWeightStr.matches(reg)) {
                        getImportErrorMessage(resultList,j,"商品净重应为数字");
                        continue;
                    }

                    if(!StringUtils.isEmpty(filingPriceStr) && !netWeightStr.matches(reg)) {
                        getImportErrorMessage(resultList,j,"备案单价应为数字");
                        continue;
                    }

                    if(!StringUtils.isEmpty(shelfLifeDaysStr) && !shelfLifeDaysStr.matches(reg)) {
                        getImportErrorMessage(resultList,j,"保质天数应为数字");
                        continue;
                    }

                    MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
                    if(StringUtils.isNotBlank(derpGoodsNo)) {
                        //如果有经分销货号校验该货号是否在公司主体下存在
                        merchandiseInfoModel.setMerchantId(user.getMerchantId());
                        merchandiseInfoModel.setGoodsNo(derpGoodsNo);
                        merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_0);
                        List<MerchandiseInfoModel> listByModel = merchandiseInfoDao.getListByModel(merchandiseInfoModel);
                        if(listByModel == null || listByModel.isEmpty()) {
                            getImportErrorMessage(resultList,j,"在" + user.getMerchantName() +"主体下未找到" + derpGoodsNo + "货号");
                            continue;
                        }
                        merchandiseInfoModel = listByModel.get(0);
                    }

                    //转换类型
                    Double grossWeight=null;
                    if (StringUtils.isNotBlank(grossWeightStr))grossWeight=Double.valueOf(grossWeightStr);
                    Double netWeight=null;
                    if (StringUtils.isNotBlank(netWeightStr))netWeight=Double.valueOf(netWeightStr);
                    Integer shelfLifeDays=null;
                    if (StringUtils.isNotBlank(shelfLifeDaysStr))shelfLifeDays=Integer.valueOf(shelfLifeDaysStr);
                    BigDecimal filingPrice=null;
                    if (StringUtils.isNotBlank(filingPriceStr))filingPrice=new BigDecimal(filingPriceStr);
                    Double length=null;
                    if (StringUtils.isNotBlank(lengthStr))length=Double.valueOf(lengthStr);
                    Double width=null;
                    if (StringUtils.isNotBlank(widthStr))width=Double.valueOf(widthStr);
                    Double height=null;
                    if (StringUtils.isNotBlank(heightStr))height=Double.valueOf(heightStr);


                    // 保存
                    MerchandiseExternalWarehouseModel wareHouseModel=new MerchandiseExternalWarehouseModel();
                    wareHouseModel.setDerpGoodsNo(derpGoodsNo);
                    wareHouseModel.setDerpMerchandisId(merchandiseInfoModel.getId());
                    wareHouseModel.setMerchantId(user.getMerchantId());
                    wareHouseModel.setMerchantName(merchant.getName());
                    wareHouseModel.setGoodsNo(goodsNo);
                    wareHouseModel.setGoodsName(brandName);
                    wareHouseModel.setBarcode(barcode);
                    wareHouseModel.setEnglishGoodsName(englishGoodsName);
                    wareHouseModel.setCustomsAreaName(customsArea);
                    wareHouseModel.setHsCode(hsCode);
                    wareHouseModel.setFactoryNo(factoryNo);
                    wareHouseModel.setCountyArea(countyArea);
                    wareHouseModel.setBrandName(productBrand);
                    wareHouseModel.setProductTypeName(productTypeName);// 二级类目
                    wareHouseModel.setProductTypeName0(productTypeName0);// 一级类目
                    wareHouseModel.setGrossWeight(grossWeight);
                    wareHouseModel.setNetWeight(netWeight);
                    wareHouseModel.setSpec(spec);
                    wareHouseModel.setShelfLifeDays(shelfLifeDays);
                    wareHouseModel.setFilingPrice(filingPrice);
                    wareHouseModel.setManufacturer(manufacturer);
                    wareHouseModel.setManufacturerAddr(manufacturerAddr);
                    wareHouseModel.setCustomsGoodsRecordNo(customsGoodsRecordNo);
                    wareHouseModel.setJinerxiang(jinerxiang);
                    wareHouseModel.setUnitNameOne(unitNameOne);
                    wareHouseModel.setUnitNameTwo(unitNameTwo);
                    wareHouseModel.setSkuCode(skuCode);
                    wareHouseModel.setBrandType(brandType);
                    wareHouseModel.setMaterialAccount(materialAccount);
                    wareHouseModel.setSaleGoodNames(saleGoodNames);
                    wareHouseModel.setEmsCode(emsCode);
                    wareHouseModel.setDescribe(describe);
                    wareHouseModel.setComponent(component);
                    wareHouseModel.setBoxToUnit(boxToUnitStr);
                    wareHouseModel.setTorrToUnit(torrToUnitStr);
                    wareHouseModel.setPackType(packType);
                    wareHouseModel.setDeclareFactor(declareFactor);
                    wareHouseModel.setLength(length);
                    wareHouseModel.setWidth(width);
                    wareHouseModel.setHeight(height);
                    wareHouseModel.setVolume(volumeStr);
                    wareHouseModel.setSource(DERP_SYS.MERCHANDISEWAREHOUSE_SOURCE_0);
                    wareHouseModel.setUnit(unitCode);
                    wareHouseModel.setCountyName(countyCode);
                    wareHouseModel.setEmsCode(emsCode);

                    if(StringUtils.isNotBlank(customsArea)){
                        CustomsAreaConfigModel customsAreaConfig=new CustomsAreaConfigModel();
                        customsAreaConfig.setName(customsArea);
                        customsAreaConfig = customsAreaConfigDao.searchByModel(customsAreaConfig);
                        if(customsAreaConfig!=null){
                            wareHouseModel.setCustomsAreaId(customsAreaConfig.getId());
                        }
                    }

                    merchandiseInfoMap.put(checkStr, wareHouseModel);
            }
        }

        List<Long> idsList=new ArrayList<>();
        //没有失败错误则新增或修改
        if (resultList.size()==0&&merchandiseInfoMap.size()>0) {
            Set<String> keySet = merchandiseInfoMap.keySet();
            for (String merchantGoodsKey : keySet) {
                //判断是新增还是修改
                MerchandiseExternalWarehouseModel model=merchandiseInfoMap.get(merchantGoodsKey);
                //根据商家+平台备案关区+商品货号查询是否存在，存在则修改，否则新增
                MerchandiseExternalWarehouseModel searchModel=new MerchandiseExternalWarehouseModel();
                searchModel.setGoodsNo(model.getGoodsNo());
                searchModel.setMerchantId(user.getMerchantId());
                searchModel.setCustomsAreaName(model.getCustomsAreaName());
                MerchandiseExternalWarehouseModel searchWareModel=merchandiseExternalWarehouseDao.searchByModel(searchModel);
                if(searchWareModel!=null){
                   model.setId(searchWareModel.getId());
                }
                // 获取二级分类
                MerchandiseCatModel merchandiseCat2 = null ;
                if(StringUtils.isNotBlank(model.getProductTypeName())){
                    merchandiseCat2= new MerchandiseCatModel();
                    merchandiseCat2.setName(model.getProductTypeName());
                    merchandiseCat2 = merchandiseCatDao.searchByModel(merchandiseCat2);
                    if(merchandiseCat2!=null){
                        model.setProductTypeId(merchandiseCat2.getId());
                    }
                }
                // 通过二级分类找一级分类
                MerchandiseCatModel merchandiseCat1= new MerchandiseCatModel();
                if (merchandiseCat2!=null&&merchandiseCat2.getParentId()!=null) {
                    merchandiseCat1.setId(merchandiseCat2.getParentId());// 二级分类的父级id
                    merchandiseCat1 = merchandiseCatDao.searchByModel(merchandiseCat1);
                    if(merchandiseCat1!=null){
                        model.setProductTypeId(merchandiseCat1.getId());
                    }
                }

                Long id=0L;
                if(model.getId()==null){
                    model.setCreateDate(TimeUtils.getNow());
                    model.setCreater(user.getId());
                    model.setCreateName(user.getName());
                    id=merchandiseExternalWarehouseDao.save(model);
                }else{
                    model.setModifier(user.getId());
                    model.setModifiyName(user.getName());
                    model.setModifyDate(TimeUtils.getNow());
                    merchandiseExternalWarehouseDao.modify(model);
                    id=model.getId();
                }
                success=success+1;
                idsList.add(id);
            }
            //插入日志
            for(Long id:idsList){
                OperateSysLogModel model=new OperateSysLogModel();
                model.setRelId(id);
                model.setOperateId(user.getId());
                model.setOperater(user.getName());
                model.setOperateAction("导入");
                model.setModule(DERP_SYS.OREARTE_SYS_LOG_1);
                model.setModifyDate(TimeUtils.getNow());
                model.setOperateDate(TimeUtils.getNow());
                operateSysLogDao.save(model);
            }
        }
        Map map = new HashMap();
        map.put("success", success);
        map.put("pass", 0);
        map.put("failure", resultList.size());
        map.put("message", resultList);
        return map;
    }

    /**
     * 判断是否为数字或小数
     * @param str
     * @return
     */
    private static boolean isNumeric(String str){
        String pattern = "^[\\-]?[\\d]+(\\.[\\d]+)?$";
        Pattern p = Pattern.compile(pattern);
        Matcher isNum = p.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    @Override
    public MerchandiseExternalWarehouseDTO getDetailById(Long id) {
        return merchandiseExternalWarehouseDao.getDetailById(id);
    }

    @Override
    public List<MerchandiseExternalWarehouseDTO> exportMerchandiseExternalWarehouse(MerchandiseExternalWarehouseDTO dto,User user) throws SQLException {
        dto.setMerchantId(user.getMerchantId());
        dto.setPageSize(5000);
        dto.setBegin(0);
        List<MerchandiseExternalWarehouseDTO> exportList=new ArrayList<>();
        while(true){
            List<MerchandiseExternalWarehouseDTO>  exportListTemp = merchandiseExternalWarehouseDao.exportList(dto);
            dto.setBegin(dto.getBegin()+dto.getPageSize());
            if (exportListTemp==null||exportListTemp.size()==0)break;
            exportList.addAll(exportListTemp);
        }
        return exportList;
    }

    @Override
    public ResponseBean saveOrUpdateByDTO(MerchandiseExternalWarehouseDTO dto, ActionTypeEnum actionType) throws Exception {
        MerchandiseExternalWarehouseModel model = new MerchandiseExternalWarehouseModel();
        BeanUtils.copyProperties(dto, model);
        if(actionType == ActionTypeEnum.UPDATE) {
            //修改
            if(StringUtils.isBlank(model.getCustomsAreaName())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10018.getCode(),"平台备案关区必填");
            }
            if(StringUtils.isBlank(model.getGoodsNo())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10018.getCode(), "平台商品货号必填");
            }
            if(StringUtils.isBlank(model.getBarcode())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10018.getCode(), "商品条形码必填");
            }
            if(StringUtils.isBlank(model.getGoodsName())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10018.getCode(), "商品名称必填");
            }

            if(dto.getProductTypeId0() != null) {
                // 获取一级类目名称
                MerchandiseCatModel merchandisCatModel1 = merchandiseCatDao.searchById(dto.getProductTypeId0());

                if(merchandisCatModel1 != null && dto.getProductTypeId() != null) {
                    // 获取二级类目名称
                    MerchandiseCatModel merchandisCatModel2  = merchandiseCatDao.searchById(dto.getProductTypeId());
                    model.setProductTypeName0(merchandisCatModel1.getName());
                    model.setProductTypeName(merchandisCatModel2.getName());
                }
            }

            MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
            if(StringUtils.isNotBlank(model.getDerpGoodsNo())) {
                //如果有经分销货号校验该货号是否在公司主体下存在
                merchandiseInfoModel.setMerchantId(model.getMerchantId());
                merchandiseInfoModel.setGoodsNo(model.getDerpGoodsNo());
                List<MerchandiseInfoModel> listByModel = merchandiseInfoDao.getListByModel(merchandiseInfoModel);
                if(listByModel == null || listByModel.isEmpty()) {
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10018.getCode(), "该经分销货号不在公司主体下");
                }
                merchandiseInfoModel = listByModel.get(0);
            }

            model.setDerpMerchandisId(merchandiseInfoModel.getId());
            int modifyNum = merchandiseExternalWarehouseDao.modify(model);
            if(modifyNum != 1) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "修改失败");
            }
        }else {
            //暂时不支持新增
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "方法暂不支持新增");
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    private Map<String, String> toMap(Object[] keys, Object[] values) {
        if (keys != null && values != null && keys.length == values.length) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            for (int i = 0; i < keys.length; i++) {
                map.put((String) keys[i], values[i].toString());
            }
            return map;
        }
        return null;
    }

    /**
     * 获取异常信息
     * @param resultList
     */
    private void getImportErrorMessage(List<ImportErrorMessage> resultList, int j, String errorMessage) {
        ImportErrorMessage message = new ImportErrorMessage();
        message.setRows(j + 1);
        message.setMessage(errorMessage);
        resultList.add(message);
    }
}
