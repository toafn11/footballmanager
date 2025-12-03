/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

/**
 *
 * @author Minh Dep Trai
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

public class PictureBox extends JLayeredPane {

    public enum SizeMode {
        NORMAL,         // Hiển thị đúng size ảnh gốc
        CENTER,         // Canh giữa
        STRETCH,        // Kéo ảnh theo component (méo)
        ZOOM,           // Fit giữ tỉ lệ
        PANEL_CROP      // Crop cover (giống background CSS)
    }

    private ImageIcon image;
    private SizeMode sizeMode = SizeMode.ZOOM;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public PictureBox() {
        setPreferredSize(new Dimension(200, 200));
    }

    // PROPERTY: Image
    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        ImageIcon old = this.image;
        this.image = image;
        pcs.firePropertyChange("image", old, image);
        repaint();
    }

    // PROPERTY: SizeMode
    public SizeMode getSizeMode() {
        return sizeMode;
    }

    public void setSizeMode(SizeMode mode) {
        SizeMode old = this.sizeMode;
        this.sizeMode = mode;
        pcs.firePropertyChange("sizeMode", old, mode);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        Rectangle box = getBox();

        g2.drawImage(image.getImage(),
                box.x, box.y, box.width, box.height,
                null);
    }

    private Rectangle getBox() {
        int pw = getWidth();
        int ph = getHeight();
        int iw = image.getIconWidth();
        int ih = image.getIconHeight();

        switch (sizeMode) {

            case NORMAL -> {
                return new Rectangle(0, 0, iw, ih);
            }

            case CENTER -> {
                return new Rectangle(
                        (pw - iw) / 2,
                        (ph - ih) / 2,
                        iw, ih
                );
            }

            case STRETCH -> {
                return new Rectangle(0, 0, pw, ph);
            }

            case ZOOM -> {
                double xScale = (double) pw / iw;
                double yScale = (double) ph / ih;
                double scale = Math.min(xScale, yScale);
                int w = (int) (iw * scale);
                int h = (int) (ih * scale);
                return new Rectangle(
                        (pw - w) / 2,
                        (ph - h) / 2,
                        w, h
                );
            }

            case PANEL_CROP -> { // giống CSS background-size: cover
                double xScale = (double) pw / iw;
                double yScale = (double) ph / ih;
                double scale = Math.max(xScale, yScale);
                int w = (int) (iw * scale);
                int h = (int) (ih * scale);
                return new Rectangle(
                        (pw - w) / 2,
                        (ph - h) / 2,
                        w, h
                );
            }
        }

        return new Rectangle(0, 0, iw, ih);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
}
