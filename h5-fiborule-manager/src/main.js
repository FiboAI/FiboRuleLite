import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/class.less'
import defData from '@/utils/class'
Vue.use(ElementUI, { size: 'mini' });
Vue.config.productionTip = false
Vue.prototype.defData = defData
Date.prototype.format = function(fmt = 'yyyy-MM-dd hh:mm:ss') {
	var o = {
		"M+": this.getMonth() + 1, //月份 
		"d+": this.getDate(), //日 
		"h+": this.getHours(), //小时 
		"m+": this.getMinutes(), //分 
		"s+": this.getSeconds(), //秒 
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		"S": this.getMilliseconds() //毫秒 
	};
	if (/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
}

Array.prototype.deleteValue = function(value){
	let index = this.indexOf(value)
	if(index!=-1){
		this.splice(index,1)
	}
	return this
}

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
