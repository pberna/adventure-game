/*
 *   Adventure Game, a digital gamebook written in java with Libgdx.
 *   Copyright (C) 2018 Pedro Berná
 *
 *   This code is based on the code from this example:
 *   https://bitbucket.org/just4phil/gdxpayexample/
 */

package com.pberna.adventure.store;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.PurchaseSystem;
import com.pberna.engine.logging.Logger;

public abstract class PlatformResolver {

    public Game game;
    protected PurchaseManager purchaseManager;
    PurchaseObserver purchaseObserver;
    PurchaseManagerConfig config;

    public PlatformResolver (Game game) {
        this.game = game;
    }

    public void initializeIAP (PurchaseManager mgr, PurchaseObserver purchaseObserver, PurchaseManagerConfig config) {
        this.purchaseManager = mgr;
        this.purchaseObserver = purchaseObserver;
        this.config = config;
    }

    public void installIAP() {
        PurchaseSystem.onAppRestarted();
        // set and install the manager manually
        if (purchaseManager != null) {
            PurchaseSystem.setManager(purchaseManager);
            purchaseManager.install(purchaseObserver, config, true);	// dont call PurchaseSystem.install() because it may bind openIAB!
            Logger.getInstance().addLogInfo("gdx-pay", "calls purchasemanager.install() manually");
        }
        else {
            Logger.getInstance().addLogInfo("gdx-pay", "initializeIAP(): purchaseManager == null => call PurchaseSystem.hasManager()");
            if (PurchaseSystem.hasManager()) { // install and get the manager automatically via reflection
                this.purchaseManager = PurchaseSystem.getManager();
                Logger.getInstance().addLogInfo("gdx-pay", "calls PurchaseSystem.install() via reflection");
                PurchaseSystem.install(purchaseObserver, config); // install the observer
                Logger.getInstance().addLogInfo("gdx-pay", "installed manager: " + this.purchaseManager.toString());
            }
        }
    }

    public void requestPurchase (String productString) {
        if (purchaseManager != null) {
            purchaseManager.purchase(productString);	// dont call PurchaseSystem... because it may bind openIAB!
            Logger.getInstance().addLogInfo("gdx-pay", "calls purchasemanager.purchase()");
        } else {
            Logger.getInstance().addLogInfo("gdx-pay", "ERROR: requestPurchase(): purchaseManager == null");
        }
    }

    public void requestPurchaseRestore () {
        if (purchaseManager != null) {
            purchaseManager.purchaseRestore();	// dont call PurchaseSystem.purchaseRestore(); because it may bind openIAB!
            Logger.getInstance().addLogInfo("gdx-pay", "calls purchasemanager.purchaseRestore()");
        } else {
            Logger.getInstance().addLogInfo("gdx-pay", "ERROR: requestPurchaseRestore(): purchaseManager == null");
        }
    }

    public PurchaseManager getPurchaseManager () {
        return purchaseManager;
    }

    public void dispose () {
        if (purchaseManager != null) {
            Logger.getInstance().addLogInfo("gdx-pay", "calls purchasemanager.dispose()");
            purchaseManager.dispose();		// dont call PurchaseSystem... because it may bind openIAB!
            purchaseManager = null;
        }
    }
}
