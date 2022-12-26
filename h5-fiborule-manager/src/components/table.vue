<template>

    <el-table border :data="data" style="width: 100%" :cell-style="{ padding: '10px' }">
        <el-table-column v-for="item in row" :prop="item.row" :label="item.label" :width="item.width" align="center">
            <template slot-scope="scope">
                <span v-if="item.type === 'Blooen'">
                    {{ scope.row[item.row] ? "是" : "否" }}
                </span>
                <span v-else-if="item.type === 'State'">
                    {{ scope.row[item.row] == "1" ? '启用' : '未启用' }}
                </span>
                <span v-else-if="item.type === 'type'">
                    {{ type[scope.row[item.row]] }}
                </span>
                <span v-else-if="item.fn">
                    {{ item.fn(scope.row[item.row]) }}
                </span>
                <span v-else-if="item.type === 'Time'" style="white-space: nowrap;" class="contText">

                </span>
                <span v-else-if="item.type === 'operation'">
                    <slot name="operation" :scope="scope.row" />
                </span>
                <span class="contText" v-else>
                    {{ scope.row[item.row] }}
                </span>
            </template>
        </el-table-column>

    </el-table>

</template>
<script>
export default {
    props: {
        data: {
            type: Array,
            default: () => []
        },
        row: {
            type: Array,
            default: () => []
        },
    }



}



</script>

