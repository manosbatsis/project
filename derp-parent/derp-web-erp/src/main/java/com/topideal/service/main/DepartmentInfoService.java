package com.topideal.service.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.DepartmentInfoDTO;
import com.topideal.entity.dto.main.ReminderEmailConfigDTO;
import com.topideal.entity.vo.main.DepartmentInfoModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 */
public interface DepartmentInfoService {

    /**
     * 分页查询
     * @param dto
     * @return
     * @throws SQLException
     */
    public DepartmentInfoDTO  getListByPage(DepartmentInfoDTO dto) throws SQLException;

    /**
     * 根据id查看详情
     * @param id
     * @return
     */
    public DepartmentInfoDTO  detailById(Long id);

    /**
     * 修改
     * @param model
     * @return
     */
    public Map modifyDepartmentInfo(DepartmentInfoModel model, User user);


    /**
     * 新增
     * @param model
     * @return
     */
    public Map saveDepartmentInfo(DepartmentInfoModel model, User user);

    /**
     * 获取部门
     * @return
     * @throws SQLException
     */
    public List<SelectBean> getSelectBean() throws SQLException;


}
