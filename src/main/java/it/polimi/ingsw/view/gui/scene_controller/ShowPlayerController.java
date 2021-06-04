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



    public void initialize(){
        ClientPlayer player = GUIController.getInstance().getPickedPlayer();
        usernameLabel.setText(player.getUsername());
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
        Label label;
        for(ResourceType resourceType : player.getStrongbox().keySet()){
            label = getStrongboxLabel(resourceType);
            label.setText(String.valueOf(player.getStrongbox().get(resourceType)));
        }
        ArrayList<WarehouseDepot> depots = player.getWarehouse().getWarehouseDepots();
        for(WarehouseDepot depot : depots){
            setResourcesInDepot(depot);
        }
    }
//TODO show resources in additional depot
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

}
