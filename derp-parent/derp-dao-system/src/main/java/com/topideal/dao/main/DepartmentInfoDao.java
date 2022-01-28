package com.topideal.dao.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.DepartmentInfoDTO;
import com.topideal.entity.vo.main.DepartmentInfoModel;
import org.apache.ibatis.annotations.Select;

import java.sql.SQLException;
import java.util.List;


public interface DepartmentInfoDao extends BaseDao<DepartmentInfoModel>{

    /**
     * 分页查询
     * @param dto
     */
    public DepartmentInfoDTO getListByPage(DepartmentInfoDTO dto);


    /**
     * 校验编码和名称的唯一性
     * @param code
     * @param name
     * @return
     */
    public List<DepartmentInfoModel> searchByNameCode(String code,String name);

    /**
     * 根据id查看详情
     * @param id
     * @return
     */
    public DepartmentInfoDTO  detailById(Long id);


    /**
     * 获取部门下拉框
     * @return
     */
    public List<SelectBean> getSelectBean();






}
