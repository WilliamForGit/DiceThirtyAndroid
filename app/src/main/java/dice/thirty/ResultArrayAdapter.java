package dice.thirty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

//for the special array adapter to the ListView
public class ResultArrayAdapter extends ArrayAdapter<GameResult>
{

    public ResultArrayAdapter(Context con, int res, List<GameResult> listGR)
    {
        super(con,res,listGR);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = LayoutInflater.from(super.getContext()).inflate(R.layout.list_result_item,null,false);

        TextView tvRoundNum = (TextView) v.findViewById(R.id.tvRoundNumber);
        TextView tvCombNumber = (TextView) v.findViewById(R.id.tvCombinationNumber);
        TextView tvRPoint = (TextView) v.findViewById(R.id.tvRoundPoint);

        GameResult gResult = super.getItem(position);
        tvRoundNum.setText("    "+gResult.getRoundNumber()+" ");
        tvCombNumber.setText("    "+gResult.getCombinationNumber()+" ");
        tvRPoint.setText("    "+gResult.getRoundPoint());

        return v;

    }
}
