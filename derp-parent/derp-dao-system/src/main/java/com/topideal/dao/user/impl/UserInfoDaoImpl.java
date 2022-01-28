package com.topideal.dao.user.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.user.UserInfoDao;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.mapper.user.UserInfoMapper;
import com.topideal.mongo.dao.UserInfoMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 */
@Repository
public class UserInfoDaoImpl implements UserInfoDao {

    @Autowired
    private UserInfoMapper mapper;
    @Autowired
    private UserInfoMongoDao userInfoMongoDao;

    @Override
    public Long save(UserInfoModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            JSONObject jsonObject=JSONObject.fromObject(model);
            jsonObject.put("userId", model.getId()) ;
            jsonObject.remove("id");
            userInfoMongoDao.insertJson(jsonObject.toString());
            return model.getId();
        }
        return null;
    }

    @Override
    public int delete(List ids) throws SQLException {
        // 先从mongoDB删除
        for (int i = 0; i < ids.size(); i++) {

            Long id = (Long)ids.get(i);

            UserInfoModel userInfoModel = new UserInfoModel();

            userInfoModel.setId(id);
            userInfoModel = mapper.get(userInfoModel);

            // 修改mongodb 客户、供应商信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userInfoModel.getId());

            userInfoMongoDao.remove(params);
        }
        return mapper.batchDel(ids);
    }

    @Override
    public int modify(UserInfoModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        int update = mapper.update(model);
        if(update > 0){
            UserInfoModel userInfoModel = new UserInfoModel();
            userInfoModel.setId(model.getId());
            userInfoModel = mapper.get(userInfoModel);
            // 修改mongodb 客户、供应商信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", model.getId());

            JSONObject jsonObject = JSONObject.fromObject(userInfoModel);
            jsonObject.put("userId", model.getId()) ;;
            jsonObject.remove("id");
            userInfoMongoDao.update(params, jsonObject);
        }
        return update;
    }

    @Override
    public UserInfoModel  searchByPage(UserInfoModel  model) throws SQLException{
    	PageDataModel<UserInfoModel> pageModel=mapper.listByPage(model);
        return (UserInfoModel ) pageModel.getModel();
    }

    @Override
    public UserInfoModel  searchById(Long id) throws SQLException{
        UserInfoModel  model=new UserInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    @Override
    public UserInfoModel searchUserInfo(UserInfoModel model) throws SQLException{
        return mapper.get(model);
    }
    @Override
    public UserInfoDTO searchByUsername(String username){
        return mapper.searchUserByUsername(username);
    }

    @Override
    public List<UserInfoModel> list(UserInfoModel model) throws SQLException {
        return mapper.list(model);
    }

	@Override
	public UserInfoModel searchByModel(UserInfoModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<String> getBtnPersonInfo(long id){
        return mapper.getBtnPersonInfo(id);
    }

	@Override
	public UserInfoDTO getDetails(Long id) throws SQLException {
		return mapper.getDetails(id);
	}

	@Override
	public UserInfoDTO searchDTOByPage(UserInfoDTO dto) {
		PageDataModel<UserInfoDTO> pageModel= mapper.searchDTOByPage(dto);
		return (UserInfoDTO)pageModel.getModel() ;
	}

    @Override
    public List<UserInfoModel> listByIds(UserInfoDTO dto) throws SQLException {
        return mapper.listByIds(dto);
    }
}

