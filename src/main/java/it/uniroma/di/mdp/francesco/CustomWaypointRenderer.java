package it.uniroma.di.mdp.francesco;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;
import org.jxmapviewer.viewer.Waypoint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class CustomWaypointRenderer implements WaypointRenderer<Waypoint> {
    //private final Icon icon;
    private  Map<String, Icon > mappaIcon;
    public CustomWaypointRenderer(Icon icon) {
        //this.icon = icon;
        // crea una mappa per le differenti icone
        mappaIcon = new HashMap<>();
        Icon iconBUS = new ImageIcon("./icon/iconBUS.png");
        Icon iconMETRO = new ImageIcon("./icon/iconMETRO.png");
        Icon iconTRAM = new ImageIcon("./icon/iconTRAM.png");
        mappaIcon.put("0", iconTRAM);
        mappaIcon.put("1",iconMETRO);
        mappaIcon.put("3", iconBUS);
        //mappaTypeDesc.put("2", "Treno");

    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint waypoint) {
        // disegna l'icona
        Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());


        // e disegna anche il testo
        if (waypoint instanceof BusWaypoint cw) {

            Icon icon = mappaIcon.get(cw.getRouteType());
            //get( (cw.getRouteType() ); // prende l'icona in base al route type
            int x = (int) point.getX() - icon.getIconWidth() / 2;
            int y = (int) point.getY() - icon.getIconHeight() / 2;
            icon.paintIcon(map, g, x, y);



            String label = cw.getLabel();
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(label);

            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString(label, x - textWidth / 2 + icon.getIconWidth() / 2, y - 5); // sopra l'icona
        }
    }
}

