/*
 *   Adventure Game, a digital gamebook written in java with Libgdx.
 *   Copyright (C) 2018 Pedro Berná
 *
 *   This code is based on the code from this example:
 *   https://bitbucket.org/just4phil/gdxpayexample/
 */

package com.pberna.adventure.store;

import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.pberna.adventure.persistence.PurchaseRepository;
import com.pberna.engine.logging.Logger;

import java.util.ArrayList;
import java.util.Date;

public class PurchaseManager implements Disposable {

    public static final int APPSTORE_UNDEFINED	= 0;
    public static final int APPSTORE_GOOGLE 	= 1;
    public static final int APPSTORE_OUYA 		= 2;
    public static final int APPSTORE_AMAZON 	= 3;
    public static final int APPSTORE_DESKTOP 	= 4;

    private static final String PrivateKey = "Your_private_key";

    private static PurchaseManager instance = null;

    private int appStore;
    private PlatformResolver platformResolver;
    private PurchaseManagerConfig purchaseManagerConfig;
    private PurchaseObserver purchaseObserver;
    private ArrayList<PurchaseManagerEventsListener> listeners;
    private PurchaseRepository purchaseRepository;
    private String uniqueId;

    private PurchaseManager() {
        appStore = APPSTORE_UNDEFINED;
        purchaseManagerConfig = createPurchaseManagerConfig();
        purchaseObserver = createPurchaseObserver();
        listeners = new ArrayList<PurchaseManagerEventsListener>();
        purchaseRepository = new PurchaseRepository();
        uniqueId = null;
    }

    private PurchaseManagerConfig createPurchaseManagerConfig() {
        PurchaseManagerConfig config = new PurchaseManagerConfig();
        for(StoreItem storeItem: StoreItem.getAllStoreItems()) {
            config.addOffer(new Offer().setType(OfferType.ENTITLEMENT).
                    setIdentifier(storeItem.getPlatformStoreId()));
        }

        return config;
    }

    private PurchaseObserver createPurchaseObserver() {
        return new PurchaseObserver() {
            @Override
            public void handleRestore (Transaction[] transactions) {
                for (int i = 0; i < transactions.length; i++) {
                    try {
                        if (checkTransaction(transactions[i].getIdentifier(), true)) {
                            break;
                        }
                    } catch (Exception ex) {
                        Logger.getInstance().addLogInfo(Logger.TagPay, ex.getMessage());
                    }
                }
            }
            @Override
            public void handleRestoreError (Throwable e) {
                try {
                    throw new GdxRuntimeException(e);
                } catch (Exception ex) {
                    Logger.getInstance().addLogInfo(Logger.TagPay, ex.getMessage());
                }
            }
            @Override
            public void handleInstall () {	}

            @Override
            public void handleInstallError (Throwable e) {
                Logger.getInstance().addLogInfo(Logger.TagPay, "PurchaseObserver: handleInstallError!: " +
                        e.getMessage());
                //throw new GdxRuntimeException(e);
            }
            @Override
            public void handlePurchase (Transaction transaction) {
                try {
                    checkTransaction(transaction.getIdentifier(), false);
                } catch (Exception ex) {
                    Logger.getInstance().addLogInfo(Logger.TagPay, ex.getMessage());
                }
            }
            @Override
            public void handlePurchaseError (Throwable e) {	//--- Amazon IAP: this will be called for cancelled
                Logger.getInstance().addLogInfo(Logger.TagPay, e.getMessage());
                //throw new GdxRuntimeException(e);
            }
            @Override
            public void handlePurchaseCanceled () {	//--- will not be called by amazonIAP
            }
        };
    }

    protected boolean checkTransaction (String platformStoreId, boolean isRestore) {

        for(StoreItem storeItem: StoreItem.getAllStoreItems()) {
            if(storeItem.getPlatformStoreId().equals(platformStoreId)) {
                if(storeItem.getId().equals(StoreItem.RemoveAdvertisingId)) {
                    Logger.getInstance().addLogInfo("checkTransaction", "full version found!");
                    for (PurchaseManagerEventsListener listener:listeners) {
                        listener.removeAdvertisingPurchased();
                    }
                    Purchase purchase = new Purchase();
                    purchase.setPlatformStoreId(storeItem.getPlatformStoreId());
                    purchase.setDate(new Date(TimeUtils.millis()));
                    addItemPurchase(purchase);
                } else {
                    Logger.getInstance().addLogInfo("checkTransaction", "Unknown item found");
                }
                return true;
            }
        }
        return false;
    }

    public static PurchaseManager getInstance() {
        if(instance == null) {
            instance = new PurchaseManager();
        }

        return instance;
    }

    @Override
    public void dispose() {
        if (platformResolver != null) {
            platformResolver.dispose();
        }

        instance = null;
    }

    public int getAppStore() {
        return appStore;
    }

    public void setAppStore(int appStore) {
        this.appStore = appStore;
    }

    public PlatformResolver getPlatformResolver() {
        return platformResolver;
    }
    public void setPlatformResolver (PlatformResolver platformResolver) {
        this.platformResolver = platformResolver;
    }

    public PurchaseManagerConfig getPurchaseManagerConfig() {
        return purchaseManagerConfig;
    }

    public PurchaseObserver getPurchaseObserver() {
        return purchaseObserver;
    }

    public void addListener(PurchaseManagerEventsListener listener) {
        listeners.add(listener);
    }

    public boolean isItemPurchased(String storePlatformId) {
        String encryptStoreId = getEncryptedString(storePlatformId);

        for( Purchase purchase : purchaseRepository.findAll()) {
            if(purchase.getPlatformStoreId().equals(encryptStoreId)) {
                return true;
            }
        }
        return false;
    }

    private String getEncryptedString(String text) {
        if(uniqueId == null || uniqueId.length() == 0) {
            return text;
        }

        return xorText(text, uniqueId);
    }

    public static String xorText(String text, String key) {
        try {
            if (text == null || key == null) {
                return text;
            }

            char[] keys = key.toCharArray();
            char[] mesg = text.toCharArray();

            int ml = mesg.length;
            int kl = keys.length;
            char[] newmsg = new char[ml];

            for (int i = 0; i < ml; i++) {
                newmsg[i] = (char)(mesg[i] ^ keys[i % kl]);
            }

            return new String(newmsg);
        } catch (Exception e) {
            return text;
        }
    }

    public void addItemPurchase(Purchase purchase) {
        if(!isItemPurchased(purchase.getPlatformStoreId())) {
            purchase.setPlatformStoreId(getEncryptedString(purchase.getPlatformStoreId()));
            purchaseRepository.add(purchase);
        }
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = xorText(uniqueId, PrivateKey);
    }
}
