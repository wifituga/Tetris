package Tetris;

import java.util.Random;

public class Forma {

    protected enum Tetromino {
        NoForma, ZForma, SForma, LineForma,
        TForma, CuadradoForma, LForma, MirroredLForma
    }

    private Tetromino piezaForma;
    private int coord[][];
    private int[][][] coordTablero;

    public Forma() {

        initForma();
    }

    private void initForma() {
        coord = new int[4][2];
        coordTablero = new int[][][] {
                { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },
                { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } },
                { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },
                { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },
                { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } },
                { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
                { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
                { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }
        };
        setForma(Tetromino.NoForma);
    }

    protected void setForma(Tetromino Forma) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; ++j) {
                coord[i][j] = coordTablero[Forma.ordinal()][i][j];
            }
        }
        piezaForma = Forma;
    }

    private void setX(int index, int x) {
        coord[index][0] = x;
    }

    private void setY(int index, int y) {
        coord[index][1] = y;
    }

    public int x(int index) {
        return coord[index][0];
    }

    public int y(int index) {
        return coord[index][1];
    }

    public Tetromino getForma() {
        return piezaForma;
    }

    public void setRandomForma() {

        var r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;

        Tetromino[] values = Tetromino.values();
        setForma(values[x]);
    }

    public int minX() {
        int m = coord[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coord[i][0]);
        }
        return m;
    }

    public int minY() {
        int m = coord[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coord[i][1]);
        }
        return m;
    }

    public Forma rotarIzquierda() {
        if (piezaForma == Tetromino.CuadradoForma) {
            return this;
        }

        var resultado = new Forma();
        resultado.piezaForma = piezaForma;

        for (int i = 0; i < 4; ++i) {
            resultado.setX(i, y(i));
            resultado.setY(i, -x(i));
        }
        return resultado;
    }

    public Forma rotarDerecha() {
        if (piezaForma == Tetromino.CuadradoForma) {
            return this;
        }

        var resultado = new Forma();
        resultado.piezaForma = piezaForma;

        for (int i = 0; i < 4; ++i) {
            resultado.setX(i, -y(i));
            resultado.setY(i, x(i));
        }
        return resultado;
    }
}
