/* Copyright 2014-2020 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.collector.core.pipeline.committer;

import com.norconex.collector.core.pipeline.DocumentPipelineContext;
import com.norconex.commons.lang.pipeline.IPipelineStage;

/**
 * Common pipeline stage for committing documents.
 * @author Pascal Essiembre
 */
public class CommitModuleStage
        implements IPipelineStage<DocumentPipelineContext> {
    @Override
    public boolean execute(DocumentPipelineContext ctx) {

        // Event triggered by service
        ctx.getCommitterService().upsert(ctx.getDocument());

        return true;
    }
}