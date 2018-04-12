<%--
  Created by IntelliJ IDEA.
  User: kryvko
  Date: 11.04.18
  Time: 23:32
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<html>
<head>
    <title>Loading</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"
          integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <div class="row justify-content-md-center align-items-center h-100">
        <div class="col-12 col-md-10 col-lg-8 col-xl-6">
            <div class="jumbotron">
                <h1 class="display-3">I'm busy now!</h1>
                <p class="lead">Now the domain <span class="font-weight-bold">${applicationScope['domain']}</span> is being processed</p>
                <hr class="my-4">
                <c:if test="${not empty applicationScope['lastDomain']}">
                    <p>You can view the result for the domain ${applicationScope['lastDomain']}</p>
                    <p class="lead">
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/lastResult" role="button">Show last result</a>
                    </p>
                </c:if>
                <c:if test="${not applicationScope['isLoading']}">
                    <c:redirect url="/lastResult"/>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>
