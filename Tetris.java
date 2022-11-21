package Tetris;

import java.awt.*;
import javax.swing.*;

public class Tetris extends JFrame {

    private JLabel statusbar;

    public Tetris() {
        initUI();
    }

    private void initUI() {
        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);

        var tablero = new Tablero(this);
        add(tablero);
        tablero.start();

        setTitle("Tetris | Grupo 3 - POO");
        setSize(400, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    JLabel getStatusBar() {
        return statusbar;
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            var juego = new Tetris();
            juego.setVisible(true);
        });
    }
}
