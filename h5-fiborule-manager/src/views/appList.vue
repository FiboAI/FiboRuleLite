<template>
    <div>
        <el-button type="primary" style="margin:10px" size="medium" @click="addApp">
            添加
        </el-button>


        <div style="display: flex;flex-wrap: wrap;" v-loading="loading">
            <div v-for="value in list" class="appItem" @click="goEngineList(value.id)">
                <p>{{ value.appName }}</p>
                <div>{{ value.descriptions }}</div>

                <div class="appDelete" @click="deleteApp(value.id)">
                    删&nbsp;除
                </div>
                <div class="appEdit" @click="appEdit(value)">
                    编&nbsp;辑
                </div>
            </div>
        </div>




        <el-dialog title="添加/编辑" :visible.sync="dialogVisible" width="500px" @close="addClose">
            <div class="fromList" v-loading="loading">
                <div>
                    <p>appCode :</p>
                    <myinput placeholder="appCode" v-model="tempApp.appCode" />
                </div>
                <div>
                    <p>app名称 :</p>
                    <myinput placeholder="app名称" v-model="tempApp.appName" />
                </div>
                <div>
                    <p>app描述 :</p>
                    <myinput placeholder="app描述" v-model="tempApp.descriptions" />
                </div>
            </div>





            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addSure" :disabled="loading">确 定</el-button>
            </span>
        </el-dialog>


    </div>
</template>
<script>
import { AppList, AppEdit, AppDelete } from '@/api'
import myinput from 'c/input.vue'
export default {
    components: { myinput },
    data() {
        return {
            list: [],
            dialogVisible: false,
            tempApp: new this.defData({
                "appCode": "",
                "appName": "",
                "descriptions": "",
            }),
            loading: false
        }
    },
    created() {
        // console.log(this.tempApp)
        this.getList()
    },
    methods: {
        goEngineList(appId){
            this.$router.push('/engineList/'+appId)
        },
        getList() {
            this.loading = true
            AppList({
                "pageNum": 1,
                "pageSize": 90,
                // "status": 0
            }).then(res => {
                this.loading = false
                this.list = res.data
            }).catch(err => {
                this.loading = false
            })
        },
        addClose() {
            this.tempApp.reSet()
        },
        addApp() {
            this.dialogVisible = true
        },
        addSure() {
            if (this.tempApp.appCode.trim() == '') {
                this.$message.error('appCode 不能为空')
                return
            }
            if (this.tempApp.appName.trim() == '') {
                this.$message.error('app名称 不能为空')
                return
            }
            this.loading = true
            AppEdit(this.tempApp).then(res => {
                this.dialogVisible = false
                if(this.tempApp.id){
                    this.$message.success('修改成功')
                }else{
                    this.$message.success('添加成功')
                }
                this.tempApp.reSet()
                this.getList()
            }).catch(err => {
                this.loading = false
            })
        },
        appEdit(app) {
            this.dialogVisible = true
            this.tempApp.set(JSON.parse(JSON.stringify(app)))
            console.log(this.tempApp)
        },
        deleteApp(id) {

            this.$confirm('确定删除此App?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.loading = true
                AppDelete({ id }).then(res => {
                    this.$message.success('删除成功')
                    this.getList()
                })
            })



        }

    }
}

</script>

<style lang="less">
@appSize :140px;
@appDeleteWidth : 30px;
@appBorder: #666 solid 2px;

.appItem {
    
    width: @appSize;
    height: @appSize;
    background-color: #fff;
    border: @appBorder;
    border-radius: 10px;
    margin: 10px;
    text-align: center;
    // line-height: @appSize;
    // padding-top: 20px;
    // box-sizing: border-box;
    user-select: none;
    font-size: 18px;
    font-weight: bold;
    cursor: pointer;
    transition: all .1s;
    position: relative;
}

.appItem:hover {
    background-color: #eee;
    border-right: 0;
    border-radius: 10px 0 0 10px;

    &>.appDelete {
        width: @appDeleteWidth;
        border: @appBorder;
        right: (0 - @appDeleteWidth);
        padding-left: 10px;
        // display: block;
    }

    &>.appEdit {
        width: @appDeleteWidth;
        border: @appBorder;
        border-left: @appBorder !important;
        left: (0 - @appDeleteWidth);
        padding-left: 10px;
    }
}

.appItem>p:nth-of-type(1) {
    // font-size: 12px;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    margin: calc(@appSize/3) 10px 10px 10px;
    
}

.appItem>div:nth-of-type(1) {
    font-size: 12px;
    color: #666;

}

.appDelete,
.appEdit {
    background-color: #ec9292;

    height: @appSize;
    position: absolute;
    top: 0;
    margin-top: -2px;
    right: 0;

    border-left: 0 !important;
    // display: none;
    width: 0;
    z-index: 2;
    border-radius: 0 10px 10px 0;
    color: #fff;
    writing-mode: vertical-lr;
    transform-origin: 0;
    text-align: center;
    transition: all .1s;
    overflow: hidden;
}

.appEdit {
    background-color: #ddd;
    left: 0;
    border-right: 0 !important;

    border-radius: 10px 0 0 10px;

}
</style>