package com.sihuatech.controller;

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

import com.sihuatech.po.Person;
import com.sihuatech.service.PersonService;

@Controller
@RequestMapping("/person")
public class PersonController {
	
	@Autowired
	private PersonService personService;

	// 所有菜清单
	@RequestMapping("/detail")
	@ResponseBody
	public Map<String, Object> detail() {
		HashMap<String, Object> maplist = new HashMap<String, Object>();
		HashMap<String, String> item;
		List<Person> list = personService.selectAll();
		for (Person tmp : list) {
			item = new HashMap<String, String>();
			item.put("id", tmp.getId() + "");
			item.put("name", tmp.getName());
			item.put("enable", tmp.getEnable() + "");
			maplist.put(tmp.getId() + "", item);
		}
		return maplist;
	}

	// 新增菜单：Object的方式
/*	@RequestMapping(value = "/addPerson", method = { RequestMethod.GET })
	public void addMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		Person person = new Person();
		if (request.getParameter("name") != null) {
			person.setName(new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8"));
		}
		personService.addPerson(person);
	}*/
	
	//TODO 新增菜单：Object的方式
		@RequestMapping(value = "/add", method = { RequestMethod.POST })
		public int add(@ModelAttribute("person") Person person) throws Exception {
			return personService.addPerson(person);
		}

	// 根据菜id查询菜信息 : restFul方式
	@RequestMapping(value = "/query/{personId}", method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, String> query(@PathVariable("personId") int personId) {
		Person person = personService.selectById(personId);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", person.getName());
		map.put("id", person.getId() + "");
		return map;
	}

	// TODO 测试 修改菜单：包含逻辑删除
	@RequestMapping(value = "/updateById", method = RequestMethod.POST)
	public int updateById(@ModelAttribute("person") Person person) {
		return personService.updateById(person);
	}
}
