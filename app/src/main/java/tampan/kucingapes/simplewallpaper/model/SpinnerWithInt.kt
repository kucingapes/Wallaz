package tampan.kucingapes.simplewallpaper.model

data class SpinnerWithInt(var title: String,
                          var interval: Int) {

    override fun toString(): String {
        return title
    }
}