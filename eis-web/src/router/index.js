import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/subProt/document',
    component: () => import('@/views/prot/sonProt/document'),
    name: 'Document',
    hidden: true,
    meta: { title: 'Document' }
  },
  {
    path: '/subProt/toolingTrackList',
    component: () => import('@/views/prot/sonProt/toolingTrackingList'),
    name: 'ToolingTrackList',
    hidden: true,
    meta: { title: 'Tooling Tracking List' }
  },
  {
    path: '/subProt/toolingPlan',
    component: () => import('@/views/prot/sonProt/toolingPlan'),
    name: 'ToolingPlan',
    hidden: true,
    meta: { title: 'Tooling Plan' }
  },
  {
    path: '/subProt/mePartList',
    component: () => import('@/views/prot/sonProt/mePartList'),
    name: 'MePartList',
    hidden: true,
    meta: { title: 'Me Part List' }
  },
  {
    path: '/subProt/pilotrunReport',
    component: () => import('@/views/prot/sonProt/pilotrunReport'),
    name: 'PilotrunReport',
    hidden: true,
    meta: { title: 'Pilot Run Report' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'Dashboard', icon: 'dashboard', affix: true, noCache: true }
      }
    ]
  },
  {
    path: '/protStatus',
    component: Layout,
    redirect: '/protStatus/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/protsummary/protSummary'),
        name: 'protStatus',
        meta: { title: 'Project Status', icon: 'skill', noCache: true }
      }
    ]
  },
  {
    path: '/prot',
    component: Layout,
    redirect: '/prot/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/prot/mainProt'),
        name: 'Prot',
        meta: { title: '项目管理', icon: 'edit', noCache: true }
      },
      {
        path: 'subProt',
        component: () => import('@/views/prot/sonProt'),
        name: 'subProt',
        hidden: true,
        meta: { title: '子项目管理', icon: 'guide', noCache: true }
      },
      {
        path: 'protMember',
        component: () => import('@/views/prot/protMember'),
        name: 'protMember',
        hidden: true,
        meta: { title: '项目成员管理', icon: 'guide', noCache: true }
      }
    ]
  },
  {
    path: '/presupposition',
    component: Layout,
    redirect: '/presupposition/moldType',
    alwaysShow: true,
    name: 'presupposition',
    meta: {
      title: '预设项',
      icon: 'list',
      noCache: true
    },
    children: [
      {
        path: 'moldType',
        component: () => import('@/views/presupposition/moldType'),
        name: 'moldType',
        meta: {
          title: '模具种类',
          noCache: true
        }
      },
      {
        path: 'moldNumber',
        component: () => import('@/views/presupposition/moldNumber'),
        name: 'moldNumber',
        meta: {
          title: '模穴数',
          noCache: true
        }
      },
      {
        path: 'materialClassification',
        component: () => import('@/views/presupposition/materialClassification'),
        name: 'materialClassification',
        meta: {
          title: '材料分类',
          noCache: true
        }
      },
      {
        path: 'partNumber',
        component: () => import('@/views/presupposition/partNumber'),
        name: 'partNumber',
        meta: {
          title: 'Part Number',
          noCache: true
        }
      }
    ]
  },
  {
    path: '/dept',
    component: Layout,
    redirect: '/dept/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/dept/index'),
        name: 'Dept',
        meta: { title: '部门管理', icon: 'guide', noCache: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/user/index'),
        name: 'User',
        meta: { title: '用户管理', icon: 'user', noCache: true }
      }
    ]
  },
  {
    path: '/subProtCon',
    redirect: '/subProtCon/index',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/prot/sonProt/sonProtCon'),
        name: 'subProtCon',
        meta: { title: '子项目内容管理', noCache: true }
      }
    ]
  },
  // {
  //   path: '/subProtCon',
  //   component: () => import('@/views/prot/sonProt/sonProtCon'),
  //   hidden: true,
  //   name: 'subProtCon'
  // },
  // {
  //   path: '/prot',
  //   component: Layout,
  //   hidden: true,
  //   children: [
  //     {
  //       path: 'subProt',
  //       component: () => import('@/views/prot/sonProt'),
  //       name: 'subProt',
  //       hidden: true,
  //       meta: { title: '子项目管理', icon: 'guide', noCache: true }
  //     }
  //   ]
  // },
  {
    path: '/changePassword',
    component: Layout,
    redirect: '/changePassword/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/user/changePassword'),
        name: 'ChangePassword',
        meta: { title: 'ChangePassword', icon: 'user', noCache: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  // {
  //   path: '/permission',
  //   component: Layout,
  //   redirect: '/permission/page',
  //   alwaysShow: true, // will always show the root menu
  //   name: 'Permission',
  //   meta: {
  //     title: 'Permission',
  //     icon: 'lock',
  //     roles: ['ROOT', 'editor'] // you can set roles in root nav
  //   },
  //   children: [
  //     {
  //       path: 'page',
  //       component: () => import('@/views/permission/page'),
  //       name: 'PagePermission',
  //       meta: {
  //         title: 'Page Permission',
  //         roles: ['ROOT'] // or you can only set roles in sub nav
  //       }
  //     },
  //     {
  //       path: 'directive',
  //       component: () => import('@/views/permission/directive'),
  //       name: 'DirectivePermission',
  //       meta: {
  //         title: 'Directive Permission'
  //         // if do not set roles, means: this page does not require permission
  //       }
  //     },
  //     {
  //       path: 'role',
  //       component: () => import('@/views/permission/role'),
  //       name: 'RolePermission',
  //       meta: {
  //         title: 'Role Permission',
  //         roles: ['ROOT']
  //       }
  //     }
  //   ]
  // },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
