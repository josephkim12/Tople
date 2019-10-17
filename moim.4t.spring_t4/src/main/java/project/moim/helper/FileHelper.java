package project.moim.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileHelper {
	private static FileHelper fileHelper = new FileHelper();

	private FileHelper() {
		super();
	}
	
	public static FileHelper getInstance() {
		return fileHelper;
	}
	
	public File fileCopyToPath(MultipartFile file, String basepath, String subpath ,String id) {
		String originalname = file.getOriginalFilename();
		String filetype = originalname.substring(originalname.lastIndexOf('.'));
		
		String filename = newname(id) + filetype;
		String filepath = basepath + subpath;
		
		File imgfile = new File(filepath, filename);
		
		try {
			FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(imgfile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return imgfile;
	}
	
	public String newname(String id) {
		SimpleDateFormat time = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
		
		String result = id + time.format(System.currentTimeMillis());
		
		return result;
	}
	
	public void copyMeta(File file, String metapath, String subpath) {
		File metafile = new File(metapath + subpath, file.getName());
		
		try {
			FileCopyUtils.copy(new FileInputStream(file), new FileOutputStream(metafile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
