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

import com.norconex.collector.core.CollectorException;
import com.norconex.collector.core.crawler.CrawlerEvent;
import com.norconex.collector.core.pipeline.DocumentPipelineContext;
import com.norconex.committer.core3.CommitterException;
import com.norconex.committer.core3.ICommitter;
import com.norconex.committer.core3.UpsertRequest;
import com.norconex.commons.lang.pipeline.IPipelineStage;
import com.norconex.importer.doc.Doc;

/**
 * Common pipeline stage for committing documents.
 * @author Pascal Essiembre
 */
public class CommitModuleStage
        implements IPipelineStage<DocumentPipelineContext> {
    @Override
    public boolean execute(DocumentPipelineContext ctx) {
        ICommitter committer = ctx.getConfig().getCommitter();
        if (committer != null) {
            Doc doc = ctx.getDocument();
            try {
                committer.upsert(new UpsertRequest(
                        doc.getReference(),
                        doc.getMetadata(),
                        doc.getInputStream()));
            } catch (CommitterException e) {
                throw new CollectorException(
                        "Could not upsert document: " + doc.getReference(), e);
            }
        }
        ctx.fireCrawlerEvent(
                CrawlerEvent.DOCUMENT_COMMITTED_ADD,
                ctx.getDocInfo(), committer);
        return true;
    }
}