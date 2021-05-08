package com.sothatsit.royalur.analysis.ui;

import com.sothatsit.royalur.RoyalUrAnalysis;
import com.sothatsit.royalur.ai.PandaAgent;
import com.sothatsit.royalur.ai.utility.PrioritiseCenterUtilityFn;
import com.sothatsit.royalur.analysis.reporting.ReportFormatter;
import com.sothatsit.royalur.analysis.reporting.TableGenerator;
import com.sothatsit.royalur.simulation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * A window that can be used to analyse positions
 * in The Royal Game of Ur.
 *
 * @author Paddy Lamont
 */
public class AnalysisWindow implements MouseListener {

    private final Color TILE_COLOUR = new Color(152, 201, 237);
    private final Color ROSETTE_COLOUR = new Color(237, 168, 152);

    private final JFrame frame;
    private final BufferedCanvas canvas;

    private final int canvasWidth;
    private final int canvasHeight;
    private final float fontSize;
    private final float titleFontSize;
    private final float legendFontSize;
    private final int textMargin;
    private final int tileWidth;
    private final int pieceWidth;
    private final Rectangle board;

    private final Rectangle lightTilesRect;
    private final Rectangle lightScoreRect;
    private final Rectangle darkTilesRect;
    private final Rectangle darkScoreRect;
    private final Rectangle turnRect;
    private final Rectangle rollRect;
    private final Rectangle resetRect;
    private final Rectangle analyseRect;

    private final Game game;
    private int roll;

    public AnalysisWindow() {
        canvasWidth = 920;
        canvasHeight = 680;
        textMargin = 15;
        fontSize = 30;
        titleFontSize = 40;
        legendFontSize = 35;
        tileWidth = 80;
        pieceWidth = 60;

        int boardWidth = 8 * tileWidth;
        int boardHeight = 3 * tileWidth;
        int boardX = (canvasWidth - boardWidth) / 2;
        int boardY = (canvasHeight - boardHeight) / 2;
        board = new Rectangle(boardX, boardY, boardWidth, boardHeight);

        lightTilesRect = new Rectangle(0, 60, 200, 50);
        lightScoreRect = new Rectangle(0, 110, 200, 50);
        darkTilesRect = new Rectangle(0, canvasHeight - 110, 200, 50);
        darkScoreRect = new Rectangle(0, canvasHeight - 160, 200, 50);
        turnRect = new Rectangle(canvasWidth / 2 - 100, 0, 200, 110);
        rollRect = new Rectangle(canvasWidth - 200, 0, 200, 110);
        resetRect = new Rectangle(canvasWidth - 200, canvasHeight - 60, 200, 60);
        analyseRect = new Rectangle(canvasWidth / 2 - 100, canvasHeight - 60, 200, 60);

        roll = 2;
        game = new Game();
        game.setState(GameState.LIGHT_TURN);
        game.light.tiles = 4;
        game.dark.tiles = 4;
        game.board.set(0, 0, Tile.LIGHT);
        game.board.set(0, 1, Tile.LIGHT);
        game.board.set(2, 0, Tile.DARK);
        game.board.set(2, 2, Tile.DARK);
        game.board.set(1, 1, Tile.DARK);
        game.board.set(1, 7, Tile.LIGHT);

        frame = new JFrame(RoyalUrAnalysis.TITLE);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new BufferedCanvas(canvasWidth, canvasHeight);
        canvas.addMouseListener(this);
        frame.setContentPane(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        repaint();
    }

    public int[] convertBoardToScreen(int x, int y) {
        return new int[] {
                board.x + y * tileWidth,
                board.y + (2 - x) * tileWidth
        };
    }

    public int[] convertScreenToBoard(int x, int y) {
        if (!board.contains(x, y))
            return null;

        return new int[] {
                2 - (y - board.y) / tileWidth,
                (x - board.x) / tileWidth
        };
    }

    public void repaint() {
        Graphics2D g = canvas.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.clearRect(0, 0, canvasWidth, canvasHeight);
        drawBoard(g);

        Font normalFont = g.getFont().deriveFont(fontSize).deriveFont(Font.PLAIN);
        Font titleFont = g.getFont().deriveFont(titleFontSize).deriveFont(Font.BOLD);

        g.setColor(Color.BLACK);
        drawText(g, titleFont, textMargin, 35, "Light");
        drawText(g, titleFont, textMargin, canvasHeight - 35, "Dark");
        drawText(g, normalFont, textMargin, 85, game.light.tiles + " tiles to play");
        drawText(g, normalFont, textMargin, canvasHeight - 85, game.dark.tiles + " tiles to play");
        drawText(g, normalFont, textMargin, 130, game.light.score + " tiles scored");
        drawText(g, normalFont, textMargin, canvasHeight - 130, game.dark.score + " tiles scored");

        drawTextAlignRight(g, titleFont, canvasWidth - textMargin, 35, "Roll");
        drawTextAlignRight(g, normalFont, canvasWidth - textMargin, 85, Integer.toString(roll));
        drawTextAlignRight(g, titleFont, canvasWidth - textMargin, canvasHeight - 35, "Reset");

        String turn = (game.state.isLightActive ? "Light's Turn" : "Dark's Turn");
        drawTextCentered(g, titleFont, canvasWidth / 2, 35, turn);
        drawTextCentered(g, titleFont, canvasWidth / 2, canvasHeight - 35, "Analyse");

        canvas.repaint();
        frame.repaint();
    }

    /** Draws text that is centered vertically on y. **/
    private void drawText(Graphics2D g, Font font, int x, int y, String text) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        y += metrics.getAscent() / 2;
        g.drawString(text, x, y);
    }

    /** Draws text that is centered vertically on y. **/
    private void drawTextAlignRight(Graphics2D g, Font font, int x, int y, String text) {
        FontMetrics metrics = g.getFontMetrics(font);
        x -= metrics.stringWidth(text);
        y += metrics.getAscent() / 2;
        g.setFont(font);
        g.drawString(text, x, y);
    }

    /** Draws text that is centered vertically on y. **/
    private void drawTextCentered(Graphics2D g, Font font, int x, int y, String text) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        x -= metrics.stringWidth(text) / 2;
        y += metrics.getAscent() / 2;
        g.drawString(text, x, y);
    }

    public void drawBoard(Graphics2D g) {
        // Draw all of the spaces on the board.
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 8; ++y) {
                if (!Tile.isOnBoard(x, y))
                    continue;

                int[] screen = convertBoardToScreen(x, y);
                boolean isRosette = Tile.isRosette(x, y);

                // Draw the background of the tile.
                g.setColor(isRosette ? ROSETTE_COLOUR : TILE_COLOUR);
                g.fillRect(screen[0], screen[1], tileWidth, tileWidth);

                // Draw the outline.
                g.setColor(Color.BLACK);
                g.setStroke(new BasicStroke(2));
                g.drawRect(screen[0], screen[1], tileWidth, tileWidth);

                // Draw the cross for rosette tiles.
                if (isRosette) {
                    g.drawLine(screen[0], screen[1], screen[0] + tileWidth, screen[1] + tileWidth);
                    g.drawLine(screen[0] + tileWidth, screen[1], screen[0], screen[1] + tileWidth);
                }

                // If there is a piece on the board, draw it.
                int piece = game.board.get(x, y);
                if (piece != Tile.EMPTY) {
                    Color colour = (piece == Tile.LIGHT ? Color.WHITE : Color.BLACK);
                    drawPiece(g, screen[0] + tileWidth / 2, screen[1] + tileWidth / 2, colour);
                }
            }
        }

        // Draw the legend for the board.
        Font legendFont = g.getFont().deriveFont(legendFontSize).deriveFont(Font.PLAIN);
        g.setColor(Color.GRAY);
        for (int x = 0; x < 3; ++x) {
            int[] location = convertBoardToScreen(x, -1);
            int screenX = location[0] + 2 * tileWidth / 3;
            int screenY = location[1] + tileWidth / 2;
            drawTextCentered(g, legendFont, screenX, screenY, "" + (char) ('A' + x));
        }
        for (int y = 0; y < 8; ++y) {
            int[] location = convertBoardToScreen((!Tile.isOnBoard(0, y) ? 0 : -1), y);
            int screenX = location[0] + tileWidth / 2;
            int screenY = location[1] + tileWidth / 3;
            drawTextCentered(g, legendFont, screenX, screenY, "" + (1 + y));
        }
    }

    /**
     * @param x the center screen X coordinate for the piece.
     * @param y the center screen Y coordinate for the piece.
     */
    public void drawPiece(Graphics2D g, int x, int y, Color colour) {
        // Draw the piece's background colour.
        g.setColor(colour);
        g.fillOval(x - pieceWidth / 2, y - pieceWidth / 2, pieceWidth, pieceWidth);

        // Draw the outline of the piece.
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawOval(x - pieceWidth / 2, y - pieceWidth / 2, pieceWidth, pieceWidth);
    }

    public void toggleTile(int x, int y, boolean backwards) {
        if (!Tile.isOnBoard(x, y))
            return;

        int tile = game.board.get(x, y);
        if (x == Tile.LIGHT_SAFE_X) {
            game.board.set(x, y, (tile == Tile.EMPTY ? Tile.LIGHT : Tile.EMPTY));
        } else if (x == Tile.DARK_SAFE_X) {
            game.board.set(x, y, (tile == Tile.EMPTY ? Tile.DARK : Tile.EMPTY));
        } else {
            game.board.set(x, y, (tile + (backwards ? 1 : -1) + 3) % 3);
        }
    }

    public void analyse() {
        // Use the same AI used for the RoyalUr.net Panda difficulty.
        Agent agent = new PandaAgent(new PrioritiseCenterUtilityFn(4.0f), 7, 2);

        Map<Pos, Float> scores = agent.scoreMoves(game, roll);
        StringBuilder builder = new StringBuilder("<html>");

        if (!scores.isEmpty()) {
            builder.append("<table>");

            List<Map.Entry<Pos, Float>> sortedMoves = new ArrayList<>(scores.entrySet());
            sortedMoves.sort(Map.Entry.comparingByValue());
            for (Map.Entry<Pos, Float> move : scores.entrySet()) {
                builder.append("<tr>");

                double score = move.getValue();
                String scoreStr = (score > 0 ? "+" : "") + ReportFormatter.format2DP(score);
                builder.append("<td><b>").append(move.getKey()).append("&nbsp;&nbsp;&nbsp;&nbsp;</b></td>");
                builder.append("<td>").append(scoreStr).append("</td>");

                builder.append("</tr>");
            }

            builder.append("</table>");
        } else {
            builder.append("<b>No moves</b>");
        }

        // Display a window with the results.
        JFrame frame = new JFrame("Panda Analysis");
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(builder.toString());
        label.setFont(label.getFont().deriveFont(fontSize).deriveFont(Font.PLAIN));
        label.setForeground(Color.BLACK);
        panel.add(label);
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(this.frame);
        frame.setVisible(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        boolean reverse = SwingUtilities.isRightMouseButton(e);

        int[] boardPos = convertScreenToBoard(e.getX(), e.getY());
        if (boardPos != null) {
            toggleTile(boardPos[0], boardPos[1], reverse);
        }

        Point mouse = e.getPoint();
        if (lightTilesRect.contains(mouse)) {
            game.light.tiles = (game.light.tiles + (reverse ? -1 : 1) + 7) % 7;
        } else if (lightScoreRect.contains(mouse)) {
            game.light.score = (game.light.score + (reverse ? -1 : 1) + 7) % 7;
        } else if (darkTilesRect.contains(mouse)) {
            game.dark.tiles = (game.dark.tiles + (reverse ? -1 : 1) + 7) % 7;
        } else if (darkScoreRect.contains(mouse)) {
            game.dark.score = (game.dark.score + (reverse ? -1 : 1) + 7) % 7;
        } else if (turnRect.contains(mouse)) {
            game.state = (game.state.isLightActive ? GameState.DARK_TURN : GameState.LIGHT_TURN);
        } else if (rollRect.contains(mouse)) {
            roll = (roll + (reverse ? -1 : 1) + 5) % 5;
        } else if (resetRect.contains(mouse)) {
            game.reset();
        } else if (analyseRect.contains(mouse)) {
            analyse();
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
