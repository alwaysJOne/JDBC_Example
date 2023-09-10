package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;

public class MemberService {
	// 1) jdbc driver 등록
	// 2) Connection 객체생성
	
	public int insertMember(Member m) {
		
		Connection conn = JDBCTemplate.getConnection();
		int result = new MemberDao().insertMember(conn, m);
		
		if (result > 0) { // 성공 => commit
			JDBCTemplate.commit(conn);
		} else { // 실패 => rollback
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	public int deleteMember(String userId) {
		Connection conn = JDBCTemplate.getConnection();
		int result = new MemberDao().deleteMember(conn, userId);
	
		if (result > 0) { // 성공 => commit
			JDBCTemplate.commit(conn);
		} else { // 실패 => rollback
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		return result;
	}
	
	public ArrayList<Member> selectList(){
		
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Member> list = new MemberDao().selectList(conn);
		JDBCTemplate.close(conn);
		
		return list;
	}
	
	public Member selectByUserId(String userId) {
		
		Connection conn = JDBCTemplate.getConnection();
		Member m = new MemberDao().selectByUserId(conn, userId);
		JDBCTemplate.close(conn);
		
		return m;
	}
	
	public ArrayList<Member> selectByuserName(String keyword){

		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Member> list = new MemberDao().selectByuserName(conn, keyword);
		JDBCTemplate.close(conn);
		
		return list;
	}
	
	public int updateMember(Member m) {
		Connection conn = JDBCTemplate.getConnection();
		int result = new MemberDao().updateMember(conn, m);
		if (result > 0) { // 성공 => commit
			JDBCTemplate.commit(conn);
		} else { // 실패 => rollback
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		return result;
		
	}
}
