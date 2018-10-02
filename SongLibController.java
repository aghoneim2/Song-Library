package application;
	
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Comparator;
	
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Node;

public class SongLibController implements Initializable {
	
	
	//these are the fields to store all the data
	@FXML Button deleteSong;
	@FXML Button editSong;
	@FXML Button addSong;
	@FXML Button edit;
	@FXML Button cancel;
	@FXML ListView<Song> songList;
	@FXML ListView<String> songDetail;
	private ObservableList<Song> obsList;
	private ObservableList<String> sortList;
	@FXML TextField songName;
	@FXML TextField artistName;
	@FXML TextField albumName;
	@FXML TextField albumYear;
	@FXML Label addEditLabel;
		
	private static int counter = 0;//counts the number of items in listView
	
	private static String song = "";
	private static String artist = "";
	private static String album = "";
	private static int year = 0;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Create an observable list from an Array list
		obsList = FXCollections.observableArrayList();
		songList.setEditable(true);
		edit.setVisible(false);
		cancel.setVisible(false);
	}
	
	
	public void buttonPushed(ActionEvent e){
		Button b = (Button)e.getSource();
		
		//SONG ADDITION ATTEMPTED
		if (b == addSong){
			
			boolean check = false;
			
				try{
					song = songName.getText();
					artist = artistName.getText();
					album = albumName.getText();
					year = Integer.parseInt(albumYear.getText());
					check = true;
				}catch (Exception e1){
					check = false;
			          if(songName.getText().isEmpty() || artistName.getText().isEmpty())
			          {
			        	  //REQUIRED FIELDS NOT COMPLETE
			        	  Alert alert = new Alert(AlertType.ERROR);
				          alert.setTitle("Required Fields empty");
			        	  alert.setHeaderText("You left required fields empty. Please follow input directions.");
			        	  alert.showAndWait();
			          }else if((albumYear.getText().isEmpty() == false) && e1 instanceof NumberFormatException){
			        	 //Entered wrong input for number, but required fields complete
			        	  Alert alert = new Alert(AlertType.ERROR);
				          alert.setTitle("Input Error");
			        	  alert.setHeaderText("You did not enter a number. Please follow input directions.");
			        	  alert.showAndWait();
			          }
			          else{
			        	  check = true;
			          }
				}
				
				if (check){
					//AT THIS POINT, THE USER ENTERED VALID INFORMATION
					add(song, artist, album, year);
					
					sortList = FXCollections.observableArrayList(
							"Song: " + songName.getText(),
							"Artist: " + artistName.getText(),
							"Album: " + albumName.getText(),
							"Year: " + albumYear.getText());
					
					songDetail.setItems(sortList);
					
					//clear textfields of data
					songName.clear();
					artistName.clear();
					albumName.clear();
					albumYear.clear();
				}
				}else if(b == deleteSong){
					//SONG DELETION ATTEMPTED
					int index = songList.getSelectionModel().getSelectedIndex();
					if (index < 0){
						//DIDN'T SELECT ANYTHING
						Alert alert = new Alert(AlertType.ERROR);
				        alert.setTitle("Deletion Error");
			        	alert.setHeaderText("Please select something to delete.");
			        	alert.showAndWait();
			        	return;
					}
					songList.getItems().remove(index);
					counter--;
					if (index < songList.getItems().size()-1){
						songList.getSelectionModel().select(index+1);
						
						sortList = FXCollections.observableArrayList(
								"Song: " + songList.getSelectionModel().getSelectedItem().getSongName(),
								"Artist: " + songList.getSelectionModel().getSelectedItem().getArtistName(),
								"Album: " + songList.getSelectionModel().getSelectedItem().getAlbumName(),
								"Year: " + songList.getSelectionModel().getSelectedItem().getAlbumYear());
						
						songDetail.setItems(sortList);
					
					}else if ((index-1) > 0){
						songList.getSelectionModel().select(index-1);
						
						sortList = FXCollections.observableArrayList(
								"Song: " + songList.getSelectionModel().getSelectedItem().getSongName(),
								"Artist: " + songList.getSelectionModel().getSelectedItem().getArtistName(),
								"Album: " + songList.getSelectionModel().getSelectedItem().getAlbumName(),
								"Year: " + songList.getSelectionModel().getSelectedItem().getAlbumYear());
						
						songDetail.setItems(sortList);
					}
					Alert alert = new Alert(AlertType.CONFIRMATION);
			        alert.setTitle("Confirmation");
		        	alert.setHeaderText("The selected song has been deleted.");
		        	alert.showAndWait();
		        	
					///SELECTION IS CAUSING PROBLEMS
					songList.getSelectionModel().selectNext();
					////SELECTION IS CAUSING PROBLEMS
					
				}else if(b == editSong)
				{
					//specify another method to change scenes
					int index = songList.getSelectionModel().getSelectedIndex();
					if (index < 0){
						//DIDN'T SELECT ANYTHING
						Alert alert = new Alert(AlertType.ERROR);
				        alert.setTitle("Error");
			        	alert.setHeaderText("Please select something to edit.");
			        	alert.showAndWait();
			        	return;
					}
					
					edit(songList.getItems().get(index), index);
				}
		}
	
		
	/*public void changeScene(ActionEvent event)throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("EditView.FXML"));
		
		Parent editViewParent = loader.load();
		Scene editViewScene = new Scene (editViewParent);
		
		EditViewController controller = loader.getController();
		try{
			controller.initData(songList.getSelectionModel().getSelectedItem(), songList);
		}catch (Exception NullPointerException){
			Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	       	alert.setHeaderText("There is nothing in the table to edit, or you have not selected anything to edit.");
	       	alert.showAndWait();
	       	return;
		}
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(editViewScene);
		window.show();
		
		//songList = controller.getSongList();
		//Song temp = controller.update();
       	//return temp;
	}
		
	*/private void edit(Song song, int indexOfSong){
		Song tempSong = song;
		addEditLabel.setText("Edit A Song");
		edit.setVisible(true);
		cancel.setVisible(true);
		addSong.setVisible(false);
		editSong.setVisible(false);
		deleteSong.setVisible(false);
		
		//populate textfields with user input
		
		songName.setText(tempSong.getSongName());
		artistName.setText(tempSong.getArtistName());
		
		if(tempSong.getAlbumName() != null){
			albumName.setText(tempSong.getAlbumName());
		}
		if(tempSong.getAlbumYear() != 0){
			albumYear.setText(Integer.toString(tempSong.getAlbumYear()));
		}
		
		//edit button pushed, commit to listView
		edit.setOnAction(event -> {
			//first, collect any data
			Song temp2 = new Song();
			
			boolean check = false;
			
			try{
				temp2.setSongName(songName.getText());
				temp2.setArtistName(artistName.getText());
				temp2.setAlbumName(albumName.getText());
				temp2.setAlbumYear(Integer.parseInt(albumYear.getText()));
				check = true;
			}catch (Exception e1){
				check = false;
		          if(songName.getText().isEmpty() || artistName.getText().isEmpty())
		          {
		        	  //REQUIRED FIELDS NOT COMPLETE
		        	  Alert alert = new Alert(AlertType.ERROR);
			          alert.setTitle("Required Fields empty");
		        	  alert.setHeaderText("You left required fields empty. Please follow input directions.");
		        	  alert.showAndWait();
		          }else if((albumYear.getText().isEmpty() == false) && e1 instanceof NumberFormatException){
		        	 //Entered wrong input for number, but required fields complete
		        	  Alert alert = new Alert(AlertType.ERROR);
			          alert.setTitle("Input Error");
		        	  alert.setHeaderText("You did not enter a number. Please follow input directions.");
		        	  alert.showAndWait();
		          }
		          else{
		        	  check = true;
		          }
			}
			
			if (check){
				//AT THIS POINT, THE USER ENTERED VALID INFORMATION
				//now have to check if it exists
				
				if(counter > 0)
				{
					for (int i = 0; i < counter; i++)
					{
						if(temp2.getSongName().toLowerCase().equals(songList.getItems().get(i).getSongName().toLowerCase())){
							//song names match
							if(temp2.getArtistName().toLowerCase().equals(songList.getItems().get(i).getArtistName().toLowerCase())){
								//artist names match also, conflict error
								Alert alert = new Alert(AlertType.ERROR);
						        alert.setTitle("Input Error");
					        	alert.setHeaderText("This song/artist combination already exists.");
					        	alert.showAndWait();
					        	return;
							}
						}
					}
				}
			}
			
			//song is not a duplicate, set in list
			songList.getItems().set(indexOfSong, temp2);
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation");
        	alert.setHeaderText("The selected song has been edited.");
        	alert.showAndWait();
        	
        	songList.getSelectionModel().select(indexOfSong);
        	
        	sortList = FXCollections.observableArrayList(
					"Song: " + songList.getSelectionModel().getSelectedItem().getSongName(),
					"Artist: " + songList.getSelectionModel().getSelectedItem().getArtistName(),
					"Album: " + songList.getSelectionModel().getSelectedItem().getAlbumName(),
					"Year: " + songList.getSelectionModel().getSelectedItem().getAlbumYear());
			
			songDetail.setItems(sortList);
			
			songName.clear();
			albumName.clear();
			artistName.clear();
			albumYear.clear();
			
			addSong.setVisible(true);
			editSong.setVisible(true);
			deleteSong.setVisible(true);
			edit.setVisible(false);
			cancel.setVisible(false);
			addEditLabel.setText("Adding a Song");
			return;
			
		});
		
		//cancel button pushed, no edit
		cancel.setOnAction(event ->{
			songName.clear();
			albumName.clear();
			artistName.clear();
			albumYear.clear();
			
			addSong.setVisible(true);
			editSong.setVisible(true);
			deleteSong.setVisible(true);
			edit.setVisible(false);
			cancel.setVisible(false);
			addEditLabel.setText("Adding a Song");
			return;
		});
	}
		
	private void add(String sN, String aN, String aN2, int aY){
		//First Check if info exists in table
		song = sN;
		artist = aN;
		album = aN2;
		year = aY;
		
		//if the listView is already populated, check for conflicts
		if(counter > 0)
		{
			for (int i = 0; i < counter; i++)
			{
				if(song.toLowerCase().equals(songList.getItems().get(i).getSongName().toLowerCase())){
					//song names match
					if(artist.toLowerCase().equals(songList.getItems().get(i).getArtistName().toLowerCase())){
						//artist names match also, conflict error
						Alert alert = new Alert(AlertType.ERROR);
				        alert.setTitle("Input Error");
			        	alert.setHeaderText("This song/artist combination already exists.");
			        	alert.showAndWait();
			        	return;
					}
				}
			}
		}
		
		//info doesn't exist, add to table
		int i = 0;
		Song addedSong = new Song();
		if (albumYear.getText().isEmpty()){
			if (albumName.getText().isEmpty()){
				addedSong = new Song(song, artist);
				songList.getItems().add(addedSong);//empty year and albumName
				
				counter++;
				//songList.getSelectionModel().select(counter);
				Alert alert = new Alert(AlertType.CONFIRMATION);
		        alert.setTitle("Confirmation");
	        	alert.setHeaderText("This song/artist combination has been added to the table.");
	        	alert.showAndWait();
			}else{
				addedSong = new Song(song, artist, album);
				songList.getItems().add(addedSong);//empty year only
				counter++;
				//songList.getSelectionModel().select(counter);
				Alert alert = new Alert(AlertType.CONFIRMATION);
		        alert.setTitle("Confirmation");
	        	alert.setHeaderText("This song/artist combination has been added to the table.");
	        	alert.showAndWait();
			}
		}else if(albumYear.getText().isEmpty()==false){
			if(albumName.getText().isEmpty()){
				addedSong = new Song(song, artist, year);
				songList.getItems().add(addedSong);//empty albumName only
				counter++;
				//songList.getSelectionModel().select(counter);
				Alert alert = new Alert(AlertType.CONFIRMATION);
		        alert.setTitle("Confirmation");
	        	alert.setHeaderText("This song/artist combination has been added to the table.");
	        	alert.showAndWait();
			}else{
				addedSong = new Song(song, artist, album, year);
				songList.getItems().add(addedSong);//nothing empty
				counter++;
				//songList.getSelectionModel().select(counter);
				Alert alert = new Alert(AlertType.CONFIRMATION);
		        alert.setTitle("Confirmation");
	        	alert.setHeaderText("This song/artist combination has been added to the table.");
	        	alert.showAndWait();
			}
		}
		
		//--------------------------------------------
		//FROM STACKOVERFLOW
		// assuming there is a instance method Class.getScore that returns int
		// (other implementations for comparator could be used too, of course)
		int temp = counter-1;
		while(temp > 0){
			Song thisSong = new Song();
			
			if(temp-1 >= 0){
				int x = songList.getItems().get(temp).getSongName().compareToIgnoreCase(songList.getItems().get(temp-1).getSongName());
				if (x < 0){
					thisSong = songList.getItems().get(temp);
					songList.getItems().set(temp, songList.getItems().get(temp-1));
					songList.getItems().set(temp-1, thisSong);
				}
			}
			temp--;
		}
		//--------------------------------------------
		
		//if above works, then I should only need to sort 
		//on artist if there is a SONG CONFLICT
		temp = counter-1;
		while(temp > 0){
			Song thisSong = new Song();
			
			if(temp-1 >= 0 && songList.getItems().get(temp).getSongName().equalsIgnoreCase(songList.getItems().get(temp-1).getSongName())){
				int x = songList.getItems().get(temp).getArtistName().compareToIgnoreCase(songList.getItems().get(temp-1).getArtistName());
				if (x < 0){
					thisSong = songList.getItems().get(temp);
					songList.getItems().set(temp, songList.getItems().get(temp-1));
					songList.getItems().set(temp-1, thisSong);
				}
			}
			temp--;
		}
		
		i = songList.getItems().indexOf(addedSong);
		//make sure item inserted is selected
		songList.getSelectionModel().select(i);
		
		sortList = FXCollections.observableArrayList(
				"Song: " + songList.getSelectionModel().getSelectedItem().getSongName(),
				"Artist: " + songList.getSelectionModel().getSelectedItem().getArtistName(),
				"Album: " + songList.getSelectionModel().getSelectedItem().getAlbumName(),
				"Year: " + songList.getSelectionModel().getSelectedItem().getAlbumYear());
		
		songDetail.setItems(sortList);
		
	}

}
	
