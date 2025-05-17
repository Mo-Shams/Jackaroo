package controller;

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
    	Card card = cardView.getCard();
    	
    	cardView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Double click detected
            	cardView.flip();
//            	firePitView.sendCardToFirePit(cardView, handView);// Or any other action
            }
            if (!cardView.isSelected()) {
                try {
                	game.deselectAll();
                    game.selectCard(card);
                    handView.clearSelection();
                    cardView.updateSelectionAnimation(true);
                } catch (InvalidCardException e) {
                    System.out.println(e.getMessage());
                }
            } else {
            	game.deselectAll();
                cardView.updateSelectionAnimation(false);
            }
        });
    }

}
