<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Sign Up</title>

    <!-- Font Icon -->
    <link rel="stylesheet" href="fonts/material-icon/css/material-design-iconic-font.min.css" />

    <!-- Main css -->
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>

<input type="hidden" id="status" value="<%= request.getAttribute("status") %>">
<input type="hidden" id="loginAttempts" value="<%= session.getAttribute("loginAttempts") != null ? session.getAttribute("loginAttempts") : 0 %>">

<div class="main">
    <!-- Sign in Form -->
    <section class="sign-in">
        <div class="container">
            <div class="signin-content">
                <div class="signin-image">
                    <figure>
                        <img src="images/signin-image.jpg" alt="Sign Up Image" />
                    </figure>
                    <a href="registration.jsp" class="signup-image-link">Create an account</a>
                </div>

                <div class="signin-form">
                    <h2 class="form-title">Sign In</h2>
                    <form method="post" action="login" class="register-form" id="login-form" onsubmit="return checkLogin()">
                        <div class="form-group">
                            <label for="username"><i class="zmdi zmdi-account material-icons-name"></i></label>
                            <input type="text" name="username" id="username" placeholder="Your Email" required />
                        </div>
                        <div class="form-group">
                            <label for="password"><i class="zmdi zmdi-lock"></i></label>
                            <input type="password" name="password" id="password" placeholder="Password" required />
                        </div>
                        <div class="form-group form-button">
                            <input type="submit" name="signin" id="signin" class="form-submit" value="Log In" />
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</div>

<!-- JS -->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="js/main.js"></script>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<script type="text/javascript">
    var status = document.getElementById("status").value;
    var loginAttempts = parseInt(document.getElementById("loginAttempts").value);
    const lockDuration = 5;  // 锁定时间5秒
    const flag = 0;

    if (status == "failed" && loginAttempts < 3 && flag == 0) {
        // 登录失败但未达到3次，显示失败提示
        swal("Sorry", `Wrong Email or Password. Attempt ${loginAttempts} of 3`, "error");
    } else if (status == "locked" && loginAttempts >= 3 && flag == 0) {
        // 登录失败达到3次，显示锁定提示框和倒计时
        swal({
            title: "Account Locked!",
            text: `Too many failed attempts. Please wait ${lockDuration} seconds...`,
            icon: "warning",
            button: false,
            closeOnClickOutside: false
        });

        document.getElementById("signin").disabled = true;  // 锁定登录按钮
        let timeLeft = lockDuration;

        // 启动倒计时
        
        const interval = setInterval(() => {
            swal({
                title: "Account Locked!",
                text: "Please wait " + timeLeft + " seconds...",
                icon: "warning",
                button: false,
                closeOnClickOutside: false
            });

            timeLeft--;

            if (timeLeft <= 0) {  
                clearInterval(interval);  // 停止倒计时
                swal.close();  // 关闭弹窗
            }
        }, 1000);
        
    }

</script>

</body>
</html>
