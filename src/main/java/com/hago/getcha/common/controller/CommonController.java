package com.hago.getcha.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hago.getcha.common.service.CommonService;

@Controller
public class CommonController {
	
	final static Logger logger = LoggerFactory.getLogger(CommonController.class);
	@Autowired CommonService service;
	
	@RequestMapping(value = "restViewProc")
	public String restViewProc(@RequestParam String restNum, Model model, HttpServletRequest req, 
			@RequestParam(value="currentPage", required=false, defaultValue="1")int currentPage) {
		if(restNum == null || restNum =="")
			return "forward:index?formpath=main";
		service.restViewProc(restNum, currentPage, model, req);	
		return "forward:index?formpath=restView";
	}
		
	
	@RequestMapping(value = "restTypeListProc")
	public String restTypeListProc(@RequestParam String mode, @RequestParam String type, Model model) {
		service.restTypeListProc(mode, type, model);
		return "forward:index?formpath=restList";
	}
	
	@RequestMapping(value = "restPriceListProc")
	public String restPriceListProc(@RequestParam String arrange, Model model) {
		service.restPriceListProc(arrange, model);
		return "forward:index?formpath=restList";
	}
	
	@RequestMapping(value = "searchProc")
	public String searchProc(Model model, HttpServletRequest req) {
		service.searchProc(model, req);
		return "forward:index?formpath=restList";
	}

	@RequestMapping(value = "guideBookShowListProc")
	public String restPriceListProc(Model model) {
		service.guideBookShowListProc(model);
		return "forward:index?formpath=restList";
	}
	
	@RequestMapping(value = "popularListProc")
	public String popularListProc(Model model) {
		service.popularListProc(model);
		return "forward:index?formpath=restList";
	}

	
	
}
	

