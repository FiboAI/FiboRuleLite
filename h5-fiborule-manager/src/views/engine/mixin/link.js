export default {
    data() {
        return {
            linkStatus: false,
            LinkTipNode: null,
            currNode: null,
            linkStartNode: null,
            currLink: null
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
            this.hoverNode.visible = true
            this.hoverNode.x = node.x - 2
            this.hoverNode.y = node.y - 2
            this.hoverNode.resizeTo((node.userData.width||node.userData.size) + 4,( node.userData.height||node.userData.size) + 4)
            this.LinkTipNode = this.hoverNode.children[0]
            this.LinkTipNode.translateCenterTo(this.hoverNode.width, 0);


        },
        closeStartLine() {
            // console.log(1)
            // this.hoverNode.visible = false
        },
        tempLink() {
            this.linkStatus = true
            this.linkStartNode = this.currNode

        },
        closeLink() {
          
            let endNode = this.Layer.getChildren().find(x => {
                return !x.isLink && this.getCoordsInNode(this.currLink.end.object, x)
            })
            this.linkStatus = false
            console.log(endNode)
            if(endNode){
                this.currLink.setEnd(endNode)
            }else{
                this.Layer.removeChild(this.currLink)
            }

            this.currLink = null
            
        },
        // 检测坐标是否在节点内
        getCoordsInNode(Coords, node) {
            console.log(Coords, node.getCenter())
            let nodeCoords = node.getCenter()
            let width = node.width||node.size
            let height = node.height||node.size
            if (Coords.x < nodeCoords.x + (width / 2) && Coords.x > nodeCoords.x - (width / 2) && Coords.y < nodeCoords.y + (height / 2) && Coords.y > nodeCoords.y - (height / 2)) {
                return true
            } else {
                return false
            }



        }
    },
    watch: {
        linkStatus(val) {
            // console.log(val,window)
            if (val) {
                let link  = new jtopo.FlexionalLink('', this.linkStartNode, this.Layer.toLocalXY(window.event.pageX, window.event.pageY - 60), 'rm', 'lm');
                this.currLink = link
                link.toArrowSize = 15;
                link.setStyles('lineDash', [6, 2]);
                link.setStyles('lineWidth', 2);
                link.setStyles('strokeStyle', '#000');
                link.showSelected = false
                link.direction = 'horizontal';
                this.Layer.addChild(link)
                window.onmousemove = (e) => {
                    link.end = { object: this.Layer.toLocalXY(e.clientX, e.clientY - 60) }
                }
            }else{
                window.onmousemove = null
            }
        }
    }
}