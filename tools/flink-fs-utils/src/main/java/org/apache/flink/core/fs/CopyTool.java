package org.apache.flink.core.fs;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.plugin.PluginManager;
import org.apache.flink.core.plugin.PluginUtils;
import org.apache.flink.util.FileUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A tool for copying files between Flink file systems.
 */
public class CopyTool {
    private static final Option PROPERTY_OPTION = Option.builder("D")
            .argName("property=value")
            .numberOfArgs(2)
            .valueSeparator('=')
            .desc("Flink configuration property")
            .build();

    public static void main(String[] args) {
        final DefaultParser parser = new DefaultParser();
        final CommandLine commandLine;
        try {
            commandLine = parser.parse(getParserOptions(), args, true);
        } catch (ParseException e) {
            printUsageAndExit();
            return;
        }

        final Properties properties =
                commandLine.getOptionProperties(PROPERTY_OPTION.getOpt());
        initialize(properties);

        final String[] values = commandLine.getArgs();
        for (String value : values) {
            if (value.startsWith("-")) {
                System.out.printf("unknown option: %s%n", value);
                printUsageAndExit();
                return;
            }
        }
        if (values.length < 2) {
            printUsageAndExit();
            return;
        }
        copy(Arrays.asList(values).subList(0, values.length - 1), values[values.length - 1]);
    }

    private static Options getParserOptions() {
        final Options options = new Options();
        options.addOption(PROPERTY_OPTION);
        return options;
    }

    public static void printUsage() {
        final PrintWriter pw = new PrintWriter(System.out);
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.setLeftPadding(5);
        String cmdLineSyntax = String.format("%s [OPTIONS] SRC [SRC]... DST",
                CopyTool.class.getName());
        formatter.printUsage(pw, formatter.getWidth(), cmdLineSyntax);
        formatter.printWrapped(pw, formatter.getWidth(), "options:");
        formatter.printOptions(pw, formatter.getWidth(), getParserOptions(),
                formatter.getLeftPadding(), formatter.getDescPadding());
        pw.flush();
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(1);
    }

    public static void initialize(Properties properties) {
        final String configDir = System.getenv(ConfigConstants.ENV_FLINK_CONF_DIR);
        if (configDir == null) {
            throw new RuntimeException(String.format("Missing environment variable: %s",
                    ConfigConstants.ENV_FLINK_CONF_DIR));
        }

        final Configuration dynamicConfiguration = new Configuration();
        final Set<String> propertyNames = properties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            dynamicConfiguration.setString(propertyName, properties.getProperty(propertyName));
        }

        final Configuration configuration = GlobalConfiguration.loadConfiguration(configDir, dynamicConfiguration);
        PluginManager pluginManager =
                PluginUtils.createPluginManagerFromRootFolder(configuration);
        FileSystem.initialize(configuration, pluginManager);
    }

    public static void copy(List<String> src, String dst) {
        final List<Path> paths = src.stream()
                .map(Path::new)
                .collect(Collectors.toList());
        for (Path path : paths) {
            final Path target = new Path(dst, path.getName());
            try {
                FileUtils.copy(path, target, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
