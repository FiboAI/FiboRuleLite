<template>
    <!-- <div>
        {{keys}}
    </div> -->
    <el-select :value="value" @input="$emit('input',$event);$emit('change',$event)" :placeholder="placeholder" v-bind="$attrs">
        <el-option v-for="item in options" :key="item[keys.value]" :label="item[keys.label]" :value="item[keys.value]">
        </el-option>
    </el-select>
</template>
<script>
export default{
    props:{
        value:{
            type:String|Number,
            default :''
        },
        placeholder:{
            type:String,
            default:'请选择'
        },
        options:{
            type:Array,
            default:()=>[]
        },
        keys:{
            type:Object,
            default:()=>({
                value:'value',
                lable:'label'
            })
        }
    }



}


</script>