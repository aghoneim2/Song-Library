package application;

public class Song {

	private String songName;
	private String artistName;
	private String albumName;
	private int albumYear;
	
	public Song(String songName, String artistName, String albumName, int albumYear){
		this.songName = songName;
		this.artistName = artistName;
		this.albumName = albumName;
		this.albumYear = albumYear;
	}
	
	public Song(String songName, String artistName, String albumName){
		this.songName = songName;
		this.artistName = artistName;
		this.albumName = albumName;
	}
	
	public Song(String songName, String artistName, int albumYear){
		this.songName = songName;
		this.artistName = artistName;
		this.albumYear = albumYear;
	}
	
	public Song(String songName, String artistName){
		this.songName = songName;
		this.artistName = artistName;
	}
	
	public Song(){
		
	}
	
	public String getSongName()
	{
		return songName;
	}
	
	public String getArtistName(){
		return artistName;
	}
	
	public String getAlbumName(){
		return albumName;
	}
	
	public int getAlbumYear(){
		return albumYear;
	}
	
	public void setSongName(String name){
		songName = name;
	}
	
	public void setArtistName(String name){
		artistName = name;
	}
	
	public void setAlbumName(String name){
		albumName = name;
	}
	
	public void setAlbumYear(int year){
		albumYear = year;
	}
	
	public String toString(){
		return songName + " - " + artistName;
	}
}
