package com.topideal.order.service.transfer.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.*;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.PackingDetailsDao;
import com.topideal.dao.sale.BillOutinDepotItemDao;
import com.topideal.dao.sale.PojzTempDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.dao.sale.SaleShelfIdepotItemDao;
import com.topideal.dao.transfer.*;
import com.topideal.entity.dto.common.CustomsPackingDetailsDTO;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOrderFrom;
import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import com.topideal.entity.dto.transfer.TransferOutInBean;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.entity.vo.transfer.TransferInDepotModel;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;
import com.topideal.entity.vo.transfer.TransferOrderModel;
import com.topideal.entity.vo.transfer.TransferOutDepotModel;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.json.pushapi.epass.e01.PurchaseGoodsListJson;
import com.topideal.json.pushapi.epass.e01.PurchaseOrderRootJson;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreGoodsItem;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreOrderRoot;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreRecipient;
import com.topideal.json.pushapi.epass.e04.TransferGoodsItemJson;
import com.topideal.json.pushapi.epass.e04.TransferOrderRootJson;
import com.topideal.json.pushapi.epass.e08.OutStoreOrderItemJson;
import com.topideal.json.pushapi.epass.e08.OutStoreOrderRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.transfer.TransferOrderService;
import com.topideal.order.webapi.transfer.enums.ActionTypeEnum;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 调拨订单service实现类
 *
 * @author yucaifu
 */
@Service
public class TransferOrderServiceImpl implements TransferOrderService {

    /* 打印日志 */
    protected Logger LOGGER = LoggerFactory.getLogger(TransferOrderServiceImpl.class);

    //调拨订单表
    @Autowired
    private TransferOrderDao transferOrderDao;

    //调拨订单商品表
    @Autowired
    private TransferOrderItemDao transferOrderItemDao;

    //调拨出库表体
    @Autowired
    private TransferOutDepotItemDao transferOutDepotItemDao;
    //调拨出库表体
    @Autowired
    private TransferInDepotItemDao transferInDepotItemDao;
    //采购订单表头
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;

    //仓库信息dao
    @Autowired
    private DepotInfoMongoDao depotInfoMongoDao;
    //货品信息dao
    @Autowired
    private BrandMongoDao brandMongoDao;

    @Autowired
    private LbxNoMongoDao lbxNoMongoDao;

    @Autowired
    private RMQProducer rocketMQProducer;//MQ;
    @Autowired
    private DepotScheduleMongoDao depotScheduleMongoDao;// 仓库附表
    @Autowired
    private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private ApiSecretConfigMongoDao apiSecretConfigMongoDao;
    @Autowired
    private SaleReturnOrderDao saleReturnOrderDao;
    // 上架入库单
    @Autowired
    private SaleShelfIdepotItemDao shelfIdepotItemDao;
    //po历史结转临时表
    @Autowired
    private PojzTempDao pojzTempDao;
    //账单出入库单表
    @Autowired
    private BillOutinDepotItemDao billOutinDepotItemDao;
    @Autowired
    private TransferInDepotDao transferInDepotDao;
    @Autowired
    private TransferOutDepotDao transferOutDepotDao;
    @Autowired
    private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;
    @Autowired
    private UnitMongoDao unitMongoDao;
    @Autowired
    private CountryOriginMongoDao countryOriginMongoDao;
    @Autowired
    private DepotCustomsRelMongoDao depotCustomsRelMongoDao;
    @Autowired
    private FileTempDao fileTempDao;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao;
    @Autowired
    private PackingDetailsDao packingDetailsDao;
    @Autowired
    private MerchandiseExternalWarehouseMongoDao merchandiseExternalWarehouseMongoDao;
    @Autowired
    private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;

    /**
     * 列表（分页）
     *
     * @param dto
     * @return
     */
    @Override
    public TransferOrderDTO listTransferOrderPage(TransferOrderDTO dto, User user) throws SQLException {
        List<Long> userBuList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (userBuList.isEmpty()) {
            dto.setTotal(0);
            dto.setList(new ArrayList());
            return dto;
        }
        dto.setUserBuList(userBuList);
        TransferOrderDTO transferOrderDTO = transferOrderDao.listDtoByPage(dto);
        List<TransferOrderDTO> dtos = transferOrderDTO.getList();
        for (TransferOrderDTO orderDTO : dtos) {
            Map<String, Object> depotParam = new HashMap<>();
            depotParam.put("depotId", orderDTO.getInDepotId());
            DepotInfoMongo inDepotInfo = depotInfoMongoDao.findOne(depotParam);
            depotParam.put("depotId", orderDTO.getOutDepotId());
            DepotInfoMongo outDepotInfo = depotInfoMongoDao.findOne(depotParam);
            Map<String, Object> depotRelParam = new HashMap<>();
            depotRelParam.put("merchantId", orderDTO.getMerchantId());
            depotRelParam.put("depotId", orderDTO.getInDepotId());
            DepotMerchantRelMongo inDepot = depotMerchantRelMongoDao.findOne(depotRelParam);
            depotRelParam.put("depotId", orderDTO.getOutDepotId());
            DepotMerchantRelMongo outDepot = depotMerchantRelMongoDao.findOne(depotRelParam);
            if (inDepot != null) {
                orderDTO.setInDepotIsInOutInstruction(inDepot.getIsInOutInstruction());
                orderDTO.setInDepotIsOutDependIn(inDepot.getIsOutDependIn());
                orderDTO.setInDepotBatchValidation(inDepotInfo.getBatchValidation());
            }

            if (outDepot != null) {
                orderDTO.setOutDepotIsInOutInstruction(outDepot.getIsInOutInstruction());
                orderDTO.setOutDepotIsInDependOut(outDepot.getIsInDependOut());
                orderDTO.setOutDepotBatchValidation(outDepotInfo.getBatchValidation());
            }
        }
        return transferOrderDTO;
    }

    /**
     * 根据id查询调拨订单
     *
     * @param id
     * @return
     */
    @Override
    public TransferOrderDTO searchTransferOrderById(Long id) throws SQLException {
        TransferOrderDTO dto = transferOrderDao.getById(id);

        //调出仓库
        Map<String, Object> depotParamMap = new HashMap<String, Object>();
        if (dto.getOutDepotId() != null) {
            depotParamMap.put("depotId", dto.getOutDepotId());
            DepotInfoMongo outDepot = depotInfoMongoDao.findOne(depotParamMap);
            dto.setOutDepotBatchValidation(outDepot.getBatchValidation());
        }

        //调入仓库
        if (dto.getInDepotId() != null) {
            depotParamMap.put("depotId", dto.getInDepotId());
            DepotInfoMongo inDepot = depotInfoMongoDao.findOne(depotParamMap);
            dto.setInDepotBatchValidation(inDepot.getBatchValidation());
        }

        return dto;
    }

    /**
     * 根据id删除(支持批量)
     *
     * @param ids
     * @return
     */
    @Override
    public Map<String, String> delTransferOrder(Long userId, String name, List<Long> ids) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        int num = 0;
        for (Long id : ids) {
            // 根据id获取信息
            TransferOrderModel transferOrder = transferOrderDao.searchById(id);
            // 判断状态是否为 待审核
            if (!DERP_ORDER.TRANSFERORDER_STATUS_013.equals(transferOrder.getStatus())) {
                resultMap.put("code", "01");
                resultMap.put("message", "调拨订单号：" + transferOrder.getCode() + "的状态不为”未提交“，不能删除");
                return resultMap;
            }
        }

        for (Long id : ids) {
            // 根据id获取信息
            TransferOrderModel transferOrder = transferOrderDao.searchById(id);
            // 判断状态是否为 待审核
            if (DERP_ORDER.TRANSFERORDER_STATUS_013.equals(transferOrder.getStatus())) {
                transferOrder.setStatus(DERP.DEL_CODE_006);//已删除
                transferOrder.setModifyDate(TimeUtils.getNow());
                transferOrder.setModifier(userId);
                transferOrder.setModifierUsername(name);
                num += transferOrderDao.modify(transferOrder);
            }
        }
        if (num == 0) {
            resultMap.put("code", "01");
            resultMap.put("message", "删除失败");
            return resultMap;
        }
        resultMap.put("code", "00");
        resultMap.put("message", "删除成功");
        return resultMap;
    }

    @Override
    public Map<String, Object> saveOrUpdateTransferOrder(User user, TransferOrderFrom form, ActionTypeEnum actionTypeEnum) throws Exception {
        //检查4种场景必填字段
        Map<String, Object> retMap = checkForAdd(form, user);
        if (!retMap.get("code").equals("00")) {
            return retMap;
        }

        TransferOrderModel transOrder = null;


        String topidealCode = null;
        // ActionType = 编辑, 需要检查调拨单是否存在
        if (actionTypeEnum == ActionTypeEnum.UPDATE) {
            //编辑
            transOrder = transferOrderDao.searchById(Long.valueOf(form.getOrderId()));
            if (transOrder == null || transOrder.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_014)) {
                retMap.put("code", "01");
                retMap.put("message", "调拨单不存在/已提交");
                return retMap;
            }
            topidealCode = transOrder.getTopidealCode();
        } else {
            //新增
            transOrder = new TransferOrderModel();
            topidealCode = user.getTopidealCode();
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("depotId", form.getOutDepotId());
        DepotInfoMongo outDepot = depotInfoMongoDao.findOne(param);
        param.put("depotId", form.getInDepotId());
        DepotInfoMongo inDepot = depotInfoMongoDao.findOne(param);

        //调入商家卓志编码
        String inTopidealCode = "";
        if (form.getInCustomerId() != null && form.getInCustomerId().longValue() > 0) {
            Map<String, Object> cMap = new HashMap<String, Object>();
            cMap.put("merchantId", form.getInCustomerId());
            MerchantInfoMongo inMerchant = merchantInfoMongoDao.findOne(cMap);
            if (inMerchant != null) {
                inTopidealCode = inMerchant.getTopidealCode();
            }
        }

        Map<String, Object> buMap = new HashMap<>();
        buMap.put("buId", form.getBuId());
        buMap.put("merchantId", user.getMerchantId());
        buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
        if (merchantBuRelMongo == null) {
            retMap.put("code", "01");
            retMap.put("message", "事业部不在该公司下");
            return retMap;
        }

        if (merchantBuRelMongo.getStockLocationManage().equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0) &&
                form.getStockLocationTypeId() == null) {
            retMap.put("code", "01");
            retMap.put("message", "公司事业部启用库位管理，库位类型不能为空");
            return retMap;
        }

        BuStockLocationTypeConfigMongo buStockLocationTypeConfigMongo = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(form.getStockLocationTypeId())) {
            Map<String, Object> buStockLocationTypeParams = new HashMap<>();
            buStockLocationTypeParams.put("merchantId", user.getMerchantId());
            buStockLocationTypeParams.put("buId", merchantBuRelMongo.getBuId());
            buStockLocationTypeParams.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
            buStockLocationTypeParams.put("buStockLocationTypeId", Long.valueOf(form.getStockLocationTypeId()));
            buStockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(buStockLocationTypeParams);

            if (buStockLocationTypeConfigMongo == null) {
                retMap.put("code", "01");
                retMap.put("message", "库位类型在公司事业部下不存在");
                return retMap;
            }

        }

        //生成 TransferOrderModel
        retMap = convertTransferOrderModel(transOrder, user, inTopidealCode,
                form, merchantBuRelMongo, outDepot, inDepot, actionTypeEnum);
        if (retMap.get("code") == "01") {
            return retMap;
        }

        //保存商品
        List<TransferOrderItemModel> orderItemList = new ArrayList<TransferOrderItemModel>();
        List<Map<String, Object>> goodsList = form.getGoodsList();
        //根据信息生成 TransferOrderItemModelList
        retMap = convertTransferOrderItemModelList(goodsList, user, transOrder, actionTypeEnum, orderItemList);
        if (retMap.get("code") == "01") {
            return retMap;
        }

        if (buStockLocationTypeConfigMongo != null) {
            transOrder.setStockLocationTypeId(buStockLocationTypeConfigMongo.getBuStockLocationTypeId());
            transOrder.setStockLocationTypeName(buStockLocationTypeConfigMongo.getName());
        } else {
            transOrder.setStockLocationTypeId(null);
            transOrder.setStockLocationTypeName(null);
        }

        Long transferOrderId = null;
        //ActionType = 编辑
        //1. 修改 transOrder
        //2. 删除旧的 transOrderItemList
        if (actionTypeEnum == ActionTypeEnum.UPDATE) {
            transferOrderDao.modifyWithNull(transOrder);
            //删除原来的商品数据
            TransferOrderItemModel itemParam = new TransferOrderItemModel();
            itemParam.setTransferOrderId(transOrder.getId());
            List<TransferOrderItemModel> listItem = transferOrderItemDao.list(itemParam);
            List<Long> listIds = new ArrayList<Long>();
            if (listItem != null && listItem.size() > 0) {
                for (TransferOrderItemModel item : listItem) {
                    listIds.add(item.getId());
                }
                transferOrderItemDao.delete(listIds);
            }
            transferOrderId = transOrder.getId();
        } else {
            //新增 = ActionType
            //1. save transferOrder
            transferOrderId = transferOrderDao.save(transOrder);
        }

        //save transferOrderItemList
        for (TransferOrderItemModel orderItem : orderItemList) {
            orderItem.setTransferOrderId(transferOrderId);
            transferOrderItemDao.save(orderItem);
        }

        //删除原有的关联箱装明细
        PackingDetailsModel packingDetailsModel = new PackingDetailsModel();
        packingDetailsModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_2);
        packingDetailsModel.setOrderId(transferOrderId);
        packingDetailsDao.deleteByModel(packingDetailsModel);
        //保存装箱明细
        List<Map<String, Object>> packingList = form.getPackingList();
        if (packingList != null && packingList.size() > 0) {
            for (Map<String, Object> packModel : packingList) {
                String torrNo = (String) packModel.get("torrNo");
                String goodsNo = (String) packModel.get("goodsNo");
                String barcode = (String) packModel.get("barcode");
                Integer boxNum = (Integer) packModel.get("boxNum");
                Integer piecesNum = (Integer) packModel.get("piecesNum");
                String cabinetNo = (String) packModel.get("cabinetNo");

                PackingDetailsModel detailsModel = new PackingDetailsModel();
                detailsModel.setTorrNo(torrNo);
                detailsModel.setGoodsNo(goodsNo);
                detailsModel.setBarcode(barcode);
                detailsModel.setBoxNum(boxNum);
                detailsModel.setPiecesNum(piecesNum);
                detailsModel.setCabinetNo(cabinetNo);
                detailsModel.setOrderId(transferOrderId);
                detailsModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_2);
                packingDetailsDao.save(detailsModel);
            }
        }

        retMap = updateSendTransfer(user.getId(), user.getMerchantId(), user.getName(),
                transOrder.getTopidealCode(), transOrder);

        return retMap;
    }

    private Map<String, Object> convertTransferOrderModel(TransferOrderModel transOrder, User user, String inTopidealCode,
                                                          TransferOrderFrom form, MerchantBuRelMongo merchantBuRelMongo,
                                                          DepotInfoMongo outDepot, DepotInfoMongo inDepot,
                                                          ActionTypeEnum actionTypeEnum) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        transOrder.setLbxNo(form.getLbxNo() != null ? form.getLbxNo().trim() : "");//LBX单号
        transOrder.setPoNo(form.getPoNo() != null ? form.getPoNo().trim() : "");//po单号
        transOrder.setContractNo(form.getContractNo() != null ? form.getContractNo().trim() : "");//合同编号
        transOrder.setOutDepotId(form.getOutDepotId());//调出仓id
        transOrder.setOutDepotName(form.getOutDepotName());//调出仓名称
        transOrder.setInDepotId(form.getInDepotId());//调入仓id
        transOrder.setInDepotName(form.getInDepotName());//调入仓名称
        transOrder.setRemark(form.getRemark());//备注

        if (actionTypeEnum == ActionTypeEnum.UPDATE) {
            // 编辑
            // 如果非代销仓 并且 数据库的 是否同关区 不是空,说明是入库仓是从代销仓变成非代销仓,把是否同关区,和地址 设置成空
            if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
                if (!StringUtils.isEmpty(transOrder.getDepotScheduleAddress())) {
                    transOrder.setDepotScheduleAddress(" ");
                }
            } else {
                if (form.getDepotScheduleId() != null) {
                    transOrder.setDepotScheduleAddress(form.getDepotScheduleAddress());
                }

                transOrder.setDepotScheduleId(form.getDepotScheduleId());
            }
        } else {
            //新增
            transOrder.setIsSameArea(form.getIsSameArea());
            if (form.getDepotScheduleId() != null) {
                transOrder.setDepotScheduleAddress(form.getDepotScheduleAddress());
            }
            transOrder.setDepotScheduleId(form.getDepotScheduleId());
            transOrder.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBO));//调拨订单编号

            transOrder.setMerchantId(user.getMerchantId());//商家id
            transOrder.setMerchantName(user.getMerchantName());//商家名称
            transOrder.setTopidealCode(user.getTopidealCode());//卓志编码
        }

        //设置业务类型和服务类型
        if (actionTypeEnum == ActionTypeEnum.ADD) {
            setModelAndServeTypes(outDepot, inDepot, transOrder, inTopidealCode, transOrder.getTopidealCode(), null);
            transOrder.setStatus(DERP_ORDER.TRANSFERORDER_STATUS_013);//待提交
            transOrder.setTallyingUnit(form.getTallyingUnit());//理货单位
            transOrder.setCreater(user.getId());//创建人
            transOrder.setCreateUsername(user.getName());//创建人名称
            transOrder.setCreateDate(TimeUtils.getNow());
        } else {
            setModelAndServeTypes(outDepot, inDepot, transOrder, inTopidealCode, transOrder.getTopidealCode(), transOrder.getId());
            transOrder.setTallyingUnit(form.getTallyingUnit() == null ? " " : form.getTallyingUnit());//理货单位
        }

        if (form.getInCustomerId() != null) {
            transOrder.setInCustomerId(form.getInCustomerId());//客户id
            transOrder.setInCustomerName(form.getInCustomerName());//客户名称
        }

        transOrder.setInvoiceNo(form.getInvoiceNo());//发票号
        transOrder.setPackType(form.getPackType());//包装方式
        transOrder.setCartons(form.getCartons().trim());//箱数
        transOrder.setFirstLadingBillNo(form.getFirstLadingBillNo());//头程单号
        transOrder.setReceivingAddress(form.getReceivingAddress());//收货地址
        transOrder.setDestination(form.getDestination());//目的地
        transOrder.setConsigneeUsername(form.getConsigneeUsername());//收货人姓名
        transOrder.setCountry(form.getCountry());//国家
        transOrder.setMark(form.getMark());//唛头
        transOrder.setShipper(form.getShipper());//境外发货人
        transOrder.setModifier(user.getId());//修改人
        transOrder.setModifierUsername(user.getName());//修改人名称
        transOrder.setModifyDate(TimeUtils.getNow());//修改时间
        transOrder.setBuId(form.getBuId());
        transOrder.setBuName(merchantBuRelMongo.getBuName());
        transOrder.setBillWeight(form.getBillWeight());
        transOrder.setTrainNumber(form.getTrainNumber()); //车次
        transOrder.setStandardCaseTeu(form.getStandardCaseTeu()); //标准箱TEU
        transOrder.setTorrNum(form.getTorrNum()); //托数
        transOrder.setCarrier(form.getCarrier()); //承运商
        transOrder.setOutdepotAddr(form.getOutdepotAddr()); //出库地点
        transOrder.setTransport(form.getTransport()); //运输方式
        transOrder.setPackType(form.getPackType());
        transOrder.setPalletMaterial(form.getPalletMaterial());
        transOrder.setPortLoading(form.getPortLoading());
        transOrder.setUnloadPort(form.getUnloadPort());
        transOrder.setPayRules(form.getPayRules());

        if (form.getOutCustomsId() != null) {
            Map<String, Object> outDepotCustomsRelMap = new HashMap<String, Object>();
            outDepotCustomsRelMap.put("depotId", form.getOutDepotId());
            outDepotCustomsRelMap.put("customsAreaId", form.getOutCustomsId());
            DepotCustomsRelMongo outDepotCustomsRelMongo = depotCustomsRelMongoDao.findOne(outDepotCustomsRelMap);//平台关区信息
            if (outDepotCustomsRelMongo == null) {
                resultMap.put("code", "01");
                resultMap.put("message", "保存失败，出仓仓库：" + outDepot.getName() + " 未关联选中平台关区");
                return resultMap;
            }
            transOrder.setOutCustomsId(form.getOutCustomsId());
            transOrder.setOutCustomsCode(outDepotCustomsRelMongo.getCustomsAreaCode());
            transOrder.setOutPlatformCustoms(outDepotCustomsRelMongo.getCustomsAreaName());
        }

        if (form.getInCustomsId() != null) {
            Map<String, Object> inDepotCustomsRelMap = new HashMap<String, Object>();
            inDepotCustomsRelMap.put("depotId", form.getInDepotId());
            inDepotCustomsRelMap.put("customsAreaId", form.getInCustomsId());
            DepotCustomsRelMongo inDepotCustomsRelMongo = depotCustomsRelMongoDao.findOne(inDepotCustomsRelMap);//平台关区信息
            if (inDepotCustomsRelMongo == null) {
                resultMap.put("code", "01");
                resultMap.put("message", "保存失败，入仓仓库：" + outDepot.getName() + " 未关联选中平台关区");
                return resultMap;
            }
            transOrder.setInCustomsId(form.getInCustomsId());
            transOrder.setInCustomsCode(inDepotCustomsRelMongo.getCustomsAreaCode());
            transOrder.setInPlatformCustoms(inDepotCustomsRelMongo.getCustomsAreaName());
        }

        if (form.getOutCustomsId() == null) {
            transOrder.setOutCustomsId(null);
            transOrder.setOutCustomsCode(null);
            transOrder.setOutPlatformCustoms(null);
        }

        if (form.getInCustomsId() == null) {
            transOrder.setInCustomsId(null);
            transOrder.setInCustomsCode(null);
            transOrder.setInPlatformCustoms(null);
        }

        return resultMap;
    }

    private Map<String, Object> convertTransferOrderItemModelList(List<Map<String, Object>> goodsList,
                                                                  User user, TransferOrderModel transOrder,
                                                                  ActionTypeEnum actionTypeEnum, List<TransferOrderItemModel> orderItemList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (goodsList == null) {
            return null;
        }
        for (int i = 0; i < goodsList.size(); i++) {
            TransferOrderItemModel orderItem = new TransferOrderItemModel();
            Map<String, Object> goodMap = goodsList.get(i);
            Integer seq = i + 1;
            if (goodMap.containsKey("seq") && !StringUtils.isEmpty((String) goodMap.get("seq"))) {
                seq = Integer.valueOf((String) goodMap.get("seq"));
            }
            Long outGoodsId = Long.valueOf((String) goodMap.get("outGoodsId"));
            String outUnit = (String) goodMap.get("outUnit");//调出单位
            Integer transferNum = Integer.valueOf((String) goodMap.get("transferNum"));//调出数量
            Long inGoodsId = Long.valueOf((String) goodMap.get("inGoodsId"));
            String inUnit = (String) goodMap.get("inUnit");//调入单位
            Integer inTransferNum = Integer.valueOf((String) goodMap.get("inTransferNum"));//调入数量
            String brandName = (String) goodMap.get("brandName");//品牌类型
            String price = (String) goodMap.get("price");//采购单价
            String grossWeight = (String) goodMap.get("grossWeight");//毛重
            String grossWeightSum = (String) goodMap.get("grossWeightSum");//毛重
            String netWeight = (String) goodMap.get("netWeight");//净重
            String netWeightSum = (String) goodMap.get("netWeightSum");//净重
            String contNo = (String) goodMap.get("contNo");//箱号
            String bargainno = (String) goodMap.get("bargainno");//合同号
            String outBarcode = (String) goodMap.get("outBarcode");//条形码
            String boxNumStr = (String) goodMap.get("boxNum");
            if (!StringUtils.isEmpty(boxNumStr)) {
                Integer boxNum = Integer.valueOf(boxNumStr);//箱数
                orderItem.setBoxNum(boxNum);
            } else {
                orderItem.setBoxNum(null);
            }
            String torrNo = (String) goodMap.get("torrNo");//托盘号

            //查询商品
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("merchandiseId", outGoodsId);
            MerchandiseInfoMogo outMerchandise = merchandiseInfoMogoDao.findOne(paramMap);
            paramMap.put("merchandiseId", inGoodsId);
            MerchandiseInfoMogo inMerchandise = merchandiseInfoMogoDao.findOne(paramMap);
            if (outMerchandise == null || inMerchandise == null) {
                resultMap.put("code", "01");
                resultMap.put("message", "商品不存在保存失败");
                return resultMap;
            }

            orderItem.setSeq(seq);
            orderItem.setOutGoodsId(outGoodsId);//调出商品id
            orderItem.setOutGoodsName(outMerchandise.getName());//调出商品名称
            orderItem.setOutGoodsCode(outMerchandise.getGoodsCode());//调出商品编码
            orderItem.setOutGoodsNo(outMerchandise.getGoodsNo());//调出商品货号
            orderItem.setOutCommbarcode(outMerchandise.getCommbarcode());//调出商品标准条码
            orderItem.setOutUnit(outUnit);//调出单位
            orderItem.setTransferNum(transferNum);//调出数量
            orderItem.setInGoodsId(inGoodsId);//调入商品id
            orderItem.setInGoodsName(inMerchandise.getName());//调出商品名称
            orderItem.setInGoodsCode(inMerchandise.getGoodsCode());//调出商品编码
            orderItem.setInGoodsNo(inMerchandise.getGoodsNo());//调出商品货号
            orderItem.setInCommbarcode(inMerchandise.getCommbarcode()); //调入商品标准条码
            orderItem.setInUnit(inUnit);//调入单位
            orderItem.setInTransferNum(inTransferNum);//调入数量
            orderItem.setBrandName(brandName);//品牌类型
            orderItem.setTorrNo(torrNo);

            Double priced = 0.00;
            if (!StringUtils.isEmpty(price)) {
                priced = Double.valueOf(price);
            }
            orderItem.setPrice(priced);//采购单价
            if (!StringUtils.isEmpty(grossWeight)) {
                orderItem.setGrossWeight(Double.valueOf(grossWeight));
            }//毛重
            if (!StringUtils.isEmpty(grossWeightSum)) {
                orderItem.setGrossWeightSum(Double.valueOf(grossWeightSum));
            }//毛重
            if (!StringUtils.isEmpty(netWeight)) {
                orderItem.setNetWeight(Double.valueOf(netWeight));
            }//净重
            if (!StringUtils.isEmpty(netWeightSum)) {
                orderItem.setNetWeightSum(Double.valueOf(netWeightSum));
            }//总净重
            orderItem.setContNo(contNo);//箱号
            orderItem.setBargainno(bargainno);//合同号
            orderItem.setOutBarcode(outMerchandise.getBarcode());
            orderItem.setInBarcode(inMerchandise.getBarcode());
            orderItem.setCreater(user.getId());//创建人
            orderItem.setCreateDate(TimeUtils.getNow());//创建时间
            orderItemList.add(orderItem);
        }
        return resultMap;
    }

    /**
     * 检查不同场景必填字段
     **/
    public Map<String, Object> checkForAdd(TransferOrderFrom model, User user) throws SQLException {

        /*******************************公共必填参数检查start*******************************/
        //检查调出和调入仓库
        if (model.getOutDepotId() == null || model.getOutDepotId().longValue() <= 0) {
            return getMap("01", "请选择调出仓库");
        }
        if (model.getInDepotId() == null || model.getInDepotId().longValue() <= 0) {
            return getMap("01", "请选择调入仓库");
        }
        if (StringUtils.isEmpty(model.getCartons())) {
            return getMap("01", "请选择箱数");
        }

        if (StringUtils.isEmpty(model.getPalletMaterial())) {
            return getMap("01", "请选择托盘材质");
        }

        if (StringUtils.isEmpty(model.getPackType())) {
            return getMap("01", "请选择包装方式");
        }

        if (StringUtils.isEmpty(model.getUnloadPort())) {
            return getMap("01", "请选择卸货港");
        }

        //检查商品数量
        List<Map<String, Object>> goodsList = model.getGoodsList();
        if (goodsList == null || goodsList.size() <= 0) {
            return getMap("01", "请选择商品");
        }
        for (Map<String, Object> goodMap : goodsList) {
            Integer transferNum = Integer.valueOf((String) goodMap.get("transferNum"));//数量
            if (transferNum == null || transferNum.intValue() <= 0) {
                return getMap("01", "请输入调出数量");
            }
            Integer inTransferNum = Integer.valueOf((String) goodMap.get("inTransferNum"));//数量
            if (inTransferNum == null || inTransferNum.intValue() <= 0) {
                return getMap("01", "请输入调入数量");
            }
        }
        /*******************************公共必填参数检查end*********************************/
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("depotId", model.getOutDepotId());
        DepotInfoMongo outDepot = depotInfoMongoDao.findOne(param);
        param.put("depotId", model.getInDepotId());
        DepotInfoMongo inDepot = depotInfoMongoDao.findOne(param);

        Map<String, Object> relParam = new HashMap<>();
        relParam.put("merchantId", user.getMerchantId());
        relParam.put("depotId", outDepot.getDepotId());
        DepotMerchantRelMongo outDepotMerchantRel = depotMerchantRelMongoDao.findOne(relParam);
        relParam.put("depotId", inDepot.getDepotId());
        DepotMerchantRelMongo inDepotMerchantRel = depotMerchantRelMongoDao.findOne(relParam);

        //如果出入库仓需要下推指令 是否同关区必填
        if ((outDepotMerchantRel.getIsInOutInstruction().equals(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1)
                || inDepotMerchantRel.getIsInOutInstruction().equals(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1)) ||
                (DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType()) && DERP_SYS.DEPOTINFO_TYPE_A.equals(inDepot.getType()))) {
            if (StringUtils.isEmpty(model.getIsSameArea())) {
                return getMap("01", "请选择是否同关区");
            }
        }

        if (inDepotMerchantRel.getIsInOutInstruction().equals(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1)) {
            if (StringUtils.isEmpty(model.getPortLoading())) {
                return getMap("01", "装货港不能为空");
            }
        }

        //入仓仓库是备查仓时，调入仓地址必填
        if (DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
            if (model.getDepotScheduleId() == null) {
                return getMap("01", "请选择调入仓地址");
            }
        }

        //唯品代销仓: po号
        if ("VIP001".equals(inDepot.getDepotCode()) || "VIP001".equals(outDepot.getDepotCode())) {
            if (StringUtils.isEmpty(model.getPoNo())) {
                return getMap("01", "请输入PO单号");
            }
        }

        //如果涉及海外仓理货单位必填
        if (inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C) || outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
            //理货单位
            if (StringUtils.isEmpty(model.getTallyingUnit())) {
                return getMap("01", "请选择理货单位");
            }
        } else if (!StringUtils.isEmpty(model.getTallyingUnit())) {
            model.setTallyingUnit(" ");
        }
        //如果调出仓不是海外仓
        if (!outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
            //目的地
            if (!StringUtils.isEmpty(model.getDestination())) {
                model.setDestination(" ");
            }
        }

        //调出仓库为保税仓/海外仓且需下推调拨指令接口的，仓库海关编码、仓库国检编码不能为空
        if (outDepot.getType().matches(DERP_SYS.DEPOTINFO_TYPE_A + "|" + DERP_SYS.DEPOTINFO_TYPE_C) &&
                DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(outDepotMerchantRel.getIsInOutInstruction())) {
            //申报地海关
            if (StringUtils.isEmpty(outDepot.getCustomsNo())) {
                return getMap("01", "请完善调出仓库海关编码");
            }
            //申报地国检
            if (StringUtils.isEmpty(outDepot.getInspectNo())) {
                return getMap("01", "请完善调出仓库国检编码");
            }
        }

        boolean isSame = !StringUtils.isEmpty(model.getIsSameArea()) && DERP.ISSAMEAREA_1.equals(model.getIsSameArea());
        boolean isNotSame = !StringUtils.isEmpty(model.getIsSameArea()) && DERP.ISSAMEAREA_0.equals(model.getIsSameArea());
        boolean isOutStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(outDepotMerchantRel.getIsInOutInstruction());
        boolean isInStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(inDepotMerchantRel.getIsInOutInstruction());
        String outDepotType = outDepot.getType();
        String inDepotType = inDepot.getType();

        //1、调出仓库类型保税仓，调入仓库类型保税仓，同关区且下推接口指令都为是: 调入客户、合同号
        if (outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
                && isSame && isOutStruction && isInStruction) {
            //调入客户
            if (model.getInCustomerId() == null || model.getInCustomerId().longValue() == 0) {
                return getMap("01", "请选择调入客户");
            }

            //表头合同号
            if (StringUtils.isEmpty(model.getContractNo())) {
                return getMap("01", "请输入合同号");
            }

        }

        //2、1) 调出仓库类型保税仓，调入仓库类型保税仓，同关区且调出接口指令为否，调入接口指令为是
        //   2) 调出仓库类型保税仓，调入仓库类型保税仓，跨关区:调入客户,合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格
        else if (inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A) && outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
                && ((isSame && isInStruction && !isOutStruction) || isNotSame)) {
            //调入客户
            if (model.getInCustomerId() == null || model.getInCustomerId().longValue() == 0) {
                return getMap("01", "请选择调入客户");
            }
            //表头合同号
            if (StringUtils.isEmpty(model.getContractNo())) {
                return getMap("01", "请输入合同号");
            }
            //发票号
            if (StringUtils.isEmpty(model.getInvoiceNo())) {
                return getMap("01", "请输入发票号");
            }

            //收货地址
            if (StringUtils.isEmpty(model.getReceivingAddress())) {
                return getMap("01", "请输入收货地址");
            }
            //包装方式
            if (StringUtils.isEmpty(model.getPackType())) {
                return getMap("01", "请输包装方式");
            }
            //箱数
            if (StringUtils.isEmpty(model.getCartons())) {
                return getMap("01", "请输箱数");
            }
            //头程提单号
            if (StringUtils.isEmpty(model.getFirstLadingBillNo())) {
                return getMap("01", "请输头程提单号");
            }

            //商品
            for (Map<String, Object> goodMap : goodsList) {
                //调拨单价
                if (StringUtils.isEmpty(goodMap.get("price"))) {
                    return getMap("01", "请输入调拨单价");
                }
            }
        }

        //3、调出仓库类型保税仓，调入仓库类型保税仓，同关区，调入接口指令为否：调入客户、合同号
        else if (inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
                && outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
                && isSame && !isInStruction) {
            //调入客户
            if (model.getInCustomerId() == null || model.getInCustomerId().longValue() == 0) {
                return getMap("01", "请选择调入客户");
            }
            //表头合同号
            if (StringUtils.isEmpty(model.getContractNo())) {
                return getMap("01", "请输入合同号");
            }
        }
        //调出仓库类型是海外仓 : 合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格,收货人姓名,国家,目的地,海外理货单位，调入客户
        else if (outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
            //调入客户
            if (model.getInCustomerId() == null || model.getInCustomerId().longValue() == 0) {
                return getMap("01", "请选择调入客户");
            }
            //表头合同号
            if (StringUtils.isEmpty(model.getContractNo())) {
                return getMap("01", "请输入合同号");
            }
            //发票号
            if (StringUtils.isEmpty(model.getInvoiceNo())) {
                return getMap("01", "请输入发票号");
            }

            //收货地址
            if (StringUtils.isEmpty(model.getReceivingAddress())) {
                return getMap("01", "请输入收货地址");
            }
            //包装方式
            if (StringUtils.isEmpty(model.getPackType())) {
                return getMap("01", "请输包装方式");
            }
            //箱数
            if (StringUtils.isEmpty(model.getCartons())) {
                return getMap("01", "请输箱数");
            }
            //头程提单号
            if (StringUtils.isEmpty(model.getFirstLadingBillNo())) {
                return getMap("01", "请输头程提单号");
            }
            if (StringUtils.isEmpty(model.getDestination())) {
                return getMap("01", "请输入目的地");
            }
            if (StringUtils.isEmpty(model.getConsigneeUsername())) {
                return getMap("01", "请输入收货人姓名");
            }
            if (StringUtils.isEmpty(model.getCountry())) {
                return getMap("01", "请输入国家");
            }

            //商品
            for (Map<String, Object> goodMap : goodsList) {
                //调拨单价
                if (StringUtils.isEmpty(goodMap.get("price"))) {
                    return getMap("01", "请输入调拨单价");
                }
            }

        }

        //调入仓库类型海外仓/保税仓调备查库【同\跨关区】 : 合同号、海外理货单位、调入客户
        else if (inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_C) ||
                (!StringUtils.isEmpty(model.getIsSameArea()) && DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepotType)
                        && DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepotType))) {
            //调入客户
            if (model.getInCustomerId() == null || model.getInCustomerId().longValue() == 0) {
                return getMap("01", "请选择调入客户");
            }
            //表头合同号
            if (StringUtils.isEmpty(model.getContractNo())) {
                return getMap("01", "请输入合同号");
            }
        }

        //8、调出仓库类型备查库或暂存区：调入客户、合同号、价格
        else if (outDepotType.matches(DERP_SYS.DEPOTINFO_TYPE_B + "|" + DERP_SYS.DEPOTINFO_TYPE_E)) {
            //调入客户
            if (model.getInCustomerId() == null || model.getInCustomerId().longValue() == 0) {
                return getMap("01", "请选择调入客户");
            }
            //表头合同号
            if (StringUtils.isEmpty(model.getContractNo())) {
                return getMap("01", "请输入合同号");
            }
            //商品
            for (Map<String, Object> goodMap : goodsList) {
                //调拨单价
                if (StringUtils.isEmpty(goodMap.get("price"))) {
                    return getMap("01", "请输入调拨单价");
                }
            }
        }

        //9.调入客户、箱数、运输方式、托数、托盘材质、出库地点、收货地址、装货港、卸货港
        else if (outDepotType.matches(DERP_SYS.DEPOTINFO_TYPE_D)) {
            //调入客户
            if (model.getInCustomerId() == null || model.getInCustomerId().longValue() == 0) {
                return getMap("01", "请选择调入客户");
            }
            //箱数
            if (StringUtils.isEmpty(model.getCartons())) {
                return getMap("01", "请输入箱数");
            }

            //运输方式
            if (StringUtils.isEmpty(model.getTransport())) {
                return getMap("01", "请输入运输方式");
            }

            //托数
            if (StringUtils.isEmpty(model.getTorrNum())) {
                return getMap("01", "请输入托数");
            }

            //托盘材质
            if (StringUtils.isEmpty(model.getPalletMaterial())) {
                return getMap("01", "请输入托盘材质");
            }

            //出库地点
            if (StringUtils.isEmpty(model.getOutdepotAddr())) {
                return getMap("01", "请输入出库地点");
            }

            //收货地址
            if (StringUtils.isEmpty(model.getReceivingAddress())) {
                return getMap("01", "请输入收货地址");
            }
            //装货港
            if (StringUtils.isEmpty(model.getPortLoading())) {
                return getMap("01", "请输入装货港");
            }

            //卸货港
            if (StringUtils.isEmpty(model.getUnloadPort())) {
                return getMap("01", "请输入卸货港");
            }
            //商品
            for (Map<String, Object> goodMap : goodsList) {
                //调拨单价
                if (StringUtils.isEmpty(goodMap.get("price"))) {
                    return getMap("01", "请输入调拨单价");
                }
            }
        }
        if (isInStruction) {
            if (StringUtils.isEmpty(model.getPortLoading())) {
                return getMap("01", "请输入装货港");
            }
        }

        return getMap("00", "检查通过");
    }


    public Map<String, Object> getMap(String code, String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("message", message);
        return map;
    }

    /**
     * 调入调出流水列表（分页）
     *
     * @param model
     * @return
     */
    @Override
    public TransferOutInBean listTransferOutInPage(TransferOutInBean model, User user) throws SQLException {
        List<Long> userBuIds = userBuRelMongoDao.getBuListByUser(user.getId());
        if (userBuIds.isEmpty()) {
            model.setTotal(0);
            model.setList(new ArrayList());
            return model;
        }

        model.setUserBuList(userBuIds);
        //查询调拨单
        TransferOutInBean outInBean = transferOrderDao.listTransferOutInPage(model);
        List<TransferOutInBean> transferOrderList = outInBean.getList();

        List<TransferOutInBean> outInListAll = new ArrayList<TransferOutInBean>();//存储拼接好的流水
        if (transferOrderList != null && transferOrderList.size() > 0) {
            for (TransferOutInBean bean : transferOrderList) {
                List<TransferOutInBean> oneOrderOutInList = new ArrayList<TransferOutInBean>();//存储拼接好的流水
                //获取调单表体
                getTransferItem(bean, oneOrderOutInList);
                //拼接调拨出库单、调拨入库单数据
                getItem(bean, oneOrderOutInList);

                outInListAll.addAll(oneOrderOutInList);
            }
        }
        outInBean.setList(null);
        outInBean.setList(outInListAll);
        return outInBean;
    }

    /**
     * 拼接调拨表体
     *
     * @throws SQLException
     **/
    public void getTransferItem(TransferOutInBean bean, List<TransferOutInBean> outInList) throws SQLException {
        //查询调拨单表体并拼接上去
        TransferOrderItemModel itemModel = new TransferOrderItemModel();
        itemModel.setTransferOrderId(bean.getId());
        List<TransferOrderItemModel> itemList = transferOrderItemDao.list(itemModel);
        if (itemList != null && itemList.size() > 0) {
            for (TransferOrderItemModel item : itemList) {
                TransferOutInBean entity = new TransferOutInBean();
                entity.setId(bean.getId());
                entity.setCode(bean.getCode());
                entity.setOutDepotName(bean.getOutDepotName());
                entity.setInDepotName(bean.getInDepotName());
                entity.setBuId(bean.getBuId());
                entity.setBuName(bean.getBuName());
                entity.setInGoodsId(item.getInGoodsId());
                entity.setInGoodsNo(item.getInGoodsNo());
                entity.setInGoodsName(item.getInGoodsName());
                entity.setOutGoodsId(item.getOutGoodsId());
                entity.setOutGoodsNo(item.getOutGoodsNo());
                entity.setOutGoodsName(item.getOutGoodsName());
                entity.setTransferNum(item.getTransferNum());//调出数量
                entity.setTallyingUnit(item.getOutUnit());//调出单位
                entity.setOrderInTransferNum(item.getInTransferNum());//调入数量
                entity.setInTallyingUnit(item.getInUnit());//调入单位
                outInList.add(entity);
            }
        }
    }

    /**
     * 拼接调拨出库单表体
     **/
    public void getItem(TransferOutInBean bean, List<TransferOutInBean> oneOrderOutInList) {

        //查询调拨出库单表体
        List<Map<String, Object>> outList = transferOutDepotItemDao.getItemListByTransferId(bean.getId());
        //拼接调拨出库数据
        if (outList != null && outList.size() > 0) {
            //拼接调拨出库数据
            for (Map outMap : outList) {
                Long outDepotOutGoodsId = (Long) outMap.get("out_goods_id");
                boolean flag = true;//调拨单里不存在商品
                for (TransferOutInBean orderItemBean : oneOrderOutInList) {
                    if (orderItemBean.getOutGoodsId() != null && orderItemBean.getOutGoodsId().longValue() == outDepotOutGoodsId.longValue()) {
                        if (StringUtils.isEmpty(orderItemBean.getOutDepotCode())) {//未匹配过
                            orderItemBean.setBuId(bean.getBuId());
                            orderItemBean.setBuName(bean.getBuName());
                            orderItemBean.setOutDepotCode((String) outMap.get("outdepotcode"));
                            orderItemBean.setOutGoodsNoItem((String) outMap.get("out_goods_no"));
                            orderItemBean.setOutGoodsNameItem((String) outMap.get("out_goods_name"));
                            orderItemBean.setOutTransferBatchNo((String) outMap.get("out_transfer_batch_no"));
                            orderItemBean.setOutTransferNum((Integer) outMap.get("out_transfer_num"));
                            orderItemBean.setOutWornNum((Integer) outMap.get("out_worn_num"));
                            orderItemBean.setOutExpireNum((Integer) outMap.get("out_expire_num"));
                            int outTransferNumAll = orderItemBean.getOutTransferNum() + orderItemBean.getOutWornNum() + orderItemBean.getOutExpireNum();
                            orderItemBean.setOutTransferNumAll(outTransferNumAll);
                        } else {//一个商品多个批次的情况
                            TransferOutInBean neworderItem = new TransferOutInBean();
                            neworderItem.setBuId(bean.getBuId());
                            neworderItem.setBuName(bean.getBuName());
                            neworderItem.setCode(bean.getCode());
                            neworderItem.setOutDepotName(bean.getOutDepotName());
                            neworderItem.setInDepotName(bean.getInDepotName());
                            neworderItem.setTallyingUnit(orderItemBean.getTallyingUnit());
                            neworderItem.setInTallyingUnit(orderItemBean.getInTallyingUnit());

                            neworderItem.setInGoodsId(orderItemBean.getInGoodsId());
                            neworderItem.setInGoodsNo(orderItemBean.getInGoodsNo());
                            neworderItem.setInGoodsName(orderItemBean.getInGoodsName());
                            neworderItem.setOutGoodsId(orderItemBean.getOutGoodsId());
                            neworderItem.setOutGoodsNo(orderItemBean.getOutGoodsNo());
                            neworderItem.setOutGoodsName(orderItemBean.getOutGoodsName());

                            neworderItem.setOutDepotCode((String) outMap.get("outdepotcode"));
                            neworderItem.setOutGoodsNoItem((String) outMap.get("out_goods_no"));
                            neworderItem.setOutGoodsNameItem((String) outMap.get("out_goods_name"));
                            neworderItem.setOutTransferBatchNo((String) outMap.get("out_transfer_batch_no"));
                            neworderItem.setOutTransferNum((Integer) outMap.get("out_transfer_num"));
                            neworderItem.setOutWornNum((Integer) outMap.get("out_worn_num"));
                            neworderItem.setOutExpireNum((Integer) outMap.get("out_expire_num"));
                            int outTransferNumAll = neworderItem.getOutTransferNum() + neworderItem.getOutWornNum() + neworderItem.getOutExpireNum();
                            neworderItem.setOutTransferNumAll(outTransferNumAll);
                            oneOrderOutInList.add(neworderItem);
                        }
                        flag = false;
                        break;
                    }
                }
                if (flag == true) {//调拨单里不存在的商品
                    TransferOutInBean entity = new TransferOutInBean();
                    entity.setCode(bean.getCode());
                    entity.setOutDepotName(bean.getOutDepotName());
                    entity.setInDepotName(bean.getInDepotName());
                    entity.setBuId(bean.getBuId());
                    entity.setBuName(bean.getBuName());
                    entity.setOutDepotCode((String) outMap.get("outdepotcode"));
                    entity.setOutGoodsNoItem((String) outMap.get("out_goods_no"));
                    entity.setOutGoodsNameItem((String) outMap.get("out_goods_name"));
                    entity.setOutTransferBatchNo((String) outMap.get("out_transfer_batch_no"));
                    entity.setOutTransferNum((Integer) outMap.get("out_transfer_num"));
                    entity.setOutWornNum((Integer) outMap.get("out_worn_num"));
                    entity.setOutExpireNum((Integer) outMap.get("out_expire_num"));
                    int outTransferNumAll = entity.getOutTransferNum() + entity.getOutWornNum() + entity.getOutExpireNum();
                    entity.setOutTransferNumAll(outTransferNumAll);
                    oneOrderOutInList.add(entity);
                }
            }
        }

        //查询调拨入库单表体
        List<Map<String, Object>> inList = transferInDepotItemDao.getInItemListByTransferId(bean.getId());

        //拼接调拨入库数据
        if (inList != null && inList.size() > 0) {
            //拼接调拨出库数据
            for (Map inMap : inList) {
                Long inDepotInGoodsId = (Long) inMap.get("in_goods_id");
                boolean flag = true;//调拨单里不存在的商品
                for (TransferOutInBean orderItemBean : oneOrderOutInList) {
                    if (orderItemBean.getInGoodsId() != null && orderItemBean.getInGoodsId().longValue() == inDepotInGoodsId.longValue()) {
                        if (StringUtils.isEmpty(orderItemBean.getInDepotCode())) {//未匹配过
                            orderItemBean.setBuId(bean.getBuId());
                            orderItemBean.setBuName(bean.getBuName());
                            orderItemBean.setInDepotCode((String) inMap.get("indepotcode"));
                            orderItemBean.setInGoodsNoItem((String) inMap.get("in_goods_no"));
                            orderItemBean.setInGoodsNameItem((String) inMap.get("in_goods_name"));
                            orderItemBean.setInTransferBatchNo((String) inMap.get("in_transfer_batch_no"));
                            orderItemBean.setInTransferNum((Integer) inMap.get("in_transfer_num"));
                            orderItemBean.setWornNum((Integer) inMap.get("worn_num"));
                            orderItemBean.setExpireNum((Integer) inMap.get("expire_num"));
                            int inTransferNumAll = orderItemBean.getInTransferNum() + orderItemBean.getWornNum() + orderItemBean.getExpireNum();
                            orderItemBean.setInTransferNumAll(inTransferNumAll);
                        } else {//一个商品多个批次的情况
                            TransferOutInBean neworderItem = new TransferOutInBean();
                            neworderItem.setCode(bean.getCode());
                            neworderItem.setBuId(bean.getBuId());
                            neworderItem.setBuName(bean.getBuName());
                            neworderItem.setOutDepotName(bean.getOutDepotName());
                            neworderItem.setInDepotName(bean.getInDepotName());
                            neworderItem.setTallyingUnit(orderItemBean.getTallyingUnit());
                            neworderItem.setInTallyingUnit(orderItemBean.getInTallyingUnit());

                            neworderItem.setInGoodsId(orderItemBean.getInGoodsId());
                            neworderItem.setInGoodsNo(orderItemBean.getInGoodsNo());
                            neworderItem.setInGoodsName(orderItemBean.getInGoodsName());
                            neworderItem.setOutGoodsId(orderItemBean.getOutGoodsId());
                            neworderItem.setOutGoodsNo(orderItemBean.getOutGoodsNo());
                            neworderItem.setOutGoodsName(orderItemBean.getOutGoodsName());

                            neworderItem.setInDepotCode((String) inMap.get("indepotcode"));
                            neworderItem.setInGoodsNoItem((String) inMap.get("in_goods_no"));
                            neworderItem.setInGoodsNameItem((String) inMap.get("in_goods_name"));
                            neworderItem.setInTransferBatchNo((String) inMap.get("in_transfer_batch_no"));
                            neworderItem.setInTransferNum((Integer) inMap.get("in_transfer_num"));
                            neworderItem.setWornNum((Integer) inMap.get("worn_num"));
                            neworderItem.setExpireNum((Integer) inMap.get("expire_num"));
                            int inTransferNumAll = neworderItem.getInTransferNum() + neworderItem.getWornNum() + neworderItem.getExpireNum();
                            neworderItem.setInTransferNumAll(inTransferNumAll);
                            oneOrderOutInList.add(neworderItem);
                        }
                        flag = false;
                        break;
                    }
                }
                if (flag == true) {//调拨单里不存在的商品
                    TransferOutInBean entity = new TransferOutInBean();
                    entity.setCode(bean.getCode());
                    entity.setOutDepotName(bean.getOutDepotName());
                    entity.setInDepotName(bean.getInDepotName());
                    entity.setBuId(bean.getBuId());
                    entity.setBuName(bean.getBuName());
                    entity.setInDepotCode((String) inMap.get("indepotcode"));
                    entity.setInGoodsNoItem((String) inMap.get("in_goods_no"));
                    entity.setInGoodsNameItem((String) inMap.get("in_goods_name"));
                    entity.setInTransferBatchNo((String) inMap.get("in_transfer_batch_no"));
                    entity.setInTransferNum((Integer) inMap.get("in_transfer_num"));
                    entity.setWornNum((Integer) inMap.get("worn_num"));
                    entity.setExpireNum((Integer) inMap.get("expire_num"));
                    Integer inTransferNumAll = entity.getInTransferNum() + entity.getWornNum() + entity.getExpireNum();
                    entity.setInTransferNumAll(inTransferNumAll);
                    oneOrderOutInList.add(entity);
                }
            }
        }
    }

    /**
     * 保存导入调拨单
     *
     * @param data 数据
     * @throws Exception
     **/
    @Override
    public Map<String, Object> saveImportTransfer(Long userId, String name, Long merchantId, String merchantName, String userTopidealCode,
                                                  String relMerchantIds, List<List<Map<String, String>>> data) throws Exception {
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();

        /***********************************检查组装调拨单数据start************************/
        List<TransferOrderModel> transferOrderList = new ArrayList<TransferOrderModel>();//检查通过的调拨单
        Map<String, Map<String, DepotInfoMongo>> sqeDepotMap = new HashMap<String, Map<String, DepotInfoMongo>>();
        Map<String, DepotInfoMongo> depotMap = null;//仓库
        for (int i = 0; i < data.get(0).size(); i++) {
            Map<String, String> transferOrder = data.get(0).get(i);
            String zcode = transferOrder.get("自编调拨单号");//自编调拨单号
            String customerCode = transferOrder.get("调入公司卓志编码");//调入商家卓志编码
            String outDepotCode = transferOrder.get("调出仓库自编码");//调出仓库自编码
            String inDepotCode = transferOrder.get("调入仓库自编码");//调入仓库自编码
            String buCode = transferOrder.get("事业部");//事业部编码
            String stockLocationType = transferOrder.get("库位类型");//库位类型
            String contractNo = transferOrder.get("合同号");//合同号
            String invoiceNo = transferOrder.get("发票号");//发票号
            String receivingAddress = transferOrder.get("收货地址");//收货地址
            String packType = transferOrder.get("包装方式");//包装方式
            String cartons = transferOrder.get("箱数");//箱数
            String firstLadingBillNo = transferOrder.get("头程提单号");//头程提单号

            String tallyingUnit = transferOrder.get("理货单位");//海外仓理货单位
            String destination = transferOrder.get("目的地编码");//目的地编码
            String consigneeUsername = transferOrder.get("收货人姓名");//收货人姓名
            String country = transferOrder.get("国家");//国家
            String shipper = transferOrder.get("境外发货人");//境外发货人
            String mark = transferOrder.get("唛头");//唛头
            String lbxNo = transferOrder.get("LBX单号");//LBX单号
            String remark = transferOrder.get("备注");//备注
            String model = "";//业务场景
            String serveTypes = "";//服务类型
            String isSameArea = transferOrder.get("是否同关区");//是否同关区
            String depotScheduleAddress = transferOrder.get("调入仓地址");//调入仓地址
            String poNo = transferOrder.get("PO号");//PO单号
            poNo = poNo.toUpperCase();

            //自编码
            if (StringUtils.isEmpty(zcode)) {
                addErrorList(i, "【基本信息】自编码为空", errorList);
                continue;
            }
            zcode = zcode.trim();

            //事业部
            if (StringUtils.isEmpty(buCode)) {
                addErrorList(i, "【基本信息】事业部为空", errorList);
                continue;
            }
            buCode = buCode.trim();

            //调出仓库
            if (StringUtils.isEmpty(outDepotCode)) {
                addErrorList(i, "【基本信息】调出仓库自编码为空", errorList);
                continue;
            }
            outDepotCode = outDepotCode.trim();
            Map<String, Object> dparamMap = new HashMap<String, Object>();
            dparamMap.put("depotCode", outDepotCode);
            DepotInfoMongo outDepot = depotInfoMongoDao.findOne(dparamMap);
            if (outDepot == null) {
                addErrorList(i, "【基本信息】调出仓库自编码不正确", errorList);
                continue;
            }

            Map<String, Object> relDepotParam = new HashMap<>();
            relDepotParam.put("merchantId", merchantId);
            relDepotParam.put("depotId", outDepot.getDepotId());
            DepotMerchantRelMongo outDepotRel = depotMerchantRelMongoDao.findOne(relDepotParam);
            if (outDepotRel == null) {
                addErrorList(i, "【基本信息】调出仓库自编码在该商家下不存在", errorList);
                continue;
            }

            //调入仓库
            if (StringUtils.isEmpty(inDepotCode)) {
                addErrorList(i, "【基本信息】调入仓库自编码为空", errorList);
                continue;
            }
            inDepotCode = inDepotCode.trim();
            dparamMap.put("depotCode", inDepotCode);
            DepotInfoMongo inDepot = depotInfoMongoDao.findOne(dparamMap);
            if (inDepot == null) {
                addErrorList(i, "【基本信息】调入仓库自编码不正确", errorList);
                continue;
            }
            relDepotParam.put("depotId", inDepot.getDepotId());
            DepotMerchantRelMongo inDepotRel = depotMerchantRelMongoDao.findOne(relDepotParam);
            if (inDepotRel == null) {
                addErrorList(i, "【基本信息】调入仓库自编码在该商家下不存在", errorList);
                continue;
            }

            Map<String, Object> buMap = new HashMap<>();
            buMap.put("buCode", buCode.trim());
            buMap.put("merchantId", merchantId);
            buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
            MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
            if (merchantBuRelMongo == null) {
                addErrorList(i, "【基本信息】事业部在该商家下不存在", errorList);
                continue;
            }

            Map<String, Object> depotBuMap = new HashMap<>();
            depotBuMap.put("buId", merchantBuRelMongo.getBuId());
            depotBuMap.put("merchantId", merchantId);
            depotBuMap.put("depotId", inDepot.getDepotId());
            MerchantDepotBuRelMongo merchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(depotBuMap);
            if (merchantDepotBuRelMongo == null) {
                addErrorList(i, "【基本信息】事业部在该商家调入仓库下不存在", errorList);
                continue;
            }
            depotBuMap.put("depotId", outDepot.getDepotId());
            MerchantDepotBuRelMongo merchantOutDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(depotBuMap);
            if (merchantOutDepotBuRelMongo == null) {
                addErrorList(i, "【基本信息】事业部在该商家调出仓库下不存在", errorList);
                continue;
            }
            boolean isUserRelateBu = userBuRelMongoDao.isUserRelateBu(userId, merchantBuRelMongo.getBuId());
            if (!isUserRelateBu) {
                addErrorList(i, "【基本信息】事业部在该用户下不存在", errorList);
                continue;
            }

            //查询公司事业部是否启用了库位管理
            if (merchantBuRelMongo.getStockLocationManage().equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0) &&
                    org.apache.commons.lang3.StringUtils.isEmpty(stockLocationType)) {
                addErrorList(i, "【基本信息】公司事业部启用库位管理，库位类型不能为空", errorList);
                continue;
            }

            BuStockLocationTypeConfigMongo buStockLocationTypeConfigMongo = null;
            if (merchantBuRelMongo.getStockLocationManage().equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0)) {
                stockLocationType = stockLocationType.trim();
                Map<String, Object> buStockLocationTypeParams = new HashMap<>();
                buStockLocationTypeParams.put("merchantId", merchantId);
                buStockLocationTypeParams.put("buId", merchantBuRelMongo.getBuId());
                buStockLocationTypeParams.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
                buStockLocationTypeParams.put("name", stockLocationType);
                buStockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(buStockLocationTypeParams);

                if (buStockLocationTypeConfigMongo == null) {
					addErrorList(i, "库位类型在公司事业部下不存在", errorList);
                    continue;
                }

            }

            if (!outDepot.getType().matches(DERP_SYS.DEPOTINFO_TYPE_A + "|" + DERP_SYS.DEPOTINFO_TYPE_B + "|"
                    + DERP_SYS.DEPOTINFO_TYPE_C + "|" + DERP_SYS.DEPOTINFO_TYPE_E)) {
                addErrorList(i, "调出仓库不能选销毁区和中转仓，仅能选保税仓、海外仓、备查库、暂存仓这四类仓库", errorList);
                continue;
            }

            if (DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())) {
                if (!inDepot.getType().matches(DERP_SYS.DEPOTINFO_TYPE_A + "|" + DERP_SYS.DEPOTINFO_TYPE_C)) {
                    addErrorList(i, "当调出仓库为保税仓时，调入仓库仅能选海外仓和保税仓", errorList);
                    continue;
                }
            }

            if (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())) {
                if (!DERP_SYS.DEPOTINFO_TYPE_A.equals(inDepot.getType())) {
                    addErrorList(i, "当调出仓库为海外仓时，调入仓库仅能选保税仓", errorList);
                    continue;
                }
            }

            if (DERP_SYS.DEPOTINFO_TYPE_B.equals(outDepot.getType())) {
                if (!DERP_SYS.DEPOTINFO_TYPE_E.equals(inDepot.getType())) {
                    addErrorList(i, "当调出仓库为备查库时，调入仓库仅能选暂存仓", errorList);
                    continue;
                }
            }

            if (DERP_SYS.DEPOTINFO_TYPE_E.equals(outDepot.getType()) && DERP_SYS.DEPOTINFO_ISTOPBOOKS_1.equals(outDepot.getIsTopBooks())) {
                if (!DERP_SYS.DEPOTINFO_TYPE_F.equals(inDepot.getType())) {
                    addErrorList(i, "当调出仓库为暂存仓且标识为代销仓时，调入仓库仅能选销毁区", errorList);
                    continue;
                }
            }

            if (DERP_SYS.DEPOTINFO_TYPE_E.equals(outDepot.getType()) && DERP_SYS.DEPOTINFO_ISTOPBOOKS_0.equals(outDepot.getIsTopBooks())) {
                if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
                    addErrorList(i, "当调出仓库为暂存仓且标识为非代销仓时，调入仓库仅能选备查库", errorList);
                    continue;
                }
            }

            if (DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(outDepotRel.getIsInOutInstruction())
                    || DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(inDepotRel.getIsInOutInstruction())) {
                if (StringUtils.isEmpty(isSameArea)) {
                    addErrorList(i, "【出,入库仓是否进出接口指令为是时,是否同关区必填", errorList);
                    continue;
                }

                //isSameArea是否同关区（0-否，1-是）
                if (DERP.getLabelByKey(DERP.isSameAreaList, DERP.ISSAMEAREA_1).equals(isSameArea)) {
                    isSameArea = DERP.ISSAMEAREA_1;
                } else if (DERP.getLabelByKey(DERP.isSameAreaList, DERP.ISSAMEAREA_0).equals(isSameArea)) {
                    isSameArea = DERP.ISSAMEAREA_0;
                } else {
                    addErrorList(i, "是否同关区取值只能为,是或者否", errorList);
                    continue;
                }

            }

            DepotScheduleMongo depotScheduleMongo = null;
            if (DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
                if (StringUtils.isEmpty(depotScheduleAddress)) {
                    addErrorList(i, "【入库仓是备查仓】,调入仓地址必填", errorList);
                    continue;
                }
                Map<String, Object> depotScheduleMap = new HashMap<String, Object>();
                depotScheduleMap.put("address", depotScheduleAddress);
                depotScheduleMap.put("depotId", inDepot.getDepotId());
                depotScheduleMongo = depotScheduleMongoDao.findOne(depotScheduleMap);
                if (depotScheduleMongo == null) {
                    addErrorList(i, "【入库仓是备查仓】,根据仓库地址查询mongdb仓库附表此仓库没有查询到", errorList);
                    continue;
                }
            }

            //调出仓库为保税仓/海外仓且需下推调拨指令接口的，仓库海关编码、仓库国检编码不能为空
            if (outDepot.getType().matches(DERP_SYS.DEPOTINFO_TYPE_A + "|" + DERP_SYS.DEPOTINFO_TYPE_C) &&
                    DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(outDepotRel.getIsInOutInstruction())) {
                //申报地海关
                if (StringUtils.isEmpty(outDepot.getCustomsNo())) {
                    addErrorList(i, "【基本信息】请完善调出仓库海关编码", errorList);
                    continue;
                }
                //申报地国检
                if (StringUtils.isEmpty(outDepot.getInspectNo())) {
                    addErrorList(i, "【基本信息】请完善调出仓库国检编码", errorList);
                    continue;
                }
            }

            //如果涉及海外仓理货单位必填
            if (inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C) || outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                //理货单位
                if (StringUtils.isEmpty(tallyingUnit)) {
                    addErrorList(i, "【基本信息】请输入理货单位", errorList);
                    continue;
                }
            }

            depotMap = new HashMap<String, DepotInfoMongo>();
            depotMap.put("outDepot", outDepot);
            depotMap.put("inDepot", inDepot);
            sqeDepotMap.put(zcode, depotMap);

            boolean isOutStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(outDepotRel.getIsInOutInstruction());
            boolean isInStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(inDepotRel.getIsInOutInstruction());

            //查询调入客户
            MerchantInfoMongo inMerchant = null;
            if (!StringUtils.isEmpty(customerCode)) {
                Map<String, Object> cparamMap = new HashMap<String, Object>();
                cparamMap.put("topidealCode", customerCode);
                inMerchant = merchantInfoMongoDao.findOne(cparamMap);
            }

            //唯品代销仓: po号必填
            if ("VIP001".equals(inDepot.getDepotCode()) || "VIP001".equals(outDepot.getDepotCode())) {
                if (StringUtils.isEmpty(poNo)) {
                    addErrorList(i, "【基本信息】请输入PO单号", errorList);
                    continue;
                }
            }

            //1.调出仓库类型保税仓，调入仓库类型保税仓，同关区且下推接口指令都为是: 调入客户、合同号、是否同关区必填
            if (outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && DERP.ISSAMEAREA_1.equals(isSameArea) && isInStruction && isOutStruction) {
                if (inMerchant == null) {
                    addErrorList(i, "【基本信息】调入商家编码不正确", errorList);
                    continue;
                }
                //表头合同号
                if (StringUtils.isEmpty(contractNo)) {
                    addErrorList(i, "【基本信息】请输入合同号", errorList);
                    continue;
                }
            }

            //2. 1）调出仓库类型保税仓，调入仓库类型保税仓，同关区且调出接口指令为否，调入接口指令为是
            //   2) 调出仓库类型保税仓，调入仓库类型保税仓，跨关区:调入客户,合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格,是否同关区
            else if (outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && ((DERP.ISSAMEAREA_1.equals(isSameArea) && isInStruction && !isOutStruction)
                    || DERP.ISSAMEAREA_0.equals(isSameArea))) {
                if (inMerchant == null) {
                    addErrorList(i, "【基本信息】调入商家编码不正确", errorList);
                    continue;
                }
                //表头合同号
                if (StringUtils.isEmpty(contractNo)) {
                    addErrorList(i, "【基本信息】请输入合同号", errorList);
                    continue;
                }
                //发票号
                if (StringUtils.isEmpty(invoiceNo)) {
                    addErrorList(i, "【基本信息】请输入发票号", errorList);
                    continue;
                }
                //收货地址
                if (StringUtils.isEmpty(receivingAddress)) {
                    addErrorList(i, "【基本信息】输入收货地址", errorList);
                    continue;
                }
                //包装方式
                if (StringUtils.isEmpty(packType)) {
                    addErrorList(i, "【基本信息】请输包装方式", errorList);
                    continue;
                }
                //箱数
                if (StringUtils.isEmpty(cartons)) {
                    addErrorList(i, "【基本信息】请输箱数", errorList);
                    continue;
                }
                //头程提单号
                if (StringUtils.isEmpty(firstLadingBillNo)) {
                    addErrorList(i, "【基本信息】请输头程提单号", errorList);
                    continue;
                }
            }
            //3.调出仓库类型保税仓，调入仓库类型保税仓，同关区，调入接口指令为否：调入客户、合同号、是否同关区
            else if (outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && DERP.ISSAMEAREA_1.equals(isSameArea) && !isInStruction) {
                if (inMerchant == null) {
                    addErrorList(i, "【基本信息】调入商家编码不正确", errorList);
                    continue;
                }
                //表头合同号
                if (StringUtils.isEmpty(contractNo)) {
                    addErrorList(i, "【基本信息】请输入合同号", errorList);
                    continue;
                }
            }
            //4.调出仓库类型是海外仓：合同号,发票号,收货地址,包装方式,箱数,头程提单号,调入客户,价格,收货人姓名,国家,目的地,海外理货单位
            else if (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())) {
                if (inMerchant == null) {
                    addErrorList(i, "【基本信息】调入商家编码不正确", errorList);
                    continue;
                }
                //表头合同号
                if (StringUtils.isEmpty(contractNo)) {
                    addErrorList(i, "【基本信息】请输入合同号", errorList);
                    continue;
                }
                //发票号
                if (StringUtils.isEmpty(invoiceNo)) {
                    addErrorList(i, "【基本信息】请输入发票号", errorList);
                    continue;
                }
                //收货地址
                if (StringUtils.isEmpty(receivingAddress)) {
                    addErrorList(i, "【基本信息】输入收货地址", errorList);
                    continue;
                }
                //包装方式
                if (StringUtils.isEmpty(packType)) {
                    addErrorList(i, "【基本信息】请输包装方式", errorList);
                    continue;
                }
                //箱数
                if (StringUtils.isEmpty(cartons)) {
                    addErrorList(i, "【基本信息】请输箱数", errorList);
                    continue;
                }
                //头程提单号
                if (StringUtils.isEmpty(firstLadingBillNo)) {
                    addErrorList(i, "【基本信息】请输头程提单号", errorList);
                    continue;
                }
                //目的地
                if (StringUtils.isEmpty(destination)) {
                    addErrorList(i, "【基本信息】请输入目的地", errorList);
                    continue;
                }
                //收货人姓名
                if (StringUtils.isEmpty(consigneeUsername)) {
                    addErrorList(i, "【基本信息】请输入收货人姓名", errorList);
                    continue;
                }
                //国家
                if (StringUtils.isEmpty(country)) {
                    addErrorList(i, "【基本信息】请输入国家", errorList);
                    continue;
                }
            }
            //5.调入仓库类型海外仓 : 合同号、海外理货单位、调入客户
            else if (outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                //表头合同号
                if (StringUtils.isEmpty(contractNo)) {
                    addErrorList(i, "【基本信息】请输入合同号", errorList);
                    continue;
                }
                if (inMerchant == null) {
                    addErrorList(i, "【基本信息】调入商家编码不正确", errorList);
                    continue;
                }
            }
            //6.保税仓调备查库【同\跨关区】：调入客户、合同号、是否同关区
            else if (outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_B)) {
                //表头合同号
                if (StringUtils.isEmpty(contractNo)) {
                    addErrorList(i, "【基本信息】请输入合同号", errorList);
                    continue;
                }
                if (inMerchant == null) {
                    addErrorList(i, "【基本信息】调入商家编码不正确", errorList);
                    continue;
                }
            }
            //7、调出仓库类型备查库或暂存区：调入客户
            else if (outDepot.getType().matches(DERP_SYS.DEPOTINFO_TYPE_B + "|" + DERP_SYS.DEPOTINFO_TYPE_E)) {
                //表头合同号
                if (StringUtils.isEmpty(contractNo)) {
                    addErrorList(i, "【基本信息】请输入合同号", errorList);
                    continue;
                }
                if (inMerchant == null) {
                    addErrorList(i, "【基本信息】调入商家编码不正确", errorList);
                    continue;
                }
            }

            TransferOrderModel transOrder = new TransferOrderModel();
            // 出入库仓时都是保税仓时,才插入数据库
            if (DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())
                    && (DERP_SYS.DEPOTINFO_TYPE_A.equals(inDepot.getType()) || DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType()))) {
                transOrder.setIsSameArea(isSameArea);
            }
            if (depotScheduleMongo != null) {
                transOrder.setDepotScheduleAddress(depotScheduleMongo.getAddress());
                transOrder.setDepotScheduleId(depotScheduleMongo.getDepotScheduleId());
            }

            setModelAndServeTypes(outDepot, inDepot, transOrder, customerCode, userTopidealCode, null);

            transOrder.setZcode(zcode);
//			    transOrder.setCode(CodeGeneratorUtil.getNo("DBO"));
            transOrder.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBO));
            transOrder.setMerchantId(merchantId);
            transOrder.setMerchantName(merchantName);
            transOrder.setTopidealCode(userTopidealCode);
            if (inMerchant != null) {
                transOrder.setInCustomerId(inMerchant.getMerchantId());
                transOrder.setInCustomerName(inMerchant.getName());
            }
            transOrder.setOutDepotId(outDepot.getDepotId());
            transOrder.setOutDepotName(outDepot.getName());
            transOrder.setInDepotId(inDepot.getDepotId());
            transOrder.setInDepotName(inDepot.getName());
            transOrder.setModel(model);
            transOrder.setServeTypes(serveTypes);
            transOrder.setContractNo(contractNo);
            transOrder.setLbxNo(lbxNo);
            transOrder.setPoNo(poNo);
            transOrder.setRemark(remark);
            transOrder.setStatus(DERP_ORDER.TRANSFERORDER_STATUS_013);//待提交
            transOrder.setInvoiceNo(invoiceNo);//发票号
            transOrder.setPackType(packType);//包装方式
            transOrder.setCartons(cartons.trim());//箱数
            transOrder.setFirstLadingBillNo(firstLadingBillNo);//头程单号
            transOrder.setReceivingAddress(receivingAddress);//收货地址
            if (outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C) || inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                transOrder.setTallyingUnit(tallyingUnit);//海外仓理货单位
            }
            if (outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                transOrder.setDestination(destination);//目的地编码
            }
            transOrder.setConsigneeUsername(consigneeUsername);//收货人姓名
            transOrder.setCountry(country);//国家
            transOrder.setShipper(shipper);//境外发货人
            transOrder.setMark(mark);//唛头
            transOrder.setModifier(userId);//修改人
            transOrder.setModifierUsername(name);
            transOrder.setCreater(userId);//创建人
            transOrder.setCreateUsername(name);
            transOrder.setModifyDate(TimeUtils.getNow());//修改时间
            transOrder.setCreateDate(TimeUtils.getNow());
            transOrder.setBuId(merchantBuRelMongo.getBuId());
            transOrder.setBuName(merchantBuRelMongo.getBuName());
            if (buStockLocationTypeConfigMongo != null) {
				transOrder.setStockLocationTypeId(buStockLocationTypeConfigMongo.getBuStockLocationTypeId());
				transOrder.setStockLocationTypeName(buStockLocationTypeConfigMongo.getName());
			}
            transferOrderList.add(transOrder);
        }
        /***********************************检查组装调拨单数据end************************/

        /***********************************检查组装商品end*****************************/
        Map<String, List<TransferOrderItemModel>> itemListMap = null;//存商品
        Map<String, Double> billWeightMap = null; //存单据对应的提单毛重
        if (errorList == null || errorList.size() <= 0) {
            //商品数据
            List<Map<String, String>> sheet1 = data.get(1);//商品
            itemListMap = new HashMap<String, List<TransferOrderItemModel>>();
            billWeightMap = new HashMap<>();
            Set existGoodsNoSet = new HashSet();
            Set existInGoodsNoSet = new HashSet();
            for (int j = 0; j < sheet1.size(); j++) {
                Map<String, String> merchandiseTemp = data.get(1).get(j);
                String zcodetwo = merchandiseTemp.get("自编调拨单号").trim();//自编调拨单号
                String outGoodsNo = merchandiseTemp.get("调出商品货号").trim();//调出商品货号
                String inGoodsNo = merchandiseTemp.get("调入商品货号").trim();//调入商品货号
                String batchNo = merchandiseTemp.get("调出批次").trim();//调出批次
                String transNumStr = merchandiseTemp.get("调出数量").trim();//调出数量
                String inTransNumStr = merchandiseTemp.get("调入数量").trim();//调入数量
                String price = merchandiseTemp.get("调拨价格").trim();//调拨价格
                String contNo = merchandiseTemp.get("箱号").trim();//箱号
                String bargainno = merchandiseTemp.get("合同号").trim();//合同号
                String remark = merchandiseTemp.get("备注").trim();//备注

                if (StringUtils.isEmpty(zcodetwo)) {
                    addErrorList(j, "【商品信息】自编码为空", errorList);
                    continue;
                }

                if (!sqeDepotMap.containsKey(zcodetwo)) {
                    addErrorList(j, "【商品信息】自编码在【基本信息】找不到对应自编码", errorList);
                    continue;
                }

                if (StringUtils.isEmpty(outGoodsNo)) {
                    addErrorList(j, "【商品信息】调出商品货号为空", errorList);
                    continue;
                }

                if (existGoodsNoSet.contains(zcodetwo + "_" + outGoodsNo)) {
                    addErrorList(j, "【商品信息】同一调拨订单存在重复调出商品，请合并再导入", errorList);
                    continue;
                }

                if (existInGoodsNoSet.contains(zcodetwo + "_" + inGoodsNo)) {
                    addErrorList(j, "【商品信息】同一调拨订单存在重复调入商品，请合并再导入", errorList);
                    continue;
                }
                existGoodsNoSet.add(zcodetwo + "_" + outGoodsNo);
                existInGoodsNoSet.add(zcodetwo + "_" + inGoodsNo);
                DepotInfoMongo outDepot = null;//调出仓库类型
                DepotInfoMongo inDepot = null;//调入仓库类型
                Map<String, DepotInfoMongo> depot = sqeDepotMap.get(zcodetwo);
                if (depot != null) {
                    outDepot = depot.get("outDepot");
                    inDepot = depot.get("inDepot");
                }

                Map<String, Object> mParamMap = new HashMap<String, Object>();
                mParamMap.put("goodsNo", outGoodsNo);
                mParamMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);// 状态(1使用中,0已禁用)
                mParamMap.put("merchantId", merchantId);
                MerchandiseInfoMogo outMerchandise = merchandiseInfoMogoDao.findOne(mParamMap);
                if (outMerchandise == null) {
                    addErrorList(j, "【商品信息】调出商品不存在", errorList);
                    continue;
                }

                Map<String, Object> merDepotParams = new HashMap<>();
                merDepotParams.put("merchandiseId", outMerchandise.getMerchandiseId());
                List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findMerchandiseByDepotId(merDepotParams, outDepot.getDepotId());
                if (merchandiseInfoMogos == null || merchandiseInfoMogos.size() == 0) {
                    addErrorList(j, "【商品信息】调出货号没有对应的调出仓库下存在", errorList);
                    continue;
                }

                mParamMap.put("goodsNo", inGoodsNo);
                mParamMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);// 状态(1使用中,0已禁用)
                mParamMap.put("merchantId", merchantId);
                MerchandiseInfoMogo inMerchandise = merchandiseInfoMogoDao.findOne(mParamMap);
                if (inMerchandise == null) {
                    addErrorList(j, "【商品信息】调入商品不存在", errorList);
                    continue;
                }
                Map<String, Object> outMerDepotParams = new HashMap<>();
                outMerDepotParams.put("merchandiseId", inMerchandise.getMerchandiseId());
                merchandiseInfoMogos = merchandiseInfoMogoDao.findMerchandiseByDepotId(merDepotParams, inDepot.getDepotId());
                if (merchandiseInfoMogos == null || merchandiseInfoMogos.size() == 0) {
                    addErrorList(j, "【商品信息】调入货号没有对应的调入仓库下存在", errorList);
                    continue;
                }

                if (!outMerchandise.getBarcode().equals(inMerchandise.getBarcode())) {
                    addErrorList(j, "【商品信息】调入货号与调出商品货号对应的条码不一致", errorList);
                    continue;
                }

                //调出数量
                if (StringUtils.isEmpty(transNumStr) || !org.apache.commons.lang3.StringUtils.isNumeric(transNumStr)) {
                    addErrorList(j, "【商品信息】调拨数量不正确", errorList);
                    continue;
                }
                Integer transferNum = Integer.valueOf(transNumStr);
                if (transferNum.intValue() <= 0) {
                    addErrorList(j, "【商品信息】调拨数量不正确", errorList);
                    continue;
                }
                //调入数量
                if (StringUtils.isEmpty(inTransNumStr) || !org.apache.commons.lang3.StringUtils.isNumeric(inTransNumStr)) {
                    addErrorList(j, "【商品信息】调入数量不正确", errorList);
                    continue;
                }
                Integer inTransferNum = Integer.valueOf(inTransNumStr);
                if (inTransferNum.intValue() <= 0) {
                    addErrorList(j, "【商品信息】调入数量不正确", errorList);
                    continue;
                }
                //调入和调出仓都不是香港仓调出调入数量必须相等
                if (!outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C) && !inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)
                        && transferNum.intValue() != inTransferNum.intValue()) {
                    addErrorList(j, "【商品信息】非香港仓调出数量和调入数量要相等", errorList);
                    continue;
                }


                //当表中调拨价格为空时，改为获取母品牌信息的申报系数乘调出货号对应的备案价格得出申报单价
                if (StringUtils.isEmpty(price)) {
                    BigDecimal filingPrice = outMerchandise.getFilingPrice();
                    BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(outMerchandise.getMerchandiseId());
                    Double priceDeclareRatio = 1.0;
                    if (brandSuperior != null) {
                        priceDeclareRatio = brandSuperior.getPriceDeclareRatio();
                    }
                    if (filingPrice != null) {
                        price = filingPrice.multiply(new BigDecimal(priceDeclareRatio.toString())).toString();
                    }
                } else {
                    if (Double.valueOf(price) < 0) {
                        addErrorList(j, "【商品信息】调拨数量不正确", errorList);
                        continue;
                    }

                }

                //查询品牌
                BrandMongo brandMongo = null;
                if (inMerchandise != null && inMerchandise.getBrandId() != null) {
                    Map<String, Object> brankMap = new HashMap<String, Object>();
                    brankMap.put("brandId", inMerchandise.getBrandId());
                    brandMongo = brandMongoDao.findOne(brankMap);
                }
                TransferOrderItemModel orderItem = new TransferOrderItemModel();
                orderItem.setZcode(zcodetwo);//自编码
                orderItem.setOutGoodsId(outMerchandise.getMerchandiseId());//调出商品id
                orderItem.setOutGoodsName(outMerchandise.getName());//调出商品名称
                orderItem.setOutGoodsCode(outMerchandise.getGoodsCode());//调出商品编码
                orderItem.setOutGoodsNo(outMerchandise.getGoodsNo());//调出商品货号
                orderItem.setOutCommbarcode(outMerchandise.getCommbarcode());//调出商品标准条码
                orderItem.setInGoodsId(inMerchandise.getMerchandiseId());//调入商品id
                orderItem.setInGoodsName(inMerchandise.getName());//调出商品名称
                orderItem.setInGoodsCode(inMerchandise.getGoodsCode());//调出商品编码
                orderItem.setInGoodsNo(inMerchandise.getGoodsNo());//调出商品货号
                orderItem.setInCommbarcode(inMerchandise.getCommbarcode());//调入商品标准条码
                orderItem.setTransferNum(transferNum);//调出数量
                orderItem.setInTransferNum(inTransferNum);//调入数量

                Double priced = null;
                if (!StringUtils.isEmpty(price)) {
                    priced = Double.valueOf(price);
                }
                orderItem.setPrice(priced);//采购单价
                double grossWeight = 0.0;//毛重
                double netWeight = 0.0;//净重

                grossWeight = inMerchandise.getGrossWeight() == null ? 0.0 : inMerchandise.getGrossWeight();
                netWeight = inMerchandise.getNetWeight() == null ? 0.0 : inMerchandise.getNetWeight();

                orderItem.setGrossWeight(grossWeight);
                orderItem.setNetWeight(netWeight);
                orderItem.setGrossWeightSum(grossWeight * inTransferNum);
                orderItem.setNetWeightSum(netWeight * inTransferNum);
                if (brandMongo != null) {
                    orderItem.setBrandName(brandMongo.getName());
                }
                orderItem.setContNo(contNo);//箱号
                orderItem.setBargainno(bargainno);//合同号
                orderItem.setRemark(remark);//备注
                orderItem.setOutBarcode(outMerchandise.getBarcode());
                orderItem.setInBarcode(inMerchandise.getBarcode());
                orderItem.setCreater(userId);//创建人
                orderItem.setCreateDate(TimeUtils.getNow());//创建时间
                Double billWeight = grossWeight * inTransferNum; //商品毛重
                if (itemListMap.containsKey(zcodetwo)) {
                    itemListMap.get(zcodetwo).add(orderItem);
                    billWeight += billWeightMap.get(zcodetwo);
                    billWeightMap.put(zcodetwo, billWeight);
                } else {
                    List<TransferOrderItemModel> orderItemList = new ArrayList<TransferOrderItemModel>();
                    orderItemList.add(orderItem);
                    itemListMap.put(zcodetwo, orderItemList);
                    billWeightMap.put(zcodetwo, billWeight);
                }
            }

        }

        /***********************************检查组装商品end*****************************/
        retMap.put("allRows", data.get(0).size());//总记录数
        if (errorList.size() > 0) {//导入失败
            retMap.put("code", "01");
            retMap.put("errorList", errorList);
            retMap.put("errorRows", errorList.size());//总记录数
            return retMap;
        }

        //保存调拨订单
        for (TransferOrderModel orderModel : transferOrderList) {
            Map<String, DepotInfoMongo> depot = sqeDepotMap.get(orderModel.getZcode());
            DepotInfoMongo outDepot = null;
            DepotInfoMongo inDepot = null;
            if (depot != null) {
                outDepot = depot.get("outDepot");
                inDepot = depot.get("inDepot");
            }
            Double billWeight = billWeightMap.get(orderModel.getZcode());
            orderModel.setBillWeight(billWeight);
            Long transferOrderId = transferOrderDao.save(orderModel);
            List<TransferOrderItemModel> itemListTemp = itemListMap.get(orderModel.getZcode());

            for (int i = 0; i < itemListTemp.size(); i++) {
                TransferOrderItemModel item = itemListTemp.get(i);
                item.setTransferOrderId(transferOrderId);
                item.setSeq(i + 1);
                if (outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {//调出仓是香港仓
                    item.setOutUnit(orderModel.getTallyingUnit());
                } else if (inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {//调入仓是香港仓
                    item.setInUnit(orderModel.getTallyingUnit());
                }
                transferOrderItemDao.save(item);
            }
        }
        //导入成功
        retMap.put("code", "00");
        retMap.put("errorList", errorList);
        retMap.put("errorRows", errorList.size());//总记录数
        return retMap;
    }

    public void addErrorList(Integer row, String message, List<Map<String, Object>> errorList) {
        Map<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put("row", row + 1);
        messageMap.put("message", message);
        errorList.add(messageMap);
    }

    /**
     * 修改lbxno
     *
     * @throws SQLException
     */
    @Override
    public Map<String, Object> updateLbxNo(Long userId, String name, Long transOrderId, String newLbxNo) throws Exception {
        Map<String, Object> retMap = new HashMap<String, Object>();
        TransferOrderModel order = transferOrderDao.searchById(transOrderId);
        //判断状态
        if (!order.getStatus().matches(DERP_ORDER.TRANSFERORDER_STATUS_013
                + "|" + DERP_ORDER.TRANSFERORDER_STATUS_014
                + "|" + DERP_ORDER.TRANSFERORDER_STATUS_023)) {
            retMap.put("code", "01");
            retMap.put("message", "此订单状态不允许修改");
            return retMap;
        }
        String oldLbxNo = order.getLbxNo();
        //若调拨单状态为待提交则只需要更新mysql
        order.setLbxNo(newLbxNo);
        transferOrderDao.modify(order);

        //若调拨单状态为已提交、调拨已出库 需要更新mongodb LbxNo
        if (order.getStatus().matches(DERP_ORDER.TRANSFERORDER_STATUS_014
                + "|" + DERP_ORDER.TRANSFERORDER_STATUS_023)) {
            LbxNoMongo entity = null;
            if (!StringUtils.isEmpty(oldLbxNo)) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("lbxNo", oldLbxNo);
                entity = lbxNoMongoDao.findOne(paramMap);
            }
            if (entity == null) {
                //插入mongodb lbxno和单号类型
                LbxNoMongo lbxentity = new LbxNoMongo();
                lbxentity.setLbxNo(newLbxNo);
                lbxentity.setType("DBO");//调拨订单
                lbxNoMongoDao.insert(lbxentity);
            } else {
                Map<String, Object> delMap = new HashMap<String, Object>();
                delMap.put("lbxNo", entity.getLbxNo());
                delMap.put("type", "DBO");
                lbxNoMongoDao.remove(delMap);
                //插入mongodb lbxno和单号类型
                LbxNoMongo lbxentity = new LbxNoMongo();
                lbxentity.setLbxNo(newLbxNo);
                lbxentity.setType("DBO");//调拨订单
                lbxNoMongoDao.insert(lbxentity);
            }
        }
        retMap.put("code", "00");
        retMap.put("message", "成功");
        return retMap;
    }

    /**
     * 根据调拨单id统计调出商品数量
     */
    @Override
    public List<Map<String, Object>> getItemSumByIds(List<Long> Ids) {
        return transferOrderDao.getItemSumByIds(Ids);
    }

    @Override
    public Map<String, Object> saveTempTransferOrder(Long userId, String name, Long merchantId, String MerchantName, String topidealCode, TransferOrderFrom model) throws Exception {
        Map<String, Object> retMap = new HashMap<>();
        String orderId = model.getOrderId();
        Long id = null;
        TransferOrderModel transferOrderModel = new TransferOrderModel();

        Map<String, Object> param = new HashMap<String, Object>();

        DepotInfoMongo outDepot = null;
        if (model.getOutDepotId() != null) {
            param.put("depotId", model.getOutDepotId());
            outDepot = depotInfoMongoDao.findOne(param);
        }

        DepotInfoMongo inDepot = null;
        if (model.getInDepotId() != null) {
            param.put("depotId", model.getInDepotId());
            inDepot = depotInfoMongoDao.findOne(param);
        }

        MerchantBuRelMongo merchantBuRelMongo = null;
        if (model.getBuId() != null) {
            Map<String, Object> buMap = new HashMap<>();
            buMap.put("buId", model.getBuId());
            buMap.put("merchantId", merchantId);
            buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
            merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
            if (merchantBuRelMongo == null) {
                retMap.put("code", "01");
                retMap.put("message", "事业部不在该公司下");
                return retMap;
            }
        }

        BuStockLocationTypeConfigMongo buStockLocationTypeConfigMongo = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(model.getStockLocationTypeId())) {
            Map<String, Object> buStockLocationTypeParams = new HashMap<>();
            buStockLocationTypeParams.put("merchantId", merchantId);
            buStockLocationTypeParams.put("buId", merchantBuRelMongo.getBuId());
            buStockLocationTypeParams.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
            buStockLocationTypeParams.put("buStockLocationTypeId", Long.valueOf(model.getStockLocationTypeId()));
			buStockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(buStockLocationTypeParams);

            if (buStockLocationTypeConfigMongo == null) {
                retMap.put("code", "01");
                retMap.put("message", "库位类型在公司事业部下不存在");
                return retMap;
            }

        }

        //通过判断orderId是否为空判断新增修改
        if (org.apache.commons.lang.StringUtils.isNotBlank(orderId)) {
            id = Long.valueOf(model.getOrderId());
            TransferOrderModel transOrder = transferOrderDao.searchById(Long.valueOf(model.getOrderId()));
            BeanUtils.copyProperties(transOrder, transferOrderModel);
            if (transOrder == null || transOrder.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_014)) {
                retMap.put("code", "01");
                retMap.put("message", "调拨单不存在/已提交");
                return retMap;
            }

            // 如果非备查仓 并且 数据库的 是否同关区 不是空,说明是入库仓是从备查仓变成非备查仓,把仓库地址 设置成空
            if (inDepot != null && !DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
                if (!StringUtils.isEmpty(transOrder.getDepotScheduleAddress())) {
                    transferOrderModel.setDepotScheduleAddress(" ");
                }
            } else {
                if (model.getDepotScheduleId() != null) {
                    transferOrderModel.setDepotScheduleAddress(model.getDepotScheduleAddress());
                }
                transferOrderModel.setDepotScheduleId(model.getDepotScheduleId());
            }

        } else {
            if (model.getDepotScheduleId() != null) {
                transferOrderModel.setDepotScheduleAddress(model.getDepotScheduleAddress());
            }
            transferOrderModel.setDepotScheduleId(model.getDepotScheduleId());
            transferOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBO));//调拨订单编号
            transferOrderModel.setMerchantId(merchantId);//商家id
            transferOrderModel.setMerchantName(MerchantName);//商家名称
            transferOrderModel.setTopidealCode(topidealCode);//卓志编码
            transferOrderModel.setStatus(DERP_ORDER.TRANSFERORDER_STATUS_013);//待提交
            transferOrderModel.setCreater(userId);//创建人
            transferOrderModel.setCreateUsername(name);//创建人名称
            transferOrderModel.setCreateDate(TimeUtils.getNow());
        }

        //调入客户卓志编码
        String inTopidealCode = "";
        if (model.getInCustomerId() != null && model.getInCustomerId().longValue() > 0) {
            Map<String, Object> cMap = new HashMap<String, Object>();
            cMap.put("merchantId", model.getInCustomerId());
            MerchantInfoMongo inMerchant = merchantInfoMongoDao.findOne(cMap);
            if (inMerchant != null) inTopidealCode = inMerchant.getTopidealCode();
        }
        transferOrderModel.setIsSameArea(model.getIsSameArea());
        transferOrderModel.setPoNo(model.getPoNo() != null ? model.getPoNo().trim() : " ");//PO单号
        transferOrderModel.setLbxNo(model.getLbxNo() != null ? model.getLbxNo().trim() : " ");//LBX单号
        transferOrderModel.setContractNo(model.getContractNo() != null ? model.getContractNo().trim() : "");//合同编号
        transferOrderModel.setOutDepotId(model.getOutDepotId());//调出仓id
        transferOrderModel.setOutDepotName(model.getOutDepotName());//调出仓名称
        transferOrderModel.setInDepotId(model.getInDepotId());//调入仓id
        transferOrderModel.setInDepotName(model.getInDepotName());//调入仓名称
        transferOrderModel.setRemark(model.getRemark());
        if (merchantBuRelMongo != null) {
            transferOrderModel.setBuId(merchantBuRelMongo.getBuId());
            transferOrderModel.setBuName(merchantBuRelMongo.getBuName());
        }

        //设置业务类型和服务类型
        if (outDepot != null && inDepot != null) {
            setModelAndServeTypes(outDepot, inDepot, transferOrderModel, inTopidealCode, topidealCode, id);
        }

        transferOrderModel.setInCustomerId(model.getInCustomerId());//客户id
        transferOrderModel.setInCustomerName(model.getInCustomerName());//客户名称
        transferOrderModel.setInvoiceNo(model.getInvoiceNo());//发票号
        transferOrderModel.setPackType(model.getPackType());//包装方式
        transferOrderModel.setCartons(model.getCartons().trim());//箱数
        transferOrderModel.setFirstLadingBillNo(model.getFirstLadingBillNo());//头程单号
        transferOrderModel.setReceivingAddress(model.getReceivingAddress());//收货地址
        transferOrderModel.setDestination(model.getDestination());//目的地
        transferOrderModel.setConsigneeUsername(model.getConsigneeUsername());//收货人姓名
        transferOrderModel.setCountry(model.getCountry());//国家
        transferOrderModel.setTallyingUnit(model.getTallyingUnit() == null ? " " : model.getTallyingUnit());//理货单位
        transferOrderModel.setMark(model.getMark());//唛头
        transferOrderModel.setShipper(model.getShipper());//境外发货人
        transferOrderModel.setModifier(userId);//修改人
        transferOrderModel.setModifierUsername(name);//修改人名称
        transferOrderModel.setModifyDate(TimeUtils.getNow());//修改时间
        transferOrderModel.setBillWeight(model.getBillWeight()); //提单毛重
        transferOrderModel.setTrainNumber(model.getTrainNumber()); //车次
        transferOrderModel.setStandardCaseTeu(model.getStandardCaseTeu()); //标准箱TEU
        transferOrderModel.setTorrNum(model.getTorrNum()); //托数
        transferOrderModel.setCarrier(model.getCarrier()); //承运商
        transferOrderModel.setOutdepotAddr(model.getOutdepotAddr()); //出库地点
        transferOrderModel.setTransport(model.getTransport()); //运输方式
        transferOrderModel.setPackType(model.getPackType());
        transferOrderModel.setPalletMaterial(model.getPalletMaterial());
        transferOrderModel.setPortLoading(model.getPortLoading());
        transferOrderModel.setUnloadPort(model.getUnloadPort());
        transferOrderModel.setPayRules(model.getPayRules());

        if (model.getOutDepotId() != null && model.getOutCustomsId() != null) {
            Map<String, Object> outDepotCustomsRelMap = new HashMap<String, Object>();
            outDepotCustomsRelMap.put("depotId", model.getOutDepotId());
            outDepotCustomsRelMap.put("customsAreaId", model.getOutCustomsId());
            DepotCustomsRelMongo outDepotCustomsRelMongo = depotCustomsRelMongoDao.findOne(outDepotCustomsRelMap);//平台关区信息
            if (outDepotCustomsRelMongo == null) {
                retMap.put("code", "01");
                retMap.put("message", "保存失败，出仓仓库：" + outDepot.getName() + " 未关联选中平台关区");
                return retMap;
            }
            transferOrderModel.setOutCustomsId(model.getOutCustomsId());
            transferOrderModel.setOutCustomsCode(outDepotCustomsRelMongo.getCustomsAreaCode());
            transferOrderModel.setOutPlatformCustoms(outDepotCustomsRelMongo.getCustomsAreaName());
        }

        if (model.getInDepotId() != null && model.getInCustomsId() != null) {
            Map<String, Object> inDepotCustomsRelMap = new HashMap<String, Object>();
            inDepotCustomsRelMap.put("depotId", model.getInDepotId());
            inDepotCustomsRelMap.put("customsAreaId", model.getInCustomsId());
            DepotCustomsRelMongo inDepotCustomsRelMongo = depotCustomsRelMongoDao.findOne(inDepotCustomsRelMap);//平台关区信息
            if (inDepotCustomsRelMongo == null) {
                retMap.put("code", "01");
                retMap.put("message", "保存失败，入仓仓库：" + outDepot.getName() + " 未关联选中平台关区");
                return retMap;
            }
            transferOrderModel.setInCustomsId(model.getInCustomsId());
            transferOrderModel.setInCustomsCode(inDepotCustomsRelMongo.getCustomsAreaCode());
            transferOrderModel.setInPlatformCustoms(inDepotCustomsRelMongo.getCustomsAreaName());
        }

        if (model.getOutCustomsId() == null) {
            transferOrderModel.setOutCustomsId(null);
            transferOrderModel.setOutCustomsCode(null);
            transferOrderModel.setOutPlatformCustoms(null);
        }

        if (model.getInCustomsId() == null) {
            transferOrderModel.setInCustomsId(null);
            transferOrderModel.setInCustomsCode(null);
            transferOrderModel.setInPlatformCustoms(null);
        }

        if (buStockLocationTypeConfigMongo != null) {
            transferOrderModel.setStockLocationTypeId(buStockLocationTypeConfigMongo.getBuStockLocationTypeId());
            transferOrderModel.setStockLocationTypeName(buStockLocationTypeConfigMongo.getName());
        } else {
            transferOrderModel.setStockLocationTypeId(null);
            transferOrderModel.setStockLocationTypeName(null);
        }

        //保存商品
        List<TransferOrderItemModel> orderItemList = new ArrayList<TransferOrderItemModel>();
        List<Map<String, Object>> goodsList = model.getGoodsList();
        for (int i = 0; i < goodsList.size(); i++) {
            TransferOrderItemModel orderItem = new TransferOrderItemModel();
            Map<String, Object> goodMap = goodsList.get(i);
            Integer seq = i + 1;
            if (goodMap.containsKey("seq") && !StringUtils.isEmpty((String) goodMap.get("seq"))) {
                seq = Integer.valueOf((String) goodMap.get("seq"));
            }
            Long outGoodsId = Long.valueOf((String) goodMap.get("outGoodsId"));
            String outUnit = (String) goodMap.get("outUnit");//调出单位
            Integer transferNum = org.apache.commons.lang.StringUtils.isNotBlank((String) goodMap.get("transferNum")) ? Integer.valueOf((String) goodMap.get("transferNum")) : null;//调出数量
            String inUnit = (String) goodMap.get("inUnit");//调入单位
            Integer inTransferNum = org.apache.commons.lang.StringUtils.isNotBlank((String) goodMap.get("inTransferNum")) ? Integer.valueOf((String) goodMap.get("inTransferNum")) : null;//调入数量
            String brandName = (String) goodMap.get("brandName");//品牌类型
            String price = (String) goodMap.get("price");//采购单价
            String grossWeight = (String) goodMap.get("grossWeight");//毛重
            String grossWeightSum = (String) goodMap.get("grossWeightSum");//总毛重
            String netWeight = (String) goodMap.get("netWeight");//净重
            String netWeightSum = (String) goodMap.get("netWeightSum");//总净重
            String contNo = (String) goodMap.get("contNo");//箱号
            String bargainno = (String) goodMap.get("bargainno");//合同号
            String outBarcode = (String) goodMap.get("outBarcode");//条形码
            String boxNumStr = (String) goodMap.get("boxNum");
            if (!StringUtils.isEmpty(boxNumStr)) {
                Integer boxNum = Integer.valueOf(boxNumStr);//箱数
                orderItem.setBoxNum(boxNum);
            } else {
                orderItem.setBoxNum(null);
            }
            String torrNo = (String) goodMap.get("torrNo");//托盘号

            //查询商品
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("merchandiseId", outGoodsId);
            MerchandiseInfoMogo outMerchandise = merchandiseInfoMogoDao.findOne(paramMap);

            orderItem.setOutGoodsId(outGoodsId);//调出商品id
            if (outMerchandise != null) {
                orderItem.setOutGoodsName(outMerchandise.getName());//调出商品名称
                orderItem.setOutGoodsCode(outMerchandise.getGoodsCode());//调出商品编码
                orderItem.setOutGoodsNo(outMerchandise.getGoodsNo());//调出商品货号
                orderItem.setOutCommbarcode(outMerchandise.getCommbarcode());//调出商品标准条码
                orderItem.setOutBarcode(outMerchandise.getBarcode());
            }
            orderItem.setOutUnit(outUnit);//调出单位
            orderItem.setTransferNum(transferNum);//调出数量
            if (goodMap.containsKey("inGoodsId") && !StringUtils.isEmpty(goodMap.get("inGoodsId"))) {
                Long inGoodsId = Long.valueOf((String) goodMap.get("inGoodsId"));
                paramMap.put("merchandiseId", inGoodsId);
                MerchandiseInfoMogo inMerchandise = merchandiseInfoMogoDao.findOne(paramMap);
                orderItem.setInGoodsId(inGoodsId);//调入商品id
                if (inMerchandise != null) {
                    orderItem.setInGoodsName(inMerchandise.getName());//调出商品名称
                    orderItem.setInGoodsCode(inMerchandise.getGoodsCode());//调出商品编码
                    orderItem.setInGoodsNo(inMerchandise.getGoodsNo());//调出商品货号
                    orderItem.setInCommbarcode(inMerchandise.getCommbarcode());//调入商品标准条码
                    orderItem.setInBarcode(inMerchandise.getBarcode());

                }

            }
            orderItem.setInUnit(inUnit);//调入单位
            orderItem.setInTransferNum(inTransferNum);//调入数量
            orderItem.setBrandName(brandName);//品牌类型
            Double priced = 0.00;
            if (!StringUtils.isEmpty(price)) {
                priced = Double.valueOf(price);
            }
            orderItem.setPrice(priced);//采购单价
            if (!StringUtils.isEmpty(grossWeight)) orderItem.setGrossWeight(Double.valueOf(grossWeight));//毛重
            if (!StringUtils.isEmpty(grossWeightSum)) orderItem.setGrossWeightSum(Double.valueOf(grossWeightSum));//毛重
            if (!StringUtils.isEmpty(netWeight)) orderItem.setNetWeight(Double.valueOf(netWeight));//净重
            if (!StringUtils.isEmpty(netWeightSum)) orderItem.setNetWeightSum(Double.valueOf(netWeightSum));//总净重
            orderItem.setContNo(contNo);//箱号
            orderItem.setBargainno(bargainno);//合同号
            orderItem.setTorrNo(torrNo);
            orderItem.setCreater(userId);//创建人
            orderItem.setCreateDate(TimeUtils.getNow());//创建时间
            orderItem.setSeq(seq);
            orderItemList.add(orderItem);
        }

        List<Long> listIds = new ArrayList<Long>();
        if (id != null) {
            transferOrderDao.modifyWithNull(transferOrderModel);
            //删除原来的商品数据
            TransferOrderItemModel itemParam = new TransferOrderItemModel();
            itemParam.setTransferOrderId(id);
            List<TransferOrderItemModel> listItem = transferOrderItemDao.list(itemParam);

            if (listItem != null && listItem.size() > 0) {
                for (TransferOrderItemModel item : listItem) {
                    listIds.add(item.getId());
                }
                transferOrderItemDao.delete(listIds);
            }
        } else {
            id = transferOrderDao.save(transferOrderModel);
        }

        //保存表体数据
        for (TransferOrderItemModel orderItem : orderItemList) {
            orderItem.setTransferOrderId(id);
            transferOrderItemDao.save(orderItem);
        }
        //删除原有的关联箱装明细
        PackingDetailsModel packingDetailsModel = new PackingDetailsModel();
        packingDetailsModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_2);
        packingDetailsModel.setOrderId(id);
        packingDetailsDao.deleteByModel(packingDetailsModel);
        //保存装箱明细
        List<Map<String, Object>> packingList = model.getPackingList();
        if (packingList != null && packingList.size() > 0) {
            for (Map<String, Object> packModel : packingList) {
                String torrNo = (String) packModel.get("torrNo");
                String goodsNo = (String) packModel.get("goodsNo");
                String barcode = (String) packModel.get("barcode");
                Integer boxNum = (Integer) packModel.get("boxNum");
                Integer piecesNum = (Integer) packModel.get("piecesNum");
                String cabinetNo = (String) packModel.get("cabinetNo");

                PackingDetailsModel detailsModel = new PackingDetailsModel();
                detailsModel.setTorrNo(torrNo);
                detailsModel.setGoodsNo(goodsNo);
                detailsModel.setBarcode(barcode);
                detailsModel.setBoxNum(boxNum);
                detailsModel.setPiecesNum(piecesNum);
                detailsModel.setCabinetNo(cabinetNo);
                detailsModel.setOrderId(id);
                detailsModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_2);
                packingDetailsDao.save(detailsModel);
            }
        }

        retMap.put("code", "00");
        retMap.put("message", "保存成功");
        return retMap;

    }

    @Override
    public Long countForExport(TransferOrderDTO dto, User user) {
        List<Long> userBuIds = userBuRelMongoDao.getBuListByUser(user.getId());
        if (userBuIds.isEmpty()) {
            return 0L;
        }
        dto.setUserBuList(userBuIds);
        return transferOrderDao.countForExport(dto);
    }

    @Override
    public List<Map<String, Object>> listForExport(TransferOrderDTO dto, User user) {
        List<Long> userBuIds = userBuRelMongoDao.getBuListByUser(user.getId());
        if (userBuIds.isEmpty()) {
            return new ArrayList<>();
        }
        dto.setUserBuList(userBuIds);
        List<Map<String, Object>> transferOrderList = transferOrderDao.listForExport(dto);
        for (Map<String, Object> map : transferOrderList) {
            String status = (String) map.get("status");
            map.put("status", DERP_ORDER.getLabelByKey(DERP_ORDER.transferOrder_statusList, status));
            String packType = (String) map.get("pack_type");
            map.put("pack_type", DERP_ORDER.getLabelByKey(DERP.packTypeList, packType));
            String unit = (String) map.get("tallying_unit");
            map.put("tallying_unit", DERP.getLabelByKey(DERP.order_tallyingUnitList, unit));
            String transport = (String) map.get("transport");
            map.put("transport", DERP.getLabelByKey(DERP.transportList, transport));
        }
        return transferOrderList;
    }

    @Override
    public List<Map<String, Object>> listForExportItem(TransferOrderDTO dto, User user) {
        List<Long> userBuIds = userBuRelMongoDao.getBuListByUser(user.getId());
        if (userBuIds.isEmpty()) {
            return new ArrayList<>();
        }
        dto.setUserBuList(userBuIds);
        List<Map<String, Object>> transferOrderItems = transferOrderDao.listForExportItem(dto);
        for (Map<String, Object> map : transferOrderItems) {
            String inUnit = (String) map.get("in_unit");
            map.put("in_unit", DERP.getLabelByKey(DERP.order_tallyingUnitList, inUnit));
            String outUnit = (String) map.get("out_unit");
            map.put("out_unit", DERP.getLabelByKey(DERP.order_tallyingUnitList, outUnit));
        }
        return transferOrderItems;
    }

    @Override
    public Map<String, String> isExistOrder(TransferOrderDTO dto) throws SQLException {
        Map<String, String> result = new HashMap<>();
        if (dto.getOrderType().equals("0")) {
            TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
            transferOutDepotModel.setTransferOrderId(dto.getId());
            transferOutDepotModel.setMerchantId(dto.getMerchantId());
            transferOutDepotModel = transferOutDepotDao.searchByModel(transferOutDepotModel);
            if (transferOutDepotModel != null && !transferOutDepotModel.getStatus().equals(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_006)) {
                result.put("code", "01");
                result.put("message", "该调拨订单存在出库中的出库单，不允许重复出库");
                return result;
            }
        }

        if (dto.getOrderType().equals("1")) {
            TransferInDepotModel transferInDepotModel = new TransferInDepotModel();
            transferInDepotModel.setMerchantId(dto.getMerchantId());
            transferInDepotModel.setTransferOrderId(dto.getId());
            transferInDepotModel = transferInDepotDao.searchByModel(transferInDepotModel);
            if (transferInDepotModel != null && !transferInDepotModel.getStatus().equals(DERP_ORDER.TRANSFERINDEPOT_STATUS_006)) {
                result.put("code", "01");
                result.put("message", "该调拨订单存在入库中的入库单，不允许重复入库");
                return result;
            }
        }
        result.put("code", "00");
        return result;
    }

    @Override
    public Map<String, String> validInDepotDate(Long id, String transferInDate) throws Exception {
        Map<String, String> result = new HashMap<>();
        TransferOutDepotModel outDepotModel = new TransferOutDepotModel();
        outDepotModel.setTransferOrderId(id);
        TransferOutDepotModel model = transferOutDepotDao.searchByModel(outDepotModel);
        if (model == null) {
            result.put("code", "01");
            result.put("message", "该调拨订单还未出库，不能入库");
            return result;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String outDepotDate = sdf.format(model.getTransferDate());
        if (transferInDate.compareTo(outDepotDate) < 0) {
            result.put("code", "01");
            result.put("message", "入库日期必须大于或等于该调拨订单的调出仓出库日期");
            return result;
        }

        String today = sdf.format(new Date());
        if (today.compareTo(transferInDate) < 0) {
            result.put("code", "01");
            result.put("message", "入库日期必须小于或等于当前日期");
            return result;
        }

        // 获取最大的关账日/月结日期
        FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
        closeAccountsMongo.setMerchantId(model.getMerchantId());
        closeAccountsMongo.setDepotId(model.getOutDepotId());
        closeAccountsMongo.setBuId(model.getBuId());
        String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
        String maxCloseAccountsMonth = "";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(maxdate)) {
            // 获取该月份下月时间
            String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
            maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(maxCloseAccountsMonth)) {
            // 入库日期必须大于关账日期
//			String closeDate = sdf.format(maxCloseAccountsMonth);
            if (transferInDate.compareTo(maxCloseAccountsMonth) < 0) {
                result.put("code", "01");
                result.put("message", "入库日期必须大于关账日期/月结日期");
                return result;
            }
        }
        result.put("code", "00");
        return result;
    }

    @Override
    public Map<String, String> validOutDepotDate(Long id, String transferOutDate) throws Exception {
        Map<String, String> result = new HashMap<>();
        TransferOrderModel model = transferOrderDao.searchById(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        if (today.compareTo(transferOutDate) < 0) {
            result.put("code", "01");
            result.put("message", "出库日期必须小于或等于当前日期");
            return result;
        }

        // 获取最大的关账日/月结日期
        FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
        closeAccountsMongo.setMerchantId(model.getMerchantId());
        closeAccountsMongo.setDepotId(model.getOutDepotId());
        closeAccountsMongo.setBuId(model.getBuId());
        String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
        String maxCloseAccountsMonth = "";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(maxdate)) {
            // 获取该月份下月时间
            String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
            maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(maxCloseAccountsMonth)) {
            // 入库日期必须大于关账日期
//			String closeDate = sdf.format(maxCloseAccountsMonth);
            if (transferOutDate.compareTo(maxCloseAccountsMonth) < 0) {
                result.put("code", "01");
                result.put("message", "出库日期必须大于关账日期/月结日期");
                return result;
            }
        }
        result.put("code", "00");
        return result;
    }

    /**
     * 调拨审核
     */
    private Map<String, Object> updateSendTransfer(Long userId, Long merchantId, String name, String topidealCode, TransferOrderModel transferOrder) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //检查状态
        if (transferOrder.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_014)) {
            resultMap.put("code", "01");
            resultMap.put("message", "商品不存在审核失败");
            return resultMap;
        }

        //查询调拨商品
        TransferOrderItemModel item = new TransferOrderItemModel();
        item.setTransferOrderId(transferOrder.getId());
        List<TransferOrderItemModel> itemList = transferOrderItemDao.list(item);
        if (itemList == null || itemList.size() < 1) {
            resultMap.put("code", "01");
            resultMap.put("message", "提交审核调拨商品不存在");
            return resultMap;
        }

        //查询调出/调入仓库
        Map<String, Object> dparamMap = new HashMap<String, Object>();
        dparamMap.put("depotId", transferOrder.getOutDepotId());
        DepotInfoMongo outdepot = depotInfoMongoDao.findOne(dparamMap);
        dparamMap.put("depotId", transferOrder.getInDepotId());
        DepotInfoMongo indepot = depotInfoMongoDao.findOne(dparamMap);
        if (outdepot == null || indepot == null) {
            resultMap.put("code", "01");
            resultMap.put("message", "提交审核调拨仓库不存在");
            return resultMap;
        }
        Map<String, Object> relParam = new HashMap<>();
        relParam.put("merchantId", merchantId);
        relParam.put("depotId", transferOrder.getOutDepotId());
        DepotMerchantRelMongo outDepotRel = depotMerchantRelMongoDao.findOne(relParam);
        relParam.put("depotId", transferOrder.getInDepotId());
        DepotMerchantRelMongo inDepotRel = depotMerchantRelMongoDao.findOne(relParam);
        if (outDepotRel == null || inDepotRel == null) {
            resultMap.put("code", "01");
            resultMap.put("message", "提交审核,在该商家下调拨仓库不存在");
            return resultMap;
        }

        //若lbx号不为空则检查lbx号是否已存在
        String lbxNo = transferOrder.getLbxNo();
        if (!StringUtils.isEmpty(lbxNo)) {
            Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put("lbxNo", lbxNo);
            LbxNoMongo entity = lbxNoMongoDao.findOne(pMap);
            if (entity != null) {
                resultMap.put("code", "01");
                resultMap.put("message", "LBX单号已存在");
                return resultMap;
            }

            //插入mongodb lbxno和单号类型
            LbxNoMongo lbxentity = new LbxNoMongo();
            lbxentity.setLbxNo(transferOrder.getLbxNo());
            lbxentity.setType("DBO");//调拨订单
            lbxNoMongoDao.insert(lbxentity);
        }

        /**
         * (1)当调出仓库为“唯品代销仓”，调入仓库为“唯品退货暂存仓”调拨单据审核时，校验PO单号+调拨出商品货号是否有存在对应上架记录；
         * 满足则执行（2）判断条件，不满足提示错误“PO单号+调拨出商品货号“找不到对应PO+商品货号上架记录”；
         * (2)校验调拨出数量必须小于等于唯品账单爬虫商品货号未核销量；条件满足则审核成功，
         * 不满足则提示错误“PO单号+商品货号调拨出数量应小于等于唯品PO未结算数量”；
         * (3)当调出仓库为“唯品退货暂存仓”，且调入仓库为“唯品代销仓”调拨单据审核时，
         * 校验PO单号+调拨入商品货号是否有存在对应上架记录；满足则审核通过，
         * 不满足提示错误“PO单号+调拨入商品货号“找不到对应PO+商品货号上架记录”；
         */

        if ("WPTH001".equals(indepot.getDepotCode()) && "VIP001".equals(outdepot.getDepotCode())) {
            for (TransferOrderItemModel itemModel : itemList) {
                Map<String, Object> saleShelfParam = new HashMap<>();
                saleShelfParam.put("merchantId", merchantId);
                saleShelfParam.put("depotId", outdepot.getDepotId());
                saleShelfParam.put("poNo", transferOrder.getPoNo());
                saleShelfParam.put("goodsNoList", Arrays.asList(itemModel.getOutGoodsNo()));
                //临时结转量
                Integer shelfNum = pojzTempDao.getPojzNum(saleShelfParam);
                if (shelfNum == null) {
                    //上架入库量
                    shelfNum = shelfIdepotItemDao.getshelfInNum(saleShelfParam);
                }
                if (shelfNum == null) {
                    resultMap.put("code", "01");
                    resultMap.put("message", "PO单号:" + transferOrder.getPoNo() + ",调拨出商品货号:" + itemModel.getOutGoodsNo() + "找不到对应PO+商品货号上架记录");
                    return resultMap;
                }
            }
            for (TransferOrderItemModel itemModel : itemList) {
                Map<String, String> params = new HashMap<>();
                params.put("poNo", transferOrder.getPoNo());
                params.put("goodsNo", itemModel.getOutGoodsNo());
                params.put("depotId", transferOrder.getOutDepotId().toString());
                Integer writeOffNum = this.getQueryWriteOffNum(params, merchantId);
                if (writeOffNum == null || writeOffNum < itemModel.getTransferNum()) {
                    resultMap.put("code", "01");
                    resultMap.put("message", "PO单号:" + transferOrder.getPoNo() + ",调拨出商品货号:" + itemModel.getOutGoodsNo() + "调拨出数量应小于等于唯品PO未结算数量" + writeOffNum);
                    return resultMap;
                }
            }
        } else if ("WPTH001".equals(outdepot.getDepotCode()) && "VIP001".equals(indepot.getDepotCode())) {
            for (TransferOrderItemModel itemModel : itemList) {
                Map<String, Object> saleShelfParam = new HashMap<>();
                saleShelfParam.put("merchantId", merchantId);
                saleShelfParam.put("depotId", indepot.getDepotId());
                saleShelfParam.put("poNo", transferOrder.getPoNo());
                saleShelfParam.put("goodsNoList", Arrays.asList(itemModel.getInGoodsNo()));
                //临时结转量
                Integer shelfNum = pojzTempDao.getPojzNum(saleShelfParam);
                if (shelfNum == null) {
                    //上架入库量
                    shelfNum = shelfIdepotItemDao.getshelfInNum(saleShelfParam);
                }
                if (shelfNum == null) {
                    resultMap.put("code", "01");
                    resultMap.put("message", "PO单号:" + transferOrder.getPoNo() + ",调拨入商品货号:" + itemModel.getInGoodsNo() + "找不到对应PO+商品货号上架记录");
                    return resultMap;
                }
            }
        }

        String signTopidealCode = topidealCode;
        //判断仓库使用当前商家秘钥,还是关联商家的秘钥 1商家key 2关联商家key
        if (outdepot.getIsMerchant() != null && outdepot.getIsMerchant().equals(DERP_SYS.DEPOTINFO_ISMERCHANT_2)) {
            Map<String, Object> aMap = new HashMap<String, Object>();
            aMap.put("merchantId", outdepot.getMerchantId());
            ApiSecretConfigMongo apiSecret = apiSecretConfigMongoDao.findOne(aMap);
            if (apiSecret == null) {
                resultMap.put("code", "01");
                resultMap.put("message", "提交审核调出仓库关联商家秘钥不存在");
                return resultMap;
            }
            signTopidealCode = apiSecret.getTopidealCode();

        }

        //根据调入调出仓库“进出接口指令”判断是否推送接口
        boolean isOutStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(outDepotRel.getIsInOutInstruction());
        boolean isInStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(inDepotRel.getIsInOutInstruction());
        if (isOutStruction || isInStruction) {
            SendResult result = null;
            boolean flag = false;
            //（1）调拨出、调拨入仓仓库均是 在对应商家下的“进出接口指令”标识为“是”的，推送指令跨境宝，调拨出库仓和调拨入库仓库必填，调出和调入商品必填；
            if (isOutStruction && isInStruction) {
                result = sendTransferInstruct(topidealCode, signTopidealCode, transferOrder, indepot, outdepot, itemList);
                flag = true;
            } else if (isOutStruction && !isInStruction) {
                //（2）调拨出仓仓库 在对应商家下的“进出接口指令”标识为“是”，且调拨入仓仓库 在对应商家下的“进出接口指令”标识为“否”的
                //a、若出库仓类型是海外仓，调用1.17销售出仓订单
                if (DERP_SYS.DEPOTINFO_TYPE_C.equals(outdepot.getType())) {
                    result = sendSaleOut(topidealCode, outdepot, transferOrder, itemList);
                    flag = true;
                }
                //b、若出库仓类型是保税仓，调用1.49出库订单推送
                if (DERP_SYS.DEPOTINFO_TYPE_A.equals(outdepot.getType())) {
                    result = sendOutStore(topidealCode, outdepot, transferOrder, itemList);
                    flag = true;
                }
            } else if (!isOutStruction && isInStruction) {
                //(3) 调拨出仓仓库 在对应商家下的“进出接口指令”标识为“否”，且调拨入仓仓库 在对应商家下的“进出接口指令”标识为“是”的 ，推送1.13采购入库
                result = sendPurchaseAdd(signTopidealCode, indepot, transferOrder, itemList);
                flag = true;

            }
            if (result == null && flag) {
                resultMap.put("code", "01");
                resultMap.put("message", "调拨提交审核调拨服务异常");
                return resultMap;
            }
            if (!result.getSendStatus().name().equals("SEND_OK") && flag) {//SEND_OK-成功
                resultMap.put("code", "01");
                resultMap.put("message", "调拨提交审核发送采购入库失败");
                return resultMap;
            }
        }

        //调拨指令发送成功冻结库存
        List<InventoryFreezeGoodsListJson> freeGoodList = new ArrayList<>();//冻结库存商品列表
        for (int n = 0; n < itemList.size(); n++) {
            TransferOrderItemModel orderItem = itemList.get(n);
            //冻结商品
            InventoryFreezeGoodsListJson freezeGoods = new InventoryFreezeGoodsListJson();
            freezeGoods.setGoodsId(String.valueOf(orderItem.getOutGoodsId()));
            freezeGoods.setGoodsName(orderItem.getOutGoodsName());
            freezeGoods.setGoodsNo(orderItem.getOutGoodsNo());
            freezeGoods.setDivergenceDate(TimeUtils.formatFullTime(new Date()));
            freezeGoods.setNum(orderItem.getTransferNum());

            if (outdepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C) && transferOrder.getTallyingUnit() != null) {
                //理货单位 00-托盘 01-箱 02-件
                if (transferOrder.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
                    freezeGoods.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱  2 件
                } else if (transferOrder.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
                    freezeGoods.setUnit(DERP.INVENTORY_UNIT_1);
                } else if (transferOrder.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
                    freezeGoods.setUnit(DERP.INVENTORY_UNIT_2);
                }
            }
            freeGoodList.add(freezeGoods);
        }
        InventoryFreezeRootJson freezeRootJson = new InventoryFreezeRootJson();
        freezeRootJson.setMerchantId(String.valueOf(transferOrder.getMerchantId()));
        freezeRootJson.setMerchantName(transferOrder.getMerchantName());
        freezeRootJson.setDepotId(String.valueOf(transferOrder.getOutDepotId()));
        freezeRootJson.setDepotName(transferOrder.getOutDepotName());
        freezeRootJson.setOrderNo(transferOrder.getCode());
        freezeRootJson.setSource(SourceStatusEnum.DBDD.getKey());
        freezeRootJson.setSourceType(InventoryStatusEnum.DBCK.getKey());
        freezeRootJson.setSourceDate(TimeUtils.formatFullTime(new Date()));
        freezeRootJson.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_0);//冻增\冻减	字符串 （0冻结，1解冻）
        freezeRootJson.setBusinessNo(transferOrder.getCode());
        freezeRootJson.setGoodsList(freeGoodList);
        net.sf.json.JSONObject jsonFree = net.sf.json.JSONObject.fromObject(freezeRootJson);
        rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());

        //调拨指令发送成功更新调拨状态
        transferOrder.setStatus(DERP_ORDER.TRANSFERORDER_STATUS_014);//已提交
        transferOrder.setSubmitOne(userId);
        transferOrder.setSubmitTime(TimeUtils.getNow());
        transferOrder.setSubmitUsername(name);
        transferOrderDao.modify(transferOrder);
        resultMap.put("code", "00");
        resultMap.put("message", "保存并审核成功！");
        return resultMap;
    }

    /**
     * 唯品账单爬虫商品货号未核销量
     *
     * @param query
     * @param merchantId
     * @return
     * @throws Exception
     */
    private Integer getQueryWriteOffNum(Map<String, String> query, Long merchantId) throws Exception {
        String poNo = query.get("poNo");
        String goodsNo = query.get("goodsNo");
        String depotId = query.get("depotId");
        /**公共查询条件*/
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("merchantId", merchantId);
        paramMap.put("depotId", depotId);
        paramMap.put("poNo", poNo);
        paramMap.put("goodsNoList", Arrays.asList(goodsNo));

        //1.上架入库量
        Integer shelfInNum = shelfIdepotItemDao.getshelfInNum(paramMap);
        if (shelfInNum == null) shelfInNum = 0;

        //2.临时结转量
        Integer jztemNum = pojzTempDao.getPojzNum(paramMap);
        if (jztemNum == null) jztemNum = 0;

        //3.账单入库量
        paramMap.put("operateType", DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//库存调整类型  0 调减 1调增
        Integer billInNum = billOutinDepotItemDao.getBillOutInDepotNum(paramMap);
        if (billInNum == null) billInNum = 0;

        //4.账单出库量
        paramMap.put("operateType", DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//库存调整类型  0 调减 1调增
        Integer billOutNum = billOutinDepotItemDao.getBillOutInDepotNum(paramMap);
        if (billOutNum == null) billOutNum = 0;
        //唯品暂存区
        Map<String, Object> depotParam = new HashMap<>();
        depotParam.put("depotCode", "WPTH001");
        DepotInfoMongo wzcDepot = depotInfoMongoDao.findOne(depotParam);
        //唯品备查库
        depotParam.put("depotCode", "VIP001");
        DepotInfoMongo vipDepot = depotInfoMongoDao.findOne(depotParam);

        //5.调拨入量=调拨单调出仓库为“唯品暂存区”，且调入仓库为“唯品备查库” 已入库的入库数量
        paramMap.put("outDepotId", wzcDepot.getDepotId());
        paramMap.put("inDepotId", vipDepot.getDepotId());
        Integer transferInNum = transferOrderDao.getTransferInNumByMap(paramMap);
        if (transferInNum == null) transferInNum = 0;

        //6.调拨出量=调拨单调出仓库为“唯品备查库”，调入仓库为“唯品暂存区” 已出库的数量
        paramMap.put("outDepotId", vipDepot.getDepotId());
        paramMap.put("inDepotId", wzcDepot.getDepotId());
        Integer transferOutNum = transferOrderDao.getTransferOutNumByMap(paramMap);
        if (transferOutNum == null) transferOutNum = 0;

        //7.销售退货数量
        Integer saleRetrunNum = saleReturnOrderDao.getReturnCount(paramMap); // 退货量(好品+坏品)
        if (saleRetrunNum == null) saleRetrunNum = 0;

        //可核销量=上架入库量+临时结转量+账单入库量+调拨入量-账单出库量-调拨出量-销售退货量
        Integer verifiNum = shelfInNum + jztemNum + billInNum + transferInNum - billOutNum - transferOutNum - saleRetrunNum;
        return verifiNum;
    }

    /**
     * 推送调拨指令接口
     *
     * @return
     * @Param
     */
    private SendResult sendTransferInstruct(String topidealCode, String signTopidealCode, TransferOrderModel transferOrder,
                                            DepotInfoMongo indepot, DepotInfoMongo outdepot, List<TransferOrderItemModel> itemList) throws Exception {
        //服务类型 E0：区内调拨调出服务 F0：区内调拨调入服务 G0：库内调拨
        Map<String, String> ServeTypesMap = new HashMap<>();
        ServeTypesMap.put("E0", "01");
        ServeTypesMap.put("F0", "F0");
        ServeTypesMap.put("G0", "02");
        //业务场景10：账册内调仓,20：账册内货号变更,30：账册内货号变更调仓,40：账册内货权转移,50：账册内货权转移调仓,60：区内跨海关账册调入,70：区内跨海关账册调出,80：非实物调拨
        Map<String, String> busiSceneMap = new HashMap<>();
        busiSceneMap.put("10", "05");
        busiSceneMap.put("20", "20");
        busiSceneMap.put("30", "30");
        busiSceneMap.put("40", "04");
        busiSceneMap.put("50", "03");
        busiSceneMap.put("60", "60");
        busiSceneMap.put("70", "70");
        busiSceneMap.put("80", "80");
        //查询调入商家
        Map<String, Object> cparamMap = new HashMap<String, Object>();
        cparamMap.put("merchantId", transferOrder.getInCustomerId());
        MerchantInfoMongo inMerchant = merchantInfoMongoDao.findOne(cparamMap);

        //拼装参数
        TransferOrderRootJson rootJson = new TransferOrderRootJson();//transferOrderDao.searchVoById(transferOrder.getId(),user.getMerchantId());
        rootJson.setDatasource("DISTRIBUTED");//数据来源 经分销：DISTRIBUTED（经分销此字段必填）
        // 出入库都是是保税仓 是否同关区必填   我们的同关区 数值和跨境宝是相反的
        if (indepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && outdepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)) {
            String isSameArea = transferOrder.getIsSameArea();//是否同关区（0-否，1-是）
            if (DERP.ISSAMEAREA_0.equals(isSameArea)) {
                rootJson.setDistrict_type("1");
            }
            if (DERP.ISSAMEAREA_1.equals(isSameArea)) {
                rootJson.setDistrict_type("0");
            }

        }
        rootJson.setTransportbl_no(transferOrder.getFirstLadingBillNo());//头程提单号
        rootJson.setOrder_id(transferOrder.getCode());//企业调拨单号
        if (!StringUtils.isEmpty(transferOrder.getModel())
                && !transferOrder.getModel().equals(DERP_ORDER.TRANSFERORDER_MODEL_10)) {//账册内调仓
            if (inMerchant != null) rootJson.setTo_electric_code(inMerchant.getTopidealCode());//调入电商企业编号
        } else {
            rootJson.setTo_electric_code(topidealCode);//调入电商企业编号
        }
        rootJson.setOdate(TimeUtils.formatFullTime());//调拨时间yyyy-mm-dd HH:mi:ss
        if (!StringUtils.isEmpty(outdepot.getCustomsNo())) {
            rootJson.setCustoms_code(outdepot.getCustomsNo());//海关编码
        }
        if (!StringUtils.isEmpty(outdepot.getInspectNo())) {
            rootJson.setCiqb_code(outdepot.getInspectNo());//国检编码
        }

        //rootJson.setBusi_scene(busiSceneMap.get(transferOrder.getModel()));//业务场景
        /*if(outdepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)&&indepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)){
            rootJson.setServe_types(ServeTypesMap.get(transferOrder.getServeTypes()));//服务类型
        }*/
        String busi_scene = "";
        String isSameArea = transferOrder.getIsSameArea();
        String Serve_types = "";//01 仓间调拨  02仓内调拨
        // 不同同关区 账册能货权转移04  Serve_types 为仓间调拨
        if ("0".equals(isSameArea)) {
            Serve_types = "01";
            busi_scene = "04";
        }
        // 不同同关区 账册能货权转移04  Serve_types 为仓间调拨
        if ("1".equals(isSameArea)) {
            Serve_types = "01";
            busi_scene = "03";
        }

        rootJson.setServe_types(Serve_types);//服务类型
        rootJson.setBusi_scene(busi_scene);//业务场景
        rootJson.setRecadd(transferOrder.getReceivingAddress());//收货地址
        rootJson.setTrust_code(topidealCode);//委托单位 卓志编码
        rootJson.setFrom_store_code(outdepot.getCode());//调出仓库
        rootJson.setTo_store_code(indepot.getCode());//调入仓库
        rootJson.setContract_no(transferOrder.getContractNo());//合同号
        rootJson.setInvoice_no(transferOrder.getInvoiceNo());//发票号
        rootJson.setPort_loading(transferOrder.getPortLoading());//装货港
        rootJson.setPack_type(transferOrder.getPackType());//包装方式
        rootJson.setCartons(transferOrder.getCartons());//箱数
        rootJson.setPay_rules(transferOrder.getPayRules());//付款条约
        String billWeight = transferOrder.getBillWeight() == null ? null : transferOrder.getBillWeight().toString();
        rootJson.setBill_weight(billWeight);//提单毛重
        rootJson.setPort_dest_no("44011501");//卸货港编码	44011501：南沙新港口岸 测试暂时写死
        rootJson.setMark(transferOrder.getMark());//唛头
        rootJson.setOverseas_shipper(transferOrder.getShipper());//境外发货人
        rootJson.setDestion_code(transferOrder.getDestination() != null ? transferOrder.getDestination() : "");//目的地代码 HP01-黄埔状元谷 HK01-香港本地 GZJC01-广州机场
        rootJson.setReceiver_name(transferOrder.getConsigneeUsername() != null ? transferOrder.getConsigneeUsername() : "");//收货人姓名
        rootJson.setReceiver_country(transferOrder.getCountry() != null ? transferOrder.getCountry() : "");//国家 字符串（中文、如：中国）
        rootJson.setFrom_store_type(outdepot.getISVDepotType());
        rootJson.setTo_store_type(indepot.getISVDepotType());

        //保税仓调海外仓
        if (outdepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && indepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
            rootJson.setRe_service_type("10");//10：退运服务（默认）20：销毁服务 30：跨关区转出转关服务
        }
        if (outdepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C) || indepot.getType().matches(DERP_SYS.DEPOTINFO_TYPE_C)) {
            rootJson.setUom(transferOrder.getTallyingUnit());//海外仓理货单位  00：托盘，01：箱 , 02：件
        }

        Integer total_num = 0;//件数合计数量
        List<TransferGoodsItemJson> goodList = new ArrayList<>();//调拨指令商品列表
        Map<String, TransferGoodsItemJson> sumMap = new LinkedHashMap<>();
        int index = 1;
        for (int k = 0; k < itemList.size(); k++) {

            TransferOrderItemModel orderItem = itemList.get(k);
            TransferGoodsItemJson itemTemp = new TransferGoodsItemJson();

            itemTemp.setIndex(index);//序号
            itemTemp.setFrom_good_Id(orderItem.getOutGoodsNo());//调出货号
            itemTemp.setGoods_name(orderItem.getOutGoodsName());//调出商品名称
            itemTemp.setTo_good_Id(orderItem.getInGoodsNo());//调入货号

            // 保税仓互调 并且跨关区,推送外部默认好品
            if (indepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && outdepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && DERP.ISSAMEAREA_0.equals(transferOrder.getIsSameArea())) {
                itemTemp.setStock_type(DERP.ISDAMAGE_0);
            }
            //保税仓调海外仓
            if (outdepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && indepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                itemTemp.setStock_type(DERP.ISDAMAGE_0);//库存类型 0:好品;1:坏品;
            }
            Integer transferNum = orderItem.getTransferNum();
            itemTemp.setBrand(orderItem.getBrandName());//品牌类型
            if (orderItem.getPrice() != null && orderItem.getPrice().doubleValue() >= 0) {
                itemTemp.setPrice(orderItem.getPrice().toString());//采购单价
            }
            String weight = orderItem.getGrossWeightSum() == null ? "" : String.valueOf(orderItem.getGrossWeightSum());
            String netWeight = orderItem.getNetWeightSum() == null ? "" : String.valueOf(orderItem.getNetWeightSum());
            itemTemp.setWeight(weight);//毛重
            itemTemp.setNet_weight(netWeight);//净重
            itemTemp.setCont_no(orderItem.getContNo());//箱号
            itemTemp.setBargainno(orderItem.getBargainno());//合同号
            total_num += transferNum;
            itemTemp.setAmount(transferNum);//调出数量
            itemTemp.setIn_amount(orderItem.getInTransferNum());//调入数量
            itemTemp.setIs_tracesrc("0");  //是否溯源 0：不溯源

            sumMap.put(itemTemp.getFrom_good_Id(), itemTemp);
            index++;

        }
        rootJson.setTotal_num(total_num);//件数合计数量

        if (!sumMap.isEmpty()) {
            rootJson.setGood_list(new ArrayList<>(sumMap.values()));
        }

        //调调拨指令接口
        net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(rootJson);
        json.put("method", EpassAPIMethodEnum.EPASS_E04_METHOD.getMethod());
        json.put("topideal_code", signTopidealCode);
        SendResult result = rocketMQProducer.send(json.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(), MQPushApiEnum.PUSH_EPASS.getTags());
        System.out.println("发送调拨指令消息返回：" + result.toString());
        return result;
    }

    /**
     * 推送销售出库接口
     *
     * @return
     * @Param
     */
    private SendResult sendSaleOut(String topidealCode, DepotInfoMongo outdepot, TransferOrderModel transferOrder, List<TransferOrderItemModel> itemList) throws Exception {
        List<SalesOutStoreGoodsItem> goodsList = new ArrayList<>();
        SalesOutStoreOrderRoot root = new SalesOutStoreOrderRoot();
        root.setOrder_id(transferOrder.getCode()); //订单编码
        root.setWarehouse_id(outdepot.getCode()); //仓库编码
        root.setDestion_code(transferOrder.getDestination()); //目的地
        root.setBusi_mode("10"); //进口模式 10：B2B
        Integer totalNum = 0;
        Map<String, SalesOutStoreGoodsItem> sumMap = new LinkedHashMap<>();
        for (int k = 0; k < itemList.size(); k++) {
            TransferOrderItemModel orderItem = itemList.get(k);
            SalesOutStoreGoodsItem itemTemp = new SalesOutStoreGoodsItem();
            itemTemp.setGoods_id(orderItem.getOutGoodsNo()); //商品货号
            itemTemp.setGoods_name(orderItem.getOutGoodsName()); //商品名称
            itemTemp.setAmount(orderItem.getTransferNum()); //数量
            itemTemp.setUom(orderItem.getOutUnit()); //理货单位
            totalNum += orderItem.getTransferNum();

            sumMap.put(itemTemp.getGoods_id(), itemTemp);
        }
        SalesOutStoreRecipient recipient = new SalesOutStoreRecipient();
        recipient.setName(transferOrder.getConsigneeUsername()); //收货人姓名
        recipient.setCountry(transferOrder.getCountry()); //国家
        recipient.setAddress(transferOrder.getReceivingAddress()); //地址
        root.setTotal_num(totalNum);
        if (!sumMap.isEmpty()) {
            root.setGoods_list(new ArrayList<>(sumMap.values()));
        }
        root.setRecipient(recipient);
//        JSONObject jsonObject = JSONObject.fromObject(root);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(root);
        jsonObject.put("method", EpassAPIMethodEnum.EPASS_E03_METHOD.getMethod());
        jsonObject.put("topideal_code", topidealCode);
        SendResult result = rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(), MQPushApiEnum.PUSH_EPASS.getTags());
        System.out.println("调拨审核发送销售出仓消息返回：" + result.toString());
        return result;
    }

    /**
     * 推送出库订单接口
     *
     * @return
     * @Param
     */
    private SendResult sendOutStore(String topidealCode, DepotInfoMongo outdepot, TransferOrderModel transferOrder, List<TransferOrderItemModel> itemList) throws Exception {
        List<OutStoreOrderItemJson> goodsList = new ArrayList<>();
        OutStoreOrderRootJson root = new OutStoreOrderRootJson();
        root.setOrder_id(transferOrder.getCode()); //订单编码
        root.setWarehouse_code(outdepot.getCode()); //仓库编码
        root.setBusi_scene("20"); //业务场景类型 20：调拨出库
        root.setCreated_time(TimeUtils.formatFullTime(transferOrder.getCreateDate()));
        root.setEbc_code(topidealCode);
        root.setCustoms_code(outdepot.getCustomsNo());
        root.setContract_no(transferOrder.getContractNo());
        Map<String, OutStoreOrderItemJson> sumMap = new LinkedHashMap<>();
        int index = 1;
        for (int k = 0; k < itemList.size(); k++) {
            TransferOrderItemModel orderItem = itemList.get(k);
            OutStoreOrderItemJson itemTemp = new OutStoreOrderItemJson();
            itemTemp.setIndex(index);
            itemTemp.setGood_id(orderItem.getOutGoodsNo());
            itemTemp.setAmount(orderItem.getTransferNum()); //数量
            itemTemp.setUnit_price(orderItem.getPrice()); //单价
            itemTemp.setGross_weight(orderItem.getGrossWeightSum());  // 毛重
            itemTemp.setNet_weight(orderItem.getNetWeightSum());   // 净重
            HashMap<String, Object> map = new HashMap<>();
            map.put("merchandiseId", orderItem.getOutGoodsId());
            MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(map);
            if (merchandise.getUnit() != null && merchandise.getUnit().longValue() > 0) {
                HashMap<String, Object> map2 = new HashMap<>();
                map2.put("unitId", merchandise.getUnit());
                UnitMongo unit = unitMongoDao.findOne(map2);
                itemTemp.setContracts_unit(unit.getCode());  // 单位
            }

            sumMap.put(itemTemp.getGood_id(), itemTemp);
            index++;
        }
        if (!sumMap.isEmpty()) {
            root.setGood_list(new ArrayList<>(sumMap.values()));
        }
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(root);
        jsonObject.put("method", EpassAPIMethodEnum.EPASS_E08_METHOD.getMethod());
        jsonObject.put("topideal_code", topidealCode);
        SendResult result = rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(), MQPushApiEnum.PUSH_EPASS.getTags());
        System.out.println("调拨审核发送出库订单消息返回：" + result.toString());
        return result;
    }

    /**
     * 推送采购入库接口
     *
     * @return
     * @Param
     */
    private SendResult sendPurchaseAdd(String signTopidealCode, DepotInfoMongo indepot, TransferOrderModel transferOrder, List<TransferOrderItemModel> itemList) throws Exception {
        PurchaseOrderRootJson rootJson = new PurchaseOrderRootJson();
        rootJson.setEnt_inbound_id(transferOrder.getCode());//企业入仓编号
        rootJson.setContract_no(transferOrder.getContractNo());//合同号
        rootJson.setWarehouse_id(indepot.getCode());//仓库编码
        rootJson.setInvoice_no(transferOrder.getInvoiceNo());//发票号
        rootJson.setServer_types(""); //服务类型
        rootJson.setRecadd(transferOrder.getReceivingAddress());//收货地址
        rootJson.setPort_loading(transferOrder.getPortLoading());//装货港
        rootJson.setPack_type("9999");//包装方式
        rootJson.setCartons(transferOrder.getCartons());//箱数
        rootJson.setPay_rules("");//付款条约
        String billWeight = transferOrder.getBillWeight() == null ? null : transferOrder.getBillWeight().toString();
        rootJson.setBill_weight(billWeight);//提单毛重
        rootJson.setPort_dest_no("44011501");//卸货港编码
        rootJson.setPort_destination("南沙新港口岸");//卸货港名称
        rootJson.setMark(transferOrder.getMark());//唛头
        rootJson.setOverseas_shipper(transferOrder.getShipper()); //境外发货人
        rootJson.setDatasource("DISTRIBUTED");
        rootJson.setBusiness_moshi(DERP_ORDER.ORDER_IMPORTMODE_10); //业务模式
        if (indepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) { //香港仓必填
            rootJson.setStatus("1"); //1：正常
            rootJson.setUom(transferOrder.getTallyingUnit());
        }
        List<PurchaseGoodsListJson> goodList = new ArrayList<>();//调拨指令商品列表
        Map<String, PurchaseGoodsListJson> sumMap = new LinkedHashMap<>();
        for (int k = 0; k < itemList.size(); k++) {
            TransferOrderItemModel orderItem = itemList.get(k);
            PurchaseGoodsListJson itemTemp = new PurchaseGoodsListJson();
            itemTemp.setIndex(String.valueOf(k + 1));//序号
            itemTemp.setBrand(orderItem.getBrandName());
            itemTemp.setPrice(String.format("%.2f", orderItem.getPrice()));
            itemTemp.setAmount(String.valueOf(orderItem.getInTransferNum()));
            itemTemp.setWeight(String.valueOf(orderItem.getGrossWeightSum()));
            itemTemp.setNet_weight(String.valueOf(orderItem.getNetWeightSum()));
            itemTemp.setCont_no(orderItem.getContNo());
            itemTemp.setBargainno(orderItem.getBargainno());
            itemTemp.setIs_tracesrc("0");  //是否溯源 0：不溯源
            itemTemp.setNote(orderItem.getRemark()); //备注
            itemTemp.setGoods_name(orderItem.getInGoodsName());
            itemTemp.setGoods_id(orderItem.getInGoodsNo());
            sumMap.put(itemTemp.getGoods_id(), itemTemp);

        }
        if (!sumMap.isEmpty()) {
            rootJson.setGoods_list(new ArrayList<>(sumMap.values()));
        }
        //调采购入库接口
//        JSONObject json = JSONObject.fromObject(rootJson);
        JSONObject json = (JSONObject) JSONObject.toJSON(rootJson);
        json.put("method", EpassAPIMethodEnum.EPASS_E01_METHOD.getMethod());
        json.put("topideal_code", signTopidealCode);
        SendResult result = rocketMQProducer.send(json.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(), MQPushApiEnum.PUSH_EPASS.getTags());
        System.out.println("调拨审核发送采购入库消息返回：" + result.toString());
        return result;
    }

    /**
     * 设置调拨订单的业务场景和服务类型
     *
     * @return
     * @Param
     */
    private void setModelAndServeTypes(DepotInfoMongo outDepot, DepotInfoMongo inDepot, TransferOrderModel transferOrderModel,
                                       String inTopidealCode, String topidealCode, Long id) {
        //同关区
        boolean isSame = !StringUtils.isEmpty(outDepot.getCustomsNo()) && !StringUtils.isEmpty(inDepot.getCustomsNo())
                && outDepot.getCustomsNo().equals(inDepot.getCustomsNo());

        //1.调出调入商家不同,出和入的仓库编码相同 -业务场景：40 ：账册内货权转移   服务类型：G0：库内调拨服务
        if (!topidealCode.equals(inTopidealCode) && outDepot.getCode().equals(inDepot.getCode())) {
            transferOrderModel.setModel(DERP_ORDER.TRANSFERORDER_MODEL_40);
            transferOrderModel.setServeTypes(DERP_ORDER.TRANSFERORDER_SERVETYPES_G0);
        }
        //2.调出调入商家不同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：50： 账册内货权转移调仓   服务类型：E0：区内调拨调出服务
        else if (!topidealCode.equals(inTopidealCode) && !outDepot.getCode().equals(inDepot.getCode())) {
            if ((id != null && isSame) || id == null) {
                transferOrderModel.setModel(DERP_ORDER.TRANSFERORDER_MODEL_50);
                transferOrderModel.setServeTypes(DERP_ORDER.TRANSFERORDER_SERVETYPES_E0);
            }

        }
        //3.调出调入商家相同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：50： 账册内货权转移调仓   服务类型：E0：区内调拨调出服务
        else if (topidealCode.equals(inTopidealCode) && isSame
                && !outDepot.getCode().equals(inDepot.getCode())) {
            transferOrderModel.setModel(DERP_ORDER.TRANSFERORDER_MODEL_50);
            transferOrderModel.setServeTypes(DERP_ORDER.TRANSFERORDER_SERVETYPES_E0);
        }
        //4.出入仓库不同关区-业务场景：空   服务类型：空
        else if (!(isSame || (StringUtils.isEmpty(outDepot.getCustomsNo()) && StringUtils.isEmpty(inDepot.getCustomsNo())))) {
            transferOrderModel.setModel("");
            transferOrderModel.setServeTypes("");
        }
    }

    /**
     * 商品导入
     */
    @Override
    public Map importTransferGoods(List<List<Map<String, String>>> data, String outDepotId, String inDepotId, User user) {
        List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> stringList = new ArrayList<Map<String, String>>();
        List<String> idList = new ArrayList<>();
        ArrayList<String> checkSeqList = new ArrayList<>(); // 存一次导入时序号的集合
        ArrayList<String> checkGoodsList = new ArrayList<>(); // 存一次导入时调出货号的集合
        ArrayList<String> checkInGoodsList = new ArrayList<>(); // 存一次导入时调入货号的集合

        Integer success = 0;
        Integer pass = 0;
        Integer failure = 0;

        //调出仓信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("depotId", Long.valueOf(outDepotId));
        DepotInfoMongo outDepotInfo = depotInfoMongoDao.findOne(params);

        // 调出仓库公司关联表
        Map<String, Object> outDepotMerchantRelParam = new HashMap<String, Object>();
        outDepotMerchantRelParam.put("merchantId", user.getMerchantId());
        outDepotMerchantRelParam.put("depotId", outDepotInfo.getDepotId());
        DepotMerchantRelMongo depotMerchantRelMongo1 = depotMerchantRelMongoDao.findOne(outDepotMerchantRelParam);
        if (depotMerchantRelMongo1 == null || "".equals(depotMerchantRelMongo1)) {
            Map<String, String> msg = new HashMap<String, String>();
            msg.put("row", "");
            msg.put("msg", "调出仓库ID为：" + outDepotInfo.getDepotId() + ",未查到该公司下调出仓库信息");
            msgList.add(msg);
            failure += 1;
        }

        //调入仓信息
        params.put("depotId", Long.valueOf(inDepotId));
        DepotInfoMongo inDepotInfo = depotInfoMongoDao.findOne(params);
        // 调入仓库公司关联表
        Map<String, Object> inDepotMerchantRelParam = new HashMap<String, Object>();
        inDepotMerchantRelParam.put("merchantId", user.getMerchantId());
        inDepotMerchantRelParam.put("depotId", inDepotInfo.getDepotId());
        DepotMerchantRelMongo inDepotMerchantRelMongo = depotMerchantRelMongoDao.findOne(inDepotMerchantRelParam);
        if (inDepotMerchantRelMongo == null || "".equals(inDepotMerchantRelMongo)) {
            Map<String, String> msg = new HashMap<String, String>();
            msg.put("row", "");
            msg.put("msg", "调入仓库ID为：" + inDepotId + ",未查到该公司下调出仓库信息");
            msgList.add(msg);
            failure += 1;
        }
        List<Map<String, String>> objects = data.get(0);
        if (failure == 0) {
            for (int j = 0; j < objects.size(); j++) {
                Map<String, String> msg = new HashMap<String, String>();
                Map<String, String> map = objects.get(j);
                String seq = map.get("序号");
                if (seq == null || "".equals(seq)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "存在序号为空的导入数据！");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                seq = seq.trim();

                if (!isInteger(seq)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "存在非数值的序号！");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                if (checkSeqList.contains(seq)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "存在序号重复的导入数据！");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                checkSeqList.add(seq);
                String goodsNo = map.get("调出商品货号");
                if (goodsNo == null || "".equals(goodsNo)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "调出商品货号不能为空");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                goodsNo = goodsNo.trim();
                if (checkGoodsList.contains(goodsNo)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "存在调出商品货号:" + goodsNo + "重复的导入数据！");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                String inGoodsNo = map.get("调入商品货号");
                if (checkInGoodsList.contains(inGoodsNo)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "存在调入商品货号:" + inGoodsNo + "重复的导入数据！");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }

                checkGoodsList.add(goodsNo);
                checkInGoodsList.add(inGoodsNo);
                String num = map.get("调出数量");
                if (num == null || "".equals(num)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "调出数量不能为空");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                num = num.trim();

                String price = map.get("调拨单价");
                if (price == null || "".equals(price)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "调拨单价不能为空！");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                Double priceSum = 0.0;
                try {
                    priceSum = Double.valueOf(price);
                } catch (Exception e) {
                }
                if (priceSum < 0.0) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "调拨单价不能小于0！");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                price = price.trim();

                String grossWeight = map.get("毛重（KG）");
                if (org.apache.commons.lang3.StringUtils.isBlank(grossWeight)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "毛重不能为空");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                grossWeight = grossWeight.trim();
                if (!isNumber(grossWeight)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "毛重只能输入两位小数");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                String netWeight = map.get("净重（KG）");
                if (org.apache.commons.lang3.StringUtils.isBlank(netWeight)) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "净重不能为空");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                netWeight = netWeight.trim();

                HashMap<String, Object> goodsMap = new HashMap<>();
                goodsMap.put("goodsNo", goodsNo);
                goodsMap.put("merchantId", user.getMerchantId());
                MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(goodsMap);
                if (goods == null) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "导入的商品货号不在该公司的商品信息表中！");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }

                //调出商品
                Map<String, Object> merDepotParams = new HashMap<>();
                merDepotParams.put("merchandiseId", goods.getMerchandiseId());
                List<MerchandiseInfoMogo> outMerchandiseInfoMogos = merchandiseInfoMogoDao.findMerchandiseByDepotId(merDepotParams, outDepotInfo.getDepotId());
                if (outMerchandiseInfoMogos == null || outMerchandiseInfoMogos.size() == 0) {
                    msg.put("row", String.valueOf(j + 2));
                    msg.put("msg", "调出货号没有在对应的调出仓库下存在" + goods.getGoodsNo());
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }

                //调入商品
                MerchandiseInfoMogo inGoods = null;
                if (org.apache.commons.lang3.StringUtils.isNotBlank(inGoodsNo)) {
                    HashMap<String, Object> inGoodsMap = new HashMap<>();
                    inGoodsMap.put("goodsNo", inGoodsNo);
                    inGoodsMap.put("merchantId", user.getMerchantId());
                    inGoods = merchandiseInfoMogoDao.findOne(inGoodsMap);
                    if (inGoods == null) {
                        msg.put("row", String.valueOf(j + 2));
                        msg.put("msg", "导入的调入商品货号不在该公司的商品信息表中！");
                        msgList.add(msg);
                        failure += 1;
                        continue;
                    }
                    if (!inGoods.getBarcode().equals(goods.getBarcode())) {
                        msg.put("row", String.valueOf(j + 2));
                        msg.put("msg", "调出商品和调入商品的条码不一致！");
                        msgList.add(msg);
                        failure += 1;
                        continue;
                    }

                    merDepotParams.put("merchandiseId", inGoods.getMerchandiseId());
                    List<MerchandiseInfoMogo> inMerchandiseInfoMogos = merchandiseInfoMogoDao.findMerchandiseByDepotId(merDepotParams, inDepotInfo.getDepotId());
                    if (inMerchandiseInfoMogos == null || inMerchandiseInfoMogos.size() == 0) {
                        msg.put("row", String.valueOf(j + 2));
                        msg.put("msg", "调入货号没有在对应的调入仓库下存在" + goods.getGoodsNo());
                        msgList.add(msg);
                        failure += 1;
                        continue;
                    }
                }

                Map<String, String> stringMap = new HashMap<String, String>();
                stringMap.put("seq", seq);
                stringMap.put("outGoodsNo", goodsNo);
                stringMap.put("outGoodsId", goods.getMerchandiseId() + "");
                stringMap.put("outGoodsCode", goods.getGoodsCode());
                stringMap.put("outCommbarcode", goods.getCommbarcode());
                stringMap.put("outGoodsName", goods.getName());
                if (org.apache.commons.lang3.StringUtils.isNotBlank(inGoodsNo)) {
                    stringMap.put("inGoodsId", inGoods.getMerchandiseId() + "");
                    stringMap.put("inGoodsCode", inGoods.getGoodsCode());
                    stringMap.put("inGoodsNo", inGoods.getGoodsNo());
                    stringMap.put("inGoodsName", inGoods.getName());
                    stringMap.put("inCommbarcode", inGoods.getCommbarcode());

                }
                stringMap.put("num", num);
                stringMap.put("price", price);
                stringMap.put("grossWeight", grossWeight);
                stringMap.put("netWeight", netWeight);
                stringMap.put("outBarcode", map.get("条形码"));
                stringMap.put("boxNum", map.get("箱数"));
                stringMap.put("torrNo", map.get("托盘号"));
                stringList.add(stringMap);

                idList.add(goods.getMerchandiseId() + "");

            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msgList", msgList);
        map.put("idList", idList);
        map.put("stringList", stringList);
        map.put("success", success);
        map.put("pass", pass);
        map.put("failure", failure);
        return map;
    }

    /**
     * 判断是否是整数
     *
     * @param str
     * @return
     */
    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    @Override
    public List<TransferOrderItemDTO> exportListItem(String json) {
        List<TransferOrderItemDTO> itemList = new ArrayList<>();
        // 解析json
        net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject.fromObject(json);
        // 解析表体数据
        JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
        for (int i = 0; i < itemArr.size(); i++) {
            net.sf.json.JSONObject job = itemArr.getJSONObject(i);
            if (job.isNullObject() || job.isEmpty()) {
                continue;
            }

            Integer seq = org.apache.commons.lang3.StringUtils.isBlank((String) job.get("seq")) ? null : Integer.valueOf((String) job.get("seq"));
            String outGoodsNo = (String) job.get("outGoodsNo");
            String inGoodsNo = (String) job.get("inGoodsNo");
            String numStr = (String) job.getString("num");
            String priceStr = (String) job.get("price"); // 调拨单价
            String grossWeightSumStr = (String) job.get("grossWeightSum");
            String netWeightSumStr = (String) job.get("netWeightSum");
            String outBarcode = (String) job.get("outBarcode");
            String boxNumStr = (String) job.get("boxNum");
            String torrNoStr = (String) job.get("torrNo");

            Integer num = org.apache.commons.lang3.StringUtils.isBlank(numStr) ? null : Integer.valueOf(numStr.trim());
            Integer boxNum = org.apache.commons.lang3.StringUtils.isBlank(boxNumStr) ? null : Integer.valueOf(boxNumStr.trim());
            Double price = org.apache.commons.lang3.StringUtils.isBlank(priceStr.trim()) ? null : Double.valueOf(priceStr.trim());
            Double grossWeightSum = org.apache.commons.lang3.StringUtils.isBlank(netWeightSumStr) ? null : Double.valueOf(grossWeightSumStr);
            Double netWeightSum = org.apache.commons.lang3.StringUtils.isBlank(grossWeightSumStr) ? null : Double.valueOf(netWeightSumStr);

            // 注入数据
            TransferOrderItemDTO itemDTO = new TransferOrderItemDTO();
            itemDTO.setSeq(seq); // 加序号
            itemDTO.setOutGoodsNo(outGoodsNo);
            itemDTO.setInGoodsNo(inGoodsNo);
            itemDTO.setTransferNum(num);
            itemDTO.setPrice(price);
            itemDTO.setGrossWeightSum(grossWeightSum);
            itemDTO.setNetWeightSum(netWeightSum);
            itemDTO.setOutBarcode(outBarcode);
            itemDTO.setBoxNum(boxNum);
            itemDTO.setTorrNo(torrNoStr);

            itemList.add(itemDTO);

        }
        return itemList;
    }

    @Override
    public Map<String, Object> exportInDepotCustomsDeclares(Long id, List<Long> inFileTempIds) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        //调入仓库模板
        if (inFileTempIds == null || inFileTempIds.isEmpty()) {
            return null;
        }

        TransferOrderModel model = transferOrderDao.searchById(id);
        if (model == null) {
            return null;
        }

        //调拨订单详情
        TransferOrderItemModel itemModel = new TransferOrderItemModel();
        itemModel.setTransferOrderId(id);
        List<TransferOrderItemModel> itemModels = transferOrderItemDao.list(itemModel);

        for (Long fileTempId : inFileTempIds) {
            FileTempModel fileTempModel = fileTempDao.searchById(fileTempId);
            if (fileTempModel == null || StringUtils.isEmpty(fileTempModel.getToUrl())) {
                continue;
            }

            //南沙仓模板（之前）
            if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NANSHA.equals(fileTempModel.getToUrl())) {
                Map<String, Object> firstCustomsInfo = getNanShaInDepotFirstCustomsInfo(model, itemModels);
                resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
            }

            //骅增菜鸟仓模板
            if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_CNHUAZENG.equals(fileTempModel.getToUrl())) {
                Map<String, Object> firstCustomsInfo = getCNHuangZengInDepotFirstCustomsInfo(model, itemModels);
                resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
            } else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NBCIXI.equals(fileTempModel.getToUrl())) {
                Map<String, Object> firstCustomsInfo = getNBCIXIFirstCustomsInfoDTO(model, itemModels, "1");
                resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
            }

        }

        return resultMap;
    }

    @Override
    public Map<String, Object> exportOutDepotCustomsDeclares(Long id, List<Long> outFileTempIds) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        //调入仓库模板
        if (outFileTempIds == null || outFileTempIds.isEmpty()) {
            return null;
        }

        TransferOrderModel model = transferOrderDao.searchById(id);
        if (model == null) {
            return null;
        }

        //调拨订单详情
        TransferOrderItemModel itemModel = new TransferOrderItemModel();
        itemModel.setTransferOrderId(id);
        List<TransferOrderItemModel> itemModels = transferOrderItemDao.list(itemModel);

        for (Long fileTempId : outFileTempIds) {
            FileTempModel fileTempModel = fileTempDao.searchById(fileTempId);
            if (fileTempModel == null || StringUtils.isEmpty(fileTempModel.getToUrl())) {
                continue;
            }

            //南沙仓模板（之前）
            if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_ZONGHEYICANG.equals(fileTempModel.getToUrl())) {
                Map<String, Object> firstCustomsInfo = getNanShaOutDepotFirstCustomsInfo(model, itemModels);
                resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
            } else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NBCIXI.equals(fileTempModel.getToUrl())) {
                Map<String, Object> firstCustomsInfo = getNBCIXIFirstCustomsInfoDTO(model, itemModels, "2");
                resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
            }
        }

        return resultMap;
    }

    @Override
    public Map importPackingDetails(List<List<Map<String, String>>> data,User user,List<TransferOrderItemModel> itemList) throws Exception {
        Integer success = 0;
        Integer failure = 0;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<PackingDetailsModel> packingDetailsModels = new ArrayList<>();
        List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
        if(itemList ==null || itemList.size() < 1){
            resultMap.put("success", success);
            resultMap.put("failure", failure++);
            addErrorList(0, "商品信息为空，无法导入装箱明细", errorList);
            resultMap.put("errorList", errorList);
            resultMap.put("packingList", packingDetailsModels);
            return resultMap;
        }

        Map<String, List<TransferOrderItemModel>> itemModelMap = itemList.stream().collect(Collectors.groupingBy(TransferOrderItemModel::getInBarcode));

        List<Map<String, String>> objects = data.get(0);
        for (int j = 0; j < objects.size(); j++) {
            Map<String, String> map = objects.get(j);

            String cabinetNo = map.get("柜号");
            String torrNo = map.get("托盘号");

            String barcode = map.get("条形码");
            if (StringUtils.isEmpty(barcode)) {
                addErrorList(j, "条形码为空", errorList);
                failure++;
                continue;
            }
            List<TransferOrderItemModel> tranfItemList = itemModelMap.get(barcode);
            if (tranfItemList == null || tranfItemList.size() < 1) {
                addErrorList(j, "条形码:" + barcode + "在调拨明细中不存在", errorList);
                failure += 1;
                continue;
            }

            String boxNum = map.get("箱数");
            if (StringUtils.isEmpty(boxNum)) {
                addErrorList(j, "箱数为空", errorList);
                failure++;
                continue;
            }
            if (!isNumber(boxNum)) {
                addErrorList(j, "箱数不为数字", errorList);
                failure++;
                continue;
            }

            String piecesNum = map.get("件数");
            if (StringUtils.isEmpty(piecesNum)) {
                addErrorList(j, "件数为空", errorList);
                failure++;
                continue;
            }
            if (!isNumber(piecesNum)) {
                addErrorList(j, "件数不为数字", errorList);
                failure++;
                continue;
            }

            PackingDetailsModel detailsModel = new PackingDetailsModel();
            detailsModel.setTorrNo(torrNo);
            detailsModel.setGoodsNo(tranfItemList.get(0).getInGoodsNo());
            detailsModel.setBarcode(barcode);
            detailsModel.setBoxNum(Integer.valueOf(boxNum));
            detailsModel.setPiecesNum(Integer.valueOf(piecesNum));
            detailsModel.setCabinetNo(cabinetNo);
            detailsModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_2);
            packingDetailsModels.add(detailsModel);
        }

        //保存
        if (failure == 0) {
            success = data.size();
        }
        resultMap.put("success", success);
        resultMap.put("failure", failure);
        resultMap.put("errorList", errorList);
        resultMap.put("packingList", packingDetailsModels);
        return resultMap;
    }


    //判断小数点后2位的数字的正则表达式
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher match = pattern.matcher(str);
        return match.matches();
    }

    /**
     * 封装调出仓库南沙仓一线清关资料实体（之前的模板）
     */
    private Map<String, Object> getNanShaOutDepotFirstCustomsInfo(TransferOrderModel orderModel, List<TransferOrderItemModel> itemModels) throws Exception {
        //清关资料调出商品详情
        List<Map<String, Object>> outDtoList = new ArrayList<>();

        //根据商家id获取商家信息
        Map<String, Object> merchant_params = new HashMap<String, Object>();
        merchant_params.put("merchantId", orderModel.getMerchantId());
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchant_params);

        Map<String, Object> depotRelParam = new HashMap<>();
        depotRelParam.put("merchantId", orderModel.getMerchantId());
        depotRelParam.put("depotId", orderModel.getOutDepotId());
        DepotMerchantRelMongo outDepot = depotMerchantRelMongoDao.findOne(depotRelParam);

        Set<String> outOriginSet = new TreeSet<>(); //原产国
        for (TransferOrderItemModel item : itemModels) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("merchandiseId", item.getOutGoodsId());
            MerchandiseInfoMogo outGoods = merchandiseInfoMogoDao.findOne(params);
            if (outGoods != null) {
                Map<String, Object> outDto = new HashMap<>();
                outDto.put("goodsNo", outGoods.getGoodsNo());
                outDto.put("goodsName", outGoods.getName());
                outDto.put("goodsSpec", outGoods.getSpec());
                outDto.put("palletNo", "");
                outDto.put("boxNum", item.getBoxNum() == null ? 0 : item.getBoxNum());
                outDto.put("num", item.getTransferNum() == null ? 0 : item.getTransferNum());
                outDto.put("totalNetWeight", item.getNetWeightSum() == null ? 0 : item.getNetWeightSum());
                outDto.put("totalGrossWeight", item.getGrossWeightSum() == null ? 0 : item.getGrossWeightSum());
                outDto.put("constituentRatio", outGoods.getComponent());

                BigDecimal price = item.getPrice() == null ? BigDecimal.ZERO : new BigDecimal(item.getPrice().toString());
                outDto.put("price", price);

                if (item.getTransferNum() == null) {
                    outDto.put("amount", BigDecimal.ZERO);
                } else {
                    //总价=单价*数量
                    BigDecimal amount = price.multiply(new BigDecimal(item.getTransferNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    outDto.put("amount", amount);
                }

                if (outGoods.getUnit() != null) {
                    Map<String, Object> unitMap = new HashMap<>();
                    unitMap.put("unitId", outGoods.getUnit());
                    UnitMongo unitMongo = unitMongoDao.findOne(unitMap);
                    if (unitMongo != null) {
                        outDto.put("unit", unitMongo.getName());
                    }
                }

                if (outGoods.getCountyId() != null) {
                    Map<String, Object> countryParams = new HashMap<String, Object>();
                    countryParams.put("countryOriginId", outGoods.getCountyId());
                    CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(countryParams);
                    if (countryOrigin != null) {
                        outDto.put("countryName", countryOrigin.getName());
                        outOriginSet.add(countryOrigin.getName());
                    }
                }
                outDtoList.add(outDto);
            }
        }

        String outOriginName = org.apache.commons.lang3.StringUtils.join(outOriginSet, ";");

        // 根据“入库仓库+关区”获取关系表
        Map<String, Object> depotCustomsParams = new HashMap<String, Object>();
        depotCustomsParams.put("depotId", orderModel.getInDepotId());
        depotCustomsParams.put("customsAreaId", orderModel.getInCustomsId());
        DepotCustomsRelMongo depotcustoms = depotCustomsRelMongoDao.findOne(depotCustomsParams);

        //调出
        Map<String, Object> dto = new HashMap<>();
        dto.put("merchantName", merchant.getFullName());
        dto.put("merchantAddr", merchant.getRegisteredAddress());
        dto.put("contractNo", orderModel.getContractNo());
        dto.put("orderDate", orderModel.getCreateDate());
        dto.put("country", outOriginName);
        dto.put("invoiceNo", orderModel.getInvoiceNo());
        dto.put("shipDate", TimeUtils.addDay(orderModel.getCreateDate(), 3));//创建时间加3天
        dto.put("pack", orderModel.getPackType());
        dto.put("portLoading", orderModel.getPortLoading());
        dto.put("shipper", orderModel.getShipper());
        dto.put("payRules", orderModel.getPayRules());
        dto.put("signNo", outDepot.getContractCode());
        dto.put("destination", orderModel.getUnloadPort());
        dto.put("torrNum", orderModel.getTorrNum());
        dto.put("itemList", outDtoList);
        dto.put("eAddressee", depotcustoms != null ? depotcustoms.getRecCompanyEnname() : "");
        dto.put("addressee", depotcustoms != null ? depotcustoms.getRecCompanyName() : orderModel.getConsigneeUsername());// 收货人

        return dto;
    }

    /**
     * 封装调入仓库南沙仓一线清关资料实体（之前的模板）
     */
    private Map<String, Object> getNanShaInDepotFirstCustomsInfo(TransferOrderModel orderModel, List<TransferOrderItemModel> itemModels) throws Exception {
        //清关资料调入商品详情
        List<Map<String, Object>> inDtoList = new ArrayList<>();

        //根据商家id获取商家信息
        Map<String, Object> merchant_params = new HashMap<String, Object>();
        merchant_params.put("merchantId", orderModel.getMerchantId());
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchant_params);

        Map<String, Object> depotRelParam = new HashMap<>();
        depotRelParam.put("merchantId", orderModel.getMerchantId());
        depotRelParam.put("depotId", orderModel.getInDepotId());
        DepotMerchantRelMongo inDepot = depotMerchantRelMongoDao.findOne(depotRelParam);

        Set<String> inOriginSet = new TreeSet<>(); //原产国
        Map<Long, InventoryLocationMappingMongo> oriGoodsNoMap = new HashMap<>();
        for (TransferOrderItemModel item : itemModels) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("merchandiseId", item.getInGoodsId());
            MerchandiseInfoMogo inGoods = merchandiseInfoMogoDao.findOne(params);

            if (inGoods != null) {
                Integer num = item.getTransferNum() == null ? 0 : item.getTransferNum();
                Map<String, Object> inDto = new HashMap<>();
                inDto.put("goodsNo", inGoods.getGoodsNo());
                inDto.put("goodsName", inGoods.getName());
                inDto.put("goodsSpec", inGoods.getSpec());
                inDto.put("palletNo", "");
                inDto.put("boxNum", item.getBoxNum() == null ? 0 : item.getBoxNum());
                inDto.put("num", num);
                inDto.put("netWeight", item.getNetWeightSum() == null ? 0 : item.getNetWeightSum());
                inDto.put("grossWeight", item.getGrossWeightSum() == null ? 0 : item.getGrossWeightSum());
                inDto.put("constituentRatio", inGoods.getComponent());

                BigDecimal price = item.getPrice() == null ? BigDecimal.ZERO : new BigDecimal(item.getPrice().toString());
                inDto.put("price", price);
                if (item.getInTransferNum() == null) {
                    inDto.put("amount", BigDecimal.ZERO);
                } else {
                    //总价=单价*数量
                    inDto.put("amount", price.multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_HALF_UP));
                }

                if (inGoods.getUnit() != null) {
                    Map<String, Object> unitMap = new HashMap<>();
                    unitMap.put("unitId", inGoods.getUnit());
                    UnitMongo unitMongo = unitMongoDao.findOne(unitMap);
                    if (unitMongo != null) {
                        inDto.put("unit", unitMongo.getName());
                    }
                }

                if (inGoods.getCountyId() != null) {
                    Map<String, Object> countryParams = new HashMap<String, Object>();
                    countryParams.put("countryOriginId", inGoods.getCountyId());
                    CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(countryParams);
                    if (countryOrigin != null) {
                        inDto.put("country", countryOrigin.getName());
                        inOriginSet.add(countryOrigin.getName());
                    }
                }
                inDtoList.add(inDto);
            }
        }

        Map<String, List<TransferOrderItemModel>> itemMap = itemModels.stream().collect(Collectors.groupingBy(TransferOrderItemModel::getInBarcode));
        //箱装明细
        List<CustomsPackingDetailsDTO> customsPackingDetailsDTOS = new ArrayList<>();
        PackingDetailsModel detailsModel = new PackingDetailsModel();
        detailsModel.setOrderId(orderModel.getId());
        detailsModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_2);
        List<PackingDetailsModel> packingDetailsModels = packingDetailsDao.list(detailsModel);

        if (!packingDetailsModels.isEmpty()) {
            Map<String, Map<String, Object>> alreadyCalWeightMap = new HashMap<>();
            Map<String, List<PackingDetailsModel>> packingDetailsMap = packingDetailsModels.stream().collect(Collectors.groupingBy(PackingDetailsModel::getBarcode));
            for (int i = 0; i < packingDetailsModels.size(); i++) {

                PackingDetailsModel packingDetailsModel = packingDetailsModels.get(i);

                CustomsPackingDetailsDTO tepmDto = new CustomsPackingDetailsDTO();

                BeanUtils.copyProperties(packingDetailsModel, tepmDto);

                //条码找到调拨单的商品条形码（找到多个随机取一个）的净重*件数
                List<TransferOrderItemModel> queryItemList = itemMap.get(packingDetailsModel.getBarcode());

                if (queryItemList != null && queryItemList.size() > 0) {
                    TransferOrderItemModel queryTransferItemModel = queryItemList.get(0);
                    BigDecimal netWeight = new BigDecimal(queryTransferItemModel.getNetWeightSum()).divide(new BigDecimal(queryTransferItemModel.getTransferNum()), 5, BigDecimal.ROUND_HALF_UP);
                    BigDecimal netWeightSum = netWeight.multiply(new BigDecimal(packingDetailsModel.getPiecesNum()));

                    tepmDto.setGoodsName(queryTransferItemModel.getInGoodsName());
                    tepmDto.setNetWeight(netWeightSum.doubleValue());
                    /**
                     * 2、毛重取值逻辑：
                     * （1）条码只有1行时：汇总求和预申报单对应条码的毛重，保留3位小数
                     * （2）条码不只有1行时，汇总求和预申报单对应条码的毛重:TOTAL_MZ,及总净重：TOTAL_JZ分以下2种情况
                     *    前N-1行：(该行总净重/TOTAL_JZ)*TOTAL_MZ,保留3位小数
                     *    第N行：TOTAL_MZ - 前N-1行总毛重
                     */
                    Double totalItemGrossWeight = queryItemList.stream().map(TransferOrderItemModel::getGrossWeightSum).reduce(0.0, Double::sum);//根据条码累计总毛重
                    Double totalItemNetWeight = queryItemList.stream().map(TransferOrderItemModel::getNetWeightSum).reduce(0.0, Double::sum);//根据条码累计总净重

                    BigDecimal grossWeight = BigDecimal.ZERO;
                    List<PackingDetailsModel> packingDetailsList = packingDetailsMap.get(packingDetailsModel.getBarcode());
                    Map<String, Object> map = alreadyCalWeightMap.get(packingDetailsModel.getBarcode());
                    int index = 1;
                    BigDecimal alreadyCalWeight = BigDecimal.ZERO;
                    if (map != null) {
                        index = (int) map.get("index");
                        alreadyCalWeight = (BigDecimal) map.get("alreadyCalWeight");
                    }

                    if (index == packingDetailsList.size()) {
                        //第N行：TOTAL_MZ - 前N-1行总毛重
                        grossWeight = new BigDecimal(totalItemGrossWeight).subtract(alreadyCalWeight).setScale(3, BigDecimal.ROUND_HALF_UP);
                    } else {
                        //前N-1行：(该行总净重/TOTAL_JZ)*TOTAL_MZ,保留3位小数
                        if (totalItemNetWeight.compareTo(0.0) > 0) {
                            grossWeight = netWeightSum.divide(new BigDecimal(totalItemNetWeight), 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(totalItemGrossWeight)).setScale(3, BigDecimal.ROUND_HALF_UP);
                            alreadyCalWeight = alreadyCalWeight.add(grossWeight);
                            map = new HashMap<>();
                            map.put("index", ++index);
                            map.put("alreadyCalWeight", alreadyCalWeight);
                            alreadyCalWeightMap.put(packingDetailsModel.getBarcode(), map);
                        }
                    }

                    tepmDto.setGrossWeight(grossWeight.doubleValue());
                    InventoryLocationMappingMongo oriGoodsNoMappingModel = oriGoodsNoMap.get(queryTransferItemModel.getInGoodsId());
                    if (oriGoodsNoMappingModel != null) {
                        tepmDto.setGoodsNo(oriGoodsNoMappingModel.getGoodsNo());
                        tepmDto.setGoodsName(oriGoodsNoMappingModel.getGoodsName());
                    }
                }
                customsPackingDetailsDTOS.add(tepmDto);
            }
        }


//		List<CustomsPackingDetailsDTO> detailsDTOList = DownloadExcelUtil.calculateWeights(customsPackingDetailsDTOS);
        List<Map<String, Object>> detailsList = new ArrayList<>();
        if (customsPackingDetailsDTOS.isEmpty()) {
            for (Map<String, Object> item : inDtoList) {
                Map<String, Object> detailItem = new HashMap<>();
                String goodsNo = (String) item.get("goodsNo");
                String goodsName = (String) item.get("goodsName");
                Integer num = (Integer) item.get("num");
                Integer boxNum = (Integer) item.get("boxNum");
                Double netWeight = (Double) item.get("netWeight");
                Double grossWeight = (Double) item.get("grossWeight");
                String palletNo = (String) item.get("palletNo");
                String cabinetNo = "/";

                detailItem.put("goodsNo", goodsNo);
                detailItem.put("goodsName", goodsName);
                detailItem.put("num", num);
                detailItem.put("boxNum", boxNum);
                detailItem.put("netWeight", netWeight);
                detailItem.put("grossWeight", grossWeight);
                detailItem.put("palletNo", palletNo);
                detailItem.put("cabinetNo", cabinetNo);
                detailsList.add(detailItem);
            }
        } else {
            for (CustomsPackingDetailsDTO item : customsPackingDetailsDTOS) {
                Map<String, Object> detailItem = new HashMap<>();
                detailItem.put("goodsNo", item.getGoodsNo());
                detailItem.put("goodsName", item.getGoodsName());
                detailItem.put("num", item.getPiecesNum() == null ? 0 : item.getPiecesNum());
                detailItem.put("boxNum", item.getBoxNum() == null ? 0 : item.getBoxNum());
                detailItem.put("netWeight", item.getNetWeight() == null ? 0 : item.getNetWeight());
                detailItem.put("grossWeight", item.getGrossWeight() == null ? 0 : item.getGrossWeight());
                detailItem.put("palletNo", item.getTorrNo());
                detailItem.put("cabinetNo", item.getCabinetNo());
                detailsList.add(detailItem);
            }
        }

        String inOriginName = org.apache.commons.lang3.StringUtils.join(inOriginSet, ";");
        //调入
        Map<String, Object> dto = new HashMap<>();
        dto.put("merchantName", merchant.getFullName());
        dto.put("merchantAddr", merchant.getRegisteredAddress());
        dto.put("contractNo", orderModel.getContractNo());
        dto.put("orderDate", orderModel.getCreateDate());
        dto.put("country", inOriginName);
        dto.put("invoiceNo", orderModel.getInvoiceNo());
        dto.put("shipDate", TimeUtils.addDay(orderModel.getCreateDate(), 3));
        dto.put("pack", orderModel.getPackType());
        dto.put("portLoading", orderModel.getPortLoading());
        dto.put("shipper", orderModel.getShipper());
        dto.put("payRules", orderModel.getPayRules());
        dto.put("signNo", inDepot.getContractCode());
        dto.put("destinationPort", orderModel.getUnloadPort());
        dto.put("torrNum", orderModel.getTorrNum());
        dto.put("itemList", inDtoList);
        dto.put("detailsDTOList", detailsList);

        return dto;
    }


    /**
     * 封装调入仓库菜鸟骅增仓清关资料实体
     */
    private Map<String, Object> getCNHuangZengInDepotFirstCustomsInfo(TransferOrderModel orderModel, List<TransferOrderItemModel> itemModels) throws Exception {
        //清关资料调入商品详情
//		List<CustomsDeclareItemDTO> inDtoList = new ArrayList<CustomsDeclareItemDTO>() ;

        //根据商家id获取商家信息
        Map<String, Object> merchant_params = new HashMap<String, Object>();
        merchant_params.put("merchantId", orderModel.getMerchantId());
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchant_params);

        Integer totalNum = 0;
        Double totalGrossWeight = 0.0;
        Double totalNetWeight = 0.0;
        BigDecimal totalAmount = new BigDecimal("0");
        List<Map<String, Object>> customsItemList = new ArrayList<>();
        for (TransferOrderItemModel item : itemModels) {
            if (item.getInGoodsId() == null) {
                continue;
            }
            Map<String, Object> customsItem = new HashMap<>();
            if (item.getInTransferNum() != null) {
                customsItem.put("num", item.getInTransferNum());
                totalNum += item.getInTransferNum();
            }

            if (item.getGrossWeightSum() != null) {
                customsItem.put("totalGrossWeight", item.getGrossWeightSum());
                totalGrossWeight += item.getGrossWeightSum();
            }

            // 1、取销售单中的商品货号，若为美赞商品需查询库位映射表转成原货号；
            // 2、以“条形码+入库关区”，找到商品表中外仓备案商品，入库关区为空，则为商品空，找到多条取其一；
            MerchandiseExternalWarehouseMongo relGoods = null;
            List<MerchandiseExternalWarehouseMongo> goodsList = new ArrayList<MerchandiseExternalWarehouseMongo>();
            if (orderModel.getInCustomsId() != null) {
                // 获取商品信息
                Map<String, Object> merchandisePramMap = new HashMap<String, Object>();
                Map<String, Object> merchandiseExternalParams = new HashMap<String, Object>();
                merchandisePramMap.put("merchandiseId", item.getInGoodsId());
                MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandisePramMap);
                merchandiseExternalParams.put("barcode", merchandise.getBarcode());// 条形码
                merchandiseExternalParams.put("customsAreaId", orderModel.getInCustomsId());//入库仓平台关区
                goodsList = merchandiseExternalWarehouseMongoDao.findAll(merchandiseExternalParams);
            }
            if (goodsList != null && goodsList.size() > 0) {
                relGoods = goodsList.get(0);
            }
            BigDecimal price = BigDecimal.ZERO;
            if (relGoods != null) {
                customsItem.put("goodsNo", relGoods.getGoodsNo());
                customsItem.put("goodsSpec", relGoods.getSpec());
                customsItem.put("barcode", relGoods.getBarcode());// 条形码
                customsItem.put("hsCode", relGoods.getHsCode());
                customsItem.put("customsGoodsRecordNo", relGoods.getCustomsGoodsRecordNo());
                customsItem.put("jinerxiang", relGoods.getJinerxiang());
                if (relGoods.getNetWeight() != null && item.getInTransferNum() != null) {
                    Double netWeightSum = relGoods.getNetWeight() * item.getInTransferNum();
                    customsItem.put("totalNetWeight", netWeightSum);
                    totalNetWeight += netWeightSum;
                }

                customsItem.put("brandName", relGoods.getBrandName());// 商品品牌
                customsItem.put("unit", relGoods.getUnit());
                customsItem.put("countryName", relGoods.getCountyName());
                customsItem.put("declareFactor", relGoods.getDeclareFactor());
                price = relGoods.getFilingPrice() == null ? BigDecimal.ZERO : new BigDecimal(relGoods.getFilingPrice().toString());

            }
            customsItem.put("goodsName", item.getInGoodsName());
            customsItem.put("price", price);
            if (item.getInTransferNum() == null) {
                customsItem.put("amount", BigDecimal.ZERO);
            } else {
                BigDecimal amount = price.multiply(new BigDecimal(item.getInTransferNum())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                //总价=单价*数量
                customsItem.put("amount", amount);
                totalAmount = totalAmount.add(amount);
            }
            customsItemList.add(customsItem);
        }

        //调入
        Map<String, Object> dto = new HashMap<>();
//		FirstCustomsInfoDTO inFirstCustomsInfoDTO = new FirstCustomsInfoDTO();
        dto.put("firstParty", merchant.getFullName());
        dto.put("firstPartyAddr", merchant.getRegisteredAddress());
        dto.put("orderDate", orderModel.getCreateDate());
        dto.put("code", TimeUtils.format(orderModel.getCreateDate(), "yyyyMMdd"));
        dto.put("portLoading", orderModel.getOutdepotAddr());
        dto.put("lbxNo", orderModel.getLbxNo());
        dto.put("poNo", orderModel.getPoNo());
        dto.put("loadingDate", TimeUtils.addDay(new Date(), 30));
        dto.put("totalAmount", totalAmount);
        dto.put("totalNum", totalNum);
        dto.put("totalGrossWeight", totalGrossWeight);
        dto.put("totalNetWeight", totalNetWeight);
        dto.put("itemList", customsItemList);

        return dto;
    }

    /**
     * 宁波慈溪一线清关资料
     *
     * @throws Exception
     */
    private Map<String, Object> getNBCIXIFirstCustomsInfoDTO(TransferOrderModel transferOrder, List<TransferOrderItemModel> itemList, String type) throws Exception {
        // 根据商家id获取商家信息
        Map<String, Object> merchantParams = new HashMap<String, Object>();
        merchantParams.put("merchantId", transferOrder.getMerchantId());
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantParams);

        Map<String, Object> dto = new HashMap<>();
        if (transferOrder != null) {
            dto.put("transportation", DERP_ORDER.getLabelByKey(DERP.transportList, transferOrder.getTransport()));// 运输方式
            dto.put("customsContractNo", transferOrder.getContractNo());// 报关合同号（excel中的提运单号）);
            dto.put("poNo", transferOrder.getPoNo());// po号（excel中的合同号）
            dto.put("contractNo", transferOrder.getContractNo());
            dto.put("invoiceNo", transferOrder.getInvoiceNo());// 发票号
            dto.put("pack", transferOrder.getPackType());// 包装方式
            // 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱
            String packType = DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList, transferOrder.getPalletMaterial());
            dto.put("palletMaterial", packType);// 托盘材质;
            dto.put("torrNum", transferOrder.getTorrNum());// 托数
            dto.put("orderDate", transferOrder.getCreateDate());// 订单日期
            dto.put("boxNum", transferOrder.getCartons());// 箱数
            dto.put("portLoading", transferOrder.getPortLoading());// 装货港
            dto.put("shipDate", TimeUtils.addDay(transferOrder.getCreateDate(), 3));//装船时间
            dto.put("destination", transferOrder.getUnloadPort());//目的地
            dto.put("shipper", transferOrder.getShipper());//承运商
            dto.put("payRules", transferOrder.getPayRules());

            if (merchant != null) {
                dto.put("merchantName", merchant.getFullName());// 当前公司全名
                dto.put("merchantAddr", merchant.getRegisteredAddress());// 当前公司地址
                dto.put("merchantEnAddr", merchant.getEnglishRegisteredAddress());// 当前公司地址（英文）
                dto.put("merchantTel", merchant.getTel());
                dto.put("merchantEmail", merchant.getEmail());
                dto.put("merchantEnName", merchant.getEnglishName());// 当前公司全名 英文
            }

            Set<String> originCountrySet = new HashSet<String>();// 原产国集合
            Double totalNetWeight = 0.0;
            List<Map<String, Object>> customsItemList = new ArrayList<>();
            Map<String, List<TransferOrderItemModel>> itemMap = null;
            if ("1".equals(type)) {//入库
                itemMap = itemList.stream().collect(Collectors.groupingBy(TransferOrderItemModel::getInGoodsNo));
            } else if ("2".equals(type)) {//出库
                itemMap = itemList.stream().collect(Collectors.groupingBy(TransferOrderItemModel::getOutGoodsNo));
            }
            for (String goodsNo : itemMap.keySet()) {
                List<TransferOrderItemModel> items = itemMap.get(goodsNo);
                Integer totalItemNum = 0;
                Double netWeight = 0.0;
                TransferOrderItemModel item = items.get(0);
                Map<String, Object> customsItem = new HashMap<>();
                Map<String, Object> merchandisePramMap = new HashMap<String, Object>();
                if ("1".equals(type)) {//入库
                    merchandisePramMap.put("merchandiseId", item.getInGoodsId());
                    totalItemNum = items.stream().map(TransferOrderItemModel::getInTransferNum).reduce(0, Integer::sum);
                } else if ("2".equals(type)) {//出库
                    merchandisePramMap.put("merchandiseId", item.getOutGoodsId());
                    totalItemNum = items.stream().map(TransferOrderItemModel::getTransferNum).reduce(0, Integer::sum);
                }

                MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandisePramMap);
                if (merchandise != null) {
                    netWeight = merchandise.getNetWeight();
                    customsItem.put("barcode", merchandise.getBarcode());// 条形码
                    customsItem.put("goodsSpec", merchandise.getSpec());// 规格
                    customsItem.put("declareFactor", merchandise.getDeclareFactor());// 申报要素
                    customsItem.put("constituentRatio", merchandise.getComponent());// 成分占比
                    customsItem.put("goodsNo", merchandise.getGoodsNo());// 商品货号
                    customsItem.put("goodsName", merchandise.getName());// 商品名称
                    customsItem.put("netWeight", merchandise.getNetWeight());// 净重
                    customsItem.put("hsCode", merchandise.getHsCode());// hs编码
                    customsItem.put("shelfLifeDays", merchandise.getShelfLifeDays());// 保质天数

                    // 根据国家id获取国家信息
                    Map<String, Object> queryParams = new HashMap<String, Object>();
                    queryParams.put("countryOriginId", merchandise.getCountyId());
                    CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(queryParams);
                    if (countryOrigin != null) {
                        customsItem.put("countryName", countryOrigin.getName());
                        originCountrySet.add(countryOrigin.getName());// 原产国
                    }

                    queryParams.clear();
                    queryParams.put("brandId", merchandise.getBrandId());
                    BrandMongo brand = brandMongoDao.findOne(queryParams);
                    if (brand != null) {
                        customsItem.put("brandName", brand.getName());
                    }
                    if (merchandise.getUnit() != null) {
                        // 根据单位id获取单位信息
                        Map<String, Object> unitParams = new HashMap<String, Object>();
                        unitParams.put("unitId", merchandise.getUnit());
                        UnitMongo unit = unitMongoDao.findOne(unitParams);
                        if (unit != null) {
                            customsItem.put("unit", unit.getName());// 单位
                        }
                    }
                }

                Double netWeightSum = 0.0;
                netWeightSum = new BigDecimal(netWeight).multiply(new BigDecimal(totalItemNum)).setScale(5, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
                customsItem.put("totalNetWeight", netWeightSum);// 总净重
                customsItem.put("num", totalItemNum);// 商品数量
                customsItem.put("cartons", item.getBoxNum());
                // 单价
                BigDecimal price = new BigDecimal(item.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                customsItem.put("price", price);// 单价

                customsItemList.add(customsItem);
                totalNetWeight = totalNetWeight + netWeightSum;

            }

            for (Map<String, Object> customsItem : customsItemList) {
                Double goodsNetWeightSum = (Double) customsItem.get("totalNetWeight");
                Integer num = (Integer) customsItem.get("num");
                //毛重 = 商品净重/总净重 * 提单毛重
                Double grossWeightSum = 0.0;
                if (totalNetWeight.doubleValue() > 0) {
                    grossWeightSum = new BigDecimal(transferOrder.getBillWeight()).multiply(
                            new BigDecimal(goodsNetWeightSum).divide(new BigDecimal(totalNetWeight), 5, BigDecimal.ROUND_HALF_UP))
                            .setScale(5, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
                }

                Double grossWeight = new BigDecimal(grossWeightSum).divide(new BigDecimal(num), 5, BigDecimal.ROUND_HALF_UP).doubleValue();
                customsItem.put("totalGrossWeight", grossWeightSum);
                customsItem.put("grossWeight", grossWeight);
            }


            String originCountry = "";
            if (originCountrySet != null && originCountrySet.size() > 0) {
                originCountry = org.apache.commons.lang.StringUtils.join(originCountrySet, ";");
            }
            dto.put("country", originCountry);// 原产国 用分号隔开
            dto.put("itemList", customsItemList);
        }

        return dto;
    }

    @Override
    public List<PackingDetailsModel> getPackingDetail(Long id) throws SQLException {
        PackingDetailsModel queryPackingModel = new PackingDetailsModel();
        queryPackingModel.setOrderId(id);
        queryPackingModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_2);
        return packingDetailsDao.list(queryPackingModel);
    }
}
