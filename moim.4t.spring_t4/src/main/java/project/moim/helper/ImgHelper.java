package project.moim.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImgHelper {
	public static final ImgHelper IMG_HELPER = new ImgHelper();
	
	private ImgHelper() {}
	
	public static ImgHelper getInstance() {
		return IMG_HELPER;
	}
	
	public File makeThumb(File file, int reW, int reH) {
		try {
			BufferedImage origin = ImageIO.read(file);
			
			int oriW = origin.getWidth();
			int oriH = origin.getHeight();
			
			// 원본 너비 기준 비율 높이 계산
			int nh = (oriW * reH) / reW;
			
			// 크랍을 위해 비율 너비 계산
			int nw = oriW;
			if (nh > oriH) {
				nw = (oriH * reW) / reH;
				nh = oriH;
			}
			
			// 이미지 센터 크랍
			BufferedImage cropImg = Scalr.crop(origin, (oriW-nw)/2, (oriH-nh)/2, nw, nh);
			
			// 크랍된 이미지 리사이즈(썸네일화)
			BufferedImage thumsrc = Scalr.resize(cropImg, reW, reH);
			
			// 썸네일 저장
			String str = file.getName();
			String filetype = str.substring(str.lastIndexOf('.') + 1);
			
			ImageIO.write(thumsrc, filetype, file);
			
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
}
