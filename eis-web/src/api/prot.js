import { axios } from '@/utils/request'

// 查询项目 表
export function listProt(query) {
  return axios({
    // url: '/prot/queryByNameAndStatus',
    url: '/prot/findMainProt',
    method: 'post',
    data: query
  })
}

// 查询所有 项目 用于 提示
export function selectAllProtForTips() {
  return axios({
    url: '/prot/selectAllProt',
    method: 'post'
  })
}

// 获取所有的 这个项目 对应的子项目 用于提示
export function selectAllSonProtForTips(protId) {
  return axios({
    url: '/prot/selectAllSonProt/' + protId,
    method: 'post'
  })
}

// 新增项目表
export function addProt(data) {
  return axios({
    url: '/prot/add',
    method: 'post',
    data: data
  })
}

// 新增主项目表
export function addRootProt(data) {
  return axios({
    url: '/prot/addRootProt',
    method: 'post',
    data: data
  })
}

// 新增子项目表
export function addSonProt(data) {
  return axios({
    url: '/prot/addSonProt',
    method: 'post',
    data: data
  })
}

// 修改项目表
export function updateProt(data) {
  return axios({
    url: '/prot/edit',
    method: 'post',
    data: data
  })
}

// 删除主项目
export function deleteRootProtById(id) {
  return axios({
    url: '/prot/deleteRootProt/' + id,
    method: 'get'
  })
}

// 删除子项目
export function deleteSonProt(id, parentId) {
  return axios({
    url: '/prot/deleteSonProt/' + id + '/' + parentId,
    method: 'get'
  })
}

// 查看项目成员
export function findByProtId(id) {
  return axios({
    url: '/userProt/findByProtId/' + id,
    method: 'get'
  })
}

// 删除项目成员
export function deleteByUserIdProtId(data) {
  return axios({
    url: '/userProt/deleteByUserIdProtId',
    method: 'post',
    data: data
  })
}

// 添加项目成员的 list
export function addMemList(protId) {
  return axios({
    url: '/userProt/addMemList/' + protId,
    method: 'get'
  })
}

// 改变项目管理员 列表
export function changeOwnerList(protId) {
  return axios({
    url: '/userProt/changeOwnerList/' + protId,
    method: 'get'
  })
}

// 添加项目成员
export function addMember(data) {
  return axios({
    url: '/userProt/add',
    method: 'post',
    data: data
  })
}

// 更改项目管理员
export function changeOwner(data) {
  return axios({
    url: '/userProt/changeOwner',
    method: 'post',
    data: data
  })
}

// 通过id查询 项目
export function selectProtByID(id) {
  return axios({
    url: '/prot/find/' + id,
    method: 'get'
  })
}

// 通过 主项目id查询 所有子项目
export function selectSubProtByMainId(id) {
  return axios({
    url: '/prot/findByPid/' + id,
    method: 'get'
  })
}

// 查询 子项目 Excel文件
export function getSubProtExcel(data) {
  return axios({
    url: '/protFile/find',
    method: 'post',
    data: data
  })
}

// 查询 Test 表
export function listTest(id) {
  return axios({
    url: '/part-test/find/' + id,
    method: 'post'
  })
}

// 修改 Test 表
export function updateTest(data) {
  return axios({
    url: '/part-test/edit',
    method: 'post',
    data: data
  })
}

// 修改 Test 表
export function download(param) {
  return axios({
    url: '/protFile/download/' + param,
    method: 'get'
  })
}

// 关注项目
export function addUserFollowProt(protId) {
  return axios({
    url: '/userFollowProt/add/' + protId,
    method: 'get'
  })
}

// 取消关注项目
export function deleteUserFollowByUserIdProtId(protId) {
  return axios({
    url: '/userFollowProt/delete/' + protId,
    method: 'get'
  })
}

// 只查看关注的项目
export function selectFollowByUserId() {
  return axios({
    url: '/prot/selectFollowByUserId',
    method: 'post'
  })
}
