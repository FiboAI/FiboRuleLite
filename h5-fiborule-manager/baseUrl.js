// console.log(process.env)
var proxyObj = {}
// console.log(process.env.VUE_PROXY)
switch (process.env.NODE_ENV) {
	case 'development': // 个人服务器后端地址
		proxyObj = {
			'/rule': {
				target: 'http://localhost:8080', // 个人服务器后端地址 - Riskmanage
				changeOrigin: true, // 是否跨域
				pathRewrite: {
					'^/rule': '/rule'
				}
			},
		}
		break
	
}


try{
	// baseUrl2 中的内容为 斐波那契公司内部使用的服务器地址信息 如测试服务器 demo服务器
	// 此部分信息为避免数据污染 不能对外公示
	// 此处已经 try catch 没有此文件不影响系统运行
	// 个人服务器请配置在上方 development 中
 let a = require('./baseUrl2')
 proxyObj = a()
}catch(err){

}

console.log(proxyObj)

module.exports = proxyObj
