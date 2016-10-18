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

package com.spectralogic.ds3cli.command;

import com.spectralogic.ds3cli.Arguments;
import com.spectralogic.ds3cli.View;
import com.spectralogic.ds3cli.ViewType;
import com.spectralogic.ds3cli.exceptions.CommandException;
import com.spectralogic.ds3cli.exceptions.CommandExceptionFactory;
import com.spectralogic.ds3cli.models.GetObjectsOnTapeResult;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.utils.Guard;
import org.apache.commons.cli.MissingOptionException;

import java.io.IOException;


public class GetObjectsOnTape extends CliCommand<GetObjectsOnTapeResult> {

    // Barcode or tape ID
    private String tapeId;

    public GetObjectsOnTape() {
    }

    @Override
    public CliCommand init(final Arguments args) throws Exception {
        this.tapeId = args.getId();
        if (Guard.isStringNullOrEmpty(this.tapeId)) {
            throw new MissingOptionException("The get_objects_on_tape command requires '-i' to be set with the tape Id or barcode");
        }
        return this;
    }

    @Override
    public GetObjectsOnTapeResult call() {
        try {

            final GetBlobsOnTapeSpectraS3Response response
                        = getClient().getBlobsOnTapeSpectraS3(new GetBlobsOnTapeSpectraS3Request(null, this.tapeId));

            return new GetObjectsOnTapeResult(this.tapeId, response.getBulkObjectListResult().getObjects().iterator());
        } catch (final IOException e) {
            if (CommandExceptionFactory.hasStatusCode(e, 404)) {
                CommandExceptionFactory.getInstance().handleException(this.getClass().getSimpleName(),
                        new CommandException("Unknown tape '" + this.tapeId +"'", e), true);
            } else {
                CommandExceptionFactory.getInstance().handleException(this.getClass().getSimpleName(), e, true);
            }
            return null;
        }
    }

    @Override
    public View<GetObjectsOnTapeResult> getView(final ViewType viewType) {
        if (viewType == ViewType.JSON) {
            return new com.spectralogic.ds3cli.views.json.GetObjectsOnTapeView();
        }
        return new com.spectralogic.ds3cli.views.cli.GetObjectsOnTapeView();
    }
}
    
