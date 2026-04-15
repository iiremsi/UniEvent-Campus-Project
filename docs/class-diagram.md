# UML Class Diagram

```mermaid
classDiagram

    User "1" -- "0..*" EventPost : creates
    User "1" -- "0..*" Comment : writes
    User "1" -- "0..*" Like : gives
    EventPost "1" -- "0..*" Comment : has
    EventPost "1" -- "0..*" Like : receives
    User --> Role : has

    class User {
        -Long id
        -String username
        -String email
        -String passwordHash
        -Role role
        +register()
        +login()
    }

    class EventPost {
        -Long id
        -User author
        -String content
        -String eventTitle
        -String eventLocation
        -LocalDateTime eventDate
        -int likeCount
        -int commentCount
        +createPost()
        +updatePost()
        +deletePost()
    }

    class Comment {
        -Long id
        -User user
        -EventPost post
        -String content
        +addComment()
    }

    class Like {
        -Long id
        -User user
        -EventPost post
        +toggleLike()
    }

    class Role {
        <<Enumeration>>
        STUDENT
        CLUB
        ADMIN
    }
