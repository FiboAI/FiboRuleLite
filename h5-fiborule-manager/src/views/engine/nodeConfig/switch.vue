<template>
    <nodeHome v-if="data" :data="data" style="text-align: center;">
        <mySelete v-model="data.nodeClazz" :options="moduleList" :keys="{ value: 'nodeClazz', label: 'name' }"
            style="margin-top: 20px;" />
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
            console.log(this.data)

            this.$emit('setNodeConfig', {
                nodeConfig: this.data.nodeConfig,
                nodeClazz: this.data.nodeClazz,
                nodeName: this.data.nodeName
            })
        }
    },
    computed: {
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