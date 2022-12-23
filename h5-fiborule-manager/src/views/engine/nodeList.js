export default [
    {
        // 节点类型
        nodeType: 1,
        // 节点默认名称
        nodeName: '开始',
        // 节点边数
        edges: 3,
        // 节点显示类型  polyNode多边形  circle 圆形 
        shape: 'polyNode',
        // 节点 宽
        width: 50,
        // 节点 高
        height: 50,
        // 节点填充颜色
        fillStyle:'#fff',
        // 节点 是否在左侧列表显示
        listShow:false,
        // 节点 code 前缀
        codePrefix:'start',
        // 节点 是否需要配置才能连线
        haveConfig:false,
        configVue:'NoConfig'
    },
    {
        nodeType: 2,
        nodeName: '结束',
        shape: 'circle',
        size: 50,
        fillStyle:'#fff',
        listShow:true,
        codePrefix:'end',
        haveConfig:false,
        configVue:'NoConfig'
    },
    {
        nodeType: 3,
        nodeName: '计算节点',
        shape: 'circle',
        size: 50,
        fillStyle:'#fff',
        listShow:true,
        codePrefix:'general',
        haveConfig:true,
        configVue:'SwitchIfGeneral'
    },
    {
        nodeType: 4,
        nodeName: 'if节点',
        edges: 4,
        shape: 'polyNode',
        width: 70,
        height: 50,
        fillStyle:'#fff',
        listShow:true,
        codePrefix:'if',
        haveConfig:true,
        haveMoreChildren:true,
        nextNodeType:[{value:'Y',label:'Yes'},{value:'N',label:'No'},],
        configVue:'SwitchIfGeneral'
    },
    {
        nodeType: 5,
        nodeName: 'switch',
        shape: 'polyNode',
        edges: 5,
        width: 50,
        height: 50,
        fillStyle:'#fff',
        haveMoreChildren:true,
        listShow:true,
        codePrefix:'switch',
        haveConfig:true,
        nextNodeType:[],
        configVue:'SwitchIfGeneral'
    },
    {
        nodeType: 6,
        nodeName: '并行',
        shape: 'circle',
        size: 50,
        fillStyle:'#fff',
        listShow:true,
        // 节点是否含有衍生节点
        haveMoreChildren:true,
        // 节点衍生节点类型
        deriveNode:7,
        codePrefix:'parallel',
        haveConfig:false,
        configVue:'NoConfig'
    },
    {
        nodeType: 7,
        nodeName: '聚合',
        shape: 'circle',
        size: 50,
        fillStyle:'#fff',
        listShow:false,
        codePrefix:'aggregation',
        haveConfig:false,
        configVue:'NoConfig'
    },
    
]