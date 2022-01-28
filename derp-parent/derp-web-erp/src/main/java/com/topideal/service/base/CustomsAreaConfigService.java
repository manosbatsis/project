package com.topideal.service.base;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.vo.main.CustomsAreaConfigModel;
import com.topideal.entity.vo.main.DepotCustomsRelModel;
import com.topideal.entity.vo.main.MerchandiseCustomsRelModel;

/**
 * 关区配置
 */
public interface CustomsAreaConfigService {


    /**
     * 查询关区列表
     * @param model
     * @return
     * @throws SQLException
     */
    CustomsAreaConfigDTO getListByPage(CustomsAreaConfigDTO model) throws SQLException;

    /**
     * 添加关区配置信息
     * @param model
     * @return
     * @throws Exception
     */
    Map<String, Object> saveCustomsArea(CustomsAreaConfigModel model) throws Exception;


    /**
     * 导出
     * @param model
     * @return
     * @throws Exception
     */
    List<CustomsAreaConfigModel>  listForExport(CustomsAreaConfigModel model) throws  Exception;


    /**
     * 导入
     * @param data
     * @param user
     * @return
     * @throws SQLException
     */
    Map importCustomsArea(Map<Integer, List<List<Object>>> data, User user) throws SQLException;


    /**
     * 批量删除
     * @return
     * @throws SQLException
     */
    Map<String, Object> delReptile(String ids) throws SQLException;
    
    /**
     * 仓库关联关区下拉
     * @param model
     * @return
     * @throws Exception
     */
    List<SelectBean> getSelectBean(DepotCustomsRelModel model) throws Exception;
    /**
     * 获取关区下拉列表
     * @param model
     * @return
     * @throws Exception
     */
    List<SelectBean> getCustomsSelectBean() throws Exception;
    
    /**
     * 获取 商品关区关系表数据
     * @param model
     * @return
     */
    List<MerchandiseCustomsRelModel>getmerchandiseCustomsRelList(MerchandiseCustomsRelModel model) throws Exception;

}
