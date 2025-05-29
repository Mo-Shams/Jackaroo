package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Colour;
import model.card.Card;
import model.card.standard.Ace;
import model.card.standard.Four;
import model.card.standard.Jack;
import model.card.standard.King;
import model.card.standard.Queen;
import model.card.standard.Seven;
import model.card.standard.Standard;
import model.card.standard.Ten;
import model.card.wild.Burner;
import model.card.wild.Saver;
import model.player.Marble;
import model.player.Player;
import scene.EndScreenScene;
import scene.GameScene;
import view.GameView;
import view.boardView.CellView;
import view.boardView.MarbleView;
import view.playersView.CardView;
import view.playersView.FirePitView;
import view.playersView.HandView;
import view.playersView.PlayerProfile;
import engine.Game;
import engine.board.Cell;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import exception.SplitOutOfRangeException;


public class GameController{
	private final Game game ; 
	private final GameScene gameScene ; 
	private final GameView gameView;
	private boolean win;
	public static int test = 1 ; 
	
	private final Stage primaryStage ; 
	
	public GameController(String name, Stage primaryStage) throws IOException {
		win = false;
		this.primaryStage = primaryStage ;
		game = new Game(name);
		gameScene = new GameScene(game);
		gameView = gameScene.getGameView();
		run();
		addShortCuts();
		addEventHandlers();
		ThemesManager.changeTheme(0);
		
	}
	public Game getGame() {
		return game;
	}
	public GameScene getGameScene() {
		return gameScene;
	}
	
	// -------------------------------------- Event handlers ----------------------------------
	

	public void addShortCuts(){
		((StackPane)gameView).setOnKeyPressed((KeyEvent e1) ->{
			try{
				if(e1.getCode() == KeyCode.S){
					game.fieldMarble(0);
				}
				else if(e1.getCode() == KeyCode.A){
					game.fieldMarble(1);
				}
				else if(e1.getCode() == KeyCode.W){
					game.fieldMarble(2);
				}
				else if(e1.getCode() == KeyCode.D){
					game.fieldMarble(3);
				}
				else if(e1.getCode() == KeyCode.T){
					ThemesManager.changeTheme(test++%5);
				}
				else if(e1.getCode() == KeyCode.F){
					win = true;
				}
				gameView.updateBoardView();
				addEventHandlers();
			}
			catch(IllegalDestroyException | CannotFieldException e){
				GameScene.showExceptionPopup(e.getMessage(), gameScene.getRoot());
			}
		});
	}
	
	
	public void addEventHandlers(){
		HandView playerHand = gameView.getHandViews().get(0);
		for(CardView cardView : playerHand.getCardViews()){
			cardView.setOnMouseClicked(e ->{
		//		ThemesManager.changeTheme(0);
				game.deselectCard();
				if(!cardView.isSelected()){
					playerHand.clearSelection();
					try {
						handleCells(false);
						game.selectCard(cardView.getCard());
						cardView.setSelected(true);
						if(game.getCurrentPlayer().getSelectedMarbles().size() == 1) handleCells(true);
						else handleCells(false);
					} catch (InvalidCardException e1) {
						GameScene.showExceptionPopup(e1.getMessage(), gameScene.getRoot());
					}
				}
				else {
					cardView.setSelected(false);
					handleCells(false);
				}
			});
		}
		
		for(MarbleView marbleView: MarbleView.MarbleToViewMap.values()){
			marbleView.setSelected(false);
			marbleView.removeHoverEffect();
		}
		
		ArrayList<Marble> actionableMarbles = game.getBoard().getActionableMarbles();
		for(Marble marble : actionableMarbles){
			MarbleView marbleView = MarbleView.MarbleToViewMap.get(marble);
			marbleView.addHoverEffect();
			marbleView.setOnMouseClicked(e ->{
				if(!marbleView.isSelected()){
					try {
						game.selectMarble(marble);
						marbleView.setSelected(true);
						if(game.getCurrentPlayer().getSelectedMarbles().size() == 1) handleCells(true);
						else handleCells(false);
					} catch (Exception e1) {
						GameScene.showExceptionPopup(e1.getMessage(), gameScene.getRoot());
					}
				}
				else{
					game.deselectMarble(marble);
					marbleView.setSelected(false);
					if(game.getCurrentPlayer().getSelectedMarbles().size() == 1) handleCells(true);
					else handleCells(false);
				}
			});
		}
		
	}
	
	private void handleCells(boolean selected) {
		Card selectedCard = game.getPlayers().get(0).getSelectedCard();
		if(selected && (selectedCard instanceof Standard)){
				Marble selectedMarble = game.getPlayers().get(0).getSelectedMarbles().get(0);
				MarbleView marbleView = MarbleView.MarbleToViewMap.get(selectedMarble);
				int steps = ((Standard) selectedCard).getRank();
				Color color = Color.valueOf(marbleView.getMarble().getColour().toString());
				ArrayList<Cell> path = game.getBoard().validateSteps_(selectedMarble, (steps==4)?-4:steps);
				if(path == null || !(game.getBoard().validatePath_(selectedMarble, path, steps == 13)) || 
						(steps != 5 && (!selectedMarble.getColour().toString().equals(game.getPlayers().get(0).getColour().toString())))) {
					handleCells(false);
					return;
				}
				CellView start = (CellView) marbleView.getParent();
				if(steps != 4){
					while(steps-- > 0){
						if(game.getBoard().getTrack().get(98) == start.getCell()) start = start.getEnter();
						else start = start.getNext();
						start.colorCell(color);
					}
				}else{
					while(steps-- > 0){
						start = start.getPrev();
						start.colorCell(color);
					}
				}
		}else{
			ThemesManager.changeTheme(ThemesManager.theme);
		}
	}
	
	public void clearPlayerSelections(){
		Player player = game.getPlayers().get(0);
		CardView cardView = CardView.cardToViewMap.get(player.getSelectedCard());
		cardView.setSelected(false);
		handleCells(false);
		cardView.scaleCard(1.15);
		for(Marble marble : player.getSelectedMarbles()){
			MarbleView.MarbleToViewMap.get(marble).setSelected(false);
		}
	}
	public void drawAllHands(){
		ArrayList<Player> players = game.getPlayers();
		int i = 0;
		for(HandView handView : gameView.getHandViews()){
			handView.drawCardViews(players.get(i).getHand());
			i++;
		}
		ThemesManager.changeTheme(ThemesManager.theme);
	}
	
	
	public void canPlayTurn(boolean canPlay){
		Button playButton = gameView.getPlayButton();
		if(canPlay){
			playButton.setOnMouseClicked(evt ->{
				//if (evt.getClickCount() == 2)return;
				HandView handView = gameView.getHandViews().get(0);
				FirePitView firePitView = gameView.getFirePitView();
				Card selectedCard = game.getPlayers().get(0).getSelectedCard(); 
				CardView cardView = CardView.cardToViewMap.get(game.getPlayers().get(0).getSelectedCard());
				if (selectedCard instanceof Seven && game.getPlayers().get(0).getSelectedMarbles().size() == 2) {
					SplitDistanceView();
					return ;
				}
				try {
					game.playPlayerTurn();
					doTheAnimation(0);
					playButton.setOnMouseClicked(null);
				
				} 
				catch (GameException e) {
					GameScene.showExceptionPopup(e.getMessage(), gameScene.getRoot());
					if(cardView != null){
						//cardView.dimCard();
						cardView.sendToFirePit(firePitView, 0, true).play();
						clearPlayerSelections();
						game.endPlayerTurn();
						gameView.updatePlayerProfiles();
					}
					game.deselectAll();
					handView.clearSelection();		
					playButton.setOnMouseClicked(null);
					run();
					
				}
			});
		}
		else
			playButton.setOnMouseClicked(null);
		
	}
	
	public void continueAfter7 () {
		HandView handView = gameView.getHandViews().get(0);
		FirePitView firePitView = gameView.getFirePitView();
		CardView cardView = CardView.cardToViewMap.get(game.getPlayers().get(0).getSelectedCard());
		try {
			game.playPlayerTurn();
			doTheAnimation(0);
		} 
		catch (GameException e) {
			GameScene.showExceptionPopup(e.getMessage(), gameScene.getRoot());
			if(cardView != null){
				//cardView.dimCard();
				cardView.sendToFirePit(firePitView, 0, true).play();
				game.endPlayerTurn();
				gameView.updatePlayerProfiles();
			}
			game.deselectAll();
			handView.clearSelection();				
			run();
		}
	}
	
	public void SplitDistanceView() {
        CompletableFuture<Integer> selectedNumberFuture = new CompletableFuture<>();

        // Overlay background
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlay.setPrefSize(2000, 1200);

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        // Central message
        Label message = new Label("Select split distance for 7");
        message.setTextFill(Color.WHITE);
        message.setFont(Font.font(24));

        // Button row
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GOLD);
        shadow.setRadius(10);
        
        for (int i = 1; i <= 6; i++) {
            Button btn = new Button(String.valueOf(i));
            btn.setPrefSize(60, 60);
            btn.setFont(Font.font(18));
            btn.setStyle(
                "-fx-background-radius: 12;" +
                "-fx-background-color: linear-gradient(to top, #006400, #00FF00);" +
                "-fx-text-fill: white;"
            );
            btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            	btn.setEffect(shadow);
            });
            btn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            	btn.setEffect(null);
            });
            
            final int number = i;
            btn.setOnAction(e -> {
                selectedNumberFuture.complete(number);   
                gameScene.getRoot().getChildren().remove(overlay);
                
                
               try {
				game.editSplitDistance(number);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				GameScene.showExceptionPopup(e1.getMessage(), gameScene.getRoot());
			}
                
               continueAfter7();
            });

            buttonBox.getChildren().add(btn);
        }

        container.getChildren().addAll(message, buttonBox);
        // VBox.setMargin(buttonBox, new Insets(0, 0, 350, 0));
        overlay.getChildren().add(container);

        StackPane.setAlignment(container, Pos.CENTER);

        gameScene.getRoot().getChildren().add(overlay);

    }
	
	
	
	//------------------------- the main Game Animation WorkFlow --------------------------
	public void doTheAnimation(int playerIndex){
		
		//---------------------- getting the needed elements ----------------------
		
		
		Card selectedCard = game.getPlayers().get(playerIndex).getSelectedCard(); 
		ArrayList<Marble> selectedMarbles = game.getPlayers().get(playerIndex).getSelectedMarbles();
		CardView selectedCardView = CardView.cardToViewMap.get(selectedCard);
		FirePitView firePitView = gameView.getFirePitView();

		
		
		//-------------------------------- the main animation ---------------------------------------------
		
		
		SequentialTransition animation = new SequentialTransition();
		
		
		//------------------------ add thinking period for the CPUs -----------------------------------------------
		
		if(playerIndex > 0){
			PauseTransition thinking = new PauseTransition(Duration.seconds(1.5)); //thinking for 3 seconds
			thinking.setOnFinished(e -> {
				if (SoundManager.Effects) SoundManager.playEffect("throw_Card.wav");
			});
			animation.getChildren().add(thinking);
		} else if (SoundManager.Effects) SoundManager.playEffect("throw_Card.wav");
		
		
		
		
		//---------------------- the first animation: card to firePit + pause --------------------------------------
		
		
		animation.getChildren().add(selectedCardView.sendToFirePit(firePitView, playerIndex, false));
		PauseTransition pt1 = new PauseTransition(Duration.seconds(0.75)); // 0.75 second pause
		animation.getChildren().add(pt1);
		
		
		//------------------------------------ adding the discard animation ------------------------------------------
		
		
		CardView discardedCardView = null;
		for(int discardedPlayerIndex = 0; discardedPlayerIndex < 4; discardedPlayerIndex++){
			HandView handView = gameView.getHandViews().get(discardedPlayerIndex);
			if(handView.getCardViews().size() <= handView.getHand().size()) continue;
			for(CardView cardView: handView.getCardViews()){
				if(!handView.getHand().contains(cardView.getCard())) {
					discardedCardView = cardView;
					SequentialTransition sq = cardView.sendToFirePit(firePitView ,discardedPlayerIndex, true);
					animation.getChildren().add(sq);
					break;
				}
			}
		}
		
//		if(discardedCardView != null){
//			CardView discardedCardViewContainer = discardedCardView;
//			pt1.setOnFinished(e -> {
//				discardedCardViewContainer.dimCard();
//			});
//		}
		
		
		//------------------------------------ getting needed elements for movement -------------------------------------
		
		
		int steps = getAction(selectedCard,selectedMarbles);
		
		
		//----------------------------------- adding movement animation --------------------------------------------------
		
		
		if (steps > 0 || steps == -4){
			MarbleView marbleView = MarbleView.MarbleToViewMap.get(selectedMarbles.get(0));
			CellView start = (CellView) marbleView.getParent();
			int i = gameView.getTrackView().getCellViews().indexOf(start);
			// CellView target = gameView.getTrackView().getCellViews().get((i + steps + 100)%100);
			CellView target = CellView.cellToViewMap.get(game.getBoard().getTargetCell());
			pt1.setOnFinished(e -> {
				if(steps > 0)
					start.moveMarbleTo(target);
				else
					start.moveBackword(4);
			});
			PauseTransition timer = new PauseTransition(Duration.millis(CellView.getMarbleSpeed() * Math.abs(steps) + 200));
			animation.getChildren().add(timer);
			
			timer.setOnFinished(e -> {
				gameView.updateBoardView();
				if(target.isWasTrap()){
					gameScene.showSeeingTrappedEffect().play();
					// SoundManager.playEffect("trap.wav");
					target.setWasTrap(false);
				}
			});
		}
		else{
			pt1.setOnFinished(e -> {
				gameView.updateBoardView();
				if (steps == -1) SoundManager.playEffect("field.wav");
				if (steps == -6) SoundManager.playEffect("saving.wav");
				if (steps == -7) SoundManager.playEffect("burner.wav");
				if (steps == -3) SoundManager.playEffect("swap.wav");
			});
		}
		
		
		//-------------------------------- pause between the current player and the next ------------------------------------
		
		
		PauseTransition pt2 = new PauseTransition(Duration.seconds(0.8)); // 1 second pause
		animation.getChildren().add(pt2);
	
		
		//---------------------------------------- playing the main animation ---------------------------------------
		
		animation.setOnFinished(e ->{
			game.endPlayerTurn();
			gameView.updatePlayerProfiles();
			run();
		});
		animation.play();
		if(playerIndex == 0) clearPlayerSelections();
	}
	
	
	
	
	
	
	
	
	// moveAction -> 1, backwordAction -> 2, discardAction -> 3, 
	// fieldAction -> 4, saveAction -> 5, BurnAction -> 6, swapAction -> 7
	private int getAction (Card card, ArrayList<Marble> marbles){ 
		
		switch (marbles.size()){
			case 0 : 
				if (card instanceof King || card instanceof Ace) return -1 ; // field action
				if (card instanceof Queen || card instanceof Ten ) return -2 ; // discard action 
				return -100 ;
			case 2 : 
				if (card instanceof Jack) return -3 ; // swap action ;
				if (card instanceof Seven) return -5 ; // dual move action 
				return -100 ;
			default : 
				if (card instanceof Four) return -4 ; // backword Move action 
				if (card instanceof Saver) return -6 ; // Saver Action 
				if (card instanceof Burner) return -7 ; // burn Action 
				
				Standard moveCard = (Standard)card ;
				return moveCard.getRank() ; // nomarl move action 
		}
	}


	
	
	// ------------------------ The Main Logic WorkFlow ------------------------------------- 
	
	public void run(){
		if(game.checkWin() != null || win) end();
		
		Player realPlayer = game.getPlayers().get(0);
		Player currentPlayer = game.getCurrentPlayer();
		if(game.canPlayTurn()){
			if(currentPlayer == realPlayer){
				if(game.getTurn() == 0){
					gameView.getFirePitView().updateFirePitView();
					drawAllHands();
				}
				addEventHandlers();
				canPlayTurn(true);
			}
			else{
				//gameView.getPlayerProfiles().get(game.getCurrentPlayerIndex()).showChatMessage("HI !! how are you?");;
				sayMessage();
				canPlayTurn(false);
				try {
					game.playPlayerTurn();
					doTheAnimation(game.getCurrentPlayerIndex());
				} catch (GameException e) {
					GameScene.showExceptionPopup(e.getMessage(), gameScene.getRoot());
				}
			}
		}
		else{
			game.endPlayerTurn();
			gameView.updatePlayerProfiles();
			run();
		}
	}
	
	
	public void sayMessage(){
		ArrayList<String> messages = new ArrayList<>();
		messages.add("You’re really consistent\nconsistently terrible.");
		messages.add("If I had a dollar for every\nmistake you made, I’d be rich\nenough to quit this game.");
		messages.add("You’re not behind\nyou’re just giving us all\na head start, right?");
		messages.add("Keep it up! You might\njust break the record… \nfor worst play ever.");
		messages.add("Wow, that was such a smart play\n. For a potato.");
		messages.add("Oh look, you’re finally doing\nsomething useful: losing to me.");
		messages.add("You call that a comeback?\nI call it a comedy.");
		messages.add("You're not losing\nyou're just... \ninnovating new ways to fail.");
		messages.add("Impressive strategy\nif losing was the goal.");
		messages.add("Did you shuffle the cards\nor your brain?");
		messages.add("Impressive! You managed to lose\nwithout anyone targeting you!");
		messages.add("Don’t worry, your cards\naren’t the problem. You are.");
		messages.add("You're making it too easy\nfor me.");
		messages.add("Risky move\nlet’s see if it pays off.");
		messages.add("Every card counts now.");
		messages.add("Go ahead, play your\nbest card\nI need a good laugh.");
		messages.add("Your strategy is so\nmysterious even you\ndon’t understand it.");
		messages.add("Wow, bold move\nplaying that like you\nactually had a plan.");
		messages.add("You sure you’re not\nhere just to make the\nrest of us look good?");
		messages.add("You’re making history here\nworst hand ever witnessed.");
		messages.add("Keep playing like\nthat and you’ll invent\nnew ways to lose.");
		messages.add("You must be a magician…\nbecause your chances\ndisappeared.");
		messages.add("You’re not out of\nthe game yet…\njust deeply,\nhilariously behind.");
		messages.add("So brave of you\nto play with no\nclue what you're doing.");
		messages.add("You just made everyone’s\njob easier.\nThanks for that.");

		int possibility = (int)(Math.random()*101);
		int index = (int)(Math.random()*messages.size());
		if(possibility < 75){
			gameView.getPlayerProfiles().get(game.getCurrentPlayerIndex()).showChatMessage(messages.get(index));
		}
	}
	
	public void end () {
		Colour colour = win ? game.getPlayers().get(0).getColour() : game.checkWin();
		ArrayList<PlayerProfile> players = gameView.getPlayerProfiles();
		ArrayList<PlayerProfile> winners = new  ArrayList<PlayerProfile>();
				
		for (PlayerProfile player : players){
			PlayerProfile playerProfile = new PlayerProfile(player.getName(), player.getColour(), player.isActive(), player.isNextActive());
			if (player.getColour() == colour) winners.add(playerProfile);
		}
			
		for (PlayerProfile player : players){
			PlayerProfile playerProfile = new PlayerProfile(player.getName(), player.getColour(), player.isActive(), player.isNextActive());
			if (player.getColour() != colour) winners.add(playerProfile);
		}

		
		EndScreenScene endScene = new EndScreenScene(winners);
		Scene scene = endScene.createScene();
		primaryStage.setScene(scene);
		// SoundManager.playEffect("laugh.wav");
		SoundManager.stopMusic();
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(true);
	}
	
	
	
	public GameView getGameView() {
		return gameView;
	}
	
}
