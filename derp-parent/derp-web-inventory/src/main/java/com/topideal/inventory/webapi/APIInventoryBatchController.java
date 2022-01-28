package com.topideal.inventory.webapi;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.inventory.service.InventoryBatchService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.InventoryBatchSearchForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/inventory/inventoryBatch")
@Api(tags = "批次库存明细")
public class APIInventoryBatchController {

    // 批次库存明细service
    @Autowired
    private InventoryBatchService inventoryBatchService;


    /**
     * 获取分页数据
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value="/listInventoryBatch.asyn")
    private ResponseBean listInventoryBatch(InventoryBatchSearchForm form)throws Exception {
        InventoryBatchDTO model =new InventoryBatchDTO();
        try {
            BeanUtils.copyProperties(form,model);
            // 响应结果集
            User user= ShiroUtils.getUserByToken(form.getToken());
            model.setMerchantId(user.getMerchantId());
            model = inventoryBatchService.listInventoryBatch(model);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
    }


    /**
     * 导出批次库存明细
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "导出批次库存明细")
    @GetMapping(value="/exportInventoryBatch.asyn")
    private void exportInventoryBatch(HttpServletResponse response, HttpServletRequest request, InventoryBatchSearchForm form)throws Exception {
        try {

            String sheetName = "批次库存明细";
            //根据勾选的获取信息
            User user= ShiroUtils.getUserByToken(form.getToken());
            InventoryBatchDTO model=new  InventoryBatchDTO();
            model.setMerchantId(user.getMerchantId());
            model.setDepotId(form.getDepotId());
            model.setGoodsNo(form.getGoodsNo());
            model.setBatchNo(form.getBatchNo());
            model.setValidityType(form.getValidityType());
            model.setBrandId(form.getBrandId());
            model.setBarcode(form.getBarcode());
            List<Map<String,Object>> result = inventoryBatchService.exportInventoryBatchMap(model);
            String[] columns={"商家名称","仓库名称","商品货号","商品名称","批次号","生产日期","失效日期","库存类型","是否过期","库存数量","理货单位","托盘号","条码","品牌","标准条码"};
            String[] keys={"merchant_name","depot_name","goods_no","goods_name","batch_no","production_date","overdue_date","type","is_expire","surplus_num","unit","lpn","barcode","brand_name","commbarcode"};
            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
