**Folder Name:** com.example.openinapp.utils

**File Name:** DrawableUtils.kt

```kotlin
// Folder Name: com.example.openinapp.utils
// File Name: DrawableUtils.kt

// This class contains utility functions for creating gradient drawables.
object DrawableUtils {
    // Function to create a gradient drawable with the specified start and end colors.
    fun createGradientDrawable(startColor: Int, endColor: Int): GradientDrawable {
        // Create a new GradientDrawable with a top-to-bottom orientation.
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            // Set the colors of the gradient.
            intArrayOf(startColor, endColor)
        )
    }
}
```