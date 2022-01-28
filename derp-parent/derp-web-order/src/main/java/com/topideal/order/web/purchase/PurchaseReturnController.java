/*package com.topideal.order.web.purchase;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOrderModel;
import com.topideal.order.service.purchase.PurchaseReturnService;
import com.topideal.order.shiro.ShiroUtils;

*//**
 * 采购退货订单 控制层
 * 
 * @author Gy
 *//*
@RequestMapping("/purchaseReturn")
@Controller
public class PurchaseReturnController {
	
	private static final Logger LOG = Logger.getLogger(PurchaseReturnController.class) ;

	private static final String[] COLUMNS = { "采购退货订单号", "公司", "出仓仓库", "入库仓库", "事业部",
			"PO单号", "供应商", "采购币种", "合同号", "理货单位", "目的地", "目的地址",
			"备注", "商品名称", "商品货号", "条码", "退货数量", "退货单价", "申报单价","退货总金额", "品牌类型",
			"箱号","合同号"};
	
	private static final String[] KEYS = { "code", "merchantName", "outDepotName", "inDepotName", "buName",
			"poNo", "supplierName", "currencyLabel", "contractNo", "tallyingUnitLabel", "destinationNameLabel", "destinationAddress",
			"remark","goodsName", "goodsNo", "barcode", "returnNum", "returnPrice", "declarePrice", "returnAmount", "brandName",
			"boxNo","subcontractNo"};

	// 采购退货订单service
	@Autowired
	PurchaseReturnService purchaseReturnService;

	*//**
	 * 访问列表页面
	 * 
	 * @param model
	 *//*
	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/purchase/purchase-return-list";
	}

	*//**
	 * 获取分页数据
	 * 
	 * @param model
	 *//*
	@RequestMapping("/listPurchaseReturnOrder.asyn")
	@ResponseBody
	private ViewResponseBean listPurchaseReturnOrder(PurchaseReturnOrderDTO dto) {
		try {
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseReturnService.listPurchaseReturnPage(dto, user);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	*//**
	 *  多选订单时，需校验是否相同出仓仓库、相同供应商，若有不同则报错提示，不予保存提交。
	 * *//*
	@RequestMapping("/isSameParameter.asyn")
	@ResponseBody
	private ViewResponseBean isSameParameter(String ids) {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            purchaseReturnService.isSameParameter(ids);
		}catch(SQLException e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			LOG.error(e.getMessage(), e);
            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
        }catch(Exception e){
        	LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}
	
	*//**
	 * 访问新增页面
	 * @param model
	 * @param ids   采购订单 ID
	 * @return
	 * @throws Exception
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, String ids)throws Exception{
		User user= ShiroUtils.getUser();
		if(ids != null && StrUtils.validateIds(ids)){//ids不为空，需要获取数据
			model.addAttribute("purchaseOrderRelCode", ids);
			PurchaseReturnOrderModel purchaseReturnOrderModel = purchaseReturnService.listPurchaseOrderAndDepotOrder(StrUtils.parseIds(ids),user.getMerchantId());
			
			String depotType = purchaseReturnService.getDepotTypeByDepotId(purchaseReturnOrderModel.getOutDepotId()) ;
			
			model.addAttribute("depotType", depotType);
			model.addAttribute("detail", purchaseReturnOrderModel);
			List<PurchaseReturnItemModel> itemList = purchaseReturnOrderModel.getItemList();
			model.addAttribute("itemCount", itemList.size());
			
		}
		model.addAttribute("merchantId", user.getMerchantId());
		model.addAttribute("merchantName", user.getMerchantName());
		return "/derp/purchase/purchase-return-add";
	}
	
	*//**
	 * 访问编辑页面
	 * @param model
	 * @param ids   采购退货订单 ID
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id)throws Exception{
		//初始化数据
		PurchaseReturnOrderDTO dto= purchaseReturnService.getDTOById(id) ;
		List<PurchaseReturnItemModel> itemList = purchaseReturnService.getItemListByOrderId(id) ;
		String depotType = purchaseReturnService.getDepotTypeByDepotId(dto.getOutDepotId()) ;
		
		model.addAttribute("depotType", depotType);
		model.addAttribute("detail", dto);
		model.addAttribute("itemList", itemList);
		
		return "/derp/purchase/purchase-return-edit";
	}
	
	*//**
	 * 访问编辑页面
	 * @param model
	 * @param ids   采购退货订单 ID
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		//初始化数据
		PurchaseReturnOrderDTO dto= purchaseReturnService.getDTOById(id) ;
		List<PurchaseReturnItemModel> itemList = purchaseReturnService.getItemListByOrderId(id) ;
		model.addAttribute("detail", dto);
		model.addAttribute("itemList", itemList);
		
		return "/derp/purchase/purchase-return-details";
	}
	
	*//**
	 * 访问打托出库页面
	 * 
	 * @param model
	 *//*
	@RequestMapping("/toOutDepotPage.html")
	public String toOutDepotPage(Model model, Long id) throws Exception {
		
		PurchaseReturnOrderDTO dto= purchaseReturnService.getDTOById(id) ;
		List<PurchaseReturnItemModel> itemList = purchaseReturnService.getItemListByOrderId(id) ;
		model.addAttribute("detail", dto);
		model.addAttribute("itemList", itemList);
		
		return "/derp/purchase/purchase-return-out";
	}
	
	*//**
	 * 非必填校验保存
	 * @param dto
	 * @return
	 *//*
	@RequestMapping("/savePurchaseReturnOrder.asyn")
	@ResponseBody
	public ViewResponseBean savePurchaseReturnOrder(PurchaseReturnOrderModel model , String goodsIds, String goodsNames,
			String goodsNos, String barcodes, String returnPrices, String declarePrices, String returnNums,
			String returnAmounts, String brandNames, String boxNos, String contactNos, String remarks) {
		try{
			
			User user = ShiroUtils.getUser();
			
			List<PurchaseReturnItemModel> itemList = new ArrayList<PurchaseReturnItemModel>() ;
			
			String[] goodsIdArr = goodsIds.split(",", -1);
			String[] goodsNameArr = goodsNames.split(",", -1);
			String[] goodsNoArr = goodsNos.split(",", -1);
			String[] barcodeArr = barcodes.split(",", -1);
			String[] returnPriceArr = returnPrices.split(",", -1);
			String[] returnNumArr = returnNums.split(",", -1);
			String[] returnAmountArr = returnAmounts.split(",", -1);
			String[] brandNameArr = brandNames.split(",", -1);
			String[] contactNoArr = contactNos.split(",", -1);
			String[] boxNoArr = boxNos.split(",", -1);
			String[] remarkArr = remarks.split(",", -1);
			String[] declarePriceArr = declarePrices.split(",", -1);
			
			
			for(int i = 0 ; i < goodsIdArr.length; i ++) {
				PurchaseReturnItemModel item = new PurchaseReturnItemModel() ;
				
				item.setGoodsId(Long.valueOf(goodsIdArr[i]));
				item.setGoodsNo(goodsNoArr[i]);
				item.setGoodsName(goodsNameArr[i]);
				item.setBarcode(barcodeArr[i]);
				item.setBrandName(brandNameArr[i]);
				item.setReturnNum(Integer.valueOf(returnNumArr[i]));
				item.setReturnPrice(new BigDecimal(returnPriceArr[i]));
				item.setReturnAmount(new BigDecimal(returnAmountArr[i]));
				item.setCreateDate(TimeUtils.getNow());
				
				if(declarePriceArr.length > 0 && StringUtils.isNotBlank(declarePriceArr[i])) {
					item.setDeclarePrice(new BigDecimal(declarePriceArr[i]));
				}
				
				if(contactNoArr.length > 0 && StringUtils.isNotBlank(contactNoArr[i])) {
					item.setContractNo(contactNoArr[i]);
				}
				
				if(boxNoArr.length > 0 && StringUtils.isNotBlank(boxNoArr[i])) {
					item.setBoxNo(boxNoArr[i]);
				}
				
				if(remarkArr.length > 0 && StringUtils.isNotBlank(remarkArr[i])) {
					item.setRemark(remarkArr[i]);
				}
				
				itemList.add(item) ;
			}
			
            purchaseReturnService.saveOrModifyPurchaseReturnOrder(model, itemList, user);
            
		}catch(SQLException e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			LOG.error(e.getMessage(), e);
            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
        }catch(Exception e){
        	LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}
	
	*//**
	 * 必填校验保存
	 * @param dto
	 * @return
	 *//*
	@RequestMapping("/saveRequirePurchaseReturnOrder.asyn")
	@ResponseBody
	public ViewResponseBean saveRequirePurchaseReturnOrder(PurchaseReturnOrderModel model , String goodsIds, String goodsNames,
			String goodsNos, String barcodes, String returnPrices, String declarePrices, String returnNums,
			String returnAmounts, String brandNames, String boxNos, String contactNos, String remarks) {
		try{
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getSupplierId()).addObject(model.getBuId())
					.addObject(model.getOutDepotId()).empty();
			
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			User user = ShiroUtils.getUser();
			
			List<PurchaseReturnItemModel> itemList = new ArrayList<PurchaseReturnItemModel>() ;
			
			String[] goodsIdArr = goodsIds.split(",", -1);
			String[] goodsNameArr = goodsNames.split(",", -1);
			String[] goodsNoArr = goodsNos.split(",", -1);
			String[] barcodeArr = barcodes.split(",", -1);
			String[] returnPriceArr = returnPrices.split(",", -1);
			String[] returnNumArr = returnNums.split(",", -1);
			String[] returnAmountArr = returnAmounts.split(",", -1);
			String[] brandNameArr = brandNames.split(",", -1);
			String[] contactNoArr = contactNos.split(",", -1);
			String[] boxNoArr = boxNos.split(",", -1);
			String[] remarkArr = remarks.split(",", -1);
			String[] declarePriceArr = declarePrices.split(",", -1);
			
			
			for(int i = 0 ; i < goodsIdArr.length; i ++) {
				PurchaseReturnItemModel item = new PurchaseReturnItemModel() ;
				
				item.setGoodsId(Long.valueOf(goodsIdArr[i]));
				item.setGoodsNo(goodsNoArr[i]);
				item.setGoodsName(goodsNameArr[i]);
				item.setBarcode(barcodeArr[i]);
				item.setBrandName(brandNameArr[i]);
				item.setReturnNum(Integer.valueOf(returnNumArr[i]));
				item.setReturnPrice(new BigDecimal(returnPriceArr[i]));
				item.setReturnAmount(new BigDecimal(returnAmountArr[i]));
				item.setCreateDate(TimeUtils.getNow());
				
				if(declarePriceArr.length > 0 && StringUtils.isNotBlank(declarePriceArr[i])) {
					item.setDeclarePrice(new BigDecimal(declarePriceArr[i]));
				}
				
				if(contactNoArr.length > 0 && StringUtils.isNotBlank(contactNoArr[i])) {
					item.setContractNo(contactNoArr[i]);
				}
				
				if(boxNoArr.length > 0 && StringUtils.isNotBlank(boxNoArr[i])) {
					item.setBoxNo(boxNoArr[i]);
				}
				
				if(remarkArr.length > 0 && StringUtils.isNotBlank(remarkArr[i])) {
					item.setRemark(remarkArr[i]);
				}
				
				itemList.add(item) ;
			}
			
            purchaseReturnService.auditPurchaseReturnOrder(model, itemList, user);
            
		}catch(SQLException e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			LOG.error(e.getMessage(), e);
            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
        }catch(Exception e){
        	LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("delPurchaseReturnOrder.asyn")
	@ResponseBody
	public ViewResponseBean delPurchaseReturnOrder(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<Long> list = StrUtils.parseIds(ids);
			boolean b = purchaseReturnService.delPurchaseReturnOrder(list);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (RuntimeException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_301, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	@RequestMapping("/exportPurchaseReturn.asyn")
	public void exportPurchaseReturn(HttpServletResponse response, HttpServletRequest request, PurchaseReturnOrderDTO dto) throws Exception {
		User user= ShiroUtils.getUser(); 
		String sheetName = "采购退货订单";
		dto.setMerchantId(user.getMerchantId());
		// 获取导出的信息
		List<PurchaseReturnOrderExportDTO> result = purchaseReturnService.getDetailsByExport(dto, user);
		
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, result) ;
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	
	*//**
	 * 打托出库保存
	 * @return
	 *//*
	@RequestMapping("/purchaseReturnDelivery.asyn")
	@ResponseBody
	public ViewResponseBean purchaseReturnDelivery(PurchaseReturnOdepotModel returnModel, @RequestParam("itemList") String itemList) {
		try{
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(returnModel.getPurchaseReturnId())
					.addObject(returnModel.getReturnOutDate()).empty();
			
            if(isNull || StringUtils.isBlank(itemList)){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            
            User user = ShiroUtils.getUser();
            
            purchaseReturnService.savePurchaseReturnOdepot(returnModel, itemList, user);
            
		}catch(Exception e){
        	LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}
}
*/