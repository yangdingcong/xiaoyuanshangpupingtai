package com.onion.o2o.dto;

import java.io.InputStream;

public class ImageHolder {
	private String imageName;
	private InputStream image;
	
	public ImageHolder(String imageName,InputStream image) {
		this.image=image;
		this.imageName=imageName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}
	
}
