package globale.controller;

import globale.model.Travaux;

public class ControllerManager {
    private static ControllerManager instance;
    private AccueilController accueilController;
    private EditionController editionController;

    private ControllerManager() {
    }

    public static ControllerManager getInstance() {
        if (instance == null) {
            instance = new ControllerManager();
        }
        return instance;
    }

    public AccueilController getAccueilController() {
        if (accueilController == null) {
            accueilController = new AccueilController(Travaux.getInstance());
        }
        return accueilController;
    }

    public EditionController getEditionController() {
        if (editionController == null) {
            editionController = new EditionController(Travaux.getInstance());
        }
        return editionController;
    }
}