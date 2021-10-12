package com.microsoft.device.display.samples.posematching.utils

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.abs
import kotlin.math.atan2

// Reference: https://developers.google.com/ml-kit/vision/pose-detection/classifying-poses#expandable-1

// REVISIT: add checks to "in frame likelihood?" can add warnings like "make sure you're in frame"
private const val inFrameThreshold = 0.8f


fun comparePoses(
    skipElbows: Boolean,
    skipShoulders: Boolean,
    skipHips: Boolean,
    skipKnees: Boolean,
    reference: Pose,
    toCompare: Pose
): Boolean {
    val elbows = skipElbows || compareElbowAngles(reference, toCompare)
    val shoulders = skipShoulders || compareShoulderAngles(reference, toCompare)
    val hips = skipHips || compareHipAngles(reference, toCompare)
    val knees = skipKnees || compareKneeAngles(reference, toCompare)

    return elbows && shoulders && hips && knees
}

private fun compareElbowAngles(reference: Pose, toCompare: Pose): Boolean {
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

private fun compareShoulderAngles(reference: Pose, toCompare: Pose): Boolean {
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

private fun compareHipAngles(reference: Pose, toCompare: Pose): Boolean {
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

private fun compareKneeAngles(reference: Pose, toCompare: Pose): Boolean {
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
): Boolean {
    val left = if (checkJointAnglesInFrame(leftLandmarks, reference))
        compareJointAngle(leftLandmarks, reference, toCompare)
    else true

    val right = if (checkJointAnglesInFrame(rightLandmarks, reference))
        compareJointAngle(rightLandmarks, reference, toCompare)
    else true

    Log.d("PoseComparator", "${joint}s matching? left: $left right: $right")
    return left && right
}

private fun checkJointAnglesInFrame(landmarks: List<Int>, reference: Pose): Boolean {
    var inFrame = true
    for (landmark in landmarks) {
        val landmarkInFrame = isInFrame(reference, landmark)
        inFrame = inFrame && landmarkInFrame
        Log.d("Pose Comparator", "landmark: $landmark in frame? $landmarkInFrame")
    }
    Log.d("Pose Comparator", "overall joint in frame? $inFrame")
    return inFrame
}

private fun isInFrame(pose: Pose, landmark: Int): Boolean {
    return pose.getPoseLandmark(landmark)!!.inFrameLikelihood >= inFrameThreshold
}

private fun compareJointAngle(landmarks: List<Int>, reference: Pose, toCompare: Pose): Boolean {
    check(landmarks.size == 3) { "Invalid landmark array size ${landmarks.size}, should be 3" }
    return compareAngle(getJointAngle(reference, landmarks), getJointAngle(toCompare, landmarks))
}

private fun compareAngle(first: Double, second: Double, threshold: Float = 30f): Boolean {
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
