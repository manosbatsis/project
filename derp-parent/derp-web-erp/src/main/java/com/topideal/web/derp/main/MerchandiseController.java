package com.topideal.web.derp.main;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.UploadResponse;
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
import com.topideal.service.base.BrandService;
import com.topideal.service.base.CountryOriginService;
import com.topideal.service.base.CustomsAreaConfigService;
import com.topideal.service.base.PackTypeService;
import com.topideal.service.base.UnitInfoService;
import com.topideal.service.main.MerchandiseCatService;
import com.topideal.service.main.MerchandiseService;
import com.topideal.shiro.ShiroUtils;

import net.sf.json.JSONObject;

/**
 * 商品管理 控制层
 */
@RequestMapping("/merchandise")
@Controller
public class MerchandiseController {
	

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

	@Autowired
	private CustomsAreaConfigService customsAreaConfigService;
	

	/**
	 * 导入页面
	 */
	@RequestMapping("/toImportImagePage.html")
	public String toImportImagePage() {
		return "/derp/main/merchandise-import-image";
	}

	/**
	 * 导入商品图片
	 */
	@RequestMapping("/importMerchandiseImage.asyn")
	@ResponseBody
	public ViewResponseBean importMerchandiseImage(@RequestParam(value = "file", required = false) MultipartFile file) {
		
		try {
			long time = TimeUtils.getNow().getTime();
			//String unzipFilePath="C:/Users/杨创/Desktop/新建文件夹 (4)/jieya/"+time+"/";
			String unzipFilePath = ApolloUtilDB.systemBasepath+"/goodsImage/"+time+"/";//商品图片压缩包文件存放目录
						
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
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
			
			User user = ShiroUtils.getUser();
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
			
			
			
			Map resultMap = merchandiseService.uploadImportImage(barcodeMap,user.getMerchantId());
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			
			return ResponseFactory.success(uploadResponse);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
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
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		List<SelectBean> brandBean = brandService.getSelectBean();
		model.addAttribute("brandBean", brandBean);
		// 获取二级类目
		MerchandiseCatModel merchandiseCatModel =new MerchandiseCatModel();
		merchandiseCatModel.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
		List<SelectBean> productTypeBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel);
		model.addAttribute("productTypeBean", productTypeBean);
		return "/derp/main/merchandise-list";
	}

	/**
	 * 访问新增页面
	 */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, Long id) throws Exception {
		List<SelectBean> brandBean = brandService.getSelectBean();
		model.addAttribute("brandBean", brandBean);
		List<SelectBean> countryBean = countryOriginService.getSelectBean();
		model.addAttribute("countryBean", countryBean);
		// 获取一级分类
		MerchandiseCatModel merchandiseCatModel =new MerchandiseCatModel();
		merchandiseCatModel.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_0);
		List<SelectBean> catBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel);
		model.addAttribute("catBean", catBean);
		List<SelectBean> unitBean = unitInfoService.getSelectBean();
		model.addAttribute("unitBean", unitBean);
		List<SelectBean> packBean = packTypeService.getSelectBean();
		model.addAttribute("packBean", packBean);

		return "/derp/main/merchandise-add";
	}

	/**
	 * 根据一级分类获取二级分类
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getTwoLevel.asyn")
	@ResponseBody
	public ViewResponseBean getTwoLevel(Long id) throws Exception {

		// 获取一级分类
		
		try {
			MerchandiseCatModel merchandiseCatModel =new MerchandiseCatModel();
			merchandiseCatModel.setParentId(id);
			merchandiseCatModel.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
			List<SelectBean> catBeanList = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel);
			return ResponseFactory.success(catBeanList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		

	}
	/**
	 * 访问详情页面
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		MerchandiseInfoDTO merchandiseInfo = merchandiseService.searchDetail(id);
		model.addAttribute("detail", merchandiseInfo);
		BrandModel brandModel=new BrandModel();
		if (merchandiseInfo.getBrandId()!=null) {
			brandModel = brandService.getDetails(merchandiseInfo.getBrandId());
		}
		if (brandModel==null)brandModel=new BrandModel();
		model.addAttribute("brandName", brandModel.getName());
		CountryOriginModel countryOriginModel=new CountryOriginModel();
		if (merchandiseInfo.getCountyId()!=null) {
			countryOriginModel = countryOriginService.getDetails(merchandiseInfo.getCountyId());
		}
		if (countryOriginModel==null)countryOriginModel=new CountryOriginModel();
		model.addAttribute("countryName", countryOriginModel.getName());
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
		model.addAttribute("unitName", unitInfoModel.getName());
		MerchandiseCustomsRelModel relQuery=new MerchandiseCustomsRelModel();
		relQuery.setGoodsId(id);
		List<MerchandiseCustomsRelModel> relList = customsAreaConfigService.getmerchandiseCustomsRelList(relQuery);
		String customsAreaIdStr="";
		for (MerchandiseCustomsRelModel relModel : relList) {
			customsAreaIdStr=customsAreaIdStr+relModel.getCustomsAreaName()+",";
		}
		model.addAttribute("customsAreaIdStr", customsAreaIdStr);
		// 日期转换
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (merchandiseInfo.getCreateDate()!= null) {
			String createDate = sft.format(merchandiseInfo.getCreateDate());
			model.addAttribute("createDate", createDate);
		}
		if (merchandiseInfo.getModifyDate()!= null) {
			String modifyDate = sft.format(merchandiseInfo.getModifyDate());
			model.addAttribute("modifyDate", modifyDate);
		}
		return "/derp/main/merchandise-details";
	}

	/**
	 * 访问编辑页面
	 */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id) throws Exception {
		MerchandiseInfoDTO merchandiseInfo = merchandiseService.searchDetail(id);
		model.addAttribute("detail", merchandiseInfo);
		
		//获取商家关区关系表数据
		MerchandiseCustomsRelModel relModel=new MerchandiseCustomsRelModel();
		relModel.setGoodsId(merchandiseInfo.getId());;
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
		model.addAttribute("customsAreaIdStr", customsAreaIdStr);
		
		// 获取一级分类
		MerchandiseCatModel merchandiseCatModel1 =new MerchandiseCatModel();
		merchandiseCatModel1.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_0);
		List<SelectBean> oneCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel1);
		model.addAttribute("oneCatBean", oneCatBean);
		// 根据一级分类id获取二级分类
		MerchandiseCatModel merchandiseCatModel2 =new MerchandiseCatModel();
		merchandiseCatModel2.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
		merchandiseCatModel2.setParentId(merchandiseInfo.getProductTypeId0());
		List<SelectBean> twoCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel2);
		model.addAttribute("twoCatBean", twoCatBean);		
				
		List<SelectBean> brandBean = brandService.getSelectBean();
		model.addAttribute("brandBean", brandBean);
		List<SelectBean> countryBean = countryOriginService.getSelectBean();
		model.addAttribute("countryBean", countryBean);
		List<SelectBean> catBean = merchandiseCatService.getSelectBean(new HashMap<String, Object>());
		model.addAttribute("catBean", catBean);
		List<SelectBean> unitBean = unitInfoService.getSelectBean();
		model.addAttribute("unitBean", unitBean);
		List<SelectBean> packBean = packTypeService.getSelectBean();
		model.addAttribute("packBean", packBean);

		// 日期转换
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (merchandiseInfo.getCreateDate()!= null) {
			String createDate = sft.format(merchandiseInfo.getCreateDate());
			model.addAttribute("createDate", createDate);
		}
		if (merchandiseInfo.getModifyDate()!= null) {
			String modifyDate = sft.format(merchandiseInfo.getModifyDate());
			model.addAttribute("modifyDate", modifyDate);
		}
		return "/derp/main/merchandise-edit";
	}

	/**
	 * 访问主数据编辑页面
	 */
/*	@RequestMapping("/toMainEditPage.html")
	public String toMainEditPage(Model model, Long id) throws Exception {
		MerchandiseInfoDTO merchandiseInfo = merchandiseService.searchDetail(id);
		model.addAttribute("detail", merchandiseInfo);
		ProductInfoDTO productInfoDTO=new ProductInfoDTO();
		if (merchandiseInfo.getProductId()!=null) {
			productInfoDTO = productInfoService.searchDetail(merchandiseInfo.getProductId());
		}
		model.addAttribute("productInfo", productInfoDTO);
		MerchandiseInfoModel model1 = merchandiseService.searchMerchandise(id);
		model.addAttribute("merchandiseImage", model1);
		// 获取一级分类
		MerchandiseCatModel merchandiseCatModel1 =new MerchandiseCatModel();
		merchandiseCatModel1.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_0);
		List<SelectBean> oneCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel1);
		model.addAttribute("oneCatBean", oneCatBean);
		// 根据一级分类id获取二级分类
		MerchandiseCatModel merchandiseCatModel2 =new MerchandiseCatModel();
		merchandiseCatModel2.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
		merchandiseCatModel2.setParentId(merchandiseInfo.getProductTypeId0());
		List<SelectBean> twoCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel2);
		model.addAttribute("twoCatBean", twoCatBean);
		// 根据商品id查询出组合单品列表
		List<MerchandiseGroupRelModel> groupList = merchandiseGroupRelService.list(id);
		model.addAttribute("groupList", groupList);
		if(groupList!=null && groupList.size()>0){
			model.addAttribute("groupListSize", groupList.size());
		}else{
			model.addAttribute("groupListSize", 0);
		}
		model.addAttribute("groupList", groupList);
		// 日期转换
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (merchandiseInfo.getCreateDate()!= null) {
			String createDate = sft.format(merchandiseInfo.getCreateDate());
			model.addAttribute("createDate", createDate);
		}
		if (merchandiseInfo.getModifyDate()!= null) {
			String modifyDate = sft.format(merchandiseInfo.getModifyDate());
			model.addAttribute("modifyDate", modifyDate);
		}
		return "/derp/main/merchandise-main-edit";
	}
*/
	/**
	 * 获取分页数据
	 */
	@RequestMapping("/listMerchandise.asyn")
	@ResponseBody
	private ViewResponseBean listMerchandise(MerchandiseInfoDTO dto) {
		try {
			User user = ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = merchandiseService.listMerchandise(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 导出商品信息
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/exportMerchandise.asyn")
	public void exportMerchandise(HttpServletResponse response, HttpServletRequest request, MerchandiseInfoDTO dto) throws Exception{
		User user= ShiroUtils.getUser();
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		String sheetName = "商品信息导出";
		List<MerchandiseInfoDTO> list = merchandiseService.exportList(dto);
		/** 标题  */
		String[] COLUMNS= {"商品货号","商品条码","标准条码","外仓统一码","状态","商品名称","计量单位","备案单价","所属商家","数据来源",
				"备案平台关区","毛重","净重","规格型号","箱规","托规","保质天数","原产国","原产地区","申报要素","长",
				"宽","高","包装方式","HS编码","企业商品编码","商品品牌","商品一级品类","商品二级品类","商品英文名称","商品成分","生产企业名称",
				"生产企业地址"};
		String[] KEYS= {"goodsNo","barcode","commbarcode","outDepotFlagLabel","statusLabel","name", "unitName" ,"filingPrice","merchantName","sourceLabel",
				"customsAreaNames","grossWeight","netWeight","spec","boxToUnit","torrToUnit","shelfLifeDays","countryName","countyArea","declareFactor","length",
				"width","height","packType","hsCode","factoryNo","brandName","productTypeName0","productTypeName","englishGoodsName","component","manufacturerAddr"};
		
		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 * 商品信息       
	 *            
	 */
	@RequestMapping("/saveMerchandise.asyn")
	@ResponseBody
	public ViewResponseBean saveMerchandise(MerchandiseInfoModel model,String customsAreaId ) {
		try {
			User user = ShiroUtils.getUser();
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getName())
					.addObject(model.getGoodsNo()).addObject(model.getBarcode()).addObject(model.getUnit())
					.addObject(model.getCountyId()).empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}

			// 设置创建人
			model.setCreater(user.getId());
			model.setCreateName(user.getName());
			 // 设置商家id
			model.setMerchantId(user.getMerchantId());
			//设置商家名称
			model.setMerchantName(user.getMerchantName());
			// 存储商品信息
			boolean b = merchandiseService.saveMerchandise(user,model,customsAreaId);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delMerchandise.asyn")
	@ResponseBody
	public ViewResponseBean delMerchandise(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			boolean b = merchandiseService.delMerchandise(list);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
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
	@RequestMapping("/modifyMerchandise.asyn")
	@ResponseBody
	public ViewResponseBean modifyMerchandise(MerchandiseInfoModel model,String customsAreaId) {
		try {
			User user = ShiroUtils.getUser();
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(model.getId());
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}

			// 修改人
			model.setModifier(user.getId());
			model.setUpdateName(user.getName());
			model.setMerchantName(user.getMerchantName());
			model.setMerchantId(user.getMerchantId());
			// 修改时间
			model.setModifyDate(TimeUtils.getNow());
			

			boolean b = merchandiseService.modifyMerchandise(user,model,customsAreaId);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 主数据修改
	 * @param model
	 * @param groupIds
	 * @param groupNums
	 * @param groupPrices
	 * @return
	 */
	/*@RequestMapping("/modifyMerchandiseMain.asyn")
	@ResponseBody
	public ViewResponseBean modifyMerchandiseMain(MerchandiseInfoModel model, String groupIds, String groupNums, String groupPrices) {
		try {
			User user = ShiroUtils.getUser();
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(model.getId());
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			// 对非组合的参数清空
			if (model.getIsGroup() != null && !model.getIsGroup().equals("1")) {
				groupIds = null;
				groupNums = null;
				groupPrices = null;
			}
			model.setModifier(user.getId());
			model.setUpdateName(user.getName());
			// 修改时间
			model.setModifyDate(TimeUtils.getNow());
			boolean b = merchandiseService.modifyMerchandiseByMain(model, groupIds, groupNums, groupPrices);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}*/

	/**
	 * 导入页面
	 */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() {
		return "/derp/main/merchandise-import";
	}

	/**
	 * 导入商品
	 */
	@RequestMapping("/importMerchandise.asyn")
	@ResponseBody
	public ViewResponseBean importMerchandise(@RequestParam(value = "file", required = false) MultipartFile file,String linshi) {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user = ShiroUtils.getUser();
			if ("1".equals(linshi)) {
				resultMap = merchandiseService.importMerchandiseLinshi(data, user);
			}else {
				resultMap = merchandiseService.importMerchandise(data, user);
			}
			
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	/**
	 * 获取下拉列表
	 */
	@RequestMapping("/getSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBean(Long depotId) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
			result = merchandiseService.getSelectBean(user.getMerchantId(), depotId);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	/**
	 * 根据ID获取商品详情
	 */
	@RequestMapping("/getMerchandiseDetails.asyn")
	@ResponseBody
	public ViewResponseBean getMerchandiseDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		MerchandiseInfoDTO model = new MerchandiseInfoDTO();
		try {
			model = merchandiseService.searchDetail(id);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}

	/**
	 * 获取商品列表
	 * 
	 * @param model
	 *            商品信息
	 */
	@RequestMapping("/getList.asyn")
	@ResponseBody
	public ViewResponseBean getList(MerchandiseInfoModel model, String unNeedIds) {
		try {
			User user = ShiroUtils.getUser();
			List ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model.setMerchantId(user.getMerchantId());
			model = merchandiseService.getList(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}

	/**
	 * 根据仓库类型获取商品列表
	 * @param model 商品信息
	 */
	@RequestMapping("/getListForType.asyn")
	@ResponseBody
	public ViewResponseBean getListForType(MerchandiseInfoModel model, String unNeedIds) {
		try {
			User user = ShiroUtils.getUser();
			List ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model.setMerchantId(user.getMerchantId());
			
			model = merchandiseService.getListForDepotType(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}
	
	/**
	 * 新增商品获取备案商品列表
	 * 
	 * @param model
	 *            商品信息
	 */
	@RequestMapping("/getListByMerchant.asyn")
	@ResponseBody
	public ViewResponseBean getListByMerchant(MerchandiseInfoModel model, String unNeedIds) {
		try {
			User user = ShiroUtils.getUser();
			List ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model.setMerchantId(user.getMerchantId());
			model = merchandiseService.getListByMerchant(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}

	/**
	 * 根据商品id获取商品信息
	 * 
	 * @param ids
	 *            商品信息
	 */
	@RequestMapping("/getListByIds.asyn")
	@ResponseBody
	public ViewResponseBean getListByIds(String ids) {
		List<MerchandiseInfoDTO> result = new ArrayList<MerchandiseInfoDTO>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			result = merchandiseService.getListByIds(list);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	/**
	 * 获取商品列表
	 * 
	 * @param model
	 *            商品信息
	 */
	@RequestMapping("/getListByMerchantId.asyn")
	@ResponseBody
	public ViewResponseBean getListByMerchantId(MerchandiseInfoModel model, Long outDepotId, String unNeedIds) {
		try {
			List ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model = merchandiseService.getListByMerchantId(model,outDepotId);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}
	
	/**
	 * 公共的商品弹窗
	 * @param model 商品信息
	 */
	/*@RequestMapping("/getPopupList.asyn")
	@ResponseBody
	public ViewResponseBean getPopupList(MerchandiseInfoModel model, String unNeedIds, String popupType, Long outDepotId) {
		try {
			User user = ShiroUtils.getUser();
			List<Long> ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			if (user.getMerchantId() != null) {
				model.setMerchantId(user.getMerchantId());
			}

			model = merchandiseService.getPopupList(model, popupType, outDepotId);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}*/
	/**
     *公共方法获取商品分类下拉列表
     *level 分类级别 0:一级类目,1:二级类目
	 */
	@RequestMapping("/getMerchandiseCatList.asyn")
	@ResponseBody
	public ViewResponseBean getMerchandiseCatList(String level) {
		List<SelectBean> catList =null;
		try {
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("level", level);
			catList = merchandiseCatService.getSelectBean(paramMap);
		}catch(Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(catList);
	}
	/**
	 * 上传图片
	 * @param file
	 * @param session
	 * @return
	 */
	@RequestMapping("/uploadFile.asyn")
	@ResponseBody
	public ViewResponseBean uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, Long id, String type, HttpSession session) {
		String path = "";
		try{
            if(file==null){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            
            String fileName = file.getOriginalFilename();
            byte[] fileBytes = file.getBytes();
            Long fileSize = file.getSize();
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            User user = ShiroUtils.getUser();
			path = merchandiseService.uploadFile(fileName,fileBytes,fileSize,ext,user.getId(),user.getName(),id,type);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(path);
	}
	/**
	 * 结算价格-选择商品
	 * 获取商家下所有商品（条码相同只取一条记录）
	 * @param model
	 * @return
	 */
	@RequestMapping("/getListForSettlment.asyn")
	@ResponseBody
	public ViewResponseBean getListForSettlmentByPage(MerchandiseInfoModel model, String unNeedIds) {
		try {
			User user = ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			List<Long> ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model = merchandiseService.getListForSettlmentByPage(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}
	/**
	 * 禁用/启用
	 * @param model
	 * @return
	 */
	@RequestMapping("/auditMerchandies.asyn")
	@ResponseBody
	public ViewResponseBean auditMerchandies(MerchandiseInfoModel model) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
			if (model.getStatus().equals(DERP_SYS.MERCHANDISEINFO_STATUS_0)) {
				MerchandiseInfoDTO isExist = merchandiseService.searchDetail(model.getId());
				if (isExist.getOutDepotFlag().equals(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0)) {
					return ResponseFactory.error(StateCodeEnum.MESSAGE_10008);
				}
			}
			User user = ShiroUtils.getUser();;
			boolean b = merchandiseService.auditMerchandies(model,user);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
			
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * @Author Chen Yiluan
	 * @Description 根据ids、单据类型、仓库id获取商品信息
	 * @Param
	 * @return
	 */
	@RequestMapping("/getListByIdsAndTypeAndInOutDepot.asyn")
	@ResponseBody
	public ViewResponseBean getListByIdsAndTypeAndInOutDepot(String ids, String type, Long depotId) {

		List<Map<String, Object>> result = new ArrayList<>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			if (StringUtils.isBlank(type) || depotId == null) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user = ShiroUtils.getUser();
			List idList = StrUtils.parseIds(ids);
			//查询相关商品信息
			result = merchandiseService.getListByIdsAndOutInDepot(idList, type, depotId, user.getMerchantId());
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	/**
	 * 不同单据公共弹窗
	 * @param dto 商品信息
	 * @param unNeedIds 已选择的商品id
	 * @param productRestriction 商品选品限制
	 */
	@RequestMapping("/getOrderPopupList.asyn")
	@ResponseBody
	public ViewResponseBean getOrderPopupList(MerchandiseInfoDTO dto, String unNeedIds, String productRestriction) {
		try {
			User user = ShiroUtils.getUser();
			if (!StringUtils.isEmpty(unNeedIds)) {
				List<Long> ids = StrUtils.parseIds(unNeedIds);
				dto.setIds(ids);
			}
			if (user.getMerchantId() != null) {
				dto.setMerchantId(user.getMerchantId());
			}

			dto = merchandiseService.getOrderPopupDtoList(dto, productRestriction,user);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

}
