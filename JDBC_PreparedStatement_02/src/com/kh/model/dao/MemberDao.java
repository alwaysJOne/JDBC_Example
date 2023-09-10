package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;

//DAO(Data access object) : DB에 직접적으로 접근해서 사용자의 요청에 맞는 sql문 실행 후 결과 받기(JDBC)
//							결과를 Controller로 다시 리턴
public class MemberDao {

	/**
	 * 사용자가 입력한 정보들을 추가시켜주는 메소드
	 * 
	 * @param m : 사용자가 입력한 값들이 담겨있는 member 객체
	 * @return : insert문 실행 후 처리된 행 수
	 */
	public int insertMember(Member m) {
		// insert => 처리된 행 수 => 트랜잭션 처리
		int result = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;

		// 실행할 sql문 (미완성된 형태로 둘수있음)
		// INSERT INTO MEMBER
		// VALUES(SEQ_USERNO.NEXTVAL, 'ID값', 'PWD값', '이름값', '성별', 나이, '이메일', '전화번호',
		// '주소', '취미', SYSDATE);

		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";

		try {
			// 1) jdbc driver등록
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2) Connection객체 생성 == DB에 연결(url, 계정명, 비밀번호)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");

			// 3) PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql); // 애초에 PreparedStatement 객체 생성시 sql문을 담은채로 생성(하필 미완성 sql문)

			// > 빈 공간을 실제값으로 채워준 후 실행
			// pstmt.setString(홀더순번, 대체할값); => '대체할값' (양옆에 홀따움표 감싸준 상태로 말아서 들어감)
			// pstmt.setInt(홀더순번, 대체할값); => 대체할값
			pstmt.setString(1, m.getUserID());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());

			// 4, 5) sql문 실행 후 결과 받기(처리된 행수)
			result = pstmt.executeUpdate();

			// 6) 트랜잭션 처리
			if (result > 0) { // 성공 => commit
				conn.commit();
			} else { // 실패 => rollback
				conn.rollback();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 7) 다쓴 jdbc객체 반납
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;

	}

	/**
	 * 사용자가 요청한 회원 전체 조회를 처리해주는 메소드
	 * 
	 * @return : 조회된 결과가 있다면 조회된 결과들이 담긴 list | 조회된 결과가 없다면 빈 list
	 */
	public ArrayList<Member> selectList() {
		// select문(여러행 조회) => ResultSet객체 => ArrayList에 담기

		// 필요한 변수들 셋팅
		ArrayList<Member> list = new ArrayList<>(); // 비어있는 상태

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null; // select문 실행시 조회된 결과값들이 최초로 담기는 객체

		// 실행할 sql문
		String sql = "SELECT * FROM MEMBER";

		try {
			// 1) JDBC driver등록
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2)Connection 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");

			// 3)Statement 생성
			pstmt = conn.prepareStatement(sql); // 애초에 완성된 sql문 담았음==> 곧바로 실행

			// 4, 5) sql문 실행 및 결과 받기
			rset = pstmt.executeQuery(); 

			// 6) ResultSet으로부터 데이터 하나씩 뽑아서 vo 객체에 담고 + list에 vo객체에 추가
			while (rset.next()) {
				// 현재 rset의 커서가 가리키고 있는 한 행의 데이터들을 싹 다 뽑아서 Member객체 담기
				Member m = new Member();
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserID(rset.getString("USERID"));
				m.setUserPwd(rset.getString("userpwd"));
				m.setUserName(rset.getString("username"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrolldate(rset.getDate("enrolldate"));
				// 현재 참조하고 있는 행에 대한 모든 컬럼에 대한 데이터들을 한 Member객체에 담기 끝!

				list.add(m); // 리스트에 담기
			}

			// 반복문이 끝난 시점에 list
			// 조회된 데이터가 없다면 리스트는 비어있을 것이다.
			// 조회된 데이터가 있었다면 list에 뭐라도 담겨있을 것이다.
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 사용자의 아이디로 회원 검색 요청 처리해주는 메소드
	 * 
	 * @param userId : 사용자가 입력한 검색하고자하는 회원 아이디값
	 * @return : 검색된 결과가 있었으면 생성된 Member객체 | 검색된 결과가 없었으면 null
	 */
	public Member selectByUserId(String userId) {
		// select문(한행) => ResultSet객체 => Member객체

		Member m = null;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		// 실행할 sql문
		// select * from member where userid = 'xxx';
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";

		try {
			// 1)
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");

			// 3)
			pstmt = conn.prepareStatement(sql); //( 미완성된 sql문 )
			pstmt.setString(1, userId);
			
			// 4, 5)
			rset = pstmt.executeQuery();

			// 6)

			if (rset.next()) {
				// 조회됐다면 해당 조회된 컬럼 값들을 쭉쭉 뽑아서 한 Member객체의 각 필드에 담기
				m = new Member(rset.getInt("userno"), rset.getString("userid"), rset.getString("userpwd"),
						rset.getString("username"), rset.getString("gender"), rset.getInt("age"),
						rset.getString("email"), rset.getString("phone"), rset.getString("address"),
						rset.getString("hobby"), rset.getDate("enrolldate"));
			}

			// 위의 조건문 다 끝난 시점
			// 만약 조회된 데이터가 없었을 경우 => m은 null상태
			// 만약 조회된 데이터가 있었을 경우 => m은 생성된 객체

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return m;
	}

	/**
	 * 이름(keyword)으로 회원을 찾아서 결과값을 반환시켜주는 메소드
	 * 
	 * @param keyword : 사용자가 입력한 이름의 키워드
	 * @return : 이름(키워드)로 부터 검색된 고객의 리스트 | 빈리스트
	 */
	public ArrayList<Member> selectByuserName(String keyword) {
		// select문(여러행) => ResultSet객체 => ArrayList<Member> 객체
		ArrayList<Member> list = new ArrayList<>(); // 텅 빈 리스트

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%?%';
		// 위의 sql문 제시한 후 pstmt.setString(1, "강"); => '강'
		// 이때 완성될 sql문 : SELECT * FROM MEMBER WHERE USERNAME LIKE '%'강'%';
		
		// 해결방법 1) 
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%' || ? || '%'";

		// 해결방법 2) 
		//String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				// 현재 rset 참조하고 있는 한 행 데이터들 뽑아서 => 한 Member객체에 담기 => list에 추가
				list.add(new Member(rset.getInt("userno"), rset.getString("userid"), rset.getString("userpwd"),
						rset.getString("username"), rset.getString("gender"), rset.getInt("age"),
						rset.getString("email"), rset.getString("phone"), rset.getString("address"),
						rset.getString("hobby"), rset.getDate("enrolldate")));

			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list; // 텅빈 리스트 | 데이터가 담겨있는 리스트
	}

	/**
	 * 회원의 아이디를 통해 회원을 찾아 정보를 변경해주는 메소드
	 * 
	 * @param m : 사용자가 입력한 아이디와 변경할 정보가 담긴 Member객체
	 * @return : 처리된 행수
	 */
	public int updateMember(Member m) {
		// update문 => 처리된 행수(int) => 트랜잭션 처리
		int result = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;

		// 실행할 sql문 (미완성형태)
		/*
		 * update member set userpwd = ?, email = ?, phone = ?, address = ? where
		 * userid = ?;
		 */
		String sql = "UPDATE MEMBER SET USERPWD = ? , EMAIL = ?, PHONE = ?, ADDRESS= ? WHERE USERID = ?";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserID());
			
			
			result = pstmt.executeUpdate();

			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 사용자가 입력한 아이디값 전달 받아서 회원 탈퇴시켜주는 메소드
	 * 
	 * @param userId : 사용자가 입력한 id
	 * @return : 처리된 행수
	 */
	public int deleteMember(String userId) {
		// DELETE FROM MEMBER WHERE USERID = '사용자가 입력한 아이디값'
		// delete문 => 처리된 행수 => 트랜잭션 처리
		int result = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "DELETE FROM MEMBER WHERE USERID = ?";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			result = pstmt.executeUpdate();

			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
