import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/dashboard'
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/dashboard',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/icon',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/page/Icon.vue'),
                    meta: { title: '自定义图标' }
                },
                {
                    path: '/user',
                    component: () => import(/* webpackChunkName: "tabs" */ '../components/page/BaseTable.vue'),
                    meta: { title: '用户管理' }
                },
                {
                    path: '/project',
                    component: () => import(/* webpackChunkName: "tabs" */ '../components/page/ProjectTable.vue'),
                    meta: { title: '项目管理' }
                },
                {
                    path: '/order',
                    component: () => import(/* webpackChunkName: "form" */ '../components/page/OrderTable.vue'),
                    meta: { title: '订单管理' }
                },
                {
                    // 富文本编辑器组件
                    path: '/webset',
                    component: () => import(/* webpackChunkName: "editor" */ '../components/page/BaseForm.vue'),
                    meta: { title: '系统管理' }
                },
                {
                    // 富文本编辑器组件
                    path: '/apiset',
                    component: () => import(/* webpackChunkName: "editor" */ '../components/page/ApiForm.vue'),
                    meta: { title: 'Api相关接口设置' }
                },
                {
                    // 富文本编辑器组件
                    path: '/pwdset',
                    component: () => import(/* webpackChunkName: "editor" */ '../components/page/PwdForm.vue'),
                    meta: { title: '密码管理' }
                },
                {
                    path: '/CSE',
                    component: () => import(/* webpackChunkName: "editor" */ '../components/page/CSE.vue'),
                    meta: { title: '客服系统' }
                },
                {
                    path: '/404',
                    component: () => import(/* webpackChunkName: "404" */ '../components/page/404.vue'),
                    meta: { title: '404' }
                },
                {
                    path: '/403',
                    component: () => import(/* webpackChunkName: "403" */ '../components/page/403.vue'),
                    meta: { title: '403' }
                }
            ]
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../components/page/Login.vue'),
            meta: { title: '登录' }
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
});
