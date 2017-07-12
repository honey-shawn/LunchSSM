package com.sihuatech.mapper;

import com.sihuatech.po.Person;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PersonMapper {
	Person selectById(@Param("id") Integer id);
	List<Person> selectAll();
	int addPerson(Person record);
	int updateById(Person record);
}