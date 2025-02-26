<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 基础表格
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box" style="text-align: center">
                <el-input v-model="query.title" placeholder="项目名" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            </div>
            <el-table
                :data="tableData"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
            >
                <el-table-column prop="title" label="项目名称"></el-table-column>
                <el-table-column label="项目金额">
                    <template slot-scope="scope">￥{{scope.row.money}}</template>
                </el-table-column>
                <el-table-column prop="acid" label="项目雇主"></el-table-column>
                <el-table-column label="状态" align="center">
                    <template slot-scope="scope">
                        <el-tag
                            :type="scope.row.state==0?'success':(scope.row.state==1?'danger':'')"
                        >{{scope.row.state==0?'雇佣中':'已结束'}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="addtime" label="项目发布时间">
                    <template slot-scope="scope">{{ formatDate(scope.row.addtime) }}</template>
                </el-table-column>
                <el-table-column label="对于该项目感兴趣" width="180" align="center">
                    <template slot-scope="scope">
                      <el-button
                          type="text"
                          icon="el-icon-lx-read"
                          @click="handleEdit(scope.$index, scope.row)"
                      >查看详情</el-button>
                      <el-button
                          type="text"
                          icon="el-icon-lx-friendaddfill"
                          class="red"
                          @click="handleDelete(scope.$index, scope.row)"
                      >联系雇主</el-button>

                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination" style="text-align: center">
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
        <!-- 编辑弹出框 -->
        <el-dialog title="项目详细需求" :visible.sync="editVisible" width="30%">
            <div>项目标题：{{Result.title}}</div><br>
            <div>项目具体内容：{{Result.content}}</div><br>
            <div>结束时间：{{formatDate(Result.endtime)}}</div><br></br>
            <div>项目要求：</div>
            <div v-for="(item,index) in Result.projectrequire" :key="index">
              <li>{{item.content}}</li>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button v-if="roleid != 1" type="primary" @click="saveEdit">申请该项目</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { fetchData } from '../../api/index';
export default {
    name: 'projecthall',
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
            pageTotal: 0,
            form: {},
            idx: -1,
            id: -1,
            Result: {},
            // 添加 roleid 数据
            roleid: localStorage.getItem('roleid')
        };
    },
    created() {
      this.getData();
    },
    methods: {
      // 获取 easy-mock 的模拟数据
      getData() {
        //console.log(localStorage.getItem('token'));
        this.$axios
            .post(this.Api + 'User/projectList', {
              'pageNum': this.query.pageIndex,
              'pageSize': this.query.pageSize,
              'keyword': this.query.title

            }, {
              headers: {
                'token': localStorage.getItem('token')
              }
            })
            .then(res => {
                  if (res.data.code == 200) {
                    this.tableData = res.data.data.records;
                    this.pageTotal = res.data.data.total;
                  }
                },
                //判断如果报错就跳转到登录页
                error => {
                  this.$message.error('请重新登录');
                  this.$router.push('/login');
                }
            );
      },
      // 触发搜索按钮
      handleSearch() {
        this.$set(this.query, 'pageIndex', 1);
        this.$set(this.query, 'title', this.query.title);

        this.getData();
      },
      saveEdit() {
        this.$axios
            .post(this.Api + 'User/enterProject', {
              'id': this.form.id
            }, {
              headers: {
                'token': localStorage.getItem('token')
              }
            })
            .then(res => {
                  if (res.data.code == 200) {
                    this.$message.success(`申请成功！`);
                    this.editVisible = false;
                  } else {
                    this.$message.error(res.data.msg);
                    this.editVisible = false;
                  }
                }
            );
      },
      handleEdit(index, row) {
        this.idx = index;
        this.form = row;
        this.$axios
            .post(this.Api + 'User/cxProjectInfo', {
              'id': row.id
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
      handleDelete(index, row) {
        this.$router.push('/chat?id=' + row.uid);
      },
      // 分页导航
      handlePageChange(val) {
        this.$set(this.query, 'pageIndex', val);
        this.getData();
      },
      // 添加时间格式化方法
      formatDate(timestamp) {
        //将timestamp 转换为 int类型
        timestamp = parseInt(timestamp);
        // 检查 timestamp 是否为有效数字
        if (typeof timestamp === 'number' && !isNaN(timestamp)) {
          // 如果时间戳是以秒为单位的，乘以 1000 转换为毫秒
          if (timestamp < 1e10) { // 假设时间戳小于 10 亿的是以秒为单位的
            timestamp *= 1000;
          }
          const date = new Date(timestamp);
          return date.toLocaleString(); // 或者使用自定义格式化方法
        } else {
          // 返回默认值或空字符串
          return "Invalid Date";
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
</style>
