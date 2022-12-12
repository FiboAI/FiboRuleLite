<template>
    <div>
        <el-input type="text" v-bind="$attrs" :value="value" @input="$emit('input',$event)" clearable />
    </div>
</template>
<script>
export default {
    props:{
        value:{
            type:String||Number,
            default:""
        }
    }
}



</script>    