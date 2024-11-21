package cscorner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ResetQuizServlet")
public class ResetQuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 重置 session 中的分数和当前索引
        session.setAttribute("score", 0);
        session.setAttribute("currentIndex", 0);

        // 可以选择重置问题列表
        session.setAttribute("questions", getDefaultQuestions());

        // 重定向到第一道题目
        response.sendRedirect("GameTrueFalse.jsp");
    }

    // 提供默认题目的方法
    private List<String> getDefaultQuestions() {
        List<String> questions = new ArrayList<>();
        questions.add("Scrum is a traditional waterfall project management method.");
        questions.add("In Scrum, the Product Owner is responsible for managing the Product Backlog and prioritizing its items.");
        questions.add("In Scrum, the team must determine all the work items they will complete during a Sprint at the beginning of the Sprint.");
        questions.add("The purpose of the Daily Standup in Scrum is to review and analyze the final project results.");
        questions.add("In Scrum, team members can add new work items to the current Sprint mid-way through the Sprint.");
        return questions;
    }
}
