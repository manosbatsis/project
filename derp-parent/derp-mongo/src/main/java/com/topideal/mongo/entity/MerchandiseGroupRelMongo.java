package com.topideal.mongo.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品组合关系表
 * @author lian_
 *
 */
public class MerchandiseGroupRelMongo {

		// 组合id(对应的商品id)
		private Long groupGoodsId;
		// 商品id(单品id)
		private Long goodsId;
		// 单品价格
		private BigDecimal price;
		// 单品数量
		private Integer num;
		// 创建人
		private Long creater;
		// id
		private Long merchandiseGroupId;


		/* groupGoodsId get 方法 */
		public Long getGroupGoodsId() {
			return groupGoodsId;
		}

		/* groupGoodsId set 方法 */
		public void setGroupGoodsId(Long groupGoodsId) {
			this.groupGoodsId = groupGoodsId;
		}

		/* goodsId get 方法 */
		public Long getGoodsId() {
			return goodsId;
		}

		/* goodsId set 方法 */
		public void setGoodsId(Long goodsId) {
			this.goodsId = goodsId;
		}

		/* price get 方法 */
		public BigDecimal getPrice() {
			return price;
		}

		/* price set 方法 */
		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		/* num get 方法 */
		public Integer getNum() {
			return num;
		}

		/* num set 方法 */
		public void setNum(Integer num) {
			this.num = num;
		}

		/* creater get 方法 */
		public Long getCreater() {
			return creater;
		}

		/* creater set 方法 */
		public void setCreater(Long creater) {
			this.creater = creater;
		}

		/* merchandiseGroupId get 方法 */
		public Long getMerchandiseGroupId() {
			return merchandiseGroupId;
		}

		/* merchandiseGroupId set 方法 */
		public void setMerchandiseGroupId(Long merchandiseGroupId) {
			this.merchandiseGroupId = merchandiseGroupId;
		}
}
