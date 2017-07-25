package com.sihuatech.service;

import java.util.List;

import com.sihuatech.po.Relation;
import com.sihuatech.po.RelationWrapper;

public interface RelationService {
	public Relation selectById(Integer id);
	public List<Relation> selectAll();
	public List<RelationWrapper> selectAllWrapper();
	int addRelation(Relation record);
//	void addRelationList(List<Relation> relationList);
	int updateById(Relation record);

}
