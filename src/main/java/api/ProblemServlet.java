package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.Problem;
import dao.ProblemDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/problem")
public class ProblemServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        resp.setContentType("application/json;charset=utf8");
        ProblemDAO problemDAO = new ProblemDAO();

        // 尝试获取题目 id,如果能获取到,说明是获取题目详情,如果不能获取到,说明是获取题目列表
        String idString = req.getParameter("id");
        if (idString == null || "".equals(idString)) {
            // 没有查询到 id 字段,获取到题目列表
            List<Problem> problems = problemDAO.selectAll();
            String respString = objectMapper.writeValueAsString(problems);
            resp.getWriter().write(respString);
        } else {
            // 查询到了 id 字段,获取到题目详情
            Problem problem = problemDAO.selectOne(Integer.parseInt(idString));
            String respString = objectMapper.writeValueAsString(problem);
            resp.getWriter().write(respString);
        }

    }
}
