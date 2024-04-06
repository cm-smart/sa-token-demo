package com.chen.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.chen.util.AjaxJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/acc")
public class LoginController {
    //测试登录  ---- http://localhost:8080/acc/doLogin?name=chenmin&pwd=cm021035
    @RequestMapping("doLogin")
    public SaResult doLogin(String name,String pwd){
        if("chenmin".equals(name) && "cm021035".equals(pwd)){
            StpUtil.login(10001);
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    // 查询登录状态  ---- http://localhost:8080/acc/isLogin
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    // 查询 Token 信息  ---- http://localhost:8080/acc/tokenInfo
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 测试注销  ---- http://localhost:8080/acc/logout
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    // 测试角色接口， 浏览器访问： http://localhost:8080/acc/testRole
    @RequestMapping("testRole")
    public AjaxJson testRole() {
        System.out.println("======================= 进入方法，测试角色接口 ========================= ");

        System.out.println("是否具有角色标识 user " + StpUtil.hasRole("user"));
        System.out.println("是否具有角色标识 admin " + StpUtil.hasRole("admin"));

        System.out.println("没有admin权限就抛出异常");
        StpUtil.checkRole("admin");

        System.out.println("在【admin、user】中只要拥有一个就不会抛出异常");
        StpUtil.checkRoleOr("admin", "user");

        System.out.println("在【admin、user】中必须全部拥有才不会抛出异常");
        StpUtil.checkRoleAnd("admin", "user");

        System.out.println("角色测试通过");

        return AjaxJson.getSuccess();
    }

    //测试权限接口    http://localhost:8080/acc/testJur
    @RequestMapping("testJur")
    public AjaxJson testJur(){
        System.out.println("======================= 进入方法，测试权限接口 ========================= ");
        System.out.println("是否具有权限101" + StpUtil.hasPermission("101"));
        System.out.println("是否具有权限user-add"+StpUtil.hasPermission("user-add"));
        System.out.println("是否具有权限article-get" + StpUtil.hasPermission("article-get"));
        System.out.println("没有user-add权限就抛出异常");
        StpUtil.checkPermission("user-add");
        System.out.println("在【101，102】中必须全部拥有才不会抛出异常");
        StpUtil.checkPermissionAnd("101","102");
        System.out.println("在【101，102】中只要拥有一个就不会抛出异常");
        StpUtil.checkPermissionOr("101","102");
        System.out.println("权限测试通过");

        return AjaxJson.getSuccess();
    }

    // 测试会话session接口， 浏览器访问： http://localhost:8080/acc/session
    @RequestMapping("session")
    public AjaxJson session() throws JsonProcessingException{
        System.out.println("======================= 进入方法，测试会话session接口 ========================= ");
        System.out.println("当前是否登录：" + StpUtil.isLogin());
        System.out.println("当前登录账号session的id:" + StpUtil.getSession().getId());
        System.out.println("测试取值name:" + StpUtil.getSession().get("name"));
        StpUtil.getSession().set("name",new Date());//写入一个值
        System.out.println("测试取值name:" + StpUtil.getSession().get("name"));
        System.out.println( new ObjectMapper().writeValueAsString(StpUtil.getSession()));
        return AjaxJson.getSuccess();
    }

    //测试自定义session接口，
    @RequestMapping("session2")
    public AjaxJson session2(){
        System.out.println("======================= 进入方法，测试自定义session接口 ========================= ");
        System.out.println("当前是否登录：" + StpUtil.isLogin());
        // 自定义session就是无需登录也可以使用 的session ：比如拿用户的手机号当做 key， 来获取 session
        System.out.println("自定义session的id为：" + SaSessionCustomUtil.getSessionById("15861803968").getId());
        System.out.println("测试取值name：" + SaSessionCustomUtil.getSessionById("1586180396").get("name"));
        SaSessionCustomUtil.getSessionById("15861803968").set("name", "陈敏");	// 写入值
        System.out.println("测试取值name：" + SaSessionCustomUtil.getSessionById("15861803968").get("name"));
        return AjaxJson.getSuccess();
    }

    //测试token专属session,http://localhost:8080/acc/getTokenSession
    @RequestMapping("getTokenSession")
    public AjaxJson getTokenSession(){
        System.out.println("======================= 进入方法，测试会话session接口 ========================= ");
        System.out.println("当前是否登录：" + StpUtil.isLogin());
        System.out.println("当前token专属session:" + StpUtil.getTokenSession().getId());
        System.out.println("测试取值name：" + StpUtil.getTokenSession().get("name"));
        StpUtil.getTokenSession().set("name","张三");
        System.out.println("测试取值name：" + StpUtil.getTokenSession().get("name"));
        return AjaxJson.getSuccess();
    }

    // 打印当前token信息， 浏览器访问： http://localhost:8080/acc/getTokenInfo
    @RequestMapping("getTokenInfo")
    public AjaxJson getTokenInfo(){
        System.out.println("======================= 进入方法，打印当前token信息 ========================= ");
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        System.out.println(tokenInfo);
        return AjaxJson.getSuccessData(tokenInfo);
    }

    //注解式鉴权，http://localhost:8080/acc/atCheck
    @SaCheckLogin
    @SaCheckRole("super-admin")
    @SaCheckPermission("user-add")
    @RequestMapping("atCheck")
    public AjaxJson atCheck() {
        System.out.println("======================= 进入方法，测试注解鉴权接口 ========================= ");
        System.out.println("只有通过注解鉴权，才能进入此方法");
//		StpUtil.checkActiveTimeout();
//		StpUtil.updateLastActiveToNow();
        return AjaxJson.getSuccess();
    }
}
