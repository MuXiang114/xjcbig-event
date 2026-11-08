<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
const categorys = ref([
    {
        "id": 3,
        "categoryName": "美食",
        "categoryAlias": "my",
        "createTime": "2023-09-02 12:06:59",
        "updateTime": "2023-09-02 12:06:59"
    },
    {
        "id": 4,
        "categoryName": "娱乐",
        "categoryAlias": "yl",
        "createTime": "2023-09-02 12:08:16",
        "updateTime": "2023-09-02 12:08:16"
    },
    {
        "id": 5,
        "categoryName": "军事",
        "categoryAlias": "js",
        "createTime": "2023-09-02 12:08:33",
        "updateTime": "2023-09-02 12:08:33"
    }
])
//调用接口获取文章分类列表,声明异步函数
import { articleCategoryListService, ArticleCategoryaddService, ArticleCategoryupdateService, ArticleCategorydeleteService } from '@/api/article.js'
const articleCategoryList = async () => {
    let result = await articleCategoryListService()
    categorys.value = result.data
}
articleCategoryList();
//控制添加分类弹窗
const dialogVisible = ref(false)

//添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: '',
    id: ''
})
//添加分类表单校验
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}
//调用接口添加表单的函数
const addCategory = async () => {
    let result = await ArticleCategoryaddService(categoryModel.value)
    if (result.code == 0) {
        ElMessage.success(result.msg ? result.msg : '添加分类成功')
        dialogVisible.value = false
        articleCategoryList()
    } else {
        ElMessage.error(result.msg ? result.msg : '添加分类失败')
    }
}
const updateCategory = async () => {
    let result = await ArticleCategoryupdateService(categoryModel.value)
    if (result.code == 0) {
        ElMessage.success(result.msg ? result.msg : '修改分类成功')
        dialogVisible.value = false
        articleCategoryList()
    } else {
        ElMessage.error(result.msg ? result.msg : '修改分类失败')
    }
}
//定义变量，控制弹窗标题
const title = ref('')
const showDialog = (row) => {
    dialogVisible.value = true; title.value = '编辑分类'
    categoryModel.value.categoryName = row.categoryName
    categoryModel.value.categoryAlias = row.categoryAlias
    categoryModel.value.id = row.id
}
//添加分类之前清空数据
const clearModel = () => {
    categoryModel.value.categoryName = ''
    categoryModel.value.categoryAlias = ''
    categoryModel.value.id = ''
}
//删除分类
import { ElMessageBox } from 'element-plus'

const deleteCategory = (row) => {
    //提示框

    ElMessageBox.confirm(
        '确定删除吗？',
        '温馨提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            let result = await ArticleCategorydeleteService(row.id);


            ElMessage({
                type: 'success',
                message: '删除成功',
            })

            articleCategoryList();

        })
        .catch(() => {

            ElMessage({
                type: 'info',
                message: '已取消删除',
            })


        })
}


</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary"
                        @click="dialogVisible = true; title = '添加分类'; clearModel()">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="categoryName"></el-table-column>
            <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
    </el-card>
    <!-- 添加分类弹窗 -->
    <el-dialog v-model="dialogVisible" :title="title" width="30%">
        <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
            <el-form-item label="分类名称" prop="categoryName">
                <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"></el-input>
            </el-form-item>
            <el-form-item label="分类别名" prop="categoryAlias">
                <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="title == '添加分类' ? addCategory() : updateCategory()"> 确认 </el-button>
            </span>
        </template>
    </el-dialog>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>
