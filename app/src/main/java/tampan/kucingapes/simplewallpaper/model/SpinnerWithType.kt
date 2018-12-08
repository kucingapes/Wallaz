package tampan.kucingapes.simplewallpaper.model


data class SpinnerWithType(var layout: String,
                           var type: String) {
    override fun toString(): String {
        return layout
    }
}