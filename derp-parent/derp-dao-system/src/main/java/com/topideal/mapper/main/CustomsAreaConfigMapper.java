package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.vo.main.CustomsAreaConfigModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface CustomsAreaConfigMapper extends BaseMapper<CustomsAreaConfigModel> {

   /**
    * 分页查询列表
    * @param model
    * @return
    */
   PageDataModel<CustomsAreaConfigDTO> getListByPage(CustomsAreaConfigDTO model);
   
   /**
  * 获取关区下拉列表
  * @param model
  * @return
  * @throws Exception
  */
   List<SelectBean> getCustomsSelectBean() throws Exception;

    List<CustomsAreaConfigModel> listByLike(CustomsAreaConfigModel customsAreaConfigModel);
}
