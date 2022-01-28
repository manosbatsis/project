package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.EmailConfigDao;
import com.topideal.entity.dto.main.EmailConfigDTO;
import com.topideal.entity.vo.main.EmailConfigModel;
import com.topideal.mapper.main.EmailConfigMapper;
import com.topideal.mongo.dao.EmailConfigMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件配置dao
 * Created by weixiaolei on 2018/4/10.
 * @author 杨创
 */
@Repository
public class EmailConfigDaoImpl implements EmailConfigDao {

    @Autowired
    private EmailConfigMapper mapper;
    
    @Autowired
	private EmailConfigMongoDao emailConfigMongoDao;  //邮件发送配置信息
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<EmailConfigModel> list(EmailConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(EmailConfigModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
			//存储到MONGODB
			String json= JSONObject.fromObject(model).toString();
			JSONObject jsonObject=JSONObject.fromObject(json);
			jsonObject.put("emailId",model.getId());
			jsonObject.remove("id");
			emailConfigMongoDao.insertJson(jsonObject.toString());
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
    	for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("emailId",(Long)ids.get(i));
			emailConfigMongoDao.remove(params);
		}
    	
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(EmailConfigModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
    	int num = mapper.update(model);
    	if(num > 0){
    		EmailConfigModel emailConfig = new EmailConfigModel();
    		emailConfig.setId(model.getId());
    		emailConfig = mapper.get(emailConfig);

			//修改mongodb 仓库信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("emailId",model.getId());

			JSONObject jsonObject=JSONObject.fromObject(emailConfig);
			jsonObject.put("emailId",model.getId());
			jsonObject.remove("id");

			emailConfigMongoDao.update(params,jsonObject);
    	}
        return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public EmailConfigModel  searchByPage(EmailConfigModel  model) throws SQLException{
        PageDataModel<EmailConfigModel> pageModel=mapper.listByPage(model);
        return (EmailConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public EmailConfigModel  searchById(Long id)throws SQLException {
        EmailConfigModel  model=new EmailConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public EmailConfigModel searchByModel(EmailConfigModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public EmailConfigDTO getListByPage(EmailConfigDTO dto) {
		PageDataModel<EmailConfigDTO> page = mapper.getListByPage(dto);
		return (EmailConfigDTO)page.getModel() ;
	}
	@Override
	public EmailConfigDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}
    
}