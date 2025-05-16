package controller;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.card.Card;
import view.CardsView.CardView;
import view.CardsView.FirePitView;
import view.CardsView.HandView;
import engine.Game;
import exception.InvalidCardException;

public class GameController extends Controller {
    private final Game game;

    public GameController(Game game, Stage stage) {
        super(stage);
        this.game = game;
    }

    public void addCardClickHandler(CardView cardView, HandView handView, FirePitView firePitView) {
        cardView.setOnMouseClicked(event -> {
            Card card = cardView.getCard();
            if (event.getClickCount() == 2) {
                // Double click detected
            	cardView.sendToFirePit(firePitView);
//            	firePitView.sendCardToFirePit(cardView, handView);// Or any other action
            }
            if (!cardView.isSelected()) {
                try {
                	game.deselectAll();
                    game.selectCard(card);
                    CardView otherSelected = handView.OtherselectedCard(cardView);
                    if (otherSelected != null) {
                        otherSelected.setEffect(null);
                        otherSelected.scaleCard(1.0);
                        otherSelected.setSelected(false);
                    }

                    cardView.setSelected(true); 
                    cardView.applyGlow(Color.DODGERBLUE); 
                    cardView.scaleCard(1.1);// selection happens first

                } catch (InvalidCardException e) {
                    System.out.println(e.getMessage());
                }
            } else {
            	game.deselectAll();
                cardView.setEffect(null);
                cardView.scaleCard(1.0);
                cardView.setSelected(false);
            }
        });
    }

    public void addCardHoverEffect(CardView cardView) {
        cardView.setPickOnBounds(false);

        cardView.setOnMouseEntered(event -> {
            if (!cardView.isSelected()) {
                cardView.applyGlow(Color.YELLOW);
                cardView.scaleCard(1.1);
            }
        });

        cardView.setOnMouseExited(event -> {
            if (!cardView.isSelected()) {
                cardView.setEffect(null);
                cardView.scaleCard(1.0);
            } 
        });
    }
}
