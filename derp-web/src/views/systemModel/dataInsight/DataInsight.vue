<template>
  <div class="container" ref="container">
    <div class="container__left">
      <!-- 事业群销售达成 -->
      <el-card class="box-card" style="margin-bottom: 20px" ref="achieve">
        <div
          slot="header"
          class="fs-14 clr-II"
          style="display: flex; justify-content: space-between"
        >
          <div style="display: flex; align-items: center">
            <h2 style="margin-right: 16px">部门销售达成</h2>
            <span>统计月份：{{ achieve.month }}</span>
          </div>
          <div>
            <span>币种：港币</span>
            <span style="margin-left: 6px">统计月份：</span>
            <el-date-picker
              v-model="achieve.month"
              type="month"
              placeholder="选择日期"
              value-format="yyyy-MM"
              @change="fetchAchieveChartData"
              :clearable="false"
            />
          </div>
        </div>
        <div>
          <!-- tabs部分 -->
          <div class="tab" style="margin-bottom: 20px">
            <ul class="tab__content">
              <li
                v-for="(item, index) in achieve.tabList"
                :key="item.id"
                :class="{ active: index === achieve.currentIdx }"
                @click="tabClick('achieve', index, item.id)"
              >
                {{ item.name }}
              </li>
            </ul>
            <div class="tab__export" @click="exportList('achieve')">
              <i class="el-icon-download" />
              导 出
            </div>
          </div>
          <!-- tabs部分 end -->
          <div
            style="
              display: flex;
              justify-content: space-between;
              position: relative;
            "
          >
            <!-- <ul class="right__tab" v-if="!achieve.buId">
              <li v-for="(item, index) in rightTabList"
                  :key="item"
                  :class="{'active': index === achieve.rightTabIdx}"
                  @click="achieveTabClick(index)">{{item}}</li>
            </ul> -->
            <!-- 柱形图 -->
            <div
              ref="achieveColumn"
              :style="{ width: '100%', height: '400px' }"
            ></div>
            <!-- 柱形图 end -->
            <!-- 饼状图 -->
            <!-- <div ref="achievePie" :style="{ width: '40%', height: '400px', marginRight: '6%' }"></div> -->
            <!-- 饼状图 end -->
          </div>
        </div>
      </el-card>
      <!-- 事业群销售达成 end -->
      <!-- 年度进销存分析 -->
      <el-card class="box-card" style="margin-bottom: 20px" ref="annual">
        <div
          slot="header"
          class="fs-14 clr-II"
          style="display: flex; justify-content: space-between"
        >
          <div style="display: flex; align-items: center">
            <h2 style="margin-right: 16px">年度进销存分析</h2>
            <span>统计月份：{{ annual.month }}</span>
          </div>
          <div>
            <span>币种：人民币</span>
            <span style="margin-left: 6px">统计时间：</span>
            <el-date-picker
              v-model="annual.month"
              type="year"
              placeholder="选择日期"
              value-format="yyyy"
              :picker-options="pickerOptions"
              @change="fetchAnnualChartData"
              :clearable="false"
            />
          </div>
        </div>
        <div>
          <!-- tabs部分 -->
          <div class="tab" style="margin-bottom: 20px">
            <ul class="tab__content">
              <li
                v-for="(item, index) in annual.tabList"
                :key="item.id"
                :class="{ active: index === annual.currentIdx }"
                @click="tabClick('annual', index, item.id)"
              >
                {{ item.name }}
              </li>
            </ul>
            <div class="tab__export" @click="exportList('annual')">
              <i class="el-icon-download" />
              导 出
            </div>
          </div>
          <el-row :gutter="16">
            <el-col :span="16">
              <el-row :class="{ 'brand-close': !annual.isExpand }">
                <el-checkbox
                  :indeterminate="annual.isIndeterminate"
                  style="
                    height: 30px;
                    line-height: 30px;
                    display: inline;
                    margin-right: 10px;
                  "
                  v-model="annual.checkAll"
                  @change="($event) => handleCheckAllChange('annual', $event)"
                >
                  全部品牌
                </el-checkbox>
                <el-checkbox-group
                  v-model="annual.brand"
                  @change="($event) => changeCheckbox('annual', $event)"
                  style="display: inline"
                >
                  <el-checkbox
                    :label="item.brandId"
                    v-for="item in annual.brandList"
                    :key="item.brandId"
                    style="height: 30px; line-height: 30px"
                  >
                    {{ item.brandName }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-row>
            </el-col>
            <el-col :span="2">
              <el-button
                type="text"
                @click="annual.isExpand = !annual.isExpand"
              >
                {{ !annual.isExpand ? '展开' : '收起' }}
                <i class="el-icon-arrow-down" v-if="!annual.isExpand" />
                <i class="el-icon-arrow-up" v-else />
              </el-button>
            </el-col>
            <el-col :span="4" :offset="2">
              <el-radio-group
                v-model="annual.isParentBrand"
                @change="isParentBrandChange"
              >
                <el-radio
                  v-for="item in annual.brandTypeList"
                  :label="item.label"
                  :key="item.label"
                >
                  {{ item.name }}
                </el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <!-- tabs部分 end -->
          <div
            style="
              display: flex;
              justify-content: space-between;
              position: relative;
            "
          >
            <!-- 柱形图 -->
            <div
              ref="annualColumn"
              :style="{ width: '60%', height: '400px' }"
            ></div>
            <!-- 柱形图 end -->
            <!-- 饼状图 -->
            <div
              ref="annualPie"
              :style="{ width: '40%', height: '400px' }"
            ></div>
            <!-- 饼状图 end -->
          </div>
        </div>
      </el-card>
      <!-- 年度进销存分析 end -->
      <!-- 品牌销量TOP8分析 -->
      <el-card class="box-card" style="margin-bottom: 20px" ref="brand">
        <div
          slot="header"
          class="fs-14 clr-II"
          style="display: flex; justify-content: space-between"
        >
          <div style="display: flex; align-items: center">
            <h2 style="margin-right: 16px">品牌销量TOP8分析</h2>
            <span>统计月份：{{ brand.month[0] }} ~ {{ brand.month[1] }}</span>
          </div>
          <div>
            <span>币种：人民币</span>
            <span style="margin-left: 6px">统计月份：</span>
            <el-date-picker
              v-model="brand.month"
              type="daterange"
              align="right"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              :clearable="false"
              @change="fetchBrandChartData"
            ></el-date-picker>
          </div>
        </div>
        <div>
          <!-- tabs部分 -->
          <div class="tab" style="margin-bottom: 20px">
            <ul class="tab__content">
              <li
                v-for="(item, index) in brand.tabList"
                :key="item.id"
                :class="{ active: index === brand.currentIdx }"
                @click="tabClick('brand', index, item.id)"
              >
                {{ item.name }}
              </li>
            </ul>
            <div class="tab__export" @click="exportList('brand')">
              <i class="el-icon-download" />
              导 出
            </div>
          </div>
          <div style="display: flex">
            <el-radio-group
              v-model="brand.isParentBrand"
              @change="fetchBrandChartData"
            >
              <el-radio
                v-for="item in brand.brandList"
                :label="item.label"
                :key="item.label"
              >
                {{ item.name }}
              </el-radio>
            </el-radio-group>
          </div>
          <!-- tabs部分 end -->
          <div
            style="
              display: flex;
              justify-content: space-between;
              position: relative;
            "
          >
            <!-- 柱形图 -->
            <div
              ref="brandColumn"
              :style="{ width: '65%', height: '340px' }"
            ></div>
            <!-- 柱形图 end -->
            <!-- 饼状图 -->
            <div
              ref="brandPie"
              :style="{ width: '35%', height: '340px' }"
            ></div>
            <!-- 饼状图 end -->
          </div>
        </div>
      </el-card>
      <!-- 品牌销量TOP8分析 end -->
      <!-- 客户销量TOP8分析 -->
      <el-card class="box-card" style="margin-bottom: 20px" ref="customer">
        <div
          slot="header"
          class="fs-14 clr-II"
          style="display: flex; justify-content: space-between"
        >
          <div style="display: flex; align-items: center">
            <h2 style="margin-right: 16px">客户销量TOP8分析</h2>
            <span>
              统计月份：{{ customer.month[0] }} ~ {{ customer.month[1] }}
            </span>
          </div>
          <div>
            <span>币种：人民币</span>
            <span style="margin-left: 6px">统计月份：</span>
            <el-date-picker
              v-model="customer.month"
              type="daterange"
              align="right"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              :clearable="false"
              @change="fetchCustomerChartData"
            ></el-date-picker>
          </div>
        </div>
        <div>
          <!-- tabs部分 -->
          <div class="tab" style="margin-bottom: 20px">
            <ul class="tab__content">
              <li
                v-for="(item, index) in customer.tabList"
                :key="item.id"
                :class="{ active: index === customer.currentIdx }"
                @click="tabClick('customer', index, item.id)"
              >
                {{ item.name }}
              </li>
            </ul>
            <div class="tab__export" @click="exportList('customer')">
              <i class="el-icon-download" />
              导 出
            </div>
          </div>
          <div>
            <el-row :gutter="16">
              <el-col :span="3" style="height: 30px; line-height: 30px">
                <el-checkbox
                  :indeterminate="customer.isIndeterminate"
                  v-model="customer.checkAll"
                  @change="($event) => handleCheckAllChange('customer', $event)"
                >
                  统计全部
                </el-checkbox>
              </el-col>
              <el-checkbox-group
                v-model="customer.channel"
                @change="($event) => changeCheckbox('customer', $event)"
              >
                <el-col
                  :span="3"
                  v-for="item in customer.channelList"
                  :key="item.label"
                  style="height: 30px; line-height: 30px"
                >
                  <el-checkbox :label="item.label">{{ item.name }}</el-checkbox>
                </el-col>
              </el-checkbox-group>
            </el-row>
          </div>
          <!-- tabs部分 end -->
          <div
            style="
              display: flex;
              justify-content: space-between;
              position: relative;
            "
          >
            <!-- 柱形图 -->
            <div
              ref="customerColumn"
              :style="{ width: '65%', height: '340px' }"
            ></div>
            <!-- 柱形图 end -->
            <!-- 饼状图 -->
            <div
              ref="customerPie"
              :style="{ width: '35%', height: '340px' }"
            ></div>
            <!-- 饼状图 end -->
          </div>
        </div>
      </el-card>
      <!-- 客户销量TOP8分析 end -->
      <!-- 库存量分析 -->
      <el-card class="box-card" style="margin-bottom: 20px" ref="stock">
        <div
          slot="header"
          class="fs-14 clr-II"
          style="display: flex; justify-content: space-between"
        >
          <div style="display: flex; align-items: center">
            <h2 style="margin-right: 16px">库存量分析</h2>
            <span>统计月份：{{ stock.month }}</span>
          </div>
          <div>
            <span>币种：人民币</span>
            <span style="margin-left: 6px">统计月份：</span>
            <el-date-picker
              v-model="stock.month"
              type="month"
              placeholder="选择日期"
              value-format="yyyy-MM"
              :clearable="false"
              @change="fetchStockChartData"
            />
          </div>
        </div>
        <div>
          <!-- tabs部分 -->
          <div class="tab" style="margin-bottom: 20px">
            <ul class="tab__content">
              <li
                v-for="(item, index) in stock.tabList"
                :key="item.id"
                :class="{ active: index === stock.currentIdx }"
                @click="tabClick('stock', index, item.id)"
              >
                {{ item.name }}
              </li>
            </ul>
            <div class="tab__export" @click="exportList('stock')">
              <i class="el-icon-download" />
              导 出
            </div>
          </div>
          <div class="flex-b-c">
            <div style="display: flex">
              <el-radio-group
                v-model="stock.channel"
                @change="fetchStockChartData"
              >
                <el-radio
                  v-for="item in stock.channelList"
                  :label="item.label"
                  :key="item.label"
                >
                  {{ item.name }}
                </el-radio>
              </el-radio-group>
            </div>
          </div>
          <!-- tabs部分 end -->
          <div
            style="
              display: flex;
              justify-content: space-between;
              position: relative;
            "
          >
            <!-- 柱形图 -->
            <div
              ref="stockColumn"
              :style="{ width: '60%', height: '340px' }"
            ></div>
            <!-- 柱形图 end -->
            <!-- 饼状图 -->
            <div
              ref="stockPie"
              :style="{ width: '40%', height: '340px' }"
            ></div>
            <!-- 饼状图 end -->
          </div>
        </div>
      </el-card>
      <!-- 库存量分析 end -->
      <!-- 商品在库天数分析 -->
      <el-card class="box-card" style="margin-bottom: 20px" ref="wares">
        <div
          slot="header"
          class="fs-14 clr-II"
          style="display: flex; justify-content: space-between"
        >
          <div style="display: flex; align-items: center">
            <h2 style="margin-right: 16px">商品在库天数分析</h2>
            <span>统计月份：{{ wares.month }}</span>
          </div>
          <div>
            <span>币种：人民币</span>
            <span style="margin-left: 6px">统计月份：</span>
            <el-date-picker
              v-model="wares.month"
              type="month"
              placeholder="选择日期"
              :clearable="false"
              value-format="yyyy-MM"
              @change="
                () => {
                  fetchWaresChartData()
                  fetchWaresBrandChartData()
                }
              "
            />
          </div>
        </div>
        <div>
          <!-- tabs部分 -->
          <div class="tab" style="margin-bottom: 20px">
            <ul class="tab__content">
              <li
                v-for="(item, index) in wares.tabList"
                :key="item.id"
                :class="{ active: index === wares.currentIdx }"
                @click="tabClick('wares', index, item.id)"
              >
                {{ item.name }}
              </li>
            </ul>
            <div class="tab__export" @click="exportList('wares')">
              <i class="el-icon-download" />
              导 出
            </div>
          </div>
          <!-- tabs部分 end -->
          <div
            style="
              display: flex;
              justify-content: space-between;
              position: relative;
            "
          >
            <!-- 柱形图 -->
            <div
              ref="waresColumn"
              :style="{ width: '60%', height: '400px' }"
            ></div>
            <!-- 柱形图 end -->
            <!-- 饼状图 -->
            <div
              ref="waresPie"
              :style="{ width: '40%', height: '400px' }"
            ></div>
            <!-- 饼状图 end -->
          </div>
          <h3 style="margin: 30px 0 10px 0">品牌在库天数TOP8分析</h3>
          <div class="flex-b-c">
            <div style="display: flex; align-items: center">
              <el-radio-group
                v-model="wares.isParentBrand"
                @change="fetchWaresBrandChartData"
              >
                <el-radio
                  v-for="item in wares.brandList"
                  :label="item.label"
                  :key="item.label"
                >
                  {{ item.name }}
                </el-radio>
              </el-radio-group>
              <div style="margin-left: 26px">
                <span>在库天数</span>
                <el-select
                  v-model="wares.inWarehouseRange"
                  placeholder="请选择"
                  filterable
                  clearable
                  style="margin-left: 10px"
                  @change="fetchWaresBrandChartData"
                >
                  <el-option
                    v-for="item in wares.warehouseRangeList"
                    :key="item"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </div>
            </div>
          </div>
          <div
            style="
              display: flex;
              justify-content: space-between;
              position: relative;
            "
          >
            <!-- 柱形图 -->
            <div
              ref="waresBrandColumn"
              :style="{ width: '60%', height: '400px' }"
            ></div>
            <!-- 柱形图 end -->
            <!-- 饼状图 -->
            <div
              ref="waresBrandPie"
              :style="{ width: '40%', height: '400px' }"
            ></div>
            <!-- 饼状图 end -->
          </div>
        </div>
      </el-card>
      <!-- 商品在库天数分析 end -->
      <!-- 仓库滞销预警 -->
      <el-card class="box-card" style="margin-bottom: 20px" ref="delay">
        <div
          slot="header"
          class="fs-14 clr-II"
          style="display: flex; justify-content: space-between"
        >
          <div style="display: flex; align-items: center">
            <h2 style="margin-right: 16px">仓库滞销预警</h2>
            <span>统计月份：{{ delay.month[0] }} ~ {{ delay.month[1] }}</span>
          </div>
          <div>
            <span>币种：人民币</span>
            <span style="margin-left: 6px">统计月份：</span>
            <el-date-picker
              v-model="delay.month"
              type="monthrange"
              align="right"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM"
              :clearable="false"
              @change="fetchDelayChartData"
            ></el-date-picker>
          </div>
        </div>
        <div>
          <!-- tabs部分 -->
          <div class="tab" style="margin-bottom: 20px">
            <div style="flex: 1">
              <!-- 公司 -->
              <el-row>
                <el-col :span="20">
                  <el-row :class="{ 'brand-close': !delay.companyIsExpand }">
                    <el-checkbox
                      :indeterminate="delay.companyIsIndeterminate"
                      style="
                        height: 30px;
                        line-height: 30px;
                        display: inline;
                        margin-right: 10px;
                      "
                      v-model="delay.companyCheckAll"
                      @change="
                        ($event) => handleCheckAllChange('delayCompany', $event)
                      "
                    >
                      全部公司
                    </el-checkbox>
                    <el-checkbox-group
                      v-model="delay.company"
                      @change="
                        ($event) => changeCheckbox('delayCompany', $event)
                      "
                      style="display: inline"
                    >
                      <el-checkbox
                        :label="item.id"
                        v-for="item in delay.companyList"
                        :key="item.id"
                        style="height: 30px; line-height: 30px"
                      >
                        {{ item.name }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </el-row>
                </el-col>
                <el-col :span="4">
                  <el-button
                    type="text"
                    @click="delay.companyIsExpand = !delay.companyIsExpand"
                  >
                    {{ !delay.companyIsExpand ? '展开' : '收起' }}
                    <i
                      class="el-icon-arrow-down"
                      v-if="!delay.companyIsExpand"
                    />
                    <i class="el-icon-arrow-up" v-else />
                  </el-button>
                </el-col>
              </el-row>
              <!-- 仓库 -->
              <el-row>
                <el-col :span="20">
                  <el-row :class="{ 'brand-close': !delay.depotIsExpand }">
                    <el-checkbox
                      :indeterminate="delay.depotIsIndeterminate"
                      style="
                        height: 30px;
                        line-height: 30px;
                        display: inline;
                        margin-right: 10px;
                      "
                      v-model="delay.depotCheckAll"
                      @change="
                        ($event) => handleCheckAllChange('delayDepot', $event)
                      "
                    >
                      全部仓库
                    </el-checkbox>
                    <el-checkbox-group
                      v-model="delay.depot"
                      @change="($event) => changeCheckbox('delayDepot', $event)"
                      style="display: inline"
                    >
                      <el-checkbox
                        :label="item.label"
                        v-for="item in delay.depotList"
                        :key="item.label"
                        style="height: 30px; line-height: 30px"
                      >
                        {{ item.name }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </el-row>
                </el-col>
                <el-col :span="4">
                  <el-button
                    type="text"
                    @click="delay.depotIsExpand = !delay.depotIsExpand"
                  >
                    {{ !delay.depotIsExpand ? '展开' : '收起' }}
                    <i class="el-icon-arrow-down" v-if="!delay.depotIsExpand" />
                    <i class="el-icon-arrow-up" v-else />
                  </el-button>
                </el-col>
              </el-row>
            </div>
            <div class="tab__export" @click="exportList('delay')">
              <i class="el-icon-download" />
              导 出
            </div>
          </div>
          <!-- 柱形图 -->
          <div
            ref="delayColumn"
            :style="{ width: '100%', height: '400px' }"
          ></div>
          <!-- 柱形图 end -->
        </div>
      </el-card>
      <!-- 仓库滞销预警 end -->
      <!-- 仓库效期预警 -->
      <el-card class="box-card" style="margin-bottom: 20px" ref="expire">
        <div
          slot="header"
          class="fs-14 clr-II"
          style="display: flex; justify-content: space-between"
        >
          <div style="display: flex; align-items: center">
            <h2 style="margin-right: 16px">仓库效期预警</h2>
            <span>统计月份：{{ expire.month[0] }} ~ {{ expire.month[1] }}</span>
          </div>
          <div>
            <span>币种：人民币</span>
            <span style="margin-left: 6px">统计月份：</span>
            <el-date-picker
              v-model="expire.month"
              type="monthrange"
              align="right"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM"
              :clearable="false"
              @change="fetchExpireChartData"
            ></el-date-picker>
          </div>
        </div>
        <div>
          <!-- tabs部分 -->
          <div class="tab" style="margin-bottom: 20px">
            <div style="flex: 1">
              <!-- 公司 -->
              <el-row>
                <el-col :span="20">
                  <el-row :class="{ 'brand-close': !expire.companyIsExpand }">
                    <el-checkbox
                      :indeterminate="expire.companyIsIndeterminate"
                      style="
                        height: 30px;
                        line-height: 30px;
                        display: inline;
                        margin-right: 10px;
                      "
                      v-model="expire.companyCheckAll"
                      @change="
                        ($event) =>
                          handleCheckAllChange('expireCompany', $event)
                      "
                    >
                      全部公司
                    </el-checkbox>
                    <el-checkbox-group
                      v-model="expire.company"
                      @change="
                        ($event) => changeCheckbox('expireCompany', $event)
                      "
                      style="display: inline"
                    >
                      <el-checkbox
                        :label="item.id"
                        v-for="item in expire.companyList"
                        :key="item.id"
                        style="height: 30px; line-height: 30px"
                      >
                        {{ item.name }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </el-row>
                </el-col>
                <el-col :span="4">
                  <el-button
                    type="text"
                    @click="expire.companyIsExpand = !expire.companyIsExpand"
                  >
                    {{ !expire.companyIsExpand ? '展开' : '收起' }}
                    <i
                      class="el-icon-arrow-down"
                      v-if="!expire.companyIsExpand"
                    />
                    <i class="el-icon-arrow-up" v-else />
                  </el-button>
                </el-col>
              </el-row>
              <!-- 品牌 -->
              <el-row>
                <el-col :span="20">
                  <el-row :class="{ 'brand-close': !expire.brandIsExpand }">
                    <el-checkbox
                      :indeterminate="expire.brandIsIndeterminate"
                      style="
                        height: 30px;
                        line-height: 30px;
                        display: inline;
                        margin-right: 10px;
                      "
                      v-model="expire.brandCheckAll"
                      @change="
                        ($event) => handleCheckAllChange('expireBrand', $event)
                      "
                    >
                      全部品牌
                    </el-checkbox>
                    <el-checkbox-group
                      v-model="expire.brand"
                      @change="
                        ($event) => changeCheckbox('expireBrand', $event)
                      "
                      style="display: inline"
                    >
                      <el-checkbox
                        :label="item.label"
                        v-for="item in expire.brandList"
                        :key="item.label"
                        style="height: 30px; line-height: 30px"
                      >
                        {{ item.name }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </el-row>
                </el-col>
                <el-col :span="4">
                  <el-button
                    type="text"
                    @click="expire.brandIsExpand = !expire.brandIsExpand"
                  >
                    {{ !expire.brandIsExpand ? '展开' : '收起' }}
                    <i
                      class="el-icon-arrow-down"
                      v-if="!expire.brandIsExpand"
                    />
                    <i class="el-icon-arrow-up" v-else />
                  </el-button>
                </el-col>
              </el-row>
            </div>
            <div class="tab__export" @click="exportList('expire')">
              <i class="el-icon-download" />
              导 出
            </div>
          </div>
          <!-- 柱形图 -->
          <div
            ref="expireColumn"
            :style="{ width: '100%', height: '400px' }"
          ></div>
          <!-- 柱形图 end -->
        </div>
      </el-card>
      <!-- 仓库效期预警 end -->
    </div>
    <!-- 右侧菜单栏 -->
    <div class="container__right">
      <div class="nav">
        <!-- <div class="nav__item" @click="navClick({ ref: 'container' })">返回顶部</div> -->
        <div
          class="nav__item"
          v-for="item in navList"
          :key="item.ref"
          :class="{ 'nav__item--active': item.name === navCurrentName }"
          @click="navClick(item)"
        >
          {{ item.name }}
        </div>
      </div>
    </div>
    <!-- 右侧菜单栏 end -->
  </div>
</template>

<script>
  import {
    createColumnChart,
    createPieChart,
    // createColumnChartX,
    createPolylineColumn,
    createRoseChart,
    createStackColumnChart
  } from '@u/chart'
  import {
    getBuList,
    getTargetAndAchieve,
    exportTargetAndAchieveUrl,
    getYearFinanceInventoryAnalysis,
    getBrandSaleTop,
    getCusSaleTop,
    getDepotList,
    getMerchantList,
    getDepotUnsellableWarning,
    getBrandList,
    getDepotExpireWarning,
    getInventoryAnalysisData,
    getInWarehouseData,
    getBrandInWarehouse,
    exportBrandUrl,
    exportDepotExpireWarningUrl,
    exportDepotUnsellableWarningUrl,
    exportInWarehouseDaysUrl,
    exportInventoryAnalysisUrl,
    exportYearFinanceInventoryAnalysisUrl,
    exportgetCustomersUrl,
    getDepartList
  } from '@a/dataInsight'
  import { getDate, randomHexColor, throttle } from '@u/tool'
  import * as echarts from 'echarts'
  export default {
    data() {
      return {
        // 日期控件配置
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() < Date.now()
          }
        },
        // 右侧导航栏数据
        navList: [
          { name: '部门销售达成', ref: 'achieve' },
          { name: '年度进销存分析', ref: 'annual' },
          { name: '品牌销量TOP8分析', ref: 'brand' },
          { name: '客户销量TOP8分析', ref: 'customer' },
          { name: '库存量分析', ref: 'stock' },
          { name: '商品在库天数分析', ref: 'wares' },
          { name: '仓库滞销预警', ref: 'delay' },
          { name: '仓库效期预警', ref: 'expire' }
        ],
        navListHeight: [],
        // 右侧导航栏选中索引
        navCurrentName: '部门销售达成',
        // 事业群销售达成
        achieve: {
          tabList: [],
          currentIdx: 0,
          rightTabIdx: 0,
          departmentId: '',
          month: '',
          data: {}
        },
        // 年度进销存分析
        annual: {
          tabList: [],
          brandList: [],
          isExpand: false,
          brandTypeList: [
            { label: '2', name: '按母品牌' },
            { label: '1', name: '按标准品牌' }
          ],
          color: [
            ['#FF720B', '#FF720B'],
            ['#1B82EB', '#1B82EB'],
            ['#55E2E6', '#55E2E6'],
            ['#5DADFF', '#5DADFF'],
            ['#B39FFF', '#B39FFF'],
            ['#D5CAFF', '#D5CAFF'],
            ['#CCD5D9', '#CCD5D9'],
            ['#79F7FB', '#79F7FB']
          ],
          checkAll: false,
          isIndeterminate: false,
          brand: [],
          isParentBrand: '2',
          currentIdx: 0,
          buId: '',
          month: [],
          data: {}
        },
        // 品牌销量TOP8分析
        brand: {
          tabList: [],
          brandList: [
            { label: '2', name: '按母品牌统计' },
            { label: '1', name: '按标准品牌统计' }
          ],
          color: [
            ['#FF720B', '#FF720B'],
            ['#1B82EB', '#1B82EB'],
            ['#55E2E6', '#55E2E6'],
            ['#5DADFF', '#5DADFF'],
            ['#B39FFF', '#B39FFF'],
            ['#D5CAFF', '#D5CAFF'],
            ['#CCD5D9', '#CCD5D9'],
            ['#79F7FB', '#79F7FB']
          ],
          isParentBrand: '2',
          currentIdx: 0,
          buId: '',
          month: [],
          data: {}
        },
        // 客户销量TOP8分析
        customer: {
          tabList: [],
          channelList: [
            { label: 'To B', name: '只看ToB渠道' },
            { label: 'To C', name: '只看ToC平台' }
          ],
          checkAll: true,
          isIndeterminate: false,
          color: [
            ['#FF720B', '#FF720B'],
            ['#1B82EB', '#1B82EB'],
            ['#55E2E6', '#55E2E6'],
            ['#5DADFF', '#5DADFF'],
            ['#B39FFF', '#B39FFF'],
            ['#D5CAFF', '#D5CAFF'],
            ['#CCD5D9', '#CCD5D9'],
            ['#79F7FB', '#79F7FB']
          ],
          channel: ['To B', 'To C'],
          currentIdx: 0,
          buId: '',
          month: [],
          data: {}
        },
        // 库存量分析
        stock: {
          tabList: [],
          channelList: [
            { label: '1', name: '按品牌' },
            { label: '2', name: '按仓库' },
            { label: '3', name: '按公司' }
          ],
          channel: '1',
          currentIdx: 0,
          buId: '',
          month: '',
          color: [
            ['#FF720B', '#FF720B'],
            ['#CCD5D9', '#CCD5D9'],
            ['#5DADFF', '#5DADFF'],
            ['#81E6E9', '#81E6E9'],
            ['#1B82EB', '#1B82EB'],
            ['#B39FFF', '#B39FFF']
          ],
          data: {}
        },
        // 商品在库天数分析
        wares: {
          tabList: [],
          brandList: [
            { label: '2', name: '按母品牌' },
            { label: '1', name: '按标准品牌' }
          ],
          isParentBrand: '2',
          inWarehouseRange: '0~30天',
          warehouseRangeList: [
            '0~30天',
            '30~60天',
            '60~90天',
            '90~120天',
            '120~150天',
            '150~180天',
            '180天以上'
          ],
          currentIdx: 0,
          buId: '',
          month: '',
          color: [
            ['#FF720B', '#FF720B'],
            ['#CCD5D9', '#CCD5D9'],
            ['#5DADFF', '#5DADFF'],
            ['#81E6E9', '#81E6E9'],
            ['#1B82EB', '#1B82EB'],
            ['#B39FFF', '#B39FFF']
          ],
          data: {}
        },
        // 仓库滞销预警
        delay: {
          company: [],
          depot: [],
          companyList: [],
          depotList: [],
          month: '',
          data: {},
          companyIsExpand: false,
          depotIsExpand: false,
          companyIsIndeterminate: false,
          companyCheckAll: false,
          depotIsIndeterminate: false,
          depotCheckAll: false,
          color: ['#0069E0', '#FF740B', '#5DADFF', '#5FD1EE', '#ADBCC3']
        },
        // 仓库效期预警
        expire: {
          company: [],
          brand: [],
          companyList: [],
          brandList: [],
          month: '',
          data: {},
          companyIsExpand: false,
          brandIsExpand: false,
          companyIsIndeterminate: false,
          companyCheckAll: false,
          brandIsIndeterminate: false,
          brandCheckAll: false,
          color: ['#0069E0', '#FF740B', '#5DADFF', '#5FD1EE', '#ADBCC3']
        },
        // 事业群销售达成
        achieveColumnDom: null,
        achievePieDom: null,
        // 年度进销存分析
        annualColumnDom: null,
        annualPieDom: null,
        // 品牌销量TOP8分析
        brandColumnDom: null,
        brandPieDom: null,
        // 客户销量TOP8分析
        customerColumnDom: null,
        customerPieDom: null,
        // 库存量分析
        stockColumnDom: null,
        stockPieDom: null,
        // 商品在库天数分析
        waresColumnDom: null,
        waresPieDom: null,
        waresBrandColumnDom: null,
        waresBrandPieDom: null,
        // 仓库滞销预警
        delayColumnDom: null,
        // 仓库效期预警
        expireColumnDom: null,
        rightTabList: ['销售汇总', '细分对比']
      }
    },
    mounted() {
      this.init()
      this.$nextTick(() => {
        window.addEventListener(
          'resize',
          () => {
            this.achieveColumnDom.resize()
            // this.achievePieDom.resize()
            this.annualColumnDom.resize()
            this.annualPieDom.resize()
            this.brandColumnDom.resize()
            this.brandPieDom.resize()
            this.customerColumnDom.resize()
            this.customerPieDom.resize()
            this.stockColumnDom.resize()
            this.stockPieDom.resize()
            this.waresColumnDom.resize()
            this.waresPieDom.resize()
            this.waresBrandColumnDom.resize()
            this.waresBrandPieDom.resize()
            this.delayColumnDom.resize()
            this.expireColumnDom.resize()
          },
          true
        )
        this.getCardHeight()
        const container = this.$refs.container
        container.addEventListener('scroll', throttle(this.handleScroll, 300))
      })
    },
    methods: {
      async init() {
        const from = sessionStorage.getItem('from')
        const nav = document.querySelector('.container__right .nav')
        if (from === 'oldsystem') {
          nav.style.top = '15px'
        } else {
          nav.style.top = '120px'
        }
        this.achieve.month = this.$formatDate(new Date(), 'yyyy-MM')
        this.annual.month = this.$formatDate(new Date(), 'yyyy')
        this.brand.month = [getDate('month', -2), getDate()]
        this.customer.month = [getDate('month', -2), getDate()]
        this.stock.month = this.$formatDate(new Date(), 'yyyy-MM')
        this.wares.month = this.$formatDate(new Date(), 'yyyy-MM')
        this.delay.month = [
          getDate('month', -2).slice(0, 7),
          getDate().slice(0, 7)
        ]
        this.expire.month = [
          getDate('month', -2).slice(0, 7),
          getDate().slice(0, 7)
        ]
        // 获取事业部列表
        await this.getBuList()
        // 获取部门信息
        await this.getDepartList()
        // 获取公司列表
        await this.getMerchantList()
        // 获取事业群销售达成数据
        await this.fetchAchieveChartData()
        // 获取年度进销存分析
        await this.fetchAnnualChartData()
        // 获取品牌销量TOP8分析
        await this.fetchBrandChartData()
        // 客户销量TOP8分析
        await this.fetchCustomerChartData()
        // 库存量分析
        await this.fetchStockChartData()
        // 商品在库天数分析
        await this.fetchWaresChartData()
        // 品牌在库天数分析
        await this.fetchWaresBrandChartData()
        // 仓库滞销预警
        await this.fetchDelayChartData()
        // 仓库滞销预警
        await this.fetchExpireChartData()
      },
      async getCardHeight() {
        await this.$nextTick()
        this.navList.forEach(({ ref }) => {
          const top = this.$refs[ref].$el.offsetTop
          this.navListHeight.push(top)
        })
      },
      async handleScroll() {
        await this.$nextTick()
        const from = sessionStorage.getItem('from')
        const container = this.$refs.container
        const top =
          from === 'oldsystem'
            ? container.scrollTop + 150
            : container.scrollTop + 220
        for (let i = 0; i < this.navListHeight.length; i++) {
          if (top > this.navListHeight[i] && top <= this.navListHeight[i + 1]) {
            this.navCurrentName = this.navList[i].name
          }
        }
      },
      // 获取事业部列表
      async getBuList() {
        const { data } = await getBuList()
        if (data && data.length) {
          this.annual.tabList = [{ id: '', name: '事业部汇总' }, ...data]
          this.brand.tabList = [{ id: '', name: '事业部汇总' }, ...data]
          this.customer.tabList = [{ id: '', name: '事业部汇总' }, ...data]
          this.stock.tabList = [{ id: '', name: '事业部汇总' }, ...data]
          this.wares.tabList = [{ id: '', name: '事业部汇总' }, ...data]
          this.expire.tabList = [{ id: '', name: '事业部汇总' }, ...data]
        }
      },
      // 获取部门信息
      async getDepartList() {
        const { data } = await getDepartList()
        if (data && data.length) {
          const list = data.map((item) => ({
            id: item.departmentId,
            name: item.departmentName
          }))
          this.achieve.tabList = [{ id: '', name: '部门汇总' }, ...list]
        }
      },
      // 获取公司列表
      async getMerchantList() {
        const { data } = await getMerchantList()
        if (data && data.length) {
          const list = data.map((item) => ({
            id: item.id,
            name: item.name
          }))
          this.delay.companyList = list
          this.expire.companyList = list
        }
      },
      // 获取事业群销售达成数据
      async fetchAchieveChartData() {
        const { departmentId, month } = this.achieve
        try {
          this.achieve.data = {
            monthAchieveAmount: [],
            yearTargetAmount: [],
            monthTargetAmount: [],
            yearAchieveAmount: []
          }
          this.achieve.xAxisData = []
          const { data = {} } = await getTargetAndAchieve({
            departmentId,
            month
          })
          if (data && data.length) {
            data.forEach((item) => {
              this.achieve.xAxisData.push(item.parentBrandName)
              this.achieve.data.monthAchieveAmount.push(
                item.monthAchieveAmount || 0
              )
              this.achieve.data.yearTargetAmount.push(
                item.yearTargetAmount || 0
              )
              this.achieve.data.monthTargetAmount.push(
                item.monthTargetAmount || 0
              )
              this.achieve.data.yearAchieveAmount.push(
                item.yearAchieveAmount || 0
              )
            })
          }
          // 渲染事业群销售达柱状图
          this.renderColumnChat()
          // 渲染事业群销售达饼图
          // this.renderPieChart()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 获取年度进销存分析
      async fetchAnnualChartData() {
        const { buId, month, isParentBrand, color, brand } = this.annual
        const brandIds = brand.length ? brand.toString() : ''
        try {
          const {
            data: { brandPurchase, brandsMap, yearAmount }
          } = await getYearFinanceInventoryAnalysis({
            buId,
            month,
            isParentBrand,
            brandIds
          })
          // 初始化值
          this.annual.brandList = brandsMap || []
          this.annual.data.saleAmount = []
          this.annual.data.purchaseEndAmount = []
          this.annual.data.inventoryEndAmount = []
          this.annual.data.brandPurchase = []
          // 柱状图数据
          if (yearAmount && yearAmount.length) {
            for (let i = 1; i <= 12; i++) {
              const target = yearAmount.find(
                (item) => item.month && +item.month === i
              )
              this.annual.data.saleAmount.push(target.saleAmount || 0)
              this.annual.data.purchaseEndAmount.push(
                target.purchaseEndAmount || 0
              )
              this.annual.data.inventoryEndAmount.push(
                target.inventoryEndAmount || 0
              )
            }
          }
          // 饼状图数据
          if (brandPurchase && brandPurchase.length) {
            const data = brandPurchase.map((item, index) => {
              const randomColor = randomHexColor()
              return {
                name: item.brandName,
                value: item.purchaseEndAmount,
                color: color[index] || [randomColor, randomColor],
                borderColor: color[index] ? color[index][0] : randomColor
              }
            })
            this.annual.data.brandPurchase = data
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
        // 渲染年度进销存分析折线、柱状图
        this.renderPolylineColumn()
        // 渲染年度进销存分析饼图
        this.renderPieChart1()
      },
      // 品牌销量TOP8分析
      async fetchBrandChartData() {
        const { buId, month, isParentBrand } = this.brand
        try {
          const { data = [] } = await getBrandSaleTop({
            buId,
            month: month[0] + '~' + month[1],
            isParentBrand
          })
          this.brand.data.xAxisData = []
          this.brand.data.seriesData = []
          if (data && data.length) {
            data.forEach((item) => {
              this.brand.data.xAxisData.push(item.brandParent || '')
              this.brand.data.seriesData.push({
                value: item.cnyAmount || 0,
                name: item.brandParent || ''
              })
            })
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
        // 渲染品牌销量TOP8分析折线、柱状图
        this.renderColumnChat2()
        // 渲染品牌销量TOP8分析饼图
        this.renderPieChart2()
      },
      // 客户销量TOP8分析
      async fetchCustomerChartData() {
        const { buId, month, channel } = this.customer
        const channelType = channel.length < 2 ? channel.toString() : ''
        try {
          const { data = [] } = await getCusSaleTop({
            buId,
            month: month[0] + '~' + month[1],
            channelType
          })
          this.customer.data.xAxisData = []
          this.customer.data.seriesData = []
          if (data && data.length) {
            data.forEach((item) => {
              this.customer.data.xAxisData.push(item.customerName || '')
              this.customer.data.seriesData.push({
                value: item.cnyAmount || 0,
                name: item.customerName || ''
              })
            })
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
        // 渲染客户销量TOP8分析析折线、柱状图
        this.renderColumnChat3()
        // 渲染客户销量TOP8分析析饼图
        this.renderPieChart3()
      },
      // 库存量分析
      async fetchStockChartData() {
        const { buId, month, color, channel: type } = this.stock
        try {
          const { data } = await getInventoryAnalysisData({ buId, month, type })
          this.stock.data.amount = []
          this.stock.data.surplusNum = []
          this.stock.data.clearDays = []
          this.stock.data.xAxisData = []
          this.stock.data.pieData = []
          if (data && data.length) {
            data.forEach((item, index) => {
              const randomColor = randomHexColor()
              this.stock.data.amount.push(item.amount || 0)
              this.stock.data.surplusNum.push(item.surplusNum || 0)
              this.stock.data.clearDays.push(item.clearDays || 0)
              this.stock.data.xAxisData.push(item.name)
              this.stock.data.pieData.push({
                value: item.amount,
                name: item.name,
                color: color[index] || [randomColor, randomColor],
                borderColor: color[index] ? color[index][0] : randomColor
              })
            })
          }
          // 渲染库存量分析析折线、柱状图
          this.renderColumnChat4()
          // 渲染库存量分析饼图
          this.renderPieChart4()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 商品在库天数分析
      async fetchWaresChartData() {
        const { buId, month, color } = this.wares
        try {
          const { data } = await getInWarehouseData({ buId, month })
          this.wares.data.totalAmount = []
          this.wares.data.totalNum = []
          this.wares.data.xAxisData = []
          this.wares.data.pieData = []
          if (data && data.length) {
            data.forEach((item, index) => {
              const randomColor = randomHexColor()
              this.wares.data.totalAmount.push(item.totalAmount || 0)
              this.wares.data.totalNum.push(item.totalNum || 0)
              this.wares.data.xAxisData.push(item.inWarehouseRange)
              this.wares.data.pieData.push({
                value: item.totalAmount,
                name: item.inWarehouseRange,
                color: color[index] || [randomColor, randomColor],
                borderColor: color[index] ? color[index][0] : randomColor
              })
            })
          }
          // 渲染库存量分析析折线、柱状图
          this.renderColumnChat5()
          // 渲染库存量分析饼图
          this.renderPieChart5()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 品牌在库天数分析
      async fetchWaresBrandChartData() {
        const { buId, month, color, inWarehouseRange, isParentBrand } =
          this.wares
        try {
          const { data: brandData } = await getBrandInWarehouse({
            buId,
            month,
            inWarehouseRange,
            isParentBrand
          })
          this.wares.data.brandXAxisData = []
          this.wares.data.brandSeriesData = []
          this.wares.data.brandPieData = []
          if (brandData && brandData.length) {
            brandData.forEach((item, index) => {
              const randomColor = randomHexColor()
              this.wares.data.brandXAxisData.push(
                item.superiorParentBrand || item.brandName
              )
              this.wares.data.brandSeriesData.push(item.wareHorseNum || 0)
              this.wares.data.brandPieData.push({
                value: item.wareHorseNum,
                name: item.superiorParentBrand || item.brandName,
                color: color[index] || [randomColor, randomColor],
                borderColor: color[index] ? color[index][0] : randomColor
              })
            })
          }
          // 渲染库存量分析析折线、柱状图
          this.renderColumnChat6()
          // 渲染库存量分析饼图
          this.renderPieChart6()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 仓库滞销预警
      async fetchDelayChartData() {
        const { buId, month, color, company, depot } = this.delay
        try {
          const { data: depotList } = await getDepotList({
            month: month[0] + '~' + month[1]
          })
          if (depotList && depotList.length) {
            const list = depotList.map((item) => ({
              name: item.depotName,
              label: item.depotId
            }))
            this.delay.depotList = list
          }
          const merchantIds = company.length ? company.toString() : ''
          const depotIds = depot.length ? depot.toString() : ''
          const { data } = await getDepotUnsellableWarning({
            buId,
            month: month[0] + '~' + month[1],
            merchantIds,
            depotIds
          })
          this.delay.data.seriesData = []
          this.delay.data.legendData = []
          if (data) {
            Object.keys(data).forEach((key, index) => {
              const randomColor = randomHexColor()
              if (key) {
                this.delay.data.legendData.push(key)
                this.delay.data.seriesData.push({
                  name: key,
                  id: data[key].brandId || '',
                  color: color[index] || randomColor,
                  data: [
                    data[key]['0~30天'] || 0,
                    data[key]['30天~60天'] || 0,
                    data[key]['60天~90天'] || 0,
                    data[key]['90天~120天'] || 0,
                    data[key]['120天以上'] || 0
                  ]
                })
              }
            })
          }
          // 渲染仓库滞销预警柱状图
          this.renderColumnChat7()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 仓库效期预警
      async fetchExpireChartData() {
        const { buId, month, company, brand, color } = this.expire
        try {
          const { data: brandList } = await getBrandList({
            month: month[0] + '~' + month[1]
          })
          if (brandList && brandList.length) {
            const list = brandList.map((item) => ({
              name: item.superiorParentBrand,
              label: item.superiorParentBrandId
            }))
            this.expire.brandList = list
          }
          const merchantIds = company.length ? company.toString() : ''
          const brandParentIds = brand.length ? brand.toString() : ''
          const { data } = await getDepotExpireWarning({
            buId,
            month: month[0] + '~' + month[1],
            merchantIds,
            brandParentIds
          })
          this.expire.data.seriesData = []
          this.expire.data.yAxisData = []
          if (data) {
            const arr = [
              '1/10以下及过期',
              '1/10-1/5',
              '1/5-1/3',
              '1/3-1/2',
              '1/2以上'
            ]
            // Object.keys(data).forEach((key) => {
            //   if (key) {
            //     this.expire.data['yAxisData'].push(key)
            //     arr.forEach((item, index) => {
            //       const randomColor = randomHexColor()
            //       this.expire.data['seriesData'].push({
            //         name: item,
            //         color: color[index] || randomColor,
            //         data: [data[key][item] || 0]
            //       })
            //     })
            //   }
            // })
            Object.keys(data).forEach((key) => {
              this.expire.data.yAxisData.push(key)
            })
            arr.forEach((item, index) => {
              const randomColor = randomHexColor()
              this.expire.data.seriesData.push({
                name: item,
                color: color[index] || randomColor,
                data: []
              })
              Object.keys(data).forEach((key) => {
                this.expire.data.seriesData[index].data.push(
                  data[key][item] || 0
                )
              })
            })
          }
          // 渲染仓库滞销预警柱状图
          this.renderColumnChat8()
        } catch (error) {
          this.$errorMsg(error.message)
        }
        // 渲染仓库效期预警柱状图
        this.renderColumnChat8()
      },
      async navClick({ name, ref }) {
        this.$nextTick(() => {
          this.navCurrentName = name
          this.$refs[ref].$el.scrollIntoView({
            behavior: 'smooth'
          })
        })
      },
      // tab栏点击
      tabClick(type, index, id) {
        switch (type) {
          case 'achieve':
            this.achieve.currentIdx = index
            this.achieve.departmentId = id
            this.fetchAchieveChartData()
            break
          case 'annual':
            this.annual.currentIdx = index
            this.annual.buId = id
            this.annual.brand = []
            this.fetchAnnualChartData()
            break
          case 'brand':
            this.brand.currentIdx = index
            this.brand.buId = id
            this.fetchBrandChartData()
            break
          case 'customer':
            this.customer.currentIdx = index
            this.customer.buId = id
            this.fetchCustomerChartData()
            break
          case 'stock':
            this.stock.currentIdx = index
            this.stock.buId = id
            this.fetchStockChartData()
            break
          case 'wares':
            this.wares.currentIdx = index
            this.wares.buId = id
            this.fetchWaresChartData()
            this.fetchWaresBrandChartData()
            break
        }
      },
      achieveTabClick(index) {
        this.achieve.rightTabIdx = index
        this.renderColumnChat()
      },
      // 渲染事业群销售达柱状图
      renderColumnChat() {
        const {
          xAxisData,
          data: {
            monthAchieveAmount,
            yearTargetAmount,
            monthTargetAmount,
            yearAchieveAmount
          }
        } = this.achieve
        const columnChartData = {
          tooltipData: {
            type: 'achieve'
          },
          xAxisData,
          seriesData: [
            {
              data: monthAchieveAmount,
              color: ['#399BFF', '#0069E0'],
              name: '月度达成',
              xAxisIndex: 1,
              z: 3,
              showLabel: true,
              labelColor: '#0069E0'
            },
            {
              data: monthTargetAmount,
              color: ['#E0F1FB', '#E0F1FB'],
              name: '月度目标'
            },
            {
              data: yearAchieveAmount,
              color: ['#FFB10B', '#FF720B'],
              name: '年度达成',
              showLabel: true,
              labelColor: '#FF740B',
              xAxisIndex: 1,
              z: 3
            },
            {
              data: yearTargetAmount,
              color: ['#F0F0F0', '#F0F0F0'],
              name: '年度目标'
            }
          ]
        }
        if (!this.achieveColumnDom) {
          this.achieveColumnDom = echarts.init(this.$refs.achieveColumn)
        }
        this.achieveColumnDom.clear()
        createColumnChart(this.achieveColumnDom, columnChartData)
        const dataLen = this.achieve.data.monthAchieveAmount.length // 数据总条数
        const showCount = 12 // 显示的条数
        if (dataLen >= showCount) {
          this.achieveColumnDom.setOption({
            grid: {
              bottom: '15%'
            },
            dataZoom: [
              {
                height: 1,
                zoomLock: true,
                filterMode: 'empty',
                showDetail: false,
                show: true,
                fillerColor: '#1C70E3',
                moveHandleSize: 14,
                start: 0,
                end: dataLen > showCount ? (showCount / dataLen) * 100 : 100,
                maxSpan:
                  dataLen > showCount ? (showCount / dataLen) * 100 : 100,
                moveHandleStyle: {
                  color: '#1C70E3'
                }
              }
            ]
          })
        }
      },
      // 渲染事业群销售达饼图
      renderPieChart() {
        const {
          data: { monthAchieve }
        } = this.achieve
        const pieChartData = {
          title: '销售占比',
          seriesData: [
            {
              value: monthAchieve[2] || 0,
              name: 'POP',
              color: ['#1B82EB', '#1B82EB'],
              borderColor: '#1B82EB'
            },
            {
              value: monthAchieve[4] || 0,
              name: '代销',
              color: ['#65D7DA', '#65D7DA'],
              borderColor: '#65D7DA'
            },
            {
              value: monthAchieve[1] || 0,
              name: '购销B',
              color: ['#B39FFF', '#B39FFF'],
              borderColor: '#B39FFF'
            },
            {
              value: monthAchieve[0] || 0,
              name: '购销A',
              color: ['#FFB10B', '#FFB10B'],
              borderColor: '#FFB10B'
            },
            {
              value: monthAchieve[3] || 0,
              name: '代发',
              color: ['#f59c79', '#f59c79'],
              borderColor: '#f59c79'
            }
          ]
        }
        if (!this.achievePieDom) {
          this.achievePieDom = echarts.init(this.$refs.achievePie)
        }
        this.achievePieDom.clear()
        createPieChart(this.achievePieDom, pieChartData)
      },
      // 渲染年度进销存分析折线、柱状图
      renderPolylineColumn() {
        const {
          data: { saleAmount, purchaseEndAmount, inventoryEndAmount }
        } = this.annual
        const chartData = {
          legendData: ['采购金额', '销售金额', '库存金额'],
          xAxisData: [
            '一月',
            '二月',
            '三月',
            '四月',
            '五月',
            '六月',
            '七月',
            '八月',
            '九月',
            '十月',
            '十一月',
            '十二月'
          ],
          tooltipData: {
            type: 'annual'
          },
          seriesData: [
            {
              data: purchaseEndAmount,
              barGap: '10%',
              name: '采购金额',
              type: 'bar',
              color: ['#399BFF', '#0069E0']
            },
            {
              data: saleAmount,
              barGap: '10%',
              name: '销售金额',
              type: 'bar',
              color: ['#FFB10B', '#FF720B']
            },
            {
              data: inventoryEndAmount,
              name: '库存金额',
              type: 'line',
              color: '#30D3D8'
            }
          ]
        }
        if (!this.annualColumnDom) {
          this.annualColumnDom = echarts.init(this.$refs.annualColumn)
        }
        this.annualColumnDom.clear()
        createPolylineColumn(this.annualColumnDom, chartData)
      },
      // 渲染年度进销存分析饼图
      renderPieChart1() {
        const {
          data: { brandPurchase: seriesData }
        } = this.annual
        const pieChartData = {
          title: '品牌采购金额TOP8占比',
          seriesData
        }
        if (!this.annualPieDom) {
          this.annualPieDom = echarts.init(this.$refs.annualPie)
        }
        this.annualPieDom.clear()
        createPieChart(this.annualPieDom, pieChartData)
      },
      // 渲染品牌销量TOP8分析柱状图
      renderColumnChat2() {
        const {
          data: { seriesData, xAxisData }
        } = this.brand
        const columnChartData = {
          tooltipData: {
            type: 'brand'
          },
          xAxisData: xAxisData,
          seriesData: [
            {
              data: seriesData,
              color: ['#399BFF', '#0069E0'],
              showLabel: true,
              labelColor: '#0069E0'
            }
          ]
        }
        if (!this.brandColumnDom) {
          this.brandColumnDom = echarts.init(this.$refs.brandColumn)
        }
        this.brandColumnDom.clear()
        createColumnChart(this.brandColumnDom, columnChartData)
        this.brandColumnDom.setOption({
          xAxis: [
            {
              axisLabel: {
                rotate: 30
              }
            }
          ]
        })
      },
      // 渲染品牌销量TOP8分析饼图
      renderPieChart2() {
        const { data, color } = this.brand
        const seriesData = data.seriesData.map((item, index) => {
          const randomColor = randomHexColor()
          return {
            value: item.value,
            name: item.name,
            color: color[index] || [randomColor, randomColor],
            borderColor: color[index] ? color[index][0] : randomColor
          }
        })
        const chartData = {
          title: '品牌销量占比',
          seriesData
        }
        if (!this.brandPieDom) {
          this.brandPieDom = echarts.init(this.$refs.brandPie)
        }
        this.brandPieDom.clear()
        createRoseChart(this.brandPieDom, chartData)
      },
      // 渲染客户销量TOP8分析柱状图
      renderColumnChat3() {
        const { data } = this.customer
        const columnChartData = {
          tooltipData: {
            type: 'brand'
          },
          xAxisData: data.xAxisData,
          seriesData: [
            {
              data: data.seriesData,
              color: ['#FFB10B', '#FF720B'],
              showLabel: true,
              labelColor: '#FFB10B'
            }
          ]
        }
        if (!this.customerColumnDom) {
          this.customerColumnDom = echarts.init(this.$refs.customerColumn)
        }
        this.customerColumnDom.clear()
        createColumnChart(this.customerColumnDom, columnChartData)
        this.customerColumnDom.setOption({
          xAxis: {
            axisLabel: {
              rotate: 30,
              interval: 0
            }
          }
        })
      },
      // 渲染客户销量TOP8分析饼图
      renderPieChart3() {
        const { data, color } = this.customer
        this.$nextTick(() => {
          const seriesData = data.seriesData.map((item, index) => {
            const randomColor = randomHexColor()
            return {
              value: item.value,
              name: item.name,
              color: color[index] || [randomColor, randomColor],
              borderColor: color[index] ? color[index][0] : randomColor
            }
          })
          const chartData = {
            title: '渠道销量占比',
            seriesData
          }
          if (!this.customerPieDom) {
            this.customerPieDom = echarts.init(this.$refs.customerPie)
          }
          this.customerPieDom.clear()
          createRoseChart(this.customerPieDom, chartData)
        })
      },
      // 渲染库存量分析折线、柱状图
      renderColumnChat4() {
        const {
          data: { xAxisData, amount, surplusNum, clearDays }
        } = this.stock
        const yAxis = [
          {
            type: 'value',
            name: '库存量(万件)',
            position: 'right',
            axisLine: {
              show: true,
              lineStyle: {
                color: '#1F8EFF'
              }
            },
            axisLabel: {
              formatter: '{value}',
              interval: 0
            },
            axisTick: {
              show: true
            },
            splitLine: {
              show: false
            }
          },
          {
            type: 'value',
            name: '库存金额(万)',
            label: {
              color: '#7C8B92',
              formatter: '{b}: \n{d}%'
            },
            nameTextStyle: {},
            position: 'right',
            offset: 80,
            axisLine: {
              show: true,
              lineStyle: {
                color: '#FF720B'
              }
            },
            axisLabel: {
              formatter: '{value}',
              interval: 0
            },
            axisTick: {
              show: true
            },
            splitLine: {
              show: false
            }
          },
          {
            type: 'value',
            name: '预计清空天数',
            nameTextStyle: {
              color: '#757B80'
            },
            position: 'left',
            axisLine: {
              show: true,
              lineStyle: {
                color: '#F3F6FA'
              }
            },
            axisLabel: {
              color: '#757B80',
              interval: 0
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#F3F6FA'
              }
            }
          }
        ]
        const chartData = {
          tooltipData: {
            type: 'stock'
          },
          yAxis,
          legendData: ['库存量', '库存金额', '预计清空天数'],
          xAxisData,
          seriesData: [
            {
              data: surplusNum,
              name: '库存量',
              color: ['#399BFF', '#0069E0'],
              type: 'bar',
              yAxisIndex: 0,
              showLabel: true,
              labelColor: '#0069E0'
            },
            {
              data: amount,
              name: '库存金额',
              color: ['#FFB10B', '#FF720B'],
              type: 'bar',
              yAxisIndex: 1,
              showLabel: true,
              labelColor: '#FF720B'
            },
            {
              data: clearDays,
              name: '预计清空天数',
              color: '#30D3D8',
              type: 'line',
              yAxisIndex: 2
            }
          ]
        }
        if (!this.stockColumnDom) {
          this.stockColumnDom = echarts.init(this.$refs.stockColumn)
        }
        this.stockColumnDom.clear()
        createPolylineColumn(this.stockColumnDom, chartData)
      },
      // 渲染库存量分析饼图
      renderPieChart4() {
        const {
          data: { pieData }
        } = this.stock
        const chartData = {
          title: '库存金额占比',
          seriesData: pieData
        }
        if (!this.stockPieDom) {
          this.stockPieDom = echarts.init(this.$refs.stockPie)
        }
        this.stockPieDom.clear()
        createRoseChart(this.stockPieDom, chartData)
      },
      // 品牌在库天数TOP8分析折线、柱状图
      renderColumnChat5() {
        const {
          data: { totalAmount, totalNum, xAxisData }
        } = this.wares
        const yAxis = [
          {
            type: 'value',
            name: '库存量（单位：件）',
            nameTextStyle: {
              color: '#757B80'
            },
            position: 'left',
            axisLine: {
              show: true,
              lineStyle: {
                color: '#F3F6FA'
              }
            },
            axisLabel: {
              color: '#757B80'
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#F3F6FA'
              }
            }
          },
          {
            type: 'value',
            name: '库存金额（单位：万）',
            label: {
              color: '#FF7D0B',
              formatter: '{b}: \n{d}%'
            },
            nameTextStyle: {},
            position: 'right',
            axisLine: {
              show: true,
              lineStyle: {
                color: '#FF7D0B'
              }
            },
            axisLabel: {
              formatter: '{value}'
            },
            axisTick: {
              show: true
            },
            splitLine: {
              show: false
            }
          }
        ]
        const chartData = {
          tooltipData: {
            type: 'wares'
          },
          yAxis,
          legendData: ['库存量', '库存金额'],
          xAxisData,
          seriesData: [
            {
              data: totalNum,
              name: '库存量',
              type: 'bar',
              color: ['#399BFF', '#0069E0'],
              yAxisIndex: 0,
              showLabel: true,
              labelColor: '#0069E0'
            },
            {
              data: totalAmount,
              type: 'line',
              name: '库存金额',
              color: '#FF7D0B',
              yAxisIndex: 1
            }
          ]
        }
        if (!this.waresColumnDom) {
          this.waresColumnDom = echarts.init(this.$refs.waresColumn)
        }
        this.waresColumnDom.clear()
        createPolylineColumn(this.waresColumnDom, chartData)
      },
      // 品牌在库天数TOP8分析饼图
      renderPieChart5() {
        const {
          data: { pieData: seriesData }
        } = this.wares
        const pieChartData = {
          title: '库存金额占比',
          seriesData
        }
        if (!this.waresPieDom) {
          this.waresPieDom = echarts.init(this.$refs.waresPie)
        }
        this.waresPieDom.clear()
        createPieChart(this.waresPieDom, pieChartData)
      },
      // 品牌在库天数TOP8分析折线、柱状图
      renderColumnChat6() {
        const {
          data: { brandXAxisData: xAxisData, brandSeriesData }
        } = this.wares
        const columnChartData = {
          tooltipData: {
            type: 'waresBrand'
          },
          yAxisData: {
            name: '单位：件'
          },
          xAxisData,
          seriesData: [
            {
              data: brandSeriesData,
              color: ['#399BFF', '#0069E0'],
              showLabel: true,
              labelColor: '#0069E0'
            }
          ]
        }
        if (!this.waresBrandColumnDom) {
          this.waresBrandColumnDom = echarts.init(this.$refs.waresBrandColumn)
        }
        this.waresBrandColumnDom.clear()
        createColumnChart(this.waresBrandColumnDom, columnChartData)
        this.waresBrandColumnDom.setOption({
          xAxis: {
            axisLabel: {
              rotate: 30,
              interval: 0
            }
          }
        })
      },
      // 品牌在库天数TOP8分析饼图
      renderPieChart6() {
        const {
          data: { brandPieData: seriesData }
        } = this.wares
        const pieChartData = {
          title: '库存数量占比',
          seriesData
        }
        if (!this.waresBrandPieDom) {
          this.waresBrandPieDom = echarts.init(this.$refs.waresBrandPie)
        }
        this.waresBrandPieDom.clear()
        createPieChart(this.waresBrandPieDom, pieChartData)
      },
      // 仓库滞销预警柱状图
      renderColumnChat7() {
        const {
          data: { legendData, seriesData },
          company,
          month,
          depot
        } = this.delay
        const merchantIds = company.length ? company.toString() : ''
        const depotIds = depot.length ? depot.toString() : ''
        const chartData = {
          tooltipData: {
            type: 'delay'
          },
          legendData,
          yAxisData: [
            '0~30天',
            '30天~60天',
            '60天~90天',
            '90天~120天',
            '120天以上'
          ],
          seriesData,
          fetchData: { merchantIds, depotIds, month: month[0] + '~' + month[1] }
        }
        if (!this.delayColumnDom) {
          this.delayColumnDom = echarts.init(this.$refs.delayColumn)
        }
        this.delayColumnDom.clear()
        createStackColumnChart(this.delayColumnDom, chartData)
      },
      // 仓库效期预警柱状图
      renderColumnChat8() {
        const {
          data: { yAxisData, seriesData }
        } = this.expire
        const chartData = {
          tooltipData: {
            type: 'expire'
          },
          // yAxisData: ['1/10以下及过期', '1/10-1/5', '1/5-1/3', '1/3-1/2', '1/2以上'],
          // legendData,
          legendData: [
            '1/10以下及过期',
            '1/10-1/5',
            '1/5-1/3',
            '1/3-1/2',
            '1/2以上'
          ],
          yAxisData,
          seriesData
        }
        if (!this.expireColumnDom) {
          this.expireColumnDom = echarts.init(this.$refs.expireColumn)
        }
        this.expireColumnDom.clear()
        createStackColumnChart(this.expireColumnDom, chartData)
      },
      // 导出列表
      exportList(type) {
        switch (type) {
          case 'achieve': {
            const { departmentId, month } = this.achieve
            this.$exportFile(exportTargetAndAchieveUrl, { departmentId, month })
            break
          }
          case 'annual': {
            const { buId, month, isParentBrand, brand } = this.annual
            const brandIds = brand.length ? brand.toString() : ''
            this.$exportFile(exportYearFinanceInventoryAnalysisUrl, {
              buId,
              month,
              isParentBrand,
              brandIds
            })
            break
          }
          case 'brand': {
            const { buId, month, isParentBrand } = this.brand
            this.$exportFile(exportBrandUrl, {
              buId,
              month: month[0] + '~' + month[1],
              isParentBrand
            })
            break
          }
          case 'customer': {
            const { buId, month, channel } = this.customer
            const channelType = channel.length < 2 ? channel.toString() : ''
            this.$exportFile(exportgetCustomersUrl, {
              buId,
              month: month[0] + '~' + month[1],
              channelType
            })
            break
          }
          case 'stock': {
            const { buId, month, channel } = this.stock
            const type = channel.length ? channel.toString() : ''
            this.$exportFile(exportInventoryAnalysisUrl, { buId, month, type })
            break
          }
          case 'wares': {
            const { buId, month } = this.wares
            this.$exportFile(exportInWarehouseDaysUrl, { buId, month })
            break
          }
          case 'delay': {
            const { month, company, depot } = this.delay
            const merchantIds = company.length ? company.toString() : ''
            const depotIds = depot.length ? depot.toString() : ''
            this.$exportFile(exportDepotUnsellableWarningUrl, {
              month: month[0] + '~' + month[1],
              merchantIds,
              depotIds
            })
            break
          }
          case 'expire': {
            const { month, company, brand } = this.expire
            const merchantIds = company.length ? company.toString() : ''
            const brandIds = brand.length ? brand.toString() : ''
            this.$exportFile(exportDepotExpireWarningUrl, {
              month: month[0] + '~' + month[1],
              merchantIds,
              brandIds
            })
            break
          }
        }
      },
      changeCheckbox(type, val) {
        switch (type) {
          case 'annual': {
            const checkedCount = val.length
            this.annual.checkAll = checkedCount === this.annual.brandList.length
            this.annual.isIndeterminate =
              checkedCount > 0 && checkedCount < this.annual.brandList.length
            this.fetchAnnualChartData()
            break
          }
          case 'customer': {
            const checkedCount = val.length
            this.customer.checkAll =
              checkedCount === this.customer.channelList.length
            this.customer.isIndeterminate =
              checkedCount > 0 &&
              checkedCount < this.customer.channelList.length
            this.fetchCustomerChartData()
            break
          }
          case 'delayCompany': {
            const checkedCount = val.length
            this.delay.companyCheckAll =
              checkedCount === this.delay.companyList.length
            this.delay.companyIsIndeterminate =
              checkedCount > 0 && checkedCount < this.delay.companyList.length
            this.fetchDelayChartData()
            break
          }
          case 'delayDepot': {
            const checkedCount = val.length
            this.delay.depotCheckAll =
              checkedCount === this.delay.depotList.length
            this.delay.depotIsIndeterminate =
              checkedCount > 0 && checkedCount < this.delay.depotList.length
            this.fetchDelayChartData()
            break
          }
          case 'expireCompany': {
            const checkedCount = val.length
            this.expire.companyCheckAll =
              checkedCount === this.expire.companyList.length
            this.expire.companyIsIndeterminate =
              checkedCount > 0 && checkedCount < this.expire.companyList.length
            this.fetchExpireChartData()
            break
          }
          case 'expireBrand': {
            const checkedCount = val.length
            this.expire.brandCheckAll =
              checkedCount === this.expire.brandList.length
            this.expire.brandIsIndeterminate =
              checkedCount > 0 && checkedCount < this.expire.brandList.length
            this.fetchExpireChartData()
            break
          }
        }
      },
      // 全选
      handleCheckAllChange(type, isCheckAll) {
        switch (type) {
          case 'annual': {
            this.annual.checkAll = isCheckAll
            if (isCheckAll) {
              const brandList = this.annual.brandList
              if (brandList && brandList.length) {
                const list = brandList.map((item) => item.brandId)
                this.annual.brand = list
              }
              this.annual.isIndeterminate = false
            } else {
              this.annual.brand = []
            }
            this.fetchAnnualChartData()
            break
          }
          case 'customer': {
            this.customer.checkAll = isCheckAll
            if (isCheckAll) {
              const channelList = this.customer.channelList
              if (channelList && channelList.length) {
                const list = channelList.map((item) => item.label)
                this.customer.channel = list
              }
              this.customer.isIndeterminate = false
            } else {
              this.customer.channel = []
            }
            this.fetchCustomerChartData()
            break
          }
          case 'delayCompany': {
            this.delay.companyCheckAll = isCheckAll
            if (isCheckAll) {
              const companyList = this.delay.companyList
              if (companyList && companyList.length) {
                const list = companyList.map((item) => item.id)
                this.delay.company = list
              }
              this.delay.companyIsIndeterminate = false
            } else {
              this.delay.company = []
            }
            this.fetchDelayChartData()
            break
          }
          case 'delayDepot': {
            this.delay.depotCheckAll = isCheckAll
            if (isCheckAll) {
              const depotList = this.delay.depotList
              if (depotList && depotList.length) {
                const list = depotList.map((item) => item.label)
                this.delay.depot = list
              }
              this.delay.depotIsIndeterminate = false
            } else {
              this.delay.depot = []
            }
            this.fetchDelayChartData()
            break
          }
          case 'expireCompany': {
            this.delay.companyCheckAll = isCheckAll
            if (isCheckAll) {
              const companyList = this.expire.companyList
              if (companyList && companyList.length) {
                const list = companyList.map((item) => item.id)
                this.expire.company = list
              }
              this.expire.companyIsIndeterminate = false
            } else {
              this.expire.company = []
            }
            this.fetchExpireChartData()
            break
          }
          case 'expireBrand': {
            this.expire.brandCheckAll = isCheckAll
            if (isCheckAll) {
              const brandList = this.expire.brandList
              if (brandList && brandList.length) {
                const list = brandList.map((item) => item.label)
                this.expire.brand = list
              }
              this.expire.brandIsIndeterminate = false
            } else {
              this.expire.brand = []
            }
            this.fetchExpireChartData()
            break
          }
        }
      },
      isParentBrandChange() {
        this.annual.brand = []
        this.fetchAnnualChartData()
      }
    }
  }
</script>

<style lang="scss" scoped>
  .container {
    display: flex;
    &__left {
      flex: 1;
    }
    &__right {
      width: 230px;
      margin-left: 10px;
    }
  }

  .container__right {
    position: relative;
    .nav {
      position: fixed;
      right: 15px;
      top: 120px;
    }
  }

  .nav {
    background: #fff;
    position: absolute;
    top: 0;
    right: 0;
    &__item {
      border-bottom: 1px solid #f3f6fa;
      padding: 16px 10px 16px 26px;
      font-size: 18px;
      color: #3488ca;
      cursor: pointer;
    }
    &__item--active {
      background-color: #0069e0;
      color: #fff;
    }
  }

  .tab {
    display: flex;
    justify-content: space-between;
    &__content {
      display: flex;
      flex-wrap: wrap;
      > li {
        min-width: 120px;
        padding: 0 6px;
        line-height: 50px;
        color: #2d353e;
        text-align: center;
        background: #f2f3f9;
        border-radius: 10px 10px 0px 0px;
        margin-right: 2px;
        cursor: pointer;
        margin-bottom: 6px;
        font-size: 14px;
      }
      > li.active {
        background-color: #0069e0;
        color: #fff;
      }
    }
    &__export {
      background-color: #0069e0;
      color: #fff;
      height: 36px;
      width: 120px;
      text-align: center;
      line-height: 36px;
      cursor: pointer;
      border-radius: 18px;
      > i {
        font-size: 20px;
        vertical-align: text-bottom;
      }
    }
  }
  .right__tab {
    background-color: #fff;
    line-height: 40px;
    position: absolute;
    right: 0;
    top: 0;
    display: flex;
    align-items: center;
    cursor: pointer;
    z-index: 2;
    > li {
      color: #757b80;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      cursor: pointer;
      border: 1px solid #0069e0;
      border-radius: 20px;
      padding-left: 16px;
      padding-right: 16px;
    }
    > li:first-child {
      padding-right: 95px;
    }
    > li:last-child {
      padding-left: 95px;
    }
    > li.active:first-child {
      padding-right: 16px;
      margin-right: -90px;
    }
    > li.active:last-child {
      margin-left: -90px;
      padding-left: 16px;
    }
    > li.active {
      background-color: #0069e0;
      color: #fff;
    }
  }

  li {
    list-style: none;
  }

  .brand-close {
    height: 30px;
    overflow-y: hidden;
    overflow-x: hidden;
  }

  .container {
    overflow: scroll;
    height: 100%;
  }

  ::v-deep.el-card {
    overflow: visible !important;
  }
</style>
