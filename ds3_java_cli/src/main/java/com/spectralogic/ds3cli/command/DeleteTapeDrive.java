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

package com.spectralogic.ds3cli.command;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3cli.Arguments;
import com.spectralogic.ds3cli.models.DefaultResult;
import com.spectralogic.ds3client.commands.spectrads3.DeleteTapeDriveSpectraS3Request;
import org.apache.commons.cli.Option;

import java.util.UUID;

import static com.spectralogic.ds3cli.ArgumentFactory.ID;

public class DeleteTapeDrive extends CliCommand<DefaultResult> {

    private final static ImmutableList<Option> requiredArgs = ImmutableList.of(ID);

    private String id;

    public DeleteTapeDrive() {
    }

    @Override
    public CliCommand init(final Arguments args) throws Exception {
        processCommandOptions(requiredArgs, EMPTY_LIST, args);

        this.id = args.getId();
        return this;
    }

    @Override
    public DefaultResult call() throws Exception {
        getClient().deleteTapeDriveSpectraS3(new DeleteTapeDriveSpectraS3Request(UUID.fromString(this.id)));
        return new DefaultResult("Success: Deleted tape drive '" + this.id+ "'.");
    }
}
