package com.topideal.order.service.purchase.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.purchase.PurchaseFrameContractDao;
import com.topideal.dao.purchase.PurchaseTryApplyOrderDao;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;
import com.topideal.entity.vo.purchase.PurchaseFrameContractModel;
import com.topideal.entity.vo.purchase.PurchaseTryApplyOrderModel;
import com.topideal.order.service.purchase.PurchaseFrameContractService;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/24 16:12
 * @Description: 采购框架合同
 */
@Service
public class PurchaseFrameContractServiceImpl implements PurchaseFrameContractService {
    private static final Logger LOG = Logger.getLogger(PurchaseFrameContractServiceImpl.class) ;
    @Autowired
    private PurchaseFrameContractDao purchaseFrameContractDao;
    @Autowired
    private PurchaseTryApplyOrderDao purchaseTryApplyOrderDao;
    
    @Override
	public List<PurchaseFrameContractDTO> listFrameContracSelect(PurchaseFrameContractDTO dto)throws SQLException {
    	
		return purchaseFrameContractDao.listFrameContracSelect(dto);
	}

    @Override
    public PurchaseFrameContractDTO listByPage(User user, PurchaseFrameContractDTO dto) {
        PurchaseFrameContractDTO resultDTO = purchaseFrameContractDao.listDTOByPage(dto);
        return resultDTO;
    }

    @Override
    public PurchaseFrameContractDTO getDetail(User user, PurchaseFrameContractDTO dto) {
        PurchaseFrameContractDTO responseDTO = purchaseFrameContractDao.getDetail(dto);
        return responseDTO;
    }

    @Override
    public int countByDTO(PurchaseFrameContractDTO dto) {
        return purchaseFrameContractDao.countByDTO(dto);
    }

    @Override
    public List<ExportExcelSheet> exportContractExcel(User user, PurchaseFrameContractDTO dto) {
        String sheetFirstName = "采购框架合同详情";
        String[] firstColumns = {"合同协议号", "原协议编号", "立项/试单编号", "合同名称", "合同模板", "申请人", "申请日期", "员工编码", "部门名称", "部门编码", "合同类型", "归档编辑号", "我司签约主体", "我司联系人", "我司联系电话", "供应商名称", "供应商类型"
                , "其他供应商", "供应商联系人", "供应商联系电话", "供应商地址", "协议开始日期", "协议结束日期", "采购类型", "资金来源", "商品类型", "其他商品", "商品来源", "其他商品来源", "合同状态", "预计年度采购金额(万元人民币)"
                , "立项额度（万元人民币）", "是否已取得授权", "起订量要求", "下采购订单的方式", "分销范围", "交货方式", "其他交货方式", "供应商对产品销售定价要求", "供应商对销售目标的要求", "市场营销费用、补贴、奖励等约定", "运营需求", "仓储运输要求", "收货及验收要求"
                , "短缺残损与滞销约定", "退货条款", "法律管辖仲裁地", "终止约定", "特殊约定（如违约金、违约责任）", "结算方式", "结算币种", "其他结算币种", "结算条款", "结算账期（自然日）", "预付款", "约定银行账号变更条款", "预付款币种", "其他预付款币种"
                , "预付款金额（万元人民币）", "供应商描述", "供应商商品描述", "数据ID", "业务对应的财务经理", "用印顺序", "用印类型"
        };

        String[] firstKeys = {"contractNo", "contractOldNo", "lxsdNo", "contractName", "contractVersionLabel", "createrName", "applicationDate", "businessManager", "businessDeptName", "businessDept", "contractTypeLabel"
                , "effectiveCode", "merchantName", "ourContacter", "ourContactTel", "counterpartContSignComy", "supplierTypeLabel", "otherSupplier", "counterpartConter", "counterpartContTel", "counterpartAdd"
                , "contractStartTime", "contractEndTime", "purchaseTypeLabel", "capitalTypeLabel", "comtyTypeLabel", "otherComty", "comtySourceLabel", "otherComtySource", "contractStateLabel", "annualPurPlanAmount", "proAccLimit"
                , "empJudgeLabel", "orderNumReq", "orderType", "saleRange", "deliveryTypeLabel", "otherDelType", "priceReq", "saleAimReq", "otherMonAppoint", "operationNeeds", "storageTranReq"
                , "acceptanceReq", "damagedAppoint", "returnGoodsApp", "argueSolvePlace", "endAppoint", "specialAppoint", "settleMentLabel", "settleCury", "otherSettleCury", "settleAppointLabel", "settleAccPeriod"
                , "advanceChargeJudgeLabel", "bankAccChaAppoint", "advanceChargeCury", "otherAdvanceChargeCury", "advanceCharge", "counterpartDesc", "supProdDesc", "dataId", "financeManagerLabel", "sealOrderLabel", "sealTypeLabel"};


        int pageSize = 5000; //每页5000
        int exportCount = purchaseFrameContractDao.countByDTO(dto);

        List<PurchaseFrameContractDTO> list = new ArrayList<>();
        for (int i = 0 ; i < exportCount; ) {
            int pageSub = (i + pageSize) < exportCount ? (i + pageSize) : exportCount;
            dto.setBegin(i);
            dto.setPageSize(pageSize);
            list.addAll(purchaseFrameContractDao.listForExport(dto));
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
    public String getLatestTime(PurchaseFrameContractDTO dto) {
        PurchaseFrameContractDTO result = purchaseFrameContractDao.getLatestDTO(dto);
        if(result == null) {
            return null;
        }
        Timestamp createDate = result.getCreateDate();
        return TimeUtils.formatFullTime(createDate);
    }

	@Override
	public 	PurchaseFrameContractDTO getContracOrTryApplyOrder(User user,String frameContractNo,String tryApplyCode) throws SQLException {

		String settleCury=null;//结算币种
		String deliveryType=null;//交货方式
		if (StringUtils.isNotBlank(frameContractNo)) {
			PurchaseFrameContractModel model=new PurchaseFrameContractModel();
			model.setContractNo(frameContractNo);
			List<PurchaseFrameContractModel> list = purchaseFrameContractDao.list(model);
			if (list.size()>0) {
				settleCury=list.get(0).getSettleCury();
				deliveryType=list.get(0).getDeliveryType();
			}
			
		}
		if (StringUtils.isNotBlank(tryApplyCode)) {
			PurchaseTryApplyOrderModel model =new PurchaseTryApplyOrderModel();
			model.setOaBillCode(tryApplyCode);
			List<PurchaseTryApplyOrderModel> list= purchaseTryApplyOrderDao.list(model);
			if (list.size()>0)deliveryType=list.get(0).getDeliveryType();
		}
		PurchaseFrameContractDTO dto =new PurchaseFrameContractDTO();
		dto.setSettleCury(settleCury);
		dto.setDeliveryType(deliveryType);
		return dto;
	}


}
