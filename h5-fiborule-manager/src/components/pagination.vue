<template>
    <div style="display: flex;justify-content: flex-end;padding-right: 30px;padding-top: 30px;">
        <el-pagination :current-page="value" @current-change="$emit('input',$event);$emit('change',$event)" background
            layout="prev, pager, next" :total="total" v-bind="$attrs">
        </el-pagination>
    </div>
</template>
<script>
export default {
    props: {
        total: {
            type: Number,
            default: 0
        },
        value: {
            type: Number,
            default: 0
        }
    }


}


</script>