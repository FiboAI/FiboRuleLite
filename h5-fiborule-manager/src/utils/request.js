import axios from 'axios'
import router from '../router';
import ElementUI from 'element-ui';
import bus from '@/components/common/bus.js'
var url = window.location.origin || window.location.protocol + '//' + window.location.hostname + (window.location.port ?
	':' + window.location.port : '')


const storeUrl = [{
		regetcache: 'decisionTable',
		url: [
			'/v3/decisionTables/addDecisionTables',
			'/v3/decisionTables/updateDecisionTables',
			'/v3/decisionTables/updateDecisionTablesStatus',
			'/v3/decisionTables/version/addVersion',
			'/v3/decisionTables/version/copyVersion',
			'/v3/decisionTables/version/updateVersion',
			'/v3/decisionTables/version/updateVersionStatus',
		]
	}, {
		regetcache: 'Engine',
		url: [
			'/v2/engine/update'
		]
	}, {
		regetcache: 'Interface',
		url: [
			'/v3/interface/addInterface',
			'/v3/interface/updateInterface',
			'/v3/interface/deleteInterface'
		]
	},
	{
		regetcache: 'SCO',
		url: [
			'/v3/scorecard/updateStatus',
			'/v3/scorecard/add',
			'/v3/scorecardVersion/addScorecardVersion',
			'/v3/scorecardVersion/updateScorecardVersionStatus',
			'/v3/scorecardVersion/copyScorecardVersion',
			'/v3/scorecardVersion/updateScorecardVersion',
			'/v3/scorecard/update'
		]
	},
	{
		regetcache: 'decisionTree',
		url: [
			'/v3/decisionTree/addDecisionTree',
			'/v3/decisionTree/updateDecisionTree',
			'/v3/decisionTree/updateDecisionTreeStatus',
			'/v3/decisionTree/version/addVersion',
			'/v3/decisionTree/version/copyVersion',
			'/v3/decisionTree/version/updateVersion',
			'/v3/decisionTree/version/updateVersionStatus',
		]
	},
	{
		regetcache: 'Sourcelist',
		url: [
			'/datasource/save',
			'/datasource/',
			'/datasource/update'
		]
	},{
		regetcache: 'BlackWhiteList',
		url: [
			'/datamanage/listmanage/save',
			'/datamanage/listmanage/update',
			'/datamanage/listmanage/updateStatus',
			'/datamanage/listmanage/upload/'
		]
	},
	// {
	// 	regetcache: 'listOperation',
	// 	url: [
	// 		'/v3/listOperation/addListOperation',
	// 		'/v3/listOperation/updateListOperation',
	// 		'/v3/listOperation/updateListOperationStatus',
	// 		'/v3/listOperation/version/addVersion',
	// 		'/v3/listOperation/version/copyVersion',
	// 		'/v3/listOperation/version/updateVersion',
	// 		'/v3/listOperation/version/updateVersionStatus',
	// 	]
	// },



]








const instance = axios.create({
	baseURL: process.env.NODE_ENV.indexOf(['produce', 'release', 'test', 'development', 'jia', 'niu',
		'wang']) != -1 ? '/' : url,
	timeout: 500000,
})



instance.interceptors.request.use((config) => {
	// config.headers['AAA'] = 'AAA';

	// console.log(config)
	deepTirm(config)

	if (config.data) {
		if (config.data.getexcel) {
			config.headers['responseType'] = 'blob'
		}
	}
	if (localStorage.getItem('token')) {
		config.headers['token'] = localStorage.getItem('token');
	}
	return config
})
instance.interceptors.response.use((response) => {
	const {
		data,
		config
	} = response


	let result = data
	// console.log(config)
	if (config.url == 'http://192.168.50.77/Riskmanage/v2/datamanage/listmanage/downTemplate/7') {
		downLoad(response)
		return response
	}
	if (response.data.status === "0") {
		if (response.data.error === "01000103") {
			if (document.getElementsByClassName('el-message').length === 0) {
				errorMessage(response.data.msg);
				router.push({
					path: '/login',
				})
			}

		} else {
			errorMessage(response.data.msg);
		}
	} else {

		storeUrl.forEach(value => {

			let is = false
			value.url.forEach(item => {
				let str = response.request.responseURL
				if (item[item.length - 1] == '/') {
					let arr = str.split('/')
					if (!isNaN(Number(arr[arr.length - 1]))) {
						arr.pop()
						str = arr.join('/') + '/'
					}
				}

				str = str.substring(str.length - item.length, str.length)
				if (item == str) {

					bus.$emit('regetcache', value.regetcache)
				}


			})
		})




	}


	return result
}, (error) => {
	// console.log('error', error)
	if (error.message.match(/timeout/)) {
		errorMessage('请求超时,请稍后再试！');
	} else if(error.response== undefined){
		errorMessage('连接失败,请稍后再试！');
	} else if (error.response.status === 500) {
		errorMessage('连接失败,请稍后再试！');
	} else if (error.response.status === 502) {
		errorMessage('网关超时,请稍后再试！');
	} else {
		errorMessage('连接失败,请稍后再试！');
	}
	return Promise.reject(error)
});





var arr = []

// 报错拦截 两秒钟以内
function errorMessage(str) {
	if (arr.indexOf(str) === -1) {
		ElementUI.Message.error(str)
	}
	arr.push(str)
	setTimeout(() => {
		arr = arr.filter(x => x !== str)
	}, 2000)
}








function deepTirm(e) {
	Object.keys(e).forEach(value => {
		if (typeof e[value] == 'string' && (e[value][0] === " " || e[value][e[value].length - 1] === " ")) {
			e[value] = e[value].trim()
		}
		if (isJSON(e[value])) {
			e[value] = JSON.stringify(deepTirm(JSON.parse(e[value])))
		}
		if (typeof e[value] === "object" && e[value] !== null) {
			if (Array.isArray(e[value])) {
				e[value].forEach(item => {
					if (typeof item === 'string' && (item[0] === " " || item[item.length - 1] ===
							" ")) {
						item = item.trim()
					}
					if (typeof item === 'object') {
						item = deepTirm(item)
					}
				})
			} else {
				deepTirm(e[value])
			}
		}
	})
	return e
}

function isJSON(str) {
	if (typeof str == 'string') {
		try {
			var obj = JSON.parse(str);
			if (typeof obj == 'object' && obj) {
				return true;
			} else {
				return false;
			}

		} catch (e) {
			return false;
		}
	}
}

function downLoad(res) {
	const blob = new Blob([res]);
	const fileName = decodeURI(res.headers['content-disposition'].split('=')[1]);

	if ('download' in document.createElement('a')) { // 非IE下载
		const elink = document.createElement('a');
		elink.download = fileName;
		elink.style.display = 'none';
		elink.href = URL.createObjectURL(blob);
		document.body.appendChild(elink);
		elink.click();
		URL.revokeObjectURL(elink.href); // 释放URL 对象
		document.body.removeChild(elink);
	} else { // IE10+下载
		navigator.msSaveBlob(blob, fileName);
	}
}

export default instance
