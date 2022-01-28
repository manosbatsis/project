import { alertError, alertWarning } from '@u/common'
import config from '@u/config'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import Vue from 'vue'
import Router from 'vue-router'
import MenuList from './menu-route'
import store from '@/store'
import { getBaseUrl, createATagClick, getUserAndPermission } from '@u/tool'
Vue.use(Router)
NProgress.configure({ showSpinner: false })
const routes = [
  {
    path: '/',
    redirect: '/Home'
  },
  {
    path: '/login',
    component: () => import('@/layout/login/index')
  },
  {
    path: '/404',
    component: () => import('@/layout/404/index')
  },
  {
    path: '*',
    component: () => import('@/layout/404/index')
  },
  ...MenuList
]
const router = new Router({
  mode: 'history',
  routes
})

// Vue Loading chunk {n} failed 报错处理
router.onError((error) => {
  console.log('router Error', error)
  const pattern = /Loading chunk (\d)+ failed/g
  const isChunkLoadFailed = error.message.match(pattern)
  if (isChunkLoadFailed) {
    alertWarning('版本已更新，需重新加载', () => {
      window.location.reload()
    })
  }
})

// 全局路由守卫
router.beforeEach(async (to, from, next) => {
  NProgress.start()
  const { path } = to
  if (config.whiteRoutes.includes(path)) {
    // 白名单path 直接通过
    next()
  } else {
    const token = sessionStorage.getItem('token') // 获取token
    if (!token) {
      // 未登录
      alertError('用户未登录，请先登录', () => {
        // 生产环境去单点登录系统(system跟路劲)，本地开发去/login
        if (process.env.NODE_ENV !== 'development') {
          createATagClick(getBaseUrl('/webapi/system/login/logout'))
        } else {
          next('/login')
        }
      })
    } else {
      // 等待权限以及用户信息加载
      await getUserAndPermission(store)
      // 权限判断
      const havePermission =
        !!store.getters.menuUrlMap.get(to.path) ||
        !!store.getters.menuUrlMap.get(to.meta.routeParentUrl) ||
        to.name === 'Home' ||
        to.path.includes('common')
      if (!havePermission) {
        alertError('没有访问' + to.meta.title + '权限')
        NProgress.done()
        next(from.path === '/' ? '/Home' : false)
        return
      }
      // 将token从链接中消掉，防止用户将token也保存进书签中
      if (to.query.token) {
        const queryParam = { ...to.query }
        delete queryParam.token
        next({ ...to, query: queryParam })
        return
      }
      next()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
export default router
