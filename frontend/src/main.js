/*
 * @Author: your name
 * @Date: 2020-04-27 21:54:30
 * @LastEditTime: 2020-04-28 00:22:13
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /frontend/src/main.js
 */
import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VueRouter from 'vue-router'
import Chat from './components/Chat.vue'
import dashboard from './components/Dashboard.vue'

Vue.use(ElementUI);

Vue.use(VueRouter);

const routes = [{
    path: '/chat',
    component: Chat
},{
    path: '/',
    component: dashboard
},
{
    path: '/dashboard',
    component: dashboard,
    meta: { title: '系统首页' }
}

];

const router = new VueRouter({
    routes
});

Vue.config.productionTip = false;

new Vue({
    router,
    render: h => h(App),
}).$mount('#app');
