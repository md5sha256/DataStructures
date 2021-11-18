package com.github.md5sha256.datastructures.collections;

import com.github.md5sha256.datastructures.collections.utils.CollectionTests;
import org.junit.jupiter.api.Test;

public class TestLinkedList {

    @Test
    public void testModification() {
        CollectionTests.testModification(LinkedList::new);
    }

}
