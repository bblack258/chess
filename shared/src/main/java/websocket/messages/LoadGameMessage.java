package websocket.messages;

import chess.ChessBoard;

import java.util.Objects;

public class LoadGameMessage extends ServerMessage {
    ChessBoard game;

    public LoadGameMessage(ServerMessageType type, ChessBoard game) {
        super(type);
        this.game = game;
    }

    public ChessBoard getGame() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        LoadGameMessage that = (LoadGameMessage) o;
        return Objects.equals(game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), game);
    }
}
