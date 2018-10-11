package com.onion.o2o.dao;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.onion.o2o.BaseTest;
import com.onion.o2o.entity.*;

public class ShopDaoTest extends BaseTest{
	@Autowired
	private ShopDao shopDao;
	
	@Test
	public void testQueryShopList() {
		Shop shopCondition=new Shop();
		PersonInfo owner=new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		List<Shop> shopList=shopDao.queryShopList(shopCondition, 0, 5);
		int count=shopDao.queryShopCount(shopCondition);
		System.out.println("查询到的店铺的数量为:"+shopList.size());
		System.out.println("店铺总数："+count);
		ShopCategory sc=new ShopCategory();
		sc.setShopCategoryId(1L);
		shopCondition.setShopCategory(sc);
		shopList=shopDao.queryShopList(shopCondition, 0, 3);
		count=shopDao.queryShopCount(shopCondition);
		System.out.println("查询到的店铺的数量为:"+shopList.size());
		System.out.println("店铺总数："+count);
		
	}
	
	
	@Test
	@Ignore
	public void testQueryShopById() 
	{
		long shopId=1;
		Shop shop=shopDao.queryByShopId(shopId);
		System.out.println("areaId:"+shop.getArea().getAreaId());
		System.out.println("areaName:"+shop.getArea().getAreaName());
	}
	
	/*@Test
	public void testInsertShop() 
	{
		Shop shop=new Shop();
		PersonInfo owner=new PersonInfo();
		Area area=new Area();
		ShopCategory shopCategory=new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(2);;
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺");
		shop.setShopAddr("广州");
		shop.setPhone("123456");
		shop.setShopImg("test");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		shop.setPriority(1);
		int effectedNum=shopDao.insertShop(shop);
		assertEquals(1,effectedNum);
	}*/
	
//	@Test
//	@Ignore
//	public void testUpdateShop() 
//	{
//		Shop shop=new Shop();
//		shop.setShopId(1L);
//		shop.setShopAddr("广州");
//		shop.setPhone("10010");
//		shop.setLastEditTime(new Date());
//		int effectedNum=shopDao.updateShop(shop);
//		assertEquals(1,effectedNum);
//	}
}
