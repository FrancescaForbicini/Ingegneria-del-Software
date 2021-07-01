package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.controller.Settings;

import java.util.Optional;

import static javafx.application.Application.launch;

public class MainCustom {
    public static Settings customSettings;

    public static void main(String[] args) {
        customSettings = Settings.getInstance();
        launch(CustomSettingsGUI.class);
        Settings.writeCustomSettings(Optional.of(customSettings), Settings.CUSTOM_SETTINGS_CLIENT_TEMPLATE);
    }
}
