package cscorner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int LOCK_DURATION = 5; // 锁定时间（秒）
    private static final int MAX_ATTEMPTS = 3;  // 最大尝试次数

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uemail = request.getParameter("username");
        String upwd = request.getParameter("password");
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = null;

        // 获取失败尝试次数
        Integer loginAttempts = (Integer) session.getAttribute("loginAttempts");
        if (loginAttempts == null) {
            loginAttempts = 0;
        }

        // 获取上次锁定的时间
        Long lockEndTime = (Long) session.getAttribute("lockEndTime");
        long currentTime = System.currentTimeMillis();

        // 检查账户是否仍然处于锁定状态
        if (lockEndTime != null && currentTime < lockEndTime) {
            request.setAttribute("status", "locked");
            request.setAttribute("remainingLockTime", (lockEndTime - currentTime) / 1000);  // 传递剩余秒数
            dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Falcons?useSSL=false",
                    "root", "RootRoot##");
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE uemail = ? AND upwd = ?")) {

                pst.setString(1, uemail);
                pst.setString(2, upwd);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    // 登录成功，重置尝试计数和锁定时间
                    session.setAttribute("name", rs.getString("uname"));
                    session.setAttribute("loginAttempts", 0);  // 重置尝试次数
                    session.removeAttribute("lockEndTime");  // 移除锁定时间
                    response.sendRedirect("index.jsp");
                } else {
                    // 登录失败，增加尝试计数
                    loginAttempts++;
                    session.setAttribute("loginAttempts", loginAttempts);

                    // 若尝试次数达到上限，设置锁定时间
                    if (loginAttempts >= MAX_ATTEMPTS) {
                        long newLockEndTime = currentTime + LOCK_DURATION * 1000;
                        session.setAttribute("lockEndTime", newLockEndTime);
                        request.setAttribute("status", "locked");
                        request.setAttribute("remainingLockTime", LOCK_DURATION);  // 锁定时间传到前端
                    } else {
                        request.setAttribute("status", "failed");
                    }
                    dispatcher = request.getRequestDispatcher("login.jsp");
                    dispatcher.forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", "error");
            dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }
}


