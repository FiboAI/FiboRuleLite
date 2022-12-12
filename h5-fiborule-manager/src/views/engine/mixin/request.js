import {getEngineDetail , nodeEdit} from '@/api'
export default{

    methods:{
        getNodeList(){
            getEngineDetail({
                engineId:this.engineId
            })
        },
        requestAddNode(node){
            console.log(node)
            nodeEdit({
                "engineId": this.engineId,
                "nodeGroup": "",
                "nodeName":node.userData.nodeName,
                "nodeType": node.userData.nodeType,
                "nodeX": node.x,
                "nodeY": node.y,
              })
        }
    }




}