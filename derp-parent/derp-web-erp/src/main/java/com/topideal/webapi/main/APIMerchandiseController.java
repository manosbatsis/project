package com.topideal.webapi.main;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topideal.entity.dto.main.UpdateDepotOrderDTO;
import com.topideal.webapi.form.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.entity.vo.base.PackTypeModel;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.MerchandiseCustomsRelModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.service.base.BrandService;
import com.topideal.service.base.CountryOriginService;
import com.topideal.service.base.CustomsAreaConfigService;
import com.topideal.service.base.PackTypeService;
import com.topideal.service.base.UnitInfoService;
import com.topideal.service.main.MerchandiseCatService;
import com.topideal.service.main.MerchandiseService;
import com.topideal.service.main.ProductInfoService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 商品管理 控制层
 */
@RestController
@RequestMapping("/webapi/system/merchandise")
@Api(tags = "商品管理")
public class APIMerchandiseController {
	
	private static final Logger LOG = Logger.getLogger(APIMerchandiseController.class) ;
	
	/** 标题  *//*
	private static final String[] COLUMNS= {"商品货号","商品条码","标准条码","外仓统一码","状态","商品名称","计量单位","单价","所属商家","数据来源",
			"是否备案","一级类目","二级类目","工厂编码","品牌"};
	private static final String[] KEYS= {"goodsNo","barcode","commbarcode","outDepotFlagLabel","statusLabel","name", "unitName" ,"filingPrice","merchantName","sourceLabel",
			"isRecordLabel","productTypeName0","productTypeName","factoryNo","brandName"};*/

	// 商品信息service
	@Autowired
	private MerchandiseService merchandiseService;
	// 品牌信息service
	@Autowired
	private BrandService brandService;
	// 原产国service
	@Autowired
	private CountryOriginService countryOriginService;
	// 商品分类
	@Autowired
	private MerchandiseCatService merchandiseCatService;
	// 标准单位
	@Autowired
	private UnitInfoService unitInfoService;

	// 包装方式
	@Autowired
	private PackTypeService packTypeService;

	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 货品表
	@Autowired
	private ProductInfoService productInfoService;
	
	@Autowired
	private CustomsAreaConfigService customsAreaConfigService;
	
	/**
	 * 导入商品
	 */
	@ApiOperation(value = "商品关联仓库导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importMerchandiseDepotRel.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importMerchandiseDepotRel(String token,@RequestParam(value = "file", required = false) MultipartFile file,String linshi) {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			resultMap = merchandiseService.importMerchandiseDepotRel(data, user);
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			Integer pass = (Integer)resultMap.get("pass"); 

			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 商品关联仓库保存
	 */
	@ApiOperation(value = "商品关联仓库保存")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "goodsId", value = "商品id", required = true),
			@ApiImplicitParam(name = "depotIds", value = "仓库id集合,多个用英文逗号隔开")
	})
	@PostMapping(value="/saveMerchandiseDepotRel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveMerchandiseDepotRel(Long goodsId,String depotIds,String token) {
		try {
			User user = ShiroUtils.getUserByToken(token); 
			if (goodsId==null) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"商品id不能为空");
			} 
			boolean b = merchandiseService.saveMerchandiseDepotRel(user,goodsId,depotIds);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
		}
	}
	
	
	/**
	 * 导入商品图片
	 */
	@ApiOperation(value = "导入商品图片")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importMerchandiseImage.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean importMerchandiseImage(String token,@RequestParam(value = "file", required = false) MultipartFile file) {
		
		try {
			long time = TimeUtils.getNow().getTime();
			//String unzipFilePath="C:/Users/杨创/Desktop/新建文件夹 (4)/jieya/"+time+"/";
			String unzipFilePath = ApolloUtilDB.systemBasepath+"/goodsImage/"+time+"/";//商品图片压缩包文件存放目录
						
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			File f = new File(unzipFilePath);
			// 创建文件夹
			if (!f.exists()) {
			f.mkdirs();
			}
			
			// 上传到服务器
			File newFile = new File(unzipFilePath + file.getOriginalFilename());
			file.transferTo(newFile);
			
			Map<String, MerchandiseInfoModel> barcodeMap=new HashMap<>();
			
			User user = ShiroUtils.getUserByToken(token);
			ZipEntry entry ; //得到zip包中文件 
			ZipFile zf = new ZipFile(unzipFilePath+file.getOriginalFilename(),Charset.forName("gbk")); 
			Enumeration enu=zf.entries(); 
			while(enu.hasMoreElements()){ 
				entry=(ZipEntry)enu.nextElement(); 
				if(entry.getName().contains(".bmp")||entry.getName().contains(".jpg")||entry.getName().contains(".jpeg")||
				entry.getName().contains(".png")||entry.getName().contains(".tif")||entry.getName().contains(".gif")||
				entry.getName().contains(".pcx")||entry.getName().contains(".tga")||entry.getName().contains(".exif")||
				entry.getName().contains(".fpx")||entry.getName().contains(".svg")||entry.getName().contains(".psd")||
				entry.getName().contains(".cdr")||entry.getName().contains(".pcd")||entry.getName().contains(".dxf")||
				entry.getName().contains(".ufo")||entry.getName().contains(".eps")||entry.getName().contains(".ai")||
				entry.getName().contains(".raw")||entry.getName().contains(".wmf")||entry.getName().contains(".webp")
				){ 
					//
				}else {
					continue; 
				}

				if(!entry.isDirectory()){ //找到文本文件，转换字字节流 
					InputStream is = zf.getInputStream(entry); 
					
					byte[] bytes = IOUtils.toByteArray(is);					
					String names = entry.getName();
					String[] splitArr = names.split("/");
					String string0 = splitArr[0];//图片上传
					String barcode = splitArr[1];//条码
					String fileNameStr = splitArr[2];//名称
					//String[] fileNameArr= fileNameStr.split(".");
					//String fileName0 = fileNameArr[0];
					//String fileName1 = fileNameArr[1];

		            String ext = fileNameStr.substring(fileNameStr.lastIndexOf(".")+1);
		            String fileName = fileNameStr.substring(0,fileNameStr.lastIndexOf("."));
		            //转base64
		            String encodeBase64String = Base64.encodeBase64String(bytes);
		            JSONObject jsonObject = new JSONObject();//推送内容
		    		jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_00003.getCode());
		    		jsonObject.put("fileName",fileName);
		    		jsonObject.put("fileBytes", encodeBase64String);
		    		jsonObject.put("fileExt",ext);
		    		jsonObject.put("fileSize",String.valueOf(imageSize(encodeBase64String)));
		    		String url=null;
		    		try {
		    			String resultMsg=SmurfsUtils.send(jsonObject,SmurfsAPIEnum.SMURFS_UPLOAD_FILE);
		    			System.out.println(resultMsg); 
		    			 JSONObject jsonObj = JSONObject.fromObject(resultMsg);
		    			 url = jsonObj.getString("url");
					} catch (Exception e) {
						e.printStackTrace();
						url=null;
					}
		    		if (is != null) is.close();
		    		if (barcodeMap.containsKey(barcode)) {
		    	    	MerchandiseInfoModel updateModel = barcodeMap.get(barcode);
		    	    	if (updateModel.getImageUrl1()==null) {
		    	    		updateModel.setImageUrl1(url);	
						}else if (updateModel.getImageUrl2()==null) {
							updateModel.setImageUrl2(url);
						}else if (updateModel.getImageUrl3()==null) {
							updateModel.setImageUrl3(url);
						}else if (updateModel.getImageUrl4()==null) {
							updateModel.setImageUrl4(url);
						}else if (updateModel.getImageUrl5()==null) {
							updateModel.setImageUrl5(url);
						}
		    	    	
		    	    	barcodeMap.put(barcode, updateModel);
		    		}else {
		    			MerchandiseInfoModel updateModel=new MerchandiseInfoModel();
			    		 //updateModel.setId(id);
			    		updateModel.setModifyDate(TimeUtils.getNow());//图片修改日期
			    	    updateModel.setImageId(user.getId());//图片修改人id
			    	    updateModel.setImageName(user.getName());//图片修改人名称
			    	    updateModel.setBarcode(barcode);
			    	    updateModel.setImageUrl1(url);
			    	    barcodeMap.put(barcode, updateModel);
					}
		    		
		    	    
				}
			
			}
			zf.close();
			File delFile = new File(unzipFilePath);
			// 删除文件中数据
			if(delFile.exists()){//目录存在
				//System.gc();
				//删除子文件
				File[] files = delFile.listFiles();
				if(files!=null&&files.length>0){
					for(File delf:files){
						boolean delete = delf.delete();
						System.out.println("删除子文件:"+delete);
					}
				}
				//删除文件夹
				boolean delete = delFile.delete();
				System.out.println("删除文件夹:"+delete);
			}
					
			Map resultMap  = merchandiseService.uploadImportImage(barcodeMap,user.getMerchantId());
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);//成功
		}catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	
	/**
	 * 
	 * @param image
	 * @return
	 */
	 public  Integer imageSize(String image){
	        String str=image.substring(22); // 1.需要计算文件流大小，首先把头部的data:image/png;base64,（注意有逗号）去掉。
	        Integer equalIndex= str.indexOf("=");//2.找到等号，把等号也去掉
	        if(str.indexOf("=")>0) {
	            str=str.substring(0, equalIndex);
	        }
	        Integer strLength=str.length();//3.原来的字符流大小，单位为字节
	        Integer size=strLength-(strLength/8)*2;//4.计算后得到的文件流大小，单位为字节
	        return size;
	    }
	
	/**
	 * 访问列表页面
	 */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseToPageResponseDTO> toPage() throws Exception {
		MerchandiseToPageResponseDTO responseDTO=new MerchandiseToPageResponseDTO();
		try {
			List<SelectBean> brandBean = brandService.getSelectBean();
			responseDTO.setBrandBean(brandBean);
			// 获取二级类目
			MerchandiseCatModel merchandiseCatModel =new MerchandiseCatModel();
			merchandiseCatModel.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
			List<SelectBean> productTypeBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel);
			responseDTO.setProductTypeBean(productTypeBean);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 访问新增页面
	 */
	@ApiOperation(value = "访问新增页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id")
	})
	@PostMapping(value="/toAddPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseToAddPageResponseDTO> toAddPage(Long id) throws Exception {
		try {
			MerchandiseToAddPageResponseDTO responseDTO=new MerchandiseToAddPageResponseDTO();
			List<SelectBean> brandBean = brandService.getSelectBean();
			responseDTO.setBrandBean(brandBean);
			List<SelectBean> countryBean = countryOriginService.getSelectBean();
			responseDTO.setCountryBean(countryBean);
			// 获取一级分类
			MerchandiseCatModel merchandiseCatModel =new MerchandiseCatModel();
			merchandiseCatModel.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_0);
			List<SelectBean> catBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel);
			responseDTO.setCatBean(catBean);
			List<SelectBean> unitBean = unitInfoService.getSelectBean();
			responseDTO.setUnitBean(unitBean);
			List<SelectBean> packBean = packTypeService.getSelectBean();
			responseDTO.setPackBean(packBean);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 根据一级分类获取二级分类
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "根据一级分类获取二级分类")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id")
	})
	@PostMapping(value="/getTwoLevel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getTwoLevel(Long id) throws Exception {

		// 获取一级分类		
		try {
			MerchandiseCatModel merchandiseCatModel =new MerchandiseCatModel();
			merchandiseCatModel.setParentId(id);
			merchandiseCatModel.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
			List<SelectBean> catBeanList = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,catBeanList);//成功
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
	public ResponseBean<MerchandiseToDetailsResponseDTO> toDetailsPage(Long id) throws Exception {		
		try {
			MerchandiseToDetailsResponseDTO responseDTO=new MerchandiseToDetailsResponseDTO();			
			MerchandiseInfoDTO merchandiseInfo = merchandiseService.searchDetail(id);
			responseDTO.setDetail(merchandiseInfo);
			BrandModel brandModel=new BrandModel();
			if (merchandiseInfo.getBrandId()!=null) {
				brandModel = brandService.getDetails(merchandiseInfo.getBrandId());
			}
			if (brandModel==null)brandModel=new BrandModel();
			responseDTO.setBrandName(brandModel.getName());
			CountryOriginModel countryOriginModel=new CountryOriginModel();
			if (merchandiseInfo.getCountyId()!=null) {
				countryOriginModel = countryOriginService.getDetails(merchandiseInfo.getCountyId());
			}
			if (countryOriginModel==null)countryOriginModel=new CountryOriginModel();
			responseDTO.setCountryName(countryOriginModel.getName());
			
			UnitInfoModel unitInfoModel=new UnitInfoModel();
			if (merchandiseInfo.getUnit()!=null) {
				unitInfoModel = unitInfoService.getDetails(merchandiseInfo.getUnit());
			}
			
			PackTypeModel packType=new PackTypeModel();
			if (StringUtils.isNotBlank(merchandiseInfo.getPackType())) {
				packType.setCode(merchandiseInfo.getPackType());
				packTypeService.serchByModel(packType);
			}
			
			if (unitInfoModel==null)unitInfoModel=new UnitInfoModel();
			responseDTO.setUnitName(unitInfoModel.getName());
			MerchandiseCustomsRelModel relQuery=new MerchandiseCustomsRelModel();
			relQuery.setGoodsId(id);
			List<MerchandiseCustomsRelModel> relList = customsAreaConfigService.getmerchandiseCustomsRelList(relQuery);
			String customsAreaIdStr="";
			for (MerchandiseCustomsRelModel relModel : relList) {
				customsAreaIdStr=customsAreaIdStr+relModel.getCustomsAreaName()+",";
			}
			responseDTO.setCustomsAreaIdStr(customsAreaIdStr);
			// 日期转换
			SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (merchandiseInfo.getCreateDate()!= null) {
				String createDate = sft.format(merchandiseInfo.getCreateDate());
				responseDTO.setCreateDate(createDate);
			}
			if (merchandiseInfo.getModifyDate()!= null) {
				String modifyDate = sft.format(merchandiseInfo.getModifyDate());
				responseDTO.setModifyDate(modifyDate);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 访问编辑页面
	 */
	@ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseToEditPageResponseDTO> toEditPage(Long id) throws Exception {
		try {
			MerchandiseToEditPageResponseDTO responseDTO=new MerchandiseToEditPageResponseDTO();
			MerchandiseInfoDTO merchandiseInfo = merchandiseService.searchDetail(id);
			responseDTO.setDetail(merchandiseInfo);
			//获取商家关区关系表数据
			MerchandiseCustomsRelModel relModel=new MerchandiseCustomsRelModel();
			relModel.setGoodsId(merchandiseInfo.getId());
			List<MerchandiseCustomsRelModel> relList = customsAreaConfigService.getmerchandiseCustomsRelList(relModel);
			String customsAreaIdStr="";
			for (int i = 0; i < relList.size(); i++) {
				Long customsAreaId = relList.get(i).getCustomsAreaId();
				if (i==relList.size()-1) {
					customsAreaIdStr=customsAreaIdStr+customsAreaId;
				}else {
					customsAreaIdStr=customsAreaIdStr+customsAreaId+",";
				}
				
			}
			responseDTO.setCustomsAreaIdStr(customsAreaIdStr);
			// 获取一级分类
			MerchandiseCatModel merchandiseCatModel1 =new MerchandiseCatModel();
			merchandiseCatModel1.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_0);
			List<SelectBean> oneCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel1);
			responseDTO.setOneCatBean(oneCatBean);
			// 根据一级分类id获取二级分类
			MerchandiseCatModel merchandiseCatModel2 =new MerchandiseCatModel();
			merchandiseCatModel2.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
			merchandiseCatModel2.setParentId(merchandiseInfo.getProductTypeId0());
			List<SelectBean> twoCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel2);
			responseDTO.setTwoCatBean(twoCatBean);
			List<SelectBean> brandBean = brandService.getSelectBean();
			responseDTO.setBrandBean(brandBean);
			List<SelectBean> countryBean = countryOriginService.getSelectBean();
			responseDTO.setCountryBean(countryBean);
			List<SelectBean> catBean = merchandiseCatService.getSelectBean(new HashMap<String, Object>());
			responseDTO.setCatBean(catBean);
			List<SelectBean> unitBean = unitInfoService.getSelectBean();
			responseDTO.setUnitBean(unitBean);
			List<SelectBean> packBean = packTypeService.getSelectBean();
			responseDTO.setPackBean(packBean);

			// 日期转换
			SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (merchandiseInfo.getCreateDate()!= null) {
				String createDate = sft.format(merchandiseInfo.getCreateDate());
				responseDTO.setCreateDate(createDate);
			}
			if (merchandiseInfo.getModifyDate()!= null) {
				String modifyDate = sft.format(merchandiseInfo.getModifyDate());
				responseDTO.setModifyDate(modifyDate);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}



	/**
	 * 获取分页数据
	 */
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/listMerchandise.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<MerchandiseInfoDTO> listMerchandise(MerchandiseInfoForm form) {
		try {
			MerchandiseInfoDTO dto=new MerchandiseInfoDTO();
			dto.setGoodsCode(form.getGoodsCode());
			dto.setName(form.getName());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setBarcode(form.getBarcode());
			dto.setFactoryNo(form.getFactoryNo());
			dto.setSource(form.getSource());
			dto.setBrandId(form.getBrandId());
			dto.setProductTypeId(form.getProductTypeId());
			dto.setIsRecord(form.getIsRecord());
			dto.setOutDepotFlag(form.getOutDepotFlag());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			

			User user = ShiroUtils.getUserByToken(form.getToken());

			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = merchandiseService.listMerchandise(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 导出商品信息
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportMerchandise.asyn")
	public void exportMerchandise(MerchandiseInfoForm form,HttpServletResponse response, HttpServletRequest request) throws Exception{
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			MerchandiseInfoDTO dto=new MerchandiseInfoDTO();
			dto.setGoodsCode(form.getGoodsCode());
			dto.setName(form.getName());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setBarcode(form.getBarcode());
			dto.setFactoryNo(form.getFactoryNo());
			dto.setSource(form.getSource());
			dto.setBrandId(form.getBrandId());
			dto.setProductTypeId(form.getProductTypeId());
			dto.setIsRecord(form.getIsRecord());
			dto.setOutDepotFlag(form.getOutDepotFlag());

			
			dto.setMerchantId(user.getMerchantId());
			String sheetName = "商品信息导出";
			List<MerchandiseInfoDTO> list = merchandiseService.exportList(dto);
			
			
			
			/** 标题  */
			String[] COLUMNS= {"商品货号","商品条码","标准条码","关联仓库","外仓统一码","状态","商品名称","计量单位","备案单价","所属商家","数据来源",
					"备案平台关区","毛重","净重","规格型号","箱规","托规","保质天数","原产国","原产地区","申报要素","长",
					"宽","高","包装方式","HS编码","企业商品编码","商品品牌","商品一级品类","商品二级品类","商品英文名称","商品成分","生产企业名称",
					"生产企业地址"};
			String[] KEYS= {"goodsNo","barcode","commbarcode","depotNames","outDepotFlagLabel","statusLabel","name", "unitName" ,"filingPrice","merchantName","sourceLabel",
					"customsAreaNames","grossWeight","netWeight","spec","boxToUnit","torrToUnit","shelfLifeDays","countryName","countyArea","declareFactor","length",
					"width","height","packType","hsCode","factoryNo","brandName","productTypeName0","productTypeName","englishGoodsName","component","manufacturerAddr"};
			
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
		
		
	}

	/**
	 * 新增
	 * 
	 * @param model
	 *            商品信息
	 * @param ids
	 *            仓库id集合        
	 *            
	 */
	@ApiOperation(value = "新增商品",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/saveMerchandise.asyn")
	public ResponseBean saveMerchandise(MerchandiseInfoAddForm form) {
		try {
			MerchandiseInfoModel model=new MerchandiseInfoModel();
			model.setGoodsNo(form.getGoodsNo());
			model.setBarcode(form.getBarcode());
			model.setName(form.getName());
			model.setUnit(form.getUnit());
			model.setCountyId(form.getCountyId());
			model.setPackType(form.getPackType());
			model.setNetWeight(form.getNetWeight());
			model.setGrossWeight(form.getGrossWeight());
			model.setShelfLifeDays(form.getShelfLifeDays());
			model.setVolume(form.getVolume());
			model.setLength(form.getLength());
			model.setWidth(form.getWidth());
			model.setHeight(form.getHeight());
			model.setFilingPrice(form.getFilingPrice());
			model.setManufacturerAddr(form.getManufacturerAddr());
			model.setHsCode(form.getHsCode());
			model.setDescribe(form.getDescribe());
			model.setSpec(form.getSpec());
			model.setManufacturer(form.getManufacturer());
			model.setBoxToUnit(form.getBoxToUnit());
			model.setTorrToUnit(form.getTorrToUnit());
			model.setFactoryNo(form.getFactoryNo());
			model.setCountyArea(form.getCountyArea());
			model.setDeclareFactor(form.getDeclareFactor());
			model.setComponent(form.getComponent());
			model.setUnitNameOne(form.getUnitNameOne());
			model.setUnitNameTwo(form.getUnitNameTwo());
			model.setCustomsGoodsRecordNo(form.getCustomsGoodsRecordNo());
			model.setBrandId(form.getBrandId());
			model.setProductTypeId0(form.getProductTypeId0());
			model.setProductTypeId(form.getProductTypeId());

			User user = ShiroUtils.getUserByToken(form.getToken());
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getName())
					.addObject(model.getGoodsNo()).addObject(model.getBarcode()).addObject(model.getUnit())
					.addObject(model.getCountyId()).addObject(model.getSpec()).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			
			// 设置创建人
			model.setCreater(user.getId());
			model.setCreateName(user.getName());
			 // 设置商家id
			model.setMerchantId(user.getMerchantId());
			//设置商家名称
			model.setMerchantName(user.getMerchantName());
			// 存储仓库信息
			model.setDepotIds(form.getDepotIds());
			
			
			boolean b = merchandiseService.saveMerchandise(user,model,form.getCustomsAreaIds());
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 删除
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id集合,多个用英文逗号隔开", required = true)
	})
	@PostMapping(value="/delMerchandise.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delMerchandise(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
				}
			List list = StrUtils.parseIds(ids);
			boolean b = merchandiseService.delMerchandise(list);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 录入修改
	 * @param model
	 * @param ids
	 * @param mins
	 * @param maxs
	 * @param groupIds
	 * @param groupNums
	 * @param groupPrices
	 * @return
	 */
	@ApiOperation(value = "录入修改")
	@PostMapping(value="/modifyMerchandise.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyMerchandise(MerchandiseInfoEditForm form) {
		try {
			if (StringUtils.isBlank(form.getSpec())) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"规格型号不能为空");//未知异常
			}
			
			User user = ShiroUtils.getUserByToken(form.getToken());
			MerchandiseInfoModel model=new MerchandiseInfoModel();
			model.setId(form.getId());
			model.setGoodsNo(form.getGoodsNo());			
			model.setBarcode(form.getBarcode());
			model.setName(form.getName());
			model.setUnit(form.getUnit());
			model.setEnglishGoodsName(form.getEnglishGoodsName());
			model.setCountyId(form.getCountyId());
			model.setPackType(form.getPackType());
			model.setNetWeight(form.getNetWeight());
			model.setGrossWeight(form.getGrossWeight());
			model.setShelfLifeDays(form.getShelfLifeDays());
			model.setVolume(form.getVolume());
			model.setLength(form.getLength());
			model.setWidth(form.getWidth());
			model.setHeight(form.getHeight());
			model.setFilingPrice(form.getFilingPrice());
			model.setManufacturerAddr(form.getManufacturerAddr());
			model.setHsCode(form.getHsCode());
			model.setDescribe(form.getDescribe());
			model.setSpec(form.getSpec());
			model.setManufacturer(form.getManufacturer());
			model.setBoxToUnit(form.getBoxToUnit());
			model.setTorrToUnit(form.getTorrToUnit());
			model.setFactoryNo(form.getFactoryNo());
			model.setCountyArea(form.getCountyArea());
			model.setDeclareFactor(form.getDeclareFactor());
			model.setComponent(form.getComponent());
			model.setUnitNameOne(form.getUnitNameOne());
			model.setUnitNameTwo(form.getUnitNameTwo());
			model.setCustomsGoodsRecordNo(form.getCustomsGoodsRecordNo());
			model.setBrandId(form.getBrandId());
			model.setProductTypeId0(form.getProductTypeId0());
			model.setProductTypeId(form.getProductTypeId());
			// 存储仓库信息
			model.setDepotIds(form.getDepotIds());

			// 校验id是否正确
			boolean isRight = StrUtils.validateId(model.getId());
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			// 修改人
			model.setModifier(user.getId());
			model.setUpdateName(user.getName());
			model.setMerchantName(user.getMerchantName());
			model.setMerchantId(user.getMerchantId());
			// 修改时间
			model.setModifyDate(TimeUtils.getNow());
			
			boolean b = merchandiseService.modifyMerchandise(user,model,form.getCustomsAreaIds());
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
		}
	}
	


	/**
	 * 导入商品
	 */
	@ApiOperation(value = "导入商品")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importMerchandise.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importMerchandise(String token,@RequestParam(value = "file", required = false) MultipartFile file,String linshi) {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			if ("1".equals(linshi)) {
				resultMap = merchandiseService.importMerchandiseLinshi(data, user);
			}else {
				resultMap = merchandiseService.importMerchandise(data, user);
			} 
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			Integer pass = (Integer)resultMap.get("pass"); 

			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取下拉列表
	 */

	@ApiOperation(value = "获取下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "depotId", value = "仓库id", required = true)
	})
	@PostMapping(value="/getSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getSelectBean(String token,Long depotId) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			result = merchandiseService.getSelectBean(user.getMerchantId(), depotId);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 根据ID获取商品详情
	 */
	@ApiOperation(value = "根据ID获取商品详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getMerchandiseDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseInfoDTO> getMerchandiseDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		MerchandiseInfoDTO model = new MerchandiseInfoDTO();
		try {
			model = merchandiseService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 获取商品列表
	 * 
	 * @param model
	 *            商品信息
	 */
	@ApiOperation(value = "获取商品列表")
	@PostMapping(value="/getList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseInfoModel> getList(MerchandiseInfoDetailForm form) {
		try {
			MerchandiseInfoModel model=new MerchandiseInfoModel();
			model.setGoodsNo(form.getGoodsNo());
			model.setCode(form.getCode());
			model.setProductId(form.getProductId());
			model.setSource(form.getSource());
			model.setMinStock(form.getMinStock());
			model.setUniques(form.getUniques());
			model.setName(form.getName());
			model.setMaxStock(form.getMaxStock());
			model.setId(form.getId());
			model.setGoodsCode(form.getGoodsCode());
			model.setFilingPrice(form.getFilingPrice());
			model.setFactoryNo(form.getFactoryNo());
			model.setPackType(form.getPackType());
			model.setWarningType(form.getWarningType());
			model.setMerchantName(form.getMerchantName());
			model.setMerchantId(form.getMerchantId());
			model.setIsRecord(form.getIsRecord());
			model.setIsGroup(form.getIsGroup());
			model.setIsSelf(form.getIsSelf());
			model.setBarcode(form.getBarcode());
			model.setStatus(form.getStatus());
			model.setCommbarcode(form.getCommbarcode());
			model.setEnglishGoodsName(form.getEnglishGoodsName());
			model.setProductName(form.getProductName());
			model.setBrandId(form.getBrandId());
			model.setBrandName(form.getBrandName());
			model.setCountyId(form.getCountyId());
			model.setCountyName(form.getCountyName());
			model.setUnit(form.getUnit());
			model.setUnitName(form.getUnitName());
			model.setProductTypeId(form.getProductTypeId());
			model.setProductTypeId0(form.getProductTypeId0());
			model.setProductTypeName(form.getProductTypeName());
			model.setProductTypeName0(form.getProductTypeName0());
			model.setGoodsClassifyName(form.getGoodsClassifyName());
			model.setShelfLifeDays(form.getShelfLifeDays());
			model.setGrossWeight(form.getGrossWeight());
			model.setNetWeight(form.getNetWeight());
			model.setHsCode(form.getHsCode());
			model.setLength(form.getLength());
			model.setWidth(form.getWidth());
			model.setHeight(form.getHeight());
			model.setVolume(form.getVolume());
			model.setManufacturer(form.getManufacturer());
			model.setManufacturerAddr(form.getManufacturerAddr());
			model.setColor(form.getColor());
			model.setSize(form.getSize());
			model.setComponent(form.getComponent());
			model.setDepotId(form.getDepotId());
			model.setIds(form.getIds());
			model.setMerchantIds(form.getMerchantIds());
			model.setSupplyMinPrice(form.getSupplyMinPrice());
			model.setCloudMerchantId(form.getCloudMerchantId());
			model.setSupplierId(form.getSupplierId());
			model.setOutDepotFlag(form.getOutDepotFlag());
			model.setTenantCode(form.getTenantCode());
			model.setVersion(form.getVersion());
			model.setSourceGoodsId(form.getSourceGoodsId());
			model.setBegin(form.getBegin());
			model.setPageSize(form.getPageSize());
			
			
			String unNeedIds=form.getUnNeedIds();
			
			User user = ShiroUtils.getUserByToken(form.getToken());
			List ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model.setMerchantId(user.getMerchantId());
			model = merchandiseService.getList(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 根据仓库类型获取商品列表
	 * @param model 商品信息
	 */
	@ApiOperation(value = "根据仓库类型获取商品列表")
	@PostMapping(value="/getListForType.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseInfoModel> getListForType(MerchandiseInfoDetailForm form) {
		try {
			MerchandiseInfoModel model=new MerchandiseInfoModel();
			model.setGoodsNo(form.getGoodsNo());
			model.setCode(form.getCode());
			model.setProductId(form.getProductId());
			model.setSource(form.getSource());
			model.setMinStock(form.getMinStock());
			model.setUniques(form.getUniques());
			model.setName(form.getName());
			model.setMaxStock(form.getMaxStock());
			model.setId(form.getId());
			model.setGoodsCode(form.getGoodsCode());
			model.setFilingPrice(form.getFilingPrice());
			model.setFactoryNo(form.getFactoryNo());
			model.setPackType(form.getPackType());
			model.setWarningType(form.getWarningType());
			model.setMerchantName(form.getMerchantName());
			model.setMerchantId(form.getMerchantId());
			model.setIsRecord(form.getIsRecord());
			model.setIsGroup(form.getIsGroup());
			model.setIsSelf(form.getIsSelf());
			model.setBarcode(form.getBarcode());
			model.setStatus(form.getStatus());
			model.setCommbarcode(form.getCommbarcode());
			model.setEnglishGoodsName(form.getEnglishGoodsName());
			model.setProductName(form.getProductName());
			model.setBrandId(form.getBrandId());
			model.setBrandName(form.getBrandName());
			model.setCountyId(form.getCountyId());
			model.setCountyName(form.getCountyName());
			model.setUnit(form.getUnit());
			model.setUnitName(form.getUnitName());
			model.setProductTypeId(form.getProductTypeId());
			model.setProductTypeId0(form.getProductTypeId0());
			model.setProductTypeName(form.getProductTypeName());
			model.setProductTypeName0(form.getProductTypeName0());
			model.setGoodsClassifyName(form.getGoodsClassifyName());
			model.setShelfLifeDays(form.getShelfLifeDays());
			model.setGrossWeight(form.getGrossWeight());
			model.setNetWeight(form.getNetWeight());
			model.setHsCode(form.getHsCode());
			model.setLength(form.getLength());
			model.setWidth(form.getWidth());
			model.setHeight(form.getHeight());
			model.setVolume(form.getVolume());
			model.setManufacturer(form.getManufacturer());
			model.setManufacturerAddr(form.getManufacturerAddr());
			model.setColor(form.getColor());
			model.setSize(form.getSize());
			model.setComponent(form.getComponent());
			model.setDepotId(form.getDepotId());
			model.setIds(form.getIds());
			model.setMerchantIds(form.getMerchantIds());
			model.setSupplyMinPrice(form.getSupplyMinPrice());
			model.setCloudMerchantId(form.getCloudMerchantId());
			model.setSupplierId(form.getSupplierId());
			model.setOutDepotFlag(form.getOutDepotFlag());
			model.setTenantCode(form.getTenantCode());
			model.setVersion(form.getVersion());
			model.setSourceGoodsId(form.getSourceGoodsId());
			model.setBegin(form.getBegin());
			model.setPageSize(form.getPageSize());
			
			String unNeedIds=form.getUnNeedIds();
			
			User user = ShiroUtils.getUserByToken(form.getToken());
			List ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model.setMerchantId(user.getMerchantId());
			
			model = merchandiseService.getListForDepotType(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		}catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 新增商品获取备案商品列表
	 * 
	 * @param model
	 *            商品信息
	 */
	@ApiOperation(value = "新增商品获取备案商品列表")
	@PostMapping(value="/getListByMerchant.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseInfoModel> getListByMerchant(MerchandiseInfoDetailForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			MerchandiseInfoModel model=new MerchandiseInfoModel();
			model.setGoodsNo(form.getGoodsNo());
			model.setCode(form.getCode());
			model.setProductId(form.getProductId());
			model.setSource(form.getSource());
			model.setMinStock(form.getMinStock());
			model.setUniques(form.getUniques());
			model.setName(form.getName());
			model.setMaxStock(form.getMaxStock());
			model.setId(form.getId());
			model.setGoodsCode(form.getGoodsCode());
			model.setFilingPrice(form.getFilingPrice());
			model.setFactoryNo(form.getFactoryNo());
			model.setPackType(form.getPackType());
			model.setWarningType(form.getWarningType());
			model.setMerchantName(form.getMerchantName());
			model.setMerchantId(form.getMerchantId());
			model.setIsRecord(form.getIsRecord());
			model.setIsGroup(form.getIsGroup());
			model.setIsSelf(form.getIsSelf());
			model.setBarcode(form.getBarcode());
			model.setStatus(form.getStatus());
			model.setCommbarcode(form.getCommbarcode());
			model.setEnglishGoodsName(form.getEnglishGoodsName());
			model.setProductName(form.getProductName());
			model.setBrandId(form.getBrandId());
			model.setBrandName(form.getBrandName());
			model.setCountyId(form.getCountyId());
			model.setCountyName(form.getCountyName());
			model.setUnit(form.getUnit());
			model.setUnitName(form.getUnitName());
			model.setProductTypeId(form.getProductTypeId());
			model.setProductTypeId0(form.getProductTypeId0());
			model.setProductTypeName(form.getProductTypeName());
			model.setProductTypeName0(form.getProductTypeName0());
			model.setGoodsClassifyName(form.getGoodsClassifyName());
			model.setShelfLifeDays(form.getShelfLifeDays());
			model.setGrossWeight(form.getGrossWeight());
			model.setNetWeight(form.getNetWeight());
			model.setHsCode(form.getHsCode());
			model.setLength(form.getLength());
			model.setWidth(form.getWidth());
			model.setHeight(form.getHeight());
			model.setVolume(form.getVolume());
			model.setManufacturer(form.getManufacturer());
			model.setManufacturerAddr(form.getManufacturerAddr());
			model.setColor(form.getColor());
			model.setSize(form.getSize());
			model.setComponent(form.getComponent());
			model.setDepotId(form.getDepotId());
			model.setIds(form.getIds());
			model.setMerchantIds(form.getMerchantIds());
			model.setSupplyMinPrice(form.getSupplyMinPrice());
			model.setCloudMerchantId(form.getCloudMerchantId());
			model.setSupplierId(form.getSupplierId());
			model.setOutDepotFlag(form.getOutDepotFlag());
			model.setTenantCode(form.getTenantCode());
			model.setVersion(form.getVersion());
			model.setSourceGoodsId(form.getSourceGoodsId());
			model.setBegin(form.getBegin());
			model.setPageSize(form.getPageSize());
			
			String unNeedIds=form.getUnNeedIds();
			List ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model.setMerchantId(user.getMerchantId());
			model = merchandiseService.getListByMerchant(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 根据商品id获取商品信息
	 * 
	 * @param ids
	 *            商品信息
	 */
	@ApiOperation(value = "根据商品id获取商品信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id的集合,多个用英文逗号隔开", required = true),
	})
	@PostMapping(value="/getListByIds.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<MerchandiseInfoDTO>> getListByIds(String ids) {
		List<MerchandiseInfoDTO> result = new ArrayList<MerchandiseInfoDTO>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
			}
			List list = StrUtils.parseIds(ids);
			result = merchandiseService.getListByIds(list);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取商品列表
	 * 
	 * @param model
	 *            商品信息
	 */
	@ApiOperation(value = "获取商品列表")
	@PostMapping(value="/getListByMerchantId.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseInfoModel> getListByMerchantId(MerchandiseInfoDetailForm form) {
		try {
			MerchandiseInfoModel model=new MerchandiseInfoModel();
			model.setGoodsNo(form.getGoodsNo());
			model.setCode(form.getCode());
			model.setProductId(form.getProductId());
			model.setSource(form.getSource());
			model.setMinStock(form.getMinStock());
			model.setUniques(form.getUniques());
			model.setName(form.getName());
			model.setMaxStock(form.getMaxStock());
			model.setId(form.getId());
			model.setGoodsCode(form.getGoodsCode());
			model.setFilingPrice(form.getFilingPrice());
			model.setFactoryNo(form.getFactoryNo());
			model.setPackType(form.getPackType());
			model.setWarningType(form.getWarningType());
			model.setMerchantName(form.getMerchantName());
			model.setMerchantId(form.getMerchantId());
			model.setIsRecord(form.getIsRecord());
			model.setIsGroup(form.getIsGroup());
			model.setIsSelf(form.getIsSelf());
			model.setBarcode(form.getBarcode());
			model.setStatus(form.getStatus());
			model.setCommbarcode(form.getCommbarcode());
			model.setEnglishGoodsName(form.getEnglishGoodsName());
			model.setProductName(form.getProductName());
			model.setBrandId(form.getBrandId());
			model.setBrandName(form.getBrandName());
			model.setCountyId(form.getCountyId());
			model.setCountyName(form.getCountyName());
			model.setUnit(form.getUnit());
			model.setUnitName(form.getUnitName());
			model.setProductTypeId(form.getProductTypeId());
			model.setProductTypeId0(form.getProductTypeId0());
			model.setProductTypeName(form.getProductTypeName());
			model.setProductTypeName0(form.getProductTypeName0());
			model.setGoodsClassifyName(form.getGoodsClassifyName());
			model.setShelfLifeDays(form.getShelfLifeDays());
			model.setGrossWeight(form.getGrossWeight());
			model.setNetWeight(form.getNetWeight());
			model.setHsCode(form.getHsCode());
			model.setLength(form.getLength());
			model.setWidth(form.getWidth());
			model.setHeight(form.getHeight());
			model.setVolume(form.getVolume());
			model.setManufacturer(form.getManufacturer());
			model.setManufacturerAddr(form.getManufacturerAddr());
			model.setColor(form.getColor());
			model.setSize(form.getSize());
			model.setComponent(form.getComponent());
			model.setDepotId(form.getDepotId());
			model.setIds(form.getIds());
			model.setMerchantIds(form.getMerchantIds());
			model.setSupplyMinPrice(form.getSupplyMinPrice());
			model.setCloudMerchantId(form.getCloudMerchantId());
			model.setSupplierId(form.getSupplierId());
			model.setOutDepotFlag(form.getOutDepotFlag());
			model.setTenantCode(form.getTenantCode());
			model.setVersion(form.getVersion());
			model.setSourceGoodsId(form.getSourceGoodsId());
			model.setBegin(form.getBegin());
			model.setPageSize(form.getPageSize());			
			String unNeedIds=form.getUnNeedIds();
			
			List ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model = merchandiseService.getListByMerchantId(model,form.getOutDepotId());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		}catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 公共的商品弹窗
	 * @param form 商品信息
	 */
	@ApiOperation(value = "公共的商品弹窗")
	@PostMapping(value="/getPopupList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseInfoDTO> getPopupList(MerchandiseInfoDetailForm form) {
		try {
			MerchandiseInfoDTO dto = new MerchandiseInfoDTO();
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());

			if (user.getMerchantId() != null) {
				dto.setMerchantId(user.getMerchantId());
			}

			if (StringUtils.isNotBlank(form.getUnNeedIds())) {
				List<Long> ids = StrUtils.parseIds(form.getUnNeedIds());
				dto.setIds(ids);
			}

			dto = merchandiseService.getOrderPopupList(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
     *公共方法获取商品分类下拉列表
     *level 分类级别 0:一级类目,1:二级类目
	 */
	@ApiOperation(value = "公共方法获取商品分类下拉列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "level", value = "分类级别 0:一级类目,1:二级类目", required = true)
	})
	@PostMapping(value="/getMerchandiseCatList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean<List<SelectBean>> getMerchandiseCatList(String level) {
		List<SelectBean> catList =null;
		try {
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("level", level);
			catList = merchandiseCatService.getSelectBean(paramMap);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,catList);//成功
		}catch(Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	/**
	 * 上传图片
	 * @param file
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "上传图片")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/uploadFile.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean uploadFile(String token,@RequestParam(value = "file", required = false) MultipartFile file, Long id, String type, HttpSession session) {
		String path = "";
		try{
            if(file==null){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

			String fileName = file.getOriginalFilename();
            byte[] fileBytes = file.getBytes();
            Long fileSize = file.getSize();
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            User user = ShiroUtils.getUserByToken(token);
			path = merchandiseService.uploadFile(fileName,fileBytes,fileSize,ext,user.getId(),user.getName(),id,type);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,path);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	/**
	 * 结算价格-选择商品
	 * 获取商家下所有商品（条码相同只取一条记录）
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "结算价格-选择商品")
	@PostMapping(value="/getListForSettlment.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseInfoModel> getListForSettlmentByPage(MerchandiseInfoDetailForm form) {
		try {
			
			MerchandiseInfoModel model=new MerchandiseInfoModel();
			model.setGoodsNo(form.getGoodsNo());
			model.setCode(form.getCode());
			model.setProductId(form.getProductId());
			model.setSource(form.getSource());
			model.setMinStock(form.getMinStock());
			model.setUniques(form.getUniques());
			model.setName(form.getName());
			model.setMaxStock(form.getMaxStock());
			model.setId(form.getId());
			model.setGoodsCode(form.getGoodsCode());
			model.setFilingPrice(form.getFilingPrice());
			model.setFactoryNo(form.getFactoryNo());
			model.setPackType(form.getPackType());
			model.setWarningType(form.getWarningType());
			model.setMerchantName(form.getMerchantName());
			model.setMerchantId(form.getMerchantId());
			model.setIsRecord(form.getIsRecord());
			model.setIsGroup(form.getIsGroup());
			model.setIsSelf(form.getIsSelf());
			model.setBarcode(form.getBarcode());
			model.setStatus(form.getStatus());
			model.setCommbarcode(form.getCommbarcode());
			model.setEnglishGoodsName(form.getEnglishGoodsName());
			model.setProductName(form.getProductName());
			model.setBrandId(form.getBrandId());
			model.setBrandName(form.getBrandName());
			model.setCountyId(form.getCountyId());
			model.setCountyName(form.getCountyName());
			model.setUnit(form.getUnit());
			model.setUnitName(form.getUnitName());
			model.setProductTypeId(form.getProductTypeId());
			model.setProductTypeId0(form.getProductTypeId0());
			model.setProductTypeName(form.getProductTypeName());
			model.setProductTypeName0(form.getProductTypeName0());
			model.setGoodsClassifyName(form.getGoodsClassifyName());
			model.setShelfLifeDays(form.getShelfLifeDays());
			model.setGrossWeight(form.getGrossWeight());
			model.setNetWeight(form.getNetWeight());
			model.setHsCode(form.getHsCode());
			model.setLength(form.getLength());
			model.setWidth(form.getWidth());
			model.setHeight(form.getHeight());
			model.setVolume(form.getVolume());
			model.setManufacturer(form.getManufacturer());
			model.setManufacturerAddr(form.getManufacturerAddr());
			model.setColor(form.getColor());
			model.setSize(form.getSize());
			model.setComponent(form.getComponent());
			model.setDepotId(form.getDepotId());
			model.setIds(form.getIds());
			model.setMerchantIds(form.getMerchantIds());
			model.setSupplyMinPrice(form.getSupplyMinPrice());
			model.setCloudMerchantId(form.getCloudMerchantId());
			model.setSupplierId(form.getSupplierId());
			model.setOutDepotFlag(form.getOutDepotFlag());
			model.setTenantCode(form.getTenantCode());
			model.setVersion(form.getVersion());
			model.setSourceGoodsId(form.getSourceGoodsId());
			model.setBegin(form.getBegin());
			model.setPageSize(form.getPageSize());			
			String unNeedIds=form.getUnNeedIds();
			
			User user = ShiroUtils.getUserByToken(form.getToken());
			model.setMerchantId(user.getMerchantId());
			List<Long> ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model = merchandiseService.getListForSettlmentByPage(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		} catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 禁用/启用
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "禁用/启用")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "id", required = true),
		@ApiImplicitParam(name = "status", value = "状态", required = true)
		
	})
	@PostMapping(value="/auditMerchandies.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean auditMerchandies(String token,Long id,String status) {
		try {
			MerchandiseInfoModel model=new MerchandiseInfoModel();
			model.setId(id);
			model.setStatus(status);
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			if (model.getStatus().equals(DERP_SYS.MERCHANDISEINFO_STATUS_0)) {
				MerchandiseInfoDTO isExist = merchandiseService.searchDetail(model.getId());
				if (isExist.getOutDepotFlag().equals(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0)) {
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"商品的外仓统一码为是,不能禁用");//未知异常
				}
			}
			User user = ShiroUtils.getUserByToken(token);
			
			boolean b = merchandiseService.auditMerchandies(model,user);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
			
		}  catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * @Author Chen Yiluan
	 * @Description 根据ids、单据类型、仓库id获取商品信息
	 * @Param
	 * @return
	 */
	@ApiOperation(value = "根据ids、单据类型、仓库id获取商品信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "id的集合多个用英文逗号隔开", required = true),
		@ApiImplicitParam(name = "type", value = "类型", required = true),
		@ApiImplicitParam(name = "type", value = "类型", required = true),
		@ApiImplicitParam(name = "depotId", value = "仓库id", required = true)
		
	})
	@PostMapping(value="/getListByIdsAndTypeAndInOutDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean getListByIdsAndTypeAndInOutDepot(String token,String ids, String type, Long depotId) {
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			if (StringUtils.isBlank(type) || depotId == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			List idList = StrUtils.parseIds(ids);
			//查询相关商品信息
			result = merchandiseService.getListByIdsAndOutInDepot(idList, type, depotId, user.getMerchantId());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	
	/**
	 * 不同单据公共弹窗
	 * @param form 商品信息
	 */
	@ApiOperation(value = "不同单据公共弹窗")
	@PostMapping(value="/getOrderPopupList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseInfoDTO> getOrderPopupList(MerchandiseInfoDetailForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			MerchandiseInfoDTO dto = new MerchandiseInfoDTO();
			dto.setGoodsNo(form.getGoodsNo());
			dto.setCode(form.getCode());
			dto.setProductId(form.getProductId());
			dto.setSource(form.getSource());
			dto.setMinStock(form.getMinStock());
			dto.setUniques(form.getUniques());
			dto.setName(form.getName());
			dto.setMaxStock(form.getMaxStock());
			dto.setId(form.getId());
			dto.setGoodsCode(form.getGoodsCode());
			dto.setFilingPrice(form.getFilingPrice());
			dto.setFactoryNo(form.getFactoryNo());
			dto.setPackType(form.getPackType());
			dto.setWarningType(form.getWarningType());
			dto.setMerchantName(form.getMerchantName());
			dto.setMerchantId(form.getMerchantId());
			dto.setIsRecord(form.getIsRecord());
			dto.setIsGroup(form.getIsGroup());
			dto.setIsSelf(form.getIsSelf());
			dto.setBarcode(form.getBarcode());
			dto.setStatus(form.getStatus());
			dto.setCommbarcode(form.getCommbarcode());
			dto.setEnglishGoodsName(form.getEnglishGoodsName());
			dto.setProductName(form.getProductName());
			dto.setBrandId(form.getBrandId());
			dto.setBrandName(form.getBrandName());
			dto.setCountyId(form.getCountyId());
			dto.setCountyName(form.getCountyName());
			dto.setUnit(form.getUnit());
			dto.setUnitName(form.getUnitName());
			dto.setProductTypeId(form.getProductTypeId());
			dto.setProductTypeId0(form.getProductTypeId0());
			dto.setProductTypeName(form.getProductTypeName());
			dto.setProductTypeName0(form.getProductTypeName0());
			dto.setGoodsClassifyName(form.getGoodsClassifyName());
			dto.setShelfLifeDays(form.getShelfLifeDays());
			dto.setGrossWeight(form.getGrossWeight());
			dto.setNetWeight(form.getNetWeight());
			dto.setHsCode(form.getHsCode());
			dto.setLength(form.getLength());
			dto.setWidth(form.getWidth());
			dto.setHeight(form.getHeight());
			dto.setVolume(form.getVolume());
			dto.setManufacturer(form.getManufacturer());
			dto.setManufacturerAddr(form.getManufacturerAddr());
			dto.setColor(form.getColor());
			dto.setSize(form.getSize());
			dto.setComponent(form.getComponent());
			dto.setDepotId(form.getDepotId());
			dto.setIds(form.getIds());
			dto.setMerchantIds(form.getMerchantIds());
			dto.setSupplyMinPrice(form.getSupplyMinPrice());
			dto.setCloudMerchantId(form.getCloudMerchantId());
			dto.setSupplierId(form.getSupplierId());
			dto.setOutDepotFlag(form.getOutDepotFlag());
			dto.setTenantCode(form.getTenantCode());
			dto.setVersion(form.getVersion());
			dto.setSourceGoodsId(form.getSourceGoodsId());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			dto.setNeedIds(form.getNeedIds());

			
			
			String unNeedIds=form.getUnNeedIds();
			String productRestriction=form.getProductRestriction();
			
			if (!StringUtils.isEmpty(unNeedIds)) {
				List<Long> ids = StrUtils.parseIds(unNeedIds);
				dto.setIds(ids);
			}
			//非管理员
			if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType()) && user.getMerchantId() != null) {
				dto.setMerchantId(user.getMerchantId());
			}

			dto = merchandiseService.getOrderPopupDtoList(dto, productRestriction,user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


	@ApiOperation(value = "不同单据切仓商品修改")
	@PostMapping(value="/getOrderUpdateMerchandiseInfo.asyn", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<UpdateDepotOrderDTO> getOrderUpdateMerchandiseInfo(@RequestBody UpdateDepotOrderForm form) {
		try {
			boolean isNull = new EmptyCheckUtils()
					.addObject(form.getOrderType())
					.addObject(form.getDepotId())
					.addObject(form.getItemList())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(form.getToken());
			List<UpdateDepotOrderDTO> depotOrderDTOS = merchandiseService.getOrderPopupDtoList(form, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, depotOrderDTOS);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	@ApiOperation(value = "根据ids、单据类型、仓库id获取商品信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id的集合多个用英文逗号隔开", required = true),
			@ApiImplicitParam(name = "type", value = "类型 3-销售退 4-调拨", required = true),
			@ApiImplicitParam(name = "depotId", value = "仓库id", required = true)

	})
	@PostMapping(value="/getListByIdsAndTypeAndDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getListByIdsAndTypeAndDepot(String token,String ids, String type, Long depotId, String unNeedIds) {
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			if (StringUtils.isBlank(type) || depotId == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			List idList = StrUtils.parseIds(ids);
			//查询相关商品信息
			result = merchandiseService.getListByIdsAndDepot(idList, type, depotId, user.getMerchantId(), unNeedIds, user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	@ApiOperation(value = "不同单据切仓商品修改，返回商品集合")
	@PostMapping(value="/getOrderUpdateMerchandiseList.asyn", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<UpdateDepotOrderDTO> getOrderUpdateMerchandiseList(@RequestBody UpdateDepotOrderForm form) {
		try {
			boolean isNull = new EmptyCheckUtils()
					.addObject(form.getOrderType())
					.addObject(form.getDepotId())
					.addObject(form.getItemList())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(form.getToken());
			List<UpdateDepotOrderDTO> depotOrderDTOS = merchandiseService.getOrderPopupList(form, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, depotOrderDTOS);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


}
