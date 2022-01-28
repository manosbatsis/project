package com.topideal.order.service.filetemp.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.FileTempCustomerRelDao;
import com.topideal.dao.common.FileTempDao;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.common.FileTempCustomerRelModel;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.AttachmentInfoMongoDao;
import com.topideal.mongo.dao.CustomerMerchantRelMongoDao;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.entity.CustomerMerchantRelMongo;
import com.topideal.order.service.filetemp.FileTempService;
import com.topideal.order.tools.ExcelConvertHTMLUtils;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

@Service
public class FileTempServiceImpl implements FileTempService{

	@Autowired
	FileTempDao fileTempDao ;
	
	@Autowired
	private AttachmentInfoMongoDao attachmentInfoMongoDao;

	@Autowired
	private FileTempCustomerRelDao fileTempCustomerRelDao;
	@Autowired
	private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;
	
	@Override
	public FileTempDTO searchById(Long id) throws SQLException {

		FileTempModel fileTempModel = fileTempDao.searchById(id);

		Map<String,Object> params = new HashMap<String,Object>() ;
		params.put("relationCode", fileTempModel.getCode()) ;
		params.put("status", DERP_ORDER.ATTACHMENT_STATUS_001) ;
		AttachmentInfoMongo pageInfo = attachmentInfoMongoDao.findOne(params);

		FileTempDTO tempDTO = new FileTempDTO();
		BeanUtils.copyProperties(fileTempModel, tempDTO);

		if (pageInfo != null) {
			tempDTO.setPageFileUrl(pageInfo.getAttachmentUrl());
		}
		params.put("relationCode", fileTempModel.getCode()+"_INVOICE") ;
		AttachmentInfoMongo invoiceInfo = attachmentInfoMongoDao.findOne(params);

		if (invoiceInfo != null) {
			tempDTO.setInvoiceFileUrl(invoiceInfo.getAttachmentUrl());
		}

		return tempDTO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FileTempDTO listFiletemp(FileTempDTO dto) throws SQLException {
		
		Integer count = fileTempDao.coutPageList(dto) ;
		List<FileTempDTO> pageList = fileTempDao.getPageList(dto);
		
		dto.setTotal(count);
		dto.setList(pageList);
		
		return dto;
	}

	@Override
	public void saveOrModity(FileTempModel model) throws Exception {
		
		if(model.getId() == null) {
//			model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_FPMB));
			model.setCreateDate(TimeUtils.getNow());
			fileTempDao.save(model) ;
		}else {
			model.setModifyDate(TimeUtils.getNow());
			fileTempDao.modify(model) ;
		}
		
	}

	@Override
	public List<FileTempCustomerRelModel> listCustomerRel(Long fileId) throws SQLException {
		FileTempCustomerRelModel fileTempCustomerRelModel = new FileTempCustomerRelModel();
		fileTempCustomerRelModel.setFileId(fileId);
		List<FileTempCustomerRelModel> relModelList = fileTempCustomerRelDao.list(fileTempCustomerRelModel);
		return relModelList;
	}

	@Override
	public Map<String, String> saveCustomerRel(FileTempDTO dto) throws Exception {
		Map<String, String> result = new HashMap<>();
		FileTempModel existModel = fileTempDao.searchById(dto.getId());
		if (existModel == null) {
			result.put("code", "01");
			result.put("message", "该发票模板不存在");
			return result;
		}
		fileTempDao.delByFileId(dto.getId());
		List<Map<String, Object>> customerRelList = dto.getCustomerRelList();
		for (Map<String, Object> relDTO : customerRelList) {
			String customerId = (String) relDTO.get("customerId");
			String customerName = (String) relDTO.get("customerName");
			String customerCode = (String) relDTO.get("customerCode");
			FileTempCustomerRelModel relModel = new FileTempCustomerRelModel();
			relModel.setFileId(dto.getId());
			relModel.setCustomerId(Long.valueOf(customerId));
			relModel.setCustomerCode(customerCode);
			relModel.setCustomerName(customerName);
			fileTempCustomerRelDao.save(relModel);
		}
		FileTempModel fileTempModel = new FileTempModel();
		fileTempModel.setId(dto.getId());
		fileTempModel.setStatus(dto.getStatus());
		fileTempDao.modify(fileTempModel);
		result.put("code", "00");
		result.put("message", "保存成功");
		return result;
	}

	@Override
	public List<FileTempDTO> listFileTempInfo(FileTempDTO dto, String customerIds) throws SQLException {
		List<Long> customerIdList = new ArrayList<>();
		if (StringUtils.isNotBlank(customerIds)) {
			String[] split = customerIds.split(",");
			for (String  str : split) {
				Map<String, Object> params = new HashMap<>();
				params.put("customerId", Long.valueOf(str));
				List<CustomerMerchantRelMongo> list = customerMerchantRelMongoDao.findAll(params);
				for (CustomerMerchantRelMongo mongo : list) {
					customerIdList.add(mongo.getCustomerId());
				}
			}
			dto.setCustomerIds(customerIdList);
		}
		return fileTempDao.listFileTempInfo(dto);
	}
	/**
	 * 获取模板下拉
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<FileTempModel> getFileTemp(FileTempModel model) throws SQLException {
		return fileTempDao.list(model);
	}

	@Override
	public String generateHtml(FileTempDTO fileTemp, User user) throws Exception {

		String templatePath = "classpath:/customsTemplate/" + fileTemp.getToUrl() +"_INVOICE.xlsx";

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource(templatePath);
		InputStream in = resource.getInputStream();

		if (in == null) {
			throw new RuntimeException("模板路径不存在！");
		}

		Workbook wb = WorkbookFactory.create(in);
		String htmlExcel="";
		if (wb instanceof XSSFWorkbook) {
			XSSFWorkbook xWb = (XSSFWorkbook) wb;
			htmlExcel = ExcelConvertHTMLUtils.getExcelInfo(xWb,true);
		}else if(wb instanceof HSSFWorkbook){
			HSSFWorkbook hWb = (HSSFWorkbook) wb;
			htmlExcel = ExcelConvertHTMLUtils.getExcelInfo(hWb,true);
		}

		return htmlExcel;
	}

	@Override
	public Map<String, Object> uploadFile(MultipartFile file, String code, User user, String type) throws Exception {
		Map<String ,Object> map = new HashMap<String, Object>() ;

		String fileName = file.getOriginalFilename();
		byte[] fileBytes = file.getBytes();
		Long fileSize = file.getSize();
		String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("fileTypeCode", SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
		jsonObject.put("fileName",fileName);
		jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
		jsonObject.put("fileExt",ext);
		jsonObject.put("fileSize",String.valueOf(fileSize));
		String resultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

		JSONObject jsonObj = JSONObject.fromObject(resultJson);

		if(jsonObj.getInt("code")!= 200){
			map.put("code" , jsonObj.getInt("code")) ;
			map.put("msg", jsonObj.getString("msg"));

			return map ;
		}

		//返回文件服务器存储URL
		String url = jsonObj.getString("url") ;


		//删除已存在的文件信息
		Map<String,Object> params = new HashMap<String,Object>() ;
		params.put("relationCode", code) ;

		if ("1".equals(type)) {
			params.put("relationCode", code+"_INVOICE");
		}
		params.put("status", DERP_ORDER.ATTACHMENT_STATUS_001) ;
		List<AttachmentInfoMongo> delAttachmentInfos = attachmentInfoMongoDao.findAll(params);

		for (AttachmentInfoMongo attachmentInfoMongo : delAttachmentInfos) {
			Map<String,Object> update = new HashMap<String,Object>() ;
			update.put("attachmentCode", attachmentInfoMongo.getAttachmentCode()) ;

			Map<String,Object> data = new HashMap<String,Object>() ;
			data.put("status", DERP_ORDER.ATTACHMENT_STATUS_006) ;
			data.put("modifyDate", new Date()) ;

			attachmentInfoMongoDao.update(update, data);
		}

		//附件信息写入MongoDB
		AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
		attachmentInfoMongo.setAttachmentName(fileName); 		//附件名
		attachmentInfoMongo.setAttachmentSize(fileSize); 		//附件大小
		attachmentInfoMongo.setAttachmentType(ext);		       	//附件类型
		attachmentInfoMongo.setCreator(user.getId());			//上传者
		attachmentInfoMongo.setCreatorName(user.getName());
		attachmentInfoMongo.setCreateDate(new Date());
		attachmentInfoMongo.setRelationCode(code);              //关联订单编码
		if ("1".equals(type)) {
			attachmentInfoMongo.setRelationCode(code+"_INVOICE");              //关联订单编码
		}

		attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
		attachmentInfoMongo.setAttachmentUrl(url);              //设置Url
		attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));

		String module = "" ;
		//设置所属模块
		for(SourceStatusEnum sourceStatusEnum : SourceStatusEnum.values()){
			if(code.contains(sourceStatusEnum.name())){
				module = sourceStatusEnum.getKey() ;
				break ;
			}
		}

		if(StringUtils.isBlank(module)) {
			module = SourceStatusEnum.XSTHRK.getValue() ;
		}

		attachmentInfoMongo.setModule(module);

		attachmentInfoMongoDao.insert(attachmentInfoMongo);

		map.put("code", 200) ;
		map.put("attachmentInfo", attachmentInfoMongo) ;

		return map;
	}

	@Override
	public void delAttachment(String code, String type) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>() ;
		Map<String,Object> data = new HashMap<String,Object>() ;
		params.put("status", DERP_ORDER.ATTACHMENT_STATUS_001) ;
		params.put("relationCode", code) ;
		if ("1".equals(type)) {
			params.put("relationCode", code+"_INVOICE");
		}

		data.put("status", DERP_ORDER.ATTACHMENT_STATUS_006) ;
		data.put("modifyDate", new Date()) ;

		attachmentInfoMongoDao.update(params, data);
	}
}
