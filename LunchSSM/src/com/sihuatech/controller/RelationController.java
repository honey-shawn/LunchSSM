package com.sihuatech.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sihuatech.po.Relation;
import com.sihuatech.po.RelationWrapper;
import com.sihuatech.service.RelationService;
import com.sihuatech.util.Tools;

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
		List<RelationWrapper> list = relationService.selectAllWrapper();
		for (RelationWrapper tmp : list) {
			item = new HashMap<String, String>();
			/*item.put("menuId", tmp.getRelation().getMenuId()+"");
			item.put("id", tmp.getRelation().getId() + "");
			item.put("enable", tmp.getRelation().getEnable() + "");
			item.put("personId", tmp.getRelation().getPersonId()+"");
			item.put("time", tmp.getRelation().getTime().getTime()+"");//化成时间戳
			item.put("priceEnd", tmp.getRelation().getPriceEnd()+"");*/
			item.put("menuId", tmp.getMenuId()+"");
			item.put("id", tmp.getId() + "");
			item.put("enable", tmp.getEnable() + "");
			item.put("personId", tmp.getPersonId()+"");
			item.put("time", tmp.getTime().getTime()+"");//化成时间戳
			item.put("priceEnd", tmp.getPriceEnd()+"");
			item.put("personName", tmp.getPersonName() + "");
			item.put("menuName", tmp.getMenuName() + "");
			maplist.put(tmp.getId() + "", item);
		}
		return maplist;
	}

	// 新增菜单：Object的方式
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public void addMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		String content = Tools.getBodyData(request);//获取实体
		System.out.println("--------------\n"+content);
		JSONArray jsonArray = JSON.parseArray(content);
		for(int i=0; i < jsonArray.size(); i++){
			Relation relation = new Relation();
			JSONObject tmp = (JSONObject)jsonArray.get(i);
			relation.setMenuId(Integer.parseInt(tmp.get("menuId").toString()));
			relation.setPersonId(Integer.parseInt(tmp.get("personId").toString()));
			relation.setPriceEnd(Double.parseDouble(tmp.get("priceEnd").toString()));
			Long time = Long.parseLong(tmp.get("date").toString());
			System.out.println(time);
			relation.setTime(new Date(time));
			relationService.addRelation(relation);
		}
		
	}
	
	
	
	/*
	 //TODO 新增点餐
	@RequestMapping(value = "/add1", method = { RequestMethod.POST })
	public void add(@ModelAttribute("data") List<Relation> relationList) throws Exception {
		System.out.println("客户端介入了。。。。。");
	}
	
	@RequestMapping(value = "/add2", method = { RequestMethod.POST })
	@ResponseBody      //这边是返回json格式数据
    public String createOrder(@RequestBody String requestBody) throws Exception {
		String str=new String((requestBody).getBytes("iso-8859-1"),"utf-8");

		System.out.println("-------------\n"+str);
		return str;
        //具体操作
    }*/
	
	// 根据菜id查询点餐信息 : restFul方式
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
