// Copyright (c) 2026 XtraCube
#include <android/log.h>
#include <exports.h>

void init_bridge_helper(const char *libraryPath)
{

}

dobby_dummy_func_t hook(void *address, dobby_dummy_func_t replace_delegate, bool specialReturnBuffer)
{

}

void unhook(void *target)
{

}

void create_alert(const char *title, const char *message)
{

}

void write_log(const char *text)
{
    __android_write_log(ANDROID_LOG_INFO, "FusionCore", "%s", text);
}