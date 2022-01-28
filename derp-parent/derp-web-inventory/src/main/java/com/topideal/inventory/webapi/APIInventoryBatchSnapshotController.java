package com.topideal.inventory.webapi;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryBatchSnapshotDTO;
import com.topideal.entity.vo.InventoryBatchSnapshotModel;
import com.topideal.inventory.service.InventoryBatchSnapshotService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.InventoryBatchSnapshotForm;
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
@RequestMapping("/webapi/inventory/inventoryBatchSnapshot")
@Api(tags = "库存批次快照表")
public class APIInventoryBatchSnapshotController {

    // 库存批次快照service
    @Autowired
    private InventoryBatchSnapshotService inventoryBatchSnapshotService;

    /**
     * 获取分页数据
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value="/listInventoryBatchSnapshot.asyn")
    private ResponseBean listInventoryBatchSnapshot(InventoryBatchSnapshotForm form)throws Exception {
        InventoryBatchSnapshotDTO model =new InventoryBatchSnapshotDTO();
        try {
            BeanUtils.copyProperties(form,model);
            // 响应结果集
            User user= ShiroUtils.getUserByToken(form.getToken());
            model.setMerchantId(user.getMerchantId());
            model = inventoryBatchSnapshotService.listInventoryBatchSnapshotModel(model);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
    }


    /**
     * 导出库存批次快照
     * @param response
     * @param request
     * @param form
     * @throws Exception
     */
    @ApiOperation(value = "导出库存批次快照")
    @GetMapping(value="/exportInventoryBatchSnapshotModelMap.asyn")
    private void exportInventoryBatchSnapshotModelMap(HttpServletResponse response, HttpServletRequest request, InventoryBatchSnapshotForm form)throws Exception {
        try {
            User user= ShiroUtils.getUserByToken(form.getToken());
            InventoryBatchSnapshotModel model =new  InventoryBatchSnapshotModel();
            model.setMerchantId(user.getMerchantId());
            model.setDepotId(form.getDepotId());
            model.setGoodsNo(form.getGoodsNo());
            model.setBatchNo(form.getBatchNo());
            model.setSnapshotDate(form.getSnapshotDate());
            String sheetName = "库存批次快照";
            //根据勾选的获取信息
            List<Map<String,Object>> result = inventoryBatchSnapshotService.exportInventoryBatchSnapshotModelMap(model);
            String[] columns={"商家名称","仓库名称","商品货号","商品名称","商品条码","品牌名称","批次号","生产日期","失效日期","库存余量","仓库现存量","库存类型","是否过期","理货单位","托盘号","创建时间"};
            String[] keys={"merchant_name","depot_name","goods_no","goods_name","barcode","brand_name","batch_no","production_date","overdue_date","surplus_num","available_num","type","is_expire","unit","lpn","create_date"};
            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
