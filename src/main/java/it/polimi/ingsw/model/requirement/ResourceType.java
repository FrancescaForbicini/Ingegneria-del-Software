package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;
import java.util.Arrays;

public enum ResourceType {
    Coins,
    Stones,
    Servants,
    Shields,
    Any;

    /**
     * Converts color of the resource type
     * @return the color of the resource type
     */
    public StringBuilder convertColor(){
        switch (this){
            case Coins:
                return new StringBuilder().append(Color.ANSI_YELLOW).append(this).append(Color.RESET);
            case Stones:
                return new StringBuilder().append(Color.ANSI_GREY).append(this).append(Color.RESET);
            case Servants:
                return new StringBuilder().append(Color.ANSI_PURPLE).append(this).append(Color.RESET);
            case Shields:
                return new StringBuilder().append(Color.ANSI_BLUE).append(this).append(Color.RESET);
            case Any:
                return new StringBuilder().append((Color.ANSI_WHITE)).append(this).append(Color.RESET);
            default: return null;
        }
    }

    public String getPath(){
        switch (this){
            case Coins:
                return "ing-sw-2021-Forbicini-Fontana-Fanton/src/GUIResources/Punchboard/ResourceType/Coin.png";
            case Stones:
                return "ing-sw-2021-Forbicini-Fontana-Fanton/src/GUIResources/Punchboard/ResourceType/Stone.png";
            case Servants:
                return "ing-sw-2021-Forbicini-Fontana-Fanton/src/GUIResources/Punchboard/ResourceType/Servant.png";
            case Shields:
                return "ing-sw-2021-Forbicini-Fontana-Fanton/src/GUIResources/Punchboard/ResourceType/Shield.png";
            default: return null;
        }
    }

    public static ArrayList<ResourceType> getAllValidResources(){
        ArrayList<ResourceType> allValidResources = new ArrayList<>(Arrays.asList(ResourceType.values()));
        allValidResources.removeIf(resourceType -> resourceType.equals(ResourceType.Any));
        return allValidResources;
    }
}
