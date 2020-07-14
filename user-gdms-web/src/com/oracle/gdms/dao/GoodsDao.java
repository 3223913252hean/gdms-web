package com.oracle.gdms.dao;

import java.util.List;
import java.util.Map;

import com.oracle.gdms.entity.GoodsEntity;
import com.oracle.gdms.entity.GoodsModel;

public interface GoodsDao {
	
	List<GoodsModel> findByPage(Map<String,Integer>map);

	int findCount();

	GoodsModel findById(int goodsid);

	int add(GoodsEntity goods);

	void updatePush(int goodsid);
}
