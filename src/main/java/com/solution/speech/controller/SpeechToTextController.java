package com.solution.speech.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.solution.speech.model.Audio;
import com.solution.speech.service.AudioStorageService;
import com.solution.speech.utils.Constants;

@Controller
public class SpeechToTextController {
	
	private CredentialsProvider credentialsProvider;
	
	@Autowired
	public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
	}
	
	private SpeechSettings settings = null;
	
	@Autowired
	private Storage storage;
	
	@PostConstruct
	public void initialize() throws IOException {
		settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(Constants.CREDENTIAL_FILE));
		storage = StorageOptions.newBuilder().setCredentials(credentials)
				    .build()
				    .getService();
	}
	
	@Autowired 
	private AudioStorageService audioStorageService;
	
	private Audio audioModel = new Audio();
	
	@GetMapping("/")
	public String get(Model model) {
		audioModel = new Audio();
		return "index";
	}
	
	@PostMapping("/uploadFiles")
	public String uploadMultipleFiles(@RequestParam("files") MultipartFile file, Model model) throws Exception {
		
		try(SpeechClient client = SpeechClient.create(settings)){
			RecognitionConfig.Builder builder = RecognitionConfig.newBuilder()
					.setEncoding(AudioEncoding.ENCODING_UNSPECIFIED).setLanguageCode(Constants.ES)
					.setAudioChannelCount(1)
		            .setEnableSeparateRecognitionPerChannel(true)
					.setEnableAutomaticPunctuation(true).setEnableWordTimeOffsets(true);
			builder.setModel(Constants.MODEL);
			
			RecognitionConfig config = builder.build();
			
			byte[] data = audioStorageService.convertMp3ToAmr(file);
			String gcsUri = audioStorageService.upload(data, file.getOriginalFilename(), storage);
			
			RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
			OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response = client.longRunningRecognizeAsync(config, audio);
			while(!response.isDone()) {
				Thread.sleep(1000);
			}
			List<SpeechRecognitionResult> speechResults = response.get().getResultsList();
			StringBuilder transcription = new StringBuilder();
			for(SpeechRecognitionResult result : speechResults) {
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				transcription.append(alternative.getTranscript());
			}
			if(!transcription.toString().isEmpty()) {
			Translate translate = TranslateOptions.newBuilder().setApiKey(Constants.API_KEY).build().getService();
			
			String mysteriousText = transcription.toString();
			Detection detection = translate.detect(mysteriousText);
			String detectedLanguage = detection.getLanguage();
			Translation translation = translate.translate(
				    mysteriousText,
				    TranslateOption.sourceLanguage(detectedLanguage),
				    TranslateOption.targetLanguage("en"),
				    TranslateOption.format("text"));
			
			audioModel.setAudioName(file.getOriginalFilename());
			audioModel.setAudioTranscription(translation.getTranslatedText());
			byte[] bytes = translation.getTranslatedText().getBytes(StandardCharsets.UTF_8);
			audioModel.setData(bytes);
			audioModel.setAudioType(Constants.AUDIO_TYPE);
			model.addAttribute("audio", audioModel);
			}
		}
		
		
		return "index";
	}
	@GetMapping("/downloadFile")
	public ResponseEntity<ByteArrayResource> downloadFile(){
		Audio audio = audioModel;
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(audio.getAudioType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+audio.getAudioName()+"\"")
				.body(new ByteArrayResource(audio.getData()));
	}
}
