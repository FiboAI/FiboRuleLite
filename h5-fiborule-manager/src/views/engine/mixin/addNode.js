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
            this.addNodeY = e.clientY - 25 + 'px'
            this.addNodeTempShow = node
            this.addNodeStatus = true

            window.onmousemove = (e) => {
                // console.log(e)
                this.addNodeX = e.clientX - 50 + 'px'
                this.addNodeY = e.clientY - 25 + 'px'
            }
            window.onmouseup = () => {
                this.addNodeStatus = false
                window.onmousemove = null
                window.onmouseup = null
            }

    
        },
        addNode(e) {

            let coord =  this.Layer.toLocalXY(window.event.pageX,window.event.pageY) 
            coord.y -= 60
            var node
            if (this.addNodeTempShow.shape == 'polyNode') {
                node = new jtopo.PolygonNode(this.addNodeTempShow.nodeName, coord.x - (this.addNodeTempShow.width / 2), coord.y - this.addNodeTempShow.height / 2, this.addNodeTempShow.width, this.addNodeTempShow.height);
                node.setStyles({
                    'fillStyle':this.addNodeTempShow.fillStyle,
                    textPosition: 'center',
                    fontColor: 'white'
                });
                node.textOffsetY = 5;
                node.edges = this.addNodeTempShow.edges;
                node.roundRadius = 6;

            } else if (this.addNodeTempShow.shape == 'rectangle') {
                node = new jtopo.Node(this.addNodeTempShow.nodeName, coord.x - (this.addNodeTempShow.width / 2), coord.y - this.addNodeTempShow.height / 2, this.addNodeTempShow.width, this.addNodeTempShow.height);
                // 线型
                node.setStyles({
                    // 'strokeStyle': 'black',
                    'fillStyle':this.addNodeTempShow.fillStyle,
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
                    'fillStyle':this.addNodeTempShow.fillStyle,
                    textPosition: 'center',
                    textBaseline: 'middle',
                    fontColor: 'white'
                });
            }
            node.userData = JSON.parse(JSON.stringify(this.addNodeTempShow))
            node.on('mouseenter', (e)=> {
                this.openStartLine(node)
                
            });
            node.on('mousedrag', (e)=> {
                this.openStartLine(node)
            });


            this.Layer.addChild(node)



        }
    }
}