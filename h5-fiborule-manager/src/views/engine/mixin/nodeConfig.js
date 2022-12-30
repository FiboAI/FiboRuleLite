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
    created(){

        getNodeConfigList({
            nodeType:5,
            engineId:this.$route.params.engineId,
        }).then(res=>{
            this.NodeConfig[5] = res.data
        })

    },
    methods:{

        // 切换nodeClazz要断掉已有所有连线 以及 清除 nextConfig
        changeNodeClazz(){
            

            if (this.clickNode.inLinks) {
                let number = this.clickNode.inLinks.length
                for (let i = 0; i < number; i++) {
                    this.deleteLink(this.clickNode.inLinks[0])
                }
            }
            
            if (this.clickNode.outLinks) {
                let number = this.clickNode.outLinks.length
                for (let i = 0; i < number; i++) {
                    console.log(i)
                    this.deleteLink(this.clickNode.outLinks[0])
                }
            }
            this.clickNode.userData.nextConfig = []

        },
        // 设置节点的 nodeConif （配置信息） 以及 nextConfig(次级节点配置信息)
        setNodeConfig(e){
            Object.assign(this.clickNode.userData,e)
            this.clickNode.setStyles('strokeStyle',"#666")
            console.log(this.moduleList,e.nodeClazz)
            let module = this.moduleList.find(x=>x.nodeClazz==e.nodeClazz)
            if(this.clickNode.userData.nextNodeType&&module&&module.branchMap){
                // console.log()
                this.clickNode.userData.nextNodeType = this.$getArrayByMap(module.branchMap)
            }
            this.clickNode.text = this.nodeTextFormet(e.nodeName)
            if(e.nodeName.length>12){
                this.clickNode.textOffsetY =this.clickNode.userData.__textOffsetY -6
            }else{
                this.clickNode.textOffsetY =this.clickNode.userData.__textOffsetY
            }
            this.requestSetNode(this.clickNode,()=>{
                this.$message.success('提交成功')
                this.tempCurrNodeUserData = this.clickNode.userData
            })
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