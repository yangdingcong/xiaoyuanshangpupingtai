package com.onion.o2o.dao;

import java.util.List;

import com.onion.o2o.entity.ProductImg;

public interface ProductImgDao {
	
	
	List<ProductImg> queryProductImgList(long productId);
	
	//批量增加商品详情图片
	int batchInsertProductImg(List<ProductImg> productImgList);
	//删除图片
	int deleteProductImgByProductId(Long productId);
}
