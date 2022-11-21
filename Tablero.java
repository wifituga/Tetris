package Tetris;

import javax.swing.*;
import Tetris.Forma.Tetromino;

import java.awt.*;
import java.awt.event.*;

public class Tablero extends JPanel {

    private final int anchTablero = 10;
    private final int altoTablero = 18;
    private final int pIntervalo = 300;

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean Pausado = false;
    private int linBorradas = 0;
    private int curX = 0;
    private int curY = 0;
    private JLabel statusbar;
    private Forma curPiece;
    private Tetromino[] Tablero;

    public Tablero(Tetris parent) {
        iniTablero(parent);
    }

    private void iniTablero(Tetris parent) {
        setFocusable(true);
        statusbar = parent.getStatusBar();
        addKeyListener(new TAdapter());
    }

    private int anchCuadrado() {
        return (int) getSize().getWidth() / anchTablero;
    }

    private int altoCuadrado() {
        return (int) getSize().getHeight() / altoTablero;
    }

    private Tetromino FormaAt(int x, int y) {
        return Tablero[(y * anchTablero) + x];
    }

    void start() {
        curPiece = new Forma();
        Tablero = new Tetromino[anchTablero * altoTablero];

        limpiarTablero();
        nuevaPieza();

        timer = new Timer(pIntervalo, new GameCycle());
        timer.start();
    }

    private void pausa() {
        Pausado = !Pausado;
        if (Pausado) {
            statusbar.setText("Pausado");
        } else {
            statusbar.setText(String.valueOf(linBorradas));
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        var size = getSize();
        int TableroTop = (int) size.getHeight() - altoTablero * altoCuadrado();

        for (int i = 0; i < altoTablero; i++) {
            for (int j = 0; j < anchTablero; j++) {
                Tetromino Forma = FormaAt(j, altoTablero - i - 1);
                if (Forma != Tetromino.NoForma) {
                    drawSquare(g, j * anchCuadrado(),
                            TableroTop + i * altoCuadrado(), Forma);
                }
            }
        }

        if (curPiece.getForma() != Tetromino.NoForma) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, x * anchCuadrado(),
                        TableroTop + (altoTablero - y - 1) * altoCuadrado(),
                        curPiece.getForma());
            }
        }
    }

    private void dropDown() {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    private void limpiarTablero() {
        for (int i = 0; i < altoTablero * anchTablero; i++) {
            Tablero[i] = Tetromino.NoForma;
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            Tablero[(y * anchTablero) + x] = curPiece.getForma();
        }
        removeFullLines();
        if (!isFallingFinished) {
            nuevaPieza();
        }
    }

    private void nuevaPieza() {
        curPiece.setRandomForma();
        curX = anchTablero / 2 + 1;
        curY = altoTablero - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setForma(Tetromino.NoForma);
            timer.stop();

            var msg = String.format("Perdiste. Puntuación: %d", linBorradas);
            statusbar.setText(msg);
        }
    }

    private boolean tryMove(Forma nuevaPieza, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + nuevaPieza.x(i);
            int y = newY - nuevaPieza.y(i);

            if (x < 0 || x >= anchTablero || y < 0 || y >= altoTablero) {
                return false;
            }
            if (FormaAt(x, y) != Tetromino.NoForma) {
                return false;
            }
        }
        curPiece = nuevaPieza;
        curX = newX;
        curY = newY;

        repaint();
        return true;
    }

    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = altoTablero - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < anchTablero; j++) {
                if (FormaAt(j, i) == Tetromino.NoForma) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < altoTablero - 1; k++) {
                    for (int j = 0; j < anchTablero; j++) {
                        Tablero[(k * anchTablero) + j] = FormaAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {

            linBorradas += numFullLines;

            statusbar.setText(String.valueOf(linBorradas));
            isFallingFinished = true;
            curPiece.setForma(Tetromino.NoForma);
        }
    }

    private void drawSquare(Graphics g, int x, int y, Tetromino Forma) {

        Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0)
        };

        var color = colors[Forma.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, anchCuadrado() - 2, altoCuadrado() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + altoCuadrado() - 1, x, y);
        g.drawLine(x, y, x + anchCuadrado() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + altoCuadrado() - 1,
                x + anchCuadrado() - 1, y + altoCuadrado() - 1);
        g.drawLine(x + anchCuadrado() - 1, y + altoCuadrado() - 1,
                x + anchCuadrado() - 1, y + 1);
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private void doGameCycle() {
        update();
        repaint();
    }

    private void update() {
        if (Pausado) {
            return;
        }

        if (isFallingFinished) {
            isFallingFinished = false;
            nuevaPieza();
        } else {
            oneLineDown();
        }
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (curPiece.getForma() == Tetromino.NoForma) {
                return;
            }

            int keycode = e.getKeyCode();

            // Java 12 switch expressions
            switch (keycode) {

                case KeyEvent.VK_P -> pausa();
                case KeyEvent.VK_LEFT -> tryMove(curPiece, curX - 1, curY);
                case KeyEvent.VK_RIGHT -> tryMove(curPiece, curX + 1, curY);
                case KeyEvent.VK_DOWN -> tryMove(curPiece.rotarDerecha(), curX, curY);
                case KeyEvent.VK_UP -> tryMove(curPiece.rotarIzquierda(), curX, curY);
                case KeyEvent.VK_SPACE -> dropDown();
                case KeyEvent.VK_D -> oneLineDown();
            }
        }
    }
}
