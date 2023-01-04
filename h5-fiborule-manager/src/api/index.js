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
// 节点移动
export const nodeMoveLocation = (params) => request.post('/rule/engineNode/moveLocation',params)
// 节点删除
export const nodeDelete = (params) => request.post('/rule/engineNode/nodeDelete',params)
// 连线
export const connectLine = (params) => request.post('/rule/engineNode/connectLine',params)
// 获取节点配置列表
export const getNodeConfigList = (params) => request.post('/rule/engineNode/listNodesByType',params)
// 引擎发布
export const engineRelease = (params) => request.post('/rule/engine/engineRelease',params)
// // 引擎场景值
// export const getSceneList = (params) => request.post('/rule/engine/getSceneList',params)
// // 引擎场景值
// export const engineDelete = (params) => request.post('/rule/engine/delete',params)