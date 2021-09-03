<html>
    <body>
        <script src="../js/jQuery-3.5.1.js"></script>
        <link rel="stylesheet" href="../css/login.css">
        <script>
            function post() {
                $.ajax({
                    url: "${model["url_auth"]}",
                    type: "post", //send it through get method
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify({
                        "username": $("#user").val(),
                        "password": $("#password").val()
                    }),
                    success: function(res) {},
                    error: function(err) {
                        alert(err)
                    }
                })
            }
        </script>
        
        <div class="login">
            <h1>Login</h1>
            <input type="text" name="u" placeholder="Username" id="user" />
            <input type="password" name="p" placeholder="Password" id="password" />
            <button id="button" onclick="post()" class="btn btn-primary btn-block btn-large">Let me in.</button>
            <div class="container unauthenticated"></div>
        </div>
    </body>
</html>