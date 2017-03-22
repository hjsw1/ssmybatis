整合Spring SpringMvc Mybatis,搭建SSM框架
2017-02-13 11:33
在Eclipse下创建Java Web项目
所用到的jar包：http://download.csdn.net/detail/askycat/9753100 
我把所有代码都贴出来了，所以看起来有点长 
目录结构：这里写图片描述
http://www.itnose.net/img/20170213/272307.png

MySQL数据库中新建一个ssm数据库，创建一张t_user表

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
 `uid` int(11) NOT NULL AUTO_INCREMENT,
 `uname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
 `upwd` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
 `umessage` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
 `utime` datetime DEFAULT NULL,
 PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'crazy', '123', '不详', '2017-02-13 08:07:13');
INSERT INTO `t_user` VALUES ('2', 'crazy', '123', '不详', '2017-02-13 08:08:30');
编写springmvc.xml这里写图片描述

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
 xmlns:mvc="http://www.springframework.org/schema/mvc"
 xsi:schemaLocation="http://www.springframework.org/schema/beans
 http://www.springframework.org/schema/beans/spring-beans.xsd
 http://www.springframework.org/schema/context
 http://www.springframework.org/schema/context/spring-context-4.0.xsd
 http://www.springframework.org/schema/mvc
 http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd">

    <!-- 注解扫描包 -->
    <context:component-scan base-package="com.crazy.controller" />

    <!-- 开启注解 -->
    <mvc:annotation-driven />

    <!-- 定义跳转的文件的前后缀 ，视图模式配置-->
    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <!-- 这里的配置我的理解是自动给后面action的方法return的字符串加上前缀和后缀，变成一个 可用的url地址 -->
        <property name="prefix" value="/" />
        <property name="suffix" value=".jsp" />
    </bean>
<!-- ----------------------以下部分可不写------------------ -->
    <!-- 返回json 导入fastjson-1.2.21.jar-->
    <mvc:annotation-driven>
        <mvc:message-converters>
            <bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter">
            <property name="supportedMediaTypes" value="application/json"/>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>

    <!-- 上传文件的配置 -->
     <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">  
        <property name="maxUploadSize" value="1073741824" />  
    </bean> 
</beans>
mybatis.xml配置文件这里写图片描述

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!-- 别名 -->
    <typeAliases>
        <package name="com.crazy.bean"/>
    </typeAliases>
</configuration>
jdbc.properties文件根据自己的数据库情况进行修改这里写图片描述

driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/ssm
username=root
password=123456

initialSize=0
#\u5B9A\u4E49\u6700\u5927\u8FDE\u63A5\u6570  
maxActive=20  
#\u5B9A\u4E49\u6700\u5927\u7A7A\u95F2  
maxIdle=20  
#\u5B9A\u4E49\u6700\u5C0F\u7A7A\u95F2  
minIdle=1  
#\u5B9A\u4E49\u6700\u957F\u7B49\u5F85\u65F6\u95F4  
maxWait=60000
配置applicationContext.xml文件这里写图片描述

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
 xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
 xsi:schemaLocation="
 http://www.springframework.org/schema/beans
 http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
 http://www.springframework.org/schema/context
 http://www.springframework.org/schema/context/spring-context-4.0.xsd
 http://www.springframework.org/schema/tx
 http://www.springframework.org/schema/tx/spring-tx-4.0.xsd">

    <!-- 自动扫描（自动注入） -->
    <context:component-scan base-package="com.crazy.service" />

    <!-- 引入资源文件 -->
    <bean id="propertyConfigurer"
 class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="location" value="classpath:config/jdbc.properties" />
    </bean>

    <!-- 配置数据源 -->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
 destroy-method="close">
        <property name="driverClassName" value="${driverClassName}" />
        <property name="url" value="${url}" />
        <property name="username" value="${username}" />
        <property name="password" value="${password}" />
        <!-- 初始化连接大小 -->
        <property name="initialSize" value="${initialSize}"></property>
        <!-- 连接池最大数量 -->
        <property name="maxActive" value="${maxActive}"></property>
        <!-- 连接池最大空闲 -->
        <property name="maxIdle" value="${maxIdle}"></property>
        <!-- 连接池最小空闲 -->
        <property name="minIdle" value="${minIdle}"></property>
        <!-- 获取连接最大等待时间 -->
        <property name="maxWait" value="${maxWait}"></property>
    </bean>

    <!-- 加载mybatis文件 -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"></property>
        <!-- mybatis配置文件 -->
        <property name="configLocation" value="classpath:config/mybatis.xml"></property>

        <!-- 自动扫描com.crazy.mapping里的配置文件 -->
        <property name="mapperLocations" value="classpath:com/crazy/mapping/*.xml"></property>
    </bean>

    <!--
 mybatis自动扫描加载Sql映射文件/接口 : MapperScannerConfigurer sqlSessionFactory
 basePackage:指定sql映射文件/接口所在的包（自动扫描）
 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.crazy.dao"></property>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
    </bean>

    <!-- 配置事务管理器 -->
    <bean id="transactionManager"
 class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <!-- 使用声明式事务 transaction-manager：引用上面定义的事务管理器 -->
    <tx:annotation-driven transaction-manager="transactionManager"/>

</beans>
日志文件log4j.properties这里写图片描述

log4j.rootLogger=INFO,stdout,info,warn,error

#\u63a7\u5236\u53f0\u8f93\u51fa
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.Threshold=INFO
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=[%p][%d{yyyy-MM-dd HH:mm:ss}] %l %m %n

#INFO\u65e5\u5fd7
log4j.logger.info=info
log4j.appender.info=org.apache.log4j.DailyRollingFileAppender
log4j.appender.info.File = D://logs//ssm//info.log
log4j.appender.info.datePattern='.'yyyy-MM-dd
log4j.appender.info.append=true
log4j.appender.info.Threshold=INFO
log4j.appender.info.layout=org.apache.log4j.PatternLayout
log4j.appender.info.layout.ConversionPattern=[%p][%d{yyyy-MM-dd HH:mm:ss}] %l %m %n

#WARN\u65e5\u5fd7
log4j.appender.warn=org.apache.log4j.DailyRollingFileAppender
log4j.appender.warn.File = D://logs//ssm//warn.log
log4j.appender.warn.datePattern='.'yyyy-MM-dd
log4j.appender.warn.append=true
log4j.appender.warn.Threshold=WARN
log4j.appender.warn.layout=org.apache.log4j.PatternLayout
log4j.appender.warn.layout.ConversionPattern=[%p][%d{yyyy-MM-dd HH:mm:ss}] %l %m %n

#ERROR\u65e5\u5fd7
log4j.appender.error=org.apache.log4j.DailyRollingFileAppender
log4j.appender.error.File = D://logs//ssm//error.log
log4j.appender.error.datePattern='.'yyyy-MM-dd
log4j.appender.error.append=true
log4j.appender.error.Threshold=ERROR
log4j.appender.error.layout=org.apache.log4j.PatternLayout
log4j.appender.error.layout.ConversionPattern=[%p][%d{yyyy-MM-dd HH:mm:ss}] %l %m %n

#\u6267\u884c\u6162\u7684SQL
log4j.logger.com.alibaba.druid.filter.stat.StatFilter=ERROR,slowsql
log4j.appender.slowsql=org.apache.log4j.DailyRollingFileAppender
log4j.appender.slowsql.File = D://logs//ssm//slow_sql.log
log4j.appender.slowsql.datePattern='.'yyyy-MM-dd
log4j.appender.slowsql.append=true
log4j.appender.slowsql.layout=org.apache.log4j.PatternLayout
log4j.appender.slowsql.layout.ConversionPattern=[%d{yyyy-MM-dd HH:mm:ss}] %m %n

#\u63a7\u5236\u53f0\u8f93\u51fa\u6240\u6709SQL
log4j.logger.com.crazy.dao=DEBUG,sql
log4j.appender.sql=org.apache.log4j.ConsoleAppender
log4j.appender.sql.Target=System.out
log4j.appender.sql.layout=org.apache.log4j.PatternLayout
log4j.appender.sql.layout.ConversionPattern=%m %n
接下来就是配置web.xml文件了

<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" id="WebApp_ID" version="3.1">
  <display-name>SSM</display-name>
  <welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
  </welcome-file-list>

  <context-param>
    <description>加载日志文件</description>
    <param-name>log4jConfigLocation</param-name>
    <param-value>classpath:config/log4j.properties</param-value>
  </context-param>
  <listener>
    <listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
  </listener>

  <context-param>
    <description>设置Spring容器加载所有的配置文件的路径</description>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:config/applicationContext.xml</param-value>
  </context-param>

  <filter>
    <description>字符编码过滤器</description>
    <filter-name>encodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>UTF-8</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>encodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

  <listener>
    <description>spring监听器</description>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

  <listener>
    <description>防止spring内存溢出监听器</description>
    <listener-class>org.springframework.web.util.IntrospectorCleanupListener</listener-class>
  </listener>

  <servlet>
    <description>配置SpringMVC核心控制器</description>
    <servlet-name>springMvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:config/springmvc.xml</param-value>
    </init-param>
    <!-- 启动加载一次 -->  
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>springMvc</servlet-name>
    <url-pattern>*.do</url-pattern>
  </servlet-mapping>

  <!-- session的保存时间 -->
  <session-config>
    <session-timeout>30</session-timeout>
  </session-config>
</web-app>
到这里已经完成60%了
接下来在com.crazy.mapping中写一个操作数据库的配置文件TUserMapper.xml 
我这可能有点长，可根据自己需求来写

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.crazy.dao.TUserMapper">
  <resultMap id="BaseResultMap" type="com.crazy.bean.TUser">
    <id column="uid" jdbcType="INTEGER" property="uid" />
    <result column="uname" jdbcType="VARCHAR" property="uname" />
    <result column="upwd" jdbcType="VARCHAR" property="upwd" />
    <result column="umessage" jdbcType="VARCHAR" property="umessage" />
    <result column="utime" jdbcType="TIMESTAMP" property="utime" />
  </resultMap>
  <sql id="Base_Column_List">
    uid, uname, upwd, umessage, utime
  </sql>
  <select id="selectByPrimaryKey" parameterType="java.lang.Integer" resultMap="BaseResultMap">
    select 
    <include refid="Base_Column_List" />
    from t_user
    where uid = #{uid,jdbcType=INTEGER}
  </select>
  <delete id="deleteByPrimaryKey" parameterType="java.lang.Integer">
    delete from t_user
    where uid = #{uid,jdbcType=INTEGER}
  </delete>
  <insert id="insert" parameterType="com.crazy.bean.TUser">
    insert into t_user (uid, uname, upwd, 
      umessage, utime)
    values (#{uid,jdbcType=INTEGER}, #{uname,jdbcType=VARCHAR}, #{upwd,jdbcType=VARCHAR}, 
      #{umessage,jdbcType=VARCHAR}, #{utime,jdbcType=TIMESTAMP})
  </insert>
  <insert id="insertSelective" parameterType="com.crazy.bean.TUser">
    insert into t_user
    <trim prefix="(" suffix=")" suffixOverrides=",">
      <if test="uid != null">
        uid,
      </if>
      <if test="uname != null">
        uname,
      </if>
      <if test="upwd != null">
        upwd,
      </if>
      <if test="umessage != null">
        umessage,
      </if>
      <if test="utime != null">
        utime,
      </if>
    </trim>
    <trim prefix="values (" suffix=")" suffixOverrides=",">
      <if test="uid != null">
        #{uid,jdbcType=INTEGER},
      </if>
      <if test="uname != null">
        #{uname,jdbcType=VARCHAR},
      </if>
      <if test="upwd != null">
        #{upwd,jdbcType=VARCHAR},
      </if>
      <if test="umessage != null">
        #{umessage,jdbcType=VARCHAR},
      </if>
      <if test="utime != null">
        #{utime,jdbcType=TIMESTAMP},
      </if>
    </trim>
  </insert>
  <update id="updateByPrimaryKeySelective" parameterType="com.crazy.bean.TUser">
    update t_user
    <set>
      <if test="uname != null">
        uname = #{uname,jdbcType=VARCHAR},
      </if>
      <if test="upwd != null">
        upwd = #{upwd,jdbcType=VARCHAR},
      </if>
      <if test="umessage != null">
        umessage = #{umessage,jdbcType=VARCHAR},
      </if>
      <if test="utime != null">
        utime = #{utime,jdbcType=TIMESTAMP},
      </if>
    </set>
    where uid = #{uid,jdbcType=INTEGER}
  </update>
  <update id="updateByPrimaryKey" parameterType="com.crazy.bean.TUser">
    update t_user
    set uname = #{uname,jdbcType=VARCHAR},
      upwd = #{upwd,jdbcType=VARCHAR},
      umessage = #{umessage,jdbcType=VARCHAR},
      utime = #{utime,jdbcType=TIMESTAMP}
    where uid = #{uid,jdbcType=INTEGER}
  </update>

    <!-- 分页查询 -->
    <select id="queryAll" parameterType="Map" resultMap="BaseResultMap">
        select <include refid="Base_Column_List" /> from t_user
        <where>
            <if test="uname != null">
                and uname = #{uname,jdbcType=VARCHAR}
            </if>
            <if test="umessage != null">
                and umessage = #{umessage,jdbcType=VARCHAR}
            </if>
        </where>
        order by utime desc
        <if test="start!=null and size!=null">
            limit #{start},#{size}
        </if>
    </select>

    <!-- 查询相册总个数 -->
    <select id="getTotal" parameterType="Map" resultType="Long">
        select count(*) from t_user 
        <where>
            <if test="uname != null">
                and uname = #{uname,jdbcType=VARCHAR}
            </if>
            <if test="umessage != null">
                and umessage = #{umessage,jdbcType=VARCHAR}
            </if>
        </where>
    </select>
</mapper>
在com.crazy.dao中写一个接口类TUserMapper.java 
方法名是TUserMapper.xml配置文件中的id

package com.crazy.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.crazy.bean.TUser;

@Repository
public interface TUserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    List<TUser> queryAll(Map<String, Object> map);

    Long getTotal(Map<String, Object> map);
}
在com.crazy.service中也写一个接口类TUserService.java，接口内容跟上面那个接口TUserMapper.java类的内容一样，这里就不重复写了

实现TUserService.java接口，在com.crazy.service.Impl中写一个TUserServiceImpl.java类，实现内容如下

package com.crazy.service.Impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.crazy.bean.TUser;
import com.crazy.dao.TUserMapper;
import com.crazy.service.TUserService;

@Service
public class TUserServiceImpl implements TUserService{

    @Resource
    private TUserMapper tuser;

    @Override
    public int deleteByPrimaryKey(Integer uid) {
        return tuser.deleteByPrimaryKey(uid);
    }

    @Override
    public int insert(TUser record) {
        return tuser.insert(record);
    }

    @Override
    public int insertSelective(TUser record) {
        return tuser.insertSelective(record);
    }

    @Override
    public TUser selectByPrimaryKey(Integer uid) {
        return tuser.selectByPrimaryKey(uid);
    }

    @Override
    public int updateByPrimaryKeySelective(TUser record) {
        return tuser.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TUser record) {
        return tuser.updateByPrimaryKey(record);
    }

    @Override
    public List<TUser> queryAll(Map<String, Object> map) {
        return tuser.queryAll(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return tuser.getTotal(map);
    }

}
好，到这里已经基本完成了，在com.crazy.controller写一个控制器

package com.crazy.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crazy.bean.TUser;
import com.crazy.service.TUserService;
import com.crazy.bean.PageBean;

@Controller
@RequestMapping("/user")
public class TUserController {

    @Resource
    private TUserService userService;

    /**
 * 添加用户
 * @param user
 * @param request
 * @return
 */
    @RequestMapping(value="/addUser")
    public String addUser(TUser user,HttpServletRequest request){
        if(user.getUname() == null){
            user.setUname("crazy");
            user.setUpwd("123");
            user.setUmessage("不详");
        }
        user.setUtime(new Date());
        int resultCount=userService.insert(user);
        if(resultCount > 0){
            request.setAttribute("addmess", "用户添加成功！");
        }else{
            request.setAttribute("addmess", "用户添加失败！");
        }
        return "index";
    }

    /**
 * 查询所有用户
 * @param user
 * @param page
 * @param rows
 * @param request
 * @return
 */
    @RequestMapping(value="/allUser")
    public String selectUser(TUser user,@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletRequest request){
        PageBean pageBean=null;
        if(page == null && rows == null){
            pageBean=new PageBean(1,10);
        }else{
            pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
        }
        Map<String, Object> map=new HashMap<>();
        map.put("uname", user.getUname());
        map.put("umessage", user.getUmessage());
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<TUser> list=userService.queryAll(map);
        Long total=userService.getTotal(map);
        request.setAttribute("list", list);
        request.setAttribute("total", total);
        return "index";
    }

    /**
 * 修改用户信息
 * @param user
 * @param request
 * @return
 */
    @RequestMapping(value="/upUser")
    public String updateUser(TUser user,HttpServletRequest request){
        if(user.getUid() != null){
            int resultcount=userService.updateByPrimaryKey(user);
            if(resultcount > 0){
                request.setAttribute("upmess", "修改成功！");
            }else{
                request.setAttribute("upmess", "修改失败！");
            }
        }
        return "index";
    }

    /**
 * 删除用户
 * @param user
 * @param request
 * @return
 */
    @RequestMapping(value="/delUser")
    public String deleteUser(Integer uid,HttpServletRequest request){
        if(uid != null){
            int resultcount=userService.deleteByPrimaryKey(uid);
            if(resultcount > 0){
                request.setAttribute("delmess", "删除成功！");
            }else{
                request.setAttribute("delmess", "删除失败！");
            }
        }
        return "index";
    }
}
最后再写一个index.jsp页面

<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<%String path=request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Crazy</title>
<style type="text/css">
 #ubody{
 width:80%;
 height:100%;
 margin:0px auto;
 border:red solid 1px;
 text-align:center;
 }
</style>
</head>
<body>
    <div id="ubody">
        <h1>Hello World!</h1>
        <form action="<%=path %>/user/addUser.do" method="post">
            <input type="text" name="uname" placeholder="请输入用户名" />
            <input type="password" name="uname" placeholder="请输入密码" />
            <input type="text" name="uname" placeholder="请输入用户信息" />
            <input type="submit" value="添加用户">
        </form>
        <a href="<%=path %>/user/allUser.do">用户列表</a>
        <p style="color:red">${addmess }  ${upmess }  ${delmess }</p>
        <c:forEach var="user" items="${list}" varStatus="status">
            <p>
                ${user.uname }&nbsp;&nbsp;&nbsp;
                ${user.umessage }&nbsp;&nbsp;&nbsp;
                <fmt:formatDate value='${user.utime }' type='both' pattern='yyyy-MM-dd HH:mm:ss' />&nbsp;&nbsp;&nbsp;
                <a href="<%=path %>/user/delUser.do?uid=${user.uid}">删除用户</a>
            </p>
        </c:forEach>
    </div>
</body>
</html>
大功告成
这里写图片描述
