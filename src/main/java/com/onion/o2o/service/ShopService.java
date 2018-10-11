package com.onion.o2o.service;




import com.onion.o2o.dto.ImageHolder;
import com.onion.o2o.dto.ShopExecution;
import com.onion.o2o.entity.Shop;
import com.onion.o2o.exception.ShopOperationException;

public interface ShopService {
	//根据shopCondition分页返回相应列表数据
	//pageIndex ：页码,pageSize:每页的行数
	public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	
	//根据店铺ID获取店铺信息
	Shop getByShopId(long shopId);
	
	//更新店铺信息，包括对图片的处理
	ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;
	
	//增加商铺
	ShopExecution addShop(Shop shop,ImageHolder thumbnail) throws RuntimeException;
}
