package com.microsoft.device.display.samples.posematching.utils

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.abs
import kotlin.math.atan2

// Reference: https://developers.google.com/ml-kit/vision/pose-detection/classifying-poses#expandable-1

// REVISIT: add checks to "in frame likelihood?" can add warnings like "make sure you're in frame"

fun comparePoses(reference: Pose, toCompare: Pose): Boolean {
    return compareElbowAngles(reference, toCompare)
            && compareShoulderAngles(reference, toCompare)
            && compareHipAngles(reference, toCompare)
            && compareKneeAngles(reference, toCompare)
}

private fun compareElbowAngles(reference: Pose, toCompare: Pose): Boolean {
    val left = compareAngle(leftElbowAngle(reference), leftElbowAngle(toCompare))
    val right = compareAngle(rightElbowAngle(reference), rightElbowAngle(toCompare))
    Log.d("PoseComparator", "Elbows matching? left: $left right: $right")
    return left && right
}

private fun compareShoulderAngles(reference: Pose, toCompare: Pose): Boolean {
    val left = compareAngle(leftShoulderAngle(reference), leftShoulderAngle(toCompare))
    val right = compareAngle(rightShoulderAngle(reference), rightShoulderAngle(toCompare))
    Log.d("PoseComparator", "Shoulders matching? left: $left right: $right")
    return left && right
}

private fun compareHipAngles(reference: Pose, toCompare: Pose): Boolean {
    val left = compareAngle(leftHipAngle(reference), leftHipAngle(toCompare))
    val right = compareAngle(rightHipAngle(reference), rightHipAngle(toCompare))
    Log.d("PoseComparator", "Hips matching? left: $left right: $right")
    return left && right
}

private fun compareKneeAngles(reference: Pose, toCompare: Pose): Boolean {
    val left = compareAngle(leftKneeAngle(reference), leftKneeAngle(toCompare))
    val right = compareAngle(rightKneeAngle(reference), rightKneeAngle(toCompare))
    Log.d("PoseComparator", "Knees matching? left: $left right: $right")
    return left && right
}

private fun compareAngle(first: Double, second: Double, threshold: Float = 30f): Boolean {
    val returnVal = abs(first - second) < threshold
    Log.d("PoseComparator", "Reference: $first Attempt: $second Matching? $returnVal")
    return returnVal
}

private fun leftKneeAngle(pose: Pose): Double {
    return getPoseAngle(
        pose,
        PoseLandmark.LEFT_ANKLE,
        PoseLandmark.LEFT_KNEE,
        PoseLandmark.LEFT_HIP
    )
}

private fun rightKneeAngle(pose: Pose): Double {
    return getPoseAngle(
        pose,
        PoseLandmark.RIGHT_ANKLE,
        PoseLandmark.RIGHT_KNEE,
        PoseLandmark.RIGHT_HIP
    )
}

private fun leftElbowAngle(pose: Pose): Double {
    return getPoseAngle(
        pose,
        PoseLandmark.LEFT_WRIST,
        PoseLandmark.LEFT_ELBOW,
        PoseLandmark.LEFT_ELBOW
    )
}

private fun rightElbowAngle(pose: Pose): Double {
    return getPoseAngle(
        pose,
        PoseLandmark.RIGHT_WRIST,
        PoseLandmark.RIGHT_ELBOW,
        PoseLandmark.RIGHT_ELBOW
    )
}

private fun leftShoulderAngle(pose: Pose): Double {
    return getPoseAngle(
        pose,
        PoseLandmark.LEFT_ELBOW,
        PoseLandmark.LEFT_SHOULDER,
        PoseLandmark.LEFT_HIP
    )
}

private fun rightShoulderAngle(pose: Pose): Double {
    return getPoseAngle(
        pose,
        PoseLandmark.RIGHT_ELBOW,
        PoseLandmark.RIGHT_SHOULDER,
        PoseLandmark.RIGHT_HIP
    )
}

private fun leftHipAngle(pose: Pose): Double {
    return getPoseAngle(
        pose,
        PoseLandmark.LEFT_SHOULDER,
        PoseLandmark.LEFT_HIP,
        PoseLandmark.LEFT_KNEE
    )
}

private fun rightHipAngle(pose: Pose): Double {
    return getPoseAngle(
        pose,
        PoseLandmark.RIGHT_SHOULDER,
        PoseLandmark.RIGHT_HIP,
        PoseLandmark.RIGHT_KNEE
    )
}

private fun getPoseAngle(pose: Pose, landmark1: Int, landmark2: Int, landmark3: Int): Double {
    return getAngle(
        pose.getPoseLandmark(landmark1)!!,
        pose.getPoseLandmark(landmark2)!!,
        pose.getPoseLandmark(landmark3)!!
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
