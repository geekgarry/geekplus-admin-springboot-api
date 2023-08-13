import request from '@/utils/request'

// 分页查询数据列表
export function list${className}(query) {
  return request({
    url: '${baseRequestMapping}/list',
    method: 'get',
    params: query
  })
}

// 查询单条数据
export function get${className}(query) {
  return request({
    url: '${baseRequestMapping}/detail',
    method: 'get',
    params: query
  })
}

// 删除单条数据
export function delete${className}(query) {
return request({
    url: '${baseRequestMapping}/delete',
    method: 'get',
    params: query
  })
}

// 批量删除数据
export function del${className}(ids) {
  return request({
    url: '${baseRequestMapping}/' + ids,
    method: 'delete'
  })
}

// 修改数据
export function update${className}(data) {
  return request({
    url: '${baseRequestMapping}/update',
    method: 'post',
    data: data
  })
}

// 添加数据
export function add${className}(data) {
  return request({
    url: '${baseRequestMapping}/add',
    method: 'post',
    data: data
  })
}

//导出数据列表
export function export${className}(query){
  return request({
    url: '${baseRequestMapping}/export',
    method: 'get',
    params: query
  })
}
