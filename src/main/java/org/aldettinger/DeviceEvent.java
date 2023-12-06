package org.aldettinger;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection // @TODO: really needed ?
public record DeviceEvent(short id, short status) {}