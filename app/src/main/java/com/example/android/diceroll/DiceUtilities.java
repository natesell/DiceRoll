package com.example.android.diceroll;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiceUtilities {
    // Takes an ImageView (presumably of a die), and rolls it,
    // changing the image to the proper random roll
    public static void rollDie (ImageView diceImageView) {
        int randomNum = (int) (Math.random() * ((6 - 1) + 1)) + 1;

        if (randomNum == 1)
            diceImageView.setImageResource(R.drawable.dice_roll_1);
        if (randomNum == 2)
            diceImageView.setImageResource(R.drawable.dice_roll_2);
        if (randomNum == 3)
            diceImageView.setImageResource(R.drawable.dice_roll_3);
        if (randomNum == 4)
            diceImageView.setImageResource(R.drawable.dice_roll_4);
        if (randomNum == 5)
            diceImageView.setImageResource(R.drawable.dice_roll_5);
        if (randomNum == 6)
            diceImageView.setImageResource(R.drawable.dice_roll_6);
    }

    // Parameters: The LinearLayout associated to the active dice,
    //             The LinearLayout associated to the held dice,
    //             The ImageView associated to the dice to be held
    public static void holdDie (LinearLayout activeDiceLinearLayout, LinearLayout heldDiceLinearLayout,
                                ImageView diceImageView) {
        if (diceImageView != null) {
            activeDiceLinearLayout.removeView(diceImageView);
            heldDiceLinearLayout.addView(diceImageView);
        }
    }

    // Holds the first die found with given int roll
    // NOTE: Only call this method after verifying the roll is in active by using "containsARoll"
    // "abc" is literally an arbitrary non-null image view because you cant call "setImageResource" on a null ImageView
    public static void holdDie(Context context, LinearLayout activeDiceLinearLayout, LinearLayout heldDiceLinearLayout,
                               int roll) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.dice_roll_question_mark);
        ImageView arbitraryImageView = new ImageView(context);
        arbitraryImageView.setImageDrawable(drawable);

        int indexOfPromptedRoll = returnIndexOfSpecificRoll(context, activeDiceLinearLayout, roll);
        ImageView diceImageView = (ImageView) activeDiceLinearLayout.getChildAt(indexOfPromptedRoll);
        holdDie(activeDiceLinearLayout, heldDiceLinearLayout, diceImageView);
    }

    public static void holdRestOfDice(LinearLayout activeDiceLinearLayout, LinearLayout heldDiceLinearLayout) {
        int currentAmountOfDice = activeDiceLinearLayout.getChildCount();
        ImageView diceImageView;
        for(int i = 0; i < currentAmountOfDice; i++) {
            diceImageView = (ImageView) activeDiceLinearLayout.getChildAt(0);
            DiceUtilities.holdDie(activeDiceLinearLayout, heldDiceLinearLayout, diceImageView);
        }
    }

    // Returns the index of whichever roll is in the parameters,
    // NOTE: Best only to use this method after using "containsARoll";
    //       if the roll isn't in the layout, you will be returned a 0,
    //       which is obviously an impossible roll
    public static int returnIndexOfSpecificRoll(Context context, LinearLayout diceLinearLayout, int roll) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.dice_roll_question_mark);
        ImageView rollImageView = new ImageView(context);
        rollImageView.setImageDrawable(drawable);

        if (roll == 1)
            rollImageView.setImageResource(R.drawable.dice_roll_1);
        if (roll == 2)
            rollImageView.setImageResource(R.drawable.dice_roll_2);
        if (roll == 3)
            rollImageView.setImageResource(R.drawable.dice_roll_3);
        if (roll == 4)
            rollImageView.setImageResource(R.drawable.dice_roll_4);
        if (roll == 5)
            rollImageView.setImageResource(R.drawable.dice_roll_5);
        if (roll == 6)
            rollImageView.setImageResource(R.drawable.dice_roll_6);

        // Since using == to compare Drawables doesn't work, we will create bitmaps of the ImageViews
        Bitmap bitmapForRollImageView = ((BitmapDrawable)rollImageView.getDrawable()).getBitmap();


        // Iterates through each ImageView/Die inside of activeDiceLinearLayout,
        // and compares the bitmap of the requested roll with the ImageView at the current index (i)
        // returning the first available index of the requested roll
        int currentAmountOfDice = diceLinearLayout.getChildCount();
        ImageView diceImageView;
        for (int i = 0; i < currentAmountOfDice; i++) {
            diceImageView = (ImageView) diceLinearLayout.getChildAt(i);
            Bitmap bitmapForDiceImageView = ((BitmapDrawable)diceImageView.getDrawable()).getBitmap();

            if (bitmapForRollImageView == bitmapForDiceImageView)
                return i;
        }

        return 0;
    }

    // "rollImageView" is literally an arbitrary non-null image view because you cant call "setImageResource" on a null ImageView
    public static boolean containsARoll(Context context, LinearLayout diceLinearLayout, int roll) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.dice_roll_question_mark);
        ImageView rollImageView = new ImageView(context);
        rollImageView.setImageDrawable(drawable);

        if (roll == 1)
            rollImageView.setImageResource(R.drawable.dice_roll_1);
        if (roll == 2)
            rollImageView.setImageResource(R.drawable.dice_roll_2);
        if (roll == 3)
            rollImageView.setImageResource(R.drawable.dice_roll_3);
        if (roll == 4)
            rollImageView.setImageResource(R.drawable.dice_roll_4);
        if (roll == 5)
            rollImageView.setImageResource(R.drawable.dice_roll_5);
        if (roll == 6)
            rollImageView.setImageResource(R.drawable.dice_roll_6);

        // Since using == to compare Drawables doesn't work, we will create bitmaps of the ImageViews
        Bitmap bitmapForRollImageView = ((BitmapDrawable)rollImageView.getDrawable()).getBitmap();

        // Iterates through each ImageView/Die inside of activeDiceLinearLayout,
        // and compares the bitmap of the requested roll with the ImageView at the current index (i)
        // returning true if the bitmaps ever are equal, and false if not
        int currentAmountOfDice = diceLinearLayout.getChildCount();
        ImageView diceImageView;
        boolean result = false;
        for (int i = 0; i < currentAmountOfDice; i++) {
            diceImageView = (ImageView) diceLinearLayout.getChildAt(i);
            Bitmap bitmapForDiceImageView = ((BitmapDrawable)diceImageView.getDrawable()).getBitmap();
            if (bitmapForRollImageView == bitmapForDiceImageView)
                result = true;
        }

        return result;
    }

    public static int intRollValueOfDieImageView(Context context, ImageView diceImageView) {
        Bitmap bitmapForDiceImageView = ((BitmapDrawable)diceImageView.getDrawable()).getBitmap();

        Drawable drawable = context.getResources().getDrawable(R.drawable.dice_roll_question_mark);
        ImageView rollImageViewForAOne = new ImageView(context);
        ImageView rollImageViewForATwo = new ImageView(context);
        ImageView rollImageViewForAThree = new ImageView(context);
        ImageView rollImageViewForAFour = new ImageView(context);
        ImageView rollImageViewForAFive = new ImageView(context);
        ImageView rollImageViewForASix = new ImageView(context);

        rollImageViewForAOne.setImageDrawable(drawable);
        rollImageViewForATwo.setImageDrawable(drawable);
        rollImageViewForAThree.setImageDrawable(drawable);
        rollImageViewForAFour.setImageDrawable(drawable);
        rollImageViewForAFive.setImageDrawable(drawable);
        rollImageViewForASix.setImageDrawable(drawable);

        rollImageViewForAOne.setImageResource(R.drawable.dice_roll_1);
        rollImageViewForATwo.setImageResource(R.drawable.dice_roll_2);
        rollImageViewForAThree.setImageResource(R.drawable.dice_roll_3);
        rollImageViewForAFour.setImageResource(R.drawable.dice_roll_4);
        rollImageViewForAFive.setImageResource(R.drawable.dice_roll_5);
        rollImageViewForASix.setImageResource(R.drawable.dice_roll_6);

        Bitmap bitmapForRollImageViewForAOne = ((BitmapDrawable)rollImageViewForAOne.getDrawable()).getBitmap();
        Bitmap bitmapForRollImageViewForATwo = ((BitmapDrawable)rollImageViewForATwo.getDrawable()).getBitmap();
        Bitmap bitmapForRollImageViewForAThree = ((BitmapDrawable)rollImageViewForAThree.getDrawable()).getBitmap();
        Bitmap bitmapForRollImageViewForAFour = ((BitmapDrawable)rollImageViewForAFour.getDrawable()).getBitmap();
        Bitmap bitmapForRollImageViewForAFive = ((BitmapDrawable)rollImageViewForAFive.getDrawable()).getBitmap();
        Bitmap bitmapForRollImageViewForASix = ((BitmapDrawable)rollImageViewForASix.getDrawable()).getBitmap();

        int roll = 0;

        if (bitmapForDiceImageView == bitmapForRollImageViewForAOne)
            roll = 1;
        if (bitmapForDiceImageView == bitmapForRollImageViewForATwo)
            roll = 2;
        if (bitmapForDiceImageView == bitmapForRollImageViewForAThree)
            roll = 3;
        if (bitmapForDiceImageView == bitmapForRollImageViewForAFour)
            roll = 4;
        if (bitmapForDiceImageView == bitmapForRollImageViewForAFive)
            roll = 5;
        if (bitmapForDiceImageView == bitmapForRollImageViewForASix)
            roll = 6;

        return roll;
    }

}
