package hu.ait.android.minesweeper;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.shimmer.ShimmerFrameLayout;

public class MainActivity extends AppCompatActivity {
    private boolean tryMode = true;
    private ToggleButton tryFlagToggleButton;
    private MinesweeperView minesweeperView;
    private TextView tvStatusBottom;
    private LinearLayout layoutContent;
    private TextView tvStatusTop;
    private TextView minesLeft;

    public void setNoOfMinesLeft(String noOfMinesLeft) {
        this.noOfMinesLeft = noOfMinesLeft;
    }

    private String noOfMinesLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryFlagToggleButton = findViewById(R.id.tryFlagToggleButton);
        minesweeperView = findViewById(R.id.minesweeper);
        tvStatusBottom = findViewById(R.id.tvStatusBotton);
        tvStatusTop = findViewById(R.id.tvStatusTop);
        layoutContent = findViewById(R.id.layoutContent);
        noOfMinesLeft = minesweeperView.getNumberOfMinesLeft();
        minesLeft = findViewById(R.id.minesLeft);
        Button restartButton = findViewById(R.id.restartButton);
        ShimmerFrameLayout shimmerView = findViewById(R.id.shimmer_view);


        shimmerView.startShimmerAnimation();
        minesLeft.setText(getString(R.string.mines_left, noOfMinesLeft));

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minesweeperView.restartGame();
                tryFlagToggleButton.setChecked(false);
                tvStatusBottom.setVisibility(View.GONE);
                tvStatusTop.setVisibility(View.GONE);
                tryFlagToggleButton.setVisibility(View.VISIBLE);
                noOfMinesLeft = minesweeperView.getNumberOfMinesLeft();
                minesLeft.setText(getString(R.string.mines_left, noOfMinesLeft));

            }
        });

        tryFlagToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tryMode = !isChecked;
            }
        });

    }

    public void updateMinesLeft(){
        noOfMinesLeft = minesweeperView.getNumberOfMinesLeft();
        minesLeft.setText(getString(R.string.mines_left, noOfMinesLeft));
    }

    public boolean isTryMode() {
        return tryMode;
    }

    public void setTryMode(boolean tryMode) {
        this.tryMode = tryMode;
        tryFlagToggleButton.setChecked(tryMode);
    }

    public void showGameResult() {
        String gameResult = "";
        String gameResultTop = "";

        if (minesweeperView.hasFailed()) {
            gameResult = getString(R.string.game_over);
            gameResultTop = getString(R.string.sorry_you_lost_game_over);
            tvStatusBottom.setVisibility(View.VISIBLE);
            tvStatusTop.setVisibility(View.VISIBLE);
            tryFlagToggleButton.setVisibility(View.GONE);

        } else if (minesweeperView.hasWon()) {
            gameResult = getString(R.string.congratulations);
            gameResultTop = getString(R.string.congratulations_you_have_won);
            tvStatusTop.setVisibility(View.VISIBLE);
            tvStatusBottom.setVisibility(View.VISIBLE);
            tryFlagToggleButton.setVisibility(View.GONE);

        }

        tvStatusTop.setText(gameResultTop);
        tvStatusBottom.setText(gameResult);
        Snackbar.make(layoutContent, gameResult, Snackbar.LENGTH_LONG).show();

    }
}
