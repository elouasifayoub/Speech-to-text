package com.solution.speech.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.solution.speech.utils.Constants;

import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

@Service
public class AudioStorageService {

	  public String upload(byte[] data, String fileName, Storage storage) {
			try {			
				BlobInfo blobInfo = storage.create(
					BlobInfo.newBuilder(Constants.BUCKET, fileName).build(), data);
				return "gs://" + Constants.BUCKET +"/" + fileName;
			}catch(IllegalStateException e){
				throw new RuntimeException(e);
			}
	  	}
	  
	  private File convertMultiPartToFile(MultipartFile file ) 
	    {
	        File convFile = new File("audio/"+ file.getOriginalFilename() );
	        FileOutputStream fos;
			try {
				fos = new FileOutputStream( convFile );
				fos.write( file.getBytes() );
		        fos.close();
			} catch ( IOException e) {
				e.printStackTrace();
			}
	        
	        return convFile;
	    }
	  public byte[] convertMp3ToAmr(MultipartFile file) throws Exception {
		    File target = new File(Constants.TARGET_FILE);
		    File source = convertMultiPartToFile(file);
	        //Audio Attributes
	        AudioAttributes audio = new AudioAttributes();
	        audio.setCodec("flac");
	        audio.setBitRate(12200);
	        audio.setChannels(1);
	        audio.setSamplingRate(8000);

	        //Encoding attributes
	        EncodingAttributes attrs = new EncodingAttributes();
	        attrs.setOutputFormat("flac");
	        attrs.setAudioAttributes(audio);

	        //Encode
	        Encoder encoder = new Encoder();
	        encoder.encode(new MultimediaObject(source), target, attrs);
	        byte[] data = FileUtils.readFileToByteArray(target);
	        return data;
	    }
	  
	  
}
