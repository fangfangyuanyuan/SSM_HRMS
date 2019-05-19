<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <script src="D:\BaiduYunDownload\SSM_HRMS-master+(2)\src\main\webapp\static\jeDate-gh-pages\src\jedate.min.js"></script  >
    <link type="text/css" rel="stylesheet" href="D:\BaiduYunDownload\SSM_HRMS-master+(2)\src\main\webapp\static\jeDate-gh-pages\static\jeDate-gh-pages\skin\jedate.css">
</head>
<body>
<form method="post" action="/hrms/group/reportUpload" enctype="multipart/form-data">
    选择一个文件:
    <input type="file" name="report" />
    <br/><br/>
    <input type="submit" value="上传" />
</form>
</body>
</html>
