package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.DepartmentInfoDTO;
import com.topideal.entity.vo.main.DepartmentInfoModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@MyBatisRepository
public interface DepartmentInfoMapper extends BaseMapper<DepartmentInfoModel> {

    /**
     * 分页查询
     * @param dto
     * @return
     */
    public PageDataModel<DepartmentInfoDTO> getListByPage(DepartmentInfoDTO dto);

    /**
     * 校验编码和名称的唯一性
     * @param code
     * @param name
     * @return
     */
    public List<DepartmentInfoModel> searchByNameCode(@Param("code")String code,@Param("name") String name);


    /**
     * 根据id查看详情
     * @param id
     * @return
     */
    public DepartmentInfoDTO  detailById(@Param("id") Long id);

    /**
     * 获取部门名称
     * @return
     */
    public List<SelectBean> getSelectBean();
}
