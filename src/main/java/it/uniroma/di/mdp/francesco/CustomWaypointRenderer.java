package it.uniroma.di.mdp.francesco;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Renderer personalizzato per i waypoint sulla mappa.
 * Visualizza icone differenti in base al tipo di mezzo (bus, metro, tram, fermata)
 * e mostra un'etichetta sopra l'icona del waypoint.
 */
public class CustomWaypointRenderer implements WaypointRenderer<Waypoint> {
    /**
     * Mappa che associa il tipo di mezzo all'icona corrispondente.
     */
    private final Map<String, Icon> mappaIcon;

    /**
     * Costruttore della classe.
     * Inizializza la mappa delle icone per i diversi tipi di mezzi di trasporto.
     * Le icone vengono caricate dalle risorse del JAR (/resources/icons/).
     */
    public CustomWaypointRenderer() {
        mappaIcon = new HashMap<>();
        Icon iconBUS = new ImageIcon(getClass().getResource("/icons/iconBUS.png"));
        Icon iconMETRO = new ImageIcon(getClass().getResource("/icons/iconMETRO.png"));
        Icon iconTRAM = new ImageIcon(getClass().getResource("/icons/iconTRAM.png"));
        Icon iconFERMATA = new ImageIcon(getClass().getResource("/icons/iconFERMATA.png"));
        mappaIcon.put("0", iconTRAM);
        mappaIcon.put("1", iconMETRO);
        mappaIcon.put("3", iconBUS);
        mappaIcon.put("9", iconFERMATA);
    }

    /**
     * Disegna il waypoint sulla mappa.
     * Se il waypoint è un'istanza di {@link BusWaypoint}, viene selezionata l'icona in base al tipo di mezzo
     * e viene disegnata l'etichetta sopra l'icona.
     *
     * @param g        il contesto grafico su cui disegnare
     * @param map      la mappa su cui disegnare il waypoint
     * @param waypoint il waypoint da disegnare
     */
    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint waypoint) {
        Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());

        if (waypoint instanceof BusWaypoint cw) {
            Icon icon = mappaIcon.get(cw.getRouteType());
            int x = (int) point.getX() - icon.getIconWidth() / 2;
            int y = (int) point.getY() - icon.getIconHeight() / 2;
            icon.paintIcon(map, g, x, y);

            String label = cw.getLabel();
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(label);

            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString(label, x - textWidth / 2 + icon.getIconWidth() / 2, y - 5);
        }
    }
}