#### prettier全局代码
```npx prettier --write .```


#### 权限码命名规则 类+"-"+方法名        
```
类/webapi/report/buSummary 
详情方法:toDetailPage
例如 事业部业务经销存详情权限的权限码buSummary_toDetailPage
```

#### git提交规范
```
feat 增加新功能
fix 修复问题/BUG
style 代码风格相关无影响运行结果的
perf 优化/性能提升
refactor 重构
revert 撤销修改
test 测试相关
docs 文档/注释
chore 依赖更新/脚手架配置修改等
workflow 工作流改进
ci 持续集成
types 类型定义文件更改
wip 开发中
```

#### windows 下快速删除node_modules
```
npm install rimraf -g
rimraf node_modules
```
