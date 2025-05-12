package com.sist.web.sitter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import com.sist.web.sitter.vo.*;
import com.sist.web.sitter.service.*;
@Controller
public class SitterController {
	@GetMapping("/sitter/list")
	public String sitter()
	{
        return "sitter/list";
	}
	
	@GetMapping("/sitter/detail")
	public String sitter_detail()
	{
        return "sitter/detail";
	}
	
	@GetMapping("/sitter/insert")
    public String sitter_insert() 
	{
        return "sitter/insert";
    }

	@GetMapping("/sitter/update")
	public String sitter_update() 
	{
	    return "sitter/update";
	}
}
