package CLI

class Book {
    String googleId

    // Basic metadata
    String title
    String authors
    String description
    String categories

    String thumbnailUrl
    String previewLink

    String  publishedDateRaw
    Integer pageCount

    Date dateCreated
    Date lastUpdated

    static constraints = {
        googleId blank: false, unique: true
        title    blank: false

        authors         nullable: true
        description     nullable: true, maxSize: 10000
        categories      nullable: true

        thumbnailUrl    nullable: true
        previewLink     nullable: true

        publishedDateRaw nullable: true
        pageCount        nullable: true, min: 0
    }

    static mapping = {
        description   type: 'text'
        categories    type: 'text'
        authors       type: 'text'
        thumbnailUrl  type: 'text'
        previewLink   type: 'text'
    }

    String toString() {
        title ?: "Book #${id}"
    }
}
