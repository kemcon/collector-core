/* Copyright 2019-2020 Norconex Inc.
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
package com.norconex.collector.core.cmdline;

import java.io.PrintWriter;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.norconex.collector.core.Collector;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.PicocliException;
import picocli.CommandLine.Spec;

/**
 * Encapsulates command line arguments when running the Collector from
 * a command prompt.
 * @author Pascal Essiembre
 * @since 2.0.0
 */
@Command(
    name = "<app>",
    description = "%nOptions:",
    descriptionHeading = "%n<app> is the executable program used to "
            + "launch me%n",
    sortOptions = false,
    separator = " ",
    commandListHeading = "%nCommands:%n",
    footerHeading = "%nExamples:%n",
    footer = "%n  Start the Collector:%n"
           + "%n    <app> start -config=/path/to/config.xml%n"
           + "%n  Stop the Collector:%n"
           + "%n    <app> stop -config=/path/to/config.xml%n"
           + "%n  Get usage help on \"check\" command:%n"
           + "%n    <app> help configcheck%n",
    subcommands = {
        HelpCommand.class,
        StartCommand.class,
        StopCommand.class,
        ConfigCheckCommand.class,
        ConfigRenderCommand.class,
        CleanCommand.class,
//        CommitCommand.class,
        StoreExportCommand.class,
        StoreImportCommand.class
    }
)
public class CollectorCommand
        implements Callable<Integer>, IExecutionExceptionHandler {

//    public static final String NORCONEX =
//            " _   _  ___  ____   ____ ___  _   _ _______  __%n"
//          + "| \\ | |/ _ \\|  _ \\ / ___/ _ \\| \\ | | ____\\ \\/ /%n"
//          + "|  \\| | | | | |_) | |  | | | |  \\| |  _|  \\  / %n"
//          + "| |\\  | |_| |  _ <| |__| |_| | |\\  | |___ /  \\ %n"
//          + "|_| \\_|\\___/|_| \\_\\\\____\\___/|_| \\_|_____/_/\\_\\%n%n"
//          + "%n";

    private final Collector collector;

    @Option(
        names = {"-h", "-help"},
        usageHelp = true,
        description = "Show this help message and exit"
    )
    private boolean help;
    @Option(
        names = {"-v", "-version"},
        description = "Show the Collector version and exit"
    )
    private boolean version;

    @Spec
    private CommandSpec spec;

    public CollectorCommand(Collector collector) {
        super();
        this.collector = collector;
    }

    Collector getCollector() {
        return collector;
    }

    @Override
    public Integer call() throws Exception {
        if (version) {
//            out.println(Collector.getReleaseVersions());

            PrintWriter out = spec.commandLine().getOut();
//            out.println(Collector.NORCONEX_ASCII);
//            out.println("Version of the Collector and key components:");
//            out.println();
//
            out.println(collector.getReleaseVersions());

           // collector.getReleaseVersions().stream().forEach(out::println);
            System.exit(0);
        }
        return 0;
    }

    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine,
            ParseResult parseResult) throws Exception {
        if (ex instanceof PicocliException
                || ex instanceof IllegalArgumentException) {
            commandLine.getErr().println(ex.getMessage());
            commandLine.getErr().println();
            commandLine.usage(commandLine.getErr());
            return -1;
        }
        throw ex;
    }

    @Override
    public boolean equals(final Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(
                this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
