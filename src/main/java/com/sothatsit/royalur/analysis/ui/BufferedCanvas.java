package com.sothatsit.royalur.analysis.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * A canvas that buffers the modifications you make to it
 * so that it can update the canvas at once without flickering.
 *
 * @author Paddy Lamont
 */
public class BufferedCanvas extends JPanel {

    private final Consumer<Graphics2D> repaintFn;

    public BufferedCanvas(int width, int height, Consumer<Graphics2D> repaintFn) {
        setPreferredSize(new Dimension(width, height));
        this.repaintFn = repaintFn;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        try {
            repaintFn.accept(graphics);
        } finally {
            graphics.dispose();
        }
    }
}
