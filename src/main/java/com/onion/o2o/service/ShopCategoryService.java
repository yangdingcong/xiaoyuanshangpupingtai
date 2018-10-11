package com.onion.o2o.service;

import java.util.List;

import com.onion.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryConditon);
}
