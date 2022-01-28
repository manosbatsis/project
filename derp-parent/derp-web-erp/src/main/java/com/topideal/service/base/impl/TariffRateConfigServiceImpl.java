package com.topideal.service.base.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.dao.main.CustomerMerchantRelDao;
import com.topideal.dao.main.TariffRateConfigDao;
import com.topideal.entity.dto.base.TariffRateConfigDTO;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.entity.vo.main.TariffRateConfigModel;
import com.topideal.service.base.TariffRateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TariffRateConfigServiceImpl  implements TariffRateConfigService {
    @Autowired
    TariffRateConfigDao tariffRateConfigDao;
    @Autowired
    CustomerMerchantRelDao customerMerchantRelDao;

    @Override
    public TariffRateConfigDTO getListByPage(TariffRateConfigDTO model) throws SQLException {
        return tariffRateConfigDao.getListByPage(model);
    }

    @Override
    public Map<String, Object> saveTariffRate(TariffRateConfigModel model) throws SQLException {
        Map<String, Object> resultMap =new HashMap<String, Object>();
        if(model.getRate()==null){
            resultMap.put("code", "01");
            resultMap.put("message", "税率不能为空");
            return resultMap;
        }
        //判断税率是否相同
        TariffRateConfigModel modelSearch=new TariffRateConfigModel();
        modelSearch.setRate(model.getRate());
        modelSearch= tariffRateConfigDao.searchByModel(modelSearch);
        if(modelSearch!=null){
            resultMap.put("code", "01");
            resultMap.put("message", "税率值已经存在");
            return resultMap;
        }
        long id= tariffRateConfigDao.save(model);
        if(id>0){
            resultMap.put("code", "00");
            resultMap.put("message", "保存成功！");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> delReptile(String ids) throws SQLException {
        Map<String, Object> resultMap =new HashMap<String, Object>();
        //校验id是否正确
        boolean isRight = StrUtils.validateIds(ids);
        if(!isRight){
            //输入信息不完整
            resultMap.put("code","01");
            resultMap.put("message","数据不能为空");
            return resultMap;//数据为空
        }
        List list = StrUtils.parseIds(ids);
        for(int i=0;i<list.size();i++){
            //查询是否被客户或供应商信息关联
            CustomerMerchantRelModel model=new CustomerMerchantRelModel();
            model.setRateId((long)list.get(i));
            List<CustomerMerchantRelModel> listModel=customerMerchantRelDao.list(model);
            if(listModel.size()>0){
                resultMap.put("code","01");
                resultMap.put("message",listModel.get(0).getRate()+"该税率被供应商或客户信息关联，不能删除");
                return resultMap;
            }
        }
        //删除税率配置
        for (int i=0;i<list.size();i++){
            tariffRateConfigDao.delete(list);
        }
        resultMap.put("code","00");
        resultMap.put("message", "删除成功！");
        return resultMap;
    }

	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		
		List<TariffRateConfigModel> list = tariffRateConfigDao.list(new TariffRateConfigModel());
		List<SelectBean> selectBeanList = new ArrayList<SelectBean>() ;
		
		for (TariffRateConfigModel tariffRateConfigModel : list) {
			
			SelectBean select = new SelectBean() ;
			
			if(tariffRateConfigModel.getRate() != null) {
				select.setLabel(tariffRateConfigModel.getRate().toString());
			}
			
			select.setValue(tariffRateConfigModel.getId().toString());
			
			selectBeanList.add(select) ;
		}
		
		return selectBeanList;
	}
	/**
     * 获取所有的税率配置
     */
	@Override
	public List<TariffRateConfigModel> getTariffRateConfigList(TariffRateConfigModel model) throws SQLException {
		return tariffRateConfigDao.list(model);
	}

}
