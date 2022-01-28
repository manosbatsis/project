package com.topideal.inventory.webapi;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.MonthlyAccountSnapshotDTO;
import com.topideal.entity.vo.MonthlyAccountSnapshotModel;
import com.topideal.inventory.service.MonthlyAccountSnapshotService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.MonthlyAccountSnapshotForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 库存管理-月结快照表-控制层
 */
@RestController
@RequestMapping("/webapi/inventory/monthlyAccountSnapshot")
@Api(tags = "月结快照表")
public class APIMonthlyAccountSnapshotController {

    //月结库存快照表service
    @Autowired
    private MonthlyAccountSnapshotService monthlyAccountSnapshotService;

    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listMonthlyAccountSnapshot.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<MonthlyAccountSnapshotDTO> listMonthlyAccountSnapshot(MonthlyAccountSnapshotForm form, HttpSession session) {
        try {
            // 响应结果集
            User user = ShiroUtils.getUserByToken(form.getToken());
            MonthlyAccountSnapshotDTO dto = new MonthlyAccountSnapshotDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = monthlyAccountSnapshotService.listMonthlyAccountSnapshotModel(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }


    @ApiOperation(value = "导出月结快照")
    @GetMapping(value = "/exportMonthlyAccountSnapshotModelMap.asyn")
    private void exportMonthlyAccountSnapshotModelMap(HttpSession session, HttpServletResponse response, HttpServletRequest request, MonthlyAccountSnapshotForm form) {

        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            MonthlyAccountSnapshotModel dto = new MonthlyAccountSnapshotModel();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            String sheetName = "月结快照";
            //根据勾选的获取信息
            List<Map<String, Object>> result = monthlyAccountSnapshotService.exportMonthlyAccountSnapshotModelMap(dto);
            String[] columns = {"商家名称", "结转月份", "仓库名称", "商品货号", "商品名称", "批次号", "生产日期", "失效日期", "库存余量", "仓库现存量", "库存类型", "结转状态", "标准条码"};
            String[] keys = {"merchant_name", "settlement_month", "depot_name", "goods_no", "goods_name", "batch_no", "production_date", "overdue_date", "surplus_num", "available_num", "type", "state", "commbarcode"};
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
