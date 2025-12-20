# No-Oops Blog Crawler

This project includes a blog crawler service that can fetch blog information from your website.

## Features

- Crawl blog posts from your website
- Extract title, date, categories, and tags
- REST API endpoints to access the data

## How to Use

### 1. Compile the Project

```bash
cd no-oops-main
mvn clean compile
```

### 2. Run the Application

```bash
cd no-oops-main-starter
mvn spring-boot:run
```

### 3. Access the API

Once the application is running, you can access the following endpoints:

- `GET /api/blog/posts` - Get all blog posts
- `GET /api/blog/post/detail?index={index}` - Get details of a specific post by index

### 4. Direct Usage

You can also use the [BlogCrawlerService](no-oops-main-application/src/main/java/cn/cccs7/service/BlogCrawlerService.java) directly in your code:

```java
BlogCrawlerService crawler = new BlogCrawlerService();
List<BlogPost> posts = crawler.crawlBlogPosts();
```

## Customization

To customize the crawler for your specific website structure, modify the CSS selectors in [BlogCrawlerService.java](no-oops-main-application/src/main/java/cn/cccs7/service/BlogCrawlerService.java).