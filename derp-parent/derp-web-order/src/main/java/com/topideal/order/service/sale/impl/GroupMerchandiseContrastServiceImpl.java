package com.topideal.order.service.sale.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.GroupMerchandiseContrastDao;
import com.topideal.dao.sale.GroupMerchandiseContrastDetailDao;
import com.topideal.dao.sale.GroupMerchandiseContrastDetailHistoryDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.GroupMerchandiseContrastDTO;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastDetailHistoryModel;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastDetailModel;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.sale.GroupMerchandiseContrastService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * @Description: 组合商品对照表 serviceimpl
 * @Author: Chen Yiluan
 * @Date: 2019/09/16 17:01
 **/
@Service
public class GroupMerchandiseContrastServiceImpl implements GroupMerchandiseContrastService {

    /* 打印日志 */
    protected Logger LOGGER = LoggerFactory.getLogger(GroupMerchandiseContrastServiceImpl.class);

    @Autowired
    private GroupMerchandiseContrastDao groupMerchandiseContrastDao;
    @Autowired
    private GroupMerchandiseContrastDetailDao groupMerchandiseContrastDetailDao;
    // 商品
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private MerchantShopRelMongoDao merchantShopRelMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private BrandMongoDao brandMongoDao;
    @Autowired
    private GroupMerchandiseContrastDetailHistoryDao groupMerchandiseContrastDetailHistoryDao;


    @Override
    public GroupMerchandiseContrastDTO listGroupMerchandiseContrast(GroupMerchandiseContrastDTO dto) throws SQLException {
        
    	if(StringUtils.isNotBlank(dto.getUpdateStartDate())) {
    		dto.setUpdateStartDate(dto.getUpdateStartDate() + " 00:00:00");
    	}
    	
    	if(StringUtils.isNotBlank(dto.getUpdateEndDate())) {
    		dto.setUpdateEndDate(dto.getUpdateEndDate() + " 23:59:59");
    	}
    	
    	return groupMerchandiseContrastDao.searchDTOByPage(dto);
    }

    @Override
    public void saveGroupMerchandiseContrast(GroupMerchandiseContrastModel model, User user) throws SQLException {
        try {
            Map<String, Object> shopParam = new HashMap<String, Object>();
            shopParam.put("shopId", model.getShopId());
            MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParam);
            if (shop == null) {
                throw new RuntimeException("店铺不存在！");
            }
            model.setShopCode(shop.getShopCode());

            Map<String, Object> merchantParam = new HashMap<String, Object>();
            merchantParam.put("merchantId", model.getMerchantId());
            MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantParam);
            if (merchant == null) {
                throw new RuntimeException("商家不存在！");
            }
            model.setMerchantName(merchant.getName());
            model.setCreateDate(TimeUtils.getNow());
            Long id = groupMerchandiseContrastDao.save(model);
            if (id != null) {
                // 新增表体
                for (GroupMerchandiseContrastDetailModel item : model.getDetailList()) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("merchandiseId", item.getGoodsId());
                    MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);
                    if (mogo == null) {
                        throw new RuntimeException("商品货号：" + item.getGoodsNo() + "商品不存在");
                    }
                    item.setGroupMerchandiseId(id);
                    item.setCreateDate(TimeUtils.getNow());
                    groupMerchandiseContrastDetailDao.save(item);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("保存失败！" + e.getMessage());
        }
    }

    @Override
    public GroupMerchandiseContrastModel getDetail(Long id) throws SQLException {
        GroupMerchandiseContrastModel model = new GroupMerchandiseContrastModel();
        model.setId(id);
        return groupMerchandiseContrastDao.getDetails(model);
    }

    @Override
    public void modifyGroupMerchandiseContrast(GroupMerchandiseContrastModel model, User user) throws SQLException {

        if (model.getId() == null) {
            throw new RuntimeException("该组合商品不存在！");
        }

        Map<String, Object> shopParam = new HashMap<String, Object>();
        shopParam.put("shopId", model.getShopId());
        MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParam);
        if (shop == null) {
            throw new RuntimeException("店铺不存在！");
        }
        model.setShopCode(shop.getShopCode());

        Map<String, Object> merchantParam = new HashMap<String, Object>();
        merchantParam.put("merchantId", model.getMerchantId());
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantParam);
        if (merchant == null) {
            throw new RuntimeException("商家不存在！");
        }

        GroupMerchandiseContrastModel preModel = groupMerchandiseContrastDao.searchById(model.getId());
        if (!preModel.getSkuId().equals(model.getSkuId())) {
            GroupMerchandiseContrastModel contrastModel = new GroupMerchandiseContrastModel();
            contrastModel.setShopCode(model.getShopCode());
            contrastModel.setSkuId(model.getSkuId());
            GroupMerchandiseContrastModel exist = groupMerchandiseContrastDao.isExist(contrastModel);
            if (exist != null) {
                throw new RuntimeException("存在重复的商家店铺组合品ID信息!");
            }
        }

        model.setMerchantName(merchant.getName());
        model.setModifyDate(TimeUtils.getNow());
        try {
            int num = groupMerchandiseContrastDao.modify(model);
            if (num > 0) {

                //对比修改的表体是否有修改，修改/删除商品时记录到更改记录
                GroupMerchandiseContrastDetailModel detailModel = new GroupMerchandiseContrastDetailModel();
                detailModel.setGroupMerchandiseId(model.getId());
                List<GroupMerchandiseContrastDetailModel> existDetails = groupMerchandiseContrastDetailDao.list(detailModel);
                Map<Long, GroupMerchandiseContrastDetailModel> detailModelMap = new HashMap<>();
                boolean flag = false;
                for (GroupMerchandiseContrastDetailModel item : existDetails){
                    detailModelMap.put(item.getGoodsId(), item);
                }

                if (model.getDetailList().size() != existDetails.size()) {
                    flag = true;
                }

                if (!flag) {
                    for (GroupMerchandiseContrastDetailModel item : model.getDetailList()) {
                        if (detailModelMap.containsKey(item.getGoodsId())) {
                            if (item.getPrice().compareTo(detailModelMap.get(item.getGoodsId()).getPrice())!= 0
                                    || !item.getNum().equals(detailModelMap.get(item.getGoodsId()).getNum())) {
                                flag = true;
                                break;
                            }
                        } else {
                            flag = true;
                            break;
                        }
                    }
                }

                if (flag) {
                    for (GroupMerchandiseContrastDetailModel exist : existDetails) {
                        GroupMerchandiseContrastDetailHistoryModel historyModel = new GroupMerchandiseContrastDetailHistoryModel();
                        BeanUtils.copyProperties(exist, historyModel, new String[] { "id", "createDate", "modifyDate" });
                        historyModel.setGroupMerchandiseId(model.getId());
                        historyModel.setEditor(user.getName());
                        historyModel.setModifyId(user.getId());
                        groupMerchandiseContrastDetailHistoryDao.save(historyModel);
                    }
                }

                //删除表体
                groupMerchandiseContrastDetailDao.batchDelByGroupId(model.getId());

                // 新增表体
                for (GroupMerchandiseContrastDetailModel item : model.getDetailList()) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("merchandiseId", item.getGoodsId());
                    MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);
                    if (mogo == null) {
                        throw new RuntimeException("商品货号：" + item.getGoodsNo() + "商品不存在");
                    }
                    item.setGroupMerchandiseId(model.getId());
                    item.setCreateDate(TimeUtils.getNow());
                    groupMerchandiseContrastDetailDao.save(item);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("保存失败！" + e.getMessage());
        }
    }

    @Override
    public GroupMerchandiseContrastModel getGroupMerchandiseContrast(GroupMerchandiseContrastModel model) throws SQLException {
        return groupMerchandiseContrastDao.isExist(model);
    }

    @Override
    public List<GroupMerchandiseContrastDTO> getexportList(GroupMerchandiseContrastDTO dto) throws SQLException {
        return groupMerchandiseContrastDao.getExportList(dto);
    }

    @Override
    public Map batchImport(Map<Integer, List<List<Object>>> data) throws SQLException {

        Map<String, List<GroupMerchandiseContrastModel>> dataMap = new HashMap<String, List<GroupMerchandiseContrastModel>>();
        List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
        int success = 0;
        int pass = 0;
        int failure = 0;

        List<List<Object>> objects = data.get(0); //表头：组合品ID、组合品名称、店铺编码
        List<List<Object>> goodsList = data.get(1); //表体：组合品ID、单品商品货号、数量、销售标准价格

        //判断表头的组合品ID是否能在商品信息中找到对应的信息
        Map<String, GroupMerchandiseContrastModel> skuIdMap = new HashMap<>();

        //1. 表头必填项校验: 组合品ID、组合品名称、店铺编码
        for (int i = 1; i < objects.size(); i++) {
            Map<String, String> map = this.toMap(objects.get(0).toArray(), objects.get(i).toArray());
            try {
                String skuId = map.get("组合品ID");
                if (StringUtils.isEmpty(skuId)) {
                    ImportErrorMessage message = new ImportErrorMessage();
                    message.setRows(i + 1);
                    message.setMessage("组合品ID不能为空");
                    resultList.add(message);
                    failure += 1;
                    continue;
                }

                String groupGoodsName = map.get("组合品名称");
                if (StringUtils.isEmpty(groupGoodsName)) {
                    ImportErrorMessage message = new ImportErrorMessage();
                    message.setRows(i + 1);
                    message.setMessage("组合品名称不能为空");
                    resultList.add(message);
                    failure += 1;
                    continue;
                }

                String shopCode = map.get("店铺编码");
                if (StringUtils.isEmpty(shopCode)) {
                    ImportErrorMessage message = new ImportErrorMessage();
                    message.setRows(i + 1);
                    message.setMessage("店铺编码不能为空");
                    resultList.add(message);
                    failure += 1;
                    continue;
                }
                if (skuIdMap.containsKey(skuId)) {
                    ImportErrorMessage message = new ImportErrorMessage();
                    message.setRows(i + 1);
                    message.setMessage("组合品ID不能重复");
                    resultList.add(message);
                    failure += 1;
                    continue;
                }

                //查询店铺是否存在
                Map<String, Object> shopParam = new HashMap<>();
                shopParam.put("shopCode", shopCode);
                MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParam);
                if (shop == null) {
                    ImportErrorMessage message = new ImportErrorMessage();
                    message.setRows(i + 1);
                    message.setMessage("店铺编码:" + shopCode + "店铺不存在");
                    resultList.add(message);
                    failure += 1;
                    continue;
                }

                //skuId在系统中是否存在
                GroupMerchandiseContrastModel contrastModel = new GroupMerchandiseContrastModel();
                contrastModel.setShopCode(shopCode);
                contrastModel.setSkuId(skuId);
                GroupMerchandiseContrastModel exist = groupMerchandiseContrastDao.isExist(contrastModel);
                if (exist != null) {
                    ImportErrorMessage message = new ImportErrorMessage();
                    message.setRows(i + 1);
                    message.setMessage("组合品ID在系统中已经存在");
                    resultList.add(message);
                    failure += 1;
                    continue;
                }
                String remark = map.get("备注");
                GroupMerchandiseContrastModel model = new GroupMerchandiseContrastModel();
                model.setSkuId(skuId);
                model.setMerchantName(shop.getMerchantName());
                model.setMerchantId(shop.getMerchantId());
                model.setGroupGoodsName(groupGoodsName);
                model.setShopCode(shopCode);
                model.setShopId(shop.getShopId());
                model.setShopName(shop.getShopName());
                model.setCreateDate(TimeUtils.getNow());
                model.setRemark(remark);
                model.setStatus(DERP_ORDER.GROUPMERCHANDISECONTRAST_STATUS_1);
                skuIdMap.put(skuId, model);
            } catch (Exception e) {
                e.printStackTrace();
                ImportErrorMessage message = new ImportErrorMessage();
                message.setRows(i + 1);
                message.setMessage(e.getMessage());
                resultList.add(message);
                failure += 1;
                continue;
            }
        }

        if (failure == 0) {
            if (goodsList.size() == 1) {
                ImportErrorMessage message = new ImportErrorMessage();
                message.setMessage("组合品信息为空");
                resultList.add(message);
                failure += 1;
            }
        }

        //2. 表体必填项校验:组合品ID、单品商品货号、数量、销售标准价格
        Set<String> groupSet = new HashSet<>();
        if (failure == 0) {
            for (int j = 1; j < goodsList.size(); j++) {
                Map<String, String> map = this.toMap(goodsList.get(0).toArray(), goodsList.get(j).toArray());
                try {
                    String skuId = map.get("组合品ID");
                    if (StringUtils.isEmpty(skuId)) {
                        ImportErrorMessage message = new ImportErrorMessage();
                        message.setRows(j + 1);
                        message.setMessage("组合品ID不能为空");
                        resultList.add(message);
                        failure += 1;
                        continue;
                    }
                    String goodsNo = map.get("单品商品货号");
                    if (StringUtils.isEmpty(goodsNo)) {
                        ImportErrorMessage message = new ImportErrorMessage();
                        message.setRows(j + 1);
                        message.setMessage("单品商品货号不能为空");
                        resultList.add(message);
                        failure += 1;
                        continue;
                    }
                    String num = map.get("数量");
                    if (StringUtils.isEmpty(num) && !StringUtils.isNumeric(num)) {
                        ImportErrorMessage message = new ImportErrorMessage();
                        message.setRows(j + 1);
                        message.setMessage("数量不能为空且为整数");
                        resultList.add(message);
                        failure += 1;
                        continue;
                    }

                    String reg = "^[0-9]+(.[0-9]+)?$";
                    String price = map.get("销售标准单价");
                    if (StringUtils.isEmpty(price) && !price.matches(reg)) {
                        ImportErrorMessage message = new ImportErrorMessage();
                        message.setRows(j + 1);
                        message.setMessage("销售标准单价不能为空且为数值");
                        resultList.add(message);
                        failure += 1;
                        continue;
                    }

                    if (!skuIdMap.containsKey(skuId)) {
                        ImportErrorMessage message = new ImportErrorMessage();
                        message.setRows(j + 1);
                        message.setMessage("组合品ID在表头不存在");
                        resultList.add(message);
                        failure += 1;
                        continue;
                    }

                    //每个组合品条码仅能存在一条记录
                    String key = skuId + goodsNo;
                    if (groupSet.contains(key)) {
                        ImportErrorMessage message = new ImportErrorMessage();
                        message.setRows(j + 1);
                        message.setMessage("组合品ID:" + skuId + ", 单品商品货号:" + goodsNo + "有多条数据,合并后导入");
                        resultList.add(message);
                        failure += 1;
                        continue;
                    } else {
                        groupSet.add(key);
                    }
                    GroupMerchandiseContrastModel exist = skuIdMap.get(skuId);
                    Map<String, Object> goodsParam = new HashMap<>();
                    goodsParam.put("goodsNo", goodsNo);
                    goodsParam.put("merchantId", exist.getMerchantId());
                    MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(goodsParam);
                    if (goods == null) {
                        ImportErrorMessage message = new ImportErrorMessage();
                        message.setRows(j + 1);
                        message.setMessage("商家:" + exist.getMerchantName() + ",单品商品货号:" + goodsNo + "的商品不存在");
                        resultList.add(message);
                        failure += 1;
                        continue;
                    }

                    GroupMerchandiseContrastDetailModel detailModel = new GroupMerchandiseContrastDetailModel();
                    detailModel.setNum(Integer.valueOf(num));
                    BigDecimal decimal = new BigDecimal(price);
                    detailModel.setPrice(decimal.setScale(4, BigDecimal.ROUND_HALF_UP));
                    detailModel.setBarcode(goods.getBarcode());
                    detailModel.setGoodsNo(goodsNo);
                    detailModel.setGoodsName(goods.getName());
                    detailModel.setGoodsId(goods.getMerchandiseId());
                    detailModel.setCreateDate(TimeUtils.getNow());
                    if (skuIdMap.get(skuId).getDetailList() != null) {
                        skuIdMap.get(skuId).getDetailList().add(detailModel);
                    } else {
                        List<GroupMerchandiseContrastDetailModel> detailModelList = new ArrayList<>();
                        detailModelList.add(detailModel);
                        skuIdMap.get(skuId).setDetailList(detailModelList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ImportErrorMessage message = new ImportErrorMessage();
                    message.setRows(j + 1);
                    message.setMessage(e.getMessage());
                    resultList.add(message);
                    failure += 1;
                    continue;
                }
            }
        }

        //3. 插入数据
        if (failure == 0) {
            for (GroupMerchandiseContrastModel model : skuIdMap.values()) {
                try {
                    Long id = groupMerchandiseContrastDao.save(model);
                    if (id > 0) {
                        // 新增表体
                        for (GroupMerchandiseContrastDetailModel item : model.getDetailList()) {
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("merchandiseId", item.getGoodsId());
                            params.put("merchantId", model.getMerchantId());
                            MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(params);
                            if (goods != null && goods.getBrandId() != null) {                               
                                Map<String, Object> brand_params = new HashMap<String, Object>();
                                brand_params.put("brandId", goods.getBrandId());
                                BrandMongo brand = brandMongoDao.findOne(brand_params);
                                if (brand != null) {
                                	item.setBrand(brand.getName());
                                }
                            }
                            item.setGroupMerchandiseId(model.getId());
                            groupMerchandiseContrastDetailDao.save(item);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("------------------组合品对照表导入新增异常---------------------");
                }
                success++;
            }
        }
        Map map = new HashMap();
        map.put("success", success);
        map.put("pass", pass);
        map.put("failure", failure);
        map.put("message", resultList);
        return map;
    }

    @Override
    public List<SelectBean> getMerchantSelectBean() {
        Map<String, Object> param = new HashMap<>();
        param.put("isProxy", "0");
        List<MerchantInfoMongo> merchantInfoMongos = merchantInfoMongoDao.findAll(param);
        List<SelectBean> selectBeans = new ArrayList<>();
        for (MerchantInfoMongo mongo : merchantInfoMongos) {
            SelectBean selectBean = new SelectBean();
            selectBean.setValue(String.valueOf(mongo.getMerchantId()));
            selectBean.setLabel(mongo.getName());
            selectBeans.add(selectBean);
        }
        return selectBeans;
    }

    /**
     * 把两个数组组成一个map
     *
     * @param keys   键数组
     * @param values 值数组
     * @return 键值对应的map
     */
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

	@Override
	public GroupMerchandiseContrastDTO getDTODetail(Long id) throws SQLException {
		GroupMerchandiseContrastDTO dto = new GroupMerchandiseContrastDTO();
        dto.setId(id);
		return groupMerchandiseContrastDao.getDTODetails(dto);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantShopRelMongo> getShopList(String shopName) {
		
		Map<String, String> likeMap = new HashMap<String, String>() ;
		
		if(StringUtils.isNotBlank(shopName)) {
			likeMap.put("shopName", shopName) ;
		}

		Map<String, Object> params = new HashMap<>();
		params.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1);

		Map<String, Sort.Direction> sortBy = new HashMap<>();
		sortBy.put("dataSourceCode", Sort.Direction.ASC);
		
		return (List<MerchantShopRelMongo>)merchantShopRelMongoDao.getListByRegexOrOther(likeMap, params, sortBy);
	}

    @Override
    public void batchDel(List<Long> ids) throws SQLException {
        if (ids.size() == 0) {
            throw new RuntimeException("删除不能为空！");
        }
        for (Long id : ids) {
            GroupMerchandiseContrastModel model = groupMerchandiseContrastDao.searchById(id);
            if (DERP_ORDER.GROUPMERCHANDISECONTRAST_STATUS_1.equals(model.getStatus())) {
                throw new RuntimeException("仅可对状态为“已停用”的商品进行删除");
            }
        }
        try {
            for (Long id : ids) {
                groupMerchandiseContrastDetailDao.batchDelByGroupId(id);
                groupMerchandiseContrastDetailHistoryDao.batchDelByGroupId(id);
            }
            groupMerchandiseContrastDao.delete(ids);
        } catch (Exception e) {
            throw new RuntimeException("删除失败！");
        }
    }


}
