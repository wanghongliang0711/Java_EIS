import { axios } from '@/utils/request'

export function login(data) {
  return axios({
    url: '/guest/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return axios({
    url: '/user/info',
    method: 'get'
  })
}

export function logout() {
  return axios({
    url: '/logout',
    method: 'get'
  })
}

// 查询用户列表
export function listUser(query) {
  return axios({
    url: '/admin/list',
    method: 'post',
    data: query
  })
}

// 新增用户
export function addUser(data) {
  return axios({
    url: '/admin/add',
    method: 'post',
    data: data
  })
}

// 删除用户
export function delUser(userId) {
  return axios({
    url: '/admin/delete',
    method: 'delete',
    params: userId
  })
}

// 批量删除用户
export function batchDeleteUser(param) {
  return axios({
    url: '/admin/batchDelete',
    method: 'delete',
    data: param
  })
}

// 用户密码重置
export function resetUserPwd(param) {
  return axios({
    url: '/admin/resetPwd/' + param,
    method: 'post'
  })
}

// 修改用户
export function updateUser(data) {
  return axios({
    url: '/admin/edit',
    method: 'post',
    data: data
  })
}

// 修改用户
export function getAllRole() {
  return axios({
    url: '/admin/role/all',
    method: 'post'
  })
}

// 修改用户角色
export function updateUserRole(data) {
  return axios({
    url: '/admin/assignRoles',
    method: 'post',
    data: data
  })
}

// 修改密码
export function changePassword(data) {
  return axios({
    url: '/user/changePassword',
    method: 'post',
    data: data
  })
}

