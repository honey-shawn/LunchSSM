package com.sihuatech.mapper;

import com.sihuatech.po.Relation;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RelationMapper {
	Relation selectById(@Param("id") Integer id);
	List<Relation> selectAll();
	int addRelation(Relation record);
	int updateById(Relation record);
}