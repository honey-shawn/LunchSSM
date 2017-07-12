package com.sihuatech.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sihuatech.mapper.RelationMapper;
import com.sihuatech.po.Relation;
import com.sihuatech.service.RelationService;

public class RelationServiceImpl implements RelationService{

	@Autowired
	private RelationMapper relationMapper;

	@Override
	public Relation selectById(Integer id) {
		return relationMapper.selectById(id);
	}

	@Override
	public List<Relation> selectAll() {
		return relationMapper.selectAll();
	}

	@Override
	public int addRelation(Relation record) {
		return relationMapper.addRelation(record);
	}

	@Override
	public int updateById(Relation record) {
		return relationMapper.updateById(record);
	}


}
