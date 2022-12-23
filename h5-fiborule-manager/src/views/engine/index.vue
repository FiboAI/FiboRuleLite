<template>
    <div style="position: relative;">

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
                style="height: 80px;margin-top: 20px;">
        </div>
        <!-- <div class="startLine" v-show="showStartLine" @mouseout="closeStartLine"
            :style="{ top: startLineTop, left: startLineLeft, height: startLineHeight, width: startLineWidth }">


        </div> -->
        <div class="nodeConfig" :style="{ height: contentHeight }" v-if="tempCurrNodeUserData">
            <!-- {{clickNode&&clickNode.nodeName}} -->
            <!-- <SwitchIfGeneral :data="tempCurrNodeUserData" :moduleList="moduleList" @setNodeConfig="setNodeConfig" @changeNodeClazz="changeNodeClazz" /> -->
            <component :is="tempCurrNodeUserData.configVue" :data="tempCurrNodeUserData" :moduleList="moduleList" @setNodeConfig="setNodeConfig" @changeNodeClazz="changeNodeClazz" />
        </div>

        <el-dialog title="请选择连线的模式" :visible.sync="LinkTypeSelectDialog" width="500px" :close-on-press-escape="false"
            :show-close="false" :close-on-click-modal="false">
            <el-button v-for="LinkType in LinkTypeList" type="primary" size="medium" @click="setLinkType(LinkType)" :disabled="LinkType.disabled">{{ LinkType.label }}</el-button>
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
export default {
    mixins: [link, node, request, nodeConfig],
    components: { SwitchIfGeneral ,NoConfig},
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
            engineId: 0
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
                this.scale -= 0.1
            } else {
                // console.log("向上滚动");
                this.scale += 0.1
            }
            this.Layer.scaleX = this.scale
            this.Layer.scaleY = this.scale
            // console.log(this.Layer.getCenter())
            // this.eventLayer.scaleX = this.scale
            // this.eventLayer.scaleY = this.scale
        }
    },
    methods: {
        openLinkMouseEnabled() {
            this.Layer.children.forEach(item => {
                if (item.isLink) {
                    item.mouseEnabled = true
                }
            });
        },
        closeLinkMouseEnabled() {
            this.Layer.children.forEach(item => {
                if (item.isLink) {
                    item.mouseEnabled = false
                }
            });
        },
        initTopo() { //jtopo初始化

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


<style scoped>
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
</style>