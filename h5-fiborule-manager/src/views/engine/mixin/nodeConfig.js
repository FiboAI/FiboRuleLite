import {getNodeConfigList} from '@/api'
export default{
    data(){
        return {
            // 当前选中的节点
            clickNode :null,
            // 当前选中的节点的userData 的深拷贝
            tempCurrNodeUserData:null,
            // 组件列表
            moduleList:[],
            // NodeConfig缓存
            NodeConfig:{}
        }
    },
    methods:{
        setNodeConfig(e){
            // console.log(e)
            Object.assign(this.clickNode.userData,e)
            // console.log(this.clickNode)
            this.clickNode.setStyles('strokeStyle',"#666")
            this.requestSetNode(this.clickNode)
        },
        getNodeConfigList(node){
            this.clickNode = node
            this.tempCurrNodeUserData = JSON.parse(JSON.stringify(node.userData))

            if(this.NodeConfig[node.userData.nodeType]){
                this.moduleList =this.NodeConfig[node.userData.nodeType]
                return
            }

            getNodeConfigList({
                nodeType:node.userData.nodeType,
                engineId:this.engineId,
                // scene:'mock场景',
                // "appId": 1,
            }).then(res=>{
                this.moduleList = res.data
                this.NodeConfig[node.userData.nodeType] = res.data
            })


        }




    }
}