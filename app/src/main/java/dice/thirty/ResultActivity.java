package dice.thirty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity
{
    private ListView listResult;
    private ArrayAdapter<GameResult> adapterResults;
    private List<GameResult> gameResults;

    int iTotalPoint;

    TextView txTotalPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        gameResults = new ArrayList<>();
        gameResults = getIntent().getParcelableArrayListExtra("listGameResult");

        iTotalPoint = getIntent().getIntExtra("totalPoint",0);

        // show the total point
        setTotalPoint(iTotalPoint);
        setupListView();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        this.finish();

        //go back to the Main Activity, and init the Main Activity
        Intent intent = new Intent();
        intent.setClass(ResultActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private void setupListView()
    {
        // look up a reference to the ListView object
        listResult = findViewById(R.id.result_list);

        // create an adapter
        adapterResults = new ResultArrayAdapter(this,R.layout.list_result_item,gameResults);

        // Set listView's adapter to the new adapter
        listResult.setAdapter(adapterResults);
    }

    private void setTotalPoint(int iTotalPoint)
    {
        txTotalPoint = findViewById(R.id.textTotalPoint);
        txTotalPoint.setText("Total point: "+iTotalPoint);
    }

}
