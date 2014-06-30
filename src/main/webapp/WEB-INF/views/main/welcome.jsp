<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="me.kafeitu.demo.activiti.util.PropertyFileUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp"%>

	<%@ include file="/common/include-base-styles.jsp" %>
    <%@ include file="/common/include-jquery-ui-theme.jsp" %>
    <link href="${ctx }/js/common/plugins/jui/extends/portlet/jquery.portlet.min.css?v=1.1.2" type="text/css" rel="stylesheet" />
    <link href="${ctx }/js/common/plugins/qtip/jquery.qtip.css?v=1.1.2" type="text/css" rel="stylesheet" />
    <%@ include file="/common/include-custom-styles.jsp" %>
    <style type="text/css">
    	.template {display:none;}
    	.version {margin-left: 0.5em; margin-right: 0.5em;}
    	.trace {margin-right: 0.5em;}
        .center {
            width: 1200px;
            margin-left:auto;
            margin-right:auto;
        }
    </style>

    <script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/extends/portlet/jquery.portlet.pack.js?v=1.1.2" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/qtip/jquery.qtip.pack.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/plugins/html/jquery.outerhtml.js" type="text/javascript"></script>
	<script src="${ctx }/js/module/activiti/workflow.js" type="text/javascript"></script>
    <script src="${ctx }/js/module/main/welcome-portlet.js" type="text/javascript"></script>
</head>
<body style="margin-top: 1em;">
	<div class="center">
        <div style="text-align: center;">
            <h3>欢迎访问Activiti Demo，专为优秀的BPMN2.0规范的轻量级工作流引擎Activiti服务</h3>
        </div>
        <div id='portlet-container'></div>
    </div>

    <!-- 隐藏 -->
    <div class="forms template">
        <ul>
            <li>
                <b>普通表单</b>：每个节点的表单内容都写死在JSP或者HTML中。
            </li>
            <li>
                <b>动态表单</b>：表单内容存放在流程定义文件中（包含在启动事件以及每个用户任务中）。
            </li>
            <li>
                <b>外置表单</b>：每个用户任务对应一个单独的<b>.form</b>文件，和流程定义文件同时部署（打包为zip/bar文件）。
            </li>
            <li>
                <b>综合流程</b>：可以查询到所有的流程（普通、动态、外置固定查询某些流程的表单，为了演示所以单独分开）；综合流程的目的
                在于可以启动用户上传或者设计后部署的流程定义。
            </li>
        </ul>
    </div>
    
    <div id="multiInstance" class="template">
        在填写<strong>会签参与人</strong>字段时使用用户的ID作为值，例如：<code>kafeitu,admin</code>表示两个用户，即两个任务实例被创建。
    </div>

    <div class="arch template">
        <ul>
            <li>
                Activiti版本：公共版本（${prop['activiti.version']}）
                <c:if test="${prop['activiti.version'] != prop['activiti.engine.version']}">&nbsp;引擎<strong>特定</strong>版本（${prop['activiti.engine.version']}）</c:if>
            </li>
            <li>Spring版本：${prop['spring.version']}</li>
            <li>Hibernate：${prop['hibernate.version']}</li>
            <li>使用<a href="http://maven.apache.org" target="_blank">Maven</a>管理依赖</li>
        </ul>
    </div>

    <div class="rest template">
        <p>在web.xml中映射了两个两个Servlet</p>
        <dl>
            <dt>ExplorerRestletServlet</dt>
            <dd>
                <p>针对Activiti Modeler的Rest接口，映射路径：/service/*</p>
            </dd>
            <dt>RestletServlet</dt>
            <dd>
                <p>官方提供的完整Rest接口</p>
                <p>访问路径：http://localhost/yourappname/<用户手册提供的Rest地址></p>
                <p>映射路径：/rest/*</p>
                <p><a href="http://www.kafeitu.me/activiti/2013/01/12/kft-activiti-demo-rest.html">如何使用Activiti Rest模块</a></p>
            </dd>
        </dl>
    </div>
</body>
</html>