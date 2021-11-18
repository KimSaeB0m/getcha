package com.hago.getcha.Member.service;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hago.getcha.Member.dao.IMemberDAO;
import com.hago.getcha.Member.dto.MemberDTO;

@Service
public class MemberService implements IMemberService{
	@Autowired IMemberDAO dao;
	@Autowired HttpSession session;
	final static Logger logger = LoggerFactory.getLogger(MemberService.class);
	
	@Override
	public String CheckEmail(String email) {
		int count = dao.CheckEmail(email);
		if(count == 0)
			return "사용 가능한 아이디";
		return "중복 아이디";
	}

	@Override
	public String memberProc(MemberDTO member) {
		String birth=member.getBirth1()+"년" + member.getBirth2()+"월"+member.getBirth3()+"일";
		member.setBirth(birth);
		logger.warn("Birth : " + member.getBirth());
		if(member.getEmail() == "" || member.getPw() == "")
			return "필수 정보입니다.";
		if(dao.CheckEmail(member.getEmail()) > 0)
			return "중복 아이디입니다.";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String securePw = encoder.encode(member.getPw());
		member.setPw(securePw);
		if("m".equals(member.getGender()) || "w".equals(member.getGender()) || member.getEmail() != null)
			dao.insertMember(member);
		session.setAttribute("email", member.getEmail());
//		session.setAttribute("nickname", member.getNickname());
//		session.setAttribute("mobile", member.getMobile());
//		session.setAttribute("birth", member.getBirth());
//		session.setAttribute("gender", member.getGender());
		return "가입완료";
	}
	
	
	public MemberDTO pwCheck(MemberDTO check) {
		if(check.getPw().equals(check.getPwCheck())==false)
			return null;
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		MemberDTO login = dao.userPassword(check.getEmail());
		if(login == null || bpe.matches(check.getPw(), login.getPw()))
			return null;
		return login;
	}

	
	@Override
	public boolean deleteProc(MemberDTO check) {
		String sessionEmail = (String)session.getAttribute("email");
		check.setEmail(sessionEmail);
		MemberDTO login = pwCheck(check);
		if(login == null)
			return false;
		String modifyEmail = (String)session.getAttribute("modifyEmail");
		dao.deleteProc(modifyEmail);
		session.removeAttribute("modifyEmail");
		session.invalidate();
		return true;
	}
	

	@Override
	public MemberDTO memberViewProc(String email) {
		logger.warn(email);
		MemberDTO member = dao.memberViewProc(email);
		logger.warn("member:"+member.getEmail());
		logger.warn("member nickname:" +member.getNickname());
		if(member != null) {
			return member;
		}
		return null;
	}
	

	@Override
	public int memberModiProc(MemberDTO member) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(member.getEmail() == "")
			return 0;
		if(dao.memberModiProc(member) == 1)
			return 1;
		else
			return 2;
	}
}
