/*
 * package com.topideal.report.web.reporting;
 * 
 * import com.topideal.common.constant.DERP; import
 * com.topideal.common.constant.DERP_REPORT; import
 * com.topideal.common.system.auth.User; import
 * com.topideal.common.system.bean.SelectBean; import
 * com.topideal.common.system.web.ResponseFactory; import
 * com.topideal.common.system.web.StateCodeEnum; import
 * com.topideal.common.system.web.ViewResponseBean; import
 * com.topideal.common.tools.ExcelUtil; import
 * com.topideal.common.tools.ExcelUtilXlsx; import
 * com.topideal.common.tools.FileExportUtil; import
 * com.topideal.common.tools.StrUtils; import
 * com.topideal.entity.dto.SettlementPriceDTO; import
 * com.topideal.entity.dto.SettlementPriceExamineDTO; import
 * com.topideal.entity.dto.SettlementPriceRecordDTO; import
 * com.topideal.entity.vo.reporting.SettlementPriceModel; import
 * com.topideal.entity.vo.system.MerchandiseInfoModel; import
 * com.topideal.report.service.reporting.BrandService; import
 * com.topideal.report.service.reporting.SettlementPriceService; import
 * com.topideal.report.shiro.ShiroUtils; import
 * org.apache.commons.lang3.StringUtils; import
 * org.apache.poi.xssf.streaming.SXSSFWorkbook; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.ResponseBody; import
 * org.springframework.web.multipart.MultipartFile;
 * 
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse; import java.sql.SQLException; import
 * java.util.ArrayList; import java.util.HashMap; import java.util.List; import
 * java.util.Map;
 *//**
	 * 结算价格
	 */
/*
 * @RequestMapping("/settlementPrice")
 * 
 * @Controller public class SettlementPriceController {
 * 
 * @Autowired private SettlementPriceService service;//结算价格 // 品牌信息service
 * 
 * @Autowired private BrandService brandService; //品牌
 * 
 * 
 *//**
	 * 获取导出的数量
	 */
/*
 * @RequestMapping("/getSettlementPriceCount.asyn")
 * 
 * @ResponseBody private ViewResponseBean getSettlementPriceCount(
 * HttpServletResponse response, HttpServletRequest request,SettlementPriceDTO
 * dto) throws Exception{ Map<String,Object> data=new HashMap<String,Object>();
 * try{ User user= ShiroUtils.getUser(); // 设置商家id
 * dto.setMerchantId(user.getMerchantId());
 * 
 * // 响应结果集 List<SettlementPriceDTO> result = service.listSettlementPrice(dto);
 * data.put("total",result.size()); }catch(SQLException e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_304,e); }catch(Exception e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(data); }
 * 
 *//**
	 * 导出
	 */
/*
 * @RequestMapping("/exportSettlementPrice.asyn")
 * 
 * @ResponseBody private void exportSettlementPrice(HttpServletResponse
 * response, HttpServletRequest request,SettlementPriceDTO dto) throws
 * Exception{ User user= ShiroUtils.getUser(); // 设置商家id
 * dto.setMerchantId(user.getMerchantId());
 * 
 * // 响应结果集 List<SettlementPriceDTO> result = service.listSettlementPrice(dto);
 * String sheetName = "标准成本单价列表"; String[]
 * columns={"商家","事业部","品牌","品类","商品名称","条形码","生效年月","标准成本单价","结算币种","创建日期",
 * "修改日期","调价原因","状态"}; String[] keys = {"merchantName" , "buName","brandName" ,
 * "productTypeName" , "goodsName" , "barcode" , "effectiveDate" , "price" ,
 * "currencyLabel" , "createDate" , "createDateRecord" ,
 * "adjustPriceResult","statusLabel"} ; //生成表格 SXSSFWorkbook wb =
 * ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result);
 * //导出文件 FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
 * }
 * 
 *//**
	 * 访问列表页面
	 * 
	 * @throws SQLException
	 */
/*
 * @RequestMapping("/toPage.html") public String toPage(Model model, String
 * buId, String barcode) throws SQLException { List<SelectBean> brandBean =
 * brandService.getSelectBean(); model.addAttribute("brandBean", brandBean);
 * model.addAttribute("buId", buId); model.addAttribute("barcode", barcode);
 * return "derp/reporting/settlement-price-list"; }
 *//**
	 * 访问编辑页面
	 * 
	 * @throws Exception
	 */
/*
 * @RequestMapping("/toEditPage.html") public String toEditPage(Model model,Long
 * id) throws Exception { SettlementPriceModel sPrice =
 * service.searchDetail(id); SettlementPriceModel settlementPrice = new
 * SettlementPriceModel();
 * settlementPrice.setMerchantId(sPrice.getMerchantId());
 * settlementPrice.setBarcode(sPrice.getBarcode());
 * settlementPrice.setBuId(sPrice.getBuId()); List<SettlementPriceModel>
 * settlementPriceList = service.list(settlementPrice);
 * List<SettlementPriceModel> list = new ArrayList<SettlementPriceModel>();
 * for(SettlementPriceModel spModel:settlementPriceList){ String month =
 * service.getMaxMonthByParam(spModel.getMerchantId());
 * if(StringUtils.isNotBlank(month)){ spModel.setStartDate(month); }
 * list.add(spModel); } model.addAttribute("detail", sPrice);
 * model.addAttribute("itemList", list); return
 * "derp/reporting/settlement-price-edit"; }
 *//**
	 * 访问新增页面
	 * 
	 * @throws SQLException
	 */
/*
 * @RequestMapping("/toAddPage.html") public String toAddPage() throws
 * SQLException { return "derp/reporting/settlement-price-add"; }
 *//**
	 * 访问详情页面
	 * 
	 * @throws SQLException
	 */
/*
 * @RequestMapping("/toDetailsPage.html") public String toDetailsPage(Model
 * model, Long id,String barcode) throws SQLException { SettlementPriceModel
 * sPrice = service.searchDetail(id); SettlementPriceModel settlementPrice = new
 * SettlementPriceModel();
 * settlementPrice.setMerchantId(sPrice.getMerchantId());
 * settlementPrice.setBarcode(sPrice.getBarcode()); List<SettlementPriceModel>
 * settlementPriceList = service.list(settlementPrice);
 * List<MerchandiseInfoModel> groupList=new ArrayList<>(); // 如果是组合商品
 * if(sPrice.getIsGroup().equals(DERP_REPORT.SETTLEMENTPRICE_ISGROUP_1)){ //
 * 获取组合商品信息 //groupList =
 * service.getAllGroupMerchandiseByGroupId(sPrice.getGoodsId());
 * model.addAttribute("groupList", groupList); }
 * 
 * for (SettlementPriceModel settlementPriceModel : settlementPriceList) {
 * String currency = settlementPriceModel.getCurrency(); // 币种
 * 
 * if(StringUtils.isNotBlank(currency)) { currency =
 * DERP.getLabelByKey(DERP.currencyCodeList, currency) ; }
 * settlementPriceModel.setCurrency(currency); }
 * 
 * model.addAttribute("detail", sPrice); model.addAttribute("itemList",
 * settlementPriceList); return "derp/reporting/settlement-price-details"; }
 *//**
	 * 获取分页数据
	 */
/*
 * @RequestMapping("/listPrice.asyn")
 * 
 * @ResponseBody private ViewResponseBean listPrice(SettlementPriceDTO dto) {
 * try{ User user= ShiroUtils.getUser(); // 设置商家id
 * dto.setMerchantId(user.getMerchantId()); // 响应结果集 dto =
 * service.listPriceDTO(dto); }catch(SQLException e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_304,e); }catch(Exception e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(dto); }
 * 
 *//**
	 * 删除
	 */
/*
 * @SuppressWarnings({ "rawtypes", "unchecked" })
 * 
 * @RequestMapping("/delPrice.asyn")
 * 
 * @ResponseBody public ViewResponseBean delPrice(String ids) { try { //
 * 校验id是否正确 boolean isRight = StrUtils.validateIds(ids); if (!isRight) { //
 * 输入信息不完整 return ResponseFactory.error(StateCodeEnum.ERROR_303); } List list =
 * StrUtils.parseIds(ids); boolean b = service.delPrice(list); if (!b) { return
 * ResponseFactory.error(StateCodeEnum.ERROR_301); } } catch (SQLException e) {
 * return ResponseFactory.error(StateCodeEnum.ERROR_302, e); } catch
 * (NullPointerException e) { return
 * ResponseFactory.error(StateCodeEnum.ERROR_304, e); } catch (Exception e) {
 * return ResponseFactory.error(StateCodeEnum.ERROR_305, e); } return
 * ResponseFactory.success(); }
 * 
 *//**
	 * 导入页面
	 */
/*
 * @RequestMapping("/toImportPage.html") public String toImportPage() { return
 * "derp/reporting/settlement-price-import"; }
 * 
 *//**
	 * 导入结算价格信息--有问题，获取不到单位名称、原产国名称
	 */
/*
 * @SuppressWarnings("rawtypes")
 * 
 * @RequestMapping("/importPrice.asyn")
 * 
 * @ResponseBody public ViewResponseBean importPrice(@RequestParam(value =
 * "file", required = false) MultipartFile file) { Map resultMap = new
 * HashMap();// 返回的结果集 try { if (file == null) { // 输入信息不完整 return
 * ResponseFactory.error(StateCodeEnum.ERROR_303); } Map<Integer,
 * List<List<Object>>> data =
 * ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
 * file.getOriginalFilename(), 1); if (data == null) {// 数据为空 return
 * ResponseFactory.error(StateCodeEnum.ERROR_302); } User user=
 * ShiroUtils.getUser(); resultMap = service.importPrice(data, user); } catch
 * (NullPointerException e) { return
 * ResponseFactory.error(StateCodeEnum.ERROR_304, e); } catch (Exception e) {
 * return ResponseFactory.error(StateCodeEnum.ERROR_305, e); } return
 * ResponseFactory.success(resultMap); }
 *//**
	 * 新增
	 * 
	 * @param model
	 */
/*
 * @RequestMapping("/saveSettlementPrice.asyn")
 * 
 * @ResponseBody public ViewResponseBean saveSettlementPrice(String json) { try
 * { User user= ShiroUtils.getUser(); if(json == null ||
 * StringUtils.isBlank(json)){ //输入信息不完整 return
 * ResponseFactory.error(StateCodeEnum.ERROR_303); } // 存储商品信息 boolean b
 * =service.saveSettlementPrice(json,user.getMerchantId(),user.getMerchantName()
 * , user); if (!b) { return ResponseFactory.error(StateCodeEnum.ERROR_301); }
 * }catch(Exception e){ e.printStackTrace(); return
 * ResponseFactory.error(StateCodeEnum.ERROR_305, e); } return
 * ResponseFactory.success(); }
 *//**
	 * 修改
	 * 
	 * @param model
	 */
/*
 * @RequestMapping("/modifySettlementPrice.asyn")
 * 
 * @ResponseBody public ViewResponseBean modifySettlementPrice(String json) {
 * try { User user= ShiroUtils.getUser(); if(json == null ||
 * StringUtils.isBlank(json)){ //输入信息不完整 return
 * ResponseFactory.error(StateCodeEnum.ERROR_303); } // 存储商品信息 boolean b
 * =service.modifySettlementPrice(json, user.getMerchantId(),
 * user.getMerchantName(), user); if (!b) { return
 * ResponseFactory.error(StateCodeEnum.ERROR_301); } }catch (Exception e) {
 * e.printStackTrace(); return ResponseFactory.error(StateCodeEnum.ERROR_305,
 * e); } return ResponseFactory.success(); }
 * 
 *//**
	 * 获取未关账的最早日期
	 * 
	 * @param model
	 */
/*
 * @RequestMapping("/getMinMonthByStatus.asyn")
 * 
 * @ResponseBody public ViewResponseBean getMinMonthByStatus(String barcode) {
 * String month = ""; try { User user= ShiroUtils.getUser(); if(barcode == null
 * && StringUtils.isBlank(barcode)){ //输入信息不完整 return
 * ResponseFactory.error(StateCodeEnum.ERROR_303); } month =
 * service.getMaxMonthByParam(user.getMerchantId()); }catch (Exception e) {
 * e.printStackTrace(); return ResponseFactory.error(StateCodeEnum.ERROR_305,
 * e); } return ResponseFactory.success(month); }
 * 
 *//**
	 * 获取变更记录分页数据
	 */
/*
 * @RequestMapping("/listRecordPrice.asyn")
 * 
 * @ResponseBody private ViewResponseBean
 * listRecordPrice(SettlementPriceRecordDTO dto) { try{ // 响应结果集 dto =
 * service.listRecordPriceDTO(dto); }catch(SQLException e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_304,e); }catch(Exception e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(dto); }
 * 
 *//**
	 * 根据商品ID查看组合商品的详情
	 * 
	 * @throws SQLException
	 */
/*
 * @RequestMapping(value="listAllGroupMerchandiseByGroupId.asyn")
 * 
 * @ResponseBody private ViewResponseBean listAllGroupMerchandiseByGroupId(Model
 * model,Long goodsId) throws SQLException { List<MerchandiseInfoModel>
 * groupList=new ArrayList<>(); // 响应结果集 //groupList =
 * service.getAllGroupMerchandiseByGroupId(goodsId);
 * model.addAttribute("groupList", groupList);
 * 
 * return ResponseFactory.success(model); }
 * 
 *//**
	 * 提交审核
	 * 
	 * @param ids
	 * @return
	 */
/*
 * @SuppressWarnings("rawtypes")
 * 
 * @RequestMapping("toExamine.asyn")
 * 
 * @ResponseBody public ViewResponseBean toExamine(String ids) { try { //
 * 校验id是否正确 boolean isRight = StrUtils.validateIds(ids); if (!isRight) { //
 * 输入信息不完整 return ResponseFactory.error(StateCodeEnum.ERROR_303); } List list =
 * StrUtils.parseIds(ids);
 * 
 * User user= ShiroUtils.getUser();
 * 
 * service.saveAudit(list, user) ;
 * 
 * return ResponseFactory.success() ; } catch (SQLException e) { return
 * ResponseFactory.error(StateCodeEnum.ERROR_302); } catch (NullPointerException
 * e) { return ResponseFactory.error(StateCodeEnum.ERROR_304); } catch
 * (Exception e) { return ResponseFactory.error(StateCodeEnum.ERROR_305, e); } }
 * 
 *//**
	 * 访问审批列表页面
	 * 
	 * @throws SQLException
	 */
/*
 * @RequestMapping("/toExaminePage.html") public String toExaminePage() throws
 * SQLException { return "derp/reporting/settlement-price-examine-list"; }
 * 
 *//**
	 * 获取审批记录分页数据
	 */
/*
 * @RequestMapping("/listExamineList.asyn")
 * 
 * @ResponseBody private ViewResponseBean
 * listExamineList(SettlementPriceExamineDTO model) { try{ User user=
 * ShiroUtils.getUser(); // 设置商家id model.setMerchantId(user.getMerchantId()); //
 * 响应结果集 model = service.listExamineList(model); }catch(Exception e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(model); }
 * 
 *//**
	 * 提交审核
	 * 
	 * @param ids
	 * @return
	 *//*
		 * @SuppressWarnings("rawtypes")
		 * 
		 * @RequestMapping("examine.asyn")
		 * 
		 * @ResponseBody public ViewResponseBean examine(String ids , String status) {
		 * try { // 校验id是否正确 boolean isRight = StrUtils.validateIds(ids); if (!isRight)
		 * { // 输入信息不完整 return ResponseFactory.error(StateCodeEnum.ERROR_303); }
		 * 
		 * if(!(DERP_REPORT.SETTLEMENTPRICE_STATUS_032.equals(status) ||
		 * DERP_REPORT.SETTLEMENTPRICE_STATUS_033.equals(status))) { return
		 * ResponseFactory.error(StateCodeEnum.ERROR_305); }
		 * 
		 * List list = StrUtils.parseIds(ids);
		 * 
		 * User user= ShiroUtils.getUser();
		 * 
		 * service.examine(list, status , user) ;
		 * 
		 * return ResponseFactory.success() ; } catch (Exception e) { return
		 * ResponseFactory.error(StateCodeEnum.ERROR_305, e); } } }
		 */