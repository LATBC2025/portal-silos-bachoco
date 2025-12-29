package com.bachoco.secutiry.utils;

import java.util.ArrayList;
import java.util.List;

public class BatchUtils {

	public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        List<List<T>> partitions = new ArrayList<>();
        if (list == null || list.isEmpty() || batchSize <= 0) {
            return partitions;
        }

        for (int i = 0; i < list.size(); i += batchSize) {
            int end = Math.min(i + batchSize, list.size());
            List<T> subList = new ArrayList<>(list.subList(i, end));
            partitions.add(subList);
        }
        return partitions;
    }

}
