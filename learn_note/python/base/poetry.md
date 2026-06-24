
# 1.包管理工具
1. pip : Python 官方自带的包安装器，只负责安装包
2. pipx	: 专门用来安装和运行 Python 命令行工具的工具
3. poetry : Python 项目的全流程管理工具（依赖 + 虚拟环境 + 打包 + 发布）
4. uv : 用 Rust 编写的超快Python包管理器，替代 pip/pipx/venv

四、功能对比总表

|功能|pip| pipx |poetry|uv|
|---|---|--|---|---|
|安装 Python 包	|✅	| ❌|✅	|✅|
|安装命令行工具|✅（但不隔离）|✅（隔离环境）|❌|✅|
|虚拟环境管理|❌|✅（自动）|✅（自动）|✅（自动）|
|依赖解析|基础|不涉及|✅ 强|✅ 强|
|锁文件|❌|❌|✅ poetry.lock|✅ uv.lock|
|打包发布|❌|❌|✅|✅|
|Python 版本管理|❌|❌|❌|✅|
|速度|慢|中|中|⚡极快|
|语言实现|Python|Python|Python|Rust|
|学习成本|低|低|中|低|

时间线:
2000s   pip 出现（Python 官方包管理器）  
│
2018    pipx 出现（解决全局 CLI 工具安装问题）  
│
2018    poetry 出现（解决项目管理全流程问题）  
│
2024    uv 出现（Rust 重写，一个工具解决所有问题）  
│
▼
未来趋势: uv 逐步成为 Python 生态的统一工具

# 2.Poetry
## 2.1安装和卸载
```
# 安装最新版
pipx install poetry
# 安装指定版本
pipx install poetry==1.8.4
# 更新
pipx upgrade poetry
# 卸载
pipx uninstall poetry
```

## 2.2初始化项目 

```
# 创建全新项目
poetry new poetry-demo

# 存量项目初始化
cd pre-existing-project
poetry init

# 项目结构
poetry-demo
├── pyproject.toml
├── README.md
├── src
│   └── poetry_demo
│       └── __init__.py
└── tests
└── __init__.py

# pyproject.toml
[project]
name = "poetry-demo"
version = "0.1.0"
description = ""
authors = [
    {name = "Sébastien Eustace", email = "sebastien@eustace.io"}
]
readme = "README.md"
requires-python = ">=3.9" #解释器版本要求
dependencies = [
    "pendulum (>=2.1,<3.0)"   # 修改文件和执行命令效果一致$ poetry add pendulum
]

[build-system]
requires = ["poetry-core>=2.0.0,<3.0.0"]
build-backend = "poetry.core.masonry.api"

[tool.poetry]
package-mode = false #是否安装项目本身到虚拟环境中
```

## 2.3使用虚拟环境
```
#使用虚拟环境中python及相关库执行脚本
poetry run python your_script.py 
```

## 2.4安装依赖
```
poetry install
poetry.lock不存在:解析toml生成lock文件
poetry.lock存在:解析toml,使用lock文件中的版本保持版本一致


# 只安装依赖不安装项目本身
poetry install --no-root

# 重新解析并更新lock文件版本，等于删掉lock文件再执行install
poetry update
```

## 2.5 依赖组
```
# main组有project.dependencies, tool.poetry.dependencies, 其他可以自定义

[dependency-groups]
docs = [
    "mkdocs",
]

[tool.poetry.group.docs]
optional = true

# 除默认依赖安装可选依赖
poetry install --with docs

# 添加依赖到指定的组
poetry add pytest --group test

# 排除指定的组
poetry install --without test,docs

# 只安装某些组
poetry install --only docs

# 只安装项目本身
poetry install --only-root

# 从特定的组移除依赖
poetry remove mkdocs --group docs

# 同步.venv和lock一致
poetry sync --without dev
poetry sync --with docs
poetry sync --only dev
```

# 2.6 打包
```
[build-system]
requires = ["poetry-core>=2.0.0,<3.0.0"]
build-backend = "poetry.core.masonry.api"

# 打包
poetry build

# 发布PyPI
poetry publish

# 发布到私有库
poetry publish -r my-repository
```

# 2.7 命令
```
# 列出所有命令
poetry

# --verbose (-v|vv|vvv)
poetry install  #正常安装
poetry install -v  #看到更多安装细节（如每个包的下载进度）
poetry install -vv  #看到更详细的依赖解析过程
poetry install -vvv  # 调试级别，看到所有内部操作（排查问题时用）

```
[commands](https://python-poetry.org/docs/cli/)

# 2.8 配置
```
# 列出所有配置
poetry config --list
```
[config](https://python-poetry.org/docs/configuration/)

# 2.9 仓库
```
# 添加私有库
poetry source add --priority=supplemental foo https://pypi.example.org/simple/
```
[repository](https://python-poetry.org/docs/repositories/)

# 2.10 环境
```
# 显示环境信息
poetry env info

# 列出所有环境
poetry env list

# 指定python版本
poetry env use python3.7
# 使用系统默认版本
poetry env use system

# 移除环境
poetry env remove /full/path/to/python
poetry env remove python3.7
poetry env remove 3.7
```

# 2.11 依赖
```
# 写法一：标准写法（project.dependencies）
# 按照 PEP 508 字符串格式
[project]
dependencies = [
    "requests>=2.28,<3.0",
    "pendulum>=2.1,<3.0"
]

# 写法二：Poetry 专属写法（tool.poetry.dependencies）
# 使用 TOML 表格式，可以定义额外信息
[tool.poetry.dependencies]
requests = {version = ">=2.28,<3.0", source = "private-pypi"}
pendulum = {version = ">=2.1,<3.0", extras = ["test"]}

什么时候必须用 tool.poetry.dependencies
# 场景一：指定包的下载源（source）
[tool.poetry.dependencies]
requests = {version = ">=2.28", source = "aliyun"}

# 场景二：使用 Poetry 特有的版本约束语法
[tool.poetry.dependencies]
python = "^3.10"           # ^ 语法是 Poetry 特有的，PEP 508 不支持

# 场景三：指定可选依赖（extras）
[tool.poetry.dependencies]
pendulum = {version = ">=2.1", extras = ["test"]}

project.dependencies是标准写法，推荐新项目使用；
tool.poetry.dependencies是Poetry 专属写法，在需要 Poetry 特有功能时使用。
两者可以共存，但建议尽量用标准写法，只在必要时用Poetry专属写法补充额外信息。
```
[dependency](https://python-poetry.org/docs/dependency-specification/)

# 2.12 插件
[plugins](https://python-poetry.org/docs/plugins/)

# 2.13 pyproject.toml
[pyproject](https://python-poetry.org/docs/pyproject/)
```
[project]
dependencies = [
    # 最简单：只写包名
    "click",

    # 固定版本
    "Flask==3.0.0",

    # 范围约束
    "requests>=2.28,<3.0",

    # 兼容版本
    "pendulum~=2.1",

    # 带可选依赖
    "requests[security,socks]>=2.28",

    # 带环境标记（只在 Windows 上安装）
    "pywin32>=227; sys_platform=='win32'",

    # 带环境标记（只在 Python 3.10+ 上安装）
    "tomli>=2.0; python_version<'3.11'",

    # 组合：版本约束 + 可选依赖 + 环境标记
    "uvicorn[standard]>=0.20; python_version>='3.8' and sys_platform!='emscripten'",
]
```