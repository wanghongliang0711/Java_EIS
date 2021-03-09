import { axios } from '@/utils/request'

export function getRoutes() {
  return axios({
    url: '/vue-element-admin/routes',
    method: 'get'
  })
}

export function getRoles() {
  return axios({
    url: '/vue-element-admin/roles',
    method: 'get'
  })
}

export function addRole(data) {
  return axios({
    url: '/vue-element-admin/role',
    method: 'post',
    data
  })
}

export function updateRole(id, data) {
  return axios({
    url: `/vue-element-admin/role/${id}`,
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return axios({
    url: `/vue-element-admin/role/${id}`,
    method: 'delete'
  })
}
