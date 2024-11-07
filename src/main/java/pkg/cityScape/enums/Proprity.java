package pkg.cityScape.enums;

import org.bukkit.ChatColor;

public enum Proprity {
    LOW, MEDIUM, HIGH;


    public ChatColor getColor() {
        return switch (this) {
            case LOW -> ChatColor.GREEN;
            case MEDIUM -> ChatColor.AQUA;
            case HIGH -> ChatColor.RED;
        };
    }
}
