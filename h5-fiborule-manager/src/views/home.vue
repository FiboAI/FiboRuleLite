<template>

   <el-container>
      <el-header class="header">FiboRule</el-header>
      <el-main class="main">
         <router-view />
      </el-main>
   </el-container>

</template>
<style scoped>
.header {
   background-color: #242f42;
   color: #fff;
   font-size: 26px;
   /* float: left; */
   line-height: 60px;
   padding-left: 40px;
}
.main{
   background-color: #fafafa;
}
</style>