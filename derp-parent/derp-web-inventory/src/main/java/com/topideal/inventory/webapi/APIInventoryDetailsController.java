package com.topideal.inventory.webapi;

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
import com.topideal.entity.dto.InventoryDetailsDTO;
import com.topideal.inventory.service.InventoryDetailsService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.InventoryDetailsForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/webapi/inventory/inventoryDetails")
@Api(tags = "商品收发明细")
public class APIInventoryDetailsController {
    // 商品收发明细service
    @Autowired
    private InventoryDetailsService inventoryDetailsService;

    /**
     * 获取分页数据
     * @param form
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listInventoryDetails.asyn")
    private ResponseBean listInventoryDetails(InventoryDetailsForm form) throws Exception {
        InventoryDetailsDTO model = new InventoryDetailsDTO();
        try {
            BeanUtils.copyProperties(form, model);
            // 响应结果集
            User user = ShiroUtils.getUserByToken(form.getToken());
            model.setMerchantId(user.getMerchantId());
            model = inventoryDetailsService.listInventoryDetails(model);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, model);
    }


    /**
     * 导出商品收发明细
     * @param response
     * @param request
     * @param form
     * @throws Exception
     */
    @ApiOperation(value = "导出商品收发明细")
    @GetMapping(value = "/exportInventoryDetails.asyn")
    private void exportInventoryDetails(HttpServletResponse response, HttpServletRequest request, InventoryDetailsForm form) throws Exception {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            InventoryDetailsDTO model = new InventoryDetailsDTO();
            model.setIds(form.getIds());
            model.setMerchantId(user.getMerchantId());
            model.setDepotId(form.getDepotId());
            model.setGoodsNo(form.getGoodsNo());
            model.setBatchNo(form.getBatchNo());
            model.setStartTime(form.getStartTime());
            model.setEndTime(form.getEndTime());
            model.setThingStatus(form.getThingStatus());
            model.setOrderNo(form.getOrderNo());
            model.setBusinessNo(form.getBusinessNo());
            model.setBuId(form.getBuId());
            model.setBarcode(form.getBarcode());
            model.setCommbarcode(form.getCommbarcode());
            model.setOperateType(form.getOperateType());
            String sheetName = "商品收发明细";
            model.setOrderTimeRange(form.getOrderTimeRange());
            List<Map<String, Object>> result = inventoryDetailsService.exportInventoryDetailsMap(model);

            String[] columns = {"商家名称", "仓库名称", "事业部", "事物类型", "来源单据号", "业务单据号", "变更日期", "商品货号", "条码", "标准条码", "商品名称", "批次号", "生产日期", "有效期至", "数量", "单位", "库存类型", "是否过期", "操作类型"};
            String[] keys = {"merchant_name", "depot_name", "bu_name", "thing_name", "order_no", "business_no", "divergence_date", "goods_no", "barcode", "commbarcode", "goods_name", "batch_no", "production_date", "overdue_date", "num", "unit", "type", "is_expire",  "operate_type"};
            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    /**
     * 单据批量回滚
     * @param form
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "单据批量回滚")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "json", value = "回滚单号集合，多个以英文逗号隔开", required = true)
    })
    @PostMapping(value = "/inventoryRollBack.asyn" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean inventoryRollBack(@RequestBody String json) throws Exception {
        try {            
            // 响应结果集
        	inventoryDetailsService.delInventoryDetail(json);
        	
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

}