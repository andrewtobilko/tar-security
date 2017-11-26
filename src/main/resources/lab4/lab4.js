function setCookieByName(name, value, exp, pth, dmn, sec) {
    document.cookie = name + '=' + escape(value)
        + ((exp) ? '; expires=' + exp : '')
        + ((pth) ? '; path=' + pth : '')
        + ((dmn) ? '; domain=' + dmn : '')
        + ((sec) ? '; secure' : '');
    console.log(document.cookie);
}

function expiresIn(d, h, m) {
    var now = new Date(),
        nowMS = now.getTime(),
        newMS = ((d * 24 + h) * 60 + m) * 60 * 1000 + nowMS;
    now.setTime(newMS);
    return now.toGMTString();
}

function setCookie() {
    setCookieByName("userName", document.getElementById("user").value, expiresIn(0, 0, .1), "/", "", "");
}

function getCookie() {
    var element = document.getElementById("result");
    var cookie = document.cookie.split("=")[1];

    element.innerHTML = cookie ? cookie : "The expiration time of the cookie has run out!";
}


