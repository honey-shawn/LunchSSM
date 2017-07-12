package com.sihuatech.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sihuatech.mapper.PersonMapper;
import com.sihuatech.po.Person;
import com.sihuatech.service.PersonService;
public class PersonServiceImpl implements PersonService{

	@Autowired
	private PersonMapper personMapper;

	@Override
	public Person selectById(Integer id) {
		return personMapper.selectById(id);
	}

	@Override
	public List<Person> selectAll() {
		return personMapper.selectAll();
	}

	@Override
	public int addPerson(Person record) {
		return personMapper.addPerson(record);
	}

	@Override
	public int updateById(Person record) {
		return personMapper.updateById(record);
	}


}
