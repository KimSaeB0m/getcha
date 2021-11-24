package com.hago.getcha.Reservation.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hago.getcha.Member.dao.IMemberDAO;
import com.hago.getcha.Member.dto.MemberDTO;
import com.hago.getcha.Reservation.dao.IReservationDAO;
import com.hago.getcha.Reservation.dto.ReservationDTO;

@Service
public class ReservationService {
	@Autowired IReservationDAO dao;
	@Autowired IMemberDAO member;
	@Autowired HttpSession session;
	Logger logger = LoggerFactory.getLogger(ReservationService.class);
	
	public ArrayList<ReservationDTO> hourList(int restNum){
		logger.warn("3.hourList");
		ArrayList<ReservationDTO> hourList = dao.getTime(restNum);
		return hourList;
	}
	public ReservationDTO getInfo(int restNum) {
		ReservationDTO info = dao.getInfo(restNum);
		return info;
	}
	public ArrayList<ReservationDTO> resList(int restNum){
		ArrayList<ReservationDTO> resList = dao.getresList(restNum);
		return resList;
	}
	public MemberDTO memberInfo(String email) {
		MemberDTO memberInfo = member.memberViewProc(email);
		return memberInfo;
	}

	public List<Map<String, String>> checkAjax(String date, int restNum) throws Exception {
		List<Map<String, String>> checkres = checkres(restNum, date);
		
		for(Map<String, String> map : checkres) {
			for(Map.Entry<String, String>entry:map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				logger.warn("key: " + key + "| value: " + value);
			}
		}
			return checkres;
	}
	//선택한 날짜의 요일찾기
	public String getDateDay(String date) throws Exception {
		logger.warn("7.getDateDay");
		String day = "";
		logger.warn("7-date:"+date);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date nDate = dateFormat.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(nDate);
		int dayNum = cal.get(Calendar.DAY_OF_WEEK);
		switch(dayNum) {
		case 1: day="일요일"; break;
		case 2: day="월요일"; break;
		case 3: day="화요일"; break;
		case 4: day="수요일"; break;
		case 5: day="목요일"; break;
		case 6: day="금요일"; break;
		case 7: day="토요일"; break;
		
		}
		return day;
	}
	//db의 open요일 전달
	public ArrayList<String> allList(int restNum){
		logger.warn("4.allList");
		ArrayList<ReservationDTO> hourList = hourList(restNum);
		ArrayList<String> weekList = new ArrayList<String>();
		for(int i=0; i<hourList.size(); i++) {
			ReservationDTO part = new ReservationDTO();
			part = hourList.get(i);
			weekList.add(part.getWeek());
			logger.warn("5.식당weeklist: " +weekList.get(i));
		}
		return weekList;
	}
	//db에서 가져온 weekList와 선택된 날 비교해서 선택된 날의 요일 전달
	public String checkDayLabel(List<String> weekList, String date) throws Exception {
		logger.warn("6.checkdaylabel");
		logger.warn("6-date:"+date);
		String day = getDateDay(date);
		logger.warn("?day:"+day);
		for(int i=0; weekList.size()<i; i++) {
			String dayLabel = weekList.get(i);
			if(dayLabel.equals("매일")==true) {
				logger.warn("7.dayLabel:"+dayLabel);
				return day;
			}
			if(dayLabel.equals("주중")) {
				if(day.equals("월요일")||day.equals("화요일")||day.equals("수요일")||
				day.equals("목요일")||	day.equals("금요일")) {
					logger.warn("7.dayLabel:"+dayLabel);
					return day;
				}
			}
			if(dayLabel.equals("주말")) {
				if(day.equals("토요일")||day.equals("일요일")) {
					logger.warn("7.dayLabel:"+dayLabel);
					return day;
				}
			}
			if(day.equals(dayLabel) == true) {
				logger.warn("7.dayLabel:"+dayLabel);
				return day;
			}else {
				return null;
			}
		}
		return day;
	}
	//선택된 요일의 운영시간 전달
	public ArrayList<String> Time(int restNum, String date) throws Exception {
		logger.warn("2.time");
		ReservationDTO part = new ReservationDTO();
		ArrayList<ReservationDTO> hourList = hourList(restNum);
		ArrayList<String>weekList = allList(restNum);
		ArrayList<String> timeList = new ArrayList<String>();
		String day = checkDayLabel(weekList, date);
		String allDay = "매일";
		String weekdays="주중";
		String weekend="주말";
		for(int i=0; i<weekList.size(); i++) {
			if(weekList.get(i).equals(day)) {
				part = hourList.get(i);
				timeList.add(part.getHours());
			}
			if(weekList.get(i).equals(allDay)) {
				part = hourList.get(i);
				timeList.add(part.getHours());
			}
			if(weekList.get(i).equals(weekdays)) {
				if(day.equals("월요일")||day.equals("화요일")||day.equals("수요일")||day.equals("목요일")||day.equals("금요일")) {
					part = hourList.get(i);
					timeList.add(part.getHours());
				}
			}
			if(weekList.get(i).equals(weekend)) {
				if(day.equals("토요일")||day.equals("일요일")) {
					part = hourList.get(i);
					timeList.add(part.getHours());
				}
			}
		}
		return timeList;
	}
	//운영시간 가져와서 휴무 or 한시간 단위로 나눈 시간 전달
	public ArrayList<String> partTime(int restNum, String date) throws Exception {
		logger.warn("1.partTime");
		ArrayList<String> timeList = Time(restNum, date);
		ArrayList<String> timePart = new ArrayList<String>();
		for(int j=0; j<timeList.size(); j++) {
			String time = timeList.get(j);
			if(time.equals("휴무")) {
				timePart.add("휴무");
			}else {
				int startTime = Integer.parseInt(time.substring(0, 2));
				int endTime = Integer.parseInt(time.substring(6, 8));
				for(int i=startTime; i<=endTime; i++) {
					String getTime = i+":00";
					timePart.add(getTime);
					logger.warn("8.getTime:"+getTime);
				}
			}
		}
		return timePart;
	}
	//checkres
	public List<Map<String, String>> checkres(int restNum, String date) throws Exception {
		logger.warn("map 호출");
		Map<String, String>timeCapa = new HashMap<String, String>();
		List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
		ReservationDTO get = new ReservationDTO();
		List<String>resTCheck = new ArrayList<String>();
		List<String>resPCheck = new ArrayList<String>();
		ArrayList<ReservationDTO> checkList = new ArrayList<ReservationDTO>();
		ArrayList<ReservationDTO> inlist = new ArrayList<ReservationDTO>();
		
		List<String> timePart = partTime(restNum, date);
		ReservationDTO info = getInfo(restNum);
		int capacity = info.getCapacity();
		ArrayList<ReservationDTO> resList = resList(restNum);
		
		if(resList.size()<1) {
			logger.warn("예약내역 없음");
			for(int i=0; i<timePart.size(); i++) {
				timeCapa = new HashMap<String, String>();
				String capa=Integer.toString(capacity);
				timeCapa.put("time", timePart.get(i));
				timeCapa.put("capa", capa);
				dataList.add(timeCapa);
				return dataList;
			}
		}else {
			for(int i=0; i<resList.size(); i++) {
				logger.warn("예약내역있음-예약된 시간 조회");
				get = resList.get(i);
				String resDay = get.getResDay();
				String resTime = get.getHours();
				String resPeople = Integer.toString(get.getCapacity());
				if(date.equals(resDay)) {
					logger.warn("checklist추가:"+get.getHours());
					logger.warn("예약된 시간 조회:"+resTime);
					resTCheck.add(resTime);
					resPCheck.add(resPeople);
					for(int j=0; j<timePart.size();j++)
						if(timePart.get(j).equals(resTime)) {
							logger.warn("equalresTime:" + resTime);
							logger.warn("remove:"+timePart.get(j));
							timePart.remove(resTime);
						}
				}else {
					timeCapa = new HashMap<String, String>();
					String capa=Integer.toString(capacity);
					timeCapa.put("time", timePart.get(i));
					timeCapa.put("capa", capa);
					dataList.add(timeCapa);
					return dataList;
				}
			}
		}
		logger.warn("=========================");
		for(int i=0; resTCheck.size()>i; i++) {
			logger.warn("resTCheck:"+ resTCheck.get(i));
		}
		logger.warn("=========================");
		for(int i=0; resPCheck.size()>i; i++) {
			logger.warn("resPCheck:"+ resPCheck.get(i));
		}
		logger.warn("=========================");
		for(int i=0; timePart.size()>i; i++) {
			logger.warn("timaPart:"+ timePart.get(i));
		}
		
		
		logger.warn("=========================");
		//예약 시간의 예약인원
		
		
		for(int i=0; i<inlist.size(); i++) {
			ReservationDTO in = new ReservationDTO();
			in = inlist.get(i);
			logger.warn("inlist확인 : " + in.getHours()+" / " + in.getCapacity());
		}
		
		logger.warn("=========================");
		//예약가능 시간과 일치하는 예약시간 없을때
		for(int i=0; i<timePart.size(); i++) {
			timeCapa = new HashMap<String, String>();
			String stCapa = Integer.toString(capacity);
			logger.warn("capa:" +stCapa);
			logger.warn("hour:"+timePart.get(i));
			timeCapa.put("time", timePart.get(i));
			timeCapa.put("capa", stCapa);
			dataList.add(timeCapa);
		}
		
		return dataList;
	}
	
	public ArrayList<ReservationDTO> checkCapa(String date, int restNum) {
		ArrayList<ReservationDTO> checklist = new ArrayList<ReservationDTO>();
		ArrayList<ReservationDTO> inlist = new ArrayList<ReservationDTO>();
		
		ArrayList<ReservationDTO> resList = resList(restNum);
		ReservationDTO info = getInfo(restNum);
		int capacity = info.getCapacity();
		
		for(int i=0; i<resList.size(); i++) {
			ReservationDTO dto = resList.get(i);
			if(date.equals(dto.getResDay())) {
				checklist.add(dto);
			}
		}
		
		for(int i=0; i<checklist.size();) {
			logger.warn("확인");
			ReservationDTO check = new ReservationDTO();	//예약 리스트에서 날짜가 일치하는 것만
			check= checklist.get(i);
			logger.warn("시간:"+check.getHours());
			if(inlist.size()<1) {
				logger.warn("inlist 없을 때 추가");
				inlist.add(check);
				return inlist;
			}else {
				for(int j=0; j<inlist.size(); j++) {
					ReservationDTO indto = new ReservationDTO();
					indto=inlist.get(j);
					if(indto.getResNum()!=check.getResNum()) {
						logger.warn("in 예약번호:"+ indto.getResNum());
						logger.warn("check 예약번호:"+check.getResNum());
						if(indto.getHours().equals(check.getHours())) {
							inlist.remove(j);
							logger.warn("inlist 삭제");
							checklist.remove(i);
							logger.warn("indto와 check 비교/시간이 같을때");
							logger.warn("in 시간:"+ indto.getHours());
							logger.warn("check 시간:"+check.getHours());
							int cap = capacity-check.getCapacity();
							String time = check.getHours();
							logger.warn("inlist 입력:"+ time +" / "+ cap);
							indto.setCapacity(cap);
							indto.setHours(time);
							inlist.add(indto);
						}else {
							logger.warn("indto와 check 비교/시간이 다를때");
							if(indto.getResNum()!=check.getResNum()) {
								inlist.add(check);
								logger.warn("inlist입력");
								checklist.remove(i);
							}
						}
					}else {
						logger.warn("indto, check 예약번호같을때");
					}
				}
			}
		}
		return resList;
	}
	
	//선택된 날짜, 시간, 인원 예약하기
	public int reservationProc(ReservationDTO dto) {
		int restNum = dto.getRestNum();
		ReservationDTO info = getInfo(restNum);
		String restName = info.getRestName();
		logger.warn("식당이름: "+ restName);
		
		int random=(int)Math.random()*1000;
		String ran = Integer.toString(random);
		logger.warn("ran:"+ran);
		int Time = Integer.parseInt(dto.getHours().substring(0, 2));
		String ti = Integer.toString(Time);
		logger.warn("time:"+ti);
		String stday = dto.getResDay().replaceAll("[^0-9]", "");
		logger.warn(stday);
		int day = Integer.parseInt(stday);
		String resNum = stday + ti + ran;
		logger.warn("예약번호:"+resNum);
		//int num = Integer.parseInt(resNum);
		
		
		dto.setOrderNum(00);
		dto.setRestName(restName);
		dto.setResNum(2011011235);
		dto.setStatus("예약확인");
		if(dto.getEmail()==""||dto.getEmail()==null)
			return 0;
		if(dao.reservationProc(dto)==1)
			return 1;
		else
			return 2;
	}
}
