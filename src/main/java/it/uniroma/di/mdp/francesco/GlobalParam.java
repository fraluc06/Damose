package it.uniroma.di.mdp.francesco;

import java.nio.file.Path;

public class GlobalParam {
    private int mapZoom;
    private String fileURL;
    private String md5URL;
    private Path folderPath;

    public GlobalParam() {
        // valori di default
        this.mapZoom = 4;
        this.fileURL = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip";
        this.md5URL = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip.md5";
        this.folderPath = Path.of("rome_static_gtfs");
    }

    // getter e setter

    public int getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(int mapZoom) {
        this.mapZoom = mapZoom;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getMd5URL() {
        return md5URL;
    }

    public void setMd5URL(String md5URL) {
        this.md5URL = md5URL;
    }

    public Path getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(Path folderPath) {
        this.folderPath = folderPath;
    }
}
