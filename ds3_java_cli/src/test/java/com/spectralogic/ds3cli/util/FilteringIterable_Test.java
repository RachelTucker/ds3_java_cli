/*
 * ***************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ***************************************************************************
 */

package com.spectralogic.ds3cli.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class FilteringIterable_Test {

    @Test
    public void noFilter() {
        final ImmutableList<String> list = ImmutableList.of("test", "test2", "test3");
        final Iterable<String> filteredIterable = new FilteringIterable<>(list, new NoFilter<>());

        assertThat(Iterables.size(filteredIterable), is(3));
    }

    @Test
    public void emptyList() {
        final ImmutableList<String> list = ImmutableList.of();
        final Iterable<String> filteredIterable = new FilteringIterable<>(list, new NoFilter<>());

        assertThat(Iterables.size(filteredIterable), is(0));
    }

    @Test
    public void filterAll() {
        final ImmutableList<String> list = ImmutableList.of();
        final Iterable<String> filteredIterable = new FilteringIterable<>(list, new FilterAll<>());

        assertThat(Iterables.size(filteredIterable), is(0));
    }

    @Test
    public void filterSome() {
        final ImmutableList<Integer> list = ImmutableList.of(3,4,5,6);
        final Iterable<Integer> filteredIterable = new FilteringIterable<>(list, new FilteringIterable.FilterFunction<Integer>() {
            @Override
            public boolean filter(final Integer item) {
                return item % 2 == 0;
            }
        });

        assertThat(Iterables.size(filteredIterable), is(2));
        final Iterator<Integer> integerIterator = filteredIterable.iterator();
        assertThat(integerIterator.next(), is(3));
        assertThat(integerIterator.next(), is(5));
        assertFalse(integerIterator.hasNext());
    }

    private class NoFilter<T> implements FilteringIterable.FilterFunction<T> {
        @Override
        public boolean filter(final T item) {
            return false;
        }
    }

    private class FilterAll<T> implements FilteringIterable.FilterFunction<T> {
        @Override
        public boolean filter(final T item) {
            return true;
        }
    }
}
