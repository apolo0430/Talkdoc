package com.example.talkdoc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler
{
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/users?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "0430";

    public static void signUp(String name, String email, String password, String userType)
    {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName(DRIVER);

            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String query = "INSERT INTO login (name, email, password, user_type) VALUES (?, ?, ?, ?)";

            stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, userType);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("회원가입이 완료되었습니다.");
            }
            else {
                System.out.println("회원가입에 실패했습니다.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean authenticate(String email, String password)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // 데이터베이스 연결
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 쿼리 준비
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            // 쿼리 실행
            rs = stmt.executeQuery();

            // 결과 확인 및 인증 여부 반환
            return rs.next(); // 결과가 있으면 인증 성공, 없으면 실패
        }
        catch (SQLException e) {
            e.printStackTrace();
            // 데이터베이스 오류 처리
            return false; // 인증 실패
        }
        finally {
            // 연결 및 자원 해제
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
