package hu.ait.android.minesweeper;

import java.util.Random;

class MinesweeperModel {

    private static MinesweeperModel minesweeperModel = null;

    private MinesweeperModel() {
    }

    static MinesweeperModel getMinesweeperModelInstance() {
        if (minesweeperModel == null) {
            minesweeperModel = new MinesweeperModel();
            minesweeperModel.generateModels();
            minesweeperModel.randomizeMines();
            minesweeperModel.countNumberOfMinesAround();
        }
        return minesweeperModel;
    }

    private int numRows = 8;
    private int numColumns = 8;
    private int numMines = 5;
    private int numberOfFlags = 0;


    static final int EMPTY = 0;
    static final int FLAG = 1;
    static final int MINE = 2;
    static final int CHECKED = 3;

    private int[][] model = new int[numRows][numColumns];
    private int[][] minesAround = new int[numRows][numColumns];

    private void generateModels(){
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                model[row][column] = EMPTY;
                minesAround[row][column]=0;
            }
        }
    }


    int getNumRows() {
        return numRows;
    }

    int getNumColumns() {
        return numColumns;
    }

    int getMinesAround(int row, int column) {
        return minesAround[row][column];
    }

    int getFieldContents(int row, int column) {
        return model[row][column];
    }

    void setFieldContent(int row, int column, int content) {
        model[row][column] = content;
    }

    private void randomizeMines() {
        Random random = new Random();
        int row, column;
        boolean alreadyMine;

        for (int i = 0; i < numMines; i++) {
            do {
                row = random.nextInt(numRows);
                column = random.nextInt(numColumns);

                alreadyMine = getFieldContents(row, column) == 2;
            }
            while (alreadyMine);
            {
                setFieldContent(row, column, 2);
            }

        }
    }

    private void countNumberOfMinesAround() {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {

                if (getFieldContents(row, column) != 2) {
                    int numberOfMines = 0;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {

                            if (row + i >= 0 && row + i < numRows
                                    && column + j >= 0 && column + j < numColumns) {

                                if (getFieldContents(row + i, column + j) == 2) {
                                    numberOfMines++;

                                }
                            }
                        }
                    }
                    minesAround[row][column] = numberOfMines;
                }

            }
        }
    }

    boolean win() {
        numberOfFlags = 0;
        for (int row = 0; row < numRows; row++)
            for (int column = 0; column < numColumns; column++)
                if (getFieldContents(row,column) == 1) {
                    numberOfFlags++;

                }

        return numberOfFlags == numMines;
    }

    String getNumberOfMinesLeft() {
        return String.valueOf(numMines - numberOfFlags);
    }


    void resetGame() {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                MinesweeperModel.getMinesweeperModelInstance().setFieldContent(row, column, 0);
            }
        }
        MinesweeperModel.getMinesweeperModelInstance().randomizeMines();
        MinesweeperModel.getMinesweeperModelInstance().countNumberOfMinesAround();
        numberOfFlags = 0;
    }


}
