import request from '../utils/request';
// ======================================App
// app列表
export const AppList = (params) => request.post('/rule/app/list',params)
// app添加编辑
export const AppEdit = (params) => request.post('/rule/app/edit',params)
// app删除
export const AppDelete = (params) => request.post('/rule/app/delete',params)

// =====================================引擎列表

// 引擎列表
export const engineList = (params) => request.post('/rule/engine/list',params)
// 引擎添加编辑
export const engineEdit = (params) => request.post('/rule/engine/edit',params)
// 引擎场景值
export const getSceneList = (params) => request.post('/rule/engine/getSceneList',params)
// 引擎场景值
export const engineDelete = (params) => request.post('/rule/engine/delete',params)


// =====================================引擎

// 引擎详情 （节点列表）
export const getEngineDetail = (params) => request.post('/rule/engine/getEngineDetail',params)
// 节点添加编辑
export const nodeEdit = (params) => request.post('/rule/engineNode/nodeEdit',params)
// // 引擎场景值
// export const getSceneList = (params) => request.post('/rule/engine/getSceneList',params)
// // 引擎场景值
// export const engineDelete = (params) => request.post('/rule/engine/delete',params)