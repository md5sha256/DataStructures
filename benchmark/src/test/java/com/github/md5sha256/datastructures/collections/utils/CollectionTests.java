package com.github.md5sha256.datastructures.collections.utils;

import com.github.md5sha256.datastructures.collections.Collection;
import org.junit.jupiter.api.Assertions;

import java.util.function.Supplier;

public class CollectionTests {

    public static void testModification(Supplier<Collection<Integer>> collectionSupplier) {
        final Collection<Integer> ints = collectionSupplier.get();
        Assertions.assertEquals(0, ints.size());
        final Integer[] params = new Integer[]{0, 1, 2, 3, 4};
        ints.addAll(params);
        Assertions.assertEquals(params.length, ints.size());
        for (Integer i : params) {
            Assertions.assertTrue(ints.contains(i));
        }
        ints.remove(0);
        Assertions.assertEquals(params.length - 1, ints.size());
        ints.clear();
        Assertions.assertEquals(0, ints.size());
        for (Integer i : params) {
            Assertions.assertFalse(ints.contains(i));
        }
    }

}
