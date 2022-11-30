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
        addNode(e) {

            let coord = this.Layer.toLocalXY(window.event.pageX, window.event.pageY)
            coord.y -= 60
            var node
            if (this.addNodeTempShow.shape == 'polyNode') {
                node = new jtopo.PolygonNode(this.addNodeTempShow.nodeName, coord.x - (this.addNodeTempShow.width / 2), coord.y - this.addNodeTempShow.height / 2, this.addNodeTempShow.width, this.addNodeTempShow.height);
                node.setStyles({
                    'fillStyle': this.addNodeTempShow.fillStyle,
                    textPosition: 'center',
                    fontColor: 'white',
                    lineWidth: 1,


                });
                console.log(node)
                node.textOffsetY = -3;
                node.edges = this.addNodeTempShow.edges;
                node.roundRadius = 6;

            } else if (this.addNodeTempShow.shape == 'rectangle') {
                node = new jtopo.Node(this.addNodeTempShow.nodeName, coord.x - (this.addNodeTempShow.width / 2), coord.y - this.addNodeTempShow.height / 2, this.addNodeTempShow.width, this.addNodeTempShow.height);
                // 线型
                node.setStyles({
                    // 'strokeStyle': 'black',
                    'fillStyle': this.addNodeTempShow.fillStyle,
                    'lineWidth': 2,
                    'lineDash': [6, 2],
                    'fontColor': 'black',
                    textPosition: 'center',
                    fontColor: 'white'
                });
                node.roundRadius = 6;
            } else if (this.addNodeTempShow.shape == 'circle') {
                node = new jtopo.CircleNode(this.addNodeTempShow.nodeName, coord.x - (this.addNodeTempShow.size / 2), coord.y - this.addNodeTempShow.size / 2, this.addNodeTempShow.size);
                node.setStyles({
                    'fillStyle': this.addNodeTempShow.fillStyle,
                    textPosition: 'center',
                    textBaseline: 'middle',
                    fontColor: 'white',
                    lineWidth: 1,
                });
            }
            node.userData = JSON.parse(JSON.stringify(this.addNodeTempShow))
            node.selectedStyle = new jtopo.Style({
                'shadowColor': '#000',
                'shadowBlur': 7,
            })

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

                        if(inlink==this.currHoverLink){
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



            this.Layer.addChild(node)



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
        newHoverNode(){
            var hoverNode  = new jtopo.Node('');
            
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
        newTipNode(){
            var tipNode = new jtopo.CircleNode('+');
            tipNode.setStyles({
                fillStyle: '#aaaaaa', // 填充颜色：红色
                textPosition: 'center', // 文本位置：居中
                textBaseline: 'middle', // 文本定位基线，参考:html5-canvas API
                fontColor: 'white' // 文本颜色
            });
            tipNode.setRadius(8);
            tipNode.draggable = false;
            tipNode.showSelected = false
            tipNode.on('mouseenter', () => {
                this.mycanvas.style.cursor = 'crosshair'
            })
            tipNode.on('mouseout', () => {
                this.mycanvas.style.cursor = 'default'
            })

            return tipNode
        }
    }
}