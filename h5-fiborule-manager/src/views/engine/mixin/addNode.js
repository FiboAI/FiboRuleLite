export default {

    data() {
        return {
            addNodeStatus: false,
            addNodeX: 0,
            addNodeY: 0,
            addNodeTempShow: {},
        }
    },
    methods: {
        nodeMouesDown(e, node) {
            this.addNodeX = e.clientX - 50 + 'px'
            this.addNodeY = e.clientY - 45 + 'px'
            this.addNodeTempShow = node
            this.addNodeStatus = true

            window.onmousemove = (e) => {
                // console.log(e)
                this.addNodeX = e.clientX - 50 + 'px'
                this.addNodeY = e.clientY - 45 + 'px'
            }
            window.onmouseup = () => {
                this.addNodeStatus = false
                window.onmousemove = null
                window.onmouseup = null
            }


        },
        addNode(addNodeTempShow,deriveNode=0,pairRandom) {

            let coord = this.Layer.toLocalXY(window.event.pageX, window.event.pageY)
            coord.y -= 60
            var node

            let nodeStyle = {
                textPosition: 'center',
                fontColor: '#000',
                lineWidth: 3,
                'strokeStyle': '#666',
                'fillStyle':addNodeTempShow.fillStyle,
            }

            if (addNodeTempShow.shape == 'polyNode') {
                node = new jtopo.PolygonNode(addNodeTempShow.nodeName, coord.x - (addNodeTempShow.width / 2) +deriveNode*150, coord.y - addNodeTempShow.height / 2, addNodeTempShow.width, addNodeTempShow.height);
                node.setStyles(nodeStyle);
                node.textOffsetY = -3;
                node.edges = addNodeTempShow.edges;
                node.roundRadius = 6;

            } else if (addNodeTempShow.shape == 'rectangle') {
                node = new jtopo.Node(addNodeTempShow.nodeName, coord.x - (addNodeTempShow.width / 2)+deriveNode*150, coord.y - addNodeTempShow.height / 2, addNodeTempShow.width, addNodeTempShow.height);
                // 线型
                node.setStyles(nodeStyle)
                node.textOffsetY = -4;
                node.roundRadius = 6;
            } else if (addNodeTempShow.shape == 'circle') {
                node = new jtopo.CircleNode(addNodeTempShow.nodeName, coord.x - (addNodeTempShow.size / 2)+deriveNode*150, coord.y - addNodeTempShow.size / 2, addNodeTempShow.size);
                node.setStyles({ ...nodeStyle, textBaseline: 'middle' });
            }

            node.userData = JSON.parse(JSON.stringify(addNodeTempShow))
            if(pairRandom){
                node.userData.pairRandom = pairRandom
                let tip = this.newTipNode('',false)
                tip.setStyles({fillStyle:'#'+pairRandom})
                tip.setRadius(5)
                tip.translateCenterTo(node.width/2, node.height/1.2)
                node.addChild(tip)
            }
            node.selectedStyle = new jtopo.Style({
                'shadowColor': '#000',
                'shadowBlur': 1,
                'fontColor': 'red',
            })

            node.removeOutLink = function(link){
                let index = this.outLinks.findIndex(x=>x==link)
                this.outLinks.splice(index,1)
            }


            this.nodeAddEvent(node)



            
            // 并行节点特殊处理
            if(addNodeTempShow.deriveNode){
                if(!pairRandom){
                    pairRandom = Math.floor(Math.random()*16777215).toString(16)
                }
                // node.setStyles({'strokeStyle':'#'+pairRandom})
                let tip = this.newTipNode('',false)
                tip.setStyles({fillStyle:'#'+pairRandom})
                tip.setRadius(5)
                tip.translateCenterTo(node.width/2, node.height/1.2)
                node.addChild(tip)
                node.userData.pairRandom = pairRandom
                // console.log(pairRandom)
                this.addNode(this.nodeList.find(x=>x.nodeType==addNodeTempShow.deriveNode),++deriveNode,pairRandom)

            }

            this.Layer.addChild(node)
            this.requestAddNode(node)
        },
        nodeAddEvent(node) {

            node.on('mouseenter', (e) => {
                this.openStartLine(node)

            });
            node.on('mousedrag', this.debounce((e) => {
                this.openStartLine(node)
                if (node.inLinks) {
                    node.inLinks.forEach(inlink => {
                        let linkModel = this.getLinkModel(inlink.begin.object, inlink.end.object)
                        inlink.direction = linkModel.direction
                        // inlink.setEnd()
                        inlink.begin.position = linkModel.start
                        inlink.end.position = linkModel.end

                        if (inlink == this.currHoverLink) {
                            this.LinkhoverNode.hide()
                        }

                    });
                }
                if (node.outLinks) {
                    node.outLinks.forEach(outLink => {
                        let linkModel = this.getLinkModel(outLink.begin.object, outLink.end.object)
                        outLink.direction = linkModel.direction
                        outLink.begin.position = linkModel.start
                        outLink.end.position = linkModel.end
                    });
                }
            }));
        },
        hoverNodeInit() {
            this.eventLayer = new jtopo.Layer('eventLayer');

            this.hoverNode = this.newHoverNode();
            var hoverNode = this.hoverNode


            this.eventLayer.addChild(hoverNode)
            this.Stage.addChild(this.eventLayer)



            // 节点可以组合，下面创建一个TipNode，作为另外一个节点的角标
            var tipNode = this.newTipNode()
            tipNode.on('mousedown', () => {
                this.tempLink()
            })

            hoverNode.addChild(tipNode);
        },
        newHoverNode() {
            var hoverNode = new jtopo.Node('');

            hoverNode.setXY(100, 50);
            hoverNode.resizeTo(40, 40);
            hoverNode.setStyles({
                'strokeStyle': 'black',
                'lineWidth': 0.5,
                'lineDash': [6, 2]
            });
            hoverNode.showSelected = false
            hoverNode.draggable = false
            hoverNode.visible = false
            return hoverNode
        },
        newTipNode(text = '+',event = true) {
            var tipNode = new jtopo.CircleNode(text);
            tipNode.setStyles({
                fillStyle: '#aaaaaa', // 填充颜色：红色
                textPosition: 'center', // 文本位置：居中
                textBaseline: 'middle', // 文本定位基线，参考:html5-canvas API
                fontColor: 'white' // 文本颜色
            });
            tipNode.setRadius(8);
            tipNode.draggable = false;
            tipNode.showSelected = false
            if(event){
                tipNode.on('mouseenter', () => {
                    this.mycanvas.style.cursor = 'crosshair'
                })
                tipNode.on('mouseout', () => {
                    this.mycanvas.style.cursor = 'default'
                })
            }
            

            return tipNode
        }
    }
}