package com.topideal.webapi.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.dto.main.ProductInfoDTO;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.service.base.BrandService;
import com.topideal.service.main.MerchandiseCatService;
import com.topideal.service.main.ProductInfoService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 产品管理 控制层
 */ 
@RestController
@RequestMapping("/webapi/system/product")
@Api(tags = "货品管理")
public class APIProductInfoController {

	//品牌信息service
	@Autowired
	private BrandService brandService;
	//货品
	@Autowired
	private ProductInfoService productInfoService;
	// 商品分类
	@Autowired
	private MerchandiseCatService merchandiseCatService;
	
	/**
	 * 访问列表页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toPage() throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			List<SelectBean> brandBean = brandService.getSelectBean();
			resultMap.put("brandBean", brandBean);
			// 获取二级类目
			MerchandiseCatModel merchandiseCatModel =new MerchandiseCatModel();
			merchandiseCatModel.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
			List<SelectBean> productTypeBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel);
			resultMap.put("productTypeBean", productTypeBean);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	
	/**
	 * 访问详情页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(Long id)throws Exception{
		try {
			ProductInfoDTO merchandiseInfo = productInfoService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,merchandiseInfo);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toEditPage(Long id)throws Exception{
		
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			ProductInfoDTO merchandiseInfo = productInfoService.searchDetail(id);
			resultMap.put("detail", merchandiseInfo);
			// 获取一级分类
			MerchandiseCatModel merchandiseCatModel1 =new MerchandiseCatModel();
			merchandiseCatModel1.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_0);
			List<SelectBean> oneCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel1);
			resultMap.put("oneCatBean", oneCatBean);
			// 根据一级分类id获取二级分类
			MerchandiseCatModel merchandiseCatModel2 =new MerchandiseCatModel();
			merchandiseCatModel2.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
			merchandiseCatModel2.setParentId(merchandiseInfo.getProductTypeId0());
			List<SelectBean> twoCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel2);
			resultMap.put("twoCatBean", twoCatBean);	
			List<SelectBean> brandBean = brandService.getSelectBean();
			resultMap.put("brandBean", brandBean);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	
	/**
	 * 获取分页数据 
	 * @param model 商品信息
	 * @return
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "name", value = "货品名称"),
			@ApiImplicitParam(name = "barcode", value = "商品条码"),			
			@ApiImplicitParam(name = "brandId", value = "商品品牌id"),
			@ApiImplicitParam(name = "productTypeId", value = "二级类目id")	
	})
	@PostMapping(value="/listProduct.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listProduct(String token,String name,String barcode,
			Long brandId,Long productTypeId,int begin,int pageSize) {
		try{
			User user = ShiroUtils.getUserByToken(token);
			MerchandiseInfoDTO dto=new MerchandiseInfoDTO();
			dto.setName(name);
			dto.setBarcode(barcode);
			dto.setBrandId(brandId);
			dto.setProductTypeId(productTypeId);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = productInfoService.getListByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
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
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "productTypeId0", value = "一级分类id",required = true),
			@ApiImplicitParam(name = "productTypeId", value = "二级分类id",required = true),
			@ApiImplicitParam(name = "brandId", value = "brandId",required = true)
	})
	@PostMapping(value="/modifyProduct.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyProduct(Long id,Long productTypeId0,Long productTypeId,Long brandId) throws SQLException {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            ProductInfoModel model=new ProductInfoModel();
            model.setId(id);
            model.setProductTypeId0(productTypeId0);
            model.setProductTypeId(productTypeId);
            model.setBrandId(brandId);
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            boolean b = productInfoService.modifyProduct(model);
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
	 * 批量修改
	 * */
	@ApiOperation(value = "批量修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id的集合,多个用英文逗号隔开", required = true),
			@ApiImplicitParam(name = "productTypeId", value = "二级分类id",required = true)
	})
	@PostMapping(value="/modifyBatchProduct.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyBatchProduct(String ids, String productTypeId) throws SQLException {
		try{
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			List <Long>list = StrUtils.parseIds(ids);
			boolean b = productInfoService.updateBatch(list,productTypeId);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	/**
	 * 根据ID获取货品详情
	 * @param id  产品ID
	 * @return
	 */
	@ApiOperation(value = "根据ID获取货品详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getProductDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getProductDetails(Long id) {
		//校验id是否正确
        boolean isRight = StrUtils.validateId(id);
        if(!isRight){
            //输入信息不完整
        	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        }
		try{
			ProductInfoDTO model = productInfoService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
}
