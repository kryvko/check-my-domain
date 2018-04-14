<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: artem
  Date: 13.04.18
  Time: 9:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ua.kh.kryvko.name.ServletContextNames" %>
<html>
<head>
    <title>Result</title>
    <style>
        .brokenLink {
            color: red;
        }

        .d0 {
            display: none;
        }
        .toggler {
            width: 15px;
            height: 15px;
            background-color: green;
            float: left;
            cursor: pointer;
            margin-right: 5px;
        }
        .open {
            background-color: red;
        }
    </style>
</head>
<body>
<c:if test="${applicationScope[ServletContextNames.BROKEN_LINKS] != null and applicationScope[ServletContextNames.UNIQUE_LINKS_COUNT] != null}">
    <div class="content">
        <div class="uniqueLinksCount">
            Count of unique links: ${applicationScope[ServletContextNames.UNIQUE_LINKS_COUNT]}
        </div>
        <div class="brokenLinks">
            <div class="brokenLinksCount">
                Broken links: ${applicationScope[ServletContextNames.BROKEN_LINKS].size()}
            </div>
            <div class="brokenLinks">
                <c:forEach items="${applicationScope[ServletContextNames.BROKEN_LINKS].keySet()}" var="brokenLink">
                    <div class="brokenLinkContainer">
                        <div class="toggler"></div><a class="brokenLink" href='${brokenLink}'>${brokenLink}</a>
                        <ul class="referenceList d0">
                            <c:forEach items="${applicationScope[ServletContextNames.BROKEN_LINKS].get(brokenLink)}"
                                       var="referenceLink">
                                <li><a href='${referenceLink}'>${referenceLink}</a></li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</c:if>
<script type="text/javascript">
    function onClickAction(event) {
        var brokenLinkContainer = event.target.closest('.brokenLinkContainer');
        brokenLinkContainer.querySelector('.referenceList').classList.toggle('d0');
        brokenLinkContainer.querySelector('.toggler').classList.toggle('open');
    }

    document.getElementsByClassName('brokenLinks')[0].addEventListener('click', onClickAction);
</script>
</body>
</html>
