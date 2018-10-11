package com.onion.o2o.service;

import java.util.List;

import com.onion.o2o.dto.ImageHolder;
import com.onion.o2o.dto.ProductExecution;
import com.onion.o2o.entity.Product;
import com.onion.o2o.exception.ProductOperationException;

public interface ProductService {
	
	//添加商品
	ProductExecution addProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgList) 
			throws ProductOperationException;
	//通过id查询商品
	Product getProductById(long productId);
	//修改商品信息
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList) 
			throws ProductOperationException;
	/*
	 * 查询商品列表并分页
	 * */
	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
}
