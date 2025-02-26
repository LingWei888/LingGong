<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 项目管理
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
                <el-select v-model="query.address" placeholder="搜索项目" class="handle-select mr10">
                    <el-option key="1" label="项目标题" value="项目标题"></el-option>
                </el-select>
                <el-input v-model="query.title" placeholder="搜索内容" class="handle-input mr10"></el-input>
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
                <el-table-column prop="title" label="项目标题"></el-table-column>
                <el-table-column label="所属用户">
                    <template slot-scope="scope">{{scope.row.uid}}</template>
                </el-table-column>

                <el-table-column label="预算金额">
                  <template slot-scope="scope">{{scope.row.money}}元</template>
                </el-table-column>

                <el-table-column prop="acid" label="接受用户"></el-table-column>
                <el-table-column label="状态" align="center">
                    <template slot-scope="scope">
                        <el-tag
                            :type="scope.row.state=='0'?'danger':(scope.row.state=='9'?'success':'')"
                        >{{scope.row.state=='0'?'未接单':(scope.row.state=='9'?'已完成':'进行中')}}</el-tag>
                    </template>
                </el-table-column>

                <el-table-column prop="addtime" label="添加时间"></el-table-column>
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
                <el-form-item label="项目名">
                    <el-input v-model="form.title"></el-input>
                </el-form-item>
                <el-form-item label="所属用户">
                    <el-input v-model="form.uid"></el-input>
                </el-form-item>
                <el-form-item label="预算金额">
                  <el-input v-model="form.money"></el-input>
                </el-form-item>
              <el-form-item label="投标用户">
                <el-input v-model="form.acid"></el-input>
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
    name: 'projecttable',
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
            id: -1
        };
    },
    created() {
        this.getData();
    },
    methods: {
        // 获取 easy-mock 的模拟数据
        getData() {
          this.$axios
              .post(this.Api+'Admin/projectList', {
                'pageNum': this.query.pageIndex,
                'pageSize': this.query.pageSize,
                'keyword': this.query.title

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
        },
        // 触发搜索按钮
        handleSearch() {
            this.$set(this.query, 'pageIndex', 1);
            this.$set(this.query, 'title', this.query.title);
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
                        .post(this.Api+'Admin/projectDelete', {
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
            .post(this.Api+'Admin/projectDelete', {
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
                .post(this.Api+'Admin/projectSave', {
                    'id': this.form.id,
                    'title': this.form.title,
                    'uid': this.form.uid,
                    'acid': this.form.acid,
                    'state': this.form.state,
                    'money': this.form.money
                }, {
                    headers: {
                        'token':  localStorage.getItem('token')
                    }
                })
                .then(res=> {
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
