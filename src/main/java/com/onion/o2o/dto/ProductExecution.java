package com.onion.o2o.dto;

import java.util.List;

import com.onion.o2o.entity.Product;
import com.onion.o2o.enums.ProductStateEnum;

public class ProductExecution {
	//结果状态
		private int state;
		//状态标识
		private String stateInfo;
		//增删改商品时
		private Product product;
		//查询时
		private List<Product> productList;
		
		private int count;//商品数量
		
		public Product getProduct() {
			return product;
		}
		public void setProduct(Product product) {
			this.product = product;
		}
		public List<Product> getProductList() {
			return productList;
		}
		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public ProductExecution() {
			
		}
		//失败时仅返回状态
		public ProductExecution(ProductStateEnum stateEnum) {
			this.state=stateEnum.getState();
			this.stateInfo=stateEnum.getStateInfo();
		}
		//操作成功的时候返回列表
		public ProductExecution(ProductStateEnum stateEnum,List<Product> productList) {
			this.state=stateEnum.getState();
			this.stateInfo=stateEnum.getStateInfo();
			this.productList=productList;
			
		}
		public ProductExecution(ProductStateEnum stateEnum,Product product) {
			this.state=stateEnum.getState();
			this.stateInfo=stateEnum.getStateInfo();
			this.product=product;
			
		}
		
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		public String getStateInfo() {
			return stateInfo;
		}
		public void setStateInfo(String stateInfo) {
			this.stateInfo = stateInfo;
		}

}
