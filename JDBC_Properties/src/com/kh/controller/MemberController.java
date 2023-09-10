package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.service.MemberService;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;

//Controller : View를 통해서 사용자가 요청한 기능에 대해서 처리하는 담당
//			  해당 메소드로 전달된 데이터 [가공처리 한 후] dao로 전달하며 호출
//			  dao로부터 반환받은 결과에따라서 성공인지 실패인지 판단 후 응답화면 결정(View 메소드 호출)
public class MemberController {

	/**
	 * 사용자의 추가요청을 처리해주는 메소드
	 * 
	 * @param userId ~ hobby : 사용자가 입력했던 정보들이 담겨있는 매개변수
	 */
	public void insertMember(String userId, String userPwd, String userName, String gender, String age, String email,
			String phone, String address, String hobby) {

		Member m = new Member(userId, userPwd, userName, gender, Integer.parseInt(age), email, phone, address, hobby);
		int result = new MemberService().insertMember(m);

		if (result > 0) { // 성공
			new MemberMenu().displaySuccess("성공적으로 회원 추가 되었습니다.");
		} else { // 실패
			new MemberMenu().displayFail("회원추가 실패했습니다.");
		}
	}

	/**
	 * 사용자의 회원 전체조회 요청을 처리해주는 메소드
	 */
	public void selectList() {

		ArrayList<Member> list = new MemberService().selectList();

		// 조회된 결과에 따라서 사용자가 보게될 응답화면 지정
		if (list.isEmpty()) { // list가 비어있을 경우
			new MemberMenu().displayNoData("전체 조회 결과가 없습니다.");
		} else { // 조회된 데이터가 있을 경우
			new MemberMenu().displayMemberList(list);
		}
	}

	/**
	 * 사용자의 아이디로 회원 검색 요청을 처리해주는 메소드
	 * 
	 * @param userId : 사용자가 입력한 검색하고자하는 회원 아이디값
	 */
	public void selectByUserId(String userId) {
		Member m = new MemberService().selectByUserId(userId);

		if (m == null) { // 검색 결과가 없을 경우 (조회된 데이터 없음)
			new MemberMenu().displayNoData(userId + "에 해당하는 검색 결과가 없습니다.");
		} else { // 검색 결과가 있을 경우 (조회된 데이터 한 행 있음)
			new MemberMenu().displayMember(m);
		}
	}

	/**
	 * 이름으로 키워드 검색 요청시 처리해주는 메소드
	 * 
	 * @param keyword : 사용자가 입력한 검색할 회원명(키워드)
	 */
	public void selectByUserName(String keyword) {
		ArrayList<Member> list = new MemberService().selectByuserName(keyword);

		if (list.isEmpty()) { // 텅빈 리스트일 경우 => 검색 결과 없음
			new MemberMenu().displayNoData(keyword + "에 해당하는 검색 결과가 없습니다.");
		} else { // 그게 아닐 경우 => 검색결과 있음
			new MemberMenu().displayMemberList(list);
		}
	}

	/**
	 * 정보 변경 요청 처리 메소드
	 * 
	 * @param userId  : 변경하고자하는 회원 아이디
	 * @param userPwd : 변경할 비밀번호
	 * @param email   : 변경할 이메일
	 * @param phone   : 변경할 전화번호
	 * @param adress  : 변경할 주소
	 */
	public void updateMember(String userId, String userPwd, String email, String phone, String adress) {

		Member m = new Member();
		m.setUserID(userId);
		m.setUserPwd(userPwd);
		m.setEmail(email);
		m.setPhone(phone);
		m.setAddress(adress);

		int result = new MemberService().updateMember(m);

		if (result > 0) { // 성공
			new MemberMenu().displaySuccess("성공적으로 회원 정보 변경 되었습니다.");
		} else { // 실패
			new MemberMenu().displayFail("회원 정보 변경에 실패했습니다.");
		}
	}

	/**
	 * 회원탈퇴 요청 처리해주는 메소드
	 * 
	 * @param userId : 사용자가 입력한 탈퇴시키고자하는 회원 아이디
	 */
	public void deleteMember(String userId) {
		int result = new MemberService().deleteMember(userId);

		if (result > 0) { // 성공
			new MemberMenu().displaySuccess("성공적으로 회원 탈퇴 시켰습니다.");
		} else { // 실패
			new MemberMenu().displayFail("회원 탈퇴에 실패하였습니다.");
		}
	}
}
