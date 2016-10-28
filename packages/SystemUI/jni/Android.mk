LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := libpoweroff_jni

LOCAL_SRC_FILES := com_android_systemui_statusbar_phone_PoweroffUtils.cpp

LOCAL_C_INCLUDES += \
    $(JNI_H_INCLUDES)

LOCAL_SHARED_LIBRARIES := \
    libnativehelper \
    libutils

LOCAL_MODULE_TAGS := optional

include $(BUILD_SHARED_LIBRARY)
