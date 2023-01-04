<template>

    <div>
        <el-button type="primary" @click="addEngine" style="margin: 10px 0;">添加引擎</el-button>
        <!-- {{list}} -->
        <myTable :data="list" :row="row" v-loading="loading">
            <template v-slot:operation="{ scope }">
                <!-- {{scope.id}} -->
                <el-button icon="el-icon-video-play" circle @click="goEngineDetil(scope.id)"  type="primary"/>
                <el-button icon="el-icon-setting" circle @click="engineEdit(scope)" />
                <el-button icon="el-icon-delete" circle @click="engineDelete(scope.id)" type="danger" />
            </template>

        </myTable>
        <myPagination :total="total" v-model="page.pageNum" @change="getList" />



        <el-dialog title="添加/编辑" :visible.sync="dialogVisible" width="500px" @close="addClose">
            <div class="fromList" v-loading="loading">
                <div>
                    <p>引擎Code :</p>
                    <myinput placeholder="引擎Code" v-model="tempEngine.engineCode" :disabled="!!tempEngine.id" />
                </div>
                <div>
                    <p>引擎名称 :</p>
                    <myinput placeholder="引擎名称" v-model="tempEngine.engineName" />
                </div>
                <div>
                    <p>引擎描述 :</p>
                    <myinput placeholder="引擎描述" v-model="tempEngine.descriptions" />
                </div>
                <div>
                    <p>场景 :</p>
                    <mySelece placeholder="场景" v-model="tempEngine.scene" :options="sceneList"
                        :disabled="!!tempEngine.id" />
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
import { engineList, engineEdit, getSceneList, engineDelete } from '@/api/index'
import myinput from 'c/input'
import myTable from 'c/table'
import mySelece from 'c/select'
import myPagination from 'c/pagination'
export default {
    components: { myTable, myinput, mySelece, myPagination },
    data() {
        return {
            list: [],
            appId: '',
            row: [
                {
                    row: 'engineCode',
                    label: '引擎Code'
                },
                {
                    row: 'engineName',
                    label: '引擎名称'
                },
                {
                    row: 'descriptions',
                    label: '引擎描述'
                },
                {
                    row: 'scene',
                    label: '场景名称'
                }, {
                    type: 'operation',
                    label: '操作'
                },
            ],
            dialogVisible: false,
            loading: false,
            tempEngine: new this.defData({
                descriptions: '',
                engineCode: '',
                engineName: '',
                scene: '',
                appId: this.$route.params.appId,
            }),
            sceneList: [],
            page: {
                pageNum: 1,
                pageSize: 10
            },
            total: 0
        }
    },
    created() {
        this.appId = this.$route.params.appId
        this.getList()
        getSceneList({ id: this.appId }).then(res => {
            this.sceneList = res.data.map(value => ({
                value,
                label: value
            }))
        })
    },
    methods: {
        goEngineDetil(id){
            this.$router.push('/engine/'+id)
        },
        engineDelete(id) {

            this.$confirm('确定删除此引擎?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                engineDelete({ id }).then(res => {
                    this.$message.success('删除成功')
                    this.getList()
                })
            })


        },
        getList() {
            this.loading = true
            engineList({
                "appId": this.appId,
                ...this.page
            }).then(res => {
                this.total = res.data.total
                this.list = res.data.list
                this.loading = false
            }).catch(err => {
                this.loading = false
            })
        },
        engineEdit(engine) {
            this.tempEngine.set(engine)
            this.dialogVisible = true

        },
        addClose() {
            this.tempEngine.reSet()
        },
        addEngine() {
            this.dialogVisible = true
        },
        addSure() {

            if (this.tempEngine.engineCode.trim() == '') {
                this.$message.error('引擎code不能为空')
                return
            }
            if (this.tempEngine.engineName.trim() == '') {
                this.$message.error('引擎名称不能为空')
                return
            }
            if (this.tempEngine.scene.trim() == '') {
                this.$message.error('引擎场景不能为空')
                return
            }
            this.loading = true
            engineEdit(this.tempEngine).then(res => {
                if (this.tempEngine.id) {
                    this.$message.success('修改成功')
                } else {
                    this.$message.success('添加成功')
                }
                this.getList()
                this.dialogVisible = false

            }).catch(err => {
                this.loading = false
            })
        }
    }



}



</script>