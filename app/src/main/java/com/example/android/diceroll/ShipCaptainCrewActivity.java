package com.example.android.diceroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShipCaptainCrewActivity extends AppCompatActivity {


    private int rollsLeft = 3;
    private LinearLayout activeDiceLinearLayout;
    private LinearLayout heldDiceLinearLayout;
    private TextView currentScoreTextView;
    private TextView currentScoreStaticTextView;
    private TextView currentRollCountTextView;
    private TextView whoseTurnTextView;
    private Button rollButton;
    private Button holdButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_captain_crew);
        activeDiceLinearLayout = findViewById(R.id.mActiveDiceLinearLayout);
        heldDiceLinearLayout = findViewById(R.id.mHeldDiceLinearLayout);
        currentScoreTextView = findViewById(R.id.mCurrentScoreTextView);
        currentScoreStaticTextView = findViewById(R.id.mCurrentScoreStaticTextView);
        currentRollCountTextView = findViewById(R.id.mCurrentRollCountTextView);
        whoseTurnTextView = findViewById(R.id.mWhoseTurnTextView);

        // Intent will have 3 strings, with name playChoice which can be, 'myself', 'computer', or 'friends'
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String playChoice = (String) bundle.get("playChoice");

            if (playChoice.equals("computer")) {
                whoseTurnTextView.setText("Computer's Turn");
                whoseTurnTextView.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                int computerScore = simulateComputerPlay();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }

    // Should lock all user interaction, then simulate a computer play, log the score,
    // return control to the user, finally passing back the computer's score
    public int simulateComputerPlay() {
        int computerScore;
        int currentComputerScore;

        // rolls
        //      if has a 6, holds
        //          if heldDiceLinearLayout.getChildCount() == 5 -> check score
        //              if score > 6 -> return score
        //              if score < 7 -> choose to reroll
        // rolls
        //      if heldDiceLinearLayout.getChildCount() == 3, hold and check score
        //          if score > 6 -> return score
        //          if score < 7 -> choose to reroll
        //      else if (2 dice are held && active contains a 4) || (1 dice is held && active contains a 5) || (0 dice are held && active contains a 6)
        //          if heldDiceLinearLayout.getChildCount() == 5 -> check score
        //              if score > 6 -> return score
        //              if score < 7 -> choose to reroll
        // rolls
        //      if (heldDiceLinearLayout.getChildCount() == 3) || (2 dice are held && active contains a 4) || (1 dice is held && active contains a 5) || (0 dice are held && active contains a 6)
        //          holds and returns score
        // -------------------------------------------------------------------------------------------
        // rolls
        //      if has a 6, holds
        //          if heldDiceLinearLayout.getChildCount() == 5 -> check score
        //              if score > 6 -> return score
        //              if score < 7 -> choose to reroll

        delayAction("roll", 1000);
        if (DiceUtilities.containsARoll(this, activeDiceLinearLayout, 6)) {
            // change
            // delayAction("hold", 1000);
            onClickHold(holdButton, true);
            if (heldDiceLinearLayout.getChildCount() == 5) {
                currentComputerScore = Integer.parseInt(currentScoreTextView.getText().toString());
                if (currentComputerScore > 6) {
                    // keep will set rolls to 0 and put all 5 dice in heldDiceLinearLayout
                    // so we return the score!
                    delayAction("keep", 1000);
                    Toast.makeText(this, "Computer is keeping a score of " + currentComputerScore + "!",
                            Toast.LENGTH_SHORT).show();
                    computerScore = currentComputerScore;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return computerScore;
                }
                if (currentComputerScore < 7) {
                    // activeDiceLinearLayout will have 2 dice
                    // and heldDiceLinearLayout will have 3 dice
                    delayAction("reroll", 1000);
                    Toast.makeText(this, "Instead of keeping a score of " + currentComputerScore + ", computer is rerolling!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }



        // rolls
        //      if heldDiceLinearLayout.getChildCount() == 3, hold and check score
        //          if score > 6 -> return score
        //          if score < 7 -> choose to reroll
        //      else if (2 dice are held && active contains a 4) || (1 dice is held && active contains a 5) || (0 dice are held && active contains a 6)
        //          if heldDiceLinearLayout.getChildCount() == 5 -> check score
        //              if score > 6 -> return score
        //              if score < 7 -> choose to reroll
        delayAction("roll", 5000);
        if (heldDiceLinearLayout.getChildCount() == 3) {
            // change
            // delayAction("hold", 1000);
            onClickHold(holdButton, true);
            currentComputerScore = Integer.parseInt(currentScoreTextView.getText().toString());
            if (currentComputerScore > 6) {
                delayAction("keep", 1000);
                Toast.makeText(this, "Computer is keeping a score of " + currentComputerScore + "!",
                        Toast.LENGTH_SHORT).show();
                computerScore = currentComputerScore;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                return computerScore;
            }
            if (currentComputerScore < 7) {
                // activeDiceLinearLayout will have 2 dice
                // and heldDiceLinearLayout will have 3 dice
                delayAction("reroll", 1000);
                Toast.makeText(this, "Instead of keeping a score of " + currentComputerScore + ", computer is rerolling!",
                        Toast.LENGTH_SHORT).show();
            }
        } else if ((heldDiceLinearLayout.getChildCount() == 2 && DiceUtilities.containsARoll(getApplicationContext(), activeDiceLinearLayout, 4)) ||
                (heldDiceLinearLayout.getChildCount() == 1 && DiceUtilities.containsARoll(getApplicationContext(), activeDiceLinearLayout, 5)) ||
                (heldDiceLinearLayout.getChildCount() == 0 && DiceUtilities.containsARoll(getApplicationContext(), activeDiceLinearLayout, 6))) {
            // change
            // delayAction("hold", 1000);
            onClickHold(holdButton, true);
            if (heldDiceLinearLayout.getChildCount() == 5) {
                currentComputerScore = Integer.parseInt(currentScoreTextView.getText().toString());
                if (currentComputerScore > 6) {
                    delayAction("keep", 1000);
                    Toast.makeText(this, "Computer is keeping a score of " + currentComputerScore + "!",
                            Toast.LENGTH_SHORT).show();
                    computerScore = currentComputerScore;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return computerScore;
                }
                if (currentComputerScore < 7) {
                    // activeDiceLinearLayout will have 2 dice
                    // and heldDiceLinearLayout will have 3 dice
                    delayAction("reroll", 1000);
                    Toast.makeText(this, "Instead of keeping a score of " + currentComputerScore + ", computer is rerolling!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

        // rolls
        //      if (heldDiceLinearLayout.getChildCount() == 3) || (2 dice are held && active contains a 4) || (1 dice is held && active contains a 5) || (0 dice are held && active contains a 6)
        //          holds and returns score
        delayAction("roll", 2500);
        if ((heldDiceLinearLayout.getChildCount() == 3) || (heldDiceLinearLayout.getChildCount() == 2 && DiceUtilities.containsARoll(getApplicationContext(), activeDiceLinearLayout, 4)) ||
                (heldDiceLinearLayout.getChildCount() == 1 && DiceUtilities.containsARoll(getApplicationContext(), activeDiceLinearLayout, 5)) ||
                (heldDiceLinearLayout.getChildCount() == 0 && DiceUtilities.containsARoll(getApplicationContext(), activeDiceLinearLayout, 6))) {
            // change
            // delayAction("hold", 1000);
            onClickHold(holdButton, true);
        }
        computerScore = Integer.parseInt(currentScoreTextView.getText().toString());

        return computerScore;
    }

    // String action can be either "roll", "hold", "reroll", or "keep"
    private void delayAction(String action, int delayInMilliseconds) {
        if (action.equals("roll")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 1s = 1000ms
                    onClickRoll(rollButton);
                }
            }, delayInMilliseconds);
        } else if (action.equals("hold")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 1s = 1000ms
                    onClickHold(holdButton, true);
                }
            }, delayInMilliseconds);
        } else if (action.equals("reroll")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 1s = 1000ms
                    rerollInsteadOfKeepScore(heldDiceLinearLayout);
                }
            }, delayInMilliseconds);
        } else if (action.equals("keep")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 1s = 1000ms
                    keepScoreInsteadOfReroll();
                }
            }, delayInMilliseconds);
        } else if (action.equals("nothing")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 1s = 1000ms
                    Log.i("supposedToBeWaiting", "I waited!");
                }
            }, delayInMilliseconds);
        }
    }

    // Method invoked after the "Roll" button is clicked;
    // should roll all dice remaining in mActiveDiceLinearLayout
    public void onClickRoll (View view) {
        // Finds how many dice are remaining in mActiveDiceLinearLayout
        // and then it rolls all dice found
        LinearLayout activeDiceLinearLayout = findViewById(R.id.mActiveDiceLinearLayout);
        int currentAmountOfDice = activeDiceLinearLayout.getChildCount();
        ImageView diceImageView;
        for(int i = 0; i < currentAmountOfDice; i++) {
            diceImageView = (ImageView) activeDiceLinearLayout.getChildAt(i);
            DiceUtilities.rollDie(diceImageView);
        }

        // The above code rolls all dice in mActiveDiceLinearLayout,
        // so let's go ahead and reduce the rolls remaining by one
        rollsLeft--;
        String rollsLeftAsString = "" + rollsLeft;
        TextView currentRollCount = findViewById(R.id.mCurrentRollCountTextView);
        currentRollCount.setText(rollsLeftAsString);


        // If there are no rolls remaining,
        // sets the roll button to an unclickable state AND changes "Current Score : " to "Final Score : "
        if(rollsLeft == 0) {
            Button rollButton = findViewById(R.id.mRollButton);
            rollButton.setAlpha(.2f);
            rollButton.setClickable(false);
        }
    }

    // TODO (1) Probably wrong place to put this, but make it so roll button isn't clickable while a hold is necessary, and hold not possible if a roll is necessary
    public void onClickHold(View view, boolean isComputerSimulating) {
        boolean isHoldingASix = DiceUtilities.containsARoll(this, heldDiceLinearLayout, 6);
        boolean hasActiveSix = DiceUtilities.containsARoll(this, activeDiceLinearLayout, 6);
        boolean isHoldingAFive = DiceUtilities.containsARoll(this, heldDiceLinearLayout, 5);
        boolean hasActiveFive = DiceUtilities.containsARoll(this, activeDiceLinearLayout, 5);
        boolean isHoldingAFour = DiceUtilities.containsARoll(this, heldDiceLinearLayout, 4);
        boolean hasActiveFour = DiceUtilities.containsARoll(this, activeDiceLinearLayout, 4);

        // If user is currently holding a 6, 5 and 4, we hold the rest of the dice in activeDiceLinearLayout
        // else if user is currently holding a 6 and 5, but NOT 4 we check activeDiceLinearLayout for a 4,
        //      If we find that 4 -> Hold the remaining dice
        //      If we didn't      -> return
        // else if user is currently holding a 6, we check activeDiceLinearLayout for a 5,
        //      If we find a 5 -> Hold the 5 and check for 4
        //              If we find that 4 -> Hold remaining dice
        //              If we didn't      -> return
        //      If we didn't find a 5 -> return
        // else user has no 6, 5 or 4, so we check activeDiceLinearLayout for a 6
        //      If we find a 6 -> Hold 6 and check for 5
        //              If we find a 5 -> Hold 5 and check for a 4
        //                      If we find a 4 -> Hold rest of dice
        //                      If we don't find a 4 -> return
        //              If we don't find a 5 -> return
        //      If we don't find a 6 -> return
        if (isHoldingASix && isHoldingAFive && isHoldingAFour) {
            DiceUtilities.holdRestOfDice(activeDiceLinearLayout, heldDiceLinearLayout);
        } else if (isHoldingASix && isHoldingAFive) {
            if (hasActiveFour) {
                DiceUtilities.holdRestOfDice(activeDiceLinearLayout, heldDiceLinearLayout);
            }   else {
                return;
            }
        } else if (isHoldingASix) {
            if (hasActiveFive) {
                DiceUtilities.holdDie(this, activeDiceLinearLayout, heldDiceLinearLayout, 5);
                if (hasActiveFour) {
                    DiceUtilities.holdRestOfDice(activeDiceLinearLayout, heldDiceLinearLayout);
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            if (hasActiveSix) {
                DiceUtilities.holdDie(this, activeDiceLinearLayout, heldDiceLinearLayout, 6);
                if (hasActiveFive) {
                    DiceUtilities.holdDie(this, activeDiceLinearLayout, heldDiceLinearLayout, 5);
                    if (hasActiveFour) {
                        DiceUtilities.holdRestOfDice(activeDiceLinearLayout, heldDiceLinearLayout);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }

        if (heldDiceLinearLayout.getChildCount() > 3) {
            int indexOfSix = DiceUtilities.returnIndexOfSpecificRoll(this, heldDiceLinearLayout, 6);
            int indexOfFive = DiceUtilities.returnIndexOfSpecificRoll(this, heldDiceLinearLayout, 5);
            int indexOfFour = DiceUtilities.returnIndexOfSpecificRoll(this, heldDiceLinearLayout, 4);
            int score = 0;


            int amountOfDiceHeld = heldDiceLinearLayout.getChildCount();
            for (int currentIndex = 0; currentIndex < amountOfDiceHeld; currentIndex++) {
                if (!(currentIndex == indexOfFour || currentIndex == indexOfFive || currentIndex == indexOfSix)) {
                    ImageView imageViewOfCurrentIndex = (ImageView) heldDiceLinearLayout.getChildAt(currentIndex);
                    score += DiceUtilities.intRollValueOfDieImageView(this, imageViewOfCurrentIndex);
                }
            }

            // Log.i("Method: onClickHold", "Int variable 'score' is : " + score + "; Int variable 'count' is : " + count);
            currentScoreTextView.setText(String.valueOf(score));
        }

        if (rollsLeft == 0) {
            Button holdButton = findViewById(R.id.mHoldButton);
            currentScoreStaticTextView.setText("Final Score : ");
            holdButton.setAlpha(.2f);
            holdButton.setClickable(false);
        }
    }

    public void onClickHold(View view) {
        boolean isHoldingASix = DiceUtilities.containsARoll(this, heldDiceLinearLayout, 6);
        boolean hasActiveSix = DiceUtilities.containsARoll(this, activeDiceLinearLayout, 6);
        boolean isHoldingAFive = DiceUtilities.containsARoll(this, heldDiceLinearLayout, 5);
        boolean hasActiveFive = DiceUtilities.containsARoll(this, activeDiceLinearLayout, 5);
        boolean isHoldingAFour = DiceUtilities.containsARoll(this, heldDiceLinearLayout, 4);
        boolean hasActiveFour = DiceUtilities.containsARoll(this, activeDiceLinearLayout, 4);

        // If user is currently holding a 6, 5 and 4, we hold the rest of the dice in activeDiceLinearLayout
        // else if user is currently holding a 6 and 5, but NOT 4 we check activeDiceLinearLayout for a 4,
        //      If we find that 4 -> Hold the remaining dice
        //      If we didn't      -> return
        // else if user is currently holding a 6, we check activeDiceLinearLayout for a 5,
        //      If we find a 5 -> Hold the 5 and check for 4
        //              If we find that 4 -> Hold remaining dice
        //              If we didn't      -> return
        //      If we didn't find a 5 -> return
        // else user has no 6, 5 or 4, so we check activeDiceLinearLayout for a 6
        //      If we find a 6 -> Hold 6 and check for 5
        //              If we find a 5 -> Hold 5 and check for a 4
        //                      If we find a 4 -> Hold rest of dice
        //                      If we don't find a 4 -> return
        //              If we don't find a 5 -> return
        //      If we don't find a 6 -> return
        if (isHoldingASix && isHoldingAFive && isHoldingAFour) {
            DiceUtilities.holdRestOfDice(activeDiceLinearLayout, heldDiceLinearLayout);
        } else if (isHoldingASix && isHoldingAFive) {
            if (hasActiveFour) {
                DiceUtilities.holdRestOfDice(activeDiceLinearLayout, heldDiceLinearLayout);
            }   else {
                return;
            }
        } else if (isHoldingASix) {
            if (hasActiveFive) {
                DiceUtilities.holdDie(this, activeDiceLinearLayout, heldDiceLinearLayout, 5);
                if (hasActiveFour) {
                    DiceUtilities.holdRestOfDice(activeDiceLinearLayout, heldDiceLinearLayout);
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            if (hasActiveSix) {
                DiceUtilities.holdDie(this, activeDiceLinearLayout, heldDiceLinearLayout, 6);
                if (hasActiveFive) {
                    DiceUtilities.holdDie(this, activeDiceLinearLayout, heldDiceLinearLayout, 5);
                    if (hasActiveFour) {
                        DiceUtilities.holdRestOfDice(activeDiceLinearLayout, heldDiceLinearLayout);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }

        // Log.i("Method: onClickHold", "heldDiceLinearLayout.getChildCount() == " + heldDiceLinearLayout.getChildCount());
        // here we want to sum dice if heldDiceLinearLayout.getChildrenCount() > 3
        if (heldDiceLinearLayout.getChildCount() > 3) {
            int indexOfSix = DiceUtilities.returnIndexOfSpecificRoll(this, heldDiceLinearLayout, 6);
            int indexOfFive = DiceUtilities.returnIndexOfSpecificRoll(this, heldDiceLinearLayout, 5);
            int indexOfFour = DiceUtilities.returnIndexOfSpecificRoll(this, heldDiceLinearLayout, 4);
            // Log.i("Method: onClickHold", "Index of Six: " + indexOfSix + " | Index of Five: " + indexOfFive + " | Index of Four: " + indexOfFour);
            int score = 0;
            int count = 0;

            int amountOfDiceHeld = heldDiceLinearLayout.getChildCount();
            for (int currentIndex = 0; currentIndex < amountOfDiceHeld; currentIndex++) {
                if (!(currentIndex == indexOfFour || currentIndex == indexOfFive || currentIndex == indexOfSix)) {
                    ImageView imageViewOfCurrentIndex = (ImageView) heldDiceLinearLayout.getChildAt(currentIndex);
                    score += DiceUtilities.intRollValueOfDieImageView(this, imageViewOfCurrentIndex);
                    // Log.i("Method: onClickHold", "DiceUtilities.intRollValueOfDieImageView(this, imageViewOfCurrentIndex) = " +
                    //        DiceUtilities.intRollValueOfDieImageView(this, imageViewOfCurrentIndex) + ", at index " + currentIndex);
                    count++;
                }
            }

            // Log.i("Method: onClickHold", "Int variable 'score' is : " + score + "; Int variable 'count' is : " + count);
            currentScoreTextView.setText(String.valueOf(score));
        }


        if (rollsLeft > 0 && heldDiceLinearLayout.getChildCount() == 5) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm");
            if (rollsLeft > 1)
                builder.setMessage("You have " + rollsLeft + " rolls left! Would you like to keep your score of " + currentScoreTextView.getText()
                        + ", or would you like to reroll the last two dice?");
            else
                builder.setMessage("You have " + rollsLeft + " roll left! Would you like to keep your score of " + currentScoreTextView.getText()
                        + ", or would you like to reroll the last two dice?");

                builder.setNegativeButton("REROLL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rerollInsteadOfKeepScore(heldDiceLinearLayout);
                        dialog.dismiss();
                    }
                });


                builder.setPositiveButton("KEEP SCORE", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        keepScoreInsteadOfReroll();
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }


        if (rollsLeft == 0) {
            Button holdButton = findViewById(R.id.mHoldButton);
            currentScoreStaticTextView.setText("Final Score : ");
            holdButton.setAlpha(.2f);
            holdButton.setClickable(false);
        }
    }

    private void rerollInsteadOfKeepScore(LinearLayout heldDiceLinearLayout) {
        int indexOfSix = DiceUtilities.returnIndexOfSpecificRoll(getApplicationContext(), heldDiceLinearLayout, 6);
        int indexOfFive = DiceUtilities.returnIndexOfSpecificRoll(getApplicationContext(), heldDiceLinearLayout, 5);
        int indexOfFour = DiceUtilities.returnIndexOfSpecificRoll(getApplicationContext(), heldDiceLinearLayout, 4);

        int currentIndex = 0;
        boolean removed = false;
        while (!removed) {
            if (!(currentIndex == indexOfFour || currentIndex == indexOfFive || currentIndex == indexOfSix)) {
                ImageView diceImageView = (ImageView) heldDiceLinearLayout.getChildAt(currentIndex);
                heldDiceLinearLayout.removeView(diceImageView);
                activeDiceLinearLayout.addView(diceImageView);
                removed = true;
            }
            currentIndex++;
        }

        currentIndex = 0;
        removed = false;
        indexOfSix = DiceUtilities.returnIndexOfSpecificRoll(getApplicationContext(), heldDiceLinearLayout, 6);
        indexOfFive = DiceUtilities.returnIndexOfSpecificRoll(getApplicationContext(), heldDiceLinearLayout, 5);
        indexOfFour = DiceUtilities.returnIndexOfSpecificRoll(getApplicationContext(), heldDiceLinearLayout, 4);

        while (!removed) {
            if (!(currentIndex == indexOfFour || currentIndex == indexOfFive || currentIndex == indexOfSix)) {
                ImageView diceImageView = (ImageView) heldDiceLinearLayout.getChildAt(currentIndex);
                heldDiceLinearLayout.removeView(diceImageView);
                activeDiceLinearLayout.addView(diceImageView);
                removed = true;
            }
            currentIndex++;
        }

        currentScoreTextView.setText(String.valueOf(0));
    }

    private void keepScoreInsteadOfReroll() {
        rollsLeft = 0;
        currentRollCountTextView.setText(String.valueOf(rollsLeft));
        Button rollButton = findViewById(R.id.mRollButton);
        Button holdButton = findViewById(R.id.mHoldButton);
        rollButton.setAlpha(.2f);
        holdButton.setAlpha(.2f);
        rollButton.setClickable(false);
        holdButton.setClickable(false);

        currentScoreStaticTextView.setText("Final Score : ");
    }

    public void onClickResetButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Would you actually like to reset your game?");


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    class AsyncClass extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }
    }
}


