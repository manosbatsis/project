package com.topideal.order.webapi.transfer;


import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOrderFrom;
import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.PackTypeMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.order.service.transfer.TransferOrderItemService;
import com.topideal.order.service.transfer.TransferOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.webapi.transfer.dto.MergeDTO;
import com.topideal.order.webapi.transfer.dto.ResultDTO;
import com.topideal.order.webapi.transfer.dto.TransferOrderResponseDTO;
import com.topideal.order.webapi.transfer.enums.ActionTypeEnum;
import com.topideal.order.webapi.transfer.form.TransferOrderForm;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 调拨订单管理 控制层
 *
 * @author yucaifu
 */
@RequestMapping("/webapi/order/transfer")
@RestController
@Api(tags = "调拨订单")
public class APITransferOrderController {


    @Autowired
    private TransferOrderService transferOrderService;
    @Autowired
    private TransferOrderItemService transferOrderItemService;
    @Autowired
    private DepotInfoMongoDao depotInfoMongoDao;
    @Autowired
    private PackTypeMongoDao packTypeDao;
    @Autowired
    private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;

    /**
     * 调拨订单列表查询
     */
    @ApiOperation(value = "调拨订单列表查询")
    @PostMapping(value="/listTransferOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TransferOrderDTO> listTransferOrder(TransferOrderForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TransferOrderDTO dto = new TransferOrderDTO();
            dto.setMerchantId(user.getMerchantId());
            dto.setCode(form.getCode());
            dto.setLbxNo(form.getLbxNo());
            dto.setContractNo(form.getContractNo());
            dto.setOutDepotId(form.getOutDepotId());
            dto.setInDepotId(form.getInDepotId());
            dto.setBuId(form.getBuId());
            dto.setStatus(form.getStatus());
            dto.setCreateDateStart(form.getCreateDateStart());
            dto.setCreateDateEnd(form.getCreateDateEnd());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());
            // 响应结果集
            dto = transferOrderService.listTransferOrderPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 调拨单详情
     */
    @ApiOperation(value = "调拨单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "调拨单id", required = true),
            @ApiImplicitParam(name = "pageSource", value = "数据来源", required = true)
    })
    @PostMapping(value = "/getDetailById.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<TransferOrderResponseDTO> getDetailById(String token, String id, String pageSource) {
        TransferOrderResponseDTO responseDTO = new TransferOrderResponseDTO();
        try {
            //查询调拨订单
            TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(Long.valueOf(id));
            //查询调拨订单商品
            TransferOrderItemDTO param = new TransferOrderItemDTO();
            param.setTransferOrderId(transferOrder.getId());
            List<TransferOrderItemDTO> orderItem = transferOrderItemService.searchTransferOrderItem(param);
            //查询装箱明细
            List<PackingDetailsModel> packingList = transferOrderService.getPackingDetail(Long.valueOf(id));

            //查询调出仓库
            Map<String, Object> depotParamMap = new HashMap<String, Object>();
            depotParamMap.put("depotId", transferOrder.getOutDepotId());
            DepotInfoMongo outDepot = depotInfoMongoDao.findOne(depotParamMap);
            responseDTO.setTransferOrder(transferOrder);
            responseDTO.setTransferOrderItemDTOList(orderItem);
            responseDTO.setOutDepotInfo(outDepot);
            responseDTO.setPackingList(packingList);

            if (StringUtils.isNotBlank(pageSource)) {
                responseDTO.setPageSource(pageSource);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, responseDTO);
    }

    /**
     * 跳转调拨单新增/编辑页面
     */
    @ApiOperation(value = "调拨单新增/编辑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "调拨单id", required = true),
            @ApiImplicitParam(name = "pageSource", value = "数据来源", required = true)
    })
    @PostMapping(value = "/addTransferOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<TransferOrderResponseDTO> addTransferOrder(String token, String id, String pageSource) {
        TransferOrderResponseDTO responseDTO = new TransferOrderResponseDTO();
        try {
            User user = ShiroUtils.getUserByToken(token);
            responseDTO.setUser(user);

            if (StringUtils.isNotBlank(pageSource)) {
                responseDTO.setPageSource(pageSource);
            }
            if (!StringUtils.isEmpty(id)) {
                TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(Long.valueOf(id));
                Map<String, Object> relParam = new HashMap<>();
                relParam.put("merchantId", user.getMerchantId());
                if (transferOrder.getOutDepotId() != null) {
                    relParam.put("depotId", transferOrder.getOutDepotId());
                    DepotMerchantRelMongo outDepotMerchantRel = depotMerchantRelMongoDao.findOne(relParam);

                    responseDTO.setOutDepotIsInOutInstruction(outDepotMerchantRel.getIsInOutInstruction());
                }

                if (transferOrder.getInDepotId() != null) {
                    relParam.put("depotId", transferOrder.getInDepotId());
                    DepotMerchantRelMongo inDepotMerchantRel = depotMerchantRelMongoDao.findOne(relParam);
                    responseDTO.setInDepotIsInOutInstruction(inDepotMerchantRel.getIsInOutInstruction());
                }

                //查询调拨订单商品
                TransferOrderItemDTO param = new TransferOrderItemDTO();
                param.setTransferOrderId(transferOrder.getId());
                List<TransferOrderItemDTO> orderItem = transferOrderItemService.searchTransferOrderItem(param);
                responseDTO.setTransferOrder(transferOrder);
                responseDTO.setTransferOrderItemDTOList(orderItem);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, responseDTO);
    }

    /**
     * 新增页面——调拨订单保存并审核
     */
    @ApiOperation(value = "调拨单新增保存并审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "json", value = "新增数据", required = true)
    })
    @PostMapping(value = "/saveTransferOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<ResultDTO> saveTransferOrder(String token, String json) {

        try {
            Map<String, Object> retMap = new HashMap<String, Object>();
            User user = ShiroUtils.getUserByToken(token);
            // 实例化JSON对象
            JSONObject jsonData = JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("goodsList", Map.class);
            classMap.put("packingList", Map.class);
            TransferOrderFrom model = (TransferOrderFrom) JSONObject.toBean(jsonData, TransferOrderFrom.class, classMap);

//            retMap = transferOrderService.saveTransferOrder(user.getId(), user.getName(),
//                    user.getMerchantId(), user.getMerchantName(), user.getTopidealCode(), model);

            retMap = transferOrderService.saveOrUpdateTransferOrder(user, model, ActionTypeEnum.ADD);
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setCode((String) retMap.get("code"));
            resultDTO.setMessage((String) retMap.get("message"));
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 编辑页面——调拨订单保存并审核
     */
    @ApiOperation(value = "调拨单编辑保存并审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "json", value = "编辑数据", required = true)
    })
    @PostMapping(value = "/updateTransferOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<ResultDTO> updateTransferOrder(String token, String json) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> retMap = new HashMap<String, Object>();
            // 实例化JSON对象
            JSONObject jsonData = JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("goodsList", Map.class);
            classMap.put("packingList", Map.class);
            TransferOrderFrom model = (TransferOrderFrom) JSONObject.toBean(jsonData, TransferOrderFrom.class, classMap);
            retMap = transferOrderService.saveOrUpdateTransferOrder(user, model, ActionTypeEnum.UPDATE);
            resultDTO.setCode((String) retMap.get("code"));
            resultDTO.setMessage((String) retMap.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
    }

    @ApiOperation(value = "删除调拨订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "调拨订单id, 多个则以','分开", required = true)
    })
    @PostMapping(value = "/delTransferOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delTransferOrder(String token, String ids) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
            }
			User user = ShiroUtils.getUserByToken(token);
            List list = StrUtils.parseIds(ids);
            Map<String, String> resultMap = transferOrderService.delTransferOrder(user.getId(), user.getName(), list);
            String code = resultMap.get("code");
            String message = resultMap.get("message");
            if (("01").equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);//数据为空
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @ApiOperation(value = "导入调拨订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value = "/importTransfer.asyn", headers = "content-type=multipart/form-data")
    public ResponseBean<UploadResponse> importTransfer(String token,
                                                       @ApiParam(value = "上传的文件", required = true)
                                                       @RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
        try {
            if (file == null) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//文件不能为空
            }
            List<Map<String, String>> mainList = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(), "基本信息");
            List<Map<String, String>> detailList = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(), "商品信息");
            if (mainList == null || detailList == null) {//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List<List<Map<String, String>>> sheetList = new ArrayList<>();
            sheetList.add(mainList);
            sheetList.add(detailList);
			User user = ShiroUtils.getUserByToken(token);
            Map resultMap = transferOrderService.saveImportTransfer(user.getId(), user.getName(), user.getMerchantId(), user.getMerchantName(),
                    user.getTopidealCode(), user.getRelMerchantIds(), sheetList);

            Integer allRows = (Integer) resultMap.get("allRows");
            Integer success = 0;
            Integer failure = (Integer) resultMap.get("errorRows");
            List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("errorList");

            if (errorList.size() == 0) {
                success = allRows;
            }
            UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
            uploadResponse.setSuccess(success);
            uploadResponse.setFailure(failure);
            uploadResponse.setErrorList(errorList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "获取调出商品分组统计数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "调拨订单id,多个以','隔开", required = true)
    })
    @PostMapping(value = "/getItemSumByIds.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<List<MergeDTO>> getItemSumByIds(String Ids) {
        try {
            String[] idsStr = Ids.split(",");
            List<Long> idArr = new ArrayList<Long>();
            for (String id : idsStr) {
                idArr.add(Long.valueOf(id));
            }
            //查询明细
            List<Map<String, Object>> itemList = transferOrderService.getItemSumByIds(idArr);
            List<MergeDTO> itemDtoList = new ArrayList<>();
            List<MergeDTO> mergeList = null;//合并好的明细
            if (itemList != null && itemList.size() > 0) {
                for (Map<String, Object> itemMap : itemList) {
                    MergeDTO mergeDTO = new MergeDTO();
                    //获取仓库编码、仓库类型
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("depotId", itemMap.get("out_depot_id"));
                    DepotInfoMongo depot = depotInfoMongoDao.findOne(map);
                    mergeDTO.setDepot_code(depot.getCode());
                    mergeDTO.setDepot_name(depot.getName());
                    mergeDTO.setDepot_type(depot.getType());
                    mergeDTO.setOut_depot_id(String.valueOf(itemMap.get("out_depot_id")));
                    mergeDTO.setOut_goods_id(String.valueOf(itemMap.get("out_goods_id")));
                    mergeDTO.setOut_goods_no(String.valueOf(itemMap.get("out_goods_no")));
                    mergeDTO.setTallying_unit((String) itemMap.get("tallying_unit"));
                    mergeDTO.setTransfer_num((String) itemMap.get("transfer_num"));
                    itemDtoList.add(mergeDTO);
                }
                //合并
                mergeList = mergeItem(itemDtoList);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, mergeList);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 按 仓库id、商品id、理货单位(香港仓)
     */
    private List<MergeDTO> mergeItem(List<MergeDTO> itemList) {
        List<MergeDTO> mergeList = new ArrayList<>();

        /**合并
         * key=仓库id、商品id、理货单位(香港仓)
         * **/
        Map<String, MergeDTO> mergeMap = new HashMap<String, MergeDTO>();
        //循环合并明细
        for (MergeDTO itemDto : itemList) {
            String depotId = itemDto.getOut_depot_id(); //仓库
            String goodsId = itemDto.getOut_goods_id(); //商品
            String tallyingUnit = "";//理货单位默认为空，香港仓时才需要
            String depotType = itemDto.getDepot_type();
            if (depotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                tallyingUnit = itemDto.getTallying_unit();
                if (tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_00)) {//转换为库存的理货单位
                    tallyingUnit = DERP.INVENTORY_UNIT_0;//托盘
                } else if (tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_01)) {
                    tallyingUnit = DERP.INVENTORY_UNIT_1;//箱
                } else if (tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_02)) {
                    tallyingUnit = DERP.INVENTORY_UNIT_2;//件
                }
            }
            String key = depotId + goodsId + tallyingUnit;
            if (mergeMap.get(key) != null) {
                MergeDTO itemValueDto = mergeMap.get(key);
                int numAll = Integer.valueOf(itemValueDto.getTransfer_num());//已合并数量
                int transfer_num = Integer.valueOf(itemDto.getTransfer_num());
                int mergeNum = numAll+transfer_num;
                itemValueDto.setTransfer_num(String.valueOf(mergeNum));
            } else {
                MergeDTO newItemDto = new MergeDTO();
                newItemDto.setOut_depot_id(itemDto.getOut_depot_id());
                newItemDto.setDepot_code(itemDto.getDepot_code());
                newItemDto.setDepot_name(itemDto.getDepot_name());
                newItemDto.setDepot_type(itemDto.getDepot_type());
                newItemDto.setOut_goods_id(itemDto.getOut_goods_id());
                newItemDto.setOut_goods_no(itemDto.getOut_goods_no());
                newItemDto.setTallying_unit(itemDto.getTallying_unit());
                newItemDto.setTransfer_num(itemDto.getTransfer_num());
                mergeMap.put(key, newItemDto);
            }
        }
        //遍历合并好的Map组装成list
        for (MergeDTO dto : mergeMap.values()) {
            mergeList.add(dto);
        }
        return mergeList;
    }

    @ApiOperation(value = "获取页面必填字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "outDepotId", value = "调出仓库id", required = true),
            @ApiImplicitParam(name = "inDepotId", value = "调入仓库id", required = true),
            @ApiImplicitParam(name = "isSameArea", value = "是否同关区（0-否，1-是）")
    })
    @PostMapping(value = "/getMustParameter.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<String> getMustParameter(String token, Long outDepotId, Long inDepotId, String isSameArea) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            List<String> mustParamList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            //1、调出仓库类型保税仓，调入仓库类型保税仓，同关区且下推接口指令都为是: 调入客户、合同号
            String[] paramAT = {"inCustomerIdLabel", "contractNoLabel"};
            //2、1）调出仓库类型保税仓，调入仓库类型保税仓，同关区且调出接口指令为否，调入接口指令为是
            //   2) 调出仓库类型保税仓，调入仓库类型保税仓，跨关区:调入客户,合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格
            String[] paramAK = {"inCustomerIdLabel", "contractNoLabel", "invoiceNoLabel", "packTypeLabel", "cartonsLabel", "firstLadingBillNoLabel", "priceLabel"};
            //3、调出仓库类型保税仓，调入仓库类型保税仓，同关区，调入接口指令为否：调入客户、合同号
            String[] paramAK2 = {"inCustomerIdLabel", "contractNoLabel"};
            //4、调出仓库类型是海外仓 : 合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格,收货人姓名,国家,目的地,海外理货单位，调入客户
            String[] paramCA = {"contractNoLabel", "invoiceNoLabel", "packTypeLabel", "cartonsLabel", "firstLadingBillNoLabel", "priceLabel",
                    "consigneeUsernameLabel", "countryLabel", "destinationLabel", "inCustomerIdLabel"};
            //5、调入仓库类型海外仓 : 合同号、海外理货单位、调入客户
            String[] paramAC = {"contractNoLabel", "inCustomerIdLabel"};
            //6、保税仓调备查库【同\跨关区】：调入客户、合同号
            String[] paramAB = {"inCustomerIdLabel", "contractNoLabel"};
            //7、调出仓库类型备查库或暂存区：调入客户、合同号、价格
            String[] paramBE = {"inCustomerIdLabel", "contractNoLabel", "priceLabel"};
            //8. 调出仓为中转仓：调入客户、箱数、运输方式、托数、托盘材质、出库地点、收货地址、装货港、卸货港
            String[] paramTURN = {"inCustomerIdLabel", "portLoadingLabel"};

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("depotId", outDepotId);
            DepotInfoMongo outDepot = depotInfoMongoDao.findOne(param);
            param.put("depotId", inDepotId);
            DepotInfoMongo inDepot = depotInfoMongoDao.findOne(param);

            Map<String, Object> depotRelParam = new HashMap<String, Object>();
            depotRelParam.put("depotId", inDepotId);
            depotRelParam.put("merchantId", user.getMerchantId());
            DepotMerchantRelMongo inDepotMerchantRel = depotMerchantRelMongoDao.findOne(depotRelParam);
            depotRelParam.put("depotId", outDepotId);
            DepotMerchantRelMongo outDepotMerchantRel = depotMerchantRelMongoDao.findOne(depotRelParam);

            boolean isOutStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(outDepotMerchantRel.getIsInOutInstruction());
            boolean isInStruction = DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(inDepotMerchantRel.getIsInOutInstruction());
            String outDepotType = outDepot.getType();
            String inDepotType = inDepot.getType();

            //1、调出仓库类型保税仓，调入仓库类型保税仓，同关区且下推接口指令都为是: 调入客户、合同号
            if (!StringUtils.isEmpty(isSameArea)
                    && inDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && outDepot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && DERP.ISSAMEAREA_1.equals(isSameArea)
                    && isOutStruction && isInStruction) {
                mustParamList = Arrays.asList(paramAT);
            }
            //2、1）调出仓库类型保税仓，调入仓库类型保税仓，同关区且调出接口指令为否，调入接口指令为是
            //   2) 调出仓库类型保税仓，调入仓库类型保税仓，跨关区:调入客户,合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格
            else if (!StringUtils.isEmpty(isSameArea) && inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && ((DERP.ISSAMEAREA_1.equals(isSameArea) && isInStruction && !isOutStruction)
                    || DERP.ISSAMEAREA_0.equals(isSameArea))) {
                mustParamList = Arrays.asList(paramAK);
            }
            //3、调出仓库类型保税仓，调入仓库类型保税仓，同关区，调入接口指令为否：调入客户、合同号
            else if (!StringUtils.isEmpty(isSameArea) && inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)
                    && DERP.ISSAMEAREA_1.equals(isSameArea)
                    && !isInStruction) {
                mustParamList = Arrays.asList(paramAK2);
            }
            //4、调出仓库类型是海外仓 : 合同号,发票号,收货地址,包装方式,箱数,头程提单号,价格,收货人姓名,国家,目的地,海外理货单位，调入客户
            else if (outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                mustParamList = Arrays.asList(paramCA);
            }
            //5、调入仓库类型海外仓 : 合同号、海外理货单位、调入客户
            else if (inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                mustParamList = Arrays.asList(paramAC);
            }
            //6、保税仓调备查库【同\跨关区】：调入客户、合同号
            else if (!StringUtils.isEmpty(isSameArea) && DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepotType)
                    && DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepotType)) {
                mustParamList = Arrays.asList(paramAB);
            }
            //7、调出仓库类型备查库或暂存区：调入客户、合同号、价格
            else if (outDepotType.matches(DERP_SYS.DEPOTINFO_TYPE_B + "|" + DERP_SYS.DEPOTINFO_TYPE_E)) {
                mustParamList = Arrays.asList(paramBE);
            }

            //8.调出仓为中转仓：调入客户、箱数、运输方式、托数、托盘材质、出库地点、收货地址、装货港、卸货港
            else if (outDepotType.matches(DERP_SYS.DEPOTINFO_TYPE_D)) {
                mustParamList = Arrays.asList(paramTURN);
            }

            list = new ArrayList<>(mustParamList);

            //9、唯品代销仓: po号
            if ("VIP001".equals(inDepot.getDepotCode()) || "VIP001".equals(outDepot.getDepotCode())) {
                list.add("poNoLabel");
            }

            //10、入仓仓库是备查仓时，调入仓地址必填
            if (DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
                list.add("depotScheduleIdLabel");
            }

            //11、当调入/调出仓需推接口指令时，价格必填
            if (isOutStruction || isInStruction) {
                if (!list.contains("priceLabel")) {
                    list.add("priceLabel");
                }
            }
            //当调出仓库为海外仓，调入仓库为保税仓时，商品信息列表栏合同号、箱号为必填
            if (outDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_C) && inDepotType.equals(DERP_SYS.DEPOTINFO_TYPE_A)) {
                list.add("contNoLabel");
                list.add("bargainnoLabel");
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, StringUtils.join(list.toArray(), ","));
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "修改调拨单LbxNo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "transOrderId", value = "调拨订单id", required = true),
            @ApiImplicitParam(name = "newLbxNo", value = "Lbx单号", required = true)
    })
    @PostMapping(value = "/updateLbxNo.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<ResultDTO> updateLbxNo(String token, Long transOrderId, String newLbxNo) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object>  retMap = transferOrderService.updateLbxNo(user.getId(), user.getName(), transOrderId, newLbxNo);
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setCode((String) retMap.get("code"));
            resultDTO.setMessage((String) retMap.get("message"));
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    /**
     * 新增/编辑页面——保存调拨订单,保存时只校验仓库、商品是否有值，这两个字段用户有输入值即可保存不做其他任何校验
     */
    @ApiOperation(value = "新增/编辑页面——保存调拨订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "json", value = "新增/编辑调拨信息", required = true)
    })
    @PostMapping(value = "/saveTempTransferOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<ResultDTO> saveTempTransferOrder(String token, String json) {
        try {
            // 实例化JSON对象
            JSONObject jsonData = JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("goodsList", Map.class);
            classMap.put("packingList", Map.class);
            TransferOrderFrom model = (TransferOrderFrom) JSONObject.toBean(jsonData, TransferOrderFrom.class, classMap);

            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> retMap = transferOrderService.saveTempTransferOrder(user.getId(), user.getName(),
                    user.getMerchantId(), user.getMerchantName(), user.getTopidealCode(), model);
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setCode((String) retMap.get("code"));
            resultDTO.setMessage((String) retMap.get("message"));
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "获取导出调拨单的数量")
    @ApiResponses({
            @ApiResponse(code = 10000,message="data = > 获取导出调拨出库单的数量")
    })
    @PostMapping(value = "/exportTransferCount.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<ResultDTO> exportTransferCount(TransferOrderForm form) throws Exception {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TransferOrderDTO dto = new TransferOrderDTO();
            dto.setMerchantId(user.getMerchantId());
            dto.setCode(form.getCode());
            dto.setLbxNo(form.getLbxNo());
            dto.setContractNo(form.getContractNo());
            dto.setOutDepotId(form.getOutDepotId());
            dto.setInDepotId(form.getInDepotId());
            dto.setBuId(form.getBuId());
            dto.setStatus(form.getStatus());
            dto.setCreateDateStart(form.getCreateDateStart());
            dto.setCreateDateEnd(form.getCreateDateEnd());
            dto.setIds(form.getIds());
            Long count = transferOrderService.countForExport(dto, user);
            Integer max = DERP.EXPORT_MAX;
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setCode("00");
            resultDTO.setMessage("检查通过");
            if(count.intValue()>max.intValue()){
                resultDTO.setCode("01");
                resultDTO.setMessage("导出数量超过"+max+"请分多次导出");
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }

    }

    @ApiOperation(value = "导出调拨订单",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportTransferOrder.asyn")
    public ResponseBean exportTransferOrder(TransferOrderForm form, HttpServletResponse response, HttpServletRequest request) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TransferOrderDTO dto = new TransferOrderDTO();
            dto.setMerchantId(user.getMerchantId());
            dto.setCode(form.getCode());
            dto.setLbxNo(form.getLbxNo());
            dto.setContractNo(form.getContractNo());
            dto.setOutDepotId(form.getOutDepotId());
            dto.setInDepotId(form.getInDepotId());
            dto.setBuId(form.getBuId());
            dto.setStatus(form.getStatus());
            dto.setCreateDateStart(form.getCreateDateStart());
            dto.setCreateDateEnd(form.getCreateDateEnd());
            dto.setIds(form.getIds());
            //表头信息
            List<Map<String, Object>> transferOrderList = transferOrderService.listForExport(dto, user);

            //表头信息
            String mainSheetName = "表头信息";
            String[] mainColumns = {"调拨单号", "合同号", "调出仓库", "调入仓库", "事业部", "库位类型", "单据状态", "LBX单号", "创建人", "创建时间",
                    "发票号", "包装方式", "箱数", "提单号", "运输方式", "承运商", "车次", "标准箱TEU", "托数", "出仓地点", "收货地址", "海外理货单位"};
            String[] mainKeys = {"code", "contract_no", "out_depot_name", "in_depot_name", "bu_name", "stock_location_type_name", "status", "lbx_no", "create_username", "create_date",
                    "invoice_no", "pack_type", "cartons", "first_lading_bill_no", "transport", "carrier", "train_number", "standard_case_teu", "torr_num",
                    "outDepot_addr", "receiving_address", "tallying_unit"};

            //商品信息
            List<Map<String, Object>> itemList = transferOrderService.listForExportItem(dto, user);

            String itemSheetName = "表体商品信息";
            String[] itemColumns = {"调拨单号", "调出商品编号", "调出商品货号", "调出商品名称", "调出单位", "调出数量", "调入商品编号", "调入商品货号",
                    "调入商品名称", "调入单位", "调入数量", "品牌类型", "调拨单价", "毛重", "净重", "箱号", "合同号", "备注"};
            String[] itemKeys = {"code", "out_goods_code", "out_goods_no", "out_goods_name", "out_unit", "transfer_num", "in_goods_code", "in_goods_no",
                    "in_goods_name", "in_unit", "in_transfer_num", "brand_name", "price", "gross_weight", "net_weight", "cont_no", "bargainno", "remark"};
            //生成表格
            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, transferOrderList);
            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, itemList);

            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
            sheets.add(mainSheet);
            sheets.add(itemSheet);

            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
            //导出文件
            String fileName = "调拨订单" + TimeUtils.formatDay();
            FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }


    }

    @ApiOperation(value = "查询调拨订单是否出库或入库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "调拨订单id", required = true),
            @ApiImplicitParam(name = "orderType", value = "调拨出入库类型，0-出库,1-入库", required = true)
    })
    @PostMapping(value = "/isExistTransferOutOrInOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<ResultDTO> isExistTransferOutOrInOrder(String token, Long id, String orderType) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).addObject(orderType).empty();
            if (isNull) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            User user = ShiroUtils.getUserByToken(token);
            TransferOrderDTO dto = new TransferOrderDTO();

            dto.setMerchantId(user.getMerchantId());
            dto.setId(id);
            dto.setOrderType(orderType);
            Map<String, String> result = transferOrderService.isExistOrder(dto);
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setCode(result.get("code"));
            resultDTO.setMessage(result.get("message"));
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    @ApiOperation(value = "获取调拨订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "调拨订单id", required = true)
    })
    @PostMapping(value = "/getTransferOrderDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<TransferOrderDTO> getTransferOrderDetail(String id) throws Exception {
        try {
            //查询调拨订单
            TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(Long.valueOf(id));
            //查询调拨订单商品
            TransferOrderItemDTO param = new TransferOrderItemDTO();
            param.setTransferOrderId(transferOrder.getId());
            List<TransferOrderItemDTO> orderItem = transferOrderItemService.searchTransferOrderItem(param);
            transferOrder.setOrderItemDTOS(orderItem);
            //查询装箱明细
            List<PackingDetailsModel> packingList = transferOrderService.getPackingDetail(Long.valueOf(id));
            transferOrder.setPackingList(packingList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, transferOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "上架入库时间校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "调拨订单id", required = true),
            @ApiImplicitParam(name = "transferInDate", value = "上架入库时间", required = true)
    })
    @PostMapping(value = "/validInDepotDate.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<ResultDTO> validInDepotDate(Long id, String transferInDate) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).addObject(transferInDate).empty();
            if (isNull) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Map<String, String> result = transferOrderService.validInDepotDate(id, transferInDate);
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setCode(result.get("code"));
            resultDTO.setMessage(result.get("message"));
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "导入调拨订单商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "outDepotId", value = "调出仓库id", required = true),
            @ApiImplicitParam(name = "inDepotId", value = "调入仓库id", required = true)
    })
    @PostMapping(value = "/importTransferGoods.asyn", headers = "content-type=multipart/form-data")
    public ResponseBean<UploadResponse> importTransferGoods(String token,String outDepotId, String inDepotId,
                                                @ApiParam(value = "上传的文件", required = true)
                                                @RequestParam(value = "file", required = true) MultipartFile file) {
        try {
            if (file == null) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if (data == null) {//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(token);
            Map resultMap = transferOrderService.importTransferGoods(data, outDepotId, inDepotId, user);
            Integer success = (Integer) resultMap.get("success");
            Integer failure = (Integer) resultMap.get("failure");
            List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
            List<Map<String, String>> stringList = (List<Map<String, String>>) resultMap.get("stringList");
            UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
            uploadResponse.setSuccess(success);
            uploadResponse.setFailure(failure);
            uploadResponse.setErrorList(errorList);
            uploadResponse.setData(stringList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "导出商品",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json", value = "导出商品信息", required = true)
    })
    @PostMapping(value="/exportItems.asyn")
    public ResponseBean exportItems(HttpServletResponse response, HttpServletRequest request, String json) throws Exception {
       try {
           /** 标题  */
           String[] COLUMNS= {"序号","调出商品货号","调入商品货号","条形码","托盘号","箱数","调出数量","调拨单价","净重（KG）","毛重（KG）"};
           String[] KEYS= {"seq","outGoodsNo","inGoodsNo","outBarcode","torrNo","boxNum","transferNum","price","netWeightSum","grossWeightSum"};

           String sheetName = "商品导出";
           List<TransferOrderItemDTO> itemList = null;
           if (json != null || org.apache.commons.lang3.StringUtils.isNotBlank(json)) {            //输入信息不完整
               itemList = transferOrderService.exportListItem(json);
           }
           //生成表格
           SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, itemList);
           //导出文件
           FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
           return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
       } catch (Exception e) {
           e.printStackTrace();
           return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
       }
    }

    @ApiOperation(value = "打托出库时间校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "调拨订单id", required = true),
            @ApiImplicitParam(name = "transferOutDate", value = "打托出库时间", required = true)
    })
    @PostMapping(value = "/validOutDepotDate.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<ResultDTO> validOutDepotDate(Long id, String transferOutDate) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).addObject(transferOutDate).empty();
            if (isNull) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Map<String, String> result = transferOrderService.validOutDepotDate(id, transferOutDate);
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setCode(result.get("code"));
            resultDTO.setMessage(result.get("message"));
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "导出一线清关资料",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json", value = "导出清关资料信息", required = true)
    })
    @PostMapping(value="/exportCustomsDeclare.asyn")
    public void exportCustomsDeclare(HttpServletResponse response, String json) throws Exception {
        try {
            JSONObject jsonData = JSONObject.fromObject(json);
            String id = (String) jsonData.get("id");
            String inFileTempIds = (String) jsonData.get("inFileTempIds");
            String outFileTempIds = (String) jsonData.get("outFileTempIds");

            if (StringUtils.isBlank(id)) {
                return;
            }
            Map<String, Workbook> resultMap = new HashMap<>();
            if (!org.springframework.util.StringUtils.isEmpty(inFileTempIds)) {
                List<Long> inFileTempIdList = new ArrayList<>();
                List<String> list = Arrays.asList(inFileTempIds.split(","));
                for (String idStr : list) {
                    inFileTempIdList.add(Long.valueOf(idStr));
                }
                Map<String, Object> resultInMap = transferOrderService.exportInDepotCustomsDeclares(Long.valueOf(id), inFileTempIdList);
                Map<String, Workbook> multipartInCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultInMap, "2");
                resultMap.putAll(multipartInCustomsDeclares);
            }

            if (!org.springframework.util.StringUtils.isEmpty(outFileTempIds)) {
                List<Long> outFileTempIdList = new ArrayList<>();
                List<String> list = Arrays.asList(outFileTempIds.split(","));
                for (String idStr : list) {
                    outFileTempIdList.add(Long.valueOf(idStr));
                }
                Map<String, Object> resultOutMap = transferOrderService.exportOutDepotCustomsDeclares(Long.valueOf(id), outFileTempIdList);
                Map<String, Workbook> multipartOutCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultOutMap, "1");
                resultMap.putAll(multipartOutCustomsDeclares);
            }

            //导出压缩文件
            String downloadFilename = URLEncoder.encode("调拨订单一线清关资料.zip", "UTF-8");
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

            for (String key : resultMap.keySet()) {
                Workbook wb = resultMap.get(key);
                zos.putNextEntry(new ZipEntry(key));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                wb.write(bos);
                bos.writeTo(zos);
                zos.closeEntry();
            }
            zos.flush();
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 装箱明细导入
     * @param file
     * @param id
     * @return
     */
    @ApiOperation(value = "导入装箱明细")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "token", value = "令牌", required = true),
          @ApiImplicitParam(name = "itemList", value = "商品明细", required = true),
    })
    @PostMapping(value = "/importPackingDetails.asyn", headers = "content-type=multipart/form-data")
    public ResponseBean<UploadResponse> importPackingDetails(String token, String itemList,
                                                             @ApiParam(value = "上传的文件", required = true)
                                                             @RequestParam(value = "file", required = true) MultipartFile file) {
        Map resultMap = new HashMap();//返回的结果集
        try{
            if(file==null){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//文件不能为空
            }
            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if(data == null ){//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            JSONArray jsonData = JSONArray.fromObject(itemList);
            List<TransferOrderItemModel> items = (List<TransferOrderItemModel>) JSONArray.toCollection(jsonData, TransferOrderItemModel.class);

            User user= ShiroUtils.getUserByToken(token);
            resultMap = transferOrderService.importPackingDetails(data, user, items);

            Integer success = (Integer) resultMap.get("success");
            Integer failure = (Integer) resultMap.get("failure");
            List<PackingDetailsModel>  packingList = (List<PackingDetailsModel>) resultMap.get("packingList");
            List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("errorList");
            UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
            uploadResponse.setSuccess(success);
            uploadResponse.setFailure(failure);
            uploadResponse.setErrorList(errorList);
            uploadResponse.setData(packingList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }
}
