package com.onion.o2o.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.onion.o2o.BaseTest;
import com.onion.o2o.entity.ProductCategory;

public class ProductCategoryDaoTest extends BaseTest {
	
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	@Ignore
	public void testQueryByShopId() throws Exception{
		long shopId=1;
		List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategoryList(shopId);
		System.out.println("该店铺自定义类别数为："+productCategoryList.size());
	}
	@Test
	@Ignore
	public void testBatchInsertProductCategory() {
		ProductCategory productCategory=new ProductCategory();
		productCategory.setProductCategoryName("商品类别1");
		productCategory.setPriority(2);
		productCategory.setCreateTime(new Date());
		productCategory.setShopId(1L);
		ProductCategory productCategory2=new ProductCategory();
		productCategory2.setProductCategoryName("商品类别2");
		productCategory2.setPriority(3);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(1L);
		List<ProductCategory> productCategoryList=new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory);
		productCategoryList.add(productCategory2);
		int effectedNum=productCategoryDao.batchInsertProductCategory(productCategoryList);
		System.out.println("增加的数量为："+effectedNum);
	}
	@Test
	public void testDeleteProductCategory() throws Exception{
		long shopId=16L;
		List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategoryList(shopId);
		for(ProductCategory pc:productCategoryList) {
			if(pc.getProductCategoryId()>=6&&pc.getProductCategoryId()<=10) {
				int effectedNum=productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
				System.out.println("删除的商品类别数为:"+effectedNum);
			}
		}
		
	}
}
