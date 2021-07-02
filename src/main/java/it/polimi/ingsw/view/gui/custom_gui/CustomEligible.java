package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.*;

public interface CustomEligible {

    static CustomCard getCustomCard(Eligible eligible){
        return getCustomCard(eligible, false);
    }
    static CustomCard getCustomCard(Eligible eligible, boolean toModify){
        if(eligible.getClass().equals(DevelopmentCard.class)){
            return new CustomDevelopmentCard((DevelopmentCard) eligible, toModify);
        }
        if(eligible.getClass().equals(AdditionalDepot.class)){
            return new CustomAdditionalDepot((AdditionalDepot) eligible, toModify);
        }
        if(eligible.getClass().equals(AdditionalTradingRule.class)){
            return new CustomAdditionalTradingRule((AdditionalTradingRule) eligible, toModify);
        }
        if(eligible.getClass().equals(AssignWhiteMarble.class)){
            return new CustomAssignWhiteMarble((AssignWhiteMarble) eligible, toModify);
        }
        if(eligible.getClass().equals(Discount.class)){
            return new CustomDiscount((Discount) eligible, toModify);
        }
        return null;
    }
}
