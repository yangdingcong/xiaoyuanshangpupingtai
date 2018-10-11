package com.onion.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.onion.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;



public class ImageUtil {
	//private static String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r= new Random();
	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) 
	{
		String realFileName=getRandomFileName(); //生存随机名字
		String extension=getFileExtension(thumbnail.getImageName());//获取拓展名，如jpg,png
		makeDirPath(targetAddr);
		String relativeAddr=targetAddr+realFileName+extension; //相对路径
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr); //路径=根路径+相对路径
		//创建缩略图
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200).outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}
	
	//创建目标路径锁涉及到的目录，例如，/home/work/xxx.jpg,那么home,work文件夹应当创建出来
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
		File dirPath=new File(realFileParentPath);
		if(!dirPath.exists()) 
		{
			dirPath.mkdirs();//此时会创建没有的文件夹
		}
		
	}
	//获取输入文件流的拓展名
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));//获取最后一个点后的字符串
	}
	//生成随机文件名
	private static String getRandomFileName() 
	{
		//获取随机的五位数
		int rannum=r.nextInt(89999)+10000;
		String nowTimeStr=sDateFormat.format(new Date());
		return nowTimeStr+rannum;
	}
	
	
	//storePath是文件路径，就删除该文件，如果是目录路径就删除该目录下的所有文件
	public static void deleteFileOrPate(String storePath) 
	{
		File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {
				File files[]=fileOrPath.listFiles();
				for(int i=0;i<files.length;i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
	//处理详情图,返回新生成图片的相对路径
	public static String generateNormalImg(ImageHolder thumbnail,String targetAddr) {
		String realFileName=getRandomFileName(); //生存随机名字
		String extension=getFileExtension(thumbnail.getImageName());//获取拓展名，如jpg,png
		makeDirPath(targetAddr);
		String relativeAddr=targetAddr+realFileName+extension; //相对路径
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr); //路径=根路径+相对路径
		//创建缩略图
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640).outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}
	
//	public static void main(String[] args) throws IOException {
//		Thumbnails.of(new File("输入文件")).size(200, 200).watermark(Positions.BOTTOM_RIGHT,
//				ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).outputQuality(0.8f).toFile("输出文件");
//	}
}
