<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>True or False</title>
  <style>
    body {
        background-color: #800080;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    .container {
        background-color: #dda0dd;
        width: 50%;
        padding: 20px;
        border-radius: 10px;
        text-align: center;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    }
    .question {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 20px;
    }
    .buttons {
        margin-bottom: 20px;
        display: flex;
        justify-content: center;
    }
    button {
        padding: 10px 20px;
        font-size: 16px;
        margin: 0 10px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    button.correct {
        background-color: #4caf50;
        color: white;
    }
    button.wrong {
        background-color: #f44336;
        color: white;
    }
    .score {
        font-size: 18px;
        font-weight: bold;
    }
    .reset-buttons {
        margin-top: 20px;
        display: flex;
        justify-content: space-between;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="reset-buttons">
      <!-- 重置按钮 -->
      <form method="post" action="ResetQuizServlet">
        <button type="submit" class="button">RESET</button>
      </form>

      <!-- 返回主页按钮 -->
      <form method="get" action="index.jsp">
        <button type="submit" class="button">BACK TO MAIN PAGE</button>
      </form>
    </div>

    <div class="question">
      <% 
        // 从 session 获取当前问题和索引
        List<String> questions = (List<String>) session.getAttribute("questions");
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");

        if (questions != null && currentIndex != null && currentIndex < questions.size()) {
            String currentQuestion = questions.get(currentIndex);
            out.println(currentQuestion);
        } else {
            // 如果所有问题都回答完，显示最终分数
            out.println("Quiz completed! Final score: " + session.getAttribute("score"));
        }
      %>
    </div>

    <div class="buttons">
      <%-- 正确按钮 --%>
      <form method="post" action="TrueOrFalseServlet">
        <input type="hidden" name="answer" value="true" />
        <button type="submit" class="correct">Correct</button>
      </form>

      <%-- 错误按钮 --%>
      <form method="post" action="TrueOrFalseServlet">
        <input type="hidden" name="answer" value="false" />
        <button type="submit" class="wrong">Wrong</button>
      </form>
    </div>

    <!-- 显示当前分数 -->
    <div class="score">Current score: <%= session.getAttribute("score") %></div>
  </div>
</
