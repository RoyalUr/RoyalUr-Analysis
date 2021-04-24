package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.analysis.reporting.ReportFormatter;
import com.sothatsit.royalur.analysis.targets.*;

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
            new UtilityFnsTarget()
    };

    public static void main(String[] args) {
        // Check that the user gave some targets to run.
        if (args.length == 0) {
            printHelp();
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
            printAvailableTargets();
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
        System.err.println("Usage of RoyalUrAnalysis CLI:");
        System.err.println("   java -jar RoyalUrAnalysis.jar <target-name>");
    }

    /** Prints all of the available targets to STDERR. **/
    public static void printAvailableTargets() {
        System.err.println("Available Targets:");
        for (Target target : TARGETS) {
            System.err.println("   " + target.name + " : " + target.description);
        }
    }
}
