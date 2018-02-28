/*
 *   Adventure Game, a digital gamebook written in java with Libgdx.
 *   Copyright (C) 2018 Pedro Berná
 *
 *   This code is based on the code from this example:
 *   https://bitbucket.org/just4phil/gdxpayexample/
 */

package com.pberna.adventure.android.store;

import android.app.Activity;
import android.content.Context;

import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.pberna.adventure.AdventureGame;
import com.pberna.adventure.android.store.util.IabHelper;
import com.pberna.adventure.android.store.util.IabResult;
import com.pberna.adventure.android.store.util.Inventory;
import com.pberna.adventure.android.store.util.SkuDetails;
import com.pberna.adventure.dependencies.IStoreItemInfo;
import com.pberna.adventure.dependencies.IStoreQuerier;
import com.pberna.adventure.dependencies.IStoreQuerierCallback;
import com.pberna.adventure.store.PlatformResolver;
import com.pberna.adventure.store.PurchaseManager;
import com.pberna.engine.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class GooglePlayResolver extends PlatformResolver implements IStoreQuerier {

    private final static String GoogleKey = "YourKey";
    private final static String KeyPart2 = "YourKey2";
    private final static String KeyPart3 = "YourKey3";
    private final static String KeyPart4 = "YourKey4";
    private final static String KeyPart5 = "YourKey5";
    private final static String KeyPart6 = "YourKey6";

    static final int RC_REQUEST = 10901;

    private Context context;
    private Activity activity;

    public GooglePlayResolver(AdventureGame game, Context context, Activity activity) {
        super(game);
        this.context = context;
        this.activity = activity;

        PurchaseManagerConfig config = PurchaseManager.getInstance().getPurchaseManagerConfig();
        config.addStoreParam(PurchaseManagerConfig.STORE_NAME_ANDROID_GOOGLE, GoogleKey + KeyPart2 +
        KeyPart3 + KeyPart4 + KeyPart5 + KeyPart6);
        initializeIAP(null, PurchaseManager.getInstance().getPurchaseObserver(), config);
    }

    public void getStoreItemsInfo(final List<String> itemsSkus, final IStoreQuerierCallback callback) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final IabHelper iabHelper = new IabHelper(context, GoogleKey + KeyPart2 +
                            KeyPart3 + KeyPart4 + KeyPart5 + KeyPart6);

                    iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                        @Override
                        public void onIabSetupFinished(IabResult result) {
                            try {
                                iabHelper.queryInventoryAsync(true, itemsSkus, new ArrayList<String>(), new IabHelper.QueryInventoryFinishedListener() {
                                    @Override
                                    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                                        if(callback != null) {
                                            callback.queryInventoryFinished(getStoreItemsInfoList(itemsSkus, inv));
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                Logger.getInstance().addLogInfo(Logger.TagPay, e.getMessage());
                                if(callback != null) {
                                    callback.queryInventoryFinished(new ArrayList<IStoreItemInfo>());
                                }
                            }
                        }
                    });

                } catch (Exception e) {
                    Logger.getInstance().addLogInfo(Logger.TagPay, e.getMessage());
                    if(callback != null) {
                        callback.queryInventoryFinished(new ArrayList<IStoreItemInfo>());
                    }
                }
            }
        });
    }

    private List<IStoreItemInfo> getStoreItemsInfoList(List<String> itemsSkus, Inventory inventory) {
        ArrayList<IStoreItemInfo> listStoreItemsInfo = new ArrayList<IStoreItemInfo>(itemsSkus.size());

        if(inventory != null) {
            for (String sku : itemsSkus) {
                SkuDetails skuDetails = inventory.getSkuDetails(sku);
                if (skuDetails != null) {
                    skuDetails.setHasPurchased(inventory.hasPurchase(skuDetails.getSku()));
                    listStoreItemsInfo.add(skuDetails);
                }
            }
        }

        return listStoreItemsInfo;
    }
}
