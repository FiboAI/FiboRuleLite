import { getEngineDetail, nodeEdit, nodeMoveLocation, nodeDelete ,connectLine } from '@/api'
export default {

    methods: {
        // 获取节点列表
        getNodeList() {
            getEngineDetail({
                engineId: this.engineId
            }).then(res => {


                // 引擎信息
                this.engineInfo = res.data.engine
                // 节点信息
                let nodeCodeMax = 0
                // 渲染节点
                res.data.nodesDetail.forEach(nodes => {

                    let newNodeConfig = JSON.parse(JSON.stringify(this.nodeList.find(x => x.nodeType == nodes.nodeType)))


                    // 拿到最大的 nodeCode 
                    if (Number(nodes.nodeCode.split('_')[1]) > nodeCodeMax) {
                        nodeCodeMax = Number(nodes.nodeCode.split('_')[1])
                    }

                    newNodeConfig = Object.assign(newNodeConfig, {
                        nodeName: nodes.nodeName,
                        nodeX: nodes.nodeX,
                        nodeY: nodes.nodeY,
                        disabledRequest: true,
                        id: nodes.id,
                        nodeCode: nodes.nodeCode,
                    })
                    nodes.nodeClazz&&(newNodeConfig.nodeClazz =  nodes.nodeClazz)
                    nodes.nodeConfig&&(newNodeConfig.nodeConfig =  JSON.parse(nodes.nodeConfig))
                    nodes.nextNodeValue&&(newNodeConfig.nextConfig =  JSON.parse(nodes.nextNodeValue))
                    // console.log(newNodeConfig)
                    this.addNode(newNodeConfig, 0, nodes.nodeGroup)
                });

                // nodeCode迭代从当前节点中最大的数字开始
                this.nodeCodeNumber = nodeCodeMax
                
                
                let startNode = this.Layer.children.find(x=>x.userData.nodeType==1)
                this.Layer.centerBy(startNode)

               
                this.Layer.translateCenterTo(this.Layer.getCenter().x-500,this.Layer.getCenter().y)

                // 渲染连线
                this.initAddLink(res.data.nodesDetail)
                

            })

        },
        // 添加节点的请求 
        requestAddNode(node) {
            console.log(node)
            nodeEdit({
                "engineId": this.engineId,
                "nodeName": node.userData.nodeName,
                "nodeType": node.userData.nodeType,
                "nodeX": node.x,
                "nodeY": node.y,
                "nodeGroup": node.userData && node.userData.pairRandom,
                "nodeCode": node.userData.nodeCode
            }).then(res=>{
                // console.log(node)
                node.userData.id = res.data.id
            })
        },
        // 设置节点的请求 
        requestSetNode(node,callBack) {
            console.log(node)
            let params = {
                "nodeId":node.userData.id,
                "nodeName": node.userData.nodeName,
                "nodeConfig":JSON.stringify(node.userData.nodeConfig),
                "nodeClazz":node.userData.nodeClazz,
                // 
                "engineId": this.engineId,
                "nodeType": node.userData.nodeType,
                "nodeX": node.x,
                "nodeY": node.y,
                "nodeGroup": node.userData && node.userData.pairRandom,
                "nodeCode": node.userData.nodeCode
            }
            node.userData.nextConfig&&(params.nextNodeValue = JSON.stringify(node.userData.nextConfig))

            nodeEdit(params).then(res=>{
                callBack&&callBack()
            })
        },
        // 节点移动的请求
        nodeDragRequest(node) {
            nodeMoveLocation({
                "nodeId": node.userData.id,
                "nodeX": node.x,
                "nodeY": node.y
            })
        },
        // 删除节点的请求
        deleteNodeRequest(node) {
            nodeDelete({
                id: node.userData.id
            })
        },
        // 添加线 删除线 （同一方法）
        addAndDeleteLinkRequest(link) {

            // console.log(link)

            let begin = link.begin.object
            let end = link.end.object


            let params = {
                nodes:[{
                    id: begin.userData.id,
                    nextNodes: this.getNextNode(begin).join(','),
                    preNodes: this.getPreNode(begin).join(','),
                }, {
                    id: end.userData.id,
                    nextNodes: this.getNextNode(end).join(','),
                    preNodes: this.getPreNode(end).join(','),
                }]
            }
            if(link.userData&&link.userData.lastEnd&&link.userData.lastEnd!=end){
                let lastEnd = link.userData.lastEnd
                params.nodes.push({
                    id: lastEnd.userData.id,
                    nextNodes: this.getNextNode(lastEnd).join(','),
                    preNodes: this.getPreNode(lastEnd).join(','),
                })
            }

            connectLine(params)

        },

    }




}