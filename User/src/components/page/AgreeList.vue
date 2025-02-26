<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 项目审核
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">

      <!-- 项目详情部分 -->
      <div class="project-details">
        <h2><strong>项目标题:</strong> {{ projectDetails.title }}</h2>
        <p><strong>项目内容:</strong> {{ projectDetails.content }}</p>
        <div><strong>项目需求进度:</strong>
          <el-card>
            <ul>
              <li v-for="(req, index) in projectDetails.requirements" :key="index">
                {{ req.content }} - {{ req.statusText }}
                <el-button v-if="req.state === 1" @click="acceptRequirement(req.id, 2)">验收需求</el-button>
                <el-button v-if="req.state === 1" @click="acceptRequirement(req.id, 0)">拒收需求</el-button>
              </li>
            </ul>
          </el-card>
        </div>
        <el-button
          type="primary"
          icon="el-icon-check"
          :disabled="!canCompleteProject(projectDetails.requirements)"
          @click="completeProject"
        >完成项目</el-button>
        <el-button
          type="primary"
          icon="el-icon-star-on"
          :disabled="!canRateProject"
          @click="rateHandler"
        >评分</el-button>
      </div>

      <!-- 申请人列表部分 -->
      <div class="handle-box" style="margin-top: 20px;">
        <el-select v-model="query.address" placeholder="用户名" class="handle-select mr10">
          <el-option key="1" label="广东省" value="用户名"></el-option>
        </el-select>
        <el-input v-model="query.name" placeholder="用户名" class="handle-input mr10"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>
      <el-table
          :data="tableData"
          border
          class="table"
          ref="multipleTable"
          header-cell-class-name="table-header"
      >
        <el-table-column label="头像(查看大图)" align="center">
          <template slot-scope="scope">
            <el-image
                class="table-td-thumb"
                :src="'https://q1.qlogo.cn/g?b=qq&nk=' + scope.row.qq + '&s=100'"
                :preview-src-list="['https://q1.qlogo.cn/g?b=qq&nk=' + scope.row.qq + '&s=100']"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户名"></el-table-column>
        <el-table-column label="操作" width="300" align="center">
          <template slot-scope="scope">
            <el-button
                type="text"
                icon="el-icon-lx-comment"
                @click="talk(scope.$index, scope.row)"
            >聊聊详情</el-button>
            <el-button
                type="text"
                icon="el-icon-lx-people"
                @click="handleEdit(scope.$index, scope.row)"
            >查看简介</el-button>
            <el-button
                type="text"
                icon="el-icon-check"
                @click="selectAsProjectHandler(scope.$index, scope.row)"
            >选为项目处理者</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            background
            layout="total, prev, pager, next"
            :current-page="query.pageIndex"
            :page-size="query.pageSize"
            :total="pageTotal"
            @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>

    <!-- 查看简介弹出框 -->
    <el-dialog title="用户简介" :visible.sync="editVisible" width="30%">
      <div>用户账号：{{Result.user}}</div><br>
      <div>用户邮箱：{{Result.mail}}</div><br>
      <div>用户标签：{{Result.tips}}</div><br>
      <div v-for="(item,index) in Result.introduce" :key="index">
        <li>{{item.title}} : {{item.content}}</li>
      </div>
      <br>
      <div v-for="(item,index) in Result.prorate" :key="index">
        <li>项目【{{item.project}}】_编号{{item.user}}评分: {{item.rate}}</li>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 评分弹出框 -->
    <el-dialog title="评分" :visible.sync="rateVisible" width="30%">
      <el-rate v-model="rating" show-text></el-rate>
      <span slot="footer" class="dialog-footer">
        <el-button @click="rateVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitRating">提交评分</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchData } from '../../api/index';
import router from "../../router";
export default {
  name: 'projectReview',
  data() {
    return {
      query: {
        address: '',
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      multipleSelection: [],
      delList: [],
      editVisible: false,
      rateVisible: false,
      pageTotal: 0,
      form: {},
      idx: -1,
      id: -1,
      Result: {},
      rating: 0,
      projectId: '', // 假设你有一个 projectId
      projectDetails: {
        amount: '',
        title: '',
        content: '',
        requirements: []
      }
    };
  },
  created() {
    // 从URL中提取项目ID

    const hash = window.location.hash;

    // 去掉开头的 '#'
    const hashWithoutHash = hash.substring(1);

    // 创建 URL 对象
    const url = new URL(hashWithoutHash, this.Api);

    // 使用 URLSearchParams 解析查询参数
    const searchParams = new URLSearchParams(url.search);

    // 获取 id 参数
    this.projectId = searchParams.get('id');

    this.getProjectDetails();
  },
  methods: {
    // 获取项目详情
    getProjectDetails() {
        this.$axios
            .post(this.Api+'User/AgreeList', { // 修改请求URL
                'id': this.projectId, // 假设你有一个 projectId
            }, {
                headers: {
                    'token': localStorage.getItem('token')
                }
            })
            .then(res => {
                if (res.data.code == 200) {
                    this.projectDetails = res.data.data;
                    this.projectDetails.requirements = res.data.data.projectrequireList.map(req => ({
                        ...req,
                        statusText: this.getStatusText(req.state) // 修改状态字段
                    }));
                    this.tableData = res.data.data.userVOList;
                }
            })
            .catch(error => {
                this.$message.error('请重新登录');
                router.push('/login');
                localStorage.removeItem('token');
            });
    },
    // 触发搜索按钮
    handleSearch() {
      this.$set(this.query, 'pageIndex', 1);
      this.getData();
    },
    talk(index, row) {
      this.$router.push('/chat?id=' + (row.id-10000));
    },
    // 删除操作
    handleDelete(index, row) {

    },
    // 多选操作
    handleSelectionChange(val) {

    },
    delAllSelection() {

    },
    // 查看简介操作
    handleEdit(index, row) {
      this.idx = index;
      this.form = row;
      this.$axios
          .post(this.Api + 'User/cxUserInfo', {
            'id': row.id-10000
          }, {
            headers: {
              'token': localStorage.getItem('token')
            }
          })
          .then(res => {
            if (res.data.code == 200) {
              this.Result = res.data.data;
            }
          });
      this.editVisible = true;
    },
    // 选为项目处理者操作
    selectAsProjectHandler(index, row) {
      if(this.projectDetails.state!='0'){
        this.$message.error('项目已经开始，无法选为项目处理者');
        return;
      }
      this.$confirm('项目负责人一经处理，就会从您的账户里扣除项目金额给系统，等待项目完成，发送给负责人。是否确认？', '提示', {
        type: 'warning'
      }).then(() => {
          this.$axios
            .post(this.Api + 'User/selectHandler', {
              'projectId': this.projectId, // 假设你有一个 projectId
              'userId': row.id-10000
            }, {
              headers: {
                'token': localStorage.getItem('token')
              }
            })
            .then(res => {
              if (res.data.code == 200) {
                this.$message.success('选为项目处理者成功');
                this.getProjectDetails();
              } else {
                this.$message.error(res.data.msg);
              }
            })
            .catch(error => {
              this.$message.error('选为项目处理者失败');
            });



      })


    },
    // 评分操作
    rateHandler() {
        if (this.canRateProject()) {
            this.rateVisible = true;
        } else {
            this.$message.error('项目尚未完成，无法评分');
        }
    },
    // 提交评分
    submitRating() {
      this.$axios
          .post(this.Api + 'User/rateHandler', {
            'id': this.projectId, // 假设你有一个 projectId
            'rating': this.rating
          }, {
            headers: {
              'token': localStorage.getItem('token')
            }
          })
          .then(res => {
            if (res.data.code == 200) {
              this.$message.success('评分成功');
              this.rateVisible = false;
            } else {
              this.$message.error(res.data.msg);
            }
          })
          .catch(error => {
            this.$message.error('评分失败');
          });
    },
    // 完成项目操作
    completeProject() {
      this.$axios
          .post(this.Api + 'User/completeProject', {
            'id': this.projectId, // 假设你有一个 projectId
          }, {
            headers: {
              'token': localStorage.getItem('token')
            }
          })
          .then(res => {
            if (res.data.code == 200) {
              this.$message.success('项目完成成功');
              this.projectDetails.status = '9';
              this.canRateProject = true;
            } else {
              this.$message.error(res.data.msg);
            }
          })
          .catch(error => {
            this.$message.error('项目完成失败');
          });
    },
    // 验收需求操作
    acceptRequirement(id, action) {
      let message = '';
      if (action === 2) {
        message = '是否确认验收该需求？';
      } else if (action === 0) {
        message = '是否确认拒收该需求？';
      }
      this.$confirm(message, '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios
          .post(this.Api + 'User/completeProjectRequire', {
            'id': id,
            'rating': action
          }, {
            headers: {
              'token': localStorage.getItem('token')
            }
          })
          .then(res => {
            if (res.data.code == 200) {
              this.$message.success(action === 2 ? '验收需求成功' : '拒收需求成功');
              this.getProjectDetails();
            } else {
              this.$message.error(res.data.msg);
            }
          })
          .catch(error => {
            this.$message.error(action === 2 ? '验收需求失败' : '拒收需求失败');
          });
      });
    },
    // 计算项目需求进度
    calculateProjectProgress(requirements) {
      if (!requirements || requirements.length === 0) return 0;
      const completed = requirements.filter(req => req.status === 2).length;
      return (completed / requirements.length) * 100;
    },
    // 判断是否可以完成项目
    canCompleteProject(requirements) {
      if (!requirements || requirements.length === 0) return false;
      return requirements.every(req => req.state === 2);
    },
    // 判断是否可以评分
    canRateProject() {
        return this.projectDetails.state === 9;
    },
    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    },
    // 获取状态文本
    getStatusText(status) {
      switch (status) {
        case 0:
          return '未完成';
        case 1:
          return '已实现';
        case 2:
          return '已完成';
        default:
          return '未知状态';
      }
    }
  }
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.red {
  color: #ff0000;
}
.mr10 {
  margin-right: 10px;
}
.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}

/* 添加样式以增加项目标题、项目内容和项目需求之间的距离 */
.project-details h2 {
  margin-bottom: 15px;
}

.project-details p {
  margin-bottom: 15px;
}

.project-details div {
  margin-bottom: 15px;
}

</style>