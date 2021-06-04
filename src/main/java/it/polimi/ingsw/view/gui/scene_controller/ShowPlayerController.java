package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
//TODO convert GridPane into ImageView, change fxml too, understand why first case of last switch unreachable

import java.util.ArrayList;
import java.util.List;


public class ShowPlayerController {
    @FXML
    private ImageView card00;
    @FXML
    private ImageView card01;
    @FXML
    private ImageView card02;
    @FXML
    private ImageView card10;
    @FXML
    private ImageView card11;
    @FXML
    private ImageView card12;
    @FXML
    private ImageView card20;
    @FXML
    private ImageView card21;
    @FXML
    private ImageView card22;
    @FXML
    private ImageView depot10;
    @FXML
    private ImageView depot20;
    @FXML
    private ImageView depot21;
    @FXML
    private ImageView depot30;
    @FXML
    private ImageView depot31;
    @FXML
    private ImageView depot32;
    @FXML
    private Label coinsStrongbox;
    @FXML
    private Label stonesStrongbox;
    @FXML
    private Label servantsStrongbox;
    @FXML
    private Label shieldsStrongbox;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView leader0;
    @FXML
    private ImageView leader1;
    @FXML
    private ImageView additionalDepot00;
    @FXML
    private ImageView additionalDepot01;
    @FXML
    private ImageView additionalDepot10;
    @FXML
    private ImageView additionalDepot11;
    @FXML
    private GridPane cell0;
    @FXML
    private GridPane cell1;
    @FXML
    private GridPane cell2;
    @FXML
    private GridPane cell3;
    @FXML
    private GridPane cell4;
    @FXML
    private GridPane cell5;
    @FXML
    private GridPane cell6;
    @FXML
    private GridPane cell7;
    @FXML
    private GridPane cell8;
    @FXML
    private GridPane cell9;
    @FXML
    private GridPane cell10;
    @FXML
    private GridPane cell11;
    @FXML
    private GridPane cell12;
    @FXML
    private GridPane cell13;
    @FXML
    private GridPane cell14;
    @FXML
    private GridPane cell15;
    @FXML
    private GridPane cell16;
    @FXML
    private GridPane cell17;
    @FXML
    private GridPane cell18;
    @FXML
    private GridPane cell19;
    @FXML
    private GridPane cell20;
    @FXML
    private GridPane cell21;
    @FXML
    private GridPane cell22;
    @FXML
    private GridPane cell23;
    @FXML
    private GridPane cell24;
    @FXML
    private ImageView Tile2;
    @FXML
    private ImageView Tile3;
    @FXML
    private ImageView Tile4;




    public void initialize(){
        ClientPlayer player = GUIController.getInstance().getPickedPlayer();
        //set username
        usernameLabel.setText(player.getUsername());
        //set leader cards and additional depots
        List<LeaderCard> leaderCards = player.getActiveLeaderCards();
        if(leaderCards.size()>0){
            leader0.setImage(new Image(getLeaderCardPath(leaderCards.get(0))));
            if(leaderCards.get(0) instanceof AdditionalDepot){
                setResourcesInAdditionalDepot(player.getWarehouse().getAdditionalDepots().get(0),0);
            }
            if(leaderCards.size()>1){
                leader1.setImage(new Image(getLeaderCardPath(leaderCards.get(1))));
                if(leaderCards.get(0) instanceof AdditionalDepot){
                    setResourcesInAdditionalDepot(player.getWarehouse().getAdditionalDepots().get(1),1);
                }
            }
        }
        //set development cards
        ImageView imageView;
        DevelopmentCard developmentCard;
        DevelopmentSlot[] developmentSlots = player.getDevelopmentSlots();
        for(int i=0; i<developmentSlots.length; i++){
            for(int j=0; j<developmentSlots[i].getCards().size(); j++){
                ArrayList<DevelopmentCard> slot = new ArrayList<>(developmentSlots[i].getCards());
                developmentCard = slot.get(j);
                imageView = getDevelopmentImageView(i,j);
                imageView.setImage(new Image(developmentCard.getPath()));
            }
        }
        //set strongbox
        Label label;
        for(ResourceType resourceType : player.getStrongbox().keySet()){
            label = getStrongboxLabel(resourceType);
            label.setText(String.valueOf(player.getStrongbox().get(resourceType)));
        }
        //set warehouse
        ArrayList<WarehouseDepot> depots = player.getWarehouse().getWarehouseDepots();
        for(WarehouseDepot depot : depots){
            setResourcesInDepot(depot);
        }
        //set faith track
        GridPane cell = getCell(player.getFaithTrack().getMarkers().get(player.getUsername()));
    }


    private String getLeaderCardPath(LeaderCard leaderCard) {
        String cardType = null;
        if(leaderCard instanceof AdditionalDepot) {
            cardType = ((AdditionalDepot) leaderCard).getDepotResourceType().name();
        }else if(leaderCard instanceof AdditionalTradingRule){
            cardType = ((AdditionalTradingRule) leaderCard).getAdditionalTradingRule().getInput().keySet().getClass().getName();
        }else if(leaderCard instanceof AssignWhiteMarble){
            cardType = ((AssignWhiteMarble) leaderCard).getResourceType().name();
        }else{
            cardType = ((Discount) leaderCard).getResourceType().name();
        }
        return "ing-sw-2021-Forbicini-Fontana-Fanton/src/GUIResources/Cards/LeaderCards/"+
                leaderCard.getClass().toString()+cardType+".png";
    }

    private void setResourcesInAdditionalDepot(WarehouseDepot additionalDepot, int leaderSlot){
        ResourceType resourceType = additionalDepot.getResourceType();
        ImageView imageView0;
        ImageView imageView1;
        if(leaderSlot==0){
            imageView0 = additionalDepot00;
            imageView1 = additionalDepot01;
        }else{
            imageView0 = additionalDepot10;
            imageView1 = additionalDepot11;
        }
        if(!additionalDepot.isEmpty()){
            imageView0.setImage(new Image(resourceType.getPath()));
            if(additionalDepot.getQuantity()>1){
                imageView1.setImage(new Image(resourceType.getPath()));
            }
        }
    }

    private ImageView getDevelopmentImageView(int slot, int card){
        if(slot==0){
            if(card==0){
                return card00;
            }
            if(card==1){
                return card01;
            }
            if(card==2){
                return card02;
            }
        }
        if(slot==1){
            if(card==0){
                return card10;
            }
            if(card==1){
                return card11;
            }
            if(card==2){
                return card12;
            }
        }
        if(slot==2){
            if(card==0){
                return card20;
            }
            if(card==1){
                return card21;
            }
            if(card==2){
                return card22;
            }
        }
        return card22;
    }

    private Label getStrongboxLabel(ResourceType resourceType){
        if(resourceType.equals(ResourceType.Coins)) {
            return coinsStrongbox;
        }
        if(resourceType.equals(ResourceType.Stones)) {
            return stonesStrongbox;
        }
        if(resourceType.equals(ResourceType.Servants)) {
            return servantsStrongbox;
        }
        if(resourceType.equals(ResourceType.Shields)) {
            return shieldsStrongbox;
        }
        return null;
    }

    private void setResourcesInDepot(WarehouseDepot depot){
        ImageView imageView;
        int level = depot.getLevel();
        if(!depot.isEmpty()){
            switch(level){
                case 1:
                    imageView = depot10;
                    imageView.setImage(new Image(depot.getResourceType().getPath()));
                    break;
                case 2:
                    imageView = depot20;
                    imageView.setImage(new Image(depot.getResourceType().getPath()));
                    if(depot.getQuantity()>1){
                        imageView = depot21;
                        imageView.setImage(new Image(depot.getResourceType().getPath()));
                    }
                    break;
                case 3:
                    imageView = depot30;
                    imageView.setImage(new Image(depot.getResourceType().getPath()));
                    if(depot.getQuantity()>1){
                        imageView = depot31;
                        imageView.setImage(new Image(depot.getResourceType().getPath()));
                        if(depot.getQuantity()>2){
                            imageView = depot32;
                            imageView.setImage(new Image(depot.getResourceType().getPath()));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private GridPane getCell (int position){
        switch(position){
            case 0:
                return cell0;
                break;
            case 1:
                return cell1;
                break;
            case 2:
                return cell2;
                break;
            case 3:
                return cell3;
                break;
            case 4:
                return cell4;
                break;
            case 5:
                return cell5;
                break;
            case 6:
                return cell6;
                break;
            case 7:
                return cell7;
                break;
            case 8:
                return cell8;
                break;
            case 9:
                return cell9;
                break;
            case 10:
                return cell10;
                break;
            case 11:
                return cell11;
                break;
            case 12:
                return cell12;
                break;
            case 13:
                return cell13;
                break;
            case 14:
                return cell14;
                break;
            case 15:
                return cell15;
                break;
            case 16:
                return cell16;
                break;
            case 17:
                return cell17;
                break;
            case 18:
                return cell18;
                break;
            case 19:
                return cell19;
                break;
            case 20:
                return cell20;
                break;
            case 21:
                return cell21;
                break;
            case 22:
                return cell22;
                break;
            case 23:
                return cell23;
                break;
            case 24:
                return cell24;
                break;
            default:
                return null;
        }
    }

}
