package com.sothatsit.royalur.analysis.targets;

/**
 * The results of an analysis target.
 *
 * @author Paddy Lamont
 */
public abstract class TargetResult {

    public final Target target;

    protected TargetResult(Target target) {
        this.target = target;
    }

    public abstract void print();
}
