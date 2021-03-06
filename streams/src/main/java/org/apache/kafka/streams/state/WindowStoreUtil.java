/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.kafka.streams.state;

import java.nio.ByteBuffer;

public class WindowStoreUtil<K, V> {

    public static final int TIMESTAMP_SIZE = 8;
    public static final int SEQNUM_SIZE = 4;
    public static final Serdes<byte[], byte[]> INNER_SERDES = Serdes.withBuiltinTypes("", byte[].class, byte[].class);
    @SuppressWarnings("unchecked")
    public static final KeyValueIterator<byte[], byte[]>[] NO_ITERATORS = (KeyValueIterator<byte[], byte[]>[]) new KeyValueIterator[0];

    public static <K> byte[] toBinaryKey(K key, final long timestamp, final int seqnum, Serdes<K, ?> serdes) {
        byte[] serializedKey = serdes.rawKey(key);

        ByteBuffer buf = ByteBuffer.allocate(serializedKey.length + TIMESTAMP_SIZE + SEQNUM_SIZE);
        buf.put(serializedKey);
        buf.putLong(timestamp);
        buf.putInt(seqnum);

        return buf.array();
    }

    public static <K> K keyFromBinaryKey(byte[] binaryKey, Serdes<K, ?> serdes) {
        byte[] bytes = new byte[binaryKey.length - TIMESTAMP_SIZE - SEQNUM_SIZE];

        System.arraycopy(binaryKey, 0, bytes, 0, bytes.length);

        return serdes.keyFrom(bytes);
    }

    public static long timestampFromBinaryKey(byte[] binaryKey) {
        return ByteBuffer.wrap(binaryKey).getLong(binaryKey.length - TIMESTAMP_SIZE - SEQNUM_SIZE);
    }

}
