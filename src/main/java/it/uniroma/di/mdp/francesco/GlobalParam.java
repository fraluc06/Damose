package it.uniroma.di.mdp.francesco;

public class GlobalParam {
    private int mapZoom;

    public int getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(int mapZoom) {
        this.mapZoom = mapZoom;
    }

    GlobalParam() {
        mapZoom = 4;

    }
}

