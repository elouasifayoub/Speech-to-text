package com.solution.speech.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Audio {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String audioName;
	private String audioTranscription;
	private String audioType;
	
	@Lob
	private byte[] data;
	
	public Audio() {}

	public Audio(String audioName, String audioType, String audioTranscription, byte[] data) {
		super();
		this.audioName = audioName;
		this.audioType = audioType;
		this.audioTranscription = audioTranscription;
		this.data = data;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAudioName() {
		return audioName;
	}

	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}

	public String getAudioType() {
		return audioType;
	}

	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	public String getAudioTranscription() {
		return audioTranscription;
	}

	public void setAudioTranscription(String audioTranscription) {
		this.audioTranscription = audioTranscription;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
