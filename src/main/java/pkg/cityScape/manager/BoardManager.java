package pkg.cityScape.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import pkg.cityScape.CityScape;
import pkg.cityScape.model.Board;

import java.io.File;
import java.io.IOException;

public class BoardManager {
    private YamlConfiguration boardConfig;
    private File boardFile;
    private CityScape cityScape;

    public BoardManager(CityScape cityScape) {
        this.cityScape = cityScape;
        boardFile = new File(cityScape.getDataFolder(), "boards.yml");
        if (boardFile.exists()){
            boardConfig = YamlConfiguration.loadConfiguration(boardFile);
        } else {
            System.out.println("File boards.yml non trovato, sarà creato.");
            boardConfig = new YamlConfiguration();
        }
    }

    private void saveConfig() {
        try {
            boardConfig.save(boardFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel salvataggio del file boards.yml");
        }
    }

    public void setNewBoard(Board board){
        String path = "boards." + board.getId();
        boardConfig.set(path + ".id", board.getId());
        boardConfig.set(path + ".title", board.getTitle());
        boardConfig.set(path + ".description", board.getDescription());
        boardConfig.set(path + ".priority", board.getPriority());
        boardConfig.set(path + ".is_public", board.isPublic());
        saveConfig();
    }
}
