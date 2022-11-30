export default {
    data() {
        return {
            linkStatus: false,
            LinkTipNode: null,
            currNode: null,
            linkStartNode: null,
            currLink: null,
            LinkhoverNode: null,
            currHoverLink: null
        }
    },
    mounted() {
        let offset = 1

        setInterval(() => {
            offset--
            this.Layer.setStyles('lineDashOffset', offset)
        }, 100)
    },
    methods: {
        openStartLine(node) {
            this.currNode = node
            if (this.Stage.mode != 'edit') {
                this.hoverNode.visible = true
            }
            this.hoverNode.x = node.x - 2
            this.hoverNode.y = node.y - 2
            this.hoverNode.resizeTo((node.width || node.size) + 4, (node.height || node.size) + 4)
            this.LinkTipNode = this.hoverNode.children
            this.LinkTipNode[0].translateCenterTo(this.hoverNode.width, this.hoverNode.height);


        },
        closeStartLine() {
            // console.log(1)
            // this.hoverNode.visible = false
        },
        tempLink() {
            this.linkStartNode = this.currNode
            this.linkStatus = true


        },
        closeLink() {

            let endNode = this.Layer.getChildren().find(x => {
                return !x.isLink && this.getCoordsInNode(this.currLink.end.object, x)
            })
            this.linkStatus = false
            if (endNode) {
                let link = this.currLink
                link.setEnd(endNode, this.getLinkModel(this.currLink.begin.object, { x: endNode.x, y: endNode.y }).end)
                if (link.userData) {
                    link.userData.lastEnd = endNode
                } else {
                    link.userData = { lastEnd: endNode }
                }
                this.linkAddevent(link)


            } else if (this.currLink.userData.lastEnd) {
                let LinkModel = this.getLinkModel(this.currLink.begin.object, this.currLink.userData.lastEnd)
                this.currLink.setEnd(this.currLink.userData.lastEnd, LinkModel.end)
                this.currLink.begin.position = LinkModel.start
                this.currLink.direction = LinkModel.direction
                // console.log(this.currLink.userData.lastEnd)
            } else {
                this.Layer.removeChild(this.currLink)
            }

            this.currLink = null

        },
        linkAddevent(link) {
            let that = this
            link.on('mouseenter', function (e) {
                let endPoint = this.getEndPoint()
                switch (this.end.position) {
                    case 'lm':
                        endPoint.x -= 25
                        break;
                    case 'rm':
                        endPoint.x += 25
                        break;
                    case 'ct':
                        endPoint.y -= 25
                        break;
                    case 'cb':
                        endPoint.y += 25
                        break;
                }

                that.currHoverLink = this
                that.LinkhoverNode.translateCenterTo(endPoint.x, endPoint.y)
                that.LinkhoverNode.show()
            })
        },
        // 检测坐标是否在节点内
        getCoordsInNode(Coords, node) {
            let nodeCoords = node.getCenter()
            let width = node.width || node.size
            let height = node.height || node.size
            if (Coords.x < nodeCoords.x + (width / 2) && Coords.x > nodeCoords.x - (width / 2) && Coords.y < nodeCoords.y + (height / 2) && Coords.y > nodeCoords.y - (height / 2)) {
                return true
            } else {
                return false
            }
        },
        LinkHoverNodeInit() {

            this.LinkhoverNode = this.newHoverNode();
            let hoverNode = this.LinkhoverNode

            var tipNode = this.newTipNode()
            tipNode.translateCenterTo(hoverNode.width, hoverNode.height);

            tipNode.on('mousedown', () => {
                this.linkStartNode = this.currHoverLink
                this.currLink = this.currHoverLink
                this.linkStatus = true
                this.LinkhoverNode.hide()
            })
            hoverNode.addChild(tipNode)
            this.eventLayer.addChild(hoverNode)
        },
        getLinkModel(startLocation, endLocation) {
            let x = Math.abs(startLocation.x - endLocation.x)
            let y = Math.abs(startLocation.y - endLocation.y)
            let direction = x - y > 0 ? 'horizontal' : 'vertical'
            let start
            let end
            if (direction == 'vertical') {
                if (startLocation.y - endLocation.y > 0) {
                    start = 'ct'
                    end = 'cb'
                } else {
                    start = 'cb'
                    end = 'ct'
                }
            } else {
                if (startLocation.x - endLocation.x > 0) {
                    start = 'lm'
                    end = 'rm'
                } else {
                    start = 'rm'
                    end = 'lm'
                }

            }
            return {
                direction,
                start,
                end
            }




        }
    },
    watch: {
        linkStatus(val) {
            // console.log(val,window)
            if (val) {
                let link
                if (!this.currLink) {
                    link = new jtopo.FlexionalLink('', this.linkStartNode, this.Layer.toLocalXY(window.event.pageX, window.event.pageY - 60), 'edge');
                    this.currLink = link
                    link.toArrowSize = 15;
                    link.direction = 'vertical'
                    link.setStyles('lineDash', [6, 2]);
                    link.setStyles('lineWidth', 2);
                    link.setStyles('strokeStyle', '#000');
                    link.showSelected = false
                    // link.direction = 'horizontal';
                    this.Layer.addChild(link)
                } else {
                    link = this.currLink
                }

                window.onmousemove = this.debounce((e) => {

                    let linkModel = this.getLinkModel(link.begin.object, link.end.object)
                    link.direction = linkModel.direction
                    link.end = { object: this.Layer.toLocalXY(e.clientX, e.clientY - 60), position: linkModel.start }
                    link.begin.position = linkModel.start
                })
            } else {
                window.onmousemove = null
            }
        }
    }
}