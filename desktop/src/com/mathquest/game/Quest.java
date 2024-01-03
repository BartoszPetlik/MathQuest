package com.mathquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

public class Quest {
    Skin skin;
    Random random;
    int keys;
    Label question,equation;
    Label label1, label2;
    int number1, number2, type;
    int wrongAnswer, correctAnswer;
    String equationString;
    Dialog questDialog;
    Stage stage;

    boolean status, again;
    Table table;
    public Quest (Stage stage) {
        this.stage = stage;
        skin = new Skin(Gdx.files.internal("shadeui/uiskin.json"));
        random = new Random();
        questDialog = new Dialog("Quest", skin) {
            @Override
            public float getPrefWidth() {
                return 600;
            }

            @Override
            public float getPrefHeight() {
                return 400;
            }
        };
    }
    public void operation () {
        status = false;
        type = random.nextInt(1,5);

        if (type == 1) { //addition
            number1 = random.nextInt(1,20);
            number2 = random.nextInt(1,21);

            question = new Label("Jaki jest wynik dodawnia?",skin,"title-plain");
            equationString = number1 + " + " + number2 + " = ?";

            correctAnswer = number1 + number2;
            while (true) {
                wrongAnswer = random.nextInt(1,51);
                if (wrongAnswer != correctAnswer) {
                    break;
                }
            }

        }else if (type == 2) { //substraction
            while (true) {
                number1 = random.nextInt(1,20);
                number2 = random.nextInt(1,21);
                if (number1 >= number2) {
                    break;
                }
            }
            question = new Label("Jaki jest wynik odejmowania?",skin,"title-plain");
            equationString = number1 + " - " + number2 + " = ?";

            correctAnswer = number1 - number2;
            while (true) {
                wrongAnswer = random.nextInt(1,51);
                if (wrongAnswer != correctAnswer) {
                    break;
                }
            }
        }else if (type == 3) {
            number1 = random.nextInt(1,11);
            number2 = random.nextInt(1,11);

            question = new Label("Jaki jest wynik mnozenia?",skin,"title-plain");
            equationString = number1 + " * " + number2 + " = ?";

            correctAnswer = number1 * number2;
            while (true) {
                wrongAnswer = random.nextInt(1,101);
                if (wrongAnswer != correctAnswer) {
                    break;
                }
            }

        }else if (type == 4) {
            number1 = random.nextInt(1,11);
            number2 = random.nextInt(1,11);
            int number = number1 * number2;

            question = new Label("Jaki jest wynik dzielenia?",skin,"title-plain");
            equationString = number + " : " + number2 + " = ?";

            correctAnswer = number1;
            while (true) {
                wrongAnswer = random.nextInt(1,11);
                if (wrongAnswer != correctAnswer) {
                    break;
                }
            }
        }

        equation = new Label(equationString, skin, "title-plain");

        table = new Table();
        table.center();
        questDialog.getContentTable().add(table);



        question.setAlignment(Align.center);
        equation.setAlignment(Align.center);
        table.add(question).padBottom(30).row();
        table.add(equation).padBottom(30).row();

        TextButton wrongButton = new TextButton(String.valueOf(wrongAnswer), skin,"round");
        TextButton correctButton = new TextButton(String.valueOf(correctAnswer), skin,"round");

        correctButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                questDialog.hide();
                questDialog.remove();


                label1 = new Label("Twoja odpowiedz jest prawidlowa!", skin, "title-plain");
                label2 = new Label("Zdobywasz 1 klucz!", skin, "title-plain");
                questAnswer(label1, label2, false);
            }
        });

        wrongButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                questDialog.hide();
                questDialog.remove();

                label1 = new Label("Twoja odpowiedz jest nieprawidlowa!", skin, "title-plain");
                label2 = new Label("Sprobuj ponownie!", skin, "title-plain");
                questAnswer(label1, label2, true);
            }
        });
        type = random.nextInt(1,3);
        if (type == 1) {
            questDialog.button(wrongButton, true);
            questDialog.button(correctButton, true);
        } else if (type == 2) {
            questDialog.button(correctButton, true);
            questDialog.button(wrongButton, true);
        }
        questDialog.show(stage);
    }

    public void questAnswer(Label label1, Label label2, boolean tryAgain) {
        questDialog = new Dialog("Quest", skin) {
            @Override
            public float getPrefWidth() {
                return 500;
            }

            @Override
            public float getPrefHeight() {
                return 300;
            }
        };

        table = new Table();
        table.center();
        questDialog.getContentTable().add(table);

        label1.setAlignment(Align.center);
        label2.setAlignment(Align.center);
        table.add(label1).padBottom(30).row();
        table.add(label2).padBottom(30).row();

        TextButton button = new TextButton("Ok", skin,"round");
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                questDialog.hide();
                questDialog.remove();

                if (tryAgain == true) {
                    again = true;
                }else {
                    status = true;
                    keys += 1;
                }
            }
        });
        questDialog.button(button, true);

        questDialog.show(stage);

    }


}
