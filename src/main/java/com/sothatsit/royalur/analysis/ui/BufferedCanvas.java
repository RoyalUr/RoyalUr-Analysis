package com.sothatsit.royalur.analysis.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A canvas that buffers the modifications you make to it
 * so that it can update the canvas at once without flickering.
 *
 * @author Paddy Lamont
 */
public class BufferedCanvas extends JPanel {

    private final BufferedImage image;
    private final Graphics2D graphics;

    public BufferedCanvas(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.graphics = image.createGraphics();
        graphics.setBackground(Color.WHITE);
    }
    public Graphics2D getGraphics() {
        return graphics;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
