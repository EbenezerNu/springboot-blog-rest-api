# springboot-blog-rest-api
Spring boot blog REST API

This project has the following components;
- User
- Post
- Comment

However, there is an additional implementation that give room to comment on a comment, like we have in Tweeter and other social media.

Likewise, in displaying posts and comments, there is a version (in the URL) that shows replies on the comments, iteratively.


I have also created a versioning on the endpoints to display posts, with their comments, each with their replies, recursively.


#### Functionalities
The project makes use or URL versioning; but also have `/api` as the context path, before the versions.
Examples: `/api/v1/posts`

#### User Routes
Below are the various endpoints concerning user model:

- 