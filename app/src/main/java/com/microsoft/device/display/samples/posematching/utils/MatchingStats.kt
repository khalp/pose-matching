package com.microsoft.device.display.samples.posematching.utils

import com.microsoft.device.display.samples.posematching.R
import kotlin.math.abs

data class MatchingStats(
    private val elbowDiff: Pair<Float?, Float?>,
    private val shoulderDiff: Pair<Float?, Float?>,
    private val hipDiff: Pair<Float?, Float?>,
    private val kneeDiff: Pair<Float?, Float?>,
) {
    /**
     * Calculates the average percentage accuracy of matching for all joints that were not
     * skipped/were in frame (if all were skipped/not in frame, returns null)
     */
    fun calculateOverallScore(): Float? {
        var count = 0f
        var total = 0f
        for (pct in calculateDetailScore()) {
            pct.first?.let {
                count++
                total += it
            }
            pct.second?.let {
                count++
                total += it
            }
        }

        // if all joints were skipped or out of frame, score is 0
        return if ( total > 0f)
            total / count
        else
            null
    }

    /**
     * Calculates and returns a list of the percentage accuracy of matching for each joint
     * (or null if the joint was skipped/out of frame)
     */
    fun calculateDetailScore(): List<Pair<Float?, Float?>> {
        val elbowPct = jointDiff(elbowDiff)
        val shoulderPct = jointDiff(shoulderDiff)
        val hipPct = jointDiff(hipDiff)
        val kneePct = jointDiff(kneeDiff)

        return listOf(elbowPct, shoulderPct, hipPct, kneePct)
    }

    private fun jointDiff(diff: Pair<Float?, Float?>): Pair<Float?, Float?> {
        val first = diff.first?.let {percentFromDiff(it)}
        val second = diff.second?.let {percentFromDiff(it)}

        return Pair(first, second)
    }

    // REVISIT: not sure if 180 makes sense
    private fun percentFromDiff(diff: Float): Float {
        return (100f) * (1f - (abs(diff) / 180f))
    }

    /**
     * Returns a string with all of the joint angle differences
     *
     * @param getS: getString with just the @StringRes id as a arguments
     * @param getSWithS: getString with the @StringRes id and a String as arguments
     * @param getSWithSF: getString with the @StringRes id, a String, and a Float as arguments
     */
    fun outputAngleDifferences(
        getS: (Int) -> String,
        getSWithS: (Int, String) -> String,
        getSWithSF: (Int, String, Float) -> String
    ): String {
        val output = StringBuilder()
        output.append(getS(R.string.matching_stats))
        output.append(jointString(R.string.elbow, elbowDiff, getS, getSWithS, getSWithSF))
        output.append(jointString(R.string.shoulder, shoulderDiff, getS, getSWithS, getSWithSF))
        output.append(jointString(R.string.hip, hipDiff, getS, getSWithS, getSWithSF))
        output.append(jointString(R.string.knee, kneeDiff, getS, getSWithS, getSWithSF))
        return output.toString()
    }

    private fun jointString(
        jointStringId: Int,
        jointDiff: Pair<Float?, Float?>,
        getStringRes: (Int) -> String,
        getStringResWithStringArg: (Int, String) -> String,
        getStringResWithStringFloatArgs: (Int, String, Float) -> String,
    ): String {
        val output = StringBuilder()
        output.append(jointStringOneSide(
            jointStringId,
            jointDiff.first,
            getStringRes,
            getStringResWithStringArg,
            getStringResWithStringFloatArgs
        ))
        output.append('\t')
        output.append(jointStringOneSide(
            jointStringId,
            jointDiff.second,
            getStringRes,
            getStringResWithStringArg,
            getStringResWithStringFloatArgs
        ))
        output.append('\n')
        return output.toString()
    }

    private fun jointStringOneSide(
        jointStringId: Int,
        jointDiff: Float?,
        getStringRes: (Int) -> String,
        getStringResWithStringArg: (Int, String) -> String,
        getStringResWithStringFloatArgs: (Int, String, Float) -> String,
    ): String {
        if (jointDiff == null) {
            return getStringResWithStringArg(
                R.string.angle_skipped,
                getStringRes(jointStringId)
            )
        }

        return getStringResWithStringFloatArgs(
            R.string.angle_difference,
            getStringRes(R.string.elbow),
            jointDiff,
        )
    }

    override fun toString(): String {
        val output = StringBuilder()
        output.append(elbowDiff.first).append('\t').append(elbowDiff.second).appendLine()
        output.append(shoulderDiff.first).append('\t').append(shoulderDiff.second).appendLine()
        output.append(hipDiff.first).append('\t').append(hipDiff.second).appendLine()
        output.append(kneeDiff.first).append('\t').append(kneeDiff.second).appendLine()
        return output.toString()
    }
}
