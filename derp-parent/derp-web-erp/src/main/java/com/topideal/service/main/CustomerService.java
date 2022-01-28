package com.topideal.service.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.CustomerInfoDTO;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.CustomerAptitudeModel;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;

import net.sf.json.JSONArray;

/**
 * 客户信息service
 * @author zhanghx
 */
public interface CustomerService {
	/**
	 * 获取客户商家关系表
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>>getCustomerMerchantRelList(Map<String, Object> map)throws Exception;
	/**
     * 获取客户商家关系表数据
     */
    List<CustomerMerchantRelModel>getCustMerListByUser(Map<String, Object>map)throws SQLException;
	
	/**
	 * 客户信息导入
	 * @return
	 */
	Map saveImportCustomer(Map<Integer, List<List<Object>>> data, Long merchantId, Long userId, String merchantName) throws Exception;
	/**
	 * 供应商导入
	 * @return
	 */
	Map saveImportSupplier(Map<Integer, List<List<Object>>> data, Long merchantId, Long userId, String merchantName) throws Exception;
    /**
     *  导出客户信息
     * @param id
     * @return
     * @throws Exception
     */
    List<CustomerInfoDTO> exportList(CustomerInfoDTO model) throws SQLException;
    /**
     *  导出客户信息
     * @param id
     * @return
     * @throws Exception
     */
    List<CustomerInfoDTO> exportSupplierList(CustomerInfoDTO model) throws SQLException;

	/**
	 * 客户列表（分页）
	 * @param model
	 * @return
	 */
	CustomerInfoDTO listCustomer(CustomerInfoDTO dto) throws SQLException;

	/**
	 * 新增客户
	 * @param model
	 * @return
	 */
	Map<String,Object> saveCustomer(CustomerInfoDTO model,JSONArray arry,JSONArray bankArry) throws Exception;
    /**保存供应商
	 * */
	public Map<String,Object> saveSupplier(CustomerInfoDTO model) throws Exception;

	/**
	 * 根据客户id删除客户(支持批量)
	 * @param ids
	 * @return
	 */
	boolean delCustomer(List<Long> ids) throws SQLException;

	/**
	 * 修改客户
	 * @param model
	 * @return
	 */
	Map<String,Object> modifyCustomer(CustomerInfoDTO model,JSONArray arry,List<Object> deleteIdsList,JSONArray bankArry) throws Exception;

	/**
	 * 根据id获取客户详情
	 * @param id
	 * @return
	 */
	CustomerInfoDTO searchDetail(Long id) throws SQLException;

	/**
	 * 根据条件查询供应商下拉列表
	 * @param Long
	 * */
	List<SelectBean> getSelectBeanBySupplier(Long id) throws SQLException;

	/**
	 * 根据条件查询客户下拉列表
	 * @param Long
	 * */
	//List<SelectBean> getSelectBeanByCustomer(Long id) throws SQLException;
	/**
	 * 根据商家ID获取客户下拉列表(必须是启用的状态)
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBeanByMerchantId(CustomerInfoDTO dto) throws SQLException;
	/**
	 * 上传图片
	 * @param fileName   文件名称
	 * @param fileBytes  文件字节数组
	 * @param fileSize   文件大小
	 * @param ext        文件后缀
	 * @param userId     用户id
	 * @param userName   用户名称
	 * @param id         供应商Id
	 * @param type       用于标识上传图片的字段 1:营业执照副本  2:组织机构代码副本  3:注册登记证   4:供货记录
	 *                                    5:证明信息  6:品牌授权  7:银行流水  8:企业备案表  9:法人身份证
	 * @return
	 * @throws SQLException
	 */
	String uploadFile(String fileName, byte[] fileBytes, Long fileSize, String ext, Long userId, String userName, Long id, String type) throws SQLException;
	/**
	 * 通过供应商id获取资质图片
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	CustomerAptitudeModel searchAptitude(Long id) throws SQLException;

	//List<SelectBean> getNameById(Long id) throws SQLException;

	/**
	 * 获取所有启用状态下的供应商下拉框
	 * */
	List<SelectBean> getAllSelectBeanBySupplier() throws SQLException;

	/**
	 * 客户和供应商的禁用和启用
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean modifyEnabledCustomer(CustomerInfoModel model) throws SQLException;

	List<SelectBean> getMerchantList(User user)throws SQLException;

	List<CustomerMerchantRelModel> getMerchantRel(String id) throws SQLException;

	/**
	 * 保存关联关系(供应商合作商家)
	 * @param list
	 * @param relModel 
	 * @return
	 * @throws SQLException 
	 */
	boolean saveMerchantRel(List<Long> list, CustomerMerchantRelModel relModel,User user) throws Exception;

	/**
	 * 保存客户合作商家
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> saveMerchantRelJSon(JSONArray  jSONArray,String customerId,User user) throws Exception;
	
	/**
	 * 获取关联公司详细信息
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<MerchantInfoModel> getMerchantRelInfo(Long id) throws SQLException;

	/**
	 * 根据主数据ID带出供应商、客户信息
	 * @param mainId
	 * @return
	 * @throws SQLException 
	 */
	CustomerInfoModel getCustomerMainInfo(Long mainId) throws SQLException;

	/**
	 * 根据关系表ID获取
	 * @param valueOf
	 * @return
	 */
	CustomerInfoDTO searchDetailByRelId(Long valueOf);

	/**
	 * 根据查询条件获取客户下拉列表(必须是启用的状态)
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<CustomerInfoDTO> getSelectBeanByDTO(CustomerInfoDTO dto) throws SQLException;

	List<CustomerInfoDTO> getCustomerByMerchantId(CustomerInfoDTO dto) throws SQLException;
	List<CustomerMerchantRelModel>  getSupplierMerchantRelByMainIdURL(CustomerInfoDTO dto) throws SQLException;

	CustomerInfoDTO getCustomerByMainMerchantId(CustomerInfoDTO dto) throws SQLException;

	List<MerchantInfoDTO> getMerchantRelInfoAndMerchantInfo(Map<String, Object> map) throws SQLException;

	List<SelectBean> getAllSupplierSelectBean() throws SQLException;
	
	/**
	 * 获取客户或者供应商的公共方法
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean>  getCusOrSurpSelectBean(CustomerInfoDTO dto)throws SQLException;
}
