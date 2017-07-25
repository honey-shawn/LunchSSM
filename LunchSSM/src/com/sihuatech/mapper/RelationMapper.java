package com.sihuatech.mapper;

import com.sihuatech.po.Relation;
import com.sihuatech.po.RelationWrapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RelationMapper {
	Relation selectById(@Param("id") Integer id);
	List<Relation> selectAll();
	List<RelationWrapper> selectAllWrapper();
	int addRelation(Relation record);
	int updateById(Relation record);
}