Sure. Here's the documentation for the provided codebase:

```
// Folder Name: data/model

// File Name: RecentLink.kt

// Represents a recent link in the database.
data class RecentLink(
    // The app that the link was opened in.
    val app: String,

    // The time when the link was created.
    val created_at: String,

    // The ID of the domain that the link belongs to.
    val domain_id: String,

    // The original image associated with the link.
    val original_image: String,

    // The smart link that was generated for the link.
    val smart_link: String,

    // The thumbnail associated with the link.
    val thumbnail: Any,

    // The time ago that the link was created, in a human-readable format.
    val times_ago: String,

    // The title of the link.
    val title: String,

    // The total number of clicks that the link has received.
    val total_clicks: Int,

    // The ID of the URL that the link is associated with.
    val url_id: Int,

    // The prefix of the URL that the link is associated with.
    val url_prefix: Any,

    // The suffix of the URL that the link is associated with.
    val url_suffix: String,

    // The web link that the link is associated with.
    val web_link: String
)
```