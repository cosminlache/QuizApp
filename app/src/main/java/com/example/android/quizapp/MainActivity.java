package com.example.android.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int totalPoints = 0;
    int i = 0;
    int[] myImageList = new int[]{
            R.drawable.italy,
            R.drawable.france,
            R.drawable.germany,
            R.drawable.romania,
            R.drawable.portugal,
            R.drawable.uk,
            R.drawable.spain,
            R.drawable.argentina,
            R.drawable.netherlands,
            R.drawable.maroc};




 //   String[] nameOfTheCountry = new String[]{"Italy", "France", "Germany", "Romania", "Portugal", "the UK", "Spain", "Argentina", "Netherlands", "Morocco"};
 //   String[] rightAnswer = new String[]{"Rome", "Paris", "Berlin", "Bucharest", "Lisbon", "London", "Madrid", "Buenos Aires", "Amsterdam", "Rabat"};
 //   String[] randomName = new String[]{"Ankara", "Atena", "Bratislava", "Sofia", "Bruxelles", "Cairo", "Haga", "Monaco", "Moscova", "Oslo", "Nicosia", "Seul"};
    int[] rightAnswerIndex = new int[]{1, 2, 0, 0, 2, 1, 2, 0, 2, 1};
    boolean xtraQuestion = false;
    boolean sendEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//  When the start button is pressed

    public void firstTime(View View) {

        // Verify if the checkBoxes are checked and store the answers in the boolean variables
        CheckBox xtraQuestionCheckBox = findViewById(R.id.xtraQuestion);
        xtraQuestion = xtraQuestionCheckBox.isChecked();
        CheckBox sendEmailCheckBox = findViewById(R.id.send_email);
        sendEmail = sendEmailCheckBox.isChecked();

        // Hide the checkBox views
        xtraQuestionCheckBox.setVisibility(android.view.View.GONE);
        sendEmailCheckBox.setVisibility(android.view.View.GONE);

        // Hide the start_button and activate the submit_answer button and radioGroup
        Button buttonStart = findViewById(R.id.start_button);
        buttonStart.setVisibility(android.view.View.GONE);

        Button buttonSubmitAnswer = findViewById(R.id.submit_answer_button);
        buttonSubmitAnswer.setVisibility(android.view.View.VISIBLE);

        RadioGroup radioGroup = findViewById(R.id.radio_group);
        radioGroup.setVisibility(android.view.View.VISIBLE);

        nextQuestion(View);
    }

    public void nextQuestion(View view) {
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        Button buttonSubmitAnswer = findViewById(R.id.submit_answer_button);
        TextView changeText = findViewById(R.id.question_text_view);
        ImageView hintImage = findViewById(R.id.hint_image_view);
        String questionText, firstButtonNameText, secondButtonNameText;
        Random r = new Random();
        int randomIndex;
        Resources res = getResources();
        String[] nameOfTheCountry = res.getStringArray(R.array.country);
        String[] randomName = res.getStringArray(R.array.randomAnswer);
        String[] rightAnswer = res.getStringArray(R.array.answer);

        if (i <= 8) {

//        Change the image hint according to the list
            hintImage.setImageResource(myImageList[i]);

//        Change the text of the question according to the list and image hint
//            Resources res = getResources();

            questionText = res.getString(R.string.newQuestion,nameOfTheCountry[i] );
            changeText.setText(questionText);

//        Change the radioButtons text for other random answers from the list

//            firstButtonNameText = "";
            secondButtonNameText = "";
            for (int j = 0; j <= 2; j++) {
                if (j == (rightAnswerIndex[i])) {
                    ((RadioButton) radioGroup.getChildAt(j)).setText(rightAnswer[i]);
                } else {
//                      make sure that radio Button texts are always different
                    randomIndex = r.nextInt(11);
                    firstButtonNameText = randomName[randomIndex];
                    if (firstButtonNameText.equals(secondButtonNameText)) {
                        do {
                            randomIndex = r.nextInt(11);
                            firstButtonNameText = randomName[randomIndex];
                        } while (firstButtonNameText.equals(secondButtonNameText));
                        ((RadioButton) radioGroup.getChildAt(j)).setText(firstButtonNameText);
                    } else {
                        ((RadioButton) radioGroup.getChildAt(j)).setText(firstButtonNameText);
                    }
                    secondButtonNameText = firstButtonNameText;
                }

            }

        } else {


            if (i == 9) {
//        If extra 5 points question checkBox was checked
                if (xtraQuestion) {
                    radioGroup.setVisibility(android.view.View.GONE);
                    hintImage.setImageResource(myImageList[i]);
                    EditText xtraAnswerText = findViewById(R.id.xtra_question_answer_textview);
                    xtraAnswerText.setVisibility(android.view.View.VISIBLE);
                    questionText = res.getString(R.string.xtraQuestionText,nameOfTheCountry[i] );
                    changeText.setText(questionText);
                    buttonSubmitAnswer.setVisibility(View.GONE);
                    Button buttonFinalAction = findViewById(R.id.final_action_button);
                    buttonFinalAction.setVisibility(android.view.View.VISIBLE);

                } else {
                    radioGroup.setVisibility(android.view.View.GONE);
                    buttonSubmitAnswer.setVisibility(View.GONE);
                    questionText = res.getString(R.string.finalScore, totalPoints );
                    changeText.setTextSize(30);
                    changeText.setText(questionText);

//        If send e-mail was checked
                    if (sendEmail) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, "");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Total score for QuizzAPP ");
                        intent.putExtra(Intent.EXTRA_TEXT, "Your total score for QuizzApp is " + totalPoints + " points !");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }

                }
            }

        }
    }


    public void submitAnswer(View View) {
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
        String scoreText;
        Resources res = getResources();
        String[] rightAnswer = res.getStringArray(R.array.answer);

//     If any radio Button is checked
        if (selectedRadioButtonID != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonID);
            String selectedRadioButtonText = selectedRadioButton.getText().toString();

//        If the right answer is checked
            if (selectedRadioButtonText.equals(rightAnswer[i])) {
                totalPoints += 1;
            }
            if (i <= 8) {
                i += 1;
                radioGroup.clearCheck();
                nextQuestion(View);
            }
        } else {

//            Toast Message to allert that no answer is selected
            scoreText = res.getString(R.string.noAnswer);
            Context context = getApplicationContext();
            CharSequence text = scoreText;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void finalAction(View View) {
        Button buttonFinalAction = findViewById(R.id.final_action_button);
        TextView changeText = findViewById(R.id.question_text_view);
        EditText xtraAnswerText = findViewById(R.id.xtra_question_answer_textview);
        String answerTypped = xtraAnswerText.getText().toString();
        String questionText;
        Resources res = getResources();
        String[] rightAnswer = res.getStringArray(R.array.answer);


        xtraAnswerText.setVisibility(android.view.View.GONE);
        buttonFinalAction.setVisibility(android.view.View.GONE);

//    If the answer to extra question is correct
        if (answerTypped.equals(rightAnswer[i])) {
            totalPoints += 5;
        }
        questionText = res.getString(R.string.finalScore, totalPoints );
        changeText.setTextSize(30);
        changeText.setText(questionText);

//            If send e-mail was checked
        if (sendEmail) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Total score for QuizzAPP ");
            intent.putExtra(Intent.EXTRA_TEXT, "Your total score for QuizzApp is " + totalPoints + " points !");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
