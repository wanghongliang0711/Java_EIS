import { axios } from '@/utils/request'

// 查询项目MePartList 表
export function selectBySonProtIdAndVersion(query) {
  return axios({
    url: '/protMePartList/find',
    method: 'post',
    data: query
  })
}

// 查询 partNum 表 中所有PartNumber 用作提示
export function selectAllPartNumberTips() {
  return axios({
    url: '/partNum/selectAll',
    method: 'post'
  })
}

// 查询 Material 表 中所有 Material 用作提示
export function selectAllMaterialTips() {
  return axios({
    url: '/matelClass/selectAll',
    method: 'post'
  })
}

// 添加 Me Part List
export function addMePartList(data) {
  return axios({
    url: '/protMePartList/add',
    method: 'post',
    data: data
  })
}

// 修改 Me Part List
export function editProtMePartList(data) {
  return axios({
    url: '/protMePartList/edit',
    method: 'post',
    data: data
  })
}

// 删除 Me Part List
export function deleteProtMePartList(id, parentId) {
  return axios({
    url: '/protMePartList/deleteById/' + id + '/' + parentId,
    method: 'get'
  })
}

// 修改now 版本 approve
export function editProtFileVer(data) {
  return axios({
    url: '/protFileVer/add',
    method: 'post',
    data: data
  })
}

// 修改now 版本 approve
export function selectProtFileVer(data) {
  return axios({
    url: '/protFileVer/find',
    method: 'post',
    data: data
  })
}

// 生成版本
export function addVersion(data) {
  return axios({
    url: '/protMePartList/addVersion',
    method: 'post',
    data: data
  })
}

// 匹配 PartNumber
export function matchPartNumber(sonProtId, parentId) {
  return axios({
    url: '/protMePartList/matchPartNum/' + sonProtId + '/' + parentId,
    method: 'get'
  })
}

// 匹配 PartNumber
export function backBeforeVersion(data) {
  return axios({
    url: '/protMePartList/backVersion',
    method: 'post',
    data: data
  })
}

// 匹配 PartNumber
export function overDescription(data) {
  return axios({
    url: '/protMePartList/overDes',
    method: 'post',
    data: data
  })
}

// 下载 PartNumber
export function downloadMePartlist(data) {
  return axios({
    url: '/protMePartList/downloadMePartList',
    method: 'post',
    data: data
  })
}
