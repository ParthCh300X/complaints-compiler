package udemy.appdev.complaintscompiler

data class Complaints(
    val id: String = "",
    val userId: String = "",
    val problemTitle: String ="",
    val category: String ="",
    val description: String ="",
    val locationText: String ="",
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis()
)
