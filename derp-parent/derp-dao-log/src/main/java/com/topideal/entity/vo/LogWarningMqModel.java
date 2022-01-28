package com.topideal.entity.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * MQ日志预警
 * @author zhanghx
 */
public class LogWarningMqModel extends PageModel implements Serializable {

	// 预警等级（1-漏洞，0-次要）
	private String level;
	// 创建时间
	private Timestamp createTime;
	// 模块编码
	private String modelCode;
	// 模块描述
	private String model;
	// 备注（描述原因）
	private String remark;
	// id
	private Long id;
	// 消费时间
	private Timestamp consumeDate;
	// 关键字
	private String keyword;
	// 埋点
	private String point;
	// 类型（用于冻结/释放库存）0-冻结 1-释放冻结
	private String type;

	// 拓展字段
	// 消费时间段开头
	private String consumeStartDate;
	// 消费时间段结束
	private String consumeEndDate;
	// 时间
	private String month;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	/* type get 方法 */
	public String getType() {
		return type;
	}

	/* type set 方法 */
	public void setType(String type) {
		this.type = type;
	}

	public String getConsumeStartDate() {
		return consumeStartDate;
	}

	public void setConsumeStartDate(String consumeStartDate) {
		this.consumeStartDate = consumeStartDate;
	}

	public String getConsumeEndDate() {
		return consumeEndDate;
	}

	public void setConsumeEndDate(String consumeEndDate) {
		this.consumeEndDate = consumeEndDate;
	}

	/* level get 方法 */
	public String getLevel() {
		return level;
	}

	/* level set 方法 */
	public void setLevel(String level) {
		this.level = level;
	}

	/* createTime get 方法 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/* createTime set 方法 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/* modelCode get 方法 */
	public String getModelCode() {
		return modelCode;
	}

	/* modelCode set 方法 */
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	/* model get 方法 */
	public String getModel() {
		return model;
	}

	/* model set 方法 */
	public void setModel(String model) {
		this.model = model;
	}

	/* remark get 方法 */
	public String getRemark() {
		return remark;
	}

	/* remark set 方法 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* consumeDate get 方法 */
	public Timestamp getConsumeDate() {
		return consumeDate;
	}

	/* consumeDate set 方法 */
	public void setConsumeDate(Timestamp consumeDate) {
		this.consumeDate = consumeDate;
	}

	/* keyword get 方法 */
	public String getKeyword() {
		return keyword;
	}

	/* keyword set 方法 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/* point get 方法 */
	public String getPoint() {
		return point;
	}

	/* point set 方法 */
	public void setPoint(String point) {
		this.point = point;
	}

}
