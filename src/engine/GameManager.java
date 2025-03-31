package engine;

import exception.*;
import model.*;
import model.player.Marble;

public interface GameManager {
	void sendHome(Marble marble);
    void fieldMarble() throws CannotFieldException, IllegalDestroyException;
    void discardCard(Colour colour) throws CannotDiscardException;
    void discardCard() throws CannotDiscardException;
    Colour getActivePlayerColour();
    Colour getNextPlayerColour();
}
