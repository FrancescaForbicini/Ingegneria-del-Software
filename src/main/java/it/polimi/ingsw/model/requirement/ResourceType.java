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

    public static ArrayList<ResourceType> getAll() {
        return (ArrayList<ResourceType>) Arrays.asList(Coins, Stones, Servants, Shields);
    }

    /**
     * Converts color of the resource type
     * @return the color of the resource type
     */
    public String convertColor(){
        StringBuilder print = new StringBuilder();
        return switch (this) {
            case Coins -> print.append(Color.ANSI_YELLOW).append(this).append(Color.RESET).toString();
            case Stones -> print.append(Color.ANSI_GREY).append(this).append(Color.RESET).toString();
            case Servants -> print.append(Color.ANSI_PURPLE).append(this).append(Color.RESET).toString();
            case Shields -> print.append(Color.ANSI_BLUE).append(this).append(Color.RESET).toString();
            case Any -> print.append((Color.ANSI_WHITE)).append(this).append(Color.RESET).toString();
            default -> null;
        };
    }

    /**
     * Gets the path of the image correspondents to the resource
     *
     * @return path of the resource
     */
    public static String getPath(ResourceType resourceType){
        switch (resourceType){
            case Coins:
                return "GUIResources/Punchboard/ResourceType/Coin.png";
            case Stones:
                return "GUIResources/Punchboard/ResourceType/Stone.png";
            case Servants:
                return "GUIResources/Punchboard/ResourceType/Servant.png";
            case Shields:
                return "GUIResources/Punchboard/ResourceType/Shield.png";
            default:
                return null;
        }
    }

    /**
     * Returns the enumeration of the Resource Type without the Resource Type Any
     *
     * @return all the resources
     */
    public static ArrayList<ResourceType> getAllValidResources(){
        ArrayList<ResourceType> allValidResources = new ArrayList<>(Arrays.asList(ResourceType.values()));
        allValidResources.removeIf(resourceType -> resourceType.equals(ResourceType.Any));
        return allValidResources;
    }
}
