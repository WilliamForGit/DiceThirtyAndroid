package dice.thirty;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Spinner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<Dice> dice = new ArrayList<Dice>();
    //init 6 Dice objects
    Dice die1;
    Dice die2;
    Dice die3;
    Dice die4;
    Dice die5;
    Dice die6;

    ArrayList<GameResult> gameResults = new ArrayList<>(); // use for transfer all rounds results to the ResultActivity

    private Spinner spinnerNumber;
    ArrayAdapter<String> adapterNumber;

    ArrayList<String> listNumber  = new ArrayList<>();  // the list for all numbers for combination

    TextView textRound;   //to show the current the number of this round and how many points
    TextView textTotalPoint;   //to show the total points of all rounds

    int numberCount;  // the number for the combination
    int roundPoints=0;  // points for the current round
    int totalPoint=0;   //count all points for 10 rounds
    int roundCheck;  // check 3 times for one round    //2019-9-06

    Button btRun;
    Button btCheck;
    Button btNext;

    // check for 10 rounds
    int roundNumber=0;
    int currentRound=1;
    boolean combinationSelected;

    private final static String TAG="System.out.println";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_main);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.activity_main_land);
        }
        */

        findViews();
        if (savedInstanceState == null) {
            die1 = new Dice();
            die2 = new Dice();
            die3 = new Dice();
            die4 = new Dice();
            die5 = new Dice();
            die6 = new Dice();

            initDiceImages();
            initDices();

            LockDices(true);   //lock dices at the first time
            diceListener();   // set dices of choosed , if a dice has been choosed , the dice will be red, or the opposite
        } else {
            roundCheck = savedInstanceState.getInt("roundCheck");
            if (roundCheck == 3) {
                btRun.setEnabled(false);
                spinnerNumber.setEnabled(true);
            }
        }

        // init all numbers for the Spinner at the first time
        if (roundNumber == 0) {
            initNumbers();
        }
        setSpinnerAdapter(listNumber);   // set adapter resource to the Spinner
        spinnerNumberListener();  //set the listener event to the Spinner of combinations

        showRoundAndTotalPoint(currentRound, roundPoints, totalPoint);  // for the first round

        //2019-09-10
        btCheck.setEnabled(false);
        btNext.setEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt("roundNumber",roundNumber);
        outState.putInt("currentRound",currentRound);
        outState.putInt("roundPoints",roundPoints);
        outState.putInt("totalPoint",totalPoint);
        outState.putInt("numberCount",numberCount);
        outState.putInt("roundCheck",roundCheck);
        outState.putBoolean("combinationSelected",combinationSelected);

        outState.putStringArrayList("lisNumber",listNumber);

        outState.putParcelableArrayList("diceList",dice);
        outState.putParcelableArrayList("gameResults",gameResults);
    }

    @Override
    protected void onRestoreInstanceState(Bundle onRestoreBundle)
    {
        super.onRestoreInstanceState(onRestoreBundle);
        roundNumber = onRestoreBundle.getInt("roundNumber");
        currentRound = onRestoreBundle.getInt("currentRound");
        roundPoints = onRestoreBundle.getInt("roundPoints");
        totalPoint = onRestoreBundle.getInt("totalPoint");
        showRoundAndTotalPoint(currentRound,roundPoints,totalPoint);

        combinationSelected = onRestoreBundle.getBoolean("combinationSelected");
        numberCount = onRestoreBundle.getInt("numberCount");
        listNumber = onRestoreBundle.getStringArrayList("lisNumber");
        restoreCombination(); //reload the combination of spinner

        dice.clear();
        dice = onRestoreBundle.getParcelableArrayList("diceList");
        restoreDices();  //reload all dices with the situation
        if(roundCheck==0) {
            LockDices(true); //2019-09-16 lock dices for the first time of a round
        }

        diceListener();   // set dices of choosed , if a dice has been choosed , the dice will be red, or the opposite
        //reload the list of game results
        gameResults = onRestoreBundle.getParcelableArrayList("gameResults");
    }

    public void findViews()
    {
        btRun = findViewById(R.id.bThrow);
        btCheck = findViewById(R.id.btCheck);
        btNext = findViewById(R.id.btNext);

        spinnerNumber = (Spinner) findViewById(R.id.spinnerNumber);  // get the Spinner view
        textRound = findViewById(R.id.tvRound);  //get the TextView of round information
        textTotalPoint = findViewById(R.id.tvTotalPoint);  //get the TextView of total point information
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //this.setRequestedOrientation(setDisplay());

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.activity_main_land);

            findViews();
            restoreCombination();
            restoreDices();
            setViewsOnConfigChanged();
            diceListener();
            spinnerNumberListener();
            showRoundAndTotalPoint(currentRound, roundPoints, totalPoint);  // for the first round
        } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_main);
            findViews();
            restoreCombination();
            restoreDices();
            setViewsOnConfigChanged();
            diceListener();
            spinnerNumberListener();
            showRoundAndTotalPoint(currentRound, roundPoints, totalPoint);  // for the first round
        }
    }

    //set the enables for  buttons and spinner when the screen change
    public void setViewsOnConfigChanged()
    {
        if (roundCheck == 3) {
            btRun.setEnabled(false);
            if (!combinationSelected) {
                spinnerNumber.setEnabled(true);
            } else {
                spinnerNumber.setEnabled(false);
            }
        }

        if (combinationSelected && numberCount == 3) {
            for (Dice d : dice) {
                d.img.setEnabled(false);
            }
        }

        if (roundCheck == 0) {
            LockDices(true); //2019-09-16 lock dices for the first time of a round
            btCheck.setEnabled(false);
            btNext.setEnabled(false);
        } else {
            btCheck.setEnabled(combinationSelected);
            btNext.setEnabled(combinationSelected);
        }
    }

    //ORIENTATION_PORTRAIT or ORIENTATION_LANDSCAPE
    public int setDisplay()
    {
        int currentOrientation;
        Display display = getWindowManager().getDefaultDisplay();
        /*
        int width=display.getWidth();
        int height=display.getHeight();

        if(width>height){
            currentOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        else{
            currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        */
        switch(display.getRotation())
        {
            case Surface.ROTATION_90:
                currentOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            case Surface.ROTATION_180:
                currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            case Surface.ROTATION_270:
                currentOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            default:
                currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        return currentOrientation;
    }

    // method for the combination of Spinner when the application onRestore
    public void restoreCombination()
    {
        if(!listNumber.isEmpty()) {
            setSpinnerAdapter(listNumber);
        }

        if (numberCount == 3) {
            spinnerNumber.setSelection(listNumber.indexOf("Low"), true);
        }else{
            if (listNumber.contains(Integer.toString(numberCount)))
            {
                spinnerNumber.setSelection(listNumber.indexOf(Integer.toString(numberCount)), true);
            }
        }
    }

    // to restore all dices when the application onRestore
    public void restoreDices()
    {
        //dice.clear();
        //dice = onRestoreBundle.getParcelableArrayList("diceList");
        dice.get(0).img=findViewById(R.id.imageView1);
        dice.get(1).img=findViewById(R.id.imageView2);
        dice.get(2).img=findViewById(R.id.imageView3);
        dice.get(3).img=findViewById(R.id.imageView4);
        dice.get(4).img=findViewById(R.id.imageView5);
        dice.get(5).img=findViewById(R.id.imageView6);

        for(Dice d:dice) {
            d.setRandomValue(d.randomValue);
            if(d.locked) {
                d.setChoosedImages(d.randomValue);
            }
            else if(d.counted) {
                d.setUsedImages(d.randomValue);
                d.img.setEnabled(false);
            }
            else {
                d.setRandomImages(d.randomValue);
            }
        }
    }

    //get 6 ImageView to those 6 dice objects of class Dice, and add them to the Dice list.
    public void initDiceImages()
    {
        die1.img = findViewById(R.id.imageView1);
        dice.add(die1);
        die2.img = findViewById(R.id.imageView2);
        dice.add(die2);
        die3.img = findViewById(R.id.imageView3);
        dice.add(die3);
        die4.img = findViewById(R.id.imageView4);
        dice.add(die4);
        die5.img = findViewById(R.id.imageView5);
        dice.add(die5);
        die6.img = findViewById(R.id.imageView6);
        dice.add(die6);
    }

    // set dices of choosed , if a dice has been choosed , the dice will be red, or the opposite
    public void diceListener()
    {
        for(final Dice die : dice)
        {
            die.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!die.locked) {
                        die.setChoosedImages(die.getRandomValue());
                        die.locked = true;
                    }
                    else
                    {
                        die.setRandomImages(die.getRandomValue());
                        die.locked = false;
                    }
                }
            });
        }
    }

    //set the listener event to the Spinner
    public void spinnerNumberListener()
    {
        spinnerNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(listNumber.get(position)!="Combination" && !listNumber.get(position).equals("Combination"))
                {
                    if(listNumber.get(position)=="Low" || listNumber.get(position).equals("Low")) //at this moment, I set Low as a number 3. 2019-09-12
                    {
                        numberCount=3;
                        setDicesForLow();  //set dices situation for these dices with value less than three
                    }
                    else {
                        numberCount=Integer.parseInt(listNumber.get(position));
                    }
                    spinnerNumber.setEnabled(false);
                    combinationSelected=true; //2019-09-17
                    btCheck.setEnabled(true);
                    btNext.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void runDice(View src)
    {
        initDices();
        //check for one round can just click three time Button THROW
        roundCheck = roundCheck+1;
        LockDices(false);

        //if the player have throw the dices 3 times , thus the Throw button will be locked.
        if(roundCheck==3)
        {
            btRun.setEnabled(false);
            spinnerNumber.setEnabled(true);
        }
    }

    public int getAnRandomValue()
    {
        Random rand = new Random();
        return rand.nextInt(6)+1;
    }

    public void initDices()
    {
        int aRandNumber;
        if(!dice.isEmpty())
        {
            for(final Dice die : dice)
            {
                if(!die.locked)
                {
                    aRandNumber= getAnRandomValue();
                    die.setRandomValue(aRandNumber);
                    die.setRandomImages(aRandNumber);
                }
            }
        }
    }

    //lock the dices for the init
    // unlock the dices after player click the Throw button
    public void LockDices(boolean lock)
    {
        for(final Dice die : dice)
        {
            if(lock)
            {
                die.img.setEnabled(false);
            }
            else
            {
                die.img.setEnabled(true);
            }
        }
    }

    // init combination numbers for the Spinner
    public void initNumbers()
    {
        listNumber.add("Combination");
        listNumber.add("Low");
        for(int i=4; i<13; i++)
        {
            listNumber.add(Integer.toString(i));
        }
    }

    public void setSpinnerAdapter(ArrayList<String> listForSpinner)
    {
        try
        {
            spinnerNumber.setPrompt("Number for points");  // set numbers to the list

            if(listNumber.size()>0) {
                adapterNumber = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listForSpinner);
                spinnerNumber.setAdapter(adapterNumber);

                checkForSpinner();  // to check that Button Throw has been clicked three times
            }
        }
        catch(Exception e) {

        }
    }

    //check the dices which have been choosed is match the combination or not
    //if points of dices match the combination, then the points will add to the round's point
    public void checkPoints(View src)
    {
        int checkRoundPoint=0;

        if(numberCount !=0 && numberCount==3)  //when player choose the 'Low'
        {
            checkRoundPoint = checkPointsForLow();

            roundPoints = roundPoints + checkRoundPoint;
            showRoundAndTotalPoint(currentRound, roundPoints, totalPoint);
        }

        if(numberCount !=0 && numberCount>=4)
        {
            if (numberCount != 0 && !dice.isEmpty()) {
                for (final Dice die : dice) {
                    if (die.locked) {
                        checkRoundPoint = checkRoundPoint + die.getRandomValue();
                    }
                }
            }

            if (checkRoundPoint == numberCount) {
                for (final Dice die : dice) {
                    if (die.locked) {
                        die.img.setEnabled(false);
                        die.setUsedImages(die.randomValue);
                        die.counted=true;
                        die.locked = false;
                    }
                }
                roundPoints = roundPoints + checkRoundPoint;
                showRoundAndTotalPoint(currentRound, roundPoints, totalPoint);
            }
        }
    }

    //when player choose 'Low' for combination,thus we will sum up all points which these dices with values lower than four
    public int checkPointsForLow()
    {
        int sumLow=0;
        for(final Dice die : dice)
        {
            if(die.locked && !die.counted)
            {
                if(die.getRandomValue()<4)
                {
                    sumLow = sumLow+die.getRandomValue();
                    die.locked=false;
                    die.counted=true;
                    die.setUsedImages(die.randomValue);
                }
            }
        }
        return sumLow;
    }

    //when player choose the 'Low' for combination,thus set all dices which less than 4 has been used and all dices can not be enable
    public void setDicesForLow()
    {
        for(final Dice die : dice)
        {
            if(die.getRandomValue()<4) {
                die.setChoosedImages(die.randomValue);
                die.locked=true;
                die.img.setEnabled(false);
            }
            else {
                die.setRandomImages(die.randomValue);
                die.img.setEnabled(false);
            }
        }
    }

    // turn to next round
    public void nextRound(View src)
    {
        roundCheck = 0;
        combinationSelected=false;
        //after point counted, the player can click the Throw button again
        btRun.setEnabled(true);
        btCheck.setEnabled(false);
        btNext.setEnabled(false);

        // check for 10 rounds
        roundNumber = roundNumber+1;
        currentRound=roundNumber+1;
        totalPoint=totalPoint+roundPoints;  //the point of one round will be counted to the total points

        refreshDices();  //initialization of dices
        showRoundAndTotalPoint(currentRound,roundPoints,totalPoint);  //set the number and points to current round

        // set the Spinner one more time after the player used a combination number
        if(numberCount==3){
            listNumber.remove("Low");
        }
        else{
            listNumber.remove(Integer.toString(numberCount));
        }

        setSpinnerAdapter(listNumber);   //initialization of SpinnerAdapter once again

        //add result data to a class object GameResult, then transfer it to the ResultActivity
        gameResults.add(new GameResult(Integer.toString(roundNumber),Integer.toString(numberCount),Integer.toString(roundPoints)));

        roundPoints=0;   //after round points have added to the game result list , clear the current rounds point
        textRound.setText("Current round:"+ currentRound +", Point: [ ]");

        // check for 10 rounds, after played 10 rounds, the player have to check the points, and can not continue play.
        if(roundNumber==10){
            showResult();
        }
    }

    // to show the current round, round point and total point
    public void showRoundAndTotalPoint(int cRound, int roundsPoint,int totalPoint)
    {
        textRound.setText("Current round:"+ cRound +"  [Point: "+roundsPoint+"]");
        textTotalPoint.setText("Total point: "+totalPoint);
    }

    // after player played 10 rounds, then the game will be end and will check the results
    // transfer all points to the ResultActivity
    private void showResult()
    {
        final AlertDialog.Builder resultDialog = new AlertDialog.Builder(MainActivity.this);
        resultDialog.setTitle("Check results");
        resultDialog.setMessage("You have already played 10 rounds,check results?");
        resultDialog.setCancelable(false);
        resultDialog.setPositiveButton("Check", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this,ResultActivity.class);
                intent.putParcelableArrayListExtra("listGameResult",gameResults);
                intent.putExtra("totalPoint",totalPoint);
                startActivity(intent);
            }
        });

        /*
        resultDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        */
        resultDialog.show();
    }

    //after point checked, reset the dices
    public void refreshDices()
    {
        for(final Dice die : dice)
        {
            die.locked=false;
            die.img.setEnabled(false);
            die.counted=false;
            die.setRandomImages(die.randomValue);
        }
    }

    // check that Button Throw has been click three times
    // thus the Spinner will be able to click
    public void checkForSpinner()
    {
        if(roundCheck==3) {
            spinnerNumber.setEnabled(true);
        }
        else {
            spinnerNumber.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        this.finish();
    }

    /*
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        System.exit(0);
    }
    */

}
