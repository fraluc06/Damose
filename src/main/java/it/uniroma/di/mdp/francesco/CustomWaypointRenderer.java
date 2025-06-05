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
 * Permette di visualizzare icone diverse in base al tipo di mezzo (bus, metro, tram)
 * e di mostrare un'etichetta sopra l'icona.
 */
public class CustomWaypointRenderer implements WaypointRenderer<Waypoint> {
    private final Map<String, Icon> mappaIcon;

    /**
     * Costruttore. Inizializza la mappa delle icone per i diversi tipi di mezzi.
     */
    public CustomWaypointRenderer() {
        mappaIcon = new HashMap<>();
        Icon iconBUS = new ImageIcon("./icon/iconBUS.png");
        Icon iconMETRO = new ImageIcon("./icon/iconMETRO.png");
        Icon iconTRAM = new ImageIcon("./icon/iconTRAM.png");
        Icon iconFERMATA = new ImageIcon("./icon/iconFERMATA.png");
        mappaIcon.put("0", iconTRAM);
        mappaIcon.put("1", iconMETRO);
        mappaIcon.put("3", iconBUS);
        mappaIcon.put("9", iconFERMATA);
    }

    /**
     * Disegna il waypoint sulla mappa, mostrando l'icona corrispondente al tipo di mezzo
     * e l'etichetta associata.
     *
     * @param g        il contesto grafico
     * @param map      la mappa su cui disegnare
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