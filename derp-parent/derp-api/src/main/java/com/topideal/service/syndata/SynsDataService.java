package com.topideal.service.syndata;

public interface SynsDataService {

	/**
	 * api密钥配置
	 */
	public void synsApiSecretConfig()throws Exception;;

	/**
	 * 同步原产国
	 * @return
	 * @throws Exception
	 */
	public void synsCountryOrigin()throws Exception;
	/**
	 * 同步客户
	 * @return
	 * @throws Exception
	 */
	public void synsCustomerInfo()throws Exception;
	/**
	 * 同步客户商家关系表
	 * @return
	 * @throws Exception
	 */
	public void synscustomerMerchantRel() throws Exception;
	/**
	 * 同步仓库
	 * @return
	 * @throws Exception
	 */
	public void synsDepotInfo()throws Exception;

	/**
	 * 同步商品表
	 * @return
	 * @throws Exception
	 */
	public void synsMerchandiseInfo()throws Exception;

	public void synsMerchandiseSchedule() throws Exception;

	public void synApiExternalConfig() throws Exception;

	/**
	 * 同步商家表
	 * @return
	 * @throws Exception
	 */
	public void synsMerchantInfo()throws Exception;
	/**
	 * 同步包装方式
	 * @return
	 * @throws Exception
	 */
	public void synsPackType()throws Exception;
	/**
	 * 同步产品表
	 * @return
	 * @throws Exception
	 */
	public void synsproductInfo()throws Exception;

	/**
	 * 同步品牌
	 * @throws Exception
	 */
	public void synsBrandInfo()throws Exception;

	/**
	 * 同步标准品牌
	 * @throws Exception
	 */
	public void synsBrandParent() throws Exception ;

	/**
	 * 同步汇率
	 * @return
	 * @throws Exception
	 */
	public void synExchangeRate() throws Exception;

	/**
	 * 同步标准条码
	 * @throws Exception
	 */
	public void synsCommbarcode() throws Exception;

	/**
	 * 同步库位货号
	 */
	//public void synsLocationMapping() throws Exception;
	/**
	 * 同步公司事业部
	 */
	public void synsMerchantBuRel() throws Exception;
	/**
	 * 邮件配置表
	 */
	public void synsEmailConfig() throws Exception;
	/**
	 * 商家店铺表
	 */
	public void synsMerchantShopRel() throws Exception;
	/**
	 * 加价比例配置表
	 */
	public void synsPurchaseCommission() throws Exception;
	/**
	 * 爬虫配置表
	 */
	public void synsReptileConfig() throws Exception;

	/**
	 * 单位表
	 */
    public void synsUnit() throws Exception;
    /**
     * 商家仓库关系
     * @throws Exception
     */
    public void synsdepotMerchantRel()throws Exception;
    /**
          * 母品牌
     * @throws Exception
     */
    public void synsBrandSuperior()throws Exception;
    /**
     * 销售价格管理
     * @throws Exception
     */
    public void synsCustomerSalePrice() throws Exception;
    /**
     * 采购价格管理
     * @throws Exception
     */
    public void synsSupplierMerchandisePrice() throws Exception;

    /**
     * 仓库关区关联
     * @throws Exception
     */
    public void synsDepotCustomsRel() throws Exception;


	/**
	 * 同步商家仓库事业部关联
	 */
	void synsMerchantDepotBuRel() throws Exception;
	/**
	 * 同步用户事业部关系表
	 * @throws Exception
	 */
	void synsUserBuRel() throws Exception;
	/**
	 * 同步商家事业部关系表
	 * @throws Exception
	 */
	void synsMerchantShopShipper() throws Exception;

	/**
	 * 同步商品关区关系表
	 * @throws Exception
	 */
	void synsMerchandiseCustomsRel() throws Exception;


	/**
	 * 同步部门关系表
	 * @throws Exception
	 */
	void synsDepartmentInfo() throws Exception;

	/**
	 * 同步事业部
	 * @throws Exception
	 */
	void synsBusinessUnit() throws Exception;
	/**
	 * 同步外仓备案商品表
	 * @throws Exception
	 */
	void synsMerchandiseExternalWarehouse() throws Exception;

	/**
	 * 同步税率表
	 *
	 * @throws Exception
	 */
	void sysTariffRateConfig() throws Exception;

	//void synsgoodsMongdb() throws Exception;

	/**
	 * 同步文件任务表
	 *
	 * @throws Exception
	 */
	//void synsFileTask() throws Exception;

	/**
	 * 同步用户公司关联表
	 */
    void synsUserMerchantRel() throws Exception;

	/**
	 * 同步商品仓库关系表
	 */
    void sysMerchandiseDepotRel() throws Exception;

	/**
	 * 用户信息表
	 */
    void sysUserInfo() throws Exception;

	/**
	 * 事业部库位类型配置表
	 */
	void sysBuStockLocationTypeConfig() throws Exception;

	void sysFixedCostPrice()throws Exception;
}
