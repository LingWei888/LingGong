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
        <el-select v-model="query.address" placeholder="搜索用户" class="handle-select mr10">
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
        <el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
        <el-table-column prop="user" label="用户名"></el-table-column>
        <el-table-column label="角色">
          <template slot-scope="scope">{{scope.row.roleid===0?'自由工作者' : '雇主'}}</template>
        </el-table-column>

        <el-table-column prop="mail" label="邮箱"></el-table-column>
        <el-table-column label="角色状态" align="center">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.roleid=='-2'?'warning':(scope.row.roleid=='1'?'success':(scope.row.roleid=='0'?'primary':(scope.row.roleid=='-1'?'danger':'')))"
            >
              {{
                scope.row.roleid == '-2' ? '待审核' :
                    scope.row.roleid == '1' ? '雇主' :
                        scope.row.roleid == '0' ? '自由工作者' :
                            scope.row.roleid=='-1'? '未实名' :
                                '未知状态'
              }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="addtime" label="注册时间"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button
                type="text"
                icon="el-icon-edit"
                @click="handleEdit(scope.$index, scope.row)"
            >编辑</el-button>
            <el-button
                type="text"
                icon="el-icon-delete"
                class="red"
                @click="handleDelete(scope.$index, scope.row)"
            >删除</el-button>
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
    <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
      <el-form ref="form" :model="form" label-width="70px">
        <el-form-item label="ID">
          <el-input v-model="form.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.user" disabled></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.pwd"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-input v-model="form.roleid"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.mail"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="form.state"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchData } from '../../api/index';
export default {
  name: '/user',
  data() {
    return {
      query: {
        address: '',
        user: '',
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
      id: -1
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
          .post(this.Api+'Admin/userList', {
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
    // 删除操作
    handleDelete(index, row) {
      // 二次确认删除
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      })
          .then(() => {
            this.$axios
                .post(this.Api+'Admin/userDelete', {
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
                });

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
        str += this.multipleSelection[i].id + ' ';
        this.$axios
            .post(this.Api+'Admin/userDelete', {
              'id': this.multipleSelection[i].id
            }, {
              headers: {
                'token':  localStorage.getItem('token')
              }
            })
            .then(res => {
              if (res.data.code == 200) {
                this.tableData.splice(index, 1);
              }
            });
      }
      this.$message.error(`删除了${str}`);
      this.multipleSelection = [];
      this.getData();
    },
    // 编辑操作
    handleEdit(index, row) {
      this.idx = index;
      this.form = row;
      this.editVisible = true;
    },
    // 保存编辑
    saveEdit() {
      this.$axios
          .post(this.Api+'Admin/userSave', {
            'id': this.form.id,
            'user': this.form.user,
            'pwd': this.form.pwd,
            'state': this.form.state,
            'roleid': this.form.roleid,
            'mail': this.form.mail

          }, {
            headers: {
              'token':  localStorage.getItem('token')
            }
          })
          .then(res => {
            if (res.data.code == 200) {
              this.editVisible = false;
              this.$set(this.tableData, this.idx, this.form);
              this.$message.success('修改成功');
            }
          })

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
