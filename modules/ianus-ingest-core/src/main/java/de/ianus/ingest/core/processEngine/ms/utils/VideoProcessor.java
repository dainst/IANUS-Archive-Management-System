package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.IOException;

import de.ianus.ingest.core.utils.CoreProperties;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

public class VideoProcessor {
	
	
	private String ffmpegPath = "ffmpeg";
	
	private String ffprobePath = "ffprobe";
	
	private CoreProperties props;
	
	
	private FFmpeg ffmpeg = null;
	
	private FFprobe ffprobe = null;
	
	public FFmpegProbeResult probeResult = null;
	
	
	private String inputPath = ""; 
	
	
	
	private void _constructor() throws IOException {
		this.props = new CoreProperties();
		this.ffmpegPath = this.props.get("ffmpeg.path");
		this.ffprobePath = this.props.get("ffprobe.path");
		this.ffprobe = new FFprobe(ffprobePath);
		this.ffmpeg = new FFmpeg(ffmpegPath);
	}
	
	public VideoProcessor(String path) throws IOException {
		this._constructor();
		this.readInput(path);
	}
	
	public VideoProcessor() throws IOException {
		this._constructor();
	}
	
	
	public void readInput(String path) {
		this.inputPath = path;
		this.probeResult = null;	// reset
		// ffprobe will return an error code on some files that are no video
		try {
			this.probeResult = ffprobe.probe(this.inputPath);
		}catch (IOException e) {
			this.inputPath = null;
		}
	}
	
	
	
	public Boolean isVideo(String path) {
		String extension = "";
		int i = path.lastIndexOf('.');
		if (i > 0) extension = path.substring(i+1);
		
		switch(extension) {
			case "mkv": case "mp4": case "mj2": case "mxf": case "mpg": case "mpeg":
			case "avi": case "mov": case "asf": case "wmv": case "ogg": case "ogv":
			case "ogx": case "ogm": case "spx": case "flv": case "fl4":
				this.readInput(path);
				if(this.probeResult != null) return true;
		}
		
		return false;
	}
	
	
	
	public void createPreview(String output, int targetWidth) {
		if(this.probeResult == null) return;
		FFmpegStream stream = probeResult.getStreams().get(0);
		int width = stream.width;
		if(width > 0 && targetWidth > width) targetWidth = width;
		FFmpegBuilder builder = this.ffmpeg.builder();
		builder.addInput(this.inputPath);
		builder.addOutput(output)
			//.setVideoWidth(50)	// no effect
			.setVideoFilter("scale=" + targetWidth + ":trunc(ow/a/2)*2") // we need even height values for some buggy encoders...
			.setStrict(FFmpegBuilder.Strict.EXPERIMENTAL);
		
		FFmpegExecutor executor = new FFmpegExecutor(this.ffmpeg, this.ffprobe);
		executor.createJob(builder).run();
	}
	
	
	/*
	public void createThumbnail(String target) {
		// ffmpeg -i test.mp4 out.png -> get the first frame or sometimes embedded imagery
		// ffmpeg -i Diafe_1933-34.mkv -ss 00:01:39.435 -f image2 -vframes 1 out.png -> works not in all cases
	}
	*/
}
