package com.topideal.service.main.impl;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.DepartmentInfoDao;
import com.topideal.entity.dto.main.DepartmentInfoDTO;
import com.topideal.entity.vo.main.DepartmentInfoModel;
import com.topideal.mongo.dao.DepartmentInfoMongoDao;
import com.topideal.service.main.DepartmentInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentInfoServiceImpl implements DepartmentInfoService {

    @Autowired
    private DepartmentInfoDao departmentInfoDao;

    @Override
    public DepartmentInfoDTO getListByPage(DepartmentInfoDTO dto) throws SQLException {
        return departmentInfoDao.getListByPage(dto);
    }

    @Override
    public DepartmentInfoDTO detailById(Long id) {
        return departmentInfoDao.detailById(id);
    }

    @Override
    public Map modifyDepartmentInfo(DepartmentInfoModel model, User user) {
        Map map=new HashMap();
        try{
            //校验名称是否重复
            List<DepartmentInfoModel> infoModel=departmentInfoDao.searchByNameCode(null,model.getName());
            if(infoModel.size()>0){
                map.put("code","01");
                map.put("message","部门名称已经存在,不允许重复！");
                return map;
            }
            model.setModifyDate(TimeUtils.getNow());
            model.setModifier(user.getId());
            model.setModifiyName(user.getName());
            int count=departmentInfoDao.modify(model);
            if(count > 0){
                map.put("code","00");
                map.put("message","修改成功！");
                return map;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map saveDepartmentInfo(DepartmentInfoModel model,User user) {
        Map map=new HashMap();
        try{
            //校验部门编码和名称是否相同
            List<DepartmentInfoModel> infoModel=departmentInfoDao.searchByNameCode(model.getCode(),null);
            if(infoModel.size()>0){
                map.put("code","01");
                map.put("message","部门编码已经存在！");
                return map;
            }
            infoModel=departmentInfoDao.searchByNameCode(null,model.getName());
            if(infoModel.size()>0){
                map.put("code","01");
                map.put("message","部门名称已经存在！");
                return map;
            }
            model.setModifyDate(TimeUtils.getNow());
            model.setModifier(user.getId());
            model.setModifiyName(user.getName());
            model.setCreateName(user.getName());
            model.setCreater(user.getId());
            model.setCreateDate(TimeUtils.getNow());
            Long id=departmentInfoDao.save(model);
            if(id > 0){
                map.put("code","00");
                map.put("message","新增成功！");
                return map;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public List<SelectBean> getSelectBean() throws SQLException {
        return departmentInfoDao.getSelectBean();
    }


}
