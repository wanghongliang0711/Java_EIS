import { axios } from '@/utils/request'

export function searchUser(name) {
  return axios({
    url: '/vue-element-admin/search/user',
    method: 'get',
    params: { name }
  })
}

export function transactionList(query) {
  return axios({
    url: '/vue-element-admin/transaction/list',
    method: 'get',
    params: query
  })
}
