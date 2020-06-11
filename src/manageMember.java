
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/manage")
public class manageMember extends HttpServlet {

	@SuppressWarnings("deprecation")
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		System.out.println("doGet");

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print("<html><head><title>manageMember</title></head>");
		out.print("<body><h3> 멤버 관리 시스템!!<h3><body>");

		String op = req.getParameter("operator");

		String id = req.getParameter("id");
		String password = req.getParameter("pwd");
		String name = req.getParameter("name");
		String tel = req.getParameter("tel");
		String email = req.getParameter("email");
		String depts[] = req.getParameterValues("dept");
		String gender = req.getParameter("gender");
		String birth = req.getParameter("birth");
		String intro = req.getParameter("introduction");

		System.out.println(op);

		int type = 0;

		String userId = "";
		String userPwd = "";
		String userName = "";
		String userTel = "";
		String userEmail = "";
		String userDept = "";
		String userGender = "";
		String userBirth = "";
		String userIntroduction = "";

		String jdbc_driver = "com.mysql.cj.jdbc.Driver";
		String jdbc_url = "jdbc:mysql://127.0.0.1:3306/databasetest?serverTimezone=UTC";

		try {
			Class.forName(jdbc_driver).newInstance();
			Connection con = DriverManager.getConnection(jdbc_url, "root", "1234");
			Statement st = con.createStatement();

			if (op.equals("전송")) {
				String dept = "";
				for (int i = 0; i < depts.length; i++) {
					dept = dept + depts[i];
				}
				ResultSet rv = st.executeQuery("SELECT * FROM databasetest.member");
				while (rv.next()) {
					if (id.equals(rv.getString("id")) && name.equals(rv.getString("name"))) {
						if (password.equals(rv.getString("password"))) {
							String b = " tel = '" + tel + "', " + "email = '" + email + "', " + "dept = '" + dept
									+ "', " + "gender = '" + gender + "', " + "birth = '" + birth + "', "
									+ "introduction = '" + intro + "'";
							System.out.println(b);
							String c = " WHERE (`id` = '" + id + "') and (`name` = '" + name + "')";
							PreparedStatement sx = con.prepareStatement("update databasetest.member set" + b + c);
							sx.executeUpdate();
							System.out.println("데이터 업데이트 성공!");
							type = 11;							

						} else {
							out.print("<br> 패스워드 오류");
							type = 21;
							break;
						}
					}
				}

				if (type > 10) {
					if (type < 20) {
						System.out.println("데이터 업데이트 성공!");
						out.print("<br> 데이터 업데이트 성공! <br>");
					} else {
						out.print("<br> 기존 데이터 <br>");
					}
				} else {
					String a = "( '" + id + "','" + password + "','" + name + "','" + tel + "','" + email + "','" + dept
							+ "','" + gender + "','" + birth + "','" + intro + "')";

					System.out.println(a);

					PreparedStatement sa = con.prepareStatement("insert into databasetest.member values" + a);
					sa.executeUpdate();
					sa.close();
				}

			}

			if (op.equals("전송") || op.equals("DB보기")) {

				if(op.equals("DB보기") || type >20) 
					out.print("<br> all data<h5> <br>");
				else
					out.print("<br> save complete!!<h5> <br>");
				
				String sql = "SELECT * FROM databasetest.member";
				ResultSet rs = st.executeQuery(sql);

				while (rs.next()) {
					userId = rs.getString("id");
					userPwd = rs.getString("password");
					userName = rs.getString("name");
					userTel = rs.getString("tel");
					userEmail = rs.getString("email");
					userDept = rs.getString("dept");
					userGender = rs.getString("gender");
					userBirth = rs.getString("birth");
					userIntroduction = rs.getString("introduction");
					System.out.printf("data: %s %s %s %s %s %s %s %s\n", userId, userName, userTel, userEmail, userDept,
							userGender, userBirth, userIntroduction);
					out.print("<br>" + "ID : " + userId + " 이름 : " + userName + " 전화번호 : " + userTel + " 이메일 : "
							+ userEmail + " 학과 : " + userDept + " 성별 : " + userGender + " 탄생계절 : " + userBirth
							+ " 자기소개 : " + userIntroduction + "<br>");

				}
			}

			if (op.equals("DB전체삭제")) {
				String c = "delete from databasetest.member";
				PreparedStatement sb = con.prepareStatement(c);
				sb.executeUpdate();
				sb.close();
				out.print("<br>모든 정보 삭제!");

			}

			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.print("</html>");
		out.close();

	}

}
