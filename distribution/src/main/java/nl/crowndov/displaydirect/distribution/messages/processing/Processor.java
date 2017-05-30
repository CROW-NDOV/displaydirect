package nl.crowndov.displaydirect.distribution.messages.processing;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public interface Processor<T> {

    public T process (T input);
}
