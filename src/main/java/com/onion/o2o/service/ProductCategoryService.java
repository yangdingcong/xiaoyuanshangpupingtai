package com.onion.o2o.service;

import java.util.List;

import com.onion.o2o.dto.ProductCategoryExecution;
import com.onion.o2o.entity.ProductCategory;
import com.onion.o2o.exception.ProductCategoryOperationException;


public interface ProductCategoryService {
	//查询指定某个店铺下的所有商品类别信息
	List<ProductCategory> getProductCategoryList(long shopId);
	//批量增加
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) 
			throws ProductCategoryOperationException;
	//删除
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) 
			throws ProductCategoryOperationException;
}
