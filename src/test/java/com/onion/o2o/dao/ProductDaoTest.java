package com.onion.o2o.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.onion.o2o.BaseTest;
import com.onion.o2o.entity.Product;
import com.onion.o2o.entity.ProductCategory;
import com.onion.o2o.entity.ProductImg;
import com.onion.o2o.entity.Shop;

public class ProductDaoTest extends BaseTest {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Test
	@Ignore
	public void testInsertProduct() throws Exception {
		Shop shop1=new Shop();
		shop1.setShopId(16L);
		ProductCategory pc1=new ProductCategory();
		pc1.setProductCategoryId(11L);
		//初始化三个商品实例并添加进shopId为1的店铺里
		Product product1=new Product();
		product1.setProductName("测试商品1");
		product1.setProductDesc("用于测试");
		product1.setImgAddr("test1");
		product1.setPriority(1);
		product1.setEnableStatus(1);
		product1.setCreateTime(new Date());
		product1.setLastEditTime(new Date());
		product1.setShop(shop1);
		product1.setProductCategory(pc1);
		Product product2=new Product();
		product2.setProductName("测试商品2");
		product2.setProductDesc("用于测试2");
		product2.setImgAddr("test2");
		product2.setPriority(2);
		product2.setEnableStatus(1);
		product2.setCreateTime(new Date());
		product2.setLastEditTime(new Date());
		product2.setShop(shop1);
		product2.setProductCategory(pc1);
		Product product3=new Product();
		product3.setProductName("测试商品3");
		product3.setProductDesc("用于测试3");
		product3.setImgAddr("test3");
		product3.setPriority(3);
		product3.setEnableStatus(1);
		product3.setCreateTime(new Date());
		product3.setLastEditTime(new Date());
		product3.setShop(shop1);
		product3.setProductCategory(pc1);
		int eff=productDao.insertProduct(product1);
		assertEquals(1,eff);
		eff=productDao.insertProduct(product2);
		assertEquals(1,eff);
		eff=productDao.insertProduct(product3);
		assertEquals(1,eff);
	}
	@Test
	@Ignore
	public void testQueryProductById() throws Exception{
		long productId=1;
		ProductImg productImg1=new ProductImg();
		productImg1.setImgAddr("tu1");
		productImg1.setImgDesc("test1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(productId);
		ProductImg productImg2=new ProductImg();
		productImg2.setImgAddr("tu2");
		productImg2.setImgDesc("test2");
		productImg2.setPriority(2);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(productId);
		List<ProductImg> productImgList=new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		int eff=productImgDao.batchInsertProductImg(productImgList);
		System.out.println("插入了"+eff+"张图片");
		Product product=productDao.queryProductById(productId);
		System.out.println("详情图的数量为："+product.getProductImgList().size());
	}
	@Test
	@Ignore
	public void testUpdateProduct() throws Exception{
		Product product=new Product();
		ProductCategory pc=new ProductCategory();
		Shop shop=new Shop();
		shop.setShopId(16L);
		pc.setProductCategoryId(11L);
		product.setProductId(1l);
		product.setShop(shop);
		product.setProductName("第一个产品");
		product.setProductCategory(pc);
		int eff=productDao.updateProduct(product);
		System.out.println("test:"+eff);
	}
	@Test
	@Ignore
	public void testQueryProductList() throws Exception{
		Product productCondition=new Product();
		//分页查询，预期返回三条结果
		List<Product> productList=productDao.queryProudctList(productCondition, 0, 3);
		System.out.println("list的大小为："+productList.size());
		int count=productDao.queryProductCount(productCondition);
		System.out.println("count1的大小为"+count);
		productCondition.setProductName("测试");
		productList=productDao.queryProudctList(productCondition, 0, 6);
		System.out.println("list的大小为："+productList.size());
		count=productDao.queryProductCount(productCondition);
		System.out.println("count2的大小为："+count);
	}
	@Test
	public void testUpdateProductCategoryToNull() {
		int eff=productDao.updateProductCategoryToNull(12L);
		assertEquals(1, eff);
	}
}
