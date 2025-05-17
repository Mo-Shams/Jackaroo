package view.boardView;

import java.util.ArrayList;

import engine.board.Cell;
import engine.board.CellType;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.marbleView.MarbleView;
import model.player.Marble;

public class HomeZoneView extends StackPane {
    private static final double SIDE_LENGTH = 100;
    private static final double GAP = 5;

    private ArrayList<Marble> marbles;
    private ArrayList<MarbleView> marbleViews;
    private final ArrayList<CellView> cellViews;
    private final Rectangle homeZone;
    private final GridPane cellGrid;

    public HomeZoneView(ArrayList<Marble> marbles) {
        this.marbles = (marbles != null)?  marbles : new ArrayList<>();
        this.marbleViews = new ArrayList<>();
        this.cellViews = new ArrayList<>();

        Color color = Color.GRAY;
        if (!this.marbles.isEmpty()) {
            color = Color.valueOf(this.marbles.get(0).getColour().toString());
        }

        for (Marble marble : this.marbles) {
            MarbleView marbleView = new MarbleView(marble);
            marbleViews.add(marbleView);
        }

        for (int i = 0; i < 4; i++) {
            cellViews.add(new CellView(new Cell(CellType.NORMAL), color));
        }

        homeZone = createSquare(SIDE_LENGTH, SIDE_LENGTH, color);

        cellGrid = new GridPane();
        cellGrid.setHgap(GAP);
        cellGrid.setVgap(GAP);
        cellGrid.setAlignment(Pos.CENTER);

        // Add cells to 2x2 Grid
        cellGrid.add(cellViews.get(0), 0, 0);
        cellGrid.add(cellViews.get(1), 1, 0);
        cellGrid.add(cellViews.get(2), 0, 1);
        cellGrid.add(cellViews.get(3), 1, 1);

        // Assign marbles to cells in order
        for (int i = 0; i < marbleViews.size() && i < cellViews.size(); i++) {
            cellViews.get(i).setMarbleView(marbleViews.get(i));
        }

        getChildren().addAll(homeZone, cellGrid);
    }

    private Rectangle createSquare(double width, double height, Color color) {
        Rectangle square = new Rectangle(width, height);
        square.setFill(Color.TRANSPARENT);
        square.setStroke(color);
        square.setArcWidth(20);
        square.setArcHeight(20);
        square.setStrokeWidth(3);

        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setRadius(10);
        glow.setSpread(0.5);
        square.setEffect(glow);

        return square;
    }

    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    public ArrayList<MarbleView> getMarbleViews() {
        return marbleViews;
    }


    // Remove the first marble from home zone and return its MarbleView, or null if empty
    public MarbleView removeFirstMarble() {
        if (marbleViews.isEmpty()) {
            return null;
        }

        MarbleView removed = marbleViews.remove(0);
        return removed;
    }

    // Add a marble to the first available empty cell; returns true if added successfully
    public boolean addMarbleView(MarbleView marbleView) {
        if (marbleViews.size() >= cellViews.size()) {
            // No empty cell available
            return false;
        }
        marbleViews.add(marbleView);
        return true;
    }

    // Get current number of marbles in the home zone
    public int getMarbleCount() {
        return marbleViews.size();
    }
}
