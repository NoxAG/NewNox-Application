package com.noxag.newnox.textanalyzer.util;

@FunctionalInterface
public interface Function<A, B, C, D> {
    public D apply(A one, B two, C three);
}
