package com.example.client;

import com.example.entity.Hand;
import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import static com.example.protocol.Constant.*;

public class Controller {
    @FXML
    public ImageView firstPlayerFirstCard;

    @FXML
    public ImageView firstPlayerSecondCard;

    @FXML
    public ImageView secondPlayerFirstCard;

    @FXML
    public ImageView secondPlayerSecondCard;

    @FXML
    public ImageView thirdPlayerFirstCard;

    @FXML
    public ImageView thirdPlayerSecondCard;

    @FXML
    public ImageView fourthPlayerFirstCard;

    @FXML
    public ImageView fourthPlayerSecondCard;

    @FXML
    public ImageView fifthPlayerFirstCard;

    @FXML
    public ImageView fifthPlayerSecondCard;

    @FXML
    public ImageView dealerFirstCart;

    @FXML
    public ImageView dealerSecondCart;

    @FXML
    public Button firstPlacePLus;

    @FXML
    public Button secondPlacePLus;

    @FXML
    public Button thirdPlacePLus;

    @FXML
    public Button fourthPlacePLus;

    @FXML
    public Button fifthPlacePLus;

    @FXML
    public Button firstPlaceMinus;

    @FXML
    public Button secondPlaceMinus;

    @FXML
    public Button thirdPlaceMinus;

    @FXML
    public Button fourthPlaceMinus;

    @FXML
    public Button fifthPlaceMinus;

    @FXML
    public Label firstPlaceScore;

    @FXML
    public Label secondPlaceScore;

    @FXML
    public Label thirdPlaceScore;

    @FXML
    public Label fourthPlaceScore;

    @FXML
    public Label fifthPlaceScore;

    @FXML
    public Label dealerScore;

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

    @FXML
    public Button button1;

    @FXML
    public Button button2;

    @FXML
    public Button button3;

    @FXML
    public Button button4;

    @FXML
    public Button button5;

    @FXML
    public Button leaveButton;

    private Player player;

    private Client client;

    public void onClickAction1(ActionEvent actionEvent) {
        button1.setDisable(true);
        player.setPlaceId((byte) 1);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
        leaveButton.setDisable(false);
    }

    public void onClickAction2(ActionEvent actionEvent) {
        button2.setDisable(true);
        player.setPlaceId((byte) 2);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
        leaveButton.setDisable(false);
    }

    public void onClickAction3(ActionEvent actionEvent) {
        button3.setDisable(true);
        player.setPlaceId((byte) 3);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
        leaveButton.setDisable(false);
    }

    public void onClickAction4(ActionEvent actionEvent) {
        button4.setDisable(true);
        player.setPlaceId((byte) 4);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
        leaveButton.setDisable(false);
    }

    public void onClickAction5(ActionEvent actionEvent) {
        button5.setDisable(true);
        player.setPlaceId((byte) 5);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
        leaveButton.setDisable(false);
    }

    public void drawPlayerPlaces(Player player) {
        switch (player.getPlaceId()) {
            case 1: {
                button1.setDisable(true);
                setText(button1, player.getUsername());
                break;
            }
            case 2: {
                button2.setDisable(true);
                setText(button2, player.getUsername());
                break;
            }
            case 3: {
                button3.setDisable(true);
                setText(button3, player.getUsername());
                break;
            }
            case 4: {
                button4.setDisable(true);
                setText(button4, player.getUsername());
                break;
            }
            case 5: {
                button5.setDisable(true);
                setText(button5, player.getUsername());
                break;
            }
        }
    }

    public void onClickActionLeave(ActionEvent actionEvent) {
        messageOutputStream.writeMessage(new Message(PLAYER_LEAVE, Parser.serialize(player)));
        leaveButton.setDisable(true);
    }

    private void setText(Button button, String username) {
        Platform.runLater(() -> button.setText(username));
    }

    public void transfer(Client client, MessageOutputStream messageOutputStream,
                         MessageInputStream messageInputStream, Player player, Stage stage) {
        this.client = client;
        client.setPlayer(player);
        client.setStage(stage);
        this.messageOutputStream = messageOutputStream;
        this.messageInputStream = messageInputStream;
        this.player = player;

    }

    public void deletePlayerPlace(byte i) {
        switch (i) {
            case 0: {
                button1.setDisable(false);
                setText(button1, "Занять");
                break;
            }
            case 1: {
                button2.setDisable(false);
                setText(button2, "Занять");
                break;
            }
            case 2: {
                button3.setDisable(false);
                setText(button3, "Занять");
                break;
            }
            case 3: {
                button4.setDisable(false);
                setText(button4, "Занять");
                break;
            }
            case 4: {
                button5.setDisable(false);
                setText(button5, "Занять");
                break;
            }
        }
    }

    public void drawFreePlaces(ArrayList<Player> players) {
        System.out.println(players);
        for (byte i = 0; i < 5; i++) {
            if (players.get(i) == null) {
                deletePlayerPlace(i);
            }
        }
        messageOutputStream.writeMessage(new Message(SERVER_DRAW_PLACES, "CHE".getBytes(StandardCharsets.UTF_8)));
    }

    public void disableLeaveButton() {
        leaveButton.setDisable(true);
    }

    public void drawPLayersCards(ArrayList<Hand> hands) {
        for (byte i = 0; i < 6; i++) {
            switch (i) {
                case 0: {
                    //рисовалка карт для дилера
                    Image[] carts = getImageByHand(hands.get(0));
                    Image secondCard = new Image("file:///C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\opponentCart.png");
                    dealerFirstCart.setImage(carts[0]);
                    dealerSecondCart.setImage(secondCard);
                    break;
                }
                case 1: {
                    //рисовалка карт для игрока под местом 1
                    Image[] carts = getImageByHand(hands.get(1));
                    firstPlayerFirstCard.setImage(carts[0]);
                    firstPlayerSecondCard.setImage(carts[1]);
                    break;
                }
                case 2: {
                    //рисовалка карт для игрока под местом 2
                    Image[] carts = getImageByHand(hands.get(2));
                    secondPlayerFirstCard.setImage(carts[0]);
                    secondPlayerSecondCard.setImage(carts[1]);
                    break;
                }
                case 3: {
                    //рисовалка карт для игрока под местом 3
                    Image[] carts = getImageByHand(hands.get(3));
                    thirdPlayerFirstCard.setImage(carts[0]);
                    thirdPlayerSecondCard.setImage(carts[1]);
                    break;
                }
                case 4: {
                    //рисовалка карт для игрока под местом 4
                    Image[] carts = getImageByHand(hands.get(4));
                    fourthPlayerFirstCard.setImage(carts[0]);
                    fourthPlayerSecondCard.setImage(carts[1]);
                    break;
                }
                case 5: {
                    //рисовалка карт для игрока под местом 5
                    Image[] carts = getImageByHand(hands.get(5));
                    fifthPlayerFirstCard.setImage(carts[0]);
                    fifthPlayerSecondCard.setImage(carts[1]);
                    break;
                }
            }
        }
    }

    private Image[] getImageByHand(Hand hand) {

        byte[] cards = hand.getCards();

        String secondImagePath = null;
        String firstImagePath = null;
        for (int i = cards.length - 1; i >= 0; i--) {
            if (cards[i] != 0) {
                secondImagePath = takePath(cards[i]);
                firstImagePath = takePath(cards[i - 1]);
                break;
            }
        }


        return new Image[] {new Image(firstImagePath), new Image(secondImagePath)};
    }

    private String takePath(byte i) {
        Random random = new Random();
        String imagePath = null;
        switch (i) {
            case 1: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartTuz.png";
                break;
            }
            case 2: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartTwo.png";
                break;
            }
            case 3: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartThree.png";
                break;
            }
            case 4: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartFour.png";
                break;
            }
            case 5: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartFive.png";
                break;
            }
            case 6: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartSix.png";
                break;
            }
            case 7: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartSeven.png";
                break;
            }
            case 8: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartEight.png";
                break;
            }
            case 9: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartNine.png";
                break;
            }
            case 10: {
                String[] cartsWithTenValue = new String[] {"C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartTen.png",
                        "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartDama.png",
                        "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartKing.png",
                        "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartValet.png"
                };
                imagePath = cartsWithTenValue[random.nextInt(4)];
                break;
            }
            case 11: {
                imagePath = "C:\\Users\\egoro\\OneDrive\\Рабочий стол\\ThirdSem\\blackjack\\project\\src\\main\\resources\\com\\example\\carts\\cartTuz.png";
                break;
            }
        }
        return "file:///" + imagePath;
    }

    public void drawCardsWhoAlreadyPlay(ArrayList<Hand> hands) {
        drawPLayersCards(hands);
    }

    public void drawPlusAndMinus(byte placeId) {
        switch (placeId) {
            case 1: {
                firstPlaceMinus.setVisible(true);
                firstPlacePLus.setVisible(true);
                break;
            }
            case 2: {
                secondPlaceMinus.setVisible(true);
                secondPlacePLus.setVisible(true);
                break;
            }
            case 3: {
                thirdPlaceMinus.setVisible(true);
                thirdPlacePLus.setVisible(true);
                break;
            }
            case 4: {
                fourthPlacePLus.setVisible(true);
                fourthPlaceMinus.setVisible(true);
                break;
            }
            case 5: {
                fifthPlacePLus.setVisible(true);
                fifthPlaceMinus.setVisible(true);
                break;
            }
        }
    }

    public void giveOneMoreCardFirstPlace(ActionEvent actionEvent) {
        messageOutputStream.writeMessage(new Message(PLAYER_TAKE_ONE_MORE_CARD, Parser.serialize(new String("1"))));
    }

    public void giveOneMoreCardSecondPlace(ActionEvent actionEvent) {
        messageOutputStream.writeMessage(new Message(PLAYER_TAKE_ONE_MORE_CARD, Parser.serialize(new String("2"))));
    }

    public void giveOneMoreCardThirdPlace(ActionEvent actionEvent) {
        messageOutputStream.writeMessage(new Message(PLAYER_TAKE_ONE_MORE_CARD, Parser.serialize(new String("3"))));
    }

    public void giveOneMoreCardFourthPlace(ActionEvent actionEvent) {
        messageOutputStream.writeMessage(new Message(PLAYER_TAKE_ONE_MORE_CARD, Parser.serialize(new String("4"))));
    }

    public void giveOneMoreCardFifthPlace(ActionEvent actionEvent) {
        messageOutputStream.writeMessage(new Message(PLAYER_TAKE_ONE_MORE_CARD, Parser.serialize(new String("5"))));
    }

    public void drawPlayersScore(ArrayList<Hand> hands) {
        for (byte i = 0; i < 6; i++) {
            switch (i) {
                case 0: {
                    dealerScore.setText(String.valueOf(hands.get(i).getCards()[0]));
                    break;
                }
                case 1: {
                    firstPlaceScore.setText(getCardsSum(hands.get(i)));
                    break;
                }
                case 2: {
                    secondPlaceScore.setText(getCardsSum(hands.get(i)));
                    break;
                }
                case 3: {
                    thirdPlaceScore.setText(getCardsSum(hands.get(i)));
                    break;
                }
                case 4: {
                    fourthPlaceScore.setText(getCardsSum(hands.get(i)));
                    break;
                }
                case 5: {
                    fifthPlaceScore.setText(getCardsSum(hands.get(i)));
                    break;
                }
            }
        }
    }

    private String getCardsSum(Hand hand) {
        byte sum = 0;
        for (byte rank : hand.getCards()) {
            sum += rank;
        }
        if (sum == 22) {
            sum = 12;
        }
        return String.valueOf(sum);
    }

    public void drawWonMessage(Player player) {
        System.out.println(player);
        drawWinScene(player.getUsername());
        hideMinusAndPlus(player.getPlaceId());
    }

    private void hideMinusAndPlus(int placeId) {
        switch (placeId) {
            case 1 : {
                firstPlaceMinus.setVisible(false);
                firstPlacePLus.setVisible(false);
                break;
            }
            case 2 : {
                secondPlaceMinus.setVisible(false);
                secondPlacePLus.setVisible(false);
                break;
            }
            case 3 : {
                thirdPlaceMinus.setVisible(false);
                thirdPlacePLus.setVisible(false);
                break;
            }
            case 4 : {
                fourthPlaceMinus.setVisible(false);
                fourthPlacePLus.setVisible(false);
                break;
            }
            case 5 : {
                fifthPlaceMinus.setVisible(false);
                fifthPlacePLus.setVisible(false);
                break;
            }

        }
    }

    private void drawWinScene(String username) {
        showAlert(Alert.AlertType.INFORMATION, "Congratulations!",
                username + " won dealer");
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public void stopTakenCardFirstPlace(ActionEvent actionEvent) {
        hideMinusAndPlus(1);
        messageOutputStream.writeMessage(new Message(PLAYER_DONT_TAKEN_CARD_ANYMORE, Parser.serialize(new String("1"))));
    }

    public void stopTakenCardSecondPlace(ActionEvent actionEvent) {
        hideMinusAndPlus(2);
        messageOutputStream.writeMessage(new Message(PLAYER_DONT_TAKEN_CARD_ANYMORE, Parser.serialize(new String("2"))));
    }

    public void stopTakenCardThirdPlace(ActionEvent actionEvent) {
        hideMinusAndPlus(3);
        messageOutputStream.writeMessage(new Message(PLAYER_DONT_TAKEN_CARD_ANYMORE, Parser.serialize(new String("3"))));
    }

    public void stopTakenCardFourthPlace(ActionEvent actionEvent) {
        hideMinusAndPlus(4);
        messageOutputStream.writeMessage(new Message(PLAYER_DONT_TAKEN_CARD_ANYMORE, Parser.serialize(new String("4"))));
    }

    public void stopTakenCardFifthPlace(ActionEvent actionEvent) {
        hideMinusAndPlus(5);
        messageOutputStream.writeMessage(new Message(PLAYER_DONT_TAKEN_CARD_ANYMORE, Parser.serialize(new String("5"))));
    }

    public void drawLoserMessage(Player player) {
        drawLoserScene(player.getUsername());
        hideMinusAndPlus(player.getPlaceId());
    }
    private void drawLoserScene(String username) {
        showAlert(Alert.AlertType.INFORMATION, "Noob!",
                username + " loser!");
    }

    public void drawTwoLastCards(ArrayList<Hand> hands) {
        drawPLayersCards(hands);
    }
    public void drawPlayersScoreMore(ArrayList<Hand> hands) {
        for (byte i = 0; i < 6; i++) {
            switch (i) {
                case 0: {
                    dealerScore.setText(String.valueOf(hands.get(i).getCards()[0]));
                    break;
                }
                case 1: {
                    firstPlaceScore.setText(getCardsSumSecondary(hands.get(i)));
                    break;
                }
                case 2: {
                    secondPlaceScore.setText(getCardsSumSecondary(hands.get(i)));
                    break;
                }
                case 3: {
                    thirdPlaceScore.setText(getCardsSumSecondary(hands.get(i)));
                    break;
                }
                case 4: {
                    fourthPlaceScore.setText(getCardsSumSecondary(hands.get(i)));
                    break;
                }
                case 5: {
                    fifthPlaceScore.setText(getCardsSumSecondary(hands.get(i)));
                    break;
                }
            }
        }
    }

    private String getCardsSumSecondary(Hand hand) {
        byte sum = 0;
        for (byte rank : hand.getCards()) {
            sum += rank;
        }
        return String.valueOf(sum);
    }

    public void drawDealerCardsAndScore(Hand hand) {
        Image[] images = getImageByHand(hand);
        dealerFirstCart.setImage(images[0]);
        dealerSecondCart.setImage(images[1]);
        dealerScore.setText(getCardsSum(hand));
    }
}