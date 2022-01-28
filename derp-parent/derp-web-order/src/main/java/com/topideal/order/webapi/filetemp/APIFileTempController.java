package com.topideal.order.webapi.filetemp;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.entity.dto.common.FileTempCustomerRelDTO;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.common.FileTempCustomerRelModel;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.order.service.filetemp.FileTempService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.filetemp.form.FileTempForm;
import com.topideal.order.webapi.filetemp.form.FileTempModelForm;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.*;

/**
 * 模版文件控制器
 * @author Guobs
 *
 */
@RestController
@RequestMapping("/webapi/order/fileTemp")
@Api(tags = "fileTemp-模版管理")
public class APIFileTempController {

	private final static String[] EXT_NAMES= {"docx" , "xlsx" , "doc" , "xls" , "txt" , "pdf" , "jpg" , "png" , "bmp" , "jpeg" , "zip" , "rar", "eml","csv"}  ;

	private static final Logger LOG = Logger.getLogger(APIFileTempController.class) ;
	
	@Autowired
	FileTempService fileTempService ;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据ID获取模版信息")
	@PostMapping("getDetailsById.asyn")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "模版ID", required = true)
    })
	public ResponseBean<FileTempDTO> getDetailsById(@RequestParam(value = "token", required = true)String token, 
			@RequestParam(value = "id", required = true)String id) throws NumberFormatException, SQLException {
		
		if(StringUtils.isBlank(id)) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		
		try {
			FileTempDTO dto = fileTempService.searchById(Long.valueOf(id)) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "分页获取模版列表")
	@PostMapping("listFiletemp.asyn")
	public ResponseBean<FileTempDTO> listFiletemp(FileTempForm form) {
		
		if(StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		
		try {
			
			User user = ShiroUtils.getUserByToken(form.getToken()) ;
			
			FileTempDTO dto = new FileTempDTO();
			BeanUtils.copyProperties(form, dto);
//			if(!"1".equals(user.getUserType())){
//				dto.setMerchantId(user.getMerchantId());
//			}
			
			List<Long> customerIdList = new ArrayList<>();
			if (StringUtils.isNotBlank(form.getCustomerIds())) {
				String[] split = form.getCustomerIds().split(",");
				for (String str : split) {
					customerIdList.add(Long.valueOf(str));
				}
				dto.setCustomerIds(customerIdList);
			}
			
			dto = fileTempService.listFiletemp(dto) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
		
	}
	
	/**
	 * 保存或修改
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "保存或修改模版")
	@PostMapping("saveOrModify.asyn")
	public ResponseBean saveOrModify(FileTempModelForm form) {
		
		if(StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		
		try {
			
			FileTempModel model = new FileTempModel() ;
			
			User user = ShiroUtils.getUserByToken(form.getToken()) ;
			model.setMerchantId(user.getMerchantId());
			model.setMerchantName(user.getMerchantName());
			model.setId(form.getId());
			model.setCode(form.getCode());
			model.setContentBody(form.getContentBody());
			model.setCusType(form.getCusType());
			model.setStatus(form.getStatus());
			model.setTitle(form.getTitle());
			model.setToUrl(form.getToUrl());
			model.setType(form.getType());
			
			fileTempService.saveOrModity(model) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
		
	}
	

	/**
	 * 获取模板适用客户
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getRelCustomer.asyn")
	@ApiOperation(value = "获取模板适用客户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "fileId", value = "模版ID", required = true)
    })
	public ResponseBean<List<FileTempCustomerRelDTO>> getRelCustomer(@RequestParam(value = "token", required = true) String token, 
			@RequestParam(value = "fileId", required = true) String fileId) {
		
		if(StringUtils.isBlank(token)
				|| StringUtils.isBlank(fileId)) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		
		List<FileTempCustomerRelDTO> resultDTOList = new ArrayList<FileTempCustomerRelDTO>();
		try {
			
			List<FileTempCustomerRelModel> resultList = fileTempService.listCustomerRel(Long.valueOf(fileId)) ;
			
			for (FileTempCustomerRelModel fileTempCustomerRelModel : resultList) {
				FileTempCustomerRelDTO dto = new FileTempCustomerRelDTO() ;
				dto.setCustomerCode(fileTempCustomerRelModel.getCustomerCode());
				dto.setCustomerName(fileTempCustomerRelModel.getCustomerName());
				dto.setCustomerId(fileTempCustomerRelModel.getCustomerId());
				dto.setFileId(fileTempCustomerRelModel.getFileId());
				
				resultDTOList.add(dto) ;
			}
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultDTOList);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	@SuppressWarnings("unchecked")
	@PostMapping("/saveCustomerRel.asyn")
	@ApiOperation(value = "保存关联客户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "json字符串", required = true)
    })
	public ResponseBean<Map<String, String>> saveCustomerRel(@RequestParam(value = "token", required = true) String token, 
			@RequestParam(value = "json", required = true) String json) {
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			// 实例化JSON对象
			JSONObject jsonData=JSONObject.fromObject(json);
			Map<String, Object> classMap = new HashMap<String, Object>();
			classMap.put("customerRelList",Map.class);
			FileTempDTO dto = (FileTempDTO) JSONObject.toBean(jsonData, FileTempDTO.class,classMap);
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(dto.getId()).empty();
			if (isNull) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			retMap = fileTempService.saveCustomerRel(dto);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
		
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/listFileTempInfo.asyn")
	@ApiOperation(value = "获取模板列表")
	public ResponseBean<List<FileTempDTO>> listFileTempInfo(FileTempForm form) {
		
		if(StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		
		try {
			User user = ShiroUtils.getUserByToken(form.getToken()) ;
			
			FileTempDTO dto = new FileTempDTO() ;
			dto.setStatus(form.getStatus()) ;
			dto.setType(form.getType()) ;
			dto.setTitle(form.getTitle());
			
			if(!"1".equals(user.getUserType())
					&& !"1".equals(form.getType())){
				dto.setMerchantId(user.getMerchantId());
			}
			
			List<FileTempDTO> fileTempDTOs = fileTempService.listFileTempInfo(dto, form.getCustomerIds()) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, fileTempDTOs);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 获取模板下拉
	 */
	@PostMapping ("/getFileTemp.asyn")
	@ApiOperation(value = "获取模板下拉")
	public ResponseBean<List<FileTempDTO>> getFileTemp(FileTempForm form) {
		List<FileTempModel> resultList = new ArrayList<>();
		try {

			FileTempModel model = new FileTempModel() ;

			BeanUtils.copyProperties(form, model);

			resultList = fileTempService.getFileTemp(model);

			List<FileTempDTO> dtoList = new ArrayList<>() ;

			for (FileTempModel fileTempModel: resultList) {

				FileTempDTO dto = new FileTempDTO() ;
				BeanUtils.copyProperties(fileTempModel, dto);

				dtoList.add(dto) ;
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dtoList);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}


	@ApiOperation(value = "模板预览")
	@PostMapping(value = "/previewTemp.asyn")
	public ResponseBean previewTemp(FileTempForm form) {

		try {
			User user = ShiroUtils.getUserByToken(form.getToken());


			FileTempDTO fileTemp = fileTempService.searchById(form.getFileTempId());

			if (!DERP_ORDER.TEMP_TYPE_2.equals(fileTemp.getType())) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "只有“应收账单“类型的模板支持预览！");
			}

			if (StringUtils.isBlank(fileTemp.getToUrl())) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "模板路径不能为空！");
			}

			//返回发票模板的html
			String excelHtml = fileTempService.generateHtml(fileTemp, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, excelHtml);

		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}


	/**
	 * 上传附件action
	 * @param file
	 * @param code
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@PostMapping("/uploadFiles.asyn")
	@ApiOperation(value = "上传附件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "code", value = "模板编码", required = true),
			@ApiImplicitParam(name = "type", value = "模板类型 0-页面模板 1-发票模板", required = true)
	})
	public ResponseBean<Map<String, Object>> uploadFiles(
			@ApiParam(value = "上传的文件", required = true)
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "token", required = true) String token,
			String code, String type) {

		if(file == null || StringUtils.isBlank(code) || StringUtils.isBlank(type)){
			//输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//文件不能为空
		}

		//判断是否超出限制文件大小
		Long fileSize = file.getSize();
		if( fileSize > 20 * 1024 * 1024L ) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, "文件大小超出限制");
		}

		//判断是否符合上传文件类型
		String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		if(!Arrays.asList(EXT_NAMES).contains(ext.toLowerCase())) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, "不支持上传文件类型");
		}

		try {
			User user= ShiroUtils.getUserByToken(token) ;

			Map<String, Object> returnMap = null ;
			returnMap = fileTempService.uploadFile(file , code , user, type);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, returnMap) ;

		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "获取模板编码")
	@PostMapping(value = "/getFileTempCode.asyn")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	public ResponseBean getFileTempCode(@RequestParam(value = "token", required = true)String token) {

		try {

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_FPMB));

		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	/**
	 * 逻辑删除附件信息
	 * @return
	 */
	@PostMapping("/delFileTemp.asyn")
	@ApiOperation(value = "逻辑删除文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "code", value = "模板编码", required = true),
			@ApiImplicitParam(name = "type", value = "模板类型 0-页面模板 1-发票模板", required = true)
	})
	public ResponseBean delAttachment(String code, String type,
									  @RequestParam(value = "token", required = true) String token) {

		if(StringUtils.isBlank(code) || StringUtils.isBlank(type)){
			//输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {
			fileTempService.delAttachment(code, type) ;
		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
	}
}
