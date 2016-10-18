/*
 * *****************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3cli.exceptions;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ArgumentExceptionHandler implements Ds3ExceptionHandler<Exception> {

    private final static Logger LOG = LoggerFactory.getLogger(ArgumentExceptionHandler.class);

    public static void register() {
        CommandExceptionFactory.getInstance().addHandler(BadArgumentException.class, new ArgumentExceptionHandler());
        CommandExceptionFactory.getInstance().addHandler(MissingOptionException.class, new ArgumentExceptionHandler());
        CommandExceptionFactory.getInstance().addHandler(UnrecognizedOptionException.class, new ArgumentExceptionHandler());
    }

    public void handle(final String locationDescription, final Exception e, final boolean throwRuntimeException) {
        final StringBuilder description = new StringBuilder(locationDescription);
        description.append(" failed (");
        description.append(e.getClass().getSimpleName());
        description.append("): ");
        description.append(e.getMessage());
        LOG.info(description.toString(), e);
        if (throwRuntimeException) {
            throw new RuntimeException(description.toString(), e);
        } else {
            System.out.println(description.toString());
        }
    }

}
