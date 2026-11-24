package com.studentapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Student> students = new ArrayList<>();
        String search = req.getParameter("q");
        String sql = "SELECT student_id, student_name, student_addr, student_age, student_qual, student_percent, student_year_passed FROM students";
        boolean filtering = search != null && !search.trim().isEmpty();
        if (filtering) sql += " WHERE student_name LIKE ? OR student_qual LIKE ?";
        sql += " ORDER BY student_id DESC";
        try (Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            if (filtering) { String term="%"+search.trim()+"%"; ps.setString(1,term); ps.setString(2,term); }
            try (ResultSet rs=ps.executeQuery()) {
                while(rs.next()) students.add(new Student(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getString(5),rs.getString(6),rs.getString(7)));
            }
        } catch(SQLException e) { getServletContext().log("Unable to load students",e); req.setAttribute("error","Could not connect to the database. Check DB settings and logs."); }
        req.setAttribute("students",students); req.setAttribute("query",search==null?"":search);
        req.getRequestDispatcher("/index.jsp").forward(req,resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action=req.getParameter("action");
        try {
            if ("delete".equals(action)) {
                int id=Integer.parseInt(req.getParameter("id"));
                try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM students WHERE student_id=?")) {
                    ps.setInt(1,id); ps.executeUpdate();
                }
            } else {
                String name=req.getParameter("name"), address=req.getParameter("address"), qualification=req.getParameter("qualification");
                String percentage=req.getParameter("percentage"), year=req.getParameter("yearPassed");
                int age=Integer.parseInt(req.getParameter("age"));
                if(name==null||name.trim().isEmpty()||address==null||address.trim().isEmpty()||age<1||age>120)
                    throw new IllegalArgumentException("Please provide valid student details.");
                try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(
                    "INSERT INTO students(student_name,student_addr,student_age,student_qual,student_percent,student_year_passed) VALUES(?,?,?,?,?,?)")) {
                    ps.setString(1,name.trim()); ps.setString(2,address.trim()); ps.setInt(3,age);
                    ps.setString(4,qualification); ps.setString(5,percentage); ps.setString(6,year); ps.executeUpdate();
                }
            }
            resp.sendRedirect(req.getContextPath()+"/students?success=1");
        } catch(IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
        } catch(SQLException e) {
            getServletContext().log("Unable to save student",e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Database operation failed. Check server logs.");
        }
    }
}
