<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 项目管理
        </el-breadcrumb-item>
        <el-breadcrumb-item>添加项目</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="plugins-tips">
        温馨提示：为了防止恶意修改项目。发布项目后，无法修改项目需求，请谨慎操作。后可经过与自由工作者协商来进行沟通修改。
      </div>
      <div class="form-box">
        <el-form ref="form" :model="form" label-width="100px">
          <el-form-item label="项目标题">
            <el-input v-model="form.title" placeholder="请输入项目标题"></el-input>
          </el-form-item>
          <el-form-item label="项目金额">
            <el-input v-model="form.amount" placeholder="请输入项目金额" type="number"></el-input>
          </el-form-item>
          <el-form-item label="项目内容">
            <el-input type="textarea" v-model="form.content" placeholder="请输入项目内容"></el-input>
          </el-form-item>
          <el-form-item label="项目需求">
            <div v-for="(requirement, index) in form.requirements" :key="index" class="requirement-item">
              <el-input v-model="form.requirements[index]" placeholder="请输入项目需求" style="width: 80%; margin-right: 10px;"></el-input>
              <el-button type="danger" @click="removeRequirement(index)">删除</el-button>
            </div>
            <el-button type="primary" @click="addRequirement">添加需求</el-button>
          </el-form-item>
          <el-form-item label="截止时间"> <!-- 新增表单项 -->
            <el-date-picker
              v-model="form.deadline"
              type="datetime"
              placeholder="选择截止时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="confirmSubmit">发布项目</el-button>
            <el-button @click="resetForm">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'addProject',
  data() {
    return {
      form: {
        title: '',
        amount: '',
        content: '',
        requirements: [],
        deadline: '' // 新增字段
      }
    };
  },
  methods: {
    check() {
      if(roleid == 0){
        this.$message.error('权限不足');
        this.$router.push('/dashboard');
      }
    },
    addRequirement() {
      this.form.requirements.push('');
    },
    removeRequirement(index) {
      this.form.requirements.splice(index, 1);
    },
    confirmSubmit() {
      this.$confirm('项目一旦被添加，将无法更改项目需求，请问是否继续发布?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.onSubmit();
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消发布'
        });
      });
    },
    onSubmit() {
      if (this.form.deadline) {
        this.form.deadline = this.form.deadline.getTime();
      }
      this.$axios
          .post(this.Api + 'User/addproject', this.form, {
            headers: {
              'token': localStorage.getItem('token')
            }
          })
          .then(res => {
            if (res.data.code == 200) {
              this.$message.success('项目添加成功');
              this.resetForm();
              this.$router.push('/myproject');
            } else {
              this.$message.error(res.data.msg);
            }
          })
          .catch(error => {
            this.$message.error('请重新登录');
            localStorage.removeItem('token');
            this.$router.push('/login');
          });
    },
    resetForm() {
      this.$refs.form.resetFields();
    }
  }
};
</script>

<style scoped>
.form-box {
  padding: 20px;

}

.requirement-item {
  margin-bottom: 10px;
}
</style>
