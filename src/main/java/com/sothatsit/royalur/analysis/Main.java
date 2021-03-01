package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.analysis.reporting.ReportFormatter;
import com.sothatsit.royalur.analysis.targets.BenchmarkTarget;
import com.sothatsit.royalur.analysis.targets.FirstMoveAdvantageTarget;
import com.sothatsit.royalur.analysis.targets.LuckTarget;
import com.sothatsit.royalur.analysis.targets.Target;

/**
 * The main entry-point to this Royal Ur Analysis program.
 *
 * @author Paddy Lamont
 */
public class Main {

    public static final Target[] TARGETS = {
            new BenchmarkTarget(),
            new FirstMoveAdvantageTarget(),
            new LuckTarget()
    };

    public static void main(String[] args) {
        // Check that the user gave the correct number of arguments.
        if (args.length != 1) {
            printHelp();
            return;
        }

        // Find the specified target to run.
        Target target = Target.find(args[0], TARGETS);
        if (target == null) {
            System.err.println("Unknown target \"" + args[0] + "\"");
            System.out.println();
            printAvailableTargets();
            return;
        }

        // Run the target!
        System.out.println("\nRunning RoyalUrAnalysis target " + target.name + ":");
        System.out.println(" - " + target.description + "\n");
        System.out.println();

        long start = System.nanoTime();
        target.run();
        String runDuration = ReportFormatter.formatSecDuration((System.nanoTime() - start) * 1e-9d);

        System.out.println();
        System.out.println("Finished RoyalUrAnalysis target " + target.name + " in " + runDuration);
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
