package com.hago.getcha.Reservation.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.hago.getcha.Member.dao.IMemberDAO;
import com.hago.getcha.Member.dto.MemberDTO;
import com.hago.getcha.Reservation.dao.IReservationDAO;
import com.hago.getcha.Reservation.dto.ReservationDTO;

@Service
public class ReservationService{
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
	
	public List<Map<String, Object>> checkAjax(String date, int restNum) throws Exception {
		List<Map<String, Object>> checkres = checkres(restNum, date);
		checkres = checkres.stream().sorted((o1,o2) -> o1.get("time").toString().compareTo(o2.get("time").toString())
				).collect(Collectors.toList());
		for(Map<String, Object> map : checkres) {
			for(Entry<String, Object> entry:map.entrySet()) {
				String key = entry.getKey();
				String value = (String) entry.getValue();
				logger.warn("key: " + key + "| value: " + value);
			}
		}
			return checkres;
	}
	//????????? ????????? ????????????
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
		case 1: day="?????????"; break;
		case 2: day="?????????"; break;
		case 3: day="?????????"; break;
		case 4: day="?????????"; break;
		case 5: day="?????????"; break;
		case 6: day="?????????"; break;
		case 7: day="?????????"; break;
		
		}
		return day;
	}
	//db??? open?????? ??????
	public ArrayList<String> allList(int restNum){
		logger.warn("4.allList");
		ArrayList<ReservationDTO> hourList = hourList(restNum);
		ArrayList<String> weekList = new ArrayList<String>();
		for(int i=0; i<hourList.size(); i++) {
			ReservationDTO part = new ReservationDTO();
			part = hourList.get(i);
			weekList.add(part.getWeek());
			logger.warn("5.??????weeklist: " +weekList.get(i));
		}
		return weekList;
	}
	//db?????? ????????? weekList??? ????????? ??? ???????????? ????????? ?????? ?????? ??????
	public String checkDayLabel(List<String> weekList, String date) throws Exception {
		logger.warn("6.checkdaylabel");
		logger.warn("6-date:"+date);
		String day = getDateDay(date);
		logger.warn("?day:"+day);
		for(int i=0; weekList.size()<i; i++) {
			String dayLabel = weekList.get(i);
			if(dayLabel.equals("??????")==true) {
				logger.warn("7.dayLabel:"+dayLabel);
				return day;
			}
			if(dayLabel.equals("??????")) {
				if(day.equals("?????????")||day.equals("?????????")||day.equals("?????????")||
				day.equals("?????????")||	day.equals("?????????")) {
					logger.warn("7.dayLabel:"+dayLabel);
					return day;
				}
			}
			if(dayLabel.equals("??????")) {
				if(day.equals("?????????")||day.equals("?????????")) {
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
	//????????? ????????? ???????????? ??????
	public ArrayList<String> Time(int restNum, String date) throws Exception {
		logger.warn("2.time");
		ReservationDTO part = new ReservationDTO();
		ArrayList<ReservationDTO> hourList = hourList(restNum);
		ArrayList<String>weekList = allList(restNum);
		ArrayList<String> timeList = new ArrayList<String>();
		String day = checkDayLabel(weekList, date);
		String allDay = "??????";
		String weekdays="??????";
		String weekend="??????";
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
				if(day.equals("?????????")||day.equals("?????????")||day.equals("?????????")||day.equals("?????????")||day.equals("?????????")) {
					part = hourList.get(i);
					timeList.add(part.getHours());
				}
			}
			if(weekList.get(i).equals(weekend)) {
				if(day.equals("?????????")||day.equals("?????????")) {
					part = hourList.get(i);
					timeList.add(part.getHours());
				}
			}
		}
		return timeList;
	}
	//???????????? ???????????? ?????? or ????????? ????????? ?????? ?????? ??????
	public ArrayList<String> partTime(int restNum, String date) throws Exception {
		logger.warn("1.partTime");
		ArrayList<String> timeList = Time(restNum, date);
		ArrayList<String> timePart = new ArrayList<String>();
		for(int j=0; j<timeList.size(); j++) {
			String time = timeList.get(j);
			if(time.equals("??????")) {
				timePart.add("??????");
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
	public List<Map<String, Object>> checkres(int restNum, String date) throws Exception {
		logger.warn("map ??????");
		Map<String, Object>timeCapa = new HashMap<String, Object>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		ReservationDTO get = new ReservationDTO();
		List<String>resTCheck = new ArrayList<String>();
		List<String>resPCheck = new ArrayList<String>();
		ArrayList<ReservationDTO> checkList = new ArrayList<ReservationDTO>();
		
		
		List<String> timePart = partTime(restNum, date);
		ReservationDTO info = getInfo(restNum);
		int capacity = info.getCapacity();
		ArrayList<ReservationDTO> resList = resList(restNum);
		
		if(resList.size()<1) {
			logger.warn("???????????? ??????");
			for(int i=0; i<timePart.size(); i++) {
				timeCapa = new HashMap<String, Object>();
				String capa=Integer.toString(capacity);
				timeCapa.put("time", timePart.get(i));
				timeCapa.put("capa", capa);
				dataList.add(timeCapa);
			}
			return dataList;
		}else {
			for(int i=0; i<resList.size(); i++) {
				logger.warn("??????????????????-????????? ?????? ??????");
				get = resList.get(i);
				String resDay = get.getResDay();
				String resTime = get.getHours();
				String resPeople = Integer.toString(get.getCapacity());
				if(date.equals(resDay)) {
					logger.warn("checklist??????:"+get.getHours());
					logger.warn("????????? ?????? ??????:"+resTime);
					resTCheck.add(resTime);
					resPCheck.add(resPeople);
					for(int j=0; j<timePart.size();j++)
						if(timePart.get(j).equals(resTime)) {
							logger.warn("equalresTime:" + resTime);
							logger.warn("remove:"+timePart.get(j));
							timePart.remove(resTime);
						}
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
		
		
		for(int i=0; i<timePart.size(); i++) {
			if(timePart.get(i).equals("??????")) {
				String cap = Integer.toString(0);
				timeCapa.put("time", timePart.get(i));
				timeCapa.put("capa", cap);
				dataList.add(timeCapa);
			}else {
				timeCapa = new HashMap<String, Object>();
				String capa=Integer.toString(capacity);
				logger.warn("check??????:"+timePart.get(i)+"/"+capa);
				timeCapa.put("time", timePart.get(i));
				timeCapa.put("capa", capa);
				dataList.add(timeCapa);
			}
		}
		
		logger.warn("=========================");
		//?????? ????????? ????????????
		for(int i=0; i<resList.size(); i++) {
			ReservationDTO dto = resList.get(i);
			if(date.equals(dto.getResDay())) {
				checkList.add(dto);
			}
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(int a=0; a<checkList.size(); a++) {
			logger.warn("checkList");
			ReservationDTO check = new ReservationDTO();
			check = checkList.get(a);
			logger.warn("??????:"+check.getHours());
			if(map.isEmpty()) {
				int capa = capacity-check.getCapacity();
				logger.warn("map ??????");
				logger.warn("map put" + "key" + check.getHours() + "value"+capa);
				map.put(check.getHours(), capa);
				checkList.remove(a);
			}
		}
		ArrayList<ReservationDTO>list = new ArrayList<ReservationDTO>();
		for(int m=0; m<checkList.size(); m++) {
			logger.warn("checkList2");
			ReservationDTO check = new ReservationDTO();
			check = checkList.get(m);
			logger.warn("??????2:"+check.getHours());
			Iterator<String>keys = map.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				if(key.equals(check.getHours())) {
					logger.warn("map:"+map.get(key) + " / " + check.getCapacity());;
					ReservationDTO dto = new ReservationDTO();
					dto.setHours(key);
					dto.setCapacity(check.getCapacity());
					logger.warn("??????????????????:"+dto.getHours());
					logger.warn("??????:"+dto.getCapacity());
					list.add(dto);
				}else {
					ReservationDTO dto = new ReservationDTO();
					String time = check.getHours();
					int cap = check.getCapacity();
					logger.warn("?????? ?????? ??????:"+time);
					logger.warn("??????:"+cap);
					dto.setHours(check.getHours());
					dto.setCapacity(check.getCapacity());
					logger.warn("??????:"+dto.getHours()+" / " + dto.getCapacity());
					list.add(dto);
				}
			}
		}
		String k =Integer.toString(list.size());
		logger.warn("k:"+k);
		
		logger.warn("=================================");
		for(int j=0; j<list.size(); j++) {
			ReservationDTO dto = new ReservationDTO();
			dto = list.get(j);
			String time = dto.getHours();
			logger.warn("list??????:"+ time + " / " + dto.getCapacity());
			if(map.get(time)!=null){
				int cap = map.get(time)-dto.getCapacity();
				logger.warn("cap:"+cap);
				map.put(time, cap);
			}else {
				int cap = capacity - dto.getCapacity();
				logger.warn("2.cap:"+cap);
				map.put(time, cap);
			}
		}
		
		logger.warn("===================================");
		for(String key : map.keySet()) {
			logger.warn("key:"+key+"/value:"+map.get(key));
			timeCapa = new HashMap<String, Object>();
			timeCapa.put("time", key);
			logger.warn("key"+key+"capa"+Integer.toString(map.get(key)));
			timeCapa.put("capa", Integer.toString(map.get(key)));
			dataList.add(timeCapa);
		}
		
		return dataList;
	}
	
	
	//????????? ??????, ??????, ?????? ????????????
	public int reservationProc(ReservationDTO dto, Model model) {
		logger.warn("?????? service");
		int restNum = dto.getRestNum();
		ReservationDTO info = getInfo(restNum);
		String restName = info.getRestName();
		logger.warn("????????????: "+ restName);
		dto.setOrderNum(00);
		dto.setRestName(restName);
		dto.setStatus("????????????");
		if(dto.getEmail()==""||dto.getEmail()==null) {
			model.addAttribute("msg","?????????????????????.");
			return 0;
		}
		if(dto.getHours().equals("??????")) {
			model.addAttribute("msg", "???????????? ???????????? ??? ????????????.");
			return 3;
		}
		if(dao.reservationProc(dto)==1) {
			model.addAttribute("msg","?????????????????????.");
			return 1;
		}
		else {
			model.addAttribute("msg", "?????? ??????");
			return 2;
		}
	}
	
	public int checkDate(int day){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());
		int day1 = Integer.parseInt(date.replaceAll("[^0-9]", ""));
		logger.warn("?????? ??????:"+day1);
		if(day < day1) {
			return 0;
		}else {
			return 1;
		}
	}
	
	public int reservationView(String email, Model model) {
		ArrayList<ReservationDTO> view = dao.reservationView(email);
		if(view==null || view.isEmpty()) {
			model.addAttribute("msg","??????????????? ????????????.");
			return 0;
		}else {
			model.addAttribute("reservationView", view);
			return 1;	
		}
	}
	
	public void resDelete(String resNum, Model model) {
		int no = Integer.parseInt(resNum);
		ReservationDTO res = dao.deleteView(no);
		model.addAttribute("res", res);
	}
	
	public int resDeleteProc(String resNum) {
		logger.warn("resNum:"+resNum);
		int no = Integer.parseInt(resNum);
		int result = dao.resDeleteProc(no);
		
		return result;
	}
	
	
}
