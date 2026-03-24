// Copyright (c) 2026 XtraCube. All rights reserved.
#ifndef FUSIONCORE_SAFEHOOK_H
#define FUSIONCORE_SAFEHOOK_H

#include <unistd.h>

// The original SafeHook was in C++, but we are using C for simplicity.

// The size of the trampoline code, which is architecture-dependent.
// See asm.cpp for details on the trampoline implementation (emit_absolute_jump).
#if defined(__aarch64__)
static constexpr size_t trampoline_size = 16;
#elif defined(__arm__)
static constexpr size_t trampoline_size = 8;
#endif

// The size of a memory page on the target system, which is needed for memory protection operations.
static const size_t page_size = sysconf(_SC_PAGESIZE);

// The allocator function used to allocate code trampolines.
// It should allocate executable memory and return a pointer to it.
using allocate_func = void *(*)(void *target, size_t size);

// Initializes the SafeHook system with the given library handle, base address, and allocator function.
bool safehook_initialize(void *library_handle, void *library_base, allocate_func allocator);

// Sets up the bridge helper, which is a small piece of code used to hook functions using X8 return buffer.
bool safehook_setup_bridge_helper(const char *bridge_library_path);

// Creates a hook for the target function, redirecting it to the hook function.
// Returns a pointer to the original function, which can be used to call the original implementation from the hook.
void *safehook_create_hook(void *target_function, void *hook_function, bool use_bridge);

// Destroys the hook created by safehook_create_hook, restoring the original function.
void *safehook_destroy_hook(void *hook_handle);


#endif //FUSIONCORE_SAFEHOOK_H
