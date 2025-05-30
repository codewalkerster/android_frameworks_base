/*
 *    Copyright (c) 2019 Sangchul Go <luke.go@hardkernel.com>
 *
 *    OdroidThings is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    OdroidThings is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with OdroidThings.
 *    If not, see <http://www.gnu.org/licenses/>.
 */

#define LOG_TAG "Rtc-JNI"

#include <nativehelper/JNIHelp.h>

#include <utils/Log.h>
#include <core_jni_helpers.h>

#include <aidl/vendor/hardkernel/hardware/rtc/IRtc.h>
#include <android/binder_manager.h>

namespace android {

using ::aidl::vendor::hardkernel::hardware::rtc::IRtc;

class Rtc {
private:
    static std::shared_ptr<IRtc> sRtc;

    Rtc() {}

public:
    static void disassociate() {
        sRtc = nullptr;
    }

    static std::shared_ptr<IRtc> associate() {
        if (sRtc == nullptr) {
            std::string instance = std::string(IRtc::descriptor) + "/default";
            sRtc = IRtc::fromBinder(
                    ndk::SpAIBinder(AServiceManager_waitForService(instance.c_str())));

            if (sRtc == nullptr) {
                ALOGE("Unable to get IRtc aidl.");
            }
        }
        return sRtc;
    }
};

std::shared_ptr<IRtc> Rtc::sRtc = nullptr;

static jstring getTime(JNIEnv* env, jobject obj) {
    std::shared_ptr<IRtc> rtc = Rtc::associate();
    std::string rtc_time_secs;
    rtc->getTime(&rtc_time_secs);

    return env->NewStringUTF(rtc_time_secs.c_str());
}

static void setWakeupAlarm(JNIEnv* env, jobject obj, jlong secs) {
    std::shared_ptr<IRtc> rtc = Rtc::associate();
    rtc->setWakeupAlarm(secs);
}

static const JNINativeMethod sManagerMethods[] = {
    /* name, signature, funcPtr */
    {"native_getTime",
        "()Ljava/lang/String;",
        reinterpret_cast<void *>(getTime)},
    {"native_setRtcWakeup",
        "(J)V",
        reinterpret_cast<void*>(setWakeupAlarm)},
};

int register_vendor_hardkernel_hardware_rtc(JNIEnv* env) {
    ALOGD("load odorid rtc server jni ");
    return jniRegisterNativeMethods(
            env,
            "vendor/hardkernel/hardware/rtc/internal/RtcManager",
            sManagerMethods,
            NELEM(sManagerMethods));
}
} // namespace android
