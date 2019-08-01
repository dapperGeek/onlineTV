package com.thegeek0.util;

import android.content.Context;
import android.os.Bundle;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ixidev.gdpr.GDPRChecker;

public class PopUpAds {

    public static void ShowInterstitialAds(Context context) {
        if (Constant.isInterstitial) {
            Constant.AD_COUNT += 1;
            if (Constant.AD_COUNT == Constant.AD_COUNT_SHOW) {
                final InterstitialAd mInterstitial = new InterstitialAd(context);
                mInterstitial.setAdUnitId(Constant.adMobInterstitialId);
                GDPRChecker.Request request = GDPRChecker.getRequest();
                AdRequest.Builder builder = new AdRequest.Builder();
                if (request == GDPRChecker.Request.NON_PERSONALIZED) {
                    Bundle extras = new Bundle();
                    extras.putString("npa", "1");
                    builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                }
                mInterstitial.loadAd(builder.build());

                mInterstitial.show();
                Constant.AD_COUNT = 0;
                if (!mInterstitial.isLoaded()) {
                    AdRequest.Builder builder1 = new AdRequest.Builder();
                    if (request == GDPRChecker.Request.NON_PERSONALIZED) {
                        Bundle extras = new Bundle();
                        extras.putString("npa", "1");
                        builder1.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                    }
                    mInterstitial.loadAd(builder1.build());
                }
                mInterstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        mInterstitial.show();
                    }
                });
            }
        }
    }
}
