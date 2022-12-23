<template>
    <nodeHome v-if="data" :data="data" style="text-align: center;" ref="nodeHome">
        <mySelete v-model="data.nodeClazz" :options="moduleList" :keys="{ value: 'nodeClazz', label: 'name' }"
            style="margin-top: 10px;" placeholder="请选择实例" />
        <!-- {{data}} -->
        <!-- {{fiboFieldDtoList}} -->
        <div v-for="fiboFieldDto in fiboFieldDtoList" class="fiboFieldDto">


            <p style="width: 120px;font-size: 12px;text-align: right;font-weight: bold;margin-right: 10px;">{{
                    fiboFieldDto.name
            }} :</p>
            <p style="width: 180px;flex-shrink: 0;">
                <myInput v-if="fiboFieldDto.type == 'NUMBER' || fiboFieldDto.type == 'STRING'"
                    v-model="data.nodeConfig[fiboFieldDto.fieldName]" :number="fiboFieldDto.type == 'NUMBER'" />

                <el-date-picker v-if="fiboFieldDto.type == 'DATE'" v-model="data.nodeConfig[fiboFieldDto.fieldName]"
                    type="datetime" placeholder="选择日期时间" style="width: 180px;">

                </el-date-picker>
            </p>
        </div>
        <div style="margin-top: 20px;">
            <el-button type="primary" @click="submit" :disabled="disabled">提交</el-button>
        </div>

    </nodeHome>
</template>
<script>
import nodeHome from './nodeHome.vue';
import mySelete from 'c/select.vue'
import myInput from 'c/input.vue'
export default {
    components: { nodeHome, mySelete, myInput },
    data() {
        return {
            lastNodeClazz: ''
        }
    },
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
    watch: {
        data() {
            this.lastNodeClazz = this.data.nodeClazz
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
    computed: {
        disabled() {
            let is = false
           
            if (this.data.nodeConfig) {
                Object.keys(this.data.nodeConfig).forEach(key => {
                    if (this.data.nodeConfig[key] === '' || this.data.nodeConfig[key] == null) {
                        is = true
                    }
                })
                return is
            }else{
                return true
            }

        },
        fiboFieldDtoList() {
            let module = this.moduleList.find(x => x.nodeClazz == this.data.nodeClazz)
            if (module && !this.data.nodeConfig) {

                let obj = {}

                module.fiboFieldDtoList.forEach(fiboFieldDto => {
                    obj[fiboFieldDto.fieldName] = ''
                });

                this.$set(this.data, 'nodeConfig', obj)
            }
            return module ? module.fiboFieldDtoList : []

        }
    }






}
</script>
<style>
.fiboFieldDto {
    display: flex;
    padding: 10px;
    align-items: center;
}
</style>