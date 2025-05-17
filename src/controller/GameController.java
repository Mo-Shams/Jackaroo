package controller;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import view.boardView.CellView;
import view.boardView.HomeZoneView;
import view.boardView.TrackView;
import view.marbleView.MarbleView;
import view.CardsView.CardView;
import view.CardsView.FirePitView;
import view.CardsView.HandView;
import engine.Game;
import model.card.Card;
import model.player.Marble;
import exception.GameException;
import exception.InvalidCardException;import exception.InvalidMarbleException;


public class GameController extends Controller {
    private final Game game;
    private TrackView trackView;
    private ArrayList<CellView> cells;
    private FirePitView firePitView;
    
    // for the player turn 
    private CardView selectedCardView;
    private final List<MarbleView> selectedMarbles = new ArrayList<>();
    
    public GameController(Game game, Stage stage) {
        super(stage);
        this.game = game;
    }
    
    public void setTrackView(TrackView trackView) {
        this.trackView = trackView;
        this.cells = new ArrayList<>(trackView.getCellViewMap().values());  // Ensure this returns a map with valid keys
    }
    
    public void addHomeZoneMarbleHandler(HomeZoneView homeZoneView) {
        // Listen for clicks on any marble in the home zone
        homeZoneView.getMarbleViews().forEach(marbleView -> {
            marbleView.setOnMouseClicked(evt -> {
                /*try {
                	game.fieldMarble();
                	trackView.animateMarbleToCell(marbleView, trackView.getBaseCellForMarble(marbleView));
                } catch (Exception e) {
                	System.err.println(e.getMessage());
                }*/
            });
        });
    } 
    
    public void addControlButtons (Button playBtn, Button deselectBtn) {
        if (game.canPlayTurn()) {
        	playBtn.setOnMouseClicked(evt -> {
           	 	try {
           	 		if (game.getCurrentPlayer().getSelectedCard() == null) {
           	 			System.out.println("You haven't chosen a card yet");
           	 			return;
           	 		}
           	 		game.playPlayerTurn();
                } catch (GameException e) {
                	System.err.println(e.getMessage());
                }
           });
        	deselectBtn.setOnMouseClicked(evt -> {
                // Clear backend and UI selection
                game.deselectAll();
                clearSelectionUI();
            });
        }
    }
    
    public void addHoverEffect(HomeZoneView homeZone){
    	 homeZone.getMarbleViews().forEach(marbleView -> {
             marbleView.setOnMouseEntered(evt -> {
                 // Animate this marble from home into base on the track
                 marbleView.applyGlow(Color.PURPLE);
             });
             marbleView.setOnMouseExited(evt ->{
            	 marbleView.setEffect(null);
             });
         });
    }
    
    public void addMarbleClickHandler (MarbleView mv) {
    	mv.setOnMouseClicked(evt -> {
    		Marble model = mv.getMarble(); 
    		if (!selectedMarbles.contains(mv)) {
    			try {
    				game.selectMarble(model);
    				selectedMarbles.add(mv);
    				
    				mv.applyGlow(Color.DEEPSKYBLUE);
    				mv.scaleMarble(1.2);
    			} catch (InvalidMarbleException e) {
    				System.err.println(e.getMessage());
    			}
    		} else {
    			game.deselectAll(); 
    			clearSelectionUI();
    		}
    	});
    }

    public void addCardClickHandler(CardView cardView, HandView handView, FirePitView firePitView) {
        cardView.setOnMouseClicked(event -> {
            Card card = cardView.getCard();
            if (event.getClickCount() == 2) {
<<<<<<< Updated upstream
                // Double click detected
            	cardView.sendToFirePit(firePitView);
=======
            	cardView.sendToFirePit(firePitView, handView);
            	
>>>>>>> Stashed changes
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
                    
                    this.selectedCardView = cardView;
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
    
    private void clearSelectionUI() {
        // clear card
        if (selectedCardView != null) {
            selectedCardView.setEffect(null);
            selectedCardView.scaleCard(1.0);
            selectedCardView.setSelected(false);
            selectedCardView = null;
        }

        // clear marbles
        for (MarbleView mv : selectedMarbles) {
            mv.setEffect(null);
            mv.setScaleX(1.0); 
            mv.setScaleY(1.0);
        }
        selectedMarbles.clear();

        // also tell the model
        game.deselectAll();
    }   
}