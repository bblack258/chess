package model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GameList extends ArrayList<GameData> {
    public GameList() {}

    public GameList(List<GameData> games) {
        super(games);
    }

    public String toString() {
        return new Gson().toJson(this.toArray());
    }
}
