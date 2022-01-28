package com.topideal.order.service.bill.impl;

import com.taobao.api.request.ItempropsGetRequest;
import com.topideal.common.system.auth.User;
import com.topideal.dao.bill.TobTemporaryReceiveBillCostItemMonthlyDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillCostItemMonthlyDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.bill.ToBTempCostMonthlySnapshotService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ToBTempCostMonthlySnapshotServiceImpl implements ToBTempCostMonthlySnapshotService {

    @Autowired
    private TobTemporaryReceiveBillCostItemMonthlyDao tobTemporaryReceiveBillCostItemMonthlyDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;

    @Override
    public TobTemporaryReceiveBillCostItemMonthlyDTO listToBTempCostMonthlyByPage(TobTemporaryReceiveBillCostItemMonthlyDTO dto, User user) throws Exception {

        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }

        return tobTemporaryReceiveBillCostItemMonthlyDao.listToBTempCostMonthlyByPage(dto);
    }

    @Override
    public Map<String, List<Map<String, Object>>> exportToBTempCostMonthlySnapshot(TobTemporaryReceiveBillCostItemMonthlyDTO dto, User user) throws Exception {
        Map<String, List<Map<String, Object>>> listMap = new HashMap<>();

        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                return listMap;
            }
            dto.setBuList(buList);
        }

        List<TobTemporaryReceiveBillCostItemMonthlyDTO> monthlyModels = tobTemporaryReceiveBillCostItemMonthlyDao.listByDto(dto);

        Map<String, TobTemporaryReceiveBillCostItemMonthlyDTO> mainMap = new HashMap<>();
        Map<String, List<TobTemporaryReceiveBillCostItemMonthlyDTO>> itemMap = new HashMap<>();

        for (TobTemporaryReceiveBillCostItemMonthlyDTO monthlyDTO : monthlyModels) {
            //月份+公司+事业部+客户+母品牌+销售币种+商品+po+上架单号
            String key = monthlyDTO.getMonth() + "_" + monthlyDTO.getMerchantId() + "_" + monthlyDTO.getCustomerId() + "_"
                    + monthlyDTO.getBuId() + "_" + monthlyDTO.getParentBrandId() + "_" + monthlyDTO.getCurrency() + "_"
                    + monthlyDTO.getGoodsId() + "_" + monthlyDTO.getPoNo() + "_" + monthlyDTO.getShelfCode() + "_"
                    + monthlyDTO.getTempItemId();

            List<TobTemporaryReceiveBillCostItemMonthlyDTO> itemMonthlyModels = new ArrayList<>();

            if (itemMap.containsKey(key)) {
                itemMonthlyModels.addAll(itemMap.get(key));
            }

            itemMonthlyModels.add(monthlyDTO);
            itemMap.put(key, itemMonthlyModels);

        }

        List<Map<String, Object>> mainMapList = new ArrayList<>();
        List<Map<String, Object>> itemMapList = new ArrayList<>();

        for (String key : itemMap.keySet()) {
            List<TobTemporaryReceiveBillCostItemMonthlyDTO> itemMonthlyDTOS = itemMap.get(key);
            Map<String, Object> map = new HashMap<>();
            //已核销金额
            BigDecimal verifyAmount = new BigDecimal("0");

            //上架月份为对应“快照月份”的总暂估应收金额
            BigDecimal shelfReceiveAmount = new BigDecimal("0");
            //是否包含上架月份为对应“快照月份”的暂估
            boolean flag = false;

            //入账月份为“快照月份”的应收账单核销金额
            BigDecimal creditVerifyAmount = new BigDecimal("0");

            List<String> receiveBillCodes = new ArrayList<>();
            for (TobTemporaryReceiveBillCostItemMonthlyDTO itemMonthlyModel : itemMonthlyDTOS) {
                BigDecimal itemMonthlyModelVerifiedAmount = itemMonthlyModel.getVerifiedAmount() == null ? BigDecimal.ZERO : itemMonthlyModel.getVerifiedAmount();
                verifyAmount = verifyAmount.add(itemMonthlyModelVerifiedAmount);
                if (StringUtils.isNotBlank(itemMonthlyModel.getReceiveCode()) && !receiveBillCodes.contains(itemMonthlyModel.getReceiveCode())) {
                    receiveBillCodes.add(itemMonthlyModel.getReceiveCode());
                }

                if (itemMonthlyModel.getShelfDate().toString().substring(0, 7).equals(dto.getMonth())) {
                    flag = true;
                }

                if (StringUtils.isNotBlank(itemMonthlyModel.getCreditMonth()) && itemMonthlyModel.getCreditMonth().equals(dto.getMonth())) {
                    creditVerifyAmount = creditVerifyAmount.add(itemMonthlyModelVerifiedAmount);
                }

            }

            //未核销金额
            BigDecimal nonVerifyAmount = itemMonthlyDTOS.get(0).getRebateAmount().subtract(verifyAmount);

            if (flag) {
                shelfReceiveAmount = itemMonthlyDTOS.get(0).getRebateAmount();
            }

            map.put("month", itemMonthlyDTOS.get(0).getMonth());
            map.put("buName", itemMonthlyDTOS.get(0).getBuName());
            map.put("customerName", itemMonthlyDTOS.get(0).getCustomerName());
            map.put("poNo", itemMonthlyDTOS.get(0).getPoNo());
            map.put("sdCode", itemMonthlyDTOS.get(0).getSdCode());
            map.put("shelfCode", itemMonthlyDTOS.get(0).getShelfCode());
            map.put("shelfDate", itemMonthlyDTOS.get(0).getShelfDate());
            map.put("goodsNo", itemMonthlyDTOS.get(0).getGoodsNo());
            map.put("parentBrandName", itemMonthlyDTOS.get(0).getParentBrandName());
            map.put("goodsName", itemMonthlyDTOS.get(0).getGoodsName());
            map.put("shelfNum", itemMonthlyDTOS.get(0).getShelfNum());
            map.put("price", itemMonthlyDTOS.get(0).getPrice());
            map.put("sdType", itemMonthlyDTOS.get(0).getSdTypeName());
            map.put("sdRatio", itemMonthlyDTOS.get(0).getSdRatio());
            map.put("currency", itemMonthlyDTOS.get(0).getCurrency());
            map.put("rebateAmount", itemMonthlyDTOS.get(0).getRebateAmount());
            map.put("verifyAmount", verifyAmount);
            map.put("nonVerifyAmount", nonVerifyAmount);
            map.put("receiveCodes", String.join(",", receiveBillCodes));
            itemMapList.add(map);

            //月份+公司+事业部+客户+母品牌+销售币种+sd类型
            String mainKey = itemMonthlyDTOS.get(0).getMonth() + "_" + itemMonthlyDTOS.get(0).getMerchantId() + "_" + itemMonthlyDTOS.get(0).getCustomerId() + "_"
                    + itemMonthlyDTOS.get(0).getBuId() + "_" + itemMonthlyDTOS.get(0).getParentBrandId() + "_" + itemMonthlyDTOS.get(0).getCurrency() + "_"
                    + itemMonthlyDTOS.get(0).getSdTypeId();

            TobTemporaryReceiveBillCostItemMonthlyDTO billItemMonthlyDTO = new TobTemporaryReceiveBillCostItemMonthlyDTO();
            BeanUtils.copyProperties(itemMonthlyDTOS.get(0), billItemMonthlyDTO);
            billItemMonthlyDTO.setNonVerifiedAmount(nonVerifyAmount);
            billItemMonthlyDTO.setRebateAmount(shelfReceiveAmount);
            billItemMonthlyDTO.setVerifiedAmount(creditVerifyAmount);

            if (mainMap.containsKey(mainKey)) {
                BigDecimal totalNonVerifyAmount = mainMap.get(mainKey).getNonVerifiedAmount().add(nonVerifyAmount);
                BigDecimal totalVerifyAmount = mainMap.get(mainKey).getVerifiedAmount().add(creditVerifyAmount);
                BigDecimal totalReceiveAmount = mainMap.get(mainKey).getRebateAmount().add(shelfReceiveAmount);
                billItemMonthlyDTO.setNonVerifiedAmount(totalNonVerifyAmount);
                billItemMonthlyDTO.setRebateAmount(totalReceiveAmount);
                billItemMonthlyDTO.setVerifiedAmount(totalVerifyAmount);
            }
            mainMap.put(mainKey, billItemMonthlyDTO);
        }

        for (String key : mainMap.keySet()) {
            TobTemporaryReceiveBillCostItemMonthlyDTO billItemMonthlyDTO = mainMap.get(key);
            Map<String, Object> map = new HashMap<>();
            map.put("month", billItemMonthlyDTO.getMonth());
            map.put("merchantName", billItemMonthlyDTO.getMerchantName());
            map.put("buName", billItemMonthlyDTO.getBuName());
            map.put("customerName", billItemMonthlyDTO.getCustomerName());
            map.put("parentBrandName", billItemMonthlyDTO.getParentBrandName());
            map.put("currency", billItemMonthlyDTO.getCurrency());
            map.put("sdType", billItemMonthlyDTO.getSdTypeName());
            map.put("receivableAmount", billItemMonthlyDTO.getRebateAmount());
            map.put("verifyAmount", billItemMonthlyDTO.getVerifiedAmount());
            map.put("nonVerifyAmount", billItemMonthlyDTO.getNonVerifiedAmount());
            mainMapList.add(map);
        }

        listMap.put("mainList", mainMapList);
        listMap.put("itemList", itemMapList);

        return listMap;
    }
}
