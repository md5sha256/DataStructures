package com.github.md5sha256.datastructures;

import com.github.md5sha256.datastructures.utils.CollectionTests;
import org.junit.jupiter.api.Test;

public class TestLinkedList {

    @Test
    public void testModification() {
        CollectionTests.testModification(LinkedList::new);
    }

}
