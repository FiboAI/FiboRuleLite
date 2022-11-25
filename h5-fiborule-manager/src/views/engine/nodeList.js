export default [
    {
        nodeType: 0,
        nodeName: '开始',
        edges: 3,
        shape: 'polyNode',
        width: 50,
        height: 50,
        fillStyle:'#2775b6'
    },

    {
        nodeType: 1,
        nodeName: '初始化',
        shape: 'rectangle',

        width: 100,
        height: 50,
        fillStyle:'#c2c2ff'
    },
    {
        nodeType: 2,
        nodeName: '配置',
        shape: 'rectangle',
        width: 100,
        height: 50,
        fillStyle:'#c2c2ff'
    },
    {
        nodeType: 3,
        nodeName: '条件',
        edges: 4,
        shape: 'polyNode',
        
        width: 100,
        height: 50,
        fillStyle:'#fbc31d'
    }
    ,
    {
        nodeType: 4,
        nodeName: '并行聚合',
        shape: 'circle',
        size: 50,
        fillStyle:'#9be4f1'
    },
    {
        nodeType: 5,
        nodeName: '结束',
        shape: 'circle',
        size: 50,
        fillStyle:'#000'
    }
]