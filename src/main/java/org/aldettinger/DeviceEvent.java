package org.aldettinger;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record DeviceEvent(short id, short status) {}