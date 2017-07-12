package com.sihuatech.service;

import java.util.List;

import com.sihuatech.po.Person;

public interface PersonService {
	public Person selectById(Integer id);
	public List<Person> selectAll();
	int addPerson(Person record);
	int updateById(Person record);

}
