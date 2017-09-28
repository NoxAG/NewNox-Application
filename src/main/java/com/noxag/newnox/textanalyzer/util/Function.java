package com.noxag.newnox.textanalyzer.util;

import java.io.IOException;

@FunctionalInterface
public interface Function<A, B, C, D> {
    public D apply(A one, B two, C three) throws IOException;
}
