const { defineConfig } = require('@vue/cli-service')
const { resolve } = require('path')
const path = require('path')
const proxyObj = require('./baseUrl')
module.exports = defineConfig({
  publicPath: './',
  // transpileDependencies: true,
  devServer:{
    // contentBase: path.join(__dirname, `../public/`),
    host: '0.0.0.0', // 'lhl.zcsmart.com', // 'lhl.zcsmart.com',//'lhl.zcsmart.com',//'localhost',
    proxy: process.env.OPEN_PROXY === false ? {} : proxyObj,
		port: 8888,
		open: false,
  },
  configureWebpack:{
    resolve:{
      alias:{
        'c':resolve('src/components')
      }
    }
  }
})
