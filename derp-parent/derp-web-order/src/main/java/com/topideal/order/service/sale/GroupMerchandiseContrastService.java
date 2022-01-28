package com.topideal.order.service.sale;


import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.sale.GroupMerchandiseContrastDTO;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastModel;
import com.topideal.mongo.entity.MerchantShopRelMongo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 组合商品对照表 service层
 * @Author: Chen Yiluan
 * @Date: 2019-09-16 16:51
 **/
public interface GroupMerchandiseContrastService {

    /**
     * 组合商品列表信息
     */
    public GroupMerchandiseContrastDTO listGroupMerchandiseContrast(GroupMerchandiseContrastDTO dto) throws SQLException;

    /**
     * 保存组合商品信息
     */
    public void saveGroupMerchandiseContrast(GroupMerchandiseContrastModel model, User user) throws SQLException;


    /**
     * 获取组合商品信息详情
     */
    public GroupMerchandiseContrastModel getDetail(Long id) throws SQLException;


    /**
     * 修改组合商品信息
     */
    public void modifyGroupMerchandiseContrast(GroupMerchandiseContrastModel model, User user)  throws SQLException;

    public GroupMerchandiseContrastModel getGroupMerchandiseContrast(GroupMerchandiseContrastModel model) throws SQLException;

    public List<GroupMerchandiseContrastDTO> getexportList(GroupMerchandiseContrastDTO dto) throws SQLException;

    public Map batchImport(Map<Integer, List<List<Object>>> data) throws SQLException;

    public List<SelectBean> getMerchantSelectBean();
    
    /**
     * 获取组合商品信息详情
     */
    public GroupMerchandiseContrastDTO getDTODetail(Long id) throws SQLException;

    /**
     * 模糊查询店铺下拉框
     * @param shopName
     * @return
     */
	public List<MerchantShopRelMongo> getShopList(String shopName);


	public void batchDel(List<Long> ids) throws SQLException;

}
