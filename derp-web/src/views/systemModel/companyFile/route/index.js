/**
 *  参数说明
 *  path 路由的完整路径 (必填)
 *  name 名称 可填
 *  component 组件 (必填)
 *  redirect 跳转路径 redirect不为空时,不显示二级菜单
 *  hide  是否不展现在菜单栏 可填
 *  meta
 *    title 显示在tag栏, 左侧菜单栏名称名称 (必填)
 *    affix 是否固定tab栏
 *    nocache 是否不需要缓存  是相当于keepAlive false
 */

// 公司档案
const route = {
  path: '/companyFile',
  name: 'companyFile',
  component: () => import('@/layout/index/index'),
  meta: {
    title: '公司档案',
    icon: 'menu-icon el-icon-coordinate'
  },
  children: [
    {
      path: '/companyFile/TaxRateConfig', // 路径
      component: () => import('@v/systemModel/companyFile/TaxRateConfig'), // 页面
      name: 'TaxRateConfig', // 名称
      meta: {
        title: '税率配置'
      }
    },
    {
      path: '/companyFile/tradeLinkConfigList', // 路径
      component: () => import('@v/systemModel/companyFile/tradeLinkConfigList'), // 页面
      name: 'tradeLinkConfigList', // 名称
      meta: {
        title: '交易链路配置'
      }
    },
    {
      path: '/companyFile/DepartmentManage', // 路径
      component: () => import('@v/systemModel/companyFile/DepartmentManage'), // 页面
      name: 'DepartmentManage', // 名称
      meta: {
        title: '部门管理'
      }
    },
    {
      path: '/companyFile/CompanyInfo', // 路径
      component: () => import('@v/systemModel/companyFile/CompanyInfo'), // 页面
      name: 'CompanyInfo', // 名称
      meta: {
        title: '公司信息'
      }
    },
    {
      path: '/companyFile/CompanyInfoAdd', // 路径
      component: () => import('@v/systemModel/companyFile/CompanyInfoEdit'), // 页面
      name: 'CompanyInfoAdd', // 名称
      hide: true,
      meta: {
        title: '公司信息新增',
        nocache: true,
        routeParentUrl: '/companyFile/CompanyInfo'
      }
    },
    {
      path: '/companyFile/CompanyInfoEdit', // 路径
      component: () => import('@v/systemModel/companyFile/CompanyInfoEdit'), // 页面
      name: 'CompanyInfoEdit', // 名称
      hide: true,
      meta: {
        title: '公司信息编辑',
        nocache: true,
        routeParentUrl: '/companyFile/CompanyInfo'
      }
    },
    {
      path: '/companyFile/CompanyInfoDetail', // 路径
      component: () => import('@v/systemModel/companyFile/CompanyInfoDetail'), // 页面
      name: 'CompanyInfoDetail', // 名称
      hide: true,
      meta: {
        title: '公司信息详情',
        nocache: true,
        routeParentUrl: '/companyFile/CompanyInfo'
      }
    },
    {
      path: '/companyFile/BuManage', // 路径
      component: () => import('@v/systemModel/companyFile/BuManage'), // 页面
      name: 'BuManage', // 名称
      meta: {
        title: '事业部管理'
      }
    },
    {
      path: '/companyFile/shopList', // 路径
      component: () => import('@v/systemModel/companyFile/shopList'), // 页面
      name: 'shopList', // 名称
      meta: {
        title: '店铺信息'
      }
    },
    {
      path: '/companyFile/addShop', // 路径
      component: () => import('@v/systemModel/companyFile/addShop'), // 页面
      name: 'addShop', // 名称
      hide: true,
      meta: {
        title: '新增店铺信息',
        nocache: true,
        routeParentUrl: '/companyFile/shopList'
      }
    },
    {
      path: '/companyFile/shopDetail', // 路径
      component: () => import('@v/systemModel/companyFile/shopDetail'), // 页面
      name: 'shopDetail', // 名称
      hide: true,
      meta: {
        title: '店铺信息详情',
        nocache: true,
        routeParentUrl: '/companyFile/shopList'
      }
    },
    {
      path: '/companyFile/CompanyBusiness', // 路径
      component: () => import('@v/systemModel/companyFile/CompanyBusiness'), // 页面
      name: 'CompanyBusiness', // 名称
      meta: {
        title: '公司事业部'
      }
    }
  ]
}

export default route
