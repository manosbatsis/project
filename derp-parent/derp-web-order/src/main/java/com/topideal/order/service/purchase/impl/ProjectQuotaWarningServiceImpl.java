package com.topideal.order.service.purchase.impl;

import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.dao.purchase.ProjectQuotaWarningDao;
import com.topideal.dao.purchase.ProjectQuotaWarningItemDao;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningExportDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningItemDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaWarningModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.purchase.ProjectQuotaWarningService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectQuotaWarningServiceImpl implements ProjectQuotaWarningService {

    @Autowired
    private ProjectQuotaWarningDao projectQuotaWarningDao ;
    @Autowired
    private ProjectQuotaWarningItemDao projectQuotaWarningItemDao ;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao ;

    @Override
    public ProjectQuotaWarningDTO getListByPage(ProjectQuotaWarningDTO dto, User user) {

        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }

            dto.setBuIds(buIds);

        }

        dto = projectQuotaWarningDao.getListByPage(dto);

        List<ProjectQuotaWarningDTO> dtoList = (List<ProjectQuotaWarningDTO>)dto.getList() ;

        for (ProjectQuotaWarningDTO tempDto: dtoList) {

            BigDecimal projectQuota = tempDto.getProjectQuota();
            if(projectQuota != null){
                tempDto.setProjectQuotaStr(StrUtils.longFormatString(projectQuota.longValue()));
            }

            BigDecimal purchaseAmount = tempDto.getPurchaseAmount();
            if(purchaseAmount != null){
                tempDto.setPurchaseAmountStr(StrUtils.doubleFormatStringByFour(purchaseAmount.doubleValue()));
            }

            BigDecimal salesCollectedAmount = tempDto.getSalesCollectedAmount();
            if(salesCollectedAmount != null){
                tempDto.setSalesCollectedAmountStr(StrUtils.doubleFormatStringByFour(salesCollectedAmount.doubleValue()));
            }

            BigDecimal availableAmount = tempDto.getAvailableAmount();
            if(availableAmount != null){
                tempDto.setAvailableAmountStr(StrUtils.doubleFormatStringByFour(availableAmount.doubleValue()));
            }
            BigDecimal addPaymentAmount = tempDto.getAddPaymentAmount();
            if(addPaymentAmount != null){
                tempDto.setAddPaymentAmountStr(StrUtils.doubleFormatStringByFour(addPaymentAmount.doubleValue()));
            }

        }

        dto.setList(dtoList);

        return dto;
    }

    @Override
    public ProjectQuotaWarningDTO getProjectQuotaWarningById(Long id) throws SQLException {

        ProjectQuotaWarningModel projectQuotaWarningModel = projectQuotaWarningDao.searchById(id);

        ProjectQuotaWarningDTO dto = new ProjectQuotaWarningDTO() ;

        BeanUtils.copyProperties(projectQuotaWarningModel, dto);

        BigDecimal projectQuota = dto.getProjectQuota();
        if(projectQuota != null){
            dto.setProjectQuotaStr(StrUtils.longFormatString(projectQuota.longValue()));
        }

        BigDecimal purchaseAmount = dto.getPurchaseAmount();
        if(purchaseAmount != null){
            dto.setPurchaseAmountStr(StrUtils.longFormatString(purchaseAmount.longValue()));
        }

        BigDecimal salesCollectedAmount = dto.getSalesCollectedAmount();
        if(salesCollectedAmount != null){
            dto.setSalesCollectedAmountStr(StrUtils.longFormatString(salesCollectedAmount.longValue()));
        }

        BigDecimal availableAmount = dto.getAvailableAmount();
        if(availableAmount != null){
            dto.setAvailableAmountStr(StrUtils.longFormatString(availableAmount.longValue()));
        }

        return dto;
    }

    @Override
    public ProjectQuotaWarningItemDTO getItemListByPage(ProjectQuotaWarningItemDTO dto, User user) {
        return projectQuotaWarningItemDao.getItemListByPage(dto);
    }

    @Override
    public List<ProjectQuotaWarningDTO> exportProjectQuotaWarning(ProjectQuotaWarningDTO dto) {
        return projectQuotaWarningDao.exportProjectQuotaWarning(dto);
    }

    @Override
    public List<ProjectQuotaWarningExportDTO> exportProjectQuotaWarningDetail(ProjectQuotaWarningItemDTO dto) {
        if(dto == null || dto.getWaringId() == null) {
            throw new RuntimeException("waringId 不能为空");
        }
        List<ProjectQuotaWarningExportDTO> collect = new ArrayList<>();
        try {
            ProjectQuotaWarningModel projectQuotaWarningModel = projectQuotaWarningDao.searchById(dto.getWaringId());
            List<ProjectQuotaWarningItemDTO> projectQuotaWarningItemDTOS = projectQuotaWarningItemDao.exportProjectQuotaWarningDetail(dto);
            collect = projectQuotaWarningItemDTOS.stream().map(entity -> {
                ProjectQuotaWarningExportDTO exportDTO = new ProjectQuotaWarningExportDTO();
                BeanUtils.copyProperties(projectQuotaWarningModel, exportDTO);
                BeanUtils.copyProperties(entity, exportDTO);

                exportDTO.setQuotaCurrency(projectQuotaWarningModel.getCurrency());
                BigDecimal projectQuota = exportDTO.getProjectQuota();
                if(projectQuota != null){
                    exportDTO.setProjectQuotaStr(StrUtils.longFormatString(projectQuota.longValue()));
                }

                BigDecimal purchaseAmount = exportDTO.getPurchaseAmount();
                if(purchaseAmount != null){
                    exportDTO.setPurchaseAmountStr(StrUtils.longFormatString(purchaseAmount.longValue()));
                }

                BigDecimal salesCollectedAmount = exportDTO.getSalesCollectedAmount();
                if(salesCollectedAmount != null){
                    exportDTO.setSalesCollectedAmountStr(StrUtils.longFormatString(salesCollectedAmount.longValue()));
                }

                BigDecimal availableAmount = exportDTO.getAvailableAmount();
                if(availableAmount != null){
                    exportDTO.setAvailableAmountStr(StrUtils.longFormatString(availableAmount.longValue()));
                }

                return exportDTO;
            }).collect(Collectors.toList());
        }catch (Exception e) {
            throw new RuntimeException("查询项目额度预警明细失败");
        }
        return collect;
    }
}
