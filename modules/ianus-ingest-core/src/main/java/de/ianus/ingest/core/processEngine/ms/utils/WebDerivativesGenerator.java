/**
 * This class iterates a data collection and creates 
 * thumbnail previews from image files or file icons for all other 
 * file types. 
 * It also creates previews for various screen resolutions from image files. 
 * 
 * Possible extensions may include generation of preview _images_ for 
 * data types other than images, eg. PDF. 
 */
package de.ianus.ingest.core.processEngine.ms.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import de.ianus.ingest.core.bo.ActivityOutput;

//import org.apache.log4j.Logger;

/**
 * @author hendrik
 *
 */
public class WebDerivativesGenerator {
	
	//private static Logger logger = Logger.getLogger(WebDerivativesGenerator.class);
	
	private String inputData;
	private String outputData;
	
	private ActivityOutput output;
	
	private VideoProcessor videoProcessor = null;
	
	
	
	public WebDerivativesGenerator(ActivityOutput output, String inputData, String outputData) {
		this.output = output;
		this.inputData = inputData;
		this.outputData = outputData;
	}
	
	/**
	 * 
	 * @param String path		the relative path without leading slash
	 */
	public void recurse(String path) throws Exception{
		//logger.info(path);
		File file = new File(this.inputData);
		String out = this.outputData;
		if(path != null) {
			file = new File(this.inputData + "/" + path);
			out = this.outputData + "/" + path;
		}
		
		if(file.exists() && file.isDirectory()) {
			// replicate the directory in the output folder
			if(!new File(out).exists()) {
				Files.createDirectories(Paths.get(out));
			}
			
			if(path != null) path += "/";
			if(path == null) path = "";
			// iterate over the folder contents...
			for(File _file : file.listFiles()) {
				String _path = path + _file.getName();
				if(_file.isFile())
					processFile(_path);
				if(_file.isDirectory())
					recurse(_path);
			}
		}
	}
	
		
	private void processFile(String path) throws IOException {
		generateThumb(path);
		generatePreview(path, 1600);
		generatePreview(path, 800);
		generatePreview(path, 400);
	}
	
	
	
	private void generateThumb(String path) throws IOException {
		String source = this.inputData + "/" + path;
		String name = path;
		String target;
		if(path.lastIndexOf("/") > 0) {
			name = path.substring(path.lastIndexOf("/") + 1);
			path = path.substring(0, path.lastIndexOf("/") + 1);
			target = this.outputData + "/" + path + "thumb_" + name + ".png";
		}else{
			target = this.outputData + "/thumb_" + name + ".png";
		}
		
		File file = new File(source);
		if(acceptedImageFormat(file)) {
			if(this.output != null) this.output.print("Creating thumbnail: " + target);
			resizeImage(file, target, 100, 100);
		}
		// no need to create static icons, as they are now fully covered as dynamic CSS icons
	}
	
	
	
	private void generatePreview(String path, int targetWidth) throws IOException {
		String source = this.inputData + "/" + path;
		String target = this.getTarget(path, targetWidth, "png");
		
		File file = new File(source);
		if(acceptedImageFormat(file)) {
			if(this.output != null) this.output.print("Creating preview: " + target);
			resizeImage(file, target, targetWidth, null);
		}else{
			
			if(this.videoProcessor == null)
				this.videoProcessor = new VideoProcessor();
			
			if(this.videoProcessor.isVideo(source)) {
				target = this.getTarget(path, targetWidth, "mp4");
				if(this.output != null) this.output.print("Creating video preview: " + target);
				this.videoProcessor.createPreview(target, targetWidth);
			}
			else{
				if(this.output != null) this.output.print("Preview not available for: " + source);
			}
		}
	}
	
	
	private String getTarget(String path, Integer targetWidth, String ext) {
		String name = path;
		String target = null;
		if(path.lastIndexOf("/") > 0) {
			name = path.substring(path.lastIndexOf("/") + 1);
			path = path.substring(0, path.lastIndexOf("/") + 1);
			target = this.outputData + "/" + path + "preview_" + Integer.toString(targetWidth) + "_" + name + "." + ext;
		}else{
			target = this.outputData + "/preview_" + Integer.toString(targetWidth) + "_" + name + "." + ext;
		}
		return target;
	}
	
	
	/**
	 * For basic Java image IO, format will equal one of: 
	 * BMP,bmp,JPEG,jpeg,JPG,jpg,WBMP,wbmp,GIF,gif,PNG,png
	 * Further plugins will be required to read eg. TIFF or JPEG2000.
	 *			
	 * @param File file
	 * @return Boolean
	 */
	private static boolean acceptedImageFormat(File file){
		ImageInputStream stream = null;
		Boolean accepted = true;
		
		try {
			stream = ImageIO.createImageInputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// get all currently registered readers that recognize the image format
		Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);
		
		// get the first reader
		if(!iter.hasNext()) {
			accepted = false;
		}
		else{
			ImageReader reader = iter.next();
			try {
				String format = reader.getFormatName();
				if(format == null || format.equals(""))
					accepted = false;
			} catch (IOException e) {
				accepted = false;
				
			}
		}
		
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return accepted;
	}	
	
	/**
	 * This method takes the imageFile, target height || width, and will return the resized image.
	 * If only one target dimension is specified, the image will be scaled accordingly.
	 * If both dimensions are given, aspect ratio will be kept and the image becomes cropped to 
	 * the center section. 
	 * The resulting image becomes written to the file system as image/PNG with the specified path, 
	 * so the path should end in '.png'.
	 * 
	 * @param File src, String targetPath, targetWidth, targetHeight
	 * @throws IOException 
	 */

	public static void resizeImage(File file, String targetPath, Integer targetWidth, Integer targetHeight)
			throws IOException {
		
		BufferedImage img = ImageIO.read(file);

		
		
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        
        BufferedImage resizedImage = img;
        
        int width = img.getWidth();
    	int height = img.getHeight();
    	
    	// keep the original specifications
    	Integer _targetHeight = targetHeight;
    	Integer _targetWidth = targetWidth;
    	
    	// if provided size is smaller on at least one dimension, do not scale down any further 
    	Boolean scaling = true;
    	if(	(targetWidth != null 	&& width <= targetWidth)
    	||	(targetHeight != null 	&& height <= targetHeight)) {
    		targetHeight = height;
    		targetWidth = width;
    		scaling = false;
    	}
    	
    	// if both target dimensions were specified, enable the cropping mechanism to fit the box
    	Boolean cropping = false;
    	if(_targetWidth != null && _targetHeight != null) {
    		// trigger cropping
    		cropping = true;
    		// if supplied img is smaller than specified box, don't crop
    		if(_targetWidth > width && _targetHeight > height) {
    			cropping = false;
    		}
    	}
    	
    	// if one targetDimension was omitted (null), 
    	// calculate the second target dimension based on the supplied image aspect ration
    	if(targetHeight == null) {
    		double ratio = (double)height / (double)width;
    		targetHeight = (int) Math.round(ratio * targetWidth);
    	}
    	else if(targetWidth == null) {
    		double ratio = (double)width / (double)height;
    		targetWidth = (int) Math.round(ratio * targetHeight);
    	}
    	
    	// For better quality, use multi-step technique: start with original size, 
        // then scale down in multiple passes with drawImage() until  target size.
    	if(scaling) {
	    	do{
	            if(targetWidth != null && width > targetWidth) {
	            	width /= 2;
	                if(width < targetWidth) {
	                	width = targetWidth;
	                }
	            }
	
	            if(targetHeight != null && height > targetHeight) {
	            	height /= 2;
	                if(height < targetHeight) {
	                	height = targetHeight;
	                }
	            }
	            
	            BufferedImage tmp = new BufferedImage(width, height, type);
	            Graphics2D g2 = tmp.createGraphics();
	            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	            g2.drawImage(resizedImage, 0, 0, width, height, null);
	            g2.dispose();
	
	            resizedImage = tmp;
	            
	        } while (
	        		(targetWidth != null  && width > targetWidth)
	        	||  (targetHeight != null && height > targetHeight));
    	}
    	
    	// do the cropping
    	if(cropping) {
    		width = resizedImage.getWidth();
        	height = resizedImage.getHeight();
        	
        	int x = 0;
    		int y = 0;
    		if(_targetWidth < width) {
    			x = (width - _targetWidth) / 2;
    		}else{
    			_targetWidth = width;
    		}
    		if(_targetHeight < height) {
    			y = (height - _targetHeight) / 2;
    		}else{
    			_targetHeight = height;
    		}
    		resizedImage = resizedImage.getSubimage(x, y, _targetWidth, _targetHeight);
    	}
    	// TODO:
    	/* Compression not supported with Java ImageIO! - Should move on to JAI...
    	Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png"); 
    	ImageWriter writer = writers.next(); 
    	ImageWriteParam params = writer.getDefaultWriteParam();
    	params.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT); 
    	params.setCompressionQuality(quality);
    	*/
    	ImageIO.write(resizedImage, "PNG", new File(targetPath));
	}
	
}
