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

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;

public class PrefixedFileObjectPutter implements Ds3ClientHelpers.ObjectChannelBuilder {

    private final static Logger LOG = LoggerFactory.getLogger(PrefixedFileObjectPutter.class);

    final private LoggingFileObjectPutter objectPutter;
    final private String prefix;

    public PrefixedFileObjectPutter(final Path inputDirectory, final String prefix) {
        this.objectPutter = new LoggingFileObjectPutter(inputDirectory);
        this.prefix = prefix;
    }

    @Override
    public SeekableByteChannel buildChannel(final String fileName) throws IOException {
        final String objectName = removePrefix(fileName);
        return this.objectPutter.buildChannel(objectName);
    }

    private String removePrefix(final String fileName) {
        if (this.prefix == null) {
            return fileName;
        } else {
            if (!fileName.startsWith(this.prefix)) {
                LOG.info("The object ({}) does not begin with prefix {}.  Ignoring adding the prefix.", fileName,  this.prefix);
                return fileName;
            } else {
                return fileName.substring(this.prefix.length());
            }
        }
    }

    static class LoggingFileObjectPutter implements Ds3ClientHelpers.ObjectChannelBuilder {
        final private FileObjectPutter objectPutter;

        private LoggingFileObjectPutter(final Path inputDirectory) {
            this.objectPutter = new FileObjectPutter(inputDirectory);
        }

        @Override
        public SeekableByteChannel buildChannel(final String s) throws IOException {
            LOG.info("Putting {} to ds3 endpoint", s);
            return this.objectPutter.buildChannel(s);
        }
    }

}
