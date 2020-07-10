package com.oracle.gdms.service;

import com.oracle.gdms.entity.GoodsModel;
import com.oracle.gdms.entity.PageModel;

public interface GoodsService {

	/**
	 * 分页显示商品数据
	 * @param offset 起始位置
	 * @param rows 每页多少条数据
	 * @return
	 */
	PageModel<GoodsModel> findByPage(int pageNumber,int rows);
}
