package com.sihuatech.mapper;

import com.sihuatech.po.Menu;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MenuMapper {
	Menu selectById(@Param("id") Integer id);
	List<Menu> selectAll();
	int addMenu(Menu record);
	int updateById(Menu record);
	
	void addMenu_(@Param("cm") Map<String, String> map);//备用
}