package com.hago.getcha.restManagement.service;

import org.springframework.web.multipart.MultipartHttpServletRequest;


public interface IRestRegisterService {
//	String FILE_LOCATION_PROMOTION = "C:\\Java_folder\\spring_workspace\\getcha\\src\\main\\webapp\\resources\\img\\promotion";
//	String FILE_LOCATION_RESTAURANT = "C:\\Java_folder\\spring_workspace\\getcha\\src\\main\\webapp\\resources\\img\\restaurant";
//	String FILE_LOCATION_MENU = "C:\\Java_folder\\spring_workspace\\getcha\\src\\main\\webapp\\resources\\img\\menu";
//	String FILE_LOCATION_WHOLEMENU = "C:\\Java_folder\\spring_workspace\\getcha\\src\\main\\webapp\\resources\\img\\wholeMenu";
	String FILE_LOCATION_PROMOTION = "/upload/promotion/";
	String FILE_LOCATION_RESTAURANT = "/upload/restaurant/";
	String FILE_LOCATION_MENU = "/upload/menu/";
	String FILE_LOCATION_WHOLEMENU = "/upload/wholeMenu/";
	
	public void restRegisterProc(String[] facilities, String[] openHour, MultipartHttpServletRequest req);
	public void menuRegisterProc(MultipartHttpServletRequest req);
}
