<template>
    <nodeHome v-if="data" :data="data" style="text-align: center;" ref="nodeHome">
        <div style="margin-top: 20px;">
            <el-button type="primary" @click="submit">提交</el-button>
        </div>

    </nodeHome>
</template>
<script>
import nodeHome from './nodeHome.vue';
import mySelete from 'c/select.vue'
import myInput from 'c/input.vue'
export default {
    components: { nodeHome, mySelete, myInput },
    props: {
        data: {
            type: Object,
            default: () => ({})
        },
        moduleList: {
            type: Array,
            default: () => []
        }
    },
    methods: {
        submit() {
            if(this.$refs.nodeHome.Name==''){
                this.$message.error('节点名称不能为空')
                return 
            }

            if (this.lastNodeClazz != this.data.nodeClazz) {
                this.$emit('changeNodeClazz')
            }
            this.lastNodeClazz = this.data.nodeClazz

            this.$emit('setNodeConfig', {
                nodeConfig: this.data.nodeConfig,
                nodeClazz: this.data.nodeClazz,
                nodeName: this.$refs.nodeHome.Name,
            })

        }
    },






}
</script>