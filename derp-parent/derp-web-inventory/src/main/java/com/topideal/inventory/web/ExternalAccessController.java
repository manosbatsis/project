package com.topideal.inventory.web;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.BuInventoryDTO;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.inventory.service.BuInventoryService;
import com.topideal.inventory.service.InventoryGoodsSnapshotService;
import com.topideal.inventory.service.MonthlyAccountService;
import com.topideal.inventory.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 首页 提供给外部访问的类
 **/
@Controller
@RequestMapping("/external")
public class ExternalAccessController {

    @Autowired
    private MonthlyAccountService monthlyAccountService;
    @Autowired
    private InventoryGoodsSnapshotService inventoryGoodsSnapshotService;
    @Autowired
    private BuInventoryService buInventoryService;
    /**
     * 月库存结转中结转状态为未结转的仓库月结记录
     */
    @RequestMapping("/getPendingCarryForward.asyn")
    @ResponseBody
    private ViewResponseBean getPendingCarryForward() {
        List<MonthlyAccountModel> list = null;
        try {
            MonthlyAccountModel monthlyAccountModel = new MonthlyAccountModel();
            User user = ShiroUtils.getUser();
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                monthlyAccountModel.setMerchantId(user.getMerchantId());
            }
            monthlyAccountModel.setState(DERP_INVENTORY.MONTHLYACCOUNT_STATE_1);
            list = monthlyAccountService.listByMonthlyAccount(monthlyAccountModel);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(list);
    }

    /**
     * 事业部库存—汇总该公司主体下各事业部库存总量占所有仓库库存总量的比例
     * @Param
     * @return
     */
    @RequestMapping("/countBuInventory.asyn")
    @ResponseBody
    private ViewResponseBean countBuInventory() {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            BuInventoryDTO dto = new BuInventoryDTO();
            User user = ShiroUtils.getUser();
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                Long merchantId = user.getMerchantId();
                dto.setMerchantId(merchantId);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String month = sdf.format(new Date());
            dto.setMonth(month);
            result = buInventoryService.countByMonthAndMerchant(dto);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 事业部库存仪表盘—汇总该公司主体下各事业部库存总量占各仓库库存总量的比例
     * @Param
     * @return
     */
    @RequestMapping("/countBuInventoryRate.asyn")
    @ResponseBody
    private ViewResponseBean countBuInventoryRate(Long buId) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            BuInventoryDTO dto = new BuInventoryDTO();
            User user = ShiroUtils.getUser();
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                Long merchantId = user.getMerchantId();
                dto.setMerchantId(merchantId);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String month = sdf.format(new Date());
            dto.setMonth(month);
            dto.setBuId(buId);
            result = buInventoryService.countBuInventoryRate(dto);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

}
