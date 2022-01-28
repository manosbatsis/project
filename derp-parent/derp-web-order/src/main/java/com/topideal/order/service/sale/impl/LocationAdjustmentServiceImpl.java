package com.topideal.order.service.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.NumberUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.dao.transfer.TransferInDepotDao;
import com.topideal.dao.transfer.TransferOrderDao;
import com.topideal.dao.transfer.TransferOrderItemDao;
import com.topideal.dao.transfer.TransferOutDepotDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.InventoryLocationAdjustExportDTO;
import com.topideal.entity.dto.sale.LocationAdjustmentOrderDTO;
import com.topideal.entity.vo.sale.*;
import com.topideal.entity.vo.transfer.TransferInDepotModel;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;
import com.topideal.entity.vo.transfer.TransferOrderModel;
import com.topideal.entity.vo.transfer.TransferOutDepotModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.LocationAdjustmentService;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.tree.TreeNode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationAdjustmentServiceImpl implements LocationAdjustmentService {

    @Autowired
    private LocationAdjustmentOrderDao locationAdjustmentOrderDao;

    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;

    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;

    @Autowired
    private TransferOrderDao transferOrderDao;

    @Autowired
    private TransferOrderItemDao transferOrderItemDao;

    @Autowired
    private SaleOutDepotDao saleOutDepotDao;

    @Autowired
    private SaleOutDepotItemDao saleOutDepotItemDao;

    @Autowired
    private SaleReturnOrderDao saleReturnOrderDao;

    @Autowired
    private SaleReturnOrderItemDao saleReturnOrderItemDao;

    @Autowired
    private DepotInfoMongoDao depotInfoMongoDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TransferInDepotDao transferInDepotDao;

    @Autowired
    private TransferOutDepotDao transferOutDepotDao;

    @Autowired
    private SaleReturnIdepotDao saleReturnIdepotDao;

    @Autowired
    private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;

    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;

    @Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;

    @Autowired
    private CommonBusinessService commonBusinessService;


    @Override
    public LocationAdjustmentOrderDTO listInventoryLocationAdjustDTO(LocationAdjustmentOrderDTO dto, User user) throws SQLException {
        if (dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buIds);
        }
        return locationAdjustmentOrderDao.queryDTOListByPage(dto);
    }

    @Override
    public Map saveImportLocationAdjustment(List<List<Map<String, String>>> data, User user) throws Exception {

        List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
        int success = 0;
        int failure = 0;

        Map<String, Object> retMap = new HashMap<>();

        List<Map<String, String>> objects = data.get(0);

        List<LocationAdjustmentOrderDTO> locationAdjustmentOrderDTOS = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            LocationAdjustmentOrderDTO orderDTO = new LocationAdjustmentOrderDTO();

            orderDTO.setInventoryType(DERP.ISDAMAGE_0);

            Map<String, String> map = objects.get(i);
            String orderType = map.get("单据类型");
            if (checkIsNullOrNot(i, orderType, "单据类型不能为空", resultList)) {
                failure += 1;
                continue;
            }
            orderType = orderType.trim() ;
            if (DERP_ORDER.getKeyByLabel(DERP_ORDER.locationAdjustmentOrder_importOrderTypeList, orderType).equals("")) {
                setErrorMsg(i, "单据类型不存在", resultList);
                failure += 1;
                continue;
            }

            String buCode = map.get("事业部编码");
            if (checkIsNullOrNot(i, buCode, "事业部编码不能为空", resultList)) {
                failure += 1;
                continue;
            }
            buCode = buCode.trim() ;

            String orderCode = map.get("订单号");
            if (!"损益".equals(orderType) && checkIsNullOrNot(i, orderCode, "订单号不能为空", resultList)) {
                failure += 1;
                continue;
            }
            orderCode = orderCode.trim() ;

            String barcode = map.get("商品条形码");
            if (checkIsNullOrNot(i, barcode, "商品条形码不能为空", resultList)) {
                failure += 1;
                continue;
            }
            barcode = barcode.trim() ;

            String increaseType = map.get("调增类型");
            if (checkIsNullOrNot(i, increaseType, "调增类型不能为空", resultList)) {
                failure += 1;
                continue;
            }
            increaseType = increaseType.trim() ;

            String reduceType = map.get("调减类型");
            if (checkIsNullOrNot(i, reduceType, "调减类型不能为空", resultList)) {
                failure += 1;
                continue;
            }
            reduceType = reduceType.trim() ;

            String num = map.get("调整数量");
            if (checkIsNullOrNot(i, num, "调整数量不能为空", resultList)) {
                failure += 1;
                continue;
            }
            num = num.trim() ;

            if (!StringUtils.isNumeric(num)) {
                setErrorMsg(i, "调整数量不是数值", resultList);
                failure += 1;
                continue;
            }

            String depotCode = map.get("仓库编码");
            String inventoryType = map.get("库存类型");
            String month = map.get("归属月份");

            if ("损益".equals(orderType)) {

                if (checkIsNullOrNot(i, inventoryType, "库存类型不能为空", resultList)) {
                    failure += 1;
                    continue;
                }

                if (checkIsNullOrNot(i, depotCode, "仓库编码不能为空", resultList)) {
                    failure += 1;
                    continue;
                }

                if (checkIsNullOrNot(i, month, "归属月份不能为空", resultList)) {
                    failure += 1;
                    continue;
                }

                if ("".equals(DERP.getKeyByLabel(DERP.isDamageList, inventoryType))) {
                    setErrorMsg(i, "库存类型不存在", resultList);
                    failure += 1;
                    continue;
                }

                Map<String, Object> dparamMap = new HashMap<String, Object>();
                dparamMap.put("depotCode", depotCode);
                DepotInfoMongo depot = depotInfoMongoDao.findOne(dparamMap);
                if (depot == null) {
                    setErrorMsg(i, "仓库不存在", resultList);
                    failure += 1;
                    continue;
                }

                orderDTO.setDepotId(depot.getDepotId());
                orderDTO.setDepotName(depot.getName());
                orderDTO.setMonth(month);
                orderDTO.setInventoryType((String) DERP.getKeyByLabel(DERP.isDamageList, inventoryType));
            }


            String remark = map.get("调整原因");

            //查看是否是公司主体下关联的事业部
            Map<String, Object> buMap = new HashMap<>();
            buMap.put("buCode", buCode.trim());
            buMap.put("merchantId", user.getMerchantId());
            buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
            MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
            if (merchantBuRelMongo == null) {
                setErrorMsg(i, "事业部在该公司下不存在", resultList);
                failure += 1;
                continue;
            }

            //校验是否存在对应的公司主体下的商品信息
            Map<String, Object> merchandiseParams = new HashMap<>();
            merchandiseParams.put("merchantId", user.getMerchantId());
            merchandiseParams.put("barcode", barcode);
            MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseParams);

            if (merchandise == null) {
                setErrorMsg(i, "该条码：" + barcode + "在该公司下不存在", resultList);
                failure += 1;
                continue;
            }

            Map<String, Object> stockLocationParams = new HashMap<>();
            stockLocationParams.put("merchantId", user.getMerchantId());
            stockLocationParams.put("buId", merchantBuRelMongo.getBuId());
            stockLocationParams.put("name", increaseType);
            stockLocationParams.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
            BuStockLocationTypeConfigMongo increaseTypeMogo = buStockLocationTypeConfigMongoDao.findOne(stockLocationParams);
            if (increaseTypeMogo == null) {
                setErrorMsg(i, "调增类型：" + increaseType + "在该公司事业部下不存在", resultList);
                failure += 1;
                continue;
            }

            stockLocationParams.put("name", reduceType);
            BuStockLocationTypeConfigMongo reduceTypeMogo = buStockLocationTypeConfigMongoDao.findOne(stockLocationParams);
            if (reduceTypeMogo == null) {
                setErrorMsg(i, "调减类型：" + reduceType + "在该公司事业部下不存在", resultList);
                failure += 1;
                continue;
            }

            orderDTO.setBuId(merchantBuRelMongo.getBuId());
            orderDTO.setBuName(merchantBuRelMongo.getBuName());
            orderDTO.setOrderType((String) DERP_ORDER.getKeyByLabel(DERP_ORDER.locationAdjustmentOrder_importOrderTypeList, orderType));
            orderDTO.setOrderCode(orderCode);
            orderDTO.setBarcode(barcode);
            orderDTO.setGoodsName(merchandise.getName());
            orderDTO.setReason(remark);
            orderDTO.setInStockLocationTypeName(increaseType);
            orderDTO.setInStockLocationTypeId(increaseTypeMogo.getBuStockLocationTypeId());
            orderDTO.setOutStockLocationTypeName(reduceType);
            orderDTO.setOutStockLocationTypeId(reduceTypeMogo.getBuStockLocationTypeId());
            orderDTO.setAdjustNum(Integer.valueOf(num));
            orderDTO.setCreater(user.getId());
            orderDTO.setCreateName(user.getName());
            orderDTO.setStatus(DERP_ORDER.LOCATIONADJUSTMENTORDER_STATUS_001);

            if (orderType.equals("电商订单")) {
                OrderModel orderModel = new OrderModel();
                orderModel.setExternalCode(orderCode);
                orderModel = orderDao.searchByModel(orderModel);

                if (orderModel != null) {
                    orderDTO.setDepotId(orderModel.getDepotId());
                    orderDTO.setDepotName(orderModel.getDepotName());
                    orderDTO.setCustomerId(orderModel.getCustomerId());
                    orderDTO.setCustomerName(orderModel.getCustomerName());
                    orderDTO.setPlatformCode(orderModel.getStorePlatformName());
                    orderDTO.setPlatformName(DERP.getLabelByKey(DERP.storePlatformList, orderModel.getStorePlatformName()));
                    orderDTO.setMonth(TimeUtils.formatMonth(orderModel.getDeliverDate()));
                }
            }

            if (orderType.equals("调拨订单")) {
                TransferOrderModel transfer = new TransferOrderModel();
                transfer.setCode(orderCode);
                transfer = transferOrderDao.searchByModel(transfer);

                if (transfer == null) {
                    setErrorMsg(i, "该业务单号不存在", resultList);
                    failure += 1;
                    continue;
                }

                TransferOrderItemModel transferOrderItemModel = new TransferOrderItemModel();
                transferOrderItemModel.setTransferOrderId(transfer.getId());
                transferOrderItemModel.setOutBarcode(barcode);
                List<TransferOrderItemModel> itemModels = transferOrderItemDao.list(transferOrderItemModel);

                if (itemModels == null || itemModels.size() == 0) {
                    setErrorMsg(i, "该条码：" + barcode + "在业务单据中不存在", resultList);
                    failure += 1;
                    continue;
                }

                Integer transferNum = 0;
                for (TransferOrderItemModel itemModel : itemModels) {
                    transferNum += itemModel.getTransferNum();
                }

                if (Integer.valueOf(num) > transferNum) {
                    setErrorMsg(i, "该条码：" + barcode + "在业务单据中的数量需小于系统单据中对应商品条码的数量", resultList);
                    failure += 1;
                    continue;
                }

                orderDTO.setDepotId(transfer.getOutDepotId());
                orderDTO.setDepotName(transfer.getOutDepotName());
                orderDTO.setOrderId(transfer.getId());
            }

            if (orderType.equals("销售出库单")) {
                SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
                saleOutDepotModel.setCode(orderCode);
                saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);

                if (saleOutDepotModel == null) {
                    setErrorMsg(i, "该业务单号不存在", resultList);
                    failure += 1;
                    continue;
                }

                SaleOutDepotItemModel itemModel = new SaleOutDepotItemModel();
                itemModel.setSaleOutDepotId(saleOutDepotModel.getId());
                itemModel.setBarcode(barcode);
                List<SaleOutDepotItemModel> saleOutDepotItemModels = saleOutDepotItemDao.list(itemModel);

                if (saleOutDepotItemModels == null || saleOutDepotItemModels.size() == 0) {
                    setErrorMsg(i, "该条码：" + barcode + "在业务单据中不存在", resultList);
                    failure += 1;
                    continue;
                }

                Integer saleNum = 0;
                for (SaleOutDepotItemModel outDepotItemModel : saleOutDepotItemModels) {
                    saleNum += outDepotItemModel.getSaleNum();
                }

                if (Integer.valueOf(num) > saleNum) {
                    setErrorMsg(i, "该条码：" + barcode + "在业务单据中的数量需小于系统单据中对应商品条码的数量", resultList);
                    failure += 1;
                    continue;
                }

                orderDTO.setDepotId(saleOutDepotModel.getOutDepotId());
                orderDTO.setDepotName(saleOutDepotModel.getOutDepotName());
                orderDTO.setCustomerId(saleOutDepotModel.getCustomerId());
                orderDTO.setCustomerName(saleOutDepotModel.getCustomerName());
                orderDTO.setMonth(TimeUtils.formatMonth(saleOutDepotModel.getDeliverDate()));

            }

            if (orderType.equals("销售退货单")) {
                SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
                saleReturnOrderModel.setCode(orderCode);
                saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);

                if (saleReturnOrderModel == null) {
                    setErrorMsg(i, "该业务单号不存在", resultList);
                    failure += 1;
                    continue;
                }

                SaleReturnOrderItemModel itemModel = new SaleReturnOrderItemModel();
                itemModel.setOrderId(saleReturnOrderModel.getId());
                itemModel.setBarcode(barcode);
                List<SaleReturnOrderItemModel> itemModels = saleReturnOrderItemDao.list(itemModel);

                if (itemModels == null || itemModels.size() == 0) {
                    setErrorMsg(i, "该条码：" + barcode + "在业务单据中不存在", resultList);
                    failure += 1;
                    continue;
                }

                Integer saleNum = 0;
                for (SaleReturnOrderItemModel outDepotItemModel : itemModels) {
                    saleNum += outDepotItemModel.getReturnNum();
                }

                if (Integer.valueOf(num) > saleNum) {
                    setErrorMsg(i, "该条码：" + barcode + "在业务单据中的数量需小于系统单据中对应商品条码的数量", resultList);
                    failure += 1;
                    continue;
                }

                orderDTO.setDepotId(saleReturnOrderModel.getInDepotId());
                orderDTO.setDepotName(saleReturnOrderModel.getInDepotName());
                orderDTO.setCustomerId(saleReturnOrderModel.getCustomerId());
                orderDTO.setCustomerName(saleReturnOrderModel.getCustomerName());
                orderDTO.setOrderId(saleReturnOrderModel.getId());
            }

            locationAdjustmentOrderDTOS.add(orderDTO);

        }

        if (failure > 0) {
            retMap.put("msgList", resultList);
            retMap.put("failure", failure);
            retMap.put("success", success);
            return retMap;

        }

        List<LocationAdjustmentOrderModel> locationAdjustmentOrderModels = new ArrayList<>();
        for (LocationAdjustmentOrderDTO adjustmentOrderDTO : locationAdjustmentOrderDTOS) {

            Map<String, Object> dparamMap = new HashMap<String, Object>();
            dparamMap.put("depotId", adjustmentOrderDTO.getDepotId());
            DepotInfoMongo depot = depotInfoMongoDao.findOne(dparamMap);

            if (depot != null && DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
                adjustmentOrderDTO.setTallyingUnit(DERP.UNIT_02);
            }

            adjustmentOrderDTO.setMerchantId(user.getMerchantId());
            adjustmentOrderDTO.setMerchantName(user.getMerchantName());
            if (adjustmentOrderDTO.getOrderType().equals(DERP_ORDER.LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_DBDD)) {

                TransferOrderModel transferOrder = transferOrderDao.searchById(adjustmentOrderDTO.getOrderId());

                TransferOutDepotModel outDepotModel = new TransferOutDepotModel();
                outDepotModel.setTransferOrderId(adjustmentOrderDTO.getOrderId());
                outDepotModel = transferOutDepotDao.searchByModel(outDepotModel);

                LocationAdjustmentOrderModel outAdjustmentOrderModel = new LocationAdjustmentOrderModel();
                BeanUtils.copyProperties(adjustmentOrderDTO, outAdjustmentOrderModel);
                outAdjustmentOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_KWTZD));
                outAdjustmentOrderModel.setOrderType(DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_DBCK);
                outAdjustmentOrderModel.setCreateDate(TimeUtils.getNow());

                if (outDepotModel != null) {
                    outAdjustmentOrderModel.setMonth(TimeUtils.formatMonth(outDepotModel.getTransferDate()));
                }
                locationAdjustmentOrderModels.add(outAdjustmentOrderModel);


                TransferInDepotModel inDepotModel = new TransferInDepotModel();
                inDepotModel.setTransferOrderId(adjustmentOrderDTO.getOrderId());
                inDepotModel = transferInDepotDao.searchByModel(inDepotModel);

                LocationAdjustmentOrderModel inAdjustmentOrderModel = new LocationAdjustmentOrderModel();
                String increaseType = adjustmentOrderDTO.getInStockLocationTypeName();
                Long increaseTypeId = adjustmentOrderDTO.getInStockLocationTypeId();
                String reduceType = adjustmentOrderDTO.getOutStockLocationTypeName();
                Long reduceTypeId = adjustmentOrderDTO.getOutStockLocationTypeId();
                BeanUtils.copyProperties(adjustmentOrderDTO, inAdjustmentOrderModel);
                inAdjustmentOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_KWTZD));
                inAdjustmentOrderModel.setOrderType(DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_DBRK);

                inAdjustmentOrderModel.setInStockLocationTypeName(reduceType);
                inAdjustmentOrderModel.setInStockLocationTypeId(reduceTypeId);
                inAdjustmentOrderModel.setOutStockLocationTypeName(increaseType);
                inAdjustmentOrderModel.setOutStockLocationTypeId(increaseTypeId);
                inAdjustmentOrderModel.setCreateDate(TimeUtils.getNow());
                inAdjustmentOrderModel.setDepotName(transferOrder.getInDepotName());
                inAdjustmentOrderModel.setDepotId(transferOrder.getInDepotId());

                dparamMap.put("depotId", transferOrder.getInDepotId());
                depot = depotInfoMongoDao.findOne(dparamMap);

                if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
                    inAdjustmentOrderModel.setTallyingUnit(DERP.UNIT_02);
                }

                if (inDepotModel != null) {
                    inAdjustmentOrderModel.setMonth(TimeUtils.formatMonth(inDepotModel.getTransferDate()));
                }
                locationAdjustmentOrderModels.add(inAdjustmentOrderModel);
            }

            if (adjustmentOrderDTO.getOrderType().equals(DERP_ORDER.LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_XSTH)) {

                SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
                saleReturnIdepotModel.setOrderId(adjustmentOrderDTO.getOrderId());
                saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);

                LocationAdjustmentOrderModel adjustmentOrderModel = new LocationAdjustmentOrderModel();
                BeanUtils.copyProperties(adjustmentOrderDTO, adjustmentOrderModel);
                adjustmentOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_KWTZD));
                adjustmentOrderModel.setCreateDate(TimeUtils.getNow());

                if (saleReturnIdepotModel != null) {
                    adjustmentOrderModel.setMonth(TimeUtils.formatMonth(saleReturnIdepotModel.getReturnInDate()));
                }
                locationAdjustmentOrderModels.add(adjustmentOrderModel);
            }

            if (!(adjustmentOrderDTO.getOrderType().equals(DERP_ORDER.LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_DBDD) ||
                    adjustmentOrderDTO.getOrderType().equals(DERP_ORDER.LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_XSTH))) {
                LocationAdjustmentOrderModel adjustmentOrderModel = new LocationAdjustmentOrderModel();
                BeanUtils.copyProperties(adjustmentOrderDTO, adjustmentOrderModel);
                adjustmentOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_KWTZD));
                adjustmentOrderModel.setCreateDate(TimeUtils.getNow());
                locationAdjustmentOrderModels.add(adjustmentOrderModel);
            }
            success++;
        }

        if (locationAdjustmentOrderModels.size() > 0) {
            locationAdjustmentOrderDao.batchSave(locationAdjustmentOrderModels);

            for (LocationAdjustmentOrderModel adjustmentOrderModel : locationAdjustmentOrderModels) {
                commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_23, adjustmentOrderModel.getCode(), "导入",  null, null);
            }
        }

        retMap.put("success",success);
        retMap.put("failure",failure);
        retMap.put("msgList",resultList);
        return retMap;
    }

    @Override
    public Integer getOrderCount(LocationAdjustmentOrderDTO dto, User user) throws SQLException {
        if (dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buIds.isEmpty()) {
                return 0;
            }
            dto.setBuList(buIds);
        }
        return locationAdjustmentOrderDao.countByDTO(dto);
    }

    @Override
    public List<LocationAdjustmentOrderDTO> getExportMainList(LocationAdjustmentOrderDTO dto, User user) throws SQLException {

        if (dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buIds.isEmpty()) {
                return new ArrayList<>();
            }
            dto.setBuList(buIds);
        }

        List<LocationAdjustmentOrderDTO> orderDTOS = new ArrayList<>();
        Integer countNum = locationAdjustmentOrderDao.countByDTO(dto);

        Integer pageSize = 2000;

        for (int i = 0; i < countNum; ) {
            int pageSub = (i + pageSize) < countNum ? (i + pageSize) : countNum;
            dto.setBegin(i);
            dto.setPageSize(pageSize);

            LocationAdjustmentOrderDTO locationAdjustmentOrderDTO = locationAdjustmentOrderDao.queryDTOListByPage(dto);
            orderDTOS.addAll(locationAdjustmentOrderDTO.getList());

            i = pageSub;
        }

        return orderDTOS;
    }

    @Override
    public boolean delLocationAdjust(String ids) throws Exception {
        LocationAdjustmentOrderDTO dto = new LocationAdjustmentOrderDTO();
        dto.setIds(ids);

        List<LocationAdjustmentOrderDTO> orderDTOS = locationAdjustmentOrderDao.listByDTO(dto);

        for (LocationAdjustmentOrderDTO orderDTO : orderDTOS) {
            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_STATUS_002.equals(orderDTO.getStatus())) {
                throw new RuntimeException("库位调整单：" + orderDTO.getCode() + "已审核，不能删除");
            }
        }

        locationAdjustmentOrderDao.delete(StrUtils.parseIds(ids));

        return true;
    }

    @Override
    public void auditLocationAdjust(String ids, User user) throws Exception {
        LocationAdjustmentOrderDTO dto = new LocationAdjustmentOrderDTO();
        dto.setIds(ids);

        List<LocationAdjustmentOrderDTO> orderDTOS = locationAdjustmentOrderDao.listByDTO(dto);

        for (LocationAdjustmentOrderDTO orderDTO : orderDTOS) {
            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_STATUS_002.equals(orderDTO.getStatus())) {
                throw new RuntimeException("库位调整单：" + orderDTO.getCode() + "已审核，不能重复审核");
            }

            if (orderDTO.getDepotId() == null) {
                throw new RuntimeException("库位调整单：" + orderDTO.getCode() + "仓库为空，无法审核");
            }

            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_XSCK.equals(orderDTO.getOrderType()) ||
                DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_XSTH.equals(orderDTO.getOrderType())) {
                if (orderDTO.getCustomerId() == null) {
                    throw new RuntimeException("库位调整单：" + orderDTO.getCode() + "客户为空，无法审核");
                }
            }

            if (StringUtils.isBlank(orderDTO.getMonth())) {
                throw new RuntimeException("库位调整单：" + orderDTO.getCode() + "归属月份为空，无法审核");
            }

            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_DSDD.equals(orderDTO.getOrderType())) {
                if (StringUtils.isBlank(orderDTO.getPlatformCode())) {
                    throw new RuntimeException("库位调整单：" + orderDTO.getCode() + "平台为空，无法审核");
                }
            }

            //判断是否已关账
            // 获取最大的关账日/月结日期
            FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
            closeAccountsMongo.setMerchantId(orderDTO.getMerchantId());
            closeAccountsMongo.setDepotId(orderDTO.getDepotId());
            closeAccountsMongo.setBuId(orderDTO.getBuId());
            String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
            String maxCloseAccountsMonth = "";
            if (StringUtils.isNotBlank(maxdate)) {
                // 获取该月份下月时间
                maxCloseAccountsMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
            }
            if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
                if (orderDTO.getMonth().compareTo(maxCloseAccountsMonth) < 0) {
                    throw new RuntimeException("库位调整单：" + orderDTO.getCode() + "已月结/已关帐，不允许审核；");
                }
            }

        }

        locationAdjustmentOrderDao.batchUpdateStatus(StrUtils.parseIds(ids), DERP_ORDER.LOCATIONADJUSTMENTORDER_STATUS_002);

        for (LocationAdjustmentOrderDTO orderDTO : orderDTOS) {
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_23, orderDTO.getCode(), "审核",  null, null);
        }

    }

    @Override
    public void refreshLocationAdjust(String ids, User user) throws Exception {
        LocationAdjustmentOrderDTO dto = new LocationAdjustmentOrderDTO();
        dto.setIds(ids);

        List<LocationAdjustmentOrderDTO> orderDTOS = locationAdjustmentOrderDao.listByDTO(dto);

        for (LocationAdjustmentOrderDTO orderDTO : orderDTOS) {
            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_STATUS_002.equals(orderDTO.getStatus())) {
                throw new RuntimeException("库位调整单：" + orderDTO.getCode() + "已审核，不能刷新");
            }
        }

        for (LocationAdjustmentOrderDTO orderDTO : orderDTOS) {
            LocationAdjustmentOrderModel model = new LocationAdjustmentOrderModel();
            BeanUtils.copyProperties(orderDTO, model);
            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_DSDD.equals(orderDTO.getOrderType())) {
                OrderModel orderModel = new OrderModel();
                orderModel.setExternalCode(orderDTO.getOrderCode());
                orderModel = orderDao.searchByModel(orderModel);

                if (orderModel != null) {
                    model.setDepotId(orderModel.getDepotId());
                    model.setDepotName(orderModel.getDepotName());
                    model.setCustomerId(orderModel.getCustomerId());
                    model.setCustomerName(orderModel.getCustomerName());
                    model.setPlatformCode(orderModel.getStorePlatformName());
                    model.setPlatformName(DERP.getLabelByKey(DERP.storePlatformList, orderModel.getStorePlatformName()));
                    model.setMonth(TimeUtils.formatMonth(orderModel.getDeliverDate()));
                    locationAdjustmentOrderDao.modify(model);
                    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_23, orderDTO.getCode(), "刷新",  null, null);
                }
            }

            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_DBCK.equals(orderDTO.getOrderType())) {
                TransferOutDepotModel outDepotModel = new TransferOutDepotModel();
                outDepotModel.setTransferOrderCode(orderDTO.getOrderCode());
                outDepotModel = transferOutDepotDao.searchByModel(outDepotModel);

                if (outDepotModel != null) {
                    model.setDepotId(outDepotModel.getOutDepotId());
                    model.setDepotName(outDepotModel.getOutDepotName());
                    model.setMonth(TimeUtils.formatMonth(outDepotModel.getTransferDate()));
                    locationAdjustmentOrderDao.modify(model);
                    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_23, orderDTO.getCode(), "刷新",  null, null);
                }
            }

            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_DBRK.equals(orderDTO.getOrderType())) {
                TransferInDepotModel inDepotModel = new TransferInDepotModel();
                inDepotModel.setTransferOrderCode(orderDTO.getOrderCode());
                inDepotModel = transferInDepotDao.searchByModel(inDepotModel);

                if (inDepotModel != null) {
                    model.setDepotId(inDepotModel.getInDepotId());
                    model.setDepotName(inDepotModel.getInDepotName());
                    model.setMonth(TimeUtils.formatMonth(inDepotModel.getTransferDate()));
                    locationAdjustmentOrderDao.modify(model);
                    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_23, orderDTO.getCode(), "刷新",  null, null);
                }
            }

            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_XSCK.equals(orderDTO.getOrderType())) {
                SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
                saleOutDepotModel.setCode(orderDTO.getOrderCode());
                saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);

                if (saleOutDepotModel != null) {
                    model.setDepotId(saleOutDepotModel.getOutDepotId());
                    model.setDepotName(saleOutDepotModel.getOutDepotName());
                    model.setCustomerId(saleOutDepotModel.getCustomerId());
                    model.setCustomerName(saleOutDepotModel.getCustomerName());
                    model.setMonth(TimeUtils.formatMonth(saleOutDepotModel.getDeliverDate()));
                    locationAdjustmentOrderDao.modify(model);
                    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_23, orderDTO.getCode(), "刷新",  null, null);
                }

            }

            if (DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_XSTH.equals(orderDTO.getOrderType())) {
                SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
                saleReturnOrderModel.setCode(orderDTO.getOrderCode());
                saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);

                if (saleReturnOrderModel == null) {
                    model.setDepotId(saleReturnOrderModel.getInDepotId());
                    model.setDepotName(saleReturnOrderModel.getInDepotName());
                    model.setCustomerId(saleReturnOrderModel.getCustomerId());
                    model.setCustomerName(saleReturnOrderModel.getCustomerName());

                    SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
                    saleReturnIdepotModel.setOrderId(saleReturnOrderModel.getId());
                    saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);

                    if (saleReturnIdepotModel != null) {
                        model.setMonth(TimeUtils.formatMonth(saleReturnIdepotModel.getReturnInDate()));
                    }
                    locationAdjustmentOrderDao.modify(model);
                    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_23, orderDTO.getCode(), "刷新",  null, null);
                }

            }

        }

    }


    /**
     * 判断输入字段是否为空
     * @param index
     * @param content
     * @param msg
     * @param resultList
     * @return
     */
    private boolean checkIsNullOrNot(int index , String content ,
                                     String msg ,List<ImportErrorMessage> resultList ) {

        if(StringUtils.isBlank(content)) {
            ImportErrorMessage message = new ImportErrorMessage();
            message.setRows(index + 2);
            message.setMessage(msg);
            resultList.add(message);

            return true ;

        }else {
            return false ;
        }

    }

    /**
     * 错误时，设置错误内容
     * @param index
     * @param msg
     * @param resultList
     */
    private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
        ImportErrorMessage message = new ImportErrorMessage();
        message.setRows(index + 2);
        message.setMessage(msg);
        resultList.add(message);
    }
}
