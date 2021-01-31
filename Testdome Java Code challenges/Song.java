import java.util.HashSet;
public class Song {
    private String name;
    private Song nextSong;
    private HashSet<String> songs_played = new HashSet<String>();
    private Song next_song;


    public Song(String name) {
        this.name = name;
    }

    public void setNextSong(Song nextSong) {
        this.nextSong = nextSong;
        this.next_song=nextSong;
    }


    public boolean isRepeatingPlaylist() {
        if (next_song == null){
            return false;
        }
        else if (songs_played.contains(next_song.name)){
            return true;
        }
        else{
            songs_played.add(next_song.name);
        }
        next_song = next_song.nextSong;
        return isRepeatingPlaylist();
    }

    public static void main(String[] args) {
        Song first = new Song("Hello");
        Song second = new Song("Eye of the tiger");
        first.setNextSong(second);
        second.setNextSong(first);
        System.out.println(first.isRepeatingPlaylist());
    }
}