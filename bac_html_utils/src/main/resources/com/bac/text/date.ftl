<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>时间内建函数</title>

</head>
<body>

<h1>日期:${today?date}</h1>
<h1>时间:${today?time}</h1>
<h1>日期时间:${today?datetime}</h1>
<h1>格式化时间:${today?string('yyyy年MM月dd日')}</h1>

</body>
</html>