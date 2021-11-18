package com.github.md5sha256.datastructures.collections;

import com.github.md5sha256.datastructures.collections.utils.CollectionTests;
import org.junit.jupiter.api.Test;

public class TestFixedSizeHashSet {

    @Test
    public void testModification() {
        CollectionTests.testModification(() -> new FixedSizeHashSet<>(10));
    }

}
