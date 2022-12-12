import axios from 'axios'
import router from '../router';
import ElementUI from 'element-ui';
var url = window.location.origin || window.location.protocol + '//' + window.location.hostname + (window.location.port ?
	':' + window.location.port : '')








const instance = axios.create({
	baseURL: url,
	timeout: 500000,
})



instance.interceptors.request.use((config) => {


	if (config.data) {
		if (config.data.getexcel) {
			config.headers['responseType'] = 'blob'
		}
	}
	// if (localStorage.getItem('token')) {
	// 	config.headers['token'] = localStorage.getItem('token');
	// }
	return config
})
instance.interceptors.response.use((response) => {
	const {
		data,
		config
	} = response


	let result = data

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
			throw 'err'
		}
	}else{
		return result
	}


	
}, (error) => {
	// console.log('error', error)
	if (error.message.match(/timeout/)) {
		errorMessage('请求超时,请稍后再试！');
	} else if (error.response == undefined) {
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
