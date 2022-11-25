<template>
    <div>

        <div class="nodeList" :style="{ height: contentHeight }">
            节点列表
            <div v-for="item in nodeList" :class="'node nodeType' + item.nodeType"
                style="cursor:pointer;user-select: none;" @mousedown="nodeMouesDown($event, item)">
                {{ item.nodeName }}
            </div>
        </div>
        <!-- 引擎 -->
        <div id="canvas" :style="{ height: contentHeight, width: contentWidth }" />
        <div v-if="addNodeStatus" :class="'tempNodeShow node nodeType' + addNodeTempShow.nodeType"
            :style="{ top: addNodeY, left: addNodeX }">
            {{ addNodeTempShow.nodeName }}
        </div>
        <!-- <div class="startLine" v-show="showStartLine" @mouseout="closeStartLine"
            :style="{ top: startLineTop, left: startLineLeft, height: startLineHeight, width: startLineWidth }">


        </div> -->
    </div>
</template>
<script>
import nodeList from './nodeList'
import addNode from './mixin/addNode'
import link from './mixin/link'

export default {
    mixins: [link, addNode],
    data() {
        return {
            contentWidth: '100vw',
            contentHeight: '100vh',
            canvas: null,
            stage: null,
            Layer: null,
            eventLayer: null,
            hoverNode: null,
            nodeList,
            mycanvas: null
        }
    },
    created() {
        console.log(this.nodeList)
    },
    mounted() {

        this.initTopo()
        this.mycanvas = document.querySelector('#canvas')
        window.onresize = (e) => {
            this.contentWidth = window.innerWidth + 'px'
            this.contentHeight = window.innerHeight - 70 + 'px'
        }
        window.onresize()
    },
    methods: {

        initTopo() { //jtopo初始化

            this.Stage = new jtopo.Stage('canvas');
            this.Layer = new jtopo.Layer('def');
            this.Layer.frames = 60;

            this.Stage.wheelZoom = null;
            this.Layer.setBackground('url(./img/decisionBcg.jpg)', '100% 100%')
            this.Layer.setStyles({
                'lineWidth': 8,
                'strokeStyle': '#E1E1E1',
                'font': '12px arial',
                'shadowColor': '#E1E1E1',
                'shadowBlur': 5,
                'shadowOffsetX': 3,
                'shadowOffsetY': 3,
            });
            this.Layer.removeChild = function(obj){
                let index = this.children.findIndex(x=>x===obj)
                this.children.splice(index,1)
            }

            this.Stage.addChild(this.Layer)

            this.Stage.on('mouseup', (e) => {
                if (this.addNodeStatus) (
                    this.addNode(e)
                )
                if(this.linkStatus){
                    this.closeLink()
                }
            })

            this.hoverNodeInit()

            // this.Stage.zoom(0, 0)
            // this.Stage.zoomFullStage()

            this.Stage.hideToolbar()
            this.Stage.show()


        },
        hoverNodeInit() {
            this.eventLayer = new jtopo.Layer('eventLayer');

            this.hoverNode = new jtopo.Node('');
            var hoverNode = this.hoverNode
            hoverNode.setXY(100, 50);
            hoverNode.resizeTo(40, 40);
            hoverNode.setStyles({
                'strokeStyle': 'black',
                'lineWidth': 2,
                'lineDash': [6, 2]
            });
            hoverNode.draggable = false
            hoverNode.visible = false
            this.eventLayer.addChild(hoverNode)
            this.Stage.addChild(this.eventLayer)



            // 节点可以组合，下面创建一个TipNode，作为另外一个节点的角标
            var tipNode = new jtopo.CircleNode('3');
            tipNode.setStyles({
                fillStyle: 'red', // 填充颜色：红色
                textPosition: 'center', // 文本位置：居中
                textBaseline: 'middle', // 文本定位基线，参考:html5-canvas API
                fontColor: 'white' // 文本颜色
            });
            tipNode.setRadius(8);
            tipNode.draggable = false;
            hoverNode.addChild(tipNode);
            tipNode.on('mouseenter', () => {
                this.mycanvas.style.cursor = 'crosshair'
            })
            tipNode.on('mouseout', () => {
                this.mycanvas.style.cursor = 'default'
            })
            tipNode.on('mousedown', () => {
                this.tempLink()
            })

        }

    }
}




</script>


<style scoped>
.nodeList {
    position: absolute;
    left: 0;
    /* height: 500px; */
    width: 300px;
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
</style>