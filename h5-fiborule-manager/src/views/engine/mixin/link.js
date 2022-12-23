export default {
    data() {
        return {
            // 是否正处于连线中
            linkStatus: false,
            // 连线的开始节点
            linkStartNode: null,
            // 当前正在被连接的线
            currLink: null,
            // 鼠标移动到连线上时 显示的虚线框节点
            LinkhoverNode: null,
            // 当前虚线框 所在的 连线
            currHoverLink: null,
            // 选择连线模式的弹出框
            LinkTypeSelectDialog: false,
            // 连线模式列表
            LinkTypeList: [],
            // 当前正在被选择模式的连线
            theSelectedModeLink: null
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
        // 初始化渲染连线
        initAddLink(nodeArr) {
            // 遍历每个节点的基础数据
            nodeArr.forEach(nodes => {
                // 如果此节点有次级节点
                if (nodes.nextNodes) {
                    // 此节点的 次级节点 以 ， 分割 再遍历出每个次级节点
                    nodes.nextNodes.split(',').forEach(nodeCode => {
                        // 找到此节点的次级节点
                        let endNode = nodeArr.find(x => x.nodeCode == nodeCode)
                        // 如果存在这个次级节点 并且 次级节点的 上级节点 也包括此节点的话
                        if (endNode && endNode.preNodes.indexOf(nodes.nodeCode) != -1) {
                            // 进行链接
                            let begin = this.Layer.children.find(x => x.isNode && (x.userData && x.userData.nodeCode == nodes.nodeCode))
                            let end = this.Layer.children.find(x => x.isNode && (x.userData && x.userData.nodeCode == endNode.nodeCode))

                            let LinkText = ''
                            // 判断开始的节点是否为多子节点 且 是否有配置好的连线模式
                            if (begin.userData.haveMoreChildren && begin.userData.nextConfig) {
                                let nextConfig = begin.userData.nextConfig.find(x => x.value == nodeCode)
                                LinkText = nextConfig && nextConfig.label
                            }
                            let link = this.newLink(LinkText, begin, end)
                            link.userData = { lastEnd: end }
                            this.linkAddevent(link)
                        }
                    })
                }
            });
        },

        // 开始连线
        tempLink() {

            this.linkStartNode = this.currNode

            // 先检测此节点 是否有nextNodeType 是否应该有

            console.log(this.currNode)
            let userData = this.currNode.userData
            if (userData.nextNodeType && userData.nextNodeType.length == 0 && userData.nodeClazz) {
                let NodeConfigItem = this.NodeConfig[userData.nodeType].find(x => x.nodeClazz == userData.nodeClazz)
                NodeConfigItem && (userData.nextNodeType = this.$getArrayByMap(NodeConfigItem.branchMap))
            }




            if (this.linkStartVerdict()) {
                this.linkStartNode = null
                return
            }
            this.linkStatus = true
        },
        // 关闭连线
        closeLink() {
            // 寻找鼠标松开的位置是否有节点
            let endNode = this.Layer.getChildren().find(x => {
                return !x.isLink && !x.userData.isHoverNode && x != this.currLink.begin.object && this.getCoordsInNode(this.currLink.end.object, x)
            })
            this.linkStatus = false
            // 如果有节点 并且能通过验证
            if (endNode && !this.linkEndVerdict(endNode)) {
                // 连接节点
                let link = this.currLink
                link.setEnd(endNode, this.getLinkModel(this.currLink.begin.object, { x: endNode.x, y: endNode.y }).end)

                // 如果 此连线的 有 上一个被连接的 结束节点 
                if (link.userData && link.userData.lastEnd) {
                    // 则将上一个连接节点的 inLinks 属性中 清除掉 此连线
                    let lastEnd = link.userData.lastEnd
                    lastEnd.inLinks.splice(lastEnd.inLinks.findIndex(x => x === link), 1)

                    // 如果开始节点是多子节点 那么需要处理一下开始节点的nextConfig
                    if (link.begin.object.userData.nextConfig) {
                        let nextConfig = link.begin.object.userData.nextConfig.find(x => x.key == link.text)
                        nextConfig && (nextConfig.value = endNode.userData.nodeCode)
                        this.requestSetNode(link.begin.object)
                    }

                }

                // 连线添加事件
                this.linkAddevent(link)




                // 如果连线的开始节点是 if 或者 switch 则需要选择连线的模式 （Yes 或者 No） 并且 不是移动过来的线
                // 如果不需要选择连线模式 就直接发送给后端存储
                if (link.begin.object.userData.nextNodeType && (!link.userData || !link.userData.lastEnd)) {
                    this.selectLinkType(link)
                } else {
                    this.addAndDeleteLinkRequest(link)
                }


                // 切换 lastEnd 
                if (link.userData) {
                    link.userData.lastEnd = endNode
                } else {
                    link.userData = { lastEnd: endNode }
                }


                // 如果鼠标松开的位置 没有节点 但是 此线有上一个被连接的 结束节点
            } else if (this.currLink.userData && this.currLink.userData.lastEnd) {
                // 连回上个连接的节点
                let LinkModel = this.getLinkModel(this.currLink.begin.object, this.currLink.userData.lastEnd)
                this.currLink.setEnd(this.currLink.userData.lastEnd, LinkModel.end)
                this.currLink.begin.position = LinkModel.start
                this.currLink.direction = LinkModel.direction

                // 如果 鼠标松开的位置没有节点 且 没有被连接过的结束节点 那么此线清除
            } else {
                this.currLink.begin.object.removeOutLink(this.currLink)
                this.Layer.removeChild(this.currLink)
            }

            this.currLink = null

        },
        // 选择连线的模式
        setLinkType(LinkType) {


            let userData = this.theSelectedModeLink.begin.object.userData
            if (userData.nextConfig) {
                userData.nextConfig.push({
                    key: LinkType.key,
                    label: LinkType.label,
                    value: this.theSelectedModeLink.end.object.userData.nodeCode
                })
            } else {
                userData.nextConfig = [{
                    key: LinkType.key,
                    label: LinkType.label,
                    value: this.theSelectedModeLink.end.object.userData.nodeCode
                }]
            }
            this.addAndDeleteLinkRequest(this.theSelectedModeLink)
            this.requestSetNode(this.theSelectedModeLink.begin.object)

            this.theSelectedModeLink.text = LinkType.label
            this.theSelectedModeLink.textOffsetX = -20
            this.theSelectedModeLink.textOffsetY = -20
            console.log(this.theSelectedModeLink)

            this.theSelectedModeLink = null
            this.LinkTypeSelectDialog = false
        },
        // 打开选择连线的模式的弹窗
        selectLinkType(link) {
            this.LinkTypeSelectDialog = true
            let userData = link.begin.object.userData
            this.LinkTypeList = userData.nextNodeType.map(value => ({
                key: value.key,
                label: value.label,
                disabled: userData.nextConfig && userData.nextConfig.find(x => x.key == value.key) ? true : false
            }))
            this.theSelectedModeLink = link
        },
        // 取消选择连线的模式
        cancelSelectLinkType() {
            this.LinkTypeSelectDialog = false
            this.Layer.removeChild(this.theSelectedModeLink)
            this.theSelectedModeLink = null
        },
        // 开始连线前的验证 通过验证才能成功拉出线
        linkStartVerdict() {

            let userData = this.linkStartNode.userData
            // 非多子节点只能出一条线
            if (this.linkStartNode.outLinks && this.linkStartNode.outLinks.length > 0 && !userData.haveMoreChildren) {
                this.$message.error('非多子节点只能出一条线')
                return true
            }
            // haveConfig必须先配置完毕才能连线
            if (userData.haveConfig && !userData.nodeClazz) {
                this.$message.error('此节点必须先配置完毕才能连线')
                return true
            }
            // 连线是否达上限
            if (userData.haveMoreChildren && userData.nextNodeType && userData.nextConfig && userData.nextNodeType.length <= userData.nextConfig.length) {
                this.$message.error('此节点连线以达上限')
                return true
            }

            // 不能以 《结束节点》开始
            if (userData.nodeType == 2) {

                    this.$message.error('不能以结束节点开始连线')
                    return true
                
            }
        },
        // 结束连线前的验证 通过验证才能成功结束连线
        linkEndVerdict(endNode) {

            // =============禁止循环连线================
            let circulationLikn = false
            console.log(endNode, this.getNextNode(endNode), this.linkStartNode)
            const circulationLiknfn = (endNode) => {
                // let node = this.Layer.children.find(x => x.userData && x.userData.nodeCode == nodeCode)
                if (endNode == this.linkStartNode) {
                    circulationLikn = true
                    return
                }
                this.getNextNode(endNode, 'node').forEach(nextNode => {
                    circulationLiknfn(nextNode)
                })
            }
            circulationLiknfn(endNode)
            if (circulationLikn) {
                this.$message.error('禁止循环连接')
                return true
            }

            // =================================不能以《开始节点》结束  

            if (endNode.userData.nodeType == 1) {
                this.$message.error('不能以开始节点结束连线')
                return true
            }






          










        },
        // 连线添加事件
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
                that.hoverNode.hide()
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
        // 鼠标经过线时显示的虚线框 初始化
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

            var deleteTipNode = this.newTipNode('x')
            deleteTipNode.translateCenterTo(hoverNode.width, 0);
            deleteTipNode.setStyles({
                fillStyle: 'red'
            })
            deleteTipNode.on('mouseenter', () => {
                this.mycanvas.style.cursor = 'pointer'
            })
            deleteTipNode.on('click', () => {
                this.deleteLink()
            })

            hoverNode.addChild(tipNode)
            hoverNode.addChild(deleteTipNode)
            hoverNode.userData = {
                isHoverNode: true
            }
            this.Layer.addChild(hoverNode)
        },
        // 删除线
        deleteLink(link) {
            console.log(link)
            link = link || this.currHoverLink
            this.Layer.removeChild(link)

            let userData = link.begin.object.userData
            if (userData.nextNodeType && userData.nextConfig) {
                let index = userData.nextConfig.findIndex(x => x.key == link.text)

                index != -1 && userData.nextConfig.splice(index, 1)
            }



            this.addAndDeleteLinkRequest(link)
            this.LinkhoverNode.hide()
        },
        // 计算线的连接模式
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




        },
        // 初始化一条线
        newLink(name, start, end) {
            let link = new jtopo.FlexionalLink(name, start, end, 'edge');

            link.toArrowSize = 15;
            link.direction = 'vertical'

            link.setStyles({
                'lineDash': [6, 2],
                'lineWidth': 1,
                'strokeStyle': '#000',
                'fontColor': '#000',
                'font': 'bold 20px 仿宋',
            });
            link.showSelected = false
            this.Layer.addChild(link)

            let linkModel = this.getLinkModel(start, end)
            link.direction = linkModel.direction
            link.end = { object: end, position: linkModel.end }
            link.begin.position = linkModel.start


            return link
        },
        // 打开线的鼠标禁用
        openLinkMouseEnabled() {
            this.Layer.children.forEach(item => {
                if (item.isLink) {
                    item.mouseEnabled = true
                }
            });
        },
         // 关闭线的鼠标禁用
        closeLinkMouseEnabled() {
            this.Layer.children.forEach(item => {
                if (item.isLink) {
                    item.mouseEnabled = false
                }
            });
        },

    },
    watch: {
        linkStatus(val) {
            // console.log(val,window)
            if (val) {
                let link
                if (!this.currLink) {
                    link = this.newLink('', this.linkStartNode, this.Layer.toLocalXY(window.event.pageX, window.event.pageY - 60))
                    // link.direction = 'horizontal';
                    this.currLink = link

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