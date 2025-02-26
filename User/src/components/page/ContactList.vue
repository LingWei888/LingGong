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
            <div class="handle-box">
                <el-button
                    type="primary"
                    icon="el-icon-delete"
                    class="handle-del mr10"
                    @click="delAllSelection"
                >批量删除</el-button>
                <el-select v-model="query.select" placeholder="用户名" class="handle-select mr10">
                    <el-option key="1" label="用户名" value="用户名"></el-option>
                </el-select>
                <el-input v-model="query.user" placeholder="用户名" class="handle-input mr10"></el-input>
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
                <el-table-column type="selection" width="55" align="center"></el-table-column>

                <el-table-column label="头像(查看大图)" align="center">
                  <template slot-scope="scope">
                    <el-image
                        class="table-td-thumb"
                        :src="'https://q1.qlogo.cn/g?b=qq&nk=' + scope.row.qq + '&s=100'"
                        :preview-src-list="['https://q1.qlogo.cn/g?b=qq&nk=' + scope.row.qq + '&s=100']"
                    ></el-image>
                  </template>
                </el-table-column>
                <el-table-column prop="toName" label="用户名(点击快速聊天)"></el-table-column>


                <el-table-column label="消息" align="center">
                    <template slot-scope="scope">
                        <el-tag
                            :type="scope.row.num>0?'success':'danger'"
                        >{{scope.row.num}}</el-tag>
                    </template>
                </el-table-column>


                <el-table-column label="操作" width="180" align="center">
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
                        >查看信息</el-button>

                        <el-button
                            type="text"
                            icon="el-icon-delete"
                            class="red"
                            @click="handleDelete(scope.$index, scope.row)"
                        >删除记录</el-button>
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

      <!-- 编辑弹出框 -->
      <el-dialog title="用户信息" :visible.sync="editVisible" width="30%">

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
                <el-button type="primary" @click="saveEdit">联系该好友</el-button>
            </span>
      </el-dialog>
    </div>
</template>

<script>
import { fetchData } from '../../api/index';
import router from "../../router";
export default {
    name: 'basetable',
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
            Result: {}

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
              .post(this.Api+'User/contactList', {
                'pageNum': this.query.pageIndex,
                'pageSize': this.query.pageSize,
                'keyword': this.query.user

              }, {
                headers: {
                  'token':  localStorage.getItem('token')
                }
              })
              .then(res => {
                    if (res.data.code == 200) {
                      this.tableData=res.data.data.records;
                      this.pageTotal=res.data.data.total;


                    }
                  },
                  //判断如果报错就跳转到登录页
                  error => {
                    this.$message.error('请重新登录');
                    this.$router.push('/login');
                  }


              );


          /*fetchData(this.query).then(res => {
            console.log(res);
            this.tableData = res.list;
            this.pageTotal = res.pageTotal || 50;
          });*/
        },
        // 触发搜索按钮
        handleSearch() {
          this.$set(this.query, 'pageIndex', 1);
          this.$set(this.query, 'user', this.query.user);

          this.getData();
        },
        talk( index, row) {
          this.$router.push('/chat?id=' + row.toid);
        },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {

                  this.$axios
                      .post(this.Api+'User/delContact', {
                          'id': row.id
                      }, {
                        headers: {
                          'token':  localStorage.getItem('token')
                        }
                      })
                      .then(res => {
                        if (res.data.code == 200) {
                          this.$message.success('删除成功');
                          this.tableData.splice(index, 1);
                        }
                      })
                })
                .catch(() => {});
        },
        // 多选操作
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        delAllSelection() {
            const length = this.multipleSelection.length;
            let str = '';
            this.delList = this.delList.concat(this.multipleSelection);
            for (let i = 0; i < length; i++) {
                str += this.multipleSelection[i].name + ' ';
            }
            this.$message.error(`删除了${str}`);
            this.multipleSelection = [];
        },
        // 编辑操作
        handleEdit(index, row) {
          this.idx = index;
          this.form = row;
          this.$axios
              .post(this.Api+'User/cxUserInfo', {
                'id': row.toid
              }, {
                headers: {
                  'token':  localStorage.getItem('token')
                }
              })
              .then(res => {
                    if (res.data.code == 200) {
                      this.Result=res.data.data;
                    }
                  }
              );
          this.editVisible = true;
        },
        // 保存编辑
        saveEdit() {
            this.editVisible = false;
            this.$router.push('/chat?id=' + this.form.toid);
        },
        // 分页导航
        handlePageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getData();
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
