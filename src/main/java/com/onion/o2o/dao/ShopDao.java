package com.onion.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.onion.o2o.entity.Shop;

public interface ShopDao {
	//分页查询店铺，可输入的条件有店铺名（模糊查询），店铺状态，店铺类别。。。。
	//分页开始的下标rowIndex,每页的数据行数pageSize
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);
	//返回查询queryShopList的总数
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
	
	
	//查询店铺
	Shop queryByShopId(long shopId);
	//新增店铺
	int insertShop(Shop shop);
	//更新店铺
	int updateShop(Shop shop);
}
