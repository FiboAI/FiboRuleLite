export default {

    data() {
        return {
            // 是否处于添加节点中
            addNodeStatus: false,
            // 添加节点时 临时显示的 跟随鼠标走的节点的 X 轴 和 Y轴
            addNodeX: 0,
            addNodeY: 0,
            // 将要被 添加的 节点的
            addNodeTempShow: {},
            // 当前被选中的节点
            currNode: null,
            // nodeCode迭代数字 nodeCode 由 节点的前缀 加 _ 加 一个迭代的数字 组成
            nodeCodeNumber: 0,
            // 鼠标移动到节点上时 显示的虚线框节点
            tempHoverNode: null
        }
    },
    methods: {
        nodeMouesDown(e, node) {
            this.addNodeX = e.clientX - 30 + 'px'
            this.addNodeY = e.clientY - 45 + 'px'
            this.addNodeTempShow = node
            this.addNodeStatus = true

            window.onmousemove = (e) => {
                // console.log(e)
                this.addNodeX = e.clientX - 30 + 'px'
                this.addNodeY = e.clientY - 45 + 'px'
            }
            window.onmouseup = () => {
                this.addNodeStatus = false 
                window.onmousemove = null
                window.onmouseup = null
            }


        },
        // 添加节点
        addNode(addNodeTempShow, deriveNode = 0, pairRandom) {

            let coord = this.Layer.toLocalXY(window.event.pageX, window.event.pageY)
            coord.y -= 60
            var node

            let nodeStyle = {
                textPosition: 'center',
                fontColor: '#000',
                lineWidth: 3,
                'strokeStyle': !addNodeTempShow.haveConfig || addNodeTempShow.nodeClazz ? '#666' : '#f00',
                'fillStyle': addNodeTempShow.fillStyle,
            }


            let x = Number(addNodeTempShow.nodeX) || coord.x - ((addNodeTempShow.width || addNodeTempShow.size) / 2) + deriveNode * 150
            let y = Number(addNodeTempShow.nodeY) || coord.y - (addNodeTempShow.height || addNodeTempShow.size) / 2

            // console.log(addNodeTempShow,addNodeTempShow.nodeX,Number(addNodeTempShow.nodeX))

            if (addNodeTempShow.shape == 'polyNode') {
                node = new jtopo.PolygonNode(addNodeTempShow.nodeName, x, y, addNodeTempShow.width, addNodeTempShow.height);
                node.setStyles(nodeStyle);
                node.textOffsetY = -3;
                node.edges = addNodeTempShow.edges;
                node.roundRadius = 6;

            } else if (addNodeTempShow.shape == 'rectangle') {
                node = new jtopo.Node(addNodeTempShow.nodeName, x, y, addNodeTempShow.width, addNodeTempShow.height);
                // 线型
                node.setStyles(nodeStyle)
                node.textOffsetY = -4;
                node.roundRadius = 6;
            } else if (addNodeTempShow.shape == 'circle') {
                node = new jtopo.CircleNode(addNodeTempShow.nodeName, x, y, addNodeTempShow.size);
                node.setStyles({ ...nodeStyle, textBaseline: 'middle' });
            }

            node.userData = JSON.parse(JSON.stringify(addNodeTempShow))
            node.userData.nodeCode = node.userData.nodeCode || addNodeTempShow.codePrefix + '_' + (++this.nodeCodeNumber)
            if (pairRandom) {
                node.userData.pairRandom = pairRandom
                let tip = this.newTipNode('', false)
                tip.setStyles({ fillStyle: '#' + (pairRandom.split('/')[0]) })
                tip.setRadius(5)
                tip.translateCenterTo(node.width / 2, node.height / 1.2)
                node.addChild(tip)
            }
            node.selectedStyle = new jtopo.Style({
                'shadowColor': '#000',
                'shadowBlur': 1,
                'fontColor': 'red',
            })

            node.removeOutLink = function (link) {
                let index = this.outLinks.findIndex(x => x == link)
                this.outLinks.splice(index, 1)
            }


            this.nodeAddEvent(node)




            // 并行节点特殊处理
            if (addNodeTempShow.deriveNode && !addNodeTempShow.disabledRequest) {
                if (!pairRandom) {
                    pairRandom = Math.floor(Math.random() * 16777215).toString(16) + '/' + new Date().getTime()
                }
                // node.setStyles({'strokeStyle':'#'+pairRandom})
                let tip = this.newTipNode('', false)
                tip.setStyles({ fillStyle: '#' + (pairRandom.split('/')[0]) })
                tip.setRadius(5)
                tip.translateCenterTo(node.width / 2, node.height / 1.2)
                node.addChild(tip)
                node.userData.pairRandom = pairRandom
                // console.log(pairRandom)
                this.addNode(this.nodeList.find(x => x.nodeType == addNodeTempShow.deriveNode), ++deriveNode, pairRandom)

            }

            this.Layer.addChild(node)
            if (!addNodeTempShow.disabledRequest) {
                this.requestAddNode(node)
            }
        },
        // 节点添加事件
        nodeAddEvent(node) {

            node.on('mouseup', (e) => {
                if (node.userData.drag) {
                    this.nodeDragRequest(node)
                }
            })
            node.on('mouseenter', (e) => {
                this.openStartLine(node)

            });
            node.on('mousedrag', this.debounce((e) => {
                node.userData.drag = true
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
            node.on('click', (e) => {
                this.getNodeConfigList(node)
            })
        },
        // 打开节点hoverNode的框
        openStartLine(node) {
            this.currNode = node
            if (this.Stage.mode != 'edit') {
                this.hoverNode.visible = true
                this.LinkhoverNode.visible = false
            }
            this.hoverNode.x = node.x - 2
            this.hoverNode.y = node.y - 2
            this.hoverNode.resizeTo((node.width || node.size) + 4, (node.height || node.size) + 4)
            this.tempHoverNode = this.hoverNode.children
            this.tempHoverNode[0].translateCenterTo(this.hoverNode.width, this.hoverNode.height);
            this.tempHoverNode[1].translateCenterTo(this.hoverNode.width, 0);


        },
        // 关闭节点hoverNode的框
        closeStartLine() {
            // console.log(1)
            // this.hoverNode.visible = false
        },

        hoverNodeInit() {
            this.eventLayer = new jtopo.Layer('eventLayer');

            this.hoverNode = this.newHoverNode();
            var hoverNode = this.hoverNode


            this.Layer.addChild(hoverNode)
            this.Stage.addChild(this.eventLayer)



            // 选择框加入 右下角的 连线按钮
            var tempHoverNode = this.newTipNode()
            tempHoverNode.on('mousedown', () => {
                this.tempLink()
            })


            // 选择框加入 右上角的 删除按钮
            var deleteTipNode = this.newTipNode('x')

            deleteTipNode.setStyles({
                fillStyle: 'red'
            })
            deleteTipNode.on('mouseenter', () => {
                this.mycanvas.style.cursor = 'pointer'
            })
            deleteTipNode.on('click', () => {
                this.deleteNode()
            })
            hoverNode.userData = {
                isHoverNode: true
            }

            hoverNode.addChild(tempHoverNode);
            hoverNode.addChild(deleteTipNode);
        },
        deleteNode(node = this.currNode) {
            const deleteNodeLink = (node) => {
            

                // deleteLink方法里会删除掉inLinks和outLinks里的内容 导致forEach顺序错乱 以及length变动 不能完全删除 只能这样调用 不能forEach
                if (node.inLinks) {
                    let number = node.inLinks.length
                    for (let i = 0; i < number; i++) {
                        this.deleteLink(node.inLinks[0])
                    }
                }
                
                if (node.outLinks) {
                    let number = node.outLinks.length
                    for (let i = 0; i < number; i++) {
                        console.log(i)
                        this.deleteLink(node.outLinks[0])
                    }
                }



            }



            this.hoverNode.hide()
            if (node.userData.pairRandom) {
                console.log(this.Layer.children)
                let groupNodeArr = this.Layer.children.filter(x => x.userData.pairRandom == node.userData.pairRandom)
                groupNodeArr.forEach(groupNode => {
                    this.Layer.removeChild(groupNode)

                    deleteNodeLink(groupNode)
                    this.deleteNodeRequest(groupNode)
                })

            } else {
                this.Layer.removeChild(node)

                deleteNodeLink(node)

                this.deleteNodeRequest(node)
            }



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
        newTipNode(text = '+', event = true) {
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
            if (event) {
                tipNode.on('mouseenter', () => {
                    this.mycanvas.style.cursor = 'crosshair'
                })
                tipNode.on('mouseout', () => {
                    this.mycanvas.style.cursor = 'default'
                })
            }


            return tipNode
        },
        getNextNode(node, type = 'nodecode') {
            if (!node.outLinks) {
                return []
            }
            return node.outLinks.map(x => type == 'nodecode' ? x.end.object.userData.nodeCode : x.end.object)
        },
        getPreNode(node, type = 'nodecode') {
            if (!node.inLinks) {
                return []
            }
            return node.inLinks.map(x => type == 'nodecode' ? x.begin.object.userData.nodeCode : x.begin.object)
        },
    }
}