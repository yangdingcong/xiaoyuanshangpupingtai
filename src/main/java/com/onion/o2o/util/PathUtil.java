package com.onion.o2o.util;

public class PathUtil {
	//获取分隔符类型，如/
	private static String separator=System.getProperty("file.separator");
	public static String getImgBasePath() 
	{
		//获取操作系统
		String os=System.getProperty("os.name");
		String basePath="";
		if(os.toLowerCase().startsWith("win"))  //如果是Windows
		{
			basePath="D:/app1/image/";
		}else 
		{
			basePath="/home/yangcong/image/";
		}
		basePath=basePath.replace("/", separator);//替换分隔符，保证路径是正确的
		return basePath;
	}
	public static String getShopImagePath(long shopId) 
	{
		String imagePath="/upload/item/shop/"+shopId+"/";
		return imagePath.replace("/",separator);
	}
	
}
