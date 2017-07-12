package com.sihuatech.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sihuatech.po.Relation;
import com.sihuatech.service.RelationService;

@Controller
@RequestMapping("/relation")
public class RelationController {

	@Autowired
	private RelationService relationService;

	// 所有菜清单
	@RequestMapping("/detail")
	@ResponseBody
	public Map<String, Object> detail() {
		HashMap<String, Object> maplist = new HashMap<String, Object>();
		HashMap<String, String> item;
		List<Relation> list = relationService.selectAll();
		for (Relation tmp : list) {
			item = new HashMap<String, String>();
			item.put("id", tmp.getId() + "");
			item.put("enable", tmp.getEnable() + "");
			item.put("menuId", tmp.getMenuId()+"");
			item.put("personId", tmp.getPersonId()+"");
			item.put("time", tmp.getTime()+"");
			item.put("priceEnd", tmp.getPriceEnd()+"");
			maplist.put(tmp.getId() + "", item);
		}
		return maplist;
	}

	// 新增菜单：Object的方式
/*	@RequestMapping(value = "/addRelation", method = { RequestMethod.GET })
	public void addMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		Relation relation = new Relation();
		if (request.getParameter("menuId") != null) {
			relation.setMenuId(Integer.parseInt(request.getParameter("menuId")));
		}
		if (request.getParameter("personId") != null) {
			relation.setPersonId(Integer.parseInt(request.getParameter("personId")));
		}
		if (request.getParameter("time") != null) {
			relation.setTime(new Date(request.getParameter("time")));//TODO
		}
		if (request.getParameter("priceEnd") != null) {
			relation.setPriceEnd(Double.parseDouble(request.getParameter("priceEnd")));
		}
		relationService.addRelation(relation);
	}*/
	
	//TODO 新增菜单
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public int add(@ModelAttribute("relation") Relation relation) throws Exception {
		return relationService.addRelation(relation);
	}

	// 根据菜id查询菜信息 : restFul方式
	@RequestMapping(value = "/query/{relationId}", method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, String> query(@PathVariable("relationId") int relationId) {
		Relation relation = relationService.selectById(relationId);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("menuId", relation.getMenuId()+"");
		map.put("id", relation.getId() + "");
		map.put("personId", relation.getPersonId() + "");
		map.put("time", relation.getTime() + "");//TODO
		map.put("priceEnd", relation.getPriceEnd() + "");
		return map;
	}

	// TODO 测试 修改菜单：包含逻辑删除
	@RequestMapping(value = "/updateById", method = RequestMethod.POST)
	public int updateById(@ModelAttribute("relation") Relation relation) {
		return relationService.updateById(relation);
	}
}
