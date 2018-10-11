package com.onion.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onion.o2o.dao.ProductDao;
import com.onion.o2o.dao.ProductImgDao;
import com.onion.o2o.dto.ImageHolder;
import com.onion.o2o.dto.ProductExecution;
import com.onion.o2o.entity.Product;
import com.onion.o2o.entity.ProductImg;
import com.onion.o2o.enums.ProductStateEnum;
import com.onion.o2o.exception.ProductOperationException;
import com.onion.o2o.service.ProductService;
import com.onion.o2o.util.ImageUtil;
import com.onion.o2o.util.PageCalculator;
import com.onion.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	
	@Override
	@Transactional
	/*
	 * 1.处理缩略图，获取缩略图相对路径并赋值给product
	 * 2.往tb_product写入商品信息，获取productID
	 * 3.结合productId批量处理商品详情图
	 * 4.将商品详情图列表批量插入tb_product_img中
	 * */
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		//空值判断
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null) {
			//给商品设置上默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			product.setEnableStatus(1);
			//如果商品缩略图不为空则添加
			if(thumbnail!=null) {
				addThumbnail(product,thumbnail);
			}
			try {
				//添加商品
				int eff=productDao.insertProduct(product);
				//如果添加失败
				if(eff<=0) {
					throw new ProductOperationException("创建商品失败");
				}
			}catch(Exception e) {
				throw new ProductOperationException("创建商品失败"+e.toString());
			}
			//若商品详情图不为空则添加
			if(productImgList!=null&&productImgList.size()>0) {
				addProductImgList(product,productImgList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		}else {
			//传参为空则返回空值错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	private void addProductImgList(Product product, List<ImageHolder> productImgList) {
		// TODO Auto-generated method stub
		//获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		
		List<ProductImg> productImglist=new ArrayList<ProductImg>();
		//遍历图片一次去处理，并添加进productImg实体类里
		for(ImageHolder productImgHolder:productImgList) {
			String imgAddr=ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg=new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImglist.add(productImg);
		}
		//如果确实是有图片需要添加的，就执行批量添加操作
		if(productImglist.size()>0) {
			try {
				int eff=productImgDao.batchInsertProductImg(productImglist);
				if(eff<=0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			}catch(Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	private void addThumbnail(Product product, ImageHolder thumbnail) {
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr=ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}

	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList) 
			throws ProductOperationException {
				//空值判断
				if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null) {
					//给商品设置上默认属性
					product.setLastEditTime(new Date());
					
					//1缩略图
					//如果商品缩略图不为空且原有缩略图不为空
					//则删除原有的缩略图并添加现有的
					if(thumbnail!=null) {
						//先获取一遍原有信息，因为原来的信息里有原图片的地址
						Product tempProduct=productDao.queryProductById(product.getProductId());
						if(tempProduct.getImgAddr()!=null) {
							ImageUtil.deleteFileOrPate(tempProduct.getImgAddr());
						}
						addThumbnail(product,thumbnail);
					}
					//2详情图
					//如果有新存入的商品详情图，则将原先的删除，并添加新的图片
					if(productImgHolderList!=null&&productImgHolderList.size()>0) {
						deleteProductImgList(product.getProductId());
						addProductImgList(product,productImgHolderList);
					}
					try {
						//添加商品
						int eff=productDao.updateProduct(product);
						//如果添加失败
						if(eff<=0) {
							throw new ProductOperationException("更新商品失败");
						}
						return new ProductExecution(ProductStateEnum.SUCCESS,product);
					}catch(Exception e) {
						throw new ProductOperationException("更新商品失败"+e.toString());
					}
				}else {
					//传参为空则返回空值错误信息
					return new ProductExecution(ProductStateEnum.EMPTY);
				}
	}

	private void deleteProductImgList(Long productId) {
		List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
		//干掉原来的图片
		for(ProductImg productImg:productImgList) {
			ImageUtil.deleteFileOrPate(productImg.getImgAddr());
		}
		productImgDao.deleteProductImgByProductId(productId);
		
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		//页码转换成数据库的行码，并调用dao层取回指定也灭的商品列表
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList=productDao.queryProudctList(productCondition, rowIndex, pageSize);
		int count=productDao.queryProductCount(productCondition);
		ProductExecution pe=new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

}
