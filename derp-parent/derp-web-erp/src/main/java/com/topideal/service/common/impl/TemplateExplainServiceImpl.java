package com.topideal.service.common.impl;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.dao.base.BrandDao;
import com.topideal.dao.base.CountryOriginDao;
import com.topideal.dao.base.PackTypeDao;
import com.topideal.dao.base.UnitInfoDao;
import com.topideal.dao.main.BusinessUnitDao;
import com.topideal.dao.main.CustomsAreaConfigDao;
import com.topideal.dao.main.DepotInfoDao;
import com.topideal.dao.main.MerchandiseCatDao;
import com.topideal.entity.dto.main.BusinessUnitDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.entity.vo.base.PackTypeModel;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.entity.vo.main.CustomsAreaConfigModel;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.service.common.TemplateExplainService;
import com.topideal.webapi.common.dto.TemplateExplainDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Wilson Lau
 * @Date: 2021/10/29 12:08
 * @Description: 模板说明ServiceImpl
 */
@Service
public class TemplateExplainServiceImpl implements TemplateExplainService {

    @Autowired
    private DepotInfoDao depotInfoDao;

    @Autowired
    private BusinessUnitDao businessUnitDao;

    @Autowired
    private CountryOriginDao countryOriginDao;

    @Autowired
    private MerchandiseCatDao merchandiseCatDao;

    @Autowired
    private UnitInfoDao unitInfoDao;

    @Autowired
    private CustomsAreaConfigDao customsAreaConfigDao;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private PackTypeDao packTypeDao;

    @Override
    public List<TemplateExplainDTO> listByDTO(TemplateExplainDTO dto) throws Exception {
        List<TemplateExplainDTO> templateExplainDTOS = new ArrayList<>();
        String type = dto.getType();
        // 仓库名称
        if(StringUtils.equals(DERP_SYS.TEMPLATE_DEPOT_CATEGORY_TYPE, type)) {
            DepotInfoModel depotInfoModel = new DepotInfoModel();
            depotInfoModel.setDepotCode(dto.getCode());
            depotInfoModel.setName(dto.getName());
            depotInfoModel.setStatus(DERP_SYS.DEPOTINFO_STATUS_1);
            List<DepotInfoModel> list = depotInfoDao.listByLike(depotInfoModel);
            templateExplainDTOS = convertTemplatExplainList(list, DepotInfoModel.class);
        }

        // 事业部
        if(StringUtils.equals(DERP_SYS.TEMPLATE_BUSINESS_UNIT_CATEGORY_TYPE, type)) {
            BusinessUnitDTO businessUnitDTO = new BusinessUnitDTO();
            businessUnitDTO.setCode(dto.getCode());
            businessUnitDTO.setName(dto.getName());
            List<BusinessUnitDTO> list = businessUnitDao.listDTOByLike(businessUnitDTO);
            templateExplainDTOS = convertTemplatExplainList(list, BusinessUnitDTO.class);
        }

        // 原产国
        if(StringUtils.equals(DERP_SYS.TEMPLATE_COUNTRY_ORIGIN_CATEGORY_TYPE, type)) {
            CountryOriginModel countryOriginModel = new CountryOriginModel();
            countryOriginModel.setCode(dto.getCode());
            countryOriginModel.setName(dto.getName());
            List<CountryOriginModel> list = countryOriginDao.listByLike(countryOriginModel);
            templateExplainDTOS = convertTemplatExplainList(list, CountryOriginModel.class);
        }

        // 二级类目
        if(StringUtils.equals(DERP_SYS.TEMPLATE_MERCHANDISE_CAT_CATEGORY_TYPE, type)) {
            MerchandiseCatModel catModel = new MerchandiseCatModel();
            catModel.setCode(dto.getCode());
            catModel.setName(dto.getName());
            catModel.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
            List<MerchandiseCatModel> list = merchandiseCatDao.listByLike(catModel);
            templateExplainDTOS = convertTemplatExplainList(list, MerchandiseCatModel.class);
        }

        // 计量单位
        if(StringUtils.equals(DERP_SYS.TEMPLATE_UNIT_CATEGORY_TYPE, type)) {
            UnitInfoModel unitInfoModel = new UnitInfoModel();
            unitInfoModel.setCode(dto.getCode());
            unitInfoModel.setName(dto.getName());
            List<UnitInfoModel> list = unitInfoDao.listByLike(unitInfoModel);
            templateExplainDTOS = convertTemplatExplainList(list, UnitInfoModel.class);
        }

        // 关区名称
        if(StringUtils.equals(DERP_SYS.TEMPLATE_CUSTOMSAREA_CATEGORY_TYPE, type)) {
            CustomsAreaConfigModel customsAreaConfigModel = new CustomsAreaConfigModel();
            customsAreaConfigModel.setCode(dto.getCode());
            customsAreaConfigModel.setName(dto.getName());
            List<CustomsAreaConfigModel> list = customsAreaConfigDao.listByLike(customsAreaConfigModel);
            templateExplainDTOS = convertTemplatExplainList(list, CustomsAreaConfigModel.class);
        }

        // 商品品牌
        if(StringUtils.equals(DERP_SYS.TEMPLATE_BRAND_TYPE, type)) {
            BrandModel brandModel = new BrandModel();
            brandModel.setName(dto.getName());
            List<BrandModel> list = brandDao.listByLike(brandModel);
            templateExplainDTOS = convertTemplatExplainList(list, BrandModel.class);
        }

        // 包装方式
        if(StringUtils.equals(DERP_SYS.TEMPLATE_PACKTYPE_CATEGORY_TYPE, type)) {
            PackTypeModel packTypeModel = new PackTypeModel();
            packTypeModel.setCode(dto.getCode());
            packTypeModel.setName(dto.getName());
            List<PackTypeModel> list = packTypeDao.listByLike(packTypeModel);
            templateExplainDTOS = convertTemplatExplainList(list, PackTypeModel.class);
        }

        return templateExplainDTOS;
    }

    /**
     *
     * @param list 原数据
     * @param clazz
     * @throws Exception
     */
    private List<TemplateExplainDTO> convertTemplatExplainList(List list, Class clazz) throws Exception {
        List<TemplateExplainDTO> resultList = new LinkedList<>();
        Method[] methods = clazz.getMethods();

        AtomicInteger num = new AtomicInteger();
        list.stream().forEach(entity -> {
            try {
                TemplateExplainDTO templateExplainDTO = new TemplateExplainDTO();
                for (Method method : methods) {
                    if(clazz == DepotInfoModel.class && "getDepotCode".equals(method.getName())) {
                        String code = (String) method.invoke(entity);
                        templateExplainDTO.setCode(code);
                    }else if(clazz == MerchandiseCatModel.class && "getSysCode".equals(method.getName())){
                        String code = (String) method.invoke(entity);
                        templateExplainDTO.setCode(code);
                    }
                    else if(clazz != DepotInfoModel.class && clazz != MerchandiseCatModel.class && "getCode".equals(method.getName())){
                        String code = (String) method.invoke(entity);
                        templateExplainDTO.setCode(code);
                    }

                    if("getName".equals(method.getName())) {
                        String name = (String)method.invoke(entity);
                        templateExplainDTO.setName(name);
                    }
                }
                templateExplainDTO.setNo(num.incrementAndGet());
                resultList.add(templateExplainDTO);
            } catch (Exception e) {
                throw new RuntimeException("转化TemplatExplain失败", e);
            }
        });
        return resultList;
    }
}
