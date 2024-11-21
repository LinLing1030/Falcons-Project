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

@WebServlet("/TrueOrFalseServlet")
public class TrueOrFalseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 获取当前分数
        Integer score = (Integer) session.getAttribute("score");
        if (score == null) {
            score = 0;
            session.setAttribute("score", score);
        }

        // 获取问题列表和当前问题索引
        List<String> questions = (List<String>) session.getAttribute("questions");
        if (questions == null) {
            questions = getDefaultQuestions();  // 如果没有设置问题列表，则加载默认问题
            session.setAttribute("questions", questions);
        }

        Integer currentIndex = (Integer) session.getAttribute("currentIndex");
        if (currentIndex == null) {
            currentIndex = 0;
            session.setAttribute("currentIndex", currentIndex);
        }

        // 如果问题列表有效，且当前索引合法
        if (questions != null && currentIndex < questions.size()) {
            String currentQuestion = questions.get(currentIndex);
            Boolean correctAnswer = getCorrectAnswer(currentQuestion);
            boolean userAnswer = Boolean.parseBoolean(request.getParameter("answer"));

            // 如果用户答案正确，增加分数
            if (userAnswer == correctAnswer) {
                score++;
            }

            // 更新 session 中的分数和当前索引
            session.setAttribute("score", score);
            session.setAttribute("currentIndex", currentIndex + 1);
        }

        // 重定向到游戏页面
        response.sendRedirect("GameTrueFalse.jsp");
    }

    // 根据问题返回正确答案
    private Boolean getCorrectAnswer(String question) {
        if ("Scrum is a traditional waterfall project management method.".equals(question)) {
            return false;
        } else if ("In Scrum, the Product Owner is responsible for managing the Product Backlog and prioritizing its items.".equals(question)) {
            return true;
        } else if ("In Scrum, the team must determine all the work items they will complete during a Sprint at the beginning of the Sprint.".equals(question)) {
            return false;
        } else if ("The purpose of the Daily Standup in Scrum is to review and analyze the final project results.".equals(question)) {
            return false;
        } else if ("In Scrum, team members can add new work items to the current Sprint mid-way through the Sprint.".equals(question)) {
            return false;
        }
        return false;
    }

    // 默认的题目列表
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
