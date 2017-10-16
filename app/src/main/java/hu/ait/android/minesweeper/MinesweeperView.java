package hu.ait.android.minesweeper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MinesweeperView extends View {

    private Bitmap bitmapFlag;
    private Bitmap bitmapBomb;
    private Paint paintBackground;
    private Paint normalPaintLine;
    private Paint numberMinesPaintLine;

    private int numRows = MinesweeperModel.getMinesweeperModelInstance().getNumRows();
    private int numColumns = MinesweeperModel.getMinesweeperModelInstance().getNumColumns();
    private boolean failed;
    private boolean won;
    private String numberOfMinesLeft;
    private boolean touchEnable;

    public boolean isTouchEnable() {
        return touchEnable;
    }

    public void setTouchEnable(boolean touchEnable) {
        this.touchEnable = touchEnable;
    }


    public String getNumberOfMinesLeft() {
        return numberOfMinesLeft;
    }

    public boolean hasFailed() {
        return failed;
    }

    public boolean hasWon() {
        return won;
    }


    public MinesweeperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        touchEnable = true;
        failed = false;
        won = false;
        numberOfMinesLeft = MinesweeperModel.getMinesweeperModelInstance().getNumberOfMinesLeft();

        paintBackground = new Paint();
        paintBackground.setColor(Color.LTGRAY);
        paintBackground.setStyle(Paint.Style.FILL);

        normalPaintLine = new Paint();
        normalPaintLine.setColor(Color.WHITE);
        normalPaintLine.setStyle(Paint.Style.STROKE);
        normalPaintLine.setStrokeWidth(5);

        numberMinesPaintLine = new Paint();
        numberMinesPaintLine.setColor(Color.BLUE);
        numberMinesPaintLine.setTextSize(60);
        numberMinesPaintLine.setStyle(Paint.Style.STROKE);
        numberMinesPaintLine.setStrokeWidth(7);


        bitmapFlag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
        bitmapBomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBackground);

        drawGameArea(canvas);

        drawFlags(canvas);
        showCheckedAroundForMines(canvas);

        if (failed) {
            showMines(canvas);
            ((MainActivity) getContext()).showGameResult();
        }
        if(won){
            ((MainActivity) getContext()).showGameResult();
        }

    }

    private void drawGameArea(Canvas canvas) {

        canvas.drawRect(0, 0, getWidth(), getHeight(), normalPaintLine);
        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(i * (getWidth() / numRows), 0, i * (getWidth() / numRows), getHeight(), normalPaintLine);
        }

        for (int j = 1; j < numColumns; j++) {
            canvas.drawLine(0, j * (getHeight() / numColumns), getWidth(), j * (getHeight() / numColumns), normalPaintLine);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isTouchEnable()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                int tX = ((int) event.getX()) / (getWidth() / numRows);
                int tY = ((int) event.getY()) / (getHeight() / numColumns);

                MinesweeperModel currentMinesweeperModelInstance = MinesweeperModel.getMinesweeperModelInstance();

                if (((MainActivity) getContext()).isTryMode()) {

                    if (currentMinesweeperModelInstance.getFieldContents(tX, tY) == MinesweeperModel.MINE) {
                        failed = true;
                        setTouchEnable(false);

                    } else if (currentMinesweeperModelInstance.getFieldContents(tX, tY) == MinesweeperModel.EMPTY) {
                        currentMinesweeperModelInstance.setFieldContent(tX, tY, 3);
                    }

                } else {
                    ((MainActivity) getContext()).setTryMode(false);
                    if (currentMinesweeperModelInstance.getFieldContents(tX, tY) == MinesweeperModel.MINE) {
                        currentMinesweeperModelInstance.setFieldContent(tX, tY, 1);
                        if (currentMinesweeperModelInstance.win()) {
                            won = true;
                            setTouchEnable(false);
                        }

                    } else {
                        failed = true;
                        setTouchEnable(false);

                    }
                }
                numberOfMinesLeft = currentMinesweeperModelInstance.getNumberOfMinesLeft();
                ((MainActivity) getContext()).updateMinesLeft();

                invalidate();
                return true;
            }
            return super.onTouchEvent(event);
        }
        else {
            return false;
        }
    }

    public void restartGame() {

        MinesweeperModel.getMinesweeperModelInstance().resetGame();
        failed = false;
        won = false;
        touchEnable = true;
        numberOfMinesLeft = MinesweeperModel.getMinesweeperModelInstance().getNumberOfMinesLeft();
        ((MainActivity) getContext()).setNoOfMinesLeft(numberOfMinesLeft);
        invalidate();

    }

    private void drawFlags(Canvas canvas) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (MinesweeperModel.getMinesweeperModelInstance().getFieldContents(i, j)
                        == MinesweeperModel.FLAG) {
                    canvas.drawBitmap
                            (bitmapFlag,
                                    null,
                                    new Rect(i * getWidth() / numRows, j * getHeight() / numColumns,
                                            (i + 1) * getWidth() / numRows, (j + 1) * getHeight() / numColumns),
                                    null);

                }
            }
        }
    }

    private void showCheckedAroundForMines(Canvas canvas) {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                if (MinesweeperModel.getMinesweeperModelInstance().getFieldContents(row, column)
                        == MinesweeperModel.CHECKED) {
                    String numberOfMines = String.valueOf(MinesweeperModel.getMinesweeperModelInstance().getMinesAround(row, column));
                    canvas.drawText(numberOfMines,
                            row * getWidth() / numRows + getWidth() / (numRows * 2) - getWidth() / (numColumns * 5),
                            column * getHeight() / numColumns + getHeight() / (numColumns * 2) + getHeight() / (numColumns * 4),
                            numberMinesPaintLine);
                }
            }
        }
    }

    public void showMines(Canvas canvas) {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                if (MinesweeperModel.getMinesweeperModelInstance().getFieldContents(row, column) == 2) {
                    canvas.drawBitmap
                            (bitmapBomb,
                                    null,
                                    new Rect(row * getWidth() / numRows, column * getHeight() / numColumns,
                                            (row + 1) * getWidth() / numRows, (column + 1) * getHeight() / numColumns),
                                    null);

                } else {
                    canvas.drawText("",
                            row * getWidth() / numRows + getWidth() / (numRows * 2) - getWidth() / (numColumns * 5),
                            column * getHeight() / numColumns + getHeight() / (numColumns * 2) + getHeight() / (numColumns * 4),
                            numberMinesPaintLine);
                }
            }
        }
    }
}
