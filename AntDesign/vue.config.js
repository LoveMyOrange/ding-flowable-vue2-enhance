const debug = process.env.NODE_ENV !== 'production';
module.exports = {
  pages: {
    index: {
      // 页面入口
      entry: 'examples/main.js',
      // 模板来源
      template: 'pubilc/index.html',
      // 输出文件名
      filename: 'index.html',
    },
  },
  productionSourceMap: false,
  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          // "primary-color": "#13c2c2",
          'primary-color': '#ff8126',
          'background-color': '#ff8126',
          'layout-color': '#9867f7',
          // "layout-color": "#ee88aa"
        },
        javascriptEnabled: true,
      },
    },
  },
  configureWebpack: (config) => {
    // 开发环境配置
    if (debug) {
      config.devtool = 'source-map';
    } else {
      // lib时 vue配置externals(打包优化)
      /* config.externals = {
        axios: 'axios',
        vue: 'Vue',
        vuex: 'Vuex',
        'vue-router': 'VueRouter',
        html2canvas: 'html2canvas',
        'vue-uuid': 'uuid',
      }; */
    }
  },
  devServer: {
    port: 83,
    proxy: {
      '/api': {
        target: process.env.VUE_APP_API_BASE_URL,
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '^/api': '', // 需要rewrite的,
        },
      },
    },
  },
};
