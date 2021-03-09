import { axios } from '@/utils/request'

// 查询 模具种类 列表
export function listModeType(query) {
  return axios({
    url: '/moldType/queryByType',
    method: 'post',
    data: query
  })
}

// 添加 模具种类 列表
export function addModeType(query) {
  return axios({
    url: '/moldType/addMoldType',
    method: 'post',
    data: query
  })
}
// 删除 模具种类 列表
export function deleteModeType(id) {
  return axios({
    url: '/moldType/deleteMoldType/' + id,
    method: 'get'
  })
}

// 修改 模具种类 列表
export function updateModeType(data) {
  return axios({
    url: '/moldType/edit',
    method: 'post',
    data: data
  })
}

// 查询 PartNum 列表
export function listPartNum(query) {
  return axios({
    url: '/partNum/queryByNumAndDes',
    method: 'post',
    data: query
  })
}

// 删除 PartNum 列表
export function deletePartNum(id) {
  return axios({
    url: '/partNum/deletePartNum/' + id,
    method: 'get'
  })
}

// 添加 PartNum 列表
export function addPartNum(data) {
  return axios({
    url: '/partNum/addPartNum',
    method: 'post',
    data: data
  })
}

// 修改 PartNum 列表
export function updatePartNum(data) {
  return axios({
    url: '/partNum/edit',
    method: 'post',
    data: data
  })
}

// 查询 MoldNum 列表
export function listMoldNum(query) {
  return axios({
    url: '/moldNum/queryByMoldNum',
    method: 'post',
    data: query
  })
}

// 添加 MoldNum 列表
export function addMoldNum(data) {
  return axios({
    url: '/moldNum/addMoldNum',
    method: 'post',
    data: data
  })
}

// 修改 MoldNum 列表
export function updateMoldNum(data) {
  return axios({
    url: '/moldNum/edit',
    method: 'post',
    data: data
  })
}

// 删除 MoldNum 列表
export function deleteMoldNum(id) {
  return axios({
    url: '/moldNum/deleteMoldNum/' + id,
    method: 'get'
  })
}

// 添加 matelClass 材料种类 列表
export function addMaterial(data) {
  return axios({
    url: '/matelClass/addMaterial',
    method: 'post',
    data: data
  })
}

// 添加 品牌/型号 材料种类 列表
export function addBrand(data) {
  return axios({
    url: '/matelClass/addBrand',
    method: 'post',
    data: data
  })
}

// 查询 MoldNum 列表
export function listMaterial(query) {
  return axios({
    url: '/matelClass/queryByMaterial',
    method: 'post',
    data: query
  })
}

// 查询 材料分类 根据一级往下查找 列表
export function listOneMaterial(query) {
  return axios({
    url: '/matelClass/queryByOneMaterial',
    method: 'post',
    data: query
  })
}

// 修改 Material 列表
export function updateMaterial(data) {
  return axios({
    url: '/matelClass/edit',
    method: 'post',
    data: data
  })
}

// 修改 品牌/型号
export function updateBrand(data) {
  return axios({
    url: '/matelClass/editBrand',
    method: 'post',
    data: data
  })
}

// 删除 MoldNum 列表
export function deleteMaterial(id) {
  return axios({
    url: '/matelClass/deleteMatelClass/' + id,
    method: 'get'
  })
}
