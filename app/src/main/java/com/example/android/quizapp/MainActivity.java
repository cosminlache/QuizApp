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

    int[] rightAnswerIndex = new int[]{1, 2, 0, 0, 2, 1, 2, 0, 2, 1};
    boolean xtraQuestionIsChecked = false;
    boolean sendEmailIsChecked = false;
    private RadioGroup radioGroup;
    private Button buttonSubmitAnswer;
    private TextView questionTextView;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radio_group);
        buttonSubmitAnswer = findViewById(R.id.submit_answer_button);
        questionTextView = findViewById(R.id.question_text_view);
        res = getResources();
    }

//  When the start button is pressed
    public void firstTime(View View) {

        // Verify if the checkBoxes are checked and store the answers in the boolean variables
        CheckBox xtraQuestionCheckBox = findViewById(R.id.xtraQuestion);
        xtraQuestionIsChecked = xtraQuestionCheckBox.isChecked();
        CheckBox sendEmailCheckBox = findViewById(R.id.send_email);
        sendEmailIsChecked = sendEmailCheckBox.isChecked();

        // Hide the checkBox views
        xtraQuestionCheckBox.setVisibility(android.view.View.GONE);
        sendEmailCheckBox.setVisibility(android.view.View.GONE);

        // Hide the start_button and activate the submit_answer button and radioGroup
        Button buttonStart = findViewById(R.id.start_button);
        buttonStart.setVisibility(android.view.View.GONE);
        buttonSubmitAnswer.setVisibility(android.view.View.VISIBLE);
        radioGroup.setVisibility(android.view.View.VISIBLE);

        nextQuestion(View);
    }

    public void nextQuestion(View view) {
        ImageView hintImage = findViewById(R.id.hint_image_view);
        String questionText, firstButtonNameText, secondButtonNameText;
        Random r = new Random();
        int randomIndex;
        String[] nameOfTheCountry = res.getStringArray(R.array.country);
        String[] randomName = res.getStringArray(R.array.randomAnswer);
        String[] rightAnswer = res.getStringArray(R.array.answer);

        if (i <= 8) {

//        Change the image hint according to the list
            hintImage.setImageResource(myImageList[i]);

//        Change the text of the question according to the list and image hint
            questionText = res.getString(R.string.newQuestion, nameOfTheCountry[i]);
            questionTextView.setText(questionText);

//        Change the radioButtons text for the right answer and other random answers from the list
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
                if (xtraQuestionIsChecked) {
                    radioGroup.setVisibility(android.view.View.GONE);
                    hintImage.setImageResource(myImageList[i]);
                    EditText xtraAnswerText = findViewById(R.id.xtra_question_answer_textview);
                    xtraAnswerText.setVisibility(android.view.View.VISIBLE);
                    questionText = res.getString(R.string.xtraQuestionText, nameOfTheCountry[i]);
                    questionTextView.setText(questionText);
                    buttonSubmitAnswer.setVisibility(View.GONE);
                    Button buttonFinalAction = findViewById(R.id.final_action_button);
                    buttonFinalAction.setVisibility(android.view.View.VISIBLE);

                } else {
                    radioGroup.setVisibility(android.view.View.GONE);
                    buttonSubmitAnswer.setVisibility(View.GONE);
                    questionText = res.getString(R.string.finalScore, totalPoints);
                    questionTextView.setTextSize(30);
                    questionTextView.setText(questionText);

//        If send e-mail checkBox was checked
                    if (sendEmailIsChecked) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, "");
                        intent.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.emailExtraSubject));
                        intent.putExtra(Intent.EXTRA_TEXT, res.getString(R.string.emailExtraText, totalPoints));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    public void submitAnswer(View View) {
        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
        String scoreText;
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
        EditText xtraAnswerText = findViewById(R.id.xtra_question_answer_textview);
        String answerTypped = xtraAnswerText.getText().toString();
        String finalText;
        String[] rightAnswer = res.getStringArray(R.array.answer);
        xtraAnswerText.setVisibility(android.view.View.GONE);
        buttonFinalAction.setVisibility(android.view.View.GONE);

//    If the answer to extra question is correct
        if (answerTypped.equals(rightAnswer[i])) {
            totalPoints += 5;
        }
        finalText = res.getString(R.string.finalScore, totalPoints);
        questionTextView.setTextSize(30);
        questionTextView.setText(finalText);

//            If send e-mail checkBox was checked
        if (sendEmailIsChecked) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "");
            intent.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.emailExtraSubject));
            intent.putExtra(Intent.EXTRA_TEXT, res.getString(R.string.emailExtraText, totalPoints));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
