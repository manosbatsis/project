package com.topideal.webapi.main;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.enums.ShopTypeEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.dto.main.MerchantShopShipperDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.entity.vo.main.MerchantShopShipperModel;
import com.topideal.service.main.MerchantShopService;
import com.topideal.service.main.MerchantShopShipperService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.MerchantShopRelForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家店铺 controller层
 * @author lian_
 */

@RestController
@RequestMapping("/webapi/system/merchantShop")
@Api(tags = "商家店铺数据")
public class APIMerchantShopController {

	@Autowired
	private MerchantShopService service;
	@Autowired
	private MerchantShopShipperService shopShipperService;
	
	/**
	 * 访问列表页面
	 */
	/*@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/main/merchant-shop-list";
	}*/
	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@ApiOperation(value = "访问新增页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toAddPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toAddPage() throws SQLException {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			List<MerchantInfoModel> merchantBean = service.getMerchant();
			resultMap.put("merchantBean", merchantBean);
			// 店铺类型
			List<Map<String, Object>> shopTypeList = new ArrayList<>() ;
		       for (ShopTypeEnum shopType : ShopTypeEnum.values()) {
		           Map<String, Object> map = new HashMap<String, Object>();
		           map.put("selectValue", shopType.getKey());
		           map.put("selectLable", shopType.getValue());
		           shopTypeList.add(map);
		       }
		       resultMap.put("shopTypeList",shopTypeList);
		       // 数据来源
		       List<Map<String, Object>> dataSourceList = new ArrayList<>() ;
		       for (DerpBasic basic : DERP.dataSourceList) {
		           Map<String, Object> map = new HashMap<String, Object>();
		           map.put("selectValue", basic.getKey());
		           map.put("selectLable", basic.getValue());
		           dataSourceList.add(map);
		       }
		       resultMap.put("dataSourceList",dataSourceList);
		       return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 访问编辑页面
	 * @param merchantId
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toEditPage(Long id) throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			List<MerchantInfoModel> merchantBean = service.getMerchant();
			MerchantShopRelDTO shopModel = service.searchDetail(id);

			//表体货主公司
			MerchantShopShipperModel shipperModel = new MerchantShopShipperModel();
			shipperModel.setShopId(shopModel.getId());
			List<MerchantShopShipperModel> shipperList = shopShipperService.getListByModel(shipperModel);
			
			//销售类型list
			ArrayList<DerpBasic> saleTypeList = DERP_SYS.shopShipperSaleTypeList;

			int isMerchantRequired=0;//0:非必填 1：必填
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(shopModel.getStoreTypeCode())){
				isMerchantRequired=1;
			}			
			resultMap.put("isMerchantRequired", isMerchantRequired);
			resultMap.put("merchantBean", merchantBean);
			resultMap.put("detail", shopModel);
			resultMap.put("shipperList", shipperList);
			resultMap.put("shipperListSize", shipperList.size());
			resultMap.put("saleTypeList", saleTypeList);
		    return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 访问详情页面
	 */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(Long id) throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			MerchantShopRelDTO shopModel = service.searchDetail(id);
			//表体货主公司
			MerchantShopShipperModel shipperModel = new MerchantShopShipperModel();
			shipperModel.setShopId(shopModel.getId());
			List<MerchantShopShipperDTO> shipperList = shopShipperService.getListDTOByModel(shipperModel);
			
			resultMap.put("detail", shopModel);
			resultMap.put("shipperList", shipperList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/listShop.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listShop(MerchantShopRelForm form) {
		try{
			User user = ShiroUtils.getUserByToken(form.getToken());
			MerchantShopRelDTO dto=new MerchantShopRelDTO();
			dto.setMerchantName(form.getMerchantName());
			dto.setShopName(form.getShopName());
			dto.setShopCode(form.getShopCode());
			dto.setStorePlatformCode(form.getStorePlatformCode());
			dto.setStatus(form.getStatus());
			dto.setDataSourceCode(form.getDataSourceCode());
			dto.setDepotId(form.getDepotId());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = service.getListByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 新增
	 * */
	@ApiOperation(value = "新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "json", required = true)
	})
	@PostMapping(value="/saveShop.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveShop(String token,String json) {
		try {
			// 实例化JSON对象
	        JSONObject jsonData=JSONObject.fromObject(json);
	        Map classMap = new HashMap();
			classMap.put("shipperList",Map.class);
			MerchantShopRelDTO model = (MerchantShopRelDTO) JSONObject.toBean(jsonData, MerchantShopRelDTO.class,classMap);			
			User user = ShiroUtils.getUserByToken(token);
			model.setOperator(user.getName());//操作人员
			service.saveShop(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 修改
	 * */
	@ApiOperation(value = "修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "json", required = true)
	})
	@PostMapping(value="/modifyShop.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyShop(String token,String json) {
		try{
			// 实例化JSON对象
	        JSONObject jsonData=JSONObject.fromObject(json);
	        Map classMap = new HashMap();
			classMap.put("shipperList",Map.class);
			MerchantShopRelDTO model = (MerchantShopRelDTO) JSONObject.toBean(jsonData, MerchantShopRelDTO.class,classMap);
			
			User user = ShiroUtils.getUserByToken(token);
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            model.setOperator(user.getName());//操作人员
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            boolean b = service.modifyShop(model);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

	}

	/**
	 * 根据ID获取详情
	 */
	@ApiOperation(value = "根据ID获取详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getShopDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getShopDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		MerchantShopRelDTO model = new MerchantShopRelDTO();
		try {
			model = service.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id集合 多个用英文逗号隔开", required = true)
	})
	@PostMapping(value="/delShop.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delShop(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = service.delShop(list);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}

	/**
	 * 获取下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSelectBean() {
		List<MerchantShopRelModel> result = new ArrayList<MerchantShopRelModel>();
		try {
			result = service.getSelectMerchantShopRelBean(new MerchantShopRelModel());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 根据平台获取店铺下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "storePlatformCode", value = "平台编码")
	})
	@PostMapping(value="/getShopByPlatformCodeUrl.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getShopByPlatformCodeUrl(String token,String storePlatformCode) {
		List<MerchantShopRelModel> result = new ArrayList<MerchantShopRelModel>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			MerchantShopRelModel model=new MerchantShopRelModel();
			model.setStorePlatformCode(storePlatformCode);
			if (!"1".equals(user.getUserType())) {
				model.setMerchantId(user.getMerchantId());
			}			
			model.setStatus(DERP_SYS.MERCHANTSHOPREL_STATUS_1);
			result = service.getListByModel(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 根据查询条件导出店铺信息
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportShop.asyn")
	public void exportShop(MerchantShopRelForm form, HttpServletResponse response, HttpServletRequest request) {
		try {
			//表头信息
			MerchantShopRelDTO dto=new MerchantShopRelDTO();
			dto.setMerchantName(form.getMerchantName());
			dto.setShopName(form.getShopName());
			dto.setShopCode(form.getShopCode());
			dto.setStorePlatformCode(form.getStorePlatformCode());
			dto.setStatus(form.getStatus());
			dto.setDataSourceCode(form.getDataSourceCode());
			dto.setDepotId(form.getDepotId());
			List<Map<String,Object>> shopList = service.getExportListByDTO(dto);

			//店铺信息
			String headSheetName = "店铺信息";
			String[] headColumns = {"店铺类型","店铺统一ID","店铺编码","店铺名称","电商平台","运营类型","开店公司","开店事业部","客户名称","仓库名称","状态"};
			String[] headKeys = {"storeTypeName","shopUnifyId","shopCode","shopName","storePlatformName","shopTypeName","merchantName","buName","customerName","depotName", "status"};

			//店铺货主信息
			List<Map<String,Object>> itemList = shopShipperService.listForExportShipper(dto);

			String itemSheetName = "店铺货主信息";
			String[] itemColumns = {"店铺编码","店铺名称","货主公司","货主事业部"};
			String[] itemKeys = {"shopCode","shopName","merchantName","buName"};
			//生成表格
			ExportExcelSheet headSheet = ExcelUtilXlsx.createSheet(headSheetName, headColumns, headKeys, shopList);
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, itemList);

			List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
			sheets.add(headSheet) ;
			sheets.add(itemSheet) ;

			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
			//导出文件
			String fileName = "店铺信息";
			FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
		
	}

}
