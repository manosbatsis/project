package com.topideal.order.service.purchase.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.purchase.PurchaseTryApplyOrderDao;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;
import com.topideal.entity.dto.purchase.PurchaseTryApplyOrderDTO;
import com.topideal.order.service.purchase.PurchaseTryApplyOrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/24 16:12
 * @Description: 采购框架合同
 */
@Service
public class PurchaseTryApplyOrderServiceImpl implements PurchaseTryApplyOrderService {
    private static final Logger LOG = Logger.getLogger(PurchaseTryApplyOrderServiceImpl.class) ;

    @Autowired
    private PurchaseTryApplyOrderDao purchaseTryApplyOrderDao;

    @Override
    public PurchaseTryApplyOrderDTO listByPage(User user, PurchaseTryApplyOrderDTO dto) {
        PurchaseTryApplyOrderDTO resultDTO = purchaseTryApplyOrderDao.listDTOByPage(dto);
        return resultDTO;
    }

    @Override
    public PurchaseTryApplyOrderDTO getDetail(User user, PurchaseTryApplyOrderDTO dto) {
        PurchaseTryApplyOrderDTO resultDTO = purchaseTryApplyOrderDao.getDetail(dto);
        return resultDTO;
    }

    @Override
    public List<PurchaseTryApplyOrderDTO> listOaBillCodeSelect(User user, PurchaseTryApplyOrderDTO dto) {
        List<PurchaseTryApplyOrderDTO> dtoList = purchaseTryApplyOrderDao.listOaBillCodeSelect(dto);
        return dtoList;
    }

    @Override
    public int countByDTO(PurchaseTryApplyOrderDTO dto) {
        return purchaseTryApplyOrderDao.countByDTO(dto);
    }

    @Override
    public List<ExportExcelSheet> exportContractExcel(User user, PurchaseTryApplyOrderDTO dto) {
        String sheetFirstName = "采购试单详情";
        String[] firstColumns = {"表单名称", "申请人", "业务员", "业务部门名称", "业务部门", "归档编辑号", "项目名称", "预计年度采购金额（万元人民币）", "立项额度（万元人民币）", "交货方式", "其他交货方式", "我司签约主体", "供应商名称", "供应商类型", "其他供应商类型", "品牌名称", "商品类型"
                , "其他商品类型", "供应商描述", "供应商商品描述", "市场营销费用、补贴、奖励等约定", "退货条款", "采购类型", "流水编号", "审批状态", "业务类型", "数据ID"
        };

        String[] firstKeys = {"formName", "createrName", "businessManagerName", "businessDeptName", "businessDept", "effectiveCode", "projectName", "annualPurPlanAmount", "proAccLimit", "deliveryTypeLabel", "otherDelType"
                , "merchantName", "supplierName", "supplierTypeLabel", "otherSupplier", "brandName", "comtyTypeLabel", "otherComty", "counterpartDesc", "supProdDesc", "otherMonAppoint"
                , "returnGoodsApp", "purchaseTypeLabel", "oaBillCode", "appStateLabel", "businessModeLabel", "dataId"};

        int pageSize = 5000; //每页5000
        int exportCount = purchaseTryApplyOrderDao.countByDTO(dto);

        List<PurchaseFrameContractDTO> list = new ArrayList<>();
        for (int i = 0 ; i < exportCount; ) {
            int pageSub = (i + pageSize) < exportCount ? (i + pageSize) : exportCount;
            dto.setBegin(i);
            dto.setPageSize(pageSize);
            list.addAll(purchaseTryApplyOrderDao.listForExport(dto));
            i = pageSub;
        }

        //生成表格
        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();

        ExportExcelSheet firstSheet = ExcelUtilXlsx.createSheet(sheetFirstName, firstColumns, firstKeys, list);
        sheets.add(firstSheet);
        list = null;
        return sheets;
    }

    @Override
    public String getLatestTime(PurchaseTryApplyOrderDTO dto) {
        PurchaseTryApplyOrderDTO result = purchaseTryApplyOrderDao.getLatestDTO(dto);
        if(result == null) {
            return null;
        }
        Timestamp createDate = result.getCreateDate();
        return TimeUtils.formatFullTime(createDate);
    }
}
