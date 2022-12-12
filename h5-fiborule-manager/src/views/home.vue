<template>

   <el-container>
      <el-header class="header">FiboRule</el-header>
      <el-main class="main" v-if="$route.meta.title">
         <div class="title" >
            {{$route.meta.title}}
         </div>
         <router-view />
      </el-main>
      <router-view v-else />
   </el-container>

</template>
<script>

export default{
   created(){
      console.log(this.$route)
   }
}



</script>
<style scoped>
.header {
   background-color: #242f42;
   color: #fff;
   font-size: 26px;
   /* float: left; */
   line-height: 60px;
   padding-left: 40px;
}
.title{
   font-size: 20px;
   font-weight: bold;
}
.main{
   background-color: #fafafa;
   height: calc(100vh - 60px);
}
</style>