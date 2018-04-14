<%--
  Created by IntelliJ IDEA.
  User: kryvko
  Date: 14.04.18
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ua.kh.kryvko.name.ServletContextNames" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
<div class="container">
    <div class="row justify-content-md-center align-items-center h-100">
        <div class="col-12 col-md-10 col-lg-8 col-xl-6">
            <div class="jumbotron">
                <h1 class="display-3">Something went wrong!</h1>
                <p class="lead">Service is not being accessible now</p>
                <hr class="my-4">
                <c:if test="${not empty applicationScope[ServletContextNames.LAST_DOMAIN]}">
                    <p>You can view the result for the domain <span class="font-weight-bold">${applicationScope[ServletContextNames.LAST_DOMAIN]}</span></p>
                    <p class="lead">
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/lastResult" role="button">Show last result</a>
                    </p>
                </c:if>
                <c:if test="${not applicationScope[ServletContextNames.IS_LOADING]}">
                    <c:redirect url="/lastResult"/>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>
