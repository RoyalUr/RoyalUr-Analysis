package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.analysis.reporting.ReportFormatter;
import com.sothatsit.royalur.analysis.targets.*;
import com.sothatsit.royalur.analysis.ui.AnalysisWindow;

/**
 * The main entry-point to this Royal Ur Analysis program.
 *
 * @author Paddy Lamont
 */
public class Main {

    public static final Target[] TARGETS = {
            new BenchmarkTarget(),
            new FirstMoveAdvantageTarget(),
            new LuckTarget(),
            new RoyalUrNetTarget(),
            new UtilityFnsTarget(),
            new DiegoTarget(),
            new AlbanTarget(),
            new MLDataGatheringTarget(),
            new ExpectimaxMLTarget(),
    };

    public static void main(String[] args) {
        // By default, start the analysis window.
        if (args.length == 0) {
            startAnalysisWindow();
            return;
        }

        // Check if the user is running the analysis window.
        if ("analysis".equalsIgnoreCase(args[0])) {
            if (args.length != 1) {
                printUsage();
                return;
            }

            startAnalysisWindow();
            return;
        }

        // Find the targets to run.
        Target[] targets = Target.find(args, TARGETS);
        boolean missingAnyTargets = false;
        for (int index = 0; index < args.length; ++index) {
            Target target = targets[index];
            if (target == null) {
                System.err.println("Unknown target \"" + args[0] + "\"");
                missingAnyTargets = true;
            }
        }
        if (missingAnyTargets) {
            System.err.println();
            printHelp();
            return;
        }

        // Print out the targets to be run.
        if (targets.length > 1) {
            System.out.println("Preparing to run targets:");
            for (Target target : targets) {
                System.out.println(" - " + target.name + ": " + target.description);
            }
        }

        // Run the targets.
        TargetResult[] results = new TargetResult[targets.length];
        for (int index = 0; index < targets.length; ++index) {
            results[index] = runTarget(targets[index]);
        }

        // Print the results.
        for (TargetResult result : results) {
            System.out.println();
            result.print();
        }
    }

    public static void startAnalysisWindow() {
        new AnalysisWindow();
    }

    public static TargetResult runTarget(Target target) {
        // Run the target!
        System.out.println("\nRunning RoyalUrAnalysis target " + target.name + ":");
        System.out.println(" - " + target.description + "\n");
        System.out.println();

        long start = System.nanoTime();
        TargetResult result = target.run();
        String runDuration = ReportFormatter.formatSecDuration((System.nanoTime() - start) * 1e-9d);

        System.out.println();
        System.out.println("Finished RoyalUrAnalysis target " + target.name + " in " + runDuration);
        System.out.println();
        return result;
    }

    /** Prints the usage of the RoyalUrAnalysis CLI, and the available targets, to STDERR. **/
    public static void printHelp() {
        printUsage();
        System.err.println();
        printAvailableTargets();
    }

    /** Prints the usage of the RoyalUrAnalysis CLI to STDERR. **/
    public static void printUsage() {
        System.err.println("Start the analysis window:");
        System.err.println("   java -jar RoyalUrAnalysis.jar [analysis]");
        System.err.println();
        System.err.println("Run analysis targets:");
        System.err.println("   java -jar RoyalUrAnalysis.jar <target-name>");
    }

    /** Prints all available targets to STDERR. **/
    public static void printAvailableTargets() {
        System.err.println("Available Targets:");
        for (Target target : TARGETS) {
            System.err.println("   " + target.name + " : " + target.description);
        }
    }
}
