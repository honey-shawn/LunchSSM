package com.sihuatech.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sihuatech.po.Menu;
import com.sihuatech.service.MenuService;

@Controller
// 为了对url进行分类管理 ，可以在这里定义根路径，最终访问url是根路径+子路径
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	// 所有菜清单
	@RequestMapping("/detail")
	@ResponseBody
	public Map<String, Object> detail() {
		HashMap<String, Object> maplist = new HashMap<String, Object>();
		HashMap<String, String> item;
		List<Menu> list = menuService.selectAll();
		for (Menu tmp : list) {
			item = new HashMap<String, String>();
			item.put("id", tmp.getId() + "");
			item.put("name", tmp.getName());
			item.put("shop", tmp.getShop() + "");
			item.put("price", tmp.getPrice() + "");
			item.put("enable", tmp.getEnable() + "");
			item.put("type", tmp.getType() + "");
			maplist.put(tmp.getId() + "", item);
		}
		return maplist;
	}

	// 新增菜单：Object的方式
/*	@RequestMapping(value = "/addMenu", method = { RequestMethod.GET })
	public void addMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		Menu menu = new Menu();
		if (request.getParameter("price") != null) {
			menu.setPrice(Double.parseDouble(request.getParameter("price")));
		}
		if (request.getParameter("shop") != null) {
			menu.setShop(Integer.parseInt(request.getParameter("shop")));
		}
		if (request.getParameter("name") != null) {
			menu.setName(new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8"));
		}
		if (request.getParameter("type") != null) {
			menu.setType(Integer.parseInt(request.getParameter("type")));
		}
		menuService.addMenu(menu);
	}*/
	
	//TODO 新增菜单
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public int add(@ModelAttribute Menu menu) throws Exception {
		return menuService.addMenu(menu);
	}

	// 根据菜id查询菜信息 : restFul方式
	@RequestMapping(value = "/query/{menuId}", method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, String> query(@PathVariable("menuId") int menuId) {
		Menu menu = menuService.selectById(menuId);
//		HashMap<String, Object> maplist = new HashMap<String, Object>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", menu.getName());
		map.put("shop", menu.getShop() + "");
		map.put("price", menu.getPrice() + "");
		map.put("id", menu.getId() + "");
//		maplist.put("1", map);
//		maplist.put("2", map);
		return map;
	}

	// TODO 测试 修改菜单：包含逻辑删除
	@RequestMapping(value = "/updateById", method = RequestMethod.POST)
	public int updateById(@ModelAttribute("menu") Menu menu) {
		return menuService.updateById(menu);
	}

	// 修改菜单：包含逻辑删除
	/*
	 * @RequestMapping(value = "/updateById", method = { RequestMethod.POST })
	 * public void updateById(HttpServletRequest request, HttpServletResponse
	 * response) throws Exception { request.setCharacterEncoding("UTF-8");
	 * Logger.getRootLogger().info("..............updateById..............");
	 * Menu menu = new Menu(); if (request.getParameter("price") != null) {
	 * menu.setPrice(Double.parseDouble(request.getParameter("price"))); } if
	 * (request.getParameter("shop") != null) {
	 * menu.setShop(Integer.parseInt(request.getParameter("shop"))); } if
	 * (request.getParameter("name") != null) { menu.setName(new
	 * String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8")); }
	 * if (request.getParameter("type") != null) {
	 * menu.setType(Integer.parseInt(request.getParameter("type"))); } if
	 * (request.getParameter("enable") != null) {
	 * menu.setEnable(Integer.parseInt(request.getParameter("enable"))); }
	 * menuService.updateById(menu); }
	 */

	
	
	
	
	// ------------------备用------------------------------
	// 新增菜单：Map的方式
	@RequestMapping(value = "/addMenu_", method = { RequestMethod.GET })
	public void addMenu_(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		Logger.getRootLogger().info("..............addMenu..............");
		HashMap<String, String> map = new HashMap<String, String>();
		if (request.getParameter("price") != null) {
			map.put("price", request.getParameter("price"));

		}
		if (request.getParameter("shop") != null) {
			map.put("shop", request.getParameter("shop"));
		}
		if (request.getParameter("name") != null) {
			map.put("name", new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8"));
			Logger.getRootLogger().info(
					"..............name：" + new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8"));
		}
		if (request.getParameter("type") != null) {
			map.put("type", request.getParameter("type"));
		}
		menuService.addMenu_(map);
		/*
		 * response.setCharacterEncoding("utf-8");
		 * response.setContentType("application/json;charset=utf-8");
		 * response.getWriter().write("success");
		 */
		// return null;
	}

}
