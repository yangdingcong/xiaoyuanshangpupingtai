package com.onion.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.onion.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	
	//通过shop id查询店铺商品列表
	List<ProductCategory> queryProductCategoryList(long shopId);
	//批量新增
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	//删除指定商品类别
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId,@Param("shopId") long shopId);
	
}
