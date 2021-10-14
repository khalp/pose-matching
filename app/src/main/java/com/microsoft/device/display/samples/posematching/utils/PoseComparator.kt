package com.microsoft.device.display.samples.posematching.utils

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.abs
import kotlin.math.atan2

// Reference: https://developers.google.com/ml-kit/vision/pose-detection/classifying-poses#expandable-1

// thresholds
private const val inFrameThreshold = 0.8f
private const val angleThreshold = 30f

fun comparePoses(
    skipElbows: Boolean,
    skipShoulders: Boolean,
    skipHips: Boolean,
    skipKnees: Boolean,
    reference: Pose,
    toCompare: Pose
): MatchingStats {
    val elbows = if (skipElbows) Pair(null, null) else compareElbowAngles(reference, toCompare)
    val shoulders =
        if (skipShoulders) Pair(null, null) else compareShoulderAngles(reference, toCompare)
    val hips = if (skipHips) Pair(null, null) else compareHipAngles(reference, toCompare)
    val knees = if (skipKnees) Pair(null, null) else compareKneeAngles(reference, toCompare)

    val stats = MatchingStats(elbows, shoulders, hips, knees)
    Log.d("Pose Comparator", stats.toString())
    return stats
}

private fun compareElbowAngles(reference: Pose, toCompare: Pose): Pair<Float?, Float?> {
    val leftLandmarks = listOf(
        PoseLandmark.LEFT_WRIST,
        PoseLandmark.LEFT_ELBOW,
        PoseLandmark.LEFT_SHOULDER
    )
    val rightLandmarks = listOf(
        PoseLandmark.RIGHT_WRIST,
        PoseLandmark.RIGHT_ELBOW,
        PoseLandmark.RIGHT_SHOULDER
    )

    return compareLeftAndRightJointAngles(
        leftLandmarks,
        rightLandmarks,
        reference,
        toCompare,
        "elbow"
    )
}

private fun compareShoulderAngles(reference: Pose, toCompare: Pose): Pair<Float?, Float?> {
    val leftLandmarks = listOf(
        PoseLandmark.LEFT_ELBOW,
        PoseLandmark.LEFT_SHOULDER,
        PoseLandmark.LEFT_HIP
    )
    val rightLandmarks = listOf(
        PoseLandmark.RIGHT_ELBOW,
        PoseLandmark.RIGHT_SHOULDER,
        PoseLandmark.RIGHT_HIP
    )

    return compareLeftAndRightJointAngles(
        leftLandmarks,
        rightLandmarks,
        reference,
        toCompare,
        "shoulder"
    )
}

private fun compareHipAngles(reference: Pose, toCompare: Pose): Pair<Float?, Float?> {
    val leftLandmarks = listOf(
        PoseLandmark.LEFT_SHOULDER,
        PoseLandmark.LEFT_HIP,
        PoseLandmark.LEFT_KNEE
    )
    val rightLandmarks = listOf(
        PoseLandmark.RIGHT_SHOULDER,
        PoseLandmark.RIGHT_HIP,
        PoseLandmark.RIGHT_KNEE
    )

    return compareLeftAndRightJointAngles(
        leftLandmarks,
        rightLandmarks,
        reference,
        toCompare,
        "hip"
    )
}

private fun compareKneeAngles(reference: Pose, toCompare: Pose): Pair<Float?, Float?> {
    val leftLandmarks = listOf(
        PoseLandmark.LEFT_ANKLE,
        PoseLandmark.LEFT_KNEE,
        PoseLandmark.LEFT_HIP
    )
    val rightLandmarks = listOf(
        PoseLandmark.RIGHT_ANKLE,
        PoseLandmark.RIGHT_KNEE,
        PoseLandmark.RIGHT_HIP
    )

    return compareLeftAndRightJointAngles(
        leftLandmarks,
        rightLandmarks,
        reference,
        toCompare,
        "knee"
    )
}

private fun compareLeftAndRightJointAngles(
    leftLandmarks: List<Int>,
    rightLandmarks: List<Int>,
    reference: Pose,
    toCompare: Pose,
    joint: String
): Pair<Float?, Float?> {
    val left = if (isJointInFrame(leftLandmarks, reference))
        compareJointAngle(leftLandmarks, reference, toCompare)
    else null

    val right = if (isJointInFrame(rightLandmarks, reference))
        compareJointAngle(rightLandmarks, reference, toCompare)
    else null

    Log.d("PoseComparator", "${joint}s matching? left: $left right: $right")
    return Pair(left, right)
}

private fun isJointInFrame(landmarks: List<Int>, pose: Pose): Boolean {
    var inFrame = true
    for (landmark in landmarks) {
        val landmarkInFrame = isInFrame(pose, landmark)
        inFrame = inFrame && landmarkInFrame
        Log.d("Pose Comparator", "landmark: $landmark in frame? $landmarkInFrame")
    }
    Log.d("Pose Comparator", "overall joint in frame? $inFrame")
    return inFrame
}

private fun isInFrame(pose: Pose, landmark: Int): Boolean {
    return pose.getPoseLandmark(landmark)!!.inFrameLikelihood >= inFrameThreshold
}

private fun compareJointAngle(landmarks: List<Int>, reference: Pose, toCompare: Pose): Float? {
    check(landmarks.size == 3) { "Invalid landmark array size ${landmarks.size}, should be 3" }

    // Make sure user joint is in frame before comparing
    if (!isJointInFrame(landmarks, toCompare))
        return null

    // Return angle difference
    return compareAngle(getJointAngle(reference, landmarks), getJointAngle(toCompare, landmarks))
}

private fun compareAngle(
    first: Double,
    second: Double,
): Float {
    val returnVal = second - first
    Log.d("PoseComparator", "Reference: $first Attempt: $second Diff: $returnVal")
    return returnVal.toFloat()
}

private fun compareAngle(
    first: Double,
    second: Double,
    threshold: Float = angleThreshold
): Boolean {
    val returnVal = abs(first - second) < threshold
    Log.d("PoseComparator", "Reference: $first Attempt: $second Matching? $returnVal")
    return returnVal
}

private fun getJointAngle(pose: Pose, landmarks: List<Int>): Double {
    check(landmarks.size == 3) { "Invalid landmark array size ${landmarks.size}, should be 3" }
    return getAngle(
        pose.getPoseLandmark(landmarks[0])!!,
        pose.getPoseLandmark(landmarks[1])!!,
        pose.getPoseLandmark(landmarks[2])!!
    )
}

private fun getAngle(
    firstPoint: PoseLandmark,
    midPoint: PoseLandmark,
    lastPoint: PoseLandmark
): Double {
    var result = Math.toDegrees(
        (atan2(
            lastPoint.position.y - midPoint.position.y,
            lastPoint.position.x - midPoint.position.x
        )
                - atan2(
            firstPoint.position.y - midPoint.position.y,
            firstPoint.position.x - midPoint.position.x
        )
                ).toDouble()
    )
    result = abs(result) // Angle should never be negative
    if (result > 180) {
        result = 360.0 - result // Always get the acute representation of the angle
    }
    return result
}
