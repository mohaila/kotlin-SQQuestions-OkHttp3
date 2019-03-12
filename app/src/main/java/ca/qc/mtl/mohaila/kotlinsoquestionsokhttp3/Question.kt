package ca.qc.mtl.mohaila.kotlinsoquestionsokhttp3

class Question(val title: String, val link: String) {
    override fun toString(): String {
        return title
    }
}

class Questions(val items: ArrayList<Question>)