:: 项目信息指定
SET oldVersion=0.0.1
SET newVersion=0.0.15-SNAPSHOT
SET projectName=cache

:: maven 命令的执行
mvn versions:set -DgroupId=com.github.mirror -DartifactId=%projectName% -DoldVersion=%oldVersion% -DnewVersion=%newVersion%
mvn -N versions:update-child-modules
mvn versions:commit

:: 用于更新 maven 版本
:: author: mirror
:: 用法：双击运行，或者当前路径 cmd 直接输入 update_version.bat