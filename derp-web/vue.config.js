const path = require('path')
const resolve = (dir) => path.join(__dirname, dir)
const CopyWebpackPlugin = require('copy-webpack-plugin')
const SpeedMeasurePlugin = require('speed-measure-webpack-plugin')
const HardSourceWebpackPlugin = require('hard-source-webpack-plugin')
// 是否是生产模式
const isProduction = process.env.NODE_ENV === 'production'

console.log('vue.config.js  NODE_ENV ：', process.env.NODE_ENV)
console.log('vue.config.js VUE_APP_ENV：', process.env.VUE_APP_ENV)
module.exports = {
  devServer: {
    proxy: {
      '/webapi/order/transfer/': {
        target: 'http://121.33.205.118:3202', // 业务 'http://192.168.28.9:9020',
        changeOrigin: true
      },
      '/webapi/order/': {
        target: 'http://121.33.205.118:3202', // 业务 'http://192.168.28.9:9020',
        changeOrigin: true
      },
      '/webapi/storage/': {
        target: 'http://121.33.205.118:3203', // 仓储
        changeOrigin: true
      },
      '/webapi/system/': {
        // target: 'http://192.168.28.11:9010',
        target: 'http://121.33.205.118:810', // 主业 192.168.28.9
        changeOrigin: true
      },
      '/webapi/inventory/': {
        target: 'http://121.33.205.118:3201', // 库存
        changeOrigin: true
      },
      '/webapi/transfer/': {
        target: 'http://121.33.205.118:3202', // 业务 'http://192.168.28.9:9020',
        changeOrigin: true
      },
      '/webapi/report/': {
        target: 'http://121.33.205.118:3205', // 报表
        changeOrigin: true
      }
    }
  },
  // 去除生产环境的productionSourceMap
  productionSourceMap: false,
  css: {
    loaderOptions: {
      css: {},
      postcss: {},
      sass: {},
      scss: {}
    }
  },
  chainWebpack: (config) => {
    // 路径解析
    config.resolve.alias
      .set('@', resolve('src'))
      .set('@c', resolve('src/components'))
      .set('@sta', resolve('static'))
      .set('@v', resolve('src/views'))
      .set('@u', resolve('src/utils'))
      .set('@m', resolve('src/mixins'))
      .set('@a', resolve('src/api'))

    if (isProduction) {
      // 打包后复制WEB - INF
      config
        .plugin('CopyWebpackPlugin')
        .use(new CopyWebpackPlugin([{ from: 'WEB-INF/', to: 'WEB-INF/' }]))
      // 时间统计
      config.plugin('speed').use(SpeedMeasurePlugin)
    }

    // 打包加速
    config.plugin('hardSource').use(HardSourceWebpackPlugin)
    // 对下面配置的 module 不进行缓存
    config
      .plugin('hardSourceExcludeModule')
      .use(HardSourceWebpackPlugin.ExcludeModulePlugin, [
        {
          test: /mini-css-extract-plugin[\\/]dist[\\/]loader/
        }
      ])
  }
}
