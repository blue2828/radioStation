$.ajaxSetup({
    cache: false,
    contentType: "application/x-www-form-urlencoded;charset=utf-8",
    complete: function (XHR, textStatus) {
        var resText = XHR.responseText;
        if (resText == 'ajaxSessionTimeOut') {
            sessionTimeOut();
        }
    }
});

function sessionTimeOut() {
    layer.msg("登录会话超时，请重新登录后操作", {icon: 5});
    setTimeout('window.top.location.href = "login.html"', 2000);
}
