<template>
    <div>
        <div class="title">
            <div style="position: absolute;left:20px;top: 5px;user-select: none;">
                <img :src="'./img/nodeimg/nodeType' + data.nodeType + '.png'" alt="" style="height: 40px;" />
            </div>
            {{ data && data.nodeName }}
        </div>
        <div style="margin: 20px;">
            <myInput v-model="Name" placeholder="节点名称" />
        </div>
        <slot />
    </div>
</template>
<script>
import myInput from 'c/input'
export default {
    components: { myInput },
    data() {
        return {
            Name: ''
        }
    },
    props: {
        data: {
            type: Object,
            default: () => ({})
        }
    },
    created() {
        this.Name = this.data.nodeName
    },
    watch: {
        data() {
            this.Name = this.data.nodeName
        }
    }




}



</script>
<style scoped>
.title {
    text-align: center;
    background-color: antiquewhite;
    font-weight: bold;
    height: 50px;
    line-height: 50px;
    position: relative;
}

img {
    -webkit-user-drag: none;
}
</style>