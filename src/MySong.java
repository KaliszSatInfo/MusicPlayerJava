public class MySong {
    private String path;
    private String name;
    private int playlistID;

    public MySong(String path, String name, int playlistID) {
        this.path = path;
        this.name = name;
        this.playlistID = playlistID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }
}
