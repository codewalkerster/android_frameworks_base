/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.biometrics.sensors.fingerprint.hidl;

import android.annotation.Nullable;
import android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.os.RemoteException;
import android.util.Slog;

/**
 * Contains helper methods for under-display fingerprint HIDL.
 */
public class UdfpsHelper {

    private static final String TAG = "UdfpsHelper";

    static void onFingerDown(IBiometricsFingerprint daemon, int x, int y, float minor,
            float major) {
        android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint extension =
                android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.castFrom(
                        daemon);
        if (extension == null) {
            Slog.v(TAG, "onFingerDown | failed to cast the HIDL to V2_3");
            return;
        }

        try {
            extension.onFingerDown(x, y, minor, major);
        } catch (RemoteException e) {
            Slog.e(TAG, "onFingerDown | RemoteException: ", e);
        }
    }

    static void onFingerUp(IBiometricsFingerprint daemon) {
        android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint extension =
                android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.castFrom(
                        daemon);
        if (extension == null) {
            Slog.v(TAG, "onFingerUp | failed to cast the HIDL to V2_3");
            return;
        }

        try {
            extension.onFingerUp();
        } catch (RemoteException e) {
            Slog.e(TAG, "onFingerUp | RemoteException: ", e);
        }
    }

    static void showUdfpsOverlay(int sensorId,
            @Nullable IUdfpsOverlayController udfpsOverlayController) {
        if (udfpsOverlayController == null) {
            return;
        }
        try {
            udfpsOverlayController.showUdfpsOverlay(sensorId);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when showing the UDFPS overlay", e);
        }
    }

    static void hideUdfpsOverlay(int sensorId,
            @Nullable IUdfpsOverlayController udfpsOverlayController) {
        if (udfpsOverlayController == null) {
            return;
        }
        try {
            udfpsOverlayController.hideUdfpsOverlay(sensorId);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception when hiding the UDFPS overlay", e);
        }
    }
}
