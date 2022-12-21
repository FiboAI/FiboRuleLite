<template>
    <div>
        <el-input  :type="number?'number':'text'" v-bind="$attrs" :value="value" @input="input" clearable @blur="blur" />
    </div>
</template>
<script>
export default {
    props:{
        value:{
            type:String|Number,
            default:""
        },
        number:{
            type:Boolean,
            default :false
        }
    },
    methods:{
        input(e){
                this.$emit('input',e)
        },
        blur(){
            if(this.number){
                console.log(Number(this.value))
                if(isNaN(Number(this.value))){
                    this.$emit('input',0)
                }else{
                    this.$emit('input',Number(this.value))
                }
               
            }
        }
    }
}



</script>    