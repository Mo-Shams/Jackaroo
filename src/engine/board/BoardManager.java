package engine.board;

import java.util.ArrayList;

import exception.*;
import model.player.Marble;

public interface BoardManager {
	int getSplitDistance();
	void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException;
	void swap(Marble marble1, Marble marble2) throws IllegalSwapException;
    void destroyMarble(Marble marble) throws IllegalDestroyException;
    void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException;
    void sendToSafe(Marble marble) throws InvalidMarbleException;
    ArrayList<Marble> getActionableMarbles();
}
