package com.topideal.dao.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.DepartmentInfoDao;
import com.topideal.entity.dto.main.DepartmentInfoDTO;
import com.topideal.entity.vo.main.DepartmentInfoModel;
import com.topideal.mapper.main.DepartmentInfoMapper;
import com.topideal.mongo.dao.DepartmentInfoMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class DepartmentInfoDaoImpl implements DepartmentInfoDao {

    @Autowired
    private DepartmentInfoMapper mapper;
    @Autowired
    private DepartmentInfoMongoDao departmentInfoMongoDao;

    /**
     * 列表查询
     * @param model
     */
    @Override
    public List<DepartmentInfoModel> list(DepartmentInfoModel model) throws SQLException {
        return mapper.list(model);
    }
    /**
     * 新增
     * @param model
     */
    @Override
    public Long save(DepartmentInfoModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            //mongo添加数据
            JSONObject json = JSONObject.fromObject(model);
            json.remove("id") ;
            json.put("departmentInfoId", model.getId());//母品牌id
            departmentInfoMongoDao.insertJson(json.toString());

            return model.getId();
        }
        return null;
    }

    /**
     * 修改
     * @param model
     */
    @Override
    public int modify(DepartmentInfoModel  model) throws SQLException {
        int num=mapper.update(model);
        if(num==1){
            //修改Mongo的数据
            DepartmentInfoModel departmentInfoModel=new DepartmentInfoModel();
            departmentInfoModel.setId(model.getId());;
            departmentInfoModel= mapper.get(departmentInfoModel);

            Map<String, Object> params = new HashMap<String,Object>();
            params.put("departmentInfoId", model.getId());

            JSONObject jsonObject = JSONObject.fromObject(departmentInfoModel);
            jsonObject.put("departmentInfoId",model.getId());
            jsonObject.remove("id");

            departmentInfoMongoDao.update(params, jsonObject);
        }
        return num;
    }

    /**
     * 分页查询
     * @param model
     */
    @Override
    public DepartmentInfoModel  searchByPage(DepartmentInfoModel  model) throws SQLException{
        PageDataModel<DepartmentInfoModel> pageModel=mapper.listByPage(model);
        return (DepartmentInfoModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public DepartmentInfoModel  searchById(Long id)throws SQLException {
        DepartmentInfoModel  model=new DepartmentInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }

    /**
     * 根据商家实体类查询商品
     * @param model
     * */
    @Override
    public DepartmentInfoModel searchByModel(DepartmentInfoModel model) throws SQLException {
        return mapper.get(model);
    }

    @Override
    public int delete(List ids) throws SQLException {
        // 先从mongoDB删除
        for (int i = 0; i < ids.size(); i++) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("departmentInfoId", (Long) ids.get(i));
            departmentInfoMongoDao.remove(params);
        }
        return mapper.batchDel(ids);
    }

    @Override
    public DepartmentInfoDTO getListByPage(DepartmentInfoDTO dto) {
        PageDataModel<DepartmentInfoDTO> pageModel = mapper.getListByPage(dto);
        return (DepartmentInfoDTO) pageModel.getModel();
    }

    @Override
    public List<DepartmentInfoModel> searchByNameCode(String code, String name) {
        return mapper.searchByNameCode(code,name);
    }

    @Override
    public DepartmentInfoDTO detailById(Long id) {
        return mapper.detailById(id);
    }


    @Override
    public List<SelectBean> getSelectBean() {
        return mapper.getSelectBean();
    }
}