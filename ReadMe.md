* 使用SSM + OLTU框架实现 OAuth Server 授权认证
* 可利用 本地cookie 实现多系统的单点登录  
> 关于用户持久的登录状态，是根据用户本地 Cookie 中的 cert 判断的。cert 其实是用户密码加密过程中使用的 salt。salt根据 Java的 UUID 生成，可以基本保证 salt 的唯一性。 

* 本 demo 只写到，Code2Token这一步，关于后续使用token换取数据，未完成，只写以下简单思路：
> cache 中校验 access_token 是否有效，无效则返回错误信息；有效则返回用户请求的相关数据

* 此 demo 未考虑申请不同权限的操作，只涉及简单的 code以及token下发

* 如果需要配合[SpringBoot-OAuth2Client](https://github.com/whitecathat/SpringBoot-OAuth2Client) 理解 OAuth2 授权码认证流程，请根据 **SpringBoot-OAuth2Client** 项目内的 ConfigConsts.java 文件内的相应数据，填充当前项目 **client** 表中数据库
