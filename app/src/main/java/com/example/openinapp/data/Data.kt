```java
// Folder Name: data
// File Name: Data.java

import com.example.openinapp.data.model.RecentLink;
import com.example.openinapp.data.model.TopLink;

/**
 * Data class to hold the overall URL chart, recent links, and top links.
 */
public data class Data(
    /**
     * Map of URL to the number of times it has been opened.
     */
    private val overallUrlChart: Map<String, Int>,

    /**
     * List of recent links.
     */
    private val recentLinks: MutableList<RecentLink>,

    /**
     * List of top links.
     */
    private val topLinks: MutableList<TopLink>
)
```