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
                    meta: { title: '个人主页' }
                },
                {
                    path: '/icon',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/page/Icon.vue'),
                    meta: { title: '自定义图标' }
                },
                {
                    path: '/projecthall',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/page/ProjectHall.vue'),
                    meta: { title: '项目大厅' }
                },
                {
                    path: '/myproject',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/page/MyProject.vue'),
                    meta: { title: '我的项目' }
                },
                {
                    path: '/myintroduce',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/page/MyIntroduce.vue'),
                    meta: { title: '个人简介' }
                },
                {
                    path: '/communicate',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/page/ContactList.vue'),
                    meta: { title: '即时通讯' }
                },
                {
                    path: '/infoedit',
                    component: () => import(/* webpackChunkName: "form" */ '../components/page/BaseForm.vue'),
                    meta: { title: '信息修改' }
                },
                {
                    path: '/CSE',
                    component: () => import(/* webpackChunkName: "markdown" */ '../components/page/CSE.vue'),
                    meta: { title: '在线客服' }
                },
                {
                    path: '/chat',
                    component: () => import(/* webpackChunkName: "markdown" */ '../components/page/Chat.vue'),
                    meta: { title: '聊天Ing' }
                },
                {
                    // markdown组件
                    path: '/pwdform',
                    component: () => import(/* webpackChunkName: "markdown" */ '../components/page/PwdForm.vue'),
                    meta: { title: '修改密码' }
                },
                {
                    // markdown组件
                    path: '/payment',
                    component: () => import(/* webpackChunkName: "markdown" */ '../components/page/PayMent.vue'),
                    meta: { title: '在线充值' }
                },
                {
                    path: '/addproject',
                    component: () => import(/* webpackChunkName: "drag" */ '../components/page/AddProject.vue'),
                    meta: { title: '项目添加' }
                },
                {
                    path: '/agreelist',
                    component: () => import(/* webpackChunkName: "drag" */ '../components/page/AgreeList.vue'),
                    meta: { title: '项目管理' }
                },
                {
                    // 拖拽列表组件
                    path: '/projectrequire',
                    component: () => import(/* webpackChunkName: "drag" */ '../components/page/DragList.vue'),
                    meta: { title: '项目需求' }
                },
                {
                    // 拖拽列表组件
                    path: '/tabs',
                    component: () => import(/* webpackChunkName: "drag" */ '../components/page/Tabs.vue'),
                    meta: { title: '消息列表' }
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
            path: '/reg',
            component: () => import(/* webpackChunkName: "login" */ '../components/page/Reg.vue'),
            meta: { title: '注册' }
        },


        {
            path: '*',
            redirect: '/404'
        }
    ]
});
