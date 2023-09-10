package com.kh.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.model.vo.Member;

//DAO(Data access object) : DB에 직접적으로 접근해서 사용자의 요청에 맞는 sql문 실행 후 결과 받기(JDBC)
//							결과를 Controller로 다시 리턴
public class MemberDao {
	
	/*
	 * 기존방식: : DAO 클레스에 사용자가 요청할때마다 실행해야되는 sql문을 자바소스코드내에 명시적으로 작성 => 정적코딩방식
	 * 
	 *  > 문제점 : sql문을 수정해야될 경우 자바소스코드를 수정해야됨 => 수정된 내용을 반영시키고자 한다면 프로그램을 재 구동해야됨
	 *  
	 *  > 해결방식 : sql문들을 별도로 관리하는 외부 파일(.xml)로 만들어서 실시간으로 그 파일에 기록된 sql문을 읽어들여서 실행 => 동적코딩방식
	 * */
	private Properties prop = new Properties();
	
	public MemberDao() {
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 사용자가 입력한 정보들을 추가시켜주는 메소드
	 * 
	 * @param m : 사용자가 입력한 값들이 담겨있는 member 객체
	 * @return : insert문 실행 후 처리된 행 수
	 */
	public int insertMember(Connection conn, Member m) {
		// insert => 처리된 행 수 => 트랜잭션 처리
		int result = 0;

		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertMember");

		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserID());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
			//conn은 아직 반납하면 안됨!
		}

		return result;

	}

	/**
	 * 사용자가 요청한 회원 전체 조회를 처리해주는 메소드
	 * 
	 * @return : 조회된 결과가 있다면 조회된 결과들이 담긴 list | 조회된 결과가 없다면 빈 list
	 */
	public ArrayList<Member> selectList(Connection conn) {
		// select문(여러행 조회) => ResultSet객체 => ArrayList에 담기
		ArrayList<Member> list = new ArrayList<>(); // 비어있는 상태

		PreparedStatement pstmt = null;
		ResultSet rset = null; // select문 실행시 조회된 결과값들이 최초로 담기는 객체

		String sql = prop.getProperty("selectList");

		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				
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
		
				list.add(m); // 리스트에 담기
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return list;
	}

	/**
	 * 사용자의 아이디로 회원 검색 요청 처리해주는 메소드
	 * 
	 * @param userId : 사용자가 입력한 검색하고자하는 회원 아이디값
	 * @return : 검색된 결과가 있었으면 생성된 Member객체 | 검색된 결과가 없었으면 null
	 */
	public Member selectByUserId(Connection conn, String userId) {
		// select문(한행) => ResultSet객체 => Member객체

		Member m = null;

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("selectByUserId");

		try {
			pstmt = conn.prepareStatement(sql); // ( 미완성된 sql문 )
			pstmt.setString(1, userId);
			rset = pstmt.executeQuery();

			if (rset.next()) {
				// 조회됐다면 해당 조회된 컬럼 값들을 쭉쭉 뽑아서 한 Member객체의 각 필드에 담기
				m = new Member(rset.getInt("userno"), rset.getString("userid"), rset.getString("userpwd"),
						rset.getString("username"), rset.getString("gender"), rset.getInt("age"),
						rset.getString("email"), rset.getString("phone"), rset.getString("address"),
						rset.getString("hobby"), rset.getDate("enrolldate"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);	
		}

		return m;
	}

	/**
	 * 이름(keyword)으로 회원을 찾아서 결과값을 반환시켜주는 메소드
	 * 
	 * @param keyword : 사용자가 입력한 이름의 키워드
	 * @return : 이름(키워드)로 부터 검색된 고객의 리스트 | 빈리스트
	 */
	public ArrayList<Member> selectByuserName(Connection conn, String keyword) {
		// select문(여러행) => ResultSet객체 => ArrayList<Member> 객체
		ArrayList<Member> list = new ArrayList<>(); // 텅 빈 리스트
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("selectByuserName");

		try {
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

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list; 
	}

	/**
	 * 회원의 아이디를 통해 회원을 찾아 정보를 변경해주는 메소드
	 * 
	 * @param m : 사용자가 입력한 아이디와 변경할 정보가 담긴 Member객체
	 * @return : 처리된 행수
	 */
	public int updateMember(Connection conn, Member m) {
		// update문 => 처리된 행수(int) => 트랜잭션 처리
		int result = 0;

		
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserID());

			result = pstmt.executeUpdate();

		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	/**
	 * 사용자가 입력한 아이디값 전달 받아서 회원 탈퇴시켜주는 메소드
	 * 
	 * @param userId : 사용자가 입력한 id
	 * @return : 처리된 행수
	 */
	public int deleteMember(Connection conn, String userId) {
		int result = 0;

		PreparedStatement pstmt = null;

		String sql = prop.getProperty("deleteMember");

		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}
}
