package com.sihuatech.service;

import java.util.List;

import com.sihuatech.po.Relation;

public interface RelationService {
	public Relation selectById(Integer id);
	public List<Relation> selectAll();
	int addRelation(Relation record);
	int updateById(Relation record);

}
