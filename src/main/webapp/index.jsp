<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<html>
<head>
    <title>Check Domain</title>
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
                    <form action="/checkResult" method="get">
                        <div class="form-group">
                            <label for="domain">Domain:</label>
                            <input class="form-control" id="domain" aria-describedby="domainHelp"
                                   placeholder="Enter domain" name="domain" required pattern="^([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,6}$">
                            <small id="domainHelp" class="form-text text-muted">domain.sth.com</small>
                        </div>
                        <div class="form-group">
                            <label for="startUrl">Email address</label>
                            <input class="form-control" id="startUrl" aria-describedby="startUrlHelp" name="startUrl"
                                   placeholder="Enter email" required pattern="^https?:\/\/([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,6}(\/.*)?$">
                            <small id="startUrlHelp" class="form-text text-muted">http://domain.sth.com/index.html
                            </small>
                        </div>
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </form>
                </div>
            </div>
    </div>
</div>
</body>
</html>