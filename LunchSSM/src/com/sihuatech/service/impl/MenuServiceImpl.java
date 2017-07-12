package com.sihuatech.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.sihuatech.mapper.MenuMapper;
import com.sihuatech.po.Menu;
import com.sihuatech.service.MenuService;

public class MenuServiceImpl implements MenuService{
	
	@Autowired
	private MenuMapper menuMapper;

	@Override
	public Menu selectById(Integer id) {
		return menuMapper.selectById(id);
	}

	@Override
	public List<Menu> selectAll() {
		return menuMapper.selectAll();
	}

	@Override
	public void addMenu_(Map<String, String> map) {
		menuMapper.addMenu_(map);
	}

	@Override
	public int addMenu(Menu record) {
		return menuMapper.addMenu(record);
	}

	@Override
	public int updateById(Menu record) {
		return menuMapper.updateById(record);
	}

	
}
