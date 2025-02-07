chcp 65001
echo "注：1、将此bat脚本文件放到项目跟路径下"
echo "    2、启动CMD 参传方式: github.bat r/s"
echo "    3、参数说明：r/s r是release正式版本，s是snapshot快照版本"

:: deploy参数，snapshot 表示快照包，简写为s， release表示正式包，简写为r
set arg=%1

::【需要修改1】：GitHub本地存储库路径
set DEPLOY_PATH=H:/github/maven-repo
::分支
set "branch="

:: 快照包发布 snapshot分支
if "s"=="%arg%" (
    set "branch=snapshot"
)

:: 正式包发布 release分支
if "r"=="%arg%" (
    set "branch=release"
)

H:
cd %DEPLOY_PATH%
git pull

echo 切换对应分支%branch%
git checkout %branch%

::【需要修改2】：项目的磁盘（假设你的项目在H盘）
H:
:: 回到项目当前根目录
cd %~dp0
echo 开始deploy, 将项目发布到本地存储库 %DEPLOY_PATH%
call mvn clean deploy -Dmaven.test.skip -DaltDeploymentRepository=self-mvn-repo::default::file:%DEPLOY_PATH%

H:
cd %DEPLOY_PATH%
echo 本地存储库的发送到github仓库 %branch% 分支上
git add .
git commit -m "提交新的版本"
git pull
git push origin %branch%

echo 将 %branch% 分支合并到 main 分支
git checkout main
git add .
git commit -m 'main'
git merge %branch%
git commit -m 'main merge'
git push origin main

pause
