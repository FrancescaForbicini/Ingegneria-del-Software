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
    public String convertColor(){
        StringBuilder print = new StringBuilder();
        switch (this){
            case Coins:
                return print.append(Color.ANSI_YELLOW).append(this).append(Color.RESET).toString();
            case Stones:
                return print.append(Color.ANSI_GREY).append(this).append(Color.RESET).toString();
            case Servants:
                return print.append(Color.ANSI_PURPLE).append(this).append(Color.RESET).toString();
            case Shields:
                return print.append(Color.ANSI_BLUE).append(this).append(Color.RESET).toString();
            case Any:
                return print.append((Color.ANSI_WHITE)).append(this).append(Color.RESET).toString();
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

    /**
     * Returns the enumeration of the Resource Type without the Resource Type Any
     * @return all the resources
     */
    public static ArrayList<ResourceType> getAllValidResources(){
        ArrayList<ResourceType> allValidResources = new ArrayList<>(Arrays.asList(ResourceType.values()));
        allValidResources.removeIf(resourceType -> resourceType.equals(ResourceType.Any));
        return allValidResources;
    }
}
