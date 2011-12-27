LOCAL_PATH := $(call my-dir)/..

include $(CLEAR_VARS)

LOCAL_MODULE := bullet
LOCAL_C_INCLUDES := $(shell find . -name '*.h' | sed 's/\/\w\+.h$///' | uniq)
LOCAL_ARM_MODE := arm
LOCAL_CFLAGS := $(LOCAL_C_INCLUDES:%=-I%) -O3 -DANDROID_NDK
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -ldl -llog -lz
LOCAL_SRC_FILES := $(shell find . -name '*.cpp')

include $(BUILD_STATIC_LIBRARY)
include $(CLEAR_VARS)

LOCAL_MODULE := bulletshared
LOCAL_STATIC_LIBRARIES := bullet

include $(BUILD_SHARED_LIBRARY)
