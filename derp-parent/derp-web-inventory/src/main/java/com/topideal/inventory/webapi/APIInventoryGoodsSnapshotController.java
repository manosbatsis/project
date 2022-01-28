package com.topideal.inventory.webapi;


import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryGoodsSnapshotDTO;
import com.topideal.entity.vo.InventoryGoodsSnapshotModel;
import com.topideal.inventory.service.InventoryGoodsSnapshotService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.InventoryGoodsSnapshotSearchForm;
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
@RequestMapping("/webapi/inventory/inventoryGoodsSnapshot")
@Api(tags = "库存商品快照表")
public class APIInventoryGoodsSnapshotController {


    // 库存商品快照service
    @Autowired
    private InventoryGoodsSnapshotService inventoryGoodsSnapshotService;


    /**
     * 获取分页数据
     * @param form
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listInventoryGoodsSnapshot.asyn")
    private ResponseBean listInventoryGoodsSnapshot(InventoryGoodsSnapshotSearchForm form) throws Exception {
        InventoryGoodsSnapshotDTO model = new InventoryGoodsSnapshotDTO();
        try {
            BeanUtils.copyProperties(form, model);
            // 响应结果集
            User user = ShiroUtils.getUserByToken(form.getToken());
            model.setMerchantId(user.getMerchantId());
            model = inventoryGoodsSnapshotService.listInventoryGoodsSnapshotModel(model);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, model);
    }


    /**
     * 导出库存商品快照
     * @param response
     * @param request
     * @param form
     * @throws Exception
     */
    @ApiOperation(value = "导出库存商品快照")
    @GetMapping(value = "/exportInventoryGoodsSnapshotModelMap.asyn")
    private void exportInventoryGoodsSnapshotModelMap(HttpServletResponse response, HttpServletRequest request, InventoryGoodsSnapshotSearchForm form) throws Exception {
        try {
            User user= ShiroUtils.getUserByToken(form.getToken());
            InventoryGoodsSnapshotModel model =new  InventoryGoodsSnapshotModel();
            model.setMerchantId(user.getMerchantId());
            model.setDepotId(form.getDepotId());
            model.setGoodsNo(form.getGoodsNo());
            model.setSnapshotDate(form.getSnapshotDate());

            String sheetName = "库存商品快照";
            //根据勾选的获取信息
            List<Map<String,Object>> result = inventoryGoodsSnapshotService.exportInventoryGoodsSnapshotModelMap(model);
            String[] columns={"商家名称","仓库名称","商品货号","商品名称","库存数量","冻结数量","坏品数量","过期数量","可用量","理货单位","快照时间"};
            String[] keys={"merchant_name","depot_name","goods_no","goods_name","surplus_num","freeze_num","bad_num","expire_num","available_num","unit","create_date"};
            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
