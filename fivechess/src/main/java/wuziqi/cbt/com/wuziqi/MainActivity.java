package wuziqi.cbt.com.wuziqi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {
    private WuziqiPanel mGamePanel;
    private Builder alertBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        alertBuilder = new Builder(MainActivity.this);
        alertBuilder.setPositiveButton("Again", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGamePanel.restartGame();
            }
        });
        alertBuilder.setNegativeButton("Exit", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Game Over");

        mGamePanel = (WuziqiPanel) findViewById(R.id.id_wuziqi);
        mGamePanel.setOnGameStatusChangeListener(new OnGameStatusChangeListener() {
            @Override
            public void onGameOver(int gameWinResult) {
                switch (gameWinResult) {
                    case WuziqiPanel.WHITE_WIN:
                        alertBuilder.setMessage("White Victory!");
                        break;
                    case WuziqiPanel.BLACK_WIN:
                        alertBuilder.setMessage("Black Victory!");
                        break;
                    case WuziqiPanel.NO_WIN:
                        alertBuilder.setMessage("draw!");
                        break;
                }
                alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });
    }
}
