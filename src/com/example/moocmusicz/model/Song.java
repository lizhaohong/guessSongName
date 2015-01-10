package com.example.moocmusicz.model;

public class Song {
	//关键类，用来定义歌曲的类
	public String SongName; //歌曲名字
	public String SongFileName; //歌曲文件名
	public int SongNameLength;//歌曲名字的长度
	
	public char[] getSongNameCharArray() {
		return SongName.toCharArray();
	}
	
	public String getSongName() {
		return SongName;
	}
	public void setSongName(String songName) {
		this.SongName = songName;
		
		this.SongNameLength = this.SongName.length();
	}
	public String getSongFileName() {
		return SongFileName;
	}
	public void setSongFileName(String songFileName) {
		this.SongFileName = songFileName;
	}
	public int getSongNameLength() {
		return SongNameLength;
	}
	public void setSongNameLength(int songNameLength) {
		SongNameLength = songNameLength;
	}
	
	
}
