import { axios } from '@/utils/request'

// 查询部门列表
export function listDept(query) {
  return axios({
    url: '/dept/findByName',
    // url: 'http://127.0.0.1:7890/dev-api/dept/list',
    method: 'get',
    params: query
  })
}

// 新增部门
export function addDept(data) {
  return axios({
    url: '/dept/add',
    method: 'post',
    data: data
  })
}

// 删除部门
export function delDept(deptId) {
  return axios({
    url: '/dept/delete/' + deptId,
    method: 'get'
  })
}

// 通过id 查询部门详细
export function getDept(deptId) {
  return axios({
    url: '/dept/find/' + deptId,
    method: 'get'
  })
}

// 修改部门
export function updateDept(data) {
  return axios({
    url: '/dept/edit',
    method: 'post',
    data: data
  })
}

// 查询部门下拉树结构
export function treeselect() {
  return axios({
    url: '/dept/treeselect',
    method: 'get'
  })
}

