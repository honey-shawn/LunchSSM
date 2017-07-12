package com.sihuatech.service;

import java.util.List;
import java.util.Map;


import com.sihuatech.po.Menu;

public interface MenuService {
	public Menu selectById(Integer id);
	public List<Menu> selectAll();
	int addMenu(Menu record);
	int updateById(Menu record);
	
	public void addMenu_(Map<String, String> map);//备选
}
