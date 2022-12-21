<template>
    <div>
        <div class="title">
            {{ data && data.nodeName }}
        </div>
        <slot />
    </div>
</template>
<script>
export default {
    props: {
        data: {
            type: Object,
            default: () => ({})
        }
    }




}



</script>
<style scoped>
.title {
    text-align: center;
    background-color: antiquewhite;
    font-weight: bold;
    height: 30px;
    line-height: 30px;

}
</style>