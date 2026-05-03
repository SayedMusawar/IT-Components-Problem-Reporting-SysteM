package edu.university.itreporting.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdSequence {
    private final AtomicInteger sequence;

    public IdSequence(int startInclusive) {
        if (startInclusive < 1) {
            throw new IllegalArgumentException("sequence start must be >= 1");
        }
        this.sequence = new AtomicInteger(startInclusive);
    }

    public int next() {
        return sequence.getAndIncrement();
    }
}
