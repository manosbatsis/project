package com.topideal.order.webapi.sale;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.sale.SaleReturnDeclareOrderDTO;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderItemModel;
import com.topideal.order.service.sale.SaleReturnDeclareOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.SaleReturnDeclareOrderForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/webapi/order/saleReturnDeclare")
@RestController
@Api(tags = "销售退预申报单")
public class APISaleReturnDeclareOrderController {
    @Autowired
    private SaleReturnDeclareOrderService saleReturnDeclareOrderService;

    @ApiOperation(value = "查询销售退预申报单列表信息")
    @PostMapping(value="/listDTObyPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SaleReturnOrderDTO> listDTObyPage(SaleReturnDeclareOrderForm form) {
        SaleReturnDeclareOrderDTO dto = new SaleReturnDeclareOrderDTO();
        try{
            BeanUtils.copyProperties(form,dto);
            User user= ShiroUtils.getUserByToken(form.getToken());
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());

            // 响应结果集
            dto = saleReturnDeclareOrderService.listDTOByPage(dto, user);

        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    @ApiOperation(value = "查询详情信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "declareOrderId", value = "销售退预申报id", required = true)
    })
    @PostMapping(value="/getDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SaleReturnOrderDTO> getDetail(Long declareOrderId , String token) {
        SaleReturnDeclareOrderDTO dto = new SaleReturnDeclareOrderDTO();
        try{
            if(declareOrderId == null){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            dto = saleReturnDeclareOrderService.searchDetail(declareOrderId);
            List<SaleReturnDeclareOrderItemModel> itemList = saleReturnDeclareOrderService.getItemList(declareOrderId);
            dto.setItemList(itemList);

        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    @ApiOperation(value = "销售退转预申报")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "saleReturnIds", value = "销售退id集合，多个用逗号隔开", required = true)
    })
    @PostMapping(value="/saleReturnToDeclareOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SaleReturnOrderDTO> saleReturnToDeclareOrder(String saleReturnIds , String token ) {
        SaleReturnDeclareOrderDTO dto = new SaleReturnDeclareOrderDTO();
        try{
            if(saleReturnIds == null){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            dto = saleReturnDeclareOrderService.saleReturnToDeclareOrder(saleReturnIds);

        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }
    @ApiOperation(value = "新增/编辑 保存")
    @PostMapping(value="/addOrModifySaleReturnOrder.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean addOrModifySaleReturnOrder(@RequestBody SaleReturnDeclareOrderForm form) {
        SaleReturnDeclareOrderDTO dto = new SaleReturnDeclareOrderDTO();
        try{
            BeanUtils.copyProperties(form,dto);
            User user= ShiroUtils.getUserByToken(form.getToken());
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            dto.setMerchantName(user.getMerchantName());
            // 响应结果集
            saleReturnDeclareOrderService.addOrModifySaleReturnOrder(dto, user);

        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @ApiOperation(value = "审核")
    @PostMapping(value="/auditSaleReturnOrder.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean auditSaleReturnOrder(@RequestBody SaleReturnDeclareOrderForm form) {
        SaleReturnDeclareOrderDTO dto = new SaleReturnDeclareOrderDTO();
        try{
            BeanUtils.copyProperties(form,dto);
            User user= ShiroUtils.getUserByToken(form.getToken());
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            dto.setMerchantName(user.getMerchantName());
            // 响应结果集
            saleReturnDeclareOrderService.auditSaleReturnOrder(dto, user);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "销售退预申报id集合，多个用逗号隔开", required = true)
    })
    @PostMapping(value="/delSaleReturnOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delSaleReturnOrder(String ids , String token ) {
        SaleReturnDeclareOrderDTO dto = new SaleReturnDeclareOrderDTO();
        try{
            if(ids == null){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            saleReturnDeclareOrderService.delSaleReturnOrder(ids);

        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    @ApiOperation(value = "导出" ,produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportOrder.asyn")
    public void exportOrder(SaleReturnDeclareOrderForm form, HttpServletResponse response, HttpServletRequest request) {
        try {
            SaleReturnDeclareOrderDTO dto = new SaleReturnDeclareOrderDTO();
            BeanUtils.copyProperties(form, dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            List<Map<String, Object>> list = saleReturnDeclareOrderService.listForExport(dto,user);

            String[] SALE_COLUMNS = {"预申报单号","关联销退订单","事业部","退出仓库","退入库仓库","订单状态","退货币种","LBX单号","海外理货单位","贸易条款","退货原因","创建人",
                    "创建时间","审核人","审核时间","装货港","卸货港","收货地点","境外发货人","发票号","报关合同号","提单毛重","箱数","托数","托盘材质","包装方式","运输方式",
                    "出库关区","入库关区","头程提单号","唛头","备注","PO单号","退入商品货号","退入商品名称","退入商品条形码","退货商品数量","退货商品单价","退货总金额（不含税）",
                    "税率","税额","批次号","品牌类型","毛重","净重","箱号"};

            String[] SALE_KEYS = {"code","saleReturnOrderCode","buName","outDepotName","inDepotName","status","currency","lbxNo","tallyingUnit","tradeTerms",
                    "returnReason","createName","createDate","auditName","auditerDate","loadPort","portDes","deliveryAddr","shipper","invoiceNo","contractNo",
                    "billWeight", "boxNum","torrNum","torrPackType","pack","transport","outCustomsName","inCustomsName","ladingBill","mark","remark","poNo",
                    "inGoodsNo","inGoodsName","inBarcode","num","price","amount","taxRate","tax","batchNo","brandName","grossWeightSum","netWeightSum","boxNo"};

            String mainSheetName = "销售退预申报单";
            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(mainSheetName, SALE_COLUMNS, SALE_KEYS, list) ;
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
