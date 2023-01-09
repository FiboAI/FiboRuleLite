<template>
    <div style="position: relative;overflow: hidden;">

        <div class="nodeList" :style="{ height: contentHeight }">
            <!-- 节点列表 -->
            <!-- :class="'node nodeType' + item.nodeType" -->
            <div v-for="item in nodeList" v-if="item.listShow"
                style="cursor:pointer;user-select: none;text-align: center;" @mousedown="nodeMouesDown($event, item)">
                <!-- {{ item.nodeName }} -->
                <img :src="'./img/nodeimg/nodeType' + item.nodeType + '.png'" alt=""
                    style="height: 65px;margin-top: 20px;">
            </div>
        </div>
        <!-- 引擎 -->
        <div id="canvas" :style="{ height: contentHeight, width: contentWidth }" />
        <div v-if="addNodeStatus" :class="'tempNodeShow'" :style="{ top: addNodeY, left: addNodeX }">
            <img :src="'./img/nodeimg/nodeType' + addNodeTempShow.nodeType + '.png'" alt=""
                style="height: 65px;margin-top: 20px;">
        </div>

        <div class="nodeConfig" :style="{ height: contentHeight }" v-if="tempCurrNodeUserData">
            <!-- {{clickNode&&clickNode.nodeName}} -->
            <!-- <SwitchIfGeneral :data="tempCurrNodeUserData" :moduleList="moduleList" @setNodeConfig="setNodeConfig" @changeNodeClazz="changeNodeClazz" /> -->
            <component :is="tempCurrNodeUserData.configVue" :data="tempCurrNodeUserData" :moduleList="moduleList"
                @setNodeConfig="setNodeConfig" @changeNodeClazz="changeNodeClazz" />
        </div>

        <div class="engineFunctionMenu" :style="{ top: engineInfo && engineInfo.bootStatus == 1 ? '5px' : '-180px' }">
            <div :class="engineInfo && engineInfo.bootStatus == 1 ? 'releaseButton undeploy' : 'releaseButton deploy'"
                @click="EngineRelease">
                {{ engineInfo && engineInfo.bootStatus == 1 ? '取消部署' : '部署' }}
            </div>
            <div style="display: flex;justify-content: space-between;padding: 5px;margin-top: -50px;">
                <!--  -->
                <el-upload action="/" :auto-upload="false" :on-change="engineImport" :show-file-list="false" :disabled="engineInfo && engineInfo.bootStatus == 1">
                    <el-button type="primary" :disabled="engineInfo && engineInfo.bootStatus == 1">导入</el-button>
                </el-upload>
                <el-button type="primary" @click="engineExport">导出</el-button>

            </div>

        </div>
        <div class="engineFunctionMenuModel" v-if="engineInfo && engineInfo.bootStatus == 1">

        </div>

        <el-dialog title="请选择连线的模式" :visible.sync="LinkTypeSelectDialog" width="500px" :close-on-press-escape="false"
            :show-close="false" :close-on-click-modal="false">
            <el-button v-for="LinkType in LinkTypeList" type="primary" size="medium" @click="setLinkType(LinkType)"
                :disabled="LinkType.disabled">{{ LinkType.label }}</el-button>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelSelectLinkType">取消连线</el-button>
            </span>
        </el-dialog>


    </div>
</template>
<script>
import nodeList from './nodeList'
import node from './mixin/node'
import link from './mixin/link'
import request from './mixin/request'
import nodeConfig from './mixin/nodeConfig'
import SwitchIfGeneral from './nodeConfig/SwitchIfGeneral.vue'
import NoConfig from './nodeConfig/NoConfig.vue'
import { engineRelease, engineImport, engineExport } from '@/api'
export default {
    mixins: [link, node, request, nodeConfig],
    components: { SwitchIfGeneral, NoConfig },
    data() {
        return {
            contentWidth: '100vw',
            contentHeight: '100vh',
            canvas: null,
            stage: null,
            Layer: null,
            // eventLayer: null,
            hoverNode: null,
            nodeList,
            mycanvas: null,
            scale: 1,
            engineId: 0,
            engineInfo: null
        }
    },
    created() {
        this.engineId = this.$route.params.engineId
    },
    mounted() {

        this.initTopo()
        this.getNodeList()
        this.mycanvas = document.querySelector('#canvas')
        window.onresize = (e) => {
            this.contentWidth = window.innerWidth + 'px'
            this.contentHeight = window.innerHeight - 70 + 'px'
        }
        window.onresize()
        window.onkeydown = this.debounce((e) => {
            if (e.key == 'Alt' && this.Stage.mode != 'edit') {
                this.Stage.setMode('edit')
                this.hoverNode.hide()
                this.LinkhoverNode.hide()
                this.closeLinkMouseEnabled()
            }
        })
        window.onclick = (e) => {
            if (!window.event.altKey) {
                this.Stage.setMode('normal')
                this.openLinkMouseEnabled()
            }
        }
        window.onkeyup = (e) => {
            if (e.key == 'Alt') {
                this.Stage.setMode('normal')
                this.openLinkMouseEnabled()
                //  this.hoverNode.show()
            }
        }

        this.mycanvas.onwheel = (e) => {
            let evt = e || window.event;  //考虑兼容性
            // evt.preventDefault();
            if (evt.deltaY > 0) {
                // console.log("向下滚动");
                // this.scale -= 0.1
                this.Stage.zoomOut()

            } else {
                // console.log("向上滚动");
                // this.scale += 0.1
                this.Stage.zoomIn()
            }
            // console.log(this.Stage.getMousePoint())
            // console.log(this.Stage.getMousePoint().x,this.Stage.getMousePoint().y,this.scale,this.scale)
            // this.Stage.zoom(this.Stage.getMousePoint().x,this.Stage.getMousePoint().y,1,1)
            // this.Layer.scaleX = this.scale
            // this.Layer.scaleY = this.scale
            // console.log(this.Layer.getCenter())
            // this.eventLayer.scaleX = this.scale
            // this.eventLayer.scaleY = this.scale
        }
    },
    methods: {
        engineExport() {
            engineExport({
                "engineId": this.engineId
            }).then(res => {

                let arr = res.data.nodesDetail.map(value => {
                    delete value.id
                    delete value.engineId
                    return value
                })
                download(`引擎导出【${this.engineId}】.json`, JSON.stringify(arr));


                function download(filename, text) {
                    var element = document.createElement('a');
                    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
                    element.setAttribute('download', filename);

                    element.style.display = 'none';
                    document.body.appendChild(element);

                    element.click();
                    document.body.removeChild(element);
                }

            })
        },
        engineImport(e) {
            console.log(e)
            
            getActionData(e.raw).then(res=>{
                // console.log()
                engineImport({
                    engine:{
                        id:this.engineId
                    },
                    nodesDetail:JSON.parse(res)
                }).then(res=>{
                    location.reload()
                })
            })

           function getActionData(file) {
               return new Promise(res=>{
                var reader = new FileReader()// 新建一个FileReader
                reader.readAsText(file, 'UTF-8')// 读取文件
                reader.onload = function (evt) { // 读取完文件之后会回来这里
                    var fileString = evt.target.result // 读取文件内容
                   res(fileString)
                }
               })
            }

        },
        EngineReleaseVerify() {
            //============ 查找是否有应该要配置的节点 未配置
            if (this.Layer.children.filter(x => !x.isLink && !x.userData.isHoverNode).find(node => {
                return node.userData.haveConfig && !node.userData.nodeClazz
            })) {
                this.$message.error('有节点未配置完毕')
                return true
            }

            // ================================  判断 并行 和 聚合 的嵌套关系是否正确
            const nestRelationfn = (node, arr) => {

                // 聚合节点
                if (node.userData.nodeType == 7) {
                    let str = arr.pop()
                    console.log(2222, str, arr)
                    if (str !== node.userData.pairRandom) {
                        nestRelation = true
                    }
                    // 并行节点
                } else if (node.userData.nodeType == 6) {
                    arr.push(node.userData.pairRandom)
                }
                // console.log(arr)

                let NextArr = this.getNextNode(node, 'array')

                if (NextArr.length) {
                    NextArr.forEach(value => {
                        nestRelationfn(value, JSON.parse(JSON.stringify(arr)))
                    })
                } else {
                    if (arr.length) {
                        nestRelation = true
                    }
                }



            }

            // console.log(endNode)
            let nestRelation = false
            // let aaaarr = []
            let startNode = this.Layer.children.find(x => x.userData.nodeType == 1)
            nestRelationfn(startNode, [])
            if (nestRelation) {
                this.$message.error('并行聚合的嵌套关系不正确')
                return true
            }


        },
        // 部署
        EngineRelease() {
            // 部署前先验证
            if (this.EngineReleaseVerify()) {
                return
            }

            console.log('部署')
            engineRelease({
                engineId: this.engineId,
                bootStatus: this.engineInfo && this.engineInfo.bootStatus == 1 ? 2 : 1
            }).then(res => {
                this.engineInfo.bootStatus = this.engineInfo && this.engineInfo.bootStatus == 1 ? 2 : 1
                this.$message.success('发布成功')
            })
        },
        //jtopo初始化
        initTopo() {

            this.Stage = new jtopo.Stage('canvas');
            this.Layer = new jtopo.Layer('def');
            this.Layer.frames = 60;



            this.Stage.wheelZoom = null;
            this.Layer.setBackground('url(./img/decisionBcg.jpg)', '100% 100%')

            // 重写删除子节点方法
            this.Layer.removeChild = function (obj) {
                let index = this.children.findIndex(x => x === obj)
                index != -1 && this.children.splice(index, 1)

                // 如果被删除的子节点是 连线节点
                if (obj.isLink) {
                    // 清除 连线两端的 节点的  outLinks inLinks 属性中的 本条线
                    let begin = obj.begin.object
                    let end = obj.end.object
                    begin.outLinks && begin.outLinks.splice(begin.outLinks.findIndex(x => x === obj), 1)
                    end.inLinks && end.inLinks.splice(end.inLinks.findIndex(x => x === obj), 1)
                }



            }

            this.Stage.addChild(this.Layer)

            this.Stage.on('mouseup', (e) => {
                if (this.addNodeStatus) (
                    this.addNode(this.addNodeTempShow)
                )
                if (this.linkStatus) {
                    this.closeLink()
                }
            })

            this.hoverNodeInit()
            this.LinkHoverNodeInit()

            // this.Stage.zoom(0, 0)
            // this.Stage.zoomFullStage()

            this.Stage.hideToolbar()
            this.Stage.show()



        },

        // 节流
        debounce(func, wait = 1, immediate) {
            var timeout;
            return function () {
                var context = this;
                var args = arguments;

                if (timeout) clearTimeout(timeout);
                // 是否在某一批事件中首次执行
                if (immediate) {
                    var callNow = !timeout;
                    timeout = setTimeout(function () {
                        timeout = null;
                        func.apply(context, args)
                        immediate = true;
                    }, wait);
                    if (callNow) {
                        func.apply(context, args)
                    }
                    immediate = false;
                } else {
                    timeout = setTimeout(function () {
                        func.apply(context, args);
                        immediate = true;
                    }, wait);
                }
            }
        }



    }
}




</script>


<style scoped lang="less">
.nodeList {
    position: absolute;
    left: 0;
    /* height: 500px; */
    width: 90px;
    background-color: #fff;
    box-shadow: 3px 3px 60px -12px;
    z-index: 1200;
}

.node {

    width: 100px;
    height: 50px;
    border: #000 2px solid;
    background-color: #000;
    border-radius: 4px;
}

.nodeType0 {
    border: #0f0 2px solid;
    background-color: #c3ffc3;
}

.nodeType1 {
    border: #00f 2px solid;
    background-color: #c2c2ff;
}

.tempNodeShow {
    position: fixed;
    z-index: 1201;
    -webkit-user-select: none;
    cursor: default;
    pointer-events: none;

}

.startLine {
    position: fixed;
    border: 1px solid #000;
    z-index: 1201;
    /* padding: 3px; */
    border-radius: 10px;
    pointer-events: none;
}

img {
    -webkit-user-drag: none;

}

.nodeConfig {
    width: 350px;
    position: absolute;
    right: 0;
    background-color: #fff;
    /* height: ; */
    top: 0;
    z-index: 1200;
    border-left: 1px solid #666;
}

@engineFunctionMenuWidth: 300px;

.engineFunctionMenu {
    width: @engineFunctionMenuWidth;
    height: 200px;
    background-color: #fff;
    position: absolute;

    border-radius: 10px;
    transition: all .2s;
    /* right: 0; */
    left: calc(50vw - @engineFunctionMenuWidth/2);
    /* margin: 0 auto; */
    z-index: 2000;
    border: #666 1px solid;
    @releaseButtonSize: 100px;

    &>.releaseButton {
        width: @releaseButtonSize;
        height: @releaseButtonSize;
        margin: calc((200px - @releaseButtonSize) /2) auto;

        border-radius: 50%;
        text-align: center;
        line-height: @releaseButtonSize;
        font-size: 22px;
        font-weight: bold;
        color: #fff;
    }

    &>.releaseButton:hover {

        cursor: pointer;
    }

    &>.deploy {
        background-color: #078607;
    }

    &>.deploy:hover {
        background-color: #045304;
    }

    &>.undeploy {
        background-color: #860707;

    }

    &>.undeploy:hover {
        background-color: #530404;
    }
}

.engineFunctionMenu:hover {
    top: 5px !important;
}

.engineFunctionMenuModel {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #00000066;
    z-index: 1201;
}
</style>